<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:if test="#lang.default">
	<wpsf:textarea useTabindexAutoIncrement="true" cols="50" rows="3" id="%{#attributeTracer.getFormFieldName(#attribute)}" 
		name="%{#attributeTracer.getFormFieldName(#attribute)}" value="%{#attribute.getTextForLang(#lang.code)}" />
</s:if>