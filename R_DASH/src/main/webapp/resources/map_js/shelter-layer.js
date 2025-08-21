(function(global){
  const BASE = (document.body && document.body.getAttribute('data-context-path')) || '';

  const API = {
    bbox:      BASE + '/shelters/bbox',
    one:       (id)=> BASE + `/shelters/${id}`,
    sugAddr:   BASE + '/shelters/suggest/adress',
    sugName:   BASE + '/shelters/suggest/name'
  };

  const state = {
    active: false,
    map: null,
    markers: [],
    clusterer: null,
    lastReq: 0,
    inflight: null,
    currentQ: '',
    limit: 500
  };

  function debounce(fn, ms){
    let t; return (...args)=>{ clearTimeout(t); t=setTimeout(()=>fn(...args), ms); };
  }

  function getBBox(map){
    const b = map && map.getBounds && map.getBounds();
    if (!b) return null;
    const sw = b.getSouthWest(), ne = b.getNorthEast();
    return { minLat: sw.getLat(), minLon: sw.getLng(), maxLat: ne.getLat(), maxLon: ne.getLng() };
  }

  function clearMarkers(){
    if (state.clusterer){
      try { typeof state.clusterer.clear === 'function' && state.clusterer.clear(); }
      catch(e){
        try {
          const ms = (typeof state.clusterer.getMarkers === 'function') ? state.clusterer.getMarkers() : state.markers;
          typeof state.clusterer.removeMarkers === 'function' && state.clusterer.removeMarkers(ms || []);
        } catch(_) {}
      }
    }
    for (const m of state.markers){ try { m.setMap(null); } catch(_) {} }
    state.markers.length = 0;
  }

  function escapeHtml(s){
    return (s||'').replace(/[&<>"']/g, m=>({ '&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;' }[m]));
  }

  function buildInfoHTML(item){
    const name = escapeHtml(item?.reareNm || '대피소');
    const addr = escapeHtml(item?.adress || '-');
    const capN = (typeof item?.capacity === 'number' && isFinite(item.capacity)) ? item.capacity : null;
    const cap = capN!=null ? `<div><b>수용인원</b> ${capN.toLocaleString()}명</div>` : '';
    const tel = item?.tel ? `<div><b>연락처</b> ${escapeHtml(item.tel)}</div>` : '';
    const href = API.one(item?.shelterNo);
    return `<div style="min-width:220px"><div style="font-weight:700;margin-bottom:4px">${name}</div><div>${addr}</div>${cap}${tel}<div style="margin-top:6px"><a href="${href}" target="_blank" rel="noopener">상세</a></div></div>`;
  }

  const requestBBox = debounce(async function(){
    try {
      if (!state.active) return;
      if (!state.map) return;
      if (typeof kakao === 'undefined' || !kakao.maps) return;

      const bbox = getBBox(state.map);
      if (!bbox || !isFinite(bbox.minLat)) return;

      const params = new URLSearchParams({
        minLat: bbox.minLat, maxLat: bbox.maxLat,
        minLon: bbox.minLon, maxLon: bbox.maxLon,
        limit: state.limit
      });
      if (state.currentQ) params.set('q', state.currentQ);

      const url = `${API.bbox}?${params.toString()}`;
      const myReqId = ++state.lastReq;

      try { state.inflight?.abort?.(); } catch(_) {}
      const ctrl = new AbortController();
      state.inflight = ctrl;

      const res = await fetch(url, { signal: ctrl.signal });
      if (!res.ok) throw new Error('HTTP ' + res.status);

      const data = await res.json();
      if (myReqId !== state.lastReq) return;

      renderMarkers(Array.isArray(data) ? data : []);
    } catch (e) {
      if (e.name === 'AbortError') return;
      console.error('[ShelterLayer] fetch/render error', e);
    }
  }, 250);

  function renderMarkers(list){
    try {
      clearMarkers();
      if (!Array.isArray(list) || list.length === 0) return;
      if (typeof kakao === 'undefined' || !kakao.maps) return;

      const overlays = [];
      for (const item of list){
        if (!item) continue;
        const lat = Number(item.lat), lon = Number(item.lon);
        if (!Number.isFinite(lat) || !Number.isFinite(lon)) continue;

        const pos = new kakao.maps.LatLng(lat, lon);
        const marker = new kakao.maps.Marker({ position: pos, title: item.reareNm || '대피소' });

        try {
          const c = item.capacity;
          const sizeVal = (typeof c === 'number' && isFinite(c)) ? (c>=1000?36:(c>=300?28:22)) : 26;
          const msize = new kakao.maps.Size(sizeVal, sizeVal);
          const mimg  = new kakao.maps.MarkerImage('https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png', msize);
          marker.setImage(mimg);
        } catch(_) {}

        try {
          const iw = new kakao.maps.InfoWindow({ content: buildInfoHTML(item) });
          kakao.maps.event.addListener(marker, 'click', () => iw.open(state.map, marker));
        } catch(_) {}

        state.markers.push(marker);
        overlays.push(marker);
      }

      try {
        if (!state.clusterer) {
          if (kakao.maps.MarkerClusterer) {
            state.clusterer = new kakao.maps.MarkerClusterer({
              map: state.map,
              averageCenter: true,
              minLevel: 6,
              disableClickZoom: false
            });
          }
        }
        if (state.clusterer) {
          if (overlays.length > 0) state.clusterer.addMarkers(overlays);
        } else {
          for (const m of state.markers) m.setMap(state.map);
        }
      } catch (e) {
        for (const m of state.markers) { try { m.setMap(state.map); } catch(_) {} }
      }
    } catch (e) {
      console.error('[ShelterLayer] renderMarkers fatal', e);
    }
  }

  const ShelterLayer = {
    key: 'shelter',
    activate(opts){
      if (state.active) return;
      state.active = true;

      state.map = (global.AppMap && AppMap.map) ? AppMap.map : global.map;
      if (!state.map) { state.active = false; return; }
      if (typeof kakao === 'undefined' || !kakao.maps || !kakao.maps.event) { state.active = false; state.map = null; return; }

      if (opts && typeof opts.q === 'string') state.currentQ = opts.q;

      kakao.maps.event.addListener(state.map, 'idle', requestBBox);
      requestBBox();

      const $q = document.getElementById('shelterSearch');
      if ($q && !$q.dataset.bound){
        $q.dataset.bound = '1';
        $q.addEventListener('input', debounce(()=>{
          state.currentQ = $q.value.trim();
          requestBBox();
        }, 200));
      }
    },
    deactivate(){
      if (!state.active) return;
      state.active = false;

      try { if (state.map && kakao && kakao.maps && kakao.maps.event) kakao.maps.event.removeListener(state.map, 'idle', requestBBox); } catch(_) {}
      try { state.inflight?.abort?.(); } catch(_) {}
      clearMarkers();
      state.clusterer = null;
    },
    refresh(){ requestBBox(); },
    setQuery(q){
      state.currentQ = (q||'').trim();
      requestBBox();
    }
  };

  global.ShelterLayer = ShelterLayer;
})(window);
