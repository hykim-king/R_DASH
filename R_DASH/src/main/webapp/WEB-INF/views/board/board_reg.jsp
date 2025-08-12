<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="CP" value="${pageContext.request.contextPath}" />
<c:set var="now" value="<%=new java.util.Date()%>" />
<c:set var="sysDate">
    <fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss" />
</c:set>
<c:set var="fontPath" value="${CP}/resources/fonts/summernote.ttf" />

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<meta name="description" content="Start your development with a Dashboard for Bootstrap 4.">
<meta name="author" content="Creative Tim">
<title>공지사항 등록하기</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script> 
<link rel="stylesheet" href="/ehr/resources/summernote/summernote-lite.min.css">
<link rel="icon" href="${CP}/resources/image/Jaemini_face.ico" type="image/x-icon"/>
<script>
document.addEventListener('DOMContentLoaded', function() {
    console.log('DOMContentLoaded');
    
    function isEmpty(value) {
        return value == null || value.trim() === '';
    }
    
    const titleInput = document.querySelector("#title");
    console.log(titleInput);
    
    const summernoteInput = document.querySelector("#summernote");
    console.log(summernoteInput);
    
    const noticeCheck = document.querySelector("#notice");
    console.log(noticeCheck);
    
    const doSaveBtn = document.querySelector("#doSave");
    console.log(doSaveBtn);
    
    const moveToListBtn = document.querySelector("#moveToList");
    console.log(moveToListBtn);
    
    moveToListBtn.addEventListener('click', function() {
        
        window.location.href = '/ehr/board/doRetrieve.do';
        
        // 사용자 확인
        if (!confirm('목록으로 이동합니다.')) {
            return;
        }
    });
    
});
</script>


</head>
<body>
<div>
<div>
    <span>🏠 홈</span><span>></span><span>공지사항</span><span>></span><span>등록</span>
</div>
<div>
    <img style="width:200px; height:150px; object-fit: contain;" src="/ehr/resources/image/board_Jeamin.png">
</div>
<div class="col">
    <div class="card">
        <div class="card-header border-0">
            <h3 class="mb-0">공지사항</h3>
        </div>
	    <!-- form area -->
	    <form action="#" method="post" enctype="multipart/form-data">
	    <div>
	        <label for="title" >제목</label>
	        <input type="text" name="title" id="title" autocomplete="title" maxlength="50" required placeholder="제목을 입력해주세요." >
	    </div>
	    <div>
	        <label for="modId">등록자</label>
	        <input type="text" name="modId" id="modId" autocomplete="modId" maxlength="50" required placeholder="등록자" >
	    </div>
	    <div>
	        <label for="notice">공지</label><input type="checkbox" name="notice" id="notice" value="30">
	    </div>
	    <div>
	        <label for="summernote" >내용</label>
	        <textarea class="form-control" id="summernote" name="contents"  maxlength="50" class="contents"></textarea>
	    </div>
	    </form>
    </div>
</div>
    <!-- //form area -->
    <!-- button area -->
    <div>
        <input type="button" id="doSave" value="등록">
        <input type="button" id="moveToList" value="목록">
    </div>
    <!-- //button area -->
<script src="${CP}/resources/summernote/summernote-lite.min.js"></script>
<script src="${CP}/resources/summernote/lang/summernote-ko-KR.js"></script>
<script>
	$('#summernote').summernote({
		height: 300,                 // 에디터 높이
        minHeight: null,             // 최소 높이
        maxHeight: null,             // 최대 높이
		lang: "ko-KR",
        placeholder: '최대 500자까지 쓸 수 있습니다',
		  toolbar: [
		    // [groupName, [list of button]]
		    ['style', ['bold', 'italic', 'underline', 'clear']],
		    ['fontname', ['fontname']],
		    ['fontsize', ['fontsize']],
		    ['color', ['color']],
		    ['table', ['table']],
		    ['para', ['ul', 'ol', 'paragraph']],
		    ['height', ['height']],
		    ['insert',['picture']]
		  ],
		  fontname: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New','맑은 고딕','궁서','굴림체','굴림','돋움체','바탕체'],
	
		    // 이미지 업로드 처리
		    callbacks: {
		        onImageUpload: function(files) {
		            let formData = new FormData();
		            formData.append("file", files[0]);
		
		            $.ajax({
		                url: '${CP}/board/imageUpload.do',
		                type: 'POST',
		                data: formData,
		                processData: false,
		                contentType: false,
		                success: function(url) {
		                    // 서버에서 반환한 URL 삽입
		                    $('#summernote').summernote('insertImage', url);
		                },
		                error: function() {
		                    alert('이미지 업로드 실패');
		                }
		            });
		        }
		    }
		});
		
</script>
</div>

</body>
</html>