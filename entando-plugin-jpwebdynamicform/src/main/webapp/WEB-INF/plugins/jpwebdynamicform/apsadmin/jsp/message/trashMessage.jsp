<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1><a href="<s:url action="list" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.messageManagement" />"><s:text name="title.messageManagement" /></a></h1>
<div id="main">
	<h2><s:text name="title.messageManagement.trash" /></h2>
	<s:form action="delete" >
		<p class="noscreen"><wpsf:hidden name="id"/></p>
		<s:set var="id" value="message.id" />
		<s:set var="typeDescr" value="message.typeDescr" />
		<p>
			<s:text name="title.messageManagement.trash.info" />:&#32;<s:text name="%{#id}"/>&#32;<s:text name="title.messageManagement.trash.info.type" />&#32;<em><s:property value="#typeDescr"/></em>?
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.confirm')}" cssClass="button" />
		</p>
	</s:form>
</div>
 