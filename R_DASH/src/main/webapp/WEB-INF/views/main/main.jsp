<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>재민이 메인화면</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(to bottom, #eaf6fb, #fef1ef);
            font-family: 'Noto Sans KR', sans-serif;
        }

        .top-bar {
            background-color: #fff;
            padding: 10px 20px;
            border-bottom: 1px solid #ccc;
        }

        .nav-button-group .btn {
            margin-right: 10px;
        }

        .search-section {
            background-image: url('https://source.unsplash.com/1600x400/?sky,disaster');
            background-size: cover;
            padding: 80px 20px;
            text-align: center;
            color: #333;
        }

        .search-input {
            width: 50%;
            border-radius: 30px 0 0 30px;
        }

        .search-btn {
            border-radius: 0 30px 30px 0;
        }

        .news-title {
            text-align: center;
            font-weight: bold;
            margin: 50px 0 20px;
            font-size: 1.8rem;
        }

        .faq-section {
            background-color: #222;
            color: #fff;
            padding: 40px 20px;
            margin-top: 60px;
        }

        .accordion-button {
            background-color: #333;
            color: #fff;
        }

        .accordion-button:not(.collapsed) {
            background-color: #444;
            color: #fff;
        }

        .accordion-body {
            background-color: #111;
            color: #ddd;
        }

        .lang-btn {
            color: white;
        }

        .login-btn {
            background-color: #dc3545;
            color: white;
            border: none;
        }

        .login-btn:hover {
            background-color: #c82333;
        }
    </style>
</head>
<body>

<!-- 🔝 상단 네비게이션 -->
<div class="top-bar d-flex justify-content-between align-items-center">
    <div class="d-flex align-items-center">
        <button class="btn btn-light me-2"><i class="bi bi-list"></i> 메뉴</button>
        <input type="text" class="form-control me-2" style="width: 200px;" placeholder="검색">
        <div class="nav-button-group d-none d-md-inline">
            <a class="btn btn-outline-dark" href="#">통계 페이지</a>
            <a class="btn btn-outline-dark" href="#">지도 페이지</a>
            <a class="btn btn-outline-dark" href="#">조회 페이지</a>
        </div>
    </div>
    <div>
        <button class="btn btn-sm btn-secondary lang-btn">🌐 한국어 ▼</button>
        <button class="btn btn-sm login-btn ms-2">로그인</button>
    </div>
</div>

<!-- 🗣 안내 텍스트 + 검색 -->
<div class="search-section">
    <h4>저희 재난 알림 사이트를 방문해주셔서 감사합니다.</h4>
    <div class="d-flex justify-content-center mt-4">
        <input type="text" class="form-control search-input" placeholder="원하시는 검색 단어를 입력해주세요.">
        <button class="btn btn-danger search-btn">검색</button>
    </div>
</div>

<!-- 📰 주요 뉴스 -->
<div class="container">
    <div class="news-title">주요 뉴스</div>
    <div class="row mt-4">
        <div class="col-md-4 mb-3">
            <div class="card h-100">
                <img src="https://source.unsplash.com/300x200/?earthquake" class="card-img-top">
                <div class="card-body">
                    <h5 class="card-title">[속보] 강진 발생</h5>
                    <p class="card-text">해당 지역 주민은 안전한 곳으로 대피 바랍니다.</p>
                </div>
            </div>
        </div>
        <div class="col-md-4 mb-3">
            <div class="card h-100">
                <img src="https://source.unsplash.com/300x200/?fire" class="card-img-top">
                <div class="card-body">
                    <h5 class="card-title">화재 시 행동요령 안내</h5>
                    <p class="card-text">119 긴급 행동요령 숙지로 생명을 지키세요.</p>
                </div>
            </div>
        </div>
        <div class="col-md-4 mb-3">
            <div class="card h-100">
                <img src="https://source.unsplash.com/300x200/?ambulance" class="card-img-top">
                <div class="card-body">
                    <h5 class="card-title">응급상황 대응 시스템 강화</h5>
                    <p class="card-text">최근 대응 시간 단축을 위한 시스템 개편 발표.</p>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- ❓ FAQ 영역 -->
<div class="faq-section">
    <div class="container">
        <h4 class="text-white mb-4">자주 묻는 질문</h4>
        <div class="accordion" id="faqAccordion">
            <div class="accordion-item">
                <h2 class="accordion-header" id="headingOne">
                    <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#faq1" aria-expanded="true">
                        대피소 위치는 어디서 확인할 수 있나요?
                    </button>
                </h2>
                <div id="faq1" class="accordion-collapse collapse show" data-bs-parent="#faqAccordion">
                    <div class="accordion-body">
                        상단 '지도 페이지' 메뉴에서 확인 가능합니다.
                    </div>
                </div>
            </div>
            <div class="accordion-item">
                <h2 class="accordion-header" id="headingTwo">
                    <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#faq2">
                        재난 종류에는 무엇이 있나요?
                    </button>
                </h2>
                <div id="faq2" class="accordion-collapse collapse" data-bs-parent="#faqAccordion">
                    <div class="accordion-body">
                        화재, 지진, 태풍, 폭염, 감염병 등 다양한 유형이 있습니다.
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>