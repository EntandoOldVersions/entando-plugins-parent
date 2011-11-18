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
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpsurvey.aps.system.services.AbstractSurveyDAO;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Choice;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Question;
import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Survey;

/**
 * This is the DAO class for the survey objects
 * @author M.E. Minnai
 */
public class SurveyDAO extends AbstractSurveyDAO implements ISurveyDAO {

	/**
	 * This method loads a complete survey. 'Complete' here means all the elements found in the database
	 * for the survey with the given ID, so check your assumptions because 'complete' is neither 'correct'
	 * nor 'logically complete'. The survey is build sorting the position of the question and then the position
	 * of the choices.
	 * @param id the ID of the survey to load
	 * @return the complete survey, null otherwise 
	 */
	public Survey loadSurvey(int id) {
		Connection conn = null;
		Survey survey = null;
		try {
			conn = this.getConnection();
			survey = this.loadSurvey(conn, id);
		} catch (Throwable t) {
			processDaoException(t, "Error while loading the survey", "loadSurvey");
		} finally {
			closeConnection(conn);
		}
		return survey;
	}
	
	private Survey loadSurvey(Connection conn, int id) {
		PreparedStatement stat = null;
		ResultSet res = null;
		Survey survey = null;
		try {
			stat = conn.prepareStatement(GET_COMPLETE_SURVEY_BY_ID);
			stat.setInt(1, id);
			res = stat.executeQuery();
			while (res.next()) {
				// FIXME usare le costanti per lo shift?
				if (null == survey) {
					survey = this.buildSurveyRecordFromResultSet(res);
				}
				Question question = this.buildQuestionRecordFromResultSet(res, 17);
				if (null == question) continue;
				if (null == survey.getQuestion(question.getId())) {
					survey.getQuestions().add(question);
//					System.out.println("loading question ID "+question.getId()+ " for survey ID "+survey.getId());
				}
				Choice choice = this.buildChoiceRecordFromResultSet(res, 24);
				if (null == choice) continue;
				if (null == survey.getQuestion(question.getId()).getChoice(choice.getId())) {
					survey.getQuestion(question.getId()).getChoices().add(choice);
//					System.out.println("loading choice ID "+choice.getId()+" for question ID "+question.getId());
				}
			}
		} catch (Throwable t) {
			processDaoException(t, "Error while loading the survey ID "+id, "loadSurvey");
		} finally {
			closeDaoResources(res, stat);
		}
		return survey;
	}
	
	/**
	 * build a survey object from the record returned by the database.
	 * @param res the result set containing to process
	 * @param survey the resulting survey object or null if errors occur
	 */
	private Survey buildSurveyRecordFromResultSet(ResultSet res) {
		Survey survey = null;
		if (null == res) return null;
		try {
			int id = res.getInt(1);
			if (id > 0) {
				survey = new Survey();
				survey.setQuestions(new ArrayList<Question>());
				survey.setId(id);
				ApsProperties prop = new ApsProperties();
				prop.loadFromXml(res.getString(2));
				survey.setDescriptions(prop);
				survey.setGroupName(res.getString(3).trim());
				survey.setStartDate(res.getDate(4));
				survey.setEndDate(res.getDate(5));
				survey.setActive(res.getBoolean(6));
				survey.setPublicPartialResult(res.getBoolean(7));
				survey.setPublicResult(res.getBoolean(8));
				survey.setQuestionnaire(res.getBoolean(9));
				survey.setGatherUserInfo(res.getBoolean(10));
				prop = new ApsProperties();
				prop.loadFromXml(res.getString(11));
				survey.setTitles(prop);
				survey.setRestricted(res.getBoolean(12));
				survey.setCheckCookie(res.getBoolean(13));
				survey.setCheckIpAddress(res.getBoolean(14));
				survey.setImageId(res.getString(15));
				prop = new ApsProperties();
				prop.loadFromXml(res.getString(16));
				survey.setImageDescriptions(prop);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "buildSurveyRecordFromResultSet", 
			"Error while building a 'survey' object from the result set");
		}
		return survey;
	}
	
