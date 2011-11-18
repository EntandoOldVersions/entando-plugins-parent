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
	<h2><s:text name="title.jpsurvey.question.edit" /></h2>
	
	<p>
		<s:text name="label.workingOn" />:&#32;
		<em><s:property value="%{getLabel(choice.surveyTitles)}"/></em>
	</p>
	
	<h3><s:text name="title.jpsurvey.trash.choice" /></h3>
	
	<s:form action="deleteChoice">
		<p class="noscreen">
			<s:hidden name="questionId" value="%{choice.questionId}" />
			<s:hidden name="choiceId" />
			<%-- 
			<s:hidden name="questionnaire" />
			<s:hidden name="surveyId" />
			<s:hidden name="deletingElement" />
			--%>
		</p>
	
		<p>
			<s:text name="jpsurvey_delete_confirm" />&#32;
	
			<s:if test="!choice.freeText">
				<s:text name="jpsurvey_the_answer" />:&#32;
				<em class="important"><s:property value="%{getLabel(choice.choices)}"/></em>
			</s:if>
		
			<s:else>
				<span class="important"><s:text name="jpsurvey_the_free_text" /></span>
			</s:else>
			
			&#32;<s:text name="jpsurvey_delete_confirm_from" />&#32;
			<em><s:property value="%{getLabel(choice.questions)}"/></em>?&#32; 
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.jpsurvey.delete')}" cssClass="button" />
		</p>
		
		<p>
			<s:text name="jpsurvey_delete_choice_go_back" />&#32;
			<a href="<s:url action="editQuestion" namespace="/do/jpsurvey/Survey"><s:param name="questionId" value="choice.questionId" /></s:url>">
				<s:text name="title.jpsurvey.question.edit" />
			</a>
		</p>
	</s:form>
</div>