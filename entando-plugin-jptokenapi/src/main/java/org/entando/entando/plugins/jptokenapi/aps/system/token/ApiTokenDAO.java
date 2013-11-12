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
package org.entando.entando.plugins.jptokenapi.aps.system.token;

import com.agiletec.aps.system.common.AbstractDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author E.Santoboni
 */
public class ApiTokenDAO extends AbstractDAO implements IApiTokenDAO {
	
	@Override
	public String updateToken(String username) {
		Connection conn = null;
		PreparedStatement stat = null;
		String token_data = username + System.nanoTime();
        String token = DigestUtils.md5Hex(token_data);
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeToken(username, conn);
			stat = conn.prepareStatement(INSERT_TOKEN);
			stat.setString(1, username);
			stat.setString(2, token);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error while updating token", "updateToken");
		} finally {
			closeDaoResources(null, stat, conn);
		}
		return token;
	}
	
	@Override
	public void removeToken(String username) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.removeToken(username, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error while deleting a token", "removeToken");
		} finally {
			closeConnection(conn);
		}
	}
	
	private void removeToken(String username, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_TOKEN);
			stat.setString(1, username);
			stat.executeUpdate();
		} catch (Throwable t) {
			processDaoException(t, "Error while deleting a token", "removeToken");
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	@Override
	public String getToken(String username) {
		String token = this.get(username, SELECT_TOKEN);
		if (null == token) {
			token = this.updateToken(username);
		}
		return token;
	}
	
	@Override
	public String getUser(String token) {
		return this.get(token, SELECT_USERNAME);
	}
	
	private String get(String field, String query) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		String data = null;
		try {
			conn = this.getConnection();
            stat = conn.prepareStatement(query);
            stat.setString(1, field);
            res = stat.executeQuery();
			if (res.next()) {
				data = res.getString(1);
			}
		} catch (Throwable t) {
			processDaoException(t, "Error while loading data", "get");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return data;
	}
	
	private static final String SELECT_TOKEN = "SELECT token FROM jptokenapi_usertokens WHERE username = ?";
	
	private static final String SELECT_USERNAME = "SELECT username FROM jptokenapi_usertokens WHERE token = ?";
	
	private static final String INSERT_TOKEN = "INSERT INTO jptokenapi_usertokens(username, token) VALUES (?, ?)";
	
	private static final String DELETE_TOKEN = "DELETE FROM jptokenapi_usertokens WHERE username = ?";
	
}