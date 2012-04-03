<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%--
jpuserprofile_EDITPASSWORD_TITLE = Modifica Password
jpuserprofile_PASSWORD_UPDATED = La password è stata aggiornata correttamente.
jpuserprofile_PLEASE_LOGIN_AGAIN = E' necessario riloggarsi.
--%>

<h2><wp:i18n key="jpuserprofile_EDITPASSWORD_TITLE" /></h2>

<p><wp:i18n key="jpuserprofile_PASSWORD_UPDATED" /></p>

<s:if test="!#session.currentUser.credentialsNonExpired">
	<p>
		<a href="<s:url action="logout" namespace="/do" />" ><wp:i18n key="jpuserprofile_PLEASE_LOGIN_AGAIN" /></a>
	</p>
</s:if>