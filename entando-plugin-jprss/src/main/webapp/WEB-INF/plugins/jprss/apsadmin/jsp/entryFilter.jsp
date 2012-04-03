<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>

<h1><a href="<s:url action="list" namespace="/do/jprss/Rss" />" title="<s:text name="note.goToSomewhere" />: <s:text name="jprss.title.rssManagement" />"><s:text name="jprss.title.rssManagement" /></a></h1>
<div id="main">
	<s:if test="strutsAction ==  1">
		<h2><s:text name="jprss.title.addChannel" /></h2>
	</s:if>
	<s:if test="strutsAction ==  2">
		<h2><s:text name="jprss.title.editChannel" /></h2>
	</s:if>
	
	<h3 class="margin-bit-top"><s:text name="jprss.title.manageFilter" /></h3> 
	
	<s:form>
		<p class="noscreen">
			<wpsf:hidden name="id" />
			<wpsf:hidden name="title" />
			<wpsf:hidden name="description" />
			<wpsf:hidden name="contentType" />
			<wpsf:hidden name="feedType" />
			<wpsf:hidden name="category" />
			<wpsf:hidden name="maxContentsSize" />
			<wpsf:hidden name="filters" />
			<wpsf:hidden name="active" />
			<wpsf:hidden name="strutsAction" />
		</p>
		<s:if test="hasFieldErrors()">
			<div class="message message_error">
			<h4><s:text name="message.title.FieldErrors" /></h4>	
				<ul>
				<s:iterator value="fieldErrors">
					<s:iterator value="value">
					<li><s:property/></li>
					</s:iterator>
				</s:iterator>
				</ul>
			</div>
		</s:if>
		<s:if test="hasActionErrors()">
			<div class="message message_error">
			<h4><s:text name="message.title.ActionErrors" /></h4>	
				<ul>
				<s:iterator value="actionErrors">
					<li><s:property escape="false" /></li>
				</s:iterator>
				</ul>
			</div>
		</s:if>
		
		<s:if test="filterTypeId < 0">
			<fieldset class="margin-bit-top"><legend><s:text name="label.info" /></legend>
				<p>
					<label for="filterKey" class="basic-mint-label"><s:text name="label.type"/>:</label>
					<wpsf:select useTabindexAutoIncrement="true" name="filterKey" id="filterKey" list="filterTypes" listKey="key" listValue="value" cssClass="text" />
					<wpsf:submit useTabindexAutoIncrement="true" action="setFilterType" value="%{getText('label.continue')}" cssClass="button" />
				</p>
			</fieldset>
		</s:if>
		<s:else>
			
			<p class="noscreen">
				<wpsf:hidden name="filterKey" />
				<wpsf:hidden name="filterTypeId" />
				<wpsf:hidden name="attributeFilter" value="%{filterTypeId>0 && filterTypeId<5}"/>
			</p>
			
			<s:set name="filterDescription" value="%{filterKey}" />
			
			<s:if test="%{filterKey == 'created'}">
				<s:set name="filterDescription" value="%{getText('label.creationDate')}" />
			</s:if>
			
			<s:elseif test="%{filterKey == 'modified'}">
				<s:set name="filterDescription" value="%{getText('label.lastModifyDate')}" />			
			</s:elseif>
			
			<p><s:text name="note.filterTypes.intro" />: <span class="important"><s:property value="filterDescription" /></span><em> (
			
			<s:if test="filterTypeId == 0">
				<s:text name="note.filterTypes.metadata" /> )</em></p>
			</s:if>
			
			<s:elseif test="filterTypeId==1">
				<%-- INIZIO FILTRO PER ATTRIBUTO TIPO STRINGA --%>
				<s:text name="note.filterTypes.TextAttribute" /> )</em></p>
				<fieldset><legend><s:text name="label.settings"/></legend>
					<p>
						<label for="filterOptionId" class="basic-mint-label"><s:text name="label.option"/>:</label>
						<wpsf:select useTabindexAutoIncrement="true" id="filterOptionId" name="filterOptionId" headerKey="-1" headerValue="%{getText('label.none')}" list="#{1:getText('label.valueLikeOptionFilter'),2:getText('label.rangeOptionFilter')}" disabled="filterOptionId>-1" cssClass="text" />
						<s:if test="filterOptionId>-1"><wpsf:hidden name="filterOptionId" /></s:if>
						<s:else><wpsf:submit useTabindexAutoIncrement="true" action="setFilterOption" value="%{getText('label.confirm')}" cssClass="button" /></s:else>	
					</p>
					
					<s:if test="filterOptionId==1">
						<p>
							<label for="stringValue" class="basic-mint-label"><s:text name="label.filterValue" />:</label>
							<wpsf:textfield useTabindexAutoIncrement="true" name="stringValue" id="stringValue" cssClass="text" />
						</p>
						<p>
							<wpsf:checkbox useTabindexAutoIncrement="true" name="like" id="like" cssClass="radiocheck" /><label for="like"><s:text name="label.filterValue.isLike" /></label>
						</p>
					</s:if>
					
					<s:if test="filterOptionId==2">
						<p>
							<label for="stringStart" class="basic-mint-label"><s:text name="label.filterFrom" />:</label>
							<wpsf:textfield useTabindexAutoIncrement="true" name="stringStart" id="stringStart" cssClass="text" />
						</p>
						<p>
							<label for="stringEnd" class="basic-mint-label"><s:text name="label.filterTo" />:</label>
							<wpsf:textfield useTabindexAutoIncrement="true" name="stringEnd" id="stringEnd" cssClass="text" />
						</p>
					</s:if>
				</fieldset>
				<%-- FINE FILTRO PER ATTRIBUTO TIPO STRINGA --%>
			</s:elseif>
			
			<s:elseif test="filterTypeId==2">
			<%-- INIZIO FILTRO PER ATTRIBUTO TIPO NUMERO --%>
				<s:text name="note.filterTypes.NumberAttribute" /> )</em></p>
				<fieldset>
					<legend><s:text name="label.settings"/></legend>
					<p>
						<label for="filterOptionId" class="basic-mint-label"><s:text name="label.option"/>:</label>
						<wpsf:select useTabindexAutoIncrement="true" name="filterOptionId" id="filterOptionId" headerKey="-1" headerValue="%{getText('label.none')}" list="#{1:getText('label.valueOptionFilter'),2:getText('label.rangeOptionFilter')}" disabled="filterOptionId>-1" cssClass="text" />
						<s:if test="filterOptionId>-1"><wpsf:hidden name="filterOptionId" /></s:if>
						<s:else><wpsf:submit useTabindexAutoIncrement="true" action="setFilterOption" value="%{getText('label.confirm')}" cssClass="button" /></s:else>	
					</p>
					
					<s:if test="filterOptionId==1">
					<p>
						<label for="numberValue" class="basic-mint-label"><s:text name="label.filterValue.exact" />:</label>
						<wpsf:textfield useTabindexAutoIncrement="true" name="numberValue" id="numberValue" cssClass="text" />
					</p>
					</s:if>
				
					<s:if test="filterOptionId==2">
						<p>
							<label for="numberStart" class="basic-mint-label"><s:text name="label.filterFrom" />:</label>
							<wpsf:textfield useTabindexAutoIncrement="true" name="numberStart" id="numberStart" cssClass="text" />
						</p>
						<p>
							<label for="numberEnd" class="basic-mint-label"><s:text name="label.filterTo" />:</label>
							<wpsf:textfield useTabindexAutoIncrement="true" name="numberEnd" id="numberEnd" cssClass="text" />
						</p>
					</s:if>
				</fieldset>
			<%-- FINE FILTRO PER ATTRIBUTO TIPO NUMERO --%>
			</s:elseif>
			
			<s:elseif test="filterTypeId==3">
				<%-- INIZIO FILTRO PER ATTRIBUTO TIPO BULEANO --%>
				<s:text name="note.filterTypes.BooleanAttribute" /> )</em></p>
				<fieldset>
					<legend><s:text name="label.settings"/></legend>	
					<ul class="noBullet radiocheck">
						<li><input type="radio" name="booleanValue" id="booleanValue_true" value="true" /><label for="booleanValue_true"><s:text name="label.yes" /></label></li>
						<li><input type="radio" name="booleanValue" id="booleanValue_false" value="false" /><label for="booleanValue_false"><s:text name="label.no" /></label></li>
						<li><input type="radio" name="booleanValue" id="booleanValue_none" checked="checked" value="" /><label for="booleanValue_none"><s:text name="label.all" /></label></li>		
					</ul>
				</fieldset>
				<%-- FINE FILTRO PER ATTRIBUTO TIPO BULEANO --%>
			</s:elseif>
			
			<s:elseif test="filterTypeId==4">
				<%-- INIZIO FILTRO PER ATTRIBUTO TIPO DATA --%>
				<s:text name="note.filterTypes.DateAttribute" /> )</em></p>
				<fieldset>
					<legend><s:text name="label.settings"/></legend>
					<p>
						<label for="filterOptionId" class="basic-mint-label"><s:text name="label.option"/>:</label>
						<wpsf:select useTabindexAutoIncrement="true" name="filterOptionId" id="filterOptionId" headerKey="-1" headerValue="%{getText('label.none')}" list="#{1:getText('label.valueOptionFilter'),2:getText('label.rangeOptionFilter')}" disabled="filterOptionId>-1" cssClass="text" />
						<s:if test="filterOptionId>-1"><wpsf:hidden name="filterOptionId" /></s:if>
						<s:else><wpsf:submit useTabindexAutoIncrement="true" action="setFilterOption" value="%{getText('label.confirm')}" cssClass="button" /></s:else>	
					</p>
				
					<s:if test="filterOptionId==1">
						<ul class="noBullet radiocheck">
							<li><input type="radio" name="dateValueType" id="dateValueType_today" value="2" <s:if test="(2 == dateValueType)">checked="checked"</s:if> /> <label for="dateValueType_today"><s:text name="label.today" /></label></li>
							<li><input type="radio" name="dateValueType" id="dateValueType_chosen" value="3" <s:if test="(3 == dateValueType)">checked="checked"</s:if> /> <label for="dateValueType_chosen"><s:text name="label.chosenDate" /></label>,
							<label for="dateValue_cal"><s:text name="label.filterValue.exact" />:</label>&#32;<wpsf:textfield useTabindexAutoIncrement="true" name="dateValue" id="dateValue_cal" cssClass="text" /></li>
						</ul>
					</s:if>
				</fieldset>
				
				<s:if test="filterOptionId==2">
					<fieldset><legend><s:text name="label.filterFrom" /></legend>
						<ul class="noBullet radiocheck">
							<li><input type="radio" name="dateStartType" id="dateStartType_none" value="1" <s:if test="(1 == dateStartType)">checked="checked"</s:if> /> <label for="dateStartType_none"><s:text name="label.none" /></label></li>
							<li><input type="radio" name="dateStartType" id="dateStartType_today" value="2" <s:if test="(2 == dateStartType)">checked="checked"</s:if> /> <label for="dateStartType_today"><s:text name="label.today" /></label></li>
							<li><input type="radio" name="dateStartType" id="dateStartType_chosen" value="3" <s:if test="(3 == dateStartType)">checked="checked"</s:if> /> <label for="dateStartType_chosen"><s:text name="label.chosenDate" /></label>,
							<label for="dateStart_cal"><s:text name="label.filterValue.exact" />:</label>&#32;<wpsf:textfield useTabindexAutoIncrement="true" name="dateStart" id="dateStart_cal" cssClass="text" /></li>
						</ul>
					</fieldset>
					<fieldset>
						<legend><s:text name="label.filterTo" /></legend>
						<ul class="noBullet radiocheck">
							<li><input type="radio" name="dateEndType" id="dateEndType_none" value="1" <s:if test="(1 == dateEndType)">checked="checked"</s:if> /> <label for="dateEndType_none"><s:text name="label.none" /></label></li>
							<li><input type="radio" name="dateEndType" id="dateEndType_today" value="2" <s:if test="(2 == dateEndType)">checked="checked"</s:if> /> <label for="dateEndType_today"><s:text name="label.today" /></label></li>
							<li><input type="radio" name="dateEndType" id="dateEndType_chosen" value="3" <s:if test="(3 == dateEndType)">checked="checked"</s:if> /> <label for="dateEndType_chosen"><s:text name="label.chosenDate" /></label>, 
							<label for="dateEnd_cal"><s:text name="label.filterValue.exact" />:</label>&#32;<wpsf:textfield useTabindexAutoIncrement="true" name="dateEnd" id="dateEnd_cal" cssClass="text" /></li>
						</ul>
					</fieldset>
				</s:if>
				<%-- FINE FILTRO PER ATTRIBUTO TIPO DATA --%>
			</s:elseif>
			
			<fieldset>
				<legend><s:text name="label.order" /></legend>
				<ul class="noBullet radiocheck">
					<li><input type="radio" name="order" id="order_none" checked="checked" value="" /><label for="order_none"><s:text name="label.none" /></label></li>
					<li><input type="radio" name="order" id="order_asc" value="ASC" <s:if test="('ASC' == order)">checked="checked"</s:if> /><label for="order_asc"><s:text name="label.order.ascendant" /></label></li>
					<li><input type="radio" name="order" id="order_desc" value="DESC" <s:if test="('DESC' == order)">checked="checked"</s:if> /><label for="order_desc"><s:text name="label.order.descendant" /></label></li>
				</ul>
			</fieldset>
			
			<s:set name="saveFilterActionName"><s:if test="filterTypeId == 0">saveFilter</s:if><s:elseif test="filterTypeId == 1">saveTextFilter</s:elseif><s:elseif test="filterTypeId == 2">saveNumberFilter</s:elseif><s:elseif test="filterTypeId == 3">saveBooleanFilter</s:elseif><s:elseif test="filterTypeId == 4">saveDateFilter</s:elseif></s:set>
			<p class="centerText">
				<wpsf:submit useTabindexAutoIncrement="true" action="%{#saveFilterActionName}" value="%{getText('label.save')}" cssClass="button" />
			</p>
		</s:else>
	
	</s:form>
</div>