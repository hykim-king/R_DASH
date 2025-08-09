<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
  <!-- Sidenav -->
<nav class="sidenav navbar navbar-vertical  fixed-left  navbar-expand-xs navbar-light bg-white" id="sidenav-main">
    <div class="scrollbar-inner">
      <!-- Brand -->
      <div class="sidenav-header  d-flex  align-items-center">
        <a class="navbar-brand" href="/ehr/resources/template/index.html">
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
                    <a href="#" class="nav-link">
                      <span class="sidenav-mini-icon"> 황사 </span>
                      <span class="sidenav-normal"> 황사 </span>
                    </a>
                  </li>
                  <li class="nav-item">
                    <a href="#" class="nav-link">
                      <span class="sidenav-mini-icon"> 소방서 </span>
                      <span class="sidenav-normal"> 소방서 </span>
                    </a>
                  </li>
                  <li class="nav-item">
                    <a href="#" class="nav-link">
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
                    <a href="/ehr/resources/template/dashboardpages/examples/login.html" class="nav-link">
                      <span class="sidenav-mini-icon"> 화재 </span>
                      <span class="sidenav-normal"> 화재 </span>
                    </a>
                  </li>
                  <li class="nav-item">
                    <a href="/ehr/resources/template/dashboardpages/examples/login.html" class="nav-link">
                      <span class="sidenav-mini-icon"> 소화기 </span>
                      <span class="sidenav-normal"> 소화기 </span>
                    </a>
                  </li>
                  <li class="nav-item">
                    <a href="/ehr/resources/template/dashboardpages/examples/login.html" class="nav-link">
                      <span class="sidenav-mini-icon"> 환자 </span>
                      <span class="sidenav-normal"> 환자 수 </span>
                    </a>
                  </li>
                  <li class="nav-item">
                    <a href="/ehr/resources/template/dashboardpages/examples/login.html" class="nav-link">
                      <span class="sidenav-mini-icon"> 피해 </span>
                      <span class="sidenav-normal"> 피해,복구 </span>
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
                    <a href="/ehr/resources/template/dashboardpages/tables/tables.html" class="nav-link">
                      <span class="sidenav-mini-icon"> T </span>
                      <span class="sidenav-normal"> Tables </span>
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
                    <a href="/ehr/resources/template/dashboardpages/maps/google.html" class="nav-link">
                      <span class="sidenav-mini-icon"> G </span>
                      <span class="sidenav-normal"> Google </span>
                    </a>
                  </li>
                </ul>
              </div>
            </li>
            <!-- 
            <hr class="my-3"/>
            <h6 class="navbar-heading pl-4 text-muted">
              <span class="docs-normal">Documentation</span>
            </h6>
            <li class="nav-item">
              <a class="nav-link" href="https://demos.creative-tim.com/impact-design-system-pro/docs/getting-started/quick-start/">
                <i class="ni ni-spaceship"></i>
                <span class="nav-link-text">Getting started</span>
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="https://demos.creative-tim.com/impact-design-system-pro/docs/dashboard/alerts/">
                <i class="ni ni-ui-04"></i>
                <span class="nav-link-text">Components</span>
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="https://demos.creative-tim.com/impact-design-system-pro/docs/plugins/charts/">
                <i class="ni ni-chart-pie-35"></i>
                <span class="nav-link-text">Plugins</span>
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link active active-pro" href="https://www.creative-tim.com/product/impact-design-system-pro" target="_blank">
                <i class="ni ni-send text-primary"></i>
                <span class="nav-link-text">Upgrade to PRO</span>
              </a>
            </li>
             -->
          </ul>
        </div>
      </div>
    </div>
  </nav>
  <!-- Main content -->
  <div class="main-content fixed-top" id="panel">
    <!-- Topnav -->
    <nav class="navbar navbar-horizontal navbar-expand-lg navbar-dark bg-warning fixed-top">
      <div class="container-fluid">
        <div class="collapse navbar-collapse justify-content-end" id="navbarSupportedContent">

          <ul class="navbar-nav align-items-center">
            <li class="nav-item dropdown">
              <a class="nav-link pr-0" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <div class="media align-items-center">
                  <span class="avatar avatar-sm rounded-circle">
                    <c:choose>
                        <c:when test="${empty sessionScope.loginUser}">
                            <img alt="Image placeholder" src="/ehr/resources/image/defaultProfile.jpg">
                        </c:when>
                        <c:otherwise>
                            <img alt="Image placeholder" src="/ehr/resources/template/dashboard/assets/img/theme/team-4.jpg">    
                        </c:otherwise>
                    </c:choose>
                  </span>
                  <div class="media-body  ml-2  d-none d-lg-block">
                    <span class="mb-0 text-sm  font-weight-bold">John Snow</span>
                  </div>
                </div>
              </a>
              <div class="dropdown-menu  dropdown-menu-right ">
                <div class="dropdown-header noti-title">
                  <h6 class="text-overflow m-0">환영합니다!</h6>
                </div>
                <a href="#!" class="dropdown-item">
                  <i class="ni ni-single-02"></i>
                  <span>마이페이지</span>
                </a>
                <a href="#!" class="dropdown-item">
                  <i class="ni ni-settings-gear-65"></i>
                  <span>비밀번호 변경</span>
                </a>
                <a href="#!" class="dropdown-item">
                  <i class="ni ni-calendar-grid-58"></i>
                  <span>Activity</span>
                </a>
                <a href="#!" class="dropdown-item">
                  <i class="ni ni-support-16"></i>
                  <span>Support</span>
                </a>
                <div class="dropdown-divider"></div>
                <a href="#!" class="dropdown-item">
                  <i class="ni ni-user-run"></i>
                  <span>로그아웃</span>
                </a>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </nav>
   </div>