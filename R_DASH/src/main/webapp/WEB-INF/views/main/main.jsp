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
<meta charset="UTF-8" />
<title>재민이</title>
<link rel="icon" href="${CP}/resources/image/Jaemini_face.ico"
	type="image/x-icon" />
<link rel="stylesheet"
	href="${CP}/resources/template/Animate/backInUp.css" />

<style>
/* =========================
   Base (공통)
========================= */
:root {--topbar-h: 64px;}

html, body {
	margin: 0;
	padding: 0;
	width: 100%;
	scroll-behavior: smooth;
	color: #fff;
	font-family: 'Noto Sans KR', system-ui, -apple-system, 'Segoe UI',
		Roboto, sans-serif;
}

.main-background {
	background-image: url('${CP}/resources/image/mainboard.png');
	background-size: cover;
	background-position: center;
	background-repeat: no-repeat;
	background-attachment: fixed;
}

/* =========================
   Header (홈에서 투명)
========================= */
body.home-page header {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	height: var(- -topbar-h);
	background: transparent !important;
	box-shadow: none !important;
	z-index: 2000;
}

body.home-page header.scrolled {
	background: rgba(0, 0, 0, .35) !important;
	backdrop-filter: blur(4px);
	box-shadow: 0 2px 10px rgba(0, 0, 0, .2);
}

/* Argon 상단바(주황) 제거 + 고정 */
body.home-page .navbar.navbar-horizontal.bg-warning {
	background: transparent !important;
	box-shadow: none !important;
}

body.home-page .navbar.navbar-horizontal {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	height: var(- -topbar-h);
	z-index: 2000;
}

body.home-page .navbar.navbar-horizontal .navbar-nav .nav-link, body.home-page .navbar.navbar-horizontal .media-body span
	{
	color: #fff !important;
}

/* 사이드바는 원래 fixed 유지 + 계층만 올림 */
body.home-page #sidenav-main {
	position: fixed !important;
	z-index: 3001 !important;
}

/* 비디오/오버레이는 클릭 막지 않기 */
#bg-video, .main-section>video, .main-section::before {
	pointer-events: none;
}

/* =========================
   Hero(비디오 영역)
========================= */
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

.main-section.main-background {
	background: none;
}

#bg-video, .main-section>video {
	position: absolute;
	inset: 0;
	width: 100%;
	height: 100%;
	object-fit: cover;
	z-index: -1;
}

.main-section::before {
	content: "";
	position: absolute;
	inset: 0;
	background: rgba(0, 0, 0, .25);
	z-index: 0;
}

.hero-text {
	position: relative;
	z-index: 1;
	color: #fff;
	padding: 0 20px;
	max-width: 920px;
}

.hero-text h1 {
	font-size: clamp(2rem, 5vw, 2.8rem);
	font-weight: 800;
	margin: 0 0 12px;
	text-shadow: 2px 2px 8px rgba(0, 0, 0, .6);
}

.hero-text .white-text {
	color: #fff;
}

.hero-text .accent {
	color: #ff4d4d;
}

.hero-text p {
	font-size: clamp(1rem, 2vw, 1.2rem);
	line-height: 1.6;
	margin: 0;
	text-shadow: 0 2px 8px rgba(0, 0, 0, .5);
}

.hero-btn {
	display: inline-block;
	margin-top: 18px;
	padding: 12px 28px;
	border-radius: 28px;
	background: #ff6b6b;
	color: #fff;
	text-decoration: none;
	font-weight: 600;
	transition: .25s;
	box-shadow: 0 6px 18px rgba(0, 0, 0, .25);
}

.hero-btn:hover {
	background: #ff4757;
	transform: translateY(-2px);
}

/* 히어로 아래 첫 섹션이 가려지지 않도록 필요 시 부여 */
.after-hero {padding-top: var(--topbar-h);}

/* =========================
   News (섹션/카드)
========================= */
.news-section {
	padding: 60px 0;
	background: #212529; /* FAQ와 같은 배경 */
	color: #fff;
}

/* 카드: 흰색으로 통일 */
.news-section .card {
	background: #fff !important;
	color: #111 !important;
	border: 1px solid #e9ecef;
	border-radius: 12px;
	box-shadow: 0 6px 18px rgba(0, 0, 0, .12);
	transition: transform .18s, box-shadow .18s;
}

.news-section .card:hover {
	transform: translateY(-3px);
	box-shadow: 0 12px 28px rgba(0, 0, 0, .2);
}

.news-section .card .card-title, .news-section .card .card-title a,
	.news-section .card .card-text {
	color: #111 !important;
	text-decoration: none;
}

.news-section .card .card-company {
	color: #6b7280 !important;
}

.news-title {
	text-align: center;
	font-size: 1.8rem;
	font-weight: 700;
	margin-bottom: 30px;
	color: #fff;
	text-shadow: 1px 1px 3px #000;
}

/* 카드 이미지 */
img.card-img-top {
	width: 100%;
	height: 200px;
	object-fit: cover;
	border-top-left-radius: 12px;
	border-top-right-radius: 12px;
}

/* 뉴스 좌우 화살표 (카드 외부 알약 버튼) */
.news-rotator-wrap {
	position: relative;
	overflow: visible;
}

