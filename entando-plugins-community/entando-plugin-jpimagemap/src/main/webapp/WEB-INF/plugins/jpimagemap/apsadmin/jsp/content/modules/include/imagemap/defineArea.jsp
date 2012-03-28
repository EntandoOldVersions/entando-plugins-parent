<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<html>
	<head>
		<title><s:text name="jpimagemap.defineArea.Title" /></title>
		<jsp:include page="/WEB-INF/apsadmin/jsp/common/template/defaultExtraResources.jsp" />
		<%--
		<script type="text/javascript" src="<wp:resourceURL />administration/common/js/mootools-core-1.3-full-compat-yc.js"></script>
		<script type="text/javascript" src="<wp:resourceURL />administration/common/js/mootools-more-1.3-full-compat-yc.js"></script>
		--%>
		<script type="text/javascript">
			var ImageMapAttribute_fieldError_linkedAreaElement_intersectedArea = "<s:property value="%{getText('ImageMapAttribute.fieldError.linkedAreaElement.intersectedArea')}" escapeJavaScript="true" />";
		</script>

		<script type="text/javascript" src="<wp:resourceURL />plugins/jpimagemap/administration/common/js/jpimagemap-areas.js"></script>
		<link rel="stylesheet" type="text/css" href="<wp:resourceURL />plugins/jpimagemap/administration/common/css/administration-jpimagemap.css" />
	</head>

	<body>

		<div id="jpimagemap_toolbarinfo">
			<div id="main">
				<h1><s:text name="jacms.menu.contentAdmin" /></h1>
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

	</body>
</html>