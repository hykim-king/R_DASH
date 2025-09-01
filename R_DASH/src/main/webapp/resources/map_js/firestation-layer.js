/* eslint-disable */
// /resources/map_js/firestation-layer.js
(function (global) {
  'use strict';

  // ── 페이지/레이어 가드 ─────────────────────────────
  var qs = new URLSearchParams((typeof location !== 'undefined' && location.search) || '');
  var LAYER = (qs.get('layer') || '').toLowerCase();
  var BODY_LAYER = ((document.body && document.body.getAttribute('data-layer')) || '').toLowerCase();
  var IS_ME = (LAYER === 'firestation') || (BODY_LAYER === 'firestation');

  if (!IS_ME) {
    try { ['fireLauncher','fsHud','fsFilterBar'].forEach(function(id){ var el = document.getElementById(id); if (el) el.remove(); }); } catch(_) {}
    console.log('[Firestation] skip: not my layer');
    return;
  }

  // AppMap 준비되면 초기화
  if (global && global.AppMap) init();
  else global.addEventListener('appmap:ready', init, { once: true });

  // === 헤더 동기화 유틸 (헤더 높이를 CSS 변수에 반영) ===
  function getHeaderEl(){
    return document.querySelector('#siteHeader') ||
           document.querySelector('.site-header') ||
           document.querySelector('#header') ||
           document.querySelector('.tiles-header') ||
           document.querySelector('header');
  }
  function ensureHeaderSync(){
    var hdr = getHeaderEl();
    var h = hdr ? Math.ceil(hdr.getBoundingClientRect().bottom) : 90;
    document.documentElement.style.setProperty('--header-height', h + 'px');
    if (hdr) { hdr.style.position = 'relative'; hdr.style.zIndex = '3000'; }
  }

  function init() {
    var App = global.AppMap;
    if (!App || !App.map) { setTimeout(init, 80); return; }
    var map = App.map;

    // 헤더 보정
    ensureHeaderSync();
    window.addEventListener('resize', ensureHeaderSync);
    if (window.ResizeObserver) {
      var _hdr = getHeaderEl();
      if (_hdr) new ResizeObserver(ensureHeaderSync).observe(_hdr);
    }

    // ===== 공통 =====
    var BASE = (document.body && (document.body.getAttribute('data-context-path') || document.body.getAttribute('data-ctx'))) || '';

    // ===== 조회 유형 =====
    var CURRENT_FIRE_TP = 'ALL'; // MAIN | SAFETY | ALL

    // ===== 아이콘 =====
    var ICON_MAIN   = 'https://img.icons8.com/external-justicon-flat-justicon/50/external-fire-station-fire-fighter-justicon-flat-justicon.png';
    var ICON_SAFETY = 'https://img.icons8.com/external-those-icons-lineal-color-those-icons/96/external-Fire-Station-places-those-icons-lineal-color-those-icons.png';
    var ICON_119    = 'https://img.icons8.com/fluency/48/ambulance.png';
    var ICON_OTHER  = ICON_MAIN;

    function markerImage(url){
      try { return new kakao.maps.MarkerImage(url, new kakao.maps.Size(28,28), {offset:new kakao.maps.Point(14,26)}); }
      catch(e){ return null; }
    }
    var IMG_MAIN   = markerImage(ICON_MAIN);
    var IMG_SAFETY = markerImage(ICON_SAFETY);
    var IMG_119    = markerImage(ICON_119);
    var IMG_OTHER  = markerImage(ICON_OTHER);

    // ===== API =====
    var API  = {
      bbox:   BASE + '/firestations/bbox',
      search: BASE + '/firestations/search'
    };

    // ===== 상태 =====
    var active = false;
    var markers = [];
    var clusterer = null;
    var openedOverlay = null; // 현재 열린 CustomOverlay 1개 유지
    var lastRowsOnMap = [];

    // HUD refs
    var hudEl = null, listEl = null, inputEl = null;

    // 요청/이벤트 안정화
    var idleTimer = null;
    var inFlight = false;
    var lastFetchKey = '';
    var lastDrawKey  = '';
    var skipNextIdleFetch = false; // 클릭 직후 1회 idle 스킵

    // ── 레이어 API 등록 (여기서 참조하는 함수들이 반드시 아래에 정의되어 있어야 함)
    var firestationAPI = { title: '소방서', activate: activate, deactivate: deactivate, refresh: refresh, setType: setType };
    if (typeof App.registerLayer === 'function') App.registerLayer('firestation', firestationAPI);
    else global.FirestationLayer = firestationAPI;

    function setType(tp){
      CURRENT_FIRE_TP = (tp || 'ALL').toUpperCase();
      refresh();
      try { updateFilterBarUI(); } catch(_) {}
    }

    // ===== 런처 버튼 + 말풍선 =====
    (function mountLauncher(){
      if (document.getElementById('fireLauncher')) return;
      var BTN_IMG = BASE + '/resources/image/jaeminsinkhole_6.png';

      var wrap = document.createElement('div');
      wrap.className = 'layer-launcher';
      wrap.id = 'fireLauncher';

      wrap.innerHTML =
        '<div class="bubble-beside">'+
          '<div class="bubble-wrap">'+
            '<div class="bubble">너네 마을에 소방서랑 119안전센터 어딨는지 궁금 하지 않아?</div>'+
            '<div class="bubble">궁금하면 날 클릭해 !</div>'+
          '</div>'+
          '<img id="fireBtnImg" class="launcher-img" alt="fire" src="'+ BTN_IMG +'" onerror="this.style.opacity=0">'+
        '</div>';

      document.body.appendChild(wrap);

      var btn = document.getElementById('fireBtnImg');
      if (btn) {
        btn.addEventListener('click', function(e){
          e.preventDefault(); e.stopPropagation();
          if (!hudEl) ensureHUD();
          var opening = (hudEl.style.display !== 'block');
 hudEl.style.display = opening ? 'block' : 'none';
 if (opening) {
   // HUD가 막 열렸다면 전체 목록을 즉시(또는 BBOX 후 자동) 채움
   populateAllOnOpen();
 }
        });
      }
    })();

    // ===== HUD =====
    function ensureHUD(){
      if (hudEl) return;

      hudEl = document.createElement('div');
      hudEl.id = 'fsHud';
      hudEl.style.display = 'none';

      var searchWrap = document.createElement('div');
      searchWrap.className = 'fs-search';
      searchWrap.style.display = 'flex';
      searchWrap.style.gap = '8px';
      searchWrap.style.padding = '10px';

      inputEl = document.createElement('input');
      inputEl.type = 'text';
      inputEl.placeholder = '소방서/지역 검색';
      inputEl.className = 'fs-input';
      Object.assign(inputEl.style, {
        flex: '1', height: '36px', fontSize: '13px', padding: '0 10px',
        border: 'none', outline: 'none', borderRadius: '10px',
        background: '#f9fafb', boxShadow: 'inset 0 0 0 1px #e5e7eb'
      });

      var clearBtn = document.createElement('button');
      clearBtn.type = 'button';
      clearBtn.className = 'fs-clear';
      Object.assign(clearBtn.style, {
        width:'30px', height:'30px', borderRadius:'8px', border:'0',
        cursor:'pointer', background:'linear-gradient(180deg,#f8fafc,#eef2ff)'
      });
      clearBtn.textContent = '×';

      searchWrap.appendChild(inputEl);
      searchWrap.appendChild(clearBtn);

      var divider = document.createElement('div');
      divider.className = 'fs-divider';
      divider.style.height = '2px';
      divider.style.margin = '2px 10px 8px';
      divider.style.background = 'linear-gradient(to right, rgba(0,0,0,.08), rgba(0,0,0,0))';

      var chipsWrap = document.createElement('div');
      chipsWrap.className = 'fs-chips';
      Object.assign(chipsWrap.style, {display:'flex', flexWrap:'wrap', gap:'6px', padding:'6px 10px 8px'});

      var REGIONS = [
        '전체','서울특별시','경기도','인천광역시','부산광역시','대구광역시','광주광역시','대전광역시',
        '울산광역시','세종특별자치시','강원도','충청북도','충청남도',
        '전라북도','전라남도','경상북도','경상남도','제주특별자치도'
      ];
      chipsWrap.innerHTML = REGIONS.map(function(n,i){
        var act = (i===0 ? ' is-active' : '');
        return '<button type="button" class="chip-btn'+act+'" data-region="'+ (n==='전체'?'':n) +'">'+ n +'</button>';
      }).join('');

      var list = document.createElement('div');
      listEl = list;
      list.className = 'fs-list';
      list.style.maxHeight = '28vh';
      list.style.overflow = 'auto';
      list.style.borderTop = '1px solid #eee';

      hudEl.appendChild(searchWrap);
      hudEl.appendChild(divider);
      hudEl.appendChild(chipsWrap);
      hudEl.appendChild(list);
      document.body.appendChild(hudEl);

      // 입력 검색
      var debTimer = null;
      inputEl.addEventListener('input', function(){
        var q = inputEl.value.trim();
        if (debTimer) clearTimeout(debTimer);
       if (!q) {
     // ✅ 검색어가 비었고, "전체" 칩이 활성화라면 전체 목록 그대로
     var allActive = document.querySelector('#fsHud .fs-chips .chip-btn.is-active[data-region=""]');
     if (allActive) {
       var rows = (lastRowsOnMap || []).slice().sort(function(a,b){
         return String(a.stationNm||'').localeCompare(String(b.stationNm||''), 'ko');
       });
       renderList(rows, { numbered: true });
       return;
     }
     listEl.innerHTML = '';
     return;
   }
        
        debTimer = setTimeout(function(){
          if (!q) { listEl.innerHTML = ''; return; }
          var url = API.search + '?q=' + encodeURIComponent(q) + '&limit=200&offset=0' +
                    '&fireTp=ALL';
          fetchJSON(url)
            .then(function(res){
              var rows = Array.isArray(res) ? res : (res && res.rows) ? res.rows : [];
              renderList(rows, { numbered: true });
            })
            .catch(function(e){ console.error('[firestation] search error', e); });
        }, 220);
      });
      clearBtn.addEventListener('click', function(){
        inputEl.value = ''; listEl.innerHTML = ''; inputEl.focus();
      });

      // 지역 칩 클릭
      chipsWrap.addEventListener('click', function(e){
        var t = e.target.closest('.chip-btn'); if (!t) return;
        chipsWrap.querySelectorAll('.chip-btn').forEach(function(b){ b.classList.remove('is-active'); b.style.background='#f3f6ff'; b.style.color='#374151'; b.style.borderColor='#e5e7eb'; });
        t.classList.add('is-active'); t.style.background='#111827'; t.style.color='#fff'; t.style.borderColor='#111827';

        var region = t.getAttribute('data-region') || '';
        

        if (!region){
     // ✅ "전체" → 방금까지 지도에서 불러온 전체(BBOX) 결과를
     // 바로 번호 매겨서 표시하고, 백그라운드로 최신화
     var rows = (lastRowsOnMap || []).slice();
     // (선호 시) 정렬: 이름순
     rows.sort(function(a,b){
       return String(a.stationNm||'').localeCompare(String(b.stationNm||''), 'ko');
     });
     renderList(rows, { numbered: true });
     requestBBox();
     return;
   }

        var url = API.search + '?q=' + encodeURIComponent(region) + '&limit=300&offset=0' +
                  '&fireTp=' + encodeURIComponent(CURRENT_FIRE_TP);

        fetchJSON(url).then(function(res){
          var rows = Array.isArray(res) ? res : (res && res.rows) ? res.rows : [];
          renderList(rows);
          if (rows.length){
            try {
              var p = new kakao.maps.LatLng(+rows[0].lat, +rows[0].lon);
              map.setLevel(Math.max(map.getLevel(), 8), {animate:true});
              map.panTo(p);
            } catch(_) {}
          }
        }).catch(function(e){ console.error('[firestation] region search error', e); });
      });
    }


 function renderList(rows, opts){
   opts = opts || {};
   var numbered = (opts.numbered !== false); 
    
    var html = (rows||[]).map(function(r, idx){
     var num = idx + 1;
    var badge = numbered ? badgeHTML(num, r) : '';
     return (
       '<div class="fs-item" data-idx="'+ idx +'" style="padding:9px 10px;border-bottom:1px solid #f1f1f1;cursor:pointer;display:flex;gap:10px;align-items:flex-start;">' +
         badge +
         '<div style="min-width:0;">' +
           '<div class="fs-item-title" style="font-weight:800;font-size:13px;letter-spacing:-.2px;">'+ escapeHtml(r.stationNm || '') +'</div>' +
           '<div class="fs-item-sub" style="font-size:12px;color:#667085;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;">'+
              escapeHtml(r.area || '') + (r.fireTp ? ' · ' + escapeHtml(r.fireTp) : '') +
           '</div>' +
         '</div>' +
       '</div>'
     );
   }).join('');
      listEl.innerHTML = html || '<div class="fs-empty" style="padding:18px;color:#94a3b8;text-align:center;">검색 결과가 없습니다.</div>';

      var items = listEl.querySelectorAll('.fs-item');
      for (var i=0;i<items.length;i++){
        items[i].addEventListener('click', function(){
          var idx = +this.getAttribute('data-idx');
          var dto = rows[idx]; if (!dto) return;

          var latlng = new kakao.maps.LatLng(+dto.lat, +dto.lon);
          map.panTo(latlng);

          var mk = findNearestMarker(latlng, 30);
          if (mk) { openOverlayForMarker(mk, dto); }
        });
      }
    }

    // ===== 하단 필터바 =====
    var filterBar = null;
    (function mountFilterBar(){
      if (document.getElementById('fsFilterBar')) {
        filterBar = document.getElementById('fsFilterBar');
      } else {
        filterBar = document.createElement('div');
        filterBar.id = 'fsFilterBar';
        filterBar.innerHTML = [
          '<button type="button" class="fs-btn" data-type="ALL">전체</button>',
          '<button type="button" class="fs-btn" data-type="MAIN">소방서</button>',
          '<button type="button" class="fs-btn" data-type="SAFETY">119안전센터</button>'
        ].join('');
        document.body.appendChild(filterBar);
      }
      filterBar.addEventListener('click', function(e){
        var t = e.target.closest('.fs-btn'); if (!t) return;
        setType(t.getAttribute('data-type'));
        updateFilterBarUI();
      });
      updateFilterBarUI();
    })();

    function updateFilterBarUI(){
      if (!filterBar) return;
      filterBar.querySelectorAll('.fs-btn').forEach(function(b){
        var tp = b.getAttribute('data-type');
        var active = (tp === CURRENT_FIRE_TP);
        if (active) b.classList.add('is-active'); else b.classList.remove('is-active');
      });
    }

    // ===== 유틸 =====
    function typeOfRow(dto){
  // draw()에서 __normTp가 붙은 경우 우선 사용, 없으면 규칙으로 판정
  return (dto && dto.__normTp) ? dto.__normTp : normalizeFireTp(dto);
}
function badgeHTML(num, dto){
  var tp = typeOfRow(dto);
  var bg  = (tp === 'MAIN')   ? '#ef4444' : (tp === 'SAFETY') ? '#f59e0b' : '#6b7280'; // 빨강 / 주황 / 회색
  var ring= (tp === 'MAIN')   ? '#b91c1c' : (tp === 'SAFETY') ? '#b45309' : '#374151';
  return '<div style="width:22px;height:22px;border-radius:50%;'
       + 'background:'+bg+';border:2px solid '+ring+';color:#fff;'
       + 'display:flex;align-items:center;justify-content:center;'
       + 'font:800 12px/1 system-ui;flex:0 0 22px;">'+ num +'</div>';
}
    
    
    function populateAllOnOpen(){
  // 검색어가 비어 있고 "전체" 칩이 활성화인 경우에만 동작
  if (!inputEl || inputEl.value.trim()) return;
  var allActive = document.querySelector('#fsHud .fs-chips .chip-btn.is-active[data-region=""]');
  if (!allActive) return;

  if (lastRowsOnMap && lastRowsOnMap.length){
    var rows = lastRowsOnMap.slice().sort(function(a,b){
      return String(a.stationNm||'').localeCompare(String(b.stationNm||''), 'ko');
    });
    renderList(rows, { numbered: true });
  } else {
    // 아직 캐시가 없으면 로딩 표시 후 BBOX 요청
    if (listEl) listEl.innerHTML = '<div style="padding:18px;color:#94a3b8;text-align:center;">불러오는 중…</div>';
    requestBBox(); // then()에서 자동으로 목록 채움
  }
}
    
    
    function normalizeFireTp(dto){
      var raw = (dto && (dto.fireTp || dto.type || dto.stationNm || '')) + '';
      var s = raw.replace(/\s+/g, '').toUpperCase();
      if (/119?안전센(터|타)/.test(s) || s.includes('CENTER') || s.includes('지역대') || s.includes('파출')) return 'SAFETY';
      if (s.includes('소방서') || s.includes('FIRESTATION') || s.includes('본부')) return 'MAIN';
      var name = ((dto && dto.stationNm) || '').toUpperCase();
      if (/119?\s*안전센(터|타)/.test(name) || name.includes('CENTER') || name.includes('지역대') || name.includes('파출')) return 'SAFETY';
      if (name.includes('소방서') || name.includes('본부')) return 'MAIN';
      return 'MAIN';
    }
    function fetchJSON(url){
      return fetch(url, { headers: { 'Accept': 'application/json' } })
        .then(function(res){ if(!res.ok) throw new Error('HTTP '+res.status); return res.json(); });
    }
    function getBBox(){
      var b = map.getBounds(); if (!b) return null;
      var sw = b.getSouthWest(), ne = b.getNorthEast();
      return { minLat: sw.getLat(), minLon: sw.getLng(), maxLat: ne.getLat(), maxLon: ne.getLng() };
    }
    function escapeHtml(s){
      return String(s||'').replace(/[&<>"']/g, function(m){
        return ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;','\'':'&#39;'}[m]);
      });
    }

    // ===== CustomOverlay 컨텐츠(유형 제거) =====
    function buildFsContent(dto){
      var el = document.createElement('div');
      el.className = 'fs-bubble';
      el.innerHTML =
        '<div class="close" title="닫기">×</div>' +
        '<div class="title">' + escapeHtml(dto.stationNm || '') + '</div>' +
        (dto.area     ? '<div class="addr">' + escapeHtml(dto.area) + '</div>' : '') +
        (dto.fireNead ? '<div class="row">관할: ' + escapeHtml(dto.fireNead) + '</div>' : '') +
        (dto.tel      ? '<div class="row tel"><span>' + escapeHtml(dto.tel) + '</span></div>' : '');
      return el;
    }
    function openOverlayForMarker(marker, dto){
      if (openedOverlay) { openedOverlay.setMap(null); openedOverlay = null; }
      var contentEl = buildFsContent(dto);
      var ovl = new kakao.maps.CustomOverlay({
        position: marker.getPosition(),
        content: contentEl,
        yAnchor: 1,
        xAnchor: 0.5,
        zIndex: 4
      });
      ovl.setMap(map);
      openedOverlay = ovl;

      var closeBtn = contentEl.querySelector('.close');
      if (closeBtn) closeBtn.addEventListener('click', function(e){
        e.stopPropagation();
        if (openedOverlay) { openedOverlay.setMap(null); openedOverlay = null; }
      });
    }

    // ===== 클러스터러 =====
    function svgBubble(size, fill, ring){
      var r = size/2, stroke = Math.max(2, Math.round(size*0.08));
      var s = [
        '<svg xmlns="http://www.w3.org/2000/svg" width="'+size+'" height="'+size+'" viewBox="0 0 '+size+' '+size+'">',
        '<defs><filter id="sh" x="-50%" y="-50%" width="200%" height="200%"><feDropShadow dx="0" dy="3" stdDeviation="3" flood-opacity="0.25"/></filter></defs>',
        '<circle cx="'+r+'" cy="'+r+'" r="'+(r-stroke)+'" fill="'+fill+'" filter="url(#sh)"/>',
        '<circle cx="'+r+'" cy="'+r+'" r="'+(r-stroke-1)+'" fill="none" stroke="'+ring+'" stroke-width="'+stroke+'"/>',
        '</svg>'
      ].join('');
      return 'data:image/svg+xml;charset=UTF-8,' + encodeURIComponent(s);
    }
    function ensureClusterer(){
      if (clusterer !== null) return;
      var ok = !!(global.kakao && kakao.maps && kakao.maps.MarkerClusterer);
      if (!ok) { console.warn('[firestation] MarkerClusterer 미로드'); return; }

      function styleOf(size, fill, ring, weight){
        var data = svgBubble(size, fill, ring);
        return {
          width:  size + 'px',
          height: size + 'px',
          background: 'url("'+ data +'") no-repeat center/contain',
          color: '#ffffff',
          fontWeight: weight || 800,
          fontSize: Math.round(size*0.34) + 'px',
          textAlign: 'center',
          lineHeight: size + 'px'
        };
      }

      clusterer = new kakao.maps.MarkerClusterer({
        map: map,
        averageCenter: true,
        minLevel: 7,
        gridSize: 100,
        minClusterSize: 2,
        calculator: [10, 50, 100, 300, 1000],
        styles: [
          styleOf(36,'#fca5a5','#ef4444',600),
          styleOf(44,'#f87171','#dc2626',700),
          styleOf(54,'#ef4444','#b91c1c',700),
          styleOf(66,'#dc2626','#991b1b',800),
          styleOf(80,'#b91c1c','#7f1d1d',800)
        ]
      });
      console.log('[firestation] clusterer ready');
    }

    // ===== 마커 렌더링 =====
    function clearMarkers(){
      for (var i=0;i<markers.length;i++){ markers[i].setMap(null); }
      if (clusterer) clusterer.clear();
      markers.length = 0;
    }
    function pickImageByType(tp){
      tp = (tp||'').toUpperCase();
      if (tp === 'MAIN')   return IMG_MAIN;
      if (tp === 'SAFETY') return IMG_SAFETY;
      if (tp === 'ONE19')  return IMG_119;
      return IMG_OTHER;
    }
    function draw(rows){
      clearMarkers();
      if (!Array.isArray(rows)) rows = [];
      ensureClusterer();

      var newMarkers = [];
      for (var i=0;i<rows.length;i++){
        var d = rows[i];
        var lat = +d.lat, lon = +d.lon;
        if (!isFinite(lat) || !isFinite(lon)) continue;

        var normTp = normalizeFireTp(d);
        d.__normTp = normTp;
        if (CURRENT_FIRE_TP !== 'ALL' && normTp !== CURRENT_FIRE_TP) continue;

        var img = pickImageByType(normTp);

        var marker = new kakao.maps.Marker({
          position: new kakao.maps.LatLng(lat, lon),
          title: d.stationNm || '',
          image: img || undefined
        });
        marker.__dto = d;

        (function(mk){
            kakao.maps.event.addListener(mk, 'click', function(){
            // ✅ panTo로 인해 곧바로 idle가 발생하며 재조회가 트리거되는데,
            // 그 1회만 건너뛰도록 플래그 설정
            skipNextIdleFetch = true;

         openOverlayForMarker(mk, mk.__dto);
         map.panTo(mk.getPosition());
        });
      })(marker);
        newMarkers.push(marker);
      }

      if (clusterer) clusterer.addMarkers(newMarkers);
      else newMarkers.forEach(function(m){ m.setMap(map); });

      markers = newMarkers;
      console.log('[firestation] draw markers:', newMarkers.length, 'type:', CURRENT_FIRE_TP);
    }

    // ===== 지도 이벤트 =====
    function onMapIdle(){
      if (!active) return;
        if (skipNextIdleFetch) {
    skipNextIdleFetch = false;
    return;
  }
      if (idleTimer) clearTimeout(idleTimer);
      idleTimer = setTimeout(requestBBox, 160);
    }
    function onTilesLoadedOnce(){
      kakao.maps.event.removeListener(map, 'tilesloaded', onTilesLoadedOnce);
      requestBBox();
    }

    // ===== 데이터 요청(BBox) =====
    function requestBBox(){
      var b = getBBox(); if (!b) return;
      var key = [b.minLat,b.minLon,b.maxLat,b.maxLon,CURRENT_FIRE_TP].map(function(v){return String(v).slice(0,6)}).join(',');

      if (!inFlight && key === lastDrawKey) return;
      if (inFlight && key === lastFetchKey) return;

      lastFetchKey = key; inFlight = true;

      var label = '';
      var url = API.bbox +

        '?minLat=' + b.minLat + '&maxLat=' + b.maxLat +
        '&minLon=' + b.minLon + '&maxLon=' + b.maxLon +
        '&limit=' + 5000 +
        '&fireTp=ALL' +
        '&fireTpLabel=' + encodeURIComponent(label);

      console.log('[firestation] bbox fetch start', { tp: CURRENT_FIRE_TP, bbox: b });
      fetchJSON(url)
        .then(function(rows){
          rows = rows || [];
           console.log('[firestation] bbox rows:', Array.isArray(rows) ? rows.length : rows);
           // ✅ 최신 지도 결과 캐시
           lastRowsOnMap = Array.isArray(rows) ? rows : [];
           // 마커 렌더
           draw(lastRowsOnMap);
          lastDrawKey = key;
  try {
     var allActive = document.querySelector('#fsHud .fs-chips .chip-btn.is-active[data-region=""]');
     var hasQuery  = (inputEl && inputEl.value.trim().length > 0);
     if (allActive && !hasQuery) {
        var listRows = lastRowsOnMap.slice().sort(function(a,b){
           return String(a.stationNm||'').localeCompare(String(b.stationNm||''), 'ko');
         });
     
        renderList(listRows, { numbered: true });
     }
   } catch (_) {}
})
        .catch(function(e){ console.error('[firestation] bbox error', e); })
        .finally(function(){ inFlight = false; });
    }

    // 검색 리스트 → 가까운 마커 찾기
    function findNearestMarker(latlng, pixelRadius){
      var proj = map.getProjection();
      var ptTarget = proj.containerPointFromCoords(latlng);
      var best = null, bestDist = Infinity;

      for (var i=0;i<markers.length;i++){
        var pt = proj.containerPointFromCoords(markers[i].getPosition());
        var dx = pt.x - ptTarget.x, dy = pt.y - ptTarget.y;
        var d = Math.sqrt(dx*dx + dy*dy);
        if (d < bestDist) { bestDist = d; best = markers[i]; }
      }
      return (bestDist <= (pixelRadius||30)) ? best : null;
    }

    // ===== 수명주기 =====
    function activate(){
      if (active) return;
      active = true;

      if (hudEl) hudEl.style.display = 'none';

      kakao.maps.event.addListener(map, 'tilesloaded', onTilesLoadedOnce);
      kakao.maps.event.addListener(map, 'idle', onMapIdle);

      inFlight = false; lastFetchKey = ''; lastDrawKey = '';
      requestBBox();
      console.log('[firestation] activate');
    }

    function deactivate(){
      if (!active) return;
      active = false;

      kakao.maps.event.removeListener(map, 'idle', onMapIdle);
      kakao.maps.event.removeListener(map, 'tilesloaded', onTilesLoadedOnce);
      if (idleTimer) { clearTimeout(idleTimer); idleTimer = null; }

      clearMarkers();
      if (hudEl) hudEl.style.display = 'none';
      if (openedOverlay) { openedOverlay.setMap(null); openedOverlay = null; }

      console.log('[firestation] deactivate');
    }

    function refresh(){
      if (!active) return;
      requestBBox();
      console.log('[firestation] refresh');
    }
  }
})(window);
