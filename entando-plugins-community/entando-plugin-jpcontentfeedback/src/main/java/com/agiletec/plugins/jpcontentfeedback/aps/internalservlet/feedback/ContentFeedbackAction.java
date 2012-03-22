/*
 *
 * Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
 *
 * This file is part of Entando software.
 * Entando is a free software;
 * you can redistribute it and/or modify it
 * under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
 *
 * See the file License for the specific language governing permissions
 * and limitations under the License
 *
 *
 *
 * Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
 *
 */
package com.agiletec.plugins.jpcontentfeedback.aps.internalservlet.feedback;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.BidiMap;
import org.apache.commons.collections.bidimap.TreeBidiMap;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsException;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.page.Showlet;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.ContentAuthorizationInfo;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.IContentDispenser;
import com.agiletec.plugins.jacms.apsadmin.content.helper.ContentActionHelper;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.CheckVotingUtil;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.IContentFeedbackManager;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.ICommentManager;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.Comment;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.CommentSearchBean;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.IComment;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.IRatingManager;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model.IRating;
import com.agiletec.plugins.jpcontentfeedback.apsadmin.feedback.AbstractContentFeedbackAction;

/**
 * @author D.Cherchi
 */
public class ContentFeedbackAction extends AbstractContentFeedbackAction implements IContentFeedbackAction, ServletResponseAware {

