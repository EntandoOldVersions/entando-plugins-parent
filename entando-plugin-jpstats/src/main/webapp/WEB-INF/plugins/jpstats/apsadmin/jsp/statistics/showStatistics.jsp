<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="cewolf" uri="http://cewolf.sourceforge.net/taglib/cewolf.tld" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<h1><s:text name="jpstats.header.statistics" /></h1>
<div id="main">
	<s:if test="hasFieldErrors()">
		<div class="message message_error">
			<h3><s:text name="message.title.FieldErrors" /></h3>	
			<ul>
				<s:iterator value="fieldErrors">
					<s:iterator value="value"><li><s:property/></li></s:iterator>
				</s:iterator>
			</ul>
		</div>
	</s:if>
	<s:if test="hasActionErrors()">
		<div class="message message_error">
			<h3><s:text name="message.title.ActionErrors" /></h3>	
			<ul>
				<s:iterator value="actionErrors"><li><s:property/></li></s:iterator>
			</ul>
		</div>
	</s:if>

	<s:form action="view">
		<fieldset class="margin-bit-top">
			<legend><s:text name="jpstats.graphic.options" /></legend>
			<p> 
				<label for="jpstats_start_cal" class="basic-mint-label"><s:text name="jpstats.statistics.from.label" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" id="jpstats_start_cal" name="start" maxlength="254" cssClass="text" />
			</p>
			<p>
				<label for="jpstats_end" class="basic-mint-label"><s:text name="jpstats.statistics.to.label" />:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" id="jpstats_end_cal" name="end" maxlength="254" cssClass="text" />
			</p>
			<p>
				<label for="jpstats_graphicType" class="basic-mint-label"><s:text name="statistics.graphicType.label" />:</label>
				<select name="selectedTypes" id="jpstats_graphicType_cal" tabindex="<wpsa:counter />">
					<s:iterator value="graphicType" id="entry">
						<%-- 
						<option value="<s:property value="%{#entry.key}"/>"><s:property name="%{#entry.value}"/></option>
						--%>
						<option value="<s:property value="%{#entry.key}"/>"><s:text name="%{#entry.key}"/></option>
					</s:iterator>
				</select>
				&#32;
			</p>
		</fieldset>
		<p class="centerText">
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('jpstats.show.detail')}" cssClass="button" />
			&#32;
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('jpstats.statistics.report')}" action="buildReport" cssClass="button" />
			&#32;
			<wpsf:submit useTabindexAutoIncrement="true" value="%{getText('jpstats.statistics.deleteStats')}" action="trash" cssClass="button" />
		</p>
	</s:form>
</div>

<%--
<div>
<s:set name="hitsProducer" value="hitsTimeData"></s:set>
<s:if test="#hitsProducer != null">
	<cewolf:chart id="hitsChart" type="verticalXYBar" xaxislabel="Intervallo" yaxislabel="Hits" showlegend="false">
		<cewolf:colorpaint color="#FFFFFF" />
		<cewolf:data>
			<cewolf:producer id="hitsProducer" />
		</cewolf:data>
	</cewolf:chart>
	<cewolf:img chartid="hitsChart" height="200" width="400" renderer="cewolf" mime="image/png" />
</s:if>
<s:else>
<div style="height: 200px;width:400px;"></div>
</s:else>
</div>
--%>

<%--
<div>
<s:set name="topPagesProducer" value="mostVisitedPagestimeData"></s:set>
<s:if test="#topPagesProducer != null">
	<cewolf:chart id="topPagesChart" type="horizontalBar"
			xaxislabel="pagecode" yaxislabel="Number of Visits"
			showlegend="false">
		<cewolf:colorpaint color="#FFFFFF" />
		<cewolf:data>
			<cewolf:producer id="topPagesProducer" />
		</cewolf:data>
	</cewolf:chart>
	<cewolf:img chartid="topPagesChart" height="200" width="700" renderer="cewolf" mime="image/png" />
</s:if>
<s:else>
<div style="height: 200px;width:400px;"></div>
</s:else>
</div>
--%>

<%--
<div>
<s:set name="topContentsProducer" value="topContentsDataset"></s:set>
<s:if test="#topContentsProducer != null">
	<cewolf:chart id="topContentsChart" type="horizontalBar"
		xaxislabel="content" yaxislabel="hits"
		showlegend="false">
		<cewolf:colorpaint color="#FFFFFF" />
		<cewolf:data>
			<cewolf:producer id="topContentsProducer" />
		</cewolf:data>
	</cewolf:chart>
	<cewolf:img chartid="topContentsChart" height="200" width="700" renderer="cewolf" mime="image/png" />
</s:if>
<s:else>
<div style="height: 200px;width:400px;"></div>
</s:else>
</div>
--%>

<%--
<dl class="table-display">
	<dt><s:text name="statistics.AverageTimeSite.label" /></dt>
		<dd><s:property value="averageTimeSite"/></dd>
	<dt><s:text name="statistics.AverageTimePage.label" /></dt>
		<dd><s:property value="averageTimePage"/></dd>
	<dt><s:text name="statistics.NumPageSession.label" /></dt>
		<dd><s:property value="numPageSession"/></dd>
	<dt><s:text name="statistics.ip.label" /></dt>
		<dd><s:property value="ipByDateInterval"/></dd>
</dl>
--%>