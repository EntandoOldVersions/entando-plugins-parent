<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<div class="jpavatar">
	<h2><wp:i18n key="jpavatar_TITLE" /></h2>
	
	<form action="<wp:action path="/ExtStr2/do/jpavatar/Front/Avatar/delete.action" />" method="post">
		<s:set var="delete"><wp:i18n key="jpavatar_DELETE" /></s:set>
		<p>
			<wp:i18n key="jpavatar_CONFIRM_DELETE" />&#32;<s:submit value="%{#delete}"/>
		</p>
	</form>
</div>
	

