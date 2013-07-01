<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<wp:i18n key="jpfastcontentedit_FASTCONTENTEDIT_SHOWLET_TITLE" />
<br />

<s:include value="linkAttributeConfigIntro.jsp"></s:include>
<h3><wp:i18n key="jpfastcontentedit_CONFIGURE_LINK_ATTRIBUTE" />&#32;<wp:i18n key="jpfastcontentedit_STEP_2_OF_2" /></h3>
<s:include value="linkAttributeConfigReminder.jsp" />
<p><wp:i18n key="jpfastcontentedit_CONFIGURE_LINK_TO_CONTENT" /></p>

<form action="<wp:action path="/ExtStr2/do/jpfastcontentedit/Content/Link/search.action" />" method="post" >

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

<s:set name="select_all_label"><wp:i18n key="jpfastcontentedit_SELECT_ALL" /></s:set>

<p><label for="text"><wp:i18n key="jpfastcontentedit_SEARCH_FOR_DESCR" />:</label><br />
<wpsf:textfield useTabindexAutoIncrement="true" name="text" id="text" cssClass="text" /></p>

<fieldset><legend class="accordion_toggler"><wp:i18n key="jpfastcontentedit_SEARCH_FILTERS" /></legend>
<div class="accordion_element">
<p><label for="contentType"><wp:i18n key="jpfastcontentedit_TYPE" />:</label><br />

<wpsf:select useTabindexAutoIncrement="true" name="contentType" id="contentType" 
	list="contentTypes" listKey="code" listValue="descr" 
	headerKey="" headerValue="%{#select_all_label}" cssClass="text"></wpsf:select>
</p>

<p><label for="state"><wp:i18n key="jpfastcontentedit_STATUS" />:</label><br />
<wpsf:select useTabindexAutoIncrement="true" name="state" id="state" list="avalaibleStatus" headerKey="" headerValue="%{#select_all_label}" cssClass="text" listKey="key" listValue="%{getText(value)}" />	
</p>

<s:set name="search_label"><wp:i18n key="jpfastcontentedit_SEARCH" /></s:set>
<p><wpsf:submit useTabindexAutoIncrement="true" value="%{#search_label}" cssClass="button" /></p>
</div>
</fieldset>
</form>

<form action="<wp:action path="/ExtStr2/do/jpfastcontentedit/Content/Link/joinContentLink.action" />" method="post" >

<wpsa:subset source="contents" count="10" objectName="groupContent" advanced="true" offset="5">
<s:set name="group" value="#groupContent" />

<div class="pager">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

<p class="noscreen">
	<wpsf:hidden name="lastGroupBy" />
	<wpsf:hidden name="lastOrder" />
	<wpsf:hidden name="contentOnSessionMarker" />
</p>

<table class="generic" summary="<wp:i18n key="jpfastcontentedit_LINKATTRIBUTE_CONTENT_SUMMARY" />">
<caption><wp:i18n key="jpfastcontentedit_LINKATTRIBUTE_CONTENT_CAPTION" /></caption>
<tr>
	<th>
<c:set var="descriptionLink">/ExtStr2/do/jpfastcontentedit/Content/Link/changeOrder.action?text=<s:property value="#request.text"/>&amp;contentType=<s:property value="#request.contentType"/>&amp;state=<s:property value="#request.state"/>&amp;pagerItem=<s:property value="#groupContent.currItem"/>&amp;lastGroupBy=<s:property value="lastGroupBy"/>&amp;lastOrder=<s:property value="lastOrder"/>&amp;groupBy=descr&amp;contentOnSessionMarker=<s:property value="contentOnSessionMarker" /></c:set>
<a href="<wp:action path="${descriptionLink}" />" tabindex="<wpsa:counter />"><wp:i18n key="jpfastcontentedit_DESCRIPTION" /></a>
</th>
	<th><wp:i18n key="jpfastcontentedit_GROUP" /></th>
	<th>
<c:set var="creationDateLink">/ExtStr2/do/jpfastcontentedit/Content/Link/changeOrder.action?text=<s:property value="#request.text"/>&amp;contentType=<s:property value="#request.contentType"/>&amp;state=<s:property value="#request.state"/>&amp;pagerItem=<s:property value="#groupContent.currItem"/>&amp;lastGroupBy=<s:property value="lastGroupBy"/>&amp;lastOrder=<s:property value="lastOrder"/>&amp;groupBy=created&amp;contentOnSessionMarker=<s:property value="contentOnSessionMarker" /></c:set>
<a href="<wp:action path="${creationDateLink}" />" tabindex="<wpsa:counter />"><wp:i18n key="jpfastcontentedit_CREATION_DATE" /></a>	
</th>
	<th>
<c:set var="lastEditLink">/ExtStr2/do/jpfastcontentedit/Content/Link/changeOrder.action?text=<s:property value="#request.text"/>&amp;contentType=<s:property value="#request.contentType"/>&amp;state=<s:property value="#request.state"/>&amp;pagerItem=<s:property value="#groupContent.currItem"/>&amp;lastGroupBy=<s:property value="lastGroupBy"/>&amp;lastOrder=<s:property value="lastOrder"/>&amp;groupBy=lastModified&amp;contentOnSessionMarker=<s:property value="contentOnSessionMarker" /></c:set>
<a href="<wp:action path="${lastEditLink}" />" tabindex="<wpsa:counter />"><wp:i18n key="jpfastcontentedit_LAST_EDIT" /></a>
</th>

</tr>
<s:iterator id="contentId">
<s:set name="content" value="%{getContentVo(#contentId)}"></s:set>
<tr>
<td><input type="radio" name="contentId" id="contentId_<s:property value="#content.id"/>" value="<s:property value="#content.id"/>" />
<%-- <td><s:property value="#content.id" /></td>  --%>
<label for="contentId_<s:property value="#content.id"/>"><s:property value="#content.descr" /></label></td>
<td><s:property value="groupsMap[#content.mainGroupCode].descr" /></td>
<td><s:date name="#content.create" format="dd/MM/yyyy HH:mm" /></td>
<td><s:date name="#content.modify" format="dd/MM/yyyy HH:mm" /></td>
</tr>
</s:iterator>
</table>

<div class="pager">
	<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
</div>

</wpsa:subset>

<p>
	<wpsf:checkbox useTabindexAutoIncrement="true" name="contentOnPageType" id="contentOnPageType"></wpsf:checkbox><label for="contentOnPageType"><wp:i18n key="jpfastcontentedit_MAKE_CONTENT_ON_PAGE" /></label>
</p>

<s:set name="confirm_label" ><wp:i18n key="jpfastcontentedit_CONFIRM"/></s:set>
<p><wpsf:submit useTabindexAutoIncrement="true" value="%{#confirm_label}" title="%{#confirm_label}" cssClass="button" /></p>

</form>