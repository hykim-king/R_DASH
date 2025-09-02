<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<link href="/ehr/resources/template/dashboard/css/dashboard.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/nucleo/css/nucleo.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/nucleo/css/nucleo-svg.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/@fortawesome/fontawesome-free/css/all.min.css" rel="stylesheet">
<style>
/* 모달 그림자 & 둥근 모서리 */
.modal-content {
    border-radius: 16px;
    box-shadow: 0 8px 20px rgba(0,0,0,0.3);
}

/* 헤더 글자 강조 */
.modal-header .modal-title {
    font-size: 1.25rem;
}

/* 푸터 버튼 hover */
.modal-footer .btn-warning:hover {
    background-color: #ff7000;
    border-color: #ff7000;
}

/* 모바일 화면에서 폰트 조정 */
@media (max-width: 576px) {
    .modal-body {
        font-size: 0.9rem;
    }
    .modal-footer .btn {
        width: 100%;
    }
}

  /* fade 효과 제거 */
  .moveToboard {
    display: inline-block;
    font-weight: 600;
    color: #fff;
    text-align: center;
    vertical-align: middle;
    cursor: pointer;
    -webkit-user-select: none;
    user-select: none;
    background-color: #333;
    border: 1px solid transparent;
    padding: 0.625rem 1.25rem;
    font-size: 0.875rem;
    line-height: 1.5;
    border-radius: 0.25rem;
    align-items : center;  /*세로축 정렬 */
    justify-content: center;
}

/* 호버 상태 스타일 */
.moveToboard:hover {
    color: #fff; /* 텍스트 색상 */
    background-color: #4e73df; /* 배경색 */
    border-color: #4e73df; /* 테두리 색상 */
    box-shadow: 0 0.25rem 0.5rem rgba(0, 123, 255, 0.15); /* 그림자 효과 */
}
.moveBtn{
  display: flex; 
  align-items : center;  /*세로축 정렬 */
  justify-content: center;
  padding: 10px 0;         /* 필요시 여백 조정 */
}
/* 공지사항 내용(이미지+글자) */
.modal-body {
    font-size: 1rem;
    color: #333;

    max-height: 60vh;   /* 화면 높이 기준으로 모달 본문 높이 제한 */
    overflow-y: auto;   /* 내용이 넘치면 스크롤 */
    word-wrap: break-word;
}

.modal-body img {
    max-width: 100% !important;  /* 부모 폭을 넘어가지 않도록 강제 */
    height: auto !important;      /* 비율 유지 */
    display: block;
    margin: 10px auto;            /* 가운데 정렬 + 위아래 여백 */
}
</style>

<div class="modal fade" id="noticeModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content rounded-4 shadow-lg border-0">
      <!-- 헤더 -->
      <div class="modal-header bg-gradient" style="background: linear-gradient(90deg, #FF8C00, #FFA500); color: white;">
        <h4 class="modal-title fw-bold">재난 관련 공지사항을 전달드립니다.</h4>
         </div>
      <!-- 내용 -->
      <div class="modal-body" style="font-size: 1rem; color: #333;">
        <div id="noticeTitle" class="mb-0"></div>
        <div id="noticeContents" class="mb-0"></div>
      </div>
      <div class="moveBtn">
       <button type="button" id="moveNoticeBtn" class="btn btn-default">공지사항 더보기 ✨</button>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>


<script>
   let socket = new SockJS("/ehr/ws");
   let stompClient = Stomp.over(socket);
   
   stompClient.connect({},function(){
	  console.log("WebSocket 연결 성공 !") 
   
   //모든 페이지에서 구독
   stompClient.subscribe("/topic/notice",function(message){
	   console.log("Connected: ",message);
	   let notice = JSON.parse(message.body);
	   console.log("Received boardNo:", notice.boardNo);
	   
	   document.getElementById("noticeTitle").innerText = notice.title;
	   document.getElementById("noticeContents").innerHTML = notice.contents;

       // 모달 표시
       let noticeModal = new bootstrap.Modal(document.getElementById("noticeModal"));
       noticeModal.show();
   });
	  //공지로 이동하기
	$("#moveNoticeBtn").on('click',function(){
		console.log("moveNoticeBtn click");
		
		alert("공지사항으로 이동합니다.");
		
		// 새 탭에서 열기
	    window.open('/ehr/board/doRetrieve.do','_self');
		
	})
});
   
</script>