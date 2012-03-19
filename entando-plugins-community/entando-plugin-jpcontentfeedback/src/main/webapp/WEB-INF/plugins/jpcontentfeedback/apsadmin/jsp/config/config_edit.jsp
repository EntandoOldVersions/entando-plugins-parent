<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1>
<s:text name="title.contentfeedbackManagement" />
</h1>

<div id="main">
	<h2><s:text name="title.contentfeedbackSettings" /></h2>

	<s:form action="update">
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

		<s:if test="hasActionMessages()">
			<div class="message message_confirm">
				<h3><s:text name="messages.confirm" /></h3>
				<ul>
				<s:iterator value="actionMessages">
					<li><s:property/></li>
				</s:iterator>
				</ul>
			</div>
		</s:if>

		<fieldset class="margin-more-top">
			<legend><s:text name="label.info" /></legend>
				<p>
					<label for="jpcontentfeedback_rateContent" class="basic-mint-label"><s:text name="jpcontentfeedback.label.rateContent" />:</label>
					<wpsf:textfield useTabindexAutoIncrement="true" id="jpcontentfeedback_rateContent" name="config.rateContent" cssClass="text" />
				</p>

				<p>
					<label for="jpcontentfeedback_comment" class="basic-mint-label"><s:text name="jpcontentfeedback.label.comment" />:</label>
					<wpsf:textfield useTabindexAutoIncrement="true" id="jpcontentfeedback_comment" name="config.comment" cssClass="text" />
				</p>

				<p>
					<label for="jpcontentfeedback_rateComment" class="basic-mint-label"><s:text name="jpcontentfeedback.label.rateComment" />:</label>
					<wpsf:textfield useTabindexAutoIncrement="true" id="jpcontentfeedback_rateComment" name="config.rateComment" cssClass="text" />
				</p>

				<p>
					<label for="jpcontentfeedback_anonymousComment" class="basic-mint-label"><s:text name="jpcontentfeedback.label.anonymousComment" />:</label>
					<wpsf:textfield useTabindexAutoIncrement="true" id="jpcontentfeedback_anonymousComment" name="config.anonymousComment" cssClass="text" />
				</p>
			</fieldset>

			<p class="centerText">
				<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" />
			</p>
	</s:form>
</div>