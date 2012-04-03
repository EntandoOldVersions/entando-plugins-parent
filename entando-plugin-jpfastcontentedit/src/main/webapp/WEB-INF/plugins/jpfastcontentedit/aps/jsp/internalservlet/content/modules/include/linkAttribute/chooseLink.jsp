<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<wp:i18n key="jpfastcontentedit_FASTCONTENTEDIT_SHOWLET_TITLE" />
<br />

<s:include value="linkAttributeConfigIntro.jsp" />
<h3><wp:i18n key="jpfastcontentedit_CONFIGURE_LINK_ATTRIBUTE" />&#32;<wp:i18n key="jpfastcontentedit_STEP_1_OF_2" /></h3>
<s:include value="linkAttributeConfigReminder.jsp" />
<p>
	<wp:i18n key="jpfastcontentedit_CHOOSE_LINK_TYPE" />:
</p>

<form action="<wp:action path="/ExtStr2/do/jpfastcontentedit/Content/Link/configLink.action" />" method="post">

<s:if test="hasFieldErrors()">
<h3><wp:i18n key="jpfastcontentedit_ERRORS" /></h3>
<ul>
	<s:iterator value="fieldErrors">
		<s:iterator value="value">
            <li><s:property escape="false" /></li>
		</s:iterator>
	</s:iterator>
</ul>
</s:if>
<ul>

<s:iterator id="typeId" value="linkDestinations">
<s:if test="#typeId != 4">
	
	<s:if test="#typeId == 1">
		<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/link-url.png</s:set>
		<s:set name="linkDestination" ><wp:i18n key="jpfastcontentedit_LINK_TO_URL"/></s:set>
	</s:if>
	<s:if test="#typeId == 2">
		<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/link-page.png</s:set>
		<s:set name="linkDestination" ><wp:i18n key="jpfastcontentedit_LINK_TO_PAGE"/></s:set>
	</s:if>
	
	<s:if test="#typeId == 3">
		<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/link-content.png</s:set>
		<s:set name="linkDestination" ><wp:i18n key="jpfastcontentedit_LINK_TO_CONTENT"/></s:set>
	</s:if>
	
	<li><input type="radio" <s:if test="#typeId == symbolicLink.destType">checked="checked"</s:if> name="linkType" id="linkType_<s:property value="#typeId"/>" value="<s:property value="#typeId"/>" /><label for="linkType_<s:property value="#typeId"/>"><img src="<s:property value="iconImagePath" />" alt=" " /> <s:property value="linkDestination" /></label></li>
	
</s:if>	
</s:iterator>

</ul>

<s:set name="continue_label" ><wp:i18n key="jpfastcontentedit_CONTINUE"/></s:set>
<p><wpsf:submit useTabindexAutoIncrement="true" value="%{#continue_label}" title="%{#continue_label}" cssClass="button" /></p>

</form>