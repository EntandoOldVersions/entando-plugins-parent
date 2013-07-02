<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<h2><wp:i18n key="jpuserreg_REGISTRATION"/></h2>

<form action="<wp:action path="/ExtStr2/do/jpuserreg/UserReg/register.action" />" method="post" >
	
	<s:if test="hasFieldErrors()">
		<h3><s:text name="message.title.FieldErrors" /></h3>
		<ul>
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
					<s:set name="label" ><s:property/></s:set>
					<li><s:property /></li>
				</s:iterator>
			</s:iterator>
		</ul>
	</s:if>
	<s:if test="hasActionErrors()">
		<h3><s:text name="message.title.ActionErrors" /></h3>
		<ul>
			<s:iterator value="actionErrors">
				<s:set name="label" ><s:property/></s:set>
				<li><s:property /></li>
			</s:iterator>
		</ul>
	</s:if>
	
	<s:set name="lang" value="defaultLang"></s:set>
	
	<p class="noscreen" >
		<wpsf:hidden name="profileTypeCode" />
	</p>
	
	<p>
		<label for="privacyPolicyAgreement"><wp:i18n key="jpuserreg_PRIVACY_AGREEMENT"/> <abbr title="(<wp:i18n key="jpuserreg_REQUIRED"/>)">*</abbr></label><br />
		<wpsf:checkbox useTabindexAutoIncrement="true" name="privacyPolicyAgreement" id="privacyPolicyAgreement" />
	</p>
	
	<p>
		<label for="username"><wp:i18n key="jpuserreg_USERNAME"/> <abbr title="(<wp:i18n key="jpuserreg_REQUIRED"/>)">*</abbr></label><br />
		<wpsf:textfield useTabindexAutoIncrement="true" name="username" id="username" maxlength="40" />
	</p>
	
	<%-- START CICLO ATTRIBUTI --%>
	<s:iterator value="userProfile.attributeList" id="attribute">
	<s:if test="%{#attribute.active}">
		<%-- INIZIALIZZAZIONE TRACCIATORE --%>
		<wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" />
		<s:set var="i18n_attribute_name">jpuserprofile_<s:property value="userProfile.typeCode" />_<s:property value="#attribute.name" /></s:set>
		<s:set var="attribute_id">jpuserprofile_<s:property value="#attribute.name" /></s:set>
		<s:include value="/WEB-INF/plugins/jpuserprofile/aps/jsp/internalServlet/inc/iteratorAttribute.jsp" />
		<s:if test="%{#attribute.name == emailAttrName}" >
		<p>
			<label for="eMailConfirm"><wp:i18n key="jpuserreg_EMAIL_CONFIRM"/> <abbr title="(<wp:i18n key="jpuserreg_REQUIRED"/>)">*</abbr></label><br />
			<wpsf:textfield useTabindexAutoIncrement="true" name="emailConfirm" id="eMailConfirm" />
		</p>
		</s:if>
	</s:if>
	</s:iterator>
	<%-- END CICLO ATTRIBUTI --%>
	
	<s:set var="savelabel"><wp:i18n key="jpuserreg_SAVE" /></s:set>
	<p><wpsf:submit useTabindexAutoIncrement="true" value="%{savelabel}" /></p>
	
</form>