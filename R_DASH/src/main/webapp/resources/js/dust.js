// PM10 등급 계산 함수
        function getGrade(pm10) {
            if(pm10 <= 30) return {text: "좋음", className: "normal"};
            else if(pm10 <= 80) return {text: "보통", className: "normal"};
            else if(pm10 <= 150) return {text: "나쁨", className: "bad"};
            else return {text: "매우 나쁨", className: "very-bad"};
        }

        $(document).ready(function() {
            // 평균 PM10
            $.ajax({
                url: "/ehr/dust/dust-avg",
                method: "GET",
                success: function(avg) {
                    const grade = getGrade(avg);
                    $("#avgCard").text(`현재 평균 PM10: ${avg} (${grade.text})`);
                    $("#avgCard").addClass(grade.className);
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
                                <td>${index + 1}</td>
                                <td>${item.STN_NM}</td>
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
                                <td>${index + 1}</td>
                                <td>${item.STN_NM}</td>
                                <td>${item.PM10}</td>
                                <td class="${grade.className}">${grade.text}</td>
                            </tr>`
                        );
                    });
                }
            });
        });