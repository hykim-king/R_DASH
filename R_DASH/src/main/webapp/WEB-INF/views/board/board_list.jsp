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
<link href="/ehr/resources/template/dashboard/assets/vendor/nucleo/css/nucleo.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/nucleo/css/nucleo-svg.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/@fortawesome/fontawesome-free/css/all.min.css" rel="stylesheet">

<title>공지사항 게시판</title>
<link rel="icon" href="${CP}/resources/image/Jaemini_face.ico" type="image/x-icon"/>
<style type="text/css">
span {
    margin-right: 20px;
}
.table.align-items-center.table-flush {
    width: 100%;
    table-layout: fixed;
    border-collapse: collapse;
    text-align: center; /* 기본은 가운데 */
}

.table.align-items-center.table-flush th,
.table.align-items-center.table-flush td {
    vertical-align: middle;
}

<!-- 두 번째 열만 왼쪽 정렬 -->
.table.align-items-center.table-flush th:nth-child(2),
.table.align-items-center.table-flush td:nth-child(2) {
    text-align: left;
}
</style>
</head>
<body>

<div class="main=content">
	
	<!-- header2 -->
	<div class="header bg-warning pb-6 header bg-gradient-warning py-7 py-lg-8 pt-lg-9">
      <!-- Header container -->
      <div class="container-fluid d-flex align-items-center">
        <div class="row">
        
          <div style="margin-left:100px;">
           <div>
                <span>🏠   홈</span><span>></span><span>재난 공지사항</span>
           </div>
            <h1 class="display-2 text-white">📢 재난 공지사항 안내</h1>
            <p class="text-white mt-0 mb-5">이 페이지에서는 최신 재난 소식과 안전 관련 안내를 확인하실 수 있습니다. <br>
                                                                                                    태풍, 폭우, 화재 등 각종 재난 상황에 대한 정보를 신속하게 제공하여 <br>
                                                                                                    시민 여러분의 안전한 생활을 돕습니다.  </p>
          </div>
        </div>
      </div>
    </div>
	
	<!-- //header2 -->
	<!-- Page content -->
	<div class="container-fluid mt--6" style="min-height: 700px; max-width:1700px; margin:0 auto;">
		<div class="row">
		<div class="col">
  		    <div class="card"> 
		        <div class="card-header border-0 d-flex align-items-center">
		            <h3 class="mb-0">공지사항</h3>
		            <!-- 검색란 -->
		            <div class="ml-auto d-flex flex-row align-items-center" ><!-- 오른쪽 끝으로 밀기 -->
		            <select name="searchDiv" class="btn btn-secondary dropdown-toggle">    
		                <option class="dropdown-item" >전체</option>
                        <option class="dropdown-item" value="10" <c:if test="${search.searchDiv == 10 }">selected</c:if>>제목</option> 
                        <option class="dropdown-item" value="20" <c:if test="${search.searchDiv == 20 }">selected</c:if>>내용</option> 
                        <option class="dropdown-item" value="30" <c:if test="${search.searchDiv == 30 }">selected</c:if>>번호</option>  
                        <option class="dropdown-item" value="40" <c:if test="${search.searchDiv == 40 }">selected</c:if>>제목+내용</option>    
		              
		            </select>		   
			            <div class="form-group mb-0 d-flex flex-row align-items-center ">       
				            <div class="input-group input-group-alternative input-group-merge me-2">
					            <div class="input-group-prepend">
					               <span class="input-group-text"><i class="fas fa-search"></i></span>
					            </div>
					            <input class="form-control" placeholder="Search" type="text">
				            </div>
				            <button type="button" class="ml-md-n-2 btn btn-sm btn-default" style="width: 70px; height:40px;">검색</button>
				            <button type="button" id="moveToReg" class="btn btn-sm btn-default wide-btn" style="width: 70px; height:40px;">등록</button>
				        </div>
		            </div>   
		            <!-- //검색란 -->
		        </div>
    <!-- //Page content -->
		<!-- Light table -->
		<div class="table-responsive">
			<table class="table align-items-center table-flush">
			 <colgroup>
		        <col style="width: 10%;"> <!-- 2 -->
		        <col style="width: 50%;" class="left-col"> <!-- 6 -->
		        <col style="width: 15%;"> <!-- 2 -->
		        <col style="width: 35%;"> <!-- 2 -->
		    </colgroup>
			    <thead class="thead-light">
			       <tr>
				        <th scope="col" >번호</th>
				        <th scope="col" >제목</th>
				        <th scope="col" >조회수</th>
				        <th scope="col" >작성일</th>
			        </tr>
			    </thead>
			     <tbody class="list">
			          <c:choose>
			            <c:when test="${list.size() > 0 }"> <!-- if -->
			                <c:forEach var="vo" items="${list }"> <!-- 향상된 for -->
			                  <tr>
			                    <td ><c:out value="${vo.no}"/></td>
			                    <td >
				                    <div class="media-body">
					                    <span class="name mb-0 text-sm">
					                       <c:out value="${vo.title }"/>
					                    </span>
				                    </div>
			                    </td>
		
			                    <td ><c:out value="${vo.viewCnt }"/></td>
			                    <td ><c:out value="${vo.modDt }"/></td>
			                    <td style="display: none;"><c:out value="${vo.boardNo }"/></td>
			                  </tr> 
			                </c:forEach>
			            </c:when>
			            <c:otherwise>    <!-- else -->
			                <tr>
			                   <td colspan="99">등록된 게시글이 없습니다.</td> 
			                </tr>
			            </c:otherwise>
			          </c:choose>
			     </tbody>
				</table>
			</div>
			<!-- //Light table -->
			<!-- 카드 푸터 -->
			<div class="card-footer py-4">
			     <nav aria-label="...">
			         <ul class="pagination justify-content-end mb-0">
			             <li class="page-item disabled"> 
			                 <a class="page-link" href="#" tabindex="-1"><i class="fas fa-angle-left"></i> 
			                 <span class="sr-only">Previous</span></a>
			             </li>
			             <li class="page-item active"><a class="page-link" href="#">1</a></li>
			             <li class="page-item"><a class="page-link" href="#">2 <span class="sr-only">(current)</span></a></li>
			             <li class="page-item"><a class="page-link" href="#">3</a></li>
			             <li class="page-item"><a class="page-link" href="#"><i class="fas fa-angle-right"></i> 
			             <span class="sr-only">Next</span></a></li>
			          </ul>
			       </nav>
			 </div>
			<!-- //카드 푸터 -->
		    </div>
		</div>
	</div>
</div>
<!--// table 영역 -->
</div>

</body>
</html>