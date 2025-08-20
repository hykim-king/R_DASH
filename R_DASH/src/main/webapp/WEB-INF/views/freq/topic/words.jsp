<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


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
.content-row {
    display: flex;                /* 가로 배치 */
    gap: 20px;
    margin: 20px auto 0 auto;     /* 상단 20px, 좌우 auto → 가운데 정렬 */
    width: 90%;                   /* 전체 폭의 90% */
    justify-content: center;       /* 여분 공간이 있을 때 요소 중앙 정렬 */
}

.box {
    flex: 1;
    border: 1px solid #ccc;
    border-radius: 12px;
    padding: 10px;
    display: flex;
    justify-content: center;
    align-items: center;
    height: 250px;               /* 높이 고정 → table과 chart 동일 */
    box-sizing: border-box;
}

/* Table div 안에서 스크롤 생길 수 있게 */
.table-box table {
    width: 100%;
    border-collapse: collapse;
    table-layout: fixed;
}

.table-box th, .table-box td {
    text-align: center;
    padding: 5px;
}



.wordcloud {
    border: solid 1px #ccc;
    border-radius: 12px;
    margin: 20px auto;
    width: 90%;
    height: 300px;
}
</style>

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

                <!-- Table + Chart 가로 배치 -->
                <div class="content-row">
                    <div class="box table-box">
                        <table>
                            <colgroup>
                                <col style="width: 15%;">
                                <col style="width: 35%;">
                                <col style="width: 25%;">
                                <col style="width: 25%;">
                            </colgroup>
                            <thead>
                               <tr>
                                  <th scope="col"></th>
                                  <th scope="col"></th>
                                  <th scope="col">빈도</th>
                                  <th scope="col">증감률(%)</th>
                               </tr>
                            </thead>
                            <c:forEach var="word" items="${rate}" varStatus="status">
                                <tr>
                                    <td>${status.count}</td>
                                    <td>${word.word}</td>
                                    <td>${word.freq}</td>
                                    <td>
									    <c:choose>
									        <c:when test="${word.prevFreq == 0}">
									            <span style="color: green;">New</span>
									        </c:when>
									
									        <c:otherwise>
									            <c:choose>
									                <c:when test="${word.changeRate >= 0}">
									                    <span style="color:red;">
									                        ${word.changeRate}▲
									                    </span>
									                </c:when>
									                <c:otherwise>
									                    <span style="color:blue;">
									                        ${word.changeRate}▼
									                    </span>
									                </c:otherwise>
									            </c:choose>
									        </c:otherwise>
									    </c:choose>
									</td>
                                </tr>
                            </c:forEach>
                        </table>
                    </div>

                    <div class="box chart-box">
                        <canvas id="top10Chart" width="400" height="300"></canvas>
                    </div>
                </div>
                <!-- //Table + Chart -->

                <!-- WordCloud는 아래 -->
                <div class="wordcloud">
                    <div id="wordcloud"><!-- wordcloud.js --></div>
                </div>

            </div>         
        </div>     
    </div>  
</div>
