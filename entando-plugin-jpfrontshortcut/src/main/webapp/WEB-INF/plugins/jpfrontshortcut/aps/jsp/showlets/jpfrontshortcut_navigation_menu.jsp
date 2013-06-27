<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%--
	A 8-year-long effort, lovely brought to you by:

	- Marco Diana <m.diana@entando.com>
	- Eugenio Santoboni <e.santoboni@entando.com>
	- William Ghelfi <w.ghelfi@@entando.com>
	- Andrea Dessì <a.dessi@agiletec.it>
--%>

<wp:currentPage param="code" var="currentPageCode" />
<c:set var="currentPageCode" value="${currentPageCode}" />

<div class="well well-small">

<ul class="nav nav-list">
<wp:nav var="page">

<c:if test="${previousPage.code != null}">
	<c:set var="previousLevel" value="${previousPage.level}" />
	<c:set var="level" value="${page.level}" />
	<%@ include file="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/showlets/jpfrontshortcut_navigation_menu_include.jsp" %>
</c:if>

	<c:set var="previousPage" value="${page}" />
</wp:nav>

	<c:set var="previousLevel" value="${previousPage.level}" />
	<c:set var="level" value="${0}"  scope="request" /> <%-- we are out, level is 0 --%>
	<%@ include file="/WEB-INF/plugins/jpfrontshortcut/aps/jsp/showlets/jpfrontshortcut_navigation_menu_include.jsp" %>
	<c:if test="${previousLevel != 0}">
		<c:forEach begin="${0}" end="${previousLevel -1}"></ul></li></c:forEach>
	</c:if>

</ul>

</div>


<!--
<div class="well well-small">
	<h3>test</h3>
	<ul class="nav nav-list">
		<li class="nav-header">	
			<a href="#">page1</a>
			<a href="#">page1</a>
		</li>
		<li class="btn-group">
		  <a class="btn dropdown-toggle btn-small" data-toggle="dropdown" href="#">
		    Action
		    <span class="caret"></span>
		  </a>
		  <ul class="dropdown-menu">
			   <li><button class="btn btn-small" id="options_anchor_NEW_PAGE_pippo_1_924429" href="javascript:void(0)" title="Create child page of: Pippo En"><img width="12" height="12" src="/frontshortcut/resources/plugins/jpfrontshortcut/static/img/icons/16x16/new.png" alt="Create child page of: Pippo En"></button></li>
			   <li><button class="btn btn-small" id="options_anchor_NEW_PAGE_pippo_1_924429" href="javascript:void(0)" title="Create child page of: Pippo En"><img width="12" height="12" src="/frontshortcut/resources/plugins/jpfrontshortcut/static/img/icons/16x16/new.png" alt="Create child page of: Pippo En"></button></li>
		  </ul>
		</li>
		<li class="active">
			<a href="#">page2</a>
			<a href="#">page1</a>
			
		</li>
		<li>
			<a href="#">page3</a>
			<a href="#">page1</a>
		</li>
	</ul>
</div>
 -->