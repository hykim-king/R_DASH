<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="CP" value="${pageContext.request.contextPath }" />
<%@page import="java.util.Date" %>
<%@page import="java.time.LocalDate" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.time.format.DateTimeFormatter" %>
<%@page import="java.time.DayOfWeek" %>  
<c:set var="now" value="<%=new java.util.Date()%>" />
<c:set var="sysDate"><fmt:formatDate value="${now}" pattern="yyyy-MM-dd_HH:mm:ss" /></c:set>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="/ehr/resources/template/dashboard/css/dashboard.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/nucleo/css/nucleo.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/nucleo/css/nucleo-svg.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/@fortawesome/fontawesome-free/css/all.min.css" rel="stylesheet">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<title>뉴스피드</title>
<style>
/* 카드 전체 여백 */
/* .col-xl-4 .card,
.my-topic-card .card {
    height: 95%;
} */
.row.align-items-stretch {
    display: flex;
}
/* 카드 스타일 */
.topic-card {
    position: relative; /* 내부 화살표 위치를 위해 */
    padding: 20px;
}

/* 공통 화살표 스타일 */
.arrow {
    position: absolute;
    top: 16%;
    width: 30px;
    height: 30px;
    border: none;
    background: none; /* 원 제거 */
    color: #000;
    font-size: 20px;
    cursor: pointer;
    transform: translateY(-50%);
}

/* 왼쪽 화살표 */
.arrow.left {
    left: 10px;
    color: black;
}

/* 오른쪽 화살표 */
.arrow.right {
    right: 10px;
    color: black;
}

#detailTitle{
    color:black;
    font-weight: bold;
    font-size: 24px;
    margin-bottom: 30px !important;
}
#detailTitleDiv{
    margin-bottom: 20px;

}
#detailContents{
    color:black;
    font-size: 22px;
}
.h3.mb-0{
    color:#172b4d;
    font-size: 30px;
}
.budget{
    font-size:18px !important;
}
.heihlight{
     box-shadow: inset 0 -20px 0 #FFFF00;
}
.budget{
    color:black !important;
  
}
/* 왼쪽 카드 기준으로 위치 */
.col-xl-5.position-relative {
    position: relative; /* clickMeWrapper 기준 */
}

.clickMeWrapper {
    position: absolute;  /* 왼쪽 카드 안에서 절대 위치 */
    top: 14.6px;          /* 카드 위쪽으로 띄움 */
    left: 20px;          /* 카드 왼쪽 안쪽 */
    display: flex;
    align-items: center;
    gap: 10px;
    z-index: 10;
}

.clickMeImg {
    width: 100px;
    height: auto;
}

.clickMeIcon {
    display: flex;
    align-items: center;
    background-color: #fff;
    border-radius: 12px;
    padding: 5px 10px;
    box-shadow: 0 2px 6px rgba(0,0,0,0.2);
}

.clickMeIcon i {
    font-size: 24px;
    color: orange;
    margin-right: 5px;
}

.chatText {
    font-size: 14px;
    color: #333;
}

