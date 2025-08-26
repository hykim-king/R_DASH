/* /resources/map_js/landslide-layer.js */
/* eslint-disable */
(function (global) {
  'use strict';

  // ---- "산사태" 페이지만 동작 ----
  var qs = new URLSearchParams((location && location.search) || '');
  var LAYER = (qs.get('layer') || '').toLowerCase();
  var BODY_LAYER = ((document.body && document.body.getAttribute('data-layer')) || '').toLowerCase();
  if (!((LAYER === 'landslide') || (BODY_LAYER === 'landslide'))) { return; }

  if (global.AppMap) init(); else global.addEventListener('appmap:ready', init, { once: true });

  function init () {
    var App = global.AppMap || {};
    var map = App.map;
    if (!map || !global.kakao || !kakao.maps) { console.error('[Landslide] kakao map not found'); return; }

    var BASE = document.body.getAttribute('data-context-path') || ''; // ex) "/ehr"
    var API = { points: BASE + '/landslide/points.do' };

    // ---------- 상태 ----------
    var state = { recentDays: 30, from: null, to: null, level: 'ALL' };
    var overlays = [];           // CustomOverlay[]
    var clusterGhostMarkers = []; // 클러스터 계산용 Marker (화면에 표시 X)

    // 줌 기준: 이 레벨 이하로 들어오면 개별 오버레이(파란/빨강/회색) 표시
    var DETAIL_LEVEL = 6;

    // ---------- 클러스터러 ----------
    var clusterer = new kakao.maps.MarkerClusterer({
      map: map,
      averageCenter: true,
      minLevel: DETAIL_LEVEL,       // level <= 6 에서는 클러스터 해제
      calculator: [20, 50, 100],
      styles: [
        clusterStyle('#60a5fa'), // small
        clusterStyle('#f59e0b'), // medium
        clusterStyle('#ef4444')  // large
      ]
    });
    function clusterStyle(bg){
      return {
        width: '40px', height: '40px',
        background: bg, color: '#fff', borderRadius: '999px',
        lineHeight: '40px', textAlign: 'center', fontWeight: '800',
        boxShadow: '0 8px 18px rgba(0,0,0,.18)'
      };
    }

    // ---------- UI ----------
    ensureFilterUI();
    ensureHUD();

    // ---------- 유틸 ----------
// ---------- 유틸 ----------
function normalizeDate(s){
  if (s == null) return '';
  // 숫자(YYYYMMDD)로 오는 경우도 대비
  s = String(s).trim();
  // 'YYYY-MM-DD', 'YYYY/MM/DD', 'YYYYMMDD', 'YYYY-MM-DD HH:mm:ss' 대응
  var m = s.match(/^(\d{4})[-\/]?(\d{2})[-\/]?(\d{2})/);
  return m ? (m[1] + '-' + m[2] + '-' + m[3]) : '';
}

function levelOf(row){
  var v = (row && (row.level || row.LEVEL || row.alert || row.ALERT || row.status || row.STATUS)) || '';
  return String(v).trim();
}

// ES5 호환: ?? 대신 (x != null ? x : y)
function latOf(r){
  if (!r) return NaN;
  var v = (r.lat != null ? r.lat : r.LAT);
  var n = Number(v);
  return isNaN(n) ? NaN : n;
}

function lonOf(r){
  if (!r) return NaN;
  var v = (r.lon != null ? r.lon : r.LON);
  var n = Number(v);
  return isNaN(n) ? NaN : n;
}

    // 색상 규칙: 요청대로 "주의보=파란색"
    var COLORS = {
      WATCH:  '#3b82f6', // 주의보
      WARNING:'#ef4444', // 경보
      ETC:    '#6b7280'  // 기타
    };

    function colorFor(level){
      if (level.indexOf('주의')>-1)  return COLORS.WATCH;
      if (level.indexOf('경보')>-1)  return COLORS.WARNING;
      return COLORS.ETC;
    }

    function fetchJSON(url){
      return fetch(url, { headers:{'Accept':'application/json'} })
        .then(r => { if(!r.ok) throw new Error('HTTP '+r.status); return r.json(); });
    }
    function getBBoxParams() {
      var b = map.getBounds(), sw=b.getSouthWest(), ne=b.getNorthEast();
      var p = new URLSearchParams({
        minLat: sw.getLat(), maxLat: ne.getLat(),
        minLon: sw.getLng(), maxLon: ne.getLng()
      });
      if (state.level && state.level!=='ALL') p.set('level', state.level);
      if (state.from||state.to) { if(state.from) p.set('from', state.from); if(state.to) p.set('to', state.to); }
      else if (state.recentDays!=null) p.set('recentDays', String(state.recentDays));
      return p.toString();
    }

    // ---------- 오버레이 생성 (줌 인용) ----------
    function makeOverlay(row){
      var lat = latOf(row), lon = lonOf(row);
      if (isNaN(lat)||isNaN(lon)) return null;
      var pos = new kakao.maps.LatLng(lat, lon);
      var level = levelOf(row);
      var date  = normalizeDate(row.date || row.DATE || row.occurYmd || row.OCCUR_YMD || row.occur_dt || row.OCCUR_DT);
      var name  = row.name || row.NAME || row.addr || row.ADDR || '';

      var levelColor = colorFor(level);

      // 타입별 마커 DOM
      var el = document.createElement('div');
      el.style.pointerEvents = 'auto';
      el.title = (date?('발생일: '+date+'\n'):'') + (level?('등급: '+level+'\n'):'') + (name?('위치: '+name):'');

      if (level.indexOf('주의')>-1) {
        // 주의보: 파란 "배지" (요청사항)
        el.style.cssText = [
          'background:'+COLORS.WATCH+'; color:#fff; border-radius:999px;',
          'padding:6px 9px; font:600 12px/1 system-ui,Apple SD Gothic Neo,Malgun Gothic,sans-serif;',
          'box-shadow:0 10px 20px rgba(0,0,0,.25); white-space:nowrap;'
        ].join('');
        el.innerHTML = (date?('<b>'+date+'</b> '):'') + '주의보';
      } else if (level.indexOf('경보')>-1) {
        // 경보: 빨간 "다이아(회전 사각형)" + 안쪽 점
        var size = 16;
        el.style.cssText = [
          'position:relative;width:'+size+'px;height:'+size+'px;',
          'transform:rotate(45deg); background:'+COLORS.WARNING+';',
          'margin-left:' + (-size/2) + 'px;',
          'margin-top:'  + (-size/2) + 'px;',
          'box-shadow:0 0 14px '+COLORS.WARNING+', 0 0 20px '+COLORS.WARNING
        ].join('');
        var dot = document.createElement('div');
        dot.style.cssText = 'position:absolute;left:50%;top:50%;width:6px;height:6px;margin-left:-3px;margin-top:-3px;background:#fff;border-radius:999px;transform:rotate(-45deg);';
        el.appendChild(dot);
      } else {
        // 기타: 작은 회색 점
        var s = 10;
        el.style.cssText = [
          'width:'+s+'px;height:'+s+'px;border-radius:999px;',
          'background:'+COLORS.ETC+';opacity:.9;',
          'margin-left:-'+(s/2)+'px;margin-top:-'+(s/2)+'px;',
          'box-shadow:0 0 8px rgba(0,0,0,.25)'
        ].join('');
      }

      return new kakao.maps.CustomOverlay({
        position: pos, content: el, xAnchor: 0.5, yAnchor: 1, zIndex: 1000
      });
    }

    // ---------- 클러스터용 고스트 마커 (화면표시 X) ----------
    function makeGhostMarker(row){
      var lat = latOf(row), lon = lonOf(row);
      if (isNaN(lat)||isNaN(lon)) return null;
      return new kakao.maps.Marker({
        position: new kakao.maps.LatLng(lat, lon),
        map: null // ✅ 화면에는 보이지 않게 (보라색 중복 제거)
      });
    }

    function clearOverlays(){
      overlays.forEach(o => o.setMap(null));
      overlays.length = 0;
    }
    function clearGhosts(){
      clusterer.clear();
      clusterGhostMarkers.forEach(m => m.setMap && m.setMap(null));
      clusterGhostMarkers.length = 0;
    }

    // ---------- 렌더 ----------
    function render(rows){
      clearOverlays();
      clearGhosts();

      var zoom = map.getLevel();

      // 1) 클러스터 (항상 계산은 함)
      var ghosts = [];
      for (var i=0;i<(rows||[]).length;i++){
        var g = makeGhostMarker(rows[i]);
        if (g) ghosts.push(g);
      }
      clusterGhostMarkers = ghosts;
      clusterer.addMarkers(ghosts); // 지도 표시 없이 클러스터 숫자만

      // 2) 줌 인일 때만 개별 오버레이 표시
      if (zoom <= DETAIL_LEVEL){
        for (var j=0;j<(rows||[]).length;j++){
          var ov = makeOverlay(rows[j]);
          if (!ov) continue;
          ov.setMap(map);
          overlays.push(ov);
        }
      }

      updateHUD(rows||[]);
    }

    // ---------- HUD ----------
    function updateHUD(rows){
      var warn=0, watch=0, etc=0, recent=null;
      rows.forEach(r=>{
        var d = normalizeDate(r.date || r.DATE || r.occurYmd || r.OCCUR_YMD || r.occur_dt || r.OCCUR_DT);
        if (d && (!recent || d>recent)) recent = d;
        var lv = levelOf(r);
        if (lv.indexOf('경보')>-1) warn++;
        else if (lv.indexOf('주의')>-1) watch++;
        else etc++;
      });
      setText('ls-hud-total', '표시 건수: '+(rows.length||0));
      setText('ls-hud-split', '경보 '+warn+' · 주의보 '+watch+' · 기타 '+etc);
      setText('ls-hud-recent', '최근 발생일: '+(recent||'-'));
    }
    function setText(id, txt){ var el=document.getElementById(id); if(el) el.textContent = txt; }

    // ---------- 데이터 로드 ----------
    function refresh(){
      fetchJSON(API.points + '?' + getBBoxParams())
        .then(render)
        .catch(err=>{ console.error('[Landslide] points error:', err); render([]); });
    }

    // 지도가 멈췄을 때만 리로드 + 줌 변경 시 오버레이 갱신
    var idleTimer=null;
    kakao.maps.event.addListener(map, 'idle', function(){
      clearTimeout(idleTimer); idleTimer = setTimeout(refresh, 200);
    });
    kakao.maps.event.addListener(map, 'zoom_changed', function(){
      // 줌만 바뀌어도 렌더 다시 (오버레이 on/off 전환)
      refresh();
    });

    // ---------- 필터 UI ----------
    function ensureFilterUI(){
      if (document.getElementById('ls-filter')) return;
      var el = document.createElement('div');
      el.id = 'ls-filter';
      el.style.cssText = 'position:absolute;right:12px;top:12px;z-index:31;background:rgba(255,255,255,.96);border-radius:12px;padding:10px;box-shadow:0 10px 22px rgba(0,0,0,.12);font:12px/1.4 system-ui,Apple SD Gothic Neo,Malgun Gothic;color:#111827;min-width:230px';
      el.innerHTML =
        '<div style="font-weight:800;margin-bottom:6px">산사태 필터</div>'+
        '<div style="display:grid;grid-template-columns:1fr 1fr;gap:6px;margin-bottom:6px">'+
          '<label style="display:flex;flex-direction:column;gap:4px"><span>From</span><input id="ls-from" type="date" style="padding:6px;border:1px solid #e5e7eb;border-radius:8px"/></label>'+
          '<label style="display:flex;flex-direction:column;gap:4px"><span>To</span><input id="ls-to" type="date" style="padding:6px;border:1px solid #e5e7eb;border-radius:8px"/></label>'+
        '</div>'+
        '<div style="display:flex;align-items:center;gap:8px;justify-content:space-between;margin-bottom:6px">'+
          '<label>최근 N일</label>'+
          '<select id="ls-days" style="padding:6px;border:1px solid #e5e7eb;border-radius:8px"><option value="7">7</option><option value="14">14</option><option value="30" selected>30</option><option value="90">90</option></select>'+
        '</div>'+
        '<div style="display:flex;align-items:center;gap:8px;justify-content:space-between;margin-bottom:8px">'+
          '<label>등급</label>'+
          '<select id="ls-level" style="padding:6px;border:1px solid #e5e7eb;border-radius:8px"><option value="ALL" selected>전체</option><option value="경보">경보</option><option value="주의보">주의보</option></select>'+
        '</div>'+
        '<div style="display:flex;gap:8px;justify-content:flex-end"><button id="ls-apply" style="padding:8px 10px;border-radius:10px;background:#111827;color:#fff;font-weight:700;border:none;cursor:pointer">적용</button><button id="ls-reset" style="padding:8px 10px;border-radius:10px;background:#e5e7eb;color:#111827;font-weight:700;border:none;cursor:pointer">초기화</button></div>';
      (document.querySelector('#map')||document.body).appendChild(el);

      el.querySelector('#ls-apply').addEventListener('click', function(){
        var from = el.querySelector('#ls-from').value || null;
        var to   = el.querySelector('#ls-to').value   || null;
        var days = Number(el.querySelector('#ls-days').value || '30');
        var lvl  = el.querySelector('#ls-level').value || 'ALL';
        state.level = lvl;
        if (from||to){ state.from=from; state.to=to; state.recentDays=null; }
        else { state.from=null; state.to=null; state.recentDays=days; }
        refresh();
      });
      el.querySelector('#ls-reset').addEventListener('click', function(){
        el.querySelector('#ls-from').value='';
        el.querySelector('#ls-to').value='';
        el.querySelector('#ls-days').value='30';
        el.querySelector('#ls-level').value='ALL';
        state = { recentDays:30, from:null, to:null, level:'ALL' };
        refresh();
      });
    }

    function ensureHUD(){
      if (document.getElementById('ls-hud')) return;
      var hud = document.createElement('div');
      hud.id = 'ls-hud';
      hud.style.cssText = 'position:absolute;left:12px;bottom:12px;z-index:30;background:rgba(255,255,255,.94);border-radius:12px;padding:10px 12px;box-shadow:0 10px 22px rgba(0,0,0,.14);color:#111827;font:600 12px/1.5 system-ui,Apple SD Gothic Neo,Malgun Gothic;';
      hud.innerHTML = '<div style="font-weight:800;margin-bottom:6px">산사태 요약(현재 화면)</div><div id="ls-hud-total">표시 건수: -</div><div id="ls-hud-split">경보 0 · 주의보 0 · 기타 0</div><div id="ls-hud-recent">최근 발생일: -</div>';
      (document.querySelector('#map')||document.body).appendChild(hud);
    }

    // 최초 로드
    refresh();
  }
})(this);
