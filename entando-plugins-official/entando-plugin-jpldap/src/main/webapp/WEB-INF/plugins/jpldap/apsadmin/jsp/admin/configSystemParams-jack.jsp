<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<fieldset>
	<legend><s:text name="jpldap.name" /></legend>
<p>
        <s:set name="jpldap_paramName" value="'jpldap_active'" />
        <input type="checkbox" class="radiocheck" id="<s:property value="#jpldap_paramName"/>" name="<s:property value="#jpldap_paramName"/>" value="true" <s:if test="systemParams.get(#jpldap_paramName)">checked="checked"</s:if> />
        <wpsf:hidden name="%{#jpldap_paramName + externalParamMarker}" value="true"/>
        <label for="<s:property value="#jpldap_paramName" />"><s:text name="label.active" /></label>
</p>
<p>
        <label for="maxMonthsSinceLastAccess" class="basic-mint-label"><s:text name="jpldap.hookpoint.configSystemParams.providerUrl" />:</label>
	<s:set name="jpldap_paramName" value="'jpldap_providerUrl'" />
        <wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpldap_paramName}" id="%{#jpldap_paramName}" value="%{systemParams.get(#jpldap_paramName)}" cssClass="text" />
        <wpsf:hidden name="%{#jpldap_paramName + externalParamMarker}" value="true"/>
</p>
<p>
        <label for="maxMonthsSinceLastAccess" class="basic-mint-label"><s:text name="jpldap.hookpoint.configSystemParams.securityPrincipal" />:</label>
	<s:set name="jpldap_paramName" value="'jpldap_securityPrincipal'" />
        <wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpldap_paramName}" id="%{#jpldap_paramName}" value="%{systemParams.get(#jpldap_paramName)}" cssClass="text" />
        <wpsf:hidden name="%{#jpldap_paramName + externalParamMarker}" value="true"/>
</p>
<p>
        <label for="maxMonthsSinceLastAccess" class="basic-mint-label"><s:text name="jpldap.hookpoint.configSystemParams.securityCredentials" />:</label>
	<s:set name="jpldap_paramName" value="'jpldap_securityCredentials'" />
        <wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpldap_paramName}" id="%{#jpldap_paramName}" value="%{systemParams.get(#jpldap_paramName)}" cssClass="text" />
        <wpsf:hidden name="%{#jpldap_paramName + externalParamMarker}" value="true"/>
</p>
<p>
        <label for="maxMonthsSinceLastAccess" class="basic-mint-label"><s:text name="jpldap.hookpoint.configSystemParams.userObjectClass" />:</label>
	<s:set name="jpldap_paramName" value="'jpldap_userObjectClass'" />
        <wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpldap_paramName}" id="%{#jpldap_paramName}" value="%{systemParams.get(#jpldap_paramName)}" cssClass="text" />
        <wpsf:hidden name="%{#jpldap_paramName + externalParamMarker}" value="true"/>
</p>
<p>
        <label for="maxMonthsSinceLastAccess" class="basic-mint-label"><s:text name="jpldap.hookpoint.configSystemParams.userIdAttributeName" />:</label>
	<s:set name="jpldap_paramName" value="'jpldap_userIdAttributeName'" />
        <wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpldap_paramName}" id="%{#jpldap_paramName}" value="%{systemParams.get(#jpldap_paramName)}" cssClass="text" />
        <wpsf:hidden name="%{#jpldap_paramName + externalParamMarker}" value="true"/>
</p>
<p>
        <label for="maxMonthsSinceLastAccess" class="basic-mint-label"><s:text name="jpldap.hookpoint.configSystemParams.filterGroup" />:</label>
	<s:set name="jpldap_paramName" value="'jpldap_filterGroup'" />
        <wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpldap_paramName}" id="%{#jpldap_paramName}" value="%{systemParams.get(#jpldap_paramName)}" cssClass="text" />
        <wpsf:hidden name="%{#jpldap_paramName + externalParamMarker}" value="true"/>
</p>
<p>
        <label for="maxMonthsSinceLastAccess" class="basic-mint-label"><s:text name="jpldap.hookpoint.configSystemParams.filterGroupAttributeName" />:</label>
	<s:set name="jpldap_paramName" value="'jpldap_filterGroupAttributeName'" />
        <wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpldap_paramName}" id="%{#jpldap_paramName}" value="%{systemParams.get(#jpldap_paramName)}" cssClass="text" />
        <wpsf:hidden name="%{#jpldap_paramName + externalParamMarker}" value="true"/>
</p>
<p>
        <label for="maxMonthsSinceLastAccess" class="basic-mint-label"><s:text name="jpldap.hookpoint.configSystemParams.searchResultMaxSize" />:</label>
	<s:set name="jpldap_paramName" value="'jpldap_searchResultMaxSize'" />
        <wpsf:textfield useTabindexAutoIncrement="true" name="%{#jpldap_paramName}" id="%{#jpldap_paramName}" value="%{systemParams.get(#jpldap_paramName)}" cssClass="text" />
        <wpsf:hidden name="%{#jpldap_paramName + externalParamMarker}" value="true"/>
</p>
</fieldset>