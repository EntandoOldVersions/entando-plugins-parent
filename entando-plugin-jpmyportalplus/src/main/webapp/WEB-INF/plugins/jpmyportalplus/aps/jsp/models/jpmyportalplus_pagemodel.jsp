<%-- IMPORTANT: remeber to modify the main.jsp inserting <jpmpp:execShowlet /> --%>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpmpp" uri="/jpmyportalplus-core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="<wp:info key="currentLang" />">
	<head>
		<title>jpmyportalplus - pagemodel</title>

		<%-- jpmyportal plus - static resources start --%>
			<script type="text/javascript" src="<wp:resourceURL />plugins/jpmyportalplus/static/js/lib/mootools-1.2-core.js"></script>
			<script type="text/javascript" src="<wp:resourceURL />plugins/jpmyportalplus/static/js/lib/mootools-1.2-more.js"></script>
			<script type="text/javascript" src="<wp:resourceURL />plugins/jpmyportalplus/static/js/lib/moo-jAPS-jpmyportalplus-sortable.js"></script>
			<script type="text/javascript" src="<wp:resourceURL />plugins/jpmyportalplus/static/js/lib/moo-jAPS-jpmyportalplus.js"></script>
			<jsp:include page="/WEB-INF/plugins/jpmyportalplus/aps/jsp/models/inc/jpmyportalplus_javascript_variables.jsp" />
			<script type="text/javascript" src="<wp:resourceURL />plugins/jpmyportalplus/static/js/jpmyportalplus.js"></script>
			<link rel="stylesheet" type="text/css" href="<wp:resourceURL />plugins/jpmyportalplus/static/css/jpmyportalplus.css" media="screen" />
		<%-- jpmyportal plus - static resources start --%>

	</head>
	<body>

		<%-- Header --%><wp:show frame="0" />

		<div id="widget-columns-container"><%//IMPORTANT: the html id "columns" is referenced in /resources/plugins/jpmyportalplus/static/js/jpmyportalplus.js %>
			<div id="widget-col1"><%//IMPORTANT: the html id "colonna1" is referenced in jpmyportalplus_javascript_variables.jsp %>
				<%-- draggable showlets for column="1" here.... --%>
				<%-- Left Column I --%> <wp:show frame="1" />
				<%-- Left Column II --%> <wp:show frame="2" />
			</div>
			<div id="widget-col2"><%//IMPORTANT: the html id "colonna2" is referenced in jpmyportalplus_javascript_variables.jsp %>
				<%-- draggable showlets for column="2" here.... --%>
				<%-- Middle Column I --%><wp:show frame="3" />
				<%-- Middle Column II --%><wp:show frame="4" />
			</div>
			<div id="widget-col3"><%//IMPORTANT: the html id "colonna3" is referenced in jpmyportalplus_javascript_variables.jsp %>
				<%-- draggable showlets for column="3" here.... --%>
				<%-- Right Column I --%><wp:show frame="5" />
				<%-- Right Column II --%><wp:show frame="6" />
			</div>
		</div>
		
		<div id="configure-page">
			<h2 id="editshowlet_title"><a href="#editshowletlist">configure</a></h2>		  		
		</div>
		
		<%-- Remember to include the Page Configuration Block --%>
		<jsp:include page="/WEB-INF/plugins/jpmyportalplus/aps/jsp/models/inc/page_configuration.jsp" />

		<%-- Footer --%><wp:show frame="7" />

	</body>
</html>