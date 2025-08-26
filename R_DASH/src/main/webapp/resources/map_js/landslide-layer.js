// /resources/map_js/landslide-layer.js
(function (global) {
  'use strict';

  // AppMap 준비되면 등록
  if (global.AppMap) register(); else global.addEventListener('appmap:ready', register, { once: true });

  function register() {
    var App = glo// /resources/map_js/landslide-layer.js
/* eslint-disable */
(function (global) {
  'use strict';

  // --- 레이어 가드: landslide 페이지에서만 동작 ---
  var qs = new URLSearchParams((typeof location !== 'undefined' && location.search) ? location.search : '');
  var LAYER = (qs.get('layer') || '').toLowerCase();
  var BODY_LAYER = (document.body && (document.body.getAttribute('data-layer') || '')).toLowerCase();
  var IS_LANDSLIDE = (LAYER === 'landslide') || (BODY_LAYER === 'landslide');
  if (!IS_LANDSLIDE) { console.log('[LandslideLayer] layer != landslide → skip'); return; }

  // AppMap 준비되면 등록
  if (global.AppMap) register(); else global.addEventListener('appmap:ready', register, { once: true });

  function register(global) {
    var App = global.AppMap || {};
    var map = App.map;
    if (!map || !global.kakao || !kakao.maps) {
      console.error('[LandslideLayer] kakao map not found'); return;
    }

    // === 설정 / API ===
    var BASE = (document.body && document.body.getAttribute('data-context-path')) || ''; // ex) "/ehr"
    var API  = { bbox: BASE + '/landslide/bbox' };

    // === 내부 상태 ===
    var clusterer = (kakao.maps.MarkerClusterer)
      ? new kakao.maps.MarkerClusterer({ map: map, averageCenter: true, minLevel: 12 })
      : null;

    var markerById = Object.create(null);
    var dataCache  = Object.create(null);
    var inflight   = null;
    var debounceTimer = null;

    var MAX_MARKER_LEVEL = 12; // 숫자 클수록 줌 아웃
    var LAYER_ON = false;

    // === AppMap 레이어 등록(자동 enable 금지) ===
    if (typeof App.register === 'function') {
      App.register('landslide', { on: enable, off: disable });
      console.log('[LandslideLayer] registered');
    } else {
      console.warn('[LandslideLayer] App.register not found; waiting for explicit enable (no auto-start)');
      // 자동 활성화하지 않음
    }

    // ---- enable / disable ----
    function enable() {
      if (LAYER_ON) return;
      LAYER_ON = true;

      kakao.maps.event.addListener(map, 'idle', onIdle); // bounds+zoom 모두 커버
      applyVisibilityByZoom();
      refresh();
      console.log('[LandslideLayer] enabled');
    }

    function disable() {
      if (!LAYER_ON) return;
      LAYER_ON = false;

      kakao.maps.event.removeListener(map, 'idle', onIdle);

      // 마커 정리
      var ids = Object.keys(markerById);
      for (var i = 0; i < ids.length; i++) {
        var mk = markerById[ids[i]];
        if (clusterer) clusterer.removeMarker(mk);
        if (mk && mk.setMap) mk.setMap(null);
      }
      markerById = Object.create(null);
      dataCache  = Object.create(null);

      if (inflight && inflight.abort) inflight.abort();
      inflight = null;
      clearTimeout(debounceTimer);

      console.log('[LandslideLayer] disabled');
    }

    function onIdle() {
      if (!LAYER_ON) return;
      clearTimeout(debounceTimer);
      debounceTimer = setTimeout(function () {
        applyVisibilityByZoom();
        refresh();
      }, 150);
    }

    // ---- 데이터 로드/반영 ----
    function refresh() {
      if (!LAYER_ON) return;

      var bounds = map.getBounds();
      if (!bounds) return;

      var pad = getPaddedBounds(bounds, 0.05);
      var sw = pad.getSouthWest(), ne = pad.getNorthEast();

      var params = {
        minLat: sw.getLat(), minLon: sw.getLng(),
        maxLat: ne.getLat(), maxLon: ne.getLng(),
        level: map.getLevel()
      };

      if (inflight && inflight.abort) { inflight.abort(); inflight = null; }

      inflight = $.ajax({
        url: API.bbox,
        method: 'GET',
        data: params,
        dataType: 'json',
        timeout: 15000
      })
      .done(function (rows) {
        if (!Array.isArray(rows)) rows = [];
        mergeCache(rows);
        if (!clusterer) syncMarkersWithin(bounds);
        applyVisibilityByZoom();
      })
      .fail(function (xhr, status) {
        if (status !== 'abort') {
          console.error('[LandslideLayer] bbox load failed:', xhr && xhr.status, xhr && xhr.responseText);
        }
      })
      .always(function () { inflight = null; });
    }

    function mergeCache(rows) {
      for (var i = 0; i < rows.length; i++) {
        var r = rows[i];
        if (!r || r.id == null) continue;
        dataCache[r.id] = r;
        if (!markerById[r.id]) {
          var mk = createMarker(r);
          markerById[r.id] = mk;
          if (clusterer) clusterer.addMarker(mk); else mk.setMap(map);
        }
      }
    }

    function syncMarkersWithin(bounds) {
      var ids = Object.keys(markerById);
      for (var i = 0; i < ids.length; i++) {
        var id = ids[i], mk = markerById[id];
        var inside = bounds.contain(mk.getPosition());
        mk.setMap(inside ? map : null);
      }
    }

    function applyVisibilityByZoom() {
      var level = map.getLevel();
      var showIndividual = (level <= MAX_MARKER_LEVEL);
      var ids = Object.keys(markerById);
      for (var i = 0; i < ids.length; i++) {
        var mk = markerById[ids[i]];
        if (clusterer) {
          mk.setMap(showIndividual ? map : null);
        } else {
          mk.setMap(showIndividual ? (mk.getMap() || map) : null);
        }
      }
    }

    function createMarker(row) {
      var lat = Number(row.lat), lon = Number(row.lon);
      if (isNaN(lat) || isNaN(lon)) { lat = 36.5; lon = 127.8; }
      var pos = new kakao.maps.LatLng(lat, lon);

      var marker = new kakao.maps.Marker({ position: pos, title: row.title || String(row.id) });

      var info = new kakao.maps.InfoWindow({ removable: true, content: infoHTML(row) });
      kakao.maps.event.addListener(marker, 'click', function () { info.open(map, marker); });

      return marker;
    }

    function infoHTML(r) {
      var date = (r.date || '').toString();
      var dmg  = (r.damage != null ? r.damage : '-');
      var ttl  = r.title || '산사태';
      return [
        '<div style="padding:8px 10px;min-width:180px">',
        ' <div style="font-weight:700;margin-bottom:6px">', escapeHTML(ttl), '</div>',
        ' <div><b>발생일:</b> ', escapeHTML(date), '</div>',
        ' <div><b>피해:</b> ', escapeHTML(String(dmg)), '</div>',
        '</div>'
      ].join('');
    }

    function getPaddedBounds(bounds, rate) {
      rate = rate || 0.05;
      var sw = bounds.getSouthWest(), ne = bounds.getNorthEast();
      var dLat = (ne.getLat() - sw.getLat()) * rate;
      var dLon = (ne.getLng() - sw.getLng()) * rate;
      return new kakao.maps.LatLngBounds(
        new kakao.maps.LatLng(sw.getLat() - dLat, sw.getLng() - dLon),
        new kakao.maps.LatLng(ne.getLat() + dLat, ne.getLng() + dLon)
      );
    }

    function escapeHTML(s) {
      return String(s)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
    }
  }
})(window);
bal.AppMap || {};
    var map = App.map;
    if (!map || !global.kakao || !kakao.maps) {
      console.error('[LandslideLayer] kakao map not found');
      return;
    }

    // === 설정 / API ===
    var BASE = (document.body && document.body.getAttribute('data-context-path')) || ''; // ex) "/ehr"
    var API  = {
      bbox: BASE + '/landslide/bbox'
    };

    // === 내부 상태 ===
    var clusterer = (kakao.maps.MarkerClusterer)
      ? new kakao.maps.MarkerClusterer({ map: map, averageCenter: true, minLevel: 12 })
      : null;

    var markerById = Object.create(null);
    var dataCache  = Object.create(null);
    var inflight   = null;
    var debounceTimer = null;

    var MAX_MARKER_LEVEL = 12; // 숫자 클수록 줌 아웃
    var LAYER_ON = false;

    // === AppMap 레이어 등록 ===
    // FIX #2: landslide 레이어를 공식 등록해야 AppMap.activate('landslide') 가 동작합니다.
    if (typeof App.register === 'function') {
      App.register('landslide', {
        on: enable,
        off: disable
      });
      console.log('[LandslideLayer] registered');
    } else {
      console.warn('[LandslideLayer] App.register not found; fallback to auto-enable');
      enable(); // 레지스트리 없는 환경에서는 즉시 활성
    }

    // ---- enable / disable ----
    function enable() {
      if (LAYER_ON) return;
      LAYER_ON = true;

      kakao.maps.event.addListener(map, 'bounds_changed', onBounds);
      kakao.maps.event.addListener(map, 'zoom_changed', onZoom);

      applyVisibilityByZoom();
      refresh();
      console.log('[LandslideLayer] enabled');
    }

    function disable() {
      if (!LAYER_ON) return;
      LAYER_ON = false;

      kakao.maps.event.removeListener(map, 'bounds_changed', onBounds);
      kakao.maps.event.removeListener(map, 'zoom_changed', onZoom);

      // 마커 정리(파괴)
      var ids = Object.keys(markerById);
      for (var i = 0; i < ids.length; i++) {
        var mk = markerById[ids[i]];
        if (clusterer) clusterer.removeMarker(mk);
        mk.setMap && mk.setMap(null);
      }
      markerById = Object.create(null);
      dataCache  = Object.create(null);

      if (inflight && inflight.abort) inflight.abort();
      inflight = null;
      clearTimeout(debounceTimer);

      console.log('[LandslideLayer] disabled');
    }

    function onBounds() {
      if (!LAYER_ON) return;
      clearTimeout(debounceTimer);
      debounceTimer = setTimeout(refresh, 180);
    }

    function onZoom() {
      if (!LAYER_ON) return;
      applyVisibilityByZoom();
      clearTimeout(debounceTimer);
      debounceTimer = setTimeout(refresh, 80);
    }

    // ---- 데이터 로드/반영 ----
    function refresh() {
      if (!LAYER_ON) return;

      var bounds = map.getBounds();
      if (!bounds) return;

      var pad = getPaddedBounds(bounds, 0.05);
      var sw = pad.getSouthWest(), ne = pad.getNorthEast();

      var minLat = sw.getLat(), minLon = sw.getLng();
      var maxLat = ne.getLat(), maxLon = ne.getLng();

      if (inflight && inflight.abort) { inflight.abort(); inflight = null; }

      inflight = $.getJSON(API.bbox, {
        // 서버 파라미터명에 맞게 수정하세요.
        minLat: minLat, maxLat: maxLat,
        minLon: minLon, maxLon: maxLon,
        level: map.getLevel()
      }).done(function (rows) {
        if (!Array.isArray(rows)) rows = [];
        mergeCache(rows);
        syncMarkersWithin(bounds);
        applyVisibilityByZoom();
      }).fail(function (xhr, status) {
        if (status !== 'abort') console.error('[LandslideLayer] bbox load failed:', status);
      }).always(function () { inflight = null; });
    }

    function mergeCache(rows) {
      for (var i = 0; i < rows.length; i++) {
        var r = rows[i];
        if (!r || r.id == null) continue;
        dataCache[r.id] = r;
        if (!markerById[r.id]) {
          var mk = createMarker(r);
          markerById[r.id] = mk;
          if (clusterer) clusterer.addMarker(mk); else mk.setMap(map);
        }
      }
    }

    function syncMarkersWithin(bounds) {
      if (clusterer) return; // 클러스터러가 알아서 관리
      var ids = Object.keys(markerById);
      for (var i = 0; i < ids.length; i++) {
        var id = ids[i], mk = markerById[id];
        var inside = bounds.contain(mk.getPosition());
        mk.setMap(inside ? map : null);
      }
    }

    function applyVisibilityByZoom() {
      var level = map.getLevel();

      // FIX #1: 줌 레벨 비교 방향 실수 방지
      var showIndividual = (level <= MAX_MARKER_LEVEL);

      var ids = Object.keys(markerById);
      for (var i = 0; i < ids.length; i++) {
        var mk = markerById[ids[i]];
        if (clusterer) {
          // 클러스터러 사용 시: 멀리서는 원본 마커를 map에서 내리고(클러스터만 보이게), 가까이서는 보이게
          mk.setMap(showIndividual ? map : null);
        } else {
          mk.setMap(showIndividual ? mk.getMap() || map : null);
        }
      }
    }

    function createMarker(row) {
      // FIX: lat/lon 뒤바뀜 방지
      var lat = Number(row.lat), lon = Number(row.lon);
      if (isNaN(lat) || isNaN(lon)) { lat = 36.5; lon = 127.8; }
      var pos = new kakao.maps.LatLng(lat, lon);

      var marker = new kakao.maps.Marker({
        position: pos,
        title: row.title || String(row.id)
      });

      var info = new kakao.maps.InfoWindow({
        removable: true,
        content: infoHTML(row)
      });

      kakao.maps.event.addListener(marker, 'click', function () {
        info.open(map, marker);
      });

      return marker;
    }

    function infoHTML(r) {
      var date = (r.date || '').toString();
      var dmg  = (r.damage != null ? r.damage : '-');
      var ttl  = r.title || '산사태';
      return [
        '<div style="padding:8px 10px;min-width:180px">',
        ' <div style="font-weight:700;margin-bottom:6px">', escapeHTML(ttl), '</div>',
        ' <div><b>발생일:</b> ', escapeHTML(date), '</div>',
        ' <div><b>피해:</b> ', escapeHTML(String(dmg)), '</div>',
        '</div>'
      ].join('');
    }

    function getPaddedBounds(bounds, rate) {
      rate = rate || 0.05;
      var sw = bounds.getSouthWest(), ne = bounds.getNorthEast();
      var dLat = (ne.getLat() - sw.getLat()) * rate;
      var dLon = (ne.getLng() - sw.getLng()) * rate;
      return new kakao.maps.LatLngBounds(
        new kakao.maps.LatLng(sw.getLat() - dLat, sw.getLng() - dLon),
        new kakao.maps.LatLng(ne.getLat() + dLat, ne.getLng() + dLon)
      );
    }

    function escapeHTML(s) {
      return String(s)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;');
    }
  }

// FIX #1: IIFE 에 window를 전달해야 global이 undefined가 아닙니다.
})(window);
