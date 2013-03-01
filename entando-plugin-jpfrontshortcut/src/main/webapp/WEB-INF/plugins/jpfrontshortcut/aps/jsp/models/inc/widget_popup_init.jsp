<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="widgetDialog"><div>
<script type='text/javascript'>
	jQuery(document).ready(function () { 
		var options_myremotelinkdialog = {};
		options_myremotelinkdialog.title = "<wp:i18n key="WIDGET_EDIT_POPUP_TITLE" />";
		options_myremotelinkdialog.autoOpen = false;
		options_myremotelinkdialog.modal = true;
		options_myremotelinkdialog.minWidth = 500;
		options_myremotelinkdialog.opentopics = "openRemoteDialog";
		options_myremotelinkdialog.jqueryaction = "dialog";
		options_myremotelinkdialog.id = "widgetDialog";
		options_myremotelinkdialog.href = "#";
		jQuery.struts2_jquery.bind(jQuery('#widgetDialog'),options_myremotelinkdialog);
	 });  
</script>