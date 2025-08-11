<%@page import="java.time.format.DateTimeFormatter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%= application.getRealPath("/resources/image/profile") %>
<!DOCTYPE html>
<html>

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="Start your development with a Dashboard for Bootstrap 4.">
  <meta name="author" content="Creative Tim">
  <title>마이페이지</title>
  <script>
  document.addEventListener('DOMContentLoaded', function () {
	//닉네임 input
	  const nicknameInput = document.querySelector('#nickname');
	  //전화번호 input
	  const telInput = document.querySelector('#tel');
	  //우편번호 input
	  const zipCodeInput = document.querySelector('#zipCode');
	  //주소 input
	  const addressInput = document.querySelector('#address');
	  //상세주소 input
	  const detailAddressInput = document.querySelector('#detailAddress');
	  //수정 버튼 
	  const updateButton = document.querySelector('#updateButton');
	  //전화번호 정규식 ex)010-1234-5678
	  const tel_valid = /^0\d{1,2}-\d{3,4}-\d{4}$/;
	  
	  updateButton.addEventListener('click',function(){
		  if(tel_valid.test(telInput.value)===false && telInput.value !== ''){
              alert('전화번호 형식이 올바르지 않습니다.\nex)010-1234-5678');
              return;
          }
          
          
          $.ajax({
              method:"POST",    //GET/POST
              url:"/ehr/user/updateInfo", //서버측 URL
              dataType:"json",//서버에서 받을 데이터 타입
              data:{          //파라메터
            	  "userNo" : '${sessionScope.loginUser.userNo}',
                  "nickname" : nicknameInput.value,
                  "tel" : telInput.value,
                  "zipCode": zipCodeInput.value,
                  "address": addressInput.value,
                  "detailAddress": detailAddressInput.value
              },
              success:function(result){//요청 성공
                  if(false === result.success){
                      alert(result.message);

                      return;
                  }else{
                      alert(result.message);
                      
                      //로그인 페이지로 이동
                      window.location.href='/ehr/user/myPage';
                  } 
                      
              },
              error:function(result){//요청 실패
                  console.log("error:"+result)
                  alert(result);
              }
              
              
          });
	  });
	  
	  const imageLink = document.querySelector('#imageLink');
	  const profile = document.querySelector('#profile');
	  const image = document.querySelector('#image');
	  const changeButton = document.querySelector('#changeButton');
	  
      // 이미지 또는 버튼 클릭 시 파일 선택 창 열기
	  imageLink.addEventListener('click', function(event) {
		    event.preventDefault();
		    image.click();
		  });
	  
	  // 파일 선택 후 이미지 변경
	  image.addEventListener('change', function() {
	    const file = this.files[0];
	    if (file) {
	      const reader = new FileReader();
	      reader.onload = function(e) {
	        profile.src = e.target.result;
	      }
	      reader.readAsDataURL(file);
	    }
	  });
	  
	  // 파일 저장 버튼 클릭
	  changeButton.addEventListener('click', function(){
		  const file = image.files[0];

		  if (!file) {
		      alert("파일을 선택하세요.");
		      return;
		  }
		  
		  const formData = new FormData();
		  formData.append("file", file);
		  formData.append("userNo", "${sessionScope.loginUser.userNo}");
		  
		  $.ajax({
              method:"POST",    //GET/POST
              url:"/ehr/user/updateImage", //서버측 URL
              dataType:"json",//서버에서 받을 데이터 타입
              data:formData,
              enctype: "multipart/form-data",
              processData: false,      // FormData를 querystring으로 변환하지 않도록
              contentType: false,      // 브라우저가 적절한 multipart boundary 설정
              
              success:function(result){//요청 실패
                  if(false === result.success){
                      alert(result.message);

                      return;
                  }else{
                      alert(result.message);
                      
                      window.location.href='/ehr/user/myPage';
                  } 
                      
              },
              error:function(result){//요청 실패
                  console.log("error:"+result)
                  alert(result);
              }
              
              
          });
		  
	  });
	  document.querySelector('#deleteButton').addEventListener('click',function(){
          const inputPw = prompt("비밀번호를 입력하세요:");
          if (inputPw === null) { // 사용자가 취소 클릭
              return;
          }
          
          $.ajax({
              method:"POST",    //GET/POST
              url:"/ehr/user/deleteUser", //서버측 URL
              dataType:"json",//서버에서 받을 데이터 타입
              data:{
            	"password":inputPw  
              },
              success:function(result){//요청 실패
                  if(false === result.success){
                      alert(result.message);

                      return;
                  }else{
                      alert(result.message);
                      
                      window.location.href='/ehr/user/login';
                  } 
                      
              },
              error:function(result){//요청 실패
                  console.log("error:"+result)
                  alert(result);
              }
              
              
          });
      });
	  
  });
   
  
  function searchAddress(){
	    new daum.Postcode({
	        oncomplete: function(data) {
	            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분입니다.
	            document.querySelector('#zipCode').value = data.zonecode; //우편번호
	            document.querySelector('#address').value = data.address; //주소
	            // 예제를 참고하여 다양한 활용법을 확인해 보세요.
	        }
	    }).open();
	}
  </script>
