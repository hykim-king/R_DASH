<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<meta name="author" content="Moonsu">
<link href="/ehr/resources/template/dashboard/css/dashboard.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/nucleo/css/nucleo.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/nucleo/css/nucleo-svg.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/@fortawesome/fontawesome-free/css/all.min.css" rel="stylesheet">
<link rel="stylesheet" href="/ehr/resources/summernote/summernote-lite.min.css">
<style>
.row {
    width: 100%; /* 카드 안에서 꽉 차게 */
}
</style>
<title>공지사항 등록하기</title>
</head>
<body>

<div class="main-content">
<div class="header bg-warning pb-6 header bg-gradient-warning py-4 py-lg-6 pt-lg-6">
    <span class="mask bg-gradient-default opacity-4"></span>
    <div class="container-fluid d-flex align-items-center">
        <div class="row">
            <div class="col-lg-7 col-md-10">         
                <h1 class="display-2 text-white">공지사항 등록 안내문</h1>
                <p class="text-white mt-0 mb-5">시민분들께 전달할 재난 최신 정보와 안전 수칙을 쉽고 명확하게 작성해 주세요.     
                                                                                                            작성 후에는 내용 확인 및 수정도 언제든지 가능하니 편하게 관리해 주세요.<br>  
                                                                                                            알림 버튼을 누르면 공지가 사이트를 방문하는 모든 회원에게 전달됩니다.</p>
            <!--    <input type="button" id="moveTolist" class="btn btn-neutral" value="목록으로 "> -->
            </div>
        </div>
    </div>
</div>   

<!-- Page Contents -->
<div class="container-fluid mt--6" style="min-height: 700px; max-width:1700px; margin:0 auto;">
    <div class="row">
    <div class="col-xl-10 offset-xl-1 order-xl-1" >
        <div class="card">
            <div class="card-header">
                <div class="row align-items-center border-0 d-flex align-items-center">
                   <div class="col-8 d-flex align-items-center">
                       <h3 class="mb-0">공지 등록</h3>
                        <label class="custom-toggle ml-3">
                            <input id="checkbox" type="checkbox" checked>
                            <span id="notice" class="custom-toggle-slider rounded-circle" data-label-off="No" data-label-on="알림"></span>
                        </label>
                   </div>
                   <div class="col-4 text-right">
                     <input type="button" id="doSave" class="btn btn-sm btn-primary" value="등록">
                     <input type="button" id="moveToList" class="btn btn-sm btn-primary" value="목록으로">
                   </div>
               </div>
            </div>
            <div class="card-body d-flex justify-content-center align-items-center" style="min-height: 300px;">
             <div class="pl-lg-4 w-100">
                <div class="card-body d-flex flex-column justify-content-center align-items-center">
                <form action="doSave" method="post" class="w-100" enctype="multipart/form-data">
                    <input type="hidden" id="boardNo" name="boardNo">
                    <div class="form-group w-100">
                        <label for="title" class="mr-2"></label>
                        <input type="text" class="form-control w-100" id="title" name="title" autocomplete="title" maxlength="50" required placeholder="제목을 입력해주세요." >
                    </div>              
                    <div class="form-group w-100">
                        <label for="summernote"></label>
                        <textarea class="form-control w-100" id="summernote" name="contents" class="contents" style="white-space: pre-wrap; overflow-wrap: break-word; resize: vertical;"></textarea>
                    </div>
                    <input type="hidden" id="regId" name="regId" value="${user.email}" />
                 </form>
                 </div>
              </div>
            </div>
        </div>
       </div>
    </div>
</div>
<!-- //Page Contents -->



<div>
    <img style="width:200px; height:150px; object-fit: contain;" src="/ehr/resources/image/board_Jeamin.png">
</div>

    
</div>
    <!-- //button area -->

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script> 
<script src="/ehr/resources/summernote/summernote-lite.min.js" defer></script>
<script src="/ehr/resources/summernote/lang/summernote-ko-KR.js"></script>

