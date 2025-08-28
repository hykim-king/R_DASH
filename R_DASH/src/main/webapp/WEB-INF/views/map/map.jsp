<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width,initial-scale=1" />
<title>지도</title>

<style>
#map {
  position: fixed;
  top: 100px;    /* 헤더 높이에 맞게 조정 */
  left: 0;
  right: 0;
  bottom: 0;     /* 꼭 추가 */
  width: 100%;
  height: calc(100vh - 100px); /* 화면 전체 높이에서 헤더만 뺌 */
  z-index: 5;
}
html, body { height: 100%; }

/* 로딩 / 오버레이 */
.loading, .overlay {
  position: fixed;
  inset: 0;
  z-index: 99999;
  transition: opacity .9s ease;
}
.overlay { background: rgba(0, 0, 0, .5); }
.loading {
  display: flex; justify-content: center; align-items: center;
  font-size: 20px; color: #fff;
}
</style>

<!-- 레이어별 스타일 -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/map_css/shelter-layer.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/map_css/firestation-layer.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/map_css/nowcast-layer.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/map_css/landslide-layer.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/map_css/sinkhole-layer.css"/>
</head>

<body data-context-path="${pageContext.request.contextPath}"
      data-layer="${empty param.layer ? 'landslide' : param.layer}"
      data-nowcast-geo="${pageContext.request.contextPath}/resources/geo/sgg.geojson,
                       ${pageContext.request.contextPath}/resources/geo/TL_SCCO_SIG.json">


  <!-- ✅ 로딩/오버레이 -->
  <div class="overlay"></div>
  <div class="loading">Loading…</div> <!-- ← fade-out에서 .loading을 참조하므로 추가 -->
  <div id="map"></div>

  <!-- ✅ jQuery 먼저 -->
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

  <!-- Kakao SDK: services 포함, autoload=false -->
  <script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=b442e080c0a64cb3d347d6158376d1da&libraries=clusterer,services&autoload=false"></script>

  <!-- 히트맵 -->
  <script src="${pageContext.request.contextPath}/resources/lib/heatmap.min.js"></script>
  
  <!-- 황사 simpleheat.js -->
  <script src="https://unpkg.com/simpleheat@0.4.0/simpleheat.js"></script>

  <!-- 지도 코어 -->
  <script src="${pageContext.request.contextPath}/resources/map_js/app-map.js"></script>

  <!-- 레이어들 -->
<script src="${pageContext.request.contextPath}/resources/map_js/dust-layer.js"></script>
<script src="${pageContext.request.contextPath}/resources/map_js/shelter-layer.js"></script>
<script src="${pageContext.request.contextPath}/resources/map_js/firestation-layer.js"></script>
<script src="${pageContext.request.contextPath}/resources/map_js/nowcast-layer.js"></script>
<script src="${pageContext.request.contextPath}/resources/map_js/landslide-layer.js"></script>
<script src="${pageContext.request.contextPath}/resources/map_js/sinkhole-layer.js"></script>
  

  <!-- GeoJSON 로드 및 부팅 -->
  <script>
    // 🔸 특정 레이어가 등록될 때까지 대기 후 boot 실행
    function waitLayerThenBoot(name, cb) {
      const deadline = Date.now() + 3000; // 최대 3초
      (function tick() {
        if (window.AppMap && AppMap.layers && AppMap.layers[name]) { cb(); return; }
        if (Date.now() > deadline) { console.warn('[map.jsp] timed out waiting layer:', name, '; continue anyway'); cb(); return; }
        setTimeout(tick, 50);
      })();
    }

    // 🔸 URL 파라미터 기반 자동 활성화
    function boot() {
      const q = new URLSearchParams(location.search);
      const layer = (q.get('layer') || '').toLowerCase();

      if (layer === 'dust') {
        AppMap.activate('dust', { airType: q.get('airType') || 'ALL' });

      } else if (layer === 'shelter') {
        AppMap.activate('shelter');

      } else if (['fire','firestation','firestations'].includes(layer)) {
        AppMap.activate('firestation');

      } else if (layer === 'nowcast') {
        AppMap.activate('nowcast', {
          category: (q.get('category') || 'T1H').toUpperCase(),
          view: (q.get('view') || 'nation').toLowerCase(),
          sidoNm: q.get('sidoNm') || null,
          signguNm: q.get('signguNm') || null
        });

      } else if (layer === 'landslide') {
        // nowcast 잔상 제거
        if (window.AppMap && AppMap.layers && AppMap.layers.nowcast && AppMap.layers.nowcast.deactivate) {
          AppMap.layers.nowcast.deactivate();
        }
        const hud = document.getElementById('nowcastHud');
        if (hud) hud.remove();

        AppMap.activate('landslide', {
          sido: q.get('sido') || null,
          sgg: q.get('sgg') || null,
          stts: q.get('stts') || null,
          apntNm: q.get('apntNm') || null,
          cluster: (q.get('cluster') || 'on').toLowerCase() !== 'off'
        });
      } else if (layer === 'sinkhole') {
    	         // 싱크홀 버블 레이어
    	         AppMap.activate('sinkhole', {
    	           year: q.get('year') || null,
    	           sido: q.get('sido') || null
    	         });
      }
    }

    // 🔸 AppMap 준비되면 자동 부팅
    window.addEventListener('appmap:ready', function () {
      const q = new URLSearchParams(location.search);
      const layer = (q.get('layer') || '').toLowerCase();

      // ✅ nowcast 페이지 표시 플래그 (스타일 등에서 활용 가능)
      if (layer === 'nowcast') {
        document.body.setAttribute('data-nowcast', 'true');
        /* ❌ 기존 코드에 있던 NowcastLayer.loadGeoJSON 호출 삭제
           nowcast-layer.js가 body의 data-nowcast-geo 속성을 읽어 스스로 로드함 */
      }

      if (layer) waitLayerThenBoot(layer, boot);
      else boot();
    }, { once: true });
  </script>

  <!-- ✅ 로딩 fade-out -->
  <script>
    document.addEventListener('DOMContentLoaded', function () {
      requestAnimationFrame(function () {
        $('.loading, .overlay').css('opacity', 0);
        setTimeout(function () {
          $('.loading, .overlay').hide();
        }, 400); // 전환시간과 맞춤 (4초가 아니라 0.4초 권장)
      });
    });
  </script>
</body>
</html>