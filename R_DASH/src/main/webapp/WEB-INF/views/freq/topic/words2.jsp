<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>오늘의 키워드</title>
<!-- DataTables JS -->
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<!-- DataTables CSS -->
<link href="/ehr/resources/template/dashboard/css/dashboard.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/nucleo/css/nucleo.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/nucleo/css/nucleo-svg.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/@fortawesome/fontawesome-free/css/all.min.css" rel="stylesheet">
<!-- D3.js -->
<script src="https://d3js.org/d3.v3.min.js"></script>
<script src="https://rawgit.com/jasondavies/d3-cloud/master/build/d3.layout.cloud.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2"></script>
<style>
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
     .canvasContainer {
            width: 700px; 
            height: 500px;
            margin-top: 20px;
        }
     .canvasContainer_half {
            width: 400px; 
            height: 300px;
            margin-top: 20px;
        }
</style>
</head>
<body>
<script type="text/javascript"> 
  // 워드 클라우드 넣을 데이터 top100
  // JSP에서 서버 데이터를 JS 변수로 변환
  var wordData = [
    <c:forEach var="c" items="${cloud}" varStatus="status">
      {
        text: "${c.word}",
        freq: <c:out value="${c.freq}" default="0"/>
      }<c:if test="${!status.last}">,</c:if>    
    </c:forEach>    
  ];
    
  console.log(wordData);
  //워드 클라우드 강조할 문자 top5(changeRate에서 가져옴)
  var keywords = [
	  <c:forEach var="r" items="${rate}" varStatus="status">
      {
        text: "${r.word}"
      }<c:if test="${!status.last}">,</c:if>      
    </c:forEach>
  ];
  console.log(keywords);
  var keywordTexts = keywords.map(function(d){ return d.text; }); 
  console.log(keywordTexts);
  
  //top5 단어를 일일 증감율 보여줌
  var changeRate = [
	  <c:forEach var="r" items="${rate}" varStatus="status">
      {
        text: "${r.word}",
        changeRate: <c:out value="${r.changeRate}" default="0"/>
      }<c:if test="${!status.last}">,</c:if>    
    </c:forEach>    
  ];
  console.log(changeRate);
  //top10 막대 그래프  
  var top10Data = [
	  <c:forEach var="t" items="${top10}" varStatus="status">
      {
        text: "${t.word}",
        freq: <c:out value="${t.freq}" default="0"/>
      }<c:if test="${!status.last}">,</c:if>    
    </c:forEach>   
  ];
  console.log(top10Data);
</script>
<script src="${pageContext.request.contextPath}/resources/js/wordcloud.js"></script> 
<script src="${pageContext.request.contextPath}/resources/js/top10.js"></script> 

<div class="main-container">
    <div class="row">
        <div class="col-xl-12">
            <div class="card">
	            <div class="card-header">
	                <h3>오늘의 키워드</h3>
                </div>
                <div class="canvasContainer_half">
				    <canvas id="top10Chart" width="600" height="400"></canvas>
				</div>
	            <div class="canvasContainer">
	                <div id="wordcloud" class="wordcloud"> <!-- wordcloud.js -->
                </div>
                </div>
            </div>         
        </div>     
    </div>  
</div>
</body>
</html>