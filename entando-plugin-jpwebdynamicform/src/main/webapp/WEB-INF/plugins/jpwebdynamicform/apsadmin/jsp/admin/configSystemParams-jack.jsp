<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<fieldset>
	<legend><s:text name="jpwebdynamicform.name" /></legend>
	
	<p>
        <s:set name="jpwebdynamicform_paramName" value="'jpwebdynamicform_recaptcha_publickey'" />
        <label for="<s:property value="#jpwebdynamicform_paramName"/>" class="basic-mint-label"><s:text name="jpwebdynamicform.recaptcha.publickey" />:</label>
		<wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpwebdynamicform_paramName}" id="%{#jpwebdynamicform_paramName}" value="%{systemParams.get(#jpwebdynamicform_paramName)}" cssClass="text" />
        <wpsf:hidden name="%{#jpwebdynamicform_paramName + externalParamMarker}" value="true"/>
	</p>
	
	<p>
        <s:set name="jpwebdynamicform_paramName" value="'jpwebdynamicform_recaptcha_privatekey'" />
        <label for="<s:property value="#jpwebdynamicform_paramName"/>" class="basic-mint-label"><s:text name="jpwebdynamicform.recaptcha.privatekey" />:</label>
		<wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpwebdynamicform_paramName}" id="%{#jpwebdynamicform_paramName}" value="%{systemParams.get(#jpwebdynamicform_paramName)}" cssClass="text" />
        <wpsf:hidden name="%{#jpwebdynamicform_paramName + externalParamMarker}" value="true"/>
	</p>
	
</fieldset>