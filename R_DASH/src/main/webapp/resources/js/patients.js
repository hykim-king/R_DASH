let chartInstance = null;
let inOutdoorChart = null;

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
                let filteredData = data.filter(d => d.groupKey && d.groupKey !== "전국" && d.groupKey !== "합계");

                let labels = filteredData.map(d => d.groupKey);
                let totalValues = filteredData.map(d => d.patientsTot);
                let inValues = filteredData.map(d => d.inSubTot);
                let outValues = filteredData.map(d => d.outSubTot);

                let ctx = document.getElementById('patientsChart').getContext('2d');
                let inOutdoorCtx = document.getElementById('inOutdoorChart').getContext('2d');

                console.log(data);
                console.log(filteredData);
                console.log(totalValues, inValues, outValues);
                // 1. 기존 차트 제거
                if (chartInstance) {
                    chartInstance.destroy();
                }
                
                if (inOutdoorChart) {
                    inOutdoorChart.destroy();
                }

                chartInstance = new Chart(ctx, {
                    type: 'bar',
                    data: {
                        labels: labels,
                        datasets: [{
                            label: '환자 수',
                            data: totalValues,
                            backgroundColor: 'rgba(38, 142, 190, 0.6)',
                            borderColor: 'rgba(56, 151, 151, 1)',
                            borderWidth: 1
                        }]
                    },
                    options: {
                        responsive: true,
                        scales: {
                            y: { beginAtZero: true, title: { display: true, text: '환자 수' } },
                            x: { title: { display: true, text: groupType === 'region' ? '지역' : '년도' },
                                grid: {
                                    drawTicks: false,   // 눈금선 제거
                                    drawBorder: false,  // 축선 제거
                                    color: 'transparent' // 격자선 색상 투명
                                    }  
                                }
                        }
                    }
                });
                
                // 실내/실외 소계 차트
                inOutdoorChart = new Chart(inOutdoorCtx, {
                    type: 'bar',
                    data: {
                        labels: labels,
                        datasets: [
                            { label: '실내', data: inValues, backgroundColor: 'rgba(54, 162, 235, 0.6)' },
                            { label: '실외', data: outValues, backgroundColor: 'rgba(255, 99, 132, 0.6)' }
                        ]
                    },
                    options: {
                        responsive: true,
                        scales: {
                            y: { 
                                beginAtZero: true, 
                                title: { display: true, text: '환자 수' } 
                                },
                            x: { 
                                title: { display: true, text: groupType === 'region' ? '지역' : '년도' },
                                grid: {
                                    drawTicks: false,   // 눈금선 제거
                                    drawBorder: false,  // 축선 제거
                                    color: 'transparent' // 격자선 색상 투명
                                    }  
                                }
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
