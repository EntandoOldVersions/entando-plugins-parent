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
package com.agiletec.plugins.jpuserprofile.apsadmin.common.helper;

import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.apsadmin.system.entity.EntityActionHelper;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.model.IUserProfile;

/**
 * @author E.Santoboni
 */
public class CurrentUserProfileActionHelper extends EntityActionHelper {
	
	@Override
	public void updateEntity(IApsEntity currentEntity, HttpServletRequest request) {
		IUserProfile profile = (IUserProfile) currentEntity;
		String isPublic = request.getParameter("publicProfile");
		profile.setPublicProfile(null != isPublic);
		super.updateEntity(profile, request);
	}
	
}
