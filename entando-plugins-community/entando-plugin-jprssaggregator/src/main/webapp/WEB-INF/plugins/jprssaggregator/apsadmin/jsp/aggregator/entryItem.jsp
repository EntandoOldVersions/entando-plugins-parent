<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/aps-core" prefix="wp" %>

<s:set var="categoryTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_category" /></s:set>

<s:set var="targetNS" value="%{'/do/jprssaggregator/Aggregator'}" />
<h1><s:text name="jprssaggregator.title.rssAggregator" /><s:include value="/WEB-INF/apsadmin/jsp/common/inc/operations-context-general.jsp" /></h1>
<div id="main">
	<h2>
	<s:if test="strutsAction == 1">
		<s:text name="jprssaggregator.title.rssAggregator.add" />
	</s:if><s:elseif test="strutsAction == 2">
		<s:text name="jprssaggregator.title.rssAggregator.edit" />
	</s:elseif>
	</h2>
	
	<s:form action="save">
		<s:if test="hasActionErrors()">
			<div class="message message_error">
				<h3><s:text name="message.title.ActionErrors" /></h3>
				<ul>
					<s:iterator value="actionErrors">
						<li><s:property/></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<s:if test="hasFieldErrors()">
			<div class="message message_error">
			<h3><s:text name="message.title.FieldErrors" /></h3>	
				<ul>
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
						<li><s:property/></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>
				
		<fieldset class="margin-more-top">   
			<legend><s:text name="label.info" /></legend>
			<p class="noscreen">
				<wpsf:hidden name="strutsAction" />
				<wpsf:hidden name="code" />
				<wpsf:hidden name="lastUpdate" />
				<wpsf:hidden name="xmlCategories" />
				<s:if test="strutsAction == 2">
					<wpsf:hidden name="contentType" />
				</s:if>
				<s:if test="strutsAction == 1"><s:set id="delay" name="delay" value="86400" /></s:if>
				<s:else><s:set id="delay" name="delay" value="delay" /></s:else>
				<s:if test="#categoryTreeStyleVar == 'request'">
				<s:iterator value="treeNodesToOpen" var="treeNodeToOpenVar"><wpsf:hidden name="treeNodesToOpen" value="%{#treeNodeToOpenVar}"></wpsf:hidden></s:iterator>
				</s:if>
			</p>
			
			<p>
				<label for="rss_contentType" class="basic-mint-label"><s:text name="jprssaggregator.rssAggregator.contentType" />:</label> 
				<s:if test="strutsAction == 1">
					<wpsf:select useTabindexAutoIncrement="true" name="contentType" id="rss_contentType" list="contentTypes"  listKey="code" listValue="descr" />
				</s:if>
				<s:elseif test="strutsAction == 2">
					<wpsf:textfield useTabindexAutoIncrement="true" name="contentType" id="rss_contentType" value="%{getSmallContentType(contentType).descr}" disabled="true" /> 
				</s:elseif>
			</p>
			
			<p>
				<label for="rss_descr" class="basic-mint-label"><s:text name="jprssaggregator.rssAggregator.description" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" id="rss_descr" name="descr" cssClass="text" />
			</p>
			
			<p>
				<label for="rss_link" class="basic-mint-label"><s:text name="jprssaggregator.rssAggregator.url" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" id="rss_link" name="link" cssClass="text" />
			</p>
			
			<p>
				<label for="rss_delay" class="basic-mint-label"><s:text name="jprssaggregator.rssAggregator.delay" />:</label>
				<wpsf:select useTabindexAutoIncrement="true" cssClass="text" name="delay" id="rss_delay" list="delays" listKey="key" listValue="value" value="#delay" />
			</p>
		</fieldset>
		
		<fieldset>
			<legend><s:text name="title.categoriesManagement"/></legend>
			<ul id="categoryTree">
				<s:set name="inputFieldName" value="'categoryCode'" />
				<s:set name="selectedTreeNode" value="categoryCode" />
				<s:set name="liClassName" value="'category'" />
				<s:if test="#categoryTreeStyleVar == 'classic'">
				<s:set name="currentRoot" value="categoryRoot" />
				<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder.jsp" />
				</s:if>
				<s:elseif test="#categoryTreeStyleVar == 'request'">
				<s:set name="currentRoot" value="showableTree" />
				<s:set name="openTreeActionName" value="'openCloseCategoryTreeNode'" />
				<s:set name="closeTreeActionName" value="'openCloseCategoryTreeNode'" />
				<s:include value="/WEB-INF/apsadmin/jsp/common/treeBuilder-request-submits.jsp" />
				</s:elseif>
			</ul>
			<p><wpsf:submit useTabindexAutoIncrement="true" action="joinCategory" value="%{getText('label.join')}" cssClass="button" /></p>
			
			<s:set var="categoriesVar" value="categories" />
			<s:if test="#categoriesVar != null && #categoriesVar.size() > 0">
			<table class="generic" summary="<s:text name="note.contentCategories.summary"/>: <s:property value="content.descr" />">
				<caption><span><s:text name="title.contentCategories.list"/></span></caption>
				<tr>
					<th><s:text name="label.category"/></th>
					<th class="icon"><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th>
				</tr>
				<s:iterator value="categoriesVar" var="categoryEntry">
					<tr>
						<td>
						<s:set var="category" value="%{getCategory(#categoryEntry.key)}"></s:set>
						<s:property value="%{#category.getFullTitle(currentLang.code)}" /></td>
						<td class="icon">
							<wpsa:actionParam action="removeCategory" var="actionName" >
								<wpsa:actionSubParam name="categoryCode" value="%{#categoryEntry.key}" />
							</wpsa:actionParam>
							<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL />administration/common/img/icons/delete.png</s:set>
							<wpsf:submit useTabindexAutoIncrement="true" type="image" src="%{#iconImagePath}" action="%{#actionName}" value="%{getText('label.remove')}" title="%{getText('label.remove') + ' ' + #category.getTitle(currentLang.code)}" />
						</td>
					</tr>
				</s:iterator>
			</table>
			</s:if>
			
		</fieldset>
		
		<p class="centerText">
			<wpsf:submit useTabindexAutoIncrement="true" cssClass="button" value="%{getText('label.save')}"/>
		</p>
		
	</s:form>
</div>