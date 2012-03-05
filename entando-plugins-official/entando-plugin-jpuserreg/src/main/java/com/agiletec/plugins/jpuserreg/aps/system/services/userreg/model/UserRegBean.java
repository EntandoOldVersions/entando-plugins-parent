/*
*
* Copyright 2008 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
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
* Copyright 2008 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
*/
package com.agiletec.plugins.jpuserreg.aps.system.services.userreg.model;

import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.model.IUserProfile;

/**
 * Transfer Object from Struts action to Manager
 * 
 * @author G.Cocco
 * */
public class UserRegBean {
	// TODO Questa classe dovrà morire quanto prima, non appena potrà essere rimosso IUserRegManager.regAccount(UserRegBean regAccountBean)
	
	public void setUsername(String username) {
		this._username = username;
	}
	public String getUsername() {
		return _username;
	}
	
	public void setUserProfile(IUserProfile userProfile) {
		this._userProfile = userProfile;
	}
	public IUserProfile getUserProfile() {
		return _userProfile;
	}
	
	private String _username;
	private IUserProfile _userProfile;
	
}