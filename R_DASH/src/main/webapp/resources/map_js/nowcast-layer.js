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

    // 라벨/단위 (HUD 표기용)
    var LABEL = { T1H: '기온', RN1: '1시간 강수량', WSD: '풍속', REH: '습도' };
    var UNIT  = { T1H: '℃',   RN1: 'mm',         WSD: 'm/s', REH: '%'   };

    // ===== 상태 =====
    var cache = { T1H: null, RN1: null, REH: null, WSD: null };
    var currentCategory = (qs.get('category') || 'T1H').toUpperCase();

    // ===== 유틸 =====
    function pickValue(row) {
      if (!row) return null;
      return (row.OBSR_VALUE != null) ? row.OBSR_VALUE
           : (row.obsrValue != null) ? row.obsrValue
           : (row.val != null)       ? row.val
           : null;
    }
    function fmt(v, u) {
      if (v == null) return '-';
      var n = Number(v);
      if (isNaN(n)) return '-';
      return String(n) + (u || '');
    }

    // ---- 이름 표준화(슬러그)
    function _sidoSlug(s){
      s = (s || '').replace(/\s+/g, '');
      s = s.replace(/특별자치도|자치도|광역시|특별시|자치시|도|시$/g, '');
      s = s.replace(/^전라북/, '전북').replace(/^전라남/, '전남')
           .replace(/^경상북/, '경북').replace(/^경상남/, '경남')
           .replace(/^충청북/, '충북').replace(/^충청남/, '충남');
      return s;
    }
    function _sggSlug(s){
      s = (s || '').trim();
      s = s.replace(/특별자치시/g, '시').replace(/특례시/g, '시');
      var parts = s.split(/\s+/);
      return parts[parts.length - 1];
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

      NowcastLayer.hud = {
        show: function(map, position, title, vals){
          if (hudOverlay){ hudOverlay.setMap(null); hudOverlay = null; }
          var content = document.createElement('div');
          content.innerHTML = buildHudHTML(title, vals);
          hudOverlay = new kakao.maps.CustomOverlay({ position: position, content: content, xAnchor:0.5, yAnchor:1.05 });
          hudOverlay.setMap(map);
          var closeBtn = content.querySelector('.nc-close');
          if (closeBtn){
            closeBtn.addEventListener('click', function(){
              if (hudOverlay){ hudOverlay.setMap(null); hudOverlay = null; }
            });
          }
        },
        hide: function(){ if (hudOverlay){ hudOverlay.setMap(null); hudOverlay = null; } },
        atPolygon: function(poly, title, vals){
          NowcastLayer.hud.show(map, polygonCenter(poly), title, vals);
        }
      };

      // 아이콘(SVG) 문자열
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

    // hex → rgb
    function hexToRgb(hex) {
      hex = (hex || '#000000').replace('#','');
      var bigint = parseInt(hex, 16);
      return { r: (bigint>>16)&255, g: (bigint>>8)&255, b: bigint&255 };
    }

    // 4개 카테고리 미리 불러오기
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

    // 전국 스냅샷을 폴리곤에 칠하기(표준화 키 사용) + 파란계열 애니메이션
    function applySnapshot(rows, cat) {
      if (!$.isArray(rows)) rows = [];
      if (!App || !App.layers || !App.layers.sig) {
        console.warn('[NowcastLayer] polygon layer missing');
        return;
      }
      var byKey = {};
      rows.forEach(function (r) {
        var v = pickValue(r);
        var key = _slug(r.sidoNm, r.signguNm);
        byKey[key] = { value: v, display: fmt(v, UNIT[cat]) };
      });
      if (App.layers.sig.setData) App.layers.sig.setData(byKey, { category: cat, label: LABEL[cat] });
      if (App.layers.sig.repaint) App.layers.sig.repaint(); // 내부에서 애니메이션 처리
    }

    // 폴리곤 레이어 구성 + 시도 필터 지원
    function ensurePolygonLayer() {
      if (App.layers && App.layers.sig) return;

      // <body data-nowcast-geo="...">[, data-nowcast-geo2="..."] 지원
      var attr  = (document.body && document.body.getAttribute('data-nowcast-geo'))  || '';
      var extra = (document.body && document.body.getAttribute('data-nowcast-geo2')) || '';
      var urls = []
        .concat(attr ? attr.split(',') : [])
        .concat(extra ? [extra] : [])
        .map(function (s) { return (s || '').trim(); })
        .filter(function (s) { return !!s; });

      if (!urls.length) { console.warn('[NowcastLayer] no geo urls on <body>'); return; }
      console.log('[NowcastLayer] geo urls:', urls);

      var polyByKey = {};
      App.layers = App.layers || {};
      App.layers.sig = {
        _values: {},
        _byKey: polyByKey,
        _sidoFilterSlug: '', // '' = 전체
        setData: function (byKey, meta) { this._values = byKey || {}; this._meta = meta || {}; },
        setSidoFilter: function(sidoNm){
          this._sidoFilterSlug = _sidoSlug(sidoNm || '');
          this.repaint();
        },
        repaint: function () {
          var self = this;
          Object.keys(polyByKey).forEach(function (k) {
            var poly = polyByKey[k];

            // 가시성: 시·도 필터
            var visible = !self._sidoFilterSlug || (poly.__sidoSlug === self._sidoFilterSlug);
            if (poly.getMap() !== (visible ? map : null)) poly.setMap(visible ? map : null);
            if (!visible) return;

            // 목표 색상 (파란 계열): 값이 높을수록 진한 파랑
            var entry = self._values[k];
            var v = entry ? Number(entry.value) : NaN;

            var targetHex = '#BFD7FF'; // 매우 낮음
            if (!isNaN(v)) {
              targetHex = (v >= 25) ? '#004AAD' :    // 높음(진파랑)
                          (v >= 15) ? '#2E75FF' :    // 중간
                          (v >= 5)  ? '#6FA8FF' :    // 낮음
                                      '#BFD7FF';     // 매우 낮음
            }

            // 애니메이션: 현재색 → 목표색 보간
            var startHex = poly.__fillHex || '#BFD7FF';
            var steps = 12, step = 0, interval = 30; // 총 360ms
            var s = hexToRgb(startHex), t = hexToRgb(targetHex);
            clearInterval(poly.__animTimer);
            poly.__animTimer = setInterval(function(){
              step++;
              var ratio = step / steps;
              var r = Math.round(s.r + (t.r - s.r) * ratio);
              var g = Math.round(s.g + (t.g - s.g) * ratio);
              var b = Math.round(s.b + (t.b - s.b) * ratio);
              var col = 'rgb('+r+','+g+','+b+')';
              poly.setOptions({
                fillColor: col,
                fillOpacity: 0.6,
                strokeWeight: 1,
                strokeOpacity: 0.9,
                strokeColor: '#2b4a7d'
              });
              if (step >= steps){
                clearInterval(poly.__animTimer);
                poly.__fillHex = targetHex;
              }
            }, interval);
          });
        }
      };

      // ---- 속성 추출 (시도코드 보강 포함) ----
      function pickNames(p) {
        var SIDO_BY_PREFIX = {
          '11':'서울특별시','26':'부산광역시','27':'대구광역시','28':'인천광역시',
          '29':'광주광역시','30':'대전광역시','31':'울산광역시','36':'세종특별자치시',
          '41':'경기도','42':'강원특별자치도','43':'충청북도','44':'충청남도',
          '45':'전라북도','46':'전라남도','47':'경상북도','48':'경상남도',
          '50':'제주특별자치도'
        };
        var sido = p.SIDO_NM || p.CTP_KOR_NM || p.CTPRVN_NM || p.sidoNm || p.SIDO || p.sido || '';
        var sgg  = p.SIG_KOR_NM || p.SIGUNGU || p.SIG_NM || p.SGG_NM || p.signguNm || p.SGG || p.sigungu || '';
        if (!sido) {
          var sigcd = (p.SIG_CD || p.sig_cd || '').toString();
          if (sigcd.length >= 2 && SIDO_BY_PREFIX[sigcd.substr(0,2)]) {
            sido = SIDO_BY_PREFIX[sigcd.substr(0,2)];
          }
        }
        return { sido: (sido || '').trim(), sgg: (sgg || '').trim() };
      }

      // ---- 좌표 → 경로 변환(모든 링 처리) ----
      function toPaths(g) {
        var paths = [];
        if (!g) return paths;
        if (g.type === 'Polygon') {
          (g.coordinates || []).forEach(function (ring) {
            if (ring && ring.length) {
              paths.push(ring.map(function (c) { return new kakao.maps.LatLng(c[1], c[0]); }));
            }
          });
        } else if (g.type === 'MultiPolygon') {
          (g.coordinates || []).forEach(function (poly) {
            (poly || []).forEach(function (ring) {
              if (ring && ring.length) {
                paths.push(ring.map(function (c) { return new kakao.maps.LatLng(c[1], c[0]); }));
              }
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

        var key = _slug(n.sido, n.sgg); // 표준화 키
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

        // 클릭 시 HUD
        kakao.maps.event.addListener(poly, 'click', function () {
          $.getJSON(API.latest4Region(n.sido, n.sgg)).done(function (rows) {
            var vals = {T1H:null, RN1:null, WSD:null, REH:null};
            (rows||[]).forEach(function(r){
              var cat = (r.category || '').toUpperCase();
              var v = (r.OBSR_VALUE!=null)? r.OBSR_VALUE : (r.obsrValue!=null? r.obsrValue : r.val);
              if (vals.hasOwnProperty(cat)) vals[cat] = v;
            });
            NowcastLayer.hud.atPolygon(poly, n.sido + ' ' + n.sgg, vals);
          });
        });
      }

      function loadOne(url) {
        return $.getJSON(url).then(function (geo) {
          if (!geo || !geo.features) { console.warn('[NowcastLayer] bad geojson', url); return; }
          geo.features.forEach(addFeature);
        }).fail(function (xhr) {
          console.warn('[NowcastLayer] geo load fail', url, xhr && xhr.status);
        });
      }

      var tasks = urls.map(loadOne);
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
      var center = (function(){
        var p = poly.__paths || [];
        var sumLat=0,sumLng=0,n=0;
        for (var i=0;i<p.length;i++) for (var j=0;j<p[i].length;j++){ sumLat+=p[i][j].getLat(); sumLng+=p[i][j].getLng(); n++; }
        return (n? new kakao.maps.LatLng(sumLat/n, sumLng/n): new kakao.maps.LatLng(36.5,127.8));
      })();
      map.setBounds(polygonBounds(poly), 20, 20, 20, 120);

      $.getJSON(API.latest4Region(sidoNm, sggNm)).done(function (rows) {
        var vals = {T1H:null, RN1:null, WSD:null, REH:null};
        (rows||[]).forEach(function(r){
          var cat = (r.category || '').toUpperCase();
          var v = (r.OBSR_VALUE!=null)? r.OBSR_VALUE : (r.obsrValue!=null? r.obsrValue : r.val);
          if (vals.hasOwnProperty(cat)) vals[cat] = v;
        });
        NowcastLayer.hud.show(map, center, sidoNm+' '+sggNm, vals);
      });
    }

    // ===== 시·도 + 시군구 칩 UI =====
    function mountSidoSggFilterUI(){
      var SIDO_LIST = [
        "전체","강원특별자치도","경기도","경상남도","경상북도",
        "광주광역시","대구광역시","대전광역시","부산광역시",
        "서울특별시","세종특별자치시","울산광역시","인천광역시",
        "전라남도","전라북도","제주특별자치도","충청남도","충청북도"
      ];

      // host/card/wrap
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
        // 첫 칩 자동 실행 (원치 않으면 주석)
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
          else { sggWrap.innerHTML=''; sggCard.style.display='none'; NowcastLayer.hud.hide(); }
        });
        wrap.appendChild(btn);
      });

      // 외부에서 제어할 수 있도록
      NowcastLayer.setSidoFilter = function(sidoNm){
        var targetSlug = _sidoSlug(sidoNm || '');
        [].forEach.call(wrap.querySelectorAll('.sido-chip'), function(el){
          var slug = _sidoSlug(el.dataset.sido || '');
          el.classList.toggle('active', targetSlug ? slug===targetSlug : (el.dataset.sido||'')==='');
        });
        if (App.layers && App.layers.sig && App.layers.sig.setSidoFilter){ App.layers.sig.setSidoFilter(sidoNm||''); }
        if (sidoNm){ renderSggChips(sidoNm); } else { sggWrap.innerHTML=''; sggCard.style.display='none'; }
      };
    } // mountSidoSggFilterUI
  } // init
})(window);
