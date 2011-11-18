<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1>
	<s:if test="survey.questionnaire">
		<a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="survey.questionnaire"></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.survey.main" />"><s:text name="title.jpsurvey.survey.main" /></a>
	</s:if>
	<s:else>
		<a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="survey.questionnaire"></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.poll.main" />"><s:text name="title.jpsurvey.poll.main" /></a>
	</s:else>
</h1>
<div id="main">
	<s:if test="survey.questionnaire">
		<h2><s:text name="title.jpsurvey.trash.survey" /></h2>
		<s:set name="surveyType" value="%{getText('message.jpsurvey.survey.type')}"/>
	</s:if>
	
	<s:else>
		<h2><s:text name="title.jpsurvey.trash.poll" /></h2>
		<s:set name="surveyType" value="%{getText('message.jpsurvey.poll.type')}"/>
	</s:else>
	
	<s:form>
		<p class="noscreen">
			<s:hidden name="questionnaire" value="%{survey.questionnaire}"/>
			<s:hidden name="surveyId" />
		</p>
		<%-- FINE HIDDEN --%>
		
		<p><s:text name="message.surveyAction.deleteWarning" /></p>
		
		<p>
			<s:text name="jpsurvey_delete_confirm" />&#32;
			<%-- <s:property value="%{surveyType}" />&#32; --%>
			<em class="important"><s:property value="%{getLabel(survey.titles)}"/></em>&#32;?&#32;
			<%-- (<s:text name="jpsurvey_id" />&#32;<s:property value="%{surveyId}" />)? --%>
			<wpsf:submit useTabindexAutoIncrement="true" action="deleteSurvey" value="%{getText('label.jpsurvey.delete')}" cssClass="button" />
		</p>
			
		<p>
			<s:text name="jpsurvey_delete_survey_go_back" />&#32;
			<a href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey"><s:param name="questionnaire" value="survey.questionnaire"/></s:url>">
				<s:if test="survey.questionnaire">
					<s:text name="title.jpsurvey.survey.main" />
				</s:if>
				<s:else>
					<s:text name="title.jpsurvey.poll.main" />
				</s:else>
			</a>
		</p>
	</s:form>
	
</div>