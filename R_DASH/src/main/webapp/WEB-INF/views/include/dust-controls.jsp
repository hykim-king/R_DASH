<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- dust-controls.jsp: 먼지/대기 데이터 전용 UI 컨트롤 (지도 위 오버레이) --%>
<%-- 버튼 3개로 필터: 교외대기(빨강), 도로변 대기(파랑), 도시대기(노랑) --%>

<div class="toolbox toolbox-dust">
  <div class="row">
    <%-- 레이어 토글(켜기) --%>
    <button id="btnDustOn"  type="button">대기 레이어 켜기</button>
    <%-- 레이어 토글(끄기) --%>
    <button id="btnDustOff" type="button">끄기</button>
  </div>

  <div class="row" style="margin-top:6px">
    <%-- 세부 타입 필터 버튼들 --%>
    <button class="dust-filter" data-type="교외대기"   title="교외대기(빨강)">교외대기</button>
    <button class="dust-filter" data-type="도로변 대기" title="도로변 대기(파랑)">도로변</button>
    <button class="dust-filter" data-type="도시대기"   title="도시대기(노랑)">도시대기</button>
  </div>
</div>

<script>
  // AppMap/DustLayer 연동 가정:
  // DustLayer.activate({ airType: '교외대기' }) 처럼 opts.airType로 필터를 전달하도록 합의되어 있음.
  document.addEventListener('DOMContentLoaded', () => {
    const on  = document.getElementById('btnDustOn');
    const off = document.getElementById('btnDustOff');
    const typeBtns = document.querySelectorAll('.dust-filter');

    // 레이어 켜기 (기본: 마지막 타입 유지)
    on?.addEventListener('click', () => {
      AppMap.activate('dust');
    });

    // 레이어 끄기
    off?.addEventListener('click', () => {
      if (AppMap && AppMap.deactivate) AppMap.deactivate('dust');
      // 선택 상태 UI 초기화(옵션)
      typeBtns.forEach(b=>b.classList.remove('active'));
    });

    // 타입 선택 → 해당 타입만 표시
    typeBtns.forEach(btn => {
      btn.addEventListener('click', () => {
        const t = btn.dataset.type;
        // 1) 즉시 활성화 + 타입 전달
        AppMap.activate('dust', { airType: t });

        // 2) UI 선택 표시
        typeBtns.forEach(b=>b.classList.remove('active'));
        btn.classList.add('active');
      });
    });
  });
</script>

<style>
/* 먼지 컨트롤 박스: shelter 박스와 안 겹치도록 살짝 아래 */
.toolbox-dust{
  position: absolute;
  top: 160px;     /* shelter-controls.jsp가 top:80px이므로 80px 더 아래 */
  right: 20px;
  background: #fff;
  padding: 8px;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0,0,0,.2);
  z-index: 1000;
  min-width: 220px;
}
.toolbox-dust .row{
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
}
.toolbox-dust button{
  padding: 6px 10px;
  border-radius: 8px;
  border: 1px solid #e3e6eb;
  background: #fff;
  cursor: pointer;
}
.toolbox-dust button:hover{
  background: #f6f8fa;
}
.toolbox-dust .dust-filter.active{
  outline: 2px solid #333;   /* 선택 강조 */
}
</style>
