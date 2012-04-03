<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1><s:text name="title.eMailManagement" /></h1>
<div id="main">
	<h2><s:text name="title.eMailManagement.sendersConfig.trashSender" /></h2>

	<s:form action="deleteSender" >
		<p class="noscreen"><wpsf:hidden name="code"/></p>
		
		<p>
			<s:text name="note.sendersConfig.trash" />&#32;<em class="important"><s:property value="code" /></em>?
			&#32;
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.remove')}" cssClass="button" />
		</p>
		
		<p><s:text name="note.sendersConfig.trash.goBack" />&nbsp;<a href="<s:url action="viewSenders"/>"><s:text name="title.eMailManagement.sendersConfig" /></a></p>
	</s:form>

</div> 