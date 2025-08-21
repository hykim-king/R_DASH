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

  document.addEventListener('DOMContentLoaded', function(){
    // 1) URL 상태 적용
    applyFromURL();

    // 2) 사이드바 "황사" 메뉴 → ALL로 진입
    var dustLink = document.getElementById('nav-dust');
    if (!dustLink) {
      var links = document.querySelectorAll('#sidenav-collapse-main a.nav-link');
      for (var i=0;i<links.length;i++){
        var t=(links[i].textContent||links[i].innerText||'').replace(/\s+/g,'');
        if (t.indexOf('황사')>-1){ dustLink=links[i]; break; }
      }
    }
    if (dustLink){
      dustLink.addEventListener('click', function(e){
        e.preventDefault();
        setQS({ airType: 'ALL' });
        putBBoxInURL();
        applyFromURL();
      });
    }

    // 3) 우측 버튼 클릭 시 URL 갱신
    document.addEventListener('click', function(e){
      var btn = e.target.closest('#dustFilter .flt'); if (!btn) return;
      var air = btn.getAttribute('data-air');
      setQS({ airType: air, limit: 500 });
      putBBoxInURL();
      highlight(air);
    });

    // 4) 뒤/앞으로 가기
    window.addEventListener('popstate', applyFromURL);
  });
})();
