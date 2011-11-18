<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="jpsu" uri="/jpsurvey-aps-core" %>

<wp:headInfo type="CSS" info="../../plugins/jpsurvey/static/css/jpsurvey.css" />

<s:set name="surveyInfo" value="surveyInfo"></s:set>
<div class="polls">
<s:if test="#surveyInfo.questionnaire">
<jpsu:pageWithShowlet var="jpsurvey_surveysListPage" showletTypeCode="jpsurvey_questionnaireList" />
<p><wp:i18n key="JPSURVEY_THANKS_FOR" />&#32;<a href="<wp:url page="${jpsurvey_surveysListPage.code}"></wp:url>" title="<wp:i18n key="JPSURVEY_GO_TO_ACTIVE_QUESTIONNAIRE" />"><wp:i18n key="JPSURVEY_ACTIVE_QUESTIONNAIRE" /></a>.</p>
</s:if>
<s:else>
<jpsu:pageWithShowlet var="jpsurvey_pollsListPage" showletTypeCode="jpsurvey_pollList" />
<p><wp:i18n key="JPSURVEY_THANKS_FOR" />&#32;<a href="<wp:url page="${jpsurvey_pollsListPage.code}"></wp:url>" title="<wp:i18n key="JPSURVEY_GO_TO_ACTIVE_POLLS" />"><wp:i18n key="JPSURVEY_GO_ACTIVE_POLLS" /></a>.</p>
</s:else>
</div>