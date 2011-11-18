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

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import javax.naming.CommunicationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.PartialResultException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.user.IUserDAO;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * The Data Access Object for LdapUser.
 * @author E.Santoboni
 */
public class LdapUserDAO implements IUserDAO {
	
	@Override
	public void addUser(UserDetails user) {
		ApsSystemUtils.getLogger().severe("Method not supported");
	}
	
	@Override
	public void deleteUser(String username) {
		ApsSystemUtils.getLogger().severe("Method not supported");
	}
	
	@Override
	public void deleteUser(UserDetails user) {
		ApsSystemUtils.getLogger().severe("Method not supported");
	}
	
	@Override
	public void updateUser(UserDetails user) {
		ApsSystemUtils.getLogger().severe("Method not supported");
	}
	
	@Override
	public List<UserDetails> loadUsersForGroup(String groupName) {
		ApsSystemUtils.getLogger().severe("Method not supported");
		return null;
	}
	
	@Override
	public void changePassword(String username, String password) {
		ApsSystemUtils.getLogger().severe("Method not supported");
	}
	
	@Override
	public List<String> loadUsernamesForGroup(String groupName) {
		ApsSystemUtils.getLogger().severe("Method not supported");
		return null;
	}
	
	@Override
	public void updateLastAccess(String username) {
		ApsSystemUtils.getLogger().severe("Method not supported");
	}
	
	@Override
	public UserDetails loadUser(String username, String password) {
		boolean isAuth = this.isAuth(username, password);
		if (isAuth) {
			LdapUser user = (LdapUser) this.loadUser(username);
			user.setPassword(password);
			return user;
		}
		return null;
	}
	
