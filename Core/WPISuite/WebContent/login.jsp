<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
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
						document.getElementById("loginresponsespan").innerHTML = "";
						document.getElementById("projectresponsespan").innerHTML = "Project is not exist";
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
				document.getElementById("projectresponsespan").innerHTML = "";
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
</script>
</head>
<body>
<h1>WPI Suite Requirements viewer</h1>
<h4>Login:</h4>
Username:<input type="text" id="loginusername"></input><span id="loginresponsespan"></span><br>
Password:<input type="password" id="loginpassword"></input><br>
Project:<input type="text" id="loginproject"></input><span id="projectresponsespan"></span><br>
<input type="button" value="Submit" onclick="login()">
</body>
</html>