<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<wp:ifauthorized permission="superuser">
<li class="openmenu"><a href="#" rel="fagiano_jpcontentworkflow" id="menu_jpcontentworkflow" class="subMenuToggler" ><s:text name="jpcontentworkflow.menu.workflowAdmin" /></a>
	<div class="menuToggler" id="fagiano_jpcontentworkflow"><div class="menuToggler-1"><div class="menuToggler-2">
		<ul>
			<li><a href="<s:url action="list" namespace="/do/jpcontentworkflow/Workflow" />" ><s:text name="jpcontentworkflow.menu.workflow" /></a></li>
			<li><a href="<s:url action="config" namespace="/do/jpcontentworkflow/Notifier" />" ><s:text name="jpcontentworkflow.menu.notifier" /></a></li>
		</ul>
	</div></div></div>
</li>
</wp:ifauthorized>