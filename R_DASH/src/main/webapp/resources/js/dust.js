// PM10 등급 계산 함수
function getGrade(pm10) {
    if(pm10 <= 30) return {text: "좋음", className: "good"};
    else if(pm10 <= 80) return {text: "보통", className: "normal"};
    else if(pm10 <= 150) return {text: "나쁨", className: "bad"};
    else return {text: "매우 나쁨", className: "very-bad"};
}

$(document).ready(function() {
    // 사용자 주소 (세션에서 내려준 값)
    let userAddress = window.loginUserAddress;

    // Geocoding API 호출 (예: Kakao)
    $.ajax({
        url: `/ehr/dust/geocode`,
        method: "GET",
        data: { address: userAddress },
        success: function(data) {
            console.log("위도:", data.lat, "경도:", data.lon);

            const lat = (data.lat != null && data.lat !== '') ? data.lat : null;
            const lon = (data.lon != null && data.lon !== '') ? data.lon : null;

            $.ajax({
                url: "/ehr/dust/dust-avg",
                method: "GET",
                data: { userLat: lat, userLon: lon },
                success: function(res) {
                    const grade = getGrade(res.value);
                    const displayText = `${res.region} 평균 PM10: ${res.value}`;
                    $("#avgCard").text(displayText)
                                .removeClass()
                                .addClass("card")
                                .addClass(grade.className);
                }
            });
        }
    });

    // TOP5
    $.ajax({
        url: "/ehr/dust/dust-top5",
        method: "GET",
        dataType: "json",
        success: function(data) {
            const table = $("#top5Table");
            $.each(data, function(index, item) {
                const grade = getGrade(item.PM10);
                table.append(
                    `<tr>
                        <td>${item.RNK}</td>
                        <td>${item.STN_LIST}</td>
                        <td>${item.PM10}</td>
                        <td class="${grade.className}">${grade.text}</td>
                    </tr>`
                );
            });
        }
    });

    // BOTTOM5
    $.ajax({
        url: "/ehr/dust/dust-bottom5",
        method: "GET",
        dataType: "json",
        success: function(data) {
            const table = $("#bottom5Table");
            $.each(data, function(index, item) {
                const grade = getGrade(item.PM10);
                table.append(
                    `<tr>
                        <td>${item.RNK}</td>
                        <td>${item.STN_LIST}</td>
                        <td>${item.PM10}</td>
                        <td class="${grade.className}">${grade.text}</td>
                    </tr>`
                );
            });
        }
    });
});