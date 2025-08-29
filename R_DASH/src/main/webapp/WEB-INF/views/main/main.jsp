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
:root { --topbar-h: 64px;}

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

/* =========================================================
   HOME (Header 투명 + Sidebar 최상단 + 오버레이 무력화 + 가독성 복구)
========================================================= */

/* 공통 변수 */
:root {
  --topbar-h: 64px;
  --z-header: 2000;
  --z-sidebar: 99999;
}

/* =========================
   Header (홈에서 투명)
========================= */
body.home-page header {
  position: fixed;
  top: 0; left: 0; right: 0;
  height: var(--topbar-h);
  background: transparent !important;
  box-shadow: none !important;
  z-index: var(--z-header) !important;
}
body.home-page header.scrolled {
  background: rgba(0,0,0,.35) !important;
  backdrop-filter: blur(4px);
  box-shadow: 0 2px 10px rgba(0,0,0,.2);
}

/* Argon 상단바 투명 고정 */
body.home-page .navbar.navbar-top.bg-warning,
body.home-page .navbar.navbar-horizontal.bg-warning {
  background: transparent !important;
  box-shadow: none !important;
  border-bottom: 0 !important;
}
body.home-page .navbar.navbar-top,
body.home-page .navbar.navbar-horizontal {
  position: fixed;
  top: 0; left: 0; right: 0;
  height: var(--topbar-h);
  z-index: var(--z-header) !important; /* 사이드바보다 낮음 */
}

/* 헤더 글자 흰색 */
body.home-page .navbar .navbar-nav .nav-link,
body.home-page .navbar .media-body span {
  color: #fff !important;
}

/* 토글은 항상 클릭 가능 */
body.home-page .sidenav-toggler,
body.home-page .sidenav-toggler * {
  pointer-events: auto !important;
}

/* =========================
   Sidebar (항상 최상단 + 가독성 복구)
========================= */
body.home-page #sidenav-main {
  position: fixed !important;
  top: 0; bottom: 0; left: 0;
  z-index: var(--z-sidebar) !important;     /* 무엇보다 위 */
  pointer-events: auto !important;
  will-change: transform;                   /* 토글 성능 */
  background: #fff !important;              /* Argon 기본 */
  color: #344767 !important;                /* Argon 기본 텍스트 */
}

/* 사이드바 내부 텍스트/링크 기본색 복구 */
body.home-page #sidenav-main .navbar-brand,
body.home-page #sidenav-main .nav-link,
body.home-page #sidenav-main .nav-link .nav-link-text,
body.home-page #sidenav-main .sidenav-normal,
body.home-page #sidenav-main .sidenav-mini-icon,
body.home-page #sidenav-main .dropdown-item,
body.home-page #sidenav-main .form-control,
body.home-page #sidenav-main select {
  color: #344767 !important;
}

/* 아이콘은 원래 색 유지(Primary 등) / 필요 시 톤 다운 */
body.home-page #sidenav-main .nav-link i {
  opacity: .9;
}

/* hover/active 상태 가독성 */
body.home-page #sidenav-main .nav-link:hover {
  background: #f6f9fc !important;
  color: #111827 !important;
}
body.home-page #sidenav-main .nav-link.active,
body.home-page #sidenav-main .nav-link[aria-expanded="true"] {
  background: #eef3ff !important;
  color: #111827 !important;
}

/* collapse 안쪽 링크도 동일 팔레트 */
body.home-page #sidenav-main .nav .nav-link {
  color: #344767 !important;
}

/* 언어 셀렉트 드롭다운 가독성 */
body.home-page #sidenav-main .form-control,
body.home-page #sidenav-main select {
  background: #fff;
  border: 1px solid #e9ecef;
}

/* 사이드바 내부는 모두 클릭 가능 */
body.home-page #sidenav-main * {
  pointer-events: auto !important;
}

