<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<h1><s:text name="title.jpcalendar.configManagement" /></h1>

<div id="main">

<s:form action="save" namespace="/do/jpcalendar/Config">

<s:if test="hasFieldErrors()">
<div class="message message_error">
<h4><s:text name="message.title.FieldErrors" /></h4>	
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
<h2><s:text name="messages.confirm" /></h2>	
<ul>
	<s:iterator value="actionMessages">
		<li><s:property escape="false" /></li>
	</s:iterator>
</ul>
</div>
</s:if>

<s:if test="null == contentType">
<fieldset><legend><s:text name="title.contentInfo" /></legend>
<p>
	<label for="contentType" class="basic-mint-label"><s:text name="label.type"/>:</label>
	<wpsf:select useTabindexAutoIncrement="true" name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" cssClass="text" />
	<wpsf:submit useTabindexAutoIncrement="true" action="configContentType" value="%{getText('label.continue')}" cssClass="button" />	
</p>
</fieldset>
</s:if>
<s:else>

<fieldset class="margin-bit-bottom"><legend><s:text name="title.contentInfo" /></legend>
<p>
	<label for="contentType" class="basic-mint-label"><s:text name="label.type"/>:</label>
	<wpsf:select useTabindexAutoIncrement="true" name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" disabled="true" cssClass="text" />
	<wpsf:submit useTabindexAutoIncrement="true" action="changeContentType" value="%{getText('label.change')}" cssClass="button" />	
</p>
<p class="noscreen">
	<wpsf:hidden name="contentType" />
</p>

</fieldset>

<fieldset><legend><s:text name="title.attributes" /></legend>
<p>
	<label for="startDateAttributeName" class="basic-mint-label"><s:text name="label.startDateAttribute"/>:</label>
	<wpsf:select useTabindexAutoIncrement="true" name="startDateAttributeName" id="startDateAttributeName" 
				 list="allowedDateAttributes" listKey="key" listValue="value" cssClass="text" />
</p>
<p>
	<label for="endDateAttributeName" class="basic-mint-label"><s:text name="label.endDateAttribute"/>:</label>
	<wpsf:select useTabindexAutoIncrement="true" name="endDateAttributeName" id="endDateAttributeName" 
				 list="allowedDateAttributes" listKey="key" listValue="value" cssClass="text" />
</p>
</fieldset>

<p class="centerText"><wpsf:submit useTabindexAutoIncrement="true" action="save" value="%{getText('label.save')}" cssClass="button" /></p>

</s:else>

</s:form>

</div>