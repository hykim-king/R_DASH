document.addEventListener('DOMContentLoaded', function() {
  const canvas = document.getElementById('SelectTemperature');

  const centerImagePlugin = {
  id: 'centerImage',
  afterDraw: (chart) => {
    const { ctx, chartArea: { left, right, top, bottom } } = chart;
    const centerX = (left + right) / 2;
    const centerY = (top + bottom) / 2;

    // 이미지를 미리 전역 변수로 로드해둔다
    if (!centerImagePlugin.image) {
      const img = new Image();
      img.src = '/ehr/resources/image/Jaemini_face (1).png';
      centerImagePlugin.image = img;
      img.onload = () => chart.draw(); // 로딩 완료 후 다시 그리기
    }

    const image = centerImagePlugin.image;
    if (image.complete) {  // 로딩 완료된 경우만 그림
      const imgSize = 90;
      ctx.drawImage(image, centerX - imgSize / 2, centerY - imgSize / 2, imgSize, imgSize);
    }
  }
};

  const hoverOff = 30;
  
  const myChart = new Chart(canvas, {
    type: 'doughnut',
    data: {
      labels: ['화재', '기온', '산사태', '싱크홀', '황사'],
      datasets: [{
        label: '통계 선택',
        data: [1, 1, 1, 1, 1],
        hoverOffset: hoverOff,
        backgroundColor: [ // 색상을 명시적으로 지정하여 라벨 색상(white)과 대비되게 합니다.
            'rgb(255, 99, 132)',
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
        // ** datalabels 플러그인 설정 **
        tooltip: {
            enabled: false
        },
        legend: {
          display: false // 범례 숨김.
        },
        datalabels: {
          display: true,
          hoverOffset: hoverOff,
          color: 'black', // 라벨 텍스트 색상
          textAlign: 'center', // 텍스트 정렬
          font: {
            weight: 'bold',
            size: 16 // 라벨 폰트 크기
          },
          formatter: (value, context) => {
            const labelName = context.chart.data.labels[context.dataIndex]; //1. 라벨 이름
            // 2. 라벨 이름 리턴
            return labelName;
          },
          anchor: 'center', // 라벨을 도넛 조각의 중앙에 위치시킴
          align: 'center', // 라벨 텍스트 정렬 (anchor와 함께 사용)
          offset: 0
        }
      },
    },
    plugins: [centerImagePlugin, ChartDataLabels]
  });

  canvas.onclick = (event) => {
    const points = myChart.getElementsAtEventForMode(event, 'nearest', { intersect: true }, true);
    if (points.length) {
        const idx = points[0].index;
        const label = myChart.data.labels[idx]; // '화재' 등
        // 라벨을 type 코드로 매핑 (안정성을 위해 매핑 사용 권장)
        const map = { '화재': 'fire', '기온': 'temperature', '산사태': 'landslide', '싱크홀': 'sinkholes', '황사': 'dust' };
        const type = map[label] || '';
        window.location.href = `/ehr/${encodeURIComponent(type)}/statsPage`;
    }
  };

}) 

