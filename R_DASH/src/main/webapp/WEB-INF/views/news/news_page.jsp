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
    top: -70px;          /* 카드 위쪽으로 띄움 */
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
    	var role = "${sessionScope.loginUser.role}";
        var tbody = $("#keywordNewsTable tbody");
        $('#newsLoadMore').hide();
        tbody.empty();
        if(data && data.length > 0){
            $.each(data, function(i, vo){
                var row = "<tr>" +
                    "<td class='budget'>" + vo.company + "</td>" +
                    "<td class='budget'><a href='" + vo.url + "' target='_blank'>" + vo.title + "</a></td>" +
                    "<td class='budget'>" + vo.pubDt + "</td>";
                    if(role === '1'){  // 관리자면 버튼 추가
                        row += "<td class='budget'><button class='btn btn-danger newsDeleteBtn' data-news-no='"+vo.newsNo+"'>뉴스 삭제</button></td>";
                    }
                    row +="</tr>";
                tbody.append(row);
            });
        } else {
            tbody.append("<tr><td colspan='99'>데이터 없음</td></tr>"); 
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
    
	const moveToTopicRegBtn = document.querySelector("#moveToTopicReg");
    //등록 모달
    if(moveToTopicRegBtn){
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
    }
    
	const moveToTopicModBtn = document.querySelector("#moveToTopicMod");
    //수정 모달
    if(moveToTopicModBtn){
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
    }
    
    // 등록 모달 받기
    function receiveDataFromChild(title, contents) {
        console.log("자식창에서 받은 데이터:", title, contents);
    
        // 예시: 부모창의 입력창에 값 넣기
        document.getElementById("detailTitle").textContent = title;
        document.getElementById("detailContents").textContent = contents;
    
       
    }
    
   // 더보기 버튼
    let pageNo = 1; // 현재 페이지
    const pageSize = 10;
    const role = "${sessionScope.loginUser.role}";

    // 뉴스 로딩 함수 (기본 + 더보기)
    function loadNews(pageNo) {
    	var role = "${sessionScope.loginUser.role}";
        $.ajax({
            url: "<c:url value='/news/doRetrieve.do'/>",
            type: 'GET',
            data: { pageNo: pageNo, pageSize: pageSize },
            dataType: 'json',
            success: function(data) {
                const tbody = $("#newsList"); // tbody id와 일치
                
                if (!data || data.length === 0) {
                    if (pageNo === 1) { // 첫 페이지도 데이터 없으면
                        tbody.html("<tr><td colspan='3'>데이터 없음</td></tr>");
                    }
                    $('#newsLoadMore').hide(); // 더보기 숨기기
                    return;
                }

                // 데이터가 있으면 tbody에 추가
                data.forEach(function(vo) {
                	let row = "<tr>" +
                        "<td class='budget'>" + vo.company + "</td>" +
                        "<td class='budget'><a href='" + vo.url + "' target='_blank'>" + vo.title + "</a></td>" +
                        "<td class='budget'>" + vo.pubDt + "</td>";
                        if(role === '1'){  // 관리자면 버튼 추가
                            row += "<td class='budget'><button class='btn btn-danger newsDeleteBtn' data-news-no='"+vo.newsNo+"'>뉴스 삭제</button></td>";
                        }
                        row += "</tr>";
                    tbody.append(row);
                    
                });
             // 남은 데이터가 적으면 더보기 버튼 숨김
                if (data.length < pageSize) {
                    $('#newsLoadMore').hide();
                } else {
                    $('#newsLoadMore').show();
                }
            },
            error: function(err) {
                console.error("AJAX Error:", err);
            }
        });
    }

    // 초기 로딩
    $(document).ready(function() {
        loadNews(pageNo);

        // 더보기 버튼 클릭
        $('#newsLoadMore').click(function() {
            pageNo++;
            loadNews(pageNo);
        });
    });
    //재민이 마우스 오버
    const clickMeWrappers = document.querySelector("#clickMe");
    const clickMeDefaultImage = document.querySelector("#clickMeDefault");
    const clickMeOverImage = document.querySelector("#clickMeOver");
    
    // 초기 상태: 기본 이미지만 보이게
    clickMeDefaultImage.style.display = "block";
    clickMeOverImage.style.display = "none";
    
    console.log(clickMeDefaultImage);
    console.log(clickMeOverImage);
    
    clickMeWrappers.addEventListener('mouseover',function(){
    	clickMeDefaultImage.style.display = "none";
        clickMeOverImage.style.display = "block";
    });
    clickMeWrappers.addEventListener('mouseout',function(){
        clickMeDefaultImage.style.display = "block";
        clickMeOverImage.style.display = "none";
    });
    
    //언어 선택
    const langSelect = document.querySelector("#lang");
    const currentLang = "${empty lang ? 'ko' : lang}";
    // 언어 선택 select
    langSelect.value = currentLang;  // selected 반영
    
    // 언어 변경 이벤트
    langSelect.addEventListener("change", function(){
        selectLang = langSelect.value;

        window.location.href = '/ehr/news/newsPage.do?lang='+selectLang;      
        
        if (keywordWindow && !keywordWindow.closed) {
            keywordWindow.location.href = "/ehr/freq/topic/words.do?lang=" + selectLang;
        } 
    });
    let keywordWindow = null;
    //오늘의 키워드 모달
    const clickMeDiv = document.querySelector("#clickMe");
    clickMeDiv.addEventListener("click",function(e){
    	selectLang = langSelect.value;
                 
        let url = "/ehr/freq/topic/words.do?lang="+ selectLang;
        
        const screenWidth = window.screen.width;
        const screenHeight = window.screen.height;
        console.log('screenWidth: '+screenWidth);
        console.log('screenHeight: '+screenHeight);

       const left = (screenWidth - 900)/2;
       const top = (screenHeight - 800)/2;

       let options = `width=800,height=800, top=${top}, left=${left}, resizable=no, scrollbars=no`;
       window.open(url,"_blank",options);
    });
    //뉴스 삭제
    $(document).on("click", ".newsDeleteBtn", function() {
    	 const btn = $(this);
    	 const newsNo = btn.data("news-no"); // 여기서 읽음

        if (!newsNo) {
            alert("삭제할 뉴스 번호가 없습니다.");
            return;
        }

        if (!confirm("정말 삭제하시겠습니까?")) return;

        $.ajax({
            type: "POST",
            url: "/ehr/news/newsDelete.do",
            dataType: "json",
            data: { newsNo: newsNo },
            success: function(response) {
                if (response.messageId === 1) {
                    alert("삭제 완료!");
                    // 삭제 성공 시 해당 row 제거
                    $(this).closest("tr").remove();
                    
                     // tbody에 남은 row 개수 확인
                    const remainingRows = $("#newsList tr").length;
                    if (remainingRows < pageSize) {
                        $('#newsLoadMore').hide(); // 남은 데이터 없으면 숨김
                    } else {
                        $('#newsLoadMore').show(); // 남은 데이터 있으면 표시
                    }
                } else {
                    alert("삭제 실패: " + response.message);
                }
            }.bind(this), // this를 Ajax success 내부에서도 유지
            error: function() {
                alert("삭제 중 오류 발생");
            }
        });
    });
});


