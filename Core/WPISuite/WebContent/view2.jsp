
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>
			Requirements viewer
		</title>
		
		<script>
			var __adobewebfontsappname__ = "code"</script>
			<script src="http://use.edgefonts.net/quicksand:i4,n3,i7,n7,n4,i3:all.js">
		</script>
		
		<script>var __adobewebfontsappname__ = "code"</script>
		<script src="http://use.edgefonts.net/lobster:n4:all;quicksand:i4,n3,i7,n7,n4,i3:all.js">
		</script>
		<link href="css/normalize.css" rel="stylesheet">
		<link href="css/foundation.css" rel="stylesheet">
		
		<link href="css/deniz.css" rel="stylesheet" >
		
		
<script type="text/javascript" src="libs/jquery-1.9.1.min.js"></script>
<script type="text/javascript">
//create new XHR
var xmlReqList = new XMLHttpRequest();
//define behavior for when the response is recieved
xmlReqList.onreadystatechange = function()
{
	if(xmlReqList.readyState == 4)//wait until response is available
	{
		if(xmlReqList.statusText=="OK"){
			// For debug
			// document.getElementById("divBuffer").innerHTML = xmlReqList.responseText;
			reqList = eval(xmlReqList.responseText);
			displayReqs();
		}else{
			window.location = "login2.jsp";
		}
	}
	
};

function displayReqs(){
	for(var i = 0; i<reqList.length; i++){
		var iterationName;
		if(reqList[i].iteration != null)
			iterationName = reqList[i].iteration.iterationNumber;
		else
			iterationName = "NONE";
		$("#reqContainer").html($("#reqContainer").html() + 
		"<div class='row' style='margin-top:10px; '>"+
		"<div class='small-12 large-10 small-centered columns task center '  onclick='ChangeStyle("+ i +");'>"+	
		"<div class='row  ' id='taskHead"+ i +"' >"+
			"<div class='small-1   columns section gray'>"+
				reqList[i].id+
			"</div>"+
			"<div class='small-7  columns section title'>"+
				reqList[i].name+
			"</div>"+
			"<div class='small-2   columns section'>"+
				iterationName+
			"</div>"+
			"<div class='small-1   columns section gray'>"+
				reqList[i].status+
			"</div>"+
			"<div class='small-1  columns section priorityM'>"+
				reqList[i].priority+
			"</div>"+
		"</div>"+
		"<div class='row collapse' id='details"+ i +"'>"+
			"<div class=' small-1   columns section '>"+
			"</div>"+
			"<div class='small-7   columns section description'>"+
				reqList[i].description+
			"</div>"+
			"<div class=' small-4   columns section '>"+
			"</div>"+
		"</div>"+
		"</div>"+
		"</div>");
		
		/*
		    reqList[i].id
		    reqList[i].name
		    reqList[i].description
		    iterationName
		   	reqList[i].status
		    reqList[i].priority
		    reqList[i].estimate
		    reqList[i].actualEffort
		    reqList[i].creationDate
		    reqList[i].lastModifiedDate
		    reqList[i].creator.name
		*/
		
	}
}

function ChangeStyle(ind){
	
	var taskHead = $("#taskHead"+ind);
	
	if(taskHead.hasClass("shine"))
		taskHead.removeClass("shine");
	else
		taskHead.addClass("shine");
	
	
	var details = $("#details"+ind);
	if(details.hasClass("extend"))
		details.removeClass("extend");
	else
		details.addClass("extend");
}

function sortByName(){
	$("#reqContainer").html("");
	reqList.sort(function(a, b){
		return (a.name>b.name);
	});
	displayReqs();
}

function sortById(){
	$("#reqContainer").html("");
	reqList.sort(function(a, b){
		return (a.id>b.id);
	});
	displayReqs();
}
function sortByIteration(){
	$("#reqContainer").html("");
	reqList.sort(function(a, b){
		
		if(a.iteration == null || b.iteration == null){
			if(a.iteration == null && b.iteration == null)
				return (a.id > b.id);
			if(a.iteration == null)
				return true;
			else
				return false;
		}else{
			if(a.iteration.iterationNumber == b.iteration.iterationNumber)
				return (a.id > b.id);
			else
				return (a.iteration.iterationNumber > b.iteration.iterationNumber);
		}
	});
	displayReqs();
}

function sortByStatus(){
	$("#reqContainer").html("");
	reqList.sort(function(a, b){
		return (a.status>b.status);
	});
	displayReqs();
}

function sortByPriority(){
	$("#reqContainer").html("");
	reqList.sort(function(a, b){
		if(a.priority != b.priority){
			if(a.priority == "LOW")
				return true;
			if(a.priority == "MEDIUM" && b.priority != "LOW")
				return true;
			if(a.priority == "HIGH")
				return false;
		}else{
			return (a.id > b.id);
		}
	});
	displayReqs();
}

function init(){
	//setup reuqest to POST to API/requirementsmanagement/requirementmodel
	xmlReqList.open('GET','API/requirementsmanagement/requirementmodel/',false);
	//send the request
	xmlReqList.send();
}
</script>
		
	</head>
	
	<body onload="init()">
		
		<img src="img/blurred2.jpg" class="center .antialiased" id="photto" >
		
		<div class="row" style="margin-top:10px;">
			<div class="small-12 large-5 large-limit small-centered columns "     >
				
				<p id="smallerTitle"  >
					Requirements Management
				</p>
			</div>
			<div class="small-12 large-3 large-limit small-centered columns "   id="smallTitle"  >
				
				<p  >
					by Dragon Sparkles, Inc.
				</p>
			</div>
		</div>
		
		<div class="row" style="margin-top:50px;">
			<div class="small-12 large-6 large-limit small-centered columns "    >
			</div>
		</div>
		
		
		<div class="row" style="margin-top:10px;">
			<div class="small-12 large-10 small-centered columns  center "     >
				
				<div class="row" >
					<div class="small-1   columns  sectionHeader " onclick="sortById();">
						ID
					</div>
					<div class="small-7  columns  sectionHeader " onclick="sortByName();">
						TITLE
					</div>
					<div class="small-2   columns  sectionHeader" onclick="sortByIteration();">
						ITERATION
					</div>
					<div class="small-1   columns  sectionHeader" onclick="sortByStatus();">
						STATUS
					</div>
					<div class="small-1  columns  sectionHeader" onclick="sortByPriority();">
						PRIORITY
					</div>
				</div>
			</div>
		</div>
		
		<div id = "reqContainer"></div>
	</body>
</html>