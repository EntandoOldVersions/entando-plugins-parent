<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="<wp:info key="currentLang" />">
	<head>
		<%-- JS_JQUERY: (mandatory) it's used to load the necessary coming from the plugin. It also set the flag "outputHeadInfo_JS_JQUERY_isHere"  --%>
			<wp:outputHeadInfo type="JS_JQUERY">
				<c:set var="outputHeadInfo_JS_JQUERY_isHere" value="${true}" />
				<wp:printHeadInfo />
			</wp:outputHeadInfo>
		
		<%-- CSS: (mandatory) it's used to place CSSs coming from other widgets --%>
			<wp:outputHeadInfo type="CSS">
				<link rel="stylesheet" type="text/css" href="<wp:cssURL /><wp:printHeadInfo />" />
			</wp:outputHeadInfo>
		
		<%-- JS: (mandatory) it's used to place js coming from other widgets --%>
			<wp:outputHeadInfo type="JS">
				<script type="text/javascript" src="<wp:resourceURL />static/js/<wp:printHeadInfo />"></script>
			</wp:outputHeadInfo>

		<%-- <title>: it's the of the page, change it as you prefer --%>
			<title><wp:currentPage param="title" /></title>

		<%-- <style>: just an example, you DON'T need this --%>
			<style type="text/css" media="all">
				body {
					font-size: 12px;
				}
				body > .sample {
					float: left;
					width: 100%;
					margin: 2em 0%;
					padding: 1em 0;
				}
				body > .sample > div {
					float: left;
					width: 40%;
					background-color: whitesmoke;
					margin: 0 5%;
				}
			</style>
	</head>
	<body>
		<h1><wp:currentPage param="title" /></h1>
		<div class="sample">
			<wp:show frame="0" />
		</div>
		
		<div class="sample">
			<div>
				<wp:show frame="1" />
				<wp:show frame="2" />
				<wp:show frame="3" />
				<wp:show frame="4" />
			</div>
			<div>
				<wp:show frame="5" />
				<wp:show frame="6" />
				<wp:show frame="7" />
				<wp:show frame="8" />
			</div>
		</div>

		<div class="sample">
			<wp:show frame="9" />
		</div>
		
		<c:if test="${outputHeadInfo_JS_JQUERY_isHere}">
			<jsp:include page="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/models/inc/widget_popup_init.jsp" />
		</c:if>
	</body>
</html>
