<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1 class="panel panel-default title-page">
	<span class="panel-body display-block"><s:text name="title.jpwebMail" /></span>
</h1>

<s:form action="save">
	<s:if test="hasActionMessages()">
		<div class="alert alert-success alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>
			<ul class="margin-base-top">
				<s:iterator value="actionMessages">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	<s:if test="hasFieldErrors()">
		<div class="alert alert-success alert-dismissable fade in">
			<button class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
			<h2 class="h4 margin-none"><s:text name="messages.confirm" /></h2>
			<ul class="margin-base-top">
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	
	<fieldset class="col-xs-12 margin-large-top" id="additional-features"><legend><s:text name="title.jpwebMail.config" /></legend>
		<div class="form-group">
			<label for="config.domainName"><s:text name="label.domainName" />:</label>
			<wpsf:textfield useTabindexAutoIncrement="true" name="config.domainName" id="config.domainName" cssClass="text" />
		</div>
	</fieldset>
	
	<fieldset class="col-xs-12 margin-large-top" id="additional-features"><legend><s:text name="label.certificates" /></legend>
		<div class="form-group">
			<label for="config.certificatePath"><s:text name="label.certificatePath" />:</label>
			<wpsf:textfield useTabindexAutoIncrement="true" name="config.certificatePath" id="config.certificatePath" cssClass="text"/>
		</div>
		
		<div class="form-group">
			<label class="checkbox">
				<wpsf:checkbox useTabindexAutoIncrement="true" name="config.certificateEnable" id="config.certificateEnable" cssClass="radiocheck"/>
				&#32;<s:text name="label.certificateEnable" />
			</label>
			<label class="checkbox">
				<wpsf:checkbox useTabindexAutoIncrement="true" name="config.certificateLazyCheck" id="config.certificateLazyCheck" cssClass="radiocheck"/>
				&#32;<s:text name="label.certificateLazyCheck" />
			</label>
			<label class="checkbox">
				<wpsf:checkbox useTabindexAutoIncrement="true" name="config.certificateDebugOnConsole" id="config.certificateDebugOnConsole" cssClass="radiocheck"/>
				&#32;<s:text name="label.certificateDebugOnConsole" />
			</label>
		</div>
		
	</fieldset>
	
	<fieldset class="col-xs-12 margin-large-top" id="additional-features"><legend><s:text name="label.imap" /></legend>
		<div class="form-group">
			<label for="config.imapHost"><s:text name="label.imapHost" />:</label>
			<wpsf:textfield useTabindexAutoIncrement="true" name="config.imapHost" id="config.imapHost" cssClass="text"/>
		</div>
		<div class="form-group">
			<label for="config.imapPort"><s:text name="label.imapPort" />:</label>
			<wpsf:textfield useTabindexAutoIncrement="true" name="config.imapPort" id="config.imapPort" cssClass="text"/>
		</div>
		<div class="form-group">
			<label for="config.imapProtocol"><s:text name="label.imapProtocol" />:</label>
			<wpsf:textfield useTabindexAutoIncrement="true" name="config.imapProtocol" id="config.imapProtocol" cssClass="text" />
		</div>
	</fieldset>
	
	<fieldset class="col-xs-12 margin-large-top" id="additional-features"><legend><s:text name="label.smtp" /></legend>
		<div class="form-group">
			<label for="config.smtpHost"><s:text name="label.smtpHost" />:</label>
			<wpsf:textfield useTabindexAutoIncrement="true" name="config.smtpHost" id="config.smtpHost" cssClass="text" />
		</div>
		<div class="form-group">
			<label for="config.smtpUserName"><s:text name="label.smtpUserName" />:</label>
			<wpsf:textfield useTabindexAutoIncrement="true" name="config.smtpUserName" id="config.smtpUserName" cssClass="text"/>
		</div>
		<div class="form-group">
			<label for="config.smtpPassword"><s:text name="label.smtpPassword" />:</label>
			<wpsf:textfield useTabindexAutoIncrement="true" name="config.smtpPassword" id="config.smtpPassword" cssClass="text" />
		</div>
		<div class="form-group">
			<label for="config.smtpPort"><s:text name="label.smtpPort" />:</label>
			<wpsf:textfield useTabindexAutoIncrement="true" name="config.smtpPort" id="config.smtpPort" cssClass="text" />
		</div>
		<div class="form-group">
			<label class="checkbox">
				<wpsf:checkbox useTabindexAutoIncrement="true" name="config.debug" id="config.debug" cssClass="radiocheck"/>
				&#32;<s:text name="label.debug" />
			</label>
			<label class="checkbox">
				<wpsf:checkbox useTabindexAutoIncrement="true" name="config.smtpEntandoUserAuth" id="config.smtpEntandoUserAuth" cssClass="radiocheck" />
				&#32;<s:text name="label.smtpEntandoUserAuth" />
			</label>
		</div>
	</fieldset>
	
	<fieldset class="col-xs-12 margin-large-top" id="additional-features"><legend><s:text name="label.folders" /></legend>
		<div class="form-group">
			<label for="config.trashFolderName"><s:text name="label.trashFolderName" />:</label>
			<wpsf:textfield useTabindexAutoIncrement="true" name="config.trashFolderName" id="config.trashFolderName" cssClass="text" />
		</div>
		<div class="form-group">
			<label for="config.sentFolderName"><s:text name="label.sentFolderName" />:</label>
			<wpsf:textfield useTabindexAutoIncrement="true" name="config.sentFolderName" id="config.sentFolderName" cssClass="text" />
		</div>
	</fieldset>
	
	<fieldset class="col-xs-12 margin-large-top" id="additional-features"><legend><s:text name="label.tempDiskRootFolder" /></legend>
		<div class="form-group">
			<label for="config.tempDiskRootFolder"><s:text name="label.tempDiskRootFolder" />:</label>
			<wpsf:textfield useTabindexAutoIncrement="true" name="config.tempDiskRootFolder" id="config.tempDiskRootFolder" cssClass="text" />
		</div>
	</fieldset>
	
	<div class="form-horizontal">
		<div class="form-group">
			<div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
				<wpsf:submit type="button" cssClass="btn btn-primary btn-block">
					<span class="icon fa fa-floppy-o"></span>&#32;
					<s:text name="label.save" />
				</wpsf:submit>
			</div>
		</div>
	</div>
	
</s:form>