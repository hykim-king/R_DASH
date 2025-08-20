document.addEventListener('DOMContentLoaded', function() {

	var width = 800, height = 300;

	var svg = d3.select("#wordcloud").append("svg") //div id="wordcloud"
	.attr("width", width)
	.attr("height", height)
	.append("g")
	.attr("transform", "translate(" + width/2 + "," + height/2 + ")");  
	
	//단어 빈도 스케일
	var wordScale = d3.scale.linear()
	                   .domain([0, d3.max(wordData, function(d){ return d.freq;})]) //데이터의 범위, 입력 크기
	                   .range([30, 100]) 
                       .clamp(true); //도메인 크기를 넘긴 값에 대해 도메인 최대값으로 고정

    //var colorScale = d3.scale.linear()
          //  .domain([0, d3.max(wordData, function(d){ return d.freq; })]);
            //.range(["#FFE5B4", "#FF8C00"]);
	
	function showCloud(data){
		d3.layout.cloud().size([width,height])
		.words(data)
		.rotate(function(d){ return d.text.length > 3 ? 0 : 90;})
	    .fontSize(function(d){return wordScale(d.freq);})
	    .on("end",draw)//클라우드 레이아웃을 초기화 > end 이벤트 발생 > 연결된 함수 작동
	    .start();

	}
	function draw(words) { 
        var cloud = svg.selectAll("text").data(words)
        //Entering words
        cloud.enter()
            .append("text")
            .attr("text-anchor", "middle") 
            .style("fill", function (d) {
                        return (keywordTexts.indexOf(d.text) > -1 ? "#fbc280" : "#405275");
                    })
            .attr('font-size', function(d){ return wordScale(d.freq) + "px"; }) //freq 기반 적용
            .text(function (d) {
                return d.text;
            }); 
        cloud
            .transition()
            .duration(600)
            .style("font-size", function (d) {
                return d.size + "px";
            })
            .attr("transform", function (d) {
                return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
            })
            .style("fill-opacity", 1); 
    }
	showCloud(wordData);

});