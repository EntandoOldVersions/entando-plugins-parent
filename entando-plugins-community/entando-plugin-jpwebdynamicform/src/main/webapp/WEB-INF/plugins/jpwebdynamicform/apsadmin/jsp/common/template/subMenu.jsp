<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<wp:contentNegotiation mimeType="application/xhtml+xml" charset="utf-8"/>
<wp:ifauthorized permission="superuser" var="isSuperuser"/>
<c:choose>
	<c:when test="${isSuperuser}">
	<li class="openmenu"><a href="#" rel="fagiano_jpwebdynamicform" id="menu_jpwebdynamicform" class="subMenuToggler"><s:text name="jpwebdynamicform.menu.admin" /></a>
		<div class="menuToggler" id="fagiano_jpwebdynamicform"><div class="menuToggler-2"><div class="menuToggler-2">
			<ul>
				<li><a href="<s:url action="list" namespace="/do/jpwebdynamicform/Message/Operator" />" ><s:text name="jpwebdynamicform.menu.messages"/></a></li>
				<li><a href="<s:url action="list" namespace="/do/jpwebdynamicform/Message/Config" />" ><s:text name="jpwebdynamicform.menu.config" /></a></li>
				<li><a href="<s:url namespace="/do/Entity" action="initViewEntityTypes"><s:param name="entityManagerName">jpwebdynamicformMessageManager</s:param></s:url>" ><s:text name="jpwebdynamicform.menu.messageTypeAdmin" /></a></li>
			</ul>
		</div></div></div>
	</li>
	</c:when>
	<c:otherwise>
		<wp:ifauthorized permission="jpwebdynamicformOperator">
			<li><a href="<s:url action="list" namespace="/do/jpwebdynamicform/Message/Operator" />" ><s:text name="jpwebdynamicform.menu.messages"/></a></li>
		</wp:ifauthorized>
	</c:otherwise>
</c:choose>