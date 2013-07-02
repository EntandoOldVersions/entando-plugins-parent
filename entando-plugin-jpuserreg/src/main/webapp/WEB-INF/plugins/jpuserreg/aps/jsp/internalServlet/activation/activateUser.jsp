<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<form action="<wp:action path="/ExtStr2/do/jpuserreg/UserReg/activate.action"/>" method="post" >
	
	<s:if test="hasFieldErrors()">
		<h3><s:text name="message.title.FieldErrors" /></h3>
		<ul>
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
		            <li><s:property/></li>
				</s:iterator>
			</s:iterator>
		</ul>
	</s:if>
	<s:if test="hasActionErrors()">
		<h3><s:text name="message.title.ActionErrors" /></h3>
		<ul>
			<s:iterator value="actionErrors">
				<li><s:property/></li>
			</s:iterator>
		</ul>
	</s:if>
	
	<p>
		<wpsf:hidden name="token" value="%{token}" />
	</p>
	
	<p>
		<label for="password"><wp:i18n key="jpuserreg_PASSWORD"/> <abbr title="(<wp:i18n key="jpuserreg_REQUIRED"/>)">*</abbr></label><br />
		<wpsf:password useTabindexAutoIncrement="true" name="password" id="password" maxlength="20" />
	</p>
	<p>
		<label for="passwordConfirm"><wp:i18n key="jpuserreg_PASSWORD_CONFIRM"/> <abbr title="(<wp:i18n key="jpuserreg_REQUIRED"/>)">*</abbr></label><br />
		<wpsf:password useTabindexAutoIncrement="true" name="passwordConfirm" id="passwordConfirm" maxlength="20" />
	</p>
	<p>
		<input type="submit" value="<wp:i18n key="jpuserreg_SAVE"/>" />
	</p>

</form>