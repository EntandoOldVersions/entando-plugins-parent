<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1><a href="<s:url action="list" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.messageManagement" />"><s:text name="title.messageManagement" /></a></h1>
<div id="main2">
	<h2><s:text name="title.messageManagement.newAnswer" /></h2>
	<s:set var="id" value="message.id" />
	<s:set var="typeCode" value="message.typeCode" />
	<p><s:text name="title.messageManagement.newAnswer.info" />:&#32;<em><s:property value="message.typeDescr"/></em>&#32;(<s:property value="%{#id}"/>)</p>
	
	<s:form action="sendAnswer" enctype="multipart/form-data">
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
			
		<div>
			<s:include value="inc/include_messageDetails.jsp" />
		</div>
		
		<fieldset>
			<legend><s:text name="label.answer" /></legend>
			<p class="noscreen">
				<wpsf:hidden name="id" />
			</p>
			<p>
				<label for="text" class="basic-mint-label"><s:text name="text" />:</label>
				<wpsf:textarea useTabindexAutoIncrement="true" name="text" id="text" cssClass="text" cols="60" rows="20" />
			</p>
			
			<p>
				<label for="attachment" class="basic-mint-label"><s:text name="attachment" />:</label>
				<input type="file" id="attachment" name="attachment" value="%{getText('label.browse')}" />
			</p>
		</fieldset>

		<p class="centerText">
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.send')}" cssClass="button" />
		</p>

	</s:form>
</div>