<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h1><s:text name="jpaddressbook.title.vcard" /></h1>
<div id="main">
	<%-- Attivazione Campi --%>
	<h2><s:text name="jpaddressbook.title.manageActiveFields" /></h2>
	<p><s:text name="jpaddressbook.manageActiveFields.intro" /></p>
		
	<s:if test="hasFieldErrors()">
		<div class="message message_error">
		<h3><s:text name="message.title.FieldErrors" /></h3>
		<ul>
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</s:iterator>
		</ul>
		</div>
	</s:if>
	
	<s:form action="filterFields">
		<fieldset class="margin-more-top">  
			<legend><s:text name="label.info" /></legend>
			
			<s:iterator var="vcardField" value="vCardFields">
				<s:set var="code" value="#vcardField.code"/>
				<s:set var="profileAttribute" value="#vcardField.profileAttribute"/>
				<p>
					<wpsf:checkbox useTabindexAutoIncrement="true" name="selectedFields" fieldValue="%{#code}" value="%{getSelectedFields().contains(#code)}" id="selectedFields_%{#code}" cssClass="radiocheck"/>
					<label for="selectedFields_<s:property value="#code"/>"><s:text name="%{'jpaddressbook.vcard.field.'+#vcardField.code}" />&#32;&ndash;&#32;<dfn><s:property value="#vcardField.code"/></dfn></label>
				</p>
			</s:iterator>
			
		</fieldset>
		<p><wpsf:submit useTabindexAutoIncrement="true" value="%{getText('jpaddressbook.vcardSubmit.continue')}" cssClass="button" /></p>
	</s:form>
</div>