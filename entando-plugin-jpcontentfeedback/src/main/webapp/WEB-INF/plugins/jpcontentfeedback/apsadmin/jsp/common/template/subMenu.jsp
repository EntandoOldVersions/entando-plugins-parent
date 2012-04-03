<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="jpcontentfeedback_moderate">
	<li class="openmenu"><a href="#" rel="fagiano_jpcontentfeedback" id="fagiano_menu_jpcontentfeedback" class="subMenuToggler" ><s:text name="jpcontentfeedback.admin.menu" /></a>
		<div id="fagiano_jpcontentfeedback" class="menuToggler">
			<div class="menuToggler-1">
				<div class="menuToggler-2">
					<ul>
						<li><a href="<s:url action="list" namespace="/do/jpcontentfeedback/Comments" />" ><s:text name="jpcontentfeedback.admin.menu.contentFeedback" /></a></li>
						<wp:ifauthorized permission="superuser">
								<li><a href="<s:url action="edit" namespace="/do/jpcontentfeedback/Config" />" ><s:text name="jpcontentfeedback.admin.menu.contentFeedback.edit" /></a></li>
						</wp:ifauthorized>
					</ul>
				</div>
			</div>
		</div>
	</li>
</wp:ifauthorized>



