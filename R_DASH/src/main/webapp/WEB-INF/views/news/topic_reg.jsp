<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="CP" value="${pageContext.request.contextPath}" />
<c:set var="now" value="<%=new java.util.Date()%>" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="/ehr/resources/template/dashboard/css/dashboard.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/nucleo/css/nucleo.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/nucleo/css/nucleo-svg.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/@fortawesome/fontawesome-free/css/all.min.css" rel="stylesheet">
<title>주제 등록</title>
</head>
<body>
<section class="section-header bg-warning text-white pb-9 pb-lg-13 mb-4 mb-lg-6">
	<div class="container">
		<div class="row justify-content-center">
			<div class="col-12 col-md-8 text-center">
				<h1 class="display-2 mb-3" _msttexthash="54081573" _msthash="30">
				오늘의 토픽을 등록하세요 !</h1>
				<p class="lead" _msttexthash="1256972990" _msthash="31">
				 GPT AI가 매일 뉴스를 분석하여 <strong>핵심 토픽 4건</strong>을 자동으로 제공합니다.<br>
기본적으로는 AI의 분석 결과를 그대로 활용하지만,<br>
필요 시 관리자가 직접 토픽을 보완하거나 추가할 수도 있습니다.
				</p>
			</div>
		</div>
	</div>
	<div class="pattern bottom"></div>
</section>
<div class="section section-lg pt-0">
	<div class="container mt-n8 mt-lg-n13 z-2">
		<div class="row justify-content-center">
			<div class="col-12">
				<div class="card border-light shadow-soft p-2 p-md-4 p-lg-5">
				<div class="card-body">
				    <form action="#" method="post">
				    <div class="row">
				         <div class="col-12 mt-4">
					        <div class="form-group">
					            <label for="title" class="form-label text-dark">주제</label>
					            <input type="text" class="form-control" id="title" name="title" autocomplete="title" maxlength="50" required placeholder="제목을 입력하세요." >
					        </div>
					        <div class="form-group">
					            <label for="contents" class="form-label text-dark">주제 상세</label>
					            <textarea style="height:200px;" class="form-control" id="contents" name="contents" maxlength="50" maxlength="300" required placeholder="내용을 입력하세요." ></textarea>
					        </div>
				        </div>
				        </div>
				    </form>
				</div>
				<div class="text-center">
					<input type="button" id="doSave" class="btn btn-warning mt-4 animate-up-2" value="등록">
				</div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
document.addEventListener('DOMContentLoaded', function() {
	
    console.log('DOMContentLoaded');
  
	function isEmpty(value) {
        return value == null || value.trim() === '';
    }
  const doSaveBtn = document.querySelector("#doSave");
  const titleInput = document.querySelector("#title");
  const contentsInput = document.querySelector("#contents");
  
  doSaveBtn.addEventListener("click",function(event){
	  console.log("doSaveBtn click");
	  
	  
		  
		  if (isEmpty(titleInput.value)) {
	          alert('제목을 입력 하세요');
	          titleInput.focus();
	          return;
	      }
	      if (isEmpty(contentsInput.value)) {
	          alert('내용을 입력 하세요');
	          contentsInput.focus();
	          return;
	      }
	      $.ajax({
	          type: "POST",
	          url: "/ehr/news/doSave.do",
	          async : "true", //비동기
	          dataType : "json",
	          data: {
	              "title": titleInput.value,
	              "contents": contentsInput.value
	          },
	          success: function(response) {
	              if(response.messageId === 1) {
	                  // 부모창 DOM 업데이트
	                  if(window.opener && !window.opener.closed) {
	                      window.opener.document.getElementById("detailTitle").textContent = titleInput.value;
	                      window.opener.document.getElementById("detailContents").textContent = contentsInput.value;
	                  }
	                  alert("저장 완료!");
	                  window.close(); // 자식창 닫기
	              } else {
	                  alert("저장 실패: " + response.message);
	              }
	          },
	          error: function() {
	              alert("저장 중 오류 발생");
	          }
	      });
});
});
</script>


</body>
</html>