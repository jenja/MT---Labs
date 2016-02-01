function sp(){

    var self = this; // for internal d3 functions
	var employ = "Employment rate";
	var earnings = "Personal earnings";
    var spDiv = $("#sp");

    var margin = {top: 20, right: 20, bottom: 30, left: 40},
        width = spDiv.width() - margin.right - margin.left,
        height = spDiv.height() - margin.top - margin.bottom;

    //initialize color scale
    var color = d3.scale.category20();
    
    //initialize tooltip
    //...

    var x = d3.scale.linear()
        .range([0, width]);

    var y = d3.scale.linear()
        .range([height, 0]);

    var xAxis = d3.svg.axis()
        .scale(x)
        .orient("bottom");

    var yAxis = d3.svg.axis()
        .scale(y)
        .orient("left");

    var svg = d3.select("#sp").append("svg")
        .attr("width", width + margin.left + margin.right)
        .attr("height", height + margin.top + margin.bottom)
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

    //Load data
    d3.csv("data/OECD-better-life-index-hi.csv", function(error, data) {
        self.data = data;
        
        //define the domain of the scatter plot axes
        //...
		console.log(data);
		
		//d3.select(self.data[1]
		x.domain(d3.extent(data,function(d){return d[employ]}));
        y.domain(d3.extent(data,function(d){return d[earnings]}));
        draw();

    });

    function draw()
    {
        
        // Add x axis and title.
        svg.append("g")
            .attr("class", "x axis")
            .attr("transform", "translate(0," + height + ")")
            .call(xAxis)
            .append("text")
			.text(employ)
			.attr("font-family", "Raleway")
			.attr("font-size", "110px")
			.attr("fill", "blue")
            .attr("class", "label")
            .attr("x", width - 150)
            .attr("y", 20);
        // Add y axis and title.
        svg.append("g")
            .attr("class", "y axis")
            .call(yAxis)
            .append("text")
			.text(earnings)
            .attr("class", "label")
            .attr("transform", "rotate(-90)")
            .attr("y", 10)
            .attr("dy", ".71em");
            
        // Add the scatter dots.
        svg.selectAll(".dot")
            .data(self.data)
            .enter().append("circle")
            .attr("class", "dot")
            //Define the x and y coordinate data values for the dots
            //...
			.attr("cx", function(d){return x(d[employ])})
			.attr("cy", function(d){return y(d[earnings])})
			.attr("r", 4)
			.style("fill", function(d, i) { return color(i); })
            //tooltip
            .on("mousemove", function(d) {
                //...
				//return d["Country"];
            })
            .on("mouseout", function(d) {
                //...   
            })
            .on("click",  function(d) {
                pc1.selectLine(d["Country"]);
				console.log(d["Country"]);
            });
    }

    //method for selecting the dot from other components
    this.selectDot = function(value){
		d3.selectAll(".dot")
		.style("opacity", function(d){
				return (d["Country"] == value) ? 1 : "none"
				});
    };
	
	
	this.replotDot = function(){
		d3.selectAll(".dot")
		.style("opacity", function(d){ return 0; });
    };
    
    //method for selecting features of other components
    function selFeature(value){
        sp1.selectDot(value);
        pc1.selectLine(value);
    }

}




