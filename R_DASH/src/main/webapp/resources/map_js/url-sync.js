// map_js/url-sync.js
// 주소창 쿼리(airType, bbox)를 변경/적용해도 페이지 이동 없이 상태만 동기화

(function () {
  'use strict';

  function getQS(){ return new URLSearchParams(window.location.search); }
  function setQS(obj){
    var qs = getQS();
    Object.keys(obj).forEach(function(k){
      var v = obj[k];
      if (v === null || v === undefined || v === '') qs.delete(k);
      else qs.set(k, v);
    });
    var newUrl = window.location.pathname + '?' + qs.toString();
    history.replaceState({ qs: qs.toString() }, '', newUrl);
  }
  function putBBoxInURL(){
    if (!window.AppMap || !window.AppMap.getBBox) return;
    var b = window.AppMap.getBBox();
    setQS({
      minLat: b.minLat.toFixed(4),
      maxLat: b.maxLat.toFixed(4),
      minLon: b.minLon.toFixed(4),
      maxLon: b.maxLon.toFixed(4)
    });
  }
  function highlight(air){
    var wrap = document.getElementById('dustFilter'); if (!wrap) return;
    var btns = wrap.querySelectorAll('.flt');
    for (var i=0;i<btns.length;i++){
      btns[i].classList.toggle('active', btns[i].getAttribute('data-air') === air);
      // 간단한 active 스타일
      btns[i].style.borderColor = btns[i].classList.contains('active') ? '#111' : '#eee';
      btns[i].style.boxShadow  = btns[i].classList.contains('active') ? 'inset 0 0 0 1px #111' : 'none';
    }
  }
  function applyFromURL(){
    var qs = getQS();
    var air = qs.get('airType') || 'ALL';
    if (window.AppMap && window.AppMap.showOnly){
      window.AppMap.showOnly('dust', { airType: air });
    }
    highlight(air);
  }

  document.addEventListener('DOMContentLoaded', function () {
  activateFromURL(0);

  // 여기 블록을 "교체"합니다
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
});

})();
