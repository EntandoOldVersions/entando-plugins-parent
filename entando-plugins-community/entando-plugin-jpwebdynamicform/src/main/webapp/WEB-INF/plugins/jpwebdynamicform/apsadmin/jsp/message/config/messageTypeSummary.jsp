<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1><s:text name="title.messageManagement" /></h1>
<div id="main">
	<h2><s:text name="title.messageManagement.configuration" /></h2>
	
	<h3 class="margin-more-top"><s:text name="label.messageTypes" /></h3>

	<s:if test="%{messageTypes.size() > 0}">
		<p><s:text name="messageTypes.intro" /></p>

		<s:form action="edit">
			<fieldset class="margin-bit-top">
				<legend><s:text name="label.info" /></legend>
				<p>
					<label for="jpwebdynamicform_message_type" class="basic-mint-label"><s:text name="label.messageType" />:</label>
					<wpsf:select useTabindexAutoIncrement="true" id="jpwebdynamicform_message_type" name="typeCode" list="%{messageTypes}" listKey="code" listValue="descr" />
					&#32;
					<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.continue')}" cssClass="button" />
				</p>
			</fieldset>
		</s:form>
	</s:if>
	<s:else>
		<p><s:text name="no.messageType.to.configure" /></p>
	</s:else>
</div>