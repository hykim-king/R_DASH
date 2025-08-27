// /resources/map_js/app-map.js
/* eslint-disable */
(function (global) {
  'use strict';

  // ─────────────────────────────────────────────────────────────
  // 멱등 가드: 이미 초기화됐다면 재실행 금지
  // ─────────────────────────────────────────────────────────────
  if (global.AppMap && global.AppMap.__initialized) {
    console.log('[AppMap] already initialized → skip');
    return;
  }

  if (!global.kakao || !kakao.maps || !kakao.maps.load) {
    console.error('[AppMap] kakao maps not loaded');
    return;
  }

  // ─────────────────────────────────────────────────────────────
  // 내부: ready 이벤트 (한 번만) + 콜백 큐
  // ─────────────────────────────────────────────────────────────
  function dispatchReadyOnce(app) {
    if (global.__appmapReadyFired) return;
    global.__appmapReadyFired = true;

    try {
      global.dispatchEvent(new CustomEvent('appmap:ready'));
    } catch (e) {
      var ev = document.createEvent('Event');
      ev.initEvent('appmap:ready', true, true);
      global.dispatchEvent(ev);
    }
    console.log('[AppMap] appmap:ready fired');

    // ready 콜백 큐 실행
    if (app && Array.isArray(app.__readyQueue)) {
      app.__readyQueue.forEach(function (fn) { try { fn(app); } catch (e) { console.warn(e); } });
      app.__readyQueue.length = 0;
    }
  }

  // ─────────────────────────────────────────────────────────────
  // 컨텍스트 경로 감지
  // ─────────────────────────────────────────────────────────────
  function detectCtx() {
    var body = document.body || {};
    var ctx = body.getAttribute('data-ctx') || body.getAttribute('data-context-path');
    if (ctx && ctx.trim()) return ctx.trim();
    try {
      var m = (location.pathname || '').match(/^\/[^\/]+/);
      return (m && m[0]) || '';
    } catch (e) { return ''; }
  }

  // ─────────────────────────────────────────────────────────────
  // 지도 생성 (단일 인스턴스 보장)
  // ─────────────────────────────────────────────────────────────
  function createMap() {
    var el = document.getElementById('map');
    if (!el) { console.error('[AppMap] #map not found'); return; }

    var AppMap = (global.AppMap = global.AppMap || {});
    AppMap.__initialized = true;

    // 컨텍스트 경로 노출
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
    }

    // 레이어 레지스트리
    AppMap.layers    = AppMap.layers || {};
    AppMap._active   = AppMap._active || null;

    // 유틸: 노드/경계/BBox 파라미터
    AppMap.getNode   = function(){ return this.map && this.map.getNode ? this.map.getNode() : null; };
    AppMap.getBounds = function(){ return this.map && this.map.getBounds ? this.map.getBounds() : null; };
    AppMap.getBBoxParams = function () {
      var b = this.getBounds(); if (!b) return null;
      var sw = b.getSouthWest(), ne = b.getNorthEast();
      var minLat = Math.min(sw.getLat(), ne.getLat());
      var maxLat = Math.max(sw.getLat(), ne.getLat());
      var minLon = Math.min(sw.getLng(), ne.getLng());
      var maxLon = Math.max(sw.getLng(), ne.getLng());
      return { minLat: minLat, maxLat: maxLat, minLon: minLon, maxLon: maxLon };
    };

    // 레이어 등록
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

    // 레이어 활성화 (최대 6초 대기)
    AppMap.activate = function (name, opts) {
      var self = this, tries = 0, MAX = 120;
      function tryActivate() {
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
            if (prev && prev.instance && typeof prev.instance.deactivate === 'function') {
              prev.instance.deactivate();
            }
          }
          if (L.instance && typeof L.instance.activate === 'function') {
            L.instance.activate(opts || {});
          }
          self._active = name;
          console.log('[AppMap] activated:', name, 'opts:', opts || {});
        } catch (e) {
          console.error('[AppMap] activate error:', name, e);
        }
      }
      tryActivate();
    };

    // 창 크기 반영
    if (!AppMap.__resizeBound) {
      global.addEventListener('resize', function () {
        if (AppMap.map) kakao.maps.event.trigger(AppMap.map, 'resize');
      });
      AppMap.__resizeBound = true;
    }

    // 준비 이벤트 발행
    dispatchReadyOnce(AppMap);

    // URL/body 기반 자동 활성화
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

  kakao.maps.load(function () {
    console.log('[AppMap] kakao.maps.load ok');
    createMap();
  });

})(window);
