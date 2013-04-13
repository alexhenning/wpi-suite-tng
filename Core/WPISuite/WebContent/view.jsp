<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript">
//create new XHR
var xmlReqList = new XMLHttpRequest();
//define behavior for when the response is recieved
xmlReqList.onreadystatechange = function()
{
	if(xmlReqList.readyState == 4)//wait until response is available
	{
		//document.getElementById("divBuffer").innerHTML = xmlReqList.statusText;
		if(xmlReqList.statusText=="OK"){
			var reqList = eval(xmlReqList.responseText);
			for(var i = 0; i<reqList.length; i++){
				document.getElementById("reqTBody").innerHTML += 
				("<tr>"+
				    "<td>"+reqList[i].id+"</td>"+
				    "<td>"+reqList[i].name+"</td>"+
				    "<td>"+reqList[i].description+"</td>"+
				    "<td>"+reqList[i].iteration.iterationNumber+"</td>"+
				   	"<td>"+reqList[i].status+"</td>"+
				    "<td>"+reqList[i].priority+"</td>"+
				    "<td>"+reqList[i].estimate+"</td>"+
				"</tr>");
			}
		}else{
			window.location = "login.jsp";
		}
	}
	
};


function init(){
	//setup reuqest to POST to API/requirementsmanagement/requirementmodel
	xmlReqList.open('GET','API/requirementsmanagement/requirementmodel/',false);
	//send the request
	xmlReqList.send();
}
</script>
</head>
<body onload = "init();">
<h1 id = "mainTitle">Project requirements</h1>
<table id="myTable" class="tablesorter" border="1"> 
<thead> 
<tr> 
    <th>id</th> 
    <th>Name</th> 
    <th>Description</th> 
    <th>Iteration</th> 
    <th>Status</th>
    <th>Priority</th>
    <th>Estimate</th>
</tr> 
</thead> 
<tbody id = "reqTBody">
</tbody> 
</table>
<div id = "divBuffer"></div>
</body>
</html>