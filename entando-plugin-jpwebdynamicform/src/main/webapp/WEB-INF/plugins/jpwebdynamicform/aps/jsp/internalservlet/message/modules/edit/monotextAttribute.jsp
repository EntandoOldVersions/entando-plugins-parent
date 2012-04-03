<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:if test="#lang.default">
	<wpsf:textfield useTabindexAutoIncrement="true" id="%{#attributeTracer.getFormFieldName(#attribute)}" 
		name="%{#attributeTracer.getFormFieldName(#attribute)}" value="%{#attribute.getTextForLang(#lang.code)}"
		maxlength="254" />
</s:if>