<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1><s:text name="title.jpwebMail" /></h1>
<div id="main">
	<s:form action="save">
		<p><s:text name="jpwebmail.intro" /></p>
		
		
		<s:if test="hasFieldErrors()">
			<div class="message message_error">	
				<h2><s:text name="message.title.FieldErrors" /></h2>
				<ul>
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
				            <li><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		
		
		<fieldset class="margin-more-top">
			<legend><s:text name="title.jpwebMail.config" /></legend>
			<p>
				<label for="config.domainName" class="basic-mint-label"><s:text name="label.domainName" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.domainName" id="config.domainName" cssClass="text" />
			</p>
		</fieldset>	
		<fieldset>
			<legend><s:text name="label.certificates" /></legend>
			<p>	
				<label for="config.certificatePath" class="basic-mint-label"><s:text name="label.certificatePath" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.certificatePath" id="config.certificatePath" cssClass="text"/>
			</p>
			<p>	
				<wpsf:checkbox useTabindexAutoIncrement="true" name="config.certificateEnable" id="config.certificateEnable" cssClass="radiocheck"/>
				<label for="config.certificateEnable"><s:text name="label.certificateEnable" /></label>
			</p>
			<p>
				<wpsf:checkbox useTabindexAutoIncrement="true" name="config.certificateLazyCheck" id="config.certificateLazyCheck" cssClass="radiocheck"/>
				<label for="config.certificateLazyCheck"><s:text name="label.certificateLazyCheck" /></label>
			</p>
			<p>
				<wpsf:checkbox useTabindexAutoIncrement="true" name="config.certificateDebugOnConsole" id="config.certificateDebugOnConsole" cssClass="radiocheck"/>
				<label for="config.certificateDebugOnConsole"><s:text name="label.certificateDebugOnConsole"/></label>
			</p>
		</fieldset>
		<fieldset>
			<legend><s:text name="label.imap" /></legend>
			<p>	
				<label for="config.imapHost"  class="basic-mint-label"><s:text name="label.imapHost" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.imapHost" id="config.imapHost" cssClass="text"/>
			</p>
			<p>	
				<label for="config.imapPort" class="basic-mint-label"><s:text name="label.imapPort" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.imapPort" id="config.imapPort" cssClass="text"/>
			</p>
			<p>	
				<label for="config.imapProtocol" class="basic-mint-label"><s:text name="label.imapProtocol" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.imapProtocol" id="config.imapProtocol" cssClass="text" />
			</p>	
		</fieldset>
		<fieldset>
			<legend><s:text name="label.smtp" /></legend>
			<p>
				<label for="config.smtpHost" class="basic-mint-label"><s:text name="label.smtpHost" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.smtpHost" id="config.smtpHost" cssClass="text" />
			</p>
			<p>	
				<label for="config.smtpUserName" class="basic-mint-label"><s:text name="label.smtpUserName" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.smtpUserName" id="config.smtpUserName" cssClass="text"/>
			</p>
			<p>	
				<label for="config.smtpPassword" class="basic-mint-label"><s:text name="label.smtpPassword" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.smtpPassword" id="config.smtpPassword" cssClass="text" />
			</p>
			<p>	
				<label for="config.smtpPort" class="basic-mint-label"><s:text name="label.smtpPort" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.smtpPort" id="config.smtpPort" cssClass="text" />
			</p>
			<p>	
				<wpsf:checkbox useTabindexAutoIncrement="true" name="config.debug" id="config.debug" cssClass="radiocheck"/>
				<label for="config.debug"><s:text name="label.debug" /></label>
			</p>
			<p>	 
				<wpsf:checkbox useTabindexAutoIncrement="true" name="config.smtpJapsUserAuth" id="config.smtpJapsUserAuth" cssClass="radiocheck" />
				<label for="config.smtpJapsUserAuth"><s:text name="label.smtpJapsUserAuth" /></label>
			</p>
		</fieldset>
		<fieldset>
			<legend><s:text name="label.folders" /></legend>
			<p>	
				<label for="config.trashFolderName" class="basic-mint-label"><s:text name="label.trashFolderName" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.trashFolderName" id="config.trashFolderName" cssClass="text" />
			</p>
			<p>
				<label for="config.sentFolderName" class="basic-mint-label"><s:text name="label.sentFolderName" />:</label> 
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.sentFolderName" id="config.sentFolderName" cssClass="text" />
			</p>
		</fieldset>
		<fieldset>
			<legend><s:text name="label.tempDiskRootFolder" /></legend>
			<p>	
				<label for="config.tempDiskRootFolder" class="basic-mint-label"><s:text name="label.tempDiskRootFolder" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.tempDiskRootFolder" id="config.tempDiskRootFolder" cssClass="text" />
			</p>
		</fieldset>
		<p class="centerText">
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button"/>
		</p>
	</s:form>
</div>