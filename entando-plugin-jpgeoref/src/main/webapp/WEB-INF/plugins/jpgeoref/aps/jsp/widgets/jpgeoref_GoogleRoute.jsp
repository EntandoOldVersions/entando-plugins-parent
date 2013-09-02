<%@ taglib prefix="jacms" uri="/jacms-aps-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="gwp" uri="/jpgeoref-aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<wp:headInfo type="JS_URL" info="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAPDUET0Qt7p2VcSk6JNU1sBRRwPhutbWBmyj82Go_H6JlE7EvFBSKFFFHFePAwvib9UM0geoA3Pgafw" />

<gwp:bodyReader var="htmlBodyTagAttribute">onunload="GUnload()"</gwp:bodyReader>

<gwp:geoRoute listName="contentList" />

<wp:currentWidget param="config" configParam="listModelId" var="listModelId" />
<c:if test="${listModelId != null}">
<ol>
	<c:forEach var="contentId" items="${contentList}">
		<li><jacms:content contentId="${contentId}" modelId="${listModelId}" /></li>
	</c:forEach>
</ol>
</c:if>

    <div id="map" style="width: 550px; height: 450px"></div>
    <div id="path" style="width:550px"></div>
    
    <script type="text/javascript">
    //<![CDATA[
    if (GBrowserIsCompatible()) {

      var map = new GMap2(document.getElementById("map"));
      var dirn = new GDirections(map);


      GEvent.addListener(dirn,"error", function() {
        alert("Directions Failed: "+dirn.getStatus().code);
      });

<gwp:geoRenderList centerCoordsParamName="center" southWestCoordsParamName="southWest" northEastCoordsParamName="northEast" 
		master="contentList" markerParamName="markers"/>
<c:forEach varStatus="status" var="contentId" items="${markers}">
<wp:currentWidget param="config" configParam="markerModelId" var="markerModelId" />
<jacms:content contentId="${contentId}" modelId="${markerModelId}" />
var point<c:out value="${status.count}"/> = <jacms:content contentId="${contentId}" modelId="${markerModelId}" />
</c:forEach>

	dirn.loadFromWaypoints([<c:forEach varStatus="status" var="contentId" items="${markers}"><c:if test="${status.count!=1}"> , </c:if>point<c:out value="${status.count}"/>.lat() +","+point<c:out value="${status.count}"/>.lng()</c:forEach>], {getSteps:true}); 

      // ============ custom direction panel ===============
      function customPanel(map,mapname,dirn,div) {
        var html = "";
      
        // ===== local functions =====

        // === waypoint banner ===
        function waypoint(point, type, address) {
          var target = '"' + mapname+".showMapBlowup(new GLatLng("+point.toUrlValue(6)+"))"  +'"';
          html += '<table style="border: 1px solid silver; margin: 10px 0px; background-color: rgb(238, 238, 238); border-collapse: collapse; color: rgb(0, 0, 0);">';
          html += '  <tr style="cursor: pointer;" onclick='+target+'>';
          html += '    <td style="padding: 4px 15px 0px 5px; vertical-align: middle; width: 20px;">';
          html += '      <img src="http://www.google.com/intl/en_ALL/mapfiles/icon-dd-' +type+ '-trans.png">'
          html += '    </td>';
          html += '    <td style="vertical-align: middle; width: 100%;">';
          html +=        address;
          html += '    </td>';
          html += '  </tr>';
          html += '</table>';
        }

        // === route distance ===
        function routeDistance(dist) {
          html += '<div style="text-align: right; padding-bottom: 0.3em;">' + dist + '</div>';
        }      

        // === step detail ===
        function detail(point, num, description, dist) {
          var target = '"' + mapname+".showMapBlowup(new GLatLng("+point.toUrlValue(6)+"))"  +'"';
          html += '<table style="margin: 0px; padding: 0px; border-collapse: collapse;">';
          html += '  <tr style="cursor: pointer;" onclick='+target+'>';
          html += '    <td style="border-top: 1px solid rgb(205, 205, 205); margin: 0px; padding: 0.3em 3px; vertical-align: top; text-align: right;">';
          html += '      <a href="javascript:void(0)"> '+num+'. </a>';
          html += '    </td>';
          html += '    <td style="border-top: 1px solid rgb(205, 205, 205); margin: 0px; padding: 0.3em 3px; vertical-align: top; width: 100%;">';
          html +=        description;
          html += '    </td>';
          html += '    <td style="border-top: 1px solid rgb(205, 205, 205); margin: 0px; padding: 0.3em 3px 0.3em 0.5em; vertical-align: top; text-align: right;">';
          html +=        dist;
          html += '    </td>';
          html += '  </tr>';
          html += '</table>';
        }

        // === Copyright tag ===
        function copyright(text) {
          html += '<div style="font-size: 0.86em;">' + text + "</div>";
        }
        

        // === read through the GRoutes and GSteps ===

        for (var i=0; i<dirn.getNumRoutes(); i++) {
          if (i==0) {
            var type="play";
          } else {
            var type="pause";
          }
          var route = dirn.getRoute(i);
          var geocode = route.getStartGeocode();
          var point = route.getStep(0).getLatLng();
          // === Waypoint at the start of each GRoute
          waypoint(point, type, geocode.address);
          routeDistance(route.getDistance().html+" (about "+route.getDuration().html+")");

          for (var j=0; j<route.getNumSteps(); j++) {
            var step = route.getStep(j);
            // === detail lines for each step ===
            detail(step.getLatLng(), j+1, step.getDescriptionHtml(), step.getDistance().html);
          }
        }
        
        // === the final destination waypoint ===   
        var geocode = route.getEndGeocode();
        var point = route.getEndLatLng();
        waypoint(point, "stop", geocode.address);
                 
        // === the copyright text ===
        copyright(dirn.getCopyrightsHtml());

        // === drop the whole thing into the target div
        div.innerHTML = html;

      } // ============ end of customPanel function ===========


      // ========== launch the custom Panel creator a millisecond after the GDirections finishes loading ==========
      // == The delay is required in case we rely on GDirections to perform the initial setCenter ==
      GEvent.addListener(dirn,"load", function() {
        setTimeout('customPanel(map,"map",dirn,document.getElementById("path"))', 1);
      });

    }
    //]]>
    // This Javascript is based on code provided by the
    // Blackpool Community Church Javascript Team
    // http://www.commchurch.freeserve.co.uk/   
    // http://econym.googlepages.com/index.htm

    </script>