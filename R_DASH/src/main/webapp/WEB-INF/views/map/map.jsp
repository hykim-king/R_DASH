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
  </style>
  
   <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/map_css/shelter-layer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/map_css/firestation-layer.css">
</head>
<body data-context-path="${pageContext.request.contextPath}">

  <div id="map"></div>

  <!-- Kakao SDK -->
  <script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=b442e080c0a64cb3d347d6158376d1da&libraries=clusterer&autoload=false"></script>

  <!-- 지도 코어 -->
  <script src="${pageContext.request.contextPath}/resources/map_js/app-map.js"></script>

  <!-- 히트맵 라이브러리 -->
  <script src="https://unpkg.com/simpleheat@0.4.0/simpleheat.js"></script>

  <!-- 레이어들 -->
  <script src="${pageContext.request.contextPath}/resources/map_js/dust-layer.js"></script>
  <script src="${pageContext.request.contextPath}/resources/map_js/shelter-layer.js"></script>
  <script src="${pageContext.request.contextPath}/resources/map_js/firestation-layer.js"></script>
  


  <!-- URL 동기화 -->
  <script src="${pageContext.request.contextPath}/resources/map_js/url-sync.js?v=2"></script>

  <script>
    (function(){
      var base = document.body.getAttribute('data-context-path') || '';
      var want = base + '/map';
      if (location.pathname !== want) {
        history.replaceState(null, '', want + (location.search || ''));
      }
    })();
  </script>
</body>
</html>
