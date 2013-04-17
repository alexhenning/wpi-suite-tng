<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Requirements viewer</title>
<link rel="stylesheet" href="css/blue/style.css" type="text/css">
<link rel="stylesheet" href="css/smoothness/jquery-ui-1.10.2.custom.css" type="text/css">
<style type="text/css">
  .reqFields {
    margin: 5px;
    }
   .reqNote {
    margin: 5px;
    padding: 3px;
    border-width: 1px;
    border-color: #e6EEEE;
    border-style: solid;
    }
</style>    
<script type="text/javascript" src="libs/jquery-1.9.1.min.js"></script> 
<script type="text/javascript" src="libs/jquery.tablesorter.js"></script>
<script type="text/javascript" src="libs/jquery-ui-1.10.2.custom.js"></script>
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
			reqList = eval(xmlReqList.responseText);
			for(var i = 0; i<reqList.length; i++){
				document.getElementById("reqTBody").innerHTML += 
				("<tr id = 'req" + i + "' onclick = 'createReqDialog("+ i +");'>"+
				    "<td>"+reqList[i].id+"</td>"+
				    "<td>"+reqList[i].name+"</td>"+
				    "<td>"+reqList[i].description+"</td>"+
				    "<td>"+reqList[i].iteration.iterationNumber+"</td>"+
				   	"<td>"+reqList[i].status+"</td>"+
				    "<td>"+reqList[i].priority+"</td>"+
				    "<td>"+reqList[i].estimate+"</td>"+
				"</tr>");
			}
			$("#reqTable").tablesorter();
			document.getElementById("divBuffer").innerHTML = xmlReqList.responseText;
		}else{
			window.location = "login.jsp";
		}
	}
	
};

function createReqDialog(reqId){
	if($("#Dialog" + reqId).length == 0){
		var dialogHTML = 
		"<div id='Dialog" + reqId + "' title='Requirement - " + reqList[reqId].name + "'>" +
		  "<p>"+
		  "<div class = 'reqFields'>ID: " + reqList[reqId].id + "</div>"+
		  "<div class = 'reqFields'>Name: " + reqList[reqId].name + "</div>"+
		  "<div class = 'reqFields'>Description:<br>" + reqList[reqId].description + "</div>"+
		  "<div class = 'reqFields'>Iteration: " + reqList[reqId].iteration.iterationNumber + "</div>"+
		  "<div class = 'reqFields'>Status: " + reqList[reqId].status + "</div>"+
		  "<div class = 'reqFields'>Priority: " + reqList[reqId].priority + "</div>"+
		  "<div class = 'reqFields'>Estimate: " + reqList[reqId].estimate + "</div>"+
		  "<div class = 'reqFields'>Actual effort: " + reqList[reqId].actualEffort + "</div>"+
		  "<div class = 'reqFields'>Date of creation: " + reqList[reqId].creationDate + "</div>"+
		  "<div class = 'reqFields'>Date of last modification: " + reqList[reqId].lastModifiedDate + "</div>"+
		  "<div class = 'reqFields'>Creator name: " + reqList[reqId].creator.name + "</div>" +
		  "<div class = 'reqFields'>Notes: </div>";
		  
		  for(var j = 0; j<reqList[reqId].events.length; j++){
			  dialogHTML = dialogHTML + 
			  	"<div class = 'reqNote'>"+
			  	"<div>" + reqList[reqId].events[j].user.name + " - " + reqList[reqId].events[j].date + "</div>" +
			  	"<div>" + reqList[reqId].events[j].body + "</div>" +
			  	"</div>";
		  }
		  dialogHTML = dialogHTML +
		  "</p>" +
		"</div>";
		$("body").append(dialogHTML);
		
		$("#Dialog" + reqId).dialog({ width: "50%", draggable: false}).css("fontSize", "12px");
	}else{
		$("#Dialog" + reqId).dialog( "open" );
	}
}


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
<table id="reqTable" class="tablesorter"> 
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
<div id = "reqDialogs"></div>
</body>
</html>