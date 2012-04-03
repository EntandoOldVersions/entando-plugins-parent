<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ page contentType="charset=UTF-8" %>
<s:set var="contentVersionsInfo" value="%{getContentVersion(versionId)}" />
<h1><a href="<s:url action="list" ></s:url>" ><s:text name="title.jpversioning.contentHistoryManagement" /></a></h1>
<div id="main">

	<h2><s:text name="title.jpversioning.content" /></h2>
	<h3><s:text name="title.jpversioning.versionsManagement" /></h3>
	
	<s:set var="id" value="contentId" />
	
	<dl class="table-display">
		<dt><s:text name="jpversioning.label.id" /></dt>
		<dd><s:property value="#id" /></dd>
		<dt><s:text name="jpversioning.label.contentType" /></dt>
		<s:set var="typeDescr" value="%{getSmallContentType(contentVersion.contentType).descr}" />
		<dd><s:if test="%{#typeDescr!=null}"><s:property value="%{#typeDescr}"/></s:if><s:else><s:property value="%{contentVersion.contentType}"/></s:else></dd>
	</dl>
	
	<s:form action="history" >
		<wpsa:subset source="contentVersions" count="10" objectName="groupContent" advanced="true" offset="5">
			<s:set var="group" value="#groupContent" />
			
			<div class="pager">
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
			</div>
			
			<p class="noscreen">
				<s:hidden name="versionId" />
				<s:hidden name="backId" />
				<s:hidden name="fromEdit" />
			</p>
			 
			<table class="generic" summary="<s:text name="note.jpversioning.versionList.summary"/>">
				<caption><span><s:text name="title.jpversioning.versionList" /></span></caption>
				<tr>
					<th><abbr title="<s:text name="jpversioning.version.full" />"><s:text name="jpversioning.version.short" /></abbr></th>
					<th><s:text name="jpversioning.label.description" /></th>
					<th><s:text name="jpversioning.label.lastVersion" /></th>
					<th><s:text name="jpversioning.label.username" /></th>
					<th class="icon"><abbr title="<s:text name="jpversioning.label.status" />">S</abbr></th>
					<th class="icon_double"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
				</tr>
				
				<s:iterator var="id" status="statusIndex">
					<s:set var="contentVersion" value="%{getContentVersion(#id)}" />
					<tr>
						<td class="monospace rightText">
							<s:property value="#contentVersion.version" />
						</td>
						<td>
							<a href="<s:url action="preview" ><s:param name="versionId" value="#contentVersion.id" /><s:param name="contentId" value="#contentVersion.contentId" /><s:param name="fromEdit" value="fromEdit" /><s:param name="backId" value="versionId" /></s:url>" title="<s:text name="jpversioning.label.detailOf" />:&#32;<s:property value="#contentVersion.version" />">
								<s:property value="#contentVersion.descr" />
							</a>
						</td>
						<td>
							<span class="monospace"><s:date name="#contentVersion.versionDate" format="dd/MM/yyyy HH:mm" /></span> 
						</td>
						<td>
							<s:property value="#contentVersion.username" />
						</td>
						<td>
							<s:property value="%{getText(#contentVersion.status)}" />
						</td>
						<td class="icon_double">
							<a href="<s:url action="entryRecover" ><s:param name="versionId" value="#contentVersion.id" /><s:param name="contentId" value="#contentVersion.contentId" /><s:param name="fromEdit" value="fromEdit" /><s:param name="backId" value="versionId" /></s:url>" title="<s:text name="jpversioning.label.restore" />:&#32;<s:property value="#contentVersion.version" />" ><img src="<wp:resourceURL/>plugins/jpversioning/administration/img/icons/edit-undo.png" alt="<s:text name="jpversioning.label.restore" />" /></a>
					<%--	<s:if test="versionId != #contentVersion.id"> --%>
							<a href="<s:url action="trash" ><s:param name="versionId" value="#contentVersion.id" /><s:param name="contentId" value="#contentVersion.contentId" /><s:param name="fromEdit" value="fromEdit" /><s:param name="backId" value="versionId" /></s:url>" title="<s:text name="label.remove" />:&#32;<s:property value="#contentVersion.version" />"><img src="<wp:resourceURL />administration/common/img/icons/delete.png" alt="<s:text name="label.alt.clear" />" /></a>
					<%--	</s:if> --%>
						</td>
					</tr>
				</s:iterator>
			</table>
			
			<div class="pager">
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
			</div>
		</wpsa:subset>
	</s:form>
</div>