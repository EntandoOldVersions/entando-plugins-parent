<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h1><s:text name="title.jpversioning.contentHistoryManagement" /></h1>
<div id="main">
	<h2><s:text name="title.jpversioning.resources.images" /></h2>
	
	<s:form action="search" cssClass="margin-bit-top">
		<p class="noscreen">
			<wpsf:hidden name="resourceTypeCode" />
		</p>
		<p>
			<label for="text" class="basic-mint-label label-search"><s:text name="label.search.for"/>&#32;<s:text name="label.description"/>:</label>
			<wpsf:textfield useTabindexAutoIncrement="true" name="text" id="text" cssClass="text" />
			&#32;
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.search')}" cssClass="button" />
		</p>
	</s:form>
	<div class="subsection-light">
		<s:form action="search">
			<p class="noscreen">
				<wpsf:hidden name="text" />
				<wpsf:hidden name="resourceTypeCode" />
			</p>
		
			<wpsa:subset source="trashedResources" count="10" objectName="groupResource" advanced="true" offset="5">
				<s:set var="group" value="#groupResource" />
				
				<div class="pager">
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pagerInfo.jsp" />
					<s:include value="/WEB-INF/apsadmin/jsp/common/inc/pager_formBlock.jsp" />
				</div>
				
				<s:iterator var="resourceid">
					<s:set var="resource" value="%{getTrashedResource(#resourceid)}" />
					<%-- http://www.maxdesign.com.au/presentation/definition/dl-image-gallery.htm --%>
					<dl class="gallery">
						<dt class="image">
							
						<s:set var="url0" >
						<s:url action="download" namespace="/do/jpversioning/Resource/Trash" >
							<s:param name="resourceId" value="#resourceid"/>
							<s:param name="size" value="0"/>
							<s:param name="langCode" value=""/>
						</s:url>
						</s:set>
						
						<s:set var="url1" >
						<s:url action="download" namespace="/do/jpversioning/Resource/Trash" >
							<s:param name="resourceId" value="#resourceid"/>
							<s:param name="size" value="1"/>
							<s:param name="langCode" value=""/>
						</s:url>
						</s:set>	
						
					<a href="<c:out value="${url0}" escapeXml="false"/>" >
						<img alt="" src="<c:out value="${url1}" escapeXml="false"/>" />
					</a>
						
						<a href="<s:property value="#url" />" ><img src="<s:property value="#url" />" alt=" " /></a></dt>
						<dt class="options">
							<a href="<s:url action="restore" ><s:param name="resourceId" value="%{#resourceid}" /><s:param name="resourceTypeCode" value="%{resourceTypeCode}" /></s:url>" title="<s:text name="jpversioning.label.restore" />" ><img src="<wp:resourceURL />plugins/jpversioning/administration/img/icons/edit-undo.png" alt="<s:text name="jpversioning.label.restore" />" /></a><span class="noscreen">, </span>
							<a href="<s:url action="remove" ><s:param name="resourceId" value="%{#resourceid}" /><s:param name="resourceTypeCode" value="%{resourceTypeCode}" /></s:url>" title="<s:text name="label.remove" />" ><img src="<wp:resourceURL />administration/common/img/icons/delete.png" alt="<s:text name="label.alt.clear" />" /></a>
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