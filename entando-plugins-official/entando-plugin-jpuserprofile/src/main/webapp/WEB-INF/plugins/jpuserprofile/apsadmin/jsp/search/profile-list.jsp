<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<wp:ifauthorized permission="jpuserprofile_profile_edit" var="hasEditProfilePermission" />	

<h1><s:text name="jpuserprofile.menu.profileAdmin" /></h1>

<div id="main">
	<h2 class="margin-more-bottom"><s:text name="jpuserprofile.title.profiles" /></h2>
	<s:form action="search">
		<%--
		<s:if test="null != entityTypeCode && entityTypeCode != ''">	
			<p class="noscreen">
				<wpsf:hidden name="entityTypeCode" />
			</p>
			<p>
				<label for="jpuserprofile_src_entityPrototypes" class="basic-mint-label label-search"><s:text name="jpuserprofile.note.search.profileType" /></label>
				<wpsf:select useTabindexAutoIncrement="true" id="jpuserprofile_src_entityPrototypes" list="entityPrototypes" name="entityTypeCode" headerKey="" headerValue="%{getText('label.all')}" listKey="typeCode" listValue="typeDescr" cssClass="text" disabled="true"/>
				<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.change')}" cssClass="button" action="changeEntityType" />
			</p>
		</s:if>			
		<s:else>
		--%>
			<p>
				<label for="jpuserprofile_src_entityPrototypes" class="basic-mint-label label-search"><s:text name="jpuserprofile.note.search.profileType" /></label>
				<wpsf:select useTabindexAutoIncrement="true" id="jpuserprofile_src_entityPrototypes" list="entityPrototypes" name="entityTypeCode" headerKey="" headerValue="%{getText('label.all')}" listKey="typeCode" listValue="typeDescr" cssClass="text" />
				<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.set')}" cssClass="button" action="changeEntityType" />
			</p>
		<%--
		</s:else>
		--%>
		<fieldset><legend class="accordion_toggler"><s:text name="title.searchFilters" /></legend>			
			<div class="accordion_element">
				<p>
					<label for="jpuserprofile_src_username" class="basic-mint-label"><s:text name="jpuserprofile.label.username" /></label>
					<wpsf:textfield useTabindexAutoIncrement="true" name="username" id="jpuserprofile_src_username" cssClass="text" />
				</p>
			</div> 
		</fieldset>
		
		<fieldset><legend class="accordion_toggler"><s:text name="title.searchResultOptions" /></legend>
			<div class="accordion_element">
				<s:set var="searcheableAttributes" value="searcheableAttributes" ></s:set>
				<s:if test="null != #searcheableAttributes && #searcheableAttributes.size() > 0">
					<s:iterator var="attribute" value="#searcheableAttributes">
						<%-- Text Attribute --%>
						<s:if test="#attribute.textAttribute">
							<s:set var="currentAttributeHtmlId">jpuserprofile_src_<s:property value="#attribute.name" /></s:set>
							<s:set name="textInputFieldName"><s:property value="#attribute.name" />_textFieldName</s:set>
							<p>
								<label for="<s:property value="#currentAttributeHtmlId" />" class="basic-mint-label"><s:property value="#attribute.name" /></label>
								<wpsf:textfield useTabindexAutoIncrement="true" id="%{#currentAttributeHtmlId}" name="%{#textInputFieldName}" value="%{getSearchFormFieldValue(#textInputFieldName)}" cssClass="text" />
							</p>
						</s:if>
						
						<%-- Date Attribute --%>
						<s:elseif test="#attribute.type == 'Date'">
							<s:set var="currentAttributeHtmlId">jpuserprofile_src_<s:property value="#attribute.name" /></s:set>
							<s:set name="dateStartInputFieldName" ><s:property value="#attribute.name" />_dateStartFieldName</s:set>
							<s:set name="dateEndInputFieldName" ><s:property value="#attribute.name" />_dateEndFieldName</s:set>
							<p>
								<label for="<s:property value="%{#currentAttributeHtmlId+'_dateStartFieldName_cal'}" />" class="basic-mint-label">
									<s:property value="#attribute.name" />&#32;<s:text name="jpuserprofile.from.date" />
								</label>
								<wpsf:textfield useTabindexAutoIncrement="true" id="%{#currentAttributeHtmlId}_dateStartFieldName_cal" name="%{#dateStartInputFieldName}" value="%{getSearchFormFieldValue(#dateStartInputFieldName)}" cssClass="text" />
								<span class="inlineNote"><s:text name="jpuserprofile.date.pattern" /></span>
							</p>			
							<p>
								<label for="<s:property value="%{#currentAttributeHtmlId+'_dateEndFieldName_cal'}" />" class="basic-mint-label">
									<s:property value="#attribute.name" />&#32;<s:text name="jpuserprofile.to.date" />
								</label>
								<wpsf:textfield useTabindexAutoIncrement="true" id="%{#currentAttributeHtmlId}_dateEndFieldName_cal" name="%{#dateEndInputFieldName}" value="%{getSearchFormFieldValue(#dateEndInputFieldName)}" cssClass="text" />
								<span class="inlineNote"><s:text name="jpuserprofile.date.pattern" /></span>
							</p>		
						</s:elseif>
						
						<%-- Number Attribute --%>
						<s:elseif test="#attribute.type == 'Number'">
							<s:set var="currentAttributeHtmlId">jpuserprofile_src_<s:property value="#attribute.name" /></s:set>
							<s:set name="numberStartInputFieldName" ><s:property value="#attribute.name" />_numberStartFieldName</s:set>
							<s:set name="numberEndInputFieldName" ><s:property value="#attribute.name" />_numberEndFieldName</s:set>
							<p>
								<label for="<s:property value="%{#currentAttributeHtmlId+'_start'}" />" class="basic-mint-label">
									<s:property value="#attribute.name" />&#32;<s:text name="jpuserprofile.from.value" />
								</label>
								<wpsf:textfield useTabindexAutoIncrement="true" id="%{#currentAttributeHtmlId}_start" name="%{#numberStartInputFieldName}" value="%{getSearchFormFieldValue(#numberStartInputFieldName)}" cssClass="text" />
							</p>			
							<p>
								<label for="<s:property value="%{#currentAttributeHtmlId+'_end'}" />" class="basic-mint-label">
									<s:property value="#attribute.name" />&#32;<s:text name="jpuserprofile.to.value" />
								</label>
								<wpsf:textfield useTabindexAutoIncrement="true" id="%{#currentAttributeHtmlId}_end" name="%{#numberEndInputFieldName}" value="%{getSearchFormFieldValue(#numberEndInputFieldName)}" cssClass="text" />
							</p>			
						</s:elseif>
						
						<s:elseif test="#attribute.type == 'Boolean' || #attribute.type == 'ThreeState'"> 
							<p>
								<span class="important"><s:property value="#attribute.name" /></span>
							</p>
							<s:set name="booleanInputFieldName" ><s:property value="#attribute.name" />_booleanFieldName</s:set>
							<s:set name="booleanInputFieldValue" ><s:property value="%{getSearchFormFieldValue(#booleanInputFieldName)}" /></s:set>
							<ul class="noBullet">
								<li><wpsf:radio useTabindexAutoIncrement="true" id="none_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="" checked="%{!#booleanInputFieldValue.equals('true') && !#booleanInputFieldValue.equals('false')}" cssClass="radiocheck" /><label for="none_<s:property value="#booleanInputFieldName" />" class="normal" ><s:text name="label.bothYesAndNo"/></label></li>
								<li><wpsf:radio useTabindexAutoIncrement="true" id="true_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="true" checked="%{#booleanInputFieldValue == 'true'}" cssClass="radiocheck" /><label for="true_<s:property value="#booleanInputFieldName" />" class="normal" ><s:text name="label.yes"/></label></li>
								<li><wpsf:radio useTabindexAutoIncrement="true" id="false_%{#booleanInputFieldName}" name="%{#booleanInputFieldName}" value="false" checked="%{#booleanInputFieldValue == 'false'}" cssClass="radiocheck" /><label for="false_<s:property value="#booleanInputFieldName" />" class="normal"><s:text name="label.no"/></label></li>
							</ul>
						</s:elseif>
					</s:iterator>
				</s:if>
				<s:else>
					<p>
						<s:text name="jpuserprofile.note.searchAdvanced.chooseType" />
					</p>
				</s:else>
			</div>
		</fieldset>
		
		<p>
			<wpsf:submit useTabindexAutoIncrement="true" cssClass="button" value="%{getText('label.search')}" action="search" />
		</p>
		
		<div class="subsection-light">
		
		<s:if test="%{null != searcheableAttributes && searcheableAttributes.size() > 0}">
			<p class="noscreen">
				<s:iterator var="attribute" value="#searcheableAttributes">
					<s:if test="#attribute.textAttribute">
						<wpsf:hidden name="%{#textInputFieldName}" value="%{getSearchFormFieldValue(#attribute.name+'_textFieldName')}" />
					</s:if>
					<s:elseif test="#attribute.type == 'Date'">
						<wpsf:hidden name="%{#dateStartInputFieldName}" value="%{getSearchFormFieldValue(#attribute.name+'_dateStartFieldName')}" />
						<wpsf:hidden name="%{#dateEndInputFieldName}" value="%{getSearchFormFieldValue(#attribute.name+'_dateEndFieldName')}" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Number'">
						<wpsf:hidden name="%{#numberStartInputFieldName}" value="%{getSearchFormFieldValue(#attribute.name+'_numberStartFieldName')}" />
						<wpsf:hidden name="%{#numberEndInputFieldName}" value="%{getSearchFormFieldValue(#attribute.name+'_numberEndFieldName')}" />
					</s:elseif>
					<s:elseif test="#attribute.type == 'Boolean' || #attribute.type == 'ThreeState'"> 
						<wpsf:hidden name="%{#booleanInputFieldName}" value="%{getSearchFormFieldValue(#booleanInputFieldName)}" />
					</s:elseif>
				</s:iterator>
			</p>
		</s:if>
		
		<c:if test="${hasEditProfilePermission}">
			<p><s:text name="jpuserprofile.note.gotoProfiles" />&#32;<a href="<s:url namespace="/do/jpuserprofile" action="list"></s:url>" tabindex="<wpsa:counter />"><s:text name="jpuserprofile.label.allUser" /></a>&#32;<s:text name="jpuserprofile.note.gotoProfiles.outro" /></p>
		</c:if>
		
		<s:set var="entityIds" value="searchResult" />
		<wpsa:subset source="#entityIds" count="10" objectName="entityGroup" advanced="true" offset="5">
			<s:set name="group" value="#entityGroup" />
	
			<div class="pager">
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
			</div>

			<s:if test="%{null != #entityIds && #entityIds.size() > 0}">
				<table class="generic" summary="<s:text name="jpuserprofile.note.userList.summary" />">
				<caption><span><s:text name="jpuserprofile.title.profileList" /></span></caption>
					<tr>
						<th><s:text name="jpuserprofile.label.username" /></th>
						<th><s:text name="jpuserprofile.label.name" /></th>
						<th><s:text name="jpuserprofile.label.mail" /></th>
						<c:if test="${hasEditProfilePermission}">
							<th><abbr title="<s:text name="jpuserprofile.label.profile.edit.long" />"><s:text name="jpuserprofile.label.profile.edit.short" /></abbr></th>
						</c:if>
					</tr>
					<s:iterator var="entityId">
						<s:set var="userProfileVar" />
						<wpsa:entity entityManagerName="jpuserprofileUserProfileManager" key="%{#entityId}" var="userProfileVar"/>
						<tr>
							<td>
								<s:property value="#entityId" />			
							</td>
							<td><s:property value="#userProfileVar.getValue(#userProfileVar.firstNameAttributeName)"/>&nbsp;<s:property value="#userProfileVar.getValue(#userProfileVar.surnameAttributeName)"/></td>
							<td><s:property value="#userProfileVar.getValue(#userProfileVar.mailAttributeName)"/></td>
							<c:if test="${hasEditProfilePermission}">
								<td class="icon">
									<a 	href="<s:url action="edit" namespace="/do/jpuserprofile"><s:param name="username" value="#entityId"/></s:url>" 
										title="<s:text name="jpuserprofile.label.editProfile" />: <s:property value="#entityId" />">
											<img src="<wp:resourceURL/>administration/common/img/icons/edit-content.png" alt="<s:text name="jpuserprofile.label.editProfile" />" />
									</a>
								</td>
							</c:if>
							<%--<wp:ifauthorized permission="superuser">
								<td class="icon">
									<a 	href="<s:url action="edit" namespace="/do/User"><s:param name="username" value="#entityId"/></s:url>" 
										title="<s:text name="label.edit" />: <s:property value="#entityId" />">
											<img src="<wp:resourceURL/>administration/common/img/icons/users.png" alt="<s:text name="label.edit" />" />
									</a>
								</td>
								<td class="icon">
									<a 	href="<s:url namespace="/do/User/Auth" action="edit"><s:param name="username" value="#entityId"/></s:url>" 
										title="<s:text name="jpuserprofile.note.configureAuthorizationsFor" />: <s:property value="#entityId" />">
											<img src="<wp:resourceURL />administration/common/img/icons/authorizations.png" alt="<s:text name="jpuserprofile.note.configureAuthorizationsFor" />: <s:property value="#entityId" />" />
									</a>
								</td>
							</wp:ifauthorized>--%>
						</tr>
					</s:iterator>
				</table>
			</s:if>
			<div class="pager">
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
			</div>
		</wpsa:subset>
		</div>
	</s:form>

</div>