<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<wp:currentPage param="code" var="currentPageCode" />
<c:set var="currentPageCode" value="${currentPageCode}" />

<div class="well well-small">
<ul class="nav nav-list">
<wp:nav var="page">
<c:if test="${previousPage.code != null}">
	<c:set var="previousLevel" value="${previousPage.level}" />
	<c:set var="level" value="${page.level}" />
	<%@ include file="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/showlets/jpfrontshortcut_navigation_menu_include.jsp" %>
</c:if>
	<c:set var="previousPage" value="${page}" />
</wp:nav>
	<c:set var="previousLevel" value="${previousPage.level}" />
	<c:set var="level" value="${0}"  scope="request" /> <%-- we are out, level is 0 --%>
	<%@ include file="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/showlets/jpfrontshortcut_navigation_menu_include.jsp" %>
	<c:if test="${previousLevel >= 2}">
		<%--
		previousLevel - 2 :
			1 because we're starting from 0,
			1 because we skipped 1 cycle in this whole algorithm and previousLevel did not get the last update
		--%>
		<c:set var="endHere" value="${previousLevel - 2}" />
	</c:if>
	<c:if test="${previousLevel < 2}">
		<c:set var="endHere" value="${previousLevel}" />
	</c:if>
	<c:if test="${endHere != 0}">
		<c:forEach begin="${0}" end="${endHere}"></ul></li></c:forEach>
	</c:if>
</ul>
</div>