// /resources/map_js/firestation-layer.js
(function (global) {
  'use strict';

  // AppMap 준비되면 초기화
  if (global && global.AppMap) init();
  else global.addEventListener('appmap:ready', init, { once: true });

  function init() {
    var App = global.AppMap;
    if (!App || !App.map) { setTimeout(init, 80); return; }
    var map = App.map;

    // ===== API (사용하는 것만 남김) =====
    var BASE = (document.body && document.body.getAttribute('data-context-path')) || '';
    var API  = {
      bbox:   BASE + '/firestations/bbox',   // ?minLat=&maxLat=&minLon=&maxLon=&limit=
      search: BASE + '/firestations/search'  // ?q=&limit=&offset=
    };

    // ===== 상태 =====
    var active = false;

    // 마커/말풍선
    var markers = [];             // kakao.maps.Marker[]
    // HUD(검색 UI)
    var hudEl = null, listEl = null, inputEl = null;

    // 요청/이벤트 안정화
    var idleTimer = null;
    var inFlight = false;
    var lastFetchKey = '';
    var lastDrawKey  = '';

    // ===== 레이어 등록 =====
    var firestationAPI = { title: '소방서', activate, deactivate, refresh };
    if (typeof App.registerLayer === 'function') App.registerLayer('firestation', firestationAPI);
    else global.FirestationLayer = firestationAPI; // 폴백

    // ===== 유틸 =====
    function fetchJSON(url){
      return fetch(url, { headers: { 'Accept': 'application/json' } })
        .then(function(res){ if(!res.ok) throw new Error('HTTP '+res.status); return res.json(); });
    }
    function getBBox(){
      var b = map.getBounds(), sw = b.getSouthWest(), ne = b.getNorthEast();
      return { minLat: sw.getLat(), minLon: sw.getLng(), maxLat: ne.getLat(), maxLon: ne.getLng() };
    }
    function escapeHtml(s){
      return String(s||'').replace(/[&<>"']/g, function(m){
        return ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;','\'':'&#39;'}[m]);
      });
    }
    function infoHTML(dto){
      return (
        '<div style="padding:10px;min-width:220px;line-height:1.45;">' +
          '<div style="font-weight:700;margin-bottom:4px;">' + escapeHtml(dto.stationNm || '') + '</div>' +
          (dto.area      ? '<div style="color:#666;font-size:12px;">'+ escapeHtml(dto.area) +'</div>' : '') +
          (dto.fireNead  ? '<div style="color:#666;font-size:12px;">관할: '+ escapeHtml(dto.fireNead) +'</div>' : '') +
          (dto.tel       ? '<div style="margin-top:6px;font-size:12px;">☎ ' + escapeHtml(dto.tel) + '</div>' : '') +
        '</div>'
      );
    }

    // ===== 마커 렌더링 =====
    function clearMarkers(){
      for (var i=0;i<markers.length;i++){
        // 각 마커에 열려있는 말풍선 닫고 제거
        if (markers[i].__info) markers[i].__info.close();
        markers[i].setMap(null);
      }
      markers.length = 0;
    }

    function draw(rows){
      clearMarkers();
      if (!Array.isArray(rows)) rows = [];

      for (var i=0;i<rows.length;i++){
        var d = rows[i];
        var lat = +d.lat, lon = +d.lon;
        if (!isFinite(lat) || !isFinite(lon)) continue;

        // ✅ 카카오 기본 아이콘(이미지 지정 X)
        var marker = new kakao.maps.Marker({
          position: new kakao.maps.LatLng(lat, lon),
          title: d.stationNm || ''
        });
        marker.setMap(map);

        // 마커별 독립 InfoWindow (닫기 X 아이콘 표시, 자동닫힘 없음)
        var info = new kakao.maps.InfoWindow({
          content: infoHTML(d),
          removable: true,
          zIndex: 3
        });

        // 마커에 DTO/InfoWindow 저장
        marker.__dto = d;
        marker.__info = info;

        // 마커 클릭 → 해당 말풍선 토글(여러 개 동시에 열림)
        (function(mk){
          kakao.maps.event.addListener(mk, 'click', function(){
            // 열려있으면 닫고, 닫혀있으면 열기
            // kakao InfoWindow엔 isOpen이 없으므로 open/close 상태를 간단 추적
            if (mk.__opened) {
              mk.__info.close();
              mk.__opened = false;
            } else {
              mk.__info.open(map, mk);
              mk.__opened = true;
              map.panTo(mk.getPosition()); // 이동 보장
            }
          });
        })(marker);

        markers.push(marker);
      }
    }

    // 검색 리스트 클릭 시 가까운 마커의 말풍선 열기
    function findNearestMarker(latlng, pixelRadius){
      var proj = map.getProjection();
      var ptTarget = proj.containerPointFromCoords(latlng);
      var best = null, bestDist = Infinity;

      for (var i=0;i<markers.length;i++){
        var pt = proj.containerPointFromCoords(markers[i].getPosition());
        var dx = pt.x - ptTarget.x, dy = pt.y - ptTarget.y;
        var d = Math.sqrt(dx*dx + dy*dy);
        if (d < bestDist) { bestDist = d; best = markers[i]; }
      }
      return (bestDist <= (pixelRadius||30)) ? best : null;
    }

    // ===== 지도 이벤트 =====
    function onMapIdle(){
      if (!active) return;
      if (idleTimer) clearTimeout(idleTimer);
      idleTimer = setTimeout(requestBBox, 160);
    }
    function onTilesLoadedOnce(){
      kakao.maps.event.removeListener(map, 'tilesloaded', onTilesLoadedOnce);
      requestBBox(); // 초기 로딩 직후 보장 1회
    }

    // ===== 데이터 요청(BBox) =====
    function requestBBox(){
      var b = getBBox(); if (!b) return;
      var key = [b.minLat,b.minLon,b.maxLat,b.maxLon].map(function(v){return v.toFixed(4)}).join(',');

      if (!inFlight && key === lastDrawKey) return;   // 이미 최신
      if (inFlight && key === lastFetchKey) return;   // 동일키 중복 방지

      lastFetchKey = key; inFlight = true;

      var url = API.bbox +
        '?minLat=' + b.minLat + '&maxLat=' + b.maxLat +
        '&minLon=' + b.minLon + '&maxLon=' + b.maxLon +
        '&limit=1000';

      fetchJSON(url)
        .then(function(rows){ draw(rows || []); lastDrawKey = key; })
        .catch(function(e){ console.error('[firestation] bbox error', e); })
        .finally(function(){ inFlight = false; });
    }

    // ===== HUD (검색창 + 결과 리스트) =====
    function ensureHUD(){
      if (hudEl) return;

      hudEl = document.createElement('div');
      hudEl.id = 'fsHud';
      hudEl.className = 'fs-hud';

      var searchWrap = document.createElement('div');
      searchWrap.className = 'fs-search';

      inputEl = document.createElement('input');
      inputEl.type = 'text';
      inputEl.placeholder = '소방서/지역 검색';
      inputEl.className = 'fs-input';

      var clearBtn = document.createElement('button');
      clearBtn.type = 'button';
      clearBtn.className = 'fs-clear';
      clearBtn.textContent = '×';

      searchWrap.appendChild(inputEl);
      searchWrap.appendChild(clearBtn);

      listEl = document.createElement('div');
      listEl.className = 'fs-list';

      hudEl.appendChild(searchWrap);
      hudEl.appendChild(listEl);
      document.body.appendChild(hudEl);

      // 디바운스 검색
      var debTimer = null;
      inputEl.addEventListener('input', function(){
        var q = inputEl.value.trim();
        if (debTimer) clearTimeout(debTimer);
        debTimer = setTimeout(function(){
          if (!q) { listEl.innerHTML = ''; return; }
          var url = API.search + '?q=' + encodeURIComponent(q) + '&limit=50&offset=0';
          fetchJSON(url)
            .then(function(res){
              var rows = Array.isArray(res) ? res : (res && res.rows) ? res.rows : [];
              renderList(rows);
            })
            .catch(function(e){ console.error('[firestation] search error', e); });
        }, 220);
      });
      clearBtn.addEventListener('click', function(){
        inputEl.value = ''; listEl.innerHTML = ''; inputEl.focus();
      });
    }

    function renderList(rows){
      var html = (rows||[]).map(function(r, idx){
        return (
          '<div class="fs-item" data-idx="'+ idx +'">' +
            '<div class="fs-item-title">'+ escapeHtml(r.stationNm || '') +'</div>' +
            '<div class="fs-item-sub">'+ escapeHtml(r.area || '') +'</div>' +
          '</div>'
        );
      }).join('');
      listEl.innerHTML = html || '<div class="fs-empty">검색 결과가 없습니다.</div>';

      var items = listEl.querySelectorAll('.fs-item');
      for (var i=0;i<items.length;i++){
        items[i].addEventListener('click', function(){
          var idx = +this.getAttribute('data-idx');
          var dto = rows[idx]; if (!dto) return;

          var latlng = new kakao.maps.LatLng(+dto.lat, +dto.lon);
          map.panTo(latlng); // ✅ 요구사항 1: 지도 이동 보장

          // 가까운 마커를 찾아 해당 말풍선 열기(여러 개 동시 유지)
          var mk = findNearestMarker(latlng, 30);
          if (mk) {
            // 최신 내용으로 갱신 후 열기 (검색 결과와 지도 데이터가 다를 수 있으니)
            mk.__info.setContent(infoHTML(dto));
            mk.__info.open(map, mk);
            mk.__opened = true;
          }
        });
      }
    }

    // ===== 수명주기 =====
    function activate(){
      if (active) return;
      active = true;
      ensureHUD();
      hudEl.style.display = 'block';

      kakao.maps.event.addListener(map, 'tilesloaded', onTilesLoadedOnce);
      kakao.maps.event.addListener(map, 'idle', onMapIdle);

      inFlight = false; lastFetchKey = ''; lastDrawKey = '';
      requestBBox(); // 강제 1회
    }

    function deactivate(){
      if (!active) return;
      active = false;

      kakao.maps.event.removeListener(map, 'idle', onMapIdle);
      kakao.maps.event.removeListener(map, 'tilesloaded', onTilesLoadedOnce);
      if (idleTimer) { clearTimeout(idleTimer); idleTimer = null; }

      clearMarkers();
      if (hudEl) hudEl.style.display = 'none';
    }

    function refresh(){
      if (!active) return;
      requestBBox();
    }
  }
})(window);