.nav-outer {
	position: absolute;
	top: 50%;
	transform: translateY(-50%);
	width: 18px;
	height: 72px;
	padding: 0;
	border: 1px solid rgba(0, 0, 0, .08);
	border-radius: 12px;
	background: rgba(255, 255, 255, .96);
	color: #111;
	font-size: 20px;
	font-weight: 900;
	line-height: 1;
	display: inline-flex;
	align-items: center;
	justify-content: center;
	cursor: pointer;
	z-index: 9;
	box-shadow: 0 6px 18px rgba(0, 0, 0, .22);
	transition: transform .12s, box-shadow .12s, background .12s, opacity
		.12s;
	opacity: .95;
}

.nav-outer.prev {
	left: -28px;
	padding-right: 2px;
}

.nav-outer.next {
	right: -28px;
	padding-left: 2px;
}

.nav-outer:hover {
	background: #fff;
	transform: translateY(-50%) scale(1.04);
	box-shadow: 0 10px 24px rgba(0, 0, 0, .28);
}

.nav-outer:focus {
	outline: 2px solid #94a3b8;
	outline-offset: 2px;
}

@media ( max-width :576px) {
	.nav-outer {
		height: 60px;
	}
	.nav-outer.prev {
		left: 6px;
	}
	.nav-outer.next {
		right: 6px;
	}
}

/* 내부 전환 페이드 */
.news-rotator .card-img-top {
	transition: opacity .18s;
}

.news-rotator.switching .card-img-top {
	opacity: .35;
}

/* =========================
   FAQ
========================= */
.faq-section {
	background: #212529;
	color: #fff;
	padding: 60px 20px;
}

.faq-section h4, .faq-section h4 a {
	color: #fff !important;
	text-decoration: none;
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

/* =========================
   Chat launcher & Modal
========================= */
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
	background: #fff;
	box-shadow: 0 6px 14px rgba(0, 0, 0, .35);
	transition: transform .18s, box-shadow .18s;
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
	overflow: visible;
	height: 68vh;
	max-height: 820px;
	display: flex;
	flex-direction: column;
	position: relative;
}

.chat-header {
	position: relative;
	z-index: 3;
	background: #dc3545;
	color: #fff;
	padding: 14px 18px;
	font-weight: 700;
	min-height: 56px;
	display: flex;
	align-items: center;
	justify-content: space-between;
}

.chat-head-actions {
	display: flex;
	align-items: center;
	gap: 8px;
}

.chat-min-btn {
	width: 28px;
	height: 28px;
	border: 0;
	border-radius: 6px;
	background: rgba(255, 255, 255, .25);
	color: #fff;
	font-weight: 900;
	line-height: 28px;
	display: inline-flex;
	align-items: center;
	justify-content: center;
	cursor: pointer;
	transition: background .12s;
}

.chat-min-btn:hover {
	background: rgba(255, 255, 255, .35);
}

