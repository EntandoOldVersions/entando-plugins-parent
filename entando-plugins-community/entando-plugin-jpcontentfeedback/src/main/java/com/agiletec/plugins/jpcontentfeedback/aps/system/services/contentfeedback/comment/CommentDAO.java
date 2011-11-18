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
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.Comment;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.ICommentSearchBean;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.IRatingDAO;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.RatingDAO;
/**
 *
 * @author D.Cherchi
 *
 */
public class CommentDAO  extends AbstractDAO implements ICommentDAO{

	@Override
	public void addComment(Comment comment) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_COMMENT);
			stat.setInt(1, comment.getId());
			stat.setString(2, comment.getContentId());
			stat.setTimestamp(3, new Timestamp(new Date().getTime()));
			stat.setString(4, comment.getComment());
			stat.setInt(5, comment.getStatus());
			stat.setString(6, comment.getUsername());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error adding a comment", "addComment");
		} finally {
			closeDaoResources(null, stat, conn);
		}

	}

	@Override
	public void deleteComment(int commentId) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			((RatingDAO)this.getRatingDAO()).removeRatingInTransaction(conn, commentId);
			stat = conn.prepareStatement(DELETE_COMMENT);
			stat.setInt(1, commentId);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error deleting a comment", "deleteComment");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public List<String> searchCommentsId(ICommentSearchBean searchBean) {
		List<String> comments = new ArrayList<String>();
		PreparedStatement stat = null;
		ResultSet res = null;
		Connection conn = null;
		try {
			conn = this.getConnection();
			String query = this.createQueryString(searchBean);
			stat = conn.prepareStatement(query);
			this.buildStatement(searchBean, stat);
			res = stat.executeQuery();
			while (res.next()) {
				String commentId = res.getString(1);
				comments.add(commentId);
			}
		} catch (Throwable t) {
			processDaoException(t, "Errore in caricamento lista id commenti", "searchCommentsId");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return comments;
	}




	protected int buildStatement(ICommentSearchBean searchBean, PreparedStatement stat) throws SQLException {
		int pos = 1;
		if (searchBean!=null) {
			String username = searchBean.getUsername();
			if (username!=null && username.length()>0) {
				stat.setString(pos++, username);
			}
			int status = searchBean.getStatus();
			if (status!=0) {
				stat.setInt(pos++, status);
			}
			String comment = searchBean.getComment();
			if (comment!=null && comment.length()>0) {
				stat.setString(pos++, this.searchLikeString(comment));
			}

			Date fromDate = searchBean.getCreationFROMDate();
			if (fromDate!=null) {
				Calendar data = new GregorianCalendar();
				data.setTime(fromDate);
				data.set(GregorianCalendar.MILLISECOND, 0);
				data.set(GregorianCalendar.SECOND, 0);
				data.set(GregorianCalendar.MINUTE, 0);
				data.set(GregorianCalendar.HOUR, 0);
				stat.setTimestamp(pos++, new Timestamp(data.getTimeInMillis()));
			}
			Date toDate = searchBean.getCreationTODate();
			if (toDate!=null) {
				Calendar data = new GregorianCalendar();
				data.setTime(toDate);
				data.set(GregorianCalendar.MILLISECOND, 999);
				data.set(GregorianCalendar.SECOND, 59);
				data.set(GregorianCalendar.MINUTE, 59);
				data.set(GregorianCalendar.HOUR_OF_DAY, 23);
				stat.setTimestamp(pos++, new Timestamp(data.getTimeInMillis()));
			}

			String contentId = searchBean.getContentId();
			if (contentId!=null && contentId.length()>0) {
				stat.setString(pos++, contentId);
			}
		}
		return pos;
	}

	@Override
	public void updateStatus(int id, int status) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_STATUS_COMMENT);
			stat.setInt(1, status);
			stat.setInt(2, id);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error updating a status comment", "updateStatus");
		} finally {
			closeDaoResources(null, stat, conn);
		}

	}

	@Override
	public Comment getComment(int id) {
		Comment comment = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res =null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(LOAD_COMMENT);
			stat.setInt(1, id);
			res = stat.executeQuery();
			if (res.next()) {
				comment=this.popualteComment(res);
			}
		} catch (Throwable t) {
			processDaoException(t, "Error load comment", "getComment");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return comment;
	}

	private String searchLikeString(String searchValue) {
		String result = "";
		searchValue.trim();
		String[] titleSplit = searchValue.split(" ");
		for ( int i = 0;  i < titleSplit.length; i++) {
			if ( titleSplit[i].length() > 0 ) {
				result += "%" + titleSplit[i];
			}
		}
		return result + "%" ;
	}

	private String createQueryString(ICommentSearchBean searchBean){
		StringBuffer query = new StringBuffer(SELECT_COMMENTS_CODES);
		if (searchBean!=null) {
			boolean appendWhere = true;
			String username = searchBean.getUsername();
			if (username!=null && username.length()>0) {
				query.append(APPEND_WHERE);
				query.append(APPEND_AUTHOR_CLAUSE);
				appendWhere = false;
			}

			int status = searchBean.getStatus();
			if (status!=0) {
				query.append(appendWhere ? APPEND_WHERE : APPEND_AND);
				query.append(APPEND_STATUS_CLAUSE);
				appendWhere = false;
			}

			String comment = searchBean.getComment();
			if (comment!=null && comment.length()>0) {
				query.append(appendWhere ? APPEND_WHERE : APPEND_AND);
				query.append(APPEND_COMMENT_CLAUSE);
				appendWhere = false;
			}

			Date fromDate = searchBean.getCreationFROMDate();
			if (fromDate!=null) {
				query.append(appendWhere ? APPEND_WHERE : APPEND_AND);
				query.append(APPEND_DATE_FROM_CLAUSE);
				appendWhere = false;
			}

			Date toDate = searchBean.getCreationTODate();
			if (toDate!=null) {
				query.append(appendWhere ? APPEND_WHERE : APPEND_AND);
				query.append(APPEND_DATE_TO_CLAUSE);
				appendWhere = false;
			}

			String contentID = searchBean.getContentId();
			if (contentID!=null && contentID.length()>0) {
				query.append(appendWhere ? APPEND_WHERE : APPEND_AND);
				query.append(APPEND_CONTENT_ID_CLAUSE);
				appendWhere = false;
			}


		}
		query.append(APPEND_ORDERBY_CLAUSE);
		return query.toString();
	}

	/**
	 * Dato il resultSet, ne estrae i dati e popola l'oggetto comment
	 * @param res
	 * @return
	 * @throws ApsSystemException
	 */
	private Comment popualteComment(ResultSet res) throws ApsSystemException {
		Comment comment = new Comment();
		try {
			comment.setId(res.getInt("id"));
			comment.setContentId(res.getString("contentid"));
			comment.setCreationDate(res.getTimestamp("creationDate"));
			comment.setComment(res.getString("usercomment"));
			comment.setStatus(res.getInt("status"));
			comment.setUsername(res.getString("username"));
		} catch (Throwable t) {
			throw new ApsSystemException("Errore loading comment", t);
		}
		return comment;
	}

	public void setRatingDAO(IRatingDAO ratingDAO) {
		this._ratingDAO = ratingDAO;
	}

	public IRatingDAO getRatingDAO() {
		return _ratingDAO;
	}

	private static final String ADD_COMMENT =
		"INSERT INTO jpcontentfeedback_comments  (id, contentid, creationdate,  usercomment, status, username) VALUES (?, ?, ?, ?, ?, ?) ";

	private static final String DELETE_COMMENT=
		"DELETE FROM jpcontentfeedback_comments WHERE id = ?";

	private static final String UPDATE_STATUS_COMMENT=
		"UPDATE jpcontentfeedback_comments SET status=? WHERE id=?";

	private final String SELECT_COMMENTS_CODES =
		"SELECT id FROM jpcontentfeedback_comments ";

	private final String LOAD_COMMENT =
		"SELECT * FROM jpcontentfeedback_comments WHERE id = ?";

	private final String APPEND_WHERE = "WHERE ";
	private final String APPEND_AND = "AND ";
	private final String APPEND_AUTHOR_CLAUSE  = "username = ? ";
	private final String APPEND_STATUS_CLAUSE  = "status = ? ";
	private final String APPEND_CONTENT_ID_CLAUSE  = "contentid = ? ";
	private final String APPEND_DATE_FROM_CLAUSE  = "creationDate >= ? ";
	private final String APPEND_DATE_TO_CLAUSE  = "creationDate <= ? ";
	private final String APPEND_COMMENT_CLAUSE  = "usercomment LIKE ? ";
	private final String APPEND_ORDERBY_CLAUSE = "ORDER BY creationdate DESC ";

	private IRatingDAO _ratingDAO;

}
