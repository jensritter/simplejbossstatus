<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="org.jens.jbossstatus.*" %>
<%@ page import="javax.naming.Context" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="org.jens.jbossstatus.JmxReader"%>
<%@page import="javax.naming.InitialContext"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JBoss Status</title>
<LINK href="style.css" title="compact" rel="stylesheet" type="text/css">
</head>
<body>
<h3>JDBC</h3>
<%

Context ctx = new InitialContext();
JmxReader reader = new JmxReader(ctx);
String[] lstjdbc = reader.listJdbc();
String[] lstEjb = reader.listEjb();
String[] lstWs = reader.listWebServices();

String jdbcDetails = request.getParameter("jdbcDetails");
if (jdbcDetails == null) {
	jdbcDetails = "";
}
String notJdbcDetails = "1";
if (jdbcDetails.equals("1")) {
	notJdbcDetails="";
}

String ejbDetails = request.getParameter("ejbDetails");
if (ejbDetails == null) {
	ejbDetails = "";
}
String notEjbDetails="1";
if (ejbDetails.equals("1")) {
	notEjbDetails="";
}

String wsDetails = request.getParameter("wsDetails");
if (wsDetails==null) {
	wsDetails="";
}
String notWsDetails="1";
if (wsDetails.equals("1")) {
	notWsDetails="";
}
%>

<table border='1'>
	<tr>
		<td>Name
			<form>
				<input type="hidden" name="jdbcDetails" value="<%=notJdbcDetails%>">
				<input type="submit" value='Details' />
			</form>
		</td>
		<% if (jdbcDetails.equals("1")) { %>
			<td class='header-small'>MaxSize</td>
			<td class='header-small'>MinSize</td>
			<td class='header-small'>IdleTimeoutMinutes</td>
	
			<td class='header-small'>ConnectionCount</td>
			<td class='header-small'>AvailableConnectionCount</td>
			<td class='header-small'>InUseConnectionCount</td>
			 
		<% } %>
		<td class='header-small'>MaxConnectionsInUse</td>
		<td>Graph</td>
	</tr>
	
	<% for (String it : lstjdbc) { %>
	<tr>
		<td><%=it %></td>
		<% if(jdbcDetails.equals("1")) { 
			out.println("<td id='jdbc-detail'>" + reader.getJdbcMaxSize(it) + "</td>");
			out.println("<td id='jdbc-detail'>" + reader.getJdbcMinSize(it) + "</td>");
			out.println("<td id='jdbc-detail'>" + reader.getJdbcIdleTimeoutMinutes(it) + "</td>");
			
			out.println("<td id='jdbc-detail'>" + reader.getJdbcConnectionCount(it) + "</td>");
			out.println("<td id='jdbc-detail'>" + reader.getJdbcAvailableConnectionCount(it) + "</td>");

			out.println("<td id='jdbc-detail'>" + reader.getJdbcInUseConnectionCount(it) + "</td>");
		}
		out.println("<td id='jdbc-detail'>" + reader.getJdbcMaxConnectionsInUse(it) + "</td>");
		%>
		<td><img src="JdbcUsageGraph?jdbc=<%=it %>" /></td>
	</tr>
	<% } %>
</table>
	
<h3>EJB</h3>
<table border="1">
	<tr>
		<td>Name
			<form>
				<input type="hidden" name="ejbDetails" value="<%=notEjbDetails%>">
				<input type="submit" value='Details' />
			</form>
		</td>
		<% if (ejbDetails.equals("1")) { %>
			
			<td class='header-small'>CurrentSize</td>
			<td class='header-small'>RemoveCount</td>
	
			<td class='header-small'>MaxSize</td>
			<td class='header-small'>AvailableCount</td>
		<% } %>
		<td class='header-small'>CreateCount</td>
		<td>Graph</td>
	</tr>
	<% for(String it : lstEjb) { %>
		<tr>
		<td><%=it %></td>
		<% if(ejbDetails.equals("1")) {
			
			out.println("<td id='ejb-detail'>" + reader.getEjbCurrentSize(it) + "</td>");	
			out.println("<td id='ejb-detail'>" + reader.getEjbRemoveCount(it) + "</td>");
			out.println("<td id='ejb-detail'>" + reader.getEjbMaxSize(it) + "</td>");
			out.println("<td id='ejb-detail'>" + reader.getEjbAvailableCount(it) + "</td>");
			/*
			out.println("<td id='ejb-detail'>" + reader.getJdbcMaxSize(it) + "</td>");
			out.println("<td id='ejb-detail'>" + reader.getJdbcMinSize(it) + "</td>");
			out.println("<td id='ejb-detail'>" + reader.getJdbcIdleTimeoutMinutes(it) + "</td>");
			
			out.println("<td id='ejb-detail'>" + reader.getJdbcConnectionCount(it) + "</td>");
			out.println("<td id='ejb-detail'>" + reader.getJdbcAvailableConnectionCount(it) + "</td>");

			out.println("<td id='ejb-detail'>" + reader.getJdbcInUseConnectionCount(it) + "</td>");
			*/
		}
		out.println("<td id='ejb-detail'>" + reader.getEjbCreateCount(it) + "</td>");
		%>
		<td><img src="EjbUsageGraph?ejb=<%=it %>" /></td>
		</tr>
	<% } %>
</table>

<h3>WebServices</h3>
<table border="1">
	<tr>
		<td>Name
			<form>
				<input type="hidden" name="wsDetails" value="<%=notWsDetails%>">
				<input type="submit" value='Details' />
			</form>
		</td>
		<% if (wsDetails.equals("1")) { %>
			
			<td class='header-small'>StartTime</td>
			<td class='header-small'>RequestCount</td>
			
	
			<td class='header-small'>Max Processing Time</td>
			<td class='header-small'>Min Processing Time</td>
			<td class='header-small'>Avg Processing Time</td>
			
		<% } %>
		<td class='header-small'>FaultCount</td>
		<td>Graph</td>
	</tr>
	<% for(String it : lstWs) { %>
		<tr>
		<td><%=it %></td>
		<% if(wsDetails.equals("1")) {
			
			out.println("<td id='ws-detail'>" + reader.getWsStartTime(it) + "</td>");
			
			out.println("<td id='ws-detail'>" + reader.getWsRequestCount(it) + "</td>");
			out.println("<td id='ws-detail'>" + reader.getWsMaxProcessingTime(it) + "</td>");
			out.println("<td id='ws-detail'>" + reader.getWsMinProcessingTime(it) + "</td>");
			out.println("<td id='ws-detail'>" + reader.getWsAverageProcessingTime(it) + "</td>");
			
			/*
			out.println("<td id='ws-detail'>" + reader.getEjbRemoveCount(it) + "</td>");
			out.println("<td id='ws-detail'>" + reader.getEjbMaxSize(it) + "</td>");
			out.println("<td id='ws-detail'>" + reader.getEjbAvailableCount(it) + "</td>");
			*/
		}
		out.println("<td id='ws-detail'>" + reader.getWsFaultCount(it) + "</td>");
		%>
		<td><img src="WsUsageGraph?ejb=<%=it %>" /></td>
		</tr>
	<% } %>
</table>
</body>
</html>