/* =========================
   메인 영역/패널
========================= */
body.home-page .main-content,
body.home-page #panel {
  position: relative;
  z-index: 0 !important; /* 사이드바 뒤 */
}

/* =========================
   오버레이/백드롭 무력화
========================= */
body.home-page .modal-backdrop,
body.home-page .sidenav-backdrop,
body.home-page .sidenav-mask,
body.home-page .backdrop,
body.home-page .mask,
body.home-page .page-overlay {
  pointer-events: none !important;
  background: transparent !important;
  z-index: -1 !important;
}

/* =========================
   배경(히어로/비디오) 클릭 차단
========================= */
body.home-page .main-section,
body.home-page #bg-video,
body.home-page .main-section > video,
body.home-page .main-section::before {
  pointer-events: none !important; /* 배경은 클릭 금지 */
  z-index: 0 !important;
}

/* 히어로/상단바/플로팅 버튼만 다시 클릭 허용 */
body.home-page .hero-text,
body.home-page .navbar,
body.home-page .floating-icon {
  pointer-events: auto !important;
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
  z-index: 0 !important;
  pointer-events: none;
}
.main-section.main-background { background: none; }

/* 4) 비디오/오버레이는 기본적으로 클릭 막기(겹침 방지) */
.main-section,
#bg-video,
.main-section > video,
.main-section::before{
  pointer-events: none !important;
  z-index: 0 !important;
}

#bg-video, .main-section > video {
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
  background: rgba(0,0,0,.25);
  z-index: 0;
}

