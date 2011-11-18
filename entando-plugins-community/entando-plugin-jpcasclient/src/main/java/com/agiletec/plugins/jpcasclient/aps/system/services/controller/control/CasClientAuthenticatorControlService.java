/*
*
* Copyright 2009 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
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
* Copyright 2009 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
*/
package com.agiletec.plugins.jpcasclient.aps.system.services.controller.control;


import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.validation.Assertion;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.controller.ControllerManager;
import com.agiletec.aps.system.services.controller.control.AbstractControlService;
import com.agiletec.aps.system.services.user.AbstractUser;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpcasclient.CasClientPluginSystemCostants;
import com.agiletec.plugins.jpcasclient.aps.system.common.AuthCommon;
import com.agiletec.plugins.jpcasclient.aps.system.services.user.CasAuthProviderManager;

/**
 * Extension of authentication service for managing CAS protocol
 * 
 * @author G.Cocco
 * */
public class CasClientAuthenticatorControlService extends AbstractControlService {
    
    public void afterPropertiesSet() throws Exception {
    	this._log.config(this.getClass().getName() + ": initialized");
	}
    
    /**
     * Execution.
     * 
     * The service method execute the following operations (int the order indicated):
     * 
     * 1) if in session there's the SAML assertion of CAS it is used for extract
     * principal information and load matching user in the session.
     * 
     * 2) if in the request there are parameters user and password the are used
     *  to try to load the matching user; if user is not null it is loaded into the session
     * 
     * 3) if there is not a user into the session the guest user is loaded into 
     * the session.
     * 
     * @param reqCtx the request context
     * @param status the status  returned by the preceding service
     * @return the resulting status
     */
    public int service(RequestContext reqCtx, int status) {
    	String name = null;
    	if (_log.isLoggable(Level.FINEST)) {
    		_log.finest("Invoked " + this.getClass().getName());
    	}
        int retStatus = ControllerManager.INVALID_STATUS;
        if (status == ControllerManager.ERROR) {
        	return status;
        }
        try {
            HttpServletRequest req = reqCtx.getRequest();
            
            //Punto 1
            Assertion assertion =
            	(Assertion) req.getSession().getAttribute(CasClientPluginSystemCostants.JPCASCLIENT_CONST_CAS_ASSERTION);
            if (_log.isLoggable(Level.FINEST)) {
        		_log.finest(" Assertion "+assertion);
            }
            if (null != assertion) {
            	AttributePrincipal attributePrincipal = assertion.getPrincipal();
            	name = attributePrincipal.getName();
            	if (_log.isLoggable(Level.FINEST)) {
            		this._log.finest(" Princ " + attributePrincipal);
            		this._log.finest(" Princ - Name " + attributePrincipal.getName());
            	}
            }
            this._log.finest("Request From User with Principal [CAS tiket validation]: " + name + " - info: AuthType " + req.getAuthType() + " " + req.getProtocol() + " " + req.getRemoteAddr() + " " + req.getRemoteHost());
            HttpSession session = req.getSession();
            if (null != name) {
            	String username = name;
            	if (getAuthCommon().hasRealmDomainInformation(name)) {
            		 username = getAuthCommon().getUsernameFromPrincipal(name);
            	}
            	this._log.finest("Request From User with Username: " + username + " - info: AuthType " + req.getAuthType() + " " + req.getProtocol() + " " + req.getRemoteAddr() + " " + req.getRemoteHost());
            	if (username != null) {
            		this._log.finest("user " + username );
            		UserDetails userOnSession = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
            		if (userOnSession == null || (userOnSession != null && !username.equals(userOnSession.getUsername()))) {
            			UserDetails user = this.getAuthenticationProvider().getUser(username);
                		if (user != null) {
                			if (!user.isAccountNotExpired()) {
                				req.setAttribute("accountExpired", new Boolean(true));
                			} else {
                				if (userOnSession != null && !userOnSession.getUsername().equals(SystemConstants.GUEST_USER_NAME)) {
                					((AbstractUser) user).setPassword(userOnSession.getPassword());
                				}
                				session.setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, user);
                				this._log.finest("New User: " + user.getUsername());
                			}
                		} else {
                			req.setAttribute("wrongAccountCredential", new Boolean(true));
                		}
            		}
            	}
            } 
            
            //Punto 2
            String userName = req.getParameter("username");
            String password = req.getParameter("password");
            if (userName != null && password != null) {
            	_log.finest("user " + userName + " - password ******** ");
                UserDetails user = this.getAuthenticationProvider().getUser(userName, password);
                if (user != null) {
                	if (!user.isAccountNotExpired()) {
                		req.setAttribute("accountExpired", new Boolean(true));
                	} else {
                		session.setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, user);
                		_log.finest("Nuovo User: " + user.getUsername());
                	}
                } else {
                	req.setAttribute("wrongAccountCredential", new Boolean(true));
                }
            }
            
            //Punto 3
            if (session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER) == null) {
            	UserDetails guestUser = this.getUserManager().getGuestUser();
                session.setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, guestUser);
            }
            retStatus = ControllerManager.CONTINUE;
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "service", "Error in processing the request");
            retStatus = ControllerManager.ERROR;
        }
        return retStatus;
    }
    
	protected IUserManager getUserManager() {
		return _userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}
	
	public void setAuthenticationProvider(CasAuthProviderManager authenticationProvider) {
		this._authenticationProvider = authenticationProvider;
	}
	public CasAuthProviderManager getAuthenticationProvider() {
		return _authenticationProvider;
	}

	public void setAuthCommon(AuthCommon kerbAuthCommon) {
		this._authCommon = kerbAuthCommon;
	}
	public AuthCommon getAuthCommon() {
		return _authCommon;
	}

	private AuthCommon _authCommon;
	private CasAuthProviderManager _authenticationProvider;
    private IUserManager _userManager;
    
}
