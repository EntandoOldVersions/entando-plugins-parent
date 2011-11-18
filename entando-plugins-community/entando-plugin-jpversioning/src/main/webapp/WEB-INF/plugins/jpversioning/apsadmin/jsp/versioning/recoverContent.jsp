<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1><a href="<s:url action="list" ></s:url>" ><s:text name="title.jpversioning.contentHistoryManagement" /></a></h1>
<div id="main">
	<h2><s:text name="title.jpversioning.content" /></h2>

	<h3><s:text name="title.jpversioning.recover" /></h3> 
		<dl class="table-display">
			<dt><s:text name="jpversioning.label.id" /></dt><dd><s:property value="contentVersion.contentId" /></dd>
			<dt><s:text name="jpversioning.label.description" /></dt><dd><s:property value="contentVersion.descr" /></dd>
			<dt><s:text name="jpversioning.label.lastVersion" /></dt><dd><s:date name="contentVersion.versionDate" format="dd/MM/yyyy HH:mm" /></dd>
			<dt><s:text name="jpversioning.label.version" /></dt><dd><s:property value="contentVersion.version" /></dd>
		</dl>
		
		<h3><s:text name="title.jpversioning.referenced.resources" /></h3>
		<h4><s:text name="title.jpversioning.resources.images" /></h4>
		<s:if test="%{trashedResources == null || trashedResources.size() == 0 }">
			<p><s:text name="message.versioning.noTrashedResources" /></p>
		</s:if>
		<s:else>
			<table class="generic">
				<caption><span><s:text name="title.versioning.resource.image.trashed" /></span></caption>
				<tr>
					<th><s:text name="versioning.resource.id"/></th>
					<th><s:text name="label.type"/></th>
					<th><s:text name="label.description"/></th>
					<th><s:text name="label.ownerGroup"/></th>
				</tr>
				<s:iterator id="id" value="trashedResources">
					<s:set name="resourceItem" value="getTrashedResource(#id)" />
					<tr>
						<td><s:property value="#resourceItem.id" /></td>
						<td><s:property value="#resourceItem.type" /></td>
						<td><s:property value="#resourceItem.descr" /></td>
						<td><s:property value="#resourceItem.mainGroup" /></td>
					</tr>
				</s:iterator>
			</table>
		</s:else>
		
		<h4><s:text name="title.jpversioning.resources.attaches" /></h4>
		<s:if test="%{trashRemovedResources == null || trashRemovedResources.size() == 0 }">
			<p><s:text name="message.versioning.noTrashRemovedResources" /></p>
		</s:if>
		<s:else>
			<table class="generic">
				<caption><span><s:text name="title.versioning.resource.attach.trashed" /></span></caption>
				<tr>
					<th><s:text name="versioning.resource.id"/></th>
					<th><s:text name="label.messages"/></th>
				</tr>
				<s:iterator id="id" value="trashRemovedResources">
					<tr>
						<td><s:property value="#id" /></td>
						<td><s:text name="message.versioning.trashRemovedResource" /></td>
					</tr>
				</s:iterator>
			</table>
		</s:else>
	<div class="subsection-light">
		<s:form action="recover" >
			<p>
				<s:hidden name="versionId" />
				<s:hidden name="contentId" />
				<s:text name="jpversioning.confirmRestore.info" />
			</p>
			<p>
				<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.confirm')}" cssClass="button" />
			</p>
		</s:form>
	</div>
</div>