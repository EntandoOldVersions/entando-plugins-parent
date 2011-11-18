<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<p class="buttons">
<s:if test="%{previousStep != null}">
	<wpsa:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>plugins/jpcontentworkflow/administration/common/img/icons/32x32/previous.png</wpsa:set>
	<wpsf:submit useTabindexAutoIncrement="true" action="previousStep" type="image" src="%{#iconImagePath}" value="%{getText('label.previousStep')}" title="%{getText('note.button.previousStep')}" />
</s:if>

	<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/32x32/save.png</s:set>
	<wpsf:submit useTabindexAutoIncrement="true" action="save" type="image" src="%{#iconImagePath}" value="%{getText('label.save')}" title="%{getText('note.button.saveContent')}" />

<s:if test="%{nextStep != null}">
	<wpsa:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>plugins/jpcontentworkflow/administration/common/img/icons/32x32/next.png</wpsa:set>
	<wpsf:submit useTabindexAutoIncrement="true" action="nextStep" type="image" src="%{#iconImagePath}" value="%{getText('label.nextStep')}" title="%{getText('note.button.nextStep')}" />
</s:if>

<wp:ifauthorized permission="validateContents">
<s:if test="%{nextStep == null}">
	<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/32x32/approve.png</s:set>
	<wpsf:submit useTabindexAutoIncrement="true" action="saveAndApprove" type="image" src="%{#iconImagePath}" value="%{getText('label.saveAndApprove')}" title="%{getText('note.button.saveAndApprove')}" />
</s:if>
<s:if test="content.onLine">
	<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/32x32/suspend.png</s:set>
	<wpsf:submit useTabindexAutoIncrement="true" action="suspend" type="image" src="%{#iconImagePath}" value="%{getText('label.suspend')}" title="%{getText('note.button.suspend')}" />
</s:if>
</wp:ifauthorized>
</p>