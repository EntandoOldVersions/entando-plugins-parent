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
	<h2><s:text name="title.workflowManagement.editRole" /></h2>
	<%-- 
	<dl>
		<dt>
			<s:text name="label.contentType" />
		</dt>
			<dd>
				<s:property value="contentType.descr"/>
			</dd>
		<dt>
			Legenda  
		</dt>
		<dd>
			<s:text name="label.mainRole" />
		</dd>
	</dl>
	--%>
	<p>
		<s:text name="note.workingOn" />:&#32;
		<s:text name="label.contentType" />&#32;
		<em><s:property value="contentType.descr"/></em>
	</p>
	
	<s:form action="saveRole" >
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
						<li><s:property escape="false"/></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		
		<fieldset class="margin-more-top">
			<legend><s:text name="label.info" /></legend>
			<p>
				<wpsf:hidden name="typeCode" />
				<label for="jpcontentworkflow_role" class="basic-mint-label"><s:text name="label.role" />:</label>
				<wpsf:select useTabindexAutoIncrement="true" name="role" id="jpcontentworkflow_role" headerKey="" headerValue="%{getText('label.none')}" list="roles" listKey="name" listValue="description" />
			</p>
		</fieldset> 
		<p class="centerText">
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" />
		</p>
	</s:form>
</div>