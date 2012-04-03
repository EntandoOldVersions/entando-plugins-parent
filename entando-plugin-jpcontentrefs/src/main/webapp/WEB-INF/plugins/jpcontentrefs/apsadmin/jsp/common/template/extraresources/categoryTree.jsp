<%@ taglib prefix="s" uri="/struts-tags" %>
<s:include value="/WEB-INF/apsadmin/jsp/common/template/defaultExtraResources.jsp" />

<script type="text/javascript">
<!--//--><![CDATA[//><!--

window.addEvent('domready', function(){
	var catTree  = new Wood({
		menuToggler: "subTreeToggler",
		rootId: "categoryTree",
		openClass: "category",
		closedClass: "category",
		showTools: "true",
		expandAllLabel: "<s:text name="label.expandAll" />",
		collapseAllLabel: "<s:text name="label.collapseAll" />",
		type: "tree",
		startIndex: "<s:property value="selectedNode" />"
	});

});

//--><!]]></script>