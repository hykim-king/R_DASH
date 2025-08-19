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
    <script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2"></script>
    <script src="${pageContext.request.contextPath}/resources/js/stats.js"></script>
    <style>
        .canvasContainer {
            width: 700px; 
            height: 500px;
            margin-top: 20px;
        }
        
        #top5Table, #bottom5Table {
		    width: 100%;  /* col 안에서 최대 폭 사용 */
		    min-width: 300px; /* 너무 작아지지 않도록 최소 폭 지정 */
		}
	
		#fireTables {
		    margin-top: 50px; /* 차트와 겹치지 않게 */
		}
		
		/* dust avg 카드 */
		.card {
            display: inline-block;
            padding: 20px;
            margin: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0,0,0,0.2);
            font-size: 18px;
            font-weight: bold;
        }
        .very-bad { background-color: #e74c3c; color: black; }
        .bad { background-color: #f39c12; color: black; }
        .normal { background-color: #d0fb50; color: black; }
        .good { background-color: #0ed13c; color: black; }
        table { border-collapse: collapse; table-layout: fixed; width: 50%; margin-bottom: 50px; }
        th, td { border: 1px solid #ccc; padding: 8px; text-align: center; }
        th { background-color: coral; color: black; }
        
        body {
		    display: flex;
		    flex-direction: column;
		    min-height: 100vh; /* 화면 전체 높이 확보 */
		}
		
		.body-container {
		    width: 80%;
		    margin: 0 auto; /* 상하 0, 좌우 자동 중앙 정렬 */
		}
		
		.main-container {
		    width: 100%; /* 또는 원하는 너비 */
		    margin: 0 auto; /* 부모 안에서 중앙 정렬 */
		}
		
		main {
		    flex: 1; /* main 영역이 남은 공간을 차지하도록 */
		    padding-bottom: 80px; /* footer 높이만큼 여백 확보 */
		}
		
		footer {
		    height: 80px;
		}
		
		.menuChart {
		  position: relative;
		  width: 280px;      
		  height: 280px;
		  overflow: visible;
		  z-index: 1;
		}
    </style>
</head>
<body>
<div class="body-container">
<c:set var="type" value="${not empty param.type ? param.type : (not empty pageType ? pageType : 'weather')}" />
<div class="menuChart mt--7">
    <canvas id="SelectTemperature"></canvas>
</div>
<div class="main-container">
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
        <div class="row mb-7">
		    <div class="col-md-6">
		      <canvas id="fireSafeChart"></canvas>
		    </div>
		    <div class="col-md-6 mt-n5 d-flex justify-content-end"> 
			    <div style="width: 80%; height: 450px;">
			      <canvas id="yearlyChart"></canvas>
			    </div>
			  </div>
		  </div>
		
		  <div class="row mb-7">
		    <div class="col-md-6">
		      <canvas id="damageChart"></canvas>
		    </div>
		    <div class="col-md-6">
		      <canvas id="subwayChart"></canvas>
		    </div>
		  </div>
		
		  <!-- 데이터테이블 -->
		  <div class="row">
		    <div class="col-12">
		      <h2>시/군/구별 소방서 개수</h2>
		      <table id="firestationTable" class="table table-bordered display">
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
		    </div>
		  </div>
    <script src="${pageContext.request.contextPath}/resources/js/fire.js"></script>
    </c:when>
    <c:when test="${type == 'dust'}">
        <div class="row mb-6">
            <div class="col-md-12">
	           <div id="avgCard" class="card"></div>
	        </div>
	    </div>
	    <div class="row mb-6">
            <div class="col-md-6 canvasContainer">
                <h2>TOP 5 지역</h2>
		        <table id="top5Table">
		            <tr>
		                <th>순위</th>
		                <th>지역</th>
		                <th>PM10</th>
		                <th>등급</th>
		            </tr>
		        </table>
            </div>
            <div class="col-md-6" style="margin-top: 19px;">
                <h2>BOTTOM 5 지역</h2>
		        <table id="bottom5Table">
		            <tr>
		                <th>순위</th>
		                <th>지역</th>
		                <th>PM10</th>
		                <th>등급</th>
		            </tr>
		        </table>
            </div>
        </div> 
	<script src="${pageContext.request.contextPath}/resources/js/dust.js"></script>
    </c:when>
</c:choose>
</div>
</div>
</body>
</html>


