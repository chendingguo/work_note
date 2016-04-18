<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="header.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet"
	href="${appPath}/bootstrap-3.3.5/css/bootstrap.min.css">
<link rel="stylesheet"
	href="${appPath}/bootstrap-3.3.5/css/bootstrap-theme.min.css">
<script src="${appPath}/jquery/jquery-1.11.1.min.js"
	type="text/javascript"></script>
<script src="${appPath}/jquery/jquery-form.js"></script>
<script src="${appPath}/bootstrap-3.3.5/js/bootstrap.min.js"></script>
<script src="${appPath}/javascript/testhttp.js"></script>
<script type="text/javascript">
	var appPath = "${appPath}";
</script>

</head>

<body style="margin: 2cm;">
	<h2>
		<b><center>接口测试</b>
		</center>
	</h2>
	<!-- Nav tabs -->
	<ul class="nav nav-tabs" id="my-tabs">
		<li><a href="#home" data-toggle="tab">Home</a></li>
		<li><a href="#uploadFile" data-toggle="tab">Upload</a></li>
		<li><a href="#browse" data-toggle="tab"
			onclick="browseComponent()">Browse</a></li>
		<li><a href="#settings" data-toggle="tab">Settings</a></li>
	</ul>

	<!-- Tab panes -->
	<div class="tab-content">
		<div class="tab-pane active" id="home">
			<br> <br>
			<form id="upload-form" action="" enctype="multipart/form-data"
				method="post">
				<div class="form-group">
					<label for="exampleInputFile">测试环境url</label> <input type="text"
						name="url1" id="url-1" size="100" /><br> <label
						for="exampleInputFile">正式环境url</label> <input type="text"
						name="url2" id="url-2" size="100" />

				</div>
				<input type="button" id="submit-btn" class="btn btn-default"
					onclick="showResult()" value="submit">

			</form>
		</div>
		<!-- ------------上传文件 ---------------------------------->
		<div class="tab-pane" id="uploadFile" style="margin-left: 30"></div>
		<!-- --------------------------浏览插件信息 ---------------------------------->
		<div class="tab-pane" id="browse" style="margin-left: 30">
			<div id="list-component"></div>
			<div id="show-component-info"></div>
		</div>



		<div class="tab-pane" id="settings">...</div>
	</div>


	<div id="result-1" align="left">1</div>
	<div id="result-2" align="right">2</div>

	<!-- 模态框（Modal） -->
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">模态框（Modal）标题</h4>
				</div>
				<div class="modal-body"></div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭
					</button>
					<button type="button" class="btn btn-primary">提交更改</button>
				</div>
			</div>
		</div>
	</div>
</body>

</html>