table thead th:nth-child(1),
table thead th:nth-child(3),
table td:nth-child(1),
table td:nth-child(3) {
    vertical-align: middle !important; /* 세로 가운데 정렬 */
    text-align: center !important;  
}
/*카드 푸터 +더보기 */
.card-footer.py-4{
    display:flex;
    align-item : center;
    justify-content: center;
}
/*뉴스 카드 키워드 버튼 */
.card-header.border-0{
    display:flex;
    flex-wrap: wrap;       /* 화면 넘어가면 줄바꿈 허용 */
    justify-content: flex-start;  /* 버튼 왼쪽부터 채우기 */
    gap: 10px;
}
.card-header.border-0 input[type="button"] {
    flex: 1;           /* 남은 공간을 균등 분배 */
    min-width: 175px;   /* 너무 작아지지 않도록 최소 크기 설정 */
    max-width: 175px;
    box-sizing: border-box;
}
#newsLoadMore:hover {
	color: #ff9077;
}
</style>
<script>
document.addEventListener('DOMContentLoaded', function(){
    console.log('DOMContentLoaded');
    $('#allBtn').on('click',function(){
    	$("#allNewsTable").show();
        $("#keywordNewsTable").hide();
    	
    });
    $(".btn[data-keyword]").not("#allBtn").on('click',function(){
    	var keyword = $(this).data("keyword");
    	
    	//비동기 통신 -> 키워드 전달
    	$.ajax({
    		url:"/ehr/news/newsKeywordSearch.do",
    		type:"GET",
    		dataType: "json",
    		data:{keyword: keyword},
    		success: function(data){
    			console.log("data: ",data);
    			renderKeywordNews(data);  // 함수 호출만
    	        $("#allNewsTable").hide();
    	        $("#keywordNewsTable").show();
    		},
            error: function(xhr, status, error) {
                console.error("AJAX Error:", status, error,xhr.responseText);
            }

    	})
    });
    function renderKeywordNews(data){
        var tbody = $("#keywordNewsTable tbody");
        tbody.empty();
        if(data && data.length > 0){
            $.each(data, function(i, vo){
                var row = "<tr>" +
                    "<td class='budget'>" + vo.company + "</td>" +
                    "<td class='budget'><a href='" + vo.url + "' target='_blank'>" + vo.title + "</a></td>" +
                    "<td class='budget'>" + vo.pubDt + "</td>" +
                    "</tr>";
                tbody.append(row);
            });
        } else {
            tbody.append("<tr><td colspan='3'>데이터 없음</td></tr>");
        }
    }
    //토픽 상세 보기 (버튼 클릭)
    // 1. js 배열로 변환
    var topicDetails = [];
    	<c:forEach var="t" items="${topicDetails}">
    	topicDetails.push({
    		topicNo: '<c:out value="${t.topicNo}" />',
    	    title: '<c:out value="${t.title}" escapeXml="false"/>',
    	    contents: '<c:out value="${t.contents}" escapeXml="false"/>'
    	});
        </c:forEach>
    
    var currentIndex = 0;
    
    function showTopic(index){
    	if(topicDetails.length === 0 )return;
    	var topic = topicDetails[index];
    	
    	document.getElementById("detailTitle").textContent = topic.title;
        document.getElementById("detailContents").textContent = topic.contents;
    }
    //첫 화면
    showTopic(currentIndex);
    document.querySelector(".arrow.left").addEventListener("click",function(){
    	//(0-1+4)%4 => 3%4 => 3번째로 이동(0,1,2,3)
    	currentIndex = (currentIndex - 1 +topicDetails.length)%topicDetails.length; 
    	showTopic(currentIndex);
    });
    document.querySelector(".arrow.right").addEventListener("click",function(){
        //(0+1)%4 => 1%4 => 1번째로 이동(0,1,2,3)
        currentIndex = (currentIndex + 1)%topicDetails.length; 
        showTopic(currentIndex);
    });
    
    //등록 모달
    const moveToTopicRegBtn = document.querySelector("#moveToTopicReg");
    moveToTopicRegBtn.addEventListener("click",function(e){
    	let url = "doSaveView.do";
    	const screenWidth = window.screen.width;
        const screenHeight = window.screen.height;
        console.log('screenWidth: '+screenWidth);
        console.log('screenHeight: '+screenHeight);

       const left = (screenWidth - 600)/2;
       const top = (screenHeight - 400)/2;

       let options = `width=600,height=600, top=${top}, left=${left}, resizable=no, scrollbars=no`;
       window.open(url,"_blank",options);
    });
    //수정 모달
    const moveToTopicModBtn = document.querySelector("#moveToTopicMod");
    moveToTopicModBtn.addEventListener("click",function(e){
    	if(topicDetails.length === 0) {
            alert("수정할 토픽이 없습니다.");
            return;
        }
    	let topicNo = topicDetails[currentIndex].topicNo; // 현재 상세보기 토픽 번호
        if(!topicNo){
            alert("토픽 번호를 확인할 수 없습니다.");
            return;
        }
        let url = "doUpdateView.do?topicNo=" + topicNo;
        const screenWidth = window.screen.width;
        const screenHeight = window.screen.height;
        console.log('screenWidth: '+screenWidth);
        console.log('screenHeight: '+screenHeight);

       const left = (screenWidth - 600)/2;
       const top = (screenHeight - 400)/2;

       let options = `width=600,height=600, top=${top}, left=${left}, resizable=yes scrollbars=yes`;
       window.open(url,"_blank",options);
    });
    
    //오늘의 키워드 모달
    const clickMeDiv = document.querySelector("#clickMe");
    clickMeDiv.addEventListener("click",function(e){
    	let url = "/ehr/freq/topic/words.do";
        const screenWidth = window.screen.width;
        const screenHeight = window.screen.height;
        console.log('screenWidth: '+screenWidth);
        console.log('screenHeight: '+screenHeight);

       const left = (screenWidth - 900)/2;
       const top = (screenHeight - 800)/2;

       let options = `width=900,height=800, top=${top}, left=${left}, resizable=no, scrollbars=no`;
       window.open(url,"_blank",options);
    });
   // 더보기 버튼
    let pageNo = 1;
    const pageSize = 10;

    function loadNews(pageNo) {
        $.ajax({
            url: '/ehr/news/newsPage.do',  // 정확히 매핑된 URL
            type: 'GET',
            data: { pageNo: pageNo, pageSize: 10 },
            dataType: 'json',
            success: function(data) {
                if (!data || data.length === 0) {
                    $('#loadMore').hide();
                    return;
                }
                data.forEach(vo => {
                	 $('#newsList').append(
                		        `<tr>
                		            <td class="budget">${vo.company}</td>
                		            <td class="budget"><a href="${vo.url}" target="_blank">${vo.title}</a></td>
                		            <td class="budget">${vo.pubDt}</td>
                		        </tr>`
                		    );
                });
            },
            error: function(err) {
                console.error("AJAX Error:", err);
            }
        });
    }

    // 초기 로딩
    loadNews(pageNo);

    // 더보기 버튼
    $('#newsLoadMore').click(function() {
        pageNo++;
        loadNews(pageNo);
    });
});

