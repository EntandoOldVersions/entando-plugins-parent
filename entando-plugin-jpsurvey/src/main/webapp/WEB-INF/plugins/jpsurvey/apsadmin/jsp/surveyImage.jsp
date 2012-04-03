<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<s:set name="currentSurveyId" value="%{surveyId}" />
<s:if test="questionnaire">
	<h1><s:text name="title.jpsurvey.survey.main" /></h1>
	<div id="main">
	<h2><s:text name="title.jpsurvey.surveyEditing" /></h2>
</s:if>
<s:else>
	<h1><s:text name="title.jpsurvey.poll.main" /></h1>
	<div id="main">
	<h2><s:text name="title.jpsurvey.pollEditing" /></h2>
</s:else>
	<p>
		<s:text name="note.survey.youwork" />:&#32;<s:property value="currentSurveyId.descr" /> 
		<s:set name="defaultLanguage" value="defaultLangCode"/>
		<s:property value="titles[#defaultLanguage]"/>
	</p>

	<h3 class="margin-bit-top"><s:text name="title.chooseImage" /></h3>

	<s:form action="search" cssClass="margin-bit-top"> 
		<p class="noscreen">
			<input type="hidden" name="surveyId" value="<s:property value="surveyId"/>" />
			<input type="hidden" name="resourceTypeCode" value="Image" />
			<input type="hidden" name="questionnaire" value="<s:property value="questionnaire"/>" />
		</p>
		
		<p>
			<label for="text" class="basic-mint-label label-search"><s:text name="label.search.by" />&#32;<s:text name="label.description"/>:</label>
			<wpsf:textfield useTabindexAutoIncrement="true" name="text" id="text" cssClass="text" />
		</p>
		
		<fieldset>
			<legend><s:text name="title.searchFilters" /></legend>
			<p class="important">
				<s:text name="label.category" />:
			</p>
			<ul id="categoryTree">
				<li class="category"><input type="radio" name="categoryCode" id="<s:property value="categoryRoot.code" />" value="<s:property value="categoryRoot.code" />" class="subTreeToggler tree_root" /><label for="<s:property value="categoryRoot.code" />"><s:if test="categoryRoot.titles[currentLang.code] == null"><s:property value="categoryRoot.code" /></s:if><s:else><s:property value="categoryRoot.titles[currentLang.code]" /></s:else></label>
				<s:if test="categoryRoot.children.size > 0">
					<ul class="treeToggler" id="tree_root">
					<s:set name="currentCategoryRoot" value="categoryRoot" />
					<s:include value="/WEB-INF/apsadmin/jsp/resource/categoryTreeBuilder.jsp" />
					</ul>
				</s:if>
				</li>
			</ul>
		</fieldset>
		<p><wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.search')}" cssClass="button" /></p>
	</s:form>

	<div class="subsection-light">
		<s:form action="search">
			<p class="noscreen">
				<input type="hidden" name="questionnaire" value="<s:property value="questionnaire"/>" />
				<input type="hidden" name="text"/>	
				<input type="hidden" name="surveyId" value="<s:property value="surveyId"/>" />
				<input type="hidden" name="categoryCode" value="<s:property value="categoryCode" /> " />
				<input type="hidden" name="resourceTypeCode" value="Image" />
			</p>
			
			<wpsa:subset source="resources" count="10" objectName="groupResource" advanced="true" offset="5">
			<s:set name="group" value="#groupResource" />
			
			<div class="pager">
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
			</div>
			
			<s:iterator id="resourceid">
			<s:set name="resource" value="%{loadResource(#resourceid)}"></s:set>
			<%-- http://www.maxdesign.com.au/presentation/definition/dl-image-gallery.htm --%>
			<dl class="gallery">
				<dt class="image"><a href="<s:property value="%{#resource.getImagePath(0)}" />" ><img src="<s:property value="%{#resource.getImagePath(1)}"/>" alt=" " /></a></dt>
				<dt class="options">
						<a href="<s:url action="joinImage" namespace="/do/jpsurvey/Survey">
								<s:param name="resourceId" value="%{#resourceid}" />
								<s:param name="surveyId" value="surveyId" />
								<s:param name="questionnaire" value="questionnaire" />
							</s:url>" title="<s:text name="note.joinThisToThat" />: TITOLO_SONDAGGIO_CORRENTE" ><s:text name="label.join" /></a>
				</dt>
				<dd>
					<p><s:property value="#resource.descr" /></p>
				</dd>
			</dl>
			
			</s:iterator>
			
			<div class="pager clear">
				<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
			</div>
			
			</wpsa:subset>
		</s:form>
	</div>
</div>