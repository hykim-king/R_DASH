function loadTopWeather(category) {
    $.ajax({
        url: '/ehr/temperature/weather.do',
        type: 'GET',
        success: function(data) {
            // 선택한 category만 필터
            let filtered = data.filter(d => d.category === category);

            let labels = filtered.map(d => d.sidoNm);
            let values = filtered.map(d => d.obsrValue);

            console.log(filtered); // 데이터 확인용
            console.log(labels, values);

            let ctx = document.getElementById('weatherChart').getContext('2d');

            // 기존 차트 제거
            if(window.weatherChart) {
                window.weatherChart.destroy();
            }

            // 막대 그래프 예시
            window.weatherChart = new Chart(ctx, {
                type: 'bar', // 필요하면 'horizontalBar' 또는 'radar' 등 변경 가능
                data: {
                    labels: labels,
                    datasets: [{
                        label: category,
                        data: values,
                        backgroundColor: 'rgba(75, 192, 192, 0.6)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    indexAxis: 'y', // 수평막대로
                    scales: {
                        x: { beginAtZero: true, title: { display: true, text: category } },
                        y: { title: { display: true, text: '지역' } }
                    }
                }
            });
        }
    });
}

// 드롭다운 이벤트
$('#dataTypeSelect').on('change', function() {
    loadTopWeather(this.value);
});

// 페이지 로딩 시 초기 값
loadTopWeather($('#dataTypeSelect').val());

