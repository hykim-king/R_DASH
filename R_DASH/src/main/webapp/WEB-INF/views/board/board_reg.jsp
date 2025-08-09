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
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script> 

<link rel="icon" href="${CP}/resources/image/Jaemini_face.ico" type="image/x-icon"/>
</head>
<body>
<div class="container px-5 my-5 px-5">
    <h2>게시글 등록하기</h2>
    <!-- form area -->
    <form action="#" method="post" enctype="multipart/form-data">
    <div class="mb-3">
        <label for="name" >제목</label>
        <input type="text" name="name" id="name" autocomplete="name" maxlength="50" required placeholder="제목을 입력해주세요." >
    </div>
    <div class="mb-3">
        <label for="modId">등록자</label>
        <input type="text" name="modId" id="modId" autocomplete="modId" maxlength="50" required placeholder="등록자" >
    </div>
    <div class="mb-3">
        <label>공지</label><input type="checkbox" name="notice" value="30">
    </div>
    <div class="mb-3">
        <label for="contents" >내용</label>
        <textarea class="form-control" id="ckeditor" name="contents"  placeholder="내용" class="contents"></textarea>
    </div>
    </form>
    <!-- //form area -->
    <!-- button area -->
    <div>
        <input type="button" class="btn btn-sm btn-success" id="doSave" value="등록">
        <input type="button" class="btn btn-sm btn-success" id="moveToList" value="목록">
    </div>
    <!-- //button area -->
<script>
    $(document).ready(function() {
        CKEDITOR.replace("ckeditor", {
            width: "100%",
            height: "400px",
            filebrowserUploadUrl: "/bbs/ckeditor/ckEditorUpload",
            image_previewText: " "
        });
    });
</script>
<script src="/ehr/resources/js/summernote/summernote-lite.js"></script>
<script src="/ehr/resources/lang/summernote/lang/summernote-ko-KR.js"></script>
</div>

</body>
</html>