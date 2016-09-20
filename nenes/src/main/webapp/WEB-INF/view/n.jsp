<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
<meta name="description" content="nenes server main">
<meta name="author" content="pparkhyung">
<link rel="icon" href="/nenes/resource/bootstrap/favicon.ico">
<title>nenes</title>

<!-- Bootstrap -->
<link href="/nenes/resource/bootstrap/css/bootstrap.min.css"
    rel="stylesheet">

<!-- nenes Custom styles -->
<link href="/nenes/resource/nenes.css" rel="stylesheet">

  <link href="http://fonts.googleapis.com/css?family=Montserrat" rel="stylesheet" type="text/css">
  <link href="http://fonts.googleapis.com/css?family=Lato" rel="stylesheet" type="text/css">

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body id="myPage" data-spy="scroll" data-target=".navbar" data-offset="60">
    <!-- <h1>Hello, nenes!</h1> -->
    
    <nav class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed"
                    data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span> 
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#myPage">nenes</a>
            </div>
            <div id="navbar" class="collapse navbar-collapse">
                <ul class="nav navbar-nav">
                    <li class="active"><a
                        href="<c:url value="/n" />">home</a></li>
                    <li><a href="#services">services</a></li>
                    <li><a href="<c:url value="/ncommand" />">message</a></li>
                    <li><a href="<c:url value="/noperation" />">operation</a></li>
                    <li><a href="<c:url value="/nfile" />">file</a></li>
                </ul>
            </div>
            <!--/.nav-collapse -->
        </div>
    </nav>

    <div class="jumbotron text-center">
        <h1>nenes</h1>
        <p>server-agent model framework</p>
        <a href="https://slides.com/hyungminpark/deck/live#/" class="btn btn-info btn-lg" role="button" target="_blank">show slide</a>
    </div>
    
    <!-- Container (About Section) -->
    <div id="about" class="container-fluid">
        <div class="row">
            <div class="col-sm-8">
                <h2>About nene</h2>
                <br>
                <h4>nene is consist of nenes(server) and nenea(agent)</h4>
                <br>
                <p>nene is made by open source software 
                - web framwork(spring-mvc), front-end (bootstrap), socket (netty) </p>
                <br>
                <!-- <button class="btn btn-default btn-lg"> visit nene git</button> -->
                <a href="https://github.com/pparkhyung/devstu" class="btn btn-default btn-lg" role="button" target="_blank">nene gitbub</a>
                <a href="https://pparkhyung.gitbooks.io/nene/content/" class="btn btn-default btn-lg" role="button" target="_blank">nene gitbook</a>
            </div>
            <div class="col-sm-4">
                <span class="glyphicon glyphicon-log-in logo"></span>
            </div>
        </div>
    </div>

    <!-- Container (Services Section) -->
    <div id="services" class="container-fluid text-center">
        <h2>SERVICES</h2>
        <h4>nene offer</h4>
        <br>

        <div class="row slideanim">
            <div class="col-sm-4">
                <span class="glyphicon glyphicon-off logo-small"></span>
                <h4>Socket Server</h4>
                <p>accept agent connection</p>
            </div>
            <div class="col-sm-4">
                <span class="glyphicon glyphicon-heart logo-small"></span>
                <h4>Web Management</h4>
                <p>web console</p>
            </div>
            <div class="col-sm-4">
                <span class="glyphicon glyphicon-lock logo-small"></span>
                <h4>Socket Client</h4>
                <p>agent framework</p>
            </div>
        </div>
        <br> <br>

        <div class="row slideanim">
            <div class="col-sm-4">
                <span class="glyphicon glyphicon-leaf logo-small"></span>
                <h4>Message</h4>
                <p>send text message (Telnet)</p>
            </div>
            <div class="col-sm-4">
                <span class="glyphicon glyphicon-certificate logo-small"></span>
                <h4>Operation</h4>
                <p>send operation massage</p>
            </div>
            <div class="col-sm-4">
                <span class="glyphicon glyphicon-wrench logo-small"></span>
                <h4>File</h4>
                <p>transfer file to agent</p>
            </div>
        </div>
    </div>
    
<footer class="container-fluid text-center">
  <a href="#myPage" title="To Top">
    <span class="glyphicon glyphicon-chevron-up"></span>
  </a>
  <!-- <p>Bootstrap Theme Made By <a href="http://www.w3schools.com" title="Visit w3schools">www.w3schools.com</a></p> -->
</footer>

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script
        src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="/nenes/resource/bootstrap/js/bootstrap.min.js"></script>

    <script>
          $(document).ready(
              function() {
                // Add smooth scrolling to all links in navbar + footer link
                $(".navbar a, footer a[href='#myPage']").on('click',
                    function(event) {

                      // Make sure this.hash has a value before overriding default behavior
                      if (this.hash !== "") {
                        // Prevent default anchor click behavior
                        event.preventDefault();

                        // Store hash
                        var hash = this.hash;

                        // Using jQuery's animate() method to add smooth page scroll
                        // The optional number (900) specifies the number of milliseconds it takes to scroll to the specified area
                        $('html, body').animate({
                          scrollTop : $(hash).offset().top
                        }, 900, function() {

                          // Add hash (#) to URL when done scrolling (default click behavior)
                          window.location.hash = hash;
                        });
                      } // End if
                    });

                $(window).scroll(function() {
                  $(".slideanim").each(function() {
                    var pos = $(this).offset().top;

                    var winTop = $(window).scrollTop();
                    if (pos < winTop + 600) {
                      $(this).addClass("slide");
                    }
                  });
                });
              })
        </script>
</body>
</html>
