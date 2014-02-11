/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.event.ContentFeedbackChangedEvent;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model.IRating;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model.Rating;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model.RatingSearchBean;

@Aspect
public class RatingManager extends AbstractService implements IRatingManager{

	private static final Logger _logger = LoggerFactory.getLogger(RatingManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}

	@Override
	public synchronized void addRatingToContent(String contentId, int vote) throws ApsSystemException{
		try {
			RatingSearchBean searchBean = new RatingSearchBean();
			searchBean.setContentId(contentId);
			Rating rating = this.getRatingDAO().getRating(searchBean);
			if (rating == null){
				rating = new Rating();
				int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
				rating.setId(key);
				rating.setContentId(contentId);
				rating.setVote(1, vote);
				this.getRatingDAO().addRating(rating);
			} else {
				rating.setVote(rating.getVoters()+1, rating.getSumvote()+vote);
				this.getRatingDAO().updateRating(rating);
			}
            this.notifyEvent(contentId, -1, ContentFeedbackChangedEvent.CONTENT_RATING, ContentFeedbackChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error add rating{} to content {}",vote, contentId, t);
			throw new ApsSystemException("Error add rating to content", t);
		}
	}

	@Override
	public synchronized void addRatingToComment(int commentId, int vote) throws ApsSystemException{
		try {
			RatingSearchBean searchBean = new RatingSearchBean();
			searchBean.setCommentId(commentId);
			Rating rating = this.getRatingDAO().getRating(searchBean);
			if (rating == null){
				rating = new Rating();
				int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
				rating.setId(key);
				rating.setCommentId(commentId);
				rating.setVote(1, vote);
				this.getRatingDAO().addRating(rating);
			} else {
				rating.setVote(rating.getVoters()+1, rating.getSumvote()+vote);
				this.getRatingDAO().updateRating(rating);
			}
            this.notifyEvent(null, commentId, ContentFeedbackChangedEvent.COMMENT_RATING, ContentFeedbackChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error add rating {} to comment {}", vote, commentId, t);
			throw new ApsSystemException("Error add rating to comment", t);
		}
	}

	@Override
	public IRating getContentRating(String contentId) throws ApsSystemException{
		IRating rating = null;
		try{
			RatingSearchBean searchBean  = new RatingSearchBean();
			searchBean.setContentId(contentId);
			rating = this.getRatingDAO().getRating(searchBean);
		} catch (Throwable t) {
			_logger.error("Error loading content rating for content {}", contentId, t);
			throw new ApsSystemException("Error get content rating", t);
		}
		return rating;
	}

	@Override
	public IRating getCommentRating(int commentId) throws ApsSystemException{
		IRating rating = null;
		try{
			RatingSearchBean searchBean  = new RatingSearchBean();
			searchBean.setCommentId(commentId);
			rating = this.getRatingDAO().getRating(searchBean);
		} catch (Throwable t) {
			_logger.error("Error loading comment rating for comment {}", commentId, t);
			throw new ApsSystemException("Error get comment rating", t);
		}
		return rating;
	}

	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jacms.aps.system.services.content.IContentManager.deleteContent(..)) && args(content)")
	public void deleteContentRating(Content content) throws ApsSystemException{
		try{
			this.getRatingDAO().removeContentRating(content.getId());
            this.notifyEvent(content.getId(), -1, ContentFeedbackChangedEvent.CONTENT_RATING, ContentFeedbackChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			_logger.error("Error while remove content rating", t);
			throw new ApsSystemException("Error while remove content rating", t);
		}
	}
	
	private void notifyEvent(String contentId, int commentId, int objectCode, int operationCode) throws ApsSystemException {
		ContentFeedbackChangedEvent event = new ContentFeedbackChangedEvent();
		event.setContentId(contentId);
		event.setCommentId(commentId);
		event.setObjectCode(objectCode);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}
    
	public IRatingDAO getRatingDAO() {
		return _ratingDAO;
	}
	public void setRatingDAO(IRatingDAO ratingDAO) {
		this._ratingDAO = ratingDAO;
	}
	
	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}
	
	private IRatingDAO _ratingDAO;
	private IKeyGeneratorManager _keyGeneratorManager;
	
}