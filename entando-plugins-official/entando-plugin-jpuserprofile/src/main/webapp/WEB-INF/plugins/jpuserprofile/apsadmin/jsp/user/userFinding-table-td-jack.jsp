<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<wpsa:entity entityManagerName="jpuserprofileUserProfileManager" key="%{#user.username}" var="userProfileVar"/>

<td class="icon">
	<s:if test="null != #userProfileVar">
		<a href="<s:url namespace="/do/jpuserprofile/User" action="edit"><s:param name="username" value="#user.username"/></s:url>" title="<s:text name="jpuserprofile.label.profile.edit" />:&#32;<s:property value="#userProfileVar.getValue(#userProfileVar.firstNameAttributeName)"/>&#32;<s:property value="#userProfileVar.getValue(#userProfileVar.surnameAttributeName)"/>" >
			<img src="<wp:resourceURL />plugins/jpuserprofile/administration/img/edit.png" alt="<s:text name="label.alt.edit" />" />
		</a>
	</s:if>
	<s:else><abbr title="<s:text name="jpuserprofile.label.noProfile" />">&ndash;</abbr></s:else>
</td>
<s:set var="userProfileVar" value="null" />