/* 추가/수정 */
.chat-body{
  flex:1;
  min-height:0;
  overflow-y:auto;
  background:#f6f7f9;
  padding:14px;

  /* ↓ 새로 추가 */
  display:flex;
  flex-direction:column;
  gap:10px;
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

/* 사용자: 오른쪽 */
.chat-bubble.user{
  align-self:flex-end;     /* ← 오른쪽 정렬(핵심) */
  margin-left:auto;        /* ← 안전망(있어도 무방) */
  background:#ffdfe3;
  color:#222;
}

/* 봇: 왼쪽 */
.chat-bubble.bot{
  align-self:flex-start;   /* ← 왼쪽 정렬 */
  margin-right:auto;
  background:#fff;
}

.chat-time {
	display: block;
	margin-top: 2px;
	font-size: .78rem;
	opacity: .6;
}

/* =========================
   대화 기록 Dock
========================= */
.history-dock {
	position: absolute;
	left: 0;
	top: -12px;
	transform: translateY(-100%);
	width: 320px;
	max-height: 60vh;
	background: rgba(17, 24, 39, .98);
	color: #fff;
	border-radius: 12px;
	box-shadow: 0 10px 24px rgba(0, 0, 0, .35);
	display: flex;
	flex-direction: column;
	overflow: hidden;
	z-index: 2000;
	transition: max-height .18s ease-in-out;
}

.history-dock.collapsed {
	max-height: 46px;
}

.history-dock.below {
	left: 12px;
	right: 12px;
	top: 56px;
	transform: none;
	width: auto;
	border-radius: 0 0 12px 12px;
	z-index: 2;
}

.history-head {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 10px 12px;
	background: #111827;
	border-bottom: 1px solid rgba(255, 255, 255, .08);
	font-weight: 700;
	font-size: .95rem;
	cursor: pointer;
}

.history-toggle {
	border: 0;
	border-radius: 8px;
	width: 32px;
	height: 28px;
	background: #374151;
	color: #fff;
	cursor: pointer;
	transition: transform .18s;
}

.history-dock:not(.collapsed) .history-toggle {
	transform: rotate(180deg);
}

.history-body {
	overflow-y: auto;
	padding: 8px 6px;
	gap: 4px;
	display: flex;
	flex-direction: column;
}

.history-item {
	cursor: pointer;
	border-radius: 10px;
	padding: 8px 10px;
	transition: background .12s;
	position: relative;
	padding-right: 42px;
}

.history-item:hover {
	background: rgba(255, 255, 255, .08);
}

.history-item.active {
	background: rgba(220, 53, 69, .92);
}

.history-title {
	font-weight: 700;
	font-size: .9rem;
}

.history-sub {
	font-size: .78rem;
	color: #c7d2fe;
	opacity: .9;
	margin-top: 2px;
}

.history-time {
	font-size: .72rem;
	opacity: .6;
	margin-top: 2px;
}

.history-foot {
	padding: 8px;
	border-top: 1px solid rgba(255, 255, 255, .08);
}

.history-new {
	width: 100%;
	height: 34px;
	border: 0;
	border-radius: 8px;
	font-weight: 700;
	background: #0d6efd;
	color: #fff;
	cursor: pointer;
}

.history-del {
	position: absolute;
	right: 8px;
	top: 50%;
	transform: translateY(-50%);
	width: 28px;
	height: 28px;
	border: 0;
	border-radius: 6px;
	background: #374151;
	color: #fff;
	font-weight: 900;
	line-height: 28px;
	cursor: pointer;
	opacity: 0;
	transition: opacity .12s, background .12s;
}

.history-item:hover .history-del {
	opacity: .9;
}

.history-del:hover {
	background: #ef4444;
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
	.history-dock {
		width: 260px;
		top: -8px;
	}
	.history-dock.below {
		left: 8px;
		right: 8px;
		top: 52px;
	}
}

/* =========================
   News 제목/내비 Row
========================= */
.news-title-row {
	display: flex;
	align-items: center;
	gap: 8px;
}

.news-title-row.right-nav {
	justify-content: space-between;
}

.news-title-row.left-nav {
	justify-content: flex-start;
}

.news-title-row .card-title {
	margin: 0;
	flex: 1 1 auto;
	min-width: 0;
}

.news-title-row .news-navs {
	display: none;
}

/* =========================
   Animation
========================= */
@
keyframes backInUp{
from { opacity:0; transform: translate3d(0, 60px, 0) scale(.98);}
to {opacity: 1; transform: translate3d(0, 0, 0) scale(1);}
}
</style>
<!-- 서버에서 내려준 JSON을 실행되지 않는 데이터 블록으로 주입 -->
<script type="application/json" id="homeNewsJson">
  <c:out value="${homeNewsJson}" default="[]" escapeXml="false"/>
</script>
</head>

<body class="home-page"
	data-login-user-no="${empty loginUserNo ? '' : loginUserNo}">
	<!-- Hero -->
	<div class="main-section main-background">
		<video id="bg-video" autoplay muted loop preload="auto"
			poster="${CP}/resources/image/mainboard.png">
			<source src="${CP}/resources/video/jaemini.mp4" type="video/mp4" />
		</video>
		<!-- 소개 문구 -->
		<div class="hero-text">
			<h1>
				<span class="white-text">재난 알림 플랫폼</span> <span
					style="color: #ff6b6b;">R-DASH</span>
			</h1>
			<p>
				재민이가 재난 정보를 알려주고<br /> 상황별 행동 요령을 안내해 드립니다.<br /> 여러분의 안전을 지키는 든든한
				파트너가 되겠습니다.
			</p>
		</div>
	</div>

	<!-- News -->
	<div class="news-section main-background animate-on-scroll" id="news">
		<div class="container">
			<div class="news-title">주요 뉴스</div>
			<div class="row">
				<div class="col-md-4 mb-3">
					<div class="news-rotator-wrap">
						<!-- 위치 기준 -->
						<button class="nav-outer prev" aria-label="이전">‹</button>

						<div class="card h-100 news-rotator" data-feed="breaking"
							data-idx="0">
							<img src="${CP}/resources/image/earth.jpg" class="card-img-top"
								alt="지진 이미지">
							<div class="card-body">
								<h5 class="card-title">
									<a class="title-text title-link" href="#">기사 제목</a>
								</h5>
								<p class="card-text card-company no-nav">언론사</p>
							</div>
						</div>
					</div>
				</div>
				<div class="col-md-4 mb-3">
					<div class="news-rotator-wrap">
						<!-- 화살표는 없지만 래퍼는 유지해도 OK -->
						<div class="card h-100 news-rotator" data-feed="safety"
							data-idx="0">
							<img src="${CP}/resources/image/dis.jpg" class="card-img-top"
								alt="화재 이미지" />
							<div class="card-body">
								<h5 class="card-title">
									<a class="title-text title-link" href="#">기사 제목</a>
								</h5>
								<p class="card-text card-company no-nav">언론사</p>
							</div>
						</div>
					</div>
				</div>
				<div class="col-md-4 mb-3">
					<div class="news-rotator-wrap">
						<!-- 위치 기준 -->
						<div class="card h-100 news-rotator" data-feed="emergency"
							data-idx="0">
							<img src="${CP}/resources/image/med.jpg" class="card-img-top"
								alt="응급 이미지">
							<div class="card-body">
								<h5 class="card-title">
									<a class="title-text title-link" href="#">기사 제목</a>
								</h5>
								<p class="card-text card-company no-nav">언론사</p>
							</div>
						</div>

						<button class="nav-outer next" aria-label="다음">›</button>
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

	<!-- Launcher -->
	<a class="floating-icon" href="#" aria-label="도움말 또는 채팅 열기" title="재민이">
		<img src="${CP}/resources/image/Jaemini_bo.jpg" alt="재민이 아이콘" />
	</a>

	<!-- Chat Modal -->
	<div class="modal fade chat-modal" id="chatModal" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">

				<!-- 내 대화방 패널 -->
				<div id="historyDock" class="history-dock collapsed">
					<div id="historyHead" class="history-head">
						<span>내 대화방</span>
						<div style="display: flex; gap: 6px; align-items: center">
							<button id="historyToggle" class="history-toggle" title="내기록"
								aria-expanded="false" aria-controls="historyBody">▾</button>
						</div>
					</div>
					<div id="historyBody" class="history-body"></div>
					<div class="history-foot">
						<button id="historyNew" class="history-new">+ 새 대화</button>
					</div>
				</div>

				<div class="chat-header">
					<strong>재민이 채팅</strong>
					<div class="chat-head-actions">
						<button id="chatMin" type="button" class="chat-min-btn"
							title="out" aria-label="Minimize">–</button>
					</div>
				</div>
				<div id="chatBody" class="chat-body">
					<div class="chat-bubble bot">
						안녕하세요! 재난 알림 도우미 <b>재민이</b>입니다. 저는 재난 및 안전 정보를 제공할 수 있습니다. 기상 정보, 지진, 화재, 태풍 등 자연재해와 관련된 정보를 알려드릴 수 있으며, 안전 수칙이나 대처 방법도 안내해 드릴 수 있습니다. 궁금한 점이 있으면 말씀해 주세요. <span
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
  var CP='${CP}';  /* 전역 */

  document.addEventListener("DOMContentLoaded", function(){

    /* ========= 애니메이션 ========= */
    function initScrollAnimations(){
      try{
        const els = document.querySelectorAll('.animate-on-scroll:not(.animated)');
        const io = new IntersectionObserver((entries, obs)=>{
          entries.forEach(en=>{
            if(en.isIntersecting){
              en.target.classList.add('backInUp','animated');
              obs.unobserve(en.target);
            }
          });
        }, { threshold: 0.15 });
        els.forEach(el => io.observe(el));
        els.forEach(el=>{
          const r = el.getBoundingClientRect();
          if (r.top < window.innerHeight && r.bottom > 0) {
            el.classList.add('backInUp','animated');
          }
        });
      }catch(e){
        document.querySelectorAll('.animate-on-scroll').forEach(el=>{
          el.classList.add('backInUp','animated');
        });
      }
    }
    initScrollAnimations();

    /* ========= 뉴스 ========= */
    const HOME_NEWS = JSON.parse(
      document.getElementById('homeNewsJson')?.textContent || '[]'
    );

    const pickImg = (kw) => {
      if(!kw) return CP+'/resources/image/news_default.jpg';
      if(kw.includes('화재')) return CP+'/resources/image/dis.jpg';
      if(kw.includes('지진')) return CP+'/resources/image/earth.jpg';
      if(kw.includes('폭우') || kw.includes('홍수')) return CP+'/resources/image/rain.jpg';
      if(kw.includes('풍랑') || kw.includes('태풍')) return CP+'/resources/image/wave.jpg';
      return CP+'/resources/image/news_default.jpg';
    };

    const asList = Array.isArray(HOME_NEWS) ? HOME_NEWS : [];
    const mapItem = (n) => ({
      title: n.title || '(제목 없음)',
      text : n.company || '',
      img  : pickImg(n.keyword),
      href : n.url || '#'
    });

    let newsData = {
      breaking : asList.slice(0,3).map(mapItem),
      safety   : asList.slice(3,6).map(mapItem),
      emergency: asList.slice(6,9).map(mapItem)
    };

    const normIndex = (i,len)=>{ i%=len; return i<0? i+len : i; };

    function renderNewsCard(card){
      const feed = card.dataset.feed;
      const list = newsData[feed] || [];
      if(!list.length) return;
      const idx = normIndex(parseInt(card.dataset.idx||'0',10), list.length);
      card.dataset.idx = idx;

      const item = list[idx];
      const img   = card.querySelector('.card-img-top');
      const title = card.querySelector('.title-text');
      const desc  = card.querySelector('.card-text');

      if(img){ img.src = item.img; img.alt = item.title; }
      if(title){
        title.textContent = item.title || '(제목 없음)';
        if (title.tagName === 'A') {
          title.href = item.href || '#';
          title.setAttribute('aria-label', item.title || '');
          title.target = '_blank';
          title.rel = 'noopener';
        }
      }
      if(desc){
        desc.textContent = item.text || '';
        desc.classList.add('card-company');
      }
    }

    function stepCard(card, delta){
      const feed = card.dataset.feed;
      const len  = (newsData[feed]||[]).length;
      if(!len) return;
      const next = normIndex((parseInt(card.dataset.idx||'0',10) + delta), len);
      card.dataset.idx = next;
      card.classList.add('switching');
      setTimeout(()=>{ renderNewsCard(card); card.classList.remove('switching'); }, 80);
    }

    function stepAll(delta){
      document.querySelectorAll('.news-rotator').forEach((c)=> stepCard(c, delta));
    }

    function initRotators(){
      const cards = document.querySelectorAll('.news-rotator');
      cards.forEach((card, i) => {
        if(!card.dataset.feed){
          const feeds = ['breaking','safety','emergency'];
          card.dataset.feed = feeds[i] || 'breaking';
        }
        card.dataset.idx = card.dataset.idx || '0';

        const wrap = card.closest('.news-rotator-wrap') || card.closest('.news-col') || card.parentElement;
        const prev = (wrap && wrap.querySelector('.nav-outer.prev')) || card.querySelector('.news-nav.prev');
        const next = (wrap && wrap.querySelector('.nav-outer.next')) || card.querySelector('.news-nav.next');

        if (prev) prev.addEventListener('click', (e)=>{ e.preventDefault(); e.stopPropagation(); stepAll(-1); });
        if (next) next.addEventListener('click', (e)=>{ e.preventDefault(); e.stopPropagation(); stepAll(+1); });

        renderNewsCard(card);
      });
    }

    initRotators();
    window.dispatchEvent(new Event('scroll'));
  });

  /* ========= 챗봇 ========= */
  var chatModalEl = document.getElementById('chatModal');
  var chatModal   = null;

  function ensureModal(){
    try{
      if (chatModal) return true;
      if (!chatModalEl) return false;
      if (!window.bootstrap || !bootstrap.Modal) {
        console.warn('bootstrap 모달이 아직 준비되지 않았습니다.');
        return false;
      }
      chatModal = new bootstrap.Modal(chatModalEl, { backdrop:'static', keyboard:true });
      return true;
    }catch(e){
      console.error('Modal 초기화 오류:', e);
      return false;
    }
  }

  var chatBody   = document.getElementById('chatBody');
  var chatInput  = document.getElementById('chatInput');
  var chatMinBtn = document.getElementById('chatMin');

  /* 인사 메시지 (빈 방일 때만 1회 표시) */
  function nowText(){ return new Date().toLocaleTimeString([], {hour:'2-digit',minute:'2-digit'}); }
  function ensureGreeting(){
    if(!chatBody) return;
    if(chatBody.querySelector('.jm-greeting')) return;
    var d=document.createElement('div');
    d.className='chat-bubble bot jm-greeting';
    d.innerHTML='안녕하세요! 재난 알림 도우미 <b>재민이</b>입니다. 저는 재난 및 안전 정보를 제공할 수 있습니다. 기상 정보, 지진, 화재, 태풍 등 자연재해와 관련된 정보를 알려드릴 수 있으며, 안전 수칙이나 대처 방법도 안내해 드릴 수 있습니다. 궁금한 점이 있으면 말씀해 주세요.'
                +'<span class="chat-time">'+nowText()+'</span>';
    chatBody.appendChild(d);
    chatBody.scrollTop=chatBody.scrollHeight;
  }

  function escapeHtml(s){ return (s||'').replace(/[&<>\"']/g,function(c){ return {'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c];}); }
  function appendBubble(text,who){
    if(!chatBody) return;
    var d=document.createElement('div');
    d.className='chat-bubble '+(who==='bot'?'bot':'user');
    d.innerHTML=text+'<span class="chat-time">'+nowText()+'</span>';
    chatBody.appendChild(d); chatBody.scrollTop=chatBody.scrollHeight;
  }

  /* login → 네임스페이스 */
  var LOGIN_USER_NO_RAW=(document.body.dataset.loginUserNo||'').trim();
  var LOGIN_USER_NO=LOGIN_USER_NO_RAW===''?null:parseInt(LOGIN_USER_NO_RAW,10);
  var NS = (LOGIN_USER_NO!=null) ? ('U:'+LOGIN_USER_NO) : 'GUEST';

  /* 오너/NS 유틸 */
  function nsKey(k){ return NS+'::'+k; }
  function ownerKey(sid){ return 'JM_OWNER_'+sid; }
  function setOwner(sid, owner){ try{ localStorage.setItem(ownerKey(sid), owner); }catch(_){ } }
  function getOwner(sid){ try{ return localStorage.getItem(ownerKey(sid)); }catch(_){ return null; } }

  var SESSION_ID = null;

  var LAST_NS = localStorage.getItem('JM_LAST_NS');
  if(LAST_NS !== NS){
    localStorage.removeItem(nsKey('X-Session-Id'));
    SESSION_ID = null;
    chatBody && (chatBody.innerHTML='');
  }
  localStorage.setItem('JM_LAST_NS', NS);

  function previewOf(t){ t=(String(t||'')).replace(/\s+/g,' ').trim(); return t.length>40?t.slice(0,40)+'…':t; }

  if(SESSION_ID===null){
    SESSION_ID = localStorage.getItem(nsKey('X-Session-Id'))||null;
  }
  function getLocalSessions(){ try{ return JSON.parse(localStorage.getItem(nsKey('JM_SESSIONS'))||'[]'); }catch(e){ return []; } }
  function setLocalSessions(arr){ localStorage.setItem(nsKey('JM_SESSIONS'), JSON.stringify((arr||[]).slice(0,50))); }
  function addLocalSession(sid){
    if(!sid) return;
    var owner=getOwner(sid);
    if(owner && owner!==NS) return;
    if(!owner) setOwner(sid, NS);
    var list=getLocalSessions().filter(function(x){ return x!==sid; });
    list.unshift(sid);
    setLocalSessions(list);
  }
  function _msgsKey(sid){ return nsKey('JM_MSGS_'+sid); }
  function getLocalMsgs(sid){ if(!sid)return[]; try{ return JSON.parse(localStorage.getItem(_msgsKey(sid))||'[]'); }catch(e){ return []; } }
  function setLocalMsgs(sid,a){ if(!sid)return; localStorage.setItem(_msgsKey(sid),JSON.stringify((a||[]).slice(0,500))); }
  function pushLocalMsg(sid,role,text){ if(!sid)return; var arr=getLocalMsgs(sid); arr.push({role:role,text:String(text||''),ts:Date.now()}); setLocalMsgs(sid,arr); }
  function migrateCache(oldSid,newSid){
    if(!oldSid||!newSid||oldSid===newSid)return;
    var o=getLocalMsgs(oldSid), n=getLocalMsgs(newSid);
    if(o.length&&!n.length) setLocalMsgs(newSid,o);
    localStorage.removeItem(_msgsKey(oldSid));
    setOwner(newSid, NS);
  }

  (function cleanupStrays(){
    try{
      var list=getLocalSessions();
      var cleaned=list.filter(function(sid){ return getOwner(sid) === NS; });
      if(cleaned.length!==list.length) setLocalSessions(cleaned);
      var cur=localStorage.getItem(nsKey('X-Session-Id'));
      if(cur && getOwner(cur)!==NS){
        localStorage.removeItem(nsKey('X-Session-Id'));
        SESSION_ID=null;
        chatBody && (chatBody.innerHTML='');
      }
    }catch(_){}
  })();

  var dockEl=document.getElementById('historyDock');
  var dockHead=document.getElementById('historyHead');
  var dockBody=document.getElementById('historyBody');
  var dockTgl=document.getElementById('historyToggle');
  var dockNew=document.getElementById('historyNew');

  function applyBodyPadding(){
    if(!dockEl || !chatBody) return;
    var isBelow=dockEl.classList.contains('below') && !dockEl.classList.contains('collapsed');
    var h=isBelow ? Math.min(dockEl.scrollHeight, window.innerHeight*0.6) : 0;
    chatBody.style.paddingTop=isBelow ? (h+'px') : '0px';
  }
  function setDockBelow(on){ if(!dockEl) return; dockEl.classList.toggle('below', !!on); applyBodyPadding(); }
  window.addEventListener('resize', applyBodyPadding);

  (function protectDock(){
    var events=['click','mousedown','mouseup','touchstart','touchend','pointerdown','pointerup','wheel'];
    function stopAll(e){ e.stopPropagation(); }
    [dockEl,dockBody,dockTgl,dockNew].forEach(function(el){ if(!el)return; events.forEach(function(ev){ el.addEventListener(ev,stopAll,false); }); });
  })();

  function toggleDock(){
    if(!dockEl)return;
    var collapsed=dockEl.classList.toggle('collapsed');
    if(dockTgl){ dockTgl.setAttribute('aria-expanded', String(!collapsed)); }
    if(!collapsed){ setDockBelow(true); } else { setDockBelow(false); }
  }
  if(dockHead){
    dockHead.addEventListener('click',function(e){ e.stopPropagation(); toggleDock(); });
    dockHead.tabIndex=0;
    dockHead.addEventListener('keydown',function(e){ if(e.key==='Enter'||e.key===' '){ e.preventDefault(); toggleDock(); } });
  }
  if(dockTgl){ dockTgl.addEventListener('click',function(e){ e.stopPropagation(); toggleDock(); }); }

  async function openInPopup(){
    let sid;
    try{
      const r=await fetch(CP+'/api/chat/sessions/new',{method:'POST', headers: (LOGIN_USER_NO!=null?{'X-User-No':LOGIN_USER_NO}:{})});
      sid=(await r.text()) || (crypto.randomUUID?crypto.randomUUID():String(Date.now()));
    }catch(_){
      sid=(crypto.randomUUID?crypto.randomUUID():String(Date.now()));
    }
    setOwner(sid, NS);
    addLocalSession(sid);
    const base=location.origin+location.pathname;
    const url =base+'?popup=1&sid='+encodeURIComponent(sid);
    const win =window.open(url,'jaemini_'+sid,'width=420,height=720,resizable=yes,scrollbars=yes');
    if(!win){ location.href=url; }
  }

  function removeLocalSession(sid){
    if(!sid) return;
    var list=getLocalSessions().filter(function(x){ return x!==sid; });
    setLocalSessions(list);
    localStorage.removeItem(_msgsKey(sid));
  }
  async function deleteSession(sid){
    if(!sid) return;
    if(LOGIN_USER_NO!=null){
      try{
        const res = await fetch(CP+'/api/chat/sessions/'+encodeURIComponent(sid), {
          method:'DELETE',
          headers:{ 'X-User-No': LOGIN_USER_NO }
        });
        if(!res.ok){ console.warn('delete api failed', res.status); }
      }catch(e){ console.warn('delete api error', e); }
    }
    removeLocalSession(sid);
    if(SESSION_ID===sid){
      const rest=getLocalSessions();
      SESSION_ID = rest[0] || null;
      if(SESSION_ID){
        localStorage.setItem(nsKey('X-Session-Id'),SESSION_ID);
        await openDockRoom(SESSION_ID);
      }else{
        localStorage.removeItem(nsKey('X-Session-Id'));
        chatBody && (chatBody.innerHTML='');
        ensureGreeting(); // 방 자체가 없으면 인사 유지
      }
    }
    await loadDockSessions();
    applyBodyPadding();
  }

  function renderDock(list){
    if(!dockBody) return;
    dockBody.innerHTML='';
    if(!list||!list.length){
      var emp=document.createElement('div');
      emp.style.cssText='padding:10px;color:#cfd6e3;font-size:.86rem;';
      emp.textContent='저장된 대화가 없습니다.';
      dockBody.appendChild(emp);
      applyBodyPadding(); return;
    }
    list.forEach(function(s){
      var cached=getLocalMsgs(s.sessionId);
      if((!s.lastMsg||s.lastMsg==='')&&cached.length){ s.lastMsg=previewOf(cached[cached.length-1].text); }

      var it=document.createElement('div');
      it.className='history-item'+(s.sessionId===SESSION_ID?' active':'');
      it.dataset.sid=s.sessionId;
      it.innerHTML =
        '<div class="history-title">'+escapeHtml(s.title||'새 대화')+'</div>'
       +'<div class="history-sub">'+escapeHtml(s.lastMsg||'')+'</div>'
       +'<div class="history-time">'+escapeHtml(s.updatedAt||'')+'</div>'
       +'<button class="history-del" title="삭제" aria-label="대화 삭제" data-del="'+s.sessionId+'">×</button>';

      it.addEventListener('click',function(e){
        if(e.target && e.target.classList.contains('history-del')) return;
        e.stopPropagation();
        SESSION_ID=s.sessionId; localStorage.setItem(nsKey('X-Session-Id'),SESSION_ID);
        openDockRoom(SESSION_ID).then(function(){
          Array.prototype.forEach.call(document.querySelectorAll('.history-item'),function(x){
            x.classList.toggle('active',x.dataset.sid===SESSION_ID);
          });
          dockEl.classList.add('collapsed');
        });
      });

      it.querySelector('.history-del').addEventListener('click', function(e){
        e.stopPropagation();
        var sid=e.currentTarget.getAttribute('data-del');
        if(confirm('이 대화를 삭제할까요?')){ deleteSession(sid); }
      });

      dockBody.appendChild(it);
    });
    applyBodyPadding();
  }

  async function loadDockSessions(){
    if(!dockBody)return;

    if(LOGIN_USER_NO!=null){
      try{
        var res=await fetch(CP+'/api/chat/sessions?limit=100',{headers:{'X-User-No':LOGIN_USER_NO}});
        if(res.ok){
          var all=await res.json();
          all = Array.isArray(all) ? all : [];
          all.forEach(function(s){ if(!getOwner(s.sessionId)) setOwner(s.sessionId, NS); });
          var list = all.filter(function(s){ return getOwner(s.sessionId) === NS; });
          renderDock(list.map(function(s){
            return {sessionId:s.sessionId,title:s.title||'새 대화',lastMsg:s.lastMsg||'',updatedAt:s.updatedAt||''};
          }));
          return;
        }
      }catch(e){ console.warn('sessions api fallback(local)',e); }
    }
    var ids=getLocalSessions();
    renderDock(ids.map(function(id){ return {sessionId:id,title:'내 대화',lastMsg:'',updatedAt:''}; }));
  }

  function renderRowsAndCache(sid,rows){
    if(!Array.isArray(rows)||!rows.length) return false;
    chatBody.innerHTML='';
    var fresh=[];
    rows.forEach(function(m){
      if(m.question && String(m.question).trim()!==''){
        var q=String(m.question);
        appendBubble(escapeHtml(q).replace(/\n/g,'<br>'),'user');
        fresh.push({role:'user',text:q,ts:Date.now()});
      }
      if(m.answer && String(m.answer).trim()!==''){
        var a=String(m.answer);
        appendBubble(escapeHtml(a).replace(/\n/g,'<br>'),'bot');
        fresh.push({role:'bot',text:a,ts:Date.now()});
      }
      if(!m.question && !m.answer && m.text){
        var role=(m.role==='bot'||m.role==='assistant')?'bot':'user';
        var t=String(m.text);
        appendBubble(escapeHtml(t).replace(/\n/g,'<br>'),role);
        fresh.push({role:role,text:t,ts:Date.now()});
      }
    });
    chatBody.scrollTop=chatBody.scrollHeight;
    setLocalMsgs(sid,fresh);
    return true;
  }

  /* ★ 여기서 기록 없으면 인사! */
  async function openDockRoom(sid){
    if(!sid) { chatBody && (chatBody.innerHTML=''); ensureGreeting(); return; }

    if (LOGIN_USER_NO != null) {
      var o = getOwner(sid);
      if (o && o !== NS) { chatBody.innerHTML=''; ensureGreeting(); return; }
    }

    chatBody.innerHTML='';
    if(LOGIN_USER_NO!=null){
      try{
        var res=await fetch(CP+'/api/chat/sessions/'+encodeURIComponent(sid)+'/messages?limit=200',
                            {headers:{'X-User-No':LOGIN_USER_NO}});
        if(res.ok){
          var rows=await res.json();
          var hasAny = renderRowsAndCache(sid,rows);
          if(!hasAny) ensureGreeting();
        }else{
          ensureGreeting();
        }
      }catch(e){ console.warn('member history load failed',e); ensureGreeting(); }
    }else{
      var cached=getLocalMsgs(sid);
      if(cached.length){
        cached.forEach(function(m){
          appendBubble(escapeHtml(m.text).replace(/\n/g,'<br>'), m.role==='bot'?'bot':'user');
        });
        chatBody.scrollTop=chatBody.scrollHeight;
      }
      try{
        var res2=await fetch(CP+'/api/chat/history?limit=200',{headers:{'X-Session-Id':sid}});
        if(res2.ok){
          var rows2=await res2.json();
          var hasAny = renderRowsAndCache(sid,rows2);
          if(!hasAny && cached.length===0) ensureGreeting();
        }else if(cached.length===0){ ensureGreeting(); }
      }catch(err){ console.error(err); if(cached.length===0) ensureGreeting(); }
    }

    applyBodyPadding();
  }

  async function ensureNewSidForNS(){
    try{
      var r=await fetch(CP+'/api/chat/sessions/new',{method:'POST', headers: (LOGIN_USER_NO!=null?{'X-User-No':LOGIN_USER_NO}:{})});
      var sid=(await r.text())||crypto.randomUUID();
      SESSION_ID=sid; setOwner(SESSION_ID, NS);
      localStorage.setItem(nsKey('X-Session-Id'),SESSION_ID);
      addLocalSession(SESSION_ID);
    }catch(_){
      SESSION_ID=crypto.randomUUID();
      setOwner(SESSION_ID, NS);
      localStorage.setItem(nsKey('X-Session-Id'),SESSION_ID);
      addLocalSession(SESSION_ID);
    }
  }

  if (dockNew) {
    dockNew.addEventListener('click', async function (e) {
      e.stopPropagation();
      if (e.altKey) { e.preventDefault(); return openInPopup(); }
      await ensureNewSidForNS();
      await loadDockSessions();
      await openDockRoom(SESSION_ID);
      dockEl.classList.remove('collapsed');
      setDockBelow(true);
    });
    dockNew.addEventListener('mouseup', function (e) {
      if (e.button === 1) { e.preventDefault(); e.stopPropagation(); openInPopup(); }
    });
  }

  async function sendMessage(){
    var text=document.getElementById('chatInput').value.trim(); if(!text) return;

    if(!SESSION_ID || (getOwner(SESSION_ID) && getOwner(SESSION_ID)!==NS)){
      await ensureNewSidForNS();
    }

    appendBubble(escapeHtml(text).replace(/\n/g,'<br>'),'user');
    pushLocalMsg(SESSION_ID,'user',text);
    document.getElementById('chatInput').value='';

    try{
      var headers={'Content-Type':'application/json'};
      if(SESSION_ID && getOwner(SESSION_ID)===NS){ headers['X-Session-Id']=SESSION_ID; }
      if(LOGIN_USER_NO!=null) headers['X-User-No']=LOGIN_USER_NO;

      var prevSid=SESSION_ID;
      var res=await fetch(CP+'/api/chat/send',{
        method:'POST', headers:headers, body:JSON.stringify({question:text,userNo:LOGIN_USER_NO||undefined})
      });

      var sid=res.headers.get('X-Session-Id');
      if(sid){
        var owner=getOwner(sid);
        if(!owner || owner===NS){
          if(!owner) setOwner(sid, NS);
          if(sid!==SESSION_ID){ migrateCache(prevSid,sid); SESSION_ID=sid; }
          localStorage.setItem(nsKey('X-Session-Id'),SESSION_ID);
          addLocalSession(SESSION_ID);
        }
      }

      var raw=await res.text(); var data=null; try{ data=JSON.parse(raw);}catch(e){}
      if(!res.ok){
        var err='AI 호출 실패('+res.status+') '+(raw||'');
        appendBubble(escapeHtml(err),'bot'); pushLocalMsg(SESSION_ID,'bot',err); console.error('chat/send error',res.status,raw); return;
      }
      var answer=(data&&data.answer)?String(data.answer):'응답을 불러오지 못했어요.';
      appendBubble(escapeHtml(answer).replace(/\n/g,'<br>'),'bot'); pushLocalMsg(SESSION_ID,'bot',answer);
      loadDockSessions();
    }catch(e){
      var net='네트워크 오류가 발생했어요. 연결 상태를 확인해 주세요.';
      appendBubble(escapeHtml(net),'bot'); pushLocalMsg(SESSION_ID,'bot',net); console.error(e);
    }
  }
  document.getElementById('chatSend').addEventListener('click',sendMessage);
  document.getElementById('chatInput').addEventListener('keydown',function(e){ if(e.key==='Enter'&&!e.shiftKey){ e.preventDefault(); sendMessage(); } });

  chatModalEl.addEventListener('shown.bs.modal', async function(){
    if (!ensureModal()) return;
    chatBody.scrollTop=chatBody.scrollHeight;
    await loadDockSessions();
    if(SESSION_ID) await openDockRoom(SESSION_ID);
    if(!chatBody.children.length) ensureGreeting(); // ★ 혹시라도 비어 있으면 인사
    setDockBelow(true);
  });

  if(chatMinBtn){
    chatMinBtn.addEventListener('click', function(e){
      e.preventDefault();
      if (!ensureModal()) return;
      chatModal.hide();
    });
  }

  var iconEl=document.querySelector('.floating-icon');
  if(iconEl) iconEl.addEventListener('click',function(e){
    e.preventDefault();
    if (!ensureModal()) return;
    chatModal.show();
    setTimeout(function(){ chatInput && chatInput.focus(); },200);
  });

  (async function bootChatInline(){
	  if (!chatBody) return;

	  try {
	    await loadDockSessions();                 // 좌측/하단 방 목록 로드
	    if (SESSION_ID) {
	      await openDockRoom(SESSION_ID);         // 현재 세션 방 열기
	    }
	  } catch (e) {
	    console.warn('bootChatInline error', e);
	  } finally {
	    // 기록이 하나도 없으면 인사 메시지 1회 삽입
	    if (!chatBody.children || chatBody.children.length === 0) {
	      ensureGreeting();
	    }
	  }
	})();
  
  (async function initPopupIfNeeded(){
    const params=new URLSearchParams(location.search);
    const isPopup=params.get('popup')==='1';
    const sidParam=params.get('sid');
    if(!isPopup) return;

    if (!ensureModal()) return;
    chatModal.show();
    setTimeout(function(){ chatInput && chatInput.focus(); },200);
    if(sidParam){
      SESSION_ID=sidParam; setOwner(SESSION_ID, NS);
      localStorage.setItem(nsKey('X-Session-Id'),SESSION_ID); addLocalSession(SESSION_ID);
      try{ await openDockRoom(SESSION_ID); }catch(_){ ensureGreeting(); }
    }else{
      await ensureNewSidForNS();
      await openDockRoom(SESSION_ID);
    }
    setDockBelow(true);
  })();
</script>
</body>
</html>