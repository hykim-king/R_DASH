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
<link href="/ehr/resources/template/dashboard/css/dashboard.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/nucleo/css/nucleo.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/nucleo/css/nucleo-svg.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/@fortawesome/fontawesome-free/css/all.min.css" rel="stylesheet">
<title>${vo.title}</title>

</head>
<body>
<div class="main-content">
	<div class="header bg-warning pb-6 header bg-gradient-warning py-7 py-lg-8 pt-lg-9">
		<span class="mask bg-gradient-default opacity-8"></span>
		<div class="container-fluid d-flex align-items-center">
		  <div class="row">
            <div class="col-lg-7 col-md-10">
                <div>
                    <span>🏠   홈</span><span> > </span><span>공지사항</span><span> > </span><span>상세보기</span>
                </div>
                <h1 class="display-2 text-white">${vo.title}</h1>
                <p class="text-white mt-0 mb-5">이번 공지에서는 시민 여러분께 중요한 정보를 전해드립니다.                         
                                                                                                            재난 안전 수칙이나 주요 소식 등 꼭 알아야 할 사항을 놓치지 말고 확인해 주세요.<br></p>
            <!--    <input type="button" id="moveTolist" class="btn btn-neutral" value="목록으로 "> -->
            </div>
        </div>
		</div>
	</div><!-- //header -->
	<!-- Page Contents -->
	<div class="container-fluid mt--6" style="min-height: 700px; max-width:1700px; margin:0 auto;">
	    <div class="row">
	    <div class="col-xl-8 offset-xl-2 order-xl-1" >
            <div class="card">
             <div class="card-header">
                <div class="row align-items-center border-0 d-flex align-items-center">
                   <div class="col-8 d-flex align-items-center">
                      <h3 class="mb-0">${vo.title}</h3>
                   </div>
                   <div class="col-4 text-right">
                     <input type="button" id="doUpdate" class="btn btn-sm btn-primary" value="수정">
                     <input type="button" id="doDelete" class="btn btn-sm btn-primary" value="삭제">
                     <input type="button" id="moveToList" class="btn btn-sm btn-primary" value="목록으로">
                   </div>
                   <span>등록자: ${vo.modId}</span><span>조회 ${vo.viewCnt}</span><span>${vo.modDt}</span>
                </div><!-- row -->
             </div> <!-- card header -->
             <div class="card-body d-flex justify-content-center align-items-center" style="min-height: 300px;"">
              <div class="pl-lg-4 w-75">
                <div class="row">
                    <div>
				      <p>${vo.contents}</p>
				   </div>
                </div>
              </div>
             </div>
            </div><!-- card -->
        </div>
	    
	    </div>
    </div>
	
	
</div> <!--//main-content  -->






<%-- <!-- 목록 버튼 -->
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
 --%>

</body>
</html>