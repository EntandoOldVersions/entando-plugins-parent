<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>



<h1><s:text name="title.eMailManagement" /></h1>


<div id="main">

	<h2 class="margin-bit-bottom"><s:text name="label.smtpConfig" /></h2> 

	<p>
		<s:text name="label.jpmail.intro" />
	</p>
	<s:form id="configurationForm" name="configurationForm" method="post" action="testSmtp">
			
		<s:include value="/WEB-INF/plugins/jpmail/apsadmin/jsp/mail/inc/smtp-messages.jsp" />
		<fieldset class="margin-more-top">
			<legend><s:text name="legend.generalSettings" /></legend>
			<p class="margin-more-top">
				<wpsf:checkbox useTabindexAutoIncrement="true" name="active" id="active" cssClass="radiocheck" />&nbsp;<label for="active"><s:text name="label.active" /></label>
			</p>
			<p>
				<wpsf:checkbox useTabindexAutoIncrement="true" name="debug" id="debug" cssClass="radiocheck" />&nbsp;<label for="debug"><s:text name="label.debug" /></label>
			</p>
		</fieldset> 

		<fieldset class="margin-more-top">
			<legend><s:text name="legend.connection" /></legend>

			<p class="margin-more-top">
				<label for="smtpHost" class="basic-mint-label"><s:text name="smtpHost" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="smtpHost" id="smtpHost" cssClass="text" />
			</p>
			<p>
				<label for="smtpPort" class="basic-mint-label"><s:text name="label.smtpPort" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="smtpPort" id="smtpPort" cssClass="text" />
			</p>
			<p class="important">
				<s:text name="label.security" />:
			</p>
			<ul class="noBullet radiocheck">
				<li>
					<wpsf:radio useTabindexAutoIncrement="true" id="smtpstd" name="smtpProtocol" value="0" checked="%{smtpProtocol == 0}" cssClass="radio" />
					<label for="smtpstd"><s:text name="label.smtp.standard"/></label>
				</li>
				<li>
					<wpsf:radio useTabindexAutoIncrement="true" id="smtpssl" name="smtpProtocol" value="1" checked="%{smtpProtocol == 1}" cssClass="radio" />
					<label for="smtpssl"><s:text name="label.smtp.ssl"/></label>
				</li>
				<li>
					<wpsf:radio useTabindexAutoIncrement="true" id="smtptls" name="smtpProtocol" value="2" checked="%{smtpProtocol == 2}" cssClass="radio" />
					<label for="smtptls"><s:text name="label.smtp.tls"/></label>
				</li>
			</ul>
			<p>
				<label for="smtpTimeout" class="basic-mint-label"><s:text name="label.smtpTimeout" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="smtpTimeout" id="smtpTimeout" cssClass="text" />
			</p>
		</fieldset> 
		<fieldset>
			<legend><s:text name="legend.authentication" /></legend> 
			<p class="margin-more-top">
				<label for="smtpUserName" class="basic-mint-label"><s:text name="smtpUserName" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="smtpUserName" id="smtpUserName" cssClass="text" />
			</p>
			<p>
				<label for="smtpPassword" class="basic-mint-label"><s:text name="smtpPassword" />:</label>
				<wpsf:hidden value="%{getSmtpPassword()}" />
				<wpsf:password useTabindexAutoIncrement="true" name="smtpPassword" id="smtpPassword" cssClass="text" />
			</p>
		</fieldset> 
		<div id="messages">
		</div>
		<p class="centerText">
			<wpsf:submit name="save" useTabindexAutoIncrement="true" action="saveSmtp" value="%{getText('label.save')}" cssClass="button" onclick="overrideSubmit('saveSmtp')"/>
			<wpsf:submit name="testMail" useTabindexAutoIncrement="true" value="%{getText('label.sendEmail')}" action="testMail" cssClass="button" onclick="overrideSubmit('testMail')"/>
			<sj:submit parentTheme="simple" formIds="configurationForm" value="%{getText('label.testConnection')}" targets="messages" cssClass="button"/>
		</p>

	</s:form>
</div>
