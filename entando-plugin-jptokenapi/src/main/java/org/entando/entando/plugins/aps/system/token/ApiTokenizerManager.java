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
package org.entando.entando.plugins.aps.system.token;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;

/**
 * @author E.Santoboni
 */
public class ApiTokenizerManager extends AbstractService implements IApiTokenizerManager {
	
	@Override
	public void init() throws Exception {
		ApsSystemUtils.getLogger().config(this.getClass().getName() + ": initialized");
	}
	
	protected ITokenDAO getApiTokenDAO() {
		return _apiTokenDAO;
	}
	public void setApiTokenDAO(ITokenDAO apiTokenDAO) {
		this._apiTokenDAO = apiTokenDAO;
	}
	
	private ITokenDAO _apiTokenDAO;
	
}