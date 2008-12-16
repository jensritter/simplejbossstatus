<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="javax.naming.Context"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="org.jens.jbossstatus.JmxReader"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Graph</title>
</head>
<body>
<table border="1">
<%
final int scale =60;
Context ctx = new InitialContext();
JmxReader reader = new JmxReader(ctx);
for(String it : reader.listJdbc()) {
	out.println("<tr>");
	out.println("<td><img src='Collector?db="+it+"&scale="+scale+"' /></td>");
	out.println("</tr>");
}
for(String it : reader.listWebServices()) {
	out.println("<tr>");
	out.println("<td><img src='Collector?db="+it+"F&scale="+scale+"' /></td>");
	out.println("<td><img src='Collector?db="+it+"C&scale="+scale+"' /></td>");
	out.println("</tr>");
}
%>
</table>
</body>
</html>