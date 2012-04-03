/*
*
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpuserprofile.apsadmin.profile;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.IUserProfileManager;

/**
 * @author E.Santoboni
 */
@Deprecated
public class UserFinderAction extends com.agiletec.apsadmin.user.UserFinderAction {
	
	@Override
	public List<UserDetails> getUsers() {
		List<UserDetails> users = null;
		try {
			List<UserDetails> systemUsers = super.getUsers();
			if (null == this.getWithProfile() || this.getWithProfile().intValue() == 0) {
				return systemUsers;
			}
			List<String> usernames = null;
			if (this.getWithProfile().intValue() == 1 && null != this.getText() && this.getText().trim().length() > 0) {
				EntitySearchFilter filter = new EntitySearchFilter(IEntityManager.ENTITY_ID_FILTER_KEY, false, this.getText().trim(), true);
				EntitySearchFilter[] filters = {filter};
				usernames = this.getUserProfileManager().searchId(filters);
			} else {
				usernames = this.getUserProfileManager().searchId(null);
			}
			users = new ArrayList<UserDetails>();
			for (int i = 0; i < systemUsers.size(); i++) {
				UserDetails user = systemUsers.get(i);
				String username = user.getUsername();
				if (this.getWithProfile().intValue() == 1 && usernames.contains(username)) {
					users.add(user);
				} else if (this.getWithProfile().intValue() == 2 && !usernames.contains(username)) {
					users.add(user);
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getUsers");
			throw new RuntimeException("Errore in ricerca utenti", t);
		}
		return users;
	}
	
	public IUserProfileManager getUserProfileManager() {
		return _userProfileManager;
	}
	public void setUserProfileManager(IUserProfileManager userProfileManager) {
		this._userProfileManager = userProfileManager;
	}
	
	public Integer getWithProfile() {
		return _withProfile;
	}
	public void setWithProfile(Integer withProfile) {
		this._withProfile = withProfile;
	}
	
	private Integer _withProfile;
	
	private IUserProfileManager _userProfileManager;
	
}