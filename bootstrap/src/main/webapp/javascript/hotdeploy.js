/**
 * 上传组件xml文件
 */
$(document).ready(function() {
	// $('#my-tabs a').click(function (e) {
	// alert('hi');
	// if()
	// })
});
var rootPath = "/bootstrap/hotdeploy/";

function uploadComponent() {
	var uploadAction = rootPath + "/uploadComponentFile.do";
	var options = {
		dataType : "text",
		type : 'post',
		url : uploadAction,
		beforeSubmit : showRequest,
		enctype : 'multipart/form-data',
		success : function(data) { // 提交成功的回调函数
			alert(data);
		},
		clearForm : true,
	};

	$("#upload-form").ajaxSubmit(options);

}

function browseComponent() {
	$.ajax({
		type : "POST",
		url : rootPath + '/listComponentNames.do',
		cache : false,
		// data : $("form").serialize(),
		dataType : "json",
		success : function(retObj) {
			var content = "";
			for (var i = 0; i < retObj.length; i++) {
				var name = retObj[i];
				content += "<li onclick=showComponentInfo('" + name + "')>"
						+ name + "</li>";
			}

			$("#list-component").html(content);

		},
		error : function(XMLHttpRequest) {
			alert("error");
		}

	});

}

function showComponentInfo(name) {
	alert(name);
	$.ajax({
		type : "POST",
		url : rootPath + '/showComponentInfo.do',
		cache : false,
		data : {
			componentName : name
		},
		dataType : "json",
		success : function(retObj) {
			$("#show-component-info").html(retObj);
		},
		error : function(XMLHttpRequest) {
			alert("error");
		}

	});

}
/**
 * 提交前
 * 
 * @returns {Boolean}
 */
function showRequest() {
	return true;
}


function showModal(){
	var options={
			keyboard:true	
	};
	$('#myModal').modal(options);
}
