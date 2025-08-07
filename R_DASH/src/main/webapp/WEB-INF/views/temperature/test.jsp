<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>온열질환자 데이터 자동 수집</title>
</head>
<body>
    <h1>온열질환자 데이터 자동 수집 및 DB 저장</h1>
    <p>
        아래 버튼을 클릭하시면 API로부터 온열질환자 데이터를 자동으로 가져와 데이터베이스에 저장합니다.<br>
        (매뉴얼 입력이 아닙니다.)
    </p>
    
    <div>
        <button id="fetchDataBtn">API 데이터 수집 시작</button>
    </div>

    <p id="resultMessage"></p>

    <script>
        const fetchDataBtn = document.getElementById('fetchDataBtn');
        const resultMessageDiv = document.getElementById('resultMessage');

        fetchDataBtn.addEventListener('click', function() {
            resultMessageDiv.textContent = '데이터 수집을 요청했습니다. 서버 응답을 기다려주세요...';
            
            try {
                // Controller의 @GetMapping("/test-insert") 엔드포인트로 GET 요청을 보냅니다.
                // 이 엔드포인트는 temperatureService.fetchAndSaveData()를 호출합니다.
                const response = await fetch('/patients/test-insert', {
                    method: 'GET', // GET 요청입니다.
                    headers: {
                        'Accept': 'text/plain;charset=UTF-8' // Controller의 produces 속성과 일치시킵니다.
                    }
                });

                const text = await response.text(); // 컨트롤러가 반환하는 "실행 완료" 또는 오류 메시지

                if (response.ok) { // HTTP 상태 코드가 200번대(성공)이면
                    resultMessageDiv.textContent = '✔️ 성공: ' + text;
                    resultMessageDiv.style.color = 'green';
                } else { // 오류 응답이면
                    resultMessageDiv.textContent = '❌ 오류: ' + text;
                    resultMessageDiv.style.color = 'red';
                }
            } catch (error) {
                // 네트워크 오류 등 예외 발생 시
                resultMessageDiv.textContent = '🚨 네트워크 오류 또는 서버 연결 문제: ' + error.message;
                resultMessageDiv.style.color = 'red';
            }
        });
    </script>
</body>
</html>