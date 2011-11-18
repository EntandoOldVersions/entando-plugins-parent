<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

*** SCEGLI IL TIPO DI PROFILO PER USERNAME <s:property value="username" /> ***

<br /><br /><br />

<s:form action="new">
<wpsf:hidden name="username" />

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

<p>
	TIPO PROFILO
	<wpsf:select useTabindexAutoIncrement="true" list="userProfileTypes" name="profileTypeCode" listKey="typeCode" listValue="typeDescr" cssClass="text" />
</p>

<p>
	<wpsf:submit useTabindexAutoIncrement="true" />
</p>


</s:form>