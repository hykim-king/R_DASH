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