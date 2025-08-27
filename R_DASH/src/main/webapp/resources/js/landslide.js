function getGrade(status) {
    if(status == 1) return {text: "경보", className: "warning"};
    else if(status == 2) return {text: "주의보", className: "caution"};
}


$(document).ready(function() {
    $.ajax({
        url: "/ehr/landslide/year",
        method: "GET",
        dataType: "json",
        success: function(data) {
            const labels = data.map(row => row.YEAR);
            const counts = data.map(row => row.TOTAL_COUNT);

            new Chart($('#yearChart'), {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: '산사태 수',
                        data: counts,
                        backgroundColor: 'rgba(75, 192, 192, 0.6)'
                    }]
                },
                options: { 
                    responsive: true,
                    plugins: {
                            title: {
                                display: true, 
                                text: '연도별 산사태 건 수 (2019~2024)',
                                font: {
                                    size: 18,
                                    weight: 'bold'
                                },
                                color: '#333' 
                            },
                            datalabels: {
                            anchor: "end",   // 데이터 레이블 위치
                            align: "top",    // 막대 위쪽에 표시
                            color: "black",  // 글자색
                            font: {
                            weight: "bold",
                            size: 16
                                }
                            }
                        },
                    scales: {
                        x: { 
                            grid: {
                                drawTicks: false,   // 눈금선 제거
                                drawBorder: false,  // 축선 제거
                                color: 'transparent' // 격자선 색상 투명
                                }  
                            }
                    }
                 },
                plugins: [ChartDataLabels]
            });
        }
    });

    $.ajax({
        url: '/ehr/landslide/region',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            const labels = data.map(row => row.REGION);
            const counts = data.map(row => row.TOTAL_COUNT);

            new Chart($('#regionChart'), {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: '산사태 수',
                        data: counts,
                        backgroundColor: 'rgba(255, 99, 132, 0.6)'
                    }]
                },
                options: { 
                    responsive: true,
                    plugins: {
                            title: {
                                display: true, 
                                text: '지역별 산사태 건수 (2019년 ~ 2024년)',
                                font: {
                                    size: 18,
                                    weight: 'bold'
                                },
                                color: '#333' 
                            },
                            datalabels: {
                            anchor: "end",   // 데이터 레이블 위치
                            align: "top",    // 막대 위쪽에 표시
                            color: "black",  // 글자색
                            font: {
                            weight: "bold"
                                }
                            }
                        },
                    scales: {
                        x: { 
                            grid: {
                                drawTicks: false,
                                drawBorder: false,
                                color: 'transparent'
                                }  
                            }
                    }
                 },
                plugins: [ChartDataLabels]
            });
        }
    });

    $.ajax({
        url: '/ehr/landslide/month',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            const labels = data.map(row => row.MONTH+"월");
            const counts = data.map(row => row.TOTAL_COUNT);

            new Chart($('#monthChart'), {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: '산사태 수',
                        data: counts,
                        backgroundColor: 'rgba(54, 162, 235, 0.6)'
                    }]
                },
                options: { 
                  responsive: true,
                  plugins: {
                            title: {
                                display: true, 
                                text: '월별 산사태 건수 (2019년 ~ 2024년)',
                                font: {
                                    size: 18,
                                    weight: 'bold'
                                },
                                color: '#333' 
                            },
                            datalabels: {
                            anchor: "end",   // 데이터 레이블 위치
                            align: "top",    // 막대 위쪽에 표시
                            color: "black",  // 글자색
                            font: {
                            weight: "bold"
                                }
                            }
                        },
                  scales: {
                        x: {
                            grid: {
                                drawTicks: false,
                                drawBorder: false,
                                color: 'transparent'
                            }
                        }
                    }
                 },
                plugins: [ChartDataLabels]
            });
        }
    });

    $.ajax({
        url: '/ehr/landslide/status',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            const labels = data.map(row => row.STATUS);
            const counts = data.map(row => row.TOTAL_COUNT);

            new Chart($('#statusChart'), {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: '산사태 수',
                        data: counts,
                        backgroundColor: 'rgba(255, 206, 86, 0.6)'
                    }]
                },
                options: { 
                  indexAxis: 'y',
                  responsive: true,
                  plugins: {
                            title: {
                                display: true, 
                                text: '예보상태별 산사태 건수 (2019년 ~ 2024년)',
                                font: {
                                    size: 18,
                                    weight: 'bold'
                                },
                                color: '#333' 
                            },
                            datalabels: {
                            anchor: "end",   // 데이터 레이블 위치
                            align: "end",    // 막대 오른쪽에 표시
                            color: "black",  // 글자색
                            font: {
                            weight: "bold",
                            size: 16
                                }
                            }
                        },
                  scales: {
                      y: {
                          grid: {
                              drawTicks: false,
                              drawBorder: false,
                              color: 'transparent'
                          }
                      }
                  }
                },
                plugins: [ChartDataLabels]
            });
        }
    });
});