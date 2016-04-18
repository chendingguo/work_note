<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String appPath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ request.getContextPath();
  // out.print(appPath);
   request.setAttribute("appPath",appPath);
%>
<script type="text/javascropt">
  var appPath="${appPath}"
</script>