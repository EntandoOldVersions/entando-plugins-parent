<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/aps-core" prefix="wp" %>

<c:choose>
	<c:when test="${sessionScope.currentUser != 'guest'}">
		<s:action name="editPassword" namespace="/do/jpuserprofile/Front/CurrentUser" executeResult="true"></s:action>
		<s:action name="edit" namespace="/do/jpuserprofile/Front/CurrentUser/Profile" executeResult="true"></s:action>
	</c:when>
	<c:otherwise>
		<p>
			<wp:i18n key="jpuserprofile_PLEASE_LOGIN" />
		</p>
	</c:otherwise>
</c:choose>