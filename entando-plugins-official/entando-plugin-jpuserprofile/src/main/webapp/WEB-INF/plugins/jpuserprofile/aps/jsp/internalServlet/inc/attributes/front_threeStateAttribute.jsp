<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/aps-core" prefix="wp" %>

<ul class="noBullet">
	<li><wpsf:radio useTabindexAutoIncrement="true" name="%{#attributeTracer.getFormFieldName(#attribute)}" id="none_%{attribute_id}" value="" checked="%{#attribute.booleanValue == null}" cssClass="radio" /><label for="none_<s:property value="%{attribute_id}" />" class="normal" ><wp:i18n key="jpuserprofile_BOTH_YES_AND_NO" /></label></li>
	<li><wpsf:radio useTabindexAutoIncrement="true" name="%{#attributeTracer.getFormFieldName(#attribute)}" id="true_%{attribute_id}" value="true" checked="%{#attribute.booleanValue != null && #attribute.booleanValue == true}" cssClass="radio" /><label for="true_<s:property value="%{attribute_id}" />" class="normal" ><wp:i18n key="jpuserprofile_YES" /></label></li>
	<li><wpsf:radio useTabindexAutoIncrement="true" name="%{#attributeTracer.getFormFieldName(#attribute)}" id="false_%{attribute_id}" value="false" checked="%{#attribute.booleanValue != null && #attribute.booleanValue == false}" cssClass="radio" /><label for="false_<s:property value="%{attribute_id}" />" class="normal"><wp:i18n key="jpuserprofile_NO" /></label></li>
</ul>
