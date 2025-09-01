<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<style>
/* 대피소 컨트롤 박스 기본 위치: 지도 오른쪽 위 */
.toolbox-shelter {
  position: absolute;
  top: 80px;       /* 헤더 높이 고려 */
  right: 20px;
  background: #fff;
  padding: 8px;
  border-radius: 8px;
  box-shadow: 0 2px 6px rgba(0,0,0,.2);
  z-index: 1000;   /* 지도보다 위 */
}
.toolbox-shelter .row {
  display: flex;
  gap: 6px;
}
.toolbox-shelter input {
  flex: 1;
  padding: 4px 6px;
}
</style>
</head>
<body>
<%-- shelter-controls.jsp: 대피소 UI 컨트롤 (지도 위에 오버레이) --%>
<div class="toolbox toolbox-shelter">
  <div class="row">
    <%-- 검색창 (시설명/주소) --%>
    <input id="shelterSearch" type="text" placeholder="대피소명/주소 검색(q)" />

    <%-- 대피소 레이어 토글 버튼 --%>
    <button id="btnShelter" type="button">대피소</button>
  </div>
</div>

<script>
  // 버튼 클릭 → 대피소 레이어 활성화
  document.addEventListener('DOMContentLoaded', () => {
    const btn = document.getElementById('btnShelter');
    if (btn) {
      btn.addEventListener('click', () => AppMap.activate('shelter'));
    }
  });
</script>
</body>
</html>