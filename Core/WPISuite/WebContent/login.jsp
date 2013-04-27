<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>
			Requirements Management
		</title>
		
		
		<link href="css/normalize.css" rel="stylesheet">
		<link href="css/foundation.css" rel="stylesheet">	
		<link href="css/flat-ui.css" rel="stylesheet">		
		<link href="css/deniz.css" rel="stylesheet" >
		
		<script src="js/vendor/jquery.js">
		</script>
		
		<script>
			var __adobewebfontsappname__ = "code"</script>
		<script src="http://use.edgefonts.net/quicksand:i4,n3,i7,n7,n4,i3:all.js">
		</script>
		
		<script>
			var __adobewebfontsappname__ = "code"</script>
		<script src="http://use.edgefonts.net/lobster:n4:all;quicksand:i4,n3,i7,n7,n4,i3:all.js">
		</script>
		
	</head>
	
	
	<body>
		
		<img src="img/blurred2.jpg" class="center .antialiased" id="photto" >
		
		
		<div  id="login-dialog" title="WPI Suite Requirements viewer">
			<div class="row" >
				<div class="small-12 large-6 large-limit small-centered columns "   style="padding-top:10%;"  >
				</div>
			</div>
			
			
			<div class="row" >
				<div class="small-12 large-5 large-limit small-centered columns "     >
					
					<p id="bigTitle"  >
						Requirements Management
					</p>
				</div>
				<div class="small-12 large-3 large-limit small-centered columns "   id="smallTitle"  >
					
					<p  >
						by Dragon Sparkles, Inc.
					</p>
				</div>
			</div>
			
			<div class="row" >
				<div class="small-12 large-6 large-limit small-centered columns "     >
					
					
					<form>
						<fieldset>
							
							<br>
							
							
							<label for="loginusername">
								Username:
							</label>
							<input type="text" placeholder="username" id="loginusername" name = "loginusername" class="">
							
							
							
							<label for = "loginpassword">
								Password:
							</label>
							<input type="password" placeholder="password" id="loginpassword" name="loginpassword" class="">
							
							<h4 id="badlogin"  class="">		</h4>
							
							<label for = "loginproject">
								Project:
							</label>
							
							<input type="text" placeholder="project" id="loginproject" name = "loginproject" >
							
							<h4 id="badproject" class="">		</h4>
							
							<input class="btn btn-large btn-block small-12"  type="button" id="loginbutton" value = "Login" onclick="login()">
							
							
						</fieldset>
					</form>
				</div>
			</div>
			
			
			
			
			
			
		</div>
		
		<div class="row footer" >
			<div class="small-12 large-6 large-limit small-centered columns ">
				&copy 2013 Dragon Sparkles, Inc. All rights reserved.
			</div>
		</div>
		
		
		
		
		
		
		
		<!--		------------------------------->
		
		<script type="text/javascript">
			
			function login()
			{
				//generate unencoded authentication header
				var authString = document.getElementById("loginusername").value + ":" + document.getElementById("loginpassword").value;
				//Base64 encode the header
				authString = window.btoa(authString);
				//add the word Basic plus a space
				authString = 'Basic ' + authString;
				
				
				//----------------------------------------------------------
				//create new XHR
				var xmlProject = new XMLHttpRequest();
				//define behavior for when the response is recieved
				xmlProject.onreadystatechange = function()
					{
						if(xmlProject.readyState == 4)//wait until response is available
						{
							if(xmlProject.statusText == "OK"){
								//document.getElementById("projectresponsespan").innerHTML = xmlProject.statusText;
								window.location = "view2.jsp";
							}
							else{
								document.getElementById("badproject").innerHTML = "Project does not exist";
								document.getElementById("badproject").setAttribute("class", "warning");
								document.getElementById("loginproject").setAttribute("class", "warningField");
							}
						}
						
					}
					;
				//----------------------------------------------------------
				
				//create new XHR
				var xml = new XMLHttpRequest();
				
				//define behavior for when the response is recieved
				xml.onreadystatechange = function()
					{
						if(xml.readyState == 4)//wait until response is available
						{
							if(xml.statusText == "OK"){
								//document.getElementById("loginresponsespan").innerHTML = xml.statusText;
								//setup reuqest to POST to /API/Login
								xmlProject.open('PUT','API/login',false);
								//send the request
								xmlProject.send(document.getElementById("loginproject").value);
							}
							else{
								
								document.getElementById("badlogin").innerHTML = "Wrong user name or password";
								document.getElementById("badlogin").setAttribute("class", "warning");
								document.getElementById("loginpassword").setAttribute("class", "warningField");
								document.getElementById("loginusername").setAttribute("class", "warningField");
							}
						}
					}
					;
				
				//setup reuqest to POST to /API/Login
				xml.open('POST','API/login',false);
				//set the request header
				xml.setRequestHeader('Authorization', authString);
				//send the request
				xml.send();
				
			}
			
			$(function() {
				$( "#login-dialog" ).dialog({
					width: "30%", draggable: false}
										   ).css("fontSize", "12px");
				$("button").css("display", "none");
				$( "#loginbutton" ).button().width("70px");
			}
			 );
		</script>
	</body>
</html>