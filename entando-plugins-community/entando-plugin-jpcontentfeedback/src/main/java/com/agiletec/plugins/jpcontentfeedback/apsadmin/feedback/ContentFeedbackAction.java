/*
*
* Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
* This file is part of jAPS software.
* jAPS is a free software;
* you can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
*
* See the file License for the specific language governing permissions
* and limitations under the License
*
*
*
* Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
*/
package com.agiletec.plugins.jpcontentfeedback.apsadmin.feedback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.ICommentManager;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.CommentSearchBean;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.IComment;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.ICommentSearchBean;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.IRatingManager;

/**
 * @author D.Cherchi
 */
public class ContentFeedbackAction extends AbstractContentFeedbackAction implements IContentFeedbackAction{

	@Override
	public String search() {
		try {
			ICommentSearchBean searchBean = this.prepareSearchBean();
			boolean hasAuthor = null != this.getAuthor() && this.getAuthor().trim().length() > 0;
			boolean hasComment = null != this.getText() && this.getText().trim().length() > 0;
			boolean hasStatus = -1 != this.getStatus();
			boolean hasFromData = null != this.getFrom() ;
			boolean hasToData = null != this.getTo();
			if ((!hasAuthor) && (!hasComment) && (!hasFromData) && (!hasToData) && (!hasStatus)) {
				this.addActionError(this.getText("error.filters.empty"));
				return INPUT;
			} else {
				this.setExecuteSearch(true);
			}
			this.setSearchBean(searchBean);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "search");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public List<String> getCommentIds() {
		List<String> comments = new ArrayList<String>();
		try {
			if (null == this.getExecuteSearch() || !this.getExecuteSearch()) return null;
			ICommentSearchBean searchBean = this.getSearchBean();
			comments = this.getCommentManager().searchCommentIds(searchBean);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getCommentIds");
		}
		return comments;
	}

	@Override
	public IComment getComment(int id){
		IComment comment = null;
		try {
			comment = this.getCommentManager().getComment(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getComment");
		}
		return comment;
	}

	@Override
	public String view() {
		try {
			IComment comment = this.getComment(this.getSelectedComment());
			this.setComment(comment);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "view");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String delete(){
		try {
			 this.getCommentManager().deleteComment(this.getSelectedComment());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "delete");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String updateStatus() {
		try {
			this.getCommentManager().updateCommentStatus(this.getSelectedComment(), this.getStatus());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "updateStatus");
			return FAILURE;
		}
		return SUCCESS;
	}

	private ICommentSearchBean prepareSearchBean() {
		CommentSearchBean searchBean = new CommentSearchBean();
		searchBean.setComment(this.getText());
		searchBean.setUsername(this.getAuthor());
		searchBean.setCreationFROMDate(this.getFrom());
		searchBean.setCreationTODate(this.getTo());
		searchBean.setStatus(this.getStatus());
		return searchBean;
	}

	public void setExecuteSearch(Boolean executeSearch) {
		this._executeSearch = executeSearch;
	}
	public Boolean getExecuteSearch() {
		return _executeSearch;
	}

	public void setCommentManager(ICommentManager commentManager) {
		this._commentManager = commentManager;
	}
	public ICommentManager getCommentManager() {
		return _commentManager;
	}

	public String getText() {
		return _commentText;
	}
	public void setText(String text) {
		this._commentText = text;
	}

	public String getAuthor() {
		return _author;
	}
	public void setAuthor(String author) {
		this._author = author;
	}

	public Date getFrom() {
		return _from;
	}
	public void setFrom(Date from) {
		this._from = from;
	}

	public Date getTo() {
		return _to;
	}
	public void setTo(Date to) {
		this._to = to;
	}

	public void setSearchBean(ICommentSearchBean searchBean) {
		this._searchBean = searchBean;
	}
	public ICommentSearchBean getSearchBean() {
		return _searchBean;
	}

	public void setSelectedComment(int selectedComment) {
		this._selectedComment = selectedComment;
	}
	public int getSelectedComment() {
		return _selectedComment;
	}

	public void setComment(IComment comment) {
		this._comment = comment;
	}
	public IComment getComment() {
		return _comment;
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
		return _votes;
	}

	public void setVote(int vote) {
		this._vote = vote;
	}
	public int getVote() {
		return _vote;
	}

	public void setStatus(int status) {
		this._status = status;
	}
	public int getStatus() {
		return _status;
	}

	private String _commentText;
	private String _author;
	private Date _from;
	private Date _to;
	private ICommentManager _commentManager;
	private Boolean _executeSearch;
	private ICommentSearchBean _searchBean;
	private int _selectedComment;
	private IComment _comment;
	private Map<String, Integer>  _votes;
	private IRatingManager _ratingManager;
	private int _status;
	private int _vote;
	private String _selectedContent;

}