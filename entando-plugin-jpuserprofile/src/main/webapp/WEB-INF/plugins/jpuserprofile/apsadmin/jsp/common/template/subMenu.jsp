<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<li class="openmenu"><a href="#" rel="fagiano_jpuserprofile" id="menu_jpuserprofile" class="subMenuToggler"  ><s:text name="jpuserprofile.menu.profileAdmin" /></a>
<div id="fagiano_jpuserprofile" class="menuToggler"><div class="menuToggler-1"><div class="menuToggler-2">
	<ul>
		<li><a href="<s:url namespace="/do/jpuserprofile/MyProfile" action="edit" />" ><s:text name="jpuserprofile.menu.changeUserProfile" /></a></li>
		<wp:ifauthorized permission="jpuserprofile_profile_view">
		<li><a href="<s:url namespace="/do/jpuserprofile" action="list" />" ><s:text name="jpuserprofile.menu.searchProfiles" /></a></li>
		</wp:ifauthorized>
		<wp:ifauthorized permission="superuser">
		<li><a href="<s:url namespace="/do/Entity" action="initViewEntityTypes"><s:param name="entityManagerName">jpuserprofileUserProfileManager</s:param></s:url>" ><s:text name="jpuserprofile.menu.profileTypeAdmin" /></a></li>
		</wp:ifauthorized>
	</ul>
</div></div></div>
</li>