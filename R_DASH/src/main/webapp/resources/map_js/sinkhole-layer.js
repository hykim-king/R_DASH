/* eslint-disable */
(function (global) {
  'use strict';

  // ───────── 기본 가드 ─────────
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

    // ───────── 아이콘(🚧) 설정 ─────────
    var ICON_URL = 'https://img.icons8.com/3d-fluency/100/under-construction.png';
    var BASE = 40; // px
    function makeIcon(size){
      return new kakao.maps.MarkerImage(
        ICON_URL,
        new kakao.maps.Size(size, size),
        { offset: new kakao.maps.Point(size/2, size/2) }
      );
    }
    var MARKER_IMAGE     = makeIcon(BASE);
    var MARKER_IMAGE_BIG = makeIcon(Math.round(BASE * 1.35)); // 클릭 애니메이션용

    // ───────── 클러스터 스타일 ─────────
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
    var infoOverlay = null; // 말풍선

    // ───────── 우측 패널 설치 ─────────
    mountRightPanel();

    // 레이어 등록(있을 경우)
    var layer = { name:'sinkhole', activate, deactivate };
    if (typeof App.register === 'function') App.register('sinkhole', layer);

    function activate(){
      loadAndRender();
      kakao.maps.event.addListener(map, 'idle', debounce(loadAndRender, 250));
      kakao.maps.event.addListener(map, 'click', hideInfo);
    }
    function deactivate(){
      kakao.maps.event.removeListener(map, 'idle', debounce(loadAndRender, 250));
      clear();
    }

    function clear(){
      if (clusterer) clusterer.clear();
      markers.forEach(function(m){ m.setMap(null); });
      markers = [];
      hideInfo();
      updateRightPanel({ total:0, byState:{} });
    }

    function loadAndRender(){
      var b = map.getBounds(); if (!b) return;
      var sw = b.getSouthWest(), ne = b.getNorthEast();
      var params = { minLat:sw.getLat(), maxLat:ne.getLat(), minLon:sw.getLng(), maxLon:ne.getLng() };
      if (YEAR)  params.year = YEAR;
      if (STATE) params.stateNm = STATE;

      ajaxJSON(API_POINTS, params).then(function(rows){
        clear();

        // 통계 누적
        var stats = { total:0, byState:{} };

        markers = (rows || []).map(function(r){
          var lat = Number(r.lat || r.LAT);
          var lon = Number(r.lon || r.LON);
          if (isNaN(lat) || isNaN(lon)) return null;

          // 상태 집계
          var st = getVal(r, ['stateNm','STATE_NM','STATE NM','status']);
          var stNorm = normalizeState(st) || '미상';
          stats.total += 1;
          stats.byState[stNorm] = (stats.byState[stNorm] || 0) + 1;

          var pos = new kakao.maps.LatLng(lat, lon);
          var m = new kakao.maps.Marker({ position: pos, image: MARKER_IMAGE });

          kakao.maps.event.addListener(m, 'click', function(){
            // 1) 아이콘 확대 애니메이션
            spawnClickAnim(m);
            // 2) 말풍선(발생일자 + 복구현황)
            showInfoAt(pos, {
              occur: getVal(r, ['occurDt','OCCUR_DT','OCCUR DT']),
              state: st
            });
          });

          return m;
        }).filter(Boolean);

        clusterer.addMarkers(markers);
        updateRightPanel(stats);
        console.log('[Sinkhole] cluster markers:', markers.length);
      }).catch(function(e){
        console.error('[Sinkhole] load error:', e);
      });
    }

    // ───────── 아이콘 애니메이션 ─────────
    function spawnClickAnim(marker){
      try{
        marker.setImage(MARKER_IMAGE_BIG);
        setTimeout(function(){ marker.setImage(MARKER_IMAGE); }, 800);
      }catch(e){ console.warn('[Sinkhole] marker animation failed:', e); }
    }

    // ───────── 말풍선 ─────────
    function showInfoAt(position, data){
      hideInfo();

      var el = document.createElement('div');
      el.style.cssText = [
        'transform:translate(-50%,-100%);',
        'background:#111827;color:#fff;border-radius:12px;',
        'padding:10px 12px;box-shadow:0 8px 22px rgba(0,0,0,.25);',
        'font-size:12px;line-height:1.45;white-space:nowrap;'
      ].join('');

      var occurText = formatDateLabel(data.occur) || '미상';
      var stateText = normalizeState(data.state) || '미상';

      el.innerHTML =
        '<div style="font-weight:700;margin-bottom:4px;">싱크홀 정보</div>'+
        '<div><b>발생일자</b> : ' + escapeHTML(occurText) + '</div>'+
        '<div><b>복구현황</b> : ' + escapeHTML(stateText) + '</div>';

      infoOverlay = new kakao.maps.CustomOverlay({
        position: position,
        content: el,
        yAnchor: 1,
        zIndex: 1000
      });
      infoOverlay.setMap(map);
    }

    function hideInfo(){
      if (infoOverlay){
        infoOverlay.setMap(null);
        infoOverlay = null;
      }
    }


// ───────── 우측 패널 구현 ─────────
function mountRightPanel(){
  if (document.getElementById('sinkholeStat')) {
    // 이미 만들어져 있으면 위치만 갱신할 수 있게
    document.getElementById('sinkholeStat').style.top = PANEL_TOP + 'px';
    return;
  }

var PANEL_BANNER_URL = CTX + '/resources/image/jaeminsinkhole_7.png';
var BANNER_HEIGHT = 180;   
var PANEL_TOP     = 240;   // ← 세로 위치 (20px 내려줌)
var PANEL_RIGHT   = 16;    // ← 가로 위치

var wrap = document.createElement('div');
wrap.id = 'sinkholeStat';
wrap.style.top   = PANEL_TOP + 'px';
wrap.style.right = PANEL_RIGHT + 'px';

wrap.innerHTML =
  '<img src="'+PANEL_BANNER_URL+'" alt="Sinkhole banner" '+
    'class="sinkhole-banner" '+
    'onerror="this.style.display=\'none\'" />'+
  '<div class="sinkhole-title">싱크홀 발생 현황</div>'+
  '<div id="sinkholeStatBody" class="sinkhole-body">'+
    '<div>총 건수: <b>0</b></div>'+
    '<div class="sinkhole-sub">복구현황별</div>'+
    '<ul id="sinkholeStateList" class="sinkhole-list"></ul>'+
  '</div>';

document.body.appendChild(wrap);
}



    function updateRightPanel(stats){
      var body = document.getElementById('sinkholeStatBody');
      var list = document.getElementById('sinkholeStateList');
      if (!body || !list) return;

      // 총 건수
      var totalEl = body.querySelector('div b');
      if (totalEl) totalEl.textContent = (stats.total || 0).toLocaleString();

      // 상태별
      list.innerHTML = '';
      var entries = Object.entries(stats.byState || {});
      if (!entries.length){
        list.innerHTML = '<li style="color:#6b7280;">(표시할 데이터 없음)</li>';
        return;
      }
      entries.sort(function(a,b){ return b[1]-a[1]; });
      entries.forEach(function(pair){
        var li = document.createElement('li');
        li.style.padding = '3px 0';
        li.innerHTML =
          '<span>'+escapeHTML(pair[0])+'</span>'+
          '<span style="float:right;font-weight:700;">'+pair[1].toLocaleString()+'</span>';
        list.appendChild(li);
      });
    }
  } // init 끝

  // ───────── 유틸 ─────────
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
  function getVal(obj, keys){
    if (!obj) return null;
    for (var i=0;i<keys.length;i++){
      if (Object.prototype.hasOwnProperty.call(obj, keys[i])) return obj[keys[i]];
    }
    var oks = Object.keys(obj);
    for (var j=0;j<keys.length;j++){
      var target = String(keys[j]).replace(/\s+|_/g,'').toLowerCase();
      var hit = oks.find(function(k){ return k.replace(/\s+|_/g,'').toLowerCase() === target; });
      if (hit) return obj[hit];
    }
    return null;
  }
  function normalizeState(s){
    if (!s) return '';
    var t = (''+s).trim();
    if (/완료/.test(t)) return '복구 완료';
    if (/임시/.test(t)) return '임시복구';
    if (/중/.test(t))   return '복구중';
    return t;
  }
  function formatDateLabel(v){
    if (!v) return '';
    try{
      var s = String(v).replace(/[^\d]/g,'');
      if (s.length >= 8) return s.slice(0,4)+'-'+s.slice(4,6)+'-'+s.slice(6,8);
      var d = new Date(v);
      if (!isNaN(d)) return [d.getFullYear(), pad(d.getMonth()+1), pad(d.getDate())].join('-');
    }catch(_){}
    return String(v);
  }
  function pad(n){ return (n<10?'0':'')+n; }
  function escapeHTML(s){
    return String(s||'').replace(/[&<>"']/g, function(m){
      return ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[m]);
    });
  }

})(this);
