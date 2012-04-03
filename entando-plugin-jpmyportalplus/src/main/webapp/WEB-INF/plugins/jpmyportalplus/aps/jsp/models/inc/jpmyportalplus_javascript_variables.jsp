<%--
File contenente i parametri di configurazione per lo script del modello di
pagina con l'interfaccia javascript
-----------------------------------------------------------------------........
URL.paramNames.startFramePos: nome del parametro passato all'action swapFrames che indica l'id del frame di partenza
URL.paramNames.endFramePos: nome del parametro passato all'action swapFrames che indica l'id del frame di arrivo (non utilizzato)
URL.paramNames.targetPrevFramePos: nome del parametro passato all'action swapFrames che indica l'id del frame precedente di arrivo
URL.paramNames.targetNextFramePos: nome del parametro passato all'action swapFrames che indica l'id del frame successivo di arrivo
URL.paramNames.frameToEmpty: nome del parametro passato all'action removeFrame.action che indica l'id del frame successivo di arrivo
URL.paramNames.formMove: nome del parametro passato all'action per il form di spostamento

URL.moveFrame: URL dell'action di spostamento/scambio/inserimento showlet
URL.removeFrame: URL dell'action di rimozione showlet
URL.closeFrame: URL dell'action di chiusura box showlet
URL.openFrame: URL dell'action di apertura box showlet
URL.openFrameStandard: URL dell'action di apertura showlet NON AJAX
URL.closeFrameStandard: URL dell'action di chiusura showlet NON AJAX
URL.formMove: URL dall'action che restituisce il form di spostamento aggiornato

frames.draggable_column1: primo frame della colonna 1
frames.draggable_column2: primo frame della colonna 2
frames.draggable_column3: primo fram della colonna 3
(le colonne sono indicate con i rispettivi id in /resources/plugins/jpmyportalplus/static/js/jpmyportalplus.js riga 8)

statusParams.edit: nome del parametro per lo stato del form di modifica
statusParams.openClose: nome del parametro per lo stato del box

i18n.loading: etichetta mostrata durante il caricamento del form di spostamento
i18n.error: etichetta mostrata durante gli errori di caricamento

-----------------------------------------------------------------------
--%>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpmpp" uri="/jpmyportalplus-core" %>
<%@ page contentType="charset=UTF-8" %>

<script type="text/javascript">
<!--//--><![CDATA[//><!--
	var JPMYPORTALPLUS_CONFIG = {
		"URL" : {
			"paramNames": {
				"startFramePos": "startFramePos",
				"endFramePos": "targetFramePos",
				"targetPrevFramePos": "targetPrevFramePos",
				"targetNextFramePos": "targetNextFramePos",
				"frameToEmpty": "frameToEmpty",
				"formMove": "frameWhereOpenSection",
				"configWidget": "frameWhereOpenSection"
			},
			"moveFrame": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/ajax/swapFrames.action?request_locale=<wp:info key="currentLang" />&currentPageCode=<wp:currentPage param="code" />",
			"removeFrame": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/ajax/removeFrame.action?request_locale=<wp:info key="currentLang" />&currentPageCode=<wp:currentPage param="code" />",
			"closeFrame": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/ajax/closeFrame.action?request_locale=<wp:info key="currentLang" />&currentPageCode=<wp:currentPage param="code" />",
			"openFrame": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/ajax/openFrame.action?request_locale=<wp:info key="currentLang" />&currentPageCode=<wp:currentPage param="code" />",
			"openFrameStandard": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/openFrame.action?request_locale=<wp:info key="currentLang" />&currentPageCode=<wp:currentPage param="code" />",
			"closeFrameStandard": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/closeFrame.action?request_locale=<wp:info key="currentLang" />&currentPageCode=<wp:currentPage param="code" />",
			"formMove": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/ajax/openSwapSection.action?request_locale=<wp:info key="currentLang" />&currentPageCode=<wp:currentPage param="code" />",
			"configWidget": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/ajax/openConfigSection.action?request_locale=<wp:info key="currentLang" />&currentPageCode=<wp:currentPage param="code" />"
		},
		"frames": {
			"widget-col1": <jpmpp:columnInfo columnId="1" />,
			"widget-col2": <jpmpp:columnInfo columnId="2" />,
			"widget-col3": <jpmpp:columnInfo columnId="3" />
		},
		"statusParams": {
			"edit" : "editFrame",
			"openclose" : "frameToResize"
		},
		"i18n": {
			"loading": "<wp:i18n key="jpmyportalplus_LOADING_INFO" />",
			"error": "<wp:i18n key="jpmyportalplus_ERROR_INFO" />"
		}
	};
//--><!]]>
</script>