/*
 *
 * Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
 * Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 */
package com.agiletec.plugins.jpsurvey.aps.system.services.collect;

import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.SingleQuestionResponse;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.Voter;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Question;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;

public class ResponseManager extends AbstractService implements IResponseManager {
	
	@Override
	public void init() throws Exception {
		ApsSystemUtils.getLogger().config(this.getClass().getName() + ": initiated ");
	}
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.aps.system.services.collect.IResponseManager#saveResponse(com.agiletec.plugins.jpsurvey.aps.system.services.collect.Response)
	 */
	public void submitResponse(SingleQuestionResponse response) throws ApsSystemException {
		try {
			this.getResponseDAO().submitResponse(response);
		} catch (Throwable t ) {
			ApsSystemUtils.logThrowable(t, this, "submitResponse");
			throw new ApsSystemException("Error while recording the response of a survey", t);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.aps.system.services.collect.IResponseManager#aggregateResponseByIds(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	public List<SingleQuestionResponse> aggregateResponseByIds(Integer voterId,
			Integer questionId, Integer choiceId, String freetext) throws ApsSystemException {
		List<SingleQuestionResponse> list = null;
		try {
			list = this.getResponseDAO().aggregateResponseByIds(voterId, questionId, choiceId, freetext);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "aggregateResponseByIds");
			throw new ApsSystemException("Error while grouping responses", t);
		}
		return list;
	}
	
	@Override
	public Map<Integer, Integer> loadQuestionStatistics(Integer questionId) throws ApsSystemException {
		Map<Integer, Integer> list = null;
		try {
			list = this.getResponseDAO().loadQuestionStatistics(questionId);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "loadQuestionStatistics");
			throw new ApsSystemException("Errore in caricamento statistiche question " + questionId, t);
		}
		return list;
	}
	
	public void deleteResponse(SingleQuestionResponse response) throws ApsSystemException {
		try {
			this.getResponseDAO().deleteResponse(response);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteResponse");
			throw new ApsSystemException("Error while deleting a result from the database ", t);
		}
	}
	
	public void deleteResponseByQuestionId(int id) throws ApsSystemException {
		try {
			this.getResponseDAO().deleteResponseByQuestionId(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteResponseByQuestionId");
			throw new ApsSystemException("Error while deleting responses by the question ID "+id);
		}
	}
	
	public void deleteResponseByChoiceId(int id) throws ApsSystemException {
		try {
			this.getResponseDAO().deleteResponseByChoiceId(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteResponseByQuestionId");
			throw new ApsSystemException("Error while deleting responses by choice ID "+id);
		}
	}
	
	public void deleteResponseBySurvey(Survey survey) throws ApsSystemException {
		try {
			if (null != survey && null != survey.getQuestions()) {
				for (Question question: survey.getQuestions()) {
					this.deleteResponseByQuestionId(question.getId());
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteResponseByQuestionId");
			throw new ApsSystemException("Error while deleting all the responses of the given survey ");
		}
	}
	
	@Override
	public void saveVoterResponse(VoterResponse voterResponse) throws ApsSystemException {
		Voter voter = voterResponse.getVoter();
		try {
			this.getVoterManager().saveVoter(voter);
			int voterId = voter.getId();
			voterResponse.setVoterOnResponses(voterId);
			this.getResponseDAO().submitResponses(voterResponse.getResponses());
		} catch (Throwable t) {
			if (voter.getId() > 0) {
				this.getVoterManager().deleteVoterById(voter.getId());
			}
			ApsSystemUtils.logThrowable(t, this, "saveVoterResponse");
			throw new ApsSystemException("Error saving a vote", t);
		}
	}
	
	protected IVoterManager getVoterManager() {
		return _voterManager;
	}
	public void setVoterManager(IVoterManager voterManager) {
		this._voterManager = voterManager;
	}
	
	protected IResponseDAO getResponseDAO() {
		return _responseDAO;
	}
	public void setResponseDAO(IResponseDAO responseDAO) {
		this._responseDAO = responseDAO;
	}
	
	private IVoterManager _voterManager;
	private IResponseDAO _responseDAO;

}
