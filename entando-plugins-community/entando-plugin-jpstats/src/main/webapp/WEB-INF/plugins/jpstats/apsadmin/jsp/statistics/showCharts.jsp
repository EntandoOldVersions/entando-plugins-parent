<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="cewolf" uri="http://cewolf.sourceforge.net/taglib/cewolf.tld" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<h1><a href="<s:url action="entryPoint" />" title="<s:text name="note.goToSomewhere" />: <s:text name="jpstats.header.statistics" />"><s:text name="jpstats.header.statistics" /></a></h1>

<div id="main">
	
	<h2><s:text name="jpstats.title.stats.detail" /></h2>
	
	<s:if test="hasFieldErrors()">
		<div class="message message_error">
		<h2><s:text name="message.title.FieldErrors" /></h2>	
			<ul>
			<s:iterator value="fieldErrors">
				<s:iterator value="value">
				<li><s:property/></li>
				</s:iterator>
			</s:iterator>
			</ul>
		</div>
	</s:if>

	<p>
		<s:text name="jpstats.message.intro.stats" />&#32;<s:date name="startDate" format="dd/MM/yyyy" />&#32;&mdash;&#32;<s:date name="endDate" format="dd/MM/yyyy" />. 
	</p>

	<%-- hits --%>
	<s:if test="selectedTypes.contains('hits')">
		<div class="subsection-light">
			<s:set name="hitsProducer" value="hitsTimeData"></s:set>
			<s:if test="#hitsProducer != null">
				<cewolf:chart id="hitsChart" type="verticalXYBar" xaxislabel="Intervallo" yaxislabel="Hits" showlegend="false">
					<cewolf:colorpaint color="#FFFFFF" />
					<cewolf:data>
						<cewolf:producer id="hitsProducer" />
					</cewolf:data>
				</cewolf:chart>
				<p><cewolf:img chartid="hitsChart" height="300" width="530" renderer="cewolf" mime="image/png" /></p>
			</s:if>
			<s:else>
				<div style="height: 200px;width:400px;"></div>
			</s:else>
		</div>
	</s:if>

	<%-- top pages --%>
	<s:if test="selectedTypes.contains('topPages')">
		<div class="subsection-light">
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
				<p><cewolf:img chartid="topPagesChart" height="300" width="530" renderer="cewolf" mime="image/png" /></p>
			</s:if>
			<s:else>
				<div style="height: 200px;width:400px;"></div>
			</s:else>
		</div>
	</s:if>

	<%-- topContents --%>
	<s:if test="selectedTypes.contains('topContents')">
		<div class="subsection-light">
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
			<p><cewolf:img chartid="topContentsChart" height="300" width="530" renderer="cewolf" mime="image/png" /></p>
		</s:if>
		<s:else>
		<div style="height: 200px;width:400px;"></div>
		</s:else>
		</div>
	</s:if>

	<%-- averages --%>
	<s:if test="selectedTypes.contains('averages')">
		<div class="subsection-light">
			<dl class="table-display">
				<dt><s:text name="jpstats.statistics.AverageTimeSite.label" /></dt>
					<dd><s:property value="averageTimeSite"/></dd>
				<dt><s:text name="jpstats.statistics.AverageTimePage.label" /></dt>
					<dd><s:property value="averageTimePage"/></dd>
				<dt><s:text name="jpstats.statistics.NumPageSession.label" /></dt>
					<dd><s:property value="numPageSession"/></dd>
				<dt><s:text name="jpstats.statistics.ip.label" /></dt>
					<dd><s:property value="ipByDateInterval"/></dd>
			</dl>
		</div>
	</s:if>
</div>

