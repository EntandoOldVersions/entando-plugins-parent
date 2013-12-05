<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<form action="<wp:action path="/ExtStr2/do/jpuserreg/UserReg/activate.action"/>" method="post" class="form-horizontal">
	
	<s:if test="hasFieldErrors()">
		<h2><wp:i18n key="ERRORS" /></h2>
		<ul>
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
		            <li><s:property/></li>
				</s:iterator>
			</s:iterator>
		</ul>
	</s:if>
	<s:if test="hasActionErrors()">
		<h2><wp:i18n key="ERRORS" /></h2>
		<ul>
			<s:iterator value="actionErrors">
				<li><s:property/></li>
			</s:iterator>
		</ul>
	</s:if>
	
	<p class="noscreen">
		<wpsf:hidden name="token" value="%{token}" />
	</p>
	
	<div class="control-group">
		<label for="password" class="control-label"><wp:i18n key="jpuserreg_PASSWORD"/> <abbr title="<wp:i18n key="jpuserreg_REQUIRED"/>">*</abbr></label><br />
		<div class="controls">
			<wpsf:password useTabindexAutoIncrement="true" name="password" id="password" maxlength="20" />
		</div>
	</div>
	<div class="control-group">
		<label for="passwordConfirm" class="control-label"><wp:i18n key="jpuserreg_PASSWORD_CONFIRM"/> <abbr title="<wp:i18n key="jpuserreg_REQUIRED"/>">*</abbr></label><br />
		<div class="controls">
			<wpsf:password useTabindexAutoIncrement="true" name="passwordConfirm" id="passwordConfirm" maxlength="20" />
		</div>
	</div>
	<p class="form-actions">
		<input type="submit" value="<wp:i18n key="jpuserreg_SAVE"/>" class="btn btn-primary" />
	</p>

</form>