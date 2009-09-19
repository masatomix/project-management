<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>


<html:html xhtml="true" lang="true">
<head>
<title>ログインJSP</title>
<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=UTF-8" />
<html:base />
</head>
<body>
<ul>
<html:messages id="message" message="false" >
  <li><bean:write name="message" /></li>
</html:messages>
</ul>

<html:form action="/login" >
ユーザID:<html:text property="userid"/> <-IDはhoge<br />
パスワード:<html:password property="password"/> <-パスワードはfuga<br />
<html:submit value="登録"/><br>
</html:form>

</body>
</html:html>



