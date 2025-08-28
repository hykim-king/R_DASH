/* eslint-disable */
(function (global) {
  'use strict';

  var qs = new URLSearchParams(location.search || '');
  var LAYER = (qs.get('layer') || '').toLowerCase();
  var BODY_LAYER = (document.body.getAttribute('data-layer') || '').toLowerCase();
  var IS_ME = (LAYER === 'sinkhole') || (BODY_LAYER === 'sinkhole');
  if (!IS_ME) { console.log('[Sinkhole] skip (not my layer)'); return; }

  function detectCtx() {
    var ctx = document.body.getAttribute('data-ctx');
    if (ctx && ctx.trim()) return ctx.trim();
    var m = (location.pathname || '').match(/^\/[^\/]+/);
    return (m && m[0]) || '';
  }
  var CTX   = detectCtx();
  var YEAR  = document.body.getAttribute('data-year')  || '';
  var STATE = document.body.getAttribute('data-state') || '';

  var API_POINTS = CTX + '/sinkholes/points';

  if (global.AppMap) init(); else global.addEventListener('appmap:ready', init, { once: true });

  function init() {
    var App = global.AppMap || {};
    var map = App.map;
    if (!map || !global.kakao || !kakao.maps) {
      console.error('[Sinkhole] kakao map not ready'); return;
    }

    // ── 클러스터 배지 스타일 ──────────────────────
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
      { width:'40px', height:'40px', lineHeight:'40px', textAlign:'center', color:'#1f2937', fontWeight:'700', fontSize:'14px',
        background: svgCircle(40,'rgba(144,238,144,.9)','rgba(34,197,94,.35)') },
      { width:'52px', height:'52px', lineHeight:'52px', textAlign:'center', color:'#1f2937', fontWeight:'700', fontSize:'15px',
        background: svgCircle(52,'rgba(255,255,153,.95)','rgba(234,179,8,.35)') },
      { width:'64px', height:'64px', lineHeight:'64px', textAlign:'center', color:'#1f2937', fontWeight:'800', fontSize:'16px',
        background: svgCircle(64,'rgba(253,230,138,.95)','rgba(245,158,11,.35)') },
      { width:'76px', height:'76px', lineHeight:'76px', textAlign:'center', color:'#111827', fontWeight:'800', fontSize:'17px',
        background: svgCircle(76,'rgba(250,204,21,.98)','rgba(245,158,11,.45)') },
      { width:'90px', height:'90px', lineHeight:'90px', textAlign:'center', color:'#0b1020', fontWeight:'900', fontSize:'18px',
        background: svgCircle(90,'rgba(96,165,250,.98)','rgba(59,130,246,.45)') },
    ];
    var clusterer = new kakao.maps.MarkerClusterer({
      map, averageCenter:true, minLevel:6, styles, calculator:[10,50,150,400], gridSize:80
    });

    var markers = [];
    var animOverlays = []; // 임시 애니메이션 오버레이들

    // 레이어 등록
    var layer = { name:'sinkhole', activate, deactivate };
    if (typeof App.register === 'function') App.register('sinkhole', layer);

    function activate(){
      loadAndRender();
      kakao.maps.event.addListener(map, 'idle', debounce(loadAndRender, 250));
    }
    function deactivate(){
      kakao.maps.event.removeListener(map, 'idle', loadAndRender);
      clear();
    }

    function clear(){
      if (clusterer) clusterer.clear();
      markers.forEach(m => m.setMap(null));
      markers = [];
      // 남아있는 애니메이션 오버레이 제거
      animOverlays.forEach(ov => ov.setMap(null));
      animOverlays = [];
    }

    function loadAndRender(){
      var b = map.getBounds(); if (!b) return;
      var sw = b.getSouthWest(), ne = b.getNorthEast();
      var params = { minLat:sw.getLat(), maxLat:ne.getLat(), minLon:sw.getLng(), maxLon:ne.getLng() };
      if (YEAR)  params.year = YEAR;
      if (STATE) params.stateNm = STATE;

      ajaxJSON(API_POINTS, params).then(function(rows){
        clear();
        markers = (rows||[]).map(function(r){
          var lat = Number(r.lat), lon = Number(r.lon);
          if (isNaN(lat) || isNaN(lon)) return null;
          var pos = new kakao.maps.LatLng(lat, lon);
          var m = new kakao.maps.Marker({ position: pos });
          // 클릭 시 애니메이션 spawn
          kakao.maps.event.addListener(m, 'click', function(){ spawnClickAnim(pos); });
          return m;
        }).filter(Boolean);
        clusterer.addMarkers(markers);
        console.log('[Sinkhole] cluster markers:', markers.length);
      }).catch(function(e){ console.error('[Sinkhole] load error:', e); });
    }

    // ▶ 클릭 애니메이션: 해당 좌표에 잠깐 CustomOverlay를 띄우고 애니메 끝나면 제거
    function spawnClickAnim(position){
      var el = document.createElement('div');
      // 전역 CSS 안 쓰고 inline + animate.css 클래스만 사용
      el.style.cssText = [
        'width:18px;height:18px;border-radius:50%;',
        'background:#8b5cf6;border:2px solid #fff;box-shadow:0 2px 6px rgba(0,0,0,.3);',
        'transform:translate(-50%,-50%);'
      ].join('');
      // 애니메이션 클래스 + 개별 duration 지정
      el.classList.add('animate__animated','animate__shakeY');
      el.style.setProperty('--animate-duration','800ms');

      // 끝나면 제거
      function cleanup(){
        ov.setMap(null);
        el.removeEventListener('animationend', cleanup);
        var idx = animOverlays.indexOf(ov);
        if (idx >= 0) animOverlays.splice(idx,1);
      }
      el.addEventListener('animationend', cleanup);

      var ov = new kakao.maps.CustomOverlay({
        position: position,
        content: el,
        yAnchor: 1,
        zIndex: 999
      });
      ov.setMap(map);
      animOverlays.push(ov);

      // 같은 애니메이션을 연속 클릭에도 재생되게 reflow 트릭
      void el.offsetWidth;
      el.classList.remove('animate__shakeY');
      el.classList.add('animate__shakeY');
    }
  }

  // 공통 유틸: GET JSON
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