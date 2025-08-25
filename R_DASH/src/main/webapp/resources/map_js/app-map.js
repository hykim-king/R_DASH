// /resources/map_js/app-map.js
(function (w) {
  'use strict';

  var AppMap = {
    map: null,
    _layers: {},
    _active: null,
    _pending: null,
    init: init,
    registerLayer: registerLayer,
    activate: activate,
    deactivate: deactivate,
    getBBox: getBBox,
    // 호환용
    showOnly: function(name, opts){ activate(name, opts); }
  };
  w.AppMap = AppMap; // 초기에 노출

  kakao.maps.load(function () {
    init();
    w.dispatchEvent(new Event('appmap:ready'));
    if (AppMap._pending) {
      var p = AppMap._pending; AppMap._pending = null;
      activate(p.name, p.opts);
    }
  });

  function init() {
    if (AppMap.map) return;
    var el = document.getElementById('map');
    if (!el) return;
    AppMap.map = new kakao.maps.Map(el, {
      center: new kakao.maps.LatLng(36.5, 127.9),
      level: 12
    });
  }

function registerLayer(name, handlers) {
  AppMap._layers[name] = handlers || {};

  // 만약 activate 대기중인 레이어가 지금 막 등록됐다면 바로 실행
  if (AppMap._pending && AppMap._pending.name === name && AppMap.map) {
    var p = AppMap._pending; 
    AppMap._pending = null;
    activate(name, p.opts || {});
  }
}

function activate(name, opts) {
  if (!AppMap.map) { 
    AppMap._pending = { name: name, opts: opts || {} }; 
    return; 
  }
  if (!AppMap._layers[name]) {
    // 아직 등록되지 않은 레이어면 pending으로 남겨둔다
    AppMap._pending = { name: name, opts: opts || {} };
    return;
  }

  if (AppMap._active && AppMap._active !== name) {
    try { AppMap._layers[AppMap._active].deactivate && AppMap._layers[AppMap._active].deactivate(); } catch(e){}
  }
  try { AppMap._layers[name].activate && AppMap._layers[name].activate(opts || {}); } catch(e){}
  AppMap._active = name;
}

  function deactivate(name) {
    var n = name || AppMap._active;
    if (n && AppMap._layers[n]) {
      try { AppMap._layers[n].deactivate && AppMap._layers[n].deactivate(); } catch(e){}
      if (!name) AppMap._active = null;
    }
  }

  function getBBox() {
    if (!AppMap.map) return null;
    var b = AppMap.map.getBounds();
    var sw = b.getSouthWest(), ne = b.getNorthEast();
    return {
      minLat: sw.getLat(), maxLat: ne.getLat(),
      minLon: sw.getLng(), maxLon: ne.getLng()
    };
  }
})(window);
