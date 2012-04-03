<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<wp:headInfo type="CSS" info="../../plugins/jpsurvey/static/css/jpsurvey.css" />

<div class="polls">
<s:set name="surveyInfo" value="surveyInfo"></s:set>

<s:set name="imageURL" value="%{getSurveyImageURL(surveyInfo.imageId,2)}" />
<s:if test="#imageURL != null && #imageURL != '' ">
	<div class="polls_column_left">
		<img alt="<s:property value="%{getLabel(#surveyInfo.imageDescriptions)}" />" src="<s:property value="#imageURL"/>" />
	</div>
	<div class="polls_column_right">
</s:if>
<dl class="left">
	<dt><wp:i18n key="JPSURVEY_TITLE" /></dt>
		<dd><s:property value="%{getLabel(#surveyInfo.titles)}" /></dd>
	<dt><wp:i18n key="JPSURVEY_DESCRIPTION" /></dt>
		<dd><s:property value="%{getLabel(#surveyInfo.descriptions)}" /></dd>
</dl>
<s:if test="#imageURL != null && #imageURL != '' "></div></s:if>
<div class="clear">	
<s:if test="#surveyInfo.questionnaire">
	<p>
	<wp:i18n key="JPSURVEY_SURVEY_STARTDAY" />&#32;<s:date name="#surveyInfo.startDate" format="EEEE dd/MM/yyyy" />
	<s:if test="null != #surveyInfo.endDate ">&#32;
		<s:if test="!#surveyInfo.archive" >
			<wp:i18n key="JPSURVEY_ENDDAY" />&#32;
		</s:if>
		<s:else>
			<wp:i18n key="JPSURVEY_ARCHIVE_ENDDAY" />&#32;
		</s:else>
		<s:date name="#surveyInfo.endDate" format="EEEE dd/MM/yyyy" />
	</s:if>
	</p>
</s:if>
<s:else>
	<p><wp:i18n key="JPSURVEY_POLL_STARTDAY" />&#32;<s:date name="#surveyInfo.startDate" format="EEEE dd/MM/yyyy" />
	<s:if test="null != #surveyInfo.endDate">&#32;
		<s:if test="!#surveyInfo.archive" >
			<wp:i18n key="JPSURVEY_ENDDAY" />&#32;
		</s:if>
		<s:else>
			<wp:i18n key="JPSURVEY_ARCHIVE_ENDDAY" />&#32;
		</s:else>
		<s:date name="#surveyInfo.endDate" format="EEEE dd/MM/yyyy" />
	</s:if></p>
</s:else>

<s:if test="voted">
	<p><wp:i18n key="JPSURVEY_YOU_VOTED" /></p>
		<%--<s:if test="#surveyInfo.questionnaire">VISUALIZZA LA TUA VOTAZIONE</s:if>--%>
</s:if>
<s:else>
	<p><wp:i18n key="JPSURVEY_YOU_NOT_VOTED" /></p>
	<p>
	<s:if test="!#surveyInfo.archive" >
	<a href="<wp:action path="/ExtStr2/do/jpsurvey/Front/Survey/startSurvey.action" ><wp:parameter name="surveyId"><s:property value="#surveyInfo.id" /></wp:parameter></wp:action>" ><wp:i18n key="JPSURVEY_BEGIN" />&#32;
	<s:if test="#surveyInfo.questionnaire"><wp:i18n key="JPSURVEY_SURVEY" /></s:if>
	<s:else><wp:i18n key="JPSURVEY_POLL" /></s:else></a>
	</s:if>
	</p>
</s:else>

<s:if test="!#surveyInfo.questionnaire">
	<s:if test="%{(#surveyInfo.archive && #surveyInfo.publicResult) || (!#surveyInfo.archive && #surveyInfo.publicPartialResult && #surveyInfo.open) }">
		<p>
		<a href="<wp:url page="poll_results"><wp:parameter name="surveyId"><s:property value="#surveyInfo.id" /></wp:parameter></wp:url>">
			<s:if test="%{#surveyInfo.archive}">
				<wp:i18n key="JPSURVEY_FINAL_RESULTS" />
			</s:if>
			<s:else>
				<wp:i18n key="JPSURVEY_PARTIAL_RESULTS" />
			</s:else>
		</a>
		</p>
	</s:if>
</s:if>
</div>
</div>