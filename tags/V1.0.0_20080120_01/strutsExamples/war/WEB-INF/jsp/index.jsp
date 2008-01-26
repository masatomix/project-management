<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<%@page import="org.apache.struts.Globals"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<html:html xhtml="true" lang="true">
<head>
<title>同期トークンの稼動確認JSP</title>
<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=UTF-8" />
<html:base />
</head>
<body>

Formの場合
<html:form method="post" action="/double" >
	<html:submit />
</html:form>
<br />

リンクの場合
<ul>
<li><html:link action="/double" >transaction属性なしのリンク(エラー)</html:link></li>
<li><html:link action="/double" transaction="false">transaction属性=falseのリンク(エラー)</html:link></li>
<li><html:link action="/double" transaction="true">transaction属性=trueのリンク(ＯＫ)</html:link></li>
<%
	Map map=new HashMap();
	map.put("data1","value1");
	map.put("data2","value2");
	map.put("data3","value3");
	map.put("data4","value4");
	request.setAttribute("map",map);
%>
<li><html:link action="/double" name="map" transaction="true">明示的にパラメタ指定しているリンクも大丈夫</html:link></li>
</ul>
<%=session.getAttribute(Globals.TRANSACTION_TOKEN_KEY) %>
</body>
</html:html>