<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>

<!-- 그룹 타입 선택 -->
<select id="groupType">
    <option value="region">지역별</option>
    <option value="year">년도별</option>
</select>

<!-- 년도 선택 -->
<select id="yearSelect">
    <option value="">전체년도</option>
    <option value="2024">2024</option>
    <option value="2023">2023</option>
</select>

<!-- 지역 선택 -->
<select id="sidoSelect">
    <option value="">전체지역</option>
    <option value="서울">서울</option>
    <option value="인천">인천</option>
    
</select>

<canvas id="patientsChart" width="400" height="200"></canvas>

<script>
function loadChart() {
    var groupType = $("#groupType").val();
    var year = $("#yearSelect").val();
    var sidoNm = $("#sidoSelect").val();

    $.ajax({
        url: '/patients/summary',
        type: 'GET',
        data: { groupType: groupType, year: year, sidoNm: sidoNm },
        success: function(data) {
            var labels = data.map(item => item.groupKey);
            var values = data.map(item => item.patientsTot);

            var ctx = document.getElementById('patientsChart').getContext('2d');
            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: '환자 수',
                        data: values,
                        backgroundColor: 'rgba(75, 192, 192, 0.6)'
                    }]
                }
            });
        }
    });
}

$("#groupType, #yearSelect, #sidoSelect").change(loadChart);

// 페이지 로드 시 초기 실행
$(document).ready(loadChart);
</script>
</body>
</html>
