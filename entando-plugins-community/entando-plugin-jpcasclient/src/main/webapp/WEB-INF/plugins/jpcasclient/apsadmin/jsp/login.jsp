<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:contentNegotiation mimeType="application/xhtml+xml" charset="utf-8"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="it">
<head>
	<title>jAPS 2.0 - Login 
	
	<% 

	String user = request.getRemoteUser();
	out.println( user );

	%>
	</title>
	<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/css/administration.css" />
	<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/css/layout.css" />
	<!--[if lte IE 6]>
		<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/css/layout_ie6.css" />
	<![endif]-->

	<%-- 	<script type="text/javascript" src="<wp:resourceURL />administration/js/mootools-beta-1.2b2.js"></script> --%>
	<script type="text/javascript" src="<wp:resourceURL />administration/js/mootools-1.2-core.js"></script>
	<script type="text/javascript" src="<wp:resourceURL />administration/js/mootools-1.2-more.js"></script>
		
	
<script type="text/javascript">
<!--//--><![CDATA[//><!--
window.addEvent('domready', function(){
	$('username').focus();
});
//--><!]]></script>	
	
</head>
<body>

<h1>jAPS 2.0 - Authorization Required</h1>

<p>This server could not verify that you are authorized to access the document requested. Either you supplied the wrong credentials (e.g., bad password), or your browser doesn't understand how to supply the credentials required.</p>


<s:if test="hasActionErrors()">
<div class="message message_error">
<h2><s:text name="message.title.ActionErrors" /></h2>
<ul>
	<s:iterator value="actionErrors">
	<li><s:property/></li>
	</s:iterator>
</ul>
</div>
</s:if>


<s:if test="hasFieldErrors()">
<div class="message message_error">
<h2><s:text name="message.title.FieldErrors" /></h2>	
<ul>
	<s:iterator value="fieldErrors">
		<s:iterator value="value">
          <li><s:property/></li>
		</s:iterator>
	</s:iterator>
</ul>
</div>
</s:if>


</body>
</html>