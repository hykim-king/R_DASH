<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width,initial-scale=1" />
  <title>지도</title>
  <style>
    /* 헤더/푸터 높이에 맞춰 지도 영역 고정 */
    #map {
      position: fixed;
      top: 56px;      /* header 높이 */
      bottom: 40px;   /* footer 높이 */
      left: 0;
      right: 0;
      width: 100%;
      height: calc(100vh - 56px - 40px);
      z-index: 5;
    }
    html, body { height: 100%; }
  </style>
</head>
<body data-context-path="${pageContext.request.contextPath}">

  <!-- 지도 컨테이너 (데이터는 사이드바에서 활성화될 때만 표시) -->
  <div id="map"></div>

  <!-- Kakao SDK (autoload=false) -->
  <script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=b442e080c0a64cb3d347d6158376d1da&libraries=clusterer&autoload=false"></script>

  <!-- 지도 코어 -->
  <script src="${pageContext.request.contextPath}/resources/map_js/app-map.js"></script>

  <!-- (황사 히트맵용) simpleheat -->
  <script src="https://unpkg.com/simpleheat@0.4.0/simpleheat.js"></script>

  <!-- 레이어들: 필요한 것만 로드해두고, 실제 활성화는 사이드바/URL로 제어 -->
  <script src="${pageContext.request.contextPath}/resources/map_js/dust-layer.js"></script>
  <script src="${pageContext.request.contextPath}/resources/map_js/shelter-layer.js"></script>

  <!-- URL ↔ 상태 동기화 (사이드바에서 /map?layer=... 로 진입 시만 활성화) -->
  <script src="${pageContext.request.contextPath}/resources/map_js/url-sync.js"></script>

  <script>
    kakao.maps.load(function () {
      if (window.AppMap && typeof AppMap.registerLayer === 'function') {
        if (window.DustLayer)    AppMap.registerLayer('dust',    window.DustLayer);
        if (window.ShelterLayer) AppMap.registerLayer('shelter', window.ShelterLayer);
      }
      // 준비 완료 신호 (url-sync.js가 듣고 있으면 즉시 동작)
      window.dispatchEvent(new Event('appmap:ready'));
      // 주의: 여기서는 어떤 레이어도 자동 활성화하지 않음
    });
  </script>
</body>
</html>
