/* eslint-disable */
(function () {
  'use strict';

  if (window.AppMap) init();
  else window.addEventListener('appmap:ready', init, { once: true });

  function init() {
    var App = window.AppMap;
    if (!App || !App.map) { setTimeout(init, 100); return; }
    var map = App.map;

    // ===== Guard =====
    var qs = new URLSearchParams(location.search || '');
    var LAYER = (qs.get('layer') || '').toLowerCase();
    var BODY_LAYER = (document.body.getAttribute('data-layer') || '').toLowerCase();
    var path = (location.pathname || '').toLowerCase();
    var IS_DUST_PATH  = /\/map\/dust$/.test(path) || /\/dust$/.test(path);
    var IS_DUST_QUERY = (LAYER === 'dust') || (BODY_LAYER === 'dust');
    var IS_DUST_CTX   = IS_DUST_PATH || IS_DUST_QUERY;

    // ===== Constants =====
    var CTX = (document.body && document.body.getAttribute('data-context-path')) || '';
    var HUD_TOP = 68 + 120;  // 페이지 기준 내려놓은 위치
    var HUD_RIGHT = 12;

    // ===== Helpers =====
    function numOrNull(v){ var n = Number(v); return isNaN(n) ? null : n; }
    function getPM10Value(d){
      var t = numOrNull(d.pm10); if (t !== null) return t;
      t = numOrNull(d.PM10);     if (t !== null) return t;
      t = numOrNull(d.avg);      if (t !== null) return t;
      t = numOrNull(d.AVG);      if (t !== null) return t;
      return null;
    }
    function whichMetricUsed(list){
      for (var i=0;i<list.length;i++){
        var v = numOrNull(list[i] && (list[i].pm10!=null ? list[i].pm10 : list[i].PM10));
        if (v !== null) return 'PM10';
      }
      return 'AVG';
    }

    // ===== HUD =====
    function mountHud(){
      if (document.getElementById('dustHud')) return;

      var hud = document.createElement('div');
      hud.id = 'dustHud';
      hud.style.right = HUD_RIGHT + 'px';
      hud.style.top = HUD_TOP + 'px';

      var box = document.createElement('div');
      box.className = 'rfilter';
      box.innerHTML = [
        '<h4>대기유형</h4>',
        '<button class="flt" data-air="ALL">전체</button>',
        '<button class="flt" data-air="교외대기">교외대기</button>',
        '<button class="flt" data-air="도로변 대기">도로변 대기</button>',
        '<button class="flt" data-air="도시대기">도시대기</button>'
      ].join('');
      hud.appendChild(box);
      document.body.appendChild(hud);

      hud.addEventListener('click', function(e){
        var b = e.target.closest('.flt');
        if (!b) return;
        var t = b.getAttribute('data-air');
        if (t) {
          setType(t);
          syncURL(t);
          highlightActive(t);
        }
      });
    }

    // ===== Mascot + Balloon =====
    function mountMascot(){
      if (document.getElementById('dustMascot')) return;

      var hudEl = document.getElementById('dustHud');
      var hudHeight = hudEl ? hudEl.offsetHeight : 160;
      var top = Math.max(20, HUD_TOP - hudHeight + 20); // HUD 바로 위에서 20px 내려오기

      var mascotRight  = HUD_RIGHT + 16;
      var balloonRight = HUD_RIGHT + 96;

      var img = document.createElement('img');
      img.id = 'dustMascot';
      img.src = CTX + '/resources/image/jaeminsinkhole_5.png';
      img.alt = 'dust mascot';
      img.style.right = mascotRight + 'px';
      img.style.top    = top + 'px';
      document.body.appendChild(img);

var balloon = document.createElement('div');
balloon.id = 'dustBalloon';
balloon.style.right = balloonRight + 'px';
balloon.style.top    = top + 'px';
balloon.style.zIndex = '11';
balloon.style.display = 'block';

// 여러 개 문구를 배열로 넣으면 각각의 말풍선으로 생성
var msgs = [
  '너네 마을도 황사 심해?',
  'ㅋ..콜록콜록.. 마스크 쓰고 다녀'
];

// 공용 말풍선 생성 함수 (기존 스타일 그대로)
function makeBubble(text){
  var b = document.createElement('div');
  b.className = 'bubble';           // ← CSS에서 스타일링
  b.textContent = text;
  return b;
}

// 배열을 돌며 하나씩 추가
msgs.forEach(function(txt){ balloon.appendChild(makeBubble(txt)); });

// 꼬리는 컨테이너 기준 오른쪽에 고정 (두 말풍선 모두에 적용되는 느낌)
var tail = document.createElement('div');
tail.className = 'tail';            // ← CSS에 이미 있음
balloon.appendChild(tail);

document.body.appendChild(balloon);

      // Mascot click -> HUD toggle
      img.addEventListener('click', function(){
        var hud = document.getElementById('dustHud');
        if (!hud) return;
        var hidden = (hud.style.display === 'none' || !hud.style.display);
        hud.style.display = hidden ? 'block' : 'none';
        balloon.style.opacity = hidden ? '0.85' : '0.55';
      });
    }
    function unmountMascot(){
      var m = document.getElementById('dustMascot'); if (m && m.parentNode) m.parentNode.removeChild(m);
      var b = document.getElementById('dustBalloon'); if (b && b.parentNode) b.parentNode.removeChild(b);
    }

    // ===== Legend =====
    function mountLegend(){
      if (document.getElementById('dustLegend')) return;

      var el = document.createElement('div');
      el.id = 'dustLegend';
      el.innerHTML =
        '<div id="dustLegendTitle" style="font-weight:600;margin-bottom:6px;">PM10 (μg/m³)</div>' +
        '<div class="bar"></div>' +
        '<div class="scale"><span id="dustLegendMin">min</span><span id="dustLegendMid">median</span><span id="dustLegendMax">max</span></div>';
      document.body.appendChild(el);
    }
    function updateLegendFrom(list){
      try {
        if (!Array.isArray(list) || list.length === 0) return;
        var metric = whichMetricUsed(list);
        var vals = [];
        for (var i=0;i<list.length;i++){
          var d = list[i];
          var v = (metric === 'PM10')
            ? numOrNull(d.pm10!=null ? d.pm10 : d.PM10)
            : numOrNull(d.avg !=null ? d.avg  : d.AVG);
          if (v !== null) vals.push(v);
        }
        if (vals.length === 0) return;
        vals.sort(function(a,b){return a-b;});
        var t = document.getElementById('dustLegendTitle');
        var mn= document.getElementById('dustLegendMin');
        var md= document.getElementById('dustLegendMid');
        var mx= document.getElementById('dustLegendMax');
        if (t)  t.textContent = (metric==='PM10'?'PM10':'AVG') + ' (μg/m³)';
        if (mn) mn.textContent = String(vals[0]);
        if (md) md.textContent = String(vals[Math.floor(vals.length/2)]);
        if (mx) mx.textContent = String(vals[vals.length-1]);
      } catch(e){ console.warn('[dust] updateLegendFrom failed', e); }
    }

    // ===== Button highlight =====
    function highlightActive(airType){
      var k = canonicalType(airType);
      var hud = document.getElementById('dustHud');
      if (!hud) return;
      hud.querySelectorAll('.flt').forEach(function(b){
        var same = canonicalType(b.getAttribute('data-air')) === k;
        b.classList.toggle('active', same);
      });
    }

    // ===== URL sync =====
    function syncURL(airType){
      try {
        var url = new URL(window.location.href);
        var s = url.searchParams;
        s.set('layer', 'dust');
        if (airType) s.set('airType', airType); else s.delete('airType');
        history.replaceState(null, '', url.pathname + '?' + s.toString());
      } catch (e) { console.warn('[dust] syncURL failed', e); }
    }

    // ===== Heatmap Canvas =====
    var heatCanvas = document.createElement('canvas');
    heatCanvas.style.position = 'absolute';
    heatCanvas.style.left = 0;
    heatCanvas.style.top  = 0;
    heatCanvas.style.pointerEvents = 'none';
    heatCanvas.style.zIndex = 7;
    map.getNode().appendChild(heatCanvas);

    var heat = null, active = false, currentType = 'ALL', dataCache = null;

    function ensureHeat(){
      var node = map.getNode();
      if (!node) return;
      var w = node.clientWidth, h = node.clientHeight;
      if (heatCanvas.width !== w || heatCanvas.height !== h) {
        heatCanvas.width = w; heatCanvas.height = h;
      }
      if (!heat) {
        if (!window.simpleheat) { console.warn('[dust] simpleheat 미로딩'); return; }
        heat = window.simpleheat(heatCanvas);
        heat.gradient({
          0.00: '#b3e5fc',
          0.25: '#4fc3f7',
          0.50: '#4caf50',
          0.75: '#ffb74d',
          1.00: '#f4511e'
        });
        heat.max(1);
      }
    }
    function clearHeat(){ var c=heatCanvas.getContext('2d'); if (!c) return; c.clearRect(0,0,heatCanvas.width,heatCanvas.height); }

    function weightPM10(pm10){
      var v = Number(pm10); if (isNaN(v)) return 0;
      var w;
      if (v <= 30) w=0.06; else if (v<=50) w=0.18; else if (v<=80) w=0.32;
      else if (v<=120) w=0.48; else if (v<=150) w=0.60; else if (v<=200) w=0.72;
      else if (v<=300) w=0.84; else w=0.90;
      if (currentType === '교외대기') w = Math.min(1, w*1.12);
      return w;
    }
    function drawHeat(list){
      ensureHeat(); if (!heat) return;
      var proj = map.getProjection(), pts=[];
      for (var i=0;i<list.length;i++){
        var d=list[i], lat=Number(d.lat!=null?d.lat:d.LAT), lon=Number(d.lon!=null?d.lon:d.LON);
        if (isNaN(lat)||isNaN(lon)) continue;
        var p = proj.containerPointFromCoords(new kakao.maps.LatLng(lat,lon));
        if (!p) continue;
        pts.push([p.x,p.y,weightPM10(getPM10Value(d))]);
      }
      heat.data(pts);
      heat.radius(24,34);
      heat.draw(currentType==='교외대기'?0.36:0.33);
    }

    // ===== Fetch =====
    function canonicalType(t){
      if (!t) return 'ALL';
      var k = String(t).replace(/\s+/g,'').toLowerCase();
      if (k==='all' || t==='전체') return 'ALL';
      if (k==='교외대기') return '교외대기';
      if (k==='도로변대기') return '도로변 대기';
      if (k==='도시대기') return '도시대기';
      return t;
    }
    function buildUrl(airType, bbox, limit){
      var s = new URLSearchParams({ airType: airType, limit: (limit || 1000) });
      if (bbox && isFinite(bbox.minLat) && isFinite(bbox.maxLat) && isFinite(bbox.minLon) && isFinite(bbox.maxLon)) {
        s.set('minLat', bbox.minLat); s.set('maxLat', bbox.maxLat);
        s.set('minLon', bbox.minLon); s.set('maxLon', bbox.maxLon);
      }
      return CTX + '/dust/latest?' + s.toString();
    }
    function fetchAndRender(airType){
      var bbox = (App && App.getBBox) ? App.getBBox() : null;
      var url  = buildUrl(airType, bbox, 1200);
      console.log('[dust] fetch:', url);
      return fetch(url, { headers: { 'Accept': 'application/json' } })
        .then(function(r){ if (!r.ok) throw new Error('HTTP '+r.status); return r.json(); })
        .then(function(list){
          dataCache = Array.isArray(list)? list : [];
          clearHeat(); drawHeat(dataCache);
          updateLegendFrom(dataCache);
          highlightActive(currentType);
        })
        .catch(function(e){ console.error('[dust] fetch error', e); });
    }

    // ===== Events =====
    function debounce(fn, ms){ var t; return function(){ var a=arguments,c=this; clearTimeout(t); t=setTimeout(function(){ fn.apply(c,a); }, ms); }; }
    var redraw = debounce(function(){
      if (!active) return;
      ensureHeat();
      if (dataCache) drawHeat(dataCache);
      fetchAndRender(currentType);
    }, 250);

    kakao.maps.event.addListener(map, 'idle', redraw);
    window.addEventListener('resize', function(){ if (active) redraw(); });

    // ===== Activate/Deactivate =====
    var active=false, currentType='ALL', dataCache=null;
    function activate(opts){
      active = true;
      heatCanvas.style.display = 'block';

      if (IS_DUST_CTX) {
        mountHud();
        var hud = document.getElementById('dustHud'); if (hud) hud.style.display = 'block';
        mountMascot();
        mountLegend();
        var lg = document.getElementById('dustLegend'); if (lg) lg.style.display = 'block';
      }

      if (opts && typeof opts.airType === 'string') currentType = canonicalType(opts.airType);
      highlightActive(currentType);

      var bounds = new kakao.maps.LatLngBounds();
      bounds.extend(new kakao.maps.LatLng(33.0, 124.5));
      bounds.extend(new kakao.maps.LatLng(38.7, 132.0));
      map.setBounds(bounds);
      map.setZoomable(false);
      map.setDraggable(false);

      ensureHeat();
      fetchAndRender(currentType);
    }
    function deactivate(){
      active=false;
      heatCanvas.style.display='none';
      clearHeat();
      map.setZoomable(true);
      map.setDraggable(true);
      var hud = document.getElementById('dustHud'); if (hud) hud.style.display='none';
      var lg  = document.getElementById('dustLegend'); if (lg) lg.style.display='none';
      unmountMascot();
    }
    function setType(type){
      currentType = canonicalType(type);
      syncURL(currentType);
      highlightActive(currentType);
      if (active) fetchAndRender(currentType);
    }

    // Register
    App.registerLayer('dust', {
      activate: activate, deactivate: deactivate, setType: setType,
      refresh: function(){ fetchAndRender(currentType); }
    });

    // Global API
    window.DustLayer = window.DustLayer || {};
    window.DustLayer.activate   = function(opts){ activate(opts || {}); };
    window.DustLayer.deactivate = function(){ deactivate(); };
    window.DustLayer.setType    = setType;
    window.DustLayer.refresh    = function(){ fetchAndRender(currentType); };

    // Auto boot
    (function autoBoot(){
      if (!(IS_DUST_PATH || IS_DUST_QUERY)) return;
      var p = new URLSearchParams(location.search);
      var airType = p.get('airType') || 'ALL';
      console.log('[dust] auto-activate:', IS_DUST_PATH ? 'path' : 'query', ', airType=', airType);
      window.DustLayer.activate({ airType: airType });
    })();
  }
})();
