<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<h2><wp:i18n key="jpuserreg_REGISTRATION"/></h2>

<form action="<wp:action path="/ExtStr2/do/jpuserreg/UserReg/initRegistration"/>" method="post" >
		<p>
			<label for="profileTypeCode"><wp:i18n key="jpuserreg_PROFILETYPE" />:</label>
			<select name="profileTypeCode" tabindex="<wpsa:counter/>" id="profileTypeCode" class="text">
				<s:iterator value="profileTypes" var="profileType" >
					<s:set name="optionDescr">jpuserreg_TITLE_<s:property value="#profileType.typeCode"/></s:set>
				    <option value="<s:property value="#profileType.typeCode"/>"><wp:i18n key="${optionDescr}" /></option>
				</s:iterator>
			</select>
		</p>
		<p>
			<s:set name="labelChoose"><wp:i18n key="jpuserreg_CHOOSE_TYPE" /></s:set>
			<wpsf:submit useTabindexAutoIncrement="true" value="%{#labelChoose}" />
		</p>
</form> 