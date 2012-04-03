<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1><a href="<s:url action="list" ></s:url>" ><s:text name="title.jpversioning.contentHistoryManagement" /></a></h1>
<div id="main">
	<h2><s:text name="title.jpversioning.content" /></h2>
	<h3 class="margin-bit-top"><s:text name="title.jpversioning.delete.confirm" /></h3>
	 
	<s:set var="contentVersion" value="%{getContentVersion(versionId)}" />
	<s:form action="delete">
		<p>
			<s:hidden name="versionId" />
			<s:hidden name="contentId" />
			<s:hidden name="backId" />
			<s:text name="message.jpversioning.confirmDelete" />&#32;<em><s:property value="%{#contentVersion.version}"/>&#32;<s:property value="#contentVersion.contentId" /></em>?
			&#32;
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.confirm')}" cssClass="button" />
		</p>
	</s:form>
</div>