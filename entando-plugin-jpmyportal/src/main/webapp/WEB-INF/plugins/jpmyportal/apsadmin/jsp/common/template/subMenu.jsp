<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="superuser">
	<li><a href="<s:url action="edit" namespace="/do/jpmyportal/MyPortal/Config" />" ><s:text name="jpmyportal.menu.myportal.config" /></a></li>
</wp:ifauthorized>