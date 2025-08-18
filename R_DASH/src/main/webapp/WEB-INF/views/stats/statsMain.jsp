<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>통계 페이지</title>
    <!-- DataTables JS -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<!-- DataTables CSS -->
	<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.13.6/css/jquery.dataTables.min.css"/>
	<script type="text/javascript" src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        .canvasContainer {
            width: 500px; 
            height: 300px;
        }
    </style>
</head>
<body>
<c:set var="type" value="${not empty param.type ? param.type : (not empty pageType ? pageType : 'weather')}" />
<c:choose>
    <c:when test="${type == 'weather'}">
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
		
		<h3>기온(T1H) Top5</h3>
		<table border="1">
		    <thead>
		        <tr>
		            <th>순위</th>
		            <th>지역</th>
		            <th>기온(℃)</th>
		        </tr>
		    </thead>
		    <tbody id="t1hTable">
		        <!-- JS에서 채움 -->
		    </tbody>
		</table>
		
		<h3>습도(REH) & 강수량(RN1) Top5</h3>
		<table border="1">
		    <thead>
		        <tr>
		            <th>순위</th>
		            <th>지역</th>
		            <th>습도(%)</th>
		            <th>강수량(mm)</th>
		        </tr>
		    </thead>
		    <tbody id="rehRn1Table">
		        <!-- JS에서 채움 -->
		    </tbody>
		</table>
		
		<script src="${pageContext.request.contextPath}/resources/js/patients.js"></script>
		<script src="${pageContext.request.contextPath}/resources/js/nowcast.js"></script>
	</c:when>
	<c:when test="${type == 'fire'}">
        <h2>시/군/구별 소방서 개수</h2>
		
		<table id="firestationTable" class="display" border="1">
		    <thead>
		        <tr>
		            <th>순위</th>
		            <th>지역</th>
		            <th>소방서 개수</th>
		        </tr>
		    </thead>
		    <tbody>
		        <!-- AJAX로 채워짐 -->
		    </tbody>
		</table>
        <script src="${pageContext.request.contextPath}/resources/js/fire.js"></script>
        
        <div class = "canvasContainer">
		    <canvas id="fireSafeChart"></canvas>
		</div>
    </c:when>
</c:choose>
</body>
</html>

