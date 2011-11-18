<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1>
<s:if test="questionnaire">
<a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="survey.questionnaire"></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.survey.main" />"><s:text name="title.jpsurvey.survey.main" /></a>
</s:if>
<s:else>
<a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="survey.questionnaire"></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.poll.main" />"><s:text name="title.jpsurvey.poll.main" /></a>
</s:else>
</h1>

<div id="main">
	<%-- new title --%>
	<s:if test="getStrutsAction() == 1">
		<s:if test="questionnaire">
			<h2><s:text name="title.jpsurvey.survey.new" /></h2>
		</s:if>
		<s:else>
			<h2><s:text name="title.jpsurvey.poll.new" /></h2>
		</s:else>
	</s:if>
	
	<%-- edit title --%>
	<s:if test="getStrutsAction() == 2">
		<s:if test="questionnaire">
			<h2><s:text name="title.jpsurvey.survey.edit" /></h2>
		</s:if>
		<s:else>
			<h2><s:text name="title.jpsurvey.poll.edit" /></h2>
		</s:else>
	</s:if>
	
	<%-- workin on --%>
	<s:if test="surveyId != null">
		<p>
			<s:text name="label.workingOn" />: <em><s:property value="%{getLabel(survey.titles)}" /></em>.
		</p>
	</s:if>
	
	<%-- edit help --%>
	<s:if test="getStrutsAction() == 2">
		<s:if test="questionnaire">
			<p><s:text name="title.jpsurvey.survey.edit.intro" /></p>
		</s:if>
		<s:else>
			<p><s:text name="title.jpsurvey.poll.edit.intro" /></p>
		</s:else>
	</s:if>
	
	<%-- error messages --%>
	<s:if test="hasFieldErrors()">
		<div class="message message_error">
		<h3><s:text name="message.title.FieldErrors" /></h3>
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
		<h3><s:text name="message.title.ActionErrors" /></h3>	
			<ul>
			<s:iterator value="actionErrors">
				<li><s:property escape="false" /></li>
			</s:iterator>
			</ul>
		</div>
	</s:if>  
	<ul class="menu horizontal tab-toggle-bar"><li><a href="#info" id="info_tab_quickmenu" class="tab-toggle">Info</a></li><s:iterator value="langs" id="lang"><li><a href="#<s:property value="#lang.code" />_tab" class="tab-toggle"><s:property value="#lang.descr" /></a></li></s:iterator></ul>
	<s:form cssClass="tab-container">
		<%-- HIDDEN FIELDS --%>
		<p class="noscreen">
			<s:if test="surveyId != null">
				<s:hidden name="questionnaire" value="%{survey.questionnaire}"/>
			</s:if>
			<s:else>
				<s:hidden name="questionnaire"/>
			</s:else>
			<s:hidden name="strutsAction" />
			<s:if test="imageId != null">
				<s:hidden name="imageId" />
			</s:if>
		</p>
		
		<s:if test="getStrutsAction() == 2">
			<p class="noscreen">
				<s:hidden name="surveyId" />
			</p>
		</s:if>
		<%-- FINE HIDDEN FIELDS --%>
		
		<%-- INIZIO ITERAZIONE CAMPI LOCALIZZATI--%>
		<div id="info" class="tab">
		 
			<fieldset class="margin-bit-top">
				<legend><s:text name="label.info" /></legend>

				<p>
					<label for="startDate_cal" class="basic-mint-label"><s:text name="jpsurvey_start_date" />:</label>
					<wpsf:textfield useTabindexAutoIncrement="true" name="startDate" id="startDate_cal" cssClass="text" />
				</p>
				
				<p>
					<label for="endDate_cal" class="basic-mint-label"><s:text name="jpsurvey_end_date" />:</label>
					<wpsf:textfield useTabindexAutoIncrement="true" name="endDate" id="endDate_cal" cssClass="text" />
				</p>
				
				<p>
					<label for="groupName" class="basic-mint-label"><s:text name="jpsurvey_group" />:</label>
					<wpsf:select useTabindexAutoIncrement="true" name="groupName" id="groupName" list="groups" listKey="name" listValue="descr" cssClass="text" />
				</p>
				
				<p class="noscreen"><s:hidden name="active" /></p>
				
				<s:if test="questionnaire">
					<%--<p>NEI QUESTIONARI RISULTATI PARZIALI E DEFINITIVI NON SONO PUBBLICI</p>--%>
				</s:if>
				
				<s:else>
					<p>
						<label for="publicPartialResult" class="basic-mint-label"><s:text name="jpsurvey_publicPartialResult" />:</label>	
						<select name="publicPartialResult" id="publicPartialResult" tabindex="<wpsa:counter />">
							<option value="1" <s:if test="%{publicPartialResult > 0}">selected="selected"</s:if> ><s:text name="jpsurvey_isPublicPartialResult" /></option>
							<option value="0" <s:if test="%{publicPartialResult == null || publicPartialResult == 0}">selected="selected"</s:if> ><s:text name="jpsurvey_notPublicPartialResult" /></option>
						</select>	
					</p>
					<p>
						<label for="publicResult" class="basic-mint-label"><s:text name="jpsurvey_publicResult" />:</label>
						<select name="publicResult" id="publicResult" tabindex="<wpsa:counter />">
							<option value="1" <s:if test="%{publicResult > 0}">selected="selected"</s:if> ><s:text name="jpsurvey_isPublicResult" /></option>
							<option value="0" <s:if test="%{publicResult == null || publicResult == 0}">selected="selected"</s:if> ><s:text name="jpsurvey_notPublicResult" /></option>
						</select>
					</p>
				</s:else>
				
				<p>
					<label for="gatherUserInfo" class="basic-mint-label"><s:text name="jpsurvey_profileUser" />:</label>
					<select name="gatherUserInfo" id="gatherUserInfo" tabindex="<wpsa:counter />">
						<option value="1" <s:if test="%{gatherUserInfo > 0}">selected="selected"</s:if> ><s:text name="jpsurvey_isProfileUser" /></option>
						<option value="0" <s:if test="%{gatherUserInfo == null || gatherUserInfo == 0}">selected="selected"</s:if> ><s:text name="jpsurvey_notProfileUser" /></option>
					</select>
				</p>

			</fieldset>
			<fieldset>
				<legend><s:text name="jpsurvey_vote_control" /></legend>
				<p>
					<wpsf:checkbox useTabindexAutoIncrement="true" name="checkCookie" id="checkCookie" cssClass="radiocheck"/><label for="checkCookie"><s:text name="jpsurvey_cookie" /></label><br />
					<wpsf:checkbox useTabindexAutoIncrement="true" name="checkIpAddress" id="checkIpAddress" cssClass="radiocheck" /><label for="checkIpAddress"><s:text name="jpsurvey_ip_username" /></label>
				</p>
			</fieldset>
		</div>
		
		<s:iterator id="currentLang" value="langs">
			<div id="<s:property value="#currentLang.code" />_tab" class="tab">
				
				<div class="contentAttributeBox">	
					<p>
						<label for="title-<s:property value="#currentLang.code"/>" class="basic-mint-label"><s:text name="jpsurvey_title" />:</label>
						<wpsf:textfield useTabindexAutoIncrement="true" name="%{'title-'+#currentLang.code}" id="%{'title-'+#currentLang.code}" value="%{titles[#currentLang.code]}" cssClass="text" />		
					</p>
				</div>
				
				<div class="contentAttributeBox">	
					<p>
						<label for="description-<s:property value="%{#currentLang.code}"/>" class="basic-mint-label"><s:text name="jpsurvey_description" />:</label>
						<wpsf:textarea useTabindexAutoIncrement="true" name="%{'description-'+#currentLang.code}" id="%{'description-'+#currentLang.code}" cols="45" rows="6" value="%{descriptions[#currentLang.code]}" />
					</p>
				</div>		
				
				<%-- PULSANTIERA IMMAGINE SURVEY--%>
				<s:if test="strutsAction == 2">
					<div class="contentAttributeBox">
						<s:if test="%{imageId == null}">
							<%-- BOTTONE ASSOCIA IMMAGINE --%>
							<p class="important margin-more-bottom">
								<label for="imageDescription-<s:property value="%{#currentLang.code}"/>" class="basic-mint-label"><s:text name="jpsurvey_imageDescription" />:</label>	
								<span class="noscreen">Choose an Image</span>
								<wpsa:actionParam action="associateSurveyImageEntry" var="associateImageAction"><wpsa:actionSubParam name="resourceTypeCode" value="Image" /></wpsa:actionParam>
								<wpsa:set name="iconImagePath" id="iconImagePath"><wp:resourceURL />administration/common/img/icons/22x22/image.png</wpsa:set>
								<wpsf:submit useTabindexAutoIncrement="true" type="image" action="%{#associateImageAction}" value="%{getText('jpsurvey_new_image')}" src="%{#iconImagePath}" title="%{getText('label.choose')}"/>		
							</p>
						</s:if>
						
						<s:else>
							<%-- BOTTONE RIMUOVI IMMAGINE --%>
							<p class="margin-more-bottom">
								<label for="imageDescription-<s:property value="%{#currentLang.code}"/>" class="basic-mint-label"><s:text name="jpsurvey_imageDescription" />:</label>
								<s:set name="resource" value="%{loadResource(imageId)}" />
								<a href="<s:property value="%{#resource.getImagePath(0)}" />" title="<s:property value="%{#resource.descr}" />" ><img src="<s:property value="%{#resource.getImagePath(1)}"/>" alt="<s:property value="%{#resource.descr}" />" /></a>
								<wpsf:textfield useTabindexAutoIncrement="true" name="%{'imageDescription-'+#currentLang.code}" id="%{'imageDescription-'+#currentLang.code}" value="%{imageDescriptions[#currentLang.code]}" cssClass="text" />
								<wpsa:set name="iconImagePath" id="iconImagePath"><wp:resourceURL />administration/common/img/icons/delete.png</wpsa:set>
								<wpsf:submit useTabindexAutoIncrement="true" type="image" src="%{#iconImagePath}" action="removeSurveyImage" value="%{getText('jpsurvey_remove_image')}" title="%{getText('label.remove')}" />
							</p>
						</s:else>
						
					</div>
				</s:if>
				<%-- FINE ITERAZIONE CAMPI LOCALIZZATI--%>
			</div>
		</s:iterator>
		
		<s:if test="%{strutsAction == 2}"> 
			<fieldset>
				<legend><s:text name="jpsurvey_questions_list" /></legend>
					<%-- INIZIO ITERAZIONE DOMANDE NELLA LINGUA CORRENTE --%>
					<s:if test="survey.questions.isEmpty">
						<p><s:text name="jpsurvey.noQuestions" /></p>
					</s:if>
						
					<s:else>
						<table class="generic" summary="<s:if test="questionnaire"><s:text name="jpsurvey_questionsTable_summary_survey" /></s:if><s:else><s:text name="jpsurvey_questionsTable_summary_poll" /></s:else>">
							<caption><span><s:text name="jpsurvey_questionsTable_caption" /></span></caption>
							<tr>
								<th><s:text name="jpsurvey_questionsTable_questions" /></th>
								<th><s:text name="jpsurvey_questionsTable_occurences" /></th>
								<th><s:text name="jpsurvey_questionsTable_answers_number" /></th>
								<th class="icon" colspan="4"><abbr title="<s:text name="jpsurvey_questionsTable_actions" />">-</abbr></th>		
							</tr>
							<s:set var="currentSurveyQuestions" value="survey.questions" />
							<s:iterator var="currentQuestion" value="#currentSurveyQuestions" status="status" >
								<tr>
									<td>
										<%-- DOMANDA LINKATA --%>
										<a href="<s:url action="editQuestion"><s:param name="surveyId" value="surveyId"/><s:param name="questionId" value="#currentQuestion.id"/><s:param name="strutsAction" value="2"/></s:url>" title="<s:text name="label.edit" />: <s:property value="%{getLabel(#currentQuestion.questions)}"/>">
											<s:property value="%{getLabel(#currentQuestion.questions)}"/>
										</a>
									</td>
									<td class="centerText">
										<s:property value="%{getResponseOccurences(#currentQuestion.id)}" />
									</td>
									<td class="centerText" ><s:property value="#currentQuestion.AnswersNumber" /></td>
									<td class="icon"><a href="<s:url action="moveQuestionUp"><s:param name="surveyId" value="surveyId"/><s:param name="questionId" value="#currentQuestion.id"/></s:url>" title="<s:text name="label.moveUp" />: <s:property value="%{getLabel(#currentQuestion.questions)}"/>" ><img src="<wp:resourceURL />administration/common/img/icons/go-up.png" alt="<s:text name="label.moveUp" />" /></a></td>
									<td class="icon"><a href="<s:url action="moveQuestionDown"><s:param name="surveyId" value="surveyId"/><s:param name="questionId" value="#currentQuestion.id"/></s:url>" title="<s:text name="label.moveDown" />: <s:property value="%{getLabel(#currentQuestion.questions)}"/>" ><img src="<wp:resourceURL />administration/common/img/icons/go-down.png" alt="<s:text name="label.moveDown" />" /></a></td>
									<td class="icon"><a href="<s:url action="trashQuestion"><s:param name="surveyId" value="surveyId"/><s:param name="questionId" value="#currentQuestion.id"/></s:url>" title="<s:text name="label.remove" />: <s:property value="%{getLabel(#currentQuestion.questions)}"/>"><img src="<wp:resourceURL />administration/common/img/icons/list-remove.png" alt="<s:text name="label.remove" />" /></a></td>
								</tr>
							</s:iterator>

						</table>
					</s:else>
					<%-- FINE ITERAZIONE DOMANDE --%>
					<s:if test="%{surveyId != null}">
						<p><wpsf:submit useTabindexAutoIncrement="true" action="addQuestion" value="%{getText('jpsurvey_new_questions')}" cssClass="button"/></p>
					</s:if>
			</fieldset>
		</s:if>
		 
		<p class="centerText margin-more-top">
			<s:if test="getStrutsAction() == 1">
				<wpsf:submit useTabindexAutoIncrement="true" action="saveSurvey" value="%{getText('label.continue')}" cssClass="button" />
			</s:if>
			<s:elseif test="getStrutsAction() == 2">
				<wpsf:submit useTabindexAutoIncrement="true" action="saveSurvey" value="%{getText('label.save')}" cssClass="button" />
			</s:elseif>
		</p>

	</s:form>
</div>