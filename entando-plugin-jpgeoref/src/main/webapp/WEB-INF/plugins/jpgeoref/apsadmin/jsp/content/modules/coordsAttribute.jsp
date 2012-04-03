<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<s:if test="#lang.default">
<p>
	<label class="basic-mint-label" for="x_<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />">X:</label>
	<wpsf:textfield useTabindexAutoIncrement="true" id="x_%{#attributeTracer.getFormFieldName(#attribute)}" 
	name="x_%{#attributeTracer.getFormFieldName(#attribute)}" value="%{#attribute.x}" 
	maxlength="254" cssClass="text" cssClass="text" />
</p>
<p>
	<label class="basic-mint-label" for="y_<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />">Y:</label>
	<wpsf:textfield useTabindexAutoIncrement="true" id="y_%{#attributeTracer.getFormFieldName(#attribute)}" 
	name="y_%{#attributeTracer.getFormFieldName(#attribute)}" value="%{#attribute.y}" 
	maxlength="254" cssClass="text" cssClass="text" />
</p>
<p>
	<label class="basic-mint-label" for="z_<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />">Z:</label>
	<wpsf:textfield useTabindexAutoIncrement="true" id="z_%{#attributeTracer.getFormFieldName(#attribute)}" 
	name="z_%{#attributeTracer.getFormFieldName(#attribute)}" value="%{#attribute.z}" 
	maxlength="254" cssClass="text" cssClass="text" />
</p>
</s:if>
<s:else>
	<p><s:text name="note.editContent.doThisInTheDefaultLanguage.must" /></p>
</s:else>
