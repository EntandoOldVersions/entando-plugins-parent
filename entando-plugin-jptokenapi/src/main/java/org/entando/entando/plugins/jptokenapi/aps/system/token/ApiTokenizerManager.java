/*
*
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jptokenapi.aps.system.token;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * @author E.Santoboni
 */
public class ApiTokenizerManager extends AbstractService implements IApiTokenizerManager {
	
	@Override
	public void init() throws Exception {
		ApsSystemUtils.getLogger().config(this.getClass().getName() + ": initialized");
	}
	
	@Override
	public String getUser(String token) throws ApsSystemException {
		String username = null;
		try {
			username = this.getApiTokenDAO().getUser(token);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getUser", "Error extracting user by token '" + token + "'");
			throw new ApsSystemException("Error extracting user by token '" + token + "'", t);
		}
		return username;
	}
	
	@Override
	public String getToken(String username) throws ApsSystemException {
		String token = null;
		try {
			token = this.getApiTokenDAO().getToken(username);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getUser", "Error extracting token by username '" + username + "'");
			throw new ApsSystemException("Error extracting token by username '" + username + "'", t);
		}
		return token;
	}
	
	protected IApiTokenDAO getApiTokenDAO() {
		return _apiTokenDAO;
	}
	public void setApiTokenDAO(IApiTokenDAO apiTokenDAO) {
		this._apiTokenDAO = apiTokenDAO;
	}
	
	private IApiTokenDAO _apiTokenDAO;
	
}