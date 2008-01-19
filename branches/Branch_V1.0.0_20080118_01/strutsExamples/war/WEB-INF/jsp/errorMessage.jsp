<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@page import="org.apache.struts.action.ActionMessages"%>
<html:html xhtml="true" lang="true">
<head>
<title>ActionMessage系のサンプル</title>
<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=UTF-8" />
<html:base />
</head>
<body>

<ul>
<html:messages id="message" message="false" >
  <li><bean:write name="message" /></li>
</html:messages>
</ul>

<ul>
<html:messages id="message"  property="HogeGroup">
  <li><bean:write name="message" /></li>
</html:messages>
</ul>
↑message属性はデフォルトfalseなので省略可能。

<ul>
<html:messages id="message"  property="<%=ActionMessages.GLOBAL_MESSAGE%>">
  <li><bean:write name="message" /></li>
</html:messages>
</ul>

----<br />
<html:errors/>
----
↑html:errorsタグの場合。requestスコープからActionMessage群を取得して、グルグル出力する。<br />
ヘッダとフッタなどはpropertiesの
<pre>
errors.header=&lt;ul&gt;
errors.prefix=&lt;li&gt;
errors.suffix=&lt;/li&gt;
errors.footer=&lt;/ul&gt;
</pre>
で指定する。このキー値は特別らしい。


<html:errors header="myErrors.header" footer="myErrors.footer" />
</body>
</html:html>