<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<h2><wp:i18n key="jpuserreg_SUSPENDING_CONFIRM_MSG"/></h2>
<p>You will be logged out and redirect to home page</p>

<s:if test="hasFieldErrors()">
		<h3><s:text name="message.title.FieldErrors" /></h3>
		<ul>
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
					<s:set name="label" ><s:property/></s:set>
					<li><s:property /></li>
				</s:iterator>
			</s:iterator>
		</ul>
	</s:if>
	<s:if test="hasActionErrors()">
		<h3><s:text name="message.title.ActionErrors" /></h3>
		<ul>
			<s:iterator value="actionErrors">
				<s:set name="label" ><s:property/></s:set>
				<li><s:property /></li>
			</s:iterator>
		</ul>
	</s:if>

<form method="post" action="<wp:action path="/ExtStr2/do/jpuserreg/UserReg/suspend.action" />" >
	<p>
		<label for="password"><wp:i18n key="jpuserreg_PASSWORD"/> <abbr title="(<wp:i18n key="jpuserreg_REQUIRED"/>)">*</abbr></label><br />
		<wpsf:password useTabindexAutoIncrement="true" name="password" required="true" id="password" />
	</p>
	<p>
		<input type="submit" value="<wp:i18n key="jpuserreg_SUSPEND"/>" />
	</p>
</form>