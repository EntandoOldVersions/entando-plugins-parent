<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="jpsu" uri="/jpsurvey-aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<wp:headInfo type="CSS" info="../../plugins/jpsurvey/static/css/jpsurvey.css" />
<wp:info key="currentLang" var="currentLang" />
<wp:info key="defaultLang" var="defaultLang" />

<div class="polls">
<p><wp:i18n key="JPSURVEY_POLLS_ARCHIVE_INTRO" />&#32;<a href="<wp:url page="polls_list" />" title="<wp:i18n key="JPSURVEY_GO_TO_ACTIVE_POLLS" />"><wp:i18n key="JPSURVEY_GO_ACTIVE_POLLS" /></a>.</p>

<jpsu:surveyList ctxName="pollList" category="poll" expired="true" />

<c:if test="${not empty pollList}">
	<ul>
		<c:forEach var="currentSurveyItem" items="${pollList}">
			<jpsu:loadSurvey ctxName="currentSurvey" surveyId="${currentSurveyItem}" preferredLang="${currentLang}" votedParamName="voted" ctxImageUrl="imageURL" imageDimension="1" />
				<li class="lista clear">
				<c:if test="${not empty currentSurvey.imageId}">
					<c:choose>
						<c:when test="${not empty currentSurvey.imageDescriptions[currentLang]}">
							<img class="surveyImg" alt="<c:out value="${currentSurvey.imageDescriptions[currentLang]}" />" src="<c:out value="${imageURL}" />" />
						</c:when>
						<c:otherwise>
							<img class="surveyImg" alt="<c:out value="${currentSurvey.imageDescriptions[defaultLang]}" />" src="<c:out value="${imageURL}" />" />
						</c:otherwise>
					</c:choose>
				</c:if>
				<%-- RICORDARSI DI SEPARARE LE PAGINE DI DESTINAZIONE NEL CASO DI SONDAGGI --%>
				<h2>
				<c:choose>
					<c:when test="${not empty currentSurvey.titles[currentLang]}">
						<a href="<wp:url page="poll_detail"><wp:parameter name="surveyId"><c:out value="${currentSurvey.id}"/></wp:parameter></wp:url>" title="<wp:i18n key="JPSURVEY_GO_TO_POLL" />:&#32;<c:out value="${currentSurvey.titles[currentLang]}"/>"><c:out value="${currentSurvey.titles[currentLang]}"/></a>
					</c:when>
					<c:otherwise>
						<a href="<wp:url page="poll_detail"><wp:parameter name="surveyId"><c:out value="${currentSurvey.id}"/></wp:parameter></wp:url>" title="<wp:i18n key="JPSURVEY_GO_TO_POLL" />:&#32;<c:out value="${currentSurvey.titles[defaultLang]}"/>"><c:out value="${currentSurvey.titles[defaultLang]}"/></a>
					</c:otherwise>
				</c:choose>
				</h2>
				<p>
				<c:choose>
					<c:when test="${not empty currentSurvey.descriptions[currentLang]}">
						<c:out value="${currentSurvey.descriptions[currentLang]}"/>
					</c:when>
					<c:otherwise>
						<c:out value="${currentSurvey.descriptions[defaultLang]}"/>
					</c:otherwise>
				</c:choose>
				</p>
				<p class="note">(<c:choose>
					<c:when test="${voted}"><wp:i18n key="JPSURVEY_YOU_VOTED" /></c:when>
					<c:otherwise><wp:i18n key="JPSURVEY_YOU_NOT_VOTED" /></c:otherwise>
				</c:choose>)
				</p>
				</li>
		</c:forEach>
	</ul>
</c:if>
</div>