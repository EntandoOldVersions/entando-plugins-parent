<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<fieldset>
	<legend><s:text name="jpversioning.name" /></legend>
	
	<p class="important"><s:text name="jpversioning.hookpoint.configSystemParams.workversion.config" />:</p>
	<s:set name="paramName" value="'jpversioning_deleteMidVersions'" />
	<ul class="noBullet radiocheck">
		<li><input type="radio" class="radiocheck" id="<s:property value="#paramName"/>_true" name="<s:property value="#paramName"/>" value="true" <s:if test="systemParams[#paramName]">checked="checked"</s:if> /><label for="<s:property value="#paramName"/>_true"><s:text name="jpversioning.hookpoint.configSystemParams.workversion.config.yes"/></label></li>
		<li><input type="radio" class="radiocheck" id="<s:property value="#paramName"/>_false" name="<s:property value="#paramName"/>" value="false" <s:if test="systemParams[#paramName] == 'false'">checked="checked"</s:if> /><label for="<s:property value="#paramName"/>_false"><s:text name="jpversioning.hookpoint.configSystemParams.workversion.config.no"/></label></li>
	</ul>
	
</fieldset>