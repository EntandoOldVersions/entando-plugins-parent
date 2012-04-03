<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:contentNegotiation mimeType="application/xhtml+xml" charset="utf-8"/>
<wp:ifauthorized permission="superuser">
	<li class="openmenu"><a href="#" rel="fagiano_jpmail" id="fagiano_menu_jpmail" class="subMenuToggler" ><s:text name="jpmail.admin.menu" /></a>
		<div id="fagiano_jpmail" class="menuToggler">
			<div class="menuToggler-1">
				<div class="menuToggler-2">
					<ul>
						<li><a href="<s:url action="editSmtp" namespace="/do/jpmail/MailConfig" />" ><s:text name="jpmail.admin.menu.smtp" /></a></li>
						<li><a href="<s:url action="viewSenders" namespace="/do/jpmail/MailConfig" />" ><s:text name="jpmail.admin.menu.senders" /></a></li>
					</ul>
				</div>
			</div>
		</div>
	</li>
</wp:ifauthorized>