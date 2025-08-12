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
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="Start your development with a Dashboard for Bootstrap 4.">
<meta name="author" content="Creative Tim">
<link href="/ehr/resources/template/dashboard/css/dashboard.css" rel="stylesheet" />
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
<!-- header2 -->
<div class="header bg-primary pb-6">
	<div class="container-fluid">
		<div class="header-body">
			<div class="row align-items-center py-4">
				<div class="col-lg-6 col-7">
				    <h6 class="h2 text-white d-inline-block mb-0">Tables</h6>
					<nav aria-label="breadcrumb" class="d-none d-md-inline-block ml-md-4">
					<ol class="breadcrumb breadcrumb-links breadcrumb-dark">
					<li class="breadcrumb-item"><a href="#"><i class="fas fa-home">
					</i></a></li>
					<li class="breadcrumb-item"><a href="#">Tables</a></li>
					<li class="breadcrumb-item active" aria-current="page">Tables</li></ol></nav>
				</div>
				    <div class="col-lg-6 col-5 text-right"> 
					<a href="#" class="btn btn-sm btn-neutral">등록</a>
                </div>
            </div>
        </div>
    </div>
</div>


<!-- //header2 -->
<div class="container-fluid mt--6">
	<div class="row">
	<div class="col">
	    <div class="card">
	        <div class="card-header border-0">
	            <h3 class="mb-0">공지사항</h3>
	        </div>
	<!-- table 영역 -->
	<div class="table-responsive">
		<table class="table align-items-center table-flush">
		    <thead class="thead-light">
		       <tr>
			        <th scope="col" class="sort">번호</th>
			        <th scope="col" class="sort">제목</th>
			        <th scope="col" class="sort">조회수</th>
			        <th scope="col" class="sort">작성일</th>
		        </tr>
		    </thead>
		    <tbody class="list">
		          <c:choose>
		            <c:when test="${list.size() > 0 }"> <!-- if -->
		                <c:forEach var="vo" items="${list }"> <!-- 향상된 for -->
		                  <tr>
		                    <td ><c:out value="${vo.no}"/></td>
		
		                    <td ><div class="media-body">
		                    <span class="name mb-0 text-sm">
		                       <c:out value="${vo.title }"/>
		                    </span></div></td>
	
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
		</div>
		<!-- 카드 푸터 -->
		
		<!-- //카드 푸터 -->
	    </div>
	</div>
	</div>
</div>
<!--// table 영역 -->


</div>

</body>
</html>