<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core"  %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<link rel="stylesheet" type="text/css" href="<wp:resourceURL />plugins/jpgeoref/administration/common/css/jpgeoref-administration.css" media="screen" />
<script type="text/javascript">
window.addEvent("domready", function(){
	window.jpgeoref = window.jpgeoref || {};
	window.jpgeoref.loadGMaps = function() {
		var callback = 'jpgeoref_create_map_<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />';
		var script = document.createElement('script');
		script.type = 'text/javascript';
		script.src = 'http://maps.googleapis.com/maps/api/js?sensor=false&callback='+ 'jpgeoref_init_maps' ;
		document.body.appendChild(script);
	};
	window.jpgeoref_init_maps = function() {
		<s:iterator value="content.attributeList" var="attribute">
			<s:if test="#attribute.type == 'Coords'">
			(function(){
				var mapcontainerHtmlId  = "mapcontainer_<s:property value="#attribute.name" />";
				var center = [<s:property value="#attribute.x" />||39.22453601575102, <s:property value="#attribute.y" />||9.096148437500005 ];
				var ourMap = new google.maps.Map(document.getElementById(mapcontainerHtmlId), {
					zoom : 5,
					center : new google.maps.LatLng(center[0], center[1]),
					streetViewControl: false,
					mapTypeId : google.maps.MapTypeId.ROADMAP
				});
				var marker = new google.maps.Marker({
					map: ourMap,
					draggable:true,
					position: new google.maps.LatLng(center[0],center[1]),
				});
				google.maps.event.addListener(marker, "dragend", function(ev)  {
					document.id("x_<s:property value="#attribute.name" />").set("value",ev.latLng.lat());
					document.id("y_<s:property value="#attribute.name" />").set("value",ev.latLng.lng());
				});
				var updateMarker = function() {
					var pos = new google.maps.LatLng(
							document.id("x_<s:property value="#attribute.name" />").get("value"),
							document.id("y_<s:property value="#attribute.name" />").get("value")
					);
					this[0].setPosition(pos);
					this[1].setCenter(pos);
				}.bind([marker,ourMap]);

				document.id("x_<s:property value="#attribute.name" />").addEvents({
					"change": updateMarker,
					"keyup": updateMarker
				});
				document.id("y_<s:property value="#attribute.name" />").addEvents({
					"change": updateMarker,
					"keyup": updateMarker
				});
			})();
			</s:if>
		</s:iterator>
	};
	window.jpgeoref.loadGMaps();
});
</script>
