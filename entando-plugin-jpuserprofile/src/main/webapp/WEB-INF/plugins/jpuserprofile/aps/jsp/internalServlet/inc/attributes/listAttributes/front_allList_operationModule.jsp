<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%-- 
jpuserprofile_ITEM_MOVEUP = Sosta su
jpuserprofile_ITEM_MOVEUP_IN = Sposta su in posizione:
jpuserprofile_ITEM_MOVEDOWN = Sosta giu
jpuserprofile_ITEM_MOVEDOWN_IN = Sposta giu in posizione
jpuserprofile_ITEM_REMOVE = Rimuovi dalla lista
--%>

<s:set var="label_moveup"><wp:i18n key="jpuserprofile_ITEM_MOVEUP" /></s:set>
<s:set var="label_moveup_in_number"><wp:i18n key="jpuserprofile_ITEM_MOVEUP_IN" /></s:set>
<s:set var="label_movedown"><wp:i18n key="jpuserprofile_ITEM_MOVEDOWN" /></s:set>
<s:set var="label_movedown_in_number"><wp:i18n key="jpuserprofile_ITEM_MOVEDOWN_IN" /></s:set>
<s:set var="label_remove"><wp:i18n key="jpuserprofile_ITEM_REMOVE" /></s:set>   

<wpsa:actionParam action="moveListElement" var="actionName" >
	<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
	<wpsa:actionSubParam name="listLangCode" value="%{#lang.code}" />
	<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
	<wpsa:actionSubParam name="movement" value="UP" />
</wpsa:actionParam>
<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/img/icons/go-up.png</s:set>
<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image" src="%{#iconImagePath}" 
	value="%{label_moveup}" 
	title="%{label_moveup_in_number}: %{#elementIndex}" />

<wpsa:actionParam action="moveListElement" var="actionName" >
	<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
	<wpsa:actionSubParam name="listLangCode" value="%{#lang.code}" />
	<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
	<wpsa:actionSubParam name="movement" value="DOWN" />
</wpsa:actionParam>
<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/img/icons/go-down.png</s:set>
<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image" src="%{#iconImagePath}" 
	value="%{label_movedown}" 
	title="%{label_movedown_in_number}: %{#elementIndex+2}" />

<wpsa:actionParam action="removeListElement" var="actionName" >
	<wpsa:actionSubParam name="attributeName" value="%{#attribute.name}" />
	<wpsa:actionSubParam name="listLangCode" value="%{#lang.code}" />
	<wpsa:actionSubParam name="elementIndex" value="%{#elementIndex}" />
</wpsa:actionParam>
<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL/>administration/img/icons/list-remove.png</s:set>
<wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" type="image" src="%{#iconImagePath}" 
	value="%{label_remove}" 
	title="%{label_remove}" />  