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
package com.agiletec.plugins.jpldap.aps.system.services.user;

import java.util.Collections;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.IUserDAO;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.system.services.user.UserManager;

import com.agiletec.plugins.jpldap.aps.system.LdapSystemConstants;
import org.apache.commons.beanutils.BeanComparator;

/**
 * The Manager for LdapUser.
 * @author E.Santoboni
 */
public class LdapUserManager extends UserManager implements ILdapUserManager {
    
    @Override
    public UserDetails getUser(String username, String password) throws ApsSystemException {
        if (!isActive()) {
			return super.getUser(username, password);
		}
        UserDetails user = null;
        try {
            user = this.getLdapUserDAO().loadUser(username, password);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "getUser");
            //throw new ApsSystemException("Error loading LDAP user" + username, t);
        }
        if (null != user) {
            return user;
        } else {
            return super.getUser(username, password);
        }
    }
    
    @Override
    public UserDetails getUser(String username) throws ApsSystemException {
        if (!isActive()) {
			return super.getUser(username);
		}
        UserDetails user = null;
        try {
            user = this.getLdapUserDAO().loadUser(username);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "getUser");
            //throw new ApsSystemException("Error loading LDAP user" + username, t);
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
        if (!isActive()) {
			return users;
		}
        try {
            users.addAll(this.getLdapUserDAO().loadUsers());
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "getUsers");
            //throw new ApsSystemException("Error loading users", t);
        }
        if (null != users) {
            BeanComparator comparator = new BeanComparator("username");
            Collections.sort(users, comparator);
        }
        return users;
    }
    
    @Override
    public List<UserDetails> searchUsers(String text) throws ApsSystemException {
        if (!isActive()) {
			return super.searchUsers(text);
		}
        if (text == null || text.trim().length() == 0) {
            return this.getUsers();
        }
        List<UserDetails> users = super.searchUsers(text);
        try {
            users.addAll(this.getLdapUserDAO().searchUsers(text));
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "searchUsers");
            //throw new ApsSystemException("Error searching users", t);
        }
        if (null != users) {
            BeanComparator comparator = new BeanComparator("username");
            Collections.sort(users, comparator);
        }
        return users;
    }
    
    @Override
    public List<UserDetails> searchUsers(String text, Boolean japsUser) throws ApsSystemException {
        if (!isActive()) {
			return super.searchUsers(text);
		}
        try {
            if (japsUser == null) {
                return this.searchUsers(text);
            }
            IUserDAO userDAO = japsUser.booleanValue() ? super.getUserDAO() : this.getLdapUserDAO();
            if (text == null || text.trim().length() == 0) {
                return userDAO.loadUsers();
            }
            return userDAO.searchUsers(text);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "searchUsers");
            throw new ApsSystemException("Error loading users", t);
        }
    }
	
	@Override
	public void addUser(UserDetails user) throws ApsSystemException {
		if (!isActive() || !isWriteUserEnable()) {
			super.addUser(user);
			return;
		}
		try {
			this.getLdapUserDAO().addUser(user);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addUser");
			throw new ApsSystemException("Error adding LDAP User" + user.getUsername(), t);
		}
	}
	
	@Override
	public void changePassword(String username, String password) throws ApsSystemException {
		if (!isActive() || !isWriteUserEnable()) {
			super.changePassword(username, password);
			return;
		}
		try {
			this.getLdapUserDAO().changePassword(username, password);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "changePassword");
			throw new ApsSystemException("Error updating the password of the LDAP User" + username, t);
		}
	}
	
	@Override
	public void removeUser(UserDetails user) throws ApsSystemException {
		if (!isActive() || !isWriteUserEnable()) {
			super.removeUser(user);
		}
		try {
			this.getLdapUserDAO().deleteUser(user);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeUser");
			throw new ApsSystemException("Error deleting a ldap user", t);
		}
	}
	
	@Override
	public void removeUser(String username) throws ApsSystemException {
		if (!isActive() || !isWriteUserEnable()) {
			super.removeUser(username);
		}
		try {
			this.getLdapUserDAO().deleteUser(username);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeUser");
			throw new ApsSystemException("Error deleting a ldap user", t);
		}
	}
	
	@Override
	public void updateUser(UserDetails user) throws ApsSystemException {
		if (!isActive() || !isWriteUserEnable()) {
			super.updateUser(user);
			return;
		}
		try {
			this.getLdapUserDAO().updateUser(user);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "updateUser");
			throw new ApsSystemException("Error updating a ldap user", t);
		}
	}
	
    private boolean isActive() {
        String activeString = this.getConfigManager().getParam(LdapSystemConstants.ACTIVE_PARAM_NAME);
        Boolean active = Boolean.parseBoolean(activeString);
        return active.booleanValue();
    }
	
	@Override
	public boolean isWriteUserEnable() {
		return this.getLdapUserDAO().isWriteUserEnable();
	}
    
    protected ILdapUserDAO getLdapUserDAO() {
        return _ldapUserDAO;
    }
    public void setLdapUserDAO(ILdapUserDAO userLdapDAO) {
        this._ldapUserDAO = userLdapDAO;
    }
    
    public ILdapUserDAO _ldapUserDAO;
    
}