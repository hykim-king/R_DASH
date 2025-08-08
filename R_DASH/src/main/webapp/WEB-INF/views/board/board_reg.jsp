<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="CP" value="${pageContext.request.contextPath}" />
<c:set var="now" value="<%=new java.util.Date()%>" />
<c:set var="sysDate">
    <fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss" />
</c:set>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>board_reg</title>
<link rel="icon" href="${CP}/resources/image/Jaemini_face.ico" type="image/x-icon"/>
</head>
<body>
<div>
    <h4>게시글 등록하기</h4>
    <!-- form area -->
    <form action="#" method="post" enctype="multipart/form-data">
    <div>
        <label for="name" >제목</label>
        <input type="text" name="name" id="name" autocomplete="name" maxlength="50" required placeholder="제목" >
    </div>
    <div>
        <label for="modId">등록자</label>
        <input type="text" name="modId" id="modId" autocomplete="modId" maxlength="50" required placeholder="등록자" >
    </div>
    <div>
        <label>공지</label><input type="checkbox" name="notice" value="30">
    </div>
    <div>
        <label for="contents" >내용</label>
        <textarea id="contents" name="contents"  placeholder="내용" class="contents"></textarea>
    </div>
    </form>
    <!-- //form area -->
    <!-- button area -->
    <div>
        <input type="button" id="doSave" value="등록">
        <input type="button" id="moveToList" value="목록">
    </div>
    <!-- //button area -->
</div>

</body>
</html>