<script>
$(document).ready(function() {
      console.log($('#summernote').length); 
    var $summernote = $('#summernote');

    $summernote.summernote({
        height: 500,
        minHeight: null,
        maxHeight: null,
        focus: true,
        lang: "ko-KR",
        placeholder: '최대 1500자까지 쓸 수 있습니다',
        toolbar: [
            ['style', ['bold', 'italic', 'underline', 'clear']],
            ['fontname', ['fontname']],
            ['fontsize', ['fontsize']],
            ['color', ['color']],
            ['table', ['table']],
            ['para', ['ul', 'ol', 'paragraph']],
            ['height', ['height']],
            ['insert', ['picture']]
        ],
        callbacks: {
            onInit: function() {
                console.log('Summernote 초기화 완료!');
                
                // 초기화 후에 실행할 코드
                setTimeout(function() {
                    $('.loading, .overlay').css('opacity', 0);
                    setTimeout(function() {
                        $('.loading, .overlay').hide();
                    }, 400);
                }, 2000);
            },
            onImageUpload: function(files) {
                // 여러 파일도 처리 가능
                for (let i = 0; i < files.length; i++) {
                    uploadImage(files[i]);
                }
            },
            onImageLinkInsert: function(url) { 
            	console.log("Image link inserted:", url);
                saveByImageUrl(url, function(savedUrl){
                    $('#summernote').summernote('insertImage', savedUrl);
                });
            }
        }
    });
    
    function uploadImage(file) {
        const formData = new FormData();
        formData.append("file", file);

        $.ajax({
            url: "/ehr/board/uploadSummernoteImageFile", // 서버 이미지 업로드 URL
            type: "POST",
            data: formData,
            contentType: false,
            processData: false,
            dataType: "json",
            success: function(data) {
                // 서버에서 반환한 이미지 URL 삽입
                $('#summernote').summernote('insertImage', data.url);
            },
            error: function(xhr, status, error) {
                console.error("이미지 업로드 실패:", error);
            }
        });
    }
    function saveByImageUrl(url,callback){
    	$.ajax({
            url: "/ehr/board/saveImageByUrl",
            type: "POST",
            contentType: "application/json",
            processData: false,
            data: JSON.stringify({ imageUrl: url }),
            dataType: "json",
            success: function(response) {
            	 console.log("response:", response);

                 // 서버에서 MessageDTO로 반환: messageId, message
                 if (response.messageId == 1) {
                     // 저장 성공
                     if (callback) callback(response.message); // message에 publicUrl이 들어있음
                 } else {
                     // 실패 시 원본 URL 그대로
                     if (callback) callback(url);
                 }
             },
            error: function(xhr, status, error) {
                console.error("url error:", error);
                if (callback) callback(url); // 실패하면 원본 URL 반환
            }
        });
    }

    // 초기화 완료 후 로그
    $summernote.on('summernote.init', function() {
        console.log('Summernote is ready!');
    });
    
    function isEmpty(value) {
        return value === null || value === undefined || value.trim() === "";
    }
    

    // 등록 버튼 클릭 이벤트도 여기 안에 넣기
    $('#doSave').on('click', function() {
    	const titleInput = document.querySelector('#title');
        const summernoteContent = $summernote.summernote('code'); // 안전하게 접근
        if (!summernoteContent || summernoteContent === '<p><br></p>') {
            alert('내용을 입력하세요');
            return;
        }
        if (isEmpty(titleInput.value)) {
            alert('제목을 입력 하세요');
            titleInput.focus();
            return;
        }
        
        // 클릭 시점 체크박스 상태 읽기
        const is_notice = $('#checkbox').is(':checked') ? 'Y' : 'N';


        const formData = new FormData();
        formData.append("title", $('#title').val());
        formData.append("contents", summernoteContent);
        formData.append("isNotice", is_notice);
        formData.append("regId", $('#regId').val());
        console.log("regId:", $('#regId').val());  // 값 찍어보기

        console.log("is_notice: ",is_notice)

        $.ajax({
            type: "POST",
            url: "/ehr/board/doSave.do",
            data: formData,
            processData: false,
            contentType: false,
            dataType: "json",
            success: function(response) {
                if (response.messageId == 1) {
                    alert("등록 되었습니다.");
                    window.location.href = '/ehr/board/doRetrieve.do';
                }
            },
            error: function(xhr, status, error) {
                console.log(xhr.responseText)
                console.error(error);
                alert("등록 중 오류가 발생했습니다.");
            }
        });
    });
    $('#moveToList').on('click', function() {
    	alert("목록으로 이동합니다.");
    	window.location.href = '/ehr/board/doRetrieve.do';
    });
    
});
</script>
</body>
</html>