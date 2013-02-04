<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<li class="openmenu"><a href="#" rel="fagiano_jpavatar_list" id="menu_jpavatar" class="subMenuToggler"><s:text name="jpavatar.title.avatar" /></a>
	<div class="menuToggler" id="fagiano_jpavatar_list"><div class="menuToggler-1"><div class="menuToggler-2">
		<ul>
			<li><a href="<s:url action="edit" namespace="/do/jpavatar/Avatar" />" ><s:text name="jpavatar.admin.menu.avatar" /></a></li>
			<wp:ifauthorized permission="superuser">
					<li><a href="<s:url namespace="/do/jpavatar/Config" action="edit" />" ><s:text name="jpavatar.admin.menu.config" /></a></li> 
			</wp:ifauthorized>
		</ul>
	</div></div></div>
</li>