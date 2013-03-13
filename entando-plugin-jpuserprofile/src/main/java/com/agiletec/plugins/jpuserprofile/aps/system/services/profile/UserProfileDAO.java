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
package com.agiletec.plugins.jpuserprofile.aps.system.services.profile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.agiletec.aps.system.common.entity.AbstractEntityDAO;
import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.model.IUserProfile;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.model.UserProfile;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.model.UserProfileRecord;

/**
 * Data Access Object for UserProfile Object. 
 * @author E.Santoboni
 */
public class UserProfileDAO extends AbstractEntityDAO implements IUserProfileDAO  {
	
	@Override
	@Deprecated
	public void addProfile(String username, UserProfile profile) throws ApsSystemException {
		this.addProfile(username, (IUserProfile) profile);
	}
	
	@Override
	@Deprecated
	public void addProfile(String username, IUserProfile profile) throws ApsSystemException {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(INSERT_PROFILE);
			stat.setString(1, username);
			stat.setString(2, profile.getTypeCode());
			stat.setString(3, profile.getXML());
			if (profile.isPublicProfile()) {
				stat.setInt(4, 1);
			} else {
				stat.setInt(4, 0);
			}
			stat.executeUpdate();
			this.addEntitySearchRecord(username, profile, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Errore in aggiunta profile", "addProfile");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	@Deprecated
	public void deleteProfile(String username) throws ApsSystemException {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteEntitySearchRecord(username, conn);
			stat = conn.prepareStatement(DELETE_PROFILE_BY_USER);
			stat.setString(1, username);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			processDaoException(t, "Errore in eliminazione profile by id", "deleteProfile by id");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	protected String getLoadEntityRecordQuery() {
		return GET_PROFILE_VO;
	}
	
	@Override
	protected ApsEntityRecord createEntityRecord(ResultSet res) throws Throwable {
		UserProfileRecord profile = new UserProfileRecord();
		profile.setId(res.getString("username"));
		profile.setTypeCode(res.getString("profiletype"));
		profile.setXml(res.getString("profilexml"));
		profile.setPublicProfile(res.getInt("publicprofile") == 1);
		return profile;
	}
	
	@Override
	protected String getAddEntityRecordQuery() {
		return INSERT_PROFILE;
	}
	
	@Override
	protected void buildAddEntityStatement(IApsEntity entity, PreparedStatement stat) throws Throwable {
		IUserProfile profile = (IUserProfile) entity;
		stat.setString(1, profile.getUsername());
		stat.setString(2, profile.getTypeCode());
		stat.setString(3, profile.getXML());
		if (profile.isPublicProfile()) {
			stat.setInt(4, 1);
		} else {
			stat.setInt(4, 0);
		}
	}
	
	@Override
	protected String getUpdateEntityRecordQuery() {
		return UPDATE_PROFILE;
	}
	
	@Override
	protected void buildUpdateEntityStatement(IApsEntity entity, PreparedStatement stat) throws Throwable {
		IUserProfile profile = (IUserProfile) entity;
		stat.setString(1, profile.getTypeCode());
		stat.setString(2, profile.getXML());
		if (profile.isPublicProfile()) {
			stat.setInt(3, 1);
		} else {
			stat.setInt(3, 0);
		}
		stat.setString(4, profile.getUsername());
	}
	
	@Override
	protected String getDeleteEntityRecordQuery() {
		return DELETE_PROFILE_BY_USER;
	}
	
	@Override
	@Deprecated
	public ApsEntityRecord getProfile(String username) throws ApsSystemException {
		return super.loadEntityRecord(username);
	}
	
	@Override
	@Deprecated
	public void updateProfile(String username, UserProfile profile) throws ApsSystemException {
		this.updateProfile(username, (IUserProfile) profile); 
	}
	
	@Override
	@Deprecated
	public void updateProfile(String username, IUserProfile profile) throws ApsSystemException {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteEntitySearchRecord(username, conn);
			stat = conn.prepareStatement(UPDATE_PROFILE);
			stat.setString(1, profile.getTypeCode());
			stat.setString(2, profile.getXML());
			if (profile.isPublicProfile()) {
				stat.setInt(3, 1);
			} else {
				stat.setInt(3, 0);
			}
			stat.setString(4, username);
			stat.executeUpdate();
			this.addEntitySearchRecord(username, profile, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Errore in aggiornamento profile", "updateProfile");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	protected String getAddingSearchRecordQuery() {
		return ADD_PROFILE_SEARCH_RECORD;
	}

	@Override
	protected String getExtractingAllEntityIdQuery() {
		return GET_ALL_ENTITY_ID;
	}

	@Override
	protected String getRemovingSearchRecordQuery() {
		return DELETE_PROFILE_SEARCH_RECORD;
	}
	
	private final String ADD_PROFILE_SEARCH_RECORD =
		"INSERT INTO jpuserprofile_profilesearch " +
		"(username, attrname, textvalue, datevalue, numvalue, langcode) " +
		"VALUES ( ? , ? , ? , ? , ? , ? ) ";
	
	private final String DELETE_PROFILE_SEARCH_RECORD =
		"DELETE FROM jpuserprofile_profilesearch WHERE username = ? ";
	
	private final String GET_ALL_ENTITY_ID = 
		"SELECT username FROM jpuserprofile_authuserprofiles";
	
	private final String INSERT_PROFILE = 
		"INSERT INTO jpuserprofile_authuserprofiles (username, profiletype, profilexml, publicprofile) values ( ? , ? , ? , ? ) ";
	
	private final String DELETE_PROFILE_BY_USER = 
		"DELETE FROM jpuserprofile_authuserprofiles WHERE username = ? ";
	
	private final String GET_PROFILE_VO = 
		"SELECT username, profiletype, profilexml, publicprofile FROM jpuserprofile_authuserprofiles WHERE username = ? ";
	
	private final String UPDATE_PROFILE = 
		"UPDATE jpuserprofile_authuserprofiles SET profiletype = ? , profilexml = ? , publicprofile = ? WHERE username = ? ";
	
}
