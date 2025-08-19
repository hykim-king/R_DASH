<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="modal fade" id="noticeModal" tabindex="-1" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content rounded-3 shadow-lg">
      <div class="modal-header bg-dark text-white">
        <h5 id="noticeTitle" class="modal-title">공지</h5>
        <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
      </div>
      <div class="modal-body">
<!--         <h6 id="noticeTitle" class="fw-bold"></h6>
 -->        <div id="noticeContents" class="mb-0"></div>
      </div>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
<script src="/ehr/resources/js/bootstrap.bundle.min.js"></script>


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