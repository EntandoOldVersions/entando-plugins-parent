<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="superuser">
	<li><a href="<s:url action="edit" namespace="/do/jpmyportalplus/Config" />" ><s:text name="jpmyportalplus.menu.config" /></a></li>
</wp:ifauthorized>