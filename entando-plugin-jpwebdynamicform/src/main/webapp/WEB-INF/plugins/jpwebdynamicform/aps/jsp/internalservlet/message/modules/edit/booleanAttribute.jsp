<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:if test="#lang.default">
	<wpsf:radio useTabindexAutoIncrement="true" name="%{#attributeTracer.getFormFieldName(#attribute)}" id="true_%{#attributeTracer.getFormFieldName(#attribute)}" value="true" checked="%{#attribute.value == true}" />
	&#32;<wp:i18n key="YES" />
	
	<wpsf:radio useTabindexAutoIncrement="true" name="%{#attributeTracer.getFormFieldName(#attribute)}" id="false_%{#attributeTracer.getFormFieldName(#attribute)}" value="false" checked="%{#attribute.value == false}" />
	&#32;<wp:i18n key="NO" />
</s:if>