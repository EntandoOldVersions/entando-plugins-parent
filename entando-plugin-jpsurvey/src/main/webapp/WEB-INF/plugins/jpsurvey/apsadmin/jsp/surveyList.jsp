<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1>
<s:if test="questionnaire"><s:text name="title.jpsurvey.survey.main" /></s:if>
<s:else><s:text name="title.jpsurvey.poll.main" /></s:else>
</h1>
<div id="main">
	<s:if test="hasFieldErrors()">
		<div class="message message_error">
			<h2><s:text name="message.title.FieldErrors" /></h2>
			<ul>
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
						<li><s:property /></li>
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
					<li><s:property /></li>
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

	<s:form action="searchSurvey">
		<p class="noscreen">
			<s:hidden name="questionnaire" />
		</p>
		
		<p>
			<label for="description" class="basic-mint-label label-search"><s:text name="label.search.by" />&#32;<s:text name="jpsurvey_description" />:</label>
			<wpsf:textfield useTabindexAutoIncrement="true" name="description" id="description" cssClass="text" />
		</p>
		<fieldset>
			<legend class="accordion_toggler"><s:text name="title.searchFilters" /></legend>
			<div class="accordion_element">
				<p>
					<label for="title" class="basic-mint-label"><s:text name="jpsurvey_title" />:</label>
					<wpsf:textfield useTabindexAutoIncrement="true" name="title" id="title" cssClass="text" />
				</p>
				
				<p>
					<label for="group" class="basic-mint-label"><s:text name="jpsurvey_group" />:</label>
					<wpsf:select useTabindexAutoIncrement="true" name="group" id="group" list="groups" listKey="name" listValue="descr" 
						headerKey="" headerValue="%{getText('jpsurvey_indifferent')}" cssClass="text" />
				</p>
			</div>
		</fieldset>
		<p><wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.search')}" cssClass="button" /></p>
		
		<div class="subsection-light">
			<%//TODO verificare posizione new %>
			<p>
				<a href="<s:url action="newSurvey" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="questionnaire"/></s:url>" tabindex="<wpsa:counter />">
				<s:if test="questionnaire"><s:text name="jpsurvey_new_survey" /></s:if>
				<s:else><s:text name="jpsurvey_new_poll" /></s:else>
				</a>
			</p>
			<s:if test="%{surveysIds.size()==0}">
				<s:if test="questionnaire">
					<p><s:text name="jpsurvey.noSurveys" /></p>
				</s:if>
				<s:else>
					<p><s:text name="jpsurvey.noPools" /></p>
				</s:else>
			</s:if>
			<s:else>
				<wpsa:subset source="surveysIds" count="10" objectName="groupSurveyIds" advanced="true" offset="5">
					<s:set name="group" value="#groupSurveyIds" />
					<div class="pager">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>
					
					<table class="generic" summary="<s:if test="questionnaire"><s:text name="jpsurvey_summary_survey" /></s:if><s:else><s:text name="jpsurvey_summary_poll" /></s:else>">
						<caption><span>
							<s:if test="questionnaire"><s:text name="jpsurvey_caption_surveys" /></s:if>
							<s:else><s:text name="jpsurvey_caption_polls" /></s:else>
						</span></caption>
						<tr>
							<th><s:text name="jpsurvey_title" /></th>
							<th><s:text name="jpsurvey_start_date" /></th>
							<th><s:text name="jpsurvey_questions_number" /></th>
							<th><abbr title="<s:text name="jpsurvey_published" />">P</abbr></th>
							<th><abbr title="<s:text name="jpsurvey_results" />">R</abbr></th>
							<th><abbr title="<s:text name="jpsurvey_actions" />">-</abbr></th>
						</tr>
						
						<s:iterator id="surveyId">
							<tr>
								<s:set name="survey" value="%{getSurvey(#surveyId)}" />
								<%--LINK ALL'AZIONE DI EDIT, DIFFERENZIAMO PER TIPO DI SURVEY --%>
								<td>
									<a href="<s:url action="editSurvey"><s:param name="surveyId" value="#survey.id"/><s:param name="questionnaire" value="questionnaire"/></s:url>" title="<s:text name="label.edit" />: <s:property value="%{getLabel(#survey.titles)}" />" ><s:property value="%{getLabel(#survey.titles)}"/></a>
									<span class="noscreen">|</span>	
								</td>
								<td class="centerText"><span class="monospace"><s:date name="#survey.startDate" format="dd/MM/yyyy" /></span></td> 
								<s:if test="%{#survey.active}">
									<wpsa:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>plugins/jpsurvey/administration/img/icons/content-isonline.png</wpsa:set>
									<s:set name="isOnlineStatus" value="%{getText('jpsurvey_published_yes')}" />
								</s:if>
								<s:elseif test="%{#survey.publishable}">
									<wpsa:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>plugins/jpsurvey/administration/img/icons/content-isnotsynched.png</wpsa:set>
									<s:set name="isOnlineStatus" value="%{getText('jpsurvey_published_not_but_ready')}" />
								</s:elseif>
								<s:else>
									<wpsa:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>plugins/jpsurvey/administration/img/icons/content-isnotonline.png</wpsa:set>
									<s:set name="isOnlineStatus" value="%{getText('jpsurvey_published_not_ready')}" />
								</s:else>
								
								<td class="centerText"><s:property value="#survey.questionsNumber"/></td>
								
								<td class="icon"><img src="<s:property value="iconImagePath" />" alt="<s:property value="isOnlineStatus" />" title="<s:property value="isOnlineStatus" />" /></td>
								<td class="icon"><a href="<s:url action="view"><s:param name="surveyId" value="#survey.id"/><s:param name="questionnaire" value="questionnaire" /></s:url>" title="<s:text name="jpsurvey_results" />: <s:property value="%{getLabel(#survey.titles)}"/>"><img src="<wp:resourceURL />plugins/jpsurvey/administration/img/icons/system-search.png" alt="<s:text name="jpsurvey_results" />" /></a></td>		
								<td class="icon_double">
									<s:if test="%{#survey.active}">
									<a href="<s:url action="retireSurvey"><s:param name="surveyId" value="#survey.id"/><s:param name="questionnaire" value="questionnaire" /></s:url>" title="<s:text name="jpsurvey_exitPublish" />: <s:property value="%{getLabel(#survey.titles)}"/>"><img src="<wp:resourceURL />plugins/jpsurvey/administration/img/icons/network-offline.png" alt="<s:text name="jpsurvey_exitPublish" />" /></a>
									</s:if>
									<s:else>
									<a href="<s:url action="publishSurvey"><s:param name="surveyId" value="#survey.id"/><s:param name="questionnaire" value="questionnaire"/></s:url>" title="<s:text name="jpsurvey_publish" />: <s:property value="%{getLabel(#survey.titles)}"/>"><img src="<wp:resourceURL />plugins/jpsurvey/administration/img/icons/internet-web-browser.png" alt="<s:text name="jpsurvey_publish" />" /></a>
									</s:else>
									<span class="noscreen">|</span>
									<a href="<s:url action="trashSurvey"><s:param name="surveyId" value="#survey.id"/><s:param name="deletingElement" value="1"/><s:param name="questionnaire" value="questionnaire"/><s:param name="deleteInfo" value="%{getLabel(#survey.titles)}"/></s:url>" title="<s:text name="label.remove" />: <s:property value="%{getLabel(#survey.titles)}"/>"><img src="<wp:resourceURL />plugins/jpsurvey/administration/img/icons/delete.png" alt="Delete" /></a>
								</td>
							</tr>	
						</s:iterator>
					</table>
					
					<div class="pager">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>
					
				</wpsa:subset>
			</s:else>
		</div>
	</s:form>
	
</div>