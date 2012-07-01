<%@ taglib prefix="mppl" uri="/jpmyportalplus-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="charset=UTF-8" %>

<%-- variabile showletId: il prefisso showlet id "__s__" è referenziato in jpmyportalplus.js --%>
<c:set var="showletId" scope="request">showlet_<wp:currentShowlet param="code" />__s__<mppl:requestContextParam param="currentFrame" /></c:set>

<%--
Struttura del box trascinabile
------------------------------

È presente un contenitore che raccoglie tutto: dragdrop_box.
All'interno vi sono principalmente due aree: editContentBox, content.
"editContentBox" contiene il form di sposta per l'accessibilità
"content" contiene il contenuto vero e proprio.

"editContentBox" è viene aperto e chiuso dal link con classe css "editContent".
"content" è viene aperto e chiuso dal link con classe css "openClose".

l'area di trascinamento è individuata dall'elemento avente classe css "dragdrop_draggable_area".
Può essere un qualsiasi elemento ed attiva l'hover del box.


Il css che costruisce la struttura del box della showlet è /plugins/jpmyportalplus/static/css/jpmyportalplus.css
(vedere il file per individuare le parti personalizzabili)

Di sotto sono descritte le funzioni di ciascuna classe css. Notare che la
posizione e l'ordine di dei bottoni "openClose" "removeBox" "editContent"
e dell'area di trascinamento "dragdrop_draggable_area"
è libera, ovvero questi elementi possono essere posizionati ovunque in
qualsiasi posizione all'interno del box principale (dragdrop_box)

È possibile aggiungere altri "spacer" allo stesso livello del div
con classe "boxspacer" per creare altro spazio oppure per aggiungere
delle immagini di sfondo.

È altresì possibile aggiungere una sorta di testata nel box prima di "editContentBox"
--%>

<%--
//contenitore del box
class="dragdrop_box" : classe css referenziata in jpmyportalplus.js, serve per dichiarare che è un box che può essere spostato
id="<c:out value="${showletId}" />" : verrà sostituito con l'identificativo della showlet o della sua attuale posizione

//area di trascinamento
class="dragdrop_draggable_area": classe css referenziata in jpmyportalplus.js, serve per dichiarare qual'è l'elemento "maniglia" per il trascinamento

//bottoni edit/apri/rimuovi
class="editContent" : referenziato in jpmyportalplus.js, fa funzionare l'apertura del box di configurazione
class="openClose": referenziato in jpmyportalplus.js, apre e chiude il contenuto di "editcontentBox" e "content"
class="removeBox": referenziato in jpmyportalplus.js, rimuove il box corrente

//contenitore per opzioni e contenuto vero e proprio
class="editContentBox": referenziato in jpmyportalplus.js, dovrebbe contenere le opzioni per il box, si apre e si chiude alla pressione di "class="editContent"
class="content" : referenziato in jpmyportalplus.js, circonda il contenuto vero e proprio, si apre e si chiude alla pressione di "class"openClose""
--%>

<%-- frame corrente --%>
	<mppl:requestContextParam param="currentFrame" var="currentFrame" />

<%-- stato del box di modifica --%>
	<c:set var="isEditFormOpen" value="${currentFrame == param.editFrame}" />
	<c:set var="isClosed" value="${false}" />
	<mppl:isCurrentFrameClosed><c:set var="isClosed" value="${true}" /></mppl:isCurrentFrameClosed>



<%-- removeActionURL: il path all'action che si occupa della rimozione del box --%>
	<c:set var="removeActionURL"><wp:info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/removeFrame.action?frameToEmpty=<c:out value="${currentFrame}" />&currentPageCode=<wp:currentPage param="code" /></c:set>

<%-- opencloseActionURL: il path all'action che si occupa di mostrare o nascondere il contenuto del box --%>
	<c:choose>
		<c:when test="${isClosed}">
			<c:set var="opencloseActionURL"><wp:info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/openFrame.action?frameToResize=<c:out value="${currentFrame}" />&currentPageCode=<wp:currentPage param="code" /></c:set>
		</c:when>
		<c:otherwise>
			<c:set var="opencloseActionURL"><wp:info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/closeFrame.action?frameToResize=<c:out value="${currentFrame}" />&currentPageCode=<wp:currentPage param="code" /></c:set>
		</c:otherwise>
	</c:choose>



