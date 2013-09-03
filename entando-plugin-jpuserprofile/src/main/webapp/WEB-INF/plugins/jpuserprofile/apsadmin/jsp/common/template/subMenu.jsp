<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="superuser">
<li class="openmenu"><a href="#" rel="fagiano_jpuserprofile" id="menu_jpuserprofile" class="subMenuToggler"  ><s:text name="jpuserprofile.menu.profileAdmin" /></a>
<div id="fagiano_jpuserprofile" class="menuToggler"><div class="menuToggler-1"><div class="menuToggler-2">
	<ul>
		<li><a href="<s:url namespace="/do/Entity" action="initViewEntityTypes"><s:param name="entityManagerName">jpuserprofileUserProfileManager</s:param></s:url>" ><s:text name="jpuserprofile.menu.profileTypeAdmin" /></a></li>
	</ul>
</div></div></div>
</li>
</wp:ifauthorized>