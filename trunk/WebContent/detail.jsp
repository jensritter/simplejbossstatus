<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="javax.naming.Context"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="org.jens.jbossstatus.JmxReader"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
	String db = request.getParameter("db");
	if ("".equals(db)) {
		response.sendRedirect("graph.jsp");
	}
	String[] times = new String[] { "70","560","1680","11760","50400","613200"};
	String[] header = new String[] { "1 Hour","8 Hours","1 Day","1 Week","1 Month","1 Year" };
	
%>
<title>Detail <%=db %></title>
</head>
<body>
<h1><a href='graph.jsp'>Status <%=db %></a></h1>
<table border='1'>
<%
	Context ctx = new InitialContext();
	JmxReader reader = new JmxReader(ctx);
	out.println("<tr>");
	for(int i = 0; i< times.length; i++) {
		out.println("<td>Kind of " + header[i] + "<br />");
		out.println("<img src='rrdbar?db="+db+"&scale="+times[i]+"' border='0'/>");
		out.println("</td>");
		if ((i+1) % 2 == 0) {
			out.println("</tr><tr>");
		}
	}
	out.println("</tr>");
%>
</table>
<br />
<a href="graph.jsp">Back</a>
</body>
</html>