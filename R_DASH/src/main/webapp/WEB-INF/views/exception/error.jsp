<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="Start your development with a Dashboard for Bootstrap 4.">
  <meta name="author" content="Creative Tim">
  <title>오류</title>
</head>
<body>
  <!-- Main content -->
  <div class="main-content" style="margin-top:300px">
    <!-- Page content -->
    <div class="container mt--9 pb-5 text-gray">
      <div class="row justify-content-center">
        <div class="col-lg-5 col-md-7">
          <div class="card bg-secondary border border-soft mb-0">
            <div class="card-header bg-transparent pb-5">
              <img src="/ehr/resources/image/error.png" alt="에러 아이콘" class="error-image" style="width:100%; height:95%; align:center;">
			    <h1>에러 코드 : ${status }</h1>
			    <p>내용 : ${message }</p>
                <div class="text-center">
                  <button type="button" onclick="location.href='/ehr/home'" id="loginButton" class="btn btn-default my-4">메인으로 이동</button>
                </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</body>
</html>