	/**
	 * When in frontEnd the tag is inserted inside a paginated list
	 * this method help to build the hidden field, eg:
	 * <pre>
	 * 	&lt;s:if test="null != frameItem"&gt;
	 * 		&lt;c:set var="framekey"&gt;&lt;s:property value="frameItem[0]"/&gt;&lt;/c:set&gt;
	 * 		&lt;c:set var="frameval"&gt;&lt;s:property value="frameItem[1]"/&gt;&lt;/c:set&gt;
	 * 		&lt;input type="hidden" name="&lt;c:out value="${framekey}" /&gt;" value="&lt;c:out value="${frameval}" /&gt;" /&gt;
	 * 	&lt;/s:if&gt;
	 * </pre>
	 * @return
	 */
	public String[] getFrameItem() {
		String[] value = null;
		Map<String, String[]> params = super.getParameters();
		if (null != params) {
			Iterator it = params.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pairs = (Map.Entry)it.next();
				String key = (String) pairs.getKey();
				if (key.startsWith("frame")) {
					String regexp = "frame[0-9]+_item";
					Pattern pattern = Pattern.compile(regexp);
					Matcher matcher = pattern.matcher(key);
					if (matcher.matches()) {
						value = new String[2];
						value[0] = key;
						String[] paramValue = (String[]) pairs.getValue();
						if (null != paramValue) {
							value[1] = paramValue[0];
						}
					}
				}
			}
		}
		return value;
	}

	@Override
	public List<String> getContentCommentIds() {
		List<String> commentIds = new ArrayList<String>();
		try {
			String contentId = this.extractContentId();
			if (null == contentId) {
				ApsSystemUtils.getLogger().severe("Content id null");
				return commentIds;
			}
			if (this.isAuth(contentId)) {
				CommentSearchBean searchBean = new CommentSearchBean();
				searchBean.setContentId(contentId);
				searchBean.setStatus(Comment.STATUS_APPROVED);
				commentIds = this.getCommentManager().searchCommentIds(searchBean);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentCommentIds");
			throw new RuntimeException("Error", t);
		}
		return commentIds;
	}

	@Override
	public boolean isAuthorizedToDelete(String username){
		return username.equals(this.getCurrentUser().getUsername());
	}

	@Override
	public String delete() {
		try {
			if(!this.getCurrentUser().getUsername().equals(SystemConstants.GUEST_USER_NAME) && isAuthorizedToDelete(this.getCurrentUser().getUsername()) ){
				if (this.isAuth(this.getContentId())){
					this.getCommentManager().deleteComment(this.getSelectedComment());
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "delete");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String addComment() {
		try {

			if (!this.getContentFeedbackManager().allowAnonymousComment() && this.getCurrentUser().getUsername().equals(SystemConstants.GUEST_USER_NAME)) {
				return BaseAction.USER_NOT_ALLOWED;
			}

			Comment comment = new Comment();
			String contentId = this.extractContentId();
			if (this.isAuth(contentId)) {
				RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
				Showlet currentShowlet = (Showlet) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_SHOWLET);
				String requiredValidation = (null != currentShowlet.getConfig()) ? currentShowlet.getConfig().getProperty("commentValidation") : null;
				if (requiredValidation != null && requiredValidation.equalsIgnoreCase("true")){
					comment.setStatus(Comment.STATUS_TO_APPROVE);
					this.addActionMessage(this.getText("jpcontentfeedback_MESSAGE_TO_APPROVED"));
				} else {
					comment.setStatus(Comment.STATUS_APPROVED);
				}
				comment.setComment(this.getCommentText());
				comment.setContentId(contentId);
				comment.setUsername(this.getCurrentUser().getUsername());
				this.getCommentManager().addComment(comment);
			}

		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addComment");
			return FAILURE;
		}
		return SUCCESS;
	}

	private boolean isAuth(String contentId) {
		IContentDispenser contentDispenser = (IContentDispenser) ApsWebApplicationUtils.getBean(JacmsSystemConstants.CONTENT_DISPENSER_MANAGER, this.getRequest());
		IAuthorizationManager authManager = (IAuthorizationManager) ApsWebApplicationUtils.getBean(SystemConstants.AUTHORIZATION_SERVICE, this.getRequest());
		ContentAuthorizationInfo authInfo = contentDispenser.getAuthorizationInfo(contentId);
		return (authInfo.isUserAllowed(authManager.getGroupsOfUser(this.getCurrentUser())));
	}

	@Override
	public IComment getComment(Integer commentId) {
		IComment comment = null;
		try {
			comment = this.getCommentManager().getComment(commentId.intValue());
		} catch (ApsSystemException e) {
			ApsSystemUtils.logThrowable(e, this, "getComment");
			throw new RuntimeException("Error", e);
		}
		return comment;
	}

	@Override
	public IRating getContentRating(){
		IRating rating = null;
		try {
			String contentId = this.extractContentId();
			if (null == contentId) {
				ApsSystemUtils.getLogger().severe("Content id null");
				return null;
			}
			rating = this.getRatingManager().getContentRating(contentId);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentRating");
			throw new RuntimeException("Error", t);
		}
		return rating;
	}

	@Override
	public IRating getCommentRating(int commentId){
		IRating rating = null;
		try {
			if (commentId == 0) {
				ApsSystemUtils.getLogger().severe("Content id null");
				return null;
			}
			rating = this.getRatingManager().getCommentRating(commentId);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getCommentRating");
			throw new RuntimeException("Error", t);
		}
		return rating;
	}

	@Override
	public String insertVote() {
		try {
			String contentId = this.extractContentId();

			if (!this.getContentFeedbackManager().allowAnonymousComment() && this.getCurrentUser().getUsername().equals(SystemConstants.GUEST_USER_NAME)) {
				return BaseAction.USER_NOT_ALLOWED;
			}

			if (this.isAuth(contentId)) {
				if (!this.isValidVote()) {
					this.addActionError(this.getText("Message.invalidVote"));
					return INPUT;
				}
				if (this.getSelectedComment() != 0) {
					boolean alreadyVoted = CheckVotingUtil.isCommentVoted(this.getSelectedComment(), this.getRequest());
					if (alreadyVoted){
						this.addActionError(this.getText("Message.alreadyVoted"));
						return INPUT;
					}
					this.getRatingManager().addRatingToComment(this.getSelectedComment(), this.getVote());
					this.addCookieRating(this.getSelectedComment());
				} else if (contentId != null && contentId.length() > 0) {
					boolean alreadyVoted = CheckVotingUtil.isContentVoted(contentId, this.getRequest());
					if (alreadyVoted){
						this.addActionError(this.getText("Message.alreadyVoted"));
						return INPUT;
					}
					this.getRatingManager().addRatingToContent(contentId, this.getVote());
					this.addCookieRating(contentId);
				} else {
					throw new ApsException("Error information vote");
				}
			} else {
				ApsSystemUtils.getLogger().info("not auth to insert vote on content " + contentId);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "insertVote");
			return FAILURE;
		}
		return SUCCESS;
	}

	protected boolean isValidVote() {
		return (null != this.getVote() && this.getVotes().values().contains(this.getVote()));
	}

	protected void addCookieRating(String contentId) {
		UserDetails currentUser = this.getCurrentUser();
		String cookieName = CheckVotingUtil.getCookieName(currentUser.getUsername(), contentId);
		String cookieValue = CheckVotingUtil.getCookieValue(currentUser.getUsername(), contentId);
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(365*24*60*60);//one year
		this.getResponse().addCookie(cookie);
	}

	protected void addCookieRating(int commentId) {
		UserDetails currentUser = this.getCurrentUser();
		String cookieName = CheckVotingUtil.getCookieName(currentUser.getUsername(), commentId);
		String cookieValue = CheckVotingUtil.getCookieValue(currentUser.getUsername(), commentId);
		Cookie cookie = new Cookie(cookieName, cookieValue);
		cookie.setMaxAge(365*24*60*60);//one year
		this.getResponse().addCookie(cookie);
	}

	protected HttpServletResponse getResponse() {
		return this._response;
	}
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this._response = response;
	}

	public void setSelectedContent(String selectedContent) {
		this._selectedContent = selectedContent;
	}
	public String getSelectedContent() {
		return _selectedContent;
	}

	public void setRatingManager(IRatingManager ratingManager) {
		this._ratingManager = ratingManager;
	}
	public IRatingManager getRatingManager() {
		return _ratingManager;
	}

	public void setVotes(Map<String, Integer> votes) {
		this._votes = votes;
	}
	public Map<String, Integer> getVotes() {
		if (null != this.getReverseVotes() && this.getReverseVotes().booleanValue()) {
			TreeMap<String, Integer> t = new TreeMap<String, Integer>(this._votes);
			NavigableMap<String, Integer> x = t.descendingMap();
			return  x;
			//return t.descendingMap();
		}
		return _votes;
	}

	public void setVote(Integer vote) {
		this._vote = vote;
	}
	public Integer getVote() {
		return _vote;
	}

	protected ICommentManager getCommentManager() {
		return _commentManager;
	}
	public void setCommentManager(ICommentManager commentManager) {
		this._commentManager = commentManager;
	}

	public String getCommentText() {
		return _commentText;
	}
	public void setCommentText(String commentText) {
		this._commentText = commentText;
	}

	public int getSelectedComment() {
		return _selectedComment;
	}
	public void setSelectedComment(int selectedComment) {
		this._selectedComment = selectedComment;
	}

	public String extractContentId() {
		String contentId = this.getContentId();
		if (null == contentId || contentId.trim().length() == 0) {
			contentId = null;
			RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
			Showlet currentShowlet = (Showlet) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_SHOWLET);
			if (null != currentShowlet.getConfig() &&
					currentShowlet.getConfig().getProperty("contentId") != null &&
					currentShowlet.getConfig().getProperty("contentId").length() > 0) {
				contentId = currentShowlet.getConfig().getProperty("contentId");
			} else {
				contentId = this.getRequest().getParameter("contentId");
			}
		}
		return contentId;
	}

	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	public String getContentId() {
		return _contentId;
	}

	protected ContentActionHelper getContentActionHelper() {
		return _contentActionHelper;
	}
	public void setContentActionHelper(ContentActionHelper contentActionHelper) {
		this._contentActionHelper = contentActionHelper;
	}

	protected IContentFeedbackManager getContentFeedbackManager() {
		return _contentFeedbackManager;
	}
	public void setContentFeedbackManager(IContentFeedbackManager contentFeedbackManager) {
		this._contentFeedbackManager = contentFeedbackManager;
	}

	public Boolean getReverseVotes() {
		return _reverseVotes;
	}
	public void setReverseVotes(Boolean reverseVotes) {
		this._reverseVotes = reverseVotes;
	}

	private String _commentText;
	private ICommentManager _commentManager;
	private Map<String, Integer>  _votes;
	private IRatingManager _ratingManager;
	private Integer _vote;
	private String _selectedContent;
	private int _selectedComment;
	private HttpServletResponse _response;
	private String _contentId;
	private ContentActionHelper _contentActionHelper;
	private IContentFeedbackManager _contentFeedbackManager;
	private Boolean _reverseVotes;

}