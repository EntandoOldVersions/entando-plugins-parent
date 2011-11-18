<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="superuser">
<li class="openmenu"><a href="#" rel="fagiano_jpuserreg" id="menu_jpuserreg" class="subMenuToggler"  ><s:text name="jpuserreg.menu.userregAdmin" /></a>
<div id="fagiano_jpuserreg" class="menuToggler"><div class="menuToggler-1"><div class="menuToggler-2">
	<ul>
		<li><a href="<s:url namespace="/do/jpuserreg/Config" action="edit" />" ><s:text name="jpuserreg.menu.config" /></a></li>
	</ul>
</div></div></div>
</li>
</wp:ifauthorized>