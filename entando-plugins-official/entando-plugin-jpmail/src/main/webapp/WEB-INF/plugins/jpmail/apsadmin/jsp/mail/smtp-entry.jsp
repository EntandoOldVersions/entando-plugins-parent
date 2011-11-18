<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1><s:text name="title.eMailManagement" /></h1>
<div id="main">
	
	<h2 class="margin-bit-bottom"><s:text name="label.smtpConfig" /></h2> 

	<p>
		<s:text name="label.jpmail.intro" />
	</p>
	
	<s:form action="saveSmtp" >
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
		<s:if test="hasActionErrors()">
			<div class="message message_error">	
				<h3><s:text name="message.title.ActionErrors" /></h3>
				<ul>
					<s:iterator value="actionErrors">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		
		<fieldset class="margin-more-top">
			<legend><s:text name="legend.connection" /></legend>

			<p>
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
			<p>
				<wpsf:checkbox useTabindexAutoIncrement="true" name="debug" id="debug" cssClass="radiocheck" />&nbsp;<label for="debug"><s:text name="label.debug" /></label>
			</p>
		</fieldset> 
		<fieldset>
			<legend><s:text name="legend.authentication" /></legend> 
			<p>
				<label for="smtpUserName" class="basic-mint-label"><s:text name="smtpUserName" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="smtpUserName" id="smtpUserName" cssClass="text" />
			</p>
			<p>
				<label for="smtpPassword" class="basic-mint-label"><s:text name="smtpPassword" />:</label>
				<wpsf:password useTabindexAutoIncrement="true" name="smtpPassword" id="smtpPassword" cssClass="text" />
			</p>
		</fieldset> 
		<p class="centerText">
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" />
		</p>
		
	</s:form>
	
</div>