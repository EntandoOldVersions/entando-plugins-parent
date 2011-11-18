<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<s:if test="question.questionnaire">
	<h1>
		<a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="question.questionnaire"></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.survey.main" />">
		<s:text name="title.jpsurvey.survey.main" />
		</a>
	</h1>
	<div id="main">
		<h2><s:text name="title.jpsurvey.survey.edit" /></h2>
</s:if>
<s:else>
	<h1>
		<a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="question.questionnaire"></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.poll.main" />">
		<s:text name="title.jpsurvey.poll.main" />
		</a>
	</h1>
	<div id="main"> 
	<h2><s:text name="title.jpsurvey.poll.edit" /></h2>
</s:else>
	<p>
		<s:text name="label.workingOn" />:&#32;
		<em><s:property value="%{getLabel(question.surveyTitles)}" /></em>
	</p>
	 
	<h3><s:text name="title.jpsurvey.trash.question" /></h3>
	
	<s:form action="deleteQuestion"> 
		<!-- HIDDEN --> 
		<p class="noscreen">
			<s:hidden name="surveyId" />
			<s:hidden name="questionId" />
		</p>
		
		<%-- warning message --%>
		<p><s:text name="message.questionAction.deleteWarning" /></p>
	
		<%-- do you really want to delete? --%>
		<p>
			<s:text name="jpsurvey_delete_confirm" />&#32;<s:text name="jpsurvey_the_question" />&#32;
			<em><s:property value="%{getLabel(question.questions)}"/></em>
			<%-- &#32;(<s:text name="jpsurvey_id" /><s:property value="%{questionId}" />)&#32; --%>
			<s:text name="jpsurvey_delete_confirm_from" />&#32;
			<em><s:property value="%{getLabel(question.surveyTitles)}"/></em>?&#32;
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.jpsurvey.delete')}" cssClass="button" />
		</p>
	
		<%-- back link to survey edit --%>
		<p>
			<s:text name="jpsurvey_delete_go_back" />&#32;
			<a href="<s:url action="editSurvey" namespace="/do/jpsurvey/Survey"><s:param name="surveyId" value="surveyId" /></s:url>">
				<s:if test="question.questionnaire"><s:text name="title.jpsurvey.survey.edit" /></s:if><s:else><s:text name="title.jpsurvey.poll.edit" /></s:else>
			</a>
		</p>
	</s:form>
</div>