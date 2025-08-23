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
            height: 400px;
            margin-top: 20px;
        }
        
		canvas {
		  aspect-ratio: auto !important;
		}
        
		.topTable {
		    width: 100%;
		    min-width: 300px;
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
		    background-color: #fff !important;
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
		
		.stateChart {
		    width: 40%;   
		    margin: 0 auto; /* 좌우 가운데 정렬 */
		}
		
		
		.groupselect select {
		    padding: 7px 14px;
		    border-radius: 5px;
		    border: 2px solid #a3a3a3;
		    font-size: 14px;
		    background-color: #ffffff;
		    margin-right: 10px; 
		    margin-bottom: 15px; 
		}
		
		/* 드롭다운 영역 (Show entries) */
		.dataTables_length {
		  font-size: 14px;
		  margin-bottom: 10px;
		  padding-left: 0px !important;
		}
		
		/* 드롭다운 select 기본 스타일 */
		.dataTables_length select {
		  border: 2px solid #ccc;
		  border-radius: 5px !important;
          border-color: coral !important;
          background-color: #fff !important;
		  padding: 4px 8px;
		  font-size: 14px;
		}
		
		/* 검색창 */
		.dataTables_filter input {
		  border: 1px solid #ccc;
		  border-radius: 5px;
		  padding: 4px;
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
        <div class = "groupselect">
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
		</div>
		<!-- 환자 전체 합계 -->
		<h1>온열질환자</h1>
		<div class="row mb-2">
            <div class="col-md-6 canvasContainer">
	            <canvas id="patientsChart"></canvas>
	        </div>
	        <div class="col-md-6 canvasContainer">
                <canvas id="inOutdoorChart"></canvas>
            </div>
		</div>
		<div class="row mb-6">
		  <div class="col-md-6">
			<h2>기온 Top5</h2>
	        <table class="topTable" border="1">
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
	      </div>
	      <div class="col-md-6">
	        <h2>습도와 강수량 Top5</h2>
	        <table class="topTable" border="1" >
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
	      </div>
		</div>
		<script src="${pageContext.request.contextPath}/resources/js/patients.js"></script>
		<script src="${pageContext.request.contextPath}/resources/js/nowcast.js"></script>
	</c:when>
	<c:when test="${type == 'fire'}">
        <div class="row mb-6">
		    <div class="col-md-6 canvasContainer">
		      <canvas id="fireSafeChart"></canvas>
		    </div>
		    <div class="col-md-6 mt-n5 d-flex justify-content-end canvasContainer"> 
			    <div style="width: 80%; height: 450px;">
			      <canvas id="yearlyChart"></canvas>
			    </div>
			  </div>
		  </div>
		
		  <div class="row mb-6">
		    <div class="col-md-6 canvasContainer">
		      <canvas id="damageChart"></canvas>
		    </div>
		    <div class="col-md-6 canvasContainer">
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
            <div class="col-md-6">
                <h2>TOP 5 지역</h2>
		        <table id="top5Table" class="topTable">
		            <tr>
		                <th>순위</th>
		                <th>지역</th>
		                <th>PM10</th>
		                <th>등급</th>
		            </tr>
		        </table>
            </div>
            <div class="col-md-6">
                <h2>BOTTOM 5 지역</h2>
		        <table id="bottom5Table" class="topTable">
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
    <c:when test="${type == 'sinkhole'}">
    <div class="row mb-7">
        <div class="col-md-6 canvasContainer">
		    <canvas id="yearlyChart"></canvas>
		</div>
		<div class="col-md-6 canvasContainer">
            <canvas id="signguChart"></canvas>
        </div>
        
	</div>
	<div class="row mb-7">
	    <div class="col-md-6 canvasContainer">
            <canvas id="monthlyChart"></canvas>
        </div>
        <div class="col-md-6 canvasContainer">
            <canvas id="damageChart"></canvas>
        </div>
    </div>
    <div class="row mb-7">
        <div class="stateChart">
            <canvas id="stateChart"></canvas>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/resources/js/sinkhole.js"></script>
    </c:when>
    <c:when test="${type == 'landslide'}">
    <div class="row mb-6">
        <div class="col-md-6 canvasContainer">
            <canvas id="yearChart"></canvas>
        </div>
        <div class="col-md-6 canvasContainer">
            <canvas id="monthChart"></canvas>
        </div>
        
    </div>
    <div class="row mb-6">
        <div class="col-md-6 canvasContainer">
            <canvas id="regionChart"></canvas>
        </div>
        <div class="col-md-6 canvasContainer">
            <canvas id="statusChart"></canvas>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/resources/js/landslide.js"></script>
    </c:when>
</c:choose>
</div>
</div>
</body>
</html>


