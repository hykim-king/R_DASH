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
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
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
<div class="col-xl-4 d-flex">
               <div class="card flex-fill">
                   <div class="card-header bg-transparent d-flex justify-content-center align-items-center">
                       <div class="row align-items-center">
                       <div class="col align-content-center">
                            <h5 class="h3 mb-0">
                            <%=year %>년 <%=month %>월 <%=day %>일(<%=aaa %>) 토픽 목록</h5>
                       </div>
                       </div>
                   </div>
                   <div class="table-responsive">
                       <table class="table align-items-center table-flush">
                       <colgroup>
                            <col style="width: 20%;"> <!-- 2 -->
                            <col style="width: 70%;" class="left-col"> <!-- 6 -->
                            <col style="width: 10%;"> <!-- 2 -->
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

</body>
</html>