</script>
</head>
<body>
<jsp:include page="/WEB-INF/views/common/socket.jsp"></jsp:include>
<div class="main-content" id="panel">
	<div class="header bg-warning pb-6">
		<div class="container-fluid">
			<div class="header-body">
			<div class="row align-items-center py-4">
				<div class="col-lg-6 col-7">
				    <!-- 오늘의 키워드 보여줄 click me -->
				   <div id="clickMe">
		           <div id="clickMeDefault" class="clickMeWrapper">
		                <img  class="clickMeImg" src="/ehr/resources/image/news_Jeamin.png" alt="나를 클릭해봐">
		                <div class="clickMeIcon">
		                   <!--  <i class="ni ni-chat-round"></i> -->
		                    <span class="chatText"> ${msgs.click} ! 📊</span>
		                </div>
		            </div>
                    <!-- 오늘의 키워드 보여줄 click me -->
                   <div id="clickMeOver" class="clickMeWrapper">
                    <img  class="clickMeImg" src="/ehr/resources/image/hello_jm.png" alt="안녕!">
                        <div class="clickMeIcon">
                           <!--  <i class="ni ni-chat-round"></i> -->
                            <span class="chatText"> ${msgs.click} ! 📊</span>
                        </div>
                    </div>
                </div>
                </div>
			    <!--  버튼 -->
 			      <c:if test="${sessionScope.loginUser.role =='1'  }">
				   	<div class="col-lg-6 col-5 text-right">
					    <input type="button" id="moveToTopicReg" class="btn btn-white" value="${msgs.reg}">
					    <input type="button" id="moveToTopicMod" class="btn btn-white" value="${msgs.modi}">
					</div>
 		    		</c:if>
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
                            ${msgs.today}</h5>
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
	                           <th>${msgs.no}</th>
	                           <th>${msgs.title}</th>
	                           <th>${msgs.topicCount}</th>
	                       </tr>
	                   </thead>
	                   <tbody class="list">
	                    <c:choose>
				            <c:when test="${not empty todayTopics}">
				                    <c:forEach var="vo" items="${todayTopics}" varStatus="status">
				                     <tr>
	                                    <td class="budget"><c:out value="${status.index + 1}"/></td>
	                                    <td class="budget"><c:out value="${vo.title }"/></td>
	                                    <td class="budget"><c:out value="${vo.topicRatio}"/></td>   
	                                    <td style="display: none;"><c:out value="${vo.topicNo}"/></td>   
	                                </tr>    
				                    </c:forEach>
				            </c:when>
				            <c:otherwise>
				                <p>${msgs.noTopic}</p>
				            </c:otherwise>
				        </c:choose>
				        </tbody>
				        </table>
	               </div>
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
	                       <h5 id="heihlight" class="h3 mb-0">✨${msgs.summrTitle}✨</h5>
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
	                <input type="button" id="allBtn" class="btn btn-warning" data-keyword="" value="${msgs.total}">
			        <input type="button" class="btn btn-warning" data-keyword="화재" value="${msgs.fire}">
			        <input type="button" class="btn btn-warning" data-keyword="싱크홀" value="${msgs.sinkhole}">
			        <input type="button" class="btn btn-warning" data-keyword="폭염" value="${msgs.heat}">
			        <input type="button" class="btn btn-warning" data-keyword="황사" value="${msgs.dust}">
			        <input type="button" class="btn btn-warning" data-keyword="태풍" value="${msgs.typhoon}">
			        <input type="button" class="btn btn-warning" data-keyword="산사태" value="${msgs.landslide}">
			        <input type="button" class="btn btn-warning" data-keyword="홍수" value="${msgs.flood}">
			        <input type="button" class="btn btn-warning" data-keyword="한파" value="${msgs.cold}">
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
	                       <th>${msgs.newspaper}</th>
	                       <th>${msgs.reg}</th>
	                       <th>${msgs.pub}</th>
	                       
	                   </tr>
	               </thead>
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
                           <th>${msgs.newspaper}</th>
                           <th>${msgs.reg}</th>
                           <th>${msgs.pub}</th>
                       </tr>
                   </thead>
                   <tbody class="list"></tbody>
                    </table>
	           </div>
	           <!-- news footer -->
	           <div class="card-footer py-4">
	               <div id="newsLoadMore" data-page="1">
	                <span>+${msgs.more}</span>
	               </div>
	           </div>
	          </div>
                 <div>${msgs.updateDay} : <c:out value="${latestRegDt}"/></div>
	          </div>
	          </div>
			      
	   </div><!-- //main -->
</body>
</html>