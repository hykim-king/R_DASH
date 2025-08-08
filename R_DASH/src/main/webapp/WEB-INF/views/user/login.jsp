<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="Start your development with a Dashboard for Bootstrap 4.">
  <meta name="author" content="Creative Tim">
  <title>Login</title>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  <script>
  document.addEventListener('DOMContentLoaded',function(){
	 //로그인form 
	 const loginForm = document.querySelector("#loginForm");
	 //이메일 input
	 const emailInput = document.querySelector("#email");
	 //비밀번호 input
	 const passwordInput = document.querySelector("#password");
	 //이메일 형식 체크
	 const valid_email = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;
	 //비밀번호 형식 체크 (영문 대/소문자 하나이상 포함, 특수문자 하나이상 포함)
	 const valid_password = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+~`=\-{}\[\]:;"'<>,.?\/\\])\S{8,16}$/;
	 
	 loginForm.addEventListener("submit",function(event){
		 event.preventDefault(); // 실제 폼 제출 막음
		 
		 if(emailInput.value === ''){
			 alert('이메일을 입력하세요');
			 emailInput.focus();
			 return;
		 }

		 
		 
		 if(passwordInput.value === ''){
			 alert('비밀번호를 입력하세요');
			 passwordInput.focus();
			 return;
		 }
		 //관리자일경우 pass
		 if(emailInput.value !== 'admin'){
			 
			 if(valid_email.test(emailInput.value)===false){
				 alert('이메일 형식이 올바르지 않습니다.');
				 emailInput.focus();
				 
				 return;
			 }
			 
			 if(valid_password.test(passwordInput.value)===false){
				 alert('비밀번호 형식이 올바르지 않습니다.\n(영문 대/소문자 하나이상 포함, 특수문자 하나이상 포함)\n(8자 이상 16자 이하)');
				 passwordInput.focus();
				 return;
			 }
		 
		 }
		 
		 
		 
		 $.ajax({
	            method:"POST",    //GET/POST
	            url:"/ehr/user/login", //서버측 URL
	            dataType:"json",//서버에서 받을 데이터 타입
	            data:{          //파라메터
	                "email": emailInput.value,
	                "password":passwordInput.value
	            },
	            success:function(result){//요청 성공
	                if(false === result.success){
	                	alert(result.message);
	  
	                	return;
	                }else{
	                	alert(result.message);
	                	
	                	//로그인 페이지로 이동(임시)
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
  </script>
</head>
<body class="bg-white">
  <!-- Main content -->
  <div class="main-content">
    <!-- Header -->
    <div class="header bg-gradient-warning py-7 py-lg-8 pt-lg-9">
      <div class="container">
        <div class="header-body text-center mb-7">
          <div class="row justify-content-center">
            <div class="col-xl-5 col-lg-6 col-md-8 px-5">
              <h1 class="text-white">Welcome!</h1>
              <p class="text-lead text-white">Use these awesome forms to login or create new account in your project for free.</p>
            </div>
          </div>
        </div>
      </div>
      <div class="separator separator-bottom separator-skew zindex-100">
        <svg x="0" y="0" viewBox="0 0 2560 100" preserveAspectRatio="none" version="1.1" xmlns="http://www.w3.org/2000/svg">
          <polygon class="fill-white" points="2560 0 2560 100 0 100"></polygon>
        </svg>
      </div>
    </div>
    <!-- Page content -->
    <div class="container mt--9 pb-5 text-gray">
      <div class="row justify-content-center">
        <div class="col-lg-5 col-md-7">
          <div class="card bg-secondary border border-soft mb-0">
            <div class="card-header bg-transparent pb-5">
              <div class="text-center mt-2 mb-3"><small>Sign in with</small></div>
              <div class="btn-wrapper text-center">
                <a href="#" class="btn btn-neutral btn-icon">
                  <span class="btn-inner--icon"><img src="/ehr/resources/template/dashboard/assets/img/icons/common/github.svg"></span>
                  <span class="btn-inner--text">Github</span>
                </a>
                <a href="#" class="btn btn-neutral btn-icon">
                  <span class="btn-inner--icon"><img src="/ehr/resources/template/dashboard/assets/img/icons/common/google.svg"></span>
                  <span class="btn-inner--text">Google</span>
                </a>
              </div>
            </div>
            <div class="card-body px-lg-5 py-lg-5">
              <div class="text-center mb-4">
                <small>Or sign in with credentials</small>
              </div>
              <form method="post" id="loginForm">
                <div class="form-group mb-3">
                  <div class="input-group input-group-merge input-group-alternative">
                    <div class="input-group-prepend">
                      <span class="input-group-text"><i class="ni ni-email-83"></i></span>
                    </div>
                    <input class="form-control" placeholder="Email" id="email" name="email">
                  </div>
                </div>
                <div class="form-group">
                  <div class="input-group input-group-merge input-group-alternative">
                    <div class="input-group-prepend">
                      <span class="input-group-text"><i class="ni ni-lock-circle-open"></i></span>
                    </div>
                    <input class="form-control" placeholder="Password" id="password" type="password" name="password">
                  </div>
                </div>
                <div class="text-center">
                  <button type="submit" id="loginButton" class="btn btn-default my-4">login</button>
                </div>
              </form>
            </div>
          </div>
          <div class="row mt-3">
            <div class="col-6">
              <a href="#" class="text-gray"><small>비밀번호 찾기</small></a>
            </div>
            <div class="col-6 text-right">
              <a href="regist" class="text-gray"><small>회원가입</small></a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>