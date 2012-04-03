<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<h1><s:text name="jpaddressbook.title.vcard" /></h1>
<div id="main">
	<%-- Configurazione Campi --%>
	<h2><s:text name="jpaddressbook.title.mappingActiveFields" /></h2>
	<p><s:text name="jpaddressbook.mappingActiveFields.intro" /></p>
		
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
	
	<s:form action="save">
		<fieldset class="margin-more-top">
			<legend><s:text name="label.info" /></legend>
			<p class="noscreen">
				<s:iterator var="field" value="selectedFields" status="rowStatus">
					<wpsf:hidden name="selectedFields" value="%{#field}" id="selectedFields_%{#rowStatus.index}" />
				</s:iterator>
			</p>

			<s:iterator var="vcardField" value="vCardFields">
				<s:set var="code" value="#vcardField.code"/>
				<s:if test="%{getSelectedFields().contains(#code)}">
					<s:set var="profileAttribute" value="%{getProfileMapping().get(#code)}" />
					<p>
						<label for="jpaddressbook_vcard_sel_<s:property value="%{#code}" />" class="basic-mint-label"><s:text name="%{'jpaddressbook.vcard.field.'+#vcardField.code}" />&#32;&ndash;&#32;<dfn><s:property value="#vcardField.code"/></dfn>:</label>
						<wpsf:select useTabindexAutoIncrement="true" name="sel_%{#code}" id="jpaddressbook_vcard_sel_%{#code}" 
							list="entityFields" 
							listKey="name" 
							listValue="name" 
							headerKey="" 
							headerValue="" 
							value="%{#profileAttribute}"
							cssClass="text"/>
					</p>
				</s:if>
			</s:iterator>
		</fieldset>
		<p class="centerText" ><wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" /></p>
	</s:form>
	<%-- Configurazione Campi --%>
</div>