document.addEventListener('DOMContentLoaded', function() {

    // 데이터에서 최대값 계산
    const maxFreq = Math.max(...top10Data.map(item => item.freq));

    // round up 
    const roundedMax = Math.ceil((maxFreq) / 100) * 100;

    //차트 로드
    const ctx = document.getElementById("top10Chart").getContext("2d");
    //차트 영역
    const top10Chart = new Chart(ctx, {
        type: 'bar',
         data: {
            labels: top10Data.map(item => item.text),
            datasets: [{
                label: "단어 발생 빈도",
                data: top10Data.map(item => item.freq),
                backgroundColor: "rgba(255, 210, 220, 0.93)",
                borderColor: "rgba(255, 99, 132, 1)",
                borderWidth: 1
            }]
        },
         options: {
            indexAxis: 'y',
            responsive: true,
            plugins: {
                title: { display: true }
            },
        scales: {
               x: {
                beginAtZero: true,  // x축 0부터 시작
                max: roundedMax,
                barPercentage: 0.8,      // 막대 자체 너비
                categoryPercentage: 0.9  // 카테고리 간격
                
            },
            y: {
                ticks: {
                    autoSkip: false,  // y축 레이블 자동 생략 방지
                    stepSize : 50
                }
              }
          }
        }

    });
});