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
<meta name="author" content="Moonsu">
<link href="/ehr/resources/template/dashboard/css/dashboard.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/nucleo/css/nucleo.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/nucleo/css/nucleo-svg.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/@fortawesome/fontawesome-free/css/all.min.css" rel="stylesheet">

<title>공지사항 수정하기</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script> 
<link rel="stylesheet" href="/ehr/resources/summernote/summernote-lite.min.css">
<link rel="icon" href="${CP}/resources/image/Jaemini_face.ico" type="image/x-icon"/>
</head>
<body>
<div class="main-content">
<div class="header bg-warning pb-6 header bg-gradient-warning py-7 py-lg-8 pt-lg-9">
    <span class="mask bg-gradient-default opacity-8"></span>
    <div class="container-fluid d-flex align-items-center">
        <div class="row">
            <div class="col-lg-7 col-md-10">
                <div>
                    <span>🏠   홈</span><span> > </span><span>공지사항</span><span> > </span><span>수정</span>
                </div>
                <h1 class="display-2 text-white">공지사항 수정 안내문</h1>
                <p class="text-white mt-0 mb-5">기존에 등록된 공지 내용을 확인하고 필요 시 수정해 주세요.     
                                                                                                            수정한 내용도 시민분들께 명확하게 전달될 수 있도록 신중히 작성해 주시기 바랍니다.<br>  
                                                                                                            알림 버튼을 누르면 변경된 공지가 사이트를 방문하는 모든 회원에게 다시 전달됩니다.</p>
            <!--    <input type="button" id="moveTolist" class="btn btn-neutral" value="목록으로 "> -->
            </div>
        </div>
    </div>
</div>   


<!-- Page Contents -->
<div class="container-fluid mt--6" style="min-height: 700px; max-width:1700px; margin:0 auto;">
    <div class="row">
    <div class="col-xl-8 offset-xl-2 order-xl-1" >
        <div class="card">
            <div class="card-header">
                <div class="row align-items-center border-0 d-flex align-items-center">
                   <div class="col-8 d-flex align-items-center">
                       <h3 class="mb-0">공지 등록</h3>
                        <label class="custom-toggle ml-3">
                            <input type="checkbox" checked>
                            <span class="custom-toggle-slider rounded-circle" data-label-off="No" data-label-on="알림"></span>
                        </label>
                   </div>
                   <div class="col-4 text-right">
                     <input type="button" id="doUpdate" class="btn btn-sm btn-primary" value="수정">
                     <input type="button" id="moveToList" class="btn btn-sm btn-primary" value="목록으로">
                   </div>
               </div>
            </div>
            <div class="card-body d-flex justify-content-center align-items-center" style="min-height: 300px;"">
             <div class="pl-lg-4 w-75">
                <div class="row">
                <form action="#" method="post" enctype="multipart/form-data">
                   <input type="hidden" name="boardNo" id="boardNo" value="<c:out value='${vo.boardNo}'/>" >
                    <div class="form-group d-flex">
                        <label for="title"></label>
                        <input type="text" class="form-control" name="title" id="title" maxlength="150" value="<c:out value='${vo.title}'/>">
                    </div>
                    <div class="form-group d-flex">
                        <label for="modId"></label>
                        <input type="text" class="form-control" name="modId" id="modId" autocomplete="modId" maxlength="50" required placeholder="${vo.modId }" disabled="disabled" disabled value="${vo.modId }">             
                    </div>              
                    <div class="form-group d-flex">
                        <label for="summernote"></label>
                        <textarea class="form-control" id="summernote" name="contents" class="contents" style="white-space: pre-wrap; overflow-wrap: break-word; resize: vertical;">${vo.contents }</textarea>
                    </div>
                 </form>
                 </div>
              </div>
            </div>
        </div>
       </div>
    </div>
</div>
<!-- //Page Contents -->
</div>


<div>
    <img style="width:200px; height:150px; object-fit: contain;" src="/ehr/resources/image/board_Jeamin.png">
</div>
<%-- <p>제목 테스트: ${vo.title}</p>
<p>등록자 테스트: ${vo.modId}</p>
<p>내용 테스트: ${vo.contents}</p> --%>
  
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




</body>
</html>