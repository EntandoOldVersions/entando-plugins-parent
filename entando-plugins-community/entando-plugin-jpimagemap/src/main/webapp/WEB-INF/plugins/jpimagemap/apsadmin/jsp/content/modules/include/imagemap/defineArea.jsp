<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<html>
<head>
<title>Select Area</title>

<s:include value="/WEB-INF/plugins/jpimagemap/apsadmin/jsp/content/modules/include/imagemap/defineAreaJS.jsp" />

<style type="text/css" >
	body {
		<s:if test="%{(attribute.resource!=null && attribute.resource.id!=0) || attribute.text!=''}">
			background-image: url('<s:property value="%{attribute.getResource().getImagePath('0')}"/>');
			background-repeat: no-repeat;
		</s:if>
	}
</style>

</head>

<body>

<s:if test="%{(attribute.resource != null && attribute.resource.id != 0 ) || attribute.text != ''}">
	<!-- ciclo sulle aree -->
	<s:if test="%{attribute.areas != null}">
		<!-- ciclo sulle aree -->
		<s:iterator value="%{attribute.areas}" id="area" status="statusElement">
			<s:if test="%{#area!=null}">
				<s:if test="%{#statusElement.index == elementIndex}" >
					
<div dojoType="LinkedArea" id="<s:property value="%{#statusElement.index}"/>" 
		title="area <s:property value="%{#statusElement.index+1}"/> - <s:text name="Content.linkedAreaElement.inDefinition"/>"
		constrainToContainer="true" 
		hasShadow="false" 
		resizable="true"
		taskBarId="mytaskbar" 
		windowState="normal"
		displayMinimizeAction="false" 
		toggle="explode"
		toggleDuration="300"
		style="position: absolute;  display: visible; opacity: 0.8;	overflow: hidden;
	<s:if test="%{#area.coords=='0,0,0,0'}">
			top: 10px;
			left: 10px;
			width: 100px;
			height: 45px;
	</s:if>
	<s:else>
		<s:iterator value="%{#area.arrayCoords}" id="coord" status="areasStatus">
			<s:if test="%{#areasStatus.index==0}">
				left: <s:property value="%{#coord}"/>px; 
				<s:set name="left" value="%{#coord}"/>	
			</s:if>
			<s:elseif test="%{#areasStatus.index==1}">
				top: <s:property value="%{#coord}"/>px; 
				<s:set name="top" value="%{#coord}"/>
			</s:elseif>
			<s:elseif test="%{#areasStatus.index==2}">
				width: <s:property value="%{#coord - #left}"/>px;
			</s:elseif>
			<s:elseif test="%{#areasStatus.index==3}">
				height: <s:property value="%{#coord - #top}"/>px; 
			</s:elseif>
		</s:iterator>
	</s:else>" >
	<div style="text-align: center; position: relative; margin: auto;" >
		<s:text name="Content.linkedAreaElement.inDefinition"/>
	</div>
</div>
				</s:if>
				<s:else>
<div dojoType="LinkedArea"
	id="<s:property value="%{#statusElement.index}"/>" 
	title="area <s:property value="%{#statusElement.index+1}"/> - <s:text name="Content.linkedAreaElement.Defined"/> "
	constrainToContainer="true" 
	hasShadow="false" 
	resizable="false"
	taskBarId="mytaskbar" 
	windowState="normal"
	displayMinimizeAction="false" 
	toggle="explode"
	toggleDuration="300"
	style="position: absolute;  display: visible; opacity: 0.8; 																	
		background-color: blue; border-color: blue; overflow: hidden;
	<s:if  test="%{#area.coords == '0,0,0,0'}" >
		top: 10px;
		left: 10px;
		width: 100px;
		height: 45px;
	</s:if>
	<s:else>
		<s:iterator value="%{#area.arrayCoords}" id="coord" status="areasStatus">
			<s:if test="%{#areasStatus.index==0}">
		left: <s:property value="%{#coord}"/>px; 
		<s:set name="left" value="%{#coord}"/>
			</s:if>
			<s:elseif test="%{#areasStatus.index==1}">
		top: <s:property value="%{#coord}"/>px; 
		<s:set name="top" value="%{#coord}"/>
			</s:elseif>
			<s:elseif test="%{#areasStatus.index==2}">
		width: <s:property value="%{#coord - #left}"/>px;
			</s:elseif>
			<s:elseif test="%{#areasStatus.index==3}">
		height: <s:property value="%{#coord - #top}"/>px; 
			</s:elseif>
		</s:iterator>
	</s:else> " >
	<div style="text-align: center; position: relative; margin: auto;" >
		<s:text name="Content.linkedAreaElement.Defined"/>
	</div>
</div>
				</s:else>
				
				
			</s:if>
		</s:iterator>
		<!-- fine ciclo sulle aree -->
		&nbsp;
	</s:if>
	<s:else>
	<div>&nbsp;</div>
	</s:else>
</s:if>
<s:else>
<div>&nbsp;</div>
</s:else>

<div dojoType="TaskBar" 
	id="mytaskbar" 
	hasShadow="true"
	resizable="false"
	style="width: 80%; height: 60px; margin: 0px; padding: 0px; bottom: 30px; left: 5%; overflow: hidden; display: none;">
</div>

<div style="position: absolute; bottom: 10px; padding: 10px;" >
<div>
	<input style="color: red;" type="text" size="28" name="messages" id="messages" ><br/>
</div>

<h4>Area: <s:property value="%{elementIndex+1}"/></h4>
<s:form action="saveArea" id="mainform" >
<div style="float: left; text-align: right; ">
	
		Top: <input type="text" size="4" name="top" id="top" /><br/>
		Left: <input type="text" size="4" name="left" id="left" /><br/>
		Width: <input type="text" size="4" name="width" id="width" /><br/>
		Height: <input type="text" size="4" name="height" id="height" /><br/>
		Bottom: <input type="text" size="4" name="bottom" id="bottom" /><br/>
		Right: <input type="text" size="4" name="right" id="right" /><br/>
		<wpsf:hidden name="attributeName"/>
		<wpsf:hidden name="elementIndex" />
		<wpsf:hidden name="langCode"/>
</div>
<wpsa:actionParam action="saveArea" var="actionName" />
<s:property value="#actionName"/>
<s:action name="saveArea" />
<div style="margin: 20px; padding: 0px; bottom: 30px; overflow: hidden;  ">
	<input class="button" id="pulsante" type="submit" value="<s:text name="label.save"/>"  />
</div>
	</s:form>
</div>
</body>
</html>