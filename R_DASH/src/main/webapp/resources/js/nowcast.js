// T1H
function loadT1HTable() {
    $.ajax({
        url: '/ehr/temperature/weather.do',
        type: 'GET',
        data: { category: 'T1H' },
        success: function(data) {
            let tbody = $('#t1hTable');
            tbody.empty();
            data.forEach((d, idx) => {
                tbody.append(`<tr>
                  <td>${idx + 1}</td>
                  <td>${d.sidoNm} ${d.signguNm}</td>
                  <td>${d.obsrValue}</td>
                </tr>`);
            });
        }
    });
}

// REH (습도)
function loadREHTable() {
    $.ajax({
        url: '/ehr/temperature/weather.do',
        type: 'GET',
        data: { category: 'REH' },
        success: function(data) {
            let tbody = $('#rehTable'); // REH용 테이블 tbody
            tbody.empty();
            data.forEach((d, idx) => {
                tbody.append(`<tr>
                  <td>${idx + 1}</td>
                  <td>${d.sidoNm} ${d.signguNm}</td>
                  <td>${d.obsrValue}</td>
                </tr>`);
            });
        }
    });
}

// RN1 (강수량)
function loadRN1Table() {
    $.ajax({
        url: '/ehr/temperature/weather.do',
        type: 'GET',
        data: { category: 'RN1' },
        success: function(data) {
            let tbody = $('#rn1Table'); // RN1용 테이블 tbody
            tbody.empty();
            data.forEach((d, idx) => {
                tbody.append(`<tr>
                  <td>${idx + 1}</td>
                  <td>${d.sidoNm} ${d.signguNm}</td>
                  <td>${d.obsrValue}</td>
                </tr>`);
            });
        }
    });
}

// 페이지 로딩 시
$(document).ready(function() {
    loadT1HTable();
    loadREHTable()
    loadRN1Table()
});