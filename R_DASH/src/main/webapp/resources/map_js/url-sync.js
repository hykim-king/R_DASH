// /resources/map_js/url-sync.js
(function () {
  'use strict';

  function qs() { return new URLSearchParams(location.search); }
  function setQS(obj) {
    var p = qs();
    Object.keys(obj).forEach(function (k) {
      var v = obj[k];
      if (v === null || v === undefined || v === '') p.delete(k);
      else p.set(k, v);
    });
    var u = location.pathname + (p.toString() ? '?' + p.toString() : '');
    history.replaceState({ qs: p.toString() }, '', u);
  }
  function putBBoxInURL() {
    if (!window.AppMap || !AppMap.getBBox) return;
    var b = AppMap.getBBox(); if (!b) return;
    setQS({
      minLat: Number(b.minLat).toFixed(4),
      maxLat: Number(b.maxLat).toFixed(4),
      minLon: Number(b.minLon).toFixed(4),
      maxLon: Number(b.maxLon).toFixed(4)
    });
  }
  function activateFromURL(retry) {
    var p = qs();
    var layer = p.get('layer');
    if (!layer) return;
    var opts = {};
    p.forEach(function (v, k) { if (k !== 'layer') opts[k] = v; });
    if (layer === 'dust' && !opts.airType) opts.airType = 'ALL';
    if (!window.AppMap || !AppMap.activate) {
      if ((retry || 0) < 50) return setTimeout(function(){ activateFromURL((retry||0)+1); }, 100);
      return;
    }
    AppMap.activate(layer, opts);
    putBBoxInURL();
  }

  document.addEventListener('DOMContentLoaded', function () {
    activateFromURL(0);

    var dustLink = document.getElementById('nav-dust');
    if (!dustLink) {
      var ctx = document.body && document.body.getAttribute('data-context-path') || '';
      var dustPath = ctx + '/dust';
      var links = document.querySelectorAll('a.nav-link, a');
      for (var i = 0; i < links.length; i++) {
        var href = links[i].getAttribute('href') || '';
        if (href === dustPath || href.endsWith('/dust')) { dustLink = links[i]; break; }
      }
    }

    if (dustLink) {
      dustLink.addEventListener('click', function (e) {
        var ctx = document.body && document.body.getAttribute('data-context-path') || '';
        var mapPath = ctx + '/map';
        if (location.pathname === mapPath) {
          e.preventDefault();
          setQS({ layer: 'dust', airType: 'ALL' });
          if (window.AppMap && AppMap.activate) AppMap.activate('dust', { airType: 'ALL' });
          putBBoxInURL();
        }
      });
    }

    document.addEventListener('click', function (e) {
      var btn = e.target.closest('#dustFilter .flt');
      if (!btn) return;
      var air = btn.getAttribute('data-air') || 'ALL';
      setQS({ layer: 'dust', airType: air, limit: 500 });
      if (window.AppMap && AppMap.activate) AppMap.activate('dust', { airType: air });
      putBBoxInURL();
    });

    window.addEventListener('popstate', function(){ activateFromURL(0); });
    window.addEventListener('appmap:ready', function(){ activateFromURL(0); });
  });
})();
