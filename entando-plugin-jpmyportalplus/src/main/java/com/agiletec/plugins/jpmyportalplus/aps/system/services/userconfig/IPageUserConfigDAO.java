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
package com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig;

import java.util.List;

import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.showlettype.ShowletType;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.model.MyPortalConfig;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.PageUserConfigBean;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.ShowletUpdateInfoBean;

/**
 * @author E.Santoboni
 */
public interface IPageUserConfigDAO {

	/**
	 * Return a bean containing the customized configuration of the user. If the user did not 
	 * customize any frame it returns null.
	 * @param username
	 * @return bean The bean containing the myportal customization of the current user, null otherwise.
	 */
	public PageUserConfigBean getUserConfig(String username);
	
	/**
	 * Return from the configuration the list of the showlets currently configurable.
	 * @param config class that maps the jpmyportal configuration
	 * @return the list of showlets, null otherwise
	 */
	public List<ShowletType> buildCustomizableShowletsList(MyPortalConfig config);
	
	public void syncCustomization(List<ShowletType> configurableShowlets, String voidShowletCode);
	
	public void updateUserPageConfig(String username, IPage page, ShowletUpdateInfoBean[] updateInfos);
	
	/**
	 * Remove from the DB te records located through the user and the page.
	 * @param username the user that owning the configurated records 
	 * @param page the involved page 
	 * @deprecated use removeUserPageConfig(String, String)
	 */
	public void removeUserPageConfig(String username, IPage page);
	
	public void removeUserPageConfig(String username, String pageCode);
	
	public void removeUnauthorizedShowlet(String username, String showletCode);
	
}
