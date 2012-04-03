<%@ page contentType="application/xhtml+xml; charset=utf-8" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="gwp" uri="/geoAps-core" %>
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="it">
<head>
	<title>
		jAPS 2.0| <wp:currentPage param="title" />
	</title>

    <wp:outputHeadInfo type="CSS"> 
        <link href="resources/css/<wp:printHeadInfo />" type="text/css" rel="stylesheet" />
    </wp:outputHeadInfo>
	
	<wp:outputHeadInfo type="JS_URL"> 
		<script type="text/javascript" src="<wp:printHeadInfo />" ></script>
	</wp:outputHeadInfo>
	
</head>
	
<body <gwp:reqCtxParamPrinter var="htmlBodyTagAttribute"/> style="font-family: Verdana; font-size: 0.9em;">


<!-- START WELCOME -->
<h1 style="font-family: Helvetica;">It Worked! jAPS - java Agile Portal System is Installed on this Host.</h1>
<h2 style="font-family: Helvetica;">Version <wp:info key="systemParam" paramName="version" /></h2>
<p>
<img style="float:left" src="<wp:imgURL />/jAPS_logo.jpg" alt="Logo jAPS" />
If you can see this page, then the people who own this host have just installed jAPS successfully.<br/>
They now have to replace this placeholder page or just its contents.</p>
<p>
You can go to the <a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/login.action">Login form</a> 
and use the following to enter in the Administration Area.<br />
Username: <strong>admin</strong>
Password: <strong>adminadmin</strong>
</p>

<p>
Have a good time with jAPS!<br />
- The AgileTeam -
</p>

<h2 style="font-family: Helvetica;">Links:</h2>
<ul>
	<li><a href="http://www.japsportal.org">Home of the jAPS project</a></li>
	<li><a href="http://sourceforge.net/projects/japs">jAPS@SourceForge</a></li>
</ul>
<!-- END WELCOME -->


<div><wp:show frame="0" /></div>

</body>
</html>
