<%@page import="com.pcwk.ehr.cmn.PcwkString"%>
<%@page import="com.pcwk.ehr.cmn.SearchDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
       
       url = cp+"/user/userList";
       //out.print("url:"+url);
       
       scriptName = "pagerDoRetrieve";
       
       String pageHtml=PcwkString.bootstrapRenderingPager(maxNum, pageNo, pageSize, bottomCount, url, scriptName);
       
%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="Start your development with a Dashboard for Bootstrap 4.">
  <meta name="author" content="Creative Tim">
  <title>회원 조회</title>
</head>
<script>
document.addEventListener('DOMContentLoaded',function(){
	if('${sessionScope.loginUser.role}'!=='1'){
        alert('관리자만 접근 가능합니다.');
        
        window.location.href='/ehr/home';
    }
	
	if('${search.searchDiv}'===''){
		searchWord.disabled = true;
	}else{
		document.querySelector('#selectDivButton').value = '${search.searchDiv}';
	}
	
	if('${search.searchWord}'!==''){
        document.querySelector('#searchWord').value = '${search.searchWord}';
    }
});
//searchDiv 설정
function selectDiv(div){
	const selectDivButton = document.querySelector('#selectDivButton');
	const searchWord = document.querySelector('#searchWord');
	
	if(div===10){
		selectDivButton.innerText = '이메일';
		selectDivButton.value=div;
		searchWord.disabled = false;
	}else if(div === 20){
		selectDivButton.innerText = '이름';
		selectDivButton.value=div;
		searchWord.disabled = false;
	}else{
		selectDivButton.innerText = '전체';
		selectDivButton.value='';
		searchWord.disabled = true;
	}
	
}
//회원 검색
function search(){
	const selectDivButton = document.querySelector('#selectDivButton');
	const searchWord = document.querySelector('#searchWord');
	const pageNo = document.querySelector('#pageNo')
	window.location.href = '/ehr/user/userList?searchDiv=' + selectDivButton.value + '&searchWord=' + searchWord.value + '&pageNo=' + pageNo.value;
}
// 권한 변경
function changeRole(userNo){
	if(confirm('권한을 변경하시겠습니까?')){
		
		$.ajax({
            method:"POST",    //GET/POST
            url:"/ehr/user/changeRole", //서버측 URL
            dataType:"json",//서버에서 받을 데이터 타입
            data:{          //파라메터
              "userNo" : userNo,
            },
            success:function(result){//요청 성공
                if(2 === result.success){
                    alert(result.message);
                    
                    //페이지 새로고침
                    window.location.reload();
                }else{
                    alert(result.message);
                    
                    //페이지 새로고침
                    window.location.reload();
                } 
                    
            },
            error:function(result){//요청 실패
                console.log("error:"+result)
                alert(result);
            }
            
            
        });
	}
}

//페이징 
function pagerDoRetrieve(url, pageNo){   
    //button
    const searchButton = document.querySelector('#searchButton');
    
    document.querySelector('#pageNo').value= pageNo;
    
    searchButton.click();     
    
}
</script>
<body>
    <!-- Header -->
    <!-- Header -->
    <div class="header bg-warning pb-6 header bg-gradient-warning py-7 py-lg-8 pt-lg-9">
      <!-- Header container -->
      <div class="container-fluid d-flex align-items-center">
        <div class="row">
          <div style="margin-left:100px;">
            <h1 class="display-2 text-white">안녕하세요! ${sessionScope.loginUser.name}님</h1>
            <p class="text-white mt-0 mb-5">이 페이지는 회원 관련 정보를 조회할 수 있고, <br>회원 권한을 변경 할 수 있습니다.</p>
          </div>
        </div>
      </div>
    </div>
    <!-- Page content -->
    <div class="container-fluid mt--6" style="min-height: 700px; max-width:1700px; margin:0 auto;">
      <div class="row">
        <!-- Light table -->
        <div class="col">
          <div class="card">
            <!-- Card header -->
            <div class="card-header border-0 d-flex align-items-center">
              <h3 class="mb-0">회원 정보</h3>
              <!-- 
              <button type="button" class="btn btn-default" style="margin-left:20px;" onclick="location.href='/ehr/user/userList?searchDiv=30'">관리자</button>
               -->
              <div class="ml-auto d-flex align-items-center"> <!-- 오른쪽 끝으로 밀기 -->
                <h3 class="mb-0 text-nowrap" style="margin-right:10px">${totalCnt } 명</h3>
                <div class="dropdown">
				  <button class="btn btn-secondary dropdown-toggle" type="button" value="" id="selectDivButton" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
				    <c:choose>
				        <c:when test="${search.searchDiv == '10' }">이메일</c:when>
				        <c:when test="${search.searchDiv == '20' }">이름</c:when>
				        <c:otherwise>전체</c:otherwise>
				    </c:choose>
				  </button>
				  <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
				    <a class="dropdown-item" onclick="javascript:selectDiv('')">전체</a>
				    <a class="dropdown-item" onclick="javascript:selectDiv(10)">이메일</a>
				    <a class="dropdown-item" onclick="javascript:selectDiv(20)">이름</a>
				  </div>
				</div>
	            <input type="text" class="form-control" id="searchWord" name="searchWord">
	            <input type="hidden"id="pageNo" name="pageNo">  
			    <button type="button" id="searchButton" onclick="javascript:search()" class="btn btn-default text-nowrap" style="margin-left:10px">검색</button>
			  </div>
            </div>
            <!-- Light table -->
            <div class="table-responsive">
              <table class="table align-items-center table-flush">
                <thead class="thead-light">
                  <tr>
                    <th scope="col">사진</th>
                    <th scope="col">번호</th>
                    <th scope="col">계정</th>
                    <th scope="col">이름</th>
                    <th scope="col">권한</th>
                    <th scope="col">가입일</th>
                    <th scope="col">권한 변경</th>
                  </tr>
                </thead>
                <tbody class="list">
                <c:choose>
                    <c:when test="${list.size()>0 }">
                    	<c:forEach var="vo" items="${list }">
                    		<tr>
			                    <th scope="row">
			                      <div class="media align-items-center">
			                          <img alt="Image placeholder" class="avatar rounded-circle mr-3" src="/ehr/resources/image/profile/${vo.image }">
			                      </div>
			                    </th>
			                    <td class="budget">
			                      ${vo.no }
			                    </td>
			                    <td>
			                      <span class="badge badge-dot mr-4">
			                        <span class="status">${vo.email }</span>
			                      </span>
			                    </td>
			                    <td>
                                  <span class="badge badge-dot mr-4">
                                    <span class="status">${vo.name }</span>
                                  </span>
                                </td>
                                <td>
			                    <c:choose>
			                    	<c:when test="${vo.role == 1 }">
			                    		<span class="badge badge-dot mr-4">
			                       			<span class="status">관리자</span>
			               		        </span>
			                    	</c:when>
			                    	<c:otherwise>
			                    		<span class="badge badge-dot mr-4">
			                       			<span class="status">사용자</span>
			               		        </span>
			                    	</c:otherwise>
			                    </c:choose>
			                    </td>
			                    <td>
			                      <span class="badge badge-dot mr-4">
			                        <span class="status">${vo.regDt }</span>
			                      </span>
			                    </td>
			                    <td>
			                      <button class="btn btn-success" onclick="changeRole(${vo.userNo})">권한 변경</button>
			                    </td>
			                  </tr>
                    	</c:forEach>
                    </c:when>
                    <c:otherwise>
                    	<tr>
                    		<td colspan="6">등록된 회원이 없습니다.</td>
                    	</tr>
                    </c:otherwise>
                </c:choose>
                  
                </tbody>
              </table>
            </div>
            <!-- Card footer -->
            <div class="card-footer py-4">
                    <!-- paging -->
            <%
                out.print(pageHtml);
            %>
            <!--// paging end -->
            <!-- 
              <nav aria-label="...">
                <ul class="pagination justify-content-end mb-0">
                  <li class="page-item disabled">
                    <a class="page-link" href="#" tabindex="-1">
                      <i class="fas fa-angle-left"></i>
                      <span class="sr-only">Previous</span>
                    </a>
                  </li>
                  <li class="page-item active">
                    <a class="page-link" href="#">1</a>
                  </li>
                  <li class="page-item">
                    <a class="page-link" href="#">2 <span class="sr-only">(current)</span></a>
                  </li>
                  <li class="page-item"><a class="page-link" href="#">3</a></li>
                  <li class="page-item">
                    <a class="page-link" href="#">
                      <i class="fas fa-angle-right"></i>
                      <span class="sr-only">Next</span>
                    </a>
                  </li>
                </ul>
              </nav>
              -->
            </div>
          </div>
        </div>
      </div>
    </div>
</body>

</html>