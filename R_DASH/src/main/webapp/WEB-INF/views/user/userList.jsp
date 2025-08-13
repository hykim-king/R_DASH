<%@page import="com.pcwk.ehr.cmn.PcwkString"%>
<%@page import="com.pcwk.ehr.cmn.SearchDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    int bottomCount = 5;
    int pageSize    = 0;//페이지 사이즈
    int pageNo      = 0;//페이지 번호
    int maxNum      = 0;//총글수
    
    String url      = "";//호출URL
    String scriptName="";//자바스크립트 이름
    
    
    //request: 요청 처리를 할수 있는 jsp 내장 객체
    String totalCntString = request.getAttribute("totalCnt").toString();
    //out.print("totalCntString:"+totalCntString);
    maxNum = Integer.parseInt(totalCntString);  
    
    SearchDTO  paramVO = (SearchDTO)request.getAttribute("search");   
    pageSize = paramVO.getPageSize();
    pageNo   = paramVO.getPageNo();
    
    String cp = request.getContextPath();
       //out.print("cp:"+cp);
       
       url = cp+"/user/userList";
       //out.print("url:"+url);
       
       scriptName = "pagerDoRetrieve";
       
       String pageHtml=PcwkString.bootstrapRenderingPager(maxNum, pageNo, pageSize, bottomCount, url, scriptName);
       
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="Start your development with a Dashboard for Bootstrap 4.">
  <meta name="author" content="Creative Tim">
  <title>회원 조회</title>
</head>
<script>
document.addEventListener('DOMContentLoaded',function(){
	if('${sessionScope.loginUser.role}'!=='1'){
        alert('관리자만 접근 가능합니다.');
        
        window.location.href='/ehr/home';
    }
	
	if('${search.searchDiv}'===''){
		searchWord.disabled = true;
	}
});
//searchDiv 설정
function selectDiv(div){
	const selectDivButton = document.querySelector('#selectDivButton');
	const searchWord = document.querySelector('#searchWord');
	
	if(div===10){
		selectDivButton.innerText = '이메일';
		selectDivButton.value=div;
		searchWord.disabled = false;
	}else if(div === 20){
		selectDivButton.innerText = '이름';
		selectDivButton.value=div;
		searchWord.disabled = false;
	}else{
		selectDivButton.innerText = '전체';
		selectDivButton.value='';
		searchWord.disabled = true;
	}
	
}
//회원 검색
function search(){
	const selectDivButton = document.querySelector('#selectDivButton');
	const searchWord = document.querySelector('#searchWord');
	const pageNo = document.querySelector('#pageNo')
	window.location.href = '/ehr/user/userList?searchDiv=' + selectDivButton.value + '&searchWord=' + searchWord.value + '&pageNo=' + pageNo.value;
}
// 권한 변경
function changeRole(userNo){
	if(confirm('권한을 변경하시겠습니까?')){
		
		$.ajax({
            method:"POST",    //GET/POST
            url:"/ehr/user/changeRole", //서버측 URL
            dataType:"json",//서버에서 받을 데이터 타입
            data:{          //파라메터
              "userNo" : userNo,
            },
            success:function(result){//요청 성공
                if(2 === result.success){
                    alert(result.message);
                    
                    //페이지 새로고침
                    window.location.reload();
                }else{
                    alert(result.message);
                    
                    //페이지 새로고침
                    window.location.reload();
                } 
                    
            },
            error:function(result){//요청 실패
                console.log("error:"+result)
                alert(result);
            }
            
            
        });
	}
}

