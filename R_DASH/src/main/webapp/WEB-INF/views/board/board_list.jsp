<%@page import="com.pcwk.ehr.cmn.PcwkString"%>
<%@page import="com.pcwk.ehr.cmn.SearchDTO"%>
<%@page import="java.util.Date"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="CP" value="${pageContext.request.contextPath }" />
<c:set var="now" value="<%=new java.util.Date()%>" />
<c:set var="sysDate"><fmt:formatDate value="${now}" pattern="yyyy-MM-dd_HH:mm:ss" /></c:set>
<%
    int bottomCount = 5;
    int pageSize    = 0;//페이지 사이즈
    int pageNo      = 0;//페이지 번호
    int maxNum      = 0;//총글수
    
    String url      = "";//호출URL
    String scriptName="";//자바스크립트 이름
    
    
    //request: 요청 처리를 할수 있는 jsp 내장 객체
    String totalCntString = request.getAttribute("totalCnt").toString();
    //out.print("totalCntString:"+totalCntString);
    maxNum = Integer.parseInt(totalCntString);  
    
    SearchDTO  paramVO = (SearchDTO)request.getAttribute("search");   
    pageSize = paramVO.getPageSize();
    pageNo   = paramVO.getPageNo();
    
    String cp = request.getContextPath();
       //out.print("cp:"+cp);
       
       url = cp+"/board/doRetireve.do";
       //out.print("url:"+url);
       
       scriptName = "pagerDoRetrieve";
       
       String pageHtml=PcwkString.bootstrapRenderingPager(maxNum, pageNo, pageSize, bottomCount, url, scriptName);
       
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="/ehr/resources/template/dashboard/css/dashboard.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/nucleo/css/nucleo.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/nucleo/css/nucleo-svg.css" rel="stylesheet" />
<link href="/ehr/resources/template/dashboard/assets/vendor/@fortawesome/fontawesome-free/css/all.min.css" rel="stylesheet">

<title>공지사항 게시판</title>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
document.addEventListener('DOMContentLoaded', function(){
    console.log('DOMContentLoaded');
    
    //언어 선택
    const langSelect = document.querySelector("#lang");
    //조회버튼
    const moveToRegBtn =document.querySelector("#moveToReg");
    console.log(moveToRegBtn);
    
    moveToRegBtn.addEventListener("click",function(event){
        console.log('moveToSaveButton click');
    	if(confirm('등록 화면으로 이동 하시겠습니까?') === false)return;
    
    	window.location.href = '/ehr/board/doSaveView.do';
 });
    
    if('${search.searchDiv}'===''){
        searchWord.disabled = false;
    }else{
        document.querySelector('#selectDivButton').value = '${search.searchDiv}';
    }
    
    if('${search.searchWord}'!==''){
        document.querySelector('#searchWord').value = '${search.searchWord}';
    }
    // 언어 변경 이벤트
    langSelect.addEventListener("change", function(){
        const selectLang = langSelect.value;
        const selectDivButton = document.querySelector('#selectDivButton').value;
        const searchWord = document.querySelector('#searchWord').value;
        const pageNo = document.querySelector('#pageNo').value;

        window.location.href = '/ehr/board/doRetrieve.do?lang='+selectLang+'&searchDiv='+selectDivButton + '&searchWord=' + searchWord + '&pageNo=' + pageNo;
    });
});


//페이징 
function pagerDoRetrieve(url, pageNo){   
    //button
    /*  const searchButton = document.querySelector('#searchButton');  */
    
    document.querySelector('#pageNo').value= pageNo;
    
    searchButton.click();     
    
}
//공지사항 검색
function search(){
	const selectDiv = document.querySelector('#selectDivButton').dataset.value;
    const searchWord = document.querySelector('#searchWord').value;
    const pageNo = document.querySelector('#pageNo').value;
    const lang = document.querySelector('#lang').value;

    window.location.href = '/ehr/board/doRetrieve.do?lang=' + lang
                            + '&searchDiv=' + selectDiv
                            + '&searchWord=' + encodeURIComponent(searchWord)
                            + '&pageNo=' + pageNo;
}

//searchDiv 설정
function selectDiv(div){
    const selectDivButton = document.querySelector('#selectDivButton');
    const searchWord = document.querySelector('#searchWord');
    
    if(div===10){
        selectDivButton.innerText = '제목';
        selectDivButton.value=div;
        searchWord.disabled = false;
    }else if(div === 20){
        selectDivButton.innerText = '내용';
        selectDivButton.value=div;
        searchWord.disabled = false;
    }else{
        selectDivButton.innerText = '전체';
        selectDivButton.value='';
        searchWord.disabled = false;  
    }
   
}
</script>
<style type="text/css">
span {
    margin-right: 20px;
}
.table.align-items-center.table-flush {
    width: 100%;
    table-layout: fixed;
    border-collapse: collapse;
    text-align: center; /* 기본은 가운데 */
}

.table.align-items-center.table-flush th,
.table.align-items-center.table-flush td {
    vertical-align: middle;
}

