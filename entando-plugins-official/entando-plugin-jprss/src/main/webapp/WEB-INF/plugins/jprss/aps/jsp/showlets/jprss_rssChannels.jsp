<%@ taglib prefix="jprss" uri="/jprss-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jprss:rssList listName="rssList" />
<wp:info key="langs" var="langs" />

<h2><wp:i18n key="jprss_FEED_LIST"/></h2>
<c:forEach var="channel" items="${rssList}">
	<h3><c:out value="${channel.title}"/></h3>
	<p><c:out value="${channel.description}"/></p> 
	<ul>
		<c:forEach var="channelLang" items="${langs}">
			<li><a href="<wp:info key="systemParam" paramName="applicationBaseURL"/>do/jprss/Rss/Feed/show.action?id=<c:out value="${channel.id}"/>&amp;lang=<c:out value="${channelLang.code}"/>"><c:out value="${channelLang.descr}"/></a></li>
		</c:forEach>
	</ul>
</c:forEach>