//페이징 
function pagerDoRetrieve(url, pageNo){   
    //button
    const searchButton = document.querySelector('#searchButton');
    
    document.querySelector('#pageNo').value= pageNo;
    
    searchButton.click();     
    
}
</script>
<body>
  <!-- Sidenav -->
  <nav class="sidenav navbar navbar-vertical  fixed-left  navbar-expand-xs navbar-light bg-white" id="sidenav-main">
    <div class="scrollbar-inner">
      <!-- Brand -->
      <div class="sidenav-header  d-flex  align-items-center">
        <a class="navbar-brand" href="/ehr/resources/template/index.html">
          <img src="/ehr/resources/template/dashboard/assets/img/brand/dark.svg" height="40" class="navbar-brand-img" alt="...">
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
                <i class="ni ni-shop text-primary"></i>
                <span class="nav-link-text">Dashboards</span>
              </a>
              <div class="collapse show" id="navbar-dashboards">
                <ul class="nav nav-sm flex-column">
                  <li class="nav-item">
                    <a href="/ehr/resources/template/dashboard/pages/dashboards/dashboard.html" class="nav-link">
                      <span class="sidenav-mini-icon"> D </span>
                      <span class="sidenav-normal"> Dashboard </span>
                    </a>
                  </li>
                </ul>
              </div>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#navbar-examples" data-toggle="collapse" role="button" aria-expanded="false" aria-controls="navbar-examples">
                <i class="ni ni-ungroup text-orange"></i>
                <span class="nav-link-text">Examples</span>
              </a>
              <div class="collapse" id="navbar-examples">
                <ul class="nav nav-sm flex-column">
                  <li class="nav-item">
                    <a href="/ehr/resources/template/dashboard/pages/examples/login.html" class="nav-link">
                      <span class="sidenav-mini-icon"> L </span>
                      <span class="sidenav-normal"> Login </span>
                    </a>
                  </li>
                  <li class="nav-item">
                    <a href="/ehr/resources/template/dashboard/pages/examples/register.html" class="nav-link">
                      <span class="sidenav-mini-icon"> R </span>
                      <span class="sidenav-normal"> Register </span>
                    </a>
                  </li>
                  <li class="nav-item">
                    <a href="/ehr/resources/template/dashboard/pages/examples/profile.html" class="nav-link">
                      <span class="sidenav-mini-icon"> P </span>
                      <span class="sidenav-normal"> Profile </span>
                    </a>
                  </li>
                </ul>
              </div>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#navbar-tables" data-toggle="collapse" role="button" aria-expanded="false" aria-controls="navbar-tables">
                <i class="ni ni-align-left-2 text-default"></i>
                <span class="nav-link-text">Tables</span>
              </a>
              <div class="collapse" id="navbar-tables">
                <ul class="nav nav-sm flex-column">
                  <li class="nav-item">
                    <a href="/ehr/resources/template/dashboard/pages/tables/tables.html" class="nav-link">
                      <span class="sidenav-mini-icon"> T </span>
                      <span class="sidenav-normal"> Tables </span>
                    </a>
                  </li>
                </ul>
              </div>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="#navbar-maps" data-toggle="collapse" role="button" aria-expanded="false" aria-controls="navbar-maps">
                <i class="ni ni-map-big text-primary"></i>
                <span class="nav-link-text">Maps</span>
              </a>
              <div class="collapse" id="navbar-maps">
                <ul class="nav nav-sm flex-column">
                  <li class="nav-item">
                    <a href="/ehr/resources/template/dashboard/pages/maps/google.html" class="nav-link">
                      <span class="sidenav-mini-icon"> G </span>
                      <span class="sidenav-normal"> Google </span>
                    </a>
                  </li>
                </ul>
              </div>
            </li>
            <hr class="my-3">
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
          </ul>
        </div>
      </div>
    </div>
  </nav>
  <!-- Main content -->
  <div class="main-content" id="panel">
    <!-- Topnav -->
    <nav class="navbar navbar-top navbar-expand navbar-dark bg-primary border-bottom">
      <div class="container-fluid">
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
          <!-- Search form -->
          <form class="navbar-search navbar-search-light form-inline mr-sm-3" id="navbar-search-main">
            <div class="form-group mb-0">
              <div class="input-group input-group-alternative input-group-merge">
                <div class="input-group-prepend">
                  <span class="input-group-text"><i class="fas fa-search"></i></span>
                </div>
                <input class="form-control" placeholder="Search" type="text">
              </div>
            </div>
            <button type="button" class="close" data-action="search-close" data-target="#navbar-search-main" aria-label="Close">
              <span aria-hidden="true">×</span>
            </button>
          </form>
          <!-- Navbar links -->
          <ul class="navbar-nav align-items-center  ml-md-auto ">
            <li class="nav-item d-xl-none">
              <!-- Sidenav toggler -->
              <div class="pr-3 sidenav-toggler sidenav-toggler-dark" data-action="sidenav-pin" data-target="#sidenav-main">
                <div class="sidenav-toggler-inner">
                  <i class="sidenav-toggler-line"></i>
                  <i class="sidenav-toggler-line"></i>
                  <i class="sidenav-toggler-line"></i>
                </div>
              </div>
            </li>
            <li class="nav-item d-sm-none">
              <a class="nav-link" href="#" data-action="search-show" data-target="#navbar-search-main">
                <i class="ni ni-zoom-split-in"></i>
              </a>
            </li>
            <li class="nav-item dropdown">
              <a class="nav-link" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <i class="ni ni-bell-55"></i>
              </a>
              <div class="dropdown-menu dropdown-menu-xl  dropdown-menu-right  py-0 overflow-hidden">
                <!-- Dropdown header -->
                <div class="px-3 py-3">
                  <h6 class="text-sm text-muted m-0">You have <strong class="text-primary">13</strong> notifications.</h6>
                </div>
                <!-- List group -->
                <div class="list-group list-group-flush">
                  <a href="#!" class="list-group-item list-group-item-action">
                    <div class="row align-items-center">
                      <div class="col-auto">
                        <!-- Avatar -->
                        <img alt="Image placeholder" src="/ehr/resources/template/dashboard/assets/img/theme/team-1.jpg" class="avatar rounded-circle">
                      </div>
                      <div class="col ml--2">
                        <div class="d-flex justify-content-between align-items-center">
                          <div>
                            <h4 class="mb-0 text-sm">John Snow</h4>
                          </div>
                          <div class="text-right text-muted">
                            <small>2 hrs ago</small>
                          </div>
                        </div>
                        <p class="text-sm mb-0">Let's meet at Starbucks at 11:30. Wdyt?</p>
                      </div>
                    </div>
                  </a>
                  <a href="#!" class="list-group-item list-group-item-action">
                    <div class="row align-items-center">
                      <div class="col-auto">
                        <!-- Avatar -->
                        <img alt="Image placeholder" src="/ehr/resources/template/dashboard/assets/img/theme/team-2.jpg" class="avatar rounded-circle">
                      </div>
                      <div class="col ml--2">
                        <div class="d-flex justify-content-between align-items-center">
                          <div>
                            <h4 class="mb-0 text-sm">John Snow</h4>
                          </div>
                          <div class="text-right text-muted">
                            <small>3 hrs ago</small>
                          </div>
                        </div>
                        <p class="text-sm mb-0">A new issue has been reported for Argon.</p>
                      </div>
                    </div>
                  </a>
                  <a href="#!" class="list-group-item list-group-item-action">
                    <div class="row align-items-center">
                      <div class="col-auto">
                        <!-- Avatar -->
                        <img alt="Image placeholder" src="/ehr/resources/template/dashboard/assets/img/theme/team-3.jpg" class="avatar rounded-circle">
                      </div>
                      <div class="col ml--2">
                        <div class="d-flex justify-content-between align-items-center">
                          <div>
                            <h4 class="mb-0 text-sm">John Snow</h4>
                          </div>
                          <div class="text-right text-muted">
                            <small>5 hrs ago</small>
                          </div>
                        </div>
                        <p class="text-sm mb-0">Your posts have been liked a lot.</p>
                      </div>
                    </div>
                  </a>
                  <a href="#!" class="list-group-item list-group-item-action">
                    <div class="row align-items-center">
                      <div class="col-auto">
                        <!-- Avatar -->
                        <img alt="Image placeholder" src="/ehr/resources/template/dashboard/assets/img/theme/team-4.jpg" class="avatar rounded-circle">
                      </div>
                      <div class="col ml--2">
                        <div class="d-flex justify-content-between align-items-center">
                          <div>
                            <h4 class="mb-0 text-sm">John Snow</h4>
                          </div>
                          <div class="text-right text-muted">
                            <small>2 hrs ago</small>
                          </div>
                        </div>
                        <p class="text-sm mb-0">Let's meet at Starbucks at 11:30. Wdyt?</p>
                      </div>
                    </div>
                  </a>
                  <a href="#!" class="list-group-item list-group-item-action">
                    <div class="row align-items-center">
                      <div class="col-auto">
                        <!-- Avatar -->
                        <img alt="Image placeholder" src="/ehr/resources/template/dashboard/assets/img/theme/team-5.jpg" class="avatar rounded-circle">
                      </div>
                      <div class="col ml--2">
                        <div class="d-flex justify-content-between align-items-center">
                          <div>
                            <h4 class="mb-0 text-sm">John Snow</h4>
                          </div>
                          <div class="text-right text-muted">
                            <small>3 hrs ago</small>
                          </div>
                        </div>
                        <p class="text-sm mb-0">A new issue has been reported for Argon.</p>
                      </div>
                    </div>
                  </a>
                </div>
                <!-- View all -->
                <a href="#!" class="dropdown-item text-center text-primary font-weight-bold py-3">View all</a>
              </div>
            </li>
            <li class="nav-item dropdown">
              <a class="nav-link" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <i class="ni ni-ungroup"></i>
              </a>
              <div class="dropdown-menu dropdown-menu-lg dropdown-menu-dark bg-default  dropdown-menu-right ">
                <div class="row shortcuts px-4">
                  <a href="#!" class="col-4 shortcut-item">
                    <span class="shortcut-media avatar rounded-circle bg-gradient-red">
                      <i class="ni ni-calendar-grid-58"></i>
                    </span>
                    <small>Calendar</small>
                  </a>
                  <a href="#!" class="col-4 shortcut-item">
                    <span class="shortcut-media avatar rounded-circle bg-gradient-orange">
                      <i class="ni ni-email-83"></i>
                    </span>
                    <small>Email</small>
                  </a>
                  <a href="#!" class="col-4 shortcut-item">
                    <span class="shortcut-media avatar rounded-circle bg-gradient-info">
                      <i class="ni ni-credit-card"></i>
                    </span>
                    <small>Payments</small>
                  </a>
                  <a href="#!" class="col-4 shortcut-item">
                    <span class="shortcut-media avatar rounded-circle bg-gradient-green">
                      <i class="ni ni-books"></i>
                    </span>
                    <small>Reports</small>
                  </a>
                  <a href="#!" class="col-4 shortcut-item">
                    <span class="shortcut-media avatar rounded-circle bg-gradient-purple">
                      <i class="ni ni-pin-3"></i>
                    </span>
                    <small>Maps</small>
                  </a>
                  <a href="#!" class="col-4 shortcut-item">
                    <span class="shortcut-media avatar rounded-circle bg-gradient-yellow">
                      <i class="ni ni-basket"></i>
                    </span>
                    <small>Shop</small>
                  </a>
                </div>
              </div>
            </li>
          </ul>
          <ul class="navbar-nav align-items-center  ml-auto ml-md-0 ">
            <li class="nav-item dropdown">
              <a class="nav-link pr-0" href="#" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                <div class="media align-items-center">
                  <span class="avatar avatar-sm rounded-circle">
                    <img alt="Image placeholder" src="/ehr/resources/template/dashboard/assets/img/theme/team-4.jpg">
                  </span>
                  <div class="media-body  ml-2  d-none d-lg-block">
                    <span class="mb-0 text-sm  font-weight-bold">John Snow</span>
                  </div>
                </div>
              </a>
              <div class="dropdown-menu  dropdown-menu-right ">
                <div class="dropdown-header noti-title">
                  <h6 class="text-overflow m-0">Welcome!</h6>
                </div>
                <a href="#!" class="dropdown-item">
                  <i class="ni ni-single-02"></i>
                  <span>My profile</span>
                </a>
                <a href="#!" class="dropdown-item">
                  <i class="ni ni-settings-gear-65"></i>
                  <span>Settings</span>
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
                  <span>Logout</span>
                </a>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </nav>
    <!-- Header -->
    <!-- Header -->
    <div class="header bg-warning pb-6 header bg-gradient-warning py-7 py-lg-8 pt-lg-9">
      <!-- Header container -->
      <div class="container-fluid d-flex align-items-center">
        <div class="row">
          <div style="margin-left:100px;">
            <h1 class="display-2 text-white">안녕하세요! ${sessionScope.loginUser.name}님</h1>
            <p class="text-white mt-0 mb-5">이 페이지는 회원 관련 정보를 조회할 수 있고, <br>회원 권한을 변경 할 수 있습니다.</p>
          </div>
        </div>
      </div>
    </div>
    <!-- Page content -->
    <div class="container-fluid mt--6" style="min-height: 700px; max-width:1700px; margin:0 auto;">
      <div class="row">
        <!-- Light table -->
        <div class="col">
          <div class="card">
            <!-- Card header -->
            <div class="card-header border-0 d-flex align-items-center">
              <h3 class="mb-0">회원 정보</h3>
              <!-- 
              <button type="button" class="btn btn-default" style="margin-left:20px;" onclick="location.href='/ehr/user/userList?searchDiv=30'">관리자</button>
               -->
              <div class="ml-auto d-flex align-items-center"> <!-- 오른쪽 끝으로 밀기 -->
                <div class="dropdown">
				  <button class="btn btn-secondary dropdown-toggle" type="button" value="" id="selectDivButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				    <c:choose>
				        <c:when test="${search.searchDiv == '10' }">이메일</c:when>
				        <c:when test="${search.searchDiv == '20' }">이름</c:when>
				        <c:otherwise>전체</c:otherwise>
				    </c:choose>
				  </button>
				  <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
				    <a class="dropdown-item" onclick="javascript:selectDiv('')">전체</a>
				    <a class="dropdown-item" onclick="javascript:selectDiv(10)">이메일</a>
				    <a class="dropdown-item" onclick="javascript:selectDiv(20)">이름</a>
				  </div>
				</div>
	            <input type="text" class="form-control" id="searchWord" name="searchWord">
	            <input type="hidden"id="pageNo" name="pageNo">  
			    <button type="button" id="searchButton" onclick="javascript:search()" class="btn btn-default text-nowrap" style="margin-left:10px">검색</button>
			  </div>
            </div>
            <!-- Light table -->
            <div class="table-responsive">
              <table class="table align-items-center table-flush">
                <thead class="thead-light">
                  <tr>
                    <th scope="col">사진</th>
                    <th scope="col">번호</th>
                    <th scope="col">계정</th>
                    <th scope="col">권한</th>
                    <th scope="col" class="sort" data-sort="completion">가입일</th>
                    <th scope="col">권한 변경</th>
                  </tr>
                </thead>
                <tbody class="list">
                <c:choose>
                    <c:when test="${list.size()>0 }">
                    	<c:forEach var="vo" items="${list }">
                    		<tr>
			                    <th scope="row">
			                      <div class="media align-items-center">
			                          <img alt="Image placeholder" class="avatar rounded-circle mr-3" src="/ehr/resources/image/profile/${vo.image }">
			                      </div>
			                    </th>
			                    <td class="budget">
			                      ${vo.no }
			                    </td>
			                    <td>
			                      <span class="badge badge-dot mr-4">
			                        <span class="status">${vo.email }</span>
			                      </span>
			                    </td>
			                    <td>
			                    <c:choose>
			                    	<c:when test="${vo.role == 1 }">
			                    		<span class="badge badge-dot mr-4">
			                       			<span class="status">관리자</span>
			               		        </span>
			                    	</c:when>
			                    	<c:otherwise>
			                    		<span class="badge badge-dot mr-4">
			                       			<span class="status">사용자</span>
			               		        </span>
			                    	</c:otherwise>
			                    </c:choose>
			                    </td>
			                    <td>
			                      <span class="badge badge-dot mr-4">
			                        <span class="status">${vo.regDt }</span>
			                      </span>
			                    </td>
			                    <td>
			                      <button class="btn btn-success" onclick="changeRole(${vo.userNo})">권한 변경</button>
			                    </td>
			                  </tr>
                    	</c:forEach>
                    </c:when>
                    <c:otherwise>
                    	<tr>
                    		<td colspan="6">등록된 회원이 없습니다.</td>
                    	</tr>
                    </c:otherwise>
                </c:choose>
                  
                </tbody>
              </table>
            </div>
            <!-- Card footer -->
            <div class="card-footer py-4">
                    <!-- paging -->
            <%
                out.print(pageHtml);
            %>
            <!--// paging end -->
            <!-- 
              <nav aria-label="...">
                <ul class="pagination justify-content-end mb-0">
                  <li class="page-item disabled">
                    <a class="page-link" href="#" tabindex="-1">
                      <i class="fas fa-angle-left"></i>
                      <span class="sr-only">Previous</span>
                    </a>
                  </li>
                  <li class="page-item active">
                    <a class="page-link" href="#">1</a>
                  </li>
                  <li class="page-item">
                    <a class="page-link" href="#">2 <span class="sr-only">(current)</span></a>
                  </li>
                  <li class="page-item"><a class="page-link" href="#">3</a></li>
                  <li class="page-item">
                    <a class="page-link" href="#">
                      <i class="fas fa-angle-right"></i>
                      <span class="sr-only">Next</span>
                    </a>
                  </li>
                </ul>
              </nav>
              -->
            </div>
          </div>
        </div>
      </div>
    </div>
</body>

</html>