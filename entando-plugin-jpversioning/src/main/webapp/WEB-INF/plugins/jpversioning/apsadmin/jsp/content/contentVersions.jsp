<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<fieldset>
	<legend><s:text name="jpversioning.label.history" /></legend>
	<s:set name="contentVersionsList" value="%{contentVersions}"/>
	
	<s:if test="%{#contentVersionsList == null || !#contentVersionsList.size() > 0}">
		<p><s:text name="jpversioning.message.no.previous.revisions" /></p>
	</s:if>
	<s:else>
		<wpsa:subset source="#contentVersionsList" count="5" objectName="groupContent" advanced="true" offset="5">
			<p>
				<wpsf:hidden name="contentId" value="%{content.id}" />
			</p>
				
			<s:set name="group" value="#groupContent" />
			<div class="pager">
				<p><s:text name="list.pagerIntro" />&#32;<s:property value="#group.size" />&#32;<s:text name="list.pagerOutro" /><br />
				<s:text name="label.page" />: [<s:property value="#group.currItem" />/<s:property value="#group.maxItem" />].</p>				
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
			</div>
			
			<table class="generic" summary="<s:text name="note.jpversioning.contentList.summary" />">
				<caption><span><s:text name="title.jpversioning.versionList" /></span></caption> 
				<tr>
					<th><abbr title="<s:text name="jpversioning.version.full" />"><s:text name="jpversioning.version.short" /></abbr></th>
					<th><s:text name="jpversioning.label.description" /></th>
					<th><s:text name="jpversioning.label.lastVersion" /></th>
					<th><s:text name="jpversioning.label.username" /></th>
					<th class="icon"><abbr title="<s:text name="jpversioning.recover.full" />"><s:text name="jpversioning.recover.short" /></abbr></th>
				</tr>
			
				<s:iterator id="versionId">
				<s:set name="contentVersion" value="%{getContentVersion(#versionId)}" />
				<tr>
					<td><span class="monospace"><s:property value="#contentVersion.version" /></span></td>
					<td>
						<a href="<s:url action="preview" namespace="/do/jpversioning/Content/Versioning">
							   <s:param name="versionId" value="#contentVersion.id" />
							   <s:param name="contentId" value="%{content.id}" />
							   <s:param name="fromEdit" value="true" />
							   <s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
						   </s:url>" ><s:property value="#contentVersion.descr" /></a>
				    </td>
					<td><span class="monospace">
						<s:date name="#contentVersion.versionDate" format="dd/MM/yyyy HH:mm" /></span> 
					</td>
					<td><s:property value="#contentVersion.username" /></td>
					<td class="icon">
						<a href="<s:url action="entryRecover" namespace="/do/jpversioning/Content/Versioning" >
							   <s:param name="versionId" value="#contentVersion.id" />
							   <s:param name="contentId" value="%{content.id}" />
							   <s:param name="fromEdit" value="true" />
							   <s:param name="contentOnSessionMarker" value="contentOnSessionMarker" />
						   </s:url>" title="<s:text name="jpversioning.label.restore" />:&#32;<s:property value="#contentVersion.version" />" >
						<img src="<wp:resourceURL/>plugins/jpversioning/administration/img/icons/edit-undo.png" alt="<s:text name="jpversioning.label.restore" />" />
						</a>
					</td>
				</tr>
				</s:iterator>			
			</table>
			<div class="pager">
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
			</div>
		</wpsa:subset>
	</s:else>
</fieldset>

