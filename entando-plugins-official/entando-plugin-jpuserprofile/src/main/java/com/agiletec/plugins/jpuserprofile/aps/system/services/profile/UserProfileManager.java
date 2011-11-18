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
package com.agiletec.plugins.jpuserprofile.aps.system.services.profile;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.ApsEntityManager;
import com.agiletec.aps.system.common.entity.IEntityDAO;
import com.agiletec.aps.system.common.entity.IEntitySearcherDAO;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.user.AbstractUser;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpuserprofile.aps.system.services.ProfileSystemConstants;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.event.ProfileChangedEvent;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.model.IUserProfile;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.model.UserProfile;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.model.UserProfileRecord;

/**
 * Implementation of ProfileManager.
 * The service is included in a transparent manner in the workflow of required 
 * to insert, update or delete user, using the Aspect-oriented programming. 
 * In that way, you can join a user with his Profile whatever implementation of User Management.
 * @author E.Santoboni
 */
@Aspect
public class UserProfileManager extends ApsEntityManager implements IUserProfileManager {
	
	@Override
	public void init() throws Exception {
		super.init();
		if (null == this.getDefaultProfileType()) {
			IApsEntity defaultProfileType = (IApsEntity) this.getEntityClass().newInstance();
			defaultProfileType.setTypeCode(ProfileSystemConstants.DEFAULT_PROFILE_TYPE_CODE);
			defaultProfileType.setTypeDescr("Default Profile Type");
			this.addEntityPrototype(defaultProfileType);
		}
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.user.IUserManager.getUser(..))", 
			returning="user")
	public void injectProfile(Object user) {
		if (user != null) {
			AbstractUser userDetails = (AbstractUser) user;
			if (null == userDetails.getProfile()) {
				try {
					IUserProfile profile = this.getProfile(userDetails.getUsername());
					userDetails.setProfile(profile);
				} catch (Throwable t) {
					ApsSystemUtils.logThrowable(t, this, "injectProfile", "ERRORE INIEZIONE PROFILO su utente " + userDetails.getUsername());
				}
			}
		}
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.user.IUserManager.addUser(..)) && args(user,..)")
	public void addProfile(Object user) {
		if (user != null) {
			UserDetails userDetails = (UserDetails) user;
			Object profile = userDetails.getProfile();
			if (null != profile) {
				try {
					this.addProfile(userDetails.getUsername(), (IUserProfile) profile);
				} catch (Throwable t) {
					ApsSystemUtils.logThrowable(t, this, "addProfile", "ERRORE AGGIUNTA PROFILO su utente " + userDetails.getUsername());
				}
			}
		}
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.user.IUserManager.updateUser(..)) && args(user,..)")
	public void updateProfile(Object user) {
		if (user != null) {
			UserDetails userDetails = (UserDetails) user;
			Object profile = userDetails.getProfile();
			if (null != profile) {
				try {
					this.updateProfile(userDetails.getUsername(), (IUserProfile) profile);
				} catch (Throwable t) {
					ApsSystemUtils.logThrowable(t, this, "updateProfile", "ERRORE Aggiornamento PROFILO su utente " + userDetails.getUsername());
				}
			}
		}
	}
	
	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.user.IUserManager.removeUser(..)) && args(key)")
	public void deleteProfile(Object key) {
		String username = null;
		if (key instanceof String) {
			username = key.toString();
		} else if (key instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) key;
			username = userDetails.getUsername();
		}
		if (username != null) {
			try {
				this.deleteProfile(username);
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "addProfile", "ERRORE CANCELLAZIONE PROFILO su utente " + username);
			}
		}
	}
	
	@Override
	public IApsEntity getEntity(String entityId) throws ApsSystemException {
		return this.getProfile(entityId);
	}
	
	@Override
	public IUserProfile getDefaultProfileType() {
		return (IUserProfile) super.getEntityPrototype(ProfileSystemConstants.DEFAULT_PROFILE_TYPE_CODE);
	}
	
	@Override
	@Deprecated
	public UserProfile getProfileType() {
		return (UserProfile) this.getDefaultProfileType();
	}
	
	@Override
	public IUserProfile getProfileType(String typeCode) {
		return (IUserProfile) super.getEntityPrototype(typeCode);
	}
	
	@Override
	@Deprecated
	public void addProfile(String username, UserProfile profile) throws ApsSystemException {
		this.addProfile(username, (IUserProfile) profile);
	}
	
	@Override
	public void addProfile(String username, IUserProfile profile) throws ApsSystemException {
		try {
			profile.setId(username);
			this.getProfileDAO().addEntity(profile);
			this.notifyProfileChanging(profile, ProfileChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addProfile");
			throw new ApsSystemException("Errore salvataggio profilo", t);
		}
	}
	
	@Override
	public void deleteProfile(String username) throws ApsSystemException {
		try {
			IUserProfile profileToDelete = this.getProfile(username);
			if (null == profileToDelete) return;
			this.getProfileDAO().deleteEntity(username);
			this.notifyProfileChanging(profileToDelete, ProfileChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteProfile");
			throw new ApsSystemException("Errore eliminazione profile per utente", t);
		}
	}	
	
	@Override
	public IUserProfile getProfile(String username) throws ApsSystemException {
		IUserProfile profile = null;
		try {
			UserProfileRecord profileVO = (UserProfileRecord) this.getProfileDAO().loadEntityRecord(username);
			if (profileVO != null) {
				profile = (IUserProfile) this.createEntityFromXml(profileVO.getTypeCode(), profileVO.getXml());
				profile.setPublicProfile(profileVO.isPublicProfile());
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getProfile");
			throw new ApsSystemException("Errore recupero profileVO", t);
		}
		return profile;
	}
	
	@Override
	@Deprecated
	public void updateProfile(String username, UserProfile profile) throws ApsSystemException {
		this.updateProfile(username, (IUserProfile) profile);
	}
	
	@Override
	public void updateProfile(String username, IUserProfile profile) throws ApsSystemException {
		try {
			profile.setId(username);
			this.getProfileDAO().updateEntity(profile);
			this.notifyProfileChanging(profile, ProfileChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "updateProfile");
			throw new ApsSystemException("Errore aggiornamento profilo", t);
		}
	}
	
	private void notifyProfileChanging(IUserProfile profile, int operationCode) throws ApsSystemException {
		ProfileChangedEvent event = new ProfileChangedEvent();
		event.setProfile(profile);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}
	
	@Override
	protected ICategoryManager getCategoryManager() {
		return null;
	}
	
	@Override
	protected IEntityDAO getEntityDao() {
		return (IEntityDAO) this.getProfileDAO();
	}
	
	@Override
	protected IEntitySearcherDAO getEntitySearcherDao() {
		return _entitySearcherDAO;
	}
	
	protected IUserProfileDAO getProfileDAO() {
		return _profileDAO;
	}
	public void setProfileDAO(IUserProfileDAO profileDAO) {
		this._profileDAO = profileDAO;
	}
	
	protected IEntitySearcherDAO getEntitySearcherDAO() {
		return _entitySearcherDAO;
	}
	public void setEntitySearcherDAO(IEntitySearcherDAO entitySearcherDAO) {
		this._entitySearcherDAO = entitySearcherDAO;
	}

	private IUserProfileDAO _profileDAO;
	private IEntitySearcherDAO _entitySearcherDAO;
	
}
