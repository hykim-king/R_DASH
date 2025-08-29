/* eslint-disable */
(function (global) {
  'use strict';

  // ── 멱등 가드
  if (global.AppMap && global.AppMap.__initialized) {
    console.log('[AppMap] already initialized → skip');
    return;
  }

  // ── ready 이벤트 (1회)
  function dispatchReadyOnce(app) {
    if (global.__appmapReadyFired) return;
    global.__appmapReadyFired = true;

    try { global.dispatchEvent(new CustomEvent('appmap:ready')); }
    catch (e) {
      var ev = document.createEvent('Event');
      ev.initEvent('appmap:ready', true, true);
      global.dispatchEvent(ev);
    }
    console.log('[AppMap] appmap:ready fired');

    if (app && Array.isArray(app.__readyQueue)) {
      app.__readyQueue.forEach(function (fn) { try { fn(app); } catch (e) { console.warn(e); } });
      app.__readyQueue.length = 0;
    }
  }

  function detectCtx() {
    var body = document.body || {};
    var ctx = body.getAttribute('data-ctx') || body.getAttribute('data-context-path');
    if (ctx && ctx.trim()) return ctx.trim();
    try {
      var m = (location.pathname || '').match(/^\/[^\/]+/);
      return (m && m[0]) || '';
    } catch (e) { return ''; }
  }

  // ── 지도 생성
  function createMap() {
    var el = document.getElementById('map');
    if (!el) { console.error('[AppMap] #map not found'); return; }

    var AppMap = (global.AppMap = global.AppMap || {});
    AppMap.__initialized = true;
    AppMap.ctx = AppMap.ctx || detectCtx();

    // ready 콜백 지원
    AppMap.ready = function (fn) {
      if (global.__appmapReadyFired) { try { fn(AppMap); } catch (e) {} return; }
      this.__readyQueue = this.__readyQueue || [];
      this.__readyQueue.push(fn);
    };

    if (AppMap.map && AppMap.map.getNode) {
      console.log('[AppMap] map exists → reuse');
    } else {
      var center = new kakao.maps.LatLng(36.5, 127.8);
      AppMap.map = new kakao.maps.Map(el, { center: center, level: 12 });
      console.log('[AppMap] map created');

      // 🔒 축소 한계(너무 멀리 못 가게)
      AppMap.map.setMaxLevel(13);
      // 필요 시 과도한 확대도 제한하려면 주석 해제
      // AppMap.map.setMinLevel(2);

      // ─────────────────────────────────────────────────────────
      // 🔒 이동 범위 제한: 대한민국(+제주+독도) 대략 경계
      //   남서(33.0,124.0) ↔ 북동(39.6,132.5) 정도를 안전 여유 포함으로 설정
      //   값은 화면/디자인에 맞게 미세조정 가능
      // ─────────────────────────────────────────────────────────
      var allowedBounds = new kakao.maps.LatLngBounds(
        new kakao.maps.LatLng(33.0, 128.0),  // SW 
        new kakao.maps.LatLng(39.6, 134.5)   // NE
      );

      // 마지막으로 유효했던 센터(경계 안)
      var lastValidCenter = AppMap.map.getCenter();

      function isInsideBounds(latlng) {
        return allowedBounds.contain(latlng);
      }

      // 경계 밖으로 나가면 가장 가까운 점으로 "클램프"해서 센터를 되돌림
      function clampToBounds(latlng) {
        var lat = latlng.getLat();
        var lng = latlng.getLng();

        // bounds 경계값
        var sw = allowedBounds.getSouthWest();
        var ne = allowedBounds.getNorthEast();
        var clampedLat = Math.max(sw.getLat(), Math.min(ne.getLat(), lat));
        var clampedLng = Math.max(sw.getLng(), Math.min(ne.getLng(), lng));
        return new kakao.maps.LatLng(clampedLat, clampedLng);
      }

      // 너무 자주 호출되는 center_changed 는 가볍게 스로틀
      var throttleTimer = null;
      kakao.maps.event.addListener(AppMap.map, 'center_changed', function () {
        if (throttleTimer) return;
        throttleTimer = setTimeout(function () {
          throttleTimer = null;
          var c = AppMap.map.getCenter();
          if (isInsideBounds(c)) {
            lastValidCenter = c;
          } else {
            // 부드럽게 끌어오기
            AppMap.map.setCenter(clampToBounds(c));
          }
        }, 80); // 80ms 스로틀
      });

      // 드래그 종료/줌 변경 시에도 보정(보장)
      kakao.maps.event.addListener(AppMap.map, 'dragend', function () {
        var c = AppMap.map.getCenter();
        if (!isInsideBounds(c)) AppMap.map.setCenter(clampToBounds(c));
      });
      kakao.maps.event.addListener(AppMap.map, 'zoom_changed', function () {
        var c = AppMap.map.getCenter();
        if (!isInsideBounds(c)) AppMap.map.setCenter(clampToBounds(c));
      });

      // 초기에도 혹시 벗어나 있으면 보정
      (function ensureInitialInside(){
        var c = AppMap.map.getCenter();
        if (!isInsideBounds(c)) AppMap.map.setCenter(clampToBounds(c));
      })();
    }

    // 레이어 레지스트리
    AppMap.layers    = AppMap.layers || {};
    AppMap._active   = AppMap._active || null;

    AppMap.getNode   = function(){ return this.map && this.map.getNode ? this.map.getNode() : null; };
    AppMap.getBounds = function(){ return this.map && this.map.getBounds ? this.map.getBounds() : null; };
    AppMap.getBBoxParams = function () {
      var b = this.getBounds(); if (!b) return null;
      var sw = b.getSouthWest(), ne = b.getNorthEast();
      return {
        minLat: Math.min(sw.getLat(), ne.getLat()),
        maxLat: Math.max(sw.getLat(), ne.getLat()),
        minLon: Math.min(sw.getLng(), ne.getLng()),
        maxLon: Math.max(sw.getLng(), ne.getLng())
      };
    };

    AppMap.register = function (name, factoryOrInstance) {
      if (typeof name === 'object' && name && name.name) {
        var obj = name;
        this.layers[obj.name] = { factory: function(){ return obj; }, instance: obj };
        console.log('[AppMap] registered (legacy object):', obj.name);
        return;
      }
      if (typeof factoryOrInstance === 'function') {
        this.layers[name] = { factory: factoryOrInstance, instance: null };
        console.log('[AppMap] registered:', name);
        return;
      }
      if (factoryOrInstance && typeof factoryOrInstance === 'object') {
        var inst = factoryOrInstance;
        this.layers[name] = { factory: function(){ return inst; }, instance: inst };
        console.log('[AppMap] registered (object):', name);
        return;
      }
      console.warn('[AppMap] invalid register call', name);
    };
    AppMap.registerLayer = AppMap.register;

    AppMap.getLayer      = function(name){ return (this.layers[name]||{}).instance || null; };
    AppMap.getActiveName = function(){ return this._active; };

    AppMap.deactivate = function(name){
      var L = this.layers[name];
      if (L && L.instance && typeof L.instance.deactivate === 'function') {
        try { L.instance.deactivate(); } catch (e) { console.warn('[AppMap] deactivate error:', name, e); }
      }
      if (this._active === name) this._active = null;
    };
    AppMap.deactivateAll = function(){
      var self = this;
      Object.keys(this.layers).forEach(function(k){ self.deactivate(k); });
    };

    AppMap.activate = function (name, opts) {
      var self = this, tries = 0, MAX = 120;
      (function tryActivate() {
        var L = self.layers[name];
        if (!L) {
          if (tries++ < MAX) return setTimeout(tryActivate, 50);
          console.warn('[AppMap] layer not found:', name);
          return;
        }
        if (!L.instance) {
          try {
            L.instance = (typeof L.factory === 'function') ? L.factory({ map: self.map, App: self }) : L.factory;
          } catch (e) {
            console.error('[AppMap] layer factory failed:', name, e);
            return;
          }
        }
        try {
          if (self._active && self._active !== name) {
            var prev = self.layers[self._active];
            if (prev && prev.instance && typeof prev.instance.deactivate === 'function') prev.instance.deactivate();
          }
          if (L.instance && typeof L.instance.activate === 'function') L.instance.activate(opts || {});
          self._active = name;
          console.log('[AppMap] activated:', name, 'opts:', opts || {});
        } catch (e) { console.error('[AppMap] activate error:', name, e); }
      })();
    };

    if (!AppMap.__resizeBound) {
      global.addEventListener('resize', function () {
        if (AppMap.map) kakao.maps.event.trigger(AppMap.map, 'resize');
      });
      AppMap.__resizeBound = true;
    }

    // 준비 이벤트
    dispatchReadyOnce(AppMap);

    // URL/body 자동 활성화
    var qs = new URLSearchParams((typeof location !== 'undefined' && location.search) ? location.search : '');
    var LAYER = (qs.get('layer') || '').toLowerCase();
    var BODY_LAYER = (document.body && (document.body.getAttribute('data-layer') || '')).toLowerCase();
    var target = LAYER || BODY_LAYER;
    if (target && AppMap.getActiveName() !== target) {
      var opts = {};
      var airType = qs.get('airType') || (document.body && document.body.getAttribute('data-airtype'));
      if (airType) opts.airType = airType;
      AppMap.activate(target, opts);
    }
  }

  // ── Kakao SDK 로드를 기다렸다가 초기화 (SDK 순서 무관)
  (function waitKakao(deadlineMs){
    var deadline = Date.now() + (deadlineMs || 8000);
    (function tick(){
      if (global.kakao && kakao.maps && typeof kakao.maps.load === 'function') {
        kakao.maps.load(function(){
          console.log('[AppMap] kakao.maps.load ok');
          createMap();
        });
        return;
      }
      if (Date.now() > deadline) {
        console.error('[AppMap] Kakao SDK not loaded (check script tag & network).');
        return;
      }
      setTimeout(tick, 50);
    })();
  })(8000);

})(window);
