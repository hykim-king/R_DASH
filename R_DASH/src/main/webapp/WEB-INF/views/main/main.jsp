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
<link rel="stylesheet"
	href="${CP}/resources/template/Animate/backInUp.css" />

<style>
/* ========== Base ========== */
html, body {
	margin: 0;
	padding: 0;
	width: 100%;
	scroll-behavior: smooth;
	color: #fff;
	font-family: 'Noto Sans KR', system-ui, -apple-system, 'Segoe UI',
		Roboto, sans-serif;
}

/* 공통 배경(뉴스/FAQ 등) */
.main-background {
	background-image: url('${CP}/resources/image/mainboard.png');
	background-size: cover;
	background-position: center;
	background-repeat: no-repeat;
	background-attachment: fixed;
	color: #fff;
}


/* ================== 메인 섹션(비디오 배경) ================== */
.main-section {
	height: 100vh;
	width: 100%;
	margin: 0 !important;
	padding: 0 !important;
	position: relative;
	display: flex;
	justify-content: center;
	align-items: center;
	text-align: center;
	overflow: hidden;
}

/* 메인 섹션은 이미지 배경 제거(비디오만) */
.main-section.main-background {
	background: none;
}

/* 메인 섹션 안에서만 비디오 꽉 채우기 */
#bg-video, .main-section>video {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	object-fit: cover; /* 비율 유지하며 꽉 채움 */
	z-index: -1;
}

/* 비디오 위 어둡게(가독성용) */
.main-section::before {
	content: "";
	position: absolute;
	inset: 0;
	background: rgba(0, 0, 0, .25);
	z-index: 0;
}

/* ========== Dropdowns / News / FAQ / Chat (기존 그대로) ========== */
.dropdown-wrapper {
	display: none;
	position: absolute;
	top: 100%;
	left: 0;
	width: 100%;
	padding-top: 10px;
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
	color: #fff;
	text-decoration: none;
	padding: 6px 12px;
	white-space: nowrap;
	font-size: .9rem;
}

.dropdown-column a:hover {
	text-decoration: underline;
	background-color: rgba(255, 255, 255, .1);
}

.dropdown-wrap:hover .mega-dropdown {
	display: flex;
}

.nav-item {
	position: relative;
}

.submenu {
	position: absolute;
	top: 100%;
	left: 0;
	display: none;
	flex-direction: column;
	background: rgba(0, 0, 0, .9);
	padding: 10px 0;
	border-radius: 6px;
	min-width: 160px;
	z-index: 1000;
}

.submenu a {
	color: #fff;
	padding: 8px 20px;
	text-decoration: none;
	font-size: .9rem;
	white-space: nowrap;
}

.submenu a:hover {
	background: rgba(255, 255, 255, .2);
}

.dropdown:hover .submenu {
	display: flex;
}

.top-bar:hover+.dropdown-wrapper, .center-menu:hover+.dropdown-wrapper,
	.dropdown-wrapper:hover {
	display: flex !important;
}

.mega-dropdown {
	display: none;
	position: absolute;
	top: 100%;
	left: 0;
	padding: 15px 20px;
	background: rgba(0, 0, 0, .9);
	z-index: 999;
	flex-direction: row;
	gap: 40px;
}

.mega-dropdown .dropdown-column {
	display: flex;
	flex-direction: column;
}

.mega-dropdown .dropdown-column a {
	color: #fff;
	text-decoration: none;
	padding: 6px 12px;
	font-size: .9rem;
}

.mega-dropdown .dropdown-column a:hover {
	background: rgba(255, 255, 255, .1);
}

.top-bar:hover ~ .mega-dropdown, .mega-dropdown:hover {
	display: flex;
}

.news-section {
	padding: 60px 0;
}

.news-section .card {
	background: rgba(255, 255, 255, .85);
	color: #111;
}

.news-title {
	text-align: center;
	font-size: 1.8rem;
	font-weight: 700;
	margin-bottom: 30px;
	color: #fff;
	text-shadow: 1px 1px 3px #000;
}

.faq-section {
	background: #212529;
	color: #fff;
	padding: 60px 20px;
}

.accordion-button {
	background: #343a40;
	color: #fff;
}

.accordion-button:not(.collapsed) {
	background: #495057;
}

.accordion-body {
	background: #2c2f33;
	color: #ccc;
}

img.card-img-top {
	width: 100%;
	height: 200px;
	object-fit: cover;
}

.floating-icon {
	position: fixed;
	right: 22px;
	bottom: 20px;
	z-index: 9999;
	cursor: pointer;
}

.floating-icon img {
	width: 76px;
	height: 76px;
	object-fit: cover;
	border-radius: 50%;
	box-shadow: 0 6px 14px rgba(0, 0, 0, .35);
	transition: transform .18s, box-shadow .18s;
	background: #fff;
}

.floating-icon:hover img {
	transform: scale(1.07);
	box-shadow: 0 10px 18px rgba(0, 0, 0, .45);
}

@media ( max-width :576px) {
	.floating-icon img {
		width: 60px;
		height: 60px;
	}
}

.chat-modal .modal-dialog {
	max-width: 560px;
	width: min(92vw, 560px);
	position: fixed;
	right: 28px;
	bottom: 120px;
	margin: 0;
	z-index: 1065;
}

