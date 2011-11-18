<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<wp:headInfo type="CSS" info="../../plugins/jpsurvey/static/css/jpsurvey.css" />

<s:set name="surveyInfo" value="voterResponse.survey"></s:set>
<div class="polls">
<p class="questionPoll_title"><wp:i18n key="JPSURVEY_QUESTION" />&#32;<s:property value="currentQuestionIndex+1" />&#32;<wp:i18n key="JPSURVEY_QUESTION_OF" />&#32;<s:property value="%{#surveyInfo.questions.size()}" /></p>

<form action="<wp:action path="/ExtStr2/do/jpsurvey/Front/Survey/saveResponse.action" />" method="post" enctype="multipart/form-data" class="clear pluginsForm">

<s:if test="hasFieldErrors()">
	<h3><s:text name="message.title.FieldErrors" /></h3>
	<ul>
		<s:iterator value="fieldErrors">
			<s:iterator value="value">
	            <li><s:property/></li>
			</s:iterator>
		</s:iterator>
	</ul>
</s:if>

<s:if test="hasActionErrors()">
	<h3><s:text name="message.title.ActionErrors" /></h3>
	<ul>
		<s:iterator value="actionErrors">
			<li><s:property/></li>
		</s:iterator>
	</ul>
</s:if>

<s:set name="currentQuestion" value="currentQuestion" ></s:set>

<dl>
	<dt><wp:i18n key="JPSURVEY_TITLE" /></dt>
		<dd><s:property value="%{getLabel(#surveyInfo.titles)}" /></dd>

<s:if test="!#currentQuestion.singleChoice">
	<dt><wp:i18n key="JPSURVEY_MIN_ANSWERS" /></dt>
		<dd><s:property value="#currentQuestion.minResponseNumber" /></dd>
	<dt><wp:i18n key="JPSURVEY_MAX_ANSWERS" /></dt>
		<dd><s:property value="#currentQuestion.maxResponseNumber" /></dd>
</s:if>
	<dt><wp:i18n key="JPSURVEY_QUESTION" /></dt>
		<dd><s:property value="%{getLabel(#currentQuestion.questions)}" /></dd>
	
	<dt><wp:i18n key="JPSURVEY_ANSWERS" /></dt>
	<dd>
		<s:iterator id="choice" value="#currentQuestion.choices" status="rowstatus" >
		
		<s:if test="#currentQuestion.singleChoice">
			<p><input type="radio" name="choiceIds" value="<s:property value="#choice.id" />" id="choiceIds-<s:property value="#rowstatus.index"/>" <s:if test="choiceIds.contains(#choice.id)">checked="checked"</s:if> />
		</s:if>
		<s:else>
			<p><input type="checkbox" name="choiceIds" value="<s:property value="#choice.id" />" id="choiceIds-<s:property value="#rowstatus.index"/>" <s:if test="choiceIds.contains(#choice.id)">checked="checked"</s:if> />
		</s:else>
		
		<s:if test="#choice.freeText">
		<label for="choiceIds-<s:property value="#rowstatus.index"/>" ><wp:i18n key="JPSURVEY_FREE_TEXT" /></label></p>
		<p>
		<label for="insertedFreeText" ><wp:i18n key="JPSURVEY_INSERT_FREE_TEXT" /></label><br />
		<wpsf:textfield useTabindexAutoIncrement="true" name="insertedFreeText" id="insertedFreeText" cssClass="text" /></p></s:if>
		<s:else>
			<label for="choiceIds-<s:property value="#rowstatus.index"/>" ><s:property value="%{getLabel(#choice.choices)}" /></label></p>
		</s:else>
		
		</s:iterator>
	</dd>
</dl>
	
<wpsa:set name="labelSubmit"><wp:i18n key="JPSURVEY_GO" /></wpsa:set>
<p><wpsf:submit useTabindexAutoIncrement="true" value="%{#labelSubmit}" cssClass="button" /></p>


</form>
</div>