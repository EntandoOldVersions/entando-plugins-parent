<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="manageSurvey">
	<li><a href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="true"></s:param></s:url>" id="menu_jpsurvey" ><s:text name="jpsurvey.label.questionnaires" /></a></li>
	<li><a href="<s:url action="listSurveys" namespace="/do/jpsurvey/Survey" ><s:param name="questionnaire" value="false"></s:param></s:url>" id="menu_jpsurvey_polls" ><s:text name="jpsurvey.label.polls" /></a></li>
</wp:ifauthorized>