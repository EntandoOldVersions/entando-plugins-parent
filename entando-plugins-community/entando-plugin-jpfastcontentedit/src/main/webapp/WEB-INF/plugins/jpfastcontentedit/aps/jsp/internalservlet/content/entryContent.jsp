<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<wp:headInfo type="CSS" info="../../plugins/jpfastcontentedit/static/css/editor.css" />
<wp:headInfo type="CSS" info="../../plugins/jpfastcontentedit/static/css/calendar.css" />
<%//FIXME disable these libraries if already included %>
<%//FIXME disable these libraries if already included %>
<wp:headInfo type="JS" info="../../plugins/jpfastcontentedit/static/js/mootools-1.2-core.js" />
<wp:headInfo type="JS" info="../../plugins/jpfastcontentedit/static/js/mootools-1.2-more.js" />
<%//TODO disable these libraries if already included %>
<%//TODO disable these libraries if already included %>
<wp:headInfo type="JS" info="../../plugins/jpfastcontentedit/static/js/moo-japs/moo-jAPS-0.2.js" />
<wp:headInfo type="JS" info="../../plugins/jpfastcontentedit/static/js/calendar_wiz.js" />
<wp:headInfo type="JS" info="../../plugins/jpfastcontentedit/static/js/fckeditor/fckeditor.js" />
<c:set var="js_for_tab">
<s:iterator value="langs" id="lang">
	<s:iterator value="content.attributeList" id="attribute">
	<%-- INIZIALIZZAZIONE TRACCIATORE --%>
	<wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" />

	<s:if test="#attribute.type == 'Date' && #lang.code == 'it' ">
	var myCal_<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />;
	window.addEvent('domready', function() { myCal_<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" /> = new Calendar({ <s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />: 'd/m/Y' }, { 
			navigation: 1, 
			months: ['<s:text name="calendar.month.gen" />','<s:text name="calendar.month.feb" />','<s:text name="calendar.month.mar" />','<s:text name="calendar.month.apr" />','<s:text name="calendar.month.may" />','<s:text name="calendar.month.jun" />','<s:text name="calendar.month.jul" />','<s:text name="calendar.month.aug" />','<s:text name="calendar.month.sep" />','<s:text name="calendar.month.oct" />','<s:text name="calendar.month.nov" />','<s:text name="calendar.month.dec" />'],
			days: ['<s:text name="calendar.week.sun" />','<s:text name="calendar.week.mon" />','<s:text name="calendar.week.tue" />','<s:text name="calendar.week.wen" />','<s:text name="calendar.week.thu" />','<s:text name="calendar.week.fri" />','<s:text name="calendar.week.sat" />']
		});});
	</s:if>
	
	<s:if test="#attribute.type == 'Hypertext'">
		window.addEvent('domready', function() {
			var ofckeditor = new FCKeditor( "<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />" ); 
			ofckeditor.Config["AppBaseUrl"] = "<wp:info key="systemParam" paramName="applicationBaseURL" />";
			ofckeditor.BasePath = "<wp:resourceURL />plugins/jpfastcontentedit/static/js/fckeditor/";
			ofckeditor.Config["CustomConfigurationsPath"] = "<wp:resourceURL />plugins/jpfastcontentedit/static/js/fckeditor/fastContentEdit_jAPSConfig.js";
			ofckeditor.ToolbarSet = "jAPS";			
			ofckeditor.Height = 250;
			ofckeditor.ReplaceTextarea();
		});

	</s:if>
	</s:iterator>
</s:iterator>
</c:set>
<wp:headInfo type="JS_RAW" info="${js_for_tab}" /> 

<wp:i18n key="jpfastcontentedit_FASTCONTENTEDIT_SHOWLET_TITLE" />
<br />

<s:if test="hasFieldErrors()">
	<div class="message message_error">
	<h3><wp:i18n key="jpfastcontentedit_ERRORS" /></h3>
		<ul>
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
					<li><s:property escape="false" /></li>
				</s:iterator>
			</s:iterator>
		</ul>
	</div>
</s:if>

<p><wp:i18n key="jpfastcontentedit_WORKING_ON" />:&#32;<em><s:property value="content.descr" /></em> (<s:property value="content.typeDescr" />)</p>

<s:set name="removeIcon" id="removeIcon"><wp:resourceURL/>administration/common/img/icons/list-remove.png</s:set>

<form action="<wp:action path="/ExtStr2/do/jpfastcontentedit/Content/save.action" />" method="post" enctype="multipart/form-data" class="newContentForm">

<div class="contentAttributeBox">

<!-- START - MAIN GROUP BLOCK -->
<p>
	<label for="mainGroup"><s:text name="label.ownerGroup" />:</label><br />
	<s:set name="lockGroupSelect" value="%{content.id != null || content.mainGroup != null}"></s:set>
	<wpsf:select useTabindexAutoIncrement="true" name="mainGroup" id="mainGroup" list="allowedGroups" value="content.mainGroup" 
		listKey="name" listValue="descr" disabled="%{lockGroupSelect}" cssClass="text" />
</p>
<!-- END - MAIN GROUP BLOCK -->

