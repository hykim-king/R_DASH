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
<title>공지사항 게시판</title>
<link rel="icon" href="${CP}/resources/image/Jaemini_face.ico" type="image/x-icon"/>
<style type="text/css">
span {
    margin-right: 20px;
}
</style>
</head>
<body>
<div>
<div>
    <span>🏠 홈</span><span>></span><span>공지사항</span>
</div>
<div>
    <img style="width:200px; height:150px; object-fit: contain;" src="/ehr/resources/image/board_Jeamin.png">
</div>
<!--검색 영역-->
<form action="#" method="get" enctype="application/x-www-form-urlencoded">
    <input type="hidden" name="pageNo" id="pageNo">
    <!--search-group  -->
    <div>
        <label for="searchDiv"></label>
        <select name="searchDiv" id="searchDiv">
            <option value="">전체</option>
            <option value="10" <c:if test="${search.searchDiv == 10 }">selected</c:if>>제목</option> 
           <option value="20" <c:if test="${search.searchDiv == 20 }">selected</c:if>>내용</option> 
           <option value="30" <c:if test="${search.searchDiv == 30 }">selected</c:if>>번호</option>  
           <option value="40" <c:if test="${search.searchDiv == 40 }">selected</c:if>>제목+내용</option>    
        </select>
        <div>
	        <input type="search" name="searchWord" id="searchWord"  size="15" value="${search.searchWord }">
	        <input type="button" value="🔍 "id="doRetrieve">
        </div>
        <div>
            <input type="button" value="등록 " id="moveToReg">
        </div>
    </div>
</form>
<!-- table 영역 -->
<table border="1">
    <thead>
        <th>번호</th>
        <th>제목</th>
        <th>조회수</th>
        <th>작성일</th>
    </thead>
    <tbody>
          <c:choose>
            <c:when test="${list.size() > 0 }"> <!-- if -->
                <c:forEach var="vo" items="${list }"> <!-- 향상된 for -->
                  <tr>
                    <td ><c:out value="${vo.no}"/></td>
                    <td ><c:out value="${vo.title }"/></td>
                    <td ><c:out value="${vo.viewCnt }"/></td>
                    <td ><c:out value="${vo.modDt }"/></td>
                    <td style="display: none;"><c:out value="${vo.boardNo }"/></td>
                  </tr> 
                </c:forEach>
            </c:when>
            <c:otherwise>    <!-- else -->
                <tr>
                   <td colspan="99"  class="table-cell text-center">조회된 데이터가 없습니다.</td> 
                </tr>
            </c:otherwise>
          </c:choose>
</table>
<!--// table 영역 -->


</div>

</body>
</html>