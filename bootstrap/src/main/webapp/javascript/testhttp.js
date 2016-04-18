function showResult() {
	
	var url1=$("#url-1").val();
	var url2=$("#url-2").val();
	$("#result-1").html(url1);
	$("#result-2").html(url2);
//	$.ajax({
//		type : "POST",
//		url : testUrl,
//		cache : false,
//		// data : $("form").serialize(),
//		dataType : "json",
//		success : function(retObj) {
//			var content = "";
//			
//
//			alert(retObj);
//
//		},
//		error : function(XMLHttpRequest) {
//			alert("error");
//		}
//
//	});

}