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

// REH + RN1
function loadREHRN1Table() {
    $.when(
        $.ajax({ url: '/ehr/temperature/weather.do', data: { category: 'REH' } }),
        $.ajax({ url: '/ehr/temperature/weather.do', data: { category: 'RN1' } })
    ).done(function(rehData, rn1Data) {
        let tbody = $('#rehRn1Table');
        tbody.empty();
        let rehValues = rehData[0];
        let rn1Values = rn1Data[0];

        for(let i=0; i<rehValues.length; i++){
            tbody.append(`<tr>
                <td>${i + 1}</td>
                <td>${rehValues[i].sidoNm} ${rehValues[i].signguNm}</td>
                <td>${rehValues[i].obsrValue}</td>
                <td>${rn1Values[i].obsrValue}</td>
            </tr>`);
        }
    });
}

// 페이지 로딩 시
$(document).ready(function() {
    loadT1HTable();
    loadREHRN1Table();
});