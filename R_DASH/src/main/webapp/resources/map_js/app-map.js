// /resources/map_js/app-map.js
/* eslint-disable */
(function (global) {
  'use strict';

  // ---- appmap:ready 이벤트 발행 ----
  function dispatchReady() {
    try {
      global.dispatchEvent(new CustomEvent('appmap:ready'));
    } catch (e) {
      var ev = document.createEvent('Event');
      ev.initEvent('appmap:ready', true, true);
      global.dispatchEvent(ev);
    }
    console.log('[AppMap] appmap:ready fired');
  }

  // ---- 지도 생성 ----
  function createMap() {
    var el = document.getElementById('map');
    if (!el) { console.error('[AppMap] #map not found'); return; }

    var center = new kakao.maps.LatLng(36.5, 127.8);
    var map = new kakao.maps.Map(el, { center: center, level: 12 });

    var AppMap = (global.AppMap = {
      map: map,
      layers: {},   // {name: {factory, instance}}
      _active: null,

      /**
       * ✅ register(name, factoryFn)
       * ✅ register(name, instanceObj)
       * ✅ register({ name, activate, deactivate })  // 레거시(객체 하나)
       */
      register: function (name, factoryOrInstance) {
        // 레거시: register({ name, activate, ... })
        if (typeof name === 'object' && name && name.name) {
          var obj = name;
          this.layers[obj.name] = { factory: function(){ return obj; }, instance: obj };
          console.log('[AppMap] registered (legacy object):', obj.name);
          return;
        }

        // 표준: register(name, factoryFn)
        if (typeof factoryOrInstance === 'function') {
          this.layers[name] = { factory: factoryOrInstance, instance: null };
          console.log('[AppMap] registered:', name);
          return;
        }

        // 지원: register(name, instanceObj)
        if (factoryOrInstance && typeof factoryOrInstance === 'object') {
          var inst = factoryOrInstance;
          this.layers[name] = { factory: function(){ return inst; }, instance: inst };
          console.log('[AppMap] registered (object):', name);
          return;
        }

        console.warn('[AppMap] invalid register call', name);
      },

      // 구버전 호환 별칭
      registerLayer: null,

      // 유틸: 레이어 조회/상태
      getLayer: function(name){ return (this.layers && this.layers[name]) ? this.layers[name].instance : null; },
      getActiveName: function(){ return this._active; },
      getNode: function(){ return this.map && this.map.getNode ? this.map.getNode() : null; },
      getBounds: function(){ return this.map && this.map.getBounds ? this.map.getBounds() : null; },

      // 비활성화(단건/전체)
      deactivate: function(name){
        var L = this.layers[name];
        if (L && L.instance && typeof L.instance.deactivate === 'function') L.instance.deactivate();
        if (this._active === name) this._active = null;
      },
      deactivateAll: function(){
        var self = this;
        Object.keys(this.layers).forEach(function(k){ self.deactivate(k); });
      },

      // 레이어 활성화 (최대 6초 대기)
      activate: function (name, opts) {
        var self = this, tries = 0, MAX = 120; // 120 * 50ms = 6s
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
            console.log('[AppMap] activated:', name);
          } catch (e) {
            console.error('[AppMap] activate error:', name, e);
          }
        }
        tryActivate();
      }
    });

    // 구버전 호환
    AppMap.registerLayer = AppMap.register;

    // 크기 변화 대응(선택)
    global.addEventListener('resize', function () {
      kakao.maps.event.trigger(map, 'resize');
    });

    // 준비 이벤트
    dispatchReady();

    // URL/body 기반 자동 활성화
    var qs = new URLSearchParams((typeof location !== 'undefined' && location.search) ? location.search : '');
    var LAYER = (qs.get('layer') || '').toLowerCase();
    var BODY_LAYER = (document.body && (document.body.getAttribute('data-layer') || '')).toLowerCase();
    var target = LAYER || BODY_LAYER;
    if (target) {
      var opts = {};
      var airType = qs.get('airType') || (document.body && document.body.getAttribute('data-airtype'));
      if (airType) opts.airType = airType;
      AppMap.activate(target, opts);
    }
  }

  // Kakao SDK 로딩 확인 후 지도 생성
  if (!global.kakao || !kakao.maps || !kakao.maps.load) {
    console.error('[AppMap] kakao maps not loaded');
    return;
  }

  kakao.maps.load(function () {
    console.log('[AppMap] kakao.maps.load ok');
    createMap();
  });

})(window);
