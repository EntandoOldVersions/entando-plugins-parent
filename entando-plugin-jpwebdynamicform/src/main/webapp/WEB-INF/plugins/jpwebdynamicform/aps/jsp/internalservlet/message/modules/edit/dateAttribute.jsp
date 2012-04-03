<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:if test="#lang.default">
	<s:if test="#attribute.failedDateString == null">
	<s:set name="dateAttributeValue" value="#attribute.getFormattedDate('dd/MM/yyyy')"></s:set>
	</s:if>
	<s:else>
	<s:set name="dateAttributeValue" value="#attribute.failedDateString"></s:set>
	</s:else>
	<wpsf:textfield useTabindexAutoIncrement="true" id="%{#attributeTracer.getFormFieldName(#attribute)}" 
			name="%{#attributeTracer.getFormFieldName(#attribute)}" value="%{#dateAttributeValue}"
			maxlength="254" />
</s:if>