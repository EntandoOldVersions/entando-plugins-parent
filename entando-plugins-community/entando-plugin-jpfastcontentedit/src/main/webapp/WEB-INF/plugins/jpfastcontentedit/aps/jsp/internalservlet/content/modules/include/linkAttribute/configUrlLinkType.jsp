<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<wp:i18n key="jpfastcontentedit_FASTCONTENTEDIT_SHOWLET_TITLE" />
<br />

<s:include value="linkAttributeConfigIntro.jsp"></s:include>
<h3><wp:i18n key="jpfastcontentedit_CONFIGURE_LINK_ATTRIBUTE" />&#32;<wp:i18n key="jpfastcontentedit_STEP_2_OF_2" /></h3>
<s:include value="linkAttributeConfigReminder.jsp"></s:include>
<p><wp:i18n key="jpfastcontentedit_CONFIGURE_URL" /></p>

<form action="<wp:action path="/ExtStr2/do/jpfastcontentedit/Content/Link/joinUrlLink.action" />" method="post" >

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
<p>
	<label for="url"><wp:i18n key="jpfastcontentedit_URL" />: </label><br />
	<wpsf:textfield useTabindexAutoIncrement="true" name="url" id="url" cssClass="text"/>
</p>

<s:set name="confirm_label" ><wp:i18n key="jpfastcontentedit_CONFIRM"/></s:set>
<p><wpsf:submit useTabindexAutoIncrement="true" value="%{#confirm_label}" title="%{#confirm_label}" cssClass="button" /></p>
</form>