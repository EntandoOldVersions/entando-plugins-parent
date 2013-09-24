<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ page contentType="charset=UTF-8" %>
<h1><s:text name="title.jpmyportalplus" /></h1>
<div id="main">
<s:form action="save">

	<s:if test="hasFieldErrors()">
	<div class="message message_error">
	<h2><s:text name="message.title.FieldErrors" /></h2>
	<ul>
		<s:iterator value="fieldErrors">
			<s:iterator value="value">
				<li><s:property escape="false" /></li>
			</s:iterator>
		</s:iterator>
	</ul>
	</div>
	</s:if>

	<s:if test="hasActionErrors()">
	<div class="message message_error">
	<h2><s:text name="message.title.ActionErrors" /></h2>
	<ul>
		<s:iterator value="actionErrors">
			<li><s:property escape="false" /></li>
		</s:iterator>
	</ul>
	</div>
	</s:if>

	<s:if test="hasActionMessages()">
	<div class="message message_confirm">
	<h2><s:text name="messages.confirm" /></h2>
	<ul>
		<s:iterator value="actionMessages">
			<li><s:property escape="false" /></li>
		</s:iterator>
	</ul>
	</div>
	</s:if>


	<s:set var="showletTypeCodes" value="showletTypeCodes" />
	<p class="noscreen">
	<s:iterator var="showletCode" value="showletTypeCodes" status="rowstatus">
		<wpsf:hidden id="showlets-%{#rowstatus.index}" name="showletTypeCodes" value="%{#showletCode}" />
	</s:iterator>
	</p>

	<fieldset  class="margin-bit-top">
		<legend><s:text name="jpmyportalplus.label.configWidget" /></legend>

		<p>
			<label for="showletCode" class="basic-mint-label"><s:text name="jpmyportalplus.label.addWidget" />:</label>
			<select name="showletCode" tabindex="<wpsa:counter />" id="showletCode">
			<s:iterator var="showletFlavour" value="showletFlavours">
				<wpsa:set var="tmpShowletType">tmpShowletTypeValue</wpsa:set>
				<s:iterator var="showletType" value="#showletFlavour" >
					<s:if test="#showletType.optgroup != #tmpShowletType">
						<s:if test="#showletType.optgroup == 'stockShowletCode'">
							<wpsa:set var="optgroupLabel"><s:text name="title.widgetManagement.widgets.stock" /></wpsa:set>
						</s:if>
						<s:elseif test="#showletType.optgroup == 'customShowletCode'">
							<wpsa:set var="optgroupLabel"><s:text name="title.widgetManagement.widgets.custom" /></wpsa:set>
						</s:elseif>
						<s:elseif test="#showletType.optgroup == 'userShowletCode'">
							<wpsa:set var="optgroupLabel"><s:text name="title.widgetManagement.widgets.user" /></wpsa:set>
						</s:elseif>
						<s:else>
							<wpsa:set var="pluginPropertyName" value="%{getText(#showletType.optgroup + '.name')}" />
							<wpsa:set var="pluginPropertyCode" value="%{getText(#showletType.optgroup + '.code')}" />
							<wpsa:set var="optgroupLabel"><s:text name="#pluginPropertyName" /></wpsa:set>
						</s:else>
					<optgroup label="<s:property value="#optgroupLabel" />">
					</s:if>
						<s:if test="!#showletTypeCodes.contains(#showletType.key)">
							<option value="<s:property value="#showletType.key" />">
							<s:property value="#showletType.value" /></option>
						</s:if>
					<wpsa:set var="tmpShowletType"><s:property value="#showletType.optgroup" /></wpsa:set>
				</s:iterator>
					</optgroup>
			</s:iterator>
			</select>
			<wpsf:submit action="addWidget" value="%{getText('label.add')}" cssClass="button" />
		</p>

		<s:if test="%{showletTypeCodes.size > 0}">
			<table class="generic">
				<caption><span><s:text name="jpmyportalplus.label.currentConfigWidget"></s:text></span></caption>
				<tr>
					<th scope="col">Showlet</th>
					<th scope="col"><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th>
				</tr>

				<s:iterator var="showletCode" value="#showletTypeCodes" status="rowstatus">
					<tr>
						<td>
							<wpsa:set var="showletType" value="%{getShowletType(#showletCode)}" />
							<s:property value="%{getTitle(#showletCode, #showletType.getTitles())}" />
						</td>
						<td class="icon">
							<wpsa:actionParam action="removeWidget" var="actionName" ><wpsa:actionSubParam name="showletCode" value="%{#showletCode}" /></wpsa:actionParam>
							<s:set name="iconImagePath" id="iconImagePath"><wp:resourceURL />administration/common/img/icons/delete.png</s:set>
							<wpsf:submit type="image" src="%{#iconImagePath}" action="%{#actionName}" value="%{getText('label.remove')}" title="%{getText('label.remove') + ': ' + getTitle(#showletCode, #showletType.getTitles())}" />
						</td>
					</tr>
				</s:iterator>

			</table>
		</s:if>
	</fieldset>

	<p class="centerText">
		<wpsf:submit value="%{getText('label.save')}" cssClass="button" />
	</p>
</s:form>
</div>
