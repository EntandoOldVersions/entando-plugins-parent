/*
 *
 * Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
 * Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 */
package com.agiletec.plugins.jpsurvey.aps.system.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Choice;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Question;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;

public class AbstractSurveyDAO extends AbstractDAO {

	/**
	 * This inspect the given table and return the id to be used as primary key for further operations
	 * @param query the query used to inspect the datasource
	 * @param conn the connection to the datasource
	 * @return The first free id to use as primary key
	 */
	protected int getUniqueId(String query, Connection conn) {
		int id = 0;
		Statement stat = null;
		ResultSet res = null;
		try {
			stat = conn.createStatement();
			res = stat.executeQuery(query);
			res.next();
			id = res.getInt(1) + 1;
		} catch (Throwable t) {
			processDaoException(t, "Error while getting last used ID - '" + query + "'", "getUniqueId");
		} finally {
			closeDaoResources(res, stat);
		}
		return id;
	}
	
	/**
	 * Create a 'question' object from the result set. Note that this method expects a row containing all
	 * the columns describing the question. Invoked from several DAOs.
	 * @param res the result set containing the record of the question
	 * @param shift The number of the first column containing the data of the questions within the result set.
	 * @return the 'question' object requested
	 */
	protected Question buildQuestionRecordFromResultSet(ResultSet res, int shift) {
		Question question = null;
		if (null == res) return null;
		if (shift > 0) --shift;
		try {
			int id = res.getInt(shift + 1);
			if (id > 0) {
				question = new Question();
				question.setChoices(new ArrayList<Choice>());
				question.setId(id);
				question.setSurveyId(res.getInt(shift + 2));
				ApsProperties prop = new ApsProperties();
				prop.loadFromXml(res.getString(shift + 3));
				question.setQuestions(prop);
				question.setPos(res.getInt(shift + 4));
				question.setSingleChoice(res.getBoolean(shift + 5));
				question.setMinResponseNumber(res.getInt(shift + 6));
				question.setMaxResponseNumber(res.getInt(shift + 7));
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "buildQuestionRecordFromResultSet", 
					"Error while building a 'question' object from the result set");
		}
		return question;
	}
	
	/**
	 * Create a 'choice' record object from the result set. Note that this method expects a row containing all
	 * the columns describing the choice. 
	 * @param res The result set as returned from the database
	 * @param shift The shift to the 'choice' object data in the result set
	 * @return the 'choice' record requested
	 */
	protected Choice buildChoiceRecordFromResultSet(ResultSet res, int shift) {
		Choice choice = null;
		if (null == res) return null;
		if (shift > 0) --shift;
		try {
			int id = res.getInt(shift + 1);
			if (id > 0) {
				choice = new Choice();
				choice.setId(id);
				choice.setQuestionId(res.getInt(shift + 2));
				ApsProperties prop = new ApsProperties();
				prop.loadFromXml(res.getString(shift + 3));
				choice.setChoices(prop);
				choice.setPos(res.getInt(shift + 4));
				choice.setFreeText(res.getBoolean(shift + 5));
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "buildChoiceRecordFromResultSet", 
			"Error while building a 'choice' object from the result set");
		}
		return choice;
	}
	
	/**
	 * This saves a choice in the database tables. Since this method is invoked in several DAOs it's been
	 * inserted in the common action.
	 * @param conn An opened connection to the database 
	 * @param choice The choice object to store
	 * @param sql the SQL statement used to record the choice
	 */
	protected void saveChoice(Connection conn, Choice choice) {
		PreparedStatement stat = null;
		try {
			int choiceId = this.getUniqueId("SELECT MAX(id) FROM jpsurvey_choices ", conn);
			stat = conn.prepareStatement(SAVE_CHOICE);
			choice.setId(choiceId);
			stat.setInt(1, choice.getId());
			stat.setInt(2, choice.getQuestionId());
			stat.setString(3, choice.getChoices().toXml());
			stat.setInt(4, choice.getPos());
			if (choice.isFreeText()) {
				stat.setInt(5, 1);
			} else {
				stat.setInt(5, 0);
			}
			stat.executeUpdate();
		} catch (Throwable t) {
			processDaoException(t, "Error while saving 'choice' ", "saveChoice");
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	protected void deleteChoice(Connection conn, int id) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_CHOICE_BY_ID);
			stat.setInt(1, id);
			stat.execute();
		} catch (Throwable t) {
			processDaoException(t, "Error while deleting the 'choice'", "deleteChoice");
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	/**
	 * This saves a question in the database tables. Since this method is invoked in several DAOs it's been
	 * inserted in the common action.
	 * @param question The question object to save
	 * @param conn An opened connection to the database 
	 * @param sql the SQL statement used to record the question
	 */
	protected void saveQuestion(Connection conn, Question question) {
		PreparedStatement stat = null;
		try {
			int questionId = this.getUniqueId("SELECT MAX(id) FROM jpsurvey_questions ", conn);
			// SAVE QUESTION ITSELF
			stat = conn.prepareStatement(SAVE_QUESTION);
			question.setId(questionId);
			stat.setInt(1, question.getId());
			stat.setInt(2, question.getSurveyId());
			stat.setString(3, question.getQuestions().toXml());
			stat.setInt(4, question.getPos());
			if (question.isSingleChoice()) {
				stat.setInt(5, 1);
			} else {
				stat.setInt(5, 0);
			}
			stat.setInt(6, question.getMinResponseNumber());
			stat.setInt(7, question.getMaxResponseNumber());
			stat.executeUpdate();
			// SAVE CHOICES
			if (null != question.getChoices() && question.getChoices().size() > 0) {
				for (Choice currentchoice: question.getChoices()) {
					currentchoice.setExtraInfo(question.getSurveyId(), null, null, question.getQuestions());
					currentchoice.setQuestionId(question.getId());
					this.saveChoice(conn, currentchoice);
				}
			}
		} catch (Throwable t) {
			processDaoException(t, "Error while saving the question", "saveQuestion");
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	/**
	 * Delete a question and the related choices, if any
	 * @param conn the connection to the database
	 * @param id The id of the question to delete
	 */
	protected void deleteQuestion(Connection conn, int id) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_CHOICE_BY_QUESTIONID);
			stat.setInt(1, id);
			stat.execute();
			// FIXME delete response here or let the action drive the deletion process?
			stat = conn.prepareStatement(DELETE_QUESTION_BY_ID);
			stat.setInt(1, id);
			stat.execute();
		} catch (Throwable t) {
			processDaoException(t, "Error while deleting the question", "deleteQuestion");
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	/**
	 * Updates the given choice 
	 * @param conn The connection to the database
	 * @param choice The object to save
	 */
	protected void updateChoice(Connection conn, Choice choice) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(UPDATE_CHOICE);
			stat.setInt(1, choice.getQuestionId());
			stat.setString(2, choice.getChoices().toXml());
			stat.setInt(3, choice.getPos());
			if (choice.isFreeText()) {
				stat.setInt(4, 1);
			} else {
				stat.setInt(4, 0);
			}
			stat.setInt(5, choice.getId());
			stat.executeUpdate();
		} catch (Throwable t) {
			processDaoException(t, "Error while updating a 'choice' record", "updateChoice");
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	/**
	 * This updates the question and saves the related choice as NEW elements in the database
	 * @param conn The connection to the database
	 * @param question The question object to save
	 */
	protected void updateQuestion(Connection conn, Question question) {
		PreparedStatement stat=null;
		try {
			stat = conn.prepareStatement(UPDATE_QUESTION);
			stat.setInt(1, question.getSurveyId());
			stat.setString(2, question.getQuestions().toXml());
			stat.setInt(3, question.getPos());
			if (question.isSingleChoice()) {
				stat.setInt(4, 1);
			} else {
				stat.setInt(4, 0);
			}
			stat.setInt(5, question.getMinResponseNumber());
			stat.setInt(6, question.getMaxResponseNumber());
			stat.setInt(7, question.getId());
			stat.executeUpdate();
			// delete the choices that don't exist anymore
			Set<Integer> kept = this.deleteChoicesInExcess(conn, question);
			// save the new choices or update the new ones
			for (Choice currentChoice: question.getChoices()) {
				if (kept.contains(currentChoice.getId())) {
					this.updateChoice(conn, currentChoice);
				} else {
					currentChoice.setQuestionId(question.getId()); // for sake of safety
					this.saveChoice(conn, currentChoice);
				}
			}
		} catch (Throwable t) {
			processDaoException(t, "Error while updating a question in the database", "updateQuestion");
		} finally {
			closeDaoResources(null, stat);
		}
		
	}
	
	/**
	 * Delete all the choices belonging to the given question
	 * @param conn An opened connection to the database
	 * @param id The ID of the questions whose choices -if any- are to be deleted
	 */
	protected void deleteChoiceByQuestionId(Connection conn, int id) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_CHOICE_BY_QUESTIONID);
			stat.setInt(1, id);
			stat.execute();
		} catch (Throwable t) {
			processDaoException(t, "Error while updating a question in the database", "deleteChoiceByQuestionId");
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	/**
	 * Delete questions of the given survey
	 * @param conn An opened connection to the database
	 * @param id The id of the survey whose questions are to be deleted
	 */
	protected void deleteQuestionBySurveyId(Connection conn, int id) {
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			stat = conn.prepareStatement(GET_QUESTION_BY_SURVEYID);
			stat.setInt(1, id);
			res = stat.executeQuery();
			while (res.next()) {
				int questionId = res.getInt(1);
				this.deleteQuestion(conn, questionId);
			}
		} catch (Throwable t) {
			processDaoException(t, "Error while updating a question in the database", "deleteQuestionsBySurveyId");
		} finally {
			closeDaoResources(res, stat);
		}
	}
	
	/**
	 * Determine whether the survey is a questionnaire or not. Do not use to check the existence of
	 * a survey!
	 * @param conn The connection to the database
	 * @param surveyId The id of the survey 
	 * @return true if the ID belongs to a questionnaire, false otherwise. 
	 */
	protected boolean isSurveyQuestionnaire(Connection conn, int surveyId) {
		PreparedStatement stat = null;
		ResultSet res = null;
		boolean isQquestionnaire = false;
		try {
			stat = conn.prepareStatement(DETERMINE_SURVEY_TYPE);
			stat.setInt(1, surveyId);
			res = stat.executeQuery();
			while (res.next()) {
				int type = res.getInt(1);
				isQquestionnaire = (type == 1 ? true:false);
			}
		} catch (Throwable t) {
			processDaoException(t, "Errore nell'ottenere il tipo di survey", "loadSurveyType");
		} finally {
			closeDaoResources(res, stat);
		}
		return isQquestionnaire;
	}
	
	/**
	 * This will delete from the database all the questions whose ID is not within the questions
	 * of the given survey. This is invoked ONLY by the update routines and MUST not be used for
	 * purposes other than updating!
	 * @param survey The survey to analyze for questions in excess
	 * @return The set of the ID of the questions which are kept, an empty list otherwise
	 */
	protected Set<Integer> deleteQuestionsInExcess(Connection conn, Survey survey) {
		ResultSet res = null;
		PreparedStatement stat = null;
		Set<Integer> list = new HashSet<Integer>();
		try {
			stat = conn.prepareStatement(GET_QUESTION_BY_SURVEYID);
			stat.setInt(1, survey.getId());
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt(1);
				if (null == survey.getQuestion(id)) {
					this.deleteQuestion(conn, id);
				} else {
					list.add(id);
				}
			}
		} catch (Throwable t) {
			this.processDaoException(t, "error while deleting question in excess from a survey", "deleteQuestionInExcess");
		} finally {
			closeDaoResources(res, stat);
		}
		return list;
	}
	
	/**
	 * This will delete from the database all the choices whose ID is not within the choices
	 * of the given question. This is invoked ONLY by the update routines and MUST not be used for
	 * purposes other than updating!
	 * @param question The question which is going to be updated 
	 * @return The set of the ID of the choices kept, an empty list otherwise
	 */
	private Set<Integer> deleteChoicesInExcess(Connection conn, Question question) {
		ResultSet res = null;
		PreparedStatement stat = null;
		Set<Integer> list = new HashSet<Integer>();
		try {
			stat = conn.prepareStatement(GET_CHOICES_BY_QUESTIONID);
			stat.setInt(1, question.getId());
			res = stat.executeQuery();
			while (res.next()) {
				int id = res.getInt(1);
				if (null == question.getChoice(id)) {
					this.deleteChoice(conn, id);
				} else {
					list.add(id);
				}
			}
		} catch (Throwable t) {
			this.processDaoException(t, "error while deleting question in excess from a survey", "deleteQuestionInExcess");
		} finally {
			closeDaoResources(res, stat);
		}
		return list;
	}
	
	/**
	 * Upon a save or update operation we have to update the extra info for safety reasons.
	 * That means that this method is invoked only by the proper DAO, so don't use it (unless, of course, 
	 * you know what are you doing!)
	 */
	protected void refreshExtraInfo(Question question) {
		if (null != question && question.getChoices() != null && question.getChoices().size() > 0) {
			for (Choice choice: question.getChoices()) {
				choice.setExtraInfo(question.getSurveyId(), null, null, question.getQuestions());
			}
		}
	}
	
	/**
	 * Upon a save or update operation we have to update the extra info for safety reasons.
	 * That means that this method is invoked only by the proper DAO, so don't use it (unless, of course, 
	 * you know what are you doing!)
	 */
	protected void refreshExtraInfo(Survey survey) {
		if (null != survey && survey.getQuestions() != null && survey.getQuestions().size() > 0) {
			for (Question question: survey.getQuestions()) {
				question.setExtraInfo(survey.isQuestionnaire(), survey.getTitles());
				if (null != question && question.getChoices() != null && question.getChoices().size() > 0) {
					for (Choice choice: question.getChoices()) {
						choice.setExtraInfo(question.getSurveyId(), survey.isQuestionnaire(), survey.getTitles(), question.getQuestions());
					}
				}
			}
		} 
	}
	
	private static final String SAVE_CHOICE =
		"INSERT INTO jpsurvey_choices (id,questionid,choice,pos,freetext) VALUES (?,?,?,?,?)";
	
	private static final String SAVE_QUESTION =
		"INSERT INTO jpsurvey_questions (id,surveyid,question,pos,singlechoice,minresponsenumber,maxresponsenumber) VALUES (?,?,?,?,?,?,?)";
	
	private static final String DELETE_CHOICE_BY_ID =
		"DELETE FROM jpsurvey_choices WHERE id = ?";
	
	private static final String DELETE_QUESTION_BY_ID =
		"DELETE FROM jpsurvey_questions WHERE id = ? ";
	
	private static final String DELETE_CHOICE_BY_QUESTIONID =
		"DELETE FROM jpsurvey_choices WHERE questionid = ?";
	
	private static final String UPDATE_CHOICE =
		"UPDATE jpsurvey_choices SET questionid = ?, choice = ?, pos = ?, freetext = ? WHERE id = ?";
	
	private static final String UPDATE_QUESTION =
		"UPDATE jpsurvey_questions SET surveyid = ?, question = ?, pos = ?, singlechoice = ?, minresponsenumber = ?, maxresponsenumber = ?  WHERE id = ? ";
	
	private static final String GET_QUESTION_BY_SURVEYID =
		"SELECT id FROM jpsurvey_questions WHERE surveyid = ? ";
	
	private static final String GET_CHOICES_BY_QUESTIONID =
		"SELECT id FROM jpsurvey_choices WHERE questionid = ? ";
	
	private static final String DETERMINE_SURVEY_TYPE = "SELECT questionnaire FROM jpsurvey WHERE id = ?";
	
}
