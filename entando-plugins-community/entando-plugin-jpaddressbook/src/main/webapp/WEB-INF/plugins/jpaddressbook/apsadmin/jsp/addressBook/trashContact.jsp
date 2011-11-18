<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<s:set var="targetNS" value="%{'/do/jpaddressbook/AddressBook'}" />
<h1><s:text name="title.contactManagement" /><jsp:include page="/WEB-INF/apsadmin/jsp/common/inc/operations-context-general.jsp" /></h1>
<div id="main">
	<h2><s:text name="jpaddressbook.title.trash" /></h2>
	<s:set var="contact" value="%{getContact(entityId)}" />
	<s:set value="#contact.contactInfo.firstNameAttributeName" var="nameKey"/>
	<s:set value="#contact.contactInfo.surnameAttributeName" var="surnameKey"/>
	<s:set value="#contact.contactInfo.getAttribute(#nameKey).getText()" var="name" />
	<s:set value="#contact.contactInfo.getAttribute(#surnameKey).getText()" var="surname" />

	<s:form action="delete">
		<p>
			<wpsf:hidden name="entityId"/>
			<s:text name="jpaddressbook.admin.confirmRemove" />&#32;
			<em class="important"><s:property value="#name" />&#32;<s:property value="#surname" /></em>&#32;
			(<s:property value="#contact.id"/>)?&#32;
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.remove')}" cssClass="button" />
		</p>
		<%--  
		<p>
			<s:text name="jpaddressbook.admin.confirmRemoveGoBack" />&#32;<a href="<s:url namespace="/do/jpaddressbook/AddressBook" action="list" />"><s:text name="jpaddressbook.title.addressbook" /></a>
		</p>
		--%>
	</s:form>
</div>