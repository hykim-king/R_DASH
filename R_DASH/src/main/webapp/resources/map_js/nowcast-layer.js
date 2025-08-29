
/* eslint-disable */
(function (global) {
  'use strict';

  // ===== layer 활성 조건: ?layer=nowcast 또는 <body data-layer="nowcast"> =====
  var qs = new URLSearchParams((global.location && location.search) || '');
  var LAYER = (qs.get('layer') || '').toLowerCase();
  var BODY_LAYER = ((global.document && document.body && document.body.getAttribute('data-layer')) || '').toLowerCase();
  if (!(LAYER === 'nowcast' || BODY_LAYER === 'nowcast')) {
    console.log('[NowcastLayer] skip (layer != nowcast)');
    return;
  }

  // ===== NOWCAST 전용 CSS 인라인 삽입 =====
  (function injectCSS(){
    if (document.getElementById('nowcast-inline-style')) return;
    var css = `
      /* (요청) 우상단 요소들 120px 위치 */
      .sido-filter-host.top-right { position:absolute; top:120px; right:16px; z-index:50; }

      /* 선택된 시군구 폴리곤 강조(입체 느낌) */
      .__nc_selected_shadow { box-shadow: 0 8px 16px rgba(0,0,0,.2); }

      /* NOWCAST 전용 아이콘 + 말풍선 */
      .nc-cta {
        position: absolute;
        top: 120px;              /* ← 20px에서 120px로 */
        right: 16px;
        display: flex;
        align-items: center;
        gap: 10px;
        z-index: 60;
        pointer-events: auto;
      }
      .nc-cta .bubble {
        max-width: 260px;
        background: #ffffff;
        border: 1px solid #d6e2f0;
        box-shadow: 0 6px 14px rgba(23, 50, 93, .15);
        border-radius: 12px;
        padding: 10px 12px;
        font-size: 13px;
        line-height: 1.35;
        color: #123;
        position: relative;
        white-space: pre-line;
      }
      .nc-cta .bubble::after {
        content: "";
        position: absolute;
        right: -8px;
        top: 16px;
        border-width: 8px;
        border-style: solid;
        border-color: transparent transparent transparent #ffffff;
        filter: drop-shadow(0 1px 1px rgba(23,50,93,.12));
      }
      .nc-cta .icon-btn {
        width: 44px; height: 44px;
        display: inline-flex;
        align-items: center;
        justify-content: center;
        border-radius: 50%;
        background: #fff;
        border: 1px solid #d6e2f0;
        box-shadow: 0 6px 14px rgba(23,50,93,.15);
        cursor: pointer;
        transition: transform .1s ease;
      }
      .nc-cta .icon-btn:hover { transform: translateY(-1px); }
      .nc-cta .icon-btn img { width: 28px; height: 28px; display:block; }
    `.trim();
    var node = document.createElement('style');
    node.id = 'nowcast-inline-style';
    node.textContent = css;
    document.head.appendChild(node);
  })();

  var NowcastLayer = (global.NowcastLayer = global.NowcastLayer || {});

  // AppMap 준비되면 init
  if (global.AppMap) init();
  else global.addEventListener('appmap:ready', init, { once: true });

  function init() {
    var App = global.AppMap || {};
    var map = App.map || global.map;
    if (!map || !global.kakao || !kakao.maps) {
      console.error('[NowcastLayer] kakao map not found');
      return;
    }

    // ===== API =====
    var BASE = (document.body && document.body.getAttribute('data-context-path')) || '';
    var API = {
      nationLatest: function (cat) { return BASE + '/nowcast/latest.do?category=' + encodeURIComponent(cat); },
      latest4All: BASE + '/nowcast/latest4',
      latest4Region: function (sido, sgg) {
        return BASE + '/nowcast/latest4-region?sidoNm=' + encodeURIComponent(sido) + '&signguNm=' + encodeURIComponent(sgg);
      }
    };

    // 라벨/단위
    var LABEL = { T1H: '기온', RN1: '1시간 강수량', WSD: '풍속', REH: '습도' };
    var UNIT  = { T1H: '℃',   RN1: 'mm',         WSD: 'm/s', REH: '%'   };

    // ===== 상태 =====
    var cache = { T1H: null, RN1: null, REH: null, WSD: null };
    var currentCategory = (qs.get('category') || 'T1H').toUpperCase();

    // 추가 상태: 선택 폴리곤/행정구역
    var __selectedPoly = null;
    var __selectedSido = null;
    var __selectedSgg  = null;

    // ===== 유틸 =====
    function pickValue(row) {
      if (!row) return null;
      var cand = [row.OBSR_VALUE, row.obsrValue, row.obsr_value, row.value, row.val, row.RN1, row.rn1];
      for (var i=0;i<cand.length;i++) if (cand[i] != null && cand[i] !== '') return cand[i];
      return null;
    }
    var CAT_ALIASES = {
      T1H: ['T1H','TEMP','TA'],
      RN1: ['RN1','PCP','PRCP','RAIN1H','R1H','RN','RAIN','RAINFALL'],
      WSD: ['WSD','WIND','WS'],
      REH: ['REH','HUMI','RH']
    };
    function normCat(c){
      c = (c||'').toUpperCase();
      for (var k in CAT_ALIASES) if (CAT_ALIASES[k].indexOf(c) !== -1) return k;
      return c;
    }
    function fmt(v, u) {
      if (v == null) return '-';
      var n = Number(v);
      if (isNaN(n)) return '-';
      return String(n) + (u || '');
    }
    function _sidoSlug(s){
      s = (s || '').replace(/\s+/g, '');
      s = s.replace(/특별자치도|자치도|광역시|특별시|자치시|도|시$/g, '');
      s = s.replace(/^전라북/, '전북').replace(/^전라남/, '전남')
           .replace(/^경상북/, '경북').replace(/^경상남/, '경남')
           .replace(/^충청북/, '충북').replace(/^충청남/, '충남');
      return s;
    }
    // 붙어있는 '성남시분당구', '포항시북구' 대응
    function _sggSlug(s){
      s = (s || '').trim().replace(/특별자치시|특례시/g, '시');
      var parts = s.split(/\s+/);
      if (parts.length > 1) return parts[parts.length - 1];
      var m = s.match(/(?:.*?(?:시|군))(.+)$/);
      return m ? m[1] : s;
    }
    function _slug(sido, sgg){ return _sidoSlug(sido) + '|' + _sggSlug(sgg); }

    // ===== HUD =======================================================
    (function(){
      var hudOverlay = null;

      function nfmt(v){ if(v==null || isNaN(+v)) return '-'; return (Math.round(+v*10)/10); }

      function buildHudHTML(title, vals){
        var t = title || '';
        var temp = (vals && vals.T1H!=null) ? nfmt(vals.T1H)+' ℃' : '-';
        var rn1  = (vals && vals.RN1!=null) ? nfmt(vals.RN1)+' mm' : '-';
        var wsd  = (vals && vals.WSD!=null) ? nfmt(vals.WSD)+' m/s' : '-';
        var reh  = (vals && vals.REH!=null) ? nfmt(vals.REH)+'%' : '-';

        return ''
        + '<div class="nc-wrap">'
        + '  <div class="nc-card">'
        + '    <div class="nc-hdr">'+ t +'</div>'
        + '    <div class="nc-grid">'
        + '      <div class="nc-chip c-temp">'+ NowcastLayer.icons.sun +' <span>기온</span><span class="v">'+ temp +'</span></div>'
        + '      <div class="nc-chip c-rain">'+ NowcastLayer.icons.rain +' <span>강수</span><span class="v">'+ rn1 +'</span></div>'
        + '      <div class="nc-chip c-wind">'+ NowcastLayer.icons.wind +' <span>풍속</span><span class="v">'+ wsd +'</span></div>'
        + '      <div class="nc-chip c-humi">'+ NowcastLayer.icons.humi +' <span>습도</span><span class="v">'+ reh +'</span></div>'
        + '    </div>'
        + '  </div>'
        + '  <div class="nc-close" title="닫기" aria-label="닫기">×</div>'
        + '</div>';
      }

      function polygonCenter(poly){
        var paths = poly.__paths || [];
        var sumLat=0, sumLng=0, n=0;
        for (var i=0;i<paths.length;i++){
          var ring = paths[i];
          for (var j=0;j<ring.length;j++){ sumLat+=ring[j].getLat(); sumLng+=ring[j].getLng(); n++; }
        }
        return (n? new kakao.maps.LatLng(sumLat/n, sumLng/n) : new kakao.maps.LatLng(36.5,127.8));
      }

NowcastLayer.hud = (function(){
  var hudOverlay = null;
  var mapClickHandler = null;
  var escHandler = null;

  function attachGlobalClosers(doClose){
    // 지도 아무데나 클릭 → 닫기
    if (!mapClickHandler){
      mapClickHandler = kakao.maps.event.addListener(map, 'click', function(){
        doClose();
      });
    }
    // ESC → 닫기
    if (!escHandler){
      escHandler = function(e){
        if (e.key === 'Escape') doClose();
      };
      document.addEventListener('keydown', escHandler);
    }
  }

  function detachGlobalClosers(){
    if (mapClickHandler){
      kakao.maps.event.removeListener(map, 'click', mapClickHandler);
      mapClickHandler = null;
    }
    if (escHandler){
      document.removeEventListener('keydown', escHandler);
      escHandler = null;
    }
  }

  function nfmt(v){ if(v==null || isNaN(+v)) return '-'; return (Math.round(+v*10)/10); }

  function buildHudHTML(title, vals){
    var t = title || '';
    var temp = (vals && vals.T1H!=null) ? nfmt(vals.T1H)+' ℃' : '-';
    var rn1  = (vals && vals.RN1!=null) ? nfmt(vals.RN1)+' mm' : '-';
    var wsd  = (vals && vals.WSD!=null) ? nfmt(vals.WSD)+' m/s' : '-';
    var reh  = (vals && vals.REH!=null) ? nfmt(vals.REH)+'%' : '-';

    return ''
    + '<div class="nc-wrap">'
    + '  <div class="nc-card">'
    + '    <div class="nc-hdr">'+ t +'</div>'
    + '    <div class="nc-grid">'
    + '      <div class="nc-chip c-temp">'+ NowcastLayer.icons.sun  +' <span>기온</span><span class="v">'+ temp +'</span></div>'
    + '      <div class="nc-chip c-rain">'+ NowcastLayer.icons.rain +' <span>강수</span><span class="v">'+ rn1  +'</span></div>'
    + '      <div class="nc-chip c-wind">'+ NowcastLayer.icons.wind +' <span>풍속</span><span class="v">'+ wsd  +'</span></div>'
    + '      <div class="nc-chip c-humi">'+ NowcastLayer.icons.humi +' <span>습도</span><span class="v">'+ reh  +'</span></div>'
    + '    </div>'
    + '  </div>'
    + '  <div class="nc-close" title="닫기" aria-label="닫기">×</div>'
    + '</div>';
  }

  function doCloseWithAnim(rootEl){
    // rootEl: .nc-wrap
    if (!hudOverlay || !rootEl) { hardClose(); return; }
    var card = rootEl.querySelector('.nc-card');
    if (card){
      card.classList.add('is-hiding');        // 애니 시작
      setTimeout(function(){ hardClose(); }, 240); // CSS 시간과 맞춤
    } else {
      hardClose();
    }
  }

  function hardClose(){
    if (hudOverlay){ hudOverlay.setMap(null); hudOverlay = null; }
    detachGlobalClosers();
    // 선택 해제 (기존 코드 유지)
    if (typeof elevatePolygon === 'function' && typeof __selectedPoly !== 'undefined' && __selectedPoly){
      elevatePolygon(__selectedPoly, false);
      __selectedPoly = null; __selectedSido = null; __selectedSgg = null;
    }
  }

  return {
    show: function(mapObj, position, title, vals){
      // 기존 HUD 제거
      if (hudOverlay){ hudOverlay.setMap(null); hudOverlay = null; }

      // 컨텐츠 생성
      var wrapper = document.createElement('div');
      wrapper.innerHTML = buildHudHTML(title, vals);
      var rootEl = wrapper.firstChild;              // .nc-wrap
      var closeBtn = rootEl.querySelector('.nc-close');

      // 커스텀 오버레이
      hudOverlay = new kakao.maps.CustomOverlay({
        position: position,
        content: rootEl,
        xAnchor: 0.5,
        yAnchor: 1.05,
        map: mapObj
      });

      // 살짝 아래로 내리고 싶다면 필요 시 유지
      if (typeof hudOverlay.setYOffset === 'function') {
        hudOverlay.setYOffset(-100);
      }

     
      // 바깥(지도) 클릭/ESC로 닫기
      attachGlobalClosers(function(){ doCloseWithAnim(rootEl); });
    },

    hide: function(){
      hardClose();
    },

    atPolygon: function(poly, title, vals){
      // 폴리곤 중심 계산은 기존 함수 사용
      function polygonCenter(poly){
        var paths = poly.__paths || [];
        var sumLat=0, sumLng=0, n=0;
        for (var i=0;i<paths.length;i++){
          var ring = paths[i];
          for (var j=0;j<ring.length;j++){ sumLat+=ring[j].getLat(); sumLng+=ring[j].getLng(); n++; }
        }
        return (n? new kakao.maps.LatLng(sumLat/n, sumLng/n) : new kakao.maps.LatLng(36.5,127.8));
      }
      this.show(map, polygonCenter(poly), title, vals);
    }
  };
})();
      NowcastLayer.icons = {
        sun:  '<svg viewBox="0 0 24 24" fill="none"><circle cx="12" cy="12" r="5" stroke="currentColor" stroke-width="1.8"/><path d="M12 1v3M12 20v3M4.22 4.22l2.12 2.12M17.66 17.66l2.12 2.12M1 12h3M20 12h3M4.22 19.78l2.12-2.12M17.66 6.34l2.12-2.12" stroke="currentColor" stroke-width="1.6" stroke-linecap="round"/></svg>',
        rain: '<svg viewBox="0 0 24 24" fill="none"><path d="M7 15a5 5 0 0 1 0-10c1.7 0 3.2.84 4.1 2.12A4.5 4.5 0 1 1 17 15H7Z" stroke="currentColor" stroke-width="1.8" stroke-linejoin="round"/><path d="M8 20l1-2M12 21l1-2M16 20l1-2" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/></svg>',
        wind: '<svg viewBox="0 0 24 24" fill="none"><path d="M3 12h10a3 3 0 1 0-3-3" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/><path d="M3 16h12a2.5 2.5 0 1 1-2.5 2.5" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/></svg>',
        humi: '<svg viewBox="0 0 24 24" fill="none"><path d="M12 3s6 6.2 6 10a6 6 0 1 1-12 0c0-3.8 6-10 6-10Z" stroke="currentColor" stroke-width="1.8"/><path d="M9 14c0 1.66 1.34 3 3 3" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"/></svg>'
      };
    })();
    // ===== /HUD =======================================================

    // ===== 폴리곤 레이어 준비 =====
    ensurePolygonLayer();

    // ===== 필터 UI 장착 (시·도/시군구 동적 칩) =====
    mountSidoSggFilterUI();

    // ===== 데이터 프리로드 & 초기 적용 =====
    preloadAll();
    console.log('[NowcastLayer] ready, default =', currentCategory);

    // ---------- functions ----------
    function hexToRgb(hex) {
      hex = (hex || '#000000').replace('#','');
      var bigint = parseInt(hex, 16);
      return { r: (bigint>>16)&255, g: (bigint>>8)&255, b: bigint&255 };
    }

    function preloadAll() {
      ['T1H', 'RN1', 'REH', 'WSD'].forEach(function (cat) {
        $.getJSON(API.nationLatest(cat))
          .done(function (rows) {
            cache[cat] = rows || [];
            if (cat === currentCategory) applySnapshot(rows, cat);
          })
          .fail(function (xhr) {
            console.error('[NowcastLayer] preload fail:', cat, xhr && xhr.status);
          });
      });
    }

    function setCategory(cat) {
      currentCategory = (cat || 'T1H').toUpperCase();
      if (cache[currentCategory]) {
        applySnapshot(cache[currentCategory], currentCategory);
      } else {
        $.getJSON(API.nationLatest(currentCategory))
          .done(function (rows) {
            cache[currentCategory] = rows || [];
            applySnapshot(rows, currentCategory);
          });
      }
    }
    NowcastLayer.setCategory = setCategory;

    function applySnapshot(rows, cat) {
      if (!$.isArray(rows)) rows = [];
      if (!App || !App.layers || !App.layers.sig) return;

      var byKey = {};
      rows.forEach(function (r) {
        var sido = r.sidoNm || r.SIDO_NM || r.CTP_KOR_NM || r.CTPRVN_NM || '';
        var sgg  = r.signguNm || r.SIGNGU_NM || r.SIG_KOR_NM || r.SIG_NM || '';

        var v = pickValue(r);
        if ((cat||'').toUpperCase()==='RN1' && (v==null || v==='')) v = 0;

        var key1 = _slug(sido, sgg);
        byKey[key1] = { value: v, display: fmt(v, UNIT[cat]) };

        var sgg2 = (function(x){
          x = (x||'').trim();
          if (/\s/.test(x)) return x;
          var m = x.match(/(?:.*?(?:시|군))(.+)$/);
          return m ? m[1] : x;
        })(sgg);
        var key2 = _sidoSlug(sido) + '|' + _sggSlug(sgg2);
        byKey[key2] = { value: v, display: fmt(v, UNIT[cat]) };
      });

      if (App.layers.sig.setData) App.layers.sig.setData(byKey, { category: cat, label: LABEL[cat] });
      if (App.layers.sig.repaint) App.layers.sig.repaint();
    }

    function elevatePolygon(poly, active){
      if (!poly) return;
      if (active){
        poly.setOptions({ strokeWeight:3, strokeColor:'#113a74', fillOpacity:0.8 });
        poly.setZIndex(10);
      }else{
        poly.setOptions({ strokeWeight:1, strokeColor:'#2b4a7d', fillOpacity:0.35 });
        poly.setZIndex(0);
      }
    }

    function ensurePolygonLayer() {
      if (App.layers && App.layers.sig) return;

      var attr  = (document.body && document.body.getAttribute('data-nowcast-geo'))  || '';
      var extra = (document.body && document.body.getAttribute('data-nowcast-geo2')) || '';
      var urls = [].concat(attr ? attr.split(',') : []).concat(extra ? [extra] : [])
                   .map(function (s) { return (s || '').trim(); }).filter(Boolean);
      if (!urls.length) { console.warn('[NowcastLayer] no geo urls on <body>'); return; }
      console.log('[NowcastLayer] geo urls:', urls);

      var polyByKey = {};
      App.layers = App.layers || {};
      App.layers.sig = {
        _values: {},
        _byKey: polyByKey,
        _sidoFilterSlug: '',
        setData: function (byKey, meta) { this._values = byKey || {}; this._meta = meta || {}; },
        setSidoFilter: function(sidoNm){ this._sidoFilterSlug = _sidoSlug(sidoNm || ''); this.repaint(); },
        repaint: function () {
          var self = this;
          Object.keys(polyByKey).forEach(function (k) {
            var poly = polyByKey[k];

            var visible = !self._sidoFilterSlug || (poly.__sidoSlug === self._sidoFilterSlug);
            if (poly.getMap() !== (visible ? map : null)) poly.setMap(visible ? map : null);
            if (!visible) return;

            var entry = self._values[k];
            var v = entry ? Number(entry.value) : NaN;

            var targetHex = '#BFD7FF';
            if (!isNaN(v)) {
              targetHex = (v >= 25) ? '#004AAD' :
                          (v >= 15) ? '#2E75FF' :
                          (v >= 5)  ? '#6FA8FF' :
                                      '#BFD7FF';
            }

            var startHex = poly.__fillHex || '#BFD7FF';
            var steps = 12, step = 0, interval = 30;
            var s = hexToRgb(startHex), t = hexToRgb(targetHex);
            clearInterval(poly.__animTimer);
            poly.__animTimer = setInterval(function(){
              step++;
              var ratio = step / steps;
              var r = Math.round(s.r + (t.r - s.r) * ratio);
              var g = Math.round(s.g + (t.g - s.g) * ratio);
              var b = Math.round(s.b + (t.b - s.b) * ratio);
              var col = 'rgb('+r+','+g+','+b+')';
              poly.setOptions({ fillColor: col, fillOpacity: 0.6, strokeWeight: 1, strokeOpacity: 0.9, strokeColor: '#2b4a7d' });
              if (step >= steps){ clearInterval(poly.__animTimer); poly.__fillHex = targetHex; }
            }, interval);
          });
        }
      };

      function pickNames(p) {
        var SIDO_BY_PREFIX = {
          '11':'서울특별시','26':'부산광역시','27':'대구광역시','28':'인천광역시',
          '29':'광주광역시','30':'대전광역시','31':'울산광역시','36':'세종특별자치시',
          '41':'경기도','42':'강원특별자치도','43':'충청북도','44':'충청남도',
          '45':'전북특별자치도','46':'전라남도','47':'경상북도','48':'경상남도',
          '50':'제주특별자치도'
        };
        var sido = p.SIDO_NM || p.CTP_KOR_NM || p.CTPRVN_NM || p.sidoNm || p.SIDO || p.sido || '';
        var sgg  = p.SIG_KOR_NM || p.SIGUNGU || p.SIG_NM || p.SGG_NM || p.signguNm || p.SGG || p.sigungu || '';
        if (!sido) {
          var sigcd = (p.SIG_CD || p.sig_cd || '').toString();
          if (sigcd.length >= 2 && SIDO_BY_PREFIX[sigcd.substr(0,2)]) sido = SIDO_BY_PREFIX[sigcd.substr(0,2)];
        }
        return { sido: (sido || '').trim(), sgg: (sgg || '').trim() };
      }

      function toPaths(g) {
        var paths = [];
        if (!g) return paths;
        if (g.type === 'Polygon') {
          (g.coordinates || []).forEach(function (ring) {
            if (ring && ring.length) paths.push(ring.map(function (c) { return new kakao.maps.LatLng(c[1], c[0]); }));
          });
        } else if (g.type === 'MultiPolygon') {
          (g.coordinates || []).forEach(function (poly) {
            (poly || []).forEach(function (ring) {
              if (ring && ring.length) paths.push(ring.map(function (c) { return new kakao.maps.LatLng(c[1], c[0]); }));
            });
          });
        }
        return paths;
      }

      function addFeature(f) {
        var p = f.properties || {};
        var n = pickNames(p);
        if (!n.sido || !n.sgg) {
          if (!App._missingNameWarned) { App._missingNameWarned = 1; console.warn('[NowcastLayer] name missing in properties sample:', p); }
          return;
        }

        var key = _slug(n.sido, n.sgg);
        if (polyByKey[key]) return;

        var paths = toPaths(f.geometry);
        if (!paths.length) {
          if (!App._noPathWarned) { App._noPathWarned = 1; console.warn('[NowcastLayer] no paths for', key); }
          return;
        }

        var poly = new kakao.maps.Polygon({
          map: map,
          path: paths,
          strokeWeight: 1,
          strokeColor: '#2b4a7d',
          strokeOpacity: 0.9,
          fillColor: '#BFD7FF',
          fillOpacity: 0.35
        });
        poly.__paths = paths;
        poly.__sido  = n.sido;
        poly.__sgg   = n.sgg;
        poly.__sidoSlug = _sidoSlug(n.sido);
        poly.__fillHex  = '#BFD7FF';
        polyByKey[key] = poly;

        kakao.maps.event.addListener(poly, 'click', function () {
          if (__selectedPoly && __selectedPoly !== poly) elevatePolygon(__selectedPoly, false);
          __selectedPoly = poly; __selectedSido = n.sido; __selectedSgg = n.sgg;
          elevatePolygon(poly, true);

          $.getJSON(API.latest4Region(n.sido, n.sgg)).done(function (rows) {
            var vals = {T1H:null, RN1:null, WSD:null, REH:null};
            (rows||[]).forEach(function(r){
              var cat = normCat(r.category);
              var v   = pickValue(r);
              if (cat === 'RN1' && (v==null || v==='')) v = 0;
              if (vals.hasOwnProperty(cat)) vals[cat] = v;
            });
            if (vals.RN1 == null) vals.RN1 = 0;
            NowcastLayer.hud.atPolygon(poly, n.sido + ' ' + n.sgg, vals);
          }).fail(function(){
            NowcastLayer.hud.atPolygon(poly, n.sido + ' ' + n.sgg, {T1H:null,RN1:0,WSD:null,REH:null});
          });
        });
      }

      var tasks = urls.map(function (u){ return $.getJSON(u).then(function(geo){ if(geo && geo.features) geo.features.forEach(addFeature); }); });
      $.when.apply($, tasks).always(function () {
        var cnt = Object.keys(polyByKey).length;
        console.log('[NowcastLayer] polygon layer ready:', cnt, 'areas (merged)');
        if (cache[currentCategory]) { applySnapshot(cache[currentCategory], currentCategory); }
      });
    } // ensurePolygonLayer

    // === 시군구 도우미 ===
    function listSggBySido(sidoNm){
      var out = [];
      if (!App.layers || !App.layers.sig || !App.layers.sig._byKey) return out;
      var target = _sidoSlug(sidoNm);
      Object.keys(App.layers.sig._byKey).forEach(function(k){
        var poly = App.layers.sig._byKey[k];
        if (poly.__sidoSlug === target) out.push(poly.__sgg);
      });
      return Array.from(new Set(out)).sort();
    }
    function findPolygon(sidoNm, sggNm){
      var key = _slug(sidoNm, sggNm);
      return (App.layers && App.layers.sig && App.layers.sig._byKey) ? App.layers.sig._byKey[key] : null;
    }
    function polygonBounds(poly){
      var b = new kakao.maps.LatLngBounds();
      var paths = poly.__paths || [];
      for (var i=0;i<paths.length;i++) for (var j=0;j<paths[i].length;j++) b.extend(paths[i][j]);
      return b;
    }

    function focusAndShowNowcast(sidoNm, sggNm){
      var poly = findPolygon(sidoNm, sggNm);
      if (!poly){ console.warn('polygon not found', sidoNm, sggNm); return; }

      if (__selectedPoly && __selectedPoly !== poly) elevatePolygon(__selectedPoly, false);
      __selectedPoly = poly; __selectedSido = sidoNm; __selectedSgg = sggNm;
      elevatePolygon(poly, true);

      // 지도를 움직이지 않고 HUD만 표시
      $.getJSON(API.latest4Region(sidoNm, sggNm)).done(function (rows) {
        var vals = {T1H:null, RN1:null, WSD:null, REH:null};
        (rows||[]).forEach(function(r){
          var cat = normCat(r.category);
          var v   = pickValue(r);
          if (cat === 'RN1' && (v==null || v==='')) v = 0;
          if (vals.hasOwnProperty(cat)) vals[cat] = v;
        });
        if (vals.RN1 == null) vals.RN1 = 0;
        NowcastLayer.hud.atPolygon(poly, sidoNm+' '+sggNm, vals);
      }).fail(function(){
        NowcastLayer.hud.atPolygon(poly, sidoNm+' '+sggNm, {T1H:null,RN1:0,WSD:null,REH:null});
      });
    }

    // ===== 시·도 + 시군구 칩 UI =====
    function mountSidoSggFilterUI(){
      var SIDO_LIST = [
        "전체","강원특별자치도","경기도","경상남도","경상북도",
        "광주광역시","대구광역시","대전광역시","부산광역시",
        "서울특별시","세종특별자치시","울산광역시","인천광역시",
        "전라남도","전북특별자치도","제주특별자치도","충청남도","충청북도"
      ];

      var host = document.getElementById('sido-filter');
      if (!host) { host = document.createElement('div'); host.id='sido-filter'; document.body.appendChild(host); }
      host.classList.add('sido-filter-host','top-right');

      var card = document.createElement('div'); card.className = 'sido-filter-card';
      var wrap = document.createElement('div'); wrap.className = 'sido-filter';
      card.appendChild(wrap);

      var sggCard = document.createElement('div'); sggCard.className = 'sgg-filter-card'; sggCard.style.display='none';
      var sggWrap  = document.createElement('div'); sggWrap.className  = 'sgg-filter';
      sggCard.appendChild(sggWrap);
      card.appendChild(sggCard);

      host.innerHTML = ''; host.appendChild(card);

      function renderSggChips(sidoNm){
        var list = listSggBySido(sidoNm);
        sggWrap.innerHTML = '';
        if (!list.length){ sggCard.style.display='none'; return; }
        sggCard.style.display='block';
        list.forEach(function(name, i){
          var btn = document.createElement('button');
          btn.type='button';
          btn.className='sgg-chip' + (i===0?' active':'');
          btn.textContent = name;
          btn.addEventListener('click', function(){
            [].forEach.call(sggWrap.querySelectorAll('.sgg-chip'), function(el){ el.classList.remove('active'); });
            btn.classList.add('active');
            focusAndShowNowcast(sidoNm, name);
          });
          sggWrap.appendChild(btn);
        });
        var first = sggWrap.querySelector('.sgg-chip'); if (first) first.click();
      }

      // 시도 칩
      SIDO_LIST.forEach(function(name, i){
        var btn = document.createElement('button');
        btn.type='button';
        btn.className='sido-chip' + (i===0?' active':'');
        btn.dataset.sido = (name==='전체'?'':name);
        btn.textContent = name;
        btn.addEventListener('click', function(){
          [].forEach.call(wrap.querySelectorAll('.sido-chip'), function(el){ el.classList.remove('active'); });
          btn.classList.add('active');

          var selected = btn.dataset.sido || '';
          if (App.layers && App.layers.sig && typeof App.layers.sig.setSidoFilter === 'function'){
            App.layers.sig.setSidoFilter(selected);
          }
          host.dispatchEvent(new CustomEvent('sido:change', { detail:{ value: selected }}));

          if (selected){ renderSggChips(selected); }
          else { 
            sggWrap.innerHTML=''; sggCard.style.display='none'; NowcastLayer.hud.hide();
            if (__selectedPoly) { elevatePolygon(__selectedPoly, false); __selectedPoly=null; }
          }
        });
        wrap.appendChild(btn);
      });

      // NOWCAST 전용 아이콘+말풍선
      mountCTA();

      NowcastLayer.setSidoFilter = function(sidoNm){
        var targetSlug = _sidoSlug(sidoNm || '');
        [].forEach.call(wrap.querySelectorAll('.sido-chip'), function(el){
          var slug = _sidoSlug(el.dataset.sido || '');
          el.classList.toggle('active', targetSlug ? slug===targetSlug : (el.dataset.sido||'')==='');
        });
        if (App.layers && App.layers.sig && App.layers.sig.setSidoFilter){ App.layers.sig.setSidoFilter(sidoNm||''); }
        if (sidoNm){ renderSggChips(sidoNm); } else { sggWrap.innerHTML=''; sggCard.style.display='none'; }
      };

      function mountCTA(){
        if (document.getElementById('nc-cta')) return;
        var box = document.createElement('div');
        box.id = 'nc-cta';
        box.className = 'nc-cta';

        var bubble = document.createElement('div');
        bubble.className = 'bubble';
        bubble.textContent = '너네 마을은 이따가 비온대 ?\n 우리 마을은 비 많이 와  !!';

        var btn = document.createElement('button');
        btn.type = 'button';
        btn.className = 'icon-btn';
        var img = document.createElement('img');
        img.alt = 'nowcast icon';
        img.src = (BASE || '') + '/resources/image/jaeminsinkhole_4.png';
        btn.appendChild(img);

      
        box.appendChild(bubble);
        box.appendChild(btn);
        document.body.appendChild(box);
      }
    } // mountSidoSggFilterUI
  } // init
})(window);