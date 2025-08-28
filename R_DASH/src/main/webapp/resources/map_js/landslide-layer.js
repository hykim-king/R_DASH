/* /resources/map_js/landslide-layer.js */
/* eslint-disable */
(function (global) {
  'use strict';

  // ---- "산사태" 페이지만 동작하도록 가드 ----
  var qs = new URLSearchParams((typeof location !== 'undefined' && location.search) ? location.search : '');
  var LAYER = (qs.get('layer') || '').toLowerCase();
  var BODY_LAYER = (document.body && (document.body.getAttribute('data-layer') || '')).toLowerCase();
  var IS_ME = (LAYER === 'landslide') || (BODY_LAYER === 'landslide');
  if (!IS_ME) { console.log('[LandslideHeat] skip (layer != landslide)'); return; }

  // ---- AppMap 준비되면 시작 ----
  if (global.AppMap) init(); else global.addEventListener('appmap:ready', init, { once: true });

  function init () {
    var App = global.AppMap || {};
    var map = App.map;
    if (!map || !global.kakao || !kakao.maps) {
      console.error('[LandslideHeat] kakao map not found');
      return;
    }

    // heatmap.js 로드 후 레이어 등록
    loadHeatmapLib(function ok () {
      var layer = createLandslideHeatLayer(App);
      if (typeof App.register === 'function') {
        App.register('landslide', layer);
        console.log('[LandslideHeat] registered');
      } else {
        console.warn('[LandslideHeat] App.register not found');
      }
    });
  }

  // ======================================================================
  // heatmap.js(h337) 동적 로더
  // ======================================================================
  function loadHeatmapLib(cb) {
    if (global.h337) { cb(); return; }
    var BASE = (document.body && document.body.getAttribute('data-context-path')) || ''; // ex) "/ehr"
    var src = BASE + '/resources/lib/heatmap.min.js';
    var s = document.createElement('script');
    s.src = src; s.async = true;
    s.onload = function(){ if (global.h337) cb(); else console.error('[LandslideHeat] h337 not available after load'); };
    s.onerror = function(){ console.error('[LandslideHeat] failed to load', src); };
    document.head.appendChild(s);
  }

  // ======================================================================
  // 레이어 팩토리 (히트맵 + 최근 사건 포인트 + HUD/필터)
  // ======================================================================
  function createLandslideHeatLayer (App) {
    var map = App.map;
    var BASE = (document.body && document.body.getAttribute('data-context-path')) || ''; // ex) "/ehr"
    var API = {
      byRegion: BASE + '/landslide/byRegionInBBox.do',
      points:   BASE + '/landslide/points.do'
    };

    // ---------- 컨테이너 & heatmap 인스턴스 ----------
    var mapEl = document.querySelector('#map') || document.body;

    var container = document.createElement('div');
    container.id = 'ls-heat-container';
    mapEl.appendChild(container);

    var heat = h337.create({
      container: container,
      radius: 32,
      maxOpacity: 0.85,
      minOpacity: 0.15,
      blur: 0.85,
      backgroundColor: 'rgba(0,0,0,0)'
    });

    var pointRoot = document.createElement('div');
    pointRoot.id = 'ls-point-root';
    mapEl.appendChild(pointRoot);

    // 범례/필터/HUD
    ensureLegend();
    ensureControls();

    // ---------- 내부 상태 ----------
    var idleListener = null, resizeObserver = null, idleTimer = null;
    var lastAggRows = null;
    var lastPointRows = null;
    var dotOverlays = [];

    var state = { recentDays: 30, from: null, to: null, level: 'ALL' };

    // ---------- 유틸 ----------
    function toNum(a,b,def){ var v = (a!=null?a:b); var n = Number(v); return isNaN(n)?(def==null?NaN:def):n; }
    function normalizeDate(s){
      if (!s) return '';
      s = String(s).trim();
      var m = s.match(/^(\d{4})[-\/]?(\d{2})[-\/]?(\d{2})/);
      return m ? [m[1], m[2], m[3]].join('-') : '';
    }
    function parseDate(s){ if(!s) return null; var d=new Date(s); return isNaN(d)?null:d; }

    function normAgg(r){
      return { name: r.name || r.NAME, lat: toNum(r.lat, r.LAT), lon: toNum(r.lon, r.LON), cnt: toNum(r.cnt, r.CNT, 0) };
    }
    function normPoint(r){
      var levelRaw = (r.level || r.LEVEL || r.alert || r.ALERT || r.status || r.STATUS || '').toString().trim();
      var dateRaw  = (r.date  || r.DATE  || r.occurYmd || r.OCCUR_YMD || r.occur_dt || r.OCCUR_DT || '').toString().trim();
      return {
        id:    r.id || r.ID || r.seq || r.SEQ || null,
        lat:   toNum(r.lat, r.LAT),
        lon:   toNum(r.lon, r.LON),
        level: levelRaw || '기타',
        date:  normalizeDate(dateRaw),
        name:  r.name || r.NAME || r.addr || r.ADDR || ''
      };
    }

    function getBBoxParams(extra) {
      var b = map.getBounds(); var sw = b.getSouthWest(), ne = b.getNorthEast();
      var p = new URLSearchParams({
        minLat: sw.getLat(), maxLat: ne.getLat(),
        minLon: sw.getLng(), maxLon: ne.getLng()
      });
      if (extra && extra.level && extra.level !== 'ALL') p.set('level', extra.level);
      if (extra && (extra.from || extra.to)) {
        if (extra.from) p.set('from', extra.from);
        if (extra.to)   p.set('to', extra.to);
      } else if (extra && extra.recentDays != null) {
        p.set('recentDays', String(extra.recentDays));
      }
      return p.toString();
    }

    function fetchJSON(url) {
      return fetch(url, { headers: { 'Accept': 'application/json' } })
        .then(function (r) {
          var ct = (r.headers.get('content-type') || '').toLowerCase();
          if (!r.ok) throw new Error('HTTP ' + r.status);
          if (ct.indexOf('application/json') === -1) {
            return r.text().then(function (t) {
              console.error('[LandslideHeat] non-JSON head:', t.slice(0, 300));
              throw new Error('Bad content-type ' + ct);
            });
          }
          return r.json();
        });
    }

    function fetchAgg(extra){ return fetchJSON(API.byRegion + '?' + getBBoxParams(extra)); }
    function fetchPoints(extra){ return fetchJSON(API.points + '?' + getBBoxParams(extra)); }

    // ---------- 좌표 변환 ----------
    function aggToHeat(aggRows) {
      if (!aggRows || !aggRows.length) return { max: 0, data: [] };
      var proj = map.getProjection();
      var rect = container.getBoundingClientRect();
      var max = 0, data = [];
      for (var i=0;i<aggRows.length;i++) {
        var r = normAgg(aggRows[i]);
        if (isNaN(r.lat) || isNaN(r.lon)) continue;
        var pt = proj.containerPointFromCoords(new kakao.maps.LatLng(r.lat, r.lon));
        var x = Math.round(pt.x), y = Math.round(pt.y);
        var v = Math.max(1, r.cnt);
        if (v > max) max = v;
        if (x < -50 || y < -50 || x > rect.width + 50 || y > rect.height + 50) continue;
        data.push({ x:x, y:y, value:v });
      }
      return { max: max, data: data };
    }

    // ---------- 렌더 ----------
    function renderHeatmap(aggRows) {
      lastAggRows = aggRows || [];
      var set = aggToHeat(lastAggRows);
      heat.setData(set);
      adjustRadius(set);
    }
    function adjustRadius() {
      var zoom = map.getLevel(); // 숫자 클수록 멀리
      var base = 16;
      var scaled = Math.max(14, Math.min(60, base + (9 - Math.min(zoom, 9)) * 6));
      heat.configure({ radius: scaled });
    }

    // ---------- 포인트 ----------
    function clearDots(){
      for (var i=0;i<dotOverlays.length;i++){ dotOverlays[i].setMap(null); }
      dotOverlays.length = 0;
    }
    function colorForLevel(level){
      var s = (level||'').toString();
      if (s.indexOf('경보')>-1) return '#e11d48';
      if (s.indexOf('주의')>-1) return '#f97316';
      return '#7c3aed';
    }
    function drawDots(pointRows){
      clearDots();
      if (!pointRows || !pointRows.length) return;
      var zoom = map.getLevel();
      var size = (zoom>=9?6:(zoom>=7?8:(zoom>=5?10:12)));
      for (var i=0;i<pointRows.length;i++){
        var p = normPoint(pointRows[i]);
        if (isNaN(p.lat) || isNaN(p.lon)) continue;

        var el = document.createElement('div');
        el.className = 'ls-dot';
        el.style.cssText = [
          'width:'+size+'px;height:'+size+'px;',
          'margin-left:' + (-size/2) + 'px;',
          'margin-top:'  + (-size/2) + 'px;',
          'box-shadow:0 0 10px '+colorForLevel(p.level)+', 0 0 20px '+colorForLevel(p.level),
          'background:'+colorForLevel(p.level)+';'
        ].join('');

        el.title = (p.date ? ('발생일: '+p.date+'\n') : '')
                 + (p.level ? ('등급: '+p.level+'\n') : '')
                 + (p.name ? ('위치: '+p.name) : '');

        var overlay = new kakao.maps.CustomOverlay({
          position: new kakao.maps.LatLng(p.lat, p.lon),
          content: el, yAnchor: 0.5, xAnchor: 0.5, zIndex: 1000
        });
        overlay.setMap(map);
        dotOverlays.push(overlay);
      }
    }

    // ---------- 클라이언트 필터 ----------
    function applyClientFilter(rows, extra){
      if (!rows) return [];
      var out = [];
      var from = extra && extra.from ? parseDate(extra.from) : null;
      var to   = extra && extra.to   ? parseDate(extra.to)   : null;
      var now  = new Date();

      for (var i=0;i<rows.length;i++){
        var p = normPoint(rows[i]);

        if (extra && extra.level && extra.level !== 'ALL'){
          if ((p.level||'').indexOf(extra.level) === -1) continue;
        }

        var d = parseDate(p.date);
        if (from || to){
          if (d){
            if (from && d < from) continue;
            if (to   && d > to)   continue;
          }
        } else if (extra && extra.recentDays != null){
          if (d){
            var diff = (now - d) / 86400000;
            if (diff > Number(extra.recentDays)) continue;
          }
        }
        out.push(rows[i]);
      }
      return out;
    }

    // ---------- HUD (필요 시 확장) ----------
    function ensureHUD(){
      if (document.getElementById('ls-hud')) return;
      var hudEl = document.createElement('div');
      hudEl.id = 'ls-hud';
      hudEl.innerHTML =
        '<div style="font-weight:800;margin-bottom:6px">산사태 요약(현재 화면)</div>'+
        '<div id="ls-hud-total">표시 건수: -</div>'+
        '<div id="ls-hud-split">경보 0 · 주의보 0 · 기타 0</div>'+
        '<div id="ls-hud-recent">최근 발생일: -</div>';
      (document.querySelector('#map') || document.body).appendChild(hudEl);
    }
    function updateHUD(pointRows){
      var total = pointRows ? pointRows.length : 0;
      var warn=0, watch=0, etc=0, recent = null;
      for (var i=0;i<(pointRows||[]).length;i++){
        var p = normPoint(pointRows[i]);
        if ((p.level||'').indexOf('경보')>-1) warn++;
        else if ((p.level||'').indexOf('주의')>-1) watch++;
        else etc++;
        var d = parseDate(p.date);
        if (d && (!recent || d>recent)) recent = d;
      }
      var recentStr = recent ? recent.toISOString().slice(0,10) : '-';
      var t = document.getElementById('ls-hud-total');
      var s = document.getElementById('ls-hud-split');
      var r = document.getElementById('ls-hud-recent');
      if (t) t.textContent = '표시 건수: ' + total;
      if (s) s.textContent = '경보 ' + warn + ' · 주의보 ' + watch + ' · 기타 ' + etc;
      if (r) r.textContent = '최근 발생일: ' + recentStr;
    }

    // ---------- 범례(배너 카드) ----------
    function ensureLegend(){
      if (document.getElementById('ls-legend')) return;
      var el = document.createElement('div');
      el.id = 'ls-legend';

      var heroSrc = BASE + '/resources/image/jaeminsinkhole_8.png';
      el.innerHTML =
        '<div class="legend-hero"><img src="'+heroSrc+'" alt="산사태 배너"></div>'+
        '<div class="legend-body">'+
        '  <h4 class="legend-title">산사태 밀도(히트맵)</h4>'+
        '  <div class="legend-section-title">밀도 스케일</div>'+
        '  <div class="legend-grad"></div>'+
        '  <div class="legend-scale"><span>낮음</span><span style="margin-left:auto">→</span><span>높음</span></div>'+
        '  <div class="legend-section-title">포인트 색상</div>'+
        '  <div class="legend-keys">'+
        '    <div class="legend-key"><span class="dot red"></span><span>경보</span></div>'+
        '    <div class="legend-key"><span class="dot orange"></span><span>주의보</span></div>'+
        '    <div class="legend-key"><span class="dot purp"></span><span>기타</span></div>'+
        '  </div>'+
        '</div>';
      (document.querySelector('#map') || document.body).appendChild(el);
    }

    // ---------- 필터 UI ----------
    function ensureControls(){
      if (document.getElementById('ls-filter')) return;
      var el = document.createElement('div');
      el.id = 'ls-filter';
      el.innerHTML =
        '<div style="font-weight:800;margin-bottom:6px">산사태 필터</div>'+
        '<div style="display:grid;grid-template-columns:1fr 1fr;gap:6px;margin-bottom:6px">'+
          '<label style="display:flex;flex-direction:column;gap:4px">'+
            '<span>From</span><input id="ls-from" type="date" style="padding:6px;border:1px solid #e5e7eb;border-radius:8px" />'+
          '</label>'+
          '<label style="display:flex;flex-direction:column;gap:4px">'+
            '<span>To</span><input id="ls-to" type="date" style="padding:6px;border:1px solid #e5e7eb;border-radius:8px" />'+
          '</label>'+
        '</div>'+
        '<div style="display:flex;align-items:center;gap:8px;justify-content:space-between;margin-bottom:6px">'+
          '<label>최근 N일</label>'+
          '<select id="ls-days" style="padding:6px;border:1px solid #e5e7eb;border-radius:8px">'+
            '<option value="7">7</option>'+
            '<option value="14">14</option>'+
            '<option value="30" selected>30</option>'+
            '<option value="90">90</option>'+
          '</select>'+
        '</div>'+
        '<div style="display:flex;align-items:center;gap:8px;justify-content:space-between;margin-bottom:8px">'+
          '<label>등급</label>'+
          '<select id="ls-level" style="padding:6px;border:1px solid #e5e7eb;border-radius:8px">'+
            '<option value="ALL" selected>전체</option>'+
            '<option value="경보">경보</option>'+
            '<option value="주의보">주의보</option>'+
          '</select>'+
        '</div>'+
        '<div style="display:flex;gap:8px;justify-content:flex-end">'+
          '<button id="ls-apply" class="ls-btn-primary">적용</button>'+
          '<button id="ls-reset" class="ls-btn-ghost">초기화</button>'+
        '</div>';
      (document.querySelector('#map') || document.body).appendChild(el);

      el.querySelector('#ls-apply').addEventListener('click', function(){
        var from = el.querySelector('#ls-from').value || null;
        var to   = el.querySelector('#ls-to').value   || null;
        var days = Number(el.querySelector('#ls-days').value || '30');
        var lvl  = el.querySelector('#ls-level').value || 'ALL';
        state.level = lvl;
        if (from || to) { state.from = from; state.to = to; state.recentDays = null; }
        else { state.from = null; state.to = null; state.recentDays = days; }
        refresh();
      });
      el.querySelector('#ls-reset').addEventListener('click', function(){
        el.querySelector('#ls-from').value = '';
        el.querySelector('#ls-to').value   = '';
        el.querySelector('#ls-days').value = '30';
        el.querySelector('#ls-level').value= 'ALL';
        state = { recentDays:30, from:null, to:null, level:'ALL' };
        refresh();
      });
    }

    // ---------- 데이터 갱신 ----------
    function refresh() {
      fetchAgg(state).then(function(rows){ renderHeatmap(rows||[]); })
        .catch(function (err) { console.error('[LandslideHeat] agg error:', err && err.message ? err.message : err); });

      fetchPoints(state).then(function(rows){
        var filtered = applyClientFilter(rows||[], state);
        lastPointRows = filtered;
        drawDots(filtered);
        updateHUD(filtered);
      }).catch(function (err) {
        console.error('[LandslideHeat] points error:', err && err.message ? err.message : err);
        lastPointRows = []; clearDots(); updateHUD([]);
      });
    }

    function redrawFromCache() {
      if (lastAggRows) renderHeatmap(lastAggRows);
    }

    function onIdleDebounced() {
      if (idleTimer) clearTimeout(idleTimer);
      idleTimer = setTimeout(refresh, 250);
    }

    // ---------- 지도 고정 + 확대 프리셋 ----------
    function lockMap(){
      map.setDraggable(false);
      map.setZoomable(false);

      if (!document.getElementById('ls-lock-badge')) {
        var b = document.createElement('div');
        b.id = 'ls-lock-badge';
        b.className = 'ls-lock-badge';
        b.textContent = '지도 고정됨 (버튼으로만 확대/축소)';
        (document.querySelector('#map') || document.body).appendChild(b);
      }
    }
    function ensureZoomButtons(){
      if (document.getElementById('ls-zoom-presets')) return;

      var box = document.createElement('div');
      box.id = 'ls-zoom-presets';
      box.className = 'ls-zoom';
      box.innerHTML = [
        '<button class="ls-zoom__btn" data-scale="50">50%</button>',
        '<button class="ls-zoom__btn" data-scale="70">70%</button>',
        '<button class="ls-zoom__btn is-active" data-scale="100">100%</button>'
      ].join('');
      (document.querySelector('#map') || document.body).appendChild(box);

      var baseCenter = map.getCenter();
      var baseLevel  = map.getLevel();

      function levelFor(percent){
        if (percent === 100) return baseLevel;
        if (percent === 70)  return baseLevel + 1;
        if (percent === 50)  return baseLevel + 2;
        return baseLevel;
      }
      function setActive(pct){
        var btns = box.querySelectorAll('.ls-zoom__btn');
        for (var i=0;i<btns.length;i++){
          var b = btns[i];
          var on = Number(b.getAttribute('data-scale')) === pct;
          if (on) b.classList.add('is-active'); else b.classList.remove('is-active');
        }
      }
      function applyPreset(pct){
        map.setCenter(baseCenter);
        map.setLevel(levelFor(pct));
        setActive(pct);
        adjustRadius();
      }

      box.addEventListener('click', function(ev){
        var btn = ev.target.closest('.ls-zoom__btn');
        if (!btn) return;
        var pct = Number(btn.getAttribute('data-scale'));
        applyPreset(pct);
      });
    }

    // ---------- 활성/비활성 ----------
    function activate() {
      lockMap();
      ensureZoomButtons();

      if (!resizeObserver && global.ResizeObserver) {
        resizeObserver = new ResizeObserver(function(){ redrawFromCache(); });
        resizeObserver.observe(container);
        resizeObserver.observe(pointRoot);
      }
      if (!idleListener) {
        idleListener = kakao.maps.event.addListener(map, 'idle', onIdleDebounced);
        kakao.maps.event.addListener(map, 'zoom_changed', redrawFromCache);
        kakao.maps.event.addListener(map, 'center_changed', redrawFromCache);
      }
      refresh();
    }

    function deactivate() {
      if (idleListener) { kakao.maps.event.removeListener(idleListener); idleListener = null; }
      kakao.maps.event.removeListener(map, 'zoom_changed', redrawFromCache);
      kakao.maps.event.removeListener(map, 'center_changed', redrawFromCache);
      if (resizeObserver) { resizeObserver.disconnect(); resizeObserver = null; }
      heat.setData({ max: 0, data: [] });
      clearDots();
      updateHUD([]);
    }

    return { name: 'landslide', activate: activate, deactivate: deactivate, destroy: deactivate };
  }

})(this);
