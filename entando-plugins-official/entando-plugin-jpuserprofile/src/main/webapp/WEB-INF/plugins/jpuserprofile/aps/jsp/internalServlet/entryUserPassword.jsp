<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<%--
jpuserprofile_EDITPASSWORD = Modifica Password
jpuserprofile_OLDPASSWORD = Vecchia password
jpuserprofile_NEWPASS = Nuova password
jpuserprofile_CONFIRM_NEWPASS = Conferma nuova password
jpuserprofile_SAVE_PASSWORD = Salva password
jpuserprofile_PLEASE_LOGIN_TO_EDIT_PASSWORD = E' necessario logarsi per cambiare la password

--%>

<h2><wp:i18n key="jpuserprofile_EDITPASSWORD" /></h2>

<c:choose>
	<c:when test="${sessionScope.currentUser != 'guest'}">
		
		<form action="<wp:action path="/ExtStr2/do/jpuserprofile/Front/CurrentUser/changePassword.action" />" method="post" class="newContentForm">
			
			<s:if test="hasFieldErrors()">
				<h3><wp:i18n key="jpuserprofile_MESSAGE_TITLE_FIELDERRORS" /></h3>
				<ul>
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
							<li><s:property escape="false" /></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</s:if>
			
			<p class="noscreen">
				<wpsf:hidden name="username" />
			</p>
			
			<p>
				<label for="jpuserprofile_oldPassword"><wp:i18n key="jpuserprofile_OLDPASSWORD" /></label>:<br />
				<wpsf:password useTabindexAutoIncrement="true" name="oldPassword" id="jpuserprofile_oldPassword" cssClass="text" />
			</p>
			
			<p>
				<label for="jpuserprofile_newPassword"><wp:i18n key="jpuserprofile_NEWPASS" /></label>:<br />
				<wpsf:password useTabindexAutoIncrement="true" name="password" id="jpuserprofile_newPassword" cssClass="text" />
			</p>
			
			<p>
				<label for="jpuserprofile_newPassword_conf"><wp:i18n key="jpuserprofile_CONFIRM_NEWPASS" /></label>:<br />
				<wpsf:password useTabindexAutoIncrement="true" name="passwordConfirm" id="jpuserprofile_newPassword_conf" cssClass="text" />
			</p>
			<s:set var="savepassword_label"><wp:i18n key="jpuserprofile_SAVE_PASSWORD" /></s:set>
			<p><wpsf:submit useTabindexAutoIncrement="true" value="%{savepassword_label}" /></p>
			
		</form>
			
	</c:when>
	<c:otherwise>
		<p>
			<wp:i18n key="jpuserprofile_PLEASE_LOGIN_TO_EDIT_PASSWORD" />
		</p>
	</c:otherwise>
</c:choose>
