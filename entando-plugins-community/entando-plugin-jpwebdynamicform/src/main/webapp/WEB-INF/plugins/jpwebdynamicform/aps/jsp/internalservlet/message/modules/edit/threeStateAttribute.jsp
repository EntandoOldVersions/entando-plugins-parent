<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:if test="#lang.default">
	<wpsf:radio useTabindexAutoIncrement="true" name="%{#attributeTracer.getFormFieldName(#attribute)}" id="none_%{#attributeTracer.getFormFieldName(#attribute)}"
			value="" checked="%{#attribute.booleanValue == null}" />
	&#32;<wp:i18n key="jpwebdynamicform_${typeCodeKey}_${attributeNameI18nKey}_NONE" />

	<wpsf:radio useTabindexAutoIncrement="true" name="%{#attributeTracer.getFormFieldName(#attribute)}" id="true_%{#attributeTracer.getFormFieldName(#attribute)}"
			value="true" checked="%{#attribute.booleanValue != null && #attribute.booleanValue == true}" />
	&#32;<wp:i18n key="jpwebdynamicform_${typeCodeKey}_${attributeNameI18nKey}_YES" />

	<wpsf:radio useTabindexAutoIncrement="true" name="%{#attributeTracer.getFormFieldName(#attribute)}" id="false_%{#attributeTracer.getFormFieldName(#attribute)}"
			value="false" checked="%{#attribute.booleanValue != null && #attribute.booleanValue == false}" />
	&#32;<wp:i18n key="jpwebdynamicform_${typeCodeKey}_${attributeNameI18nKey}_NO" />
</s:if>