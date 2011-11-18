<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1><a href="<s:url action="list" ></s:url>" ><s:text name="title.jpversioning.contentHistoryManagement" /></a></h1>
<div id="main">
	<h2><s:text name="title.jpversioning.content" /></h2>
	<h3 class="margin-bit-bottom"><s:text name="jpversioning.label.detail" /></h3>
	<ul class="menu horizontal tab-toggle-bar"><li><a href="#info" id="info_tab_quickmenu" class="tab-toggle"><abbr title="<s:text name="title.contentInfo" />">Info</abbr></a></li><s:iterator value="langs" id="lang"><li><a href="#<s:property value="#lang.code" />_tab" class="tab-toggle"><s:property value="#lang.descr" /></a></li></s:iterator></ul>
	<div class="tab-container jpversioning">
		<div id="info" class="tab">
			<dl class="table-display">
				<dt><s:text name="jpversioning.label.id" /></dt><dd><s:property value="contentVersion.contentId" /></dd>
				<dt><s:text name="jpversioning.label.description" /></dt><dd><s:property value="contentVersion.descr" /></dd>
				<dt><s:text name="jpversioning.label.version.date" /></dt><dd><s:date name="contentVersion.versionDate" format="dd/MM/yyyy HH:mm" /></dd>
				<dt><s:text name="jpversioning.label.author" /></dt><dd><s:property value="%{contentVersion.username}" /></dd> 
				<dt><s:text name="jpversioning.label.version" /></dt><dd><s:property value="contentVersion.version" /></dd>
				<dt><s:text name="jpversioning.label.status" /></dt><dd><s:property value="%{getText(contentVersion.status)}" /></dd>
				<s:set name="contentGroup" value="%{getGroup(content.getMainGroup())}" />
				<dt><s:text name="label.ownerGroup" /></dt><dd><s:property value="%{#contentGroup.descr}" /></dd>
			</dl>
			
			<%-- GROUPS --%>
			<h3><s:text name="label.extraGroups" /></h3>
			<s:if test="%{content.groups.size() <= 0}"><p><s:text name="noExtraGroup" /></p></s:if>
			<s:if test="%{content.groups.size() > 0}">
				<ul>
					<s:iterator value="content.groups" id="groupName">
						<li><s:property value="%{getGroupsMap()[#groupName].getDescr()}"/></li>
					</s:iterator>
				</ul>
			</s:if>
			
			<%-- CATEGORIES --%>
			<h3><s:text name="title.contentCategories.list"/></h3>
			<s:if test="%{content.categories.size() <=  0}"><p><s:text name="category.no.category" /></p></s:if>
			
			<s:if test="%{content.categories.size() > 0}">
				<ul>
					<s:iterator value="content.categories" id="contentCategory">
						<li><s:property value="#contentCategory.defaultFullTitle"/></li>
					</s:iterator>
				</ul>
			</s:if>
		</div>
		
		<s:include value="/WEB-INF/plugins/jpversioning/apsadmin/jsp/versioning/inc/contentView.jsp" />
		
	</div>
</div>