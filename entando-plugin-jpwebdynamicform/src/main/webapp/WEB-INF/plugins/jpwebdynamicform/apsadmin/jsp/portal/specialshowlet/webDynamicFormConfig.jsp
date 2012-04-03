<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<h1><a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />"><s:text name="title.pageManagement" /></a></h1>

<div id="main">
<h2><s:text name="title.configPage" /></h2>

<s:set var="breadcrumbs_pivotPageCode" value="pageCode" />
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

<div class="subsection-light">
<h3><s:text name="title.configPage.youAreDoing" /></h3>

<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="pageCode"></s:param></s:action>
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/frameInfo.jsp" />

<s:set var="showletType" value="%{getShowletType(showletTypeCode)}"></s:set>
<h3 class="margin-more-top margin-more-bottom"><s:text name="name.showlet" />:&#32;<s:property value="%{getTitle(#showletType.code, #showletType.titles)}" /></h3>

<s:form action="saveConfig" namespace="/do/jpwebdynamicform/Page/SpecialShowlet/Webdynamicform">
	<s:if test="hasActionErrors()">
	<div class="message message_error">
	<h4><s:text name="message.title.ActionErrors" /></h4>
		<ul>
		<s:iterator value="actionErrors">
			<li><s:property escape="false" /></li>
		</s:iterator>
		</ul>
	</div>
	</s:if>
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

	<p class="noscreen">
		<wpsf:hidden name="pageCode" />
		<wpsf:hidden name="frame" />
		<wpsf:hidden name="showletTypeCode" value="%{showlet.type.code}" />
	</p>
	<p>
		<label for="jpwebdynamicform_typecode"><s:text name="label.typeCode"/></label>:<br />
		<wpsf:select useTabindexAutoIncrement="true" id="jpwebdynamicform_typecode" name="typeCode" list="messageTypes" listKey="code" listValue="descr" />
	</p>
	<p><wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" /></p>
</s:form>
</div>
</div>