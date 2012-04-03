<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<wp:headInfo type="CSS" info="../../plugins/jpfastcontentedit/static/css/editor.css" />

<wp:headInfo type="JS" info="../../plugins/jpfastcontentedit/static/js/moo-japs/moo-jAPS-0.2.js" />

<s:set name="js_for_tab">
//per pageTree
window.addEvent('domready', function(){

	var pageTree = new Wood({
		rootId: "pageTree",
		menuToggler: "subTreeToggler",
		openClass: "node_open",
		closedClass: "node_closed",
		showTools: "true",
		expandAllLabel: "<s:text name="label.expandAll"/>",
		collapseAllLabel: "<s:text name="label.collapseAll"/>",
		type: "tree",
		<s:if test="%{selectedNode != null && !(selectedNode.equalsIgnoreCase(''))}">
		startIndex: "fagianonode_<s:property value="selectedNode" />",
		</s:if>
		toolTextIntro: "<s:text name="label.introExpandAll" />",
		toolexpandAllLabelTitle: "<s:text name="label.expandAllTitle" />",
		toolcollapseLabelTitle: "<s:text name="label.collapseAllTitle" />"
	});

});

</s:set>
<wp:headInfo type="JS_RAW" info="${js_for_tab}" />

<wp:i18n key="jpfastcontentedit_FASTCONTENTEDIT_SHOWLET_TITLE" />
<br />

<s:include value="linkAttributeConfigIntro.jsp"></s:include>
<h3><wp:i18n key="jpfastcontentedit_CONFIGURE_LINK_ATTRIBUTE" />&#32;<wp:i18n key="jpfastcontentedit_STEP_2_OF_2" /></h3>
<s:include value="linkAttributeConfigReminder.jsp" />
<p>
	<wp:i18n key="jpfastcontentedit_CONFIGURE_LINK_TO_PAGE" />
	<s:if test="contentId != null">&#32;<wp:i18n key="jpfastcontentedit_FOR_THE_CONTENT" />: <s:property value="contentId"/> &ndash; <s:property value="%{getContentVo(contentId).descr}"/></s:if>.
</p>
<form action="<wp:action path="/ExtStr2/do/jpfastcontentedit/Content/Link/joinPageLink.action" />" method="post" >

<s:if test="hasFieldErrors()">
<h3><wp:i18n key="jpfastcontentedit_ERRORS" /></h3>
<ul>
	<s:iterator value="fieldErrors">
		<s:iterator value="value">
            <li><s:property escape="false" /></li>
		</s:iterator>
	</s:iterator>
</ul>
</s:if>

<p><s:if test="contentId == null">
<wpsf:hidden name="linkType" value="2" />
</s:if>
<s:else>
<wpsf:hidden name="contentId" />
<wpsf:hidden name="linkType" value="4" />
</s:else></p>

<fieldset><legend><wp:i18n key="jpfastcontentedit_PAGE_TREE" /></legend>
<ul id="pageTree">
	<s:set name="inputFieldName" value="'selectedNode'" />
	<s:set name="selectedTreeNode" value="selectedNode" />
	<s:set name="liClassName" value="'page'" />
	<s:set name="currentRoot" value="treeRootNode" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
</ul>
</fieldset>

<s:set name="confirm_label" ><wp:i18n key="jpfastcontentedit_CONFIRM"/></s:set>
<p><wpsf:submit useTabindexAutoIncrement="true" value="%{#confirm_label}" title="%{#confirm_label}" cssClass="button" /></p>

</form>