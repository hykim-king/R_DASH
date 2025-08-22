<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 접근 제한</title>
<script>
    alert('로그인 시에만 접근 가능합니다.');
    setTimeout(function(){
    location.href = '/ehr/main.do';  // 메인 페이지 URL로 이동
    }, 100);
</script>
</head>
<body>
</body>
</html>