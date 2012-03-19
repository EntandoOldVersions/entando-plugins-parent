/*
*
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* jAPS is a free software; 
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
package com.agiletec.plugins.jpmyportalplus.aps.system.services.config;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.showlettype.ShowletType;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.model.MyPortalConfig;

/**
 * Interface for the service of MyPortal that handles the configuration.
 * @author E.Santoboni
 */
public interface IMyPortalConfigManager {
	
	public MyPortalConfig getConfig();
	
	public void saveConfig(MyPortalConfig config) throws ApsSystemException;
	
	/**
	 * Get the list of the showlet that cannot be handled by the jpmyportal plugin
	 */
	public List<ShowletType> getCustomizableShowlets();
	
	public String getVoidShowletCode();
	
}