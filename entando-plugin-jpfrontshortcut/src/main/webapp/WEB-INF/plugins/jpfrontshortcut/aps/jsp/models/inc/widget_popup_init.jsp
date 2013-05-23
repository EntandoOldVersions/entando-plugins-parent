<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%-- js escape setup //start --%>
<% pageContext.setAttribute("carriageReturn", "\r");%> 
<% pageContext.setAttribute("newLine", "\n");%> 
<c:set var="singleQuotes">'</c:set>
<c:set var="singleQuotesReplace">\'</c:set>
<c:set var="doubleQuotes">"</c:set>
<c:set var="doubleQuotesReplace">\"</c:set>
<%-- js escape setup //end --%>
<%-- your string --%>
<wp:i18n key="jpfrontshortcut_POPUP_TITLE" var="WIDGET_EDIT_POPUP_TITLE" />
<c:set var="WIDGET_EDIT_POPUP_TITLE" value="${fn:replace(fn:replace(fn:replace(fn:replace(WIDGET_EDIT_POPUP_TITLE,carriageReturn,' '),newLine,' '), singleQuotes, singleQuotesReplace),doubleQuotes,doubleQuotesReplace)}" />
<div id="widgetDialog"></div>
<script type='text/javascript'>
	<!--//--><![CDATA[//><!--
	jQuery(document).ready(function() {
		var options_widgetDialog = {};
		options_widgetDialog.minWidth = 500;
		options_widgetDialog.title = "<c:out value="${WIDGET_EDIT_POPUP_TITLE}" escapeXml="false" />";
		options_widgetDialog.autoOpen = false;
		options_widgetDialog.modal = true;
		options_widgetDialog.closetopics = "<wp:info key="systemParam" paramName="applicationBaseURL" />dialog/close";
		options_widgetDialog.jqueryaction = "dialog";
		options_widgetDialog.id = "widgetDialog";
		options_widgetDialog.href = "#";
		jQuery.struts2_jquery_ui.bind(jQuery('#widgetDialog'), options_widgetDialog);
	});
	//--><!]]>
</script>