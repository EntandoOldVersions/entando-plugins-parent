<%@ taglib prefix="s" uri="/struts-tags" %>
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
<s:if test="hasActionMessages()">
	<div class="message message_confirm">
		<h3><s:text name="messages.confirm" /></h3>	
		<ul>
			<s:iterator value="actionMessages">
				<li><s:property escape="false" /></li>
			</s:iterator>
		</ul>
	</div>
</s:if>

