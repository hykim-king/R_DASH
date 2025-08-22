// map_js/dust-layer.js
// 황사 데이터 히트맵 렌더링 + 우측 HUD(대기유형 버튼) 생성/표시 제어

(function () {
  'use strict';

  // app-map 준비되면 시작
  if (window.AppMap) init();
  else window.addEventListener('appmap:ready', init, { once: true });

  function init() {
    var App = window.AppMap;
    var map = App.map;

    // ====== HUD(UI) 동적 생성 ======
    function mountHud(){
      if (document.getElementById('dustHud')) return;
      var hud = document.createElement('div');
      hud.id = 'dustHud';
      hud.style.position = 'fixed';
      hud.style.right = '12px';
      hud.style.top = '68px'; // header(56)+12
      hud.style.zIndex = '10';
      hud.style.display = 'none'; // 활성화 시에만 보임

      hud.innerHTML =
        '<div class="rfilter" id="dustFilter" style="width:160px;background:#fff;border:1px solid #ddd;border-radius:10px;padding:10px;box-shadow:0 6px 18px rgba(0,0,0,.08)">' +
        '  <h4 style="margin:0 0 8px;font-size:13px;">대기유형</h4>' +
        '  <button class="flt" data-air="ALL" style="display:block;width:100%;margin:6px 0;padding:6px 8px;font-size:12px;border:1px solid #eee;border-radius:6px;background:#fff;cursor:pointer">전체</button>' +
        '  <button class="flt" data-air="교외대기" style="display:block;width:100%;margin:6px 0;padding:6px 8px;font-size:12px;border:1px solid #eee;border-radius:6px;background:#fff;cursor:pointer">교외대기</button>' +
        '  <button class="flt" data-air="도로변대기" style="display:block;width:100%;margin:6px 0;padding:6px 8px;font-size:12px;border:1px solid #eee;border-radius:6px;background:#fff;cursor:pointer">도로변대기</button>' +
        '  <button class="flt" data-air="도시대기" style="display:block;width:100%;margin:6px 0;padding:6px 8px;font-size:12px;border:1px solid #eee;border-radius:6px;background:#fff;cursor:pointer">도시대기</button>' +
        '</div>';
      document.body.appendChild(hud);
    }
    mountHud();

    // ====== Heatmap 캔버스 ======
    var heatCanvas = document.createElement('canvas');
    heatCanvas.style.position = 'absolute';
    heatCanvas.style.left = 0;
    heatCanvas.style.top  = 0;
    heatCanvas.style.pointerEvents = 'none';
    heatCanvas.style.zIndex = 7;
    map.getNode().appendChild(heatCanvas);

    var heat = null;                   // simpleheat 인스턴스
    var active = false;
    var currentType = 'ALL';
    var dataCache = null;              // 마지막 데이터

    function ensureHeat(){
      var w = map.getNode().clientWidth, h = map.getNode().clientHeight;
      if (heatCanvas.width !== w || heatCanvas.height !== h) {
        heatCanvas.width = w; heatCanvas.height = h;
      }
      if (!heat && window.simpleheat) {
        heat = window.simpleheat(heatCanvas);
        heat.gradient({
          0.0:'#c8f6d7', 0.35:'#f9f3c2', 0.65:'#f6d2a8', 1.0:'#f2a6a6'
        });
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

    // ====== 데이터 호출 ======
    function buildUrl(airType, bbox, limit){
      var ctx = document.body.getAttribute('data-context-path') || '';
      var qs = new URLSearchParams({
        airType: airType,
        minLat: bbox.minLat, maxLat: bbox.maxLat,
        minLon: bbox.minLon, maxLon: bbox.maxLon,
        limit: limit || 1000
      });
      return ctx + '/dust/latest?' + qs.toString();
    }
    function fetchAndRender(airType){
      var bbox = App.getBBox();
      if (airType !== 'ALL'){
        return fetch(buildUrl(airType, bbox, 1200))
          .then(function(r){ return r.json(); })
          .then(function(list){ dataCache=list; clearHeat(); drawHeat(list); })
          .catch(function(e){ console.error('[dust] fetch', e); });
      }
      var types = ['교외대기','도로변대기','도시대기'];
      return Promise.all(types.map(function(t){ return fetch(buildUrl(t, bbox, 600)).then(function(r){return r.json();}); }))
        .then(function(rs){ var merged=[].concat(rs[0],rs[1],rs[2]); dataCache=merged; clearHeat(); drawHeat(merged); })
        .catch(function(e){ console.error('[dust] fetch all', e); });
    }

    // 지도 이동 시 재그리기
    function debounce(fn, ms){ var t; return function(){ var a=arguments,c=this; clearTimeout(t); t=setTimeout(function(){ fn.apply(c,a); }, ms); }; }
    var redraw = debounce(function(){
      if (!active) return;
      ensureHeat();
      if (dataCache) drawHeat(dataCache);
      fetchAndRender(currentType);
    }, 250);
    kakao.maps.event.addListener(map, 'idle', redraw);
    window.addEventListener('resize', function(){ if (active) redraw(); });

// ====== 레이어 수명주기 ======
function activate(opts){
  active = true;
  heatCanvas.style.display = 'block';
  var hud = document.getElementById('dustHud'); if (hud) hud.style.display = 'block';

  if (opts && opts.airType) currentType = opts.airType;

  // ✅ 대한민국 전체(제주 포함)로 화면 맞추기 + 인터랙션 잠금
  var bounds = new kakao.maps.LatLngBounds();
  // 남서쪽(제주 바다 포함) ~ 북동쪽(강원/동해 쪽)
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
      // 잠금 해제
      map.setZoomable(true);
      map.setDraggable(true);
    }

    // 레이어 등록
    App.registerLayer('dust', { activate: activate, deactivate: deactivate });

    // ====== HUD 버튼 동작 (URL 동기화는 url-sync.js가 처리) ======
    document.addEventListener('click', function(e){
      var btn = e.target.closest('#dustFilter .flt');
      if (!btn) return;
      var air = btn.getAttribute('data-air');
      if (!window.AppMap) return;
      window.AppMap.showOnly('dust', { airType: air });
    });
  }
})();
