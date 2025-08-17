<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="CP" value="${pageContext.request.contextPath }" />
<%@page import="java.util.Date" %>
<%@page import="java.time.LocalDate" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.time.format.DateTimeFormatter" %>
<%@page import="java.time.DayOfWeek" %>  
<c:set var="CP" value="${pageContext.request.contextPath }" />
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
</head>
<body>
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
	<div class="header bg-primary pb-6">
		<div class="container-fluid">
			<div class="header-body">
			<div class="row align-items-center py-4">
				<div class="col-lg-6 col-7">
				</div>
			    <!--  버튼 -->
				<div class="col-lg-6 col-5 text-right">
				    <input type="button" id="doSave" class="btn btn-sm btn-neutral" value="등록">
				    <input type="button" id="doUpdate" class="btn btn-sm btn-neutral" value="수정">
				</div>
				 <!-- //버튼 -->
		      </div>
		   </div>
		</div>
	</div>
	<div class="container-fluid mt--6">
	   <div class=row>
	   <!-- 왼쪽 : 토픽 카드 -->
	       <div class="col-xl-4">
	           <div class="card">
	               <div class="card-header bg-transparent">
                       <div class="row align-items-center">
                       <div class="col">
                            <h5 class="h3 text-white mb-0" style="color: black !important;"><%=year %>년 <%=month %>월 <%=day %>일(<%=aaa %>)</h5>
                       </div>
                       </div>
	               </div>
	               <div class="card-body">
	                   <div>
	                    <c:choose>
				            <c:when test="${not empty todayTopics}">
				                <ul style="list-style: none; padding: 0; margin: 0;">
				                    <c:forEach var="vo" items="${todayTopics}">
				                        <li data-topicNo="${vo.topicNo}">
				                            ${vo.no} ${vo.title} (${vo.topicRatio}건)
				                        </li>
				                    </c:forEach>
				                </ul>
				            </c:when>
				            <c:otherwise>
				                <p>등록된 토픽이 없습니다.</p>
				            </c:otherwise>
				        </c:choose>
	                   </div>
	               </div>
	           </div>
	       </div>
	       <!-- 오른쪽 : 토픽 상세 -->
	       <div class="col-xl-8">
	           <div class="card bg-default">
	               <div class="card-header bg-transparent">
	                   <div class="row align-items-center">
	                   <div class="col">
	                       <h5 class="h3 text-white mb-0">재민이가 오늘의 주제를 요약해 드립니다 !</h5>
	                   </div>
	                   </div>
	               </div>
	               <div class="card-body">
	                   <div>
		                 <c:if test="${not empty topicDetails}">
		                     <p id="detailTitle">${topicDetails[0].title}</p>
		                     <p id="detailContents">${topicDetails[0].contents}</p>
		                 </c:if>
	                   </div>
	               </div>
	           </div>
           </div>
	   </div>
	</div>
	<div class="row">
	   <div class="col">
	       <div class="card">
	           <!-- news header -->
	           <div class="card-header border-0">
	                <input type="button" class="btn btn-sm btn-neutral" value="재난 종합">
			        <input type="button" class="btn btn-sm btn-neutral" value="화재">
			        <input type="button" class="btn btn-sm btn-neutral" value="싱크홀">
			        <input type="button" class="btn btn-sm btn-neutral" value="폭염">
			        <input type="button" class="btn btn-sm btn-neutral" value="황사">
			        <input type="button" class="btn btn-sm btn-neutral" value="태풍">
			        <input type="button" class="btn btn-sm btn-neutral" value="산사태">
			        <input type="button" class="btn btn-sm btn-neutral" value="홍수">
			        <input type="button" class="btn btn-sm btn-neutral" value="한파">
	           </div>
	           <!-- news table -->
	           <div class="table-response">
	               <div class="table align-items-center table-flush">
	               <table>
	               <thead style="display: none;">
	                   <tr>
	                       <th>신문사</th>
	                       <th>제목</th>
	                       <th>발행일자</th>
	                   </tr>
	               </thead>
	               <tbody class="list">
				    <c:choose>
				        <c:when test="${newsList.size() > 0 }">
						    <c:forEach var="vo" items="${newsList }">
	                            <tr>
		                            <td><c:out value="${vo.company }"/></td>
		                            <td><c:out value="${vo.title }"/></td>
		                            <td><c:out value="${vo.pubDt }"/></td>   
					           </tr>
					        </c:forEach>
				        </c:when>
				        <c:otherwise>
				        </c:otherwise>
				    </c:choose>
				    </tbody>
				    </table>
				    </div>  
				    <div class="table align-items-center table-flush">
                   <table>
                   <thead style="display: none;">
                       <tr>
                           <th>신문사</th>
                           <th>제목</th>
                           <th>발행일자</th>
                       </tr>
                   </thead>
                   <tbody class="list">
                    <c:choose>
                        <c:when test="${keywordNews.size() > 0 }">
                            <c:forEach var="vo" items="${keywordNews }">
                                <tr>
                                    <td><c:out value="${vo.company }"/></td>
                                    <td><c:out value="${vo.title }"/></td>
                                    <td><c:out value="${vo.pubDt }"/></td>   
                               </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                    </table>
                    </div>  
	           </div>
	           <!-- news footer -->
	           <div class="card-footer py-4">
	               <span>+더보기</span>
	           </div>
	           
	       </div>
	   </div>
	</div>

    <!-- 최종없데이트 일자 -->
    <div>최종 업데이트:</div>
    
    <!-- 오늘의 키워드 -->
    <div>
        <img src="/ehr/resources/newspage/wordcloud_result.png"style="width: 200px; height: auto;">
        <img src="/ehr/resources/newspage/topn_result.png"style="width: 200px; height: auto;">
    
    </div>
    <!-- //오늘의 키워드 -->
</div> <!-- //main -->

</body>
</html>