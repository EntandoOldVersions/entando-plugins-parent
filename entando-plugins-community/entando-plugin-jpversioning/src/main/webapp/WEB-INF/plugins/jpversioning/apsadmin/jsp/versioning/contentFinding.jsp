<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<h1><s:text name="title.jpversioning.contentHistoryManagement" /></h1>
<div id="main">
	
	<h2 class="margin-more-bottom"><s:text name="title.jpversioning.content" /></h2>
	<s:form action="search" >
		<p>
			<label for="descr" class="basic-mint-label label-search"><s:text name="label.search.by" />&#32;<s:text name="label.description"/>:</label>
			<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" name="descr" id="descr" />
		</p>
		<fieldset>
		<legend class="accordion_toggler"><s:text name="title.searchFilters" /></legend>
			<div class="accordion_element">
				<p>
					<label for="contentType" class="basic-mint-label"><s:text name="label.type"/>:</label>
					<wpsf:select useTabindexAutoIncrement="true" name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" headerKey="" headerValue="%{getText('label.all')}" ></wpsf:select>
				</p>
			</div>
		</fieldset>
		<p><wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.search')}" cssClass="button" /></p>
		
		<div class="subsection-light">
		
			<s:if test="%{null == latestVersions || latestVersions.size == 0 }">
				<p><s:text name="message.jpversioning.noModifiedContents"></s:text></p>
			</s:if>
			
			<s:else>
				<wpsa:subset source="latestVersions" count="10" objectName="groupContent" advanced="true" offset="5">
					<s:set var="group" value="#groupContent" />
					
					<div class="pager">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>
					
					<table class="generic" summary="<s:text name="note.jpversioning.contentList.summary" />">
						<caption><span><s:text name="title.jpversioning.contentList" /></span></caption>
						<tr>
							<th><s:text name="jpversioning.label.id" /></th>
							<th><s:text name="jpversioning.label.description" /></th>
							<th><s:text name="jpversioning.label.contentType" /></th>
							<th><s:text name="jpversioning.label.username" /></th>
							<th><s:text name="jpversioning.label.lastVersion" /></th>
							<th class="icon"><abbr title="<s:text name="jpversioning.label.status" />">S</abbr></th>
						</tr>
						<s:iterator var="id">
							<s:set var="contentVersion" value="%{getContentVersion(#id)}" />
							<tr>
								<td class="monospace"><s:property value="#contentVersion.contentId" /></td>
								<td>
									<a href="<s:url action="history" ><s:param name="contentId" value="#contentVersion.contentId" /><s:param name="versionId" value="#contentVersion.id" /></s:url>" title="<s:text name="jpversioning.label.detailOf" />&#32;<s:property value="#contentVersion.descr" />"><s:property value="#contentVersion.descr" /></a>		
								</td>
								<td><s:property value="%{getSmallContentType(#contentVersion.contentType).descr}" /></td>
								<td><s:property value="#contentVersion.username" /></td>
								<td><span class="monospace"><s:property value="#contentVersion.version" />&#32;(<s:date name="#contentVersion.versionDate" format="dd/MM/yyyy" />)</span></td>
								<td class="icon"><s:property value="%{getText(#contentVersion.status)}" /></td>
							</tr>
						</s:iterator>
					</table>
					
					<div class="pager">
						<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
					</div>
					
				</wpsa:subset>
			</s:else>
		</div>
	</s:form>
</div>