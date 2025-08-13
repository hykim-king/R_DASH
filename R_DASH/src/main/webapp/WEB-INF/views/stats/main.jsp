<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style>
.chart-wrap {
  width: 900px;      
  height: 900px;
  overflow: visible;
}
canvas { width: 100% !important; height: 100% !important; }
</style>


<title>통계 페이지</title>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="https://cdn.jsdelivr.net/npm/chartjs-plugin-datalabels@2"></script>
<script>
//도큐먼트 실행 완료하면 실행
document.addEventListener('DOMContentLoaded', function() {
  const canvas = document.getElementById('SelectTemperature');
  
  Chart.register(ChartDataLabels);

  const hoverOff = 30;
  
  const myChart = new Chart(canvas, {
    type: 'doughnut',
    data: {
      labels: ['화재', '폭염', '한파', '산사태', '싱크홀', '황사'],
      datasets: [{
        label: '통계 선택',
        data: [1, 1, 1, 1, 1, 1],
        hoverOffset: hoverOff,
        backgroundColor: [ // 색상을 명시적으로 지정하여 라벨 색상(white)과 대비되게 합니다.
            'rgb(255, 99, 132)',
            'rgb(54, 162, 235)',
            'rgb(255, 205, 86)',
            'rgb(75, 192, 192)',
            'rgb(153, 102, 255)',
            'rgb(255, 159, 64)'
        ],
      }]
    },
    options: {
      responsive: true, // 반응형 차트 설정
      // 차트 클릭 시
      layout: {
        // hoverOffset 만큼 내부 여백을 확보하면 튀어나와도 잘리지 않습니다.
        padding: {
            top: hoverOff,
            bottom: hoverOff,
            left: hoverOff,
            right: hoverOff
        }
      },
      plugins: {
        // ***** 핵심: datalabels 플러그인 설정 *****
        tooltip: {
            enabled: false
        },
        legend: {
          display: false // 범례 숨김.
        },
        datalabels: {
          color: 'black', // 라벨 텍스트 색상
          textAlign: 'center', // 텍스트 정렬
          font: {
            weight: 'bold',
            size: 36 // 라벨 폰트 크기
          },
          formatter: (value, context) => {
            const labelName = context.chart.data.labels[context.dataIndex]; //1. 라벨 이름

            // 2. 라벨 이름 리턴
            return labelName;

          },
          anchor: 'center', // 라벨을 도넛 조각의 중앙에 위치시킴
          align: 'center', // 라벨 텍스트 정렬 (anchor와 함께 사용)
        }
      },
    }
  });

  canvas.onclick = (event) => {
    const points = myChart.getElementsAtEventForMode(event, 'nearest', { intersect: true }, true);
    if (points.length) {
        const idx = points[0].index;
        const label = myChart.data.labels[idx]; // '화재' 등
        // 라벨을 type 코드로 매핑 (안정성을 위해 매핑 사용 권장)
        const map = { '화재': 'Fire', '폭염': 'Heatwave', '한파': 'Coldwave', '산사태': 'Landslide', '싱크홀': 'Sinkhole', '황사': 'Dust' };
        const type = map[label] || '';
        window.location.href = '/ehr/temperature/temperatureView.do?type='+ encodeURIComponent(type);
    }
  };
}) 
</script>

</head>
<body>
    <div class="chart-wrap">
        <canvas id="SelectTemperature"></canvas>
    </div>
</body>
</html>