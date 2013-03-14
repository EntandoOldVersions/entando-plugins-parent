<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>

<fieldset>
	<legend><s:text name="jpfrontshortcut.name" /></legend>
	<p>
        <s:set name="jpfrontshortcut_paramName" value="'jpfrontshortcut_activePageFrontEndEditing'" />
        <input type="checkbox" class="radiocheck" id="<s:property value="#jpfrontshortcut_paramName" />" name="<s:property value="#jpfrontshortcut_paramName" />" value="true" <s:if test="systemParams.get(#jpfrontshortcut_paramName)">checked="checked"</s:if> />
        <wpsf:hidden name="%{#jpfrontshortcut_paramName + externalParamMarker}" value="true" />
        <label for="<s:property value="#jpfrontshortcut_paramName" />"><s:text name="jpfrontshortcut.label.activePageFrontEndEditing" /></label>
	</p>
	<p>
        <s:set name="jpfrontshortcut_paramName" value="'jpfrontshortcut_activeFrameFrontEndEditing'" />
        <input type="checkbox" class="radiocheck" id="<s:property value="#jpfrontshortcut_paramName" />" name="<s:property value="#jpfrontshortcut_paramName" />" value="true" <s:if test="systemParams.get(#jpfrontshortcut_paramName)">checked="checked"</s:if> />
        <wpsf:hidden name="%{#jpfrontshortcut_paramName + externalParamMarker}" value="true" />
        <label for="<s:property value="#jpfrontshortcut_paramName" />"><s:text name="jpfrontshortcut.label.activeFrameFrontEndEditing" /></label>
	</p>
	<p>
        <s:set name="jpfrontshortcut_paramName" value="'jpfrontshortcut_activeContentFrontEndEditing'" />
        <input type="checkbox" class="radiocheck" id="<s:property value="#jpfrontshortcut_paramName" />" name="<s:property value="#jpfrontshortcut_paramName" />" value="true" <s:if test="systemParams.get(#jpfrontshortcut_paramName)">checked="checked"</s:if> />
        <wpsf:hidden name="%{#jpfrontshortcut_paramName + externalParamMarker}" value="true" />
        <label for="<s:property value="#jpfrontshortcut_paramName" />"><s:text name="jpfrontshortcut.label.activeContentFrontEndEditing" /></label>
	</p>
	<%--
	<p>
		<s:text name="jpfrontshortcut.hookpoint.configSystemParams.reloadConfigurationInfo" />
	</p>
	--%>
</fieldset>