/* eslint-disable */
if (window.__ShelterLayerLoaded__) {
  console.warn('[ShelterLayer] duplicate load skipped');
} else {
  window.__ShelterLayerLoaded__ = true;

  (function (global) {
    'use strict';

    // ===== 페이지 가드 =====
    var search = (typeof location !== 'undefined' && location.search) ? location.search : '';
    var qs     = new URLSearchParams(search);
    var LAYER_Q = (qs.get('layer') || '').toLowerCase();
    var BODY_LAYER = (document.body && (document.body.getAttribute('data-layer') || '')).toLowerCase();
    if (!((LAYER_Q === 'shelter') || (BODY_LAYER === 'shelter'))) {
      console.log('[ShelterLayer] not shelter page → skip init'); return;
    }

    // ===== 공통 설정 =====
    var CONFIG = {
      offsetTop: 110,      // 런처(다람쥐) 상단 위치(px) - 말풍선/HUD도 함께 내려감
      launcherRight: 16,   // 오른쪽 여백(px)
      hudGap: 18           // 버튼 ↔ HUD 간격(px)
    };

    // AppMap 준비
    if (global.AppMap) init(); else global.addEventListener('appmap:ready', init, { once: true });

    function init() {
      var App = global.AppMap;
      if (!App || !App.map) { setTimeout(init, 80); return; }
      var map = App.map;

      // ===== API =====
      var BASE = (document.body && document.body.getAttribute('data-context-path')) || '';
      var API  = {
        bbox:    BASE + '/shelters/bbox',
        one:     function(id){ return BASE + '/shelters/' + id; },
        sugAddr: BASE + '/shelters/suggest/adress', // (오타 그대로)
        sugName: BASE + '/shelters/suggest/name'
      };

      // ===== 상태/상수 =====
      var state = {
        active: false,        // 데이터/이벤트 바인딩 여부
        panelOpen: false,     // HUD 보임 여부
        q: '',
        category: '',         // '' = 전체
        baseLimit: 6000,
        lastReq: 0,
        inflight: null,
        markers: [],
        markerMap: new Map(),
        clusterer: null,
        dragging: false,
        idleHandler: null,
        dragStartHandler: null,
        dragEndHandler: null
      };
      var LIST_MAX       = 300;
      var FETCH_DEBOUNCE = 650;
      var KOREA_BOUNDS   = { minLat: 33, maxLat: 39.5, minLon: 124, maxLon: 132 };

      // ===== 유틸 =====
      function debounce(fn, ms) { var t; return function(){ var a=arguments,c=this; clearTimeout(t); t=setTimeout(function(){ fn.apply(c,a); }, ms); }; }
      function escapeHtml(s){ return (s||'').replace(/[&<>"']/g, function(m){ return ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[m]); }); }
      function getBBox(){ return (App && App.getBBox) ? App.getBBox() : null; }
      function once(target, type, handler){
        var wrapped = function(){ try { handler.apply(null, arguments); } finally { kakao.maps.event.removeListener(target, type, wrapped); } };
        kakao.maps.event.addListener(target, type, wrapped);
        return wrapped;
      }
      
      
      var ZM = {
  base: 900,  // 헤더를 못 찾았을 때 기본값(지도 위, 팝오버 아래일 가능성이 높음)
  selectors: [
    '.tiles-popover', '[data-tiles-popover]',      // tiles 계열
    '[role="dialog"]', '.popover', '.dropdown',    // 일반 팝오버/다이얼로그
    '#header [role="dialog"]', '.global-header [role="dialog"]'
  ]
};
function detectHeaderZ() {
  var max = 0;
  for (var i = 0; i < ZM.selectors.length; i++) {
    var els = document.querySelectorAll(ZM.selectors[i]);
    for (var j = 0; j < els.length; j++) {
      var zi = parseInt(getComputedStyle(els[j]).zIndex, 10);
      if (!isNaN(zi) && zi > max) max = zi;
    }
  }
  return max;
}
function computeUnderHeaderZ() {
  var headZ = detectHeaderZ();
  return headZ ? (Math.max(100, headZ - 1)) : ZM.base;
}
// 런처/HUD/버블에 적용
function applyZIndices() {
  var z = computeUnderHeaderZ();
  var launcher = document.getElementById('shelterLauncher');
  var hud = document.getElementById('shelterHud');

<<<<<<< HEAD
  if (launcher) launcher.style.zIndex = String(z);
  if (hud)      hud.style.zIndex      = String(z - 10);
=======
if (launcher) launcher.style.zIndex = String(z + 10); // ← 런처를 HUD 위로
if (hud)      hud.style.zIndex      = String(z);      // HUD는 기본 z
>>>>>>> a76d822e155237841302239ac2dbc91ab4e3722e
  // 열린 버블이 있으면 같이 내림
  try { if (shelterBubble && shelterBubble.setZIndex) shelterBubble.setZIndex(z - 5); } catch(_) {}
}
// 팝오버 열리고 닫힐 때도 자동 보정
var _applyZDeb = debounce(applyZIndices, 100);
var _mo = new MutationObserver(_applyZDeb);
_mo.observe(document.body, { subtree: true, childList: true, attributes: true, attributeFilter: ['class','style'] });
window.addEventListener('resize', _applyZDeb);
      function focusToExact(lat, lon, level){
        var pos = new kakao.maps.LatLng(Number(lat), Number(lon));
        var targetLevel = (typeof level === 'number') ? level : 4;
        try {
          map.setLevel(targetLevel, { animate: true });
          map.setCenter(pos);
          once(map, 'tilesloaded', function(){ try { map.setCenter(pos); } catch(_) {} });
          once(map, 'idle',        function(){ try { map.setCenter(pos); } catch(_) {} });
          setTimeout(function(){ try { map.setCenter(pos); } catch(_) {} }, 350);
        } catch(_) {}
      }

      // ===== HUD 위치 =====
      function placeHUD(where){ // 'below' | 'above'
        var hud = document.getElementById('shelterHud');
        var btnWrap = document.getElementById('shelterLauncher');
        if (!hud || !btnWrap) return;

        var b = btnWrap.getBoundingClientRect();
        var hW = hud.offsetWidth  || 360;
        var hH = hud.offsetHeight || 320;
        var gap = CONFIG.hudGap;

        var left = Math.max(12, Math.min(b.right - hW, window.innerWidth - hW - 12)); // 오른쪽 정렬
        var top  = (where === 'above') ? (b.top - hH - gap) : (b.bottom + gap);
        top = Math.max(CONFIG.offsetTop - 20, Math.min(top, window.innerHeight - hH - 12));

        hud.style.left = left + 'px';
        hud.style.top  = top  + 'px';
      }
      function placeHUDSafe(){ placeHUD('below'); }
      window.addEventListener('resize', function(){ if (state.panelOpen) placeHUDSafe(); });
      window.addEventListener('scroll',  function(){ if (state.panelOpen) placeHUDSafe(); });

      // ===== 상세 버블(CustomOverlay) =====
      var shelterBubble = null;
      function bubbleHTML(item){
        var name = escapeHtml(item.reareNm || '대피소');
        var addr = escapeHtml(item.ronaDaddr || item.adress || item.address || '-');
        var cap  = (item.capacity!=null) ? ('<div class="row"><b>수용인원</b> ' + Number(item.capacity).toLocaleString() + '명</div>') : '';
        var tel  = item.tel ? ('<div class="row"><b>연락처</b> ' + escapeHtml(item.tel) + '</div>') : '';

        return (
          '<div class="shel-bubble" style="width:340px;">' +
          '  <div class="close" data-close="1" aria-label="close">×</div>' +
          '  <div class="title">'+ name +'</div>' +
          '  <div class="addr">'+ addr +'</div>' +
               cap + tel +
          '</div>'
        );
      }
      function openShelterBubble(pos, item){
        if (shelterBubble) { try { shelterBubble.setMap(null); } catch(_){} shelterBubble = null; }
        var el = document.createElement('div');
        el.innerHTML = bubbleHTML(item);
        var root = el.firstChild;
        root.addEventListener('click', function(e){
          if (e.target && e.target.getAttribute('data-close') === '1') {
            if (shelterBubble) { shelterBubble.setMap(null); shelterBubble = null; }
            e.stopPropagation();
          }
        });
        shelterBubble = new kakao.maps.CustomOverlay({
          position: pos,
          content: root,
          yAnchor: 1.15,
          xAnchor: 0.5,
          zIndex: computeUnderHeaderZ() - 5
        });
        shelterBubble.setMap(map);
      }
      // 지도 클릭 → 버블 닫기
      kakao.maps.event.addListener(map, 'click', function(){
        if (shelterBubble) { try { shelterBubble.setMap(null); } catch(_){} shelterBubble = null; }
      });

      // ===== 클러스터/마커 =====
      var SHELTER_ICON_URL = 'https://img.icons8.com/color/48/bomb-shelter.png';
      var imgCache = {};
      function getShelterMarkerImage(){
        if (!imgCache['48']) {
          imgCache['48'] = new kakao.maps.MarkerImage(
            SHELTER_ICON_URL,
            new kakao.maps.Size(48, 48),
            { offset: new kakao.maps.Point(24, 48) }
          );
        }
        return imgCache['48'];
      }
      function ensureClusterer(){
        if (!state.clusterer && kakao.maps.MarkerClusterer) {
          state.clusterer = new kakao.maps.MarkerClusterer({
            map: map, averageCenter: true, minLevel: 6, disableClickZoom: false
          });
        }
      }
      function clusterAdd(markers){
        if (!state.clusterer) { markers.forEach(function(m){ m.setMap(map); }); return; }
        try { if (markers.length) state.clusterer.addMarkers(markers); }
        catch(_) { markers.forEach(function(m){ m.setMap(map); }); }
      }
      function clusterRemove(markers){
        if (!state.clusterer) { markers.forEach(function(m){ m.setMap(null); }); return; }
        try {
          if (state.clusterer.removeMarkers) state.clusterer.removeMarkers(markers);
          else { state.clusterer.clear(); state.clusterer.addMarkers(state.markers); }
        } catch(_) { markers.forEach(function(m){ m.setMap(null); }); }
      }

      function renderMarkers(list, skipList){
        ensureClusterer();

        var nextIds = new Set();
        var toAdd = [], toRemove = [];

        for (var i=0;i<list.length;i++){
          var d = list[i];
          if (d.lat==null || d.lon==null) continue;

          var id = String(d.shelterNo || d.id || d.SHELTER_NO || '').trim();
          if (!id) continue;
          if (nextIds.has(id)) continue;  // 입력 중복 제거
          nextIds.add(id);

          var entry = state.markerMap.get(id);
          var pos = new kakao.maps.LatLng(Number(d.lat), Number(d.lon));

          if (!entry) {
            var marker = new kakao.maps.Marker({ position: pos, title: d.reareNm || '대피소' });
            try { marker.setImage(getShelterMarkerImage()); } catch(_) {}
            kakao.maps.event.addListener(marker, 'click', (function(m, data){
              return function(){
                try { map.panTo(m.getPosition()); } catch(_) {}
                openShelterBubble(m.getPosition(), data); // ✅ 커스텀 버블
              };
            })(marker, d));
            state.markerMap.set(id, { marker: marker, data: d });
            state.markers.push(marker);
            toAdd.push(marker);
          } else {
            try { entry.marker.setPosition(pos); } catch(_) {}
            try { entry.marker.setImage(getShelterMarkerImage()); } catch(_) {}
            entry.data = d;
          }
        }

        // 빠진 항목 제거
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

        if (toRemove.length) clusterRemove(toRemove);
        if (toAdd.length)    clusterAdd(toAdd);

        if (!skipList) renderList(list);
      }

      // ===== 리스트 =====
      function renderList(list){
        var box = document.getElementById('shelterList');
        if (!box) return;

        if (!Array.isArray(list) || list.length === 0) {
          box.innerHTML = '<div class="no-result" style="padding:10px;color:#6b7280;">검색 결과가 없습니다.</div>';
          return;
        }

        // id 기준 중복 제거
        var seen = new Set(), cut = [];
        for (var i=0;i<list.length && cut.length<LIST_MAX; i++){
          var d = list[i];
          var id = String(d.shelterNo || d.id || d.SHELTER_NO || '').trim();
          if (!id || seen.has(id)) continue;
          seen.add(id); cut.push(d);
        }

        var html = [];
        for (var j=0; j<cut.length; j++){
          var d = cut[j];
          var num = j + 1;
          html.push(
            '<div class="s-item" data-id="'+ (d.shelterNo||d.id||d.SHELTER_NO||'') +'" ' +
            '     data-lat="'+ (d.lat!=null?d.lat:'') +'" data-lon="'+ (d.lon!=null?d.lon:'') +'">' +
            '  <div class="num">'+ num +'</div>' +
            '  <div class="meta">' +
            '    <div class="title">'+ escapeHtml(d.reareNm||'대피소') +'</div>' +
            '    <div class="addr">'+ escapeHtml(d.ronaDaddr || d.adress || d.address || '-') +'</div>' +
            '  </div>' +
            '</div>'
          );
        }
        if (list.length > cut.length) {
          html.push('<div class="more" style="padding:8px 10px;color:#6b7280;">+' + (list.length - cut.length) + '개 더 있음. 확대해서 보세요.</div>');
        }
        box.innerHTML = html.join('');

        // 클릭 → 지도 이동 + 마커 클릭
        box.onclick = function(e){
          var item = e.target.closest('.s-item'); if (!item) return;
          var id  = String(item.getAttribute('data-id') || '');
          var lat = Number(item.getAttribute('data-lat'));
          var lon = Number(item.getAttribute('data-lon'));
          if (isNaN(lat) || isNaN(lon)) return;

          focusToExact(lat, lon, 4);
          var ent = state.markerMap.get(id);
          once(map, 'idle', function(){ try { if (ent && ent.marker) kakao.maps.event.trigger(ent.marker, 'click'); } catch(_) {} });
        };
      }

      // ===== HUD 생성 =====
      function mountHUD(){
        if (document.getElementById('shelterHud')) return;
        var wrap = document.createElement('div');
        wrap.id = 'shelterHud';
        wrap.style.display = 'none';
        wrap.innerHTML =
          '<div>' +
          '  <div class="hdr">' +
          '    <input id="shelterSearch" type="text" placeholder="대피소명/주소 검색"/>' +
          '    <ul id="shelterSuggest" style="display:none;"></ul>' +
          '  </div>' +
          '  <div id="shelterCats" class="cats"></div>' +
          '  <div id="shelterList"></div>' +
          '</div>';
        document.body.appendChild(wrap);

        var CATS = ['전체','강원특별자치도','경기도','경상남도','경상북도','광주광역시','대구광역시','대전광역시','부산광역시','서울특별시','세종특별자치시','울산광역시','인천광역시','전라남도','전북특별자치도','제주특별자치도','충청남도','충청북도'];
        var box = document.getElementById('shelterCats');
        box.innerHTML = CATS.map(function(nm, i){
          var cls = (i===0 ? 'btn cat active' : 'btn cat');
          return '<button type="button" class="'+cls+'" data-cat="'+escapeHtml(nm)+'">'+escapeHtml(nm)+'</button>';
        }).join('');

        // 카테고리 클릭 → 전국 BBOX 재조회
        box.addEventListener('click', function(e){
          var b = e.target.closest('.cat'); if (!b) return;
          var name = b.getAttribute('data-cat') || '';
          Array.prototype.forEach.call(box.querySelectorAll('.cat'), function(x){ x.classList.remove('active'); });
          b.classList.add('active');
          state.category = (name === '전체') ? '' : name;
          requestBBox('search');
        });
      }
      mountHUD();

      // ===== 런처 버튼 + 말풍선 (이미지 버튼) =====
      (function mountLauncher(){
        if (document.getElementById('shelterLauncher')) return;
        var BTN_IMG = BASE + '/resources/image/jaeminsinkhole_3.png';

        var wrap = document.createElement('div');
        wrap.className = 'layer-launcher';
        wrap.id = 'shelterLauncher';
        wrap.style.top = CONFIG.offsetTop + 'px';
        wrap.style.setProperty('--launcherRight', CONFIG.launcherRight + 'px');

        wrap.innerHTML =
          '<div class="bubble-wrap">'+
            '<div class="bubble">너네 마을에 대피소는 어디에 있어 ?</div>'+
            '<div class="bubble">궁금하면 날 클릭해 !</div>'+
          '</div>'+
          '<img id="shelterBtnImg" class="launcher-img" alt="대피소" src="'+ BTN_IMG +'" onerror="this.style.opacity=0">';
        document.body.appendChild(wrap);

        var btnImg = document.getElementById('shelterBtnImg');
        btnImg.addEventListener('click', function(e){
          e.preventDefault(); e.stopPropagation();
          if (!state.active) activateLayer(); // 안전 가드
          togglePanel();                       // HUD만 토글
        });
      })();

      // ===== 검색 입력(최소 연결) =====
      function wireSuggest() {
        var inputEl = document.getElementById('shelterSearch');
        if (!inputEl) return;
        inputEl.addEventListener('keydown', function(e){
          if (e.key === 'Enter') {
            state.q = (inputEl.value || '').trim();
            requestBBox('search');
          }
        });
      }

      // ===== 서버 호출: 카테고리/검색(q) + BBOX 반영 =====
      var requestBBox = debounce(function(reason){
        if (!state.active || state.dragging) return;

        var bbox = (reason === 'search') ? KOREA_BOUNDS : getBBox();
        var params = new URLSearchParams({
          minLat: bbox ? bbox.minLat : '',
          maxLat: bbox ? bbox.maxLat : '',
          minLon: bbox ? bbox.minLon : '',
          maxLon: bbox ? bbox.maxLon : '',
          limit: state.baseLimit
        });

        // 카테고리 > 검색어 우선
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
            var arr = Array.isArray(list) ? list : [];
            renderMarkers(arr);
            renderList(arr);
          })
          .catch(function(e){
            if (e.name!=='AbortError') console.error('[ShelterLayer] fetch error', e);
          });
      }, FETCH_DEBOUNCE);

      // ===== 패널 전용 열기/닫기 =====
      function openPanel(){
        var hud = document.getElementById('shelterHud');
        if (!hud) return;
        hud.style.display = 'block';
        placeHUDSafe();
        wireSuggest();
        state.panelOpen = true;
      }
      function closePanel(){
        var hud = document.getElementById('shelterHud');
        if (!hud) return;
        hud.style.display = 'none';
        state.panelOpen = false;
      }
      function togglePanel(){ state.panelOpen ? closePanel() : openPanel(); }

      // ===== 레이어 활성/비활성 =====
      function activateLayer(){
        if (state.active) return;
        state.active = true;

        if (!state.dragStartHandler) {
          state.dragStartHandler = function(){ state.dragging = true; };
          kakao.maps.event.addListener(map, 'dragstart', state.dragStartHandler);
        }
        if (!state.dragEndHandler) {
          state.dragEndHandler = function(){ state.dragging = false; requestBBox('map'); };
          kakao.maps.event.addListener(map, 'dragend', state.dragEndHandler);
        }
        if (!state.idleHandler) {
          state.idleHandler = function(){ requestBBox('map'); };
          kakao.maps.event.addListener(map, 'idle', state.idleHandler);
        }

        // 첫 로딩: 전국 BBOX(전체)
        requestBBox('search');
      }
      function deactivateLayer(){
        if (!state.active) return;
        state.active = false;

        try {
          if (state.idleHandler)      { kakao.maps.event.removeListener(map, 'idle',      state.idleHandler);      state.idleHandler = null; }
          if (state.dragStartHandler) { kakao.maps.event.removeListener(map, 'dragstart', state.dragStartHandler); state.dragStartHandler = null; }
          if (state.dragEndHandler)   { kakao.maps.event.removeListener(map, 'dragend',   state.dragEndHandler);   state.dragEndHandler = null; }
        } catch(_){}

        try { state.inflight && state.inflight.abort && state.inflight.abort(); } catch(_){}
        if (state.clusterer && state.clusterer.clear) { try { state.clusterer.clear(); } catch(_) {} }
        state.markers.forEach(function(m){ try{ m.setMap(null);}catch(e){} });
        state.markers.length = 0;
        state.markerMap.clear();

        closePanel();
      }

      // 런처 위치 실시간 조정(옵션)
      function setLauncherTop(px){
        CONFIG.offsetTop = Number(px)||CONFIG.offsetTop;
        var wrap = document.getElementById('shelterLauncher');
        if (wrap) wrap.style.top = CONFIG.offsetTop + 'px';
        if (state.panelOpen) placeHUDSafe();
      }

      // ===== 외부 API 등록 + 초기 활성화 =====
      var api = { activate: activateLayer, deactivate: deactivateLayer, togglePanel: togglePanel, setTop: setLauncherTop };
      if (App && typeof App.registerLayer === 'function') App.registerLayer('shelter', api);
      global.ShelterLayer = api;

      // ✅ 페이지 진입 시 자동 활성화(전체 시각화). HUD는 닫힘 상태.
      try { if (!state.active) activateLayer(); } catch(_) {}
    }
  })(window);
<<<<<<< HEAD
}
=======
}
>>>>>>> a76d822e155237841302239ac2dbc91ab4e3722e
