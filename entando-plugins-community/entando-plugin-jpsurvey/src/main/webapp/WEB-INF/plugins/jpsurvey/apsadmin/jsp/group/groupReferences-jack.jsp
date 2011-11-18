<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="references['jpsurveySurveyManagerUtilizers']">
<h3><s:text name="jpsurvey.message.title.referencedSurveys" /></h3>
<ul>
<s:iterator var="survey" value="references['jpsurveySurveyManagerUtilizers']">
	<li><s:property value="#survey.id" /> - <s:property value="%{getTitle(#survey.id, #survey.titles)}" /></li>
</s:iterator>
</ul>
</s:if>