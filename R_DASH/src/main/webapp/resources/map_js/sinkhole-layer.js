/* eslint-disable */
(function (global) {
  'use strict';

  // ──────────────────────────────────────────────
  //  페이지/레이어 가드: ?layer=sinkhole
  // ──────────────────────────────────────────────
  var qs = new URLSearchParams(location.search || '');
  var LAYER = (qs.get('layer') || '').toLowerCase();
  var BODY_LAYER = (document.body.getAttribute('data-layer') || '').toLowerCase();
  var IS_ME = (LAYER === 'sinkhole') || (BODY_LAYER === 'sinkhole');
  if (!IS_ME) { console.log('[Sinkhole] skip (not my layer)'); return; }

  // 컨텍스트 경로 추론
  function detectCtx() {
    var ctx = document.body.getAttribute('data-ctx');
    if (ctx && ctx.trim()) return ctx.trim();
    var m = (location.pathname || '').match(/^\/[^\/]+/);
    return (m && m[0]) || '';
  }
  var CTX   = detectCtx();
  var YEAR  = document.body.getAttribute('data-year')  || '';
  var STATE = document.body.getAttribute('data-state') || '';

  // API
  var API_POINTS = CTX + '/sinkholes/points';

  // 아이콘/애니메이션
  var VLC_ICON_URL       = 'https://img.icons8.com/3d-fluency/100/vlc.png';
  var SHOW_OVERLAY_LEVEL = 5;
  var ANIM_CLASS         = 'animate__shakeY';
  var ANIM_DURATION      = '900ms';

  // 마스코트
  var MASCOT_1 = CTX + '/resources/image/jaeminsinkhole_1.png';

  // KakaoMap 준비 후 init
  if (global.AppMap) init();
  else global.addEventListener('appmap:ready', init, { once: true });

  function init() {
    var App = global.AppMap || {};
    var map = App.map;
    if (!map || !global.kakao || !kakao.maps) {
      console.error('[Sinkhole] kakao map not ready');
      return;
    }

    // ───────────────────────────
    //  말풍선 HUD
    // ───────────────────────────
    var bubbleOv = null;
    function showBubbleAt(pos, row){
      var raw = (row.stateNm || row.STATE_NM || row['STATE NM'] || row.status || '') + '';
      var s   = raw.replace(/\s+/g,'');
      var label = '상태 미상';
      var colorClass = 'sh-mini-prog';
      if (s.includes('완료'))      { label = '복구 완료'; colorClass = 'sh-mini-done'; }
      else if (s.includes('임시')) { label = '임시복구';  colorClass = 'sh-mini-temp'; }
      else if (s.includes('중'))   { label = '복구중';    colorClass = 'sh-mini-prog'; }

      var sido   = row.SIDO_NM || row.sidoNm || row['SIDO NM'] || '';
      var signgu = row.SIGNGU_NM || row.signguNm || row['SIGNGU NM'] || '';
      var occur  = row.OCCUR_DT || row.occurDt || row['OCCUR DT'] || '';

      var html =
        '<div class="sh-bubble">' +
          '<h5>싱크홀 복구현황</h5>' +
          '<div class="sh-badge"><span class="sh-dot-mini '+colorClass+'"></span><span>'+label+'</span></div>' +
          (sido||signgu ? '<div class="sh-place">'+ [sido, signgu].filter(Boolean).join(' ') +'</div>' : '') +
          (occur ? '<div class="sh-date">발생일: '+ occur +'</div>' : '') +
        '</div>';

      if (bubbleOv) bubbleOv.setMap(null);
      bubbleOv = new kakao.maps.CustomOverlay({ position: pos, content: html, yAnchor: 1.15, xAnchor: 0.5, zIndex: 300 });
      bubbleOv.setMap(map);

      clearTimeout(showBubbleAt._t);
      showBubbleAt._t = setTimeout(function(){
        if (bubbleOv) { bubbleOv.setMap(null); bubbleOv = null; }
      }, 10000);
    }

    // ───────────────────────────
    //  클러스터 (투명 마커)
    // ───────────────────────────
    var TRANSPARENT_IMG = 'data:image/png;base64,iVBOR...'; // 생략
    var transparentImage = new kakao.maps.MarkerImage(
      TRANSPARENT_IMG, new kakao.maps.Size(1, 1), { offset: new kakao.maps.Point(0, 0) }
    );

    function svgCircle(size, inner, outer){
      var svg =
        '<svg xmlns="http://www.w3.org/2000/svg" width="'+size+'" height="'+size+'" viewBox="0 0 '+size+' '+size+'">' +
          '<defs><filter id="s"><feGaussianBlur stdDeviation="2"/></filter></defs>' +
          '<circle cx="'+(size/2)+'" cy="'+(size/2)+'" r="'+(size/2-2)+'" fill="'+outer+'" filter="url(#s)"/>' +
          '<circle cx="'+(size/2)+'" cy="'+(size/2)+'" r="'+(size/2-4)+'" fill="'+inner+'"/>' +
        '</svg>';
      return 'url("data:image/svg+xml;utf8,' + encodeURIComponent(svg) + '")';
    }

    var styles = [
      { width:'40px', height:'40px', lineHeight:'40px', background: svgCircle(40,'rgba(144,238,144,.9)','rgba(34,197,94,.35)') },
      { width:'52px', height:'52px', lineHeight:'52px', background: svgCircle(52,'rgba(255,255,153,.95)','rgba(234,179,8,.35)') },
      { width:'64px', height:'64px', lineHeight:'64px', background: svgCircle(64,'rgba(253,230,138,.95)','rgba(245,158,11,.35)') },
      { width:'76px', height:'76px', lineHeight:'76px', background: svgCircle(76,'rgba(250,204,21,.98)','rgba(245,158,11,.45)') },
      { width:'90px', height:'90px', lineHeight:'90px', background: svgCircle(90,'rgba(96,165,250,.98)','rgba(59,130,246,.45)') },
    ];

    var clusterer = new kakao.maps.MarkerClusterer({
      map: map, averageCenter: true, minLevel: 6, styles: styles,
      calculator: [10,50,150,400], gridSize: 80
    });

    // ───────────────────────────
    //  상태
    // ───────────────────────────
    var markers = [];
    var overlays = [];
    var rowsCache = [];

    var layer = { name:'sinkhole', activate, deactivate };
    if (typeof App.register === 'function') App.register('sinkhole', layer);

    var debouncedLoad = debounce(loadAndRender, 250);
    var onZoomChanged = debounce(updateOverlayVisibility, 0);

    function activate(){
      installStaticPanel();
      loadAndRender();
      kakao.maps.event.addListener(map, 'idle',          debouncedLoad);
      kakao.maps.event.addListener(map, 'zoom_changed',  onZoomChanged);
      kakao.maps.event.addListener(map, 'click', function(){
        if (bubbleOv){ bubbleOv.setMap(null); bubbleOv = null; }
      });
    }
    function deactivate(){
      kakao.maps.event.removeListener(map, 'idle',         debouncedLoad);
      kakao.maps.event.removeListener(map, 'zoom_changed', onZoomChanged);
      clearAll(); removeStaticPanel();
    }

    function clearAll(){
      if (clusterer) clusterer.clear();
      markers.forEach(m => m.setMap(null)); markers = [];
      overlays.forEach(ov => ov.setMap(null)); overlays = [];
    }

    function updateOverlayVisibility(){
      var show = map.getLevel() <= SHOW_OVERLAY_LEVEL;
      overlays.forEach(function(ov){ ov.setMap(show ? map : null); });
    }

    function loadAndRender(){
      var b = map.getBounds(); if (!b) return;
      var sw = b.getSouthWest(), ne = b.getNorthEast();
      var params = { minLat:sw.getLat(), maxLat:ne.getLat(), minLon:sw.getLng(), maxLon:ne.getLng() };
      if (YEAR)  params.year = YEAR;
      if (STATE) params.stateNm = STATE;

      ajaxJSON(API_POINTS, params).then(function(rows){
        rowsCache = rows || [];
        render(rowsCache);
        refreshStaticPanel();
      }).catch(function(e){ console.error('[Sinkhole] load error:', e); });
    }

    function render(rows){
      if (clusterer) clusterer.clear();
      markers.forEach(m => m.setMap(null)); markers = [];
      overlays.forEach(ov => ov.setMap(null)); overlays = [];

      overlays = (rows || []).map(function(r){
        var lat = +(r.lat || r.LAT);
        var lon = +(r.lon || r.LON);
        if (isNaN(lat) || isNaN(lon)) return null;
        var pos = new kakao.maps.LatLng(lat, lon);

        var img = document.createElement('img');
        img.src = VLC_ICON_URL;
        img.className = 'sh-icon animate__animated';
        img.style.setProperty('--animate-duration', ANIM_DURATION);
        img.addEventListener('click', function(e){
          e.stopPropagation();
          img.classList.remove(ANIM_CLASS); void img.offsetWidth; img.classList.add(ANIM_CLASS);
          showBubbleAt(pos, r);
        });

        var ov = new kakao.maps.CustomOverlay({ position: pos, content: img, yAnchor: 1 });
        ov.setMap(map.getLevel() <= SHOW_OVERLAY_LEVEL ? map : null);
        return ov;
      }).filter(Boolean);

      markers = overlays.map(function(ov){
        return new kakao.maps.Marker({ position: ov.getPosition(), image: transparentImage });
      });
      clusterer.addMarkers(markers);
    }

    // ───────────────────────────
    //  고정 패널
    // ───────────────────────────
    function installStaticPanel(){
      if (document.getElementById('sinkholePanel')) return;
      var wrap = document.createElement('div');
      wrap.id = 'sinkholePanel';
      wrap.className = 'sh-fixed-wrap';

      var img = document.createElement('img');
      img.src = MASCOT_1; img.alt = 'mascot'; img.className = 'sh-mascot';
      wrap.appendChild(img);

      var panel = document.createElement('div');
      panel.className = 'sh-panel';
      panel.innerHTML =
        '<h4>복구현황 (현재 화면)</h4>' +
        '<div class="sh-row"><div class="sh-dot sh-dot-temp"></div><div class="sh-name">임시복구</div><div class="sh-cnt" id="cntTemp">0</div></div>' +
        '<div class="sh-row"><div class="sh-dot sh-dot-prog"></div><div class="sh-name">복구중</div><div class="sh-cnt" id="cntProg">0</div></div>' +
        '<div class="sh-row"><div class="sh-dot sh-dot-done"></div><div class="sh-name">복구 완료</div><div class="sh-cnt" id="cntDone">0</div></div>';
      wrap.appendChild(panel);
      document.body.appendChild(wrap);
    }
    function removeStaticPanel(){
      var el = document.getElementById('sinkholePanel'); if (el) el.remove();
    }
    function refreshStaticPanel(){
      var wrap = document.getElementById('sinkholePanel'); if (!wrap) return;
      var temp=0, prog=0, done=0;
      rowsCache.forEach(function(r){
        var s = (r.stateNm || r.STATE_NM || r['STATE NM'] || r.status || '').replace(/\s+/g,'');
        if (!s) return;
        if (s.indexOf('임시') > -1) temp++;
        else if (s.indexOf('중') > -1) prog++;
        else if (s.indexOf('완료') > -1) done++;
      });
      wrap.querySelector('#cntTemp').textContent = temp.toLocaleString();
      wrap.querySelector('#cntProg').textContent = prog.toLocaleString();
      wrap.querySelector('#cntDone').textContent = done.toLocaleString();
    }
  }

  // ───────────────────────────
  //  유틸
  // ───────────────────────────
  function ajaxJSON(url, params) {
    return new Promise(function (resolve, reject) {
      var q = '';
      if (params && Object.keys(params).length) {
        q = '?' + Object.keys(params).map(function (k) {
          return encodeURIComponent(k) + '=' + encodeURIComponent(params[k]);
        }).join('&');
      }
      var xhr = new XMLHttpRequest();
      xhr.open('GET', url + q, true);
      xhr.setRequestHeader('Accept', 'application/json');
      xhr.onreadystatechange = function () {
        if (xhr.readyState !== 4) return;
        if (xhr.status >= 200 && xhr.status < 300) {
          try { resolve(JSON.parse(xhr.responseText)); }
          catch (e) { reject(e); }
        } else { reject(new Error('HTTP ' + xhr.status)); }
      };
      xhr.send();
    });
  }
  function debounce(fn, ms){ var t; return function(){ clearTimeout(t); t = setTimeout(fn, ms); }; }

})(this);
