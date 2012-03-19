<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="jpavatar" uri="/jpavatar-apsadmin-core" %>

<h1><s:text name="title.avatarManagement" /></h1>
<div id="main">
	<p class="message message_confirm">
		<s:text name="jpavatar.label.saveOK" />
	</p>
	<p>
		<a href="<s:url action="edit" />"><s:text name="label.back.to.management" /></a>
	</p>
</div>