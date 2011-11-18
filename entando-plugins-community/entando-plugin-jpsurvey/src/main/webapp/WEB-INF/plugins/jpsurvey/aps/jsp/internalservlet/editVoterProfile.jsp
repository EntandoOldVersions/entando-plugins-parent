<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<wp:headInfo type="CSS" info="../../plugins/jpsurvey/static/css/jpsurvey.css" />
<div class="polls">
<s:set name="surveyInfo" value="voterResponse.survey"></s:set>

<s:set name="imageURL" value="%{getSurveyImageURL(surveyInfo.imageId,2)}" />
<s:if test="#imageURL != null && #imageURL != '' ">
	<div class="polls_column_left">
		<img alt="<s:property value="%{getLabel(#surveyInfo.imageDescriptions)}" />" src="<s:property value="#imageURL"/>" />
	</div>
	<div class="polls_column_right">
</s:if>
<dl class="left">
	<dt><wp:i18n key="JPSURVEY_TITLE" />:</dt>
	<dd><s:property value="%{getLabel(#surveyInfo.titles)}" /></dd>
	<dt><wp:i18n key="JPSURVEY_DESCRIPTION" />:</dt>
	<dd><s:property value="%{getLabel(#surveyInfo.descriptions)}" /></dd>
</dl>
<s:if test="#imageURL != null && #imageURL != '' "></div></s:if>
<div class="clear">
<p><wp:i18n key="JPSURVEY_PROFILE_NEEDED" /></p>

<form action="<wp:action path="/ExtStr2/do/jpsurvey/Front/Survey/saveVoterProfile.action" />" method="post" enctype="multipart/form-data" class="pluginsForm">
	
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


<p>
	<label for="age"><wp:i18n key="JPSURVEY_AGE" />:</label><br />
	<wpsf:textfield useTabindexAutoIncrement="true" name="age" id="age" cssClass="text" />
</p>

<p>
	<label for="country"><wp:i18n key="JPSURVEY_COUNTRY" />:</label><br />
	<wpsf:textfield useTabindexAutoIncrement="true" name="country" id="country" cssClass="text" />
</p>

<p>
	<label for="sex"><wp:i18n key="JPSURVEY_SEX" />:</label><br />
	<wpsf:select useTabindexAutoIncrement="true" name="sex" id="sex" list="#{'M':getText('label.sex.male'),'F':getText('label.sex.female')}" cssClass="text" />
</p>

<p>
	<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" />
</p>

</form>
</div>
</div>