	public void saveSurvey(Survey survey) {
		Connection conn = null;
		PreparedStatement stat = null;
		String NEXT_ID = "SELECT MAX(id) FROM jpsurvey";
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			// SAVE SURVEY
			int selfGeneratedId = this.getUniqueId( NEXT_ID, conn);
			stat = conn.prepareStatement(SAVE_SURVEY);
			survey.setId(selfGeneratedId);
			stat.setInt(1, survey.getId()); //1
			stat.setString(2, survey.getDescriptions().toXml()); //2
			stat.setString(3, survey.getGroupName()); //3
			stat.setDate(4, new java.sql.Date(survey.getStartDate().getTime())); //4
			Date date = survey.getEndDate();
			if (null != date) {
				stat.setDate(5, new java.sql.Date(survey.getEndDate().getTime())); //5
			} else {
				stat.setNull(5, java.sql.Types.DATE); //5
			}
			if (survey.isActive()) {
				stat.setInt(6, 1); //6
			} else {
				stat.setInt(6, 0); //6
			}
			if (survey.isPublicPartialResult()) {
				stat.setInt(7, 1); //7
			} else {
				stat.setInt(7, 0); //7
			}
			if (survey.isPublicResult()) {
				stat.setInt(8, 1); //8
			} else {
				stat.setInt(8, 0); //8
			}
			if (survey.isQuestionnaire()) {
				stat.setInt(9, 1); //9
			} else {
				stat.setInt(9, 0); //9
			}
			if (survey.isGatherUserInfo()) {
				stat.setInt(10, 1); //10
			} else {
				stat.setInt(10, 0); //10
			}
			stat.setString(11, survey.getTitles().toXml()); //11
			if (survey.isRestricted()) {
				stat.setInt(12, 1); //12
			} else {
				stat.setInt(12, 0); //12
			}
			if (survey.isCheckCookie()) {
				stat.setInt(13, 1); //13
			} else {
				stat.setInt(13, 0); //13
			}
			if (survey.isCheckIpAddress()) {
				stat.setInt(14, 1); //14
			} else {
				stat.setInt(14, 0); //14
			}
			if (null != survey.getImageId() && survey.getImageId().length() > 0) {
				stat.setString(15, survey.getImageId()); // 15
			} else {
				stat.setNull(15, java.sql.Types.VARCHAR); // 15
			}
			if (null != survey.getImageDescriptions() && !survey.getImageDescriptions().isEmpty()) {
				stat.setString(16, survey.getImageDescriptions().toXml()); //16
			} else {
				stat.setNull(16, java.sql.Types.VARCHAR); // 16
			}
			stat.executeUpdate();
			// SAVE QUESTIONS
			if (null != survey.getQuestions() && !survey.getQuestions().isEmpty()) {
				Iterator<Question> itr = survey.getQuestions().iterator();
				while (itr.hasNext()) {
					Question currentQuestion = itr.next();
					currentQuestion.setSurveyId(survey.getId());
					this.saveQuestion(conn, currentQuestion);
				}
			}
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error while saving the survey loadSurvey", "saveSurvey");
		} finally {
			this.refreshExtraInfo(survey);
			closeDaoResources(null, stat, conn);
		}
	}
	
	public void deleteSurvey(int id) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			Survey survey = this.loadSurvey(id);
			if (null != survey) {
				// CANCELLA LE DOMANDE
				for (Question question: survey.getQuestions()) {
					this.deleteQuestion(conn, question.getId());
				}
				// CANCELLA IL SONDAGGIO
				stat = conn.prepareStatement(DELETE_SURVEY_BY_ID);
				stat.setInt(1, id);
				stat.execute();
			}
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error while deleting the survey", "deleteSurvey");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	public void updateSurvey(Survey survey) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_SURVEY_BY_ID);
			stat.setString(1, survey.getDescriptions().toXml()); // 1
			stat.setString(2, survey.getGroupName()); // 2
			stat.setDate(3, new java.sql.Date(survey.getStartDate().getTime())); // 3
			if (null == survey.getEndDate()) {
				stat.setNull(4, java.sql.Types.DATE); // 4
			} else {
				stat.setDate(4, new java.sql.Date(survey.getEndDate().getTime())); // 4
			}
			if (survey.isActive()) {
				stat.setInt(5, 1); // 5
			} else {
				stat.setInt(5, 0); // 5
			}
			if (survey.isPublicPartialResult()) {
				stat.setInt(6, 1); // 6
			} else {
				stat.setInt(6, 0); // 6
			}
			if (survey.isPublicResult()) {
				stat.setInt(7, 1); // 7
			} else {
				stat.setInt(7, 0); // 7
			}
			if (survey.isQuestionnaire()) {
				stat.setInt(8, 1); // 8
			} else {
				stat.setInt(8, 0); // 8
			}
			if (survey.isGatherUserInfo()) {
				stat.setInt(9, 1); // 9
			} else {
				stat.setInt(9, 0); // 9
			}
			stat.setString(10, survey.getTitles().toXml()); // 10
			if (survey.isRestricted())  {
				stat.setInt(11, 1); // 11
			} else {
				stat.setInt(11, 0); // 11
			}
			
			if (survey.isCheckCookie()) {
				stat.setInt(12, 1); //12
			} else {
				stat.setInt(12, 0); //12
			}
			if (survey.isCheckIpAddress()) {
				stat.setInt(13, 1); //13
			} else {
				stat.setInt(13, 0); //13
			}
			if (null != survey.getImageId() && survey.getImageId().length() > 0) {
				stat.setString(14, survey.getImageId()); //14
			} else {
				stat.setNull(14, java.sql.Types.VARCHAR); //14
			}
			if (null != survey.getImageDescriptions() && !survey.getImageDescriptions().isEmpty()) {
				stat.setString(15, survey.getImageDescriptions().toXml()); //15
			} else {
				stat.setNull(15, java.sql.Types.VARCHAR); // 15
			}
			stat.setInt(16, survey.getId());
			stat.executeUpdate();
			// UPDATE QUESTIONS
			Set<Integer> kept = this.deleteQuestionsInExcess(conn, survey);
			for (Question currentQuestion: survey.getQuestions()) {
				if (kept.contains(currentQuestion.getId())) {
					this.updateQuestion(conn, currentQuestion);
				} else {
					currentQuestion.setSurveyId(survey.getId()); // for sake of safety
					this.saveQuestion(conn, currentQuestion);
				}
			}
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			this.processDaoException(t, "Error while updating the survey", "updateSurvey");
		} finally {
			this.refreshExtraInfo(survey);
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public List<Integer> searchSurvey(Integer id, String description, Collection<String> groups, Boolean isActive, Boolean isQuestionnaire, String title, Boolean isPublic) {
		List<Integer> list = new ArrayList<Integer>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			int idx=1;
			conn = this.getConnection();
			String query = this.createSearchIdsQueryString(id, description, groups, isActive,isQuestionnaire, title, isPublic);
			stat = conn.prepareStatement(query);
			if (null != id) {
				stat.setInt(idx++, id);
			}
			if (null != description && description.length() > 0) {
				stat.setString(idx++, "%"+description+"%");
			}
			if (null != groups && groups.size() > 0) {
				Iterator<String> iter = groups.iterator();
				while (iter.hasNext()) {
					String code = iter.next();
					stat.setString(idx++, code);
				}
			}
			if (null != title && title.length () > 0) {
				stat.setString(idx++, "%"+title+"%");
			}
			res = stat.executeQuery();
			while (res.next()) {
				Integer currentId= new Integer(res.getInt(1));
				// if the search includes part of the title and / or the description we must perform extra checks
				if ((null != description && description.trim().length () > 0) || 
						(null != title && title.trim().length () > 0)) {
					Survey survey = this.loadSurvey(conn, currentId);
					if ((null != description && description.trim().length() > 0)) {
						if (this.searchInProperties(description, survey.getDescriptions())) {
							list.add(currentId);
							continue;
						}
					}
					if ((null != title && title.trim().length() > 0)) {
						if (this.searchInProperties(title, survey.getTitles()))
							list.add(currentId);
					}
				} else {
					// always add if no description or title description are given
					list.add(currentId);
				}
			}
		} catch (Throwable t) {
			this.processDaoException(t, "Error while searching for surveys", "searchSurvey");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return list;
	}
	
	@Override
	public List<Question> getSurveyQuestions(int id) {
		Survey survey = null;
		List<Question> list = new ArrayList<Question>();
		try {
			survey = this.loadSurvey(id);
			if (null != survey) list = survey.getQuestions();
		} catch (Throwable t) {
			this.processDaoException(t, "Error getting the questions of a survey", "searchSurveyByIds");
		}
		return list;
	}
	
	private String createSearchIdsQueryString(Integer id, String description, Collection<String> groups, Boolean isActive, Boolean isQuestionnaire, String title, Boolean isRestricted) {
		StringBuffer query = new StringBuffer("SELECT id FROM jpsurvey ");
		boolean isWherePresent = false;
		if (null != id) {
			if (!isWherePresent) {
				isWherePresent=true;
				query.append(" WHERE ");
			}
			query.append(" id = ? ");
		}
		if (null != description && description.length () > 0) {
			if (!isWherePresent) {
				isWherePresent=true;
				query.append(" WHERE ");
			} else {
				query.append(" AND ");
			}
			query.append(" LOWER(description) LIKE LOWER(?) ");
		}
		if (null != groups && groups.size() > 0) {
			for (int i = 0; i < groups.size(); i++) {
				if (i == 0) {
					if (!isWherePresent) {
						isWherePresent = true;
						query.append(" WHERE ( ");
					} else {
						query.append(" AND ( ");
					}
				} else {
					query.append(" OR ");
				}
				query.append(" LOWER(maingroup) = LOWER(?) ");
				if (i == (groups.size()-1)) {
					query.append(" ) ");
				}
			}
		}
		if (null != isActive) {
			if (!isWherePresent) {
				isWherePresent=true;
				query.append(" WHERE ");
			} else {
				query.append(" AND ");
			}
			if (isActive) {
				query.append(" active > 0 ");
			} else {
				query.append(" active = 0 ");
			}
		}
		if (null != isQuestionnaire) {
			if (!isWherePresent) {
				isWherePresent=true;
				query.append(" WHERE ");
			} else {
				query.append(" AND ");
			}
			if (isQuestionnaire) {
				query.append(" questionnaire > 0 ");
			} else {
				query.append(" questionnaire = 0 ");
			}
		}
		if (null != title && title.length () > 0) {
			if (!isWherePresent) {
				isWherePresent = true;
				query.append(" WHERE ");
			} else {
				query.append(" AND ");
			}
			query.append(" LOWER(title) LIKE LOWER(?) ");
		}
		if (null != isRestricted) {
			if (!isWherePresent) {
				isWherePresent=true;
				query.append(" WHERE ");
			} else {
				query.append(" AND ");
			}
			if (isRestricted) {
				query.append(" restrictedaccess > 0 ");
			} else {
				query.append(" restrictedaccess = 0 ");
			}
		}
		query.append(" ORDER BY startdate DESC");
		return query.toString();
	}
	
	private boolean searchInProperties(String string, ApsProperties props) {
		for (Enumeration enu = props.keys(); enu.hasMoreElements(); ) {
			String key = (String) enu.nextElement();
			String value = props.getProperty(key);
			if (value.toLowerCase().contains(string.toLowerCase())) return true;
		}
		return false;
	}
	
	private static final String GET_COMPLETE_SURVEY_BY_ID = 
		"SELECT " +
			// survey: 1 - 16
			"jpsurvey.id,description, maingroup, startdate, enddate, active, publicpartialresult, publicresult, questionnaire, gatheruserinfo, title, restrictedaccess, checkcookie, checkipaddress, imageid, imagedescr, "+
			// questions: 17 - 23 
			"jpsurvey_questions.id, surveyid, question, jpsurvey_questions.pos, singlechoice, minresponsenumber, maxresponsenumber, " +
			// choices: 24 - 28
			"jpsurvey_choices.id, questionid, choice, jpsurvey_choices.pos, freetext " +
		"FROM jpsurvey "+
			"LEFT JOIN jpsurvey_questions ON jpsurvey.id = jpsurvey_questions.surveyid "+
			"LEFT JOIN jpsurvey_choices ON jpsurvey_questions.id = jpsurvey_choices.questionid "+
		"WHERE jpsurvey.id= ? ORDER BY jpsurvey_questions.pos, jpsurvey_choices.pos";
	
	private static final String SAVE_SURVEY =
		"INSERT INTO " +
		"jpsurvey " +
		"	(id, description, maingroup, startdate, enddate, active, publicpartialresult, publicresult, " +
		"	questionnaire, gatheruserinfo, title, restrictedaccess, " +
		"	checkcookie, checkipaddress, imageid, imagedescr) " +
		"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final String UPDATE_SURVEY_BY_ID = 
		"UPDATE jpsurvey SET description = ? , maingroup = ? , startdate = ? , enddate = ? , active = ? , " +
		"publicpartialresult = ?, publicresult = ?, questionnaire = ?, gatheruserinfo = ? , title = ? , restrictedaccess = ? , checkcookie = ? , checkipaddress = ?, imageid = ?, imagedescr = ? WHERE  id = ? ";
	
	private static final String DELETE_SURVEY_BY_ID = 
		"DELETE FROM jpsurvey WHERE id = ? ";
}
