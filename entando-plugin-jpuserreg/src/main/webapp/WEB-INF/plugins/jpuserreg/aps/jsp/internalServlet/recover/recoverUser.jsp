<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<h2><wp:i18n key="jpuserreg_REACTIVATION_PASSWORD_LOST_MSG"/></h2>

<form method="post" action="<wp:action path="/ExtStr2/do/jpuserreg/UserReg/recoverFromUsername.action" />" enctype="multipart/form-data">
<s:if test="hasFieldErrors()">
	<h3><s:text name="message.title.FieldErrors" /></h3>
	<ul>
		<s:iterator value="fieldErrors">
			<s:iterator value="value">
				<s:set name="label">
					<s:property />
				</s:set>
				<li><s:property /></li>
			</s:iterator>
		</s:iterator>
	</ul>
</s:if> <s:if test="hasActionErrors()">
	<h3><s:text name="message.title.ActionErrors" /></h3>
	<ul>
		<s:iterator value="actionErrors">
			<s:set name="label">
				<s:property />
			</s:set>
			<li><s:property /></li>
		</s:iterator>
	</ul>
</s:if>
	
	<p>
		<label for="username"><wp:i18n key="jpuserreg_USERNAME"/></label><br />
		<wpsf:textfield useTabindexAutoIncrement="true" name="username" id="username" />
	</p>
	<p>
		<input type="submit" value="<wp:i18n key="jpuserreg_SEND"/>" />
	</p>
</form>

<h2><wp:i18n key="jpuserreg_REACTIVATION_USERNAME_LOST_MSG"/></h2>

<form method="post" action="<wp:action path="/ExtStr2/do/jpuserreg/UserReg/recoverFromEmail.action" />" enctype="multipart/form-data">
	
	<p>
		<label for="email"><wp:i18n key="jpuserreg_EMAIL"/></label><br />
		<wpsf:textfield useTabindexAutoIncrement="true" name="email" id="email" />
	</p>
	<p>	
		<input type="submit" value="<wp:i18n key="jpuserreg_SEND"/>" />
	</p>
</form>