<!-- 두 번째 열만 왼쪽 정렬 -->
.table.align-items-center.table-flush th:nth-child(2),
.table.align-items-center.table-flush td:nth-child(2) {
    text-align: left;
}
</style>
</head>
<body>
<div class="main-content">
    <div class="form-group">
	    <label for="lang"></label>
	    <select id="lang" name="lang">
	        <c:choose>
	            <c:when test="${lang == 'ko'}">
	                <option value="ko" selected>KO</option>
	                <option value="en">EN</option>
	            </c:when>
	            <c:when test="${lang == 'en'}">
	                <option value="ko">KO</option>
	                <option value="en" selected>EN</option>
	            </c:when>
	            <c:otherwise>
	                <option value="ko">KO</option>
	                <option value="en">EN</option>
	            </c:otherwise>
	        </c:choose>
	    </select>
	</div>
	
	<!-- header2 -->
	<div class="header bg-warning pb-6 header bg-gradient-warning py-5 py-lg-6 pt-lg-6">
      <!-- Header container -->
      <div class="container-fluid d-flex align-items-center">
        <div class="row">
        
          <div style="margin-left:100px;">
            <h1 class="display-2 text-white">📢 ${msgs.saveBoardTitle}</h1>
            <p class="text-white mt-0 mb-5">${msgs.saveBoardComment1} <br>
                                            ${msgs.saveBoardComment2} <br>
                                            ${msgs.saveBoardComment3} </p>
          </div>
        </div>
      </div>
    </div>
	
	<!-- //header2 -->
	<!-- Page content -->
	<div class="container-fluid mt--6" style="min-height: 700px; max-width:1700px; margin:0 auto;">
		<div class="row">
	 <!-- Light table -->
		<div class="col">
  		    <div class="card"> 
  		    <!-- Card header -->
		        <div class="card-header border-0 d-flex align-items-center">
		            <h3 class="mb-0">${msgs.noticeBoard} <span>(${ totalCnt}${msgs.gun})</span></h3>
		            <!-- 검색란 -->
		            <div class="ml-auto d-flex align-items-center" ><!-- 오른쪽 끝으로 밀기 -->
		            <!-- 드롭다운 -->
		             <div class="dropdown">
                  <button class="btn btn-secondary dropdown-toggle" type="button" value="" id="selectDivButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                    <c:choose>
                        <c:when test="${search.searchDiv == '10' }">${msgs.title}</c:when>
                        <c:when test="${search.searchDiv == '20' }">${msgs.contents}</c:when>
                        <c:otherwise>${msgs.all}</c:otherwise>
                    </c:choose>
                  </button>
                  <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
                    <a class="dropdown-item" onclick="javascript:selectDiv('')">${msgs.all}</a>
                    <a class="dropdown-item" onclick="javascript:selectDiv(10)">${msgs.title}</a>
                    <a class="dropdown-item" onclick="javascript:selectDiv(20)">${msgs.contents}</a>
                  </div>
                </div>
                <!-- //드롭다운 -->
                <input type="hidden" id="searchDivValue" value="${search.searchDiv}">
		        <input type="text" class="form-control" id="searchWord" name="searchWord">
                <input type="hidden"id="pageNo" name="pageNo" value="${search.pageNo != 0 ? search.pageNo : 1}">  
                <button type="button" id="searchButton" onclick="javascript:search()" class="btn btn-default text-nowrap" style="margin-left:10px">검색</button>
                <button type="button" id="moveToReg" class="btn btn-default text-nowrap" style="margin-left:3px">등록</button>
              </div>
            </div>   
    <!-- //Page content -->
		<!-- Light table -->
		<div class="table-responsive">
			<table class="table align-items-center table-flush">
			 <colgroup>
		        <col style="width: 10%;"> <!-- 2 -->
		        <col style="width: 50%;" class="left-col"> <!-- 6 -->
		        <col style="width: 15%;"> <!-- 2 -->
		        <col style="width: 35%;"> <!-- 2 -->
		    </colgroup>
			    <thead class="thead-light">
			       <tr>
				        <th scope="col" >${msgs.no}</th>
				        <th scope="col" >${msgs.title}</th>
				        <th scope="col" >${msgs.view}</th>
				        <th scope="col" >${msgs.regDt}</th>
			        </tr>
			    </thead>
			     <tbody class="list">
			          <c:choose>
			            <c:when test="${list.size() > 0 }"> <!-- if -->
			                <c:forEach var="vo" items="${list }"> <!-- 향상된 for -->
			                  <tr>
			                    <td ><c:out value="${vo.no}"/></td>
			                    <td >
				                    <div class="media-body">
				                    
					                    <span class="name mb-0 text-sm">
					                    <a href="/ehr/board/doSelectOne.do?boardNo=${vo.boardNo}"><c:out value="${vo.title }"/></a>
					                    </span>
				                    </div>
			                    </td>
		
			                    <td ><c:out value="${vo.viewCnt }"/></td>
			                    <td ><c:out value="${vo.modDt }"/></td>
			                    <td style="display: none;"><c:out value="${vo.boardNo }"/></td>
			                  </tr> 
			                </c:forEach>
			            </c:when>
			            <c:otherwise>    <!-- else -->
			                <tr>
			                   <td colspan="99">등록된 게시글이 없습니다.</td> 
			                </tr>
			            </c:otherwise>
			          </c:choose>
			     </tbody>
				</table>
			</div>
			<!-- //Light table -->
			<!-- 카드 푸터 -->
			<div class="card-footer py-4">
			<%
                out.print(pageHtml);
            %>
			 </div>
			<!-- //카드 푸터 -->
		    </div>
		</div>
	</div>
</div>
<!--// table 영역 -->
</div>
</body>
</html>