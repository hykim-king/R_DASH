<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="Start your development with a Dashboard for Bootstrap 4.">
  <meta name="author" content="Creative Tim">
  <title>로그인</title>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  <script>
  document.addEventListener('DOMContentLoaded',function(){
	 //로그인form 
	 const loginForm = document.querySelector("#loginForm");
	 //이메일 input
	 const emailInput = document.querySelector("#email");
	 //비밀번호 input
	 const passwordInput = document.querySelector("#password");

	 loginForm.addEventListener("submit",function(event){
		 event.preventDefault(); // 실제 폼 제출 막음
		 
		 if(emailInput.value === ''){
			 alert('이메일을 입력하세요');
			 emailInput.focus();
			 return;
		 } 
		 if(passwordInput.value === ''){
             alert('비밀번호를 입력하세요');
             emailInput.focus();
             return;
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
	                	window.location.href='/ehr/user/myPage';
	                } 
	                	
	            },
	            error:function(){//요청 실패
	                alert('서버와의 문제가 생겼습니다.\n다음에 다시 시도해 주세요');
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
              <h1 class="text-white">환영합니다!</h1>
              <p class="text-lead text-white">재민이 사이트를 찾아주셔서 감사합니다! <br>즐거운 하루 되세요!</p>
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
              <div class="text-center mt-2 mb-3"><small>소셜 로그인</small></div>
              <div class="btn-wrapper text-center">
                <a href="/ehr/oauth2/authorization/kakao" class="btn btn-neutral btn-icon">
                  <span class="btn-inner--icon"><img src="/ehr/resources/template/dashboard/assets/img/icons/common/kakao.png"></span>
                  <span class="btn-inner--text">Kakao</span>
                </a>
                <a href="/ehr/oauth2/authorization/google" class="btn btn-neutral btn-icon">
                  <span class="btn-inner--icon"><img src="/ehr/resources/template/dashboard/assets/img/icons/common/google.svg"></span>
                  <span class="btn-inner--text">Google</span>
                </a>
              </div>
            </div>
            <div class="card-body px-lg-5 py-lg-5">
              <div class="text-center mb-4">
                 <small>가입한 계정으로 로그인하세요!</small> 
              </div>
              <form method="post" id="loginForm">
                <div class="form-group mb-3">
                  <div class="input-group input-group-merge input-group-alternative">
                    <div class="input-group-prepend">
                      <span class="input-group-text"><i class="ni ni-email-83"></i></span>
                    </div>
                    <input class="form-control" placeholder="이메일" id="email" name="email" maxlength="30">
                  </div>
                </div>
                <div class="form-group">
                  <div class="input-group input-group-merge input-group-alternative">
                    <div class="input-group-prepend">
                      <span class="input-group-text"><i class="ni ni-lock-circle-open"></i></span>
                    </div>
                    <input class="form-control" placeholder="비밀번호" id="password" type="password" name="password" maxlength="16">
                  </div>
                </div>
                <div class="text-center">
                  <button type="submit" id="loginButton" class="btn btn-default my-4">로그인</button>
                </div>
              </form>
            </div>
          </div>
          <div class="row mt-3">
            <div class="col-6">
              <a href="findPw" class="text-gray"><small>비밀번호 찾기</small></a>
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