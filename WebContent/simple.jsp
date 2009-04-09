<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.jens.jbossstatus.*"%>
<%@page import="javax.naming.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Insert title here</title>
</head>
<body>
<%
Context ctx = new InitialContext();
JmxReader reader = new JmxReader(ctx);
JBossSysinfo sysinfo = reader.getSysInfo();
NumberFormat nf = NumberFormat.getIntegerInstance(Locale.GERMAN);
%>
<h1>JBossSysStatus</h1>
<div style="border: 1px; style: solid;">
<table border="1">
<tr><td>
	<%=sysinfo.getHostName()%> (<%=sysinfo.getHostAddress() %>) <br />
	<%=sysinfo.getOSName() %> - <%=sysinfo.getOSVersion() %> - (<%=sysinfo.getOSArch() %>) <br />
	CPU(s):<%=sysinfo.getAvailableProcessors() %>
	</td>
	<td>
	<%=sysinfo.getJavaVendor() %> - <%=sysinfo.getJavaVersion() %> <br />
	<%=sysinfo.getJavaVMVendor() %> - <%=sysinfo.getJavaVMName() %>
	</td>
</tr>
<tr>
	<td>
	Max:<%=nf.format(sysinfo.getMaxMemory()  / 1024 / 1024 ) %> - Used: <%=nf.format(sysinfo.getTotalMemory() / 1024 / 1024) %> - Free:<%=nf.format(sysinfo.getFreeMemory() / 1024 / 1024 ) %><br />
	Max:<%=nf.format(sysinfo.getMaxMemory() ) %> - Used: <%=nf.format(sysinfo.getTotalMemory()) %> - Free:<%=nf.format(sysinfo.getFreeMemory() ) %><br />
	</td>
	<td>
	ThreadCount :<%=sysinfo.getActiveThreadCount() %> <br />
	ThreadGroupCount: <%=sysinfo.getActiveThreadGroupCount() %>
	</td>
</tr>
<tr>
<td>
<%=reader.getMemoryUsageAsString(true) %>
</td>
</tr>
</table>
</div>
</body>
</html>