<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<s:if test="#lang.default">
	<label class="radio inline" for="<s:property value="%{#attribute_id + '-true'}" />">
		<wpsf:radio 
			useTabindexAutoIncrement="true" 
			name="%{#attributeTracer.getFormFieldName(#attribute)}" 
			id="%{#attribute_id + '-true'}" 
			value="true" 
			checked="%{#attribute.value == true}" 
			cssClass="radio" />
			<wp:i18n key="YES" />
	</label>
	&#32;
	<label class="radio inline" for="<s:property value="%{#attribute_id+'-false'}" />">
		<wpsf:radio 
			useTabindexAutoIncrement="true" 
			name="%{#attributeTracer.getFormFieldName(#attribute)}" 
			id="%{#attribute_id + '-false'}" 
			value="false" 
			checked="%{#attribute.value == false}" 
			cssClass="radio" />
			<wp:i18n key="NO" />
	</label>


</s:if>