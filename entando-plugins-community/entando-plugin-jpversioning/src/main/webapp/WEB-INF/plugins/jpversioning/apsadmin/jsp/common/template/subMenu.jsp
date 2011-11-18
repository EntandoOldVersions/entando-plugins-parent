<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="superuser">
	<li class="openmenu"><a href="#" rel="fagiano_jpversioning" id="menu_jpversioning" class="subMenuToggler" ><s:text name="jpversioning.admin.menu" /></a>
		<div class="menuToggler" id="fagiano_jpversioning"><div class="menuToggler-1"><div class="menuToggler-2">
			<ul>
				<li><a href="<s:url action="list" namespace="/do/jpversioning/Content/Versioning" />" id="menu_versioning" ><s:text name="jpversioning.menu.contentList" /></a></li>
				<li><a href="<s:url action="list" namespace="/do/jpversioning/Resource/Trash"><s:param name="resourceTypeCode" >Image</s:param></s:url>" ><s:text name="jpversioning.menu.images" /></a></li>
				<li><a href="<s:url action="list" namespace="/do/jpversioning/Resource/Trash"><s:param name="resourceTypeCode" >Attach</s:param></s:url>" ><s:text name="jpversioning.menu.attaches" /></a></li>
			</ul>
		</div></div></div>
	</li>
</wp:ifauthorized>