<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="jpcf" uri="/jpcontentfeedback-aps-core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<wp:headInfo type="CSS" info="../../plugins/jpcontentfeedback/static/css/jpcontentfeedback.css"/>
<s:set var="htmlIdPrefix">jpfeedback_<wp:currentPage param="code" /><wp:currentShowlet param="code"/>_</s:set>
<div class="jpcontentfeedback">
	<s:if test="hasActionErrors()">
		<div class="error">
			<h3><wp:i18n key="ERRORS" /></h3>
			<ul>
			<s:iterator value="actionErrors">
				<li><s:property escape="false" /></li>
			</s:iterator>
			</ul>
		</div>
	</s:if>
	<s:if test="hasActionMessages()">
		<div class="message">
			<h3><wp:i18n key="MESSAGES" /></h3>
			<ul>
				<s:iterator value="actionMessages">
					<li><s:property /></li>
				</s:iterator>
			</ul>
		</div>
	</s:if>

	<jpcf:ifViewContentOption param="usedContentRating">
		<div class="content_rating">
			<h3 class="jpcontentfeedback_title"><span><wp:i18n key="jpcontentfeedback_CONTENT_RATING" /></span></h3>
			<s:set var="rating" value="%{getContentRating()}" />
			<s:if test="#rating != null">
				<p>
					<wp:i18n key="jpcontentfeedback_AVG_RATING" />&#32;<fmt:formatNumber value="${rating.average}" pattern="#0.00" />. &#32;<s:property value="#rating.voters" />&#32;<wp:i18n key="jpcontentfeedback_VOTERS_NUM" />
				</p>
			</s:if>
			<s:else>
				<p><wp:i18n key="jpcontentfeedback_VOTERS_NULL" /></p>
			</s:else>
			
		 	<wp:ifauthorized permission="jpcontentfeedback_rating_edit">
				<form action="<wp:action path="/ExtStr2/do/jpcontentfeedback/FrontEnd/contentfeedback/insertVote.action"/>" method="post">
					<fieldset>
						<legend><wp:i18n key="jpcontentfeedback_VOTE" /></legend>
						<p class="noscreen">
							<wpsf:hidden name="contentId" value="%{contentId}" />
						</p>
						<ul class="rate_values">
							<s:iterator value="votes" var="voteItem">
								<li class="rate_value"> 
									<s:set var="currentId" value="%{#htmlIdPrefix+'_vote_'+#voteItem.key}" />
									<input type="radio" name="vote" id="<s:property value="#currentId" />" value="<s:property value="#voteItem.key" />" />
									&#32;
									<label for="<s:property value="#currentId" />" title="<wp:i18n key="jpcontentfeedback_VOTE" />:&#32;<s:property value="#voteItem.key" />"><s:property value="#voteItem.value" /></label>
								</li> 
							</s:iterator>
						</ul> 
						<%-- submit --%> 
						<p class="centerText">
							<s:set name="labelSubmit"><wp:i18n key="jpcontentfeedback_VOTE_SUBMIT" /></s:set>
							<wpsf:submit useTabindexAutoIncrement="true" action="insertVote" value="%{#labelSubmit}" cssClass="button" /> 
						</p>						
					</fieldset>
				</form>
			</wp:ifauthorized>
		</div>
	</jpcf:ifViewContentOption>
	
	<%-- Comments --%>
	<jpcf:ifViewContentOption param="usedCommentWithRating" var="isUsedCommentWithRating" />
	<jpcf:ifViewContentOption param="usedComment">
		<div class="content_comments">
			<h3 class="jpcontentfeedback_title"><span><wp:i18n key="jpcontentfeedback_COMMENTS" /></span></h3>
			<s:set var="contentCommentIdsVar" value="%{contentCommentIds}"/>
			<s:if test="#contentCommentIdsVar.size > 0 ">
				<wp:pager listName="contentCommentIdsVar" objectName="groupComment" pagerIdFromFrame="true"  max="10">
					<ol>
					<c:forEach var="commentId" items="${contentCommentIdsVar}" begin="${groupComment.begin}" end="${groupComment.end}" varStatus="status">
						<li <c:if test="${status.last}">class="last_comment"</c:if>> 
							<jpcf:contentCommentViewer commentId="${commentId}" commentName="comment"/>
							<div class="comment_info">
								<%-- comment info --%>
								<dl>
									<dt> <wp:i18n key="jpcontentfeedback_AUTHOR" />:</dt>
										<dd><c:out value="${comment.username}" /></dd>
									<dt><wp:i18n key="jpcontentfeedback_COMMENT_DATE" />:</dt>
										<dd><fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss" value="${comment.creationDate}" /></dd>
									<dt class="comment_text"><wp:i18n key="jpcontentfeedback_COMMENT_TEXT" />:</dt>
										<dd><blockquote><p>
											<s:set var="ccc"><c:out value="${comment.comment}" /></s:set>
											<s:property value='#ccc.replaceAll("\r", "").replaceAll("\n\n+", "<br />\n").replaceAll("\n+", "<br />")' escapeHtml="false" />
										</p></blockquote></dd>
								</dl>
								<%-- remove --%>
								<c:if test="${comment.username == sessionScope.currentUser}" >
									<wp:ifauthorized permission="jpcontentfeedback_comment_edit">
										<p class="comment_actions">
											<a href="<wp:action path="/ExtStr2/do/jpcontentfeedback/FrontEnd/contentfeedback/delete.action">
														<wp:parameter name="selectedComment"><c:out value="${commentId}" /></wp:parameter>
														<wp:parameter name="contentId"><s:property value="extractContentId()" /></wp:parameter>
													</wp:action>" title="<wp:i18n key="jpcontentfeedback_DELETE" />">
												<img src="<wp:resourceURL />plugins/jpcontentfeedback/static/img/delete.png" alt="<wp:i18n key="jpcontentfeedback_DELETE" />"/>
											</a>
										</p>
									</wp:ifauthorized>
								</c:if>
							</div>
							<c:if test="${isUsedCommentWithRating}" >
								<div class="comment_rating">
									<h4><wp:i18n key="jpcontentfeedback_COMMENT_RATING" /><span class="noscreen">:&#32;<c:out value="${comment.username}" />&#32;<fmt:formatDate pattern="dd/MM/yyyy HH:mm:ss" value="${comment.creationDate}" /> </span></h4>
									<s:set var="commentIdvalue"><c:out value="${commentId}" /></s:set>
									<s:set var="commentRating" value="%{getCommentRating(#commentIdvalue)}"></s:set>
									<s:if test="#commentRating != null">
										<p class="comment_current_average_vote">
											<fmt:formatNumber value="${commentRating.average}" pattern="#0.00" />
										</p>
										<p><c:out value="${commentRating.voters}" />&#32;<wp:i18n key="jpcontentfeedback_VOTERS_NUM" /></p>
									</s:if>
									<s:else>
										<p><abbr title="<wp:i18n key="jpcontentfeedback_COMMENT_NORATING" />">&ndash;</abbr></p> 
									</s:else>
									<wp:ifauthorized permission="jpcontentfeedback_rating_edit">
										<form action="<wp:action path="/ExtStr2/do/jpcontentfeedback/FrontEnd/contentfeedback/insertVote.action" />" method="post">
											<p>
												<wpsf:hidden name="contentId" value="%{contentId}" />
												<wpsf:hidden name="selectedComment" value="%{#commentIdvalue}" />
												<s:set var="htmt_vode_id">votecomment_<c:out value="${comment.id}" /><c:out value="${comment.contentId}" /></s:set>
												<s:set name="labelSubmit"><wp:i18n key="jpcontentfeedback_SEND" /></s:set>
												<label for="<s:property value="#htmt_vode_id" />"><wp:i18n key="jpcontentfeedback_LABEL_RATING" /></label>
												<%-- 
												<wpsf:select useTabindexAutoIncrement="true" list="votes" name="vote" id="%{#htmt_vode_id}" value="0" />&nbsp;
												--%>
												<select name="vote" id="<s:property value="#htmt_vode_id" />">
													<s:iterator value="votes" var="voteItem">
															<s:set var="currentId" value="%{#htmlIdPrefix+'_vote_'+#voteItem.key}" />
															<option value="<s:property value="#voteItem.key" />"><s:property value="#voteItem.value" /></option>
													</s:iterator>
												</select>
											</p>
											<p>												
												<wpsf:submit useTabindexAutoIncrement="true" action="insertCommentVote" value="%{#labelSubmit}" cssClass="button" />
											</p>
										</form>
									</wp:ifauthorized>								
									
									
								</div>
							</c:if>
						</li>
					</c:forEach>
					
					</ol>
					<%--  --%>   
							
					<c:if test="${groupComment.size > groupComment.max}">
						<p class="paginazione">
							<c:choose>
							<c:when test="${'1' == groupComment.currItem}">&laquo; <wp:i18n key="PREV" /></c:when>
							<c:otherwise><a href="<wp:url paramRepeat="true" ><wp:parameter name="${groupComment.paramItemName}" ><c:out value="${groupComment.prevItem}"/></wp:parameter></wp:url>">&laquo;&#32;<wp:i18n key="PREV" /></a></c:otherwise>					
							</c:choose>
							<c:forEach var="item" items="${groupComment.items}">
								<c:choose>
								<c:when test="${item == groupComment.currItem}">&#32;[<c:out value="${item}"/>]&#32;</c:when>
								<c:otherwise>&#32;<a href="<wp:url paramRepeat="true" ><wp:parameter name="${groupComment.paramItemName}" ><c:out value="${item}"/></wp:parameter></wp:url>"><c:out value="${item}"/></a>&#32;</c:otherwise>
								</c:choose>
							</c:forEach>
							<c:choose>
							<c:when test="${groupComment.maxItem == groupComment.currItem}"><wp:i18n key="NEXT" />&#32;&raquo;</c:when>
							<c:otherwise><a href="<wp:url paramRepeat="true" ><wp:parameter name="${groupComment.paramItemName}" ><c:out value="${groupComment.nextItem}"/></wp:parameter></wp:url>"><wp:i18n key="NEXT" />&#32;&raquo;</a></c:otherwise>					
							</c:choose>
						</p>
					</c:if>
				</wp:pager>
			</s:if>
			<s:else>
				<p><wp:i18n key="jpcontentfeedback_COMMENTS_NULL" /></p>
			</s:else>
		
			<wp:ifauthorized permission="jpcontentfeedback_comment_edit">
				<div class="comment_form">
					<h3 class="jpcontentfeedback_title"><span><wp:i18n key="jpcontentfeedback_COMMENT_THE_CONTENT" /></span></h3>
					<%-- Messaggi di errore utente/validazione --%>
					<s:if test="hasFieldErrors()">
						<div class="error">
						<h4><wp:i18n key="ERRORS" /></h4>
							<ul>
								<s:iterator value="fieldErrors">
									<s:iterator value="value">
										<li><s:property escape="false" /></li>
									</s:iterator>
								</s:iterator>
							</ul>
						</div>
					</s:if>
				
					<form action="<wp:action path="/ExtStr2/do/jpcontentfeedback/FrontEnd/contentfeedback/insert.action"/>" method="post">
						<p>
							<wpsf:hidden name="contentId" value="%{contentId}" ></wpsf:hidden>
							<label for="commentText"><wp:i18n key="jpcontentfeedback_LABEL_COMMENTTEXT" />:</label><br /> 
							<wpsf:textarea useTabindexAutoIncrement="true" name="commentText" id="commentText" value="" cssClass="text" cols="40" rows="3" />
						</p>
						<p>
							<s:token />
							<s:set name="labelSubmit"><wp:i18n key="jpcontentfeedback_SEND" /></s:set>
							<wpsf:submit useTabindexAutoIncrement="true" action="insert" value="%{#labelSubmit}" cssClass="button" />
						</p>
					</form>
				</div>
			</wp:ifauthorized>
		</div>
	</jpcf:ifViewContentOption>

</div>