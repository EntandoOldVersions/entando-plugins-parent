<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<form action="<wp:action path="/ExtStr2/do/jpwebdynamicform/Message/User/new.action"/>" method="post">
	<wp:info key="currentLang" var="currentLang" />
	<fieldset>
	<p>
		<label for="typeCode"><wp:i18n key="jpwebdynamicform_MESSAGETYPE" /></label>:<br />

		<select name="typeCode" tabindex="<wpsa:counter/>" id="typeCode" class="text">
		<s:iterator value="messageTypes" id="messageType" >
			<s:set name="optionDescr">jpwebdynamicform_TITLE_<s:property value="#messageType.code"/></s:set>
		    <option value="<s:property value="#messageType.code"/>"><wp:i18n key="${optionDescr}" /></option>
		</s:iterator>
		</select>
	</p>
	</fieldset>
	<fieldset>
		<p>
			<s:set name="labelChoose"><wp:i18n key="jpwebdynamicform_CHOOSE_TYPE" /></s:set>
			<wpsf:submit useTabindexAutoIncrement="true" value="%{#labelChoose}" />
		</p>
	</fieldset>

</form>