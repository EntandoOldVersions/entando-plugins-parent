<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<s:set name="titleKey">jpwebdynamicform_TITLE_<s:property value="typeCode"/></s:set>
<s:set name="typeCodeKey" value="typeCode" />

<br />
<wp:i18n key="${titleKey}" />
<br />

<form action="<wp:action path="/ExtStr2/do/jpwebdynamicform/Message/User/send.action"/>" method="post">
	<s:if test="hasFieldErrors()">
	<div>
		<wp:i18n key="ERRORS"/>
		<ul>
		<s:iterator value="fieldErrors">
			<s:iterator value="value">
				<li><s:property escape="false" /></li>
			</s:iterator>
		</s:iterator>
		</ul>
	</div>
	</s:if>
	<s:if test="hasActionErrors()">
	<div>
		<wp:i18n key="ERRORS"/>
		<ul>
			<s:iterator value="actionErrors">
				<li><s:property escape="false" /></li>
			</s:iterator>
		</ul>
	</div>
	</s:if>

	<wpsf:hidden name="typeCode" />

	<s:set name="lang" value="defaultLang" />
	<br />
	<wp:i18n key="jpwebdynamicform_INFO" />
	<br />

<!-- START CICLO ATTRIBUTI -->
<s:iterator value="message.attributeList" id="attribute">
<%-- INIZIALIZZAZIONE TRACCIATORE --%>
<wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" />
<s:set name="attributeNameI18nKey" value="#attribute.name" />

<br />

<s:if test="#attribute.type == 'Boolean' || #attribute.type == 'ThreeState' || #attribute.type == 'CheckBox'">
	<wp:i18n key="jpwebdynamicform_${typeCodeKey}_${attributeNameI18nKey}" /><s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/inc/include_attributeInfo.jsp" />:
</s:if>
<s:else>
	<wp:i18n key="jpwebdynamicform_${typeCodeKey}_${attributeNameI18nKey}" /><s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/inc/include_attributeInfo.jsp" />:
	<br />
</s:else>

<s:if test="#attribute.type == 'Monotext' || #attribute.type == 'Text'">
<!-- ############# ATTRIBUTO TESTO MONOLINGUA ############# -->
<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/monotextAttribute.jsp" />
<br />
</s:if>

<s:elseif test="#attribute.type == 'Longtext'">
<!-- ############# ATTRIBUTO TESTOLUNGO ############# -->
<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/longtextAttribute.jsp" />
<br />
</s:elseif>

<s:elseif test="#attribute.type == 'Number'">
<!-- ############# ATTRIBUTO Number ############# -->
<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/numberAttribute.jsp" />
<br />
</s:elseif>

<s:elseif test="#attribute.type == 'Date'">
<!-- ############# ATTRIBUTO Date ############# -->
<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/dateAttribute.jsp" />
<br />
</s:elseif>

<s:elseif test="#attribute.type == 'Boolean'">
<!-- ############# ATTRIBUTO Boolean ############# -->
<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/booleanAttribute.jsp" />
<br />
</s:elseif>

<s:elseif test="#attribute.type == 'CheckBox'">
<!-- ############# ATTRIBUTO Boolean ############# -->
<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/checkBoxAttribute.jsp" />
<br />
</s:elseif>

<s:elseif test="#attribute.type == 'ThreeState'">
<!-- ############# ATTRIBUTO ThreeState ############# -->
<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/threeStateAttribute.jsp" />
<br />
</s:elseif>

<s:elseif test="#attribute.type == 'Enumerator'">
<!-- ############# ATTRIBUTO TESTO Enumerator ############# -->
<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/enumeratorAttribute.jsp" />
<br />
</s:elseif>

<s:elseif test="#attribute.type == 'Composite'">
<!-- ############# ATTRIBUTO Composite ############# -->
<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/compositeAttribute.jsp" />
<br />
</s:elseif>

</s:iterator>
<!-- END CICLO ATTRIBUTI -->

<br />
<s:set name="labelSubmit"><wp:i18n key="jpwebdynamicform_INVIA" /></s:set>
<wpsf:submit useTabindexAutoIncrement="true" value="%{#labelSubmit}" />

</form>