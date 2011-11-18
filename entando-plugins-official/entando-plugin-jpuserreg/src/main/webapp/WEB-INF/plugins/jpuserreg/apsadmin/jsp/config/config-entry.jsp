<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/aps-core" prefix="wp" %>

<h1><s:text name="jpuserreg.title.config" /></h1>
<div id="main">

<s:form action="save">
	<s:if test="hasFieldErrors()">
		<div class="message message_error">	
		<h2><s:text name="message.title.FieldErrors" /></h2>
		<ul>
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
		            <li><s:property/></li>
				</s:iterator>
			</s:iterator>
		</ul>
		</div>
	</s:if>

	<s:if test="hasActionErrors()">
		<div class="message message_error">	
		<h2><s:text name="message.title.ActionErrors" /></h2>
		<ul>
			<s:iterator value="actionErrors">
				<li><s:property/></li>
			</s:iterator>
		</ul>
		</div>
	</s:if>

	<fieldset>
		<legend><s:text name="label.info" /></legend>
		<p class="noscreen">
			<s:iterator var="auth" value="config.groups">
				<wpsf:hidden name="config.groups" value="%{#auth}" />
			</s:iterator>
			<s:iterator var="auth" value="config.roles">
				<wpsf:hidden name="config.roles" value="%{#auth}" />
			</s:iterator>
		</p>
		
		<p>
			<label for="tokenValidityMinutes" class="basic-mint-label" ><s:text name="jpuserreg.tokenValidityMinutes" />:</label>
			<wpsf:select useTabindexAutoIncrement="true" id="tokenValidityMinutes" name="config.tokenValidityMinutes" 
				list="#{ 
						1440: '1 '+getText('day'),
						2880: '2 '+getText('days'),
						4320: '3 '+getText('days'),
						5760: '4 '+getText('days'),
						7200: '5 '+getText('days'),
						8640: '6 '+getText('days'),
						10080: '7 '+getText('days'), 
						20160: '14 '+getText('days'),
						43200: '30 '+getText('days')
						}" cssClass="text" /> 
		</p>
		
		<p>
			<label for="eMailSenderCode" class="basic-mint-label"><s:text name="jpuserreg.eMailSenderCode" />:</label>
			<wpsf:select useTabindexAutoIncrement="true" name="config.eMailSenderCode" id="eMailSenderCode" list="mailSenders" listKey="key" listValue="value" ></wpsf:select>
		</p>
	</fieldset>

	<fieldset>
		<legend><s:text name="jpuserreg.groups" /></legend>
		<p><s:text name="jpuserreg.note.groupList" /></p>
		<s:if test="%{config.groups.size() > 0}">
			<s:set var="removeIcon"><wp:resourceURL />administration/common/img/icons/list-remove.png</s:set>
			<ul>
				<s:iterator var="auth" value="config.groups">
				<li> 
					<wpsa:actionParam action="removeGroup" var="removeGroupActionName" >
						<wpsa:actionSubParam name="groupName" value="%{#auth}" />
					</wpsa:actionParam>	
					<wpsf:submit useTabindexAutoIncrement="true" action="%{#removeGroupActionName}" type="image" src="%{#removeIcon}" value="%{getText('label.remove')}" title="%{getText('label.remove')}" />: <s:property value="#auth" />		
				</li>
				</s:iterator>
			</ul>
		</s:if>
		<p>
			<s:set var="addIcon"><wp:resourceURL />administration/common/img/icons/list-add.png</s:set>
			<label for="groupName" class="basic-mint-label"><s:text name="jpuserreg.label.group" />:</label>
			<wpsf:select useTabindexAutoIncrement="true" name="groupName" id="groupName" list="systemGroups" listKey="name" listValue="descr" cssClass="text" />
			<wpsf:submit useTabindexAutoIncrement="true" action="addGroup" type="image" src="%{#addIcon}" value="%{getText('label.add')}" title="%{getText('label.add')}" />
		</p>
	</fieldset>

	<fieldset>
		<legend><s:text name="jpuserreg.roles" /></legend>
		<p><s:text name="jpuserreg.note.roleList" /></p>
		<s:if test="%{config.roles.size > 0}">
		<ul>
			<s:iterator var="auth" value="config.roles">
			<li> 
				<wpsa:actionParam action="removeRole" var="removeRoleActionName" >
					<wpsa:actionSubParam name="roleName" value="%{#auth}" />
				</wpsa:actionParam>
				<wpsf:submit useTabindexAutoIncrement="true" action="%{#removeRoleActionName}" type="image" src="%{#removeIcon}" value="%{getText('label.remove')}" title="%{getText('label.remove')}" />: <s:property value="#auth" />
			</li>
			</s:iterator>
		</ul>
		</s:if>
		<p>
			<label for="roleName" class="basic-mint-label"><s:text name="jpuserreg.label.role" />:</label>
			<wpsf:select useTabindexAutoIncrement="true" name="roleName" id="roleName" list="systemRoles" listKey="name" listValue="description" cssClass="text" />
			<wpsf:submit useTabindexAutoIncrement="true" action="addRole" type="image" src="%{#addIcon}" value="%{getText('label.add')}" title="%{getText('label.add')}" />
		</p>
	</fieldset>
	
	<fieldset>
		<legend><s:text name="jpuserreg.activation" /></legend>

		<p>
			<s:property value="jpuserreg.parameters.summary" />
		</p>
		
		<dl class="table-display">
			<dt><s:text name="jpuserreg.activationPageCode" /></dt>
				<dd><s:property value="%{getText('jpuserreg.activationPageCode.help')}" escape="false" /></dd>
			<dt><s:text name="jpuserreg.label.subject" /></dt>
				<dd><s:property value='%{getText("jpuserreg.label.activation.subject.help")}' escape="false" /></dd>
			<dt><s:text name="jpuserreg.label.body" /></dt>
				<dd><s:property value='%{getText("jpuserreg.label.activation.body.help")}' escape="false"  /></dd> 
		</dl>

		<p>
			<label for="activationPageCode" class="basic-mint-label"><s:text name="jpuserreg.activationPageCode" />:</label>
			<wpsf:select useTabindexAutoIncrement="true"  name="config.activationPageCode" id="activationPageCode" list="%{pages}" listKey="code" listValue="titles[currentLang.code]" cssClass="text" />
		</p>
		
		<s:iterator var="lang" value="langs" > 
			<s:set var="template" value="%{config.activationTemplates.get(#lang.code)}" />
			<p>
				<label for="config_activationTemplates_subject_<s:property value="#lang.code"/>" class="basic-mint-label"><span class="monospace">(<s:property value="#lang.code"/>)</span>&#32;<s:text name="jpuserreg.label.subject" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" value="%{#template.subject}" name="config.activationTemplates['%{#lang.code}'].subject" 
						id="config_activationTemplates_subject_%{#lang.code}" cssClass="text"/>
			</p>
			<p>
				<label for="config_activationTemplates_body_<s:property value="#lang.code"/>" class="basic-mint-label"><span class="monospace">(<s:property value="#lang.code"/>)</span>&#32;<s:text name="jpuserreg.label.body" />:</label>
				<wpsf:textarea useTabindexAutoIncrement="true" value="%{#template.body}" name="config.activationTemplates['%{#lang.code}'].body" id="config_activationTemplates_body_%{#lang.code}" cssClass="text" rows="8" cols="50" />
			</p>
		</s:iterator>
	</fieldset>

	<fieldset>
		<legend><s:text name="jpuserreg.reactivation" /></legend>
		
		<p>
			<s:property value="jpuserreg.parameters.summary" />
		</p>
		
		<dl class="table-display">
			<dt><s:text name="jpuserreg.reactivationPageCode" /></dt>
				<dd><s:property value="%{getText('jpuserreg.reactivationPageCode.help')}" escape="false" /></dd>
			<dt><s:text name="jpuserreg.label.subject" /></dt>
				<dd><s:property value='%{getText("jpuserreg.label.reactivation.subject.help")}' escape="false" /></dd>
			<dt><s:text name="jpuserreg.label.body" /></dt>
				<dd><s:property value='%{getText("jpuserreg.label.reactivation.body.help")}' escape="false"  /></dd> 
		</dl>

		<p>
			<label for="reactivationPageCode" class="basic-mint-label"><s:text name="jpuserreg.reactivationPageCode" />:</label>
			<wpsf:select useTabindexAutoIncrement="true"  name="config.reactivationPageCode" id="reactivationPageCode" list="%{pages}" listKey="code" listValue="titles[currentLang.code]" cssClass="text" />
		</p>
		
		<s:iterator var="lang" value="langs">
			<s:set var="template" value="%{config.reactivationTemplates.get(#lang.code)}" />
			<p>
				<label for="config_reactivationTemplates_subject_<s:property value="#lang.code"/>" class="basic-mint-label"><span class="monospace">(<s:property value="#lang.code"/>)</span>&#32;<s:text name="jpuserreg.label.subject" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" value="%{#template.subject}" name="config.reactivationTemplates['%{#lang.code}'].subject" 
						id="config_reactivationTemplates_subject_%{#lang.code}" cssClass="text"/>
			</p>
			<p>
				<label for="config_reactivationTemplates_body_<s:property value="#lang.code"/>" class="basic-mint-label"><span class="monospace">(<s:property value="#lang.code"/>)</span>&#32;<s:text name="jpuserreg.label.body" />:</label>
				<wpsf:textarea useTabindexAutoIncrement="true" value="%{#template.body}" name="config.reactivationTemplates['%{#lang.code}'].body" 
						id="config_reactivationTemplates_body_%{#lang.code}" cssClass="text" rows="8" cols="50" />
			</p>
		</s:iterator>
	</fieldset>
	
	<p class="centerText">
		<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" />
	</p>
	
</s:form>

</div>