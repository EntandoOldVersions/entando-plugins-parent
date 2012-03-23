<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1><s:text name="title.workflowManagement" /><%-- anchor print --%>
	<a href="<s:url action="list" />" title="<s:text name="label.list" />">
		<img src="<wp:resourceURL/>administration/common/img/icons/32x32/general-list.png" alt="<s:text name="label.list" />" />
	</a>
</h1> 
<div id="main">
	<h2><s:text name="title.workflowManagement.editSteps" /></h2>
	<p>
		<s:text name="note.workingOn" />:&#32;
		<s:text name="label.contentType" />&#32;
		<em><s:property value="contentType.descr"/></em>
	</p>
	<s:form action="saveSteps" >
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
				<wpsf:hidden name="typeCode" />
				<wpsf:hidden name="stepCodes" />
			</p>
			<s:if test="%{steps==null || steps.size()==0}">
				<p><s:text name="note.stepList.empty" /></p>
			</s:if>
			<s:else>
				<table class="generic">
					<caption>
						<span><s:text name="note.stepList.caption" /></span> 
					</caption>
					<tr>
						<th><s:text name="label.code" /></th>
						<th><s:text name="label.descr" /></th>
						<th><s:text name="label.role" /></th>
						<th><s:text name="label.actions" /></th> 
					</tr>
					<s:iterator var="step" value="steps" status="rowstatus" >
						<tr>
							<td class="icon monospace"><s:property value="#step.code"/></td>
							<td>
								<wpsf:textfield useTabindexAutoIncrement="true" name="%{code + '_SEP_descr'}" value="%{#step.descr}" cssClass="text" />
							</td>
							<td> 
								<wpsf:select useTabindexAutoIncrement="true" name="%{code + '_SEP_role'}" headerKey="" headerValue="%{getText('label.select')}" list="roles" listKey="name" listValue="description" value="%{#step.role}"/>
							</td>
							<td class="centerText iconDouble"> 
								<s:set name="iconImagePath" var="iconImagePath"><wp:resourceURL/>administration/common/img/icons/go-up.png</s:set>
								<wpsa:actionParam action="moveStep" var="actionName" >
									<wpsa:actionSubParam name="movement" value="UP" />
									<wpsa:actionSubParam name="elementIndex" value="%{#rowstatus.index}" />
								</wpsa:actionParam>
								<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image" src="%{#iconImagePath}" 
										value="%{getText('label.moveUp')}" title="%{getText('label.moveUp')+': '+#step.code}" />
							
								<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/go-down.png</s:set>
								<wpsa:actionParam action="moveStep" var="actionName" >
									<wpsa:actionSubParam name="movement" value="DOWN" />
									<wpsa:actionSubParam name="elementIndex" value="%{#rowstatus.index}" />
								</wpsa:actionParam>
								<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image" src="%{#iconImagePath}" 
										value="%{getText('label.moveDown')}" title="%{getText('label.moveDown')+': '+#step.code}"/>
						 	
								<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/delete.png</s:set>
								<wpsa:actionParam action="removeStep" var="actionName" >
									<wpsa:actionSubParam name="stepCode" value="%{#step.code}" />
								</wpsa:actionParam>
								<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image" src="%{#iconImagePath}" 
										value="%{getText('label.remove')}" title="%{getText('label.remove')+': '+#step.code}" />
							</td>
						</tr>
					</s:iterator>
				</table>
			</s:else>
			<div class="subsection-light">
			<fieldset>
				<legend><s:text name="title.newStep" /></legend>
				<p> 
					<label for="jpcontentworkflow_stepCode" class="basic-mint-label"><s:text name="label.code" />:</label>
					<wpsf:textfield useTabindexAutoIncrement="true" name="stepCode" id="jpcontentworkflow_stepCode" cssClass="text" />
				</p>
				<p>
					<label for="jpcontentworkflow_stepDescr" class="basic-mint-label"><s:text name="label.descr" />:</label>
					<wpsf:textfield useTabindexAutoIncrement="true" name="stepDescr" id="jpcontentworkflow_stepDescr" cssClass="text" />
				</p>
				<p>  
					<label for="jpcontentworkflow_stepRole" class="basic-mint-label"><s:text name="label.role" />:</label>
					<wpsf:select useTabindexAutoIncrement="true" id="jpcontentworkflow_stepRole" name="stepRole" headerKey="" headerValue="" 
							list="roles" listKey="name" listValue="description" />
				</p>
				<p>
					<wpsa:actionParam action="addStep" var="actionName" />
					<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" value="%{getText('label.addStep')}" title="%{getText('label.addStep')}" cssClass="button" />
				</p>
			</fieldset>
			</div>
		<div class="subsection-light">
			<p class="centerText">
				<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" />
			</p>
		</div>
	</s:form>
</div>