<!DOCTYPE html>
<html>
<head>
<title>Image Spinner</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<style>
  /* 전체 화면을 덮는 반투명 오버레이 */
  .overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background: rgba(0, 0, 0, 0.5); /* 검정 반투명 */
    z-index: 9998; /* 로딩 스피너보다 아래 */
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

    /* 이미지 크기 맞춤 */
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
</head>
<body>
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
