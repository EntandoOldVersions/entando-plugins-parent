<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<h1><a href="<s:url action="viewTree" namespace="/do/Page" />" title="<s:text name="note.goToSomewhere" />: <s:text name="title.pageManagement" />"><s:text name="title.pageManagement" /></a></h1>
<p class="noscreen"><a href="#editFrame"><s:text name="note.goToEditFrame" /></a></p>

<div id="main">

<h2><s:text name="title.configPage" /></h2>
<s:set var="breadcrumbs_pivotPageCode" value="currentPage.code" />
<s:include value="/WEB-INF/apsadmin/jsp/portal/include/pageInfo_breadcrumbs.jsp" />

<div class="subsection-light">
	<h3><s:text name="title.configPage.youAreDoing" /></h3>
	
	<s:action namespace="/do/Page" name="printPageDetails" executeResult="true" ignoreContextParams="true"><s:param name="selectedNode" value="currentPage.code"></s:param></s:action>
	<h2 id="editFrame"><s:text name="title.editFrame" />: <s:property value="frame" /> &ndash; <s:property value="currentPage.getModel().getFrames()[frame]"/></h2>
	
	<h3 class="margin-more-top margin-bit-bottom">Showlet: <s:property value="%{getTitle(showlet.type.code, showlet.type.titles)}" /></h3>
	<s:form action="saveConfigParameters" namespace="/do/jpfastcontentedit/Page/SpecialWidget">
		<p class="noscreen">
			<wpsf:hidden name="pageCode" />
			<wpsf:hidden name="frame" />
			<wpsf:hidden name="showletTypeCode" value="%{showlet.type.code}" />
			<s:if test="showlet.config['typeCode'] != null">
				<wpsf:hidden name="typeCode" value="%{getShowlet().getConfig().get('typeCode')}" />
			</s:if>
		</p>
		
		<s:if test="showlet.config['typeCode'] == null">
			<fieldset>
				<legend><s:text name="label.info" /></legend>
				<p>
					<label for="contentType" class="basic-mint-label"><s:text name="label.type"/>:</label>
					<wpsf:select useTabindexAutoIncrement="true" name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" cssClass="text" />
					<wpsf:submit useTabindexAutoIncrement="true" action="configContentType" value="%{getText('label.continue')}" cssClass="button" />
				</p>
			</fieldset>
		</s:if>
		<s:else>
			<fieldset>
				<legend><s:text name="title.contentInfo" /></legend>
				<p>
					<label for="contentType" class="basic-mint-label"><s:text name="label.type"/>:</label>
					<wpsf:select useTabindexAutoIncrement="true" name="contentType" id="contentType" list="contentTypes" listKey="code" listValue="descr" disabled="true" value="%{getShowlet().getConfig().get('contentType')}" cssClass="text" />
					<wpsf:submit useTabindexAutoIncrement="true" action="changeContentType" value="%{getText('label.change')}" cssClass="button" />	
				</p>
				<p> 
					<label for="contentTypeAuthor" class="basic-mint-label"><s:text name="jpfastcontentedit.label.authorAttribute"/>:</label>
					<wpsf:select useTabindexAutoIncrement="true" id="contentTypeAuthor" list="selectableAttributes" headerKey="" headerValue="%{getText('label.none')}" 
						name="authorAttribute" value="%{getShowlet().getConfig().get('authorAttribute')}" listKey="name" listValue="name" />
				</p>
			</fieldset>
			<p class="centerText"><wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" /></p>
		</s:else>
	</s:form>
</div>
</div>