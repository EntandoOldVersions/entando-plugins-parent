<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<s:set name="currentLang" value="%{getCurrentLang().code}" />
<h1><a href="<s:url action="listSurveys" ><s:param name="questionnaire" value="questionnaire"></s:param></s:url>" title="<s:text name="note.goToSomewhere" />: <s:text name="title.jpsurvey.survey.main" />"><s:text name="title.jpsurvey.survey.main" /></a></h1>
<div id="main">
	<s:if test="%{freeTextMap != NULL && freeTextMap.size() > 0}">
		<h2><s:text name="jpsurvey_freeText_results" /></h2>	
		<table class="generic" summary="<s:text name="jpsurvey_freeText_results_summary" />">
			<caption><span><s:text name="jpsurvey_freeText_results_caption" /></span></caption>
			<tr>
				<th><s:text name="jpsurvey_freeText_answers" /></th>
				<th><s:text name="jpsurvey_freeText_occurences" /></th>		
			</tr>
			<s:iterator id="currentFreeText" value="freeTextMap">
			<tr>
				<td><s:property value="#currentFreeText.key" /></td>
				<td class="centerText"><s:property value="#currentFreeText.value" /></td>
			</tr>
			</s:iterator>
		</table>
	</s:if>
	<s:else>
		<p><s:text name="jpsurvey_freeText_noAnswers" /></p>
	</s:else>
</div>

<%-- FARE UN BOTTONE / LINK INDIETRO? 
<s:hidden name="choiceId" />
<s:hidden name="questionId" />
<s:hidden name="surveyId" />
<s:hidden name="questionnaire" />
--%>