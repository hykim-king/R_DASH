<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.Date" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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

<link rel="icon" href="${CP}/resources/image/Jaemini_face.ico" type="image/x-icon"/>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="${CP}/resources/template/Animate/backInUp.css">

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
    position: relative;
}

/* 상단 네비게이션 */
.top-bar {
    position: absolute;
    top: 0;
    width: 100%;
    padding: 10px 30px;
    background: rgba(0, 0, 0, 0.3);
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.nav-button-group a {
    margin-right: 10px;
    color: white;
    text-decoration: none;
}

.nav-button-group a:hover {
    text-decoration: underline;
}

.lang-btn, .login-btn {
    margin-left: 10px;
}

.login-btn {
    background-color: #dc3545;
    color: white;
    border: none;
}

/* 메인 타이틀 */
.search-container h1 {
    font-size: 1.6rem;
    font-weight: bold;
    margin-bottom: 20px;
    text-shadow: 1px 1px 4px rgba(0,0,0,0.5);
}

/* 검색창 */
.search-container {
    margin-top: 150px;
}

.search-box {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: center;
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
    white-space: nowrap;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 0.9rem;
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

/* FAQ */
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
</style>
</head>

<body>

<!-- 메인 화면 -->
<div class="main-section main-background">
    <div class="top-bar">
        <div class="d-flex align-items-center">
            <a class="btn btn-sm btn-light me-2">메뉴</a>
            <div class="nav-button-group d-none d-md-inline">
                <a href="#news">통계 페이지</a>
                <a href="#news">지도 페이지</a>
                <a href="#faq">조회 페이지</a>
            </div>
        </div>
        <div>
            <button class="btn btn-sm btn-secondary lang-btn">🌐 한국어 ▼</button>
            <button class="btn btn-sm login-btn">로그인</button>
        </div>
    </div>

    <div class="search-container backInUp">
        <h1>저희 재난 알림 사이트를 방문해주셔서 감사합니다.</h1>
        <div class="search-box">
            <form action="${CP}/search" method="get" style="display: flex; width: 100%;">
                <input type="text" name="keyword" class="search-input" placeholder="원하시는 검색 단어를 입력해주세요." required>
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
                    <img src="https://source.unsplash.com/600x400/?earthquake,nature" class="card-img-top" alt="지진 이미지">
                    <div class="card-body">
                        <h5 class="card-title">[속보] 강진 발생</h5>
                        <p class="card-text">해당 지역 주민은 안전한 곳으로 대피 바랍니다.</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-3">
                <div class="card h-100">
                    <img src="https://source.unsplash.com/600x400/?fire,emergency" class="card-img-top" alt="화재 이미지">
                    <div class="card-body">
                        <h5 class="card-title">화재 시 행동요령 안내</h5>
                        <p class="card-text">119 긴급 행동요령 숙지로 생명을 지키세요.</p>
                    </div>
                </div>
            </div>
            <div class="col-md-4 mb-3">
                <div class="card h-100">
                    <img src="https://source.unsplash.com/600x400/?ambulance,emergency" class="card-img-top" alt="응급 이미지">
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
                    <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#faq1" aria-expanded="true">
                        대피소 위치는 어디서 확인할 수 있나요?
                    </button>
                </h2>
                <div id="faq1" class="accordion-collapse collapse show" data-bs-parent="#faqAccordion">
                    <div class="accordion-body">상단 '지도 페이지' 메뉴에서 확인 가능합니다.</div>
                </div>
            </div>
            <div class="accordion-item">
                <h2 class="accordion-header" id="headingTwo">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq2">
                        재난 종류에는 무엇이 있나요?
                    </button>
                </h2>
                <div id="faq2" class="accordion-collapse collapse" data-bs-parent="#faqAccordion">
                    <div class="accordion-body">화재, 지진, 태풍, 폭염, 감염병 등 다양한 유형이 있습니다.</div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
document.addEventListener("DOMContentLoaded", () => {
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
});
</script>

</body>
</html>