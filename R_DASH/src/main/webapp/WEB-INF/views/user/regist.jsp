<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="Start your development with a Dashboard for Bootstrap 4.">
  <meta name="author" content="Creative Tim">
  <title>회원가입</title>
 <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
<script>
document.addEventListener('DOMContentLoaded',function(){
	 //회원가입form
	 const regForm = document.querySelector("#regForm");
	//이름 input
	 const nameInput = document.querySelector("#name");
	 //이메일 input
	 const emailInput = document.querySelector("#email");
	 //비밀번호 input
	 const passwordInput = document.querySelector("#password");
	//우편번호 input
	 const zipCodeInput = document.querySelector("#zipCode");
	//주소 input
	 const addressInput = document.querySelector("#address");
	//숭사주소 input
	 const detailAddressInput = document.querySelector("#detailAddress");
	
	 //이름 숫자 특수문자 불가
	 const valid_name =	/^[A-Za-z가-힣]+$/
	 //이메일 형식 체크
	 const valid_email = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;
	 //비밀번호 형식 체크 (영문 대/소문자 하나이상 포함, 특수문자 하나이상 포함)
	 const valid_password = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+~`=\-{}\[\]:;"'<>,.?\/\\])\S{8,16}$/;
	 
	 //이메일 체크 버튼
     const checkEmailButton = document.querySelector("#checkEmailButton");
	 
     const duplicateInput = document.querySelector("#duplicate");
	 
	 regForm.addEventListener("submit",function(event){
		 event.preventDefault(); // 실제 폼 제출 막음
		 
		 if(nameInput.value === ''){
			 alert('이름을 입력하세요.');
			 nameInput.focus();
			 return;
		 }

		 if(emailInput.value === ''){
			 alert('이메일을 입력하세요.');
			 emailInput.focus();
			 return;
		 }

		 
		 
		 if(passwordInput.value === ''){
			 alert('비밀번호를 입력하세요.');
			 passwordInput.focus();
			 return;
		 }
		 
		 if(valid_name.test(nameInput.value)===false){
			 alert('이름에 영문,한글만 사용 가능합니다.');
			 nameInput.focus();
			 return;
		 }
		 
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
		 
		 if(duplicateInput.innerText !== '가능한 이메일입니다.'){
			 alert('메일 중복을 확인하세요.');
			 
			 return;
		 }
		 
		 
		 $.ajax({
	            method:"POST",    //GET/POST
	            url:"/ehr/user/regist", //서버측 URL
	            dataType:"json",//서버에서 받을 데이터 타입
	            data:{          //파라메터
	            	"name": nameInput.value,
	                "email": emailInput.value,
	                "password":passwordInput.value,
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
function checkEmail(){
	//이메일 input
    const emailInput = document.querySelector("#email");
    const duplicateInput = document.querySelector("#duplicate");
    //이메일 형식 체크
    const valid_email = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;
    
    if(emailInput.value === ''){
    	alert('이메일을 입력하세요');
    	
    	return;
    }
    
    if(valid_email.test(emailInput.value)===false){
        alert('이메일 형식이 올바르지 않습니다.');
        emailInput.focus();
        
        return;
    }
    
	$.ajax({
        method:"POST",    //GET/POST
        url:"/ehr/user/checkEmail", //서버측 URL
        dataType:"json",//서버에서 받을 데이터 타입
        data:{          //파라메터
            "email": emailInput.value
        },
        success:function(result){//요청 성공
            if(false === result.success){
                duplicateInput.textContent = result.message;
                duplicateInput.style.color = "red";
                return;
            }else{
            	duplicateInput.textContent = result.message;
            	duplicateInput.style.color = "green";
            	return;
            } 
                
        },
        error:function(result){//요청 실패
            console.log("error:"+result)
            alert(result);
        }
        
        
    });
}


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
<body class="bg-white">
  <!-- Main content -->
  <div class="main-content">
    <!-- Header -->
    <div class="header bg-gradient-warning py-7 py-lg-8 pt-lg-9">
      <div class="container">
        <div class="header-body text-center mb-7">
          <div class="row justify-content-center">
            <div class="col-xl-5 col-lg-6 col-md-8 px-5">
              <h1 class="text-white">회원가입</h1>
              <p class="text-lead text-white">아래 양식을 작성하여 새 계정을 만들어보세요!</p>
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
    <div class="container mt--9 pb-5">
      <!-- Table -->
      <div class="row justify-content-center">
        <div class="col-lg-6 col-md-8">
          <div class="card bg-secondary border border-soft">
            <div class="card-header bg-transparent pb-5">
              <div class="text-muted text-center mt-2 mb-4"><small>소셜 회원가입</small></div>
              <div class="text-center">
                <a href="#" class="btn btn-neutral btn-icon mr-4">
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
              <div class="text-center text-muted mb-4">
                <small>계정을 생성하세요!</small>
              </div>
              <form method="post" id="regForm">
                <div class="form-group">
                  <div class="input-group input-group-merge input-group-alternative mb-3">
                    <div class="input-group-prepend">
                      <span class="input-group-text"><i class="ni ni-hat-3"></i></span>
                    </div>
                    <input class="form-control" name="name" id="name" placeholder="이름" type="text">
                  </div>
                </div>
                <div class="form-group">
                  <div class="input-group input-group-merge input-group-alternative mb-3">
                    <div class="input-group-prepend">
                      <span class="input-group-text"><i class="ni ni-email-83"></i></span>
                    </div>
                    <input class="form-control" name="email" id="email" placeholder="이메일" type="email">
                    <input type="button" onclick="checkEmail()" id="checkEmailButton" class="btn btn-default btn-sm" value="메일중복 확인" style="margin-left: 10px;">
                  </div>
                </div>
                <div id="duplicate" >
                
                </div>
                <div class="form-group">
                  <div class="input-group input-group-merge input-group-alternative">
                    <div class="input-group-prepend">
                      <span class="input-group-text"><i class="ni ni-lock-circle-open"></i></span>
                    </div>
                    <input class="form-control" name="password" id="password" placeholder="비밀번호" type="password">
                  </div>
                </div>
                <div class="form-group">
                  <div class="input-group input-group-merge input-group-alternative">
                  	<div class="input-group-prepend" >
                      <span class="input-group-text" style="background-color:#ededed"><i class="ni ni-world-2"></i></span>
                    </div>
					  <input type="text" class="form-control"" name="zipCode" id="zipCode" placeholder="우편번호(선택사항)" readonly>
					  <input type="button" class="btn btn-default btn-sm" onclick="searchAddress()" value="우편번호 찾기" style="margin-left: 10px;">
                  </div>
                   	  
                </div>
                <div class="form-group">
                  <div class="input-group input-group-merge input-group-alternative">
                	  <div class="input-group-prepend">
                      <span class="input-group-text" style="background-color:#ededed"><i class="ni ni-align-left-2"></i></span>
                    </div>
					  <input type="text" class="form-control" id="address" name="address" placeholder="주소(선택사항)" readonly>
                  </div>
                </div>
                <div class="form-group">
                  <div class="input-group input-group-merge input-group-alternative">
                 	 <div class="input-group-prepend">
                      <span class="input-group-text"><i class="ni ni-align-left-2"></i></span>
                    </div>
					  <input type="text" class="form-control" id="detailAddress" name="detailAddress" placeholder="상세주소(선택사항)">
                  </div>
                </div>
                <div class="text-center">
                  <button type="submit" id="regButton"  class="btn btn-default mt-4">회원가입</button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</body>

</html>