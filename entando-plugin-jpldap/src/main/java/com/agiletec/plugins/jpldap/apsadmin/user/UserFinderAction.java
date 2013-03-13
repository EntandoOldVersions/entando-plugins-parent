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
package com.agiletec.plugins.jpldap.apsadmin.user;

import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpldap.aps.system.services.user.ILdapUserManager;

/**
 * Classe action delegate alla ricerca utenti.
 * @author E.Santoboni
 */
public class UserFinderAction extends com.agiletec.apsadmin.user.UserFinderAction {
	
	@Override
	public List<UserDetails> getUsers() {
		try {
			Integer userType = this.getUserType();
			Boolean japsUser = userType == null ? null : new Boolean(userType.intValue() == 1);
			List<UserDetails> users = ((ILdapUserManager) this.getUserManager()).searchUsers(this.getText(), japsUser);
			BeanComparator comparator = new BeanComparator("username");
			Collections.sort(users, comparator);
			return users;
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getUsers");
			throw new RuntimeException("Error while searching users", t);
		}
	}
	
	public Integer getUserType() {
		return _userType;
	}
	public void setUserType(Integer userType) {
		this._userType = userType;
	}
	
	private Integer _userType;
	
}