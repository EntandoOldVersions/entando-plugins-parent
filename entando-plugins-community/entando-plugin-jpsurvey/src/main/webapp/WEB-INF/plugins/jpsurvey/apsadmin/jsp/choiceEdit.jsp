<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1>
<s:if test="choice.questionnaire">
<a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="choice.questionnaire"></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.survey.main" />"><s:text name="title.jpsurvey.survey.main" /></a>
</s:if>
<s:else>
<a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="choice.questionnaire"></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.poll.main" />"><s:text name="title.jpsurvey.poll.main" /></a>
</s:else>
</h1>
<div id="main"> 
	<h2>
		<s:if test="getStrutsAction() == 1"><s:text name="title.jpsurvey.choice.new" /></s:if>
		<s:if test="getStrutsAction() == 2"><s:text name="title.jpsurvey.choice.edit" /></s:if>
	</h2>
	<p>
		<s:text name="label.workingOn" />:&#32;
		<em><s:property value="%{getLabel(choice.surveyTitles)}" /></em>, <s:text name="label.workingOn.question" />&#32;<em><s:property value="%{getLabel(choice.questions)}" /></em>
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
	
	<s:form cssClass="tab-container" action="saveChoice">
	
	<%-- HIDDEN --%>
	<p class="noscreen">
	<s:hidden name="choiceId" value="%{choice.id}"/>
	<s:hidden name="questionId" value="%{choice.questionId}" />
	<%-- 
	<s:hidden name="questionnaire" />
	<s:hidden name="surveyId" />
	--%>
	<s:hidden name="strutsAction" />
	</p>
	<%-- FINE HIDDEN --%>
	
	
	<!-- INIZIO ITERAZIONE LOCALIZZAZIONE OPZIONI -->
	<s:iterator id="localizedLang" value="langs">
	<div id="<s:property value="#localizedLang.code" />_tab" class="tab">
		<p>
			<label for="choice-<s:property value="#localizedLang.code" />" class="basic-mint-label">
				<s:if test="choice.questionnaire">
					<s:text name="label.answer" />
				</s:if>
				<s:else>
					<s:text name="jpsurvey_choice" />
				</s:else>:
			</label>
			<s:set name="localization" value="%{getChoices()[#localizedLang.code]}"/>
			<wpsf:textarea useTabindexAutoIncrement="true" name="%{'choice-'+#localizedLang.code}" id="%{'choice-'+#localizedLang.code}" value="%{#localization}" cols="60" rows="3" />
		</p>
	</div>
	</s:iterator>
	<!-- FINE ITERAZIONE LOCALIZZAZIONE OPZIONI -->
	<p class="margin-more-top centerText">
		<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" />
	</p>
	</s:form>
</div>