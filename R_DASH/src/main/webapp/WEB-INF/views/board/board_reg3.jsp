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
<meta name="author" content="Moonsu">
<link href="/ehr/resources/template/dashboard/css/dashboard.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/nucleo/css/nucleo.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/nucleo/css/nucleo-svg.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/@fortawesome/fontawesome-free/css/all.min.css" rel="stylesheet">

<title>공지사항 등록하기</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script> 
<link rel="stylesheet" href="/ehr/resources/summernote/summernote-lite.min.css">
<link rel="icon" href="${CP}/resources/image/Jaemini_face.ico" type="image/x-icon"/>
</head>
<body>
<jsp:include page="/WEB-INF/views/loading/loading.jsp"></jsp:include>

<div class="main-content">
<div class="header bg-warning pb-6 header bg-gradient-warning py-7 py-lg-8 pt-lg-9">
    <span class="mask bg-gradient-default opacity-8"></span>
    <div class="container-fluid d-flex align-items-center">
        <div class="row">
            <div class="col-lg-7 col-md-10">
                <div>
                    <span>🏠   홈</span><span> > </span><span>공지사항</span><span> > </span><span>등록</span>
                </div>
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
    <div class="col-xl-8 offset-xl-2 order-xl-1" >
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
            <div class="card-body d-flex justify-content-center align-items-center" style="min-height: 300px;"">
             <div class="pl-lg-4 w-75">
                <div class="row">
                <form action="#" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="image" id="image">
                    <div class="form-group d-flex">
                        <label for="title"></label>
                        <input type="text" class="form-control" id="title" name="title" autocomplete="title" maxlength="50" required placeholder="제목을 입력해주세요." >
                    </div>              
                    <div class="form-group d-flex">
                        <label for="summernote"></label>
                        <textarea class="form-control" id="summernote" name="contents" class="contents" style="white-space: pre-wrap; overflow-wrap: break-word; resize: vertical;"></textarea>
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



<div>
    <img style="width:200px; height:150px; object-fit: contain;" src="/ehr/resources/image/board_Jeamin.png">
</div>

    
</div>
    <!-- //button area -->
<script src="${CP}/resources/summernote/summernote-lite.min.js"></script>
<script src="${CP}/resources/summernote/lang/summernote-ko-KR.js"></script>
<script>
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
//등록 버튼이 존재
doSaveBtn.addEventListener('click',function(event){
    console.log('doSaveBtn click');
    // 필수값 체크
    if (isEmpty(titleInput.value)) {
        alert('제목을 입력 하세요');
        titleInput.focus();
        return;
    }
    if (isEmpty(summernoteInput.value)) {
        alert('내용을 입력 하세요');
        summernoteInput.focus();
    }
    
    // FormData 객체 생성
    var formData = new FormData();
    formData.append("title",titleInput.value);
    formData.append("summernote",summernoteInput.value);
    
    $.ajax({
        type: "POST",
        url: "/ehr/board/doSave.do",
        data: formData,
        processData: false,  // 필수! 데이터를 query string으로 변환하지 않음
        contentType: false,  // 필수! multipart/form-data 헤더를 자동 설정
        dataType: "json",    // 서버가 JSON 응답일 경우
        success: function(response) {
            console.log("success: ", response);

            alert(response.message);

            if (response.messageId == 1) {
                window.location.href = '/ehr/board/doRetrieve.do';
            }
        },
        error: function(xhr, status, error) {
            console.log("error: ", error);
            alert("등록 중 오류가 발생했습니다.");
        }
    });
 
});

$('#summernote').summernote({
    height: 300,                 // 에디터 높이
    minHeight: null,             // 최소 높이
    maxHeight: null,             // 최대 높이
    focus: true,                 // 에디터 로딩 후 포커스 여부
    lang: "ko-KR",
    placeholder: '최대 500자까지 쓸 수 있습니다',
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
    fontNames: ['Arial', 'Arial Black', 'Comic Sans MS', 'Courier New', '맑은 고딕', '궁서', '굴림체', '굴림', '돋움체', '바탕체'],

    // 이미지 첨부 콜백
    callbacks: { 
        onImageUpload: function(files) {
            uploadSummernoteImage(files[0], this);   
        },
        onPaste: function(e) {
            var clipBoardData = e.originalEvent.clipboardData;
            if (clipBoardData && clipBoardData.items && clipBoardData.items.length) {
                var item = clipBoardData.items[0];
                if (item.kind === 'file' && item.type.indexOf('image/') !== -1) {
                    e.preventDefault();
                }
            }
        }
    }
});
//이미지 파일 업로드
function uploadSummernoteImage(file, editor) {
    var data = new FormData();
    data.append("file", file);

    $.ajax({
        data: data,
        type: "POST",
        url: "/ehr/board/uploadSummernoteImageFile",
        contentType: false,
        processData: false,
        dataType: "json",
        success: function(data) {
        	console.log("서버 응답:", data); // 전체 객체 확인
            console.log("data.url 값:", data.url); // url 속성만 확인
            // 항상 업로드된 파일의 URL이 있어야 한다.
            if (data.url) {
	        $(editor).summernote('insertImage', data.url);
		    } else {
		        console.error("서버 응답에 url 속성이 없습니다.", data);
		    }
            if (typeof data === "string") {
                try {
                    data = JSON.parse(data);
                } catch (e) {
                    console.error("JSON 파싱 실패", e);
                }
            }
        },
        error: function(err) {
            console.error("이미지 업로드 실패:", err);
        }
    });
}
</script>
<script>
  // 예시로 3초 후에 로딩 숨기기 (실제 로딩 완료 이벤트에 맞게 조절하세요)
  $(document).ready(function() {
    setTimeout(function() {
      $('.loading, .overlay').css('opacity', 0);
      setTimeout(function() {
        $('.loading, .overlay').hide();
      }, 400); // transition 시간과 맞춤
    }, 2000);
  });
</script>
</body>
</html>