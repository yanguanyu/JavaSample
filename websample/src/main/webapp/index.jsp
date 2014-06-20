<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="greeter" class="org.gradle.sample.Greeter"/>
<html>
<p>${greeter.greeting}</p>
</html>
<%
String nameValue = (String)request.getAttribute("name");
String secondValue = (String)request.getAttribute("second");
String valueValue = request.getParameter("value");
%>
<h1>name=<%=nameValue %></h1>
<h1>secondValue=<%=secondValue %></h1>
<h1>valueValue=<%=valueValue %></h1>