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
package com.agiletec.plugins.jpldap.aps.system.services.user;

import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.IUserDAO;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.system.services.user.UserManager;

/**
 * The Manager for LdapUser.
 * @author E.Santoboni
 */
public class LdapUserManager extends UserManager implements ILdapUserManager {
	
	@Override
	public UserDetails getUser(String username, String password) throws ApsSystemException {
		UserDetails user = null;
		try {
			user = this.getLdapUserDAO().loadUser(username, password);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getUser");
            throw new ApsSystemException("Errore in caricamento utente LDAP " + username, t);
		}
		if (null != user) {
			return user;
		} else {
			return super.getUser(username, password);
		}
	}
	
	@Override
	public UserDetails getUser(String username) throws ApsSystemException {
		UserDetails user = null;
		try {
			user = this.getLdapUserDAO().loadUser(username);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getUser");
            throw new ApsSystemException("Errore in caricamento utente LDAP " + username, t);
		}
		if (null != user) {
			return user;
		} else {
			return super.getUser(username);
		}
	}
	
	@Override
	public List<UserDetails> getUsers() throws ApsSystemException {
		List<UserDetails> users = super.getUsers();
		try {
			users.addAll(this.getLdapUserDAO().loadUsers());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getUsers");
            throw new ApsSystemException("Errore in caricamento lista utenti", t);
		}
		return users;
	}
	
	@Override
	public List<UserDetails> searchUsers(String text) throws ApsSystemException {
		if (text == null || text.trim().length()==0) return this.getUsers();
		List<UserDetails> users = super.searchUsers(text);
		try {
			users.addAll(this.getLdapUserDAO().searchUsers(text));
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "searchUsers");
            throw new ApsSystemException("Errore in ricerca lista utenti", t);
		}
		return users;
	}
	
	@Override
	public List<UserDetails> searchUsers(String text, Boolean japsUser) throws ApsSystemException {
		try {
			if (japsUser == null) {
				return this.searchUsers(text);
			}
			IUserDAO userDAO = japsUser.booleanValue() ? super.getUserDAO() : this.getLdapUserDAO();
			if (text == null || text.trim().length() == 0) return userDAO.loadUsers();
			return userDAO.searchUsers(text);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "searchUsers");
            throw new ApsSystemException("Errore in ricerca lista utenti", t);
		}
	}
	
	protected IUserDAO getLdapUserDAO() {
		return _ldapUserDAO;
	}
	public void setLdapUserDAO(IUserDAO userLdapDAO) {
		this._ldapUserDAO = userLdapDAO;
	}
	
	public IUserDAO _ldapUserDAO;
	
}
