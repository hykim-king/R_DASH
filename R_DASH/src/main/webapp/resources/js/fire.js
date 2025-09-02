var jq36 = jQuery.noConflict(true); // 3.6.0 jQuery 별칭

jq36(document).ready(function() {
    // 소방서 데이터
    jq36.ajax({
        url: '/ehr/fire/fire-stations.do',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            var tbody = jq36('#firestationTable tbody');
            tbody.empty();

            data.forEach(function(row, index) {
                var tr = jq36('<tr></tr>');
                var region = row.REGION || row.region; 
                var mainCount = row.MAIN_CNT || row.main_cnt || 0;
                var safeCount = row.SAFE_CNT || row.safe_cnt || 0;

                tr.append('<td>' + region + '</td>');
                tr.append('<td>' + mainCount + '</td>');
                tr.append('<td>' + safeCount + '</td>');
                tbody.append(tr);
            });

            // DataTables 초기화
            jq36('#firestationTable').DataTable({
                pageLength: 10,
                searching: true,
                ordering: true,
                destroy: true,
                language: {
                    lengthMenu: "_MENU_ 개씩 보기",     // "Show 10 entries" → "10개씩 보기"
                    search: "검색:",                    // "Search:" → "검색:"
                    info: "총 _TOTAL_개 중 _START_ ~ _END_",
                    infoEmpty: "데이터 없음",
                    infoFiltered: "(전체 _MAX_개 중 검색됨)",
                    paginate: {
                        first: "처음",
                        last: "마지막",
                        next: "다음",
                        previous: "이전"
                    }
                }
            });
        },
        error: function(xhr, status, error) {
            console.error('AJAX 오류:', error);
        }
    });
<<<<<<< HEAD

=======
/*
>>>>>>> a76d822e155237841302239ac2dbc91ab4e3722e
    // 복구현황
    jq36.ajax({
        url: '/ehr/fire/fire-safe.do',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            const labels = data.map(row => row.YEAR);                 // 연도
            const damage = data.map(row => row.TOTALDAMAGE);         // 피해금액
            const recovery = data.map(row => row.TOTALRECOVERY);     // 복구금액

            const ctx = document.getElementById('fireSafeChart').getContext('2d');

            new Chart(ctx, {
                data: {
                    labels: labels,
                    datasets: [
                        {
                            type: 'bar',
                            label: '피해금액',
                            data: damage,
                            backgroundColor: 'rgba(255, 99, 132, 0.6)',
                            borderColor: 'rgba(255, 99, 132, 1)',
<<<<<<< HEAD
                            borderWidth: 1
=======
                            borderWidth: 1,
                            yAxisID: 'y'
>>>>>>> a76d822e155237841302239ac2dbc91ab4e3722e
                        },
                        {
                            type: 'line',
                            label: '복구금액',
                            data: recovery,
                            borderColor: 'rgba(54, 162, 235, 1)',
                            backgroundColor: 'rgba(54, 162, 235, 0.2)',
                            tension: 0.4,
                            fill: false,
<<<<<<< HEAD
                            yAxisID: 'y'
=======
                            yAxisID: 'y1'
>>>>>>> a76d822e155237841302239ac2dbc91ab4e3722e
                        }
                    ]
                },
                options: {
                    responsive: true,
                    interaction: { mode: 'index', intersect: false },
                    plugins: {
                        title: { display: true,
<<<<<<< HEAD
                                 text: '연도별 화재 피해금액 및 복구금액',
=======
                                 text: '연도별 피해금액 및 복구금액',
>>>>>>> a76d822e155237841302239ac2dbc91ab4e3722e
                                 font: {
                                     size: 18,          // 폰트 크기
                                     weight: 'bold'
                                 },
                                 color: '#333'  },
                        tooltip: { mode: 'index' }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
<<<<<<< HEAD
                            title: { display: true, text: '금액 (원)' }
=======
                            position: 'left',
                            title: { display: true, text: '피해금액 (원)' }
                        },
                        y1: {
                            beginAtZero: true,
                            position: 'right',
                            grid: { drawOnChartArea: false }, // 겹치는 그리드 제거
                            title: { display: true, text: '복구금액 (원)' }
>>>>>>> a76d822e155237841302239ac2dbc91ab4e3722e
                        },
                        x: { 
                            grid: {
                                drawTicks: false, 
                                drawBorder: false,  
                                color: 'transparent'
                                }  
                            }
                    }
                }
            });

        },
        error: function(xhr, status, error) {
            console.error('AJAX 오류:', error);
        }
    });
<<<<<<< HEAD

=======
*/
>>>>>>> a76d822e155237841302239ac2dbc91ab4e3722e
    jq36.ajax({
        url: '/ehr/fire/fire-yearly',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            const labels = data.map(row => row.YEAR);
            const counts = data.map(row => row.FIRE_COUNT);

            const ctxYearly = document.getElementById('yearlyChart').getContext('2d');

            new Chart(ctxYearly, {
                type: 'doughnut',
                data: {
                    labels: labels,
                    datasets: [{
                        label: '화재 건수',
                        data: counts,
                        backgroundColor: [
                            '#ff951c','#ff9c45ff','#FFA94D','#FFB870','#FFD1A6','#FFE5CC'
                        ],
                        borderColor: '#FFD1A6',
                        borderWidth: 2
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        title: {
                            display: true,
                            text: '연도별 화재 통계',
                            font: {
                                size: 18,          // 폰트 크기
                                weight: 'bold'
                            },
                            color: '#333'
                        },
                        datalabels: {
                            color: 'black',
                            font: {
                                weight: 'bold',
                                size: 14
                            },
                            formatter: (value, context) => {
                                // 전체 합계
                                const dataArr = context.chart.data.datasets[0].data;
                                const total = dataArr.reduce((a, b) => a + b, 0);

                                // 퍼센트 계산
                                const percentage = ((value / total) * 100).toFixed(1);

                                // 라벨(년도)
                                const label = context.chart.data.labels[context.dataIndex];

                                return `${label}년\n${percentage}%`;
                            },
                            anchor: 'center',
                            align: 'center'
                        }
                    },
                    rotation: 0, // 시작 각도 0
                    circumference: 360 // 전체 360도
                },
                plugins: [ChartDataLabels] // datalabels 플러그인 활성화
            });
        },
        error: function(xhr, status, error) {
            console.error('연도별 화재 통계 AJAX 오류:', error);
        }
    });


    jq36.ajax({
        url: '/ehr/fire/fire-damage', 
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            const typeMap = {
                '임야': '산지/숲'
            };

            const labels = data.map(row => typeMap[row.FIRE_TYPE] || row.FIRE_TYPE);
            const damage = data.map(row => row.TOTAL_DAMAGE);

            const ctxDamage = document.getElementById('damageChart').getContext('2d');

            new Chart(ctxDamage, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: '재산피해 합계',
                        data: damage,
                        backgroundColor: 'rgba(255, 182, 87, 0.8)',
                        borderColor: '#FFD1A6',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        title: { display: true,
                                 text: '화재유형별 재산피해(2019~2023)',
                                 font: {
                                     size: 18,          // 폰트 크기
                                     weight: 'bold'
                                     },
                                color: '#333'
                             }, 
                        legend: { display: true },
                        datalabels: {
                            anchor: "end",   // 데이터 레이블 위치
                            align: "top",    // 막대 위쪽에 표시
                            color: "black",  // 글자색
                            font: {
                            weight: "bold"
                                },
                            formatter: (value) => value.toLocaleString()
                            }
                    },
                    scales: {
                        y: { beginAtZero: true,
                             title: { display: true,
                                      text: '재산피해 (천원)'
                                    },
                            },
                        x: { 
                            ticks:{
                                autoSkip: false,
                                maxRotation: 0,    
                                minRotation: 0
                            },
                            grid: {
                                drawTicks: false,   // 눈금선 제거
                                drawBorder: false,  // 축선 제거
                                color: 'transparent' // 격자선 색상 투명
                                }  //grid end
                            } //x end
                        } //scales end
                    },
                    plugins: [ChartDataLabels]
            });
        },
        error: function(xhr, status, error) {
            console.error('화재유형별 재산피해 AJAX 오류:', error);
        }
    });

    jq36.ajax({
        url: '/ehr/fire/fire-ext',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            const labels = data.map(d => d.SUB_NO);
            const counts = data.map(d => d.EXTINGUISHER_COUNT);

            const ctx = document.getElementById('subwayChart').getContext('2d');

            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: '소화기 개수',
                        data: counts,
                        backgroundColor: 'rgba(255, 182, 87, 0.8)',
                        borderColor: '#FFD1A6',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        title: { display: true,
                                 text: '호선별 소화기 개수 (1호선부터, 가나다 순)',
                                 font: {
                                     size: 18,          // 폰트 크기
                                     weight: 'bold'
                                 },
                                 color: '#333'
                             },
                    },
                    scales: {
                        y: { beginAtZero: true },
                        x: { 
                            grid: {
                                drawTicks: false,   // 눈금선 제거
                                drawBorder: false,  // 축선 제거
                                color: 'transparent' // 격자선 색상 투명
                                }  
                            }
                    }
                },
            });
        },
        error: function() { console.error('호선별 소화기 AJAX 오류'); }
    });
    
});