.chat-modal .modal-content {
	border-radius: 16px;
	overflow: hidden;
	height: 68vh;
	max-height: 820px;
	display: flex;
	flex-direction: column;
}

.chat-header {
	background: #dc3545;
	color: #fff;
	padding: 14px 18px;
	font-weight: 700;
}

.chat-body {
	flex: 1;
	min-height: 0;
	overflow-y: auto;
	background: #f6f7f9;
	padding: 14px;
}

.chat-footer {
	display: flex;
	gap: 10px;
	padding: 12px 14px 16px;
	background: #fff;
	border-top: 1px solid #eee;
}

.chat-input {
	flex: 1;
	resize: none;
	height: 52px;
	padding: 12px 14px;
	border: 1px solid #ddd;
	border-radius: 10px;
	outline: none;
}

.chat-send-btn {
	border: none;
	border-radius: 10px;
	padding: 0 18px;
	background: #dc3545;
	color: #fff;
	font-weight: 700;
	height: 52px;
}

.chat-bubble {
	max-width: 86%;
	margin: 8px 0;
	padding: 12px 14px;
	border-radius: 14px;
	line-height: 1.5;
	word-break: break-word;
	box-shadow: 0 1px 2px rgba(0, 0, 0, .06);
	font-size: 1rem;
	color: #111;
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
	font-size: .78rem;
	opacity: .6;
}

@media ( max-width :576px) {
	.chat-modal .modal-dialog {
		right: 14px;
		bottom: 90px;
		width: 94vw;
		max-width: none;
	}
	.chat-modal .modal-content {
		height: 72vh;
	}
	.chat-input, .chat-send-btn {
		height: 48px;
	}
}
</style>
</head>

<body>

	<!-- 메인 화면(히어로) : 풀블리드 적용 -->
	<div class="main-section main-background">
		<!-- 배경 비디오 -->
		<video id="bg-video" autoplay muted loop preload="auto"
			poster="${CP}/resources/image/mainboard.png">
			<source src="${CP}/resources/video/jaemini.mp4" type="video/mp4">
			동영상을 재생할 수 없습니다.
		</video>
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
	<a class="floating-icon" href="#" aria-label="도움말 또는 채팅 열기" title="재민이">
		<img src="${CP}/resources/image/Jaemini_bo.jpg" alt="재민이 아이콘">
	</a>

	<!-- 채팅 모달 -->
	<div class="modal fade chat-modal" id="chatModal" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div
					class="chat-header d-flex justify-content-between align-items-center">
					<strong>재민이 채팅</strong>
					<button id="chatClose" type="button"
						class="btn-close btn-close-white" data-bs-dismiss="modal"
						aria-label="Close"></button>
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

    // 채팅 모달
    const icon = document.querySelector('.floating-icon');
    const chatModalEl = document.getElementById('chatModal');
    const chatModal = new bootstrap.Modal(chatModalEl);
    const chatBody = document.getElementById('chatBody');
    const chatInput = document.getElementById('chatInput');
    const chatSend = document.getElementById('chatSend');

    const chatCloseBtn = document.getElementById('chatClose');
    if (chatCloseBtn) {
      chatCloseBtn.addEventListener('click', (e) => {
        e.preventDefault(); e.stopPropagation(); chatModal.hide();
      });
    }
    if (icon) {
      icon.addEventListener('click', (e) => {
        e.preventDefault(); chatModal.show(); setTimeout(() => chatInput.focus(), 200);
      });
    }

    const nowText = () => {
      const d = new Date();
      return d.toLocaleTimeString([], {hour:'2-digit', minute:'2-digit'});
    };

    function appendBubble(text, who) {
      var role = (who === 'bot') ? 'bot' : 'user';
      var wrap = document.createElement('div');
      wrap.className = 'chat-bubble ' + role;
      wrap.innerHTML = text + '<span class="chat-time">' + nowText() + '</span>';
      chatBody.appendChild(wrap);
      chatBody.scrollTop = chatBody.scrollHeight;
    }

    // 세션ID 유지
    let SESSION_ID = localStorage.getItem('X-Session-Id') || null;

    async function sendMessage() {
      const text = chatInput.value.trim();
      if (!text) return;
      appendBubble(text, 'user');
      chatInput.value = '';

      try {
        const res = await fetch(`${CP}/api/chat/send`, {
          method: 'POST',
          headers: { 'Content-Type':'application/json', ...(SESSION_ID ? {'X-Session-Id': SESSION_ID} : {}) },
          body: JSON.stringify({ question: text })
        });

        const sid = res.headers.get('X-Session-Id');
        if (sid) { SESSION_ID = sid; localStorage.setItem('X-Session-Id', sid); }

        const raw = await res.text();
        let data = null; try { data = JSON.parse(raw); } catch {}
        if (!res.ok) {
          appendBubble(`AI 호출 실패(${res.status}) ${raw || ''}`, 'bot');
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
      if (e.key === 'Enter' && !e.shiftKey) { e.preventDefault(); sendMessage(); }
    });

    chatModalEl.addEventListener('shown.bs.modal', () => {
      chatBody.scrollTop = chatBody.scrollHeight;
    });
  });
  </script>
</body>
</html>