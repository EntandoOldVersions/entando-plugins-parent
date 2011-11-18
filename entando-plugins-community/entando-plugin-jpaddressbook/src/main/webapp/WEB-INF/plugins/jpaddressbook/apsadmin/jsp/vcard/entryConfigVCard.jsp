<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<%--
	21-Gen-2011, domanda
	
	NON È UTILIZZATA?????????????????????
	NON È UTILIZZATA?????????????????????
	NON È UTILIZZATA?????????????????????
	NON È UTILIZZATA?????????????????????
	NON È UTILIZZATA?????????????????????
	NON È UTILIZZATA?????????????????????
	NON È UTILIZZATA?????????????????????
	NON È UTILIZZATA?????????????????????
	NON È UTILIZZATA?????????????????????
	NON È UTILIZZATA?????????????????????
	NON È UTILIZZATA?????????????????????
	NON È UTILIZZATA?????????????????????
--%>

<h1><s:text name="jpaddressbook.title.vcard" /></h1>
<%-- Attivazione Campi --%>
	<h2><s:text name="jpaddressbook.title.manageActiveFields" /></h2>

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
		<p><s:text name="jpaddressbook.manageActiveFields.intro" /></p>
		<s:iterator var="vcardField" value="VCardsFields">
			<s:set var="code" value="#vcardField.code"/>
			<s:set var="profileAttribute" value="#vcardField.profileAttribute"/>
			<p>
				<wpsf:checkbox useTabindexAutoIncrement="true" name="chk_%{#code}" value="#vcardField.Enabled" id="%{'jpddressbook_vcard_'+#code}"/>
				<label for="jpddressbook_vcard_<s:property value="%{#code}"/>"><s:text name="%{'jpaddressbook.vcard.field.'+#vcardField.code}" />&#32;&ndash;&#32;<dfn><s:property value="#vcardField.code"/></dfn></label>
			</p>
		</s:iterator>
		
		<p><wpsf:submit useTabindexAutoIncrement="true" value="%{getText('jpaddressbook.vcardSubmit.continue')}" cssClass="button" action="" /></p>
	</s:form>
<%-- Attivazione Campi --%>



<%-- Configurazione Campi --%>
	<h1><s:text name="jpaddressbook.title.vcard" /></h1>
	<h2><s:text name="jpaddressbook.title.mappingActiveFields" /></h2>

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
		<p><s:text name="jpaddressbook.mappingActiveFields.intro" /></p>
		<s:iterator var="vcardField" value="VCardsFields">
			<s:set var="code" value="#vcardField.code"/>
			<s:set var="profileAttribute" value="#vcardField.profileAttribute"/>
			
			<p>
				<label for="jpaddressbook_vcard_sel_<s:property value="%{#code}" />">
					<s:text name="%{'jpaddressbook.vcard.field.'+#vcardField.code}" />&#32;&ndash;&#32;<dfn><s:property value="#vcardField.code"/></dfn>
				</label><br />
				<wpsf:select useTabindexAutoIncrement="true" name="sel_%{#code}" id="jpaddressbook_vcard_sel_%{#code}" 
					list="entityFields" 
					listKey="name" 
					listValue="name"    
					headerKey="" 
					headerValue="" 
					value="%{#profileAttribute}"
					cssClass="text"/>
			</p>
		</s:iterator>
		<p><wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" action="" /></p>
	</s:form>

<%-- Configurazione Campi --%>