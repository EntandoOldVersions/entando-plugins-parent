<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<s:set var="targetNS" value="%{'/do/jpaddressbook/AddressBook'}" />
<h1><s:text name="title.contactManagement" /><jsp:include page="/WEB-INF/apsadmin/jsp/common/inc/operations-context-general.jsp" /></h1>
<div id="main">
	<s:if test="hasFieldErrors()">
		<div class="message message_error">
			<h2><s:text name="message.title.FieldErrors" /></h2>
			<ul>
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
						<li><s:property escape="false" /></li>
					</s:iterator>
				</s:iterator>
			</ul>
		</div>
	</s:if>
 
	<s:form action="search">  
		<fieldset class="margin-bit-top">
			<legend class="accordion_toggler"><s:text name="jpaddressbook.label.search" /></legend>  
			
			<div class="accordion_element">
				
				<s:set var="searcheableAttributes" value="searcheableAttributes" ></s:set>
				<s:iterator var="attribute" value="#searcheableAttributes">
					<s:set var="currentFieldId">jpaddressbook_contactFinding_<s:property value="#attribute.name" /></s:set> 
			
					<s:if test="#attribute.textAttribute"> 
						<p>
							<label for="<s:property value="currentFieldId" />" class="basic-mint-label"><s:property value="#attribute.name" />:</label>
							<s:set name="textInputFieldName"><s:property value="#attribute.name" />_textFieldName</s:set>
							<wpsf:textfield useTabindexAutoIncrement="true" id="%{currentFieldId}" cssClass="text" name="%{#textInputFieldName}" value="%{getSerchFormValue(#textInputFieldName)}" /><br />
						</p>
					</s:if>
						
					<s:elseif test="#attribute.type == 'Date'">
						<s:set name="dateStartInputFieldName" ><s:property value="#attribute.name" />_dateStartFieldName</s:set>
						<s:set name="dateEndInputFieldName" ><s:property value="#attribute.name" />_dateEndFieldName</s:set>
						<p>
							<label for="<s:property value="currentFieldId" />_start_cal" class="basic-mint-label"><s:property value="#attribute.name" />&#32;<s:text name="jpaddressbook.label.search.dateStartvalue" />:</label>
							<wpsf:textfield useTabindexAutoIncrement="true" id="%{currentFieldId}_start_cal" cssClass="text" name="%{#dateStartInputFieldName}" value="%{getSerchFormValue(#dateStartInputFieldName)}" /><span class="inlineNote">dd/MM/yyyy</span>
						</p>      
						<p>
							<label for="<s:property value="currentFieldId" />_end_cal" class="basic-mint-label"><s:property value="#attribute.name" />&#32;<s:text name="jpaddressbook.label.search.dateEndvalue" />:</label>
							<wpsf:textfield useTabindexAutoIncrement="true" id="%{currentFieldId}_end_cal" cssClass="text" name="%{#dateEndInputFieldName}" value="%{getSerchFormValue(#dateEndInputFieldName)}" /><span class="inlineNote">dd/MM/yyyy</span>
						</p>
					</s:elseif> 
						
					<s:elseif test="#attribute.type == 'Number'">
						<s:set name="numberStartInputFieldName" ><s:property value="#attribute.name" />_numberStartFieldName</s:set>
						<s:set name="numberEndInputFieldName" ><s:property value="#attribute.name" />_numberEndFieldName</s:set>
						<p>
							<label for="<s:property value="currentFieldId" />_start" class="basic-mint-label"><s:property value="#attribute.name" />&#32;<s:text name="jpaddressbook.label.search.numberStartvalue" />:</label>
							<wpsf:textfield useTabindexAutoIncrement="true" id="%{currentFieldId}_start" cssClass="text" name="%{#numberStartInputFieldName}" value="%{getSerchFormValue(#numberStartInputFieldName)}" /><br />
						</p>
						<p> 
							<label for="<s:property value="currentFieldId" />_end" class="basic-mint-label"><s:property value="#attribute.name" />&#32;<s:text name="jpaddressbook.label.search.numberEndvalue" />:</label>
							<wpsf:textfield useTabindexAutoIncrement="true" id="%{currentFieldId}_end" cssClass="text" name="%{#numberEndInputFieldName}" value="%{getSerchFormValue(#numberEndInputFieldName)}" /><br />
						</p> 
					</s:elseif>
						
					<s:elseif test="#attribute.type == 'Boolean'"> 
						<p>
							<wpsf:checkbox useTabindexAutoIncrement="true" name="%{#attribute.name}" id="%{currentFieldId}"  cssClass="radiocheck"/>&nbsp;<label for="<s:property value="currentFieldId"/>" ><s:property value="#attribute.name" /></label>
						</p>
					</s:elseif> 
						
					<s:elseif test="#attribute.type == 'ThreeState'">
						<p>
							<span class="important"><s:property value="#attribute.name" /></span><br />
						</p>
						<ul class="noBullet">
							<li><wpsf:radio useTabindexAutoIncrement="true" name="%{#attribute.name}" id="none_%{currentFieldId}" value="" checked="%{#attribute.booleanValue == null}" cssClass="radiocheck" /><label for="none_<s:property value="%{currentFieldId}" />" class="normal" ><s:text name="label.bothYesAndNo"/></label></li>
							<li><wpsf:radio useTabindexAutoIncrement="true" name="%{#attribute.name}" id="true_%{currentFieldId}" value="true" checked="%{#attribute.booleanValue != null && #attribute.booleanValue == true}" cssClass="radiocheck" /><label for="true_<s:property value="%{currentFieldId}" />" class="normal" ><s:text name="label.yes"/></label></li>
							<li><wpsf:radio useTabindexAutoIncrement="true" name="%{#attribute.name}" id="false_%{currentFieldId}" value="false" checked="%{#attribute.booleanValue != null && #attribute.booleanValue == false}" cssClass="radiocheck" /><label for="false_<s:property value="%{currentFieldId}" />" class="normal"><s:text name="label.no"/></label></li>
						</ul>
					</s:elseif>
				</s:iterator>
			</div>
		</fieldset>

		<p><wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.search')}" cssClass="button" action="search" /></p>

	</s:form>
	
	<div class="subsection-light">
		<s:form action="search">
			<p class="noscreen">
				<s:iterator var="attribute" value="#searcheableAttributes">
					<s:if test="#attribute.textAttribute">
							<s:set name="textInputFieldName" ><s:property value="#attribute.name" />_textFieldName</s:set>
							<wpsf:hidden name="%{#textInputFieldName}" value="%{getSerchFormValue(#textInputFieldName)}" />
					</s:if>
					<s:elseif test="#attribute.type == 'Date'">
							<s:set name="dateStartInputFieldName" ><s:property value="#attribute.name" />_dateStartFieldName</s:set>
							<s:set name="dateEndInputFieldName" ><s:property value="#attribute.name" />_dateEndFieldName</s:set>
							<wpsf:hidden name="%{#dateStartInputFieldName}" value="%{getSerchFormValue(#dateStartInputFieldName)}" />
							<wpsf:hidden name="%{#dateEndInputFieldName}" value="%{getSerchFormValue(#dateEndInputFieldName)}" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Number'">
							<s:set name="numberStartInputFieldName" ><s:property value="#attribute.name" />_numberStartFieldName</s:set>
							<s:set name="numberEndInputFieldName" ><s:property value="#attribute.name" />_numberEndFieldName</s:set>
							<wpsf:hidden name="%{#numberStartInputFieldName}" value="%{getSerchFormValue(#numberStartInputFieldName)}" />
							<wpsf:hidden name="%{#numberEndInputFieldName}" value="%{getSerchFormValue(#numberEndInputFieldName)}" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Boolean'">
						<%-- DA FARE --%>
					</s:elseif>
					<s:elseif test="#attribute.type == 'ThreeState'">
						<%-- DA FARE --%>
					</s:elseif>
				</s:iterator>
			</p>
				
			<s:set var="contactIds" value="searchResult" />
			<%-- <s:if test="#contactIds.isEmpty()">no risultati </s:if> --%>
			<wpsa:subset source="#contactIds" count="15" objectName="groupContact" advanced="true" offset="5"> 
				<s:set name="group" value="#groupContact" />
				
				<div class="pager">
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
				</div>
				
				<s:set value="userProfilePrototype" var="userProfilePrototype" ></s:set>
	
				<table class="generic" summary="<s:text name="contacts.list.summary" />">
					<caption><span><s:text name="contacts.list" /></span></caption>
					<tr>
						<%-- <th>ID</th> --%>
						<s:if test="#userProfilePrototype.surnameAttributeName != null"><th><s:text name="jpaddressbook.label.surnameAttribute" /></th></s:if>
						<s:if test="#userProfilePrototype.firstNameAttributeName != null"><th><s:text name="jpaddressbook.label.nameAttribute" /></th></s:if>
						<s:if test="#userProfilePrototype.mailAttributeName != null"><th><s:text name="jpaddressbook.label.mailAttribute" /></th></s:if>
						<th><abbr title="<s:text name="jpaddressbook.label.ownerAttributeFull" />"><s:text name="jpaddressbook.label.ownerAttributeShort" /></abbr></th>
						<th class="icon"><abbr title="<s:text name="jpaddressbook.download.vcard" />"><acronym title="Versitcard"><s:text name="jpaddressbook.name.vcard" /></acronym></abbr></th>
						<th class="icon"><abbr title="<s:text name="jpaddressbook.label.openDetailsFull" />"><s:text name="jpaddressbook.label.openDetailsShort" /></abbr></th>
						<th class="icon"><abbr title="<s:text name="jpaddressbook.label.deleteActionFull" />">&ndash;</abbr></th>
					</tr>
					
					<s:iterator var="contactId">
						<s:set var="contact" value="%{getContact(#contactId)}" />
						<tr>
							<%-- <td><s:property value="#contactId" /></td> --%>
							<%-- EVENTUALI COLONNE CONFIGURABILI IN BASE AL MODELLO DEL PROFILO --%>
							
							<s:if test="#userProfilePrototype.surnameAttributeName != null">
								<td> 
									<a title="<s:text name="jpaddressbook.label.editContact" />:&#32;<s:property value="#contact.getValue(#userProfilePrototype.surnameAttributeName)"/>&#32;<s:property value="#contact.getValue(#userProfilePrototype.firstNameAttributeName)"/>" href="<s:url action="edit" ><s:param name="entityId" value="#contactId"></s:param></s:url>" tabindex="<wpsa:counter />">
										<s:property value="#contact.getValue(#userProfilePrototype.surnameAttributeName)"/>
									</a>
								</td> 
							</s:if>
							<s:if test="#userProfilePrototype.firstNameAttributeName != null">
								<td>
									<a title="<s:text name="jpaddressbook.label.editContact" />:&#32;<s:property value="#contact.getValue(#userProfilePrototype.surnameAttributeName)"/>&#32;<s:property value="#contact.getValue(#userProfilePrototype.firstNameAttributeName)"/>" href="<s:url action="edit" ><s:param name="entityId" value="#contactId"></s:param></s:url>" tabindex="<wpsa:counter />">
										<s:property value="#contact.getValue(#userProfilePrototype.firstNameAttributeName)"/>
									</a>
								</td>
							</s:if>
							<s:if test="#userProfilePrototype.mailAttributeName != null">
								<td>
									<s:property value="#contact.getValue(#userProfilePrototype.mailAttributeName)"/>
								</td>
							</s:if>
							
							<td>
								<s:property value="#contact.owner" /> 
							</td>
							<td class="icon">
								<a title="<s:text name="jpaddressbook.download.vcard" />:&#32;<s:property value="#contact.getValue(#userProfilePrototype.firstNameAttributeName)"/>&#32;<s:property value="#contact.getValue(#userProfilePrototype.surnameAttributeName)"/>" href="<s:url action="downloadvcard" namespace="/do/jpaddressbook/VCard" ><s:param name="entityId" value="#contactId"></s:param></s:url>" tabindex="<wpsa:counter />">
										<img src="<wp:resourceURL />administration/common/img/icons/22x22/attachment.png" alt="Download VCard&#32;<s:property value="#contact.getValue(#userProfilePrototype.firstNameAttributeName)"/>&#32;<s:property value="#contact.getValue(#userProfilePrototype.surnameAttributeName)"/>" />
								</a>
							</td>
							<td class="icon">
								<s:if test="#contact.owner.equals(#session.currentUser.username)">
									<a title="<s:text name="jpaddressbook.label.editContact" />:&#32;<s:property value="#contact.getValue(#userProfilePrototype.firstNameAttributeName)"/>&#32;<s:property value="#contact.getValue(#userProfilePrototype.surnameAttributeName)"/>" href="<s:url action="view" ><s:param name="entityId" value="#contactId"></s:param></s:url>" tabindex="<wpsa:counter />">
										<img src="<wp:resourceURL />administration/common/img/icons/edit-content.png" alt="<s:text name="jpaddressbook.label.editContact" />&#32;<s:property value="#contact.getValue(#userProfilePrototype.firstNameAttributeName)"/>&#32;<s:property value="#contact.getValue(#userProfilePrototype.surnameAttributeName)"/> " /> 
									</a>
								</s:if>
								<s:else>
									<abbr title="<s:text name="jpaddressbook.label.notAvaiable" />">&ndash;</abbr>
								</s:else>
							</td>
							<td class="icon">	
								<s:if test="#contact.owner.equals(#session.currentUser.username)">
									<a title="<s:text name="jpaddressbook.label.deleteActionFull" />:&#32;<s:property value="#contact.getValue(#userProfilePrototype.firstNameAttributeName)"/>&#32;<s:property value="#contact.getValue(#userProfilePrototype.surnameAttributeName)"/>" href="<s:url action="trash" ><s:param name="entityId" value="#contactId"></s:param></s:url>" tabindex="<wpsa:counter />">
										<img src="<wp:resourceURL />administration/common/img/icons/delete.png" alt="<s:text name="jpaddressbook.label.deleteActionFull" />&#32;<s:property value="#contact.getValue(#userProfilePrototype.firstNameAttributeName)"/>&#32;<s:property value="#contact.getValue(#userProfilePrototype.surnameAttributeName)"/>" />
									</a>
								</s:if> 
								<s:else>
									<abbr title="<s:text name="jpaddressbook.label.cannotEditThis" />">&ndash;</abbr>
								</s:else>
							</td>
						</tr>
						
					</s:iterator>
				</table>
				
				<div class="pager">
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
				</div>
				
			</wpsa:subset> 
			
		</s:form>

	
	</div>
</div>