<%@page import="com.pcwk.ehr.cmn.PcwkString"%>
<%@page import="com.pcwk.ehr.cmn.SearchDTO"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="CP" value="${pageContext.request.contextPath }" />
<c:set var="now" value="<%=new java.util.Date()%>" />
<c:set var="sysDate"><fmt:formatDate value="${now}" pattern="yyyy-MM-dd_HH:mm:ss" /></c:set>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>${vo.title}</title>
<link rel="icon" href="${CP}/resources/image/Jaemini_face.ico" type="image/x-icon"/>

<style type="text/css">
span {
    margin-right: 20px;
}
</style>
</head>
<body>

<!-- 목록 버튼 -->
<div>
    <input type="button" id="moveToList" value="목록으로">
</div>
<!--//목록 버튼 -->
<!-- 글 상세 -->
<div>
	<div>
	   <p>${vo.title}</p>
	</div>
   <span>${vo.modId }</span><span>조회 ${vo.viewCnt}</span><span>${vo.modDt}</span>
   <hr/>
   <div>
      <p>${vo.contents}</p>
   </div>
</div>
<!-- //글 상세 -->
<!-- 버튼 -->
<div>
    <input type="button" id="doUpdate" value="수정">
    <input type="button" id="doDelete" value="삭제">
</div>
<!--//버튼 -->


</body>
</html>