<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<li class="openmenu"><a href="#" rel="fagiano_jpaddressbook_list" id="menu_jpaddressbook" class="subMenuToggler"><s:text name="jpaddressbook.title.addressbook" /></a>
	<div class="menuToggler" id="fagiano_jpaddressbook_list"><div class="menuToggler-1"><div class="menuToggler-2">
		<ul>
			<li><a href="<s:url namespace="/do/jpaddressbook/AddressBook" action="intro" />" ><s:text name="jpaddressbook.title.menuList" /></a></li>
			<wp:ifauthorized permission="superuser">
					<li><a href="<s:url namespace="/do/jpaddressbook/VCard" action="edit" />" ><s:text name="jpaddressbook.vcard.conf" /></a></li> 
			</wp:ifauthorized>
		</ul>
	</div></div></div>
</li>