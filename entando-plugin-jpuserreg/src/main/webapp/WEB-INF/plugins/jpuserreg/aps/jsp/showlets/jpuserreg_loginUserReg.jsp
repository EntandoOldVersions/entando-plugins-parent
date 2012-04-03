<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
	<c:when test="${sessionScope.currentUser != 'guest'}">
		<p><wp:i18n key="WELCOME" />, <c:out value="${sessionScope.currentUser}"/>!</p>
		
		<c:if test="${sessionScope.currentUser.japsUser}">
			<p>
				CREATION DATE = <c:out value="${sessionScope.currentUser.creationDate}"/> <br />
				LAST ACCESS = <c:out value="${sessionScope.currentUser.lastAccess}"/> <br />
				LAST PASSWORD CHANGE = <c:out value="${sessionScope.currentUser.lastPasswordChange}"/> <br />
			</p>
		
			<c:if test="${!sessionScope.currentUser.credentialsNotExpired}">
				<p>PASSWORD SCADUTA</p>
				<p>ACCEDERE ALLA PAGINA DI <a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/editPassword.action">CAMBIO PASSWORD</a></p>
			</c:if>
		
		</c:if>
		
		<wp:ifauthorized permission="enterBackend">
		<h3><wp:i18n key="ADMINISTRATION" />:</h3>
		<p>
		<a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/main.action?request_locale=<wp:info key="currentLang" />&amp;backend_client_gui=normal"><wp:i18n key="ADMINISTRATION_BASIC" /></a> | 
		<a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/main.action?request_locale=<wp:info key="currentLang" />&amp;backend_client_gui=advanced"><wp:i18n key="ADMINISTRATION_MINT" /></a>
		</p>
		</wp:ifauthorized>
		<p>
		<a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/logout.action"><wp:i18n key="LOGOUT" /></a>
		</p>

		<wp:ifauthorized permission="superuser" >
			<c:set var="ctrl" value="admin" />
		</wp:ifauthorized>
	
		<c:if test="${ctrl != 'admin'}">
			<p><a href="<wp:url page="disactiv" />" ><wp:i18n key="jpuserreg_ACCOUNT_SUSPENSION" /></a></p>
		</c:if>				
		
		<p><a href="<wp:url page="editprof_page_code" />" ><wp:i18n key="jpuserreg_PROFILE_CONFIGURATION" /></a></p>
		
	</c:when>
	<c:otherwise>
	
	<c:if test="${accountExpired}">
		<p><wp:i18n key="USER_STATUS_EXPIRED" /></p>
	</c:if>
	<c:if test="${wrongAccountCredential}">
		<p><wp:i18n key="USER_STATUS_CREDENTIALS_INVALID" /></p>
	</c:if>
	
	<form action="<wp:url/>" method="post">
		<p>
			<label for="username"><wp:i18n key="USERNAME" />:</label><br />
			<input id="username" type="text" name="username" class="text"/>
		</p>
		<p>
			<label for="password"><wp:i18n key="PASSWORD" />:</label><br />
			<input id="password" type="password" name="password" class="text"/>
		</p>
		<p>
			<input type="submit" value="Ok"  class="button"/>
		</p>
	</form>
	
		<p><a href="<wp:url page="registr" />" ><wp:i18n key="jpuserreg_REGISTRATION" /></a></p>
		<p><a href="<wp:url page="userrecover" />" ><wp:i18n key="jpuserreg_PASSWORD_RECOVER" /></a></p>
	
	</c:otherwise>
</c:choose>

