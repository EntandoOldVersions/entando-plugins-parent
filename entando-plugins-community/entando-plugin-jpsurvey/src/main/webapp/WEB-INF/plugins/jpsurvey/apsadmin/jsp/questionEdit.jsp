<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1>
	<s:if test="question.questionnaire">
		<a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="question.questionnaire"></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.survey.main" />"><s:text name="title.jpsurvey.survey.main" /></a>
		<s:set name="titleSurveyQuestionnary" value="QUESTIONARIO_LABEL"/>
	</s:if>
	<s:else>
		<a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="question.questionnaire"></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.poll.main" />"><s:text name="title.jpsurvey.poll.main" /></a>
		<s:set name="titleSurveyQuestionnary" value="SONDAGGIO_LABEL"/>
	</s:else>
</h1>

<div id="main">
	<h2>
		<s:if test="getStrutsAction() == 2">
			<s:text name="title.jpsurvey.question.edit" />
		</s:if>
		<s:if test="getStrutsAction() == 1">
			<s:text name="title.jpsurvey.question.new" />
			<s:set name="isModifiable" value="true"/>
		</s:if>
	</h2> 
	
	<p>
		<s:text name="label.workingOn" />:&#32;
		<em><s:property value="%{getLabel(question.surveyTitles)}" /></em>
	</p>
	
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
				<li><s:property /></li>
			</s:iterator>
		</ul>
		</div>
	</s:if>
	
	<ul class="menu horizontal tab-toggle-bar">
		<s:iterator value="langs" id="lang">
		<li><a href="#<s:property value="#lang.code" />_tab" class="tab-toggle"><s:property value="#lang.descr" /></a></li>
		</s:iterator>
	</ul>
	<s:form cssClass="tab-container" >
		
		<p class="noscreen">
			<s:hidden name="strutsAction" />
			<s:if test="question != null">
				<s:hidden name="questionnaire" value="%{question.questionnaire}"/>
				<s:hidden name="surveyId" value="%{question.surveyId}"/>
			</s:if>
			<s:else> <%-- NEL CASO SIANO EREDITATI DA QUALCHE PARTE PROVARE A LEVARE!--%>
				<s:hidden name="questionnaire" />
				<s:hidden name="surveyId" />
			</s:else>
			<s:if test="getStrutsAction() == 2">
				<s:hidden name="questionId" />
			</s:if>
		</p>
		
		<s:iterator id="cyclingLang" value="langs">
			<div id="<s:property value="#cyclingLang.code" />_tab" class="tab">
				<p>
					<label for="question-<s:property value="#lang.code" />" class="basic-mint-label"><s:text name="jpsurvey_question" />:</label>
					<s:set name="localization" value="%{getQuestions()[#cyclingLang.code]}"/>
					<wpsf:textarea useTabindexAutoIncrement="true" name="%{'question-'+#cyclingLang.code}" id="%{'question-'+#cyclingLang.code}" value="%{#localization}" cssClass="text" cols="60" rows="3" />
				</p>
			</div>
		</s:iterator>
		
		<s:if test="%{strutsAction == 2}">
			<p>
				<label for="singleChoice" class="basic-mint-label"><s:text name="jpsurvey_singleChoice" />:</label>
				<select name="singleChoice" id="singleChoice" tabindex="<wpsa:counter />">
					<option value="1" <s:if test="%{singleChoice == null || singleChoice > 0}">selected="selected"</s:if> ><s:text name="jpsurvey_isSingleChoice" /></option>
					<option value="0" <s:if test="%{singleChoice == 0}">selected="selected"</s:if> ><s:text name="jpsurvey_notSingleChoice" /></option>
				</select>
			</p>
			
			<fieldset class="margin-more-top"> 
				<legend><s:text name="jpsurvey_multipleChoice_configuration" /></legend>
				<p>
					<label for="minResponseNumber" class="basic-mint-label"><s:text name="jpsurvey_minResponseNumber" />:</label>
					<wpsf:textfield useTabindexAutoIncrement="true" name="minResponseNumber" id="minResponseNumber" cssClass="text" />
				</p>
				<p>
					<label for="maxResponseNumber" class="basic-mint-label"><s:text name="jpsurvey_maxResponseNumber" />:</label>
					<wpsf:textfield useTabindexAutoIncrement="true" name="maxResponseNumber" id="maxResponseNumber" cssClass="text" />
				</p>
			</fieldset>
		
			<s:if test="question.choices.isEmpty"> 
				<p><s:text name="jpsurvey.noAnswers" /></p>
			</s:if>
			<s:else>
				<div class="tab-separation">
					<table class="generic" summary="<s:if test="questionnaire"><s:text name="jpsurvey_answersTable_summary_survey" /></s:if><s:else><s:text name="jpsurvey_answersTable_summary_poll" /></s:else>">
						<caption><span><s:text name="jpsurvey_answersTable_caption" /></span></caption>
						<tr>
							<th><s:text name="jpsurvey_answersTable_answers" /></th>
							<s:if test="%{!#isModifiable}">
								<th><s:text name="jpsurvey_answersTable_occurences"></s:text></th>
							</s:if>
							<th class="icon" colspan="3"><abbr title="<s:text name="jpsurvey_answersTable_actions" />">-</abbr></th>
						</tr>
						<%-- STAMPA LE OPZIONI DISPONIBILI STAMPATE NELLA LINGUA DEL BACKEND--%>
						<s:iterator id="currentChoice" value="question.choices" status="status">
							<tr>
								<s:set name="currentStat" value="choiceStats[#currentChoice.id]" />
								<s:if test="%{#currentChoice.freeText== false}">
									<%-- possible answer column --%>
									<td>
										<!-- OTTIENE LA STRINGA LOCALIZZATA -->
										<%-- cancellare
										<s:set name="localizedString" value="#currentChoice.choices[currentLang.code]" />
										--%>
										<a href="<s:url action="editChoice"><s:param name="choiceId" value="#currentChoice.id"/><s:param name="strutsAction" value="2"/></s:url>" title="<s:text name="label.edit" />: <s:property value="%{getLabel(#currentChoice.choices)}"/>" >
											<s:property value="%{getLabel(#currentChoice.choices)}"/>
										</a>
										<s:set name="localizedString" value="%{getLabel(#currentChoice.choices)}" />
									</td>
								</s:if>
								<s:else> 
									<%-- possible answer column --%>
									<td><s:text name="jpsurvey_freeText" /></td>
									<s:set name="localizedString" value="%{getText('jpsurvey_freeText')}" />
								</s:else>
								
								<%-- Number of occurrences colums --%>
								<td class="centerText">
									<s:if test="%{#currentStat == '' || #currentStat==null}">
										0
									</s:if>
									<s:else>
										<%--<s:if test="%{#currentChoice.freeText== true}">
											<a href="<s:url action="freeTextListEntry"><s:param name="choiceId" value="#currentChoice.id"/><s:param name="questionId" value="%{getQuestionId()}"/><s:param name="surveyId" value="%{getSurveyId()}"/><s:param name="questionnaire" value="questionnaire"/><s:param name="strutsAction" value="2"/></s:url>" title="<s:text name="label.view.freeText" />" >
												<s:property value="#currentStat" />
											</a>
										</s:if>
										<s:else>--%>
											<s:property value="#currentStat" />
										<%--</s:else>--%>
									</s:else>
								</td>
								<%-- actions: up/down/delete --%>
								<td class="icon"><a href="<s:url action="moveChoiceUp"><s:param name="choiceId" value="#currentChoice.id"/><s:param name="questionId" value="question.id"/></s:url>" title="<s:text name="label.moveUp" />: <s:property value="#localizedString"/>" ><img src="<wp:resourceURL />administration/common/img/icons/go-up.png" alt="<s:text name="label.moveUp" />" /></a></td>
								<td class="icon"><a href="<s:url action="moveChoiceDown"><s:param name="choiceId" value="#currentChoice.id"/><s:param name="questionId" value="question.id"/></s:url>" title="<s:text name="label.moveDown" />: <s:property value="#localizedString"/>" ><img src="<wp:resourceURL />administration/common/img/icons/go-down.png" alt="<s:text name="label.moveDown" />" /></a></td>
								<td class="icon"><a href="<s:url action="trashChoice"><s:param name="choiceId" value="#currentChoice.id"/></s:url>" title="<s:text name="label.remove" />: <s:property value="#localizedString"/>"><img src="<wp:resourceURL />administration/common/img/icons/list-remove.png" alt="<s:text name="label.remove" />" /></a></td>
							</tr>
						</s:iterator>
					</table>
				</div>
			</s:else>
		</s:if>
		
		<s:if test="%{questionId != null && questionId != ''}">
			<p>
				
				<wpsa:actionParam action="addChoice" var="addChoiceAction">
					<wpsa:actionSubParam name="overrideResponseNumberFieldsCheck" value="true"></wpsa:actionSubParam>
				</wpsa:actionParam>
				<wpsf:submit useTabindexAutoIncrement="true" action="%{#addChoiceAction}" value="%{getText('jpsurvey_new_choice')}" cssClass="button" />
				<s:if test="question.questionnaire">
					&#32;&#32; | &#32;&#32;
				<wpsa:actionParam action="addFreeText" var="addFreeTextAction">
					<wpsa:actionSubParam name="overrideResponseNumberFieldsCheck" value="true"></wpsa:actionSubParam>
				</wpsa:actionParam>
				<wpsf:submit useTabindexAutoIncrement="true" action="%{#addFreeTextAction}" value="%{getText('jpsurvey_new_freeText')}" cssClass="button" />
				</s:if>
			</p>
		</s:if>
	
		<p class="centerText"><wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" action="saveQuestion" /></p>
		
	</s:form>
</div>