/* 히어로 텍스트만 클릭 가능 */
.hero-text {
  position: relative;
  z-index: 1;
  color: #fff;
  padding: 0 20px;
  max-width: 920px;
  pointer-events: auto;
}
.hero-text h1 {
  font-size: clamp(2rem, 5vw, 2.8rem);
  font-weight: 800;
  margin: 0 0 12px;
  text-shadow: 2px 2px 8px rgba(0,0,0,.6);
}
.hero-text .white-text { color: #fff; }
.hero-text .accent { color: #ff4d4d; }
.hero-text p {
  font-size: clamp(1rem, 2vw, 1.2rem);
  line-height: 1.6;
  margin: 0;
  text-shadow: 0 2px 8px rgba(0,0,0,.5);
}
/* 5) 실제로 눌러야 하는 것들은 다시 클릭 가능 */
.hero-text,
body.home-page .navbar,
.floating-icon{
  pointer-events: auto !important;
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
  box-shadow: 0 6px 18px rgba(0,0,0,.25);
}

.hero-btn:hover {
  background: #ff4757;
  transform: translateY(-2px);
}

/* 히어로 아래 첫 섹션이 가리지 않도록 */
.after-hero { padding-top: var(--topbar-h); }

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
.chat-body {
	flex: 1;
	min-height: 0;
	overflow-y: auto;
	background: #f6f7f9;
	padding: 14px;
	/* ↓ 새로 추가 */
	display: flex;
	flex-direction: column;
	gap: 10px;
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
.chat-bubble.user {
	align-self: flex-end; /* ← 오른쪽 정렬(핵심) */
	margin-left: auto; /* ← 안전망(있어도 무방) */
	background: #ffdfe3;
	color: #222;
}

/* 봇: 왼쪽 */
.chat-bubble.bot {
	align-self: flex-start; /* ← 왼쪽 정렬 */
	margin-right: auto;
	background: #fff;
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
keyframes backInUp {
from { opacity:0; transform: translate3d(0, 60px, 0) scale(.98);}
to {opacity: 1;	transform: translate3d(0, 0, 0) scale(1);}
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
						안녕하세요! 재난 알림 도우미 <b>재민이</b>입니다. 저는 재난 및 안전 정보를 제공할 수 있습니다. 기상 정보,
						지진, 화재, 태풍 등 자연재해와 관련된 정보를 알려드릴 수 있으며, 안전 수칙이나 대처 방법도 안내해 드릴 수
						있습니다. 궁금한 점이 있으면 말씀해 주세요. <span class="chat-time"></span>
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
    let HOME_NEWS = [];
    try {
      HOME_NEWS = JSON.parse(
        document.getElementById('homeNewsJson')?.textContent || '[]'
      );
    } catch (e) {
      console.warn('homeNewsJson 파싱 오류:', e);
      HOME_NEWS = [];
    }

    const DEFAULT_IMG = CP + '/resources/image/new.png';
    
 // 상대경로를 절대경로로

    function absolutize(u){
      if (!u) return '';
      u = String(u).trim();
      if (/^(https?:|data:|blob:)/i.test(u)) return u;      // 외부/데이터 URL
      if (u.startsWith('//')) return location.protocol + u; // //example.com/...
      if (u.startsWith('/'))  return CP + u;                // /resources/...
      return CP + '/' + u.replace(/^\.?\//,'');             // resources/... or ./...
    }
    function looksLikeImgUrl(v){
    	  if (typeof v !== 'string') return false;
    	  const s = v.trim();
    	  return /^data:image\//i.test(s) ||
    	         /\.(png|jpe?g|gif|webp|svg)(\?.*)?$/i.test(s) ||
    	         /^https?:\/\//i.test(s) || s.startsWith('/') || s.startsWith('./') || s.startsWith('../');
    	}

    	function getProvidedImageDeep(obj){
    	  if (!obj || typeof obj !== 'object') return '';

    	  // (a) 키 이름 힌트 우선
    	  for (const k of Object.keys(obj)) {
    	    const lk = k.toLowerCase();
    	    if (/(thumb|image|img|thumbnail|picture|photo|url)$/.test(lk)) {
    	      const v = obj[k];
    	      if (typeof v === 'string' && looksLikeImgUrl(v)) return v;
    	      if (Array.isArray(v)) for (const x of v) if (looksLikeImgUrl(x)) return x;
    	    }
    	  }
    	  // (b) 값 자체가 이미지처럼 보이면
    	  for (const v of Object.values(obj)) {
    	    if (typeof v === 'string' && looksLikeImgUrl(v)) return v;
    	  }
    	  // (c) 배열/중첩 객체 재귀
    	  for (const v of Object.values(obj)) {
    	    if (Array.isArray(v)) {
    	      for (const x of v) { const r = getProvidedImageDeep(x); if (r) return r; }
    	    } else if (v && typeof v === 'object') {
    	      const r = getProvidedImageDeep(v); if (r) return r;
    	    }
    	  }
    	  return '';
    	}

    // 여러 후보 필드 중 첫 번째 썸네일 값 꺼내기
    function getProvidedImage(n){
      return n?.thumb || n?.image || n?.img || n?.imageUrl
          || n?.thumbnail || n?.thumbnailUrl || n?.thumbUrl || n?.imgUrl || '';
    }

    function pickImg(raw) {
    	  const t = String(raw || '');

    	  // 보험/재무 문맥: '화재'가 회사명/보험 기사일 때
    	  const insuranceCtx = /(삼성화재|메리츠화재|흥국화재|현대해상|DB손해|DB손해보험|KB손해|KB손해보험|한화손해|롯데손해|캐롯|농협손해|손해보험|보험|보험료|보험금|실손|실손보험|주가|실적|영업이익|순이익|배당)/i.test(t);
    	  // 실제 화재 상황을 암시하는 단어들
    	  const fireCtx = /(산불|불길|화염|연기|대피|진화|사망|사상|인명|피해|참사|소방|방화|폭발|누전|발화|화마|전소|불이|불로|화재 발생|화재로|화재 진압|화재 진화)/.test(t);

    	  // 🔥 화재: '산불'은 항상, '화재'는 문맥 있을 때만 (보험/재무 문맥이면 제외)
    	  if (/산불/.test(t) || (/화재/.test(t) && fireCtx && !insuranceCtx)) {
    	    return CP + '/resources/image/fire.png';
    	  }

      // 2) 태풍/풍랑/강풍
      if (/태풍|풍랑|강풍|돌풍|폭풍/.test(t)) return CP + '/resources/image/typhoon.png';

      // 3) 지진/여진
      if (/지진|여진|진도/.test(t)) return CP + '/resources/image/earthquake.png';

      // 4) 산사태
      if (/산사태/.test(t)) return CP + '/resources/image/landslide.png';

      // 5) 폭설/대설/눈보라
      if (/폭설|대설|눈보라|적설/.test(t)) return CP + '/resources/image/snow.png';

      // 6) 폭염/열대야/고온(기온/재난 제거)
      if (/폭염|열대야|고온|온열/.test(t)) return CP + '/resources/image/temper.png';

      // 7) 싱크홀/땅꺼짐/지반침하
      if (/싱크홀|땅꺼짐|지반침하/.test(t)) return CP + '/resources/image/sinkhole.png';

      // 8) 황사/미세먼지
      if (/황사|미세먼지|스모그|대기오염/.test(t)) return CP + '/resources/image/dust.png';

      // 9) 감염병/전염병
      if (/감염병|전염병|질병|독감|코로나|인플루엔자/.test(t)) return CP + '/resources/image/disease.png';

      // 기본
      return DEFAULT_IMG;
    };

    const asList = Array.isArray(HOME_NEWS) ? HOME_NEWS : [];
    
    function extractKeywords(n){
    	  const parts = [];
    	  const add = (v) => {
    	    if (!v) return;
    	    if (Array.isArray(v)) { v.forEach(add); return; }
    	    if (typeof v === 'object') { Object.values(v).forEach(add); return; }
    	    parts.push(String(v));
    	  };
    	  add(n?.keyword);
    	  add(n?.keywords);
    	  add(n?.category);
    	  add(n?.categories);
    	  add(n?.tags);
    	  add(n?.title);
    	  return parts.join(' ');
    	}
 // 2) mapItem 교체 (제공 썸네일 > 키워드 매핑 > 기본 + fallback 보강)
    const mapItem = (n) => {
      const providedRaw = getProvidedImage(n) || getProvidedImageDeep(n); // 빠른→깊은
      const provided    = absolutize(providedRaw);
      const kwText      = extractKeywords(n);
      const byKeyword   = pickImg(kwText);

      const item = {
        title   : n?.title || '(제목 없음)',
        text    : n?.company || '',
        href    : n?.url || '#',
        img     : provided || byKeyword || DEFAULT_IMG,
        fallback: byKeyword || DEFAULT_IMG
      };

      // 혼합콘텐츠 차단(https 페이지에서 http 이미지) 회피
      if (location.protocol === 'https:' && /^http:\/\//i.test(item.img)) {
        item.img = item.fallback;
      }
      return item;
    };
    	
    const newsData = { breaking: [], safety: [], emergency: [] };
    asList.forEach((n, i) => {
      const item = mapItem(n);
      (i % 3 === 0 ? newsData.breaking
       : i % 3 === 1 ? newsData.safety
                     : newsData.emergency).push(item);
    });

    const normIndex = (i,len)=>{ i%=len; return i<0? i+len : i; };

 // 3) renderNewsCard에서 onerror가 fallback을 사용하도록 수정
    function renderNewsCard(card){
      const feed = card.dataset.feed;
      const list = newsData[feed] || [];
      if(!list.length) return;
      const idx = normIndex(parseInt(card.dataset.idx||'0',10), list.length);
      card.dataset.idx = idx;

      const item  = list[idx];
      const img   = card.querySelector('.card-img-top');
      const title = card.querySelector('.title-text');
      const desc  = card.querySelector('.card-text');

      if(img){
        img.onerror = () => { img.onerror = null; img.src = item.fallback || DEFAULT_IMG; };
        img.src = item.img;
        img.alt = item.title;
      }
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

        const wrap = card.closest('.news-rotator-wrap') || card.parentElement;
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