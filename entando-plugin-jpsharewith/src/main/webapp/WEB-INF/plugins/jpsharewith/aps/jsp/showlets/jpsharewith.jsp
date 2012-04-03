<%@ page contentType="charset=UTF-8" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<wp:contentNegotiation mimeType="application/xhtml+xml" charset="UTF-8"/>

<wp:headInfo type="CSS" info="../../plugins/jpsharewith/static/css/jpsharewith.css"/>
<c:set var="VAR_JS_VALUE">
var jpsharewith_imgURL='<wp:resourceURL />plugins/jpsharewith/static/img/';
</c:set>
<wp:headInfo type="JS_VARS" info="${VAR_JS_VALUE}" />

<wp:headInfo type="JS" info="../../plugins/jpsharewith/static/js/lib/mootools-1.2-core.js" />
<wp:headInfo type="JS" info="../../plugins/jpsharewith/static/js/lib/mootools-1.2-more.js" />

<wp:headInfo type="JS" info="../../plugins/jpsharewith/static/js/jpsharewith_buttons.js" />
<wp:headInfo type="JS" info="../../plugins/jpsharewith/static/js/jpsharewith.js" />

<div class="jpsharewith">
	<p><wp:i18n key="jpsharewith_TITLE" /></p>
	<p id="jpsharewith">
		<noscript><wp:i18n key="jpsharewith_NOSCRIPT" /></noscript>
	</p>
</div>