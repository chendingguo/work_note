
$(document).ready(function() {
    var json = [{"col1":"row1", "col2":"row1", "col3":"row1"}, {"col1":"row2", "col2":"row2", "col3":"row2"}]; 
    $('#test-result').columns({data:json});
  });

function showRightDiv(serviceName){
	var htmlStr="";
	var parameterArray=new Array();
	parameterArray[0]="param0";
	parameterArray[1]="param1";
	parameterArray[2]="param2";
	var content="<form id='test-form'>";
	for(var i=0;i<parameterArray.length;i++){
		var serviceName=parameterArray[i];
		var inputStr=serviceName+": <input name='"+parameterArray[i]+"'/><br>";
		content+=inputStr;
	}
	content+="</form>"
	$("#parameters-input-list").html(content)
}

function sendPost(serviceName){
	var parameters=$("#test-form").serialize();
	alert(parameters);

	var requestUrl="";
	
//	$.ajax({
//		type : "POST",
//		url :requestUrl,
//		cache : false,
//		 data : $("form").serialize(),
//		dataType : "json",
//		success : function(retObj) {
//			var content = "";
//			for (var i = 0; i < retObj.length; i++) {
//				var name = retObj[i];
//				content += "<li onclick=showComponentInfo('" + name + "')>"
//						+ name + "</li>";
//			}
//
//			$("#list-component").html(content);
//
//		},
//		error : function(XMLHttpRequest) {
//			alert("error");
//		}
//
//	});
}

