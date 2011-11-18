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
package com.agiletec.plugins.jpsurvey.aps.system.services.survey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.GroupUtilizer;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.IResponseDAO;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.IVoterDAO;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Choice;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Question;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.SurveyRecord;

public class SurveyManager extends AbstractService implements ISurveyManager, GroupUtilizer {
	
	@Override
	public void init() throws Exception {
		ApsSystemUtils.getLogger().config(this.getClass().getName() + ": initiated ");
	}
	
	@Override
	public Survey loadSurvey(int id) throws ApsSystemException {
		try {
			return this.getSurveyDAO().loadSurvey(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "loadSurvey");
			throw new ApsSystemException("Error while loading a complete survey", t);
		}
	}
	
	@Override
	public void saveSurvey(Survey survey) throws ApsSystemException {
		try {
			this.getSurveyDAO().saveSurvey(survey);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveSurvey");
			throw new ApsSystemException("Error while saving a complete survey", t);
		}
	}
	
	@Override
	public void deleteSurvey(int id) throws ApsSystemException {
		try {
			this.getSurveyDAO().deleteSurvey(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteSurvey");
			throw new ApsSystemException("Error while deleting a complete survey", t);
		}
	}
	
	@Override
	public Question loadQuestion(int id) throws ApsSystemException {
		try {
			return this.getQuestionDAO().loadQuestion(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "loadQuestion");
			throw new ApsSystemException("Error while loading a question with its choices", t);
		}
	}
	
	@Override
	public Choice loadChoice(int id) throws ApsSystemException {
		try {
			return this.getChoiceDAO().loadChoice(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "loadChoice");
			throw new ApsSystemException("Error while loading a choice", t);
		}
	}
	
	@Override
	public void saveChoice(Choice choice) throws ApsSystemException {
		try {
			this.getChoiceDAO().saveChoice(choice);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveChoice");
			throw new ApsSystemException("Error while saving a choice", t);
		}
	}
	
	@Override
	public void saveChoiceInSortedPosition(Choice choice) throws ApsSystemException {
		try {
			this.getChoiceDAO().saveChoiceInSortedPosition(choice);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveChoiceInSortedPosition");
			throw new ApsSystemException("Error while saving a choice in sorted position", t);
		}
	}
	
	@Override
	public void deleteChoice(int id) throws ApsSystemException {
		try {
			this.getChoiceDAO().deleteChoice(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteChoice");
			throw new ApsSystemException("Error while deleting the choice", t);
		}
	}
	
	@Override
	public List<Choice> getQuestionChoices(int id) throws ApsSystemException {
		List<Choice> choices = null;
		try {
			choices = this.getQuestionDAO().getQuestionChoices(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getQuestionChoices");
			throw new ApsSystemException("Error while getting the choices of a question", t);
		}
		return choices;
	}
	
	@Override
	public List<Question> getSurveyQuestions(int id) throws ApsSystemException {
		List<Question> questions = null;
		try {
			questions = this.getSurveyDAO().getSurveyQuestions(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getSurveyQuestions");
			throw new ApsSystemException("Error while getting the questions of a survey", t);
		}
		return questions;
	}
	
	@Override
	public void saveQuestion(Question question) throws ApsSystemException {
		try {
			this.getQuestionDAO().saveQuestion(question);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveQuestion");
			throw new ApsSystemException("Error while saving a question", t);
		}
	}
	
	@Override
	public void deleteQuestion(int id) throws ApsSystemException{
		try {
			this.getQuestionDAO().deleteQuestion(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteQuestion");
			throw new ApsSystemException("Error while deleting a question", t);
		}
	}
	
	@Override
	public void deleteChoiceByQuestionId(int id) throws ApsSystemException{
		try {
			this.getChoiceDAO().deleteChoiceByQuestionId(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteChoiceByQuestionId");
			throw new ApsSystemException("Error while deleting choices by their question ID ", t);
		}
	}
	
	@Override
	public void updateChoice(Choice choice) throws ApsSystemException {
		try {
			this.getChoiceDAO().updateChoice(choice);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "updateChoice");
			throw new ApsSystemException("Error while updating a choice", t);
		}
	}
	
	@Override
	public void updateQuestion(Question question) throws ApsSystemException {
		try {
			this.getQuestionDAO().updateQuestion(question);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "updateQuestion");
			throw new ApsSystemException("Error while updating a question", t);
		}
	}
	
	@Override
	public void deleteQuestionBySurveyId(int id) throws ApsSystemException {
		try {
			this.getQuestionDAO().deleteQuestionBySurveyId(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteQuestionBySurveyId");
			throw new ApsSystemException("Error while deleting the questions of a survey", t);
		}
	}
	
	@Override
	public void updateSurvey(Survey survey) throws ApsSystemException {
		try {
			this.getSurveyDAO().updateSurvey(survey);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "updateSurvey");
			throw new ApsSystemException("Error while deleting the questions of a survey", t);
		}
	}
	
	@Override
	public void swapQuestionPosition(int id, boolean isUp) throws ApsSystemException{
		try {
			this.getQuestionDAO().swapQuestionPosition(id, isUp);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "swapQuestionPosition");
			throw new ApsSystemException("Error while swapping two questions of a survey", t);
		}
	}
	
	@Override
	public void swapChoicePosition(int id, boolean isUp) throws ApsSystemException {
		try {
			this.getChoiceDAO().swapChoicePosition(id, isUp);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "swapChoicePosition");
			throw new ApsSystemException("Error while swapping two choices in a question", t);
		}
	}
	
	public void saveQuestionInSortedPosition(Question question) throws ApsSystemException {
		try {
			this.getQuestionDAO().saveQuestionInSortedPosition(question);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveQuestionInSortedPosition");
			throw new ApsSystemException("Error while saving a question in a sorted position", t);
		}
	}
	
	@Override
	public List<Integer> searchSurvey(Integer id, String description,
			Collection<String> groups, Boolean isActive,
			Boolean isQuestionnaire, String title, Boolean isPublic)
			throws ApsSystemException {
		List<Integer> result = new ArrayList<Integer>();
		try {
			result = this.getSurveyDAO().searchSurvey(id, description, groups, isActive, 
					isQuestionnaire, title, isPublic);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "searchSurvey");
			throw new ApsSystemException("Error while serching surveys", t);
		}
		return result;
	}
	
	public List<Integer> getActiveSurveyByUser(UserDetails userdetails, Boolean isQuestionnaire, Boolean archive) throws ApsSystemException {
		if (null == userdetails) return null;
		Set<String> groups = new HashSet<String>();
		groups.add(Group.FREE_GROUP_NAME);
		List<Group> userGroups = this.getAuthorizationManager().getGroupsOfUser(userdetails);
		for (int i = 0; i < userGroups.size(); i++) {
			groups.add(userGroups.get(i).getName());
		}
		if (groups.contains(Group.ADMINS_GROUP_NAME)) groups = null;
		List<Integer> result = new ArrayList<Integer>();
		try {
			List<Integer> list = this.getSurveyDAO().searchSurvey(null, null, groups, true, isQuestionnaire, null, null);
			if (null != list) {
				Iterator<Integer> itr = list.iterator();
				while (itr.hasNext()) {
					Integer currentId = itr.next();
					SurveyRecord survey = this.getSurveyDAO().loadSurvey(currentId);
					if ((survey.isArchive() && archive) || (survey.isOpen() && !archive)) {
						result.add(currentId);
					}
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getSurveysByUser");
			throw new ApsSystemException("Errore loading the survey list available for user " + userdetails.getUsername(), t);
		}
		return result;
	}
	
	@Override
	public List<Integer> getActiveSurveyList() throws ApsSystemException {
		List<Integer> list = null;
		try {
			list = this.getSurveyDAO().searchSurvey(null, null, null, true, null, null, null);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getActiveSurveyIds");
			throw new ApsSystemException("Error loading the active surveys list", t);
		}
		return list;
	}
	
	@Override
	public List<Integer> getSurveyList() throws ApsSystemException {
		List<Integer> list = null;
		try {
			list = this.getSurveyDAO().searchSurvey(null, null, null, null, null, null, null);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getActiveSurveyIds");
			throw new ApsSystemException("Error loading the active surveys list", t);
		}
		return list;
	}
	
	@Override
	public List getGroupUtilizers(String groupName) throws ApsSystemException {
		List<Survey> surveys = null;
		try {
			Collection<String> groups = new ArrayList<String>();
			groups.add(groupName);
			List<Integer> surveyIds = this.getSurveyDAO().searchSurvey(null, null, groups, null, null, null, null);
			if (null == surveyIds || surveyIds.size() == 0) return null;
			surveys = new ArrayList<Survey>(surveyIds.size());
			for (int i = 0; i < surveyIds.size(); i++) {
				Integer id = surveyIds.get(i);
				surveys.add(this.loadSurvey(id));
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getGroupUtilizers");
			throw new ApsSystemException("Error loading surveys by group " + groupName, t);
		}
		return surveys;
	}
	
	public void setSurveyDAO(ISurveyDAO surveyDAO) {
		this._surveyDAO = surveyDAO;
	}
	protected ISurveyDAO getSurveyDAO() {
		return _surveyDAO;
	}

	public void setQuestionDAO(IQuestionDAO questionDAO) {
		this._questionDAO = questionDAO;
	}
	protected IQuestionDAO getQuestionDAO() {
		return _questionDAO;
	}

	public void setChoiceDAO(IChoiceDAO choiceDAO) {
		this._choiceDAO = choiceDAO;
	}
	protected IChoiceDAO getChoiceDAO() {
		return _choiceDAO;
	}

	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}
	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}

	public void setResponseDAO(IResponseDAO responseDAO) {
		this._responseDAO = responseDAO;
	}
	protected IResponseDAO getResponseDAO() {
		return _responseDAO;
	}
	
	public void setVoterDAO(IVoterDAO voterDAO) {
		this._voterDAO = voterDAO;
	}
	protected IVoterDAO getVoterDAO() {
		return _voterDAO;
	}
	
	private ISurveyDAO _surveyDAO;
	private IQuestionDAO _questionDAO;
	private IChoiceDAO _choiceDAO;
	private IResponseDAO _responseDAO;
	private IVoterDAO _voterDAO;
	private IAuthorizationManager _authorizationManager;
	
}
