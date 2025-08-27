<%@page import="java.time.format.DateTimeFormatter"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="Start your development with a Dashboard for Bootstrap 4.">
  <meta name="author" content="Creative Tim">
  <title>비밀번호 변경</title>
  <script>
  document.addEventListener('DOMContentLoaded', function () {
	if('${sessionScope.loginUser}'===''){
		alert('로그인이 필요합니다.');
		
		window.location.href='/ehr/user/login';
	}
	//비밀번호 
	const password = document.querySelector('#password');
	//비밀번호 확인
	const checkPassword = document.querySelector('#checkPassword');
	//변경 버튼
	const updateButton = document.querySelector('#updateButton');
	//비밀번호 형식 체크 (영문 대/소문자 하나이상 포함, 특수문자 하나이상 포함)
	const valid_password = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+~`=\-{}\[\]:;"'<>,.?\/\\])\S{8,16}$/;
	 
	checkPassword.addEventListener('input',function(){
		if(password.value !== checkPassword.value){
			checkPassword.style.borderColor = 'red';
		}else{
			checkPassword.style.borderColor = 'green';
		}
	}); 
	 
	updateButton.addEventListener('click',function(){
		if(password.value===''){
			alert('비밀번호를 입력하세요');
			return;
		}	
		if(valid_password.test(password.value)===false){
			alert('비밀번호 형식이 올바르지 않습니다.\n(영문 대/소문자 하나이상 포함, 특수문자 하나이상 포함)\n(8자 이상 16자 이하)');
			return;
		}
		
		if(password.value !== checkPassword.value){
			alert('비밀번호가 일치하지 않습니다.');
			return;
		}
		
		$.ajax({
            method:"POST",    //GET/POST
            url:"/ehr/user/changePw", //서버측 URL
            dataType:"json",//서버에서 받을 데이터 타입
            data:{          //파라메터
          	  "userNo" : '${sessionScope.loginUser.userNo}',
              "password" : password.value
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
            	alert('서버와의 문제가 생겼습니다.\n다음에 다시 시도해 주세요');
            }
            
            
        });
	});
	
	
  });
  </script>
</head>
<body>
    <!-- Header -->
    <!-- Header -->
    <div class="header bg-gradient-warning py-7 py-lg-6 pt-lg-6">
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
          <div class="card-header">
             <div class="col-8">
                <h3 class="mb-0">프로필 이미지 </h3>
              </div>
            </div>
            <img src="/ehr/resources/template/dashboard/assets/img/theme/white.png" style="max-height: 100px;" alt="Image placeholder" class="card-img-top">
            <div class="row justify-content-center">
              <div class="col-lg-3 order-lg-2">
                <div class="card-profile-image">
	               <img id="profile" src="/ehr/resources/image/profile/${sessionScope.loginUser.image }" class="rounded-circle" style="object-fit: cover;">
                </div>
              </div>
            </div>
            <div class="card-header text-center border-0 pt-8 pt-md-4 pb-0 pb-md-4">
            </div>
            <div class="card-body pt-0">
              <div class="row">
                <div class="col">
                  <div class="card-profile-stats d-flex justify-content-center">
                  </div>
                </div>
              </div>
              <div class="text-center">
                <h5 class="h3">
                  ${sessionScope.loginUser.name }
                </h5>
              </div>
            </div>
          </div>
         </div>
        <div class="col-xl-8 order-xl-1">
          <div class="card">
            <div class="card-header">
              <div class="row align-items-center">
                <div class="col-8">
                  <h3 class="mb-0">비밀번호 변경 </h3>
                </div>
                <div class="col-4 text-right">
                  <button class="btn btn-lg btn-default" id="updateButton">변경</button>    
                </div>
              </div>
            </div>
            <div class="card-body">
                <h6 class="heading-small text-muted mb-4">회원 정보</h6>
                <div class="pl-lg-4">
                  <div class="row">
                    <div class="col-lg-6">
                      <div class="form-group">
                        <label class="form-control-label" for="password">변경할 비밀번호</label>
                        <input type="password" id="password" name="password" class="form-control">
                      </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col-lg-6">
                      <div class="form-group">
                        <label class="form-control-label" for="checkPassword">비밀번호 확인</label>
                        <input type="password" id="checkPassword" name="checkPassword" class="form-control">
                      </div>
                    </div>
                  </div>
                </div>				
            </div>
          </div>
        </div>
      </div>
    </div>

</body>

</html>