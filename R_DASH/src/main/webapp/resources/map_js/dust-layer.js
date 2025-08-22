// /resources/map_js/dust-layer.js
(function () {
  'use strict';

  if (window.AppMap) init();
  else window.addEventListener('appmap:ready', init, { once: true });

  function init() {
    var App = window.AppMap;
    if (!App || !App.map) { setTimeout(init, 100); return; }
    var map = App.map;

    function mountHud(){
      if (document.getElementById('dustHud')) return;
      var hud = document.createElement('div');
      hud.id = 'dustHud';
      hud.style.position = 'fixed';
      hud.style.right = '12px';
      hud.style.top = '68px';
      hud.style.zIndex = '10';
      hud.style.display = 'none';
      hud.innerHTML =
        '<div class="rfilter" id="dustFilter" style="width:160px;background:#fff;border:1px solid #ddd;border-radius:10px;padding:10px;box-shadow:0 6px 18px rgba(0,0,0,.08)">' +
        '  <h4 style="margin:0 0 8px;font-size:13px;">대기유형</h4>' +
        '  <button class="flt" data-air="ALL" style="display:block;width:100%;margin:6px 0;padding:6px 8px;font-size:12px;border:1px solid #eee;border-radius:6px;background:#fff;cursor:pointer">전체</button>' +
        '  <button class="flt" data-air="교외대기" style="display:block;width:100%;margin:6px 0;padding:6px 8px;font-size:12px;border:1px solid #eee;border-radius:6px;background:#fff;cursor:pointer">교외대기</button>' +
        '  <button class="flt" data-air="도로변 대기" style="display:block;width:100%;margin:6px 0;padding:6px 8px;font-size:12px;border:1px solid #eee;border-radius:6px;background:#fff;cursor:pointer">도로변 대기</button>' +
        '  <button class="flt" data-air="도시대기" style="display:block;width:100%;margin:6px 0;padding:6px 8px;font-size:12px;border:1px solid #eee;border-radius:6px;background:#fff;cursor:pointer">도시대기</button>' +
        '</div>';
      document.body.appendChild(hud);
    }
    mountHud();

    var heatCanvas = document.createElement('canvas');
    heatCanvas.style.position = 'absolute';
    heatCanvas.style.left = 0;
    heatCanvas.style.top  = 0;
    heatCanvas.style.pointerEvents = 'none';
    heatCanvas.style.zIndex = 7;
    map.getNode().appendChild(heatCanvas);

    var heat = null;
    var active = false;
    var currentType = 'ALL';
    var dataCache = null;

    function ensureHeat(){
      var w = map.getNode().clientWidth, h = map.getNode().clientHeight;
      if (heatCanvas.width !== w || heatCanvas.height !== h) {
        heatCanvas.width = w; heatCanvas.height = h;
      }
      if (!heat && window.simpleheat) {
        heat = window.simpleheat(heatCanvas);
        heat.gradient({ 0.0:'#c8f6d7', 0.35:'#f9f3c2', 0.65:'#f6d2a8', 1.0:'#f2a6a6' });
        heat.max(1);
      }
    }
    function clearHeat(){
      var ctx = heatCanvas.getContext('2d'); if (!ctx) return;
      ctx.clearRect(0,0,heatCanvas.width,heatCanvas.height);
    }
    function norm(pm10){
      var v = Number(pm10); if (isNaN(v)) return 0;
      var w = (v - 40) / 120; if (w<0) w=0; if (w>1) w=1; return w;
    }
    function drawHeat(list){
      ensureHeat();
      if (!heat) return;
      var proj = map.getProjection();
      var pts = [];
      for (var i=0;i<list.length;i++){
        var d = list[i], lat = Number(d.lat), lon = Number(d.lon);
        if (isNaN(lat)||isNaN(lon)) continue;
        var pm10 = d.pm10!=null && d.pm10!=='' ? d.pm10 : (d.avg!=null ? d.avg : null);
        var p = proj.containerPointFromCoords(new kakao.maps.LatLng(lat, lon));
        pts.push([p.x, p.y, norm(pm10)]);
      }
      heat.data(pts);
      heat.radius(18, 22);
      heat.draw(0.38);
    }

    function buildUrl(airType, bbox, limit){
      var ctx = document.body.getAttribute('data-context-path') || '';
      var qs = new URLSearchParams({ airType: airType, limit: limit || 1000 });
      if (bbox && isFinite(bbox.minLat) && isFinite(bbox.maxLat) && isFinite(bbox.minLon) && isFinite(bbox.maxLon)) {
        qs.set('minLat', bbox.minLat);
        qs.set('maxLat', bbox.maxLat);
        qs.set('minLon', bbox.minLon);
        qs.set('maxLon', bbox.maxLon);
      }
      return ctx + '/dust/latest?' + qs.toString();
    }
    function fetchAndRender(airType){
      var bbox = (App && App.getBBox) ? App.getBBox() : null;
      var url  = buildUrl(airType, bbox, 1200);
      return fetch(url)
        .then(function(r){ if (!r.ok) throw new Error('HTTP '+r.status); return r.json(); })
        .then(function(list){ dataCache = Array.isArray(list)? list : []; clearHeat(); drawHeat(dataCache); })
        .catch(function(e){ console.error('[dust] fetch error', e); });
    }

    function debounce(fn, ms){ var t; return function(){ var a=arguments,c=this; clearTimeout(t); t=setTimeout(function(){ fn.apply(c,a); }, ms); }; }
    var redraw = debounce(function(){
      if (!active) return;
      ensureHeat();
      if (dataCache) drawHeat(dataCache);
      fetchAndRender(currentType);
    }, 250);
    kakao.maps.event.addListener(map, 'idle', redraw);
    window.addEventListener('resize', function(){ if (active) redraw(); });

    function activate(opts){
      active = true;
      heatCanvas.style.display = 'block';
      var hud = document.getElementById('dustHud'); if (hud) hud.style.display = 'block';
      if (opts && typeof opts.airType === 'string') currentType = opts.airType;

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
      active = false;
      heatCanvas.style.display = 'none';
      var hud = document.getElementById('dustHud'); if (hud) hud.style.display = 'none';
      clearHeat();
      map.setZoomable(true);
      map.setDraggable(true);
    }

    App.registerLayer('dust', {
      activate: activate,
      deactivate: deactivate,
      setType: function(type){ currentType = type || 'ALL'; fetchAndRender(currentType); },
      refresh: function(){ fetchAndRender(currentType); }
    });

    // 클릭 바인딩은 url-sync.js에서 처리
  }
})();
