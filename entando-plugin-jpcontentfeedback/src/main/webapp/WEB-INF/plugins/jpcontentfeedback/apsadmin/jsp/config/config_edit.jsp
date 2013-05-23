<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<h1><s:text name="title.contentfeedbackManagement" /></h1>
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
			<legend><s:text name="jpcontentfeedback.comments" /></legend>
			<p>
				<input 
					type="checkbox" 
					class="radiocheck"
					id="jpcontentfeedback_comment"
					name="config.comment" 
					value="true" <s:if test="config.comment"> checked="checked"</s:if>  
				/>
				<label for="jpcontentfeedback_comment"><s:text name="jpcontentfeedback.label.commentsOnContent" /></label>
			</p>
			<p>
				<input 
					type="checkbox" 
					class="radiocheck"
					id="jpcontentfeedback_anonymousComment"
					name="config.anonymousComment" 
					value="true" <s:if test="config.anonymousComment"> checked="checked"</s:if>
				/>
				<label for="jpcontentfeedback_anonymousComment"><s:text name="jpcontentfeedback.label.anonymousComments"/></label>
				<span class="inline"><s:text name="jpcontentfeedback.note.anonymousComments" /></span>
			</p>
			<p>
				<input 
					type="checkbox" 
					class="radiocheck"
					id="jpcontentfeedback_moderatedComment"
					name="config.moderatedComment" 
					value="true" <s:if test="config.moderatedComment"> checked="checked"</s:if>
				/>
				<label for="jpcontentfeedback_moderatedComment"><s:text name="jpcontentfeedback.label.commentsModeration"/></label>
				<span class="inline"><s:text name="jpcontentfeedback.note.commentsModeration" /></span>
			</p>
			
			<p>
				<input 
					type="checkbox" 
					class="radiocheck"
					id="jpcontentfeedback_rateComment"
					name="config.rateComment" 
					value="true" <s:if test="config.rateComment"> checked="checked"</s:if>
				/>
				<label for="jpcontentfeedback_rateComment"><s:text name="jpcontentfeedback.label.commentsRating" /></label>
			</p>
		
		</fieldset>
		<fieldset>
			<legend><s:text name="jpcontentfeedback.contents" /></legend>
			<p>
				<input 
					type="checkbox" 
					class="radiocheck"
					id="jpcontentfeedback_rateContent"
					name="config.rateContent" 
					value="true" <s:if test="config.rateContent"> checked="checked"</s:if> 
				/>
				<label for="jpcontentfeedback_rateContent"><s:text name="jpcontentfeedback.label.contentsRating" /></label>
			</p>
		</fieldset>
		<p class="centerText">
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" />
		</p>
	</s:form>
</div>