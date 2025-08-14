<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>통계 페이지</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        .canvasContainer {
            width: 500px; 
            height: 300px;
        }
    </style>
</head>
<body>

<!-- 그룹 타입 선택 -->
<select id="groupType">
    <option value="region">지역별</option>
    <option value="year">년도별</option>
</select>

<!-- 년도 선택 (항상 DOM에 존재) -->
<select id="yearSelect">
    <option value="">전체년도</option>
    <c:forEach var="year" items="${yearList}">
        <option value="${year}">${year}</option>
    </c:forEach>
</select>

<!-- 지역 선택 (항상 DOM에 존재) -->
<select id="sidoSelect">
    <option value="">전체지역</option>
    <c:forEach var="sido" items="${sidoList}">
        <option value="${sido}">${sido}</option>
    </c:forEach>
</select>

<!-- 환자 전체 합계 -->
<div class = "canvasContainer">
    <canvas id="patientsChart"></canvas>
</div>

<!-- 환자 실내외 소계 -->
<div class = "canvasContainer">
    <canvas id="inOutdoorChart"></canvas>
</div>

<select id="dataTypeSelect">
    <option value="TEMP">기온</option>
    <option value="HUM">습도</option>
    <option value="RAIN">강수량</option>
</select>

<canvas id="top5Chart" width="400" height="200"></canvas>

<script src="${pageContext.request.contextPath}/resources/js/patients.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/nowcast.js"></script>

</body>
</html>

