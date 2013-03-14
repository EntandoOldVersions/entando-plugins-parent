<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<wpsa:entity entityManagerName="jpuserprofileUserProfileManager" key="%{#user.username}" var="userProfileVar"/>

<td class="icon_double">
	<%--
	<a href="<s:url namespace="/do/jpuserprofile/User" action="edit"><s:param name="username" value="#user.username"/></s:url>" title="<s:text name="jpuserprofile.label.profile.edit" />:&#32;<s:property value="#userProfileVar.getValue(#userProfileVar.firstNameAttributeName)"/>&#32;<s:property value="#userProfileVar.getValue(#userProfileVar.surnameAttributeName)"/>" >
		<img src="<wp:resourceURL />plugins/jpuserprofile/administration/common/img/icons/edit.png" alt="<s:text name="label.alt.edit" />" />
	</a>
	--%>
	<c:if test="${hasEditProfilePermission}"><a href="<s:url action="edit" namespace="/do/jpuserprofile/User"><s:param name="username" value="#usernameVar"/></s:url>"
		title="<s:text name="jpuserprofile.label.editProfile" />:&#32;<s:property value="#userProfileVar.getValue(#userProfileVar.firstNameAttributeName)"/>&#32;<s:property value="#userProfileVar.getValue(#userProfileVar.surnameAttributeName)"/>"><img src="<wp:resourceURL/>plugins/jpuserprofile/administration/common/img/icons/edit.png" alt="<s:text name="jpuserprofile.label.editProfile" />" /></a></c:if>
	<s:if test="null != #userProfileVar"><a href="<s:url action="view" namespace="/do/jpuserprofile"><s:param name="username" value="#usernameVar"/></s:url>"
		title="<s:text name="jpuserprofile.label.viewProfile" />: <s:property value="#usernameVar" />"><img src="<wp:resourceURL/>plugins/jpuserprofile/administration/common/img/icons/details.png" alt="<s:text name="jpuserprofile.label.viewProfile" />" /></a></s:if>
</td>
<s:set var="userProfileVar" value="null" />