<!-- START - EXTRA GROUP BLOCK -->
<s:if test="content.groups.size != 0">
<ul>
<s:iterator value="content.groups" id="groupName">
	<li>
		<wpsa:actionParam action="removeGroup" var="actionName" >
			<wpsa:actionSubParam name="extraGroupName" value="%{#groupName}" />
		</wpsa:actionParam>
		<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image" src="%{#removeIcon}" value="%{getText('label.remove')}" title="%{getText('label.remove')}" />: <s:property value="%{getGroupsMap()[#groupName].getDescr()}"/> 
	</li>
</s:iterator>
</ul>
</s:if>
<p>
	<label for="extraGroups"><s:text name="label.join" />&#32;<s:text name="label.extraGroups" />:</label><br />
	<wpsf:select useTabindexAutoIncrement="true" name="extraGroupName" id="extraGroups" list="groups" 
		listKey="name" listValue="descr" cssClass="text" />
	<wpsf:submit useTabindexAutoIncrement="true" action="joinGroup" value="%{getText('label.join')}" cssClass="button" />
</p>
<!-- END - EXTRA GROUP BLOCK -->

</div>

<!-- START CICLO LINGUA -->
<s:set name="contentType" value="content.typeCode" />
<s:set name="lang" value="defaultLang" />

<s:iterator value="langs" id="lang">
<div id="<s:property value="#lang.code" />_tab" class="tab">
<h3><wp:i18n key="jpfastcontentedit_INTRO_${lang.code}"/></h3>

<!-- START CICLO ATTRIBUTI -->
<s:iterator value="content.attributeList" id="attribute">
<s:if test="#attribute.active">

<s:set var="attributeName" value="#attribute.name" />

<div class="contentAttributeBox">
<%-- INIZIALIZZAZIONE TRACCIATORE --%>
<wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" />

<s:if test="#attribute.type == 'List' || #attribute.type == 'Monolist'">
<p class="important">
	<wp:i18n key="jpfastcontentedit_${contentType}_${attributeName}"/><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" /><span class="monospace">&#32;(<wp:i18n key="jpfastcontentedit_LIST" />)</span>:
</p>
</s:if>
<s:elseif test="#attribute.type == 'Image' || #attribute.type == 'CheckBox' || #attribute.type == 'Boolean' || #attribute.type == 'ThreeState' || #attribute.type == 'Composite'">
<p>
	<span class="important"><wp:i18n key="jpfastcontentedit_${contentType}_${attributeName}"/><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />:</span>
	
</s:elseif>
<s:elseif test="#attribute.type == 'Monotext' || #attribute.type == 'Text' || #attribute.type == 'Longtext' || #attribute.type == 'Hypertext' || #attribute.type == 'Attach' || #attribute.type == 'Number' || #attribute.type == 'Date' || #attribute.type == 'Link' || #attribute.type == 'Enumerator'">
<p>
	<label for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />">
	<wp:i18n key="jpfastcontentedit_${contentType}_${attributeName}"/><s:include value="/WEB-INF/apsadmin/jsp/entity/modules/include/attributeInfo.jsp" />:</label><br />
</s:elseif>

<s:if test="#attribute.type == 'Monotext'">
<!-- ############# ATTRIBUTO TESTO MONOLINGUA ############# -->
<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/monotextAttribute.jsp" />
</p>
</s:if>

<s:elseif test="#attribute.type == 'Text'">
<!-- ############# ATTRIBUTO TESTO SEMPLICE MULTILINGUA ############# -->
<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Longtext'">
<!-- ############# ATTRIBUTO TESTOLUNGO ############# -->
<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/longtextAttribute.jsp" />
</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Hypertext'">
<!-- ############# ATTRIBUTO TESTOLUNGO ############# -->
<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/hypertextAttribute.jsp" />
</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Image'">
<!-- ############# ATTRIBUTO Image ############# -->
<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/imageAttribute.jsp" />
</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Attach'">
<!-- ############# ATTRIBUTO Attach ############# -->
<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/attachAttribute.jsp" />
</p>
</s:elseif>

<s:elseif test="#attribute.type == 'CheckBox'">
<!-- ############# ATTRIBUTO Composite ############# -->
<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/checkBoxAttribute.jsp" />
</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Boolean'">
<!-- ############# ATTRIBUTO Boolean ############# -->
<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/booleanAttribute.jsp" />
</p>
</s:elseif>

<s:elseif test="#attribute.type == 'ThreeState'">
<!-- ############# ATTRIBUTO ThreeState ############# -->
<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/threeStateAttribute.jsp" />
</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Number'">
<!-- ############# ATTRIBUTO Number ############# -->
<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/numberAttribute.jsp" />
</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Date'">
<!-- ############# ATTRIBUTO Date ############# -->
<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/dateAttribute.jsp" />
</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Link'">
<!-- ############# ATTRIBUTO Link ############# -->
<%-- 
<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/linkAttribute.jsp" />
 --%>
<s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/linkAttribute.jsp" />
</p>
</s:elseif>


<s:elseif test="#attribute.type == 'Enumerator'">
<!-- ############# ATTRIBUTO TESTO Enumerator ############# -->
<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorAttribute.jsp" />
</p>
</s:elseif>

<s:elseif test="#attribute.type == 'Monolist'">
<!-- ############# ATTRIBUTO Monolist ############# -->
<%--<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/entity/modules/monolistAttribute.jsp" /> --%>
<s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/monolistAttribute.jsp" />
</s:elseif>

<s:elseif test="#attribute.type == 'List'">
<!-- ############# ATTRIBUTO List ############# -->
<s:include value="/WEB-INF/apsadmin/jsp/entity/modules/listAttribute.jsp" />
</s:elseif>

<s:elseif test="#attribute.type == 'Composite'">
<!-- ############# ATTRIBUTO Composite ############# -->
<s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/entity/modules/compositeAttribute.jsp" />
</p>
</s:elseif>

</div>

</s:if>

</s:iterator>
<!-- END CICLO ATTRIBUTI -->
</div>
</s:iterator>
<!-- END CICLO LINGUA -->

	<p class="buttons">
	<s:set name="save_label"><wp:i18n key="jpfastcontentedit_SAVE" /></s:set>
	<wpsf:submit useTabindexAutoIncrement="true" action="save" value="%{#save_label}" title="%{#save_label}" cssClass="button"/>
	</p>
</form>