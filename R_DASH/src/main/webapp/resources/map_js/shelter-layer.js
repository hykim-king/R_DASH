/* 중복 로드 가드: 스크립트가 두 번 로드되면 바로 종료 */
if (window.__ShelterLayerLoaded__) {
  console.warn('[ShelterLayer] duplicate load skipped');
} else {
  window.__ShelterLayerLoaded__ = true;

  (function (global) {
    'use strict';

    // AppMap 준비되면 init
    if (global.AppMap) init();
    else global.addEventListener('appmap:ready', init, { once: true });

    function init() {
      var App = global.AppMap;
      if (!App || !App.map) { setTimeout(init, 80); return; }
      var map = App.map;

      // ===== API =====
      var BASE = (document.body && document.body.getAttribute('data-context-path')) || '';
      var API  = {
        bbox:    BASE + '/shelters/bbox',
        one:     function(id){ return BASE + '/shelters/' + id; },
        sugAddr: BASE + '/shelters/suggest/adress', // 서버 경로 그대로(오타 포함)
        sugName: BASE + '/shelters/suggest/name'
      };

      // ===== 상태 =====
      var state = {
        active: false,
        q: '',                 // 검색창 텍스트
        category: '',          // 카테고리(지역명). ''이면 전체
        baseLimit: 6000,       // ▶ 표시량 복구: 전국 단위도 충분히 크게
        lastReq: 0,
        inflight: null,
        markers: [],           // 현재 지도에 보이는 Marker들
        markerMap: new Map(),  // id -> { marker, data }
        clusterer: null,
        dragging: false,

        // ★ 중복 리스너 방지용 참조 보관
        idleHandler: null,
        dragStartHandler: null,
        dragEndHandler: null
      };

      // ===== 튜닝 =====
      var LIST_MAX       = 300;   // 리스트 DOM 상한
      var FETCH_DEBOUNCE = 650;   // 지도 이동 후 서버 콜 디바운스
      var KOREA_BOUNDS   = { minLat: 33, maxLat: 39.5, minLon: 124, maxLon: 132 }; // 전국 조회 범위

      // ===== 유틸 =====
      function debounce(fn, ms) { var t; return function(){ var a=arguments,c=this; clearTimeout(t); t=setTimeout(function(){ fn.apply(c,a); }, ms); }; }
      function escapeHtml(s){ return (s||'').replace(/[&<>"']/g, function(m){ return ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[m]); }); }
      function getBBox(){ return (App && App.getBBox) ? App.getBBox() : null; }
      function norm(s){ return String(s||'').replace(/\s+/g,'').toLowerCase(); }
      function once(target, type, handler){
        var wrapped = function(){
          try { handler.apply(null, arguments); } finally { kakao.maps.event.removeListener(target, type, wrapped); }
        };
        kakao.maps.event.addListener(target, type, wrapped);
        return wrapped;
      }

      // 정확 포커싱: setLevel → setCenter(즉시) → idle/tilesloaded에서 다중 보정
      function focusToExact(lat, lon, level){
        var pos = new kakao.maps.LatLng(Number(lat), Number(lon));
        var targetLevel = (typeof level === 'number') ? level : 4; // 더 가깝게
        try {
          map.setLevel(targetLevel, { animate: true });
          map.setCenter(pos);                  // 1차 고정
          once(map, 'tilesloaded', function(){ try { map.setCenter(pos); } catch(_) {} }); // 2차
          once(map, 'idle',        function(){ try { map.setCenter(pos); } catch(_) {} }); // 3차
          setTimeout(function(){ try { map.setCenter(pos); } catch(_) {} }, 350);          // 4차 안전보정
        } catch(_) {}
      }

      // ===== HUD 생성 (검색창 + 카테고리 버튼 + 리스트) =====
      function mountHUD(){
        if (document.getElementById('shelterHud')) return;

        var wrap = document.createElement('div');
        wrap.id = 'shelterHud';
        wrap.innerHTML =
          '<div>' +
          '  <div class="hdr">' +
          '    <input id="shelterSearch" type="text" placeholder="대피소명/주소 검색"/>' +
          '    <ul id="shelterSuggest"></ul>' +
          '  </div>' +
          '  <div id="shelterCats" class="cats"></div>' +
          '  <div id="shelterList"></div>' +
          '</div>';
        document.body.appendChild(wrap);

        // 카테고리 버튼 (가나다 정렬 + '전체'는 맨 앞 고정)
        var CATS = ['전체','강원특별자치도','경기도','경상남도','경상북도','광주광역시','대구광역시','대전광역시','부산광역시','서울특별시','세종특별자치시','울산광역시','인천광역시','전라남도','전라북도','제주특별자치도','충청남도','충청북도'];
        var box = document.getElementById('shelterCats');
        box.innerHTML = CATS.map(function(nm, i){
          var cls = (i===0 ? 'btn cat active' : 'btn cat');
          return '<button type="button" class="'+cls+'" data-cat="'+escapeHtml(nm)+'">'+escapeHtml(nm)+'</button>';
        }).join('');

        // 클릭 핸들러
        box.addEventListener('click', function(e){
          var b = e.target.closest('.cat'); if (!b) return;
          var name = b.getAttribute('data-cat') || '';
          Array.prototype.forEach.call(box.querySelectorAll('.cat'), function(x){ x.classList.remove('active'); });
          b.classList.add('active');
          state.category = (name === '전체') ? '' : name;
          requestBBox('search');   // ▶ 리스트 갱신 + 전국 조회 + (카테고리일 땐 자동포커싱 생략)
        });
      }
      mountHUD();

      // ===== 인포윈도우(공용 1개) =====
      var sharedIW = new kakao.maps.InfoWindow({ removable: true });
      kakao.maps.event.addListener(sharedIW, 'domready', function(){
        var btn = document.querySelector('.goto-shelter');
        if (!btn) return;
        btn.onclick = function(){
          var lat = Number(this.getAttribute('data-lat'));
          var lon = Number(this.getAttribute('data-lon'));
          if (isNaN(lat) || isNaN(lon)) return;
          focusToExact(lat, lon, 4);
        };
      });

      // ===== 마커 이미지 캐시 =====
      var imgCache = {};
      function getMarkerImageByCapacity(cap){
        var size;
        if (cap != null) {
          var n = Number(cap);
          size = (n>=1000? 36 : n>=300? 28 : 22);
        } else {
          size = 26;
        }
        if (!imgCache[size]) {
          imgCache[size] = new kakao.maps.MarkerImage(
            'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png',
            new kakao.maps.Size(size, size)
          );
        }
        return imgCache[size];
      }

      // ===== 클러스터러 =====
      function ensureClusterer(){
        if (!state.clusterer && kakao.maps.MarkerClusterer) {
          state.clusterer = new kakao.maps.MarkerClusterer({
            map: map,
            averageCenter: true,
            minLevel: 6,            // ▶ 더 이른 단계에서 클러스터링
            disableClickZoom: false
          });
        }
      }
      function clusterAdd(markers){
        if (!state.clusterer) return markers.forEach(function(m){ m.setMap(map); });
        try { if (markers.length) state.clusterer.addMarkers(markers); }
        catch(_) { markers.forEach(function(m){ m.setMap(map); }); }
      }
      function clusterRemove(markers){
        if (!state.clusterer) return markers.forEach(function(m){ m.setMap(null); });
        try {
          if (state.clusterer.removeMarkers) state.clusterer.removeMarkers(markers);
          else { state.clusterer.clear(); state.clusterer.addMarkers(state.markers); }
        } catch(_) { markers.forEach(function(m){ m.setMap(null); }); }
      }

      // ===== 인포 HTML =====
      function infoHTML(item){
        var cap = (item.capacity!=null) ? ('<div><b>수용인원</b> ' + Number(item.capacity).toLocaleString() + '명</div>') : '';
        var tel = item.tel ? ('<div><b>연락처</b> ' + escapeHtml(item.tel) + '</div>') : '';
        return (
          '<div class="info-window">' +
          '  <div class="title">' + escapeHtml(item.reareNm||'대피소') + '</div>' +
          '  <div class="addr">' + escapeHtml(item.ronaDaddr || item.adress || item.address || '-') + '</div>' +
               cap + tel +
          '  <div class="actions">' +
          '    <button type="button" class="goto-shelter" ' +
          '      data-lat="'+ escapeHtml(String(item.lat)) +'" ' +
          '      data-lon="'+ escapeHtml(String(item.lon)) + '">여기로 이동</button>' +
          '  </div>' +
          '</div>'
        );
      }

      // ===== 마커 렌더(O(n) diff) =====
      function renderMarkers(list, skipList){
        ensureClusterer();

        var nextIds = new Set();
        var toAdd = [], toRemove = [];

        // 추가/갱신
        for (var i=0;i<list.length;i++){
          var d = list[i];
          if (d.lat==null || d.lon==null) continue;

          var id = String(d.shelterNo || d.id || d.SHELTER_NO || '');
          if (!id) continue;
          nextIds.add(id);

          var entry = state.markerMap.get(id);
          var pos = new kakao.maps.LatLng(Number(d.lat), Number(d.lon));

          if (!entry) {
            var marker = new kakao.maps.Marker({ position: pos, title: d.reareNm || '대피소' });
            try { marker.setImage(getMarkerImageByCapacity(d.capacity)); } catch(_) {}

            // 마커 클릭 → 공용 인포윈도우
            kakao.maps.event.addListener(marker, 'click', (function(m, data){
              return function(){
                try { map.panTo(m.getPosition()); } catch(_) {}
                sharedIW.setContent(infoHTML(data));
                sharedIW.open(map, m);
              };
            })(marker, d));

            state.markerMap.set(id, { marker: marker, data: d });
            state.markers.push(marker);
            toAdd.push(marker);
          } else {
            try { entry.marker.setPosition(pos); } catch(_) {}
            try { entry.marker.setImage(getMarkerImageByCapacity(d.capacity)); } catch(_) {}
            entry.data = d;
          }
        }

        // 제거
        state.markerMap.forEach(function(entry, id){
          if (!nextIds.has(id)) {
            toRemove.push(entry.marker);
            state.markerMap.delete(id);
          }
        });
        if (toRemove.length) {
          var keep = new Set();
          state.markerMap.forEach(function(e){ keep.add(e.marker); });
          state.markers = state.markers.filter(function(m){ return keep.has(m); });
        }

        // 클러스터 갱신
        if (toRemove.length) clusterRemove(toRemove);
        if (toAdd.length)    clusterAdd(toAdd);

        // 리스트(지도 이동으로는 갱신 안 함)
        if (!skipList) renderList(list);
      }

      // ===== 리스트 =====
      function renderList(list){
        var box = document.getElementById('shelterList');
        if (!Array.isArray(list) || list.length === 0) {
          box.innerHTML = '<div class="no-result">검색 결과가 없습니다.</div>';
          return;
        }

        var cut = list.slice(0, LIST_MAX);
        var html = [];
        for (var i=0;i<cut.length;i++){
          var d = cut[i];
          var num = i + 1;
          html.push(
            '<div class="s-item" data-id="'+ escapeHtml(String(d.shelterNo)) +'" ' +
            '     data-lat="'+ escapeHtml(String(d.lat)) +'" data-lon="'+ escapeHtml(String(d.lon)) +'">' +
            '  <div class="num">'+ num +'</div>' +
            '  <div class="meta">' +
            '    <div class="title">'+ escapeHtml(d.reareNm||'대피소') +'</div>' +
            '    <div class="addr">'+ escapeHtml(d.ronaDaddr || d.adress || d.address || '-') +'</div>' +
            '  </div>' +
            '</div>'
          );
        }
        if (list.length > LIST_MAX) {
          html.push('<div class="more">+' + (list.length - LIST_MAX) + '개 더 있음. 확대해서 보세요.</div>');
        }
        box.innerHTML = html.join('');

        // 리스트 클릭 → 정확 포커싱 + "마커 클릭" 트리거(모양/위치 동일)
        box.onclick = function(e){
          var item = e.target.closest('.s-item'); if (!item) return;
          var id  = item.getAttribute('data-id');
          var lat = Number(item.getAttribute('data-lat'));
          var lon = Number(item.getAttribute('data-lon'));
          if (isNaN(lat) || isNaN(lon)) return;

          var ent = state.markerMap.get(String(id));
          focusToExact(lat, lon, 4);
          if (ent && ent.marker) {
            once(map, 'idle', function(){ try { kakao.maps.event.trigger(ent.marker, 'click'); } catch(_) {} });
          }
        };
      }

      // ===== 자동완성 =====
      var suggestBox = document.getElementById('shelterSuggest');
      var inputEl    = document.getElementById('shelterSearch');

      function hideSuggest(){ suggestBox.style.display = 'none'; suggestBox.innerHTML = ''; }
      function showSuggest(items){
        if (!items || items.length===0) { hideSuggest(); return; }
        var html = items.map(function(t){
          return '<li class="sg" data-v="'+ escapeHtml(t) +'">'+ escapeHtml(t) +'</li>';
        }).join('');
        suggestBox.innerHTML = html;
        suggestBox.style.display = 'block';
      }

      var suggest = debounce(function(q){
        if (!q || q.length < 1) { hideSuggest(); return; }
        var addrP = fetch(API.sugAddr + '?q=' + encodeURIComponent(q)).then(function(r){return r.ok?r.json():[];});
        var nameP = fetch(API.sugName + '?q=' + encodeURIComponent(q)).then(function(r){return r.ok?r.json():[];});
        Promise.all([addrP, nameP]).then(function(rs){
          var merged = []
            .concat(Array.isArray(rs[1])?rs[1]:[])
            .concat(Array.isArray(rs[0])?rs[0]:[]);
          var seen = new Set(), out=[];
          for (var i=0;i<merged.length;i++){
            var t = String(merged[i]||'').trim();
            if(!t || seen.has(t)) continue;
            seen.add(t); out.push(t);
            if(out.length>=20) break;
          }
          showSuggest(out);
        }).catch(hideSuggest);
      }, 200);

      suggestBox.addEventListener('click', function(e){
        var li = e.target.closest('.sg'); if(!li) return;
        inputEl.value = li.getAttribute('data-v') || '';
        hideSuggest();
        state.q = inputEl.value.trim();
        requestBBox('search');   // ▶ 검색: 전국 범위 + 리스트 갱신 + 정확 포커싱
      });

      inputEl.addEventListener('input', function(){
        state.q = this.value.trim();
        suggest(state.q);
      });
      inputEl.addEventListener('keydown', function(e){
        if (e.key === 'Enter') {
          hideSuggest();
          state.q = this.value.trim();
          requestBBox('search'); // ▶ 검색: 전국 범위 + 리스트 갱신 + 정확 포커싱
        }
      });

      // ===== 서버 호출 (reason: 'map' | 'search') =====
      var requestBBox = debounce(function(reason){
        if (!state.active || state.dragging) return;

        // 검색/카테고리일 때는 전국 범위
        var bbox = (reason === 'search') ? KOREA_BOUNDS : getBBox();

        // limit: 지도 이동/검색 모두 동일 큰 상한 사용
        var limit = state.baseLimit;

        var params = new URLSearchParams({
          minLat: bbox ? bbox.minLat : '',
          maxLat: bbox ? bbox.maxLat : '',
          minLon: bbox ? bbox.minLon : '',
          maxLon: bbox ? bbox.maxLon : '',
          limit: limit
        });

        // q 구성: 카테고리 우선, 없으면 검색어
        var qVal = state.category || state.q;
        if (qVal) params.set('q', qVal);

        var url = API.bbox + '?' + params.toString();
        var reqId = ++state.lastReq;

        try { state.inflight && state.inflight.abort && state.inflight.abort(); } catch(_){}
        var ctrl = new AbortController(); state.inflight = ctrl;

        fetch(url, { signal: ctrl.signal })
          .then(function(r){ if(!r.ok) throw new Error('HTTP '+r.status); return r.json(); })
          .then(function(list){
            if (reqId !== state.lastReq) return;
            var arr = Array.isArray(list)? list : [];

            // 지도 이벤트(map)면 리스트 고정, 검색(search)면 리스트 갱신
            renderMarkers(arr, reason === 'map');

            // 검색 텍스트가 있을 때만 자동 포커싱(카테고리만 선택 시엔 생략)
            if (reason === 'search' && state.q && arr.length) {
              var qn = norm(state.q);
              var best = null, bestScore = -1;
              for (var i=0;i<arr.length;i++){
                var d = arr[i];
                var name = norm(d.reareNm);
                var addr = norm(d.ronaDaddr || d.adress || d.address);
                var score = 0;
                if (name && qn && name.indexOf(qn) >= 0) score += 2;
                if (addr && qn && addr.indexOf(qn) >= 0) score += 1;
                if (score > bestScore) { bestScore = score; best = d; }
              }
              if (!best) best = arr[0];

              var id = best && (best.shelterNo || best.id || best.SHELTER_NO);
              if (best && best.lat != null && best.lon != null) {
                focusToExact(best.lat, best.lon, 4);
                var ent = id ? state.markerMap.get(String(id)) : null;
                once(map, 'idle', function(){
                  try {
                    if (ent && ent.marker) kakao.maps.event.trigger(ent.marker, 'click');
                    else {
                      sharedIW.setContent(infoHTML(best));
                      sharedIW.open(map, new kakao.maps.Marker({ position: new kakao.maps.LatLng(Number(best.lat), Number(best.lon)) }));
                    }
                  } catch(_) {}
                });
              }
            }
          })
          .catch(function(e){
            if (e.name === 'AbortError') return;
            console.error('[ShelterLayer] fetch error', e);
          });
      }, FETCH_DEBOUNCE);

      // ===== 활성/비활성 =====
      function activate(opts){
        if (state.active) return;
        state.active = true;

        var hud = document.getElementById('shelterHud'); 
        if (hud) hud.style.display = 'block';

        if (opts && typeof opts.q === 'string') {
          state.q = opts.q.trim();
          var $in = document.getElementById('shelterSearch');
          if ($in) $in.value = state.q;
        }

        // ★ dragstart/dragend 리스너: 참조 저장 후 등록
        if (!state.dragStartHandler) {
          state.dragStartHandler = function(){ state.dragging = true; };
          kakao.maps.event.addListener(map, 'dragstart', state.dragStartHandler);
        }
        if (!state.dragEndHandler) {
          state.dragEndHandler = function(){ state.dragging = false; requestBBox('map'); };
          kakao.maps.event.addListener(map, 'dragend', state.dragEndHandler);
        }

        // ★ idle 리스너: 참조 저장 후 등록 (중복 방지)
        if (state.idleHandler) {
          kakao.maps.event.removeListener(map, 'idle', state.idleHandler);
        }
        state.idleHandler = function(){ requestBBox('map'); };
        kakao.maps.event.addListener(map, 'idle', state.idleHandler);

        requestBBox('map'); // 초기엔 마커만, 리스트는 검색/카테고리 후에만 갱신
      }

      function deactivate(){
        if (!state.active) return;
        state.active = false;

        // ★ 정확히 같은 핸들러 참조로 제거
        try { 
          if (state.idleHandler) {
            kakao.maps.event.removeListener(map, 'idle', state.idleHandler);
            state.idleHandler = null;
          }
          if (state.dragStartHandler) {
            kakao.maps.event.removeListener(map, 'dragstart', state.dragStartHandler);
            state.dragStartHandler = null;
          }
          if (state.dragEndHandler) {
            kakao.maps.event.removeListener(map, 'dragend', state.dragEndHandler);
            state.dragEndHandler = null;
          }
        } catch(_){}

        try { state.inflight && state.inflight.abort && state.inflight.abort(); } catch(_){}
        try { state.clusterer && state.clusterer.clear(); } catch(_){}
        state.markers.forEach(function(m){ try{ m.setMap(null);}catch(e){} });
        state.markers.length = 0;
        state.markerMap.clear();

        var hud = document.getElementById('shelterHud'); 
        if (hud) hud.style.display = 'none';
      }

      // ===== 외부 API =====
      var api = {
        activate: activate,
        deactivate: deactivate,
        setQuery: function(q){ state.q = (q||'').trim(); var $in = document.getElementById('shelterSearch'); if ($in) $in.value = state.q; requestBBox('search'); },
        refresh: function(){ requestBBox('search'); },
        setBaseLimit: function(n){ state.baseLimit = Math.max(200, Number(n)||6000); requestBBox('map'); },
        setCategory: function(name){ state.category = (name === '전체') ? '' : String(name||''); requestBBox('search'); }
      };

      App.registerLayer('shelter', api);
      global.ShelterLayer = api;
    }
  })(window);
}