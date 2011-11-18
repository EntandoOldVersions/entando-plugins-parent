<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1><s:text name="title.workflowManagement" /></h1>
<div id="main">
	<h2><s:text name="title.workflowNotifierManagement.config" /></h2> 
  
	<s:form action="save" >
		<s:if test="hasFieldErrors()">
			<div class="message message_error">	
				<h3><s:text name="message.title.FieldErrors" /></h3>
				<ul>
					<s:iterator value="fieldErrors">
						<s:iterator value="value"><li><s:property escape="false" /></li></s:iterator>
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
			<legend><s:text name="notifier.generalSettings" /></legend>
			<p>
				<wpsf:checkbox useTabindexAutoIncrement="true" name="config.active" id="active" cssClass="radiocheck" />&nbsp;
				<label for="active"><s:text name="label.active" /></label>
			</p>
		</fieldset>
		
		<fieldset>
			<legend><s:text name="label.schedulerSettings" /></legend>
			<p>
				<label for="hoursDelay" class="basic-mint-label"><s:text name="label.hoursDelay" />:</label>
				<s:set name="hoursDelayVar" value="%{hoursDelay}" scope="page" />
				<select name="config.hoursDelay" id="hoursDelay">
					<c:forEach begin="1" end="10" varStatus="status">
						<option <c:if test="${(status.count*24) == hoursDelayVar}">selected="selected"</c:if> 
							value="<c:out value="${status.count*24}" />" ><c:out value="${status.count*24}" /></option>
					</c:forEach>
				</select>
			</p>
			<p>
				<label for="jpcontentworkflownotifier_date_cal" class="basic-mint-label"><s:text name="label.startDate" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="startDate" id="jpcontentworkflownotifier_date_cal" cssClass="text" />&nbsp;<span class="monospace"><s:text name="label.startDatePattern" /></span>
			</p>
			<p>
				<%--
				<label for="hour"><s:text name="label.hour" />:</label>
				 --%>
				<span class="bold"><s:text name="notifier.time"/></span>&nbsp;
				<wpsf:select useTabindexAutoIncrement="true" list="%{getCounterArray(0, 24)}" name="hour" id="hour"/>:
				<wpsf:select useTabindexAutoIncrement="true" list="%{getCounterArray(0, 60)}" name="minute" id="minute"/>
			</p>
		</fieldset>
		
		<fieldset>
			<legend><s:text name="label.mailSettings" /></legend>
			<p>
				<label for="senderCode" class="basic-mint-label"><s:text name="label.senderCode"/>:</label>
				<wpsf:select useTabindexAutoIncrement="true" name="config.senderCode" id="senderCode" list="senderCodes" />
			</p>
			<p>
				<label for="mailAttrName" class="basic-mint-label"><s:text name="label.mailAttrName"/>:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.mailAttrName" id="mailAttrName" cssClass="text" />
			</p>
			<p>
				<wpsf:checkbox useTabindexAutoIncrement="true" name="config.html" id="html" cssClass="radiocheck" />&nbsp; 
				<label for="html"><s:text name="label.html" /></label>
			</p>
			<p>
				<label for="subject" class="basic-mint-label"><s:text name="label.subject" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="config.subject" id="subject" cssClass="text"/>
			</p>
			<p>
				<label for="jpcontentworkflow_header" class="basic-mint-label"><s:text name="label.header" />:</label>
				<wpsf:textarea useTabindexAutoIncrement="true" name="config.header" id="jpcontentworkflow_header" cols="50" rows="10" cssClass="text"/> 
			</p>
			<p>
				<label for="jpcontentworkflow_template" class="basic-mint-label"><s:text name="label.template" />:</label>
				<wpsf:textarea useTabindexAutoIncrement="true" name="config.template" id="jpcontentworkflow_template" cols="50" rows="10" cssClass="text"/>
			</p>
			<p>
				<label for="jpcontentworkflow_footer" class="basic-mint-label"><s:text name="label.footer" />:</label>
				<wpsf:textarea useTabindexAutoIncrement="true" name="config.footer" id="jpcontentworkflow_footer" cols="50" rows="10" cssClass="text"/>
			</p>
		</fieldset>
		<p class="centerText">
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" />
		</p>
		
	</s:form>
</div>