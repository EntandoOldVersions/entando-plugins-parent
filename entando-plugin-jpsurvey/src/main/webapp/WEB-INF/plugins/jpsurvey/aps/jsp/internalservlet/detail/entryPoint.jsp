<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<wp:headInfo type="CSS" info="../../plugins/jpsurvey/static/css/jpsurvey.css" />

<div class="polls">
<s:set name="survey" value="survey"></s:set>

<s:set name="imageURL" value="%{getSurveyImageURL(survey.imageId,2)}" />
<s:if test="#imageURL != null && #imageURL != '' ">
	<div class="polls_column_left">
		<img alt="<s:property value="%{getLabel(#survey.imageDescriptions)}" />" src="<s:property value="#imageURL"/>" />
	</div>
	<div class="polls_column_right">
</s:if>
<dl class="left">
	<dt><wp:i18n key="JPSURVEY_TITLE" /></dt>
		<dd><s:property value="%{getLabel(#survey.titles)}" /></dd>
	<dt><wp:i18n key="JPSURVEY_DESCRIPTION" /></dt>
		<dd><s:property value="%{getLabel(#survey.descriptions)}" /></dd>
</dl>
<s:if test="#imageURL != null && #imageURL != '' "></div></s:if>
<div class="clear">
<s:if test="voted">
	<p><wp:i18n key="JPSURVEY_YOU_VOTED" /></p>
		<%--<s:if test="#survey.questionnaire">VISUALIZZA LA TUA VOTAZIONE</s:if>--%>
</s:if>
<s:else>
	<p><wp:i18n key="JPSURVEY_YOU_NOT_VOTED" /></p>
</s:else>

<p><wp:i18n key="JPSURVEY_VOTED_TOT" />&#32;<s:property value="%{getTotalVoters(#survey.id)}" />&#32;<wp:i18n key="JPSURVEY_PERSON" /></p>


<s:iterator id="question" value="#survey.questions" status="questionIndex">
<s:set name="occurrences" value="%{getQuestionStatistics(#question.id)}" ></s:set>
	<h4 class="borderTop"><wp:i18n key="JPSURVEY_QUESTION" />&#32;<s:property value="#questionIndex.index + 1"/></h4>
	<p><s:property value="%{getLabel(#question.questions)}" /></p>
	<h4><wp:i18n key="JPSURVEY_ANSWERS" /></h4>
	<ol>
		<s:iterator id="choice" value="#question.choices" status="rowstatus" >	
		<li><s:property value="%{getLabel(#choice.choices)}" /></li>
		</s:iterator>
	</ol>
	<div class="graphic">
	<dl class="graph">		
		<s:iterator id="choice" value="#question.choices" status="rowstatus" >	
			<dt><s:property value="%{getLabel(#choice.choices)}" /></dt>
				<s:set name="occurrence" value="#occurrences[#choice.id]" />
				<s:set name="roundedPercentage" value="%{getChoicePercentage(#occurrences, #choice.id)}" />
				
				<s:if test="#occurrence && (#roundedPercentage > 20)">
					<dd>
					<span class="p<s:text name="format.roundedPercentage"><s:param name="value" value="#roundedPercentage"/></s:text>">
					<span class="noscreen"><wp:i18n key="JPSURVEY_ANSWER_NUMBER" /></span>&#32;<em class="leftEm"><s:property value="#rowstatus.index + 1"/></em>&#32;<span class="noscreen"><wp:i18n key="JPSURVEY_OBTAINED" /></span>&#32;<em class="rightEm"><s:property value="#occurrence" />&#32;<wp:i18n key="JPSURVEY_VOTES" /></em></span>
					</dd>
				</s:if>
				<s:elseif test="#occurrence && (#roundedPercentage < 20)">
					<dd>
					<span class="p<s:text name="format.roundedPercentage"><s:param name="value" value="#roundedPercentage"/></s:text>">
					<span class="noscreen"><wp:i18n key="JPSURVEY_ANSWER_NUMBER" /></span>&#32;<em class="leftEm"><s:property value="#rowstatus.index + 1"/></em>&#32;<span class="noscreen"><wp:i18n key="JPSURVEY_OBTAINED" /></span></span>&#32;<em class="rightEmZero"><s:property value="#occurrence" />&#32;<wp:i18n key="JPSURVEY_VOTES" /></em>
					</dd>
				</s:elseif>
				<s:else>
					<dd>
					<span class="p0"><span class="noscreen"><wp:i18n key="JPSURVEY_ANSWER_NUMBER" /></span>&#32;<em class="leftEm"><s:property value="#rowstatus.index + 1"/></em></span>&#32;<span class="noscreen"><wp:i18n key="JPSURVEY_OBTAINED" /></span>&#32;<em class="rightEmZero">0 &#32;<wp:i18n key="JPSURVEY_VOTES" /></em>
					</dd>
				</s:else>
		</s:iterator>		 
	</dl>
	</div>

</s:iterator>
</div>
</div>