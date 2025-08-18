$(document).ready(function() {
    //소방서
    $.ajax({
        url: '/ehr/fire/fire-stations-count.do',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            console.log(data);
            var tbody = $('#firestationTable tbody');
            tbody.empty();
            data.forEach(function(row, index) {
                var tr = $('<tr></tr>');
                var region = row.REGION || row.region; 
                var count = row.FIRE_STATION_CNT || row.fire_station_cnt;

                tr.append('<td>' + (index + 1) + '</td>');
                tr.append('<td>' + region + '</td>');
                tr.append('<td>' + count + '</td>');
                tbody.append(tr);
            });
        },
        error: function(xhr, status, error) {
            console.error('AJAX 오류:', error);
        }
    });

    //복구현황
    $.ajax({
        url: '/ehr/fire/fire-safe.do',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            // 연도, 피해금액, 복구금액 배열 생성
            var labels = data.map(row => row.MSTN_YR);
            var damage = data.map(row => row.CFMTN_AMT);
            var recovery = data.map(row => row.CFMTN_RCRY);

            // Chart.js 그래프 생성
            var ctx = document.getElementById('fireSafeChart').getContext('2d');
            var fireChart = new Chart(ctx, {
                data: {
                    labels: labels,
                    datasets: [
                        {
                            type: 'bar',
                            label: '피해금액',
                            data: damage,
                            backgroundColor: 'rgba(255, 99, 132, 0.6)',
                            borderColor: 'rgba(255, 99, 132, 1)',
                            borderWidth: 1
                        },
                        {
                            type: 'line',
                            label: '복구금액',
                            data: recovery,
                            borderColor: 'rgba(54, 162, 235, 1)',
                            backgroundColor: 'rgba(54, 162, 235, 0.2)',
                            tension: 0.4,
                            fill: false,
                            yAxisID: 'y'
                        }
                    ]
                },
                options: {
                    responsive: true,
                    interaction: {
                        mode: 'index',
                        intersect: false,
                    },
                    plugins: {
                        title: {
                            display: true,
                            text: '년도별 화재 피해금액 및 복구금액'
                        },
                        tooltip: {
                            enabled: true,
                            mode: 'index'
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            title: {
                                display: true,
                                text: '금액 (단위: 원)'
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
});
