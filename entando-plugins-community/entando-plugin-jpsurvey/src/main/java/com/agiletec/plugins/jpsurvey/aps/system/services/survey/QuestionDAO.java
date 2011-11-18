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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpsurvey.aps.system.services.AbstractSurveyDAO;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Choice;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Question;

/**
 * This DAO class allow to select individual 'question's of the survey
 * @author M.E. Minnai
 */
public class QuestionDAO extends AbstractSurveyDAO implements IQuestionDAO{
	
	@Override
	public Question loadQuestion(int id) {
		Question question = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_COMPLETE_QUESTION_BY_ID);
			stat.setInt(1, id);
			res = stat.executeQuery();
			while (res.next()) {
				if (null == question) {
					question = this.buildQuestionRecordFromResultSet(res, 1);
				}
				// get extra info: questions need the survey type
				int questionnaireValue = res.getInt(13);
				boolean questionnaire = questionnaireValue == 1 ? true:false;
				ApsProperties titles = new ApsProperties();
				titles.loadFromXml(res.getString(14));
				question.setExtraInfo(questionnaire, titles);
				Choice choice = this.buildChoiceRecordFromResultSet(res, 8);
				if (null == choice) continue;
				choice.setExtraInfo(question.getSurveyId(), question.isQuestionnaire(), question.getSurveyTitles(), question.getQuestions());
				if (null == question.getChoice(choice.getId())) {
					question.getChoices().add(choice);
				}
			}
		} catch (Throwable t) {
			processDaoException(t, "Error while loading the question of ID "+id, "loadQuestion");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return question;
	}

	@Override
	public List<Choice> getQuestionChoices(int id) {
		List<Choice> list = new ArrayList<Choice>();
		try {
			Question question = this.loadQuestion(id);
			if (null != question) list = question.getChoices();
		} catch (Throwable t) {
			processDaoException(t, "Error while loading the choices belonging to the question of ID "+id, "loadQuestion");
		}
		return list;
	}
	
	@Override
	public void saveQuestion(Question question) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.saveQuestion(conn, question);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error while saving the question", "saveQuestion");
		} finally {
			this.refreshExtraInfo(question);
			closeConnection(conn);
		}
	}
	
	@Override
	public void deleteQuestion(int id) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteQuestion(conn, id);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error while deleting the question", "deleteQuestion");
		} finally {
			closeConnection(conn);
		}
	}
	
	@Override
	public void updateQuestion(Question question) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.updateQuestion(conn, question);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error while updating a 'question'", "deleteQuestion");
		} finally {
			this.refreshExtraInfo(question);
			closeConnection(conn);
		}
	}
	
	@Override
	public void deleteQuestionBySurveyId(int id) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteQuestionBySurveyId(conn, id);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error while updating a question in the database", "deleteQuestionsBySurveyId");
		} finally {
			closeConnection(conn);
		}
	}
	
	@Override
	public void swapQuestionPosition(int id, boolean isUp) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		Question targetQuestion = null; 
		Question proxQuestion = null;
		int tmp;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			targetQuestion = this.loadQuestion(id);
			if (null == targetQuestion) return;
			if (isUp) {
				stat = conn.prepareStatement(GET_QUESTION_LESSER_THAN);
			} else {
				stat = conn.prepareStatement(GET_QUESTION_GREATER_THAN);
			}
			stat.setInt(1, targetQuestion.getSurveyId());
			stat.setInt(2, targetQuestion.getPos());
			res = stat.executeQuery();
			// we are interested only in the first result!
			if (res.next()) {
				ApsProperties prop = new ApsProperties();
				proxQuestion = new Question();
				proxQuestion.setId(res.getInt(1)); // 1
				proxQuestion.setSurveyId(res.getInt(2)); // 2
				prop.loadFromXml(res.getString(3)); // 2
				proxQuestion.setQuestions(prop);
				proxQuestion.setPos(res.getInt(4)); // 4
				proxQuestion.setSingleChoice(res.getInt(5)==1); // 5
				proxQuestion.setMinResponseNumber(res.getInt(6)); // 6
				proxQuestion.setMaxResponseNumber(res.getInt(7)); // 7
			} else {
				return;
			}
			// swap positions
			tmp = targetQuestion.getPos();
			targetQuestion.setPos(proxQuestion.getPos());
			proxQuestion.setPos(tmp);
			this.updateQuestion(conn, targetQuestion);
			this.updateQuestion(conn, proxQuestion);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error while swapping the position of two questions", "changeQuestionPosition");
		} finally {
			closeDaoResources(res, stat, conn);
		}
	}
	
	@Override
	public void saveQuestionInSortedPosition(Question question) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(GET_QUESTION_GREATER_POS);
			stat.setInt(1, question.getSurveyId());
			res = stat.executeQuery();
			if (res.next()) {
				int lastPosition = res.getInt(1);
				question.setPos(++lastPosition);
			} else {
				question.setPos(0);
			}
			this.saveQuestion(conn, question);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error while saving a question in a sorted position", "saveQuestionInSortedPosition");
		} finally {
			closeDaoResources(res, stat, conn);
		}
	}
	
	private static final String GET_COMPLETE_QUESTION_BY_ID =
		"SELECT " +
		// questions 1 - 7
			"jpsurvey_questions.id, surveyid, question, jpsurvey_questions.pos, singlechoice, minresponsenumber, maxresponsenumber, "+
		// choices 8 - 12
			"jpsurvey_choices.id, questionid, choice, jpsurvey_choices.pos, freetext, " +
		// extra info 13 - 14
			"jpsurvey.questionnaire, jpsurvey.title " +
		" FROM jpsurvey " +
			" LEFT JOIN jpsurvey_questions ON jpsurvey.id = jpsurvey_questions.surveyid " +
			" LEFT JOIN jpsurvey_choices ON jpsurvey_questions.id = jpsurvey_choices.questionid " +
		"WHERE jpsurvey_questions.id= ? ORDER BY jpsurvey_questions.pos, jpsurvey_choices.pos ";
	
	private static final String GET_QUESTION_LESSER_THAN =
		"SELECT id,surveyid,question,pos,singlechoice,minresponsenumber,maxresponsenumber " +
		"FROM jpsurvey_questions WHERE surveyid = ? AND pos < ? ORDER BY pos DESC";
	
	private static final String GET_QUESTION_GREATER_THAN =
		"SELECT id,surveyid,question,pos,singlechoice,minresponsenumber,maxresponsenumber " +
		"FROM jpsurvey_questions WHERE surveyid = ? AND pos > ? ORDER BY pos ASC";
	
	private static final String GET_QUESTION_GREATER_POS =
		"SELECT pos FROM jpsurvey_questions WHERE surveyid = ? ORDER BY pos DESC";
}
