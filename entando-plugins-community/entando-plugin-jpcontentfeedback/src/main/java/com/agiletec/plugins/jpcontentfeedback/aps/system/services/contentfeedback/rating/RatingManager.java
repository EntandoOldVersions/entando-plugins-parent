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
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model.IRating;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model.Rating;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model.RatingSearchBean;

@Aspect
public class RatingManager extends AbstractService implements IRatingManager{

	@Override
	public void init() throws Exception {
		ApsSystemUtils.getLogger().config(this.getName() + ": initialized");
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
			}else{
				rating.setVote(rating.getVoters()+1, rating.getSumvote()+vote);
				this.getRatingDAO().updateRating(rating);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addRatingToContent");
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
			}else{
				rating.setVote(rating.getVoters()+1, rating.getSumvote()+vote);
				this.getRatingDAO().updateRating(rating);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addRatingToComment");
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
			ApsSystemUtils.logThrowable(t, this, "getRating");
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
			ApsSystemUtils.logThrowable(t, this, "getCommentRating");
			throw new ApsSystemException("Error get comment rating", t);
		}
		return rating;
	}

	@AfterReturning(
			pointcut="execution(* com.agiletec.plugins.jacms.aps.system.services.content.IContentManager.deleteContent(..)) && args(content)")
	public void deleteContentRating(Content content) throws ApsSystemException{
		try{
			this.getRatingDAO().removeContentRating(content.getId());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteContentRating");
			throw new ApsSystemException("Error while remove content rating", t);
		}
	}
	public void setRatingDAO(IRatingDAO ratingDAO) {
		this._ratingDAO = ratingDAO;
	}

	public IRatingDAO getRatingDAO() {
		return _ratingDAO;
	}

	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	public IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}

	private IRatingDAO _ratingDAO;
	private IKeyGeneratorManager _keyGeneratorManager;


}
