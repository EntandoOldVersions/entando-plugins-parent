<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>

<script type="text/javascript">
<!--
	var djConfig = {isDebug: false };
//-->
</script>
<script type="text/javascript" src="<wp:resourceURL />plugins/jpimagemap/administration/js/dojo/dojo.js"></script>

<script language="JavaScript" type="text/javascript">
<!--
	dojo.require("dojo.widget.*");
	dojo.require("dojo.widget.TaskBar");
	dojo.require("dojo.widget.LayoutContainer");	
	dojo.require("dojoapsadmin.widget.LinkedArea");
	dojo.require("dojo.widget.ResizeHandle");
	dojo.require("dojo.event.*");
    dojo.require("dojo.widget.Button");
//-->
</script>



<script language="JavaScript" type="text/javascript">
<!--
<s:iterator value="%{attribute.areas}" id="area" status="statusElement">
<s:if test="%{#statusElement.index != elementIndex}">
function notMove<s:property value="%{#statusElement.index+1}"/>(){
	<s:iterator value="%{#area.arrayCoords}" id="coord" status="areasStatus">
		<s:if test="%{#areasStatus.index==0}">
	dojo.byId('<s:property value="%{#statusElement.index}"/>').style.left=<s:property value="%{#coord}"/>;
		</s:if>
		<s:if test="%{#areasStatus.index==1}">
	dojo.byId('<s:property value="%{#statusElement.index}"/>').style.top=<s:property value="%{#coord}"/>; 
		</s:if>
	</s:iterator>
}
</s:if>
</s:iterator>

var itop;
var ileft;
var ibottom;
var iright;	


function move(){
	
	itop = dojo.byId('<s:property value="%{getElementIndex()}"/>').style.top.replace("px","");
	ileft = dojo.byId('<s:property value="%{getElementIndex()}"/>').style.left.replace("px","");
	dojo.byId('width').value = dojo.byId('<s:property value="%{getElementIndex()}"/>').style.width.replace("px","");
	dojo.byId('height').value = dojo.byId('<s:property value="%{getElementIndex()}"/>').style.height.replace("px","");
	ibottom = parseInt(dojo.byId('<s:property value="%{getElementIndex()}"/>').style.top.replace("px","")) + parseInt(dojo.byId('<s:property value="%{getElementIndex()}"/>').style.height.replace("px",""));
	iright = parseInt(dojo.byId('<s:property value="%{getElementIndex()}"/>').style.left.replace("px","")) + parseInt(dojo.byId('<s:property value="%{getElementIndex()}"/>').style.width.replace("px",""));
	dojo.byId('top').value = itop;
	dojo.byId('left').value = ileft;
	dojo.byId('bottom').value = ibottom;
	dojo.byId('right').value = iright;
	
}

function ctrl(){
	var controllo = false;
	var rectCurrent = {left: ileft , top:  itop , bottom: ibottom , right: iright };
	
	<s:iterator value="%{attribute.areas}" id="area" status="statusElement">
		<s:if test="%{#statusElement.index != elementIndex}">
	if (controllo == false ) {
		var rect<s:property value="%{#statusElement.index+1}"/> = {
		<s:iterator value="%{#area.arrayCoords}" id="coord" status="areasStatus">
			<s:if test="%{#areasStatus.index==0}">
			left: <s:property value="%{#coord}"/> , 
			</s:if>
			<s:if test="%{#areasStatus.index==1}">
			top: <s:property value="%{#coord}"/> , 
			</s:if>
			<s:if test="%{#areasStatus.index==2}">
			right: <s:property value="%{#coord}"/> , 
			</s:if>
			<s:if test="%{#areasStatus.index==3}">
			bottom: <s:property value="%{#coord}"/>
			</s:if>
		</s:iterator> } ;
		
		var resultV<s:property value="%{#statusElement.index+1}"/> = isIntersectVer(rectCurrent, rect<s:property value="%{#statusElement.index+1}"/>);	
		var resultH<s:property value="%{#statusElement.index+1}"/> = isIntersectHor(rectCurrent, rect<s:property value="%{#statusElement.index+1}"/>);				
	
		if ( resultH<s:property value="%{#statusElement.index+1}"/> == true && resultV<s:property value="%{#statusElement.index+1}"/> == true ){
			controllo = true; 
		} 	
	}
		</s:if>
	</s:iterator>
	
	if (controllo == true ) {
		dojo.byId('messages').value='<s:text name="label.overlap"/>';
		dojo.byId('pulsante').disabled="disabled";
	} else {
		dojo.byId('messages').value='';
		dojo.byId('pulsante').disabled=false;
	}				
}	

<!-- Inizio Funzioni Controllo sovraposizione -->
function isIntersectVer( arect, brect ){
	if ( brect.top < arect.bottom && brect.bottom > arect.top) {
		return true;
	}
	return false;
}

function isIntersectHor(arect, brect){
	if ( brect.left < arect.right && brect.right > arect.left) {
		return true;
	}
	return false;
}
<!-- Fine Funzioni Controllo sovrapposizione -->
	
function init() {
  	<!-- Current Area -->
  	dojo.event.connect( 'onmousemove', 'move');
  	<!-- Blocked Areas -->
  	<s:iterator value="%{attribute.areas}" id="area" status="statusElement">
		<s:if test="%{#statusElement.index != elementIndex}">
	dojo.event.connect( 'onmousemove', 'notMove<s:property value="%{statusElement.index+1}"/>');
		</s:if>
  	</s:iterator>
  	
	dojo.event.connect( 'onmousemove', 'ctrl');
}

dojo.addOnLoad(init);

//-->
</script>