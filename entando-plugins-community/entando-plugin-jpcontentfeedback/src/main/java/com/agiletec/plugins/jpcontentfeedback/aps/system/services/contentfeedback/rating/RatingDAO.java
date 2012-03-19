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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model.IRating;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model.IRatingSearchBean;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model.Rating;

public class RatingDAO  extends AbstractDAO implements IRatingDAO{

	@Override
	public synchronized void addRating(IRating rating) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_RATING);
			stat.setInt(1, rating.getId());
			stat.setString(2, rating.getContentId());
			if (rating.getCommentId() == 0){
				stat.setNull(3, Types.INTEGER);
			}else{
				stat.setInt(3, rating.getCommentId());
			}
			stat.setInt(4, rating.getVoters());
			stat.setInt(5, rating.getSumvote());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error adding a rating", "addRating");
		} finally {
			closeDaoResources(null, stat, conn);
		}

	}

	@Override
	public void updateRating(IRating rating) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_RATING);
			stat.setInt(1, rating.getVoters());
			stat.setInt(2, rating.getSumvote());
			stat.setInt(3, rating.getId());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error updating a rating", "updateRating");
		} finally {
			closeDaoResources(null, stat, conn);
		}

	}


	@Override
	public Rating getRating(IRatingSearchBean searchBean){
		Rating rating = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			String query = this.createQueryString(searchBean);
			stat = conn.prepareStatement(query);
			this.buildStatement(searchBean, stat);
			res = stat.executeQuery();
			if (res.next()) {
				rating = new Rating();
				rating.setId(res.getInt("id"));
				rating.setCommentId(res.getInt("commentid"));
				rating.setContentId(res.getString("contentid"));
				rating.setVote(res.getInt("voters"), res.getInt("sumvote"));
			}
		} catch (Throwable t) {
			processDaoException(t, "Errore while search rating", "getRating");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return rating;
	}

	@Override
	public void removeRating(int commentId) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			String query = REMOVE_RATING_BY_COMMENT_ID;
			stat = conn.prepareStatement(query);
			stat.setInt(1, commentId);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Errore while remove rating", "removeRating");
		} finally {
			closeDaoResources(null, stat, conn);
		}

	}

	@Override
	public void removeContentRating(String contentId) {
		PreparedStatement stat = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			String query = DELETE_RATING_BY_CONTENT_ID;
			stat = conn.prepareStatement(query);
			stat.setString(1, contentId);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Errore while remove rating", "removeContentRating");
		} finally {
			closeDaoResources(null, stat, conn);
		}

	}

	public void removeRatingInTransaction(Connection conn, int commentId) {
		PreparedStatement stat = null;
		try {
			String query = REMOVE_RATING_BY_COMMENT_ID;
			stat = conn.prepareStatement(query);
			stat.setInt(1, commentId);
			stat.executeUpdate();
		} catch (Throwable t) {
			processDaoException(t, "Errore while remove rating", "removeRating");
		} finally {
			closeDaoResources(null, stat, null);
		}

	}

	protected int buildStatement(IRatingSearchBean searchBean, PreparedStatement stat) throws SQLException {
		int pos = 1;
		if (searchBean!=null) {
			int  id = searchBean.getId();
			int commentId = searchBean.getCommentId();
			String contentId = searchBean.getContentId();

			if (id!=0) {
				stat.setInt(1, id);
			} else if (contentId!=null && contentId.length()>0) {
				stat.setString(1, contentId);
			} else if (commentId!=0){
				stat.setInt(1, commentId);
			}
		}
		return pos;
	}
	private String createQueryString(IRatingSearchBean searchBean){
		StringBuffer query = new StringBuffer(LOAD_RATING);
		if (searchBean!=null) {
			int  id = searchBean.getId();
			int commentId = searchBean.getCommentId();
			String contentId = searchBean.getContentId();
			if (id!=0) {
				query.append(APPEND_WHERE);
				query.append(APPEND_RATING_ID_CLAUSE);
			} else if (contentId!=null && contentId.length()>0) {
				query.append(APPEND_WHERE);
				query.append(APPEND_CONTENT_ID_CLAUSE);
			} else if (commentId!=0){
				query.append(APPEND_WHERE);
				query.append(APPEND_COMMENT_ID_CLAUSE);
			}
		}
		return query.toString();
	}

	private static final String ADD_RATING =
		"INSERT INTO jpcontentfeedback_rating (id, contentid, commentid, voters, sumvote) VALUES (?, ?, ?, ?, ?) ";

	private static final String UPDATE_RATING =
		"UPDATE jpcontentfeedback_rating SET voters=?, sumvote=? WHERE id=?";

	private static final String DELETE_RATING_BY_CONTENT_ID=
		"DELETE FROM jpcontentfeedback_rating WHERE contentid = ?";

	private static final String REMOVE_RATING_BY_COMMENT_ID=
		"DELETE FROM jpcontentfeedback_rating WHERE commentid = ?";

	private final String LOAD_RATING =
		"SELECT * FROM jpcontentfeedback_rating ";

	private final String APPEND_WHERE = "WHERE ";
	private final String APPEND_CONTENT_ID_CLAUSE  = "contentid = ? ";
	private final String APPEND_COMMENT_ID_CLAUSE  = "commentid = ? ";
	private final String APPEND_RATING_ID_CLAUSE  = "id = ? ";



}