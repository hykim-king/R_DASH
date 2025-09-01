<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="Start your development with a Dashboard for Bootstrap 4.">
  <meta name="author" content="Creative Tim">
  <title>비밀번호 찾기</title>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
  <script>
  let codeTimer = null;
  
  function sendMail(){
	  const emailInput = document.querySelector('#email');
	  const codeBox = document.querySelector('#codeBox');
	  //이메일 형식 체크
	  const valid_email = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;
	  
      if(emailInput.value === ''){
          alert('이메일을 입력하세요.');
          emailInput.focus();
          return;
      }
      
      if(valid_email.test(emailInput.value)===false){
          alert('이메일 형식이 올바르지 않습니다.');
          emailInput.focus();
          
          return;
      }
      
      codeBox.style.display='block'; 
      emailInput.readOnly = true;
      document.querySelector('#sendMailButton').disabled = true;
	  
	  $.ajax({
	        method:"POST",    //GET/POST
	        url:"/ehr/user/sendMail", //서버측 URL
	        dataType:"json",//서버에서 받을 데이터 타입
	        data:{          //파라메터
	            "email": emailInput.value
	        },
	        success:function(result){//요청 성공

	             // 3분 카운트다운
	            let t = result.expiresInSec||180;
	            codeTimer = setInterval(()=>{
	              const timerEl = document.querySelector('#timer');
	              const m = Math.floor(t/60);
	              const s = String(t%60).padStart(2,'0');
	              timerEl.textContent = `남은 시간: \${m}:\${s}`;
	              timerEl.style.color = 'red';
	              if (--t < 0) { 
	            	  clearInterval(codeTimer); 
	            	  timerEl.textContent = '만료됨';
	            	  
	              }
	            }, 1000);
	            
	        },
	        error:function(result){//요청 실패
	        	alert('서버와의 문제가 생겼습니다.\n다음에 다시 시도해 주세요');
	        }
	        
	        
	    });
  }
  function verify(){
	  const emailInput = document.querySelector('#email');
	  const codeInput = document.querySelector('#code');
	  const buttonBox = document.querySelector('#buttonBox')
	  $.ajax({
          method:"POST",    //GET/POST
          url:"/ehr/user/verify", //서버측 URL
          dataType:"json",//서버에서 받을 데이터 타입
          data:{          //파라메터
              "email": emailInput.value,
              "code" : codeInput.value
          },
          success:function(result){//요청 성공
        	  if(result.success === true){
        		  buttonBox.style.display='block';
        		  codeInput.readOnly = true;
        		  clearInterval(codeTimer); 
        		  document.querySelector('#verifyButton').disabled = true;
        		  document.querySelector('#timer').textContent = '인증';
                  document.querySelector('#timer').style.color = 'green';
        		  return;
        	  }else{
        		  alert(result.message);
        		  return;
        	  }
        		                 
          },
          error:function(result){//요청 실패
        	  alert('서버와의 문제가 생겼습니다.\n다음에 다시 시도해 주세요');
          }
          
          
      });
  }
  
  function sendResetPw(){
	  const emailInput = document.querySelector('#email');
	  document.querySelector('#resetButton').disabled = true;
	  $.ajax({
          method:"POST",    //GET/POST
          url:"/ehr/user/resetPw", //서버측 URL
          dataType:"json",//서버에서 받을 데이터 타입
          data:{          //파라메터
              "email": emailInput.value,
          },
          success:function(result){//요청 성공
              alert(result.message);
              window.location.href = "/ehr/user/login";
          },
          error:function(result){//요청 실패
        	  alert('서버와의 문제가 생겼습니다.\n다음에 다시 시도해 주세요');
          }
          
          
      });
  }
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
              <h1 class="text-white">비밀번호 찾기</h1>
              <p class="text-lead text-white">이메일 인증으로 비밀번호 찾기! <br>즐거운 하루 되세요!</p>
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
                 <small>이메일을 인증해주세요!</small> 
              </div>
                <div class="form-group mb-3" id="emailBox">
                  <div class="input-group input-group-merge input-group-alternative">
                    <div class="input-group-prepend">
                      <span class="input-group-text"><i class="ni ni-email-83"></i></span>
                    </div>
                    <input class="form-control" placeholder="이메일" id="email" name="email" maxlength="30">
                    <input type="button" onclick="javascript:sendMail()" id="sendMailButton" class="btn btn-default btn-sm" value="메일 전송" style="margin-left: 10px;">
                  </div>
                </div>
                <div class="form-group" style="display:none;" id="codeBox">
                  <div class="input-group input-group-merge input-group-alternative">
                    <div class="input-group-prepend">
                      <span class="input-group-text"><i class="ni ni-lock-circle-open"></i></span>
                    </div>
                    <input class="form-control" placeholder="인증 코드" id="code" name="code" maxlength="6">
                    <input type="button" id="verifyButton" onclick="javascript:verify()" class="btn btn-default btn-sm" value="코드 인증" style="margin-left: 10px;">
                  </div>
                  <div id="timer">
                  
                  </div>
                </div>
                <div class="text-center" style="display:none;" id="buttonBox">
                  <button onclick="javascript:sendResetPw()" id="resetButton" class="btn btn-default my-4">비밀번호 찾기</button>
                </div>
            </div>
          </div>
          <div class="row mt-3">
            <div class="col-6">
              <a href="login" class="text-gray"><small>로그인</small></a>
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