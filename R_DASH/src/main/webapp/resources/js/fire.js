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
                var count = row.FIRE_STATION_CNT || row.fire_station_cnt;

                tr.append('<td>' + (index + 1) + '</td>');
                tr.append('<td>' + region + '</td>');
                tr.append('<td>' + count + '</td>');
                tbody.append(tr);
            });

            // DataTables 초기화
            jq36('#firestationTable').DataTable({
                pageLength: 10,
                searching: true,
                ordering: true,
                destroy: true
            });
        },
        error: function(xhr, status, error) {
            console.error('AJAX 오류:', error);
        }
    });

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
                            yAxisID: 'y1'
                        }
                    ]
                },
                options: {
                    responsive: true,
                    interaction: { mode: 'index', intersect: false },
                    plugins: {
                        title: { display: true, text: '년도별 화재 피해금액 및 복구금액' },
                        tooltip: { mode: 'index' }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            title: { display: true, text: '피해금액 (원)' }
                        },
                        y1: {
                            beginAtZero: true,
                            position: 'right',
                            title: { display: true, text: '복구금액 (원)' },
                            grid: { drawOnChartArea: false }
                        }
                    }
                }
            });

        },
        error: function(xhr, status, error) {
            console.error('AJAX 오류:', error);
        }
    });

    jq36.ajax({
        url: '/ehr/fire/fire-yearly',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            const labels = data.map(row => row.YEAR);
            const counts = data.map(row => row.FIRE_COUNT);

            const ctxYearly = document.getElementById('yearlyChart').getContext('2d');

            new Chart(ctxYearly, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [{
                        label: '화재 건수',
                        data: counts,
                        borderColor: 'rgba(255, 159, 64, 1)',
                        backgroundColor: 'rgba(255, 159, 64, 0.2)',
                        fill: true,
                        tension: 0.2
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        title: { display: true, text: '년도별 화재 통계' },
                        legend: { display: true }
                    },
                    scales: {
                        y: { beginAtZero: true, title: { display: true, text: '건수' } }
                    }
                }
            });
        },
        error: function(xhr, status, error) {
            console.error('년도별 화재 통계 AJAX 오류:', error);
        }
    });

    jq36.ajax({
        url: '/ehr/fire/fire-damage', 
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            const labels = data.map(row => row.FIRE_TYPE);
            const damage = data.map(row => row.TOTAL_DAMAGE);

            const ctxDamage = document.getElementById('damageChart').getContext('2d');

            new Chart(ctxDamage, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: '재산피해 합계',
                        data: damage,
                        backgroundColor: 'rgba(75, 192, 192, 0.6)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        title: { display: true, text: '화재유형별 재산피해' },
                        legend: { display: true }
                    },
                    scales: {
                        y: { beginAtZero: true, title: { display: true, text: '재산피해 (원)' } }
                    }
                }
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
                        backgroundColor: 'rgba(75, 192, 192, 0.6)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        title: { display: true, text: '호선별 소화기 개수' }
                    },
                    scales: {
                        y: { beginAtZero: true }
                    }
                }
            });
        },
        error: function() { console.error('호선별 소화기 AJAX 오류'); }
    });
    
});
