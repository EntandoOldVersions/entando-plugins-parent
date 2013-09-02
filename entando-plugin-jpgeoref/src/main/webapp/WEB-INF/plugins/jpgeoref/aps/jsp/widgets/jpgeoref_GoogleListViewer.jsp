<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="gwp" uri="/jpgeoref-aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<wp:headInfo type="JS_URL" info="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAPDUET0Qt7p2VcSk6JNU1sBRRwPhutbWBmyj82Go_H6JlE7EvFBSKFFFHFePAwvib9UM0geoA3Pgafw" />

<gwp:bodyReader var="htmlBodyTagAttribute">onunload="GUnload()"</gwp:bodyReader>

<jacms:contentList listName="contentList" titleVar="titleVar" 
	pageLinkVar="pageLinkVar" pageLinkDescriptionVar="pageLinkDescriptionVar" userFilterOptionsVar="userFilterOptionsVar" />

<c:if test="${null != titleVar}">
	<h2><span><c:out value="${titleVar}" /></span></h2>
</c:if>

<c:set var="userFilterOptionsVar" value="${userFilterOptionsVar}" scope="request" />
<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/js_content_viewer_list.jsp" />
<c:import url="/WEB-INF/plugins/jacms/aps/jsp/widgets/inc/userFilter-module.jsp" />

<div id="map" style="width: 550px; height: 450px"></div>
 
<script type="text/javascript">
//<![CDATA[

if (GBrowserIsCompatible()) { 
  
  // A function to create the marker and set up the event window
  // Dont try to unroll this function. It has to be here for the function closure
  // Each instance of the function preserves the contends of a different instance
  // of the "marker" and "html" variables which will be needed later when the event triggers.    
  function createMarker(point,html) {
    var marker = new GMarker(point);
    GEvent.addListener(marker, "click", function() {
      marker.openInfoWindowHtml(html);
    });
    return marker;
  }

<gwp:geoRenderList centerCoordsParamName="center" southWestCoordsParamName="southWest" northEastCoordsParamName="northEast" master="contentList" markerParamName="markers"/>

  // Display the map, with some controls and set the initial location 
  var map = new GMap2(document.getElementById("map"));
  map.addControl(new GLargeMapControl());
  map.addControl(new GMapTypeControl());
  
  var bounds = new GLatLngBounds(new GLatLng(<c:out value="${southWest[0]}"/>, <c:out value="${southWest[1]}"/>), 
  		new GLatLng(<c:out value="${northEast[0]}"/>, <c:out value="${northEast[1]}"/>));
  var zoomLevel = map.getBoundsZoomLevel(bounds);
  
  map.setCenter(new GLatLng(<c:out value="${center[0]}"/>, <c:out value="${center[1]}"/>), zoomLevel);
  
  <c:forEach var="geoInfoBean" items="${markers}">
  	var point = new GLatLng(<c:out value="${geoInfoBean.x}" />,<c:out value="${geoInfoBean.y}" />);
  	var marker = createMarker(point,'<jacms:content contentId="${geoInfoBean.contentId}" />');
  	map.addOverlay(marker);
  </c:forEach>
}

// display a warning if the browser was not compatible
else {
  alert("Sorry, the Google Maps API is not compatible with this browser");
}

// This Javascript is based on code provided by the
// Blackpool Community Church Javascript Team
// http://www.commchurch.freeserve.co.uk/   
// http://econym.googlepages.com/index.htm

//]]>
</script>

<c:if test="${null != pageLinkVar && null != pageLinkDescriptionVar}">
	<p><a href="<wp:url page="${pageLinkVar}"/>"><c:out value="${pageLinkDescriptionVar}" /></a></p>
</c:if>

<%-- Important: reset variables --%>
<c:set var="userFilterOptionsVar" value="${null}" scope="request" />
<c:set var="contentList" value="${null}"  scope="request" />
<c:set var="group" value="${null}"  scope="request" />