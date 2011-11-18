<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<s:set var="facetTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_category" /></s:set>

<fieldset>
	<legend><s:text name="jpfacetnav.title.facets"/></legend>

	<p class="noscreen">
		<wpsf:hidden name="facetRootNodes" />
		<s:if test="#facetTreeStyleVar == 'request'">
		<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar"><wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"></wpsf:hidden></s:iterator>
		</s:if>
	</p>

	<ul id="facetTree">
		<s:set name="inputFieldName" value="'facetCode'" />
		<s:set name="selectedTreeNode" value="facetCode" />
		<s:set name="liClassName" value="'category'" />
		<s:if test="#facetTreeStyleVar == 'classic'">
		<s:set name="currentRoot" value="facetRoot" />
		<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
		</s:if>
		<s:elseif test="#facetTreeStyleVar == 'request'">
		<s:set name="currentRoot" value="showableTree" />
		<s:set name="openTreeActionName" value="'openCloseFacetTreeNode'" />
		<s:set name="closeTreeActionName" value="'openCloseFacetTreeNode'" />
		<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-submits.jsp" />
		</s:elseif>
	</ul>

	<p><wpsf:submit useTabindexAutoIncrement="true" action="joinFacet" value="%{getText('label.join')}" cssClass="button" /></p>

	<s:if test="%{facetRootCodes.size()>0}">
		<table class="generic" summary="<s:text name="jpfacetnav.note.categories.summary"/>">
			<caption><span><s:text name="jpfacetnav.title.categories" /></span></caption>
			<tr>
				<th><s:text name="jpfacetnav.label.facet"/></th>
				<th class="icon"><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th>
			</tr>
			<s:iterator value="facetRootCodes" id="currentFacetCode">
				<wpsa:set name="currentFacet" value="%{getFacet(#currentFacetCode)}" />
				<tr>
					<td><s:property value="#currentFacet.defaultFullTitle"/></td>
					<td class="icon">
						<wpsa:actionParam action="removeFacet" var="actionName" >
							<wpsa:actionSubParam name="facetCode" value="%{#currentFacetCode}" />
						</wpsa:actionParam>
						<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/common/img/icons/delete.png</s:set>
						<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image" src="%{#iconImagePath}" value="%{getText('label.remove')}" title="%{getText('label.remove') + ' ' + #currentFacet.defaultFullTitle}"/>
					</td>
				</tr>
			</s:iterator>
		</table>
	</s:if>

</fieldset>