<%-- moveActionURL: il path all'action che si occupa di mostrare o nascondere il form per lo spostamento --%>
	<c:choose>
		<c:when test="${isEditFormOpen}"><c:set var="editFrameValue" value="null" /></c:when>
		<c:otherwise><c:set var="editFrameValue" value="${currentFrame}" /></c:otherwise>
	</c:choose>
	<c:set var="moveActionUrl"><wp:url paramRepeat="false" />?editFrame=<c:out value="${editFrameValue}" escapeXml="false" />&currentPageCode=<wp:currentPage param="code" escapeXml="false" /></c:set>

<div class="dragdrop_box" id="<c:out value="${showletId}" />"> <%-- contenitore del box | class="dragdrop_box" : classe css referenziata in jpmyportalplus.js, serve per dichiarare che è un box che può essere spostato | id="<c:out value="${showletId}" />" : identificativo della showlet o della sua attuale posizione --%>
	<div class="boxspacer"> <%-- classe spaziatrice: serve per dare eventuale del padding (non esiste in nessun css, se non serve rimuovere) --%>
		<div class="dragdrop_draggable_area"> <%-- class="dragdrop_draggable_area": classe css referenziata in jpmyportalplus.js, serve per dichiarare qual'è l'elemento "maniglia" per il trascinamento. --%>
				<div class="shortcut">
					<a href="<c:out value="${removeActionURL}" />" class="button removeBox" title="remove"><img src="<wp:resourceURL />plugins/jpmyportalplus/static/img/remove.png" alt="remove" /></a>&#32;<%-- class="removeBox": referenziato in jpmyportalplus.js, rimuove il box corrente --%>
					<a href="<c:out value="${moveActionUrl}" />" class="button editContent" title="configure/options"><img src="<wp:resourceURL />plugins/jpmyportalplus/static/img/configure.png" alt="move" /></a>
					<a href="<c:out value="${opencloseActionURL}" />" class="button openClose" title="toggle open close"><img src="<wp:resourceURL />plugins/jpmyportalplus/static/img/open-close.png" alt="open/close" /></a>&#32;<%-- class="openClose": referenziato in jpmyportalplus.js, apre e chiude il contenuto di "editcontentBox" e "content" --%>
				</div>
				<c:choose>
					<c:when test="${!empty showletTitle}">
						<h2><c:out value="${showletTitle}" escapeXml="false" /></h2>
					</c:when>
					<c:otherwise>
						<h2><wp:currentShowlet param="title" /></h2>
					</c:otherwise>
				</c:choose>
		</div>

		<%--
		 --%>
		<%-- class="editContent" : referenziato in jpmyportalplus.js, fa funzionare l'apertura del box di configurazione --%>
		
		
		<div class="editContentBox <c:if test="${!isEditFormOpen}">hide</c:if>"> 
			<form action="<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/swapFrames.action" method="post">
				<p>
					<input type="hidden" name="currentPageCode" value="<wp:currentPage param="code" />" />
					<input type="hidden" name="startFramePos" value="<mppl:requestContextParam param="currentFrame" />" />
					<label for="scambia_<c:out value="${showletId}" />"><wp:i18n key="jpmyportalplus_MOVETHISSHOWLET" /></label>:<br />
					<mppl:frameSelectItem var="selectItems" />
					<select id="scambia_<c:out value="${showletId}" />" name="targetFramePos">
						<c:forEach items="${selectItems}" var="selectItem">
							<option value="<c:out value="${selectItem.frameId}" />">
							<c:choose>
								<c:when test="${selectItem.sameColumn}"><wp:i18n key="jpmyportalplus_SWAPITWITH" />&#32;<c:out value="${selectItem.title}" /></c:when>
								<c:otherwise><wp:i18n key="jpmyportalplus_INSERTINTOCOLUMN" />&#32;<c:out value="${selectItem.columnIdDest}" /></c:otherwise>
							</c:choose>
							</option>
						</c:forEach>
					</select>
				</p>
				<p>
					<input class="button" type="submit" value="<wp:i18n key="jpmyportalplus_MOVE" />" />
				</p>
			</form>
			<%-- 
			<wp:internalServlet actionPath="/ExtStr2/do/jpmyportalplus/front/ajax/openConfigSection.action" />
			--%>
		</div>
		<%-- class="content" : referenziato in jpmyportalplus.js, circonda il contenuto vero e proprio, si apre e si chiude alla pressione di "class"openClose"" --%>
		<div class="dragdrop_content <c:if test="${isClosed}">hide</c:if>">

<%-- reset dei parametri --%>
<c:set var="removeActionURL" value="${null}" />
<c:set var="opencloseActionURL" value="${null}" />
<c:set var="moveActionURL" value="${null}" />