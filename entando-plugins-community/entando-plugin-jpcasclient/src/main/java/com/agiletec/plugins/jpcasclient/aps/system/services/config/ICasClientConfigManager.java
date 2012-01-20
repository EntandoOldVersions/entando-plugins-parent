/*
*
* Copyright 2012 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
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
* Copyright 2012 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
*/
package com.agiletec.plugins.jpcasclient.aps.system.services.config;

import com.agiletec.aps.system.exception.ApsSystemException;

public interface ICasClientConfigManager {

	public CasClientConfig getClientConfig();

	public void updateConfig(CasClientConfig config) throws ApsSystemException;

}
