<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<%@page import="org.apache.struts.Globals"%>
<html:html xhtml="true" lang="true">
<head>
<title>同期トークンの稼動確認JSP</title>
<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=UTF-8" />
<html:base />
</head>
<body>
<html:form method="post" action="/double" >
	<html:submit />
</html:form>
<%=session.getAttribute(Globals.TRANSACTION_TOKEN_KEY) %>
</body>
</html:html>