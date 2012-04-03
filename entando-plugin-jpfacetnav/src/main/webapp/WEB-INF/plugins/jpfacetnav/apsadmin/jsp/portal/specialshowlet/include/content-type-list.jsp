<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<fieldset>
	<legend><s:text name="jpfacetnav.title.contentTypes" /></legend>
	<p class="noscreen">
		<wpsf:hidden name="contentTypesFilter" />
	</p>
	<p>
		<label for="contentTypeCode" class="basic-mint-label"><s:text name="label.type"/>:</label>
		<wpsf:select useTabindexAutoIncrement="true" name="contentTypeCode" id="contentTypeCode" list="contentTypes" listKey="code" listValue="descr" cssClass="text" />
		<wpsf:submit useTabindexAutoIncrement="true" action="joinContentType" value="%{getText('label.add')}" cssClass="button" />
	</p>
	<s:if test="%{contentTypeCodes.size()>0}">
		<table class="generic" summary="<s:text name="jpfacetnav.note.contentTypes.summary"/>">
			<caption><span><s:text name="jpfacetnav.title.contentTypes.associated"/></span></caption>
			<tr>
				<th><s:text name="jpfacetnav.label.contentType"/></th>
				<th class="icon"><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th>
			</tr>
			<s:iterator value="contentTypeCodes" id="currentContentTypeCode" status="rowstatus">
			<tr>
				<td>
					<wpsa:set name="currentContentType" value="%{getContentType(#currentContentTypeCode)}" />
					<input type="hidden" name="contentTypeCodes" value="<s:property value="#currentContentTypeCode" />" id="contentTypeCodes-<s:property value="#rowstatus.index" />"/>
					<s:property value="#currentContentType.descr"/>
				</td>
				<td class="icon">
					<wpsa:actionParam action="removeContentType" var="actionName" >
						<wpsa:actionSubParam name="contentTypeCode" value="%{#currentContentTypeCode}" />
					</wpsa:actionParam>
					<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/delete.png</s:set>
					<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image" src="%{#iconImagePath}" value="%{getText('label.remove')}" title="%{getText('label.remove')}: %{#currentContentType.descr}" />
				</td>
			</tr>
			</s:iterator>
		</table>
	</s:if>
</fieldset>