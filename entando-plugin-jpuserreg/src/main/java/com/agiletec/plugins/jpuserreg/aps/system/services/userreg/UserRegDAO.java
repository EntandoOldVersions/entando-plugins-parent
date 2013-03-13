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
package com.agiletec.plugins.jpuserreg.aps.system.services.userreg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.agiletec.aps.system.common.AbstractDAO;

/**
 * Data Access Object for managing information associated
 * with Account Requests, Suspinsion and Password Recover
 * 
 * @author zuanni
 * */
public class UserRegDAO extends AbstractDAO implements IUserRegDAO {
	
	@Override
	public void addActivationToken(String username, String token, Date regtime, String type) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_TOKEN);
			stat.setString(1, username);
			stat.setString(2, token);
			stat.setTimestamp(3, new Timestamp(regtime.getTime()));
			stat.setString(4, type);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error adding token for account activation", "addActivationToken");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public String getUsernameFromToken(String token) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		String username = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_USERNAME_FROM_TOKEN);
			stat.setString(1,token);
			res = stat.executeQuery();
			if (res.next()) {
				username = res.getString("username");
			}
		} catch (Throwable t) {
			processDaoException(t, "Error getting Username from token", "getUsernameFromToken");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return username;
	}
	
	@Override
	public void removeConsumedToken(String token) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_USED_TOKEN);
			stat.setString(1,token);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error removing consumed Token", "removeConsumedToken");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void clearOldTokens(Date date) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			Timestamp timeBound = new Timestamp(date.getTime());
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_OLD_TOKENS);
			stat.setTimestamp(1, timeBound);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error in clear old tokens", "clearOldAccountRequests");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void clearTokenByUsername(String username) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_USER_TOKENS);
			stat.setString(1, username);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error in clear tokens by username", "clearTokenByUsername");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public List<String> oldAccountsNotActivated(Date date) {
		List<String> usernames = new ArrayList<String>();
		Timestamp timeBound = new Timestamp(date.getTime());
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		String username = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(USERNAMES_FROM_OLD_ACCOUNT_REQUESTS);
			stat.setTimestamp(1, timeBound);
			res = stat.executeQuery();
			while (res.next()) {
				username = res.getString("username");
				usernames.add(username);
			}
		} catch (Throwable t) {
			processDaoException(t, "Error ", "oldAccountsNotActivated");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return usernames;
	}
	
	private static final String ADD_TOKEN = 
		"INSERT INTO jpuserreg_activationtokens(username, token, regtime, tokentype) VALUES (?, ?, ?, ?)";
	
	private static final String GET_USERNAME_FROM_TOKEN = 
		"SELECT username FROM jpuserreg_activationtokens WHERE token = ?";
	
	private static final String DELETE_USED_TOKEN = 
		"DELETE FROM jpuserreg_activationtokens WHERE token = ?";
	
	private static final String USERNAMES_FROM_OLD_ACCOUNT_REQUESTS =
		"SELECT jpuserreg_activationtokens.username FROM jpuserreg_activationtokens, authusers " +
		" WHERE regtime < ? AND tokentype = 'activation' " +
		" AND jpuserreg_activationtokens.username = authusers.username " +
		" AND authusers.active = 0 ";
	
	private static final String DELETE_OLD_TOKENS = 
		"DELETE FROM jpuserreg_activationtokens WHERE regtime < ? AND tokentype = 'reactivation_recover'";
	
	private static final String DELETE_USER_TOKENS = 
		"DELETE FROM jpuserreg_activationtokens WHERE username = ?";

}