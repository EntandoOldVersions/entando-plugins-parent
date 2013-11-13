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
package com.agiletec.plugins.jpsurvey.aps.system.services.collect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.plugins.jpsurvey.aps.system.services.AbstractSurveyDAO;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.SingleQuestionResponse;

public class ResponseDAO extends AbstractSurveyDAO implements IResponseDAO {
	
	@Override
	public void submitResponses(List<SingleQuestionResponse> responses) {
		Connection conn = null;
		PreparedStatement stat= null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(SAVE_RESPONSE);
			for (int i=0; i<responses.size(); i++) {
				SingleQuestionResponse result = responses.get(i);
				stat.setInt(1, result.getVoterId()); // 1
				stat.setInt(2, result.getQuestionId()); // 2
				stat.setInt(3, result.getChoiceId()); // 3
				if (null!=result.getFreeText()) {
					stat.setString(4, result.getFreeText()); // 4
				} else {
					stat.setNull(4, java.sql.Types.VARCHAR); // 4
				}
				stat.addBatch();
				stat.clearParameters();
			}			
			stat.executeBatch();
			conn.commit();
		} catch (Throwable t) {
			this.processDaoException(t, "Error saving answers", "submitResponses");
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public Map<Integer, Integer> loadQuestionStatistics(Integer questionId) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_QUESTION_STATS);
			stat.setInt(1, questionId);
			res = stat.executeQuery();
			while (res.next()) {
				int choiceId = res.getInt(1);
				int occurrences = res.getInt(2);
				map.put(choiceId, occurrences);
			}
		} catch (Throwable t) {
			processDaoException(t, "loadQuestionStatistics id " + questionId, "loadQuestionStatistics");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return map;
	}
	
	private static final String LOAD_QUESTION_STATS = 
		"SELECT jpsurvey_responses.choiceid, COUNT(jpsurvey_responses.choiceid) " +
		"FROM jpsurvey_responses WHERE jpsurvey_responses.questionid = ? GROUP BY jpsurvey_responses.choiceid";
	
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.aps.system.services.collect.IResponseDAO#saveResponse(com.agiletec.plugins.jpsurvey.aps.system.services.collect.Response)
	 */
	public void submitResponse(SingleQuestionResponse result) {
		Connection conn = null;
		PreparedStatement stat=null;
		try {
			conn = this.getConnection();
			stat=conn.prepareStatement(SAVE_RESPONSE);
			stat.setInt(1, result.getVoterId()); // 1
			stat.setInt(2, result.getQuestionId()); // 2
			stat.setInt(3, result.getChoiceId()); // 3
			if (null!=result.getFreeText()) {
				stat.setString(4, result.getFreeText()); // 4
			}
			stat.executeUpdate();
		} catch (Throwable t) {
			this.processDaoException(t, "Error saving vote", "saveResponse");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.aps.system.services.collect.IResponseDAO#aggregateResponseByIds(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	public List<SingleQuestionResponse> aggregateResponseByIds(Integer voterId, Integer questionId, Integer choiceId, String freetext) {
		List<SingleQuestionResponse> list = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		int idx = 1;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(prepareAggregationStatment(voterId,questionId,choiceId,freetext));
			if (null!=voterId) {
				stat.setInt(idx++, voterId);
			}
			if (null!=questionId) {
				stat.setInt(idx++, questionId);
			}
			if (null!=choiceId) {
				stat.setInt(idx++, choiceId);
			}
			if (null!=freetext) {
				stat.setString(idx++, "%"+freetext+"%");
			}
			res = stat.executeQuery();
			list = createListFromResultSet(res);
		} catch (Throwable t) {
			this.processDaoException(t, "Errore searching responseId", "loadResponseByIds");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return list;
	}

	private String prepareAggregationStatment(Integer voterId,
			Integer questionId, Integer choiceId, String freetext) {
		StringBuffer query=new StringBuffer(AGGREGATE_RESPONSE);
		boolean isWhereInserted=false;
		if (null!=voterId) {
			if (!isWhereInserted) {
				query.append(" WHERE ");
				isWhereInserted=true;
			}
			query.append(" voterid = ? ");
		}
		if (null!=questionId) {
			if (!isWhereInserted) {
				query.append(" WHERE ");
				isWhereInserted=true;
			} else {
				query.append(" AND ");
			}
			query.append(" questionid = ? ");
		}
		if (null!=choiceId) {
			if (!isWhereInserted) {
				query.append(" WHERE ");
				isWhereInserted=true;
			} else {
				query.append(" AND ");
			}
			query.append(" choiceid = ? ");
		}
		if (null!=freetext) {
			if (!isWhereInserted) {
				query.append(" WHERE ");
				isWhereInserted=true;
			} else {
				query.append(" AND ");
			}
			query.append(" LOWER(freetext ) LIKE LOWER(?) ");
		}
		query.append("ORDER BY voterid ");
		return query.toString();
	}
	
	private List<SingleQuestionResponse> createListFromResultSet(ResultSet res) {
		List<SingleQuestionResponse> list=new ArrayList<SingleQuestionResponse>();
		try {
			while (res.next()) {
				SingleQuestionResponse response=this.createRecordFromResultSet(res);
				list.add(response);
			}
		}
		catch (Throwable t) {
			this.processDaoException(t, "Error building the vote result list", "createListFromResultSet");
		}
		if (list.isEmpty()) return null;
		return list;
	}
	
	private SingleQuestionResponse createRecordFromResultSet(ResultSet res) {
		SingleQuestionResponse response = new SingleQuestionResponse();
		try {
			response.setVoterId(res.getInt(1)); // 1
			response.setQuestionId(res.getInt(2)); // 2
			response.setChoiceId(res.getInt(3)); // 3
			response.setFreeText(res.getString(4)); // 4
		} catch (Throwable t) {
			this.processDaoException(t, "Error creating the 'response' onject form resulset", "createRecordFromResultSet");
		}
		return response;
	}

	public void deleteResponse(SingleQuestionResponse response) {
		Connection conn=null;
		PreparedStatement stat = null;
		try {			
			conn=this.getConnection();
			if (null!=response.getFreeText() && response.getFreeText().length()>0) {
				stat=conn.prepareStatement(DELETE_RESPONSE_FREETEXT);
			} else {
				stat=conn.prepareStatement(DELETE_RESPONSE_NO_FREETEXT);
			}
			stat.setInt(1, response.getVoterId()); // 1
			stat.setInt(2, response.getQuestionId()); // 2
			stat.setInt(3, response.getChoiceId()); // 3
			if (null!=response.getFreeText() && response.getFreeText().length()>0) {
				stat.setString(4, response.getFreeText()); // 4
			}
			stat.execute();
		} catch (Throwable t) {
			this.processDaoException(t, "Error deleting the object response", "deleteResponse");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	public void deleteResponseByQuestionId(int questionId) {
		Connection conn=null;
		PreparedStatement stat = null;
		try {
			conn=this.getConnection();
			stat=conn.prepareStatement(DELETE_RESPONSE_BY_QUESTION_ID);
			stat.setInt(1, questionId); // 1
			stat.execute();
		} catch (Throwable t) {
			this.processDaoException(t, "Error deleting responses by question ID "+questionId, "deleteResponseByQuestionId");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	public void deleteResponseByChoiceId(int choiceId) {
		Connection conn=null;
		PreparedStatement stat = null;
		try {
			conn=this.getConnection();
			stat=conn.prepareStatement(DELETE_RESPONSE_BY_CHOICE_ID);
			stat.setInt(1, choiceId); // 1
			stat.execute();
		} catch (Throwable t) {
			this.processDaoException(t, "Error deleting responses by choice ID "+choiceId, "deleteResponseByQuestionId");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	final String SAVE_RESPONSE =
		"INSERT INTO jpsurvey_responses (voterid,questionid,choiceid,freetext) VALUES (?,?,?,?)";
	
	final String AGGREGATE_RESPONSE =
		"SELECT voterid,questionid,choiceid,freetext FROM jpsurvey_responses ";
	
	final String DELETE_RESPONSE_FREETEXT =
		"DELETE FROM jpsurvey_responses WHERE voterid = ? AND questionid = ? AND choiceid = ? AND freetext = ? ";
	
	final String DELETE_RESPONSE_NO_FREETEXT =
		"DELETE FROM jpsurvey_responses WHERE voterid = ? AND questionid = ? AND choiceid = ? AND freetext IS NULL ";
	
	final String DELETE_RESPONSE_BY_QUESTION_ID = "DELETE FROM jpsurvey_responses WHERE questionid = ? ";
	
	final String DELETE_RESPONSE_BY_CHOICE_ID = "DELETE FROM jpsurvey_responses WHERE choiceid = ? ";
}
