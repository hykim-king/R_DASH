<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="CP" value="${pageContext.request.contextPath}" />
<c:set var="now" value="<%=new java.util.Date()%>" />
<c:set var="sysDate">
	<fmt:formatDate value="${now}" pattern="yyyy-MM-dd HH:mm:ss" />
</c:set>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>재민이</title>

<link rel="icon" href="${CP}/resources/image/Jaemini_face.ico"
	type="image/x-icon" />
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="${CP}/resources/template/Animate/backInUp.css">

<style>
html, body {
	margin: 0;
	padding: 0;
	font-family: 'Noto Sans KR', sans-serif;
	scroll-behavior: smooth;
	width: 100%;
}

/* 메인 + 뉴스 공통 배경 */
.main-background {
	background-image: url('${CP}/resources/image/mainboard.png');
	background-size: cover;
	background-position: center;
	background-repeat: no-repeat;
	background-attachment: fixed;
	color: white;
}

/* 메인 섹션 */
.main-section {
	height: 100vh;
	display: flex;
	flex-direction: column;
	justify-content: center;
	align-items: center;
	text-align: center;
	padding-top: 80px;
	position: relative;
}

/* 상단 네비게이션 바 */
.top-bar {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	padding: 10px 30px;
	background: rgba(0, 0, 0, 0.4);
	display: flex;
	justify-content: center;
	align-items: center;
	z-index: 1000;
	backdrop-filter: blur(5px);
}

/* 언어/로그인 버튼 */
.right-menu {
	position: absolute;
	right: 30px;
	display: flex;
	gap: 10px;
	align-items: center;
}

.lang-btn, .login-btn {
	padding: 6px 12px;
	border: none;
	border-radius: 5px;
	font-size: 0.9rem;
}

.lang-btn {
	background-color: #6c757d;
	color: white;
}

.login-btn {
	background-color: #dc3545;
	color: white;
}

/* 상단 메뉴 */
.center-menu {
	display: flex;
	justify-content: center;
	gap: 40px;
	z-index: 1;
}

.center-menu a {
	color: white;
	text-decoration: none;
	font-size: 1rem;
	font-weight: 500;
}

/* 드롭다운(공통) */
.dropdown-wrapper {
	display: none;
	position: absolute;
	top: 100%;
	left: 0;
	width: 100%;
	padding-top: 10px;
	display: flex;
	justify-content: center;
	gap: 40px;
	z-index: 999;
	background: transparent;
}

.dropdown-column {
	display: flex;
	flex-direction: column;
}

.dropdown-column a {
	color: white;
	text-decoration: none;
	padding: 6px 12px;
	white-space: nowrap;
	font-size: 0.9rem;
}

.dropdown-column a:hover {
	text-decoration: underline;
	background-color: rgba(255, 255, 255, 0.1);
}

.dropdown-wrap:hover .mega-dropdown {
	display: flex;
}

/* 개별 nav-item 하위 드롭다운 */
.nav-item {
	position: relative;
}

.submenu {
	position: absolute;
	top: 100%;
	left: 0;
	display: none;
	flex-direction: column;
	background: rgba(0, 0, 0, 0.9);
	padding: 10px 0;
	border-radius: 6px;
	min-width: 160px;
	z-index: 1000;
}

.submenu a {
	color: white;
	padding: 8px 20px;
	text-decoration: none;
	font-size: 0.9rem;
	white-space: nowrap;
}

.submenu a:hover {
	background-color: rgba(255, 255, 255, 0.2);
}

.dropdown:hover .submenu {
	display: flex;
}

/* 전체 드롭다운 표시 트리거 */
.top-bar:hover+.dropdown-wrapper, .center-menu:hover+.dropdown-wrapper,
	.dropdown-wrapper:hover {
	display: flex !important;
}

/* 메가 드롭다운 */
.mega-dropdown {
	display: none;
	position: absolute;
	top: 100%;
	left: 0;
	padding: 15px 20px;
	background: rgba(0, 0, 0, 0.9);
	z-index: 999;
	flex-direction: row;
	gap: 40px;
}

.mega-dropdown .dropdown-column {
	display: flex;
	flex-direction: column;
}

.mega-dropdown .dropdown-column a {
	color: white;
	text-decoration: none;
	padding: 6px 12px;
	font-size: 0.9rem;
}

.mega-dropdown .dropdown-column a:hover {
	background-color: rgba(255, 255, 255, 0.1);
}

/* 모든 상단 메뉴 hover 시 전체 드롭다운 */
.top-bar:hover ~ .mega-dropdown, .mega-dropdown:hover {
	display: flex;
}