</head>
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
    <nav class="navbar navbar-top navbar-expand navbar-dark bg-default border-bottom">
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
    <div class="header pb-6 d-flex align-items-center" style="min-height: 500px; background-image: url(/ehr/resources/template/dashboard/assets/img/theme/profile-cover.jpg); background-size: cover; background-position: center top;">
      <!-- Mask -->
      <span class="mask bg-gradient-default opacity-8"></span>
      <!-- Header container -->
      <div class="container-fluid d-flex align-items-center">
        <div class="row">
          <div class="col-lg-7 col-md-10">
            <h1 class="display-2 text-white">안녕하세요! ${sessionScope.loginUser.name}님</h1>
            <p class="text-white mt-0 mb-5">This is your profile page. You can see the progress you've made with your work and manage your projects or assigned tasks</p>
          </div>
        </div>
      </div>
    </div>
    <!-- Page content -->
    <div class="container-fluid mt--6">
      <div class="row">
        <div class="col-xl-4 order-xl-2">
          <div class="card card-profile">
            <img src="/ehr/resources/template/dashboard/assets/img/theme/img-1-1000x600.jpg" alt="Image placeholder" class="card-img-top">
            <div class="row justify-content-center">
              <div class="col-lg-3 order-lg-2">
                <div class="card-profile-image">
                  <form id="imageForm" enctype="">
	                  <a href="#" id="imageLink">
	                    <img id="profile" src="/ehr/resources/image/profile/${sessionScope.loginUser.image }" class="rounded-circle" style="object-fit: cover;">
	                  </a>
	                  <input type="file" id="image" name="image" accept="image/*" style="display:none">
                  </form>
                </div>
              </div>
            </div>
            <div class="card-header text-center border-0 pt-8 pt-md-4 pb-0 pb-md-4">
            </div>
            <div class="card-body pt-0">
              <div class="row">
                <div class="col">
                  <div class="card-profile-stats d-flex justify-content-center">
                  <!-- 이미지 변경시 버튼 -->
                  <!-- 
                    <div>
                      <span class="heading">22</span>
                      <span class="description">Friends</span>
                    </div>
                    <div>
                      <span class="heading">10</span>
                      <span class="description">Photos</span>
                    </div>
                    <div>
                      <span class="heading">89</span>
                      <span class="description">Comments</span>
                    </div>
                     -->
                  </div>
                </div>
              </div>
              <div class="text-center">
                <h5 class="h3">
                  ${sessionScope.loginUser.name }
                </h5>
                <div class="h5 font-weight-300">
                  <button class="btn btn-sm btn-default" id="changeButton">프로필 변경</button>
                </div>
              </div>
            </div>
          </div>
         </div>
        <div class="col-xl-8 order-xl-1">
          <div class="card">
            <div class="card-header">
              <div class="row align-items-center">
                <div class="col-8">
                  <h3 class="mb-0">회원 정보 수정 </h3>
                </div>
                <div class="col-4 text-right">
                  <button class="btn btn-lg btn-default" id="updateButton">수정</button>
                  <button class="btn btn-lg btn-default" id="deleteButton">탈퇴</button>
                  
                </div>
              </div>
            </div>
            <div class="card-body">
              <form>
                <h6 class="heading-small text-muted mb-4">회원 정보</h6>
                <div class="pl-lg-4">
                  <div class="row">
                    <div class="col-lg-6">
                      <div class="form-group">
                        <label class="form-control-label" for="name">이름</label>
                        <input type="text" id="name" name="name" class="form-control" value="${sessionScope.loginUser.name }" readonly>
                      </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-lg-6">
                      <div class="form-group">
                        <label class="form-control-label" for="email">Email</label>
                        <input type="email" id="email" name="email" class="form-control" value="${sessionScope.loginUser.email }" readonly>
                      </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-lg-6">
                      <div class="form-group">
                        <label class="form-control-label" for="nickname">별명</label>
                        <input type="text" id="nickname" class="form-control" placeholder="ex)부리부리" value="${sessionScope.loginUser.nickname }">
                      </div>
                    </div>
                  </div>
                </div>
                <hr class="my-4" />
                <!-- info -->
                <h6 class="heading-small text-muted mb-4">연락처 정보</h6>
                <div class="pl-lg-4">
                <div class="row">
                    <div class="col-md-4">
                      <div class="form-group">
                        <label class="form-control-label" for="tel">전화번호 </label>
                        <input id="tel" name="tel" class="form-control" placeholder="ex)010-1234-5678" value="${sessionScope.loginUser.tel }" type="tel">
                      </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-4">
                      <div class="form-group">
                        <label class="form-control-label" for="zipCode">우편번호</label>
                        <input id="zipCode" name="zipCode" class="form-control" placeholder="ex)12345" value="${sessionScope.loginUser.zipCode }" type="text" readonly>
                      </div>
                    </div>
                    <div class="col-md-2">
                      <div class="form-group">
                      <label class="form-control-label" for="zipCode">&nbsp;</label>
                        <input type="button" class="form-control" onclick="searchAddress()" value="우편번호 찾기">
                      </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-6">
                      <div class="form-group">
                        <label class="form-control-label" for="address">주소</label>
                        <input id="address" name="address" class="form-control" placeholder="ex)사랑시 고백구 행복동" value="${sessionScope.loginUser.address }" type="text" readonly>
                      </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-md-6">
                      <div class="form-group">
                        <label class="form-control-label" for="detailAddress">상세 주소</label>
                        <input id="detailAddress" name="detailAddress" class="form-control" placeholder="ex)에이콘 마을 1004동 1004호" value="${sessionScope.loginUser.detailAddress }" type="text">
                      </div>
                    </div>
                  </div>
                </div>
                <hr class="my-4" />
                <!-- Description -->
                <h4 class="text-muted mb-4">가입일 :${sessionScope.loginUser.regDt }</h4>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</body>

</html>