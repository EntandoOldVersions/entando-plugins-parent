<%@ page contentType="charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<jacms:contentInfo param="authToEdit" var="canEditThisVar" />
<jacms:contentInfo param="contentId" var="contentIdVar" />
<c:if test="${canEditThisVar}">
<span class="widget_noscreen">
	<div class="bar-content-edit">
		<p>
			<a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/jacms/Content/edit.action?contentId=<c:out value="${contentIdVar}" />" title="<wp:i18n key="EDIT_THIS_CONTENT" />">
			&dArr;
			<img src="<wp:resourceURL />administration/common/img/icons/edit-content.png" width="22" height="22" alt="<wp:i18n key="EDIT_THIS_CONTENT" />" />
			&dArr;
			</a>
		</p>
	</div>
</span>
</c:if>
<jacms:content publishExtraTitle="true" />

<c:if test="${canEditThisVar}">
<wp:info key="systemParam" paramName="jpfrontshortcut_activeContentFrontEndEditing" var="activeContentEditingVar" />
<c:if test="${null != activeContentEditingVar && activeContentEditingVar  == 'true'}">
<!-- modal popup start-->
<p class="vai">
<a id="options_anchor_<c:out value="${contentIdVar}" />" href="javascript:void(0)"><wp:i18n key="EDIT_CONTENT" /></a>
<script type='text/javascript'>
jQuery(document).ready(function () { 
jQuery.struts2_jquery.bind(jQuery('#options_anchor_<c:out value="${contentIdVar}" />'),{
"opendialog": "widgetDialog",
"jqueryaction": "anchor",
"id": "anchor_config_<c:out value="${contentIdVar}" />",
"href": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpfrontshortcut/Content/introView?contentId=<c:out value="${contentIdVar}" />&modelId=<jacms:contentInfo param="modelId" />&request_locale=<wp:info key="currentLang" />&langCode=<wp:info key="currentLang" />",
"button": false
});
 });  
</script>
</p>
<!-- modal popup end -->
</c:if>
</c:if>