<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<s:set var="myClient"><wpsa:backendGuiClient /></s:set>
<html>
	<head>
		<title>Entando - <s:text name="jpimagemap.defineArea.Title" /></title>
		<!-- entando default -->
			<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/common/css/administration.css" media="screen" />
			<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/common/css/menu.css" media="screen" />
			<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/common/css/layout-general.css" media="screen" />

			<!--[if IE 7]>
				<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/common/css/menu-ie7.css" media="screen" />
				<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/common/css/layout-general-ie7.css" media="screen" />
			<![endif]-->

			<!--[if IE 8]>
					<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/common/css/administration-ie8.css" media="screen" />
			<![endif]-->

		<s:if test="#myClient == 'normal'">
			<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/basic/css/administration.css" media="screen" />
			<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/basic/css/menu.css" media="screen" />
			<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/basic/css/layout-general.css" media="screen" />
		</s:if>

		<s:if test="#myClient == 'advanced'">
			<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/mint/css/administration.css" media="screen" />
			<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/mint/css/menu.css" media="screen" />
			<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/mint/css/layout-general.css" media="screen" />

			<!--[if IE 7]>
				<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/mint/css/menu-ie7.css" media="screen" />
				<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/mint/css/layout-general-ie7.css" media="screen" />
			<![endif]-->

			<!--[if IE 8]>
				<link rel="stylesheet" type="text/css" href="<wp:resourceURL />administration/mint/css/administration-ie8.css" media="screen" />
			<![endif]-->
		</s:if>
		<jsp:include page="/WEB-INF/apsadmin/jsp/common/template/defaultExtraResources.jsp" />
<!-- entando default -->

<!-- jpimagemap -->
<%--
		<script type="text/javascript" src="<wp:resourceURL />administration/common/js/mootools-core-1.3-full-compat-yc.js"></script>
		<script type="text/javascript" src="<wp:resourceURL />administration/common/js/mootools-more-1.3-full-compat-yc.js"></script>
		<script type="text/javascript" src="<wp:resourceURL />administration/common/js/moo-japs/moo-jAPS-0.2.js"></script>
--%>
		<script type="text/javascript">
			var ImageMapAttribute_fieldError_linkedAreaElement_intersectedArea = "<s:property value="%{getText('ImageMapAttribute.fieldError.linkedAreaElement.intersectedArea')}" escapeJavaScript="true" />";
		</script>
		<script type="text/javascript" src="<wp:resourceURL />plugins/jpimagemap/administration/js/jpimagemap-areas.js"></script>
		<link rel="stylesheet" type="text/css" href="<wp:resourceURL />plugins/jpimagemap/administration/css/administration-jpimagemap.css" />
<!-- jpimagemap -->
	</head>

<body class="client-<s:property value="#myClient" />">
<div id="topbar">
<jsp:include page="/WEB-INF/apsadmin/jsp/common/template/topbar.jsp" />
</div>
<div id="header" class="clear">
<h1 class="noscreen" id="fagiano_start"><s:text name="title.mainFunctions" /></h1>
<jsp:include page="/WEB-INF/apsadmin/jsp/common/template/header.jsp" />
</div>
<div id="corpo">
	<div id="colonna1">
	<jsp:include page="/WEB-INF/apsadmin/jsp/common/template/menu.jsp" />
	</div>
	<div id="colonna2">
	<p class="noscreen"><a href="#fagiano_start" id="fagiano_mainContent"><s:text name="note.backToStart" /></a></p>
	<!-- jpimagemap -->
		<h1><s:text name="jacms.menu.contentAdmin" /></h1>
		<div id="main">
<div id="jpimagemap_toolbarinfo">
			<s:form action="saveArea" id="mainform">
				<p>
					<input value="" size="4" name="top" type="hidden" />
					<input value="" size="4" name="left" type="hidden" />
					<input value="" size="4" name="width" type="hidden" />
					<input value="" size="4" name="height" type="hidden" />
					<input value="" size="4" name="bottom" type="hidden"/>
					<input value="" size="4" name="right" type="hidden" />
					
					<wpsf:hidden name="attributeName"/>
					<wpsf:hidden name="elementIndex" />
					<wpsf:hidden name="langCode"/>
					<wpsf:hidden name="contentOnSessionMarker" />
					
					<s:text name="jpimagemap.defineArea.note.positioning" />
				</p>
				<p>
					<input class="button" id="pulsante" value="<s:text name="jpimagemap.label.save" />" type="submit" />
					<span id="messages"></span>
				</p>
			</s:form>
		</div>

		<div id="areas">
			<img src="<s:property value="%{attribute.getResource().getImagePath('0')}"/>" />

			<s:if test="%{(attribute.resource != null && attribute.resource.id != 0 ) || attribute.text != ''}">
				<s:if test="%{attribute.areas != null}">
					<s:iterator value="%{attribute.areas}" id="area" status="statusElement">
						<s:if test="%{#area!=null}">
							<!-- cords: <s:property value="#area.coords=='0,0,0,0'" /> -->
							<s:if test="%{#statusElement.index == elementIndex}" >
								<s:if  test="%{#area.coords == '0,0,0,0'}" >
									<s:set var="top">0</s:set>
									<s:set var="left">0</s:set>
									<s:set var="width">120</s:set>
									<s:set var="height">120</s:set>
								</s:if>
								<s:else>
									<s:set var="left" value="%{#area.arrayCoords[0]}" />
									<s:set var="top" value="%{#area.arrayCoords[1]}" />
									<s:set var="width" value="%{#area.arrayCoords[2]-#left}" />
									<s:set var="height" value="%{#area.arrayCoords[3]-#top}" />								</s:else>
								<div class="area area<s:property value="#statusElement.index" /> area-current"
									style=" top: <s:property value="#top" />px; left: <s:property value="#left" />px; width: <s:property value="#width" />px; height: <s:property value="#height" />px; ">
										Area <s:property value="#statusElement.count" />
								</div>
							</s:if>
							<s:else>
								<s:if  test="%{#area.coords == '0,0,0,0'}" >
									<s:set var="top">0</s:set>
									<s:set var="left">0</s:set>
									<s:set var="width">120</s:set>
									<s:set var="height">120</s:set>
								</s:if>
								<s:else>
									<s:set var="left" value="%{#area.arrayCoords[0]}" />
									<s:set var="top" value="%{#area.arrayCoords[1]}" />
									<s:set var="width" value="%{#area.arrayCoords[2]-#left}" />
									<s:set var="height" value="%{#area.arrayCoords[3]-#top}" />
								</s:else>
								<div class="area area<s:property value="#statusElement.index" /> area-blocked"
									style=" top: <s:property value="#top" />px; left: <s:property value="#left" />px; width: <s:property value="#width" />px; height: <s:property value="#height" />px; ">
										Area&#32;<s:property value="#statusElement.count" />
								</div>
							</s:else>
						</s:if>
					</s:iterator>
				</s:if>

			</s:if>

		</div>
	</div>
	<!-- jpimagemap -->
	<p class="noscreen"><a href="#fagiano_mainContent"><s:text name="note.backToMainContent" /></a></p>
	<p class="noscreen"><a href="#fagiano_start"><s:text name="note.backToStart" /></a></p>
	</div>
</div>
<div id="footer">
<jsp:include page="/WEB-INF/apsadmin/jsp/common/template/footer.jsp" />
</div>
</body>
</html>
