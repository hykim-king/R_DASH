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
<style>
.boardBtns{
    align-items: flex-end;
    justify-content: flex-end;
    gap:1px;
    }

</style>
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
   //언어 선택
   const langSelect = document.querySelector("#lang");
   const currentLang = "${empty lang ? 'ko' : lang}";
   langSelect.value = currentLang;  // selected 반영
   
   // 언어 변경 이벤트
   langSelect.addEventListener("change", function(){
       selectLang = langSelect.value;


       window.location.href = '/ehr/board/doSelectOne.do?boardNo='+boardNo+'&lang='+selectLang;
   });
});
   
</script>
</head>
<body>
<div class="main-content">
	<div class="header bg-warning pb-6 header bg-gradient-warning py-3 py-lg-5 pt-lg-5">
		<span class="mask bg-gradient-warning opacity-8"></span>
		  <div class="container-fluid d-flex flex-column justify-content-center align-items-center text-center" 
               style="min-height:200px; position:relative; z-index:1;">
		    <h1 class="display-2 text-white text-shadow mb-0">${vo.title}</h1>
		    <div class="text-white mb-0 mt-2">
		        <span>${msgs.admin}: ${vo.modId}</span>
                <span> | </span>
                <span>${msgs.view} ${vo.viewCnt}</span>
                <span> | </span>
                <span>${vo.modDt}</span>
		    </div>
		  </div>
		    <div class="boardBtns position-absolute" style="bottom:15px; right:20px;">
		    <c:if test="${sessionScope.loginUser.role =='1'  }">
<<<<<<< HEAD
		      <input type="button" id="moveToUpdate" class="btn btn-sm btn-primary" value="${msgs.modi}">
              <input type="button" id="doDelete" class="btn btn-sm btn-primary" value="${msgs.del}">
=======
		      <input type="button" id="moveToUpdate" class="btn btn-sm btn-primary" value="${msgs.reg}">
              <input type="button" id="doDelete" class="btn btn-sm btn-primary" value="${msgs.modi}">
>>>>>>> a76d822e155237841302239ac2dbc91ab4e3722e
            </c:if>
              <input type="button" id="moveToList" class="btn btn-sm btn-primary" value="${msgs.toList}">
		    </div>
	</div><!-- //header -->

<div class="container-fluid d-flex justify-content-center align-items-start" 
     style="margin:0; padding:20px; background:white; min-height:100vh;">
    <div class="row w-100 d-flex justify-content-center">
        <div class="col-8 d-flex justify-content-center" >
            <div class="card w-100" style="min-height: 1000px; box-shadow: none !important;">
                <div class="card-body" style="min-width: 800px;">
                    <input type="hidden" id="boardNo" name="boardNo" value="<c:out value='${vo.boardNo }'/>">
                    <p>${vo.contents}</p>
                    <!-- 내용 길면 스크롤 가능 -->
                </div>
            </div><!-- card -->
        </div>
    </div>
</div>
</div> <!--//main-content  -->
</body>
</html>