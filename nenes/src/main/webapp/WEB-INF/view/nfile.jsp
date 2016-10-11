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
                    <li><a href="<c:url value="/n" />">home</a></li>
                    <li><a href="<c:url value="/ncommand" />">message</a></li>
                    <li><a href="<c:url value="/noperation" />">operation</a></li>
                    <li class="active"><a href="<c:url value="/nfile" />">file</a></li>
                    <li><a href="<c:url value="/nconfiguration" />">configuration</a></li>
                </ul>
            </div>
            <!--/.nav-collapse -->
        </div>
    </nav>

    <div class="jumbotron text-center">
        <h1>File</h1>
        <p>send file to connected nenea(agent)</p>
        
<form:form class="form-horizontal" action="nfile" method="post" commandName="fileMessage">

  <div class="form-group">
    <label class="control-label col-sm-4" for="operation" >Choice : </label>
    <div class="col-sm-6 agent"  style="background-color:#6f60b3">
        <form:checkboxes path="agent" items="${agents}" />
    </div>
  </div>

  <div class="form-group">
    <label class="control-label col-sm-4" for="operation" >File List : </label>
    <div class="col-sm-6 agent"  style="background-color:#6f60b3">
        <form:checkboxes path="fileList" items="${fileList}" />
    </div>
  </div>
  
  <div class="form-group">
    <label class="control-label col-sm-4" for="operation">File : </label>
    <div class="col-sm-6">
      <input type="text" class="form-control" id="fileName" name="fileName" placeholder="enter file location" value="${fileName}">
    </div>
  </div>
  
  <div class="form-group"> 
    <div class="col-sm-offset-4 col-sm-5" >
      <button type="submit" class="btn btn-info btn-lg">Send</button>    
    </div>
  </div>
  
</form:form>
        </div>
        
    
    <!-- /.container -->

    <!-- Bootstrap core JavaScript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script
        src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="/nenes/resource/bootstrap/js/bootstrap.min.js"></script>

</body>
</html>
