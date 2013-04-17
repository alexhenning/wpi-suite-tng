<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Requirements viewer login</title>
<link rel="stylesheet" href="css/smoothness/jquery-ui-1.10.2.custom.css" type="text/css">
<style type="text/css">
	lable{
    font-size: 10px;
    display:block;
    }
    input{
    width: 200px;
    display:block;
    }
</style>
<script type="text/javascript" src="libs/jquery-1.9.1.min.js"></script>
 <script type="text/javascript" src="libs/jquery-ui-1.10.2.custom.js"></script>
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
						window.location = "view.jsp";
					}else{
						document.getElementById("loginresponsespan").innerHTML = "Project is not exist";
					}
				}
				
			};
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
			}else{	
				document.getElementById("loginresponsespan").innerHTML = "Wrong user name or password";
			}
		}
	};
	
	//setup reuqest to POST to /API/Login
	xml.open('POST','API/login',false);
	//set the request header
	xml.setRequestHeader('Authorization', authString);
	//send the request
	xml.send();             
}

$(function() {
    $( "#login-dialog" ).dialog({ width: "30%", draggable: false}).css("fontSize", "12px");
    $("button").css("display", "none");
    $( "#loginbutton" ).button().width("70px");
});
</script>
</head>
<body>
<div id="login-dialog" title="WPI Suite Requirements viewer">
	<h3>Login:</h3>
	<form>
	<fieldset>
	<h4 id="loginresponsespan">All form fields are required.</h4><br>
	<lable for="loginusername">Username:</lable>
	<input type="text" id="loginusername" name = "loginusername"></input>
	<lable for = "loginpassword">Password:</lable>
	<input type="password" id="loginpassword" name="loginpassword"></input>
	<lable for = "loginproject">Project:</lable>
	<input type="text" id="loginproject" name = "loginproject"></input>
	<input type="button" id="loginbutton" value = "Login" onclick="login()"></input>
	</fieldset>
	</form>
</div>
</body>
</html>