</script>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/socket.jsp"></jsp:include>

<% 
  LocalDate today = LocalDate.now();
  int year = today.getYear();
  int month = today.getMonthValue();
  int day = today.getDayOfMonth();
  DayOfWeek dayOfWeek = today.getDayOfWeek();
  String aaa = "";
  String[] korWeek = {"월","화","수","목","금","토","일"};
  
  if(dayOfWeek != null) {
      int idx = dayOfWeek.getValue() - 1; // 1(월)~7(일) → 배열 0~6
      if(idx >= 0 && idx < korWeek.length) {
          aaa = korWeek[idx];
      }
  }
%>
<div class="main-content" id="panel">
	<div class="header bg-warning pb-6">
		<div class="container-fluid">
			<div class="header-body">
			<div class="row align-items-center py-4">
				<div class="col-lg-6 col-7">
				</div>
			    <!--  버튼 -->
				<div class="col-lg-6 col-5 text-right">
				    <input type="button" id="moveToTopicReg" class="btn btn-white" value="등록">
				    <input type="button" id="moveToTopicMod" class="btn btn-white" value="수정">
				</div>
				 <!-- //버튼 -->
		      </div>
		   </div>
		</div>
	</div>
	<div class="container-fluid mt--6">
	   <div class="row d-flex align-items-stretch">
	   <!-- 왼쪽 : 토픽 카드 -->
	       <div class="col-xl-5 d-flex">
	           <div class="card flex-fill">
	               <div class="card-header bg-transparent d-flex justify-content-center align-items-center">
                       <div class="row align-items-center">
                       <div class="col align-content-center">
                            <h5 class="h3 mb-0">
                            <%=year %>년 <%=month %>월 <%=day %>일(<%=aaa %>)</h5>
                       </div>
                       </div>
	               </div>
	               <div class="table-responsive">
	                   <table class="table align-items-center table-flush">
	                   <colgroup>
	                        <col style="width: 15%;"> <!-- 2 -->
	                        <col style="width: 65%;" class="left-col"> <!-- 6 -->
	                        <col style="width: 20%;"> <!-- 2 -->
	                    </colgroup>
	                   <thead class="thead-light">
	                       <tr>
	                           <th>no</th>
	                           <th>제목</th>
	                           <th>관련 뉴스 건수</th>
	                       </tr>
	                   </thead>
	                   <tbody class="list">
	                    <c:choose>
				            <c:when test="${not empty todayTopics}">
				                    <c:forEach var="vo" items="${todayTopics}">
				                     <tr>
	                                    <td class="budget"><c:out value="${vo.no}"/></td>
	                                    <td class="budget"><c:out value="${vo.title }"/></td>
	                                    <td class="budget"><c:out value="${vo.topicRatio}"/></td>   
	                                    <td style="display: none;"><c:out value="${vo.topicNo}"/></td>   
	                                </tr>    
				                    </c:forEach>
				            </c:when>
				            <c:otherwise>
				                <p>등록된 토픽이 없습니다.</p>
				            </c:otherwise>
				        </c:choose>
				        </tbody>
				        </table>
	               </div>
	           </div>
	       </div>
	       <!-- 오늘의 키워드 보여줄 click me -->
	       <div id="clickMe" class="clickMeWrapper">
		        <img class="clickMeImg" src="/ehr/resources/image/news_Jeamin.png" alt="나를 클릭해봐">
		        <div class="clickMeIcon">
		           <!--  <i class="ni ni-chat-round"></i> -->
		            <span class="chatText"> 나를 클릭해 봐 ! 📊</span>
		        </div>
		    </div>
	       <!-- 오른쪽 : 토픽 상세 -->
	       <div class="col-xl-7 d-flex my-topic-card">
	           <div class="card flex-fill topic-card">
	               <div class="card-header bg-transparent d-flex justify-content-center align-items-center">
				        <button class="arrow left">
				            <i class="ni ni-bold-left"></i>
				        </button>
	                   <div class="row align-items-center">
	                   <div class="col align-content-center">
	                       <h5 id="heihlight" class="h3 mb-0">✨재민이AI의 오늘의 토픽 요약✨</h5>
	                   </div>
	                   </div>
	                   <button class="arrow right">
		                    <i class="ni ni-bold-right"></i>
		                </button>
	               </div>
	               <div class="card-body">             
	                   <div id="detailTitleDiv">
		                 <span id="detailTitle" class="heihlight"></span>
		               </div>
		               <div>
		                 <p id="detailContents"></p>
	                   </div>
	               </div><!-- //카드 body -->
	           </div><!-- //카드 -->
           </div>
           <!--//오른쪽 : 토픽 상세  -->
	   </div>
	</div>

	
	<div class="col-xl-12">
	   <div class="col-xl-12">
	       <div class="card">
	           <!-- news header -->
	           <div class="card-header border-0">
	                <input type="button" id="allBtn" class="btn btn-warning" data-keyword="" value="재난 종합">
			        <input type="button" class="btn btn-warning" data-keyword="화재" value="화재">
			        <input type="button" class="btn btn-warning" data-keyword="싱크홀" value="싱크홀">
			        <input type="button" class="btn btn-warning" data-keyword="폭염" value="폭염">
			        <input type="button" class="btn btn-warning" data-keyword="황사" value="황사">
			        <input type="button" class="btn btn-warning" data-keyword="태풍" value="태풍">
			        <input type="button" class="btn btn-warning" data-keyword="산사태" value="산사태">
			        <input type="button" class="btn btn-warning" data-keyword="홍수" value="홍수">
			        <input type="button" class="btn btn-warning" data-keyword="한파" value="한파">
	           </div>
	           <!-- news table -->
	           <div id="allNewsTable" class="table-responsive">
	               <table class="table align-items-center table-flush">
	               <colgroup>
		                <col style="width: 15%;"> <!-- 2 -->
		                <col style="width: 65%;" class="left-col"> <!-- 6 -->
		                <col style="width: 20%;"> <!-- 2 -->
		            </colgroup>
	               <thead style="display: none;">
	                   <tr>
	                       <th>신문사</th>
	                       <th>제목</th>
	                       <th>발행일자</th>
	                   </tr>
	               </thead>
	      <%--          <tbody class="list" id="newsList">
				    <c:choose>
				        <c:when test="${newsList.size() > 0 }">
						    <c:forEach var="vo" items="${newsList }">
	                            <tr>
		                            <td class="budget"><c:out value="${vo.company }"/></td>
		                            <td class="budget">
		                              <a href="${vo.url}" target ="_blank">
		                              <c:out value="${vo.title }"/></a>
		                            </td>
		                            <td class="budget"><c:out value="${vo.pubDt }"/></td>   
					           </tr>
					        </c:forEach>
				        </c:when>
				        <c:otherwise>
				        </c:otherwise>
				    </c:choose>
				    </tbody> --%>
				    <tbody class="list" id="newsList"></tbody>
				    </table>
				 </div>  <!-- //전체 조회 테이블 -->
				    <!-- 키워드 테이블 (처음엔 숨기기) -->
				  <div id="keywordNewsTable" class="table-responsive" style="display:none;">
                   <table class="table align-items-center table-flush">
                   <colgroup>
                        <col style="width: 15%;"> <!-- 2 -->
                        <col style="width: 65%;" class="left-col"> <!-- 6 -->
                        <col style="width: 20%;"> <!-- 2 -->
                    </colgroup>
                   <thead style="display: none;">
                       <tr>
                           <th>신문사</th>
                           <th>제목</th>
                           <th>발행일자</th>
                       </tr>
                   </thead>
                   <tbody class="list">
               
                    </tbody>
                    </table>
	           </div>
	           <!-- news footer -->
	           <div class="card-footer py-4">
	               <div id="newsLoadMore" data-page="1">
	                <span>+더보기</span>
	               </div>
	           </div>
	          </div>
                 <div>최종 업데이트 일자 : <c:out value="${latestRegDt}"/></div>
	          </div>
	          </div>
			      
	   </div><!-- //main -->


    


</body>
</html>