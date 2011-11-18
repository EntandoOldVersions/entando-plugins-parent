<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="jpavatar" uri="/jpavatar-apsadmin-core" %>
<div class="jpavatar">
	<h2><wp:i18n key="jpavatar_TITLE" /></h2>
	<p>
		<wp:i18n key="jpavatar_AVATAR_SAVED" />.&#32;
		<a href="<s:url action="edit" />"><wp:i18n key="jpavatar_OK_THANKYOU" /></a>
	</p>
</div>
