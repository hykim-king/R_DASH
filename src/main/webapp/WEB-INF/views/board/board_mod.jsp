<%@ page language="java" contentType="text/html; charset=UTF-8" 
    pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="CP" value="${pageContext.request.contextPath}" />
<c:set var="now" value="<%=new java.util.Date()%>" />
<c:set var="sysDate">
    <fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss" />
</c:set>
<c:set var="fontPath" value="${CP}/resources/fonts/summernote.ttf" />
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 수정하기</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script> 
<link rel="stylesheet" href="/ehr/resources/summernote/summernote-lite.min.css">
<link rel="icon" href="${CP}/resources/image/Jaemini_face.ico" type="image/x-icon"/>
</head>
<body>

<div> 
<div>
    <span>🏠 홈</span><span>></span><span>공지사항</span><span>></span><span>수정</span>
</div>
<div>
    <img style="width:200px; height:150px; object-fit: contain;" src="/ehr/resources/image/board_Jeamin.png">
</div>
<%-- <p>제목 테스트: ${vo.title}</p>
<p>등록자 테스트: ${vo.modId}</p>
<p>내용 테스트: ${vo.contents}</p> --%>
    <h2>공지사항 수정하기</h2>
    <!-- form area -->
    <form action="doUpdate.do" method="post" enctype="multipart/form-data">
    <input type="hidden" name="boardNo" id="boardNo" value="<c:out value='${vo.boardNo}'/>" >
    <div>
        <label for="title" >제목</label>
        <input type="text" name="title" id="title" maxlength="50" value="<c:out value='${vo.title}'/>">
    </div>
    <div>
        <label for="modId">등록자</label>
        <input type="text" name="modId" id="modId" autocomplete="modId" maxlength="50" required placeholder="${vo.modId }" disabled="disabled" disabled value="${vo.modId }">
    </div>
    <div>
        <label>공지</label><input type="checkbox" name="notice" value="30">
    </div>
    <div>
        <label for="contents" >내용</label>
        <textarea class="form-control" id="summernote" name="contents"  maxlength="50" class="contents">${vo.contents }</textarea>
    </div>
    </form>
    <!-- //form area -->
    <!-- button area -->
    <div>
        <input type="button" id="doUpdate" value="수정">
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
    // 초기값 셋팅
    var initContents = `<c:out value='${fn:escapeXml(vo.contents)}'/>`;
    $('#summernote').summernote('code', initContents);
       
</script>

</div>


</body>
</html>