/* 검색창 */
.search-container {
	margin-top: 150px;
	text-align: center;
}

.search-container h1 {
	font-size: 1.6rem;
	font-weight: bold;
	margin-bottom: 20px;
	text-shadow: 1px 1px 4px rgba(0, 0, 0, 0.5);
}

.search-box {
	display: flex;
	justify-content: center;
	align-items: center;
	width: 320px;
	margin: 15px auto;
}

.search-input {
	border-radius: 30px 0 0 30px;
	padding: 10px;
	border: none;
	width: 100%;
	font-size: 0.9rem;
}

.search-btn {
	border-radius: 0 30px 30px 0;
	border: none;
	padding: 10px 18px;
	background-color: #dc3545;
	color: white;
	font-size: 0.9rem;
	writing-mode: horizontal-tb;
	line-height: 1;
	white-space: nowrap;
}

/* 뉴스 섹션 */
.news-section {
	padding: 60px 0;
}

.news-section .card {
	background-color: rgba(255, 255, 255, 0.85);
}

.news-title {
	text-align: center;
	font-size: 1.8rem;
	font-weight: bold;
	margin-bottom: 30px;
	color: white;
	text-shadow: 1px 1px 3px black;
}

/* FAQ 섹션 */
.faq-section {
	background-color: #212529;
	color: white;
	padding: 60px 20px;
}

.accordion-button {
	background-color: #343a40;
	color: #fff;
}

.accordion-button:not(.collapsed) {
	background-color: #495057;
}

.accordion-body {
	background-color: #2c2f33;
	color: #ccc;
}

img.card-img-top {
	width: 100%;
	height: 200px;
	object-fit: cover;
}

/* ===== 우측 하단 플로팅 이모티콘 ===== */
.floating-icon {
	position: fixed;
	right: 22px;
	bottom: 22px;
	z-index: 2000;
	cursor: pointer;
}

.floating-icon img {
	width: 76px;
	height: 76px;
	object-fit: cover;
	border-radius: 50%;
	box-shadow: 0 6px 14px rgba(0, 0, 0, 0.35);
	transition: transform .18s ease-in-out, box-shadow .18s ease-in-out;
	background: #fff;
}

.floating-icon:hover img {
	transform: scale(1.07);
	box-shadow: 0 10px 18px rgba(0, 0, 0, 0.45);
}

@media ( max-width : 576px) {
	.floating-icon img {
		width: 60px;
		height: 60px;
	}
}

/* ===== 채팅 모달 ===== */
.chat-modal .modal-dialog {
	max-width: 420px;
}

/* 우하단 고정 배치 (부트스트랩 중앙 배치 대신) */
.chat-modal .modal-dialog {
	position: fixed;
	right: 24px;
	bottom: 110px; /* 플로팅 아이콘과 간격 */
	margin: 0;
}

.chat-modal .modal-content {
	border-radius: 16px;
	overflow: hidden;
}

.chat-header {
	background: #dc3545;
	color: #fff;
	padding: 12px 16px;
}

.chat-body {
	height: 360px;
	overflow-y: auto;
	background: #f6f7f9;
	padding: 12px;
}

.chat-footer {
	display: flex;
	gap: 8px;
	padding: 10px 12px 14px;
	background: #fff;
	border-top: 1px solid #eee;
}

.chat-input {
	flex: 1;
	resize: none;
	height: 44px;
	padding: 10px 12px;
	border: 1px solid #ddd;
	border-radius: 10px;
	outline: none;
}

.chat-send-btn {
	border: none;
	border-radius: 10px;
	padding: 0 16px;
	background: #dc3545;
	color: #fff;
	font-weight: 600;
}

.chat-bubble {
	max-width: 78%;
	margin: 6px 0;
	padding: 10px 12px;
	border-radius: 14px;
	line-height: 1.4;
	word-break: break-word;
	box-shadow: 0 1px 2px rgba(0, 0, 0, .06);
	font-size: .95rem;
}

.chat-bubble.user {
	margin-left: auto;
	background: #ffe3e6;
}

.chat-bubble.bot {
	margin-right: auto;
	background: #fff;
}

.chat-time {
	display: block;
	margin-top: 2px;
	font-size: .75rem;
	opacity: .6;
}
</style>
</head>

