<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>

<wp:i18n key="jpfastcontentedit_FASTCONTENTEDIT_SHOWLET_TITLE" />
<br />

<p><wp:i18n key="jpfastcontentedit_GENERIC_ERROR" /></p>

<s:if test="hasActionErrors()">
	<h3><wp:i18n key="jpfastcontentedit_ERRORS" /></h3>
	<ul>
	<s:iterator value="actionErrors">
		<li><s:property escape="false" /></li>
	</s:iterator>
	</ul>
</s:if>