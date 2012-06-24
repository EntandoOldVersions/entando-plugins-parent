<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<h1><s:text name="title.messageManagement" /></h1>
<div id="main">
	
	<s:form action="search" >
		<p>
			<label for="jpwebdynamicform_MSG_TYPE" class="basic-mint-label label-search"><s:text name="label.search.message.type" />:</label>
			<wpsf:select useTabindexAutoIncrement="true" name="entityTypeCode" id="entityTypeCode" list="entityPrototypes" listKey="typeCode" listValue="typeDescr" headerKey="" headerValue="%{getText('label.all')}" cssClass="text" disabled="!(null == entityTypeCode || entityTypeCode == '')" />
			<s:if test="!(null == entityTypeCode || entityTypeCode == '')">
				&#32;<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.change')}" cssClass="button" action="changeEntityType" />
			</s:if>
		</p>

		<fieldset>
			<legend class="accordion_toggler"><s:text name="search.basic" /></legend>
			<div class="accordion_element">

				<p>
					<label for="jpwebdynamicform_from_cal" class="basic-mint-label"><s:text name="label.from"/>:</label>
					<wpsf:textfield useTabindexAutoIncrement="true" name="from" id="jpwebdynamicform_from_cal" cssClass="text" />
				</p>

				<p>
					<label for="jpwebdynamicform_to_cal" class="basic-mint-label"><s:text name="label.to"/>:</label>
					<wpsf:textfield useTabindexAutoIncrement="true" name="to" id="jpwebdynamicform_to_cal" cssClass="text" />
				</p>

				<p> 
					<label for="jpwebdynamicform_status" class="basic-mint-label"><s:text name="label.status"/>:</label>
					<select name="answered" id="jpwebdynamicform_status" >
						<option value="" ><s:text name="label.all" /></option>
						<option value="0" <s:if test="%{answered==0}">selected="selected" </s:if>><s:text name="label.waiting" /></option>
						<option value="1" <s:if test="%{answered==1}">selected="selected" </s:if>><s:text name="label.answered" /></option>
					</select>
				</p>
	
			</div>
		</fieldset>
		<fieldset>
			<legend class="accordion_toggler"><s:text name="search.advanced" /></legend>
			<div class="accordion_element">
	
				<s:set var="searcheableAttributes" value="searcheableAttributes" />
				<s:if test="null != #searcheableAttributes && #searcheableAttributes.size() > 0">
	
					<s:iterator var="attribute" value="#searcheableAttributes">
						<s:set var="currentFieldId">entityFinding_<s:property value="#attribute.name" /></s:set>
						<s:if test="#attribute.textAttribute">
							<p>
								<label for="<s:property value="#currentFieldId" />" class="basic-mint-label"><s:property value="#attribute.name" />:</label>
								<s:set name="textInputFieldName"><s:property value="#attribute.name" />_textFieldName</s:set>
								<wpsf:textfield useTabindexAutoIncrement="true" id="%{#currentFieldId}" cssClass="text" name="%{#textInputFieldName}" value="%{getSearchFormFieldValue(#textInputFieldName)}" /><br />
							</p>
						</s:if>
	
						<s:elseif test="#attribute.type == 'Date'">
							<s:set name="dateStartInputFieldName" ><s:property value="#attribute.name" />_dateStartFieldName</s:set>
							<s:set name="dateEndInputFieldName" ><s:property value="#attribute.name" />_dateEndFieldName</s:set>
							<p>
								<label for="<s:property value="#currentFieldId" />_start_cal"><s:property value="#attribute.name" /> **valore iniziale**</label>:<br />
								<wpsf:textfield useTabindexAutoIncrement="true" id="%{#currentFieldId}_dateStartFieldName_cal" cssClass="text" name="%{#dateStartInputFieldName}" value="%{getSearchFormFieldValue(#dateStartInputFieldName)}" /><span class="inlineNote">dd/MM/yyyy</span>
							</p>
							<p>
								<label for="<s:property value="#currentFieldId" />_end_cal"><s:property value="#attribute.name" /> **valore finale**</label>:<br />
								<wpsf:textfield useTabindexAutoIncrement="true" id="%{#currentFieldId}_end_cal" cssClass="text" name="%{#dateEndInputFieldName}" value="%{getSearchFormFieldValue(#dateEndInputFieldName)}" /><span class="inlineNote">dd/MM/yyyy</span>
							</p>
						</s:elseif>
	
						<s:elseif test="#attribute.type == 'Number'">
							<s:set name="numberStartInputFieldName" ><s:property value="#attribute.name" />_numberStartFieldName</s:set>
							<s:set name="numberEndInputFieldName" ><s:property value="#attribute.name" />_numberEndFieldName</s:set>
							<p>
								<label for="<s:property value="#currentFieldId" />_start"><s:property value="#attribute.name" /> **inizio**</label>:<br />
								<wpsf:textfield useTabindexAutoIncrement="true" id="%{#currentFieldId}_start" cssClass="text" name="%{#numberStartInputFieldName}" value="%{getSearchFormFieldValue(#numberStartInputFieldName)}" /><br />
							</p>
							<p>
								<label for="<s:property value="#currentFieldId" />_end"><s:property value="#attribute.name" /> **fine**</label>:<br />
								<wpsf:textfield useTabindexAutoIncrement="true" id="%{#currentFieldId}_end" cssClass="text" name="%{#numberEndInputFieldName}" value="%{getSearchFormFieldValue(#numberEndInputFieldName)}" /><br />
							</p>
						</s:elseif>
	
						<s:elseif test="#attribute.type == 'Boolean'">
							<p>
								<label for="<s:property value="#currentFieldId"/>" ><s:property value="#attribute.name" /></label>&nbsp;
								<wpsf:checkbox useTabindexAutoIncrement="true" name="%{#attribute.name}" id="%{#currentFieldId}" />
							</p>
						</s:elseif>
	
						<s:elseif test="#attribute.type == 'ThreeState'">
							<p>
								<span class="important"><s:property value="#attribute.name" /></span><br />
							</p>
							<ul class="noBullet">
								<li><wpsf:radio useTabindexAutoIncrement="true" name="%{#attribute.name}" id="none_%{#currentFieldId}" value="" checked="%{#attribute.booleanValue == null}" cssClass="radio" /><label for="none_<s:property value="%{#currentFieldId}" />" class="normal" ><s:text name="label.bothYesAndNo"/></label></li>
								<li><wpsf:radio useTabindexAutoIncrement="true" name="%{#attribute.name}" id="true_%{#currentFieldId}" value="true" checked="%{#attribute.booleanValue != null && #attribute.booleanValue == true}" cssClass="radio" /><label for="true_<s:property value="%{#currentFieldId}" />" class="normal" ><s:text name="label.yes"/></label></li>
								<li><wpsf:radio useTabindexAutoIncrement="true" name="%{#attribute.name}" id="false_%{#currentFieldId}" value="false" checked="%{#attribute.booleanValue != null && #attribute.booleanValue == false}" cssClass="radio" /><label for="false_<s:property value="%{#currentFieldId}" />" class="normal"><s:text name="label.no"/></label></li>
							</ul>
						</s:elseif>
					</s:iterator>
				</s:if>
				<s:else>
					<p><s:text name="search.advanced.choose.type" /></p>
				</s:else>
			</div>
		</fieldset>
		<p>
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.search')}" cssClass="button" />
		</p>
	</s:form>
	
	<div class="subsection-light">
		<s:set var="entityIds" value="searchResult" />
		<s:if test="%{#entityIds.size()==0}">
			<p><s:text name="note.message.list.none" /></p>
		</s:if>
		<s:else>
			<s:form action="search" >
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
					<wpsf:hidden name="entityTypeCode" />
					<wpsf:hidden name="from" />
					<wpsf:hidden name="to" />
					<wpsf:hidden name="answered"/>
					<s:iterator var="attribute" value="#searcheableAttributes">
						<s:if test="#attribute.textAttribute">
								<s:set name="textInputFieldName" ><s:property value="#attribute.name" />_textFieldName</s:set>
								<wpsf:hidden name="%{#textInputFieldName}" value="%{getSearchFormFieldValue(#textInputFieldName)}" />
						</s:if>
						<s:elseif test="#attribute.type == 'Date'">
								<s:set name="dateStartInputFieldName" ><s:property value="#attribute.name" />_dateStartFieldName</s:set>
								<s:set name="dateEndInputFieldName" ><s:property value="#attribute.name" />_dateEndFieldName</s:set>
								<wpsf:hidden name="%{#dateStartInputFieldName}" value="%{getSearchFormFieldValue(#dateStartInputFieldName)}" />
								<wpsf:hidden name="%{#dateEndInputFieldName}" value="%{getSearchFormFieldValue(#dateEndInputFieldName)}" />
						</s:elseif>
						<s:elseif test="#attribute.type == 'Number'">
								<s:set name="numberStartInputFieldName" ><s:property value="#attribute.name" />_numberStartFieldName</s:set>
								<s:set name="numberEndInputFieldName" ><s:property value="#attribute.name" />_numberEndFieldName</s:set>
								<wpsf:hidden name="%{#numberStartInputFieldName}" value="%{getSearchFormFieldValue(#numberStartInputFieldName)}" />
								<wpsf:hidden name="%{#numberEndInputFieldName}" value="%{getSearchFormFieldValue(#numberEndInputFieldName)}" />
						</s:elseif>
						<s:elseif test="#attribute.type == 'Boolean'">
							<%-- DA FARE --%>
						</s:elseif>
						<s:elseif test="#attribute.type == 'ThreeState'">
							<%-- DA FARE --%>
						</s:elseif>
					</s:iterator>
				</p>
				
				<wpsa:subset source="#entityIds" count="15" objectName="entityGroup" advanced="true" offset="5">
					<s:set name="group" value="#entityGroup" />
					
					<div class="pager">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>
					
					<table class="generic" id="messageListTable" summary="<s:text name="note.message.list.summary" />">
						<caption><span><s:text name="title.message.list" /></span></caption>
						<tr>
							<th><s:text name="label.request" /></th>
							<th><s:text name="label.creationDate" /></th>
							<th class="icon"><s:text name="label.status"/></th>
							<th><abbr title="<s:text name="label.operation" />">&ndash;</abbr></th>
						</tr>
						<s:iterator var="messageId">
						<s:set name="message" value="%{getMessage(#messageId)}" />
						<s:set name="answers" value="%{getAnswers(#messageId)}" />
						<tr>
							<td>
								<a href="<s:url action="view"><s:param name="id" value="#message.id"></s:param></s:url>" title="<s:text name="label.view"/>&#32;<s:property value="#message.id"/>">
								<s:property value="#message.id"/>&#32;&ndash;&#32;<s:property value="#message.typeDescr"/>
								</a>
							</td>
							<td class="centerText monospace"><s:date name="#message.creationDate" format="dd/MM/yyyy HH:ss"/></td>
							<s:if test="%{#answers.size()>0}">
								<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/content-isonline.png</s:set>
								<s:set name="thereIsAnswer" value="%{getText('label.answered')}" />
							</s:if>
							<s:else>
								<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/content-isnotonline.png</s:set>
								<s:set name="thereIsAnswer" value="%{getText('label.waiting')}" />
							</s:else>
							<td class="centerText"><img src="<s:property value="iconImagePath" />" alt="<s:property value="thereIsAnswer" />" title="<s:property value="thereIsAnswer" />" /></td>
							<td class="icon_double">
							<a href="<s:url action="newAnswer"><s:param name="id" value="#message.id"/></s:url>" title="<s:text name="label.newAnswer.at" />: <s:property value="#message.id" />"><img src="<wp:resourceURL />plugins/jpwebdynamicform/administration/img/icons/mail-reply-sender.png" alt="<s:text name="label.newAnswer" />" /></a><span class="noscreen">|</span><a href="<s:url action="trash"><s:param name="id" value="#message.id"/></s:url>" title="<s:text name="label.remove" />: <s:property value="#message.id" />"><img src="<wp:resourceURL />administration/common/img/icons/delete.png" alt="<s:text name="label.remove" />" /></a></td>
						</tr>
					</s:iterator>
					</table>
					
					<div class="pager">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>
					
				</wpsa:subset>
			</s:form>
		</s:else>
		
	</div>
	
	
</div>