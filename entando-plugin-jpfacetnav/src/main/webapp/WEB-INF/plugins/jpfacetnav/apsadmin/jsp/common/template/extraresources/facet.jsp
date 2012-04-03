<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<s:include value="/WEB-INF/apsadmin/jsp/common/template/defaultExtraResources.jsp" />

<s:set var="facetTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_category" /></s:set>

<script type="text/javascript">
<!--//--><![CDATA[//><!--

//per faccette
window.addEvent('domready', function(){
<s:if test="#facetTreeStyleVar == 'classic'">
	var catTree  = new Wood({
		menuToggler: "subTreeToggler",
		rootId: "facetTree",
		openClass: "node_open",
		closedClass: "node_closed",
		showTools: "true",
		expandAllLabel: "<s:text name="label.expandAll" />",
		collapseAllLabel: "<s:text name="label.collapseAll" />",
		type: "tree",
		startIndex: "<s:property value="selectedNode" />",
		toolTextIntro: "<s:text name="label.introExpandAll" />",
		toolexpandAllLabelTitle: "<s:text name="label.expandAllTitle" />",
		toolcollapseLabelTitle: "<s:text name="label.collapseAllTitle" />"
	});
</s:if>

<s:if test="#myClient == 'advanced'">
	<s:include value="/WEB-INF/apsadmin/jsp/common/template/extraresources/inc/js_trees_context_menu.jsp" />
</s:if>

});

//--><!]]></script>