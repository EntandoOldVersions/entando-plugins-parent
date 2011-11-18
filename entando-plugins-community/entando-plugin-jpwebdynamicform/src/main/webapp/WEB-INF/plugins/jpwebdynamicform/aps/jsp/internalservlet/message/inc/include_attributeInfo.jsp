<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>

<s:if test="#attribute.required || #attribute.indexingType != 'NONE' || (#attribute.textAttribute && (#attribute.minLength != -1 || #attribute.maxLength != -1))">
	<s:if test="#attribute.required"><abbr title="<wp:i18n key="jpwebdynamicform_ENTITY_ATTRIBUTE_FLAG_MANDATORY_FULL"/>"><wp:i18n key="jpwebdynamicform_ENTITY_ATTRIBUTE_FLAG_MANDATORY_SHORT"/></abbr> </s:if>
	<s:if test="#attribute.textAttribute">
		<s:if test="#attribute.minLength != -1">&#32;<abbr title="<wp:i18n key="jpwebdynamicform_ENTITY_ATTRIBUTE_FLAG_MINLENGTH_FULL" />" ><wp:i18n key="jpwebdynamicform_ENTITY_ATTRIBUTE_FLAG_MINLENGTH_SHORT" /></abbr>:<s:property value="#attribute.minLength" /> </s:if>
		<s:if test="#attribute.maxLength != -1">&#32;<abbr title="<wp:i18n key="jpwebdynamicform_ENTITY_ATTRIBUTE_FLAG_MAXLENGTH_FULL" />" ><wp:i18n key="jpwebdynamicform_ENTITY_ATTRIBUTE_FLAG_MAXLENGTH_SHORT" /></abbr>:<s:property value="#attribute.maxLength" /> </s:if>
	</s:if>
</s:if>
