// 1. 년도별 발생횟수
$.getJSON("/ehr/sinkholes/year", function(data) {
    const labels = data.map(row => row.YEAR);
    const counts = data.map(row => row.CNT);

    const yearlyChart = new Chart(document.getElementById("yearlyChart"), {
        type: "bar",
        data: {
            labels: labels,
            datasets: [{
                label: "발생횟수",
                data: counts,
                backgroundColor: "rgba(54, 162, 235, 0.6)"
            }]
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true, 
                    text: '연도별 발생횟수 (2018년 ~ 2025년)',
                    font: {
                        size: 18,
                        weight: 'bold'
                    },
                    color: '#333' 
                }
            },
            scales:{
                x:{
                    grid: {
                        drawTicks: false,   // 눈금선 제거
                        drawBorder: false,  // 축선 제거
                        color: 'transparent' // 격자선 색상 투명
                    } 
                }
            }
        }
    });
});

$.getJSON("/ehr/sinkholes/signgu", function(data) {
    const labels = data.map(row => row.SIGNGU_NM);
    const counts = data.map(row => row.CNT);

    const signguChart = new Chart(document.getElementById("signguChart"), { 
        type: "bar",
        data: {
            labels: labels,
            datasets: [{
                label: "발생횟수",
                data: counts,
                backgroundColor: "rgba(255, 99, 132, 0.6)"
            }]
        },
        options: {
            indexAxis: 'x',
            responsive: true,
            plugins: {
                title: {
                    display: true, 
                    text: '시군구별 발생횟수 (2018년 ~ 2025년)',
                    font: {
                        size: 18,
                        weight: 'bold'
                    },
                    color: '#333' 
                }
            },
            scales: {
              x: {
                  ticks: {
                      autoSkip: false,
                      maxRotation: 45,    
                      minRotation: 45
                  }
              }
          }
        }
    });
});

// 3. 월별 발생횟수
$.getJSON("/ehr/sinkholes/month", function(data) {
    const labels = data.map(row => row.MONTH);
    const counts = data.map(row => row.CNT);

    const monthlyChart = new Chart(document.getElementById("monthlyChart"), {
        type: "line",
        data: {
            labels: labels,
            datasets: [{
                label: "발생횟수",
                data: counts,
                borderColor: "rgba(75, 192, 192, 1)",
                backgroundColor: "rgba(75, 192, 192, 0.2)",
                tension: 0.3
            }]
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true, 
                    text: '월별 발생횟수 (2018년 ~ 2025년)',
                    font: {
                        size: 18,
                        weight: 'bold'
                    },
                    color: '#333' 
                }
            }
        }
    });
});

// 4. 복구 상태 분포
$.getJSON("/ehr/sinkholes/state", function(data) {
    const labels = data.map(row => row.STATE_NM);
    const counts = data.map(row => row.CNT);

    const stateChart = new Chart(document.getElementById("stateChart"), {
        type: "pie",
        data: {
            labels: labels,
            datasets: [{
                label: "복구 상태 분포",
                data: counts,
                backgroundColor: [
                    "rgba(255, 99, 132, 0.6)",
                    "rgba(54, 162, 235, 0.6)",
                    "rgba(255, 206, 86, 0.6)",
                    "rgba(75, 192, 192, 0.6)"
                ]
            }]
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true, 
                    text: '복구 상태 분포 (2018년 ~ 2025년)',
                    font: {
                        size: 18,
                        weight: 'bold'
                    },
                    color: '#333' 
                }
            }
        }
    });
});

// 5. 년도별 피해 통계
$.getJSON("/ehr/sinkholes/damage", function(data) {
    const labels = data.map(row => row.YEAR);
    const dprs = data.map(row => row.DPRS_TOT);
    const inj = data.map(row => row.INJ_TOT);
    const veh = data.map(row => row.VEH_TOT);

    const damageChart = new Chart(document.getElementById("damageChart"), {
        type: "bar",
        data: {
            labels: labels,
            datasets: [
                {
                    label: "사망자",
                    data: dprs,
                    backgroundColor: "rgba(255, 99, 132, 0.6)"
                },
                {
                    label: "부상자",
                    data: inj,
                    backgroundColor: "rgba(54, 162, 235, 0.6)"
                },
                {
                    label: "피해 차량",
                    data: veh,
                    backgroundColor: "rgba(255, 206, 86, 0.6)"
                }
            ]
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true, 
                    text: '연도별 피해 통계 (2018년 ~ 2025년)',
                    font: {
                        size: 18,
                        weight: 'bold'
                    },
                    color: '#333' 
                }
            },
        }
    });
});
