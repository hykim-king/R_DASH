<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="utf-8" />
  <title>지도</title>
  <style>
    /* 헤더/푸터 높이에 맞춰 지도 영역 고정 */
    #map{
      position: fixed;
      top: 56px;          /* header 높이 */
      bottom: 40px;       /* footer 높이 */
      left: 0; right: 0;
      width: 100%;
      height: calc(100vh - 56px - 40px);
      z-index: 5;
    }
  </style>
</head>
<body data-context-path="${pageContext.request.contextPath}">

  <!-- 지도 컨테이너 -->
  <div id="map"></div>

  <!-- 1) Kakao SDK -->
  <script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=b442e080c0a64cb3d347d6158376d1da&libraries=clusterer&autoload=false"></script>

  <!-- 2) 지도 코어(AppMap) -->
  <script src="${pageContext.request.contextPath}/resources/map_js/app-map.js"></script>

  <!-- 3) 히트맵 라이브러리 -->
  <script src="https://unpkg.com/simpleheat@0.4.0/simpleheat.js"></script>

  <!-- 4) 황사 레이어(히트맵 + HUD 생성 포함) -->
  <script src="${pageContext.request.contextPath}/resources/map_js/dust-layer.js"></script>

  <!-- 5) 주소창 ↔ 버튼 ↔ 레이어 동기화 -->
  <script src="${pageContext.request.contextPath}/resources/map_js/url-sync.js"></script>


<!-- map.jsp 맨 아래에 한 줄 추가 (app-map.js 로딩 뒤면 어디든 ok) -->
<script>
  (function(){
    var base = document.body.getAttribute('data-context-path') || '';
    var want = base + '/map';
    if (location.pathname !== want) {
      // 페이지는 그대로 두고 주소창만 /ehr/map 으로 통일
      history.replaceState(null, '', want + (location.search || ''));
    }
  })();
</script>
</body>
</html>
