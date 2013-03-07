<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<%@ taglib uri="/jpcalendar-aps-core" prefix="cal" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%
/*  
	Author: William Ghelfi <w.ghelfi@agiletec.it> - 2005/05/23
	Author: Eugenio Santoboni <e.santoboni@agiletec.it>
	Author: Rinaldo Bonazzo <r.bonazzo@agiletec.it>
	Erogatore automatico di contenuti eventi del giorno.
	
*/  		
%>

<c:choose>
	<c:when test="${empty param.selectedDate}">
	<h2><wp:i18n key="jpcalendar_EVENTS_TITLE" /></h2>
	</c:when>
	<c:otherwise>
	<fmt:parseDate value="${param.selectedDate}" pattern="yyyyMMdd" var="currentDate"/> 
	<h2><wp:i18n key="jpcalendar_EVENTS_TITLE" />: <fmt:formatDate value="${currentDate}" dateStyle="full"/></h2>
	</c:otherwise>
</c:choose>

<cal:eventsOfDay listName="contentList" />

<c:if test="${contentList != null}">

	<wp:pager listName="contentList" objectName="groupContent" pagerIdFromFrame="true" max="1" >
		<c:forEach var="content" items="${contentList}" begin="${groupContent.begin}" end="${groupContent.end}">
			<jacms:content contentId="${content}" modelId="list"/>
		</c:forEach>	

		<c:if test="${groupContent.size > groupContent.max}">
			<p>
				<c:choose>
				<c:when test="${'1' == groupContent.currItem}"><wp:i18n key="jpcalendar_EVENTS_PREVIOUS" /></c:when>
				<c:otherwise><a href="<wp:url paramRepeat="true" ><wp:parameter name="${groupContent.paramItemName}" ><c:out value="${groupContent.prevItem}"/></wp:parameter></wp:url>"><wp:i18n key="jpcalendar_EVENTS_PREVIOUS" /></a></c:otherwise>					
				</c:choose>
				<c:forEach var="item" items="${groupContent.items}">
					<c:choose>
					<c:when test="${item == groupContent.currItem}">&#32;[<c:out value="${item}"/>]&#32;</c:when>
					<c:otherwise>&#32;<a href="<wp:url paramRepeat="true" ><wp:parameter name="${groupContent.paramItemName}" ><c:out value="${item}"/></wp:parameter></wp:url>"><c:out value="${item}"/></a>&#32;</c:otherwise>
					</c:choose>
				</c:forEach>
				<c:choose>
				<c:when test="${groupContent.maxItem == groupContent.currItem}"><wp:i18n key="jpcalendar_EVENTS_NEXT" /></c:when>
				<c:otherwise><a href="<wp:url paramRepeat="true" ><wp:parameter name="${groupContent.paramItemName}" ><c:out value="${groupContent.nextItem}"/></wp:parameter></wp:url>"><wp:i18n key="jpcalendar_EVENTS_NEXT" /></a></c:otherwise>					
				</c:choose>
			</p>
		</c:if>
		
	</wp:pager>

</c:if>	
