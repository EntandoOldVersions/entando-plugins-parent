<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<h1><a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />"><s:text name="title.pageManagement" /></a></h1>
<p class="noscreen"><a href="#editFrame"><s:text name="note.goToEditFrame" /></a></p>

<div id="main">
<h2><s:text name="title.configPage" /></h2>
<s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />
	<div class="subsection-light">
		<h3><s:text name="title.configPage.youAreDoing" /></h3>
		<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>
		<s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />
		<h2 id="editFrame"><s:text name="title.editFrame" />: <s:property value="frame" /> &ndash; <s:property value="currentPage.getModel().getFrames()[frame]"/></h2>
		<h3 class="margin-more-top">Showlet: <s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" /></h3>

		<s:form action="saveConfig" namespace="/do/jpfacetnav/Page/SpecialWidget/FacetNavResult">
			<p class="noscreen">
				<wpsf:hidden name="pageCode" />
				<wpsf:hidden name="frame" />
				<wpsf:hidden name="widgetTypeCode" value="%{showlet.type.code}" />
			</p>

			<s:if test="hasFieldErrors()">
				<div class="message message_error">
				<h4><s:text name="message.title.FieldErrors" /></h4>
					<ul>
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
						<li><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
					</ul>
				</div>
			</s:if>

			<p class="margin-more-bottom">
				<s:text name="jpfacetnav.note.facetNavResult.intro" />
			</p>

			<wpsa:include value="/WEB-INF/plugins/jpfacetnav/apsadmin/jsp/portal/SpecialShowlet/include/content-type-list.jsp" />

			<p class="centerText">
				<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" />
			</p>
		</s:form>
	</div>
</div>