<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false"%>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <title></title>
        <meta name="description" content="">
        <meta name="viewport" content="width=device-width">

        <link rel="stylesheet" href="resources/css/bootstrap.min.css">
        <style>
            body {
                padding-top: 60px;
                padding-bottom: 40px;
            }
        </style>
        <link rel="stylesheet" href="resources/css/bootstrap-responsive.min.css">
        <link rel="stylesheet" href="resources/css/main.css">

        <script src="resources/js/vendor/modernizr-2.6.2-respond-1.1.0.min.js"></script>
    </head>
    <body>
        <!--[if lt IE 7]>
            <p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
        <![endif]-->

        <!-- This code is taken from http://twitter.github.com/bootstrap/examples/hero.html -->

        <div class="navbar navbar-inverse navbar-fixed-top">
            <div class="navbar-inner">
                <div class="container">
                    <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </a>
                    <a class="brand" href="#">aic crowdsourcing w/ mobileworks</a>
                    <div class="nav-collapse collapse">
                        <ul class="nav">
                        </ul>
                        <form id="companySelectionForm" class="navbar-form pull-right">
	                       Choose Company: 
                        </form>
                    </div><!--/.nav-collapse -->
                </div>
            </div>
        </div>

        <div class="container">

			<div id="home">
	            <!-- Main hero unit for a primary marketing message or call to action -->
	            <div class="hero-unit">
	                <h1>Sentiment Analysis via Crowdsourcing w/ Mobileworks</h1>
	                <p style="font-style: italic">
	                	[Crowdsourcing] is based on the assumption that there are tasks that are almost impossible 
	                	to solve algorithmically, which can still be easily solved by any human (even without specific job training).
	                </p>
	                <p>
	                	In this project we perform sentiment analysis on companies and their products based on articles fetched from <a href="">Yahoo! Finance</a> leveraging the crowdsourcing platform <a href="https://www.mobileworks.com/">Mobileworks</a>. 
	                </p>
	                <p>
	                	Choose one of the companies on top of the page to get started.
	                </p>                
	            </div>
	        </div>
	        
	        <div id="companyDetail" style="display: none;">
	        	<h2 id="companyName"></h2>
	        	Current company sentiment (overall): <span id="overallRating"></span> 
	        	
	        	<div id="products">
	        		<h2>Products</h2>
	        		<ul id="productList">
	        			
	        		</ul>
	        	</div>
	        	
				<div id="container" style="min-width: 400px; height: 400px; margin: 0 auto"></div>	        	
	        </div>
	
	            <hr>

            <footer>
                <p>Happily provided by Michael Abseher, Johannes Birgmeier, Bernhard Bonigl, Jürgen Cito, Christoph Laaber, Martin Planer, Bernd Rathmanner
                </p>
            </footer>

        </div> <!-- /container -->

        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.8.3/jquery.min.js"></script>
        <script>window.jQuery || document.write('<script src="js/vendor/jquery-1.8.3.min.js"><\/script>')</script>

        <script src="resources/js/vendor/bootstrap.min.js"></script>

        <script src="resources/js/main.js"></script>

		
		<script src="http://code.highcharts.com/highcharts.js"></script>
		<script src="http://code.highcharts.com/modules/exporting.js"></script>		
		
        <script>
 
	       
          </script>    
    </body>
</html>