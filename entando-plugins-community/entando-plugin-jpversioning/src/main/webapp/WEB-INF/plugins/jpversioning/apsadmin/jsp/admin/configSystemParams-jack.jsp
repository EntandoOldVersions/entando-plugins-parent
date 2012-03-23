<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<fieldset>
	<legend><s:text name="jpversioning.name" /></legend>
	
	<p class="important"><s:text name="jpversioning.hookpoint.configSystemParams.workversion.config" />:</p>
	<s:set name="jpversioning_paramName1" value="'jpversioning_deleteMidVersions'" />
	<ul class="noBullet radiocheck">
		<li><input type="radio" class="radiocheck" id="<s:property value="#jpversioning_paramName1"/>_true" name="<s:property value="#jpversioning_paramName1" />" value="true" <s:if test="systemParams[#jpversioning_paramName1]">checked="checked"</s:if> /><label for="<s:property value="#jpversioning_paramName1" />_true"><s:text name="jpversioning.hookpoint.configSystemParams.workversion.config.yes"/></label></li>
		<li><input type="radio" class="radiocheck" id="<s:property value="#jpversioning_paramName1"/>_false" name="<s:property value="#jpversioning_paramName1" />" value="false" <s:if test="systemParams[#jpversioning_paramName1] == 'false'">checked="checked"</s:if> /><label for="<s:property value="#jpversioning_paramName1" />_false"><s:text name="jpversioning.hookpoint.configSystemParams.workversion.config.no"/></label></li>
		<wpsf:hidden name="%{#jpversioning_paramName1 + externalParamMarker}" value="true"/>
	</ul>
	
	<p>
        <label for="maxMonthsSinceLastAccess" class="basic-mint-label"><s:text name="jpversioning.hookpoint.configSystemParams.resourceTrashRootDiskFolder" />:</label>
		<s:set name="jpversioning_paramName2" value="'jpversioning_resourceTrashRootDiskFolder'" />
        <wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpversioning_paramName2}" id="%{#jpversioning_paramName2}" value="%{systemParams.get(#jpversioning_paramName2)}" cssClass="text" />
        <wpsf:hidden name="%{#jpversioning_paramName2 + externalParamMarker}" value="true"/>
	</p>
	
</fieldset>