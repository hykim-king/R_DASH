<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
  <!-- Sidenav -->
<nav class="sidenav navbar navbar-vertical  fixed-left  navbar-expand-xs navbar-light bg-white" id="sidenav-main">
    <div class="scrollbar-inner">
      <!-- Brand -->
      <div class="sidenav-header  d-flex  align-items-center">
        <a class="navbar-brand" href="/ehr/home">
          <img src="/ehr/resources/template/dashboard/assets/img/r_dash_logo.png" height="40" class="navbar-brand-img" alt="...">
        </a>
        <div class=" ml-auto ">
          <!-- Sidenav toggler -->
          <div class="sidenav-toggler d-none d-xl-block" data-action="sidenav-unpin" data-target="#sidenav-main">
            <div class="sidenav-toggler-inner">
              <i class="sidenav-toggler-line"></i>
              <i class="sidenav-toggler-line"></i>
              <i class="sidenav-toggler-line"></i>
            </div>
          </div>
        </div>
      </div>
      <div class="navbar-inner">
        <!-- Collapse -->
        <div class="collapse navbar-collapse" id="sidenav-collapse-main">
          <!-- Nav items -->
          <ul class="navbar-nav">
            <li class="nav-item">
              <a class="nav-link active" href="#navbar-dashboards" data-toggle="collapse" role="button" aria-expanded="true" aria-controls="navbar-dashboards">
                <i class="ni ni-map-big text-primary"></i>
                <span class="nav-link-text">지도</span>
              </a>
              <div class="collapse" id="navbar-dashboards">
                <ul class="nav nav-sm flex-column">
                  <li class="nav-item">
                    <a href="#" class="nav-link">
                      <span class="sidenav-mini-icon"> 날씨 </span>
                      <span class="sidenav-normal"> 날씨 </span>
                    </a>
                  </li>
                  <li class="nav-item">
                    <a href="#" class="nav-link">
                      <span class="sidenav-mini-icon"> 산사태 </span>
                      <span class="sidenav-normal"> 산사태 </span>
                    </a>
                  </li>
                  <li class="nav-item">
                    <a href="#" class="nav-link">
                      <span class="sidenav-mini-icon"> 싱크홀 </span>
                      <span class="sidenav-normal"> 싱크홀 </span>
                    </a>
                  </li>
                  <li class="nav-item">
                  <a href="${pageContext.request.contextPath}/map/dust" class="nav-link">
					  <span class="sidenav-mini-icon"> 황사 </span>
					  <span class="sidenav-normal"> 황사 </span>
					</a>
                  </li>
                  <li class="nav-item">
                   <a href="${pageContext.request.contextPath}/map/firestation" class="nav-link">
                      <span class="sidenav-mini-icon"> 소방서 </span>
                      <span class="sidenav-normal"> 소방서 </span>
                    </a>
                  </li>
                  <li class="nav-item">
                     <a href="${pageContext.request.contextPath}/map/shelter" class="nav-link">
                      <span class="sidenav-mini-icon"> 대피소 </span>
                      <span class="sidenav-normal"> 대피소 </span>
                    </a>
                  </li>
                </ul>
              </div>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#navbar-examples" data-toggle="collapse" role="button" aria-expanded="false" aria-controls="navbar-examples">
                <i class="ni ni-chart-pie-35"></i>
                <span class="nav-link-text">통계</span>
              </a>
              <div class="collapse" id="navbar-examples">
                <ul class="nav nav-sm flex-column">
                  <li class="nav-item">
                    <a href="/ehr/fire/statsPage" class="nav-link">
                      <span class="sidenav-mini-icon"> 화재 </span>
                      <span class="sidenav-normal"> 화재 </span>
                    </a>
                  </li>
                  <li class="nav-item">
                    <a href="/ehr/temperature/statsPage" class="nav-link">
                      <span class="sidenav-mini-icon"> 기온 </span>
                      <span class="sidenav-normal"> 기온 </span>
                    </a>
                  </li>
                  <li class="nav-item">
                    <a href="/ehr/dust/statsPage" class="nav-link">
                      <span class="sidenav-mini-icon"> 황사 </span>
                      <span class="sidenav-normal"> 황사 </span>
                    </a>
                  </li>
                  <li class="nav-item">
                    <a href="/ehr/sinkholes/statsPage" class="nav-link">
                      <span class="sidenav-mini-icon"> 싱크홀 </span>
                      <span class="sidenav-normal"> 싱크홀 </span>
                    </a>
                  </li>
                  <li class="nav-item">
                    <a href="/ehr/landslide/statsPage" class="nav-link">
                      <span class="sidenav-mini-icon"> 산사태 </span>
                      <span class="sidenav-normal"> 산사태 </span>
                    </a>
                  </li>
                </ul>
              </div>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#navbar-tables" data-toggle="collapse" role="button" aria-expanded="false" aria-controls="navbar-tables">
                <i class="ni ni-align-left-2 text-default"></i>
                <span class="nav-link-text">뉴스</span>
              </a>
              <div class="collapse" id="navbar-tables">
                <ul class="nav nav-sm flex-column">
                  <li class="nav-item">
                    <a href="/ehr/news/newsPage.do" class="nav-link">
                      <span class="sidenav-mini-icon">뉴스</span>
                      <span class="sidenav-normal"> 재난 뉴스 </span>
                    </a>
                  </li>
                </ul>
              </div>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#navbar-maps" data-toggle="collapse" role="button" aria-expanded="false" aria-controls="navbar-maps">
                <i class="ni ni-bell-55"></i>
                <span class="nav-link-text">공지사항</span>
              </a>
              <div class="collapse" id="navbar-maps">
                <ul class="nav nav-sm flex-column">
                  <li class="nav-item">
                    <a href="/ehr/board/doRetrieve.do" class="nav-link">
                      <span class="sidenav-mini-icon">공지</span>
                      <span class="sidenav-normal"> 공지보기 </span>
                    </a>
                  </li>
                </ul>
              </div>
            </li>
          </ul>
        </div>
      </div>
            <div class="sidenav-footer mt-auto" style="position: absolute; bottom: 10px; right: 5px; width: 50px; text-align: center;"> <!-- mt-auto로 아래로 밀기 -->
			    <select id="lang" name="lang" class="form-control" style="width: 100%; padding:2px;">
			       <option value="ko" ${lang eq 'ko' ? 'selected' : ''}>KO</option>
			       <option value="en" ${lang eq 'en' ? 'selected' : ''}>EN</option>
			    </select>
			</div>
    </div>
  </nav>
    <!-- Main content -->
  <div class="main-content" id="panel">
    <!-- Topnav -->
    <nav class="navbar navbar-horizontal navbar-expand-lg navbar-dark bg-warning">
      <div class="container-fluid">
        <div class="collapse navbar-collapse justify-content-end" id="navbarSupportedContent">
          <c:choose>
            <c:when test="${empty sessionScope.loginUser}">   
            <ul class="navbar-nav align-items-center">
                <li class="nav-item dropdown"> 
                   <a class="nav-link pr-0" href="/ehr/user/login" role="button" data-toggle="dropdown" aria-haspopup="false">
                     <div class="media align-items-center">
                        <span class="avatar avatar-sm rounded-circle">
                          <img alt="Image placeholder" src="/ehr/resources/image/profile/defaultProfile.jpg">    
                        </span>
                        <div class="media-body  ml-2  d-none d-lg-block">
                          <span class="mb-0 text-sm  font-weight-bold">비회원</span>
                        </div>
                      </div>
                    </a>
                    <div class="dropdown-menu  dropdown-menu-right ">
                    <div class="dropdown-header noti-title">
                      <h6 class="text-overflow m-0">환영합니다!</h6>
                    </div>
                    <a href="/ehr/user/login" class="dropdown-item">
                      <i class="ni ni-single-02"></i>
                      <span>로그인</span>
                    </a>
                    <a href="/ehr/user/regist" class="dropdown-item">
                      <i class="ni ni-settings-gear-65"></i>
                      <span>회원가입</span>
                    </a>
                  </div>
                </li>
            </ul>
            </c:when>
            <c:otherwise>
              <ul class="navbar-nav align-items-center">
                <li class="nav-item dropdown">
                  <a class="nav-link pr-0" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <div class="media align-items-center">
                      <span class="avatar avatar-sm rounded-circle">
                        <img alt="Image placeholder" src="/ehr/resources/image/profile/${sessionScope.loginUser.image}">    
                      </span>
                      <div class="media-body  ml-2  d-none d-lg-block">
                        <span class="mb-0 text-sm  font-weight-bold">${sessionScope.loginUser.name}</span>
                      </div>
                    </div>
                  </a>
                  <div class="dropdown-menu  dropdown-menu-right ">
                    <div class="dropdown-header noti-title">
                      <h6 class="text-overflow m-0">환영합니다!</h6>
                    </div>
                    <a href="/ehr/user/myPage" class="dropdown-item">
                      <i class="ni ni-single-02"></i>
                      <span>마이페이지</span>
                    </a>
                    <a href="/ehr/user/changePw" class="dropdown-item">
                      <i class="ni ni-settings-gear-65"></i>
                      <span>비밀번호 변경</span>
                    </a>
                    <c:if test="${sessionScope.loginUser.role==1 }">
                        <a href="/ehr/user/userList" class="dropdown-item">
                          <i class="ni ni-calendar-grid-58"></i>
                          <span>회원 관리</span>
                        </a>
                    </c:if>
                    <div class="dropdown-divider"></div>
                    <a href="/ehr/user/logout" class="dropdown-item">
                      <i class="ni ni-user-run"></i>
                      <span>로그아웃</span>
                    </a>
                  </div>
                </li>
              </ul>    
            </c:otherwise>
          </c:choose>
        </div>
      </div>
    </nav>