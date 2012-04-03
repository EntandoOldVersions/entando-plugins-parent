<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ page contentType="charset=UTF-8" %>

<h1><s:text name="title.myportalManagement" /></h1> 
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
		
		<fieldset>
			<legend><s:text name="label.configShowlet" /></legend>
			
			<p> 
				<label for="showletCode" class="basic-mint-label"><s:text name="label.addShowlet" />:</label>
				<select name="showletCode" tabindex="<wpsa:counter />" id="showletCode">
				<s:iterator var="showletFlavour" value="showletFlavours">
					
					<wpsa:set var="tmpShowletType">tmpShowletTypeValue</wpsa:set>
					
					<s:iterator var="showletType" value="#showletFlavour" >
								
						<s:if test="#showletType.optgroup != #tmpShowletType">
						
							<s:if test="#showletType.optgroup == 'stockShowletCode'">
								<wpsa:set var="optgroupLabel"><s:text name="title.showletManagement.showlets.stock" /></wpsa:set>
							</s:if>
							<s:elseif test="#showletType.optgroup == 'customShowletCode'">
								<wpsa:set var="optgroupLabel"><s:text name="title.showletManagement.showlets.custom" /></wpsa:set>
							</s:elseif>
							<s:elseif test="#showletType.optgroup == 'userShowletCode'">
								<wpsa:set var="optgroupLabel"><s:text name="title.showletManagement.showlets.user" /></wpsa:set>
							</s:elseif>
							<s:else>
								<wpsa:set var="pluginPropertyName" value="%{getText(#showletType.optgroup + '.name')}" />		
								<wpsa:set var="pluginPropertyCode" value="%{getText(#showletType.optgroup + '.code')}" />					
								<wpsa:set var="optgroupLabel">(<s:text name="#pluginPropertyCode" />) <s:text name="#pluginPropertyName" /></wpsa:set>
							</s:else>
									
						<optgroup label="<s:property value="#optgroupLabel" />">
						</s:if>
							<option value="<s:property value="#showletType.key" />"><s:property value="#showletType.value" /></option>
					
						<wpsa:set var="tmpShowletType"><s:property value="#showletType.optgroup" /></wpsa:set>
					
					</s:iterator>
						</optgroup>	
				</s:iterator>
				</select>
				&#32; 
				<wpsf:submit useTabindexAutoIncrement="true" action="addShowlet" value="%{getText('label.add')}" cssClass="button" />
			</p>
			
			<s:if test="%{showlets.size > 0}">
				<table class="generic">
					<caption><span><s:text name="label.currentConfigShowlet" /></span></caption> 
					<tr> 
						<th scope="col">Showlet</th>
						<th scope="col"><abbr title="<s:text name="label.remove" />">&ndash;</abbr></th> 
					</tr>
	
					<s:iterator var="showletCode" value="showlets" status="rowstatus">
						<tr>
							<td>
								<wpsa:set var="showletType" value="%{getShowletType(#showletCode)}" />
								<s:property value="%{getTitle(#showletCode, #showletType.getTitles())}" />
							</td>
							<td class="icon">
								<wpsa:actionParam action="removeShowlet" var="actionName" ><wpsa:actionSubParam name="showletCode" value="%{#showletCode}" /></wpsa:actionParam>
								<s:set var="iconImagePath"><wp:resourceURL />administration/common/img/icons/delete.png</s:set>
								<wpsf:submit useTabindexAutoIncrement="true" type="image" src="%{#iconImagePath}" action="%{#actionName}" value="%{getText('label.remove')}" title="%{getText('label.remove') + ': ' + getTitle(#showletCode, #showletType.getTitles())}" />
							</td>
						</tr>
					</s:iterator>	
						
				</table>
			</s:if>
			<s:else>
				<p><s:text name="note.emptyShowletList" /></p>
			</s:else>
		</fieldset>
		
		<fieldset>
			<legend><s:text name="label.options" /></legend>
			
			<p>
				<s:iterator var="showletCode" value="showlets" status="rowstatus">
					<wpsf:hidden id="showlets-%{#rowstatus.index}" name="showlets" value="%{#showletCode}" />
				</s:iterator>
				<wpsf:checkbox useTabindexAutoIncrement="true" name="ajaxEnabled" id="jpmyportal_ajax_enabled" cssClass="radiocheck" />&#32;
				<label for="jpmyportal_ajax_enabled"><s:text name="label.ajaxEnabled" /></label>
			</p>
			
		</fieldset>
		
		<p class="centerText">
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('label.save')}" cssClass="button" />
		</p>
	</s:form>
</div>