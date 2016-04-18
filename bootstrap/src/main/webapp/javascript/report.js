var rootPath = "/bootstrap/";
// 统计项
var itemNames = new Array("pass", "fail", "skip");

/**
 * 初始化矩阵数组
 * 
 * @param m
 * @param n
 * @returns {Array}
 */
function initMatrix(m, n) {
	var tArray = new Array(); // 先声明一维
	for (var k = 0; k < m; k++) {
		tArray[k] = new Array(); // 声明二维
		for (var j = 0; j < n; j++) {
			tArray[k][j] = 0; // 这里将变量初始化
		}
	}
	return tArray;

}
/**
 * 矩阵转置
 * 
 * @param matrix
 * @param transposeMatrix
 * @returns
 */
function transposeMatrix(matrixArray) {

	var m = matrixArray.length;
	var n = matrixArray[0].length;

	var transposeMatrixArray = initMatrix(n, m);
	for (var row = 0; row < n; row++) {
		for (var col = 0; col < m; col++) {
			// 字符串转数字
			transposeMatrixArray[row][col] = parseInt(matrixArray[col][row]);

		}

	}
	return transposeMatrixArray;

}

$(function() {

	var currentDate=getCurrentDate();
	$('#test-data').val(currentDate);
	loadReport(currentDate);
	$('#test-data').datetimepicker().on('changeDate', function(ev) {
		$('#test-data').datetimepicker('hide');
		var date = $('#test-data').val();

		loadReport(date);

	});

});

function getCurrentDate() {
	var myDate = new Date();

	var year = myDate.getFullYear(); // 获取完整的年份(4位,1970-????)
	var month = myDate.getMonth() + 1; // 获取当前月份(0-11,0代表1月)
	var day = myDate.getDate();
	return year + "-" + month + "-" + day;
}

function loadReport(date) {
	alert(date);
	
	
	

	
	
	$.ajax({
		type : "POST",
		url : "../json/report.json",
		async : false,
		cache : false,
		// data : $("form").serialize(),
		dataType : "json",
		success : function(jsonData) {
			var reportJson = jsonData;
			// 创建图标
			createChart(reportJson);
			// 创建表格
			createTable(reportJson);

		},
		error : function(XMLHttpRequest) {
			alert("error");
		}

	});
}

function receive(data) {  
    alert(" dddd");  
}  

/**
 * 获取图表数据结构
 * 
 * @param reportJson
 * @returns {Array}
 */
function getSeriesArray(reportJson) {
	var seriesArray = new Array();

	var matrixArray = new Array();

	for (var i = 0; i < reportJson.length; i++) {
		var dataArray = new Array();
		var itemJson = reportJson[i];
		for (var j = 0; j < itemNames.length; j++) {
			var itemName = itemNames[j];
			dataArray[j] = itemJson[itemName];
		}
		matrixArray[i] = dataArray;
	}

	// 矩阵转置
	var transposeMatrixArray = transposeMatrix(matrixArray);

	var seriesArray = new Array();
	for (var i = 0; i < itemNames.length; i++) {
		var dataArray = transposeMatrixArray[i];

		var item = {
			name : itemNames[i],
			data : dataArray
		}
		seriesArray[i] = item;
	}

	return seriesArray;

}

function createChart(reportJson) {
	// 项目名称集合
	var categories = new Array();
	for (var i = 0; i < reportJson.length; i++) {
		categories[i] = reportJson[i].pjName
	}

	// 生成图表数据结构
	var seriesArray = getSeriesArray(reportJson);
	$('#container')
			.highcharts(
					{
						chart : {
							type : 'column'
						},
						title : {
							text : '<b>Project Report</b>'
						},
						subtitle : {
							text : ''
						},
						xAxis : {
							categories : categories,
							crosshair : true
						},
						yAxis : {
							min : 0,
							title : {
								text : ' (次)'
							}
						},
						tooltip : {
							headerFormat : '<span style="font-size:10px">{point.key}</span><table>',
							pointFormat : '<tr><td style="color:{series.color};padding:0">{series.name}: </td>'
									+ '<td style="padding:0"><b>{point.y:.0f} </b></td></tr>',
							footerFormat : '</table>',
							shared : true,
							useHTML : true
						},
						plotOptions : {
							column : {
								pointPadding : 0.2,
								borderWidth : 0
							}
						},
						series : seriesArray
					});

}

function createTable(reportJson) {
	var content = "<table class='table table-hover'>";
	content += "<thead>";
	content += "<th> PROJECT NAME</th>";
	content += "<th>PASS</th>";
	content += "<th>FAIL</th>";
	content += "<th>SKIP</th>";
	content += "</thead>";
	for (var i = 0; i < reportJson.length; i++) {
		content += "<tr>"
		content += "<td>" + reportJson[i].pjName + "</td>";
		content += "<td>" + reportJson[i].pass + "</td>";
		content += "<td>" + reportJson[i].fail + "</td>";
		content += "<td>" + reportJson[i].skip + "</td>";
		content += "<td><a href='#'>link</a></td>";
		content += "</tr>"
	}
	content += "</table>";

	$("#project-info").html(content);

}
