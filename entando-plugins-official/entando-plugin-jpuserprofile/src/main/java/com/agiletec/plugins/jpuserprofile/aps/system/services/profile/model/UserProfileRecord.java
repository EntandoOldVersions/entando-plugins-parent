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
package com.agiletec.plugins.jpuserprofile.aps.system.services.profile.model;

import com.agiletec.aps.system.common.entity.model.ApsEntityRecord;

/**
 * A UserProfile Record.
 * @author E.Santoboni
 */
public class UserProfileRecord extends ApsEntityRecord {
	
	public boolean isPublicProfile() {
		return _publicProfile;
	}
	
	public void setPublicProfile(boolean publicProfile) {
		this._publicProfile = publicProfile;
	}
	
	private boolean _publicProfile;
	
}
