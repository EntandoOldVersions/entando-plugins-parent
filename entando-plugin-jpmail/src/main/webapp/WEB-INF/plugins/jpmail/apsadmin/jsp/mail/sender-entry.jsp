<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1><s:text name="title.eMailManagement" /></h1>
<div id="main">
	
	<h2 class="margin-bit-bottom">
		<s:if test="%{strutsAction==1}" ><s:text name="title.eMailManagement.newSender" /></s:if>
		<s:else><s:text name="title.eMailManagement.editSender" />:&nbsp;<s:property value="currentCode"/></s:else>
	</h2>
	
	<s:form action="saveSender" >
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
		
		<p class="noscreen">	
			<wpsf:hidden name="strutsAction"/>
			<s:if test="%{strutsAction==2}" ><wpsf:hidden name="code"/></s:if>
		</p>
		
		<fieldset class="margin-more-top"> 
			<legend><s:text name="label.info" /></legend> 
			<p>
				<label for="code" class="basic-mint-label"><s:text name="code" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="code" id="code" disabled="%{strutsAction==2}" cssClass="text" />
			</p>
			<p>
				<label for="mail" class="basic-mint-label"><s:text name="mail" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="mail" id="mail" cssClass="text" />
			</p>
		</fieldset>
		
		<p class="centerText">
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" />
		</p>
		
	</s:form>	
</div>