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

import com.agiletec.aps.system.common.entity.IEntityDAO;
import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.model.IUserProfile;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.model.UserProfile;

/**
 * Interface for Data Access Object of UserProfile Object. 
 * @author E.Santoboni
 */
public interface IUserProfileDAO extends IEntityDAO {
	
	/**
	 * Update a UserProfile.
	 * @param username The username of the user that you must update the profile.
	 * @param profile The profile to update.
	 * @throws ApsSystemException In case of Exception.
	 * @deprecated From jAPS 2.0 version 2.0.7, updateProfile(String, IUserProfile)
	 */
	public void updateProfile(String username, UserProfile profile) throws ApsSystemException;
	
	/**
	 * Update a UserProfile.
	 * @param username The username of the user that you must update the profile.
	 * @param profile The profile to update.
	 * @throws ApsSystemException In case of Exception.
	 * @deprecated From jAPS 2.0 version 2.0.9, use updateEntity
	 */
	public void updateProfile(String username, IUserProfile profile) throws ApsSystemException;
	
	/**
	 * Delete a UserProfile by username.
	 * @param username The username of the Profile owner that you must delete the profile.
	 * @throws ApsSystemException In case of Exception.
	 * @deprecated From jAPS 2.0 version 2.0.9, use deleteEntity
	 */
	public void deleteProfile(String username) throws ApsSystemException;
	
	/**
	 * Return an ApsEntityRecord (db record) by username.
	 * @param username The username.
	 * @return The ApsEntityRecord required.
	 * @throws ApsSystemException In case of Exception.
	 * @deprecated From jAPS 2.0 version 2.0.7, use loadEntityRecord
	 */
	public ApsEntityRecord getProfile(String username) throws ApsSystemException;
	
	/**
	 * Add a UserProfile
	 * @param username The username of the Profile owner.
	 * @param profile The UserProfile to add.
	 * @throws ApsSystemException In case of Exception.
	 * @deprecated use From jAPS 2.0 version 2.0.7, addProfile(String, IUserProfile)
	 */
	public void addProfile(String username, UserProfile profile) throws ApsSystemException;
	
	/**
	 * Add a UserProfile
	 * @param username The username of the Profile owner.
	 * @param profile The UserProfile to add.
	 * @throws ApsSystemException In case of Exception.
	 * @deprecated From jAPS 2.0 version 2.0.9, use addEntity
	 */
	public void addProfile(String username, IUserProfile profile) throws ApsSystemException;
	
}
