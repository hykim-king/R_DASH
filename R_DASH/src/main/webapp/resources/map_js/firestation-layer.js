// /resources/map_js/firestation-layer.js
(function (global) {
  'use strict';

  // AppMap 준비되면 초기화
  if (global.AppMap) init();
  else global.addEventListener('appmap:ready', init, { once: true });

  function init() {
    var App = global.AppMap;
    if (!App || !App.map) { setTimeout(init, 80); return; }
    var map = App.map;

    // ===== 설정 / API =====
    var BASE = (document.body && document.body.getAttribute('data-context-path')) || '';
    var API  = {
      // BBox 내 목록 (최신 또는 전부)
      bbox:   BASE + '/firestations/bbox',          // ?minLat=&maxLat=&minLon=&maxLon=&limit=1000
      one:    function(id){ return BASE + '/firestations/' + id; },
      sug:    BASE + '/firestations/suggest',       // ?kw=
      search: BASE + '/firestations/search'         // ?kw=
    };

    // ===== 상태 =====
    var active = false;
    var overlays = [];           // kakao.maps.CustomOverlay[]
    var overlayPool = [];        // 재사용 풀
    var byId = new Map();        // id -> data
    var hudEl = null;            // 상단 우측 HUD (검색창+리스트)
    var listEl = null;           // 검색 리스트 컨테이너
    var inputEl = null;          // 검색 인풋
    var idleTimer = null;
    var lastFetchKey = '';

    // ===== 레이어 등록 =====
    App.addLayer('firestation', {
      title: '소방서',
      activate: activate,
      deactivate: deactivate,
      refresh: refresh
    });

    // ===== 유틸 =====
    function fetchJSON(url){
      return fetch(url, { headers: { 'Accept': 'application/json' }}).then(function(res){
        if(!res.ok) throw new Error('HTTP '+res.status);
        return res.json();
      });
    }

    function getBBox(){
      var bounds = map.getBounds();
      var sw = bounds.getSouthWest();
      var ne = bounds.getNorthEast();
      return {
        minLat: sw.getLat(), minLon: sw.getLng(),
        maxLat: ne.getLat(), maxLon: ne.getLng()
      };
    }

    function clearOverlays(){
      for (var i=0;i<overlays.length;i++){
        overlays[i].setMap(null);
        overlayPool.push(overlays[i]);
      }
      overlays.length = 0;
    }

    function getOverlay(){
      return overlayPool.pop() || new kakao.maps.CustomOverlay({ yAnchor: 1 });
    }

    function bubbleHTML(item){
      // item: { stationNo, stationNm, area, fireHead, tel, lat, lon, fireTp }
      var tp = item.fireTp || '';
      return (
        '<div class="fs-bubble '+ escapeHtml(tp) +'">' +
          '<div class="fs-dot"></div>' +
          '<div class="fs-card">' +
            '<div class="fs-title">'+ escapeHtml(item.stationNm || '') +'</div>' +
            '<div class="fs-area">'+ escapeHtml(item.area || '') +'</div>' +
            (item.tel ? ('<div class="fs-tel">'+ escapeHtml(item.tel) +'</div>') : '') +
          '</div>' +
        '</div>'
      );
    }

    function escapeHtml(s){
      return String(s||'').replace(/[&<>"']/g, function(m){
        return ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;', "'":'&#39;'}[m]);
      });
    }

    function draw(data){
      clearOverlays();
      for (var i=0;i<data.length;i++){
        var d = data[i];
        if (d.stationNo != null) byId.set(String(d.stationNo), d);

        var ov = getOverlay();
        ov.setContent(bubbleHTML(d));
        ov.setPosition(new kakao.maps.LatLng(+d.lat, +d.lon));
        ov.setMap(map);
        overlays.push(ov);

        // 클릭 시 상세 패널/이동
        (function(ovRef, item){
          kakao.maps.event.addListener(ovRef, 'click', function(){
            onPick(item);
          });
        })(ov, d);
      }
    }

    // ===== 이벤트 핸들러 =====
    function onMapIdle(){
      if (!active) return;
      if (idleTimer) clearTimeout(idleTimer);
      idleTimer = setTimeout(fetchBBox, 120);
    }

    function fetchBBox(){
      var b = getBBox();
      var key = [b.minLat,b.minLon,b.maxLat,b.maxLon].map(function(v){return v.toFixed(4)}).join(',');
      if (key === lastFetchKey) return;
      lastFetchKey = key;

      var url = API.bbox + 
        '?minLat=' + b.minLat + '&maxLat=' + b.maxLat +
        '&minLon=' + b.minLon + '&maxLon=' + b.maxLon +
        '&limit=1000';
      fetchJSON(url).then(draw).catch(function(e){
        console.error('[firestation] bbox error', e);
      });
    }

    function onPick(item){
      // 지도로 이동 + 간단 강조 애니메이션
      var latlng = new kakao.maps.LatLng(+item.lat, +item.lon);
      map.panTo(latlng);

      // 상세 팝업이 필요하면 서버 엔드포인트를 사용
      // fetchJSON(API.one(item.stationNo)).then(function(detail){ ... });
    }

    // ===== HUD (검색창 + 결과 리스트) =====
    function ensureHUD(){
      if (hudEl) return;
      hudEl = document.createElement('div');
      hudEl.id = 'fsHud';
      hudEl.className = 'fs-hud';

      var searchWrap = document.createElement('div');
      searchWrap.className = 'fs-search';

      inputEl = document.createElement('input');
      inputEl.type = 'text';
      inputEl.placeholder = '소방서/지역 검색';
      inputEl.className = 'fs-input';

      var clearBtn = document.createElement('button');
      clearBtn.type = 'button';
      clearBtn.className = 'fs-clear';
      clearBtn.textContent = '×';

      searchWrap.appendChild(inputEl);
      searchWrap.appendChild(clearBtn);

      listEl = document.createElement('div');
      listEl.className = 'fs-list';

      hudEl.appendChild(searchWrap);
      hudEl.appendChild(listEl);
      document.body.appendChild(hudEl);

      // 이벤트
      var debTimer = null;
      inputEl.addEventListener('input', function(){
        var kw = inputEl.value.trim();
        if (debTimer) clearTimeout(debTimer);
        debTimer = setTimeout(function(){
          if (!kw) { listEl.innerHTML = ''; return; }
          fetchJSON(API.search + '?kw=' + encodeURIComponent(kw))
            .then(renderList)
            .catch(function(e){ console.error('search error', e); });
        }, 220);
      });
      clearBtn.addEventListener('click', function(){
        inputEl.value = '';
        listEl.innerHTML = '';
        inputEl.focus();
      });
    }

    function renderList(rows){
      // rows: [{stationNo, stationNm, area, tel, lat, lon, fireTp}]
      var html = rows.map(function(r){
        var id = r.stationNo != null ? String(r.stationNo) : '';
        return (
          '<div class="fs-item" data-id="'+ id +'">' +
            '<div class="fs-item-title">'+ escapeHtml(r.stationNm || '') +'</div>' +
            '<div class="fs-item-sub">'+ escapeHtml(r.area || '') +'</div>' +
          '</div>'
        );
      }).join('');
      listEl.innerHTML = html || '<div class="fs-empty">검색 결과가 없습니다.</div>';

      var items = listEl.querySelectorAll('.fs-item');
      for (var i=0;i<items.length;i++){
        items[i].addEventListener('click', function(){
          var id = this.getAttribute('data-id');
          var known = byId.get(id);
          if (known) onPick(known);
          else {
            // 리스트에서만 보이는 경우 좌표 포함되어 있으면 사용
            var idx = Array.prototype.indexOf.call(items, this);
            var row = rows[idx];
            if (row) onPick(row);
          }
        });
      }
    }

    // ===== 수명주기 =====
    function activate(){
      if (active) return;
      active = true;
      ensureHUD();
      hudEl.style.display = 'block';
      kakao.maps.event.addListener(map, 'idle', onMapIdle);
      fetchBBox();
    }

    function deactivate(){
      if (!active) return;
      active = false;
      kakao.maps.event.removeListener(map, 'idle', onMapIdle);
      clearOverlays();
      if (hudEl) hudEl.style.display = 'none';
    }

    function refresh(){
      if (!active) return;
      fetchBBox();
    }
  }
})();
