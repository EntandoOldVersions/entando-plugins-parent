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
package com.agiletec.plugins.jpuserprofile.apsadmin.search;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.apsadmin.system.entity.AbstractApsEntityFinderAction;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.IUserProfileManager;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.model.IUserProfile;

/**
 * @author F.Deidda - E.Santoboni
 */
public class UserProfileFinderAction extends AbstractApsEntityFinderAction implements IUserProfileFinderAction {
	
	@Override
	public String execute() {
		try {
			String username = this.getUsername();
			EntitySearchFilter filter = null;
			if (username != null && username.trim().length() > 0) {
				filter = new EntitySearchFilter(IEntityManager.ENTITY_ID_FILTER_KEY, false, username, true);
			} else {
				filter = new EntitySearchFilter(IEntityManager.ENTITY_ID_FILTER_KEY, false);
			}
			filter.setOrder(EntitySearchFilter.ASC_ORDER);
			this.addFilter(filter);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "execute");
			throw new RuntimeException("Error while creating filter by username", t);
		}
		return super.execute();
	}
	
	public String getEmailAttributeValue(String username) {
		IUserProfile userProfile = (IUserProfile) this.getEntity(username);
		String email = (String) userProfile.getValue(userProfile.getMailAttributeName());
		return email;
	}
	
	public Lang getDefaultLang(){
		return this.getLangManager().getDefaultLang();
 	}
	
	@Override
	public String viewProfile() {
		return SUCCESS;
	}

	@Override
	protected void deleteEntity(String entityId) throws Throwable {
		//Not supported
	}
	
	public IUserProfile getUserProfile(String username) {
		return (IUserProfile) this.getEntity(username);
	}
	
	@Override
	protected IEntityManager getEntityManager() {
		return (IEntityManager) this.getUserProfileManager();
	}
	
	protected IUserProfileManager getUserProfileManager() {
		return _userProfileManager;
	}
	public void setUserProfileManager(IUserProfileManager userProfileManager) {
		this._userProfileManager = userProfileManager;
	}
	
	@Override
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}
	
	private IUserProfileManager _userProfileManager;
	
	private String _username;
	
}