<body>

	<!-- 메인 화면 -->
	<div class="main-section main-background">
		<div class="top-bar">
			<div class="center-menu">
				<div class="nav-item">
					<a href="#">통계 페이지</a>
				</div>
				<div class="nav-item">
					<a href="#">재난 페이지</a>
				</div>
				<div class="nav-item">
					<a href="#">뉴스 페이지</a>
				</div>
				<div class="nav-item">
					<a href="#">토픽 페이지</a>
				</div>
				<div class="nav-item">
					<a href="#">지도 페이지</a>
				</div>
				<div class="nav-item">
					<a href="#">마이 페이지</a>
				</div>
			</div>
			<div class="right-menu">
				<button class="lang-btn">🌐 한국어 ▾</button>
				<a href="${CP}/user/login" class="login-btn">로그인</a>
			</div>
		</div>

		<div class="mega-dropdown">
			<div class="dropdown-column">
				<a href="#">온여지료환자 통계</a> <a href="#">초단기 실화 통계</a> <a href="#">공공시설
					복구현황</a> <a href="#">화장 재난 통계</a> <a href="#">소화기 통계</a>
			</div>
			<div class="dropdown-column">
				<a href="#">지진</a> <a href="#">황사</a> <a href="#">싱크홀</a>
			</div>
			<div class="dropdown-column">
				<a href="#">오늘의 뉴스</a> <a href="#">재난 뉴스</a> <a href="#">그것이
					알고싶다!</a>
			</div>
			<div class="dropdown-column">
				<a href="#">안녕</a> <a href="#">클레오파트라</a> <a href="#">세상에서 제일가는
					포테이토치프</a>
			</div>
			<div class="dropdown-column">
				<a href="#">지진은 어디인가?</a> <a href="#">황사 바람 온다.</a> <a href="#">싱크홀
					땅 긴진~</a>
			</div>
			<div class="dropdown-column">
				<a href="#">게시판</a>
			</div>
		</div>

		<div class="search-container backInUp">
			<h1>저희 재난 알림 사이트를 방문해주셔서 감사합니다.</h1>
			<div class="search-box">
				<form action="${CP}/search" method="get"
					style="display: flex; width: 100%;">
					<input type="text" name="keyword" class="search-input"
						placeholder="원하시는 검색 단어를 입력해주세요." required>
					<button type="submit" class="search-btn">검색</button>
				</form>
			</div>
		</div>
	</div>

	<!-- 주요 뉴스 -->
	<div class="news-section main-background animate-on-scroll" id="news">
		<div class="container">
			<div class="news-title">주요 뉴스</div>
			<div class="row">
				<div class="col-md-4 mb-3">
					<div class="card h-100">
						<img src="${CP}/resources/image/earth.jpg" class="card-img-top"
							alt="지진 이미지">
						<div class="card-body">
							<h5 class="card-title">[속보] 강진 발생</h5>
							<p class="card-text">해당 지역 주민은 안전한 곳으로 대피 바랍니다.</p>
						</div>
					</div>
				</div>
				<div class="col-md-4 mb-3">
					<div class="card h-100">
						<img src="${CP}/resources/image/dis.jpg" class="card-img-top"
							alt="화재 이미지">
						<div class="card-body">
							<h5 class="card-title">화재 시 행동요령 안내</h5>
							<p class="card-text">119 긴급 행동요령 숙지로 생명을 지키세요.</p>
						</div>
					</div>
				</div>
				<div class="col-md-4 mb-3">
					<div class="card h-100">
						<img src="${CP}/resources/image/med.jpg" class="card-img-top"
							alt="응급 이미지">
						<div class="card-body">
							<h5 class="card-title">응급상황 대응 시스템 강화</h5>
							<p class="card-text">최근 대응 시간 단축을 위한 시스템 개편 발표.</p>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- FAQ -->
	<div class="faq-section animate-on-scroll" id="faq">
		<div class="container">
			<h4 class="mb-4">자주 묻는 질문</h4>
			<div class="accordion" id="faqAccordion">
				<div class="accordion-item">
					<h2 class="accordion-header" id="headingOne">
						<button class="accordion-button" type="button"
							data-bs-toggle="collapse" data-bs-target="#faq1"
							aria-expanded="true">대피소 위치는 어디서 확인할 수 있나요?</button>
					</h2>
					<div id="faq1" class="accordion-collapse collapse show"
						data-bs-parent="#faqAccordion">
						<div class="accordion-body">상단 '지도 페이지' 메뉴에서 확인 가능합니다.</div>
					</div>
				</div>
				<div class="accordion-item">
					<h2 class="accordion-header" id="headingTwo">
						<button class="accordion-button collapsed" type="button"
							data-bs-toggle="collapse" data-bs-target="#faq2">재난 종류에는
							무엇이 있나요?</button>
					</h2>
					<div id="faq2" class="accordion-collapse collapse"
						data-bs-parent="#faqAccordion">
						<div class="accordion-body">화재, 지진, 태풍, 폭염, 감염병 등 다양한 유형이
							있습니다.</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- 우측 하단 고정 아이콘 -->
	<a class="floating-icon" href="#" aria-label="도움말 또는 채팅 열기"
		title="도움말/채팅"> <img src="${CP}/resources/image/Jaemini_bo.jpg"
		alt="재민이 아이콘">
	</a>

	<!-- 채팅 모달 -->
	<div class="modal fade chat-modal" id="chatModal" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div
					class="chat-header d-flex justify-content-between align-items-center">
					<strong>재민이 채팅</strong>
					<button type="button" class="btn-close btn-close-white"
						data-bs-dismiss="modal" aria-label="Close"></button>
				</div>
				<div id="chatBody" class="chat-body">
					<div class="chat-bubble bot">
						안녕하세요! 재난 알림 도우미 <b>재민이</b>입니다. 무엇을 도와드릴까요? <span
							class="chat-time"></span>
					</div>
				</div>
				<div class="chat-footer">
					<textarea id="chatInput" class="chat-input"
						placeholder="궁금한거 있으면 물어봐요!"></textarea>
					<button id="chatSend" class="chat-send-btn">전송</button>
				</div>
			</div>
		</div>
	</div>

	<!-- JS -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
	<script>
document.addEventListener("DOMContentLoaded", () => {
  const CP = '${CP}';

  // 스크롤 애니메이션
  const elements = document.querySelectorAll(".animate-on-scroll");
  const observer = new IntersectionObserver((entries, observer) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add("backInUp");
        observer.unobserve(entry.target);
      }
    });
  }, { threshold: 0.2 });
  elements.forEach(el => observer.observe(el));

  // 채팅 모달 오픈
  const icon = document.querySelector('.floating-icon');
  const chatModalEl = document.getElementById('chatModal');
  const chatModal = new bootstrap.Modal(chatModalEl);
  const chatBody = document.getElementById('chatBody');
  const chatInput = document.getElementById('chatInput');
  const chatSend = document.getElementById('chatSend');

  if (icon) {
    icon.addEventListener('click', (e) => {
      e.preventDefault();
      chatModal.show();
      setTimeout(() => chatInput.focus(), 200);
    });
  }

  const nowText = () => {
    const d = new Date();
    return d.toLocaleTimeString([], {hour:'2-digit', minute:'2-digit'});
  };

  function appendBubble(text, who) {
	  var role = (who === 'bot') ? 'bot' : 'user';
	  var wrap = document.createElement('div');
	  wrap.className = 'chat-bubble ' + role;  // ✅ JSP EL 충돌 없음

	  wrap.innerHTML = text + '<span class="chat-time">' + nowText() + '</span>';
	  chatBody.appendChild(wrap);
	  chatBody.scrollTop = chatBody.scrollHeight;
	}

  // 세션ID를 로컬에 저장해 대화 유지
  let SESSION_ID = localStorage.getItem('X-Session-Id') || null;

  async function sendMessage() {
	  const text = chatInput.value.trim();
	  if (!text) return;
	  appendBubble(text, 'user');
	  chatInput.value = '';

	  try {
	    console.log('POST', `${CP}/api/chat/send`, {question: text}); // 디버그
	    const res = await fetch(`${CP}/api/chat/send`, {
	      method: 'POST',
	      headers: {
	        'Content-Type':'application/json',
	        ...(SESSION_ID ? {'X-Session-Id': SESSION_ID} : {})
	      },
	      body: JSON.stringify({ question: text })
	    });

	    const sid = res.headers.get('X-Session-Id');
	    if (sid) { SESSION_ID = sid; localStorage.setItem('X-Session-Id', sid); }

	    const raw = await res.text(); // ← 응답을 문자열로 먼저
	    let data = null; try { data = JSON.parse(raw); } catch {}
	    if (!res.ok) {
	      appendBubble(`AI 호출 실패(${res.status}) ${raw || ''}`, 'bot'); // 서버 메시지까지 보여주기
	      console.error('chat/send error', res.status, raw);
	      return;
	    }

	    appendBubble((data && data.answer) || '응답을 불러오지 못했어요.', 'bot');
	  } catch (e) {
	    appendBubble('네트워크 오류가 발생했어요. 연결 상태를 확인해 주세요.', 'bot');
	    console.error(e);
	  }
	}

  chatSend.addEventListener('click', sendMessage);
  chatInput.addEventListener('keydown', (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      sendMessage();
    }
  });

  chatModalEl.addEventListener('shown.bs.modal', () => {
    chatBody.scrollTop = chatBody.scrollHeight;
  });
});
</script>
</body>
</html>