(function () {
  'use strict';

 // ▼ BBox를 주소창에 넣을지 여부. 깔끔하게 가려면 false.
  var WRITE_BBOX_TO_URL = false;   // <-- 이 줄 추가



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
  function putBBoxInURL(){
  if (!WRITE_BBOX_TO_URL) return; // ← 끔
  if (!window.AppMap || !AppMap.getBBox) return;
  var b = AppMap.getBBox();
  setQS({
    minLat: Number(b.minLat).toFixed(4),
    maxLat: Number(b.maxLat).toFixed(4),
    minLon: Number(b.minLon).toFixed(4),
    maxLon: Number(b.maxLon).toFixed(4)
  });
}
  function highlightDust(air){
    var wrap = document.getElementById('dustFilter'); if (!wrap) return;
    var btns = wrap.querySelectorAll('.flt');
    for (var i=0;i<btns.length;i++){
      var on = btns[i].getAttribute('data-air') === air;
      btns[i].classList.toggle('active', on);
      btns[i].style.borderColor = on ? '#111' : '#eee';
      btns[i].style.boxShadow  = on ? 'inset 0 0 0 1px #111' : 'none';
    }
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
    if (layer === 'dust') highlightDust(opts.airType || 'ALL');
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
          if (!window.AppMap || !AppMap.activate) {
            window.addEventListener('appmap:ready', function once(){
              AppMap.activate('dust', { airType: 'ALL' });
              highlightDust('ALL');
              putBBoxInURL();
            }, { once:true });
            return;
          }
          AppMap.activate('dust', { airType: 'ALL' });
          highlightDust('ALL');
          putBBoxInURL();
        }
      });
    }

    document.addEventListener('click', function (e) {
      var btn = e.target.closest('#dustFilter .flt');
      if (!btn) return;
      var air = btn.getAttribute('data-air') || 'ALL';
      setQS({ layer: 'dust', airType: air });
      if (window.AppMap && AppMap.activate) AppMap.activate('dust', { airType: air });
      highlightDust(air);
      putBBoxInURL();
    });

    window.addEventListener('popstate', function(){ activateFromURL(0); });
    window.addEventListener('appmap:ready', function(){ activateFromURL(0); });
  });



  

  // 대피소 메뉴 지원 (map에 있을 때는 리로드 없이)
var sh = document.getElementById('nav-shelter');
if (!sh) {
  var ctx = document.body && document.body.getAttribute('data-context-path') || '';
  var links = document.querySelectorAll('a.nav-link, a');
  for (var i=0;i<links.length;i++){
    var t = (links[i].textContent||links[i].innerText||'').replace(/\s+/g,'');
    var href = links[i].getAttribute('href') || '';
    if (t.indexOf('대피소')>-1 || href.endsWith('/shelter') || href.indexOf('layer=shelter')>-1) { sh = links[i]; break; }
  }
}
if (sh) {
  sh.addEventListener('click', function(e){
    var ctx = document.body && document.body.getAttribute('data-context-path') || '';
    var mapPath = ctx + '/map';
    if (location.pathname === mapPath) {
      e.preventDefault();
      // BBox를 URL에 쓰지 않는다면 layer,q 만
      var p = new URLSearchParams(location.search);
      p.set('layer','shelter');
      history.replaceState({qs:p.toString()},'', location.pathname + '?' + p.toString());
      if (window.AppMap && AppMap.activate) AppMap.activate('shelter', {});
    }
  });
}

})();