	@Override
	public UserDetails loadUser(String username) {
		LdapUser user = null;
		try {
			SearchResult sr = this.searchUserByFilterExpr(username);
			if (sr == null) return null; 
			Attributes attrs = sr.getAttributes();
			user = this.createUserFromAttributes(attrs);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "loadUser", "Errore in caricamento utente " + username);
			throw new RuntimeException("Errore in caricamento utente " + username, t);
		}
		return user;
	}
	
	protected SearchResult searchUserByFilterExpr(String uid) {
		SearchResult res = null;
		DirContext dirCtx = null;
		String filterExpr = "(&(objectClass=" + this.getUserObjectClass() + ")(" + this.getUserIdAttributeName() + "=" + uid + ")" + this.getFilterGroupBlock() + ")";
		try {
			dirCtx = getDirContext();
			SearchControls ctls = new SearchControls();
			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration<SearchResult> answer = dirCtx.search("", filterExpr, ctls);
			if (answer.hasMore()) {
				res = answer.next();
			}
		} catch (ConnectException e) {
			ApsSystemUtils.logThrowable(e, this, "searchUserByFilterExpr", "Estrazione utente " + uid + " : Directory non raggiungibile");
			//non rilancia eccezioni in maniera da non fermare i servizi in caso di non funzionamento della Directory
		} catch (CommunicationException e) {
			ApsSystemUtils.logThrowable(e, this, "searchUserByFilterExpr", "Estrazione utente " + uid + " : Directory non raggiungibile");
			//non rilancia eccezioni in maniera da non fermare i servizi in caso di non funzionamento della Directory
		} catch (PartialResultException e) {
			ApsSystemUtils.logThrowable(e, this, "searchUserByFilterExpr", "Estrazione utente " + uid + " : Directory non raggiungibile");
			//non rilancia eccezioni in maniera da non fermare i servizi in caso di non funzionamento della Directory
		} catch (Throwable t) {
			throw new RuntimeException("Errore in caricamento utente " + uid, t);
		} finally {
			closeDirContext(dirCtx);
		}
		return res;
	}
	
	protected boolean isAuth(String username, String password) {
		Logger log = ApsSystemUtils.getLogger();
		boolean isAuth = false;
		DirContext dirCtx = null;
		try {
			dirCtx = this.getDirContext();
			SearchControls ctls = new SearchControls();
			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration<SearchResult> answer = dirCtx.search("", "(&(objectClass=" + this.getUserObjectClass() + ")(" + this.getUserIdAttributeName() + "="+username+")" + this.getFilterGroupBlock() + ")", ctls);
			if (answer.hasMoreElements()) {
				dirCtx.close();
				try {
					SearchResult searchResult = answer.nextElement();
					String userdn = searchResult.getNameInNamespace();
					answer.close();
					Hashtable<String, String> clonedParams = new Hashtable<String, String>();
					clonedParams.putAll(this.getParams());
					clonedParams.put(Context.SECURITY_PRINCIPAL, userdn);
					clonedParams.put(Context.SECURITY_CREDENTIALS, password);
					dirCtx = new InitialDirContext(clonedParams);
					answer = dirCtx.search("", "(&(objectClass=" + this.getUserObjectClass() + ")(" + this.getUserIdAttributeName() + "="+username+")" + this.getFilterGroupBlock() + ")", ctls);
					isAuth = answer.hasMoreElements();
					answer.close();
					if (isAuth) {
						log.info("Found User '" + username + "'");
					}
				} catch (Throwable t) {
					log.info("Bad Login for User '" + username + "'");
				}
			} else {
				log.info("Not found User '" + username + "'");
			}
			answer.close();
		} catch (ConnectException e) {
			ApsSystemUtils.logThrowable(e, this, "isAuth", "Directory non raggiungibile");
			//non rilancia eccezioni in maniera da non fermare i servizi in caso di non funzionamento della Directory
		} catch (CommunicationException e) {
			ApsSystemUtils.logThrowable(e, this, "isAuth", " : Directory non raggiungibile");
			//non rilancia eccezioni in maniera da non fermare i servizi in caso di non funzionamento della Directory
		} catch (NamingException e) {
			ApsSystemUtils.logThrowable(e, this, "isAuth", "autenticazione utente " + username + " : Directory non raggiungibile");
			//non rilancia eccezioni in maniera da non fermare i servizi in caso di non funzionamento della Directory
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "isAuth", "Errore in verifica autenticazione utente " + username);
			throw new RuntimeException("Errore in verifica autenticazione utente " + username, t);
		} finally {
			this.closeDirContext(dirCtx);
		}
		return isAuth;
	}
	
	@Override
	public List<UserDetails> loadUsers() {
		String filterExpr = "(&(objectClass=" + this.getUserObjectClass() + ")(" + this.getUserIdAttributeName() + "=*)" + this.getFilterGroupBlock() + ")";
		return this.searchUsersByFilterExpr(filterExpr);
	}
	
	protected LdapUser createUserFromAttributes(Attributes attrs) throws NamingException {
		LdapUser user = new LdapUser(this.getUserToStringConfig());
		String userName = (String) attrs.get(this.getUserIdAttributeName()).get(0);
		user.setUsername(userName);
		if (null != attrs.get("userPassword")) {
			byte[] bytesPwd = (byte[]) attrs.get("userPassword").get(0);
			String userPassword = new String(bytesPwd);
			user.setPassword(userPassword);
		}
		user.setAttributes(attrs);
		return user;
	}
	
	@Override
	public List<UserDetails> searchUsers(String text) {
		String filterExpr = "(&(objectClass=" + this.getUserObjectClass() + ")" + this.getFilterGroupBlock() + "(|(" + this.getUserIdAttributeName() + "=*"+text+"*)(cn=*"+text+"*)))";
		return this.searchUsersByFilterExpr(filterExpr);
	}
	
	protected String getFilterGroupBlock() {
		String block = "";
		String filterGroup = this.getFilterGroup();
		if (null != filterGroup && filterGroup.trim().length()>0) {
			block = "(" + this.getFilterGroupAttributeName() + "=" + filterGroup + ")";
		}
		return block;
	}
	
	protected List<UserDetails> searchUsersByFilterExpr(String filterExpr) {
		List<UserDetails> users = new ArrayList<UserDetails>();
		DirContext dirCtx = null;
		try {
			dirCtx = this.getDirContext();
			SearchControls ctls = new SearchControls();
			String[] attrIDs = { this.getUserIdAttributeName(), "userPassword", "cn", "sn" };
			ctls.setReturningAttributes(attrIDs);
			ctls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			if (this.getSearchResultMaxSize() > 0) {
				ctls.setCountLimit(this.getSearchResultMaxSize());
			}
			NamingEnumeration<SearchResult> answer = dirCtx.search("", filterExpr, ctls);
			while (answer.hasMore()) {
				SearchResult res = answer.next();
				Attributes attrs = res.getAttributes();
				LdapUser user = this.createUserFromAttributes(attrs);
				users.add(user);
			}
		} catch (ConnectException e) {
			ApsSystemUtils.logThrowable(e, this, "searchUsersByFilterExpr", "Directory non raggiungibile");
			//non rilancia eccezioni in maniera da non fermare i servizi in caso di non funzionamento della Directory
		} catch (CommunicationException e) {
			ApsSystemUtils.logThrowable(e, this, "searchUsersByFilterExpr", " : Directory non raggiungibile");
			//non rilancia eccezioni in maniera da non fermare i servizi in caso di non funzionamento della Directory
		} catch (NamingException e) {
			ApsSystemUtils.logThrowable(e, this, "searchUsersByFilterExpr", "Caricamento utenti : Directory non raggiungibile");
			//non rilancia eccezioni in maniera da non fermare i servizi in caso di non funzionamento della Directory
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "searchUsersByFilterExpr", "Errore in caricamento lista di utenti");
			throw new RuntimeException("Errore in caricamento lista di utenti", t);
		} finally {
			this.closeDirContext(dirCtx);
		}
		return users;
	}
	
	protected DirContext getDirContext() throws NamingException, CommunicationException, ConnectException {
		DirContext dirCtx = null;
		try {
			dirCtx = new InitialDirContext(this.getParams());
		} catch (NamingException e) {
			throw e;
		}
		return dirCtx;
	}
	
	protected void closeDirContext(DirContext dirCtx) {
		try {
			dirCtx.close();
		} catch (NullPointerException e) {
			ApsSystemUtils.getLogger().severe("DirContext LDAP Nullo");
		} catch (NamingException e) {
			throw new RuntimeException("Errore in chiusura DirContext LDAP", e);
		}
	}
	
	protected Hashtable<String, String> getParams() {
		Hashtable<String, String> params = new Hashtable<String, String>(11);
		params.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		params.put(Context.PROVIDER_URL, this.getProviderUrl());
		if (null != this.getSecurityPrincipal() && null != this.getSecurityCredentials()) {
			params.put(Context.SECURITY_PRINCIPAL, this.getSecurityPrincipal());
			params.put(Context.SECURITY_CREDENTIALS, this.getSecurityCredentials());
			params.put(Context.REFERRAL, "ignore");
		}
		return params;
	}
	
	protected String getProviderUrl() {
		return _providerUrl;
	}
	public void setProviderUrl(String providerUrl) {
		this._providerUrl = providerUrl;
	}
	
	protected String getSecurityPrincipal() {
		return _securityPrincipal;
	}
	public void setSecurityPrincipal(String securityPrincipal) {
		this._securityPrincipal = securityPrincipal;
	}
	
	protected String getSecurityCredentials() {
		return _securityCredentials;
	}
	public void setSecurityCredentials(String securityCredentials) {
		this._securityCredentials = securityCredentials;
	}
	
	protected String getUserObjectClass() {
		if (null == this._userObjectClass) {
			return "posixAccount";
		}
		return _userObjectClass;
	}
	public void setUserObjectClass(String userObjectClass) {
		this._userObjectClass = userObjectClass;
	}
	
	protected String getUserIdAttributeName() {
		return _userIdAttributeName;
	}
	public void setUserIdAttributeName(String userIdAttributeName) {
		this._userIdAttributeName = userIdAttributeName;
	}
	
	public String getFilterGroup() {
		return _filterGroup;
	}
	public void setFilterGroup(String filterGroup) {
		this._filterGroup = filterGroup;
	}
	
	public String getFilterGroupAttributeName() {
		return _filterGroupAttributeName;
	}
	public void setFilterGroupAttributeName(String filterGroupAttributeName) {
		this._filterGroupAttributeName = filterGroupAttributeName;
	}
	
	protected int getSearchResultMaxSize() {
		return _searchResultMaxSize;
	}
	public void setSearchResultMaxSize(int searchResultMaxSize) {
		this._searchResultMaxSize = searchResultMaxSize;
	}
	
	public void setUserToStringConfig(String userToStringConfig) {
		this._userToStringConfig = userToStringConfig;
	}
	public String getUserToStringConfig() {
		return _userToStringConfig;
	}

	private String _providerUrl;
	private String _securityPrincipal;
	private String _securityCredentials;
	
	private String _userObjectClass;
	private String _userIdAttributeName;
	
	private String _filterGroup;
	private String _filterGroupAttributeName;
	
	private int _searchResultMaxSize = -1;
	
	private String _userToStringConfig;
	
}