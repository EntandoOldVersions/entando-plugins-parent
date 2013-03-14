<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1><s:text name="title.avatarManagement" /></h1>
<div id="main">
	
	<h2 class="margin-bit-bottom"><s:text name="label.avatar.config" /></h2> 


	
	<s:form action="save" >
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
		

		<fieldset class="margin-more-top">
			<legend><s:text name="legend.config" /></legend>
			
			<p>
				<span class="important"><s:property value="SYTLE" /></span><br />
			</p>
			<ul class="noBullet">
				<li><wpsf:radio useTabindexAutoIncrement="true" name="avatarConfig.style" value="local" id="local_avatarConfig_style" 	 checked="%{avatarConfig.style == 'local'}" 	cssClass="radio" /><label for="local_avatarConfig_style" class="normal" ><s:text name="label.avatarConfig.style.local"/></label></li>
				<li><wpsf:radio useTabindexAutoIncrement="true" name="avatarConfig.style" value="gravatar" id="gravatar_avatarConfig_style" 	 checked="%{avatarConfig.style == 'gravatar'}"	cssClass="radio" /><label for="gravatar_avatarConfig_style" class="normal" ><s:text name="label.avatarConfig.style.gravatar"/></label></li>
			</ul>

		</fieldset> 
 
		<p class="centerText">
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" />
		</p>
		
	</s:form>
	
</div>