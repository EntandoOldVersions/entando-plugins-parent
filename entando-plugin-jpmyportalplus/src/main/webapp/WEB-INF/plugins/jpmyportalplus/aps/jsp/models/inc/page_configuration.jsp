<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpmpp" uri="/jpmyportalplus-core" %>
<%-- edit dragdrop boxes --%>
<div class="jpmyportalplus_page_configuration">
	<%-- stato box modifica --%>
	<c:choose>
		<c:when test="${!empty param.openFrame && 'editshowletlist' == param.openFrame}">
			<c:set var="edit_link_href_value" value="${null}" />
			<c:set var="isConfigureShowletsOpen" value="${true}" />
		</c:when>
		<c:otherwise>
			<c:set var="edit_link_href_value" value="editshowletlist#editshowlet" />
			<c:set var="isConfigureShowletsOpen" value="${false}" />
		</c:otherwise>
	</c:choose>

	<div id="editshowletlist" class="<c:if test="${!isConfigureShowletsOpen}">hide</c:if>">
	<%-- configure homepage --%>
		<form action="<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/addWidgets.action#editshowlet" method="post">
				<p><wp:i18n key="JPMYPORTALPLUS_CONFIG_INTRO"/></p>
				<jpmpp:customizableShowlets var="customizableShowlets" />
				<%-- configurable showlet list --%>
					<c:if test="${!empty customizableShowlets}">
						<ul>
							<c:forEach var="customizableShowlet" items="${customizableShowlets}" varStatus="status">
								<li>
									<input id="check<c:out value="${status.count}" />" type="checkbox" name="showletToShow" <c:if test="${customizableShowlet.checked}" >checked="checked"</c:if> value="<c:out value="${customizableShowlet.code}" />" />&nbsp;<label for="check<c:out value="${status.count}" />"><c:out value="${customizableShowlet.title}" /></label>
								</li>
							</c:forEach>
						</ul>
					</c:if>
				<%-- submit --%>
				<p>
					<input type="hidden" name="currentPageCode" value="<wp:currentPage param="code" />" />
					<input class="button" id="applica_submit" type="submit" value="<wp:i18n key="JPMYPORTALPLUS_APPLY"/>" />
				</p>
		</form>
	<%-- reset the homepage --%>
		<form action="<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/resetFrames.action" method="post">
			<p>
				<wp:i18n key="jpmyportalplus_RESET_INTRO" /><br />
			</p>
			<p class="submit_reset">
				<input type="hidden" name="currentPageCode" value="<wp:currentPage param="code" />" />
				<input class="button" id="reset_submit" name="reset_submit" type="submit" value="<wp:i18n key="JPMYPORTALPLUS_RESET"/>" />
			</p>
		</form>

	</div>
</div>
<%-- edit dragdrop boxes --%>