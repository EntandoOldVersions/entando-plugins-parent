<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="superuser">
	<li class="openmenu"><a href="#" rel="fagiano_jpwebmail" id="fagiano_menu_jpwebmail" class="subMenuToggler" ><s:text name="jpwebmail.admin.menu" /></a>
		<div id="fagiano_jpwebmail" class="menuToggler"><div class="menuToggler-1"><div class="menuToggler-2">
			<ul>
				<%--
				<li><a href="<s:url action="intro" namespace="/do/jpwebmail/WebMail" />" ><s:text name="jpwebmail.admin.submenu.webmail" /></a></li>
				--%>
				<li><a href="<s:url action="edit" namespace="/do/jpwebmail/Config" />" ><s:text name="jpwebmail.admin.menu.config" /></a></li>
			</ul>
		</div></div></div>
		
	</li>
</wp:ifauthorized>