<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="javax.naming.Context"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="org.jens.jbossstatus.JmxReader"%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Graph</title>
</head>
<body>
<h1>History</h1>
<% 
	int scale = 60;
	if (request.getParameterMap().containsKey("scale")) {
		scale = Integer.parseInt(request.getParameter("scale"));
	}
	String[] selected = new String[]{"","","","","","" };
	int[] times = new int[] { 70,560,1680,11760,50400,613200};
	String[] header = new String[] { "1 Hour","8 Hours","1 Day","1 Week","1 Month","1 Year" };
	for(int i = 0; i< times.length; i++) {
		
	}
%>

<form method="post" name="scaleMode">
	<select name="scale" onchange="document.scaleMode.submit();">
	<%
		for(int i = 0; i< times.length; i++) {
			out.print("<option ");
			if (times[i] == scale) {
				out.print("selected ");
			}
			out.println("value=\"" +times[i] + "\">" + header[i] + "</option>" );
		}
	%>
	</select>
	<!--  <input type="submit" />  -->
	<!--  <input type="test" name="scale" value="<%=scale %>" /> <input  type="submit" /> -->
</form>
<table border="1">
<%
Context ctx = new InitialContext();
JmxReader reader = new JmxReader(ctx);
for(String it : reader.listJdbc()) {
	out.println("<tr>");
	out.println("<td><a href='detail.jsp?db="+it+"'><img src='rrdbar?db="+it+"&scale="+scale+"' border='0'/></a></td>");
	out.println("</tr>");
}
for(String it : reader.listWebServices()) {
	out.println("<tr>");
	out.println("<td><img src='rrdbar?db="+it+"_FAULT&scale="+scale+"' /></td>");
	out.println("<td><img src='rrdbar?db="+it+"_COUNTER&scale="+scale+"' /></td>");
	out.println("</tr>");
}
%>
</table>
</body>
</html>