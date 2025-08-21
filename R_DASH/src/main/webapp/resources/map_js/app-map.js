// map_js/app-map.js
// Kakao 지도 초기화 + 레이어 등록/교체 + BBox 유틸

(function (w) {
  'use strict';

  var AppMap = {
    map: null,
    _layers: {},       // name -> { activate(), deactivate() }
    _active: null,     // active layer name
    registerLayer: registerLayer,
    showOnly: showOnly,
    getBBox: getBBox
  };

  // ---- 지도 초기화 ----
  kakao.maps.load(function () {
    var container = document.getElementById('map');
    var options = {
      center: new kakao.maps.LatLng(36.5, 127.9),
      level: 12
    };
    var map = new kakao.maps.Map(container, options);
    AppMap.map = map;

    // 페이지 로더에 알려주기
    w.AppMap = AppMap;
    var evt = new Event('appmap:ready');
    w.dispatchEvent(evt);
  });

  function registerLayer(name, handlers) {
    AppMap._layers[name] = handlers || {};
  }

  function showOnly(name, opts) {
    if (AppMap._active && AppMap._layers[AppMap._active] && AppMap._active !== name) {
      try { AppMap._layers[AppMap._active].deactivate && AppMap._layers[AppMap._active].deactivate(); } catch(e){}
    }
    if (AppMap._layers[name]) {
      AppMap._layers[name].activate && AppMap._layers[name].activate(opts || {});
      AppMap._active = name;
    }
  }

  // 현재 화면 BBox (min/max lat/lon)
  function getBBox() {
    var map = AppMap.map;
    var proj = map.getProjection();
    var sw = proj.coordsFromContainerPoint(new kakao.maps.Point(0, map.getNode().clientHeight));
    var ne = proj.coordsFromContainerPoint(new kakao.maps.Point(map.getNode().clientWidth, 0));
    return {
      minLat: sw.getLat(), maxLat: ne.getLat(),
      minLon: sw.getLng(), maxLon: ne.getLng()
    };
  }
})(window);
