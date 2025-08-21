<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8" />
<title>지도</title>
<style>
/* 헤더/푸터 높이에 맞춰 지도 영역 고정 */
#map {
	position: fixed;
	top: 56px; /* header 높이 */
	bottom: 40px; /* footer 높이 */
	left: 0;
	right: 0;
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
	<script
		src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=b442e080c0a64cb3d347d6158376d1da&libraries=clusterer&autoload=false"></script>
	<script
		src="${pageContext.request.contextPath}/resources/map_js/app-map.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/map_js/dust-layer.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/map_js/shelter-layer.js"></script>
	<script
		src="${pageContext.request.contextPath}/resources/map_js/url-sync.js"></script>

	<script>
  kakao.maps.load(function(){
    if (window.AppMap?.registerLayer) {
      window.DustLayer    && AppMap.registerLayer('dust', DustLayer);
      window.ShelterLayer && AppMap.registerLayer('shelter', ShelterLayer);
    }
    window.dispatchEvent(new Event('appmap:ready')); // <-- 추가
  });
</script>

</body>
</html>
