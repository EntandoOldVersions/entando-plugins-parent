<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="#lang.default">
	<s:set name="labelSelect"><wp:i18n key="jpwebdynamicform_SELECT" /></s:set>
	<wpsf:select useTabindexAutoIncrement="true" name="%{#attributeTracer.getFormFieldName(#attribute)}" id="%{#attributeTracer.getFormFieldName(#attribute)}"
		headerValue="%{labelSelect}" headerKey="" list="#attribute.items" value="%{#attribute.getText()}" />
</s:if>