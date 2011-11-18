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
package com.agiletec.plugins.jpuserprofile.apsadmin.common.attribute.action.list;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.apsadmin.system.entity.IEntityActionHelper;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.model.IUserProfile;
import com.agiletec.plugins.jpuserprofile.apsadmin.common.ICurrentUserProfileAction;

/**
 * Action classe for the management of operations on the list type attributes in User Profile.
 * @author E.Santoboni
 */
public class ListAttributeAction extends com.agiletec.apsadmin.system.entity.attribute.action.list.ListAttributeAction {
	
	@Override
	protected IApsEntity getCurrentApsEntity() {
		IUserProfile userProfile = this.updateUserProfileOnSession();
		return userProfile;
	}
	
	public IUserProfile getUserProfile() {
		return (IUserProfile) this.getRequest().getSession().getAttribute(ICurrentUserProfileAction.SESSION_PARAM_NAME_CURRENT_PROFILE);
	}
	
	protected IUserProfile updateUserProfileOnSession() {
		IUserProfile userProfile = this.getUserProfile();
		if (null != userProfile) {
			this.getEntityActionHelper().updateEntity(userProfile, this.getRequest());
		}
		return userProfile;
	}
	
	protected IEntityActionHelper getEntityActionHelper() {
		return _entityActionHelper;
	}
	public void setEntityActionHelper(IEntityActionHelper entityActionHelper) {
		this._entityActionHelper = entityActionHelper;
	}
	
	private IEntityActionHelper _entityActionHelper;

}