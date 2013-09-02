<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- Tag di MyPortal --%>
<%@ taglib prefix="jpmp" uri="/jpmyportal-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- Acquisisci le proprietà del frame corrente --%>
<jpmp:customizableFrame>
<%-- APRE IL CORPO SE PRESENTE ESISTE UTENTE LOGGATO (NO GUEST O ADMIN) IL FRAME E NON é BLOCCATO --%>
<wp:headInfo type="CSS_Plugin" info="jpmyportal/static/css/jpmyportal.css" />

<wp:currentWidget param="code" var="thisShowletCode" />
<jpmp:requestContextParam param="currentFrame" var="thisShowletFrame"/>
<jpmp:requestContextParam param="extraParamCurrentPage" var="currentPage"/>
<wp:currentPage param="code" var="currentPageCode" />
<wp:info key="currentLang" var="currentLangCode" />

<jpmp:ajaxEnabled>
<wp:headInfo type="JS_Plugin" info="jpmyportal/static/js/iShowlet.js" />
	
<c:set var="I_SHOWLET">
	var varajaxSwapUrl="<wp:info key="systemParam" paramName="applicationBaseURL" />/do/Ajax/jpmyportal/MyPortal/swapFrames.action";
	var varajaxDeleteUrl="<wp:info key="systemParam" paramName="applicationBaseURL" />/do/Ajax/jpmyportal/MyPortal/emptyFrame.action";
	//<c:set var="handleToolTip"><wp:i18n key="jpmyportal_HANDLE_TOOLTIP" /></c:set>
	var varhandleToolTipText="<c:out value="${handleToolTip}" escapeXml="true"/>";
	var varajaxParamCurrentPage="<wp:currentPage param="code" />";	
</c:set>
	
	<wp:headInfo type="JS_EXT" info="${I_SHOWLET}" />
	<wp:headInfo type="JS_Plugin" info="jpmyportal/static/js/jpmyportal.js" /> 
	
	<span class="noscreen this_editable"><span class="<c:out value="${thisShowletFrame}" />"></span></span>
</jpmp:ajaxEnabled>

<%-- OTTIENE LA PAGINA CORRENTE --%>

<c:choose>
	<c:when test="${thisShowletCode eq 'jpmyportal_void'}">
	<form class="myPortalForm" action="<wp:url page="customizeuserconfig"><wp:parameter name="currentPage"><c:out value="${currentPageCode}" /></wp:parameter><wp:parameter name="currentPageDescr"><c:out value="${currentPage.descr}" /></wp:parameter><wp:parameter name="frameSource"><c:out value="${thisShowletFrame}" /></wp:parameter></wp:url>" method="post" >
		<p><input class="inputNew" type="image" title="<wp:i18n key="jpmyportal_ADD_SHOWLET" />" value="<wp:i18n key="jpmyportal_NEW" />" src="<wp:resourceURL/>/plugins/jpmyportal/static/img/window-new.png" alt="<wp:i18n key="jpmyportal_NEW" />" /></p>
 	</form>
	</c:when>
	<c:otherwise>
		<%-- acquisisci l'elenco delle showlet scambiabili con quella contenuta nel frame corrente --%>
		<jpmp:swappableFrames var="swappableFrames" />
		<c:choose>
			<c:when test="${param.frameToEdit eq thisShowletFrame}">
				<form class="myPortalForm" action="<wp:info key="systemParam" paramName="applicationBaseURL" />/do/Front/jpmyportal/MyPortal/swapFrames.action" method="post"  >
					<p class="noscreen">
					<input type="hidden" name="currentPage" value="${currentPageCode}" />
					<input type="hidden" name="currentLangCode" value="${currentLangCode}" />
					<input type="hidden" name="frameSource" value="${thisShowletFrame}" />
					</p>
					<p class="inputExchange">
					<label for="frameDest"><wp:i18n key="jpmyportal_EXCHANGE_TO" />:</label>
					<select name="frameDest" id="frameDest">
						<c:forEach items="${swappableFrames}" var="swappableElement">
							<option value="<c:out value="${swappableElement.key}" />"><wp:i18n key="jpmyportal_BOX" />&#32;<c:out value="${swappableElement.key-3}" /></option>
						</c:forEach>
					</select>
					<input class="inputImage" type="image" value="<wp:i18n key="jpmyportal_EXCHANGE" />" title="<wp:i18n key="jpmyportal_EXCHANGE" />" src="<wp:resourceURL/>/plugins/jpmyportal/static/img/view-refresh.png" alt="<wp:i18n key="jpmyportal_EXCHANGE" />" />
					</p>
				</form>
				<form class="myPortalForm" action="<wp:url />" method="post" >
				<p class="inputBack"><input class="inputImage" type="image" value="<wp:i18n key="jpmyportal_BACK" />" title="<wp:i18n key="jpmyportal_BACK" />" src="<wp:resourceURL/>/plugins/jpmyportal/static/img/edit-undo.png" alt="<wp:i18n key="jpmyportal_BACK" />" /></p>
				</form>
			</c:when>
			<c:otherwise>
				<c:if test="${not empty swappableFrames}">
					<form class="myPortalForm" action="<wp:url><wp:parameter name="frameToEdit"><c:out value="${thisShowletFrame}" /></wp:parameter></wp:url>" method="post" >
						<p>
						<input class="inputOptions" type="image" value="<wp:i18n key="jpmyportal_OPTIONS" />" title="<wp:i18n key="jpmyportal_OPTIONS" />" src="<wp:resourceURL/>/plugins/jpmyportal/static/img/preferences-system.png" alt="<wp:i18n key="jpmyportal_OPTIONS" />" />
						</p>
					</form>
				</c:if>
				<form class="myPortalForm" action="<wp:info key="systemParam" paramName="applicationBaseURL" />/do/Front/jpmyportal/MyPortal/emptyFrame.action" method="post" >
					<p>
					<input class="inputEmpty" type="image" value="<wp:i18n key="jpmyportal_EMPTY" />" title="<wp:i18n key="jpmyportal_DELETE_SHOWLET" />" src="<wp:resourceURL/>/plugins/jpmyportal/static/img/edit-clear.png" alt="<wp:i18n key="jpmyportal_EMPTY" />" />
					<input type="hidden" name="currentPage" value="${currentPageCode}" />
					<input type="hidden" name="currentLangCode" value="${currentLangCode}" />
					<input type="hidden" name="frameSource" value="${thisShowletFrame}" />
					</p>
				</form>
			</c:otherwise>
		</c:choose>
		
	</c:otherwise>
</c:choose>
</jpmp:customizableFrame>
	<%-- PANNELLO DI CONTROLLO DI MYPORTAL END --%>