<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>통계 페이지</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

</head>
<body>

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


<div style="width: 500px; height: 300px;">
    <canvas id="patientsChart"></canvas>
</div>
    

<script>
let chartInstance = null;

$(document).ready(function() {

    // 드롭다운 토글 함수
    function toggleDropdowns() {
	    let type = $("#groupType").val();
	
	    if (type === "region") {
	        // 지역별 → 년도 드롭다운만 보이게
	        $("#yearSelect").show();
	        $("#sidoSelect").hide().val(''); // 숨길 때 값 초기화
	    } else if (type === "year") {
	        // 년도별 → 지역 드롭다운만 보이게
	        $("#sidoSelect").show();
	        $("#yearSelect").hide().val(''); // 숨길 때 값 초기화
	    }
	}

    // 차트 로드
    function loadChart() {
        let groupType = $("#groupType").val();
        let year = $("#yearSelect").val();
        let sidoNm = $("#sidoSelect").val();

        $.ajax({
            url: '/ehr/temperature/summary.do',
            type: 'GET',
            data: { groupType, year, sidoNm },
            success: function(data) {
                let labels = data
                    .filter(item => item.groupKey !== "전국") // 전국 제외
                    .map(item => item.groupKey);

                let values = data
                    .filter(item => item.groupKey !== "전국")
                    .map(item => item.patientsTot);

                let ctx = document.getElementById('patientsChart').getContext('2d');

                if (window.chartInstance) {
                    window.chartInstance.destroy();
                }

                window.chartInstance = new Chart(ctx, {
                    type: 'bar',
                    data: {
                        labels: labels,
                        datasets: [{
                            label: '환자 수',
                            data: values,
                            backgroundColor: 'rgba(75, 192, 192, 0.6)',
                            borderColor: 'rgba(75, 192, 192, 1)',
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        scales: {
                            y: { beginAtZero: true, title: { display: true, text: '환자 수' } },
                            x: { title: { display: true, text: groupType === 'region' ? '지역' : '년도' } }
                        }
                    }
                });
            }
        });
    }

    // 이벤트 바인딩
    $("#groupType").on("change", function() {
        toggleDropdowns();
        loadChart();
    });

    $("#yearSelect, #sidoSelect").on("change", loadChart);

    // 초기 실행
    toggleDropdowns();
    loadChart();
});
</script>
</body>
</html>

