<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpcs" uri="/jpfrontshortcut-aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% pageContext.setAttribute("random", (int) (Math.random() * 999999)); %>
<wp:headInfo type="CSS" info="../../plugins/jpfrontshortcut/static/css/jpfrontshortcut-editcontent.css" />
<wp:headInfo type="CSS" info="../../plugins/jpfrontshortcut/static/css/jpfrontshortcut-frameconfig.css" />
<wp:headInfo type="JS" info="../../plugins/jpfrontshortcut/static/js/ckeditor/ckeditor.js" />
<wpcs:staticInternalServlet actionPath="/ExtStr2/do/jpfrontshortcut/introHeader" />
<wp:info key="systemParam" paramName="jpfrontshortcut_activeFrameFrontEndEditing" var="frontEditingActiveVar" /> 
<c:if test="${null != frontEditingActiveVar && frontEditingActiveVar  == 'true'}">
	<c:set var="showletCodeVar" value="${null}" />
	<wp:currentPage param="owner" var="ownerGroupVar" />
	<wp:ifauthorized permission="managePages" groupName="${ownerGroupVar}">
		<wp:info key="currentLang" var="currentLang" />
		<wp:currentPage param="code" var="currentPageCode" />
		<wpcs:requestContextParam param="currentFrame" var="framePosVar" />
		<%-- EDIT FRAME --%>
			[<c:out value="${framePosVar}" />
				<a id="<c:out value="anchor_config_EDIT_FRAME_${framePosVar}_${random}" />" href="javascript:void(0)" title="Edit frame <c:out value="${framePosVar}" />"><img width="16" height="16" src="<wp:resourceURL />plugins/jpfrontshortcut/static/img/icons/16x16/edit.png" alt="Edit frame <c:out value="${framePosVar}" />" /></a>
				<script type='text/javascript'>
					<!--//--><![CDATA[//><!--
					jQuery(document).ready(function () { 
						jQuery.struts2_jquery.bind(jQuery('#<c:out value="anchor_config_EDIT_FRAME_${framePosVar}_${random}" />'),{
							"opendialog": "widgetDialog",
							"jqueryaction": "anchor",
							"id": "<c:out value="anchor_config_EDIT_FRAME_${framePosVar}_${random}" />",
							"href": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpfrontshortcut/Page/editFrame?langCode=<c:out value="${currentLang}" />&pageCode=<c:out value="${currentPageCode}" />&frame=<c:out value="${framePosVar}" />",
							"button": false
						});
					});
					//--><!]]>
				</script>
		<wp:currentShowlet param="code" var="showletCodeVar" />
		<%-- TRASH SHOWLET --%>
			<c:if test="${null != showletCodeVar}" >
				<a id="<c:out value="options_anchor_TRASH_SHOWLET_${framePosVar}_${random}" />" href="javascript:void(0)" title="Remove Showlet from frame <c:out value="${framePosVar}" />"><img width="16" height="16" src="<wp:resourceURL />plugins/jpfrontshortcut/static/img/icons/16x16/clear.png" alt="Remove Showlet from frame <c:out value="${framePosVar}" />" /></a>
				<script type='text/javascript'>
					<!--//--><![CDATA[//><!--
					jQuery(document).ready(function () { 
						jQuery.struts2_jquery.bind(jQuery('#<c:out value="options_anchor_TRASH_SHOWLET_${framePosVar}_${random}" />'),{
							"opendialog": "widgetDialog",
							"jqueryaction": "anchor",
							"id": "<c:out value="options_anchor_TRASH_SHOWLET_${framePosVar}_${random}" />",
							"href": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpfrontshortcut/Page/trashShowletFromPage?langCode=<c:out value="${currentLang}" />&pageCode=<c:out value="${currentPageCode}" />&frame=<c:out value="${framePosVar}" />",
							"button": false
						});
					});  
					//--><!]]>
				</script>
		</c:if>
		]
	</wp:ifauthorized>
</c:if>