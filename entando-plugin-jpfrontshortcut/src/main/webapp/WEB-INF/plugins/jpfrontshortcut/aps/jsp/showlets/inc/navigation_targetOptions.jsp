<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpcs" uri="/jpfrontshortcut-aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${sessionScope.currentUser != 'guest'}">
<wp:pageInfo pageCode="${currentTarget.code}" info="owner" var="currentPageOwner" />
<wp:ifauthorized permission="managePages" groupName="${currentPageOwner}">

<wp:info key="systemParam" paramName="jpfrontshortcut_activePageFrontEndEditing" var="activePageEditingVar" />
<c:if test="${null != activePageEditingVar && activePageEditingVar  == 'true'}">

<wp:info key="currentLang" var="currentLang" />
<wp:currentPage param="code" var="currentViewCode" />
<wp:pageInfo pageCode="${currentTarget.code}" info="code" var="targetPageCode" />
<wp:pageInfo pageCode="${currentTarget.code}" info="hasChild" var="targetPageHasChildVar" />
<wpcs:requestContextParam param="currentFrame" var="framePosVar" />
	<!-- NEW PAGE -->
	<a id="options_anchor_NEW_PAGE_<c:out value="${targetPageCode}" />_<c:out value="${framePosVar}" />" href="javascript:void(0)" title="New page from <c:out value="${currentTarget.title}" />">
	<img width="12" height="12" src="<wp:resourceURL />plugins/jpfrontshortcut/static/img/icons/16x16/new.png" alt="New page from <c:out value="${currentTarget.title}" />" /></a>
	<script type='text/javascript'>
	jQuery(document).ready(function () { 
	jQuery.struts2_jquery.bind(jQuery('#options_anchor_NEW_PAGE_<c:out value="${targetPageCode}" />_<c:out value="${framePosVar}" />'),{
	"opendialog": "widgetDialog",
	"jqueryaction": "anchor",
	"id": "anchor_config_newPage_<c:out value="${targetPageCode}" />",
	"href": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpfrontshortcut/Page/new?langCode=<c:out value="${currentLang}" />&selectedNode=<c:out value="${targetPageCode}" />",
	"button": false
	});
	});  
	</script>
	
	<!-- EDIT PAGE -->
	<a id="options_anchor_EDIT_PAGE_<c:out value="${targetPageCode}" />_<c:out value="${framePosVar}" />" href="javascript:void(0)" title="Edit page <c:out value="${currentTarget.title}" />">
	<img width="12" height="12" src="<wp:resourceURL />plugins/jpfrontshortcut/static/img/icons/16x16/edit.png" alt="Edit page <c:out value="${currentTarget.title}" />" /></a>
	<script type='text/javascript'>
	jQuery(document).ready(function () { 
	jQuery.struts2_jquery.bind(jQuery('#options_anchor_EDIT_PAGE_<c:out value="${targetPageCode}" />_<c:out value="${framePosVar}" />'),{
	"opendialog": "widgetDialog",
	"jqueryaction": "anchor",
	"id": "anchor_config_editPage_<c:out value="${targetPageCode}" />",
	"href": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpfrontshortcut/Page/edit?langCode=<c:out value="${currentLang}" />&selectedNode=<c:out value="${targetPageCode}" />",
	"button": false
	});
	});  
	</script>
	
	<c:if test="${currentViewCode != targetPageCode && !targetPageHasChildVar}">
	<!-- DELETE PAGE -->
	<a id="options_anchor_DELETE_PAGE_<c:out value="${targetPageCode}" />_<c:out value="${framePosVar}" />" href="javascript:void(0)" title="Delete page <c:out value="${currentTarget.title}" />">
	<img width="12" height="12" src="<wp:resourceURL />plugins/jpfrontshortcut/static/img/icons/16x16/delete.png" alt="Delete page <c:out value="${currentTarget.title}" />" /></a>
	<script type='text/javascript'>
	jQuery(document).ready(function () { 
	jQuery.struts2_jquery.bind(jQuery('#options_anchor_DELETE_PAGE_<c:out value="${targetPageCode}" />_<c:out value="${framePosVar}" />'),{
	"opendialog": "widgetDialog",
	"jqueryaction": "anchor",
	"id": "anchor_config_deletePage_<c:out value="${targetPageCode}" />",
	"href": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpfrontshortcut/Page/trash?langCode=<c:out value="${currentLang}" />&selectedNode=<c:out value="${targetPageCode}" />",
	"button": false
	});
	});  
	</script>
	</c:if>
</c:if>
</wp:ifauthorized>

</c:if>