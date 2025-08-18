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
<h3>토픽을 등록하세요 !</h3>
<div>
	<input type="button" id="doSave" class="btn btn-sm btn-primary" value="등록">
    <input type="button" id="moveToNewsPage" class="btn btn-sm btn-primary" value="목록으로">
</div>
<script>
  const moveToNewsPageBtn = document.querySelector("#moveToNewsPage");
  
  moveToNewsPageBtn.addEventListener("click",function(event){
	  if(window.opener && !window.opener.closed){
	          //window.opener.receiveDataFromChild(msg);
	  }else{
	      alert('부모창을 찾을 수 없어요.');
	  }

});
  
</script>


</body>
</html>