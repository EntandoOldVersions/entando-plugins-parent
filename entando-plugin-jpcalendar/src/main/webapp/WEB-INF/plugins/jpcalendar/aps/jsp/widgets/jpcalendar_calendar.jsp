<%@ page contentType="application/xhtml+xml; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/jpcalendar-aps-core" prefix="cal" %>
<cal:calendar nomeGruppo="jpcalendarCal" datePattern="yyyyMMdd" />

<jsp:useBean id="now" class="java.util.Date" /> 
<c:set var="today"><fmt:formatDate value="${now}" pattern="yyyyMMdd" /></c:set>

<form id="calendarForm" action="<wp:url />" method="post">
<p>
<a href="<wp:url><wp:parameter name="month" ><c:out value="${prevMonth}"/></wp:parameter><wp:parameter name="year" ><c:out value="${prevYear}"/></wp:parameter></wp:url>"><wp:i18n key="jpcalendar_MONTH_PREVIOUS" /></a>
	<label for="month"><wp:i18n key="jpcalendar_MONTH_CHOOSE" /></label>
	<select id="month" name="month"> <!-- e questi sono statici -->
		<option <c:if test="${selectedMonth == '0'}">selected="selected"</c:if> value="0"><wp:i18n key="jpcalendar_MONTH_JANUARY" /></option>
		<option <c:if test="${selectedMonth == '1'}">selected="selected"</c:if> value="1"><wp:i18n key="jpcalendar_MONTH_FEBRUARY" /></option>
		<option <c:if test="${selectedMonth == '2'}">selected="selected"</c:if> value="2"><wp:i18n key="jpcalendar_MONTH_MARCH" /></option>
		<option <c:if test="${selectedMonth == '3'}">selected="selected"</c:if> value="3"><wp:i18n key="jpcalendar_MONTH_APRIL" /></option>
		<option <c:if test="${selectedMonth == '4'}">selected="selected"</c:if> value="4"><wp:i18n key="jpcalendar_MONTH_MAY" /></option>
		<option <c:if test="${selectedMonth == '5'}">selected="selected"</c:if> value="5"><wp:i18n key="jpcalendar_MONTH_JUNE" /></option>
		<option <c:if test="${selectedMonth == '6'}">selected="selected"</c:if> value="6"><wp:i18n key="jpcalendar_MONTH_JULY" /></option>
		<option <c:if test="${selectedMonth == '7'}">selected="selected"</c:if> value="7"><wp:i18n key="jpcalendar_MONTH_AUGUST" /></option>
		<option <c:if test="${selectedMonth == '8'}">selected="selected"</c:if> value="8"><wp:i18n key="jpcalendar_MONTH_SEPTEMBER" /></option>
		<option <c:if test="${selectedMonth == '9'}">selected="selected"</c:if> value="9"><wp:i18n key="jpcalendar_MONTH_OCTOBER" /></option>
		<option <c:if test="${selectedMonth == '10'}">selected="selected"</c:if> value="10"><wp:i18n key="jpcalendar_MONTH_NOVEMBER" /></option>
		<option <c:if test="${selectedMonth == '11'}">selected="selected"</c:if> value="11"><wp:i18n key="jpcalendar_MONTH_DECEMBER" /></option>
	</select>
	<label for="year"><wp:i18n key="jpcalendar_YEAR_CHOOSE" /></label>	
	<select id="year" name="year">
		<c:forEach var="yearOpt" items="${yearsForSelect}" >
			<option <c:if test="${selectedYear == yearOpt}">selected="selected"</c:if> value="<c:out value="${yearOpt}" />"><c:out value="${yearOpt}" /></option>
		</c:forEach>
	</select>
<a href="<wp:url><wp:parameter name="month" ><c:out value="${nextMonth}"/></wp:parameter><wp:parameter name="year" ><c:out value="${nextYear}"/></wp:parameter></wp:url>"><wp:i18n key="jpcalendar_MONTH_NEXT" /></a>	
</p>
<p>
	<input type="submit" value="<wp:i18n key="jpcalendar_SEARCH_GO" />" />
</p>
</form>

<table cellpadding="0" cellspacing="0" summary="<wp:i18n key="jpcalendar_SUMMARY" />">
<caption><wp:i18n key="jpcalendar_CAPTION" /></caption>
	<tr>
		<th scope="col"><span><wp:i18n key="jpcalendar_WEEK_NUMBER" /></span></th>
		<th scope="col"><span><wp:i18n key="jpcalendar_WEEK_MONDAY" /></span></th>
		<th scope="col"><span><wp:i18n key="jpcalendar_WEEK_TUESDAY" /></span></th>
		<th scope="col"><span><wp:i18n key="jpcalendar_WEEK_WEDNESDAY" /></span></th>
		<th scope="col"><span><wp:i18n key="jpcalendar_WEEK_THURSDAY" /></span></th>
		<th scope="col"><span><wp:i18n key="jpcalendar_WEEK_FRIDAY" /></span></th>
		<th scope="col"><span><wp:i18n key="jpcalendar_WEEK_SATURDAY" /></span></th>
		<th scope="col"><span><wp:i18n key="jpcalendar_WEEK_SUNDAY" /></span></th>		
	</tr>
	<tr><td colspan="8">&nbsp;</td></tr>
	
	
	
<c:forEach var="settimana" items="${jpcalendarCal.calendario}" varStatus="status">
	<tr>
		<th scope="row">

		<c:choose>
			<c:when test="${jpcalendarCal.settimane[status.count-1] == jpcalendarCal.currentWeek}">
				<span><c:out value="${jpcalendarCal.settimane[status.count-1]}"/></span>
			</c:when>
			<c:otherwise>
				<span><c:out value="${jpcalendarCal.settimane[status.count-1]}"/></span>
			</c:otherwise>
		</c:choose>
	

	</th>
	<c:forEach var="giorno" items="${settimana}" varStatus="dayStatus">
		
		<c:if test="${giorno != null}">
		<c:if test="${(giorno.formattedDate == today) && (giorno.hasEvents)}">
		<td>
		</c:if>
		
		<c:if test="${(giorno.formattedDate == today) && (!giorno.hasEvents)}">
		<td>
		</c:if>
		
		<c:if test="${(giorno.formattedDate != today) && (giorno.hasEvents)}">
		<td>
		</c:if>		
		
		<c:if test="${(giorno.formattedDate != today) && (!giorno.hasEvents)}">
		<td>
		</c:if>		
		
		<c:if test="${!giorno.hasEvents}"><c:if test="${giorno.formattedDate == today}"><strong></c:if><c:out value="${giorno.day}"/><c:if test="${giorno.formattedDate == today}"></strong></c:if></c:if>
		<c:if test="${giorno.hasEvents}">
			<wp:pageWithWidget var="jpcalendar_dailyEventsVar" widgetTypeCode="jpcalendar_dailyEvents" />
			<a href="<wp:url page="${jpcalendar_dailyEventsVar.code}" ><wp:parameter name="selectedDate" ><c:out value="${giorno.formattedDate}"/></wp:parameter></wp:url>"><c:if test="${giorno.formattedDate == today}"><strong></c:if><c:out value="${giorno.day}"/><c:if test="${giorno.formattedDate == today}"></strong></c:if></a>
		</c:if>
		</td>
		</c:if>
		<c:if test="${giorno == null}"><td></td></c:if>
	</c:forEach>
	</tr>
</c:forEach>
</table>
