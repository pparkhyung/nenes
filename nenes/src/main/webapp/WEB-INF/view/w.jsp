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

<link href="http://fonts.googleapis.com/css?family=Montserrat"
    rel="stylesheet" type="text/css">
<link href="http://fonts.googleapis.com/css?family=Lato"
    rel="stylesheet" type="text/css">
<link href="http://fonts.googleapis.com/icon?family=Material+Icons"
    rel="stylesheet">

<!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body id="myPage" data-spy="scroll" data-target=".navbar"
    data-offset="60">

    <nav class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed"
                    data-toggle="collapse" data-target="#navbar"
                    aria-expanded="false" aria-controls="navbar">
                    <span class="sr-only">Toggle navigation</span> <span
                        class="icon-bar"></span> <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#myPage">nenes</a>
            </div>
            <div id="navbar" class="collapse navbar-collapse">
                <ul class="nav navbar-nav">
                
                <li>
                        <div class="dropdown">
                            <button
                                class="btn btn-primary dropdown-toggle"
                                id="menu1" type="button"
                                data-toggle="dropdown" style="background-color:#6f60b3; border-color:#6f60b3">
                                <i class="material-icons" 
                                    style="font-size: 36px; color: white">notifications_none</i>
                                     <span id="noti-count" class="badge badge-notify" style="color:#6f60b3"></span>
                            </button>
                            <ul id="noti" class="dropdown-menu" role="menu" aria-labelledby="menu1">
                            </ul>
                        </div>
                    </li>
                
                    <%-- <li><a href="<c:url value="/w" />">home</a></li> --%>
                    <li><a href="<c:url value="/ncommand" />" target="iframe_ws">message</a></li>
                    <li><a href="<c:url value="/noperation"/>" target="iframe_ws">operation</a></li>
                    <li><a href="<c:url value="/nfile"/>" target="iframe_ws">file</a></li>
                    <li><a href="<c:url value="/nconfiguration" />" target="iframe_ws">configuration</a></li>
                </ul>
                
            </div>
            <!--/.nav-collapse -->
            
        </div>
    </nav>

    <iframe src="<c:url value="/noperation"/>" id="iframe_ws" name="iframe_ws" width="100%" height="98%" style="border:none"></iframe>

    <div id="output"></div>

    <script
        src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="/nenes/resource/bootstrap/js/bootstrap.min.js"></script>
    <script src="<c:url value="/resource/js/ws.js"/>"></script>
    <script>
          var noti_a = "notifications_active";
          var noti_n = "notifications_none";
          var eventOn = false;
          var notiCount = 0;

          $(document).ready(function() {
            $(".dropdown").on("show.bs.dropdown", function(event) {
              //var x = $(event.relatedTarget).text(); // Get the button text
              //alert("You clicked on: " + x);
              $(".material-icons").text(noti_n);
              $(".badge").hide();
            });
            
            $(".dropdown").on("hide.bs.dropdown", function(event) {
              /* $(".dropdown-menu").html("<li role=\"presentation\"><a role=\"menuitem\" tabindex=\"-1\" href=\"#\">" + "no event" + "</a></li>"); */
              $(".dropdown-menu").html("");
            });
            
            //$(".material-icons").click(function() {
              //alert("Value: " +   $(".material-icons").text());
              //$(".material-icons").text(noti_n);
              //eventOn = false;
              //informEvent(evt);
            //});
            
          });
          
          function informEvent(evt) {
            var message = evt.data;
            //alert(message);
            $(".material-icons").text(noti_a);
            $(".dropdown-menu").append("<li role=\"presentation\"><a role=\"menuitem\" tabindex=\"-1\" href=\"#\">" + message + "</a></li>");
            //notiCount = $(".dropdown-menu").childElementCount;
            notiCount = document.getElementById("noti").childElementCount;
            //alert(notiCount);
            $("#noti-count").text(notiCount);
            $(".badge").show();
          }
          
        </script>

</body>
</html>
