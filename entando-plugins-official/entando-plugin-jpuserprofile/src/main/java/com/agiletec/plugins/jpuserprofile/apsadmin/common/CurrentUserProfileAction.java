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
package com.agiletec.plugins.jpuserprofile.apsadmin.common;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.services.user.AbstractUser;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.apsadmin.system.entity.AbstractApsEntityAction;
import com.agiletec.plugins.jpuserprofile.aps.system.services.ProfileSystemConstants;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.IUserProfileManager;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.model.IUserProfile;

/**
 * Classe action per la gestione del Profilo Utente corrente.
 * @author E.Santoboni
 */
public class CurrentUserProfileAction extends AbstractApsEntityAction implements ICurrentUserProfileAction {
	
	@Override
	public void validate() {
		if (this.getUserProfile() != null) {
			super.validate();
		}
	}
	
	@Override
	public IApsEntity getApsEntity() {
		return this.getUserProfile();
	}
	
	public IUserProfile getUserProfile() {
		return (IUserProfile) this.getRequest().getSession().getAttribute(SESSION_PARAM_NAME_CURRENT_PROFILE);
	}
	
	@Override
	public String createNew() {
		// nothing to do
		return null;
	}
	
	@Override
	public String view() {
		// Operation Not Allowed
		return null;
	}
	
	@Override
	public String edit() {
		try {
			IUserProfile userProfile = null;
			Object object = this.getCurrentUser().getProfile();
			if (null != object && object instanceof IUserProfile) {
				String username = this.getCurrentUser().getUsername();
				userProfile = this.getUserProfileManager().getProfile(username);
			} else {
				userProfile = this.getUserProfileManager().getDefaultProfileType();
				userProfile.setId(this.getCurrentUser().getUsername());
			}
			userProfile.disableAttributes(ProfileSystemConstants.ATTRIBUTE_DISABLING_CODE_ON_EDIT);
			this.getRequest().getSession().setAttribute(SESSION_PARAM_NAME_CURRENT_PROFILE, userProfile);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String save() {
		try {
			IUserProfile profile = this.getUserProfile();
			if (profile == null) {
				return FAILURE;
			}
			UserDetails user = this.getCurrentUser();
			((AbstractUser) user).setProfile(profile);
			if (null == this.getUserProfileManager().getProfile(user.getUsername())) {
				this.getUserProfileManager().addProfile(user.getUsername(), profile);
			} else {
				this.getUserProfileManager().updateProfile(user.getUsername(), profile);
			}
			this.getRequest().getSession().removeAttribute(SESSION_PARAM_NAME_CURRENT_PROFILE);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected IUserProfileManager getUserProfileManager() {
		return _userProfileManager;
	}
	public void setUserProfileManager(IUserProfileManager userProfileManager) {
		this._userProfileManager = userProfileManager;
	}
	
	private IUserProfileManager _userProfileManager;
	
}