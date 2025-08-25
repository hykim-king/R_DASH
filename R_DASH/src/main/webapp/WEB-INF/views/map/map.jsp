<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width,initial-scale=1" />
  <title>지도</title>

  <style>
    #map{
      position: fixed;
      top: 56px;
      bottom: 40px;
      left: 0; right: 0;
      width: 100%;
      height: calc(100vh - 56px - 40px);
      z-index: 5;
    }
    
    html, body { height: 100%; }
    
    /* 로딩 / 오버레이 스타일 */
    .loading, .overlay {
      position: fixed;
      inset: 0;
      z-index: 99999;
      transition: opacity 4s ease; /* 부드럽게 사라짐 */
    }
    .overlay {
      background: rgba(0, 0, 0, 0.5);
    }
    .loading {
      display: flex;
      justify-content: center;
      align-items: center;
      font-size: 20px;
      color: #fff;
    }
  </style>

  <!-- 레이어 별 스타일 -->
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/map_css/shelter-layer.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/map_css/firestation-layer.css">
  
</head>

<body data-context-path="${pageContext.request.contextPath}">
   <!-- ✅ 로딩/오버레이 요소 -->
  <div class="overlay"></div>
  <div id="map"></div>

  <!-- Kakao SDK (autoload=false면 app-map.js에서 kakao.maps.load로 초기화) -->
  <script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=b442e080c0a64cb3d347d6158376d1da&libraries=clusterer&autoload=false"></script>

  <!-- 히트맵 라이브러리: dust-layer가 쓰므로 먼저 로드 -->
  <script src="https://unpkg.com/simpleheat@0.4.0/simpleheat.js"></script>

  <!-- 지도 코어 -->
  <script src="${pageContext.request.contextPath}/resources/map_js/app-map.js"></script>

  <!-- 레이어들 -->
  <script src="${pageContext.request.contextPath}/resources/map_js/dust-layer.js"></script>
  <script src="${pageContext.request.contextPath}/resources/map_js/shelter-layer.js"></script>
  <script src="${pageContext.request.contextPath}/resources/map_js/firestation-layer.js"></script>



  <!-- 초기 진입 시 URL/경로 읽어서 레이어 자동 활성화 -->
<script>
(function waitApp(){
  if (window.AppMap && window.AppMap.map) boot();
  else window.addEventListener('appmap:ready', boot, { once:true });
})();
function boot(){
  const q = new URLSearchParams(location.search);
  const layer = (q.get('layer') || '').toLowerCase();

  if (layer === 'dust') {
    AppMap.activate('dust', { airType: q.get('airType') || 'ALL' });
  } else if (layer === 'shelter') {
    AppMap.activate('shelter');     // 등록이 늦어도 A안에서 자동 켜짐
  } else if (layer === 'fire' || layer === 'firestation' || layer === 'firestations') {
    AppMap.activate('firestation');
  }
}

</script>

  <!-- ✅ 로딩 fade-out 스크립트 -->
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <script>
  document.addEventListener('DOMContentLoaded', function() {
    // 브라우저 렌더 완료 후 서서히 숨기기
    requestAnimationFrame(function() {
      $('.loading, .overlay').css('opacity', 0);
      setTimeout(function() {
        $('.loading, .overlay').hide();
      }, 4000); // CSS transition 시간과 동일
    });
  });
  </script>

</body>
</html>
