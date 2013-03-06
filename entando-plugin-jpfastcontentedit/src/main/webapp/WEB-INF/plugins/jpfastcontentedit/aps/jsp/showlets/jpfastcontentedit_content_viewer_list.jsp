<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<%@ taglib prefix="fce" uri="/jpfastcontentedit-aps-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<wp:headInfo type="CSS" info="../../plugins/jpfastcontentedit/static/css/jpfastcontentedit.css" />

<jacms:contentList listName="contentIds" titleVar="titleVar" pageLinkVar="pageLinkVar" pageLinkDescriptionVar="pageLinkDescriptionVar" userFilterOptionsVar="userFilterOptionsVar"/>

<c:if test="${null != titleVar}">
	<h2><span><c:out value="${titleVar}" /></span></h2>
</c:if>

<c:set var="userFilterOptionsVar" value="${userFilterOptionsVar}" scope="request" />
<c:import url="/WEB-INF/plugins/jacms/aps/jsp/showlets/inc/userFilter-module.jsp" />

<c:choose>
	<c:when test="${contentIds != null && !empty contentIds}">

	<fce:allowedContents var="allowedContents" />

	<wp:pager listName="contentIds" objectName="groupContent" pagerIdFromFrame="true" max="10" >
		<ul>
		<c:forEach var="contentId" items="${contentIds}" begin="${groupContent.begin}" end="${groupContent.end}">
			<li><jacms:content contentId="${contentId}" />
<wp:ifauthorized permission="jpfastcontentedit_editContents" >
<fce:isContentAllowed listName="allowedContents" contentId="${contentId}" >
	&#32;|&#32;
	<a href="<wp:url page="fastcontentedit" >
			<wp:parameter name="contentId" ><c:out value="${contentId}" /></wp:parameter>
			<wp:parameter name="contentTypeCode" ><wp:currentShowlet param="config" configParam="contentType" /></wp:parameter>
			<wp:parameter name="finalPageDest" ><wp:currentPage param="code" /></wp:parameter>
		</wp:url>" ><wp:i18n key="jpfastcontentedit_EDIT_CONTENT" />
	</a>
	&#32;|&#32;
	<a href="<wp:url page="fastcontentedit" >
			<wp:parameter name="delete" >true</wp:parameter>
			<wp:parameter name="contentId" ><c:out value="${contentId}" /></wp:parameter>
			<wp:parameter name="contentTypeCode" ><wp:currentShowlet param="config" configParam="contentType" /></wp:parameter>
			<wp:parameter name="finalPageDest" ><wp:currentPage param="code" /></wp:parameter>
		</wp:url>" ><wp:i18n key="jpfastcontentedit_REMOVE_CONTENT" />
	</a>
</fce:isContentAllowed>
</wp:ifauthorized>

			</li>
		</c:forEach>
		</ul>
		<c:if test="${groupContent.size > groupContent.max}">
			<p class="paginazione">
				<c:choose>
				<c:when test="${'1' == groupContent.currItem}">&lt;&lt; <wp:i18n key="PREV" /></c:when>
				<c:otherwise><a href="<wp:url paramRepeat="true" ><wp:parameter name="${groupContent.paramItemName}" ><c:out value="${groupContent.prevItem}"/></wp:parameter></wp:url>">&lt;&lt; <wp:i18n key="PREV" /></a></c:otherwise>
				</c:choose>
				<c:forEach var="item" items="${groupContent.items}">
					<c:choose>
					<c:when test="${item == groupContent.currItem}">&#32;[<c:out value="${item}"/>]&#32;</c:when>
					<c:otherwise>&#32;<a href="<wp:url paramRepeat="true" ><wp:parameter name="${groupContent.paramItemName}" ><c:out value="${item}"/></wp:parameter></wp:url>"><c:out value="${item}"/></a>&#32;</c:otherwise>
					</c:choose>
				</c:forEach>
				<c:choose>
				<c:when test="${groupContent.maxItem == groupContent.currItem}"><wp:i18n key="NEXT" /> &gt;&gt;</c:when>
				<c:otherwise><a href="<wp:url paramRepeat="true" ><wp:parameter name="${groupContent.paramItemName}" ><c:out value="${groupContent.nextItem}"/></wp:parameter></wp:url>"><wp:i18n key="NEXT" /> &gt;&gt;</a></c:otherwise>
				</c:choose>
			</p>
		</c:if>

	</wp:pager>

</c:when>
	<c:otherwise>
		<p><wp:i18n key="LIST_EMPTY" /></p>
	</c:otherwise>
</c:choose>

<c:if test="${null != pageLinkVar && null != pageLinkDescriptionVar}">
	<p><a href="<wp:url page="${pageLinkVar}"/>"><c:out value="${pageLinkDescriptionVar}" /></a></p>
</c:if>

<wp:ifauthorized permission="jpfastcontentedit_editContents" >
	<p><a class="linkInEvidenza" href="<wp:url page="fastcontentedit" >
			<wp:parameter name="contentTypeCode" ><wp:currentShowlet param="config" configParam="contentType" /></wp:parameter>
			<wp:parameter name="finalPageDest" ><wp:currentPage param="code" /></wp:parameter>
		</wp:url>" ><wp:i18n key="jpfastcontentedit_LABEL_NEW_CONTENT" />
	</a></p>
</wp:ifauthorized>