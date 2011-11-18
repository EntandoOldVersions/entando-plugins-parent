<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="jpcontentfeedback_moderate">
	<li><a href="<s:url action="list" namespace="/do/jpcontentfeedback/Comments" />" ><s:text name="jpcontentfeedback.admin.menu.contentFeedback" /></a></li>
</wp:ifauthorized>