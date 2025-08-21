document.addEventListener('DOMContentLoaded', function() {

	var width = 800, height = 350;

	var svg = d3.select("#wordcloud").append("svg") //div id="wordcloud"
	.attr("width", width)
	.attr("height", height)
	.append("g")
	.attr("transform", "translate(" + width/2 + "," + height/2 + ")");  
	
	//단어 빈도 스케일
	var wordScale = d3.scale.linear()
	                   .domain([0, d3.max(wordData, function(d){ return d.freq;})]) //데이터의 범위, 입력 크기
	                   .range([5, 80]) 
                       .clamp(true); //도메인 크기를 넘긴 값에 대해 도메인 최대값으로 고정

    var colorScale1 = d3.scale.linear()
        .domain([0, d3.max(wordData, function(d){ return d.freq; })])
        .range(["#FFE5B4", "#FF8C00"]);

    var colorScale2 = d3.scale.linear()
    .domain([0, d3.max(wordData, function(d){ return d.freq; })])
    .range(["#b4f3ffff", "#8792d2ff"]);
	
	function showCloud(data){
		d3.layout.cloud().size([width,height])
		.words(data)
		.rotate(function(d) { return d.text.length > 3 ? (Math.random() < 0.5 ? 0 : 90) : 0; })
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
            .style("fill", function(d) {
                return (keywordTexts.indexOf(d.text) > -1 ? colorScale1(d.freq) : "#395a8dff");
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