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
<script>
document.addEventListener('DOMContentLoaded', function() {
console.log('DOMContentLoaded');
    
    function isEmpty(value) {
        return value == null || value.trim() === '';
    }
    const boardNo = document.querySelector("#boardNo").value;
	   
   $("#moveToUpdate").on('click',function(){
	   if (confirm('수정하시겠습니까?')) {
		    // 확인(Y) 버튼을 누른 경우 실행
		    alert("수정 화면으로 이동합니다.");
		    window.location.href = '/ehr/board/doUpdateView.do?boardNo='+boardNo;
		} else {
		    // 취소(N) 버튼을 누른 경우 실행
		    alert("취소되었습니다.");
		}
   });
   $("#moveToList").on('click',function(){
	   alert("목록으로 이동합니다.");
       window.location.href = '/ehr/board/doRetrieve.do';
   });
   $("#doDelete").on('click',function(){
	   if (confirm('정말로 삭제하시겠습니까?')) {
           
         //ajax 비동기 통신
           $.ajax({
               type : "POST", //GET/POST
               url : "/ehr/board/doDelete.do", //서버측 URL
               asyn : "true", //비동기
               dataType : "html",//서버에서 받을 데이터 타입
               data : { //파라메터
                   "boardNo" : '${vo.boardNo}'
               },
               success : function(response) {//요청 성공
                   console.log("success:" + response)
                   //문자열 : javascript 객체
                   const message = JSON.parse(response);
                   //{"messageId":1,"message":"제목등록되었습니다.","no":0,"totalCnt":0,"pageSize":0,"pageNo":0}
                   if (message.messageId === 1) { //등록 성공
                       alert(message.message);
       
                       //목록 화면으로 이동
                       window.location.href = '/ehr/board/doRetrieve.do';
                   } else {
                       alert(message.message);
                   }
               },
               error : function(response) {//요청 실패
                   console.log("error:" + response)
               }
       
           });
           
       } else {
           // 취소(N) 버튼을 누른 경우 실행
           alert("취소되었습니다.");
       }
   });
   
   
});
   
</script>
</head>
<body>
<div class="main-content">
	<div class="header bg-warning pb-6 header bg-gradient-warning py-4 py-lg-6 pt-lg-6">
		<span class="mask bg-gradient-warning opacity-8"></span>
		  <div class="container-fluid d-flex justify-content-center align-items-center text-center" style="min-height:200px; position:relative; z-index:1;">
		    <h1 class="display-2 text-white text-shadow mb-0">${vo.title}</h1>
		  </div>
	</div><!-- //header -->
	<!-- Page Contents -->
	<div class="container-fluid" style="margin:0 auto;">
	    <div class="row">
	    <div class="col" >
            <div class="card">
             <div class="card-header">
                <div class="row align-items-center border-0 d-flex align-items-center">
                   <input type="hidden" id="boardNo" name="boardNo" value="<c:out value='${vo.boardNo }'/>">
                   <div class="col-8 d-flex align-items-center">
                      <h3 class="mb-0">${vo.title}</h3>
                   </div>
                   <div class="col-4 text-right">
                     <input type="button" id="moveToUpdate" class="btn btn-sm btn-primary" value="수정">
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

</body>
</html>