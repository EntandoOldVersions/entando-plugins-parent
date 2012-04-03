<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<s:set var="targetNS" value="%{'/do/jpaddressbook/AddressBook'}" />
<h1><s:text name="title.contactManagement" /><jsp:include page="/WEB-INF/apsadmin/jsp/common/inc/operations-context-general.jsp" /></h1>
<div id="main">
	<div class="intro jpaddressbook">
			<s:property value="%{getText('jpaddressbook.note.contacts.intro.html')}" escape="false" />	
	</div>
</div>