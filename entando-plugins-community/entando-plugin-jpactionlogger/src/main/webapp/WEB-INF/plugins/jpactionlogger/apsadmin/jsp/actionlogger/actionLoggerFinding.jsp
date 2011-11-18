<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<h1><s:text name="title.actionLogger.management" /></h1>
<div id="main">
	<s:form action="list">
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
		<p>
			<label for="jpactionlogger_dateStart_cal" class="basic-mint-label label-search"><s:text name="start" />:</label>
			<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" id="jpactionlogger_dateStart_cal" name="start" /><span class="inlineNote">dd/MM/yyyy</span>
		</p>
	
		<fieldset>
			<legend class="accordion_toggler"><s:text name="title.searchFilters" /></legend>
			<div class="accordion_element">
				<p>
					<label for="jpactionlogger_dateEnd_cal" class="basic-mint-label"><s:text name="end" />:</label>
					<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" id="jpactionlogger_dateEnd_cal" name="end" /><span class="inlineNote">dd/MM/yyyy</span>
				</p>
				<p>
					<label for="jpactionlogger_username" class="basic-mint-label"><s:text name="username" /></label>
					<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" id="jpactionlogger_username" name="username" />
				</p>
			
				<p>
					<label for="jpactionlogger_namespace" class="basic-mint-label"><s:text name="namespace" /></label>
					<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" id="jpactionlogger_namespace" name="namespace" />
				</p>
				<p>
					<label for="jpactionlogger_actionName" class="basic-mint-label"><s:text name="actionName" /></label>
					<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" id="jpactionlogger_actionName" name="actionName" />
				</p>
				<p>
					<label for="jpactionlogger_params" class="basic-mint-label"><s:text name="params" /></label>
					<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" id="jpactionlogger_params" name="params" />
				</p>
			</div>
		</fieldset>
		<p>
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.search')}" cssClass="button" />
		</p>
		</s:form>
		<s:form action="search">	
			<div class="subsection-light">
				<p class="noscreen">
					<s:hidden name="username" />
					<s:hidden name="namespace" />
					<s:hidden name="actionName" />
					<s:hidden name="params" />
					<s:hidden name="start" />
					<s:hidden name="end" />
				</p>
			
				<wpsa:subset source="actionRecords" count="10" objectName="groupActions" advanced="true" offset="5">
					<s:set name="group" value="#groupActions" />
					
					<div class="pager">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>
				
					<table class="generic">
						<caption><span><s:text name="actionLogger.list" /></span></caption>
						<tr>
							<th scope="col"><s:text name="actiondate" /></th>
							<th scope="col"><s:text name="username" /></th>
							<th scope="col"><s:text name="namespace" /></th>
							<th scope="col"><s:text name="actionName" /></th>
							<th scope="col"><s:text name="params" /></th>
							<th scope="col"><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th>
						</tr>
						<s:iterator var="id">
							<tr>
								<td class="monospace centerText">
									<s:set name="logRecord" value="%{getActionRecord(#id)}" />
									<s:date name="#logRecord.actionDate" format="dd/MM/yyyy HH:mm:ss" />
								</td>
								<td class="monospace"><s:property value="#logRecord.username"/></td>
								<td><s:property value="#logRecord.namespace"/></td>
								<td><s:property value="#logRecord.actionName"/></td>
								<td><s:property value="#logRecord.params"/></td>
								<td class="icon"><a href="<s:url action="delete"><s:param name="id" value="#logRecord.id"></s:param></s:url>" title="<s:text name="label.remove" />: <s:date name="#logRecord.actionDate" format="dd/MM/yyyy HH:mm:ss" />"><img src="<wp:resourceURL />administration/common/img/icons/delete.png" alt="<s:text name="label.alt.clear" />" /></a></td>
							</tr>
						</s:iterator>
					</table>
				
					<div class="pager">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>
				
				</wpsa:subset>
			</div>
	</s:form>
</div>