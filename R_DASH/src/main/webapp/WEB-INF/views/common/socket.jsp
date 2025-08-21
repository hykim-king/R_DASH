<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
</style>


<div class="modal fade" id="noticeModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content rounded-4 shadow-lg border-0">
      <!-- 헤더 -->
      <div class="modal-header bg-gradient" style="background: linear-gradient(90deg, #FF8C00, #FFA500); color: white;">
        <h5 id="noticeTitle" class="modal-title fw-bold">공지</h5>
<!--         <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
 -->      </div>
      <!-- 내용 -->
      <div class="modal-body" style="font-size: 1rem; color: #333;">
        <div id="noticeContents" class="mb-0"></div>
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
	   console.log("Received boardNo:", message.boardNo);
	   
	   document.getElementById("noticeTitle").innerText = notice.title;
	   document.getElementById("noticeContents").innerHTML = notice.contents;

       // 모달 표시
       let noticeModal = new bootstrap.Modal(document.getElementById("noticeModal"));
       noticeModal.show();
   });
});
   
</script>