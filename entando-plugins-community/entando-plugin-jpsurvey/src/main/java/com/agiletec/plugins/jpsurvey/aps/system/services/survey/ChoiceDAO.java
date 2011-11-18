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

import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpsurvey.aps.system.services.AbstractSurveyDAO;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Choice;

public class ChoiceDAO extends AbstractSurveyDAO implements IChoiceDAO {
	
	@Override
	public Choice loadChoice(int id) {
		Choice choice = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_CHOICE_BY_ID);
			stat.setInt(1, id);
			res = stat.executeQuery();
			if (res.next()) {
				choice = this.buildChoiceRecordFromResultSet(res, 1);
				// process get extra info
				int surveyId = res.getInt(6);
				boolean questionnaire = res.getInt(7) == 1 ? true:false;
				ApsProperties surveyTitle = new ApsProperties();
				surveyTitle.loadFromXml(res.getString(8));
				ApsProperties questionTitle = new ApsProperties();
				questionTitle.loadFromXml(res.getString(9));
				choice.setExtraInfo(surveyId, questionnaire, surveyTitle, questionTitle);
			}
		} catch (Throwable t) {
			processDaoException(t, "Error while loading the choice of ID "+id, "loadChoice");
		} finally {
			closeDaoResources(res, stat, conn);
		}
//		if (null != choice) this.loadChoiceExtraInformations(conn, choice);
		return choice;
	}
	
	@Override
	public void saveChoice(Choice choice) {
		Connection conn = null;
		if (null == choice) return;
		try {
			conn = this.getConnection();
			saveChoice(conn, choice);
		} catch (Throwable t) {
			processDaoException(t, "Error while saving the 'choice' ID "+choice.getId(), "saveChoice");
		} finally {
			closeConnection(conn);
		}
	}
	
	@Override
	public void saveChoiceInSortedPosition(Choice choice) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(GET_CHOICE_GREATER_POS);
			stat.setInt(1, choice.getQuestionId());
			res = stat.executeQuery();
			if (res.next()) {
				int lastPosition = res.getInt(1);
				choice.setPos(++lastPosition);
			} else {
				choice.setPos(0);
			}
			this.saveChoice(conn, choice);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error while saving the choice ID "+choice.getId()+" in the proper position "+choice.getPos(), "saveChoiceInSortedPosition");
		} finally {
			closeDaoResources(res, stat, conn);
		}
	}
	
	@Override
	public void deleteChoice(int id) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			this.deleteChoice(conn,id);
		} catch (Throwable t) {
			processDaoException(t, "Error while deleting the 'choice' ID "+id, "deleteChoice");
		} finally {
			closeConnection(conn);
		}
	}
	
	@Override
	public void deleteChoiceByQuestionId(int id) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			this.deleteChoiceByQuestionId(conn, id);
		} catch (Throwable t) {
			processDaoException(t, "Error while deleting choices by question ID ", "deleteChoiceByQuestionId");
		} finally {
			closeConnection(conn);
		}
	}
	
	@Override
	public void updateChoice(Choice choice) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			this.updateChoice(conn, choice);
		} catch (Throwable t) {
			processDaoException(t, "Error while updating a 'choice' ", "updateChoice");
		} finally {
			closeConnection(conn);
		}
	}
	
	public void swapChoicePosition(int id, boolean isUp) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		Choice targetChoice = null; 
		Choice proxChoice = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			targetChoice = this.loadChoice(id);
			if (null == targetChoice) return;
			if (isUp) {
				stat = conn.prepareStatement(GET_CHOICE_LESSER_THAN);
			} else {
				stat = conn.prepareStatement(GET_CHOICE_GREATER_THAN);
			}
			stat.setInt(1, targetChoice.getQuestionId());
			stat.setInt(2, targetChoice.getPos());
			res = stat.executeQuery();
			// we are interested only in the first result!
			if (res.next()) {
				ApsProperties prop = new ApsProperties();
				proxChoice = new Choice();
				proxChoice.setId(res.getInt(1)); // 1
				proxChoice.setQuestionId(res.getInt(2)); // 2
				prop.loadFromXml(res.getString(3)); // 2
				proxChoice.setChoices(prop);
				proxChoice.setPos(res.getInt(4)); // 4
				proxChoice.setFreeText(res.getInt(5)==1); // 5
			} else {
				return;
			}
			// swap positions
			int tmp = targetChoice.getPos();
			targetChoice.setPos(proxChoice.getPos());
			proxChoice.setPos(tmp);
			this.updateChoice(conn, targetChoice);
			this.updateChoice(conn, proxChoice);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Errore swapping position of two 'choice' objects", "changeQuestionPosition");
		} finally {
			closeDaoResources(res, stat, conn);
		}
	}
	
	private static final String GET_CHOICE_BY_ID = // THE SMART WAY
		"SELECT " +
		// choices 1 - 5
			"jpsurvey_choices.id, questionid, choice, jpsurvey_choices.pos, freetext, " +
		// extra info 6 - 9
			"jpsurvey.id, jpsurvey.questionnaire, jpsurvey.title, jpsurvey_questions.question "+
		"FROM jpsurvey " +
			"LEFT JOIN jpsurvey_questions ON jpsurvey.id = jpsurvey_questions.surveyid "+
			"LEFT JOIN jpsurvey_choices ON jpsurvey_questions.id = jpsurvey_choices.questionid "+
		"WHERE jpsurvey_choices.id = ? ";
	
	private static final String GET_CHOICE_GREATER_POS =
		"SELECT pos FROM jpsurvey_choices WHERE questionid = ? ORDER BY pos DESC";
	
	private static final String GET_CHOICE_LESSER_THAN =
		"SELECT id, questionid, choice, pos, freetext FROM jpsurvey_choices WHERE questionid = ? AND pos < ? ORDER BY pos DESC";

	private static final String GET_CHOICE_GREATER_THAN =
		"SELECT id, questionid, choice, pos, freetext FROM jpsurvey_choices WHERE questionid = ? AND pos > ? ORDER BY pos ASC";

}
