<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>R-DASH</title>
<style>
  .overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background: rgba(0, 0, 0, 0.5); 
    z-index: 9998;
    opacity: 1;
    transition: opacity 0.5s ease;
  }
  .loading {
    width: 90vw;
    margin: 3rem auto;
    text-align: center;
    position: absolute;
    top:35%;
    z-index: 9999;  
  }
  .loading_spinner {
    display: flex;
    justify-content: center;
    margin: 30px;
    z-index: 9999; 
  }
  .loading_spinner .droplet {
    width: 40px;
    height: 40px;
    margin: 0 6px;
    z-index: 9999; 

    /* ì´ë¯¸ì§ í¬ê¸° ë§ì¶¤ */
    object-fit: cover;

    transform-origin: center bottom;
    animation: bounce 1.2s cubic-bezier(0.3, 0.001, 0.4, 1) infinite;
  }
  .loading_spinner .droplet:nth-child(1) {
    animation-delay: -0.4s;
  }
  .loading_spinner .droplet:nth-child(2) {
    animation-delay: -0.2s;
  }
  .loading_spinner .droplet:nth-child(3) {
    animation-delay: 0s;
  }
  @keyframes bounce {
    0%, 100% {
      transform: translateY(0);
    }
    50% {
      transform: translateY(-20px);
    }
  }
</style>
<link rel="icon" href="/ehr/resources/image/Jaemini_face.ico" type="image/x-icon"/> <!-- 아이콘 -->
</head>
<body>
<script>
document.addEventListener('DOMContentLoaded', function() {
	 console.log("로딩 DOMContentLoaded 실행됨");
      // 브라우저가 렌더링을 완료한 다음에 숨기기
      requestAnimationFrame(function() {
        $('.loading, .overlay').css('opacity', 0);
        requestAnimationFrame(function() {
          $('.loading, .overlay').hide();
        }); // CSS transition 시간과 맞춤
      });
    });
</script>
<div class="overlay"></div>
  <div class="loading">
    <div class="loading_spinner">
      <img src="/ehr/resources/image/Jaemini_face (1).png" alt="droplet1" class="droplet" />
      <img src="/ehr/resources/image/Jaemini_face (1).png" class="droplet" />
      <img src="/ehr/resources/image/Jaemini_face (1).png" class="droplet" />
    </div>
  </div>
</body>
</html>
