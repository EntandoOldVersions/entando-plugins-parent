<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>

<%--Version for plugin jpwebdynamicform --%>
<s:set name="masterCompositeAttributeTracer" value="#attributeTracer" />
<s:set name="masterCompositeAttribute" value="#attribute" />
<s:iterator value="#attribute.attributes" id="attribute">
<s:set name="attributeTracer" value="#masterCompositeAttributeTracer.getCompositeTracer(#masterCompositeAttribute)"></s:set>
<s:set name="parentAttribute" value="#masterCompositeAttribute"></s:set>
	<s:set name="attributeNameI18nKey" value="#attribute.name" />
	<br />

	<wp:i18n key="jpwebdynamicform_${typeCodeKey}_${attributeNameI18nKey}" />
	<s:if test="#attribute.type == 'Monotext' || #attribute.type == 'Text'">
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/monotextAttribute.jsp" />
	</s:if>
	<s:elseif test="#attribute.type == 'Longtext'">
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/longtextAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Number'">
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/numberAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Date'">
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/dateAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Boolean'">
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/booleanAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'ThreeState'">
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/threeStateAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'Enumerator'">
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/enumeratorAttribute.jsp" />
	</s:elseif>
	<s:elseif test="#attribute.type == 'CheckBox'">
		<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/checkBoxAttribute.jsp" />
	</s:elseif>
</s:iterator>
<s:set name="attributeTracer" value="#masterCompositeAttributeTracer" />
<s:set name="attribute" value="#masterCompositeAttribute" />
<s:set name="attributeNameI18nKey" value="#attribute.name" />
<s:set name="parentAttribute" value=""></s:set>