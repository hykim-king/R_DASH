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
                        label: '년도별 산사태 건수',
                        data: counts,
                        backgroundColor: 'rgba(75, 192, 192, 0.6)'
                    }]
                },
                options: { responsive: true }
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
                        label: '지역별 산사태 건수',
                        data: counts,
                        backgroundColor: 'rgba(255, 99, 132, 0.6)'
                    }]
                },
                options: { responsive: true }
            });
        }
    });

    $.ajax({
        url: '/ehr/landslide/month',
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            const labels = data.map(row => row.MONTH);
            const counts = data.map(row => row.TOTAL_COUNT);

            new Chart($('#monthChart'), {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: '월별 산사태 건수',
                        data: counts,
                        backgroundColor: 'rgba(54, 162, 235, 0.6)'
                    }]
                },
                options: { 
                  responsive: true,
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
                        label: '예보상태별 산사태 건수',
                        data: counts,
                        backgroundColor: 'rgba(255, 206, 86, 0.6)'
                    }]
                },
                options: { 
                  indexAxis: 'y',
                  responsive: true }
            });
        }
    });
});