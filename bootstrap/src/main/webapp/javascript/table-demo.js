var columnsData = [ {
	field : 'orderno',
	title : '订单号'
}, {
	sortable : true,
	field : 'serviceName',
	title : '服务名称'
}, {
	field : 'uid',
	title : '提交人'
}, {
	field : 'params',
	title : '订单参数'
}, {
	field : 'status',
	title : '订单状态'
} ];

$(document).ready(function() {

	$('#create-date-from').datetimepicker({
		inline : true,
		sideBySide : true,
		minView : "month",
		format : 'yyyy-mm-dd'

	});

	$('#create-date-to').datetimepicker({
		inline : true,
		sideBySide : true,
		minView : "month",
		format : 'yyyy-mm-dd'

	});
	loadTableData();

});

/**
 * 加载订单数据
 */

function loadTableData() {
	var searchParams = "{}";
	$('#table').bootstrapTable({
		url : '/bootstrap/json/table-demo.json',
		searchParams : searchParams,
		sidePagination : "server",
		pagination : true,
		// search:true,
		columns : columnsData,
		onClickRow:function getOrderDetailInfo(row,$element){
			showModal();
		}
	});

}



/**
 * 查询操作
 */
function searchOrder() {

	var userName = $("#username").val();
	var orderNo = $("#orderno").val();
	var status = $("#order-status").val();
	var createDateFrom = $("#create-date-from").val();
	var createDateTo = $("#create-date-to").val();
	var searchParams = {
		userName : userName,
		orderNo : orderNo,
		status : status,
		createDateFrom : createDateFrom,
		createDateTo : createDateTo

	};
	$("#console-div").html(JSON.stringify(searchParams));
	loadTableData(searchParams);

}


function showModal(){
	var options={
			keyboard:true	
	};
	$('#myModal').modal(options);
}

