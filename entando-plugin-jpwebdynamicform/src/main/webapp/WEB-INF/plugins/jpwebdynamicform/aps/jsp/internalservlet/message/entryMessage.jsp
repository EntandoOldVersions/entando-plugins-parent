<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<s:set var="titleKey">jpwebdynamicform_TITLE_<s:property value="typeCode"/></s:set>
<s:set var="typeCodeKey" value="typeCode" />
<s:set var="lang" value="defaultLang" />

<h1><wp:i18n key="${titleKey}" /></h1>

<form 
	class="form-horizontal jpwebdynamicform-<s:property value="#typeCodeKey" />"
	action="<wp:action path="/ExtStr2/do/jpwebdynamicform/Message/User/send.action"/>" 
	method="post">
		<s:if test="hasFieldErrors()">
			<div class="alert alert-block">
				<p><strong><wp:i18n key="ERRORS"/></strong></p>
				<ul class="unstyled">
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>

		<s:if test="hasActionErrors()">
			<div class="alert alert-block">
				<p><strong><wp:i18n key="ERRORS"/></strong></p>
				<ul class="unstyled">
					<s:iterator value="actionErrors">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		
		<s:set var="hasFieldErrors" value="%{hasFieldErrors()}" />
		<s:set var="fieldErrors" value="%{fieldErrors}" />

		<p class="noscreen hide">
			<wpsf:hidden name="typeCode" />
		</p>

		<p>
			<wp:i18n key="jpwebdynamicform_INFO" />
		</p>
		
		<s:iterator value="message.attributeList" var="attribute">
			<wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" />
			<s:set var="i18n_attribute_name" value="%{'jpwebdynamicform_'+ #typeCodeKey +'_'+ #attribute.name}" scope="request" />
			<s:set var="attribute_id" value="%{'jpwebdynamicform_'+ #typeCodeKey +'_'+ #attributeTracer.getFormFieldName(#attribute)}" />
			<s:set var="fieldErrorClass" value="%{#fieldErrors.containsKey(#attributeTracer.getFormFieldName(#attribute)) ? ' error ' : ' '  }" />

			<%-- Attributes --%>
				<s:if test="#attribute.type != 'Composite'">
					<div class="control-group <s:property value="%{' attribute-type-'+#attribute.type+' '}" /> <s:property value="#fieldErrorClass" />">
						<label class="control-label" for="<s:property value="#attribute_id" />">
							<wp:i18n key="${i18n_attribute_name}" />
							<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/inc/include_attributeInfo.jsp" />
						</label>
						<div class="controls">
							<s:if test="#attribute.type == 'Boolean'">
								<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/booleanAttribute.jsp" />
							</s:if>

							<s:elseif test="#attribute.type == 'CheckBox'">
								<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/checkBoxAttribute.jsp" />
							</s:elseif>

							<s:elseif test="#attribute.type == 'Date'">
								<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/dateAttribute.jsp" />
							</s:elseif>

							<s:elseif test="#attribute.type == 'Enumerator'">
								<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/enumeratorAttribute.jsp" />
							</s:elseif>

							<s:elseif test="#attribute.type == 'Longtext'">
								<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/longtextAttribute.jsp" />
							</s:elseif>

							<s:elseif test="#attribute.type == 'Number'">
								<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/numberAttribute.jsp" />
							</s:elseif>

							<s:elseif test="#attribute.type == 'Monotext' || #attribute.type == 'Text'">
								<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/monotextAttribute.jsp" />
							</s:elseif>

							<s:elseif test="#attribute.type == 'ThreeState'">
								<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/threeStateAttribute.jsp" />
							</s:elseif>

							<s:else>
								<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/monotextAttribute.jsp" />
							</s:else>

							<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/inc/front_attributeInfo-help-block.jsp" />
						</div>
					</div>
				</s:if>
				<s:else>
					<div class="well well-small">
						<fieldset class=" <s:property value="%{' attribute-type-'+#attribute.type+' '}" /> ">
							<legend class="margin-medium-top"><wp:i18n key="${i18n_attribute_name}" /><s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/inc/include_attributeInfo.jsp" /></legend>
							<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/inc/front_attributeInfo-help-block.jsp" />
							<s:include value="/WEB-INF/plugins/jpwebdynamicform/aps/jsp/internalservlet/message/modules/edit/compositeAttribute.jsp" />
						</fieldset>
					</div>
				</s:else>
			<%-- Attributes end--%>

			<%-- 
			<s:set var="attributeNameI18nKey" value="#attribute.name" />
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
			--%>
		</s:iterator>
		
		<p class="form-actions">
			<wp:i18n key="jpwebdynamicform_INVIA" var="labelSubmit" />
			<wpsf:submit 
				cssClass="btn btn-primary"
				useTabindexAutoIncrement="true" 
				value="%{#attr.labelSubmit}" />
		</p>
</form>