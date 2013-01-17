   $.getJSON('/crowdsourcing/company', function(data) {
	   var items = ['<option id="0"> </option>'];
	  
	   $.each(data.companies, function(index, item) {
	     items.push('<option value="' + item.id + '">' + item.name + '</option>');
	   });
	  
	   $('<select/>', {
	     'id' : 'companySelection',
	     html: items.join('')
	   }).appendTo('#companySelectionForm');
	   $('#companySelection').change(function() { CompanyController.init(this.value); });
	   //alert(JSON.stringify(data));
	 });
   

	 function between(value, from, to) {
		 return value >= from  && value <= to;
	 }
	  
	 var HtmlUtil = {
	 	setRatingSpan : function(rating, ratingSpanId) {
	 		var backgroundColor, fontColor, description;
	 		//should be replaced with just setting proper css classes
	 		if(between(rating, -5, -3)) 	{ backgroundColor = 'red'; 		fontColor = 'black'; description = 'Negative'; }
	 		if(between(rating, -3, -1.5)) 	{ backgroundColor = 'yellow'; 	fontColor = 'black'; description = 'Poor';}
	 		if(between(rating, -1.5, 1.5)) 	{ backgroundColor = 'lightgray'; 	fontColor = 'black'; description = 'Neutral';}
	 		if(between(rating, 1.5, 5)) 	{ backgroundColor = 'green'; 	fontColor = 'white'; description = 'Positive';}
	 		
	 		var span = $('#' + ratingSpanId);
	 		span.css('background-color', backgroundColor);
	 		span.css('color', fontColor);
	 		span.css('font-weight', 'bold');
	 		span.text(description + ' (Sentiment Value ' + rating.toFixed(2) + ')');
	 	}			
	 }
	 
	 
	 /*
	 	Singleton local storage of the selected company and its products
	 	Probably not the nicest solution, but it serves its purpose for now
	 */
     var CompanyController = {
    	_contentId : 'companyDetail',
    	_company : null,
    	chart : null,
    		 
    	setCompany : function(company) {
    		this._company = company;
    	},
    	
    	getName : function() {
    		if(this._company == null) { return ''; }
    		return this._company.name;
    	},
    	
     	init : function(id) {
     		$('#home').hide();
     		$('#' + this._contentId).show();
     		
     		//load product
     		var companyDetailUrl = '/crowdsourcing/company/' + id;
     		$.getJSON(companyDetailUrl, function(companyData) {
         		if(companyData == null) { return; }
         		
         		//fetch all product ids
         		/*var productIds = [];
         		for(var i=0; i < companyData.products.length; i++) {
         			productIds.push(companyData.products);
         		}*/
         		 
         		
         		CompanyController.setCompany(companyData);
         		
         		$('#companyName').text(companyData.name);
         		HtmlUtil.setRatingSpan(companyData.rating, 'overallRating');
         		
         		//iterate through products
         		var products = companyData.products;
         		var productHtml = [];
         		for(var i = 0; i < products.length; i++) {
         			productHtml.push('<li>' + products[i].name + '</li>');
         		}
         		if(products.length > 0) {
       	    	    $('#productList').html(productHtml.join(''));		         			
         			$('#products').show();
         		}
         		else {
         			$('#products').hide();
         		}
         	
         		//load company ratings
         		var companyRatingsUrl = '/crowdsourcing/company/' + companyData.id  +  '/ratings';
         		$.getJSON(companyRatingsUrl, function(ratingData) { 
         			if(ratingData == null) { return; }
         			
         			var ratingXAxis = [];
         			var dataPoints = [];
         			var ratingDate;
         			
         			for(var i = 0; i < ratingData.length; i++) {
         				ratingDate = new Date(ratingData[i].lastModified);
         				//ratingXAxis[i] = ratingDate.getDate() + '.' + (ratingDate.getMonth()+1) + '.' + ratingDate.getYear();
         				dataPoints[i] = ratingData[i].rating;
         			}
         		
	         		CompanyController.chart = new Highcharts.Chart({
	                    chart: {
	                        renderTo: 'container',
	                        type: 'line',
	                        marginRight: 130,
	                        marginBottom: 25
	                    },
	                    title: {
	                        text: 'Sentiment over Time',
	                        x: -20 //center
	                    },
	                    subtitle: {
	                        text: 'Aggregated through Human Computation',
	                        x: -20
	                    },
	                    xAxis: {
	                        categories: ratingXAxis
	                    },
	                    yAxis: {
	                        title: {
	                            text: 'Sentiment Value (Scale from -5 to 5)'
	                        },
	                        plotLines: [{
	                            value: 0,
	                            width: 1,
	                            color: '#808080'
	                        }]
	                    },
	                    tooltip: {
	                        formatter: function() {
	                                return '<b>'+ this.series.name +'</b><br/>'+
	                                'Sentiment: '+ this.y;
	                        }
	                    },
	                    legend: {
	                        layout: 'vertical',
	                        align: 'right',
	                        verticalAlign: 'top',
	                        x: -10,
	                        y: 100,
	                        borderWidth: 0
	                    },
	                    series: [{
	                        name: CompanyController.getName(),
	                        data: dataPoints
	                    }]
	                });
         		});
         		
         		var prodcutRatingsUrl;
         		for(var i = 0; i < products.length; i++) {
         			productRatingsUrl = '/crowdsourcing/product/' + products[i].id  +  '/ratings';
	         		$.getJSON(productRatingsUrl, 
	         			(function(productName) {  //closure in order to pass the product name into the context
	         				return function(ratingData) {
			         			var dataPoints = [];
			         			for(var i = 0; i < ratingData.length; i++) {
			         				dataPoints[i] = ratingData[i].rating;
			         			}	
			         			CompanyController.chart.addSeries({name: productName, data: dataPoints});
			         		}
	         			})(products[i].name)
	         		);
         		}		         		
         	});
     	}
     };
