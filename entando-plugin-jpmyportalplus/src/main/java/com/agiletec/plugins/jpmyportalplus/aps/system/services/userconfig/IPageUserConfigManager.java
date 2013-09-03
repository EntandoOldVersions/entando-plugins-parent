/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
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
package com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.entando.entando.aps.system.services.widgettype.WidgetType;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.CustomPageConfig;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.PageUserConfigBean;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.ShowletUpdateInfoBean;

/**
 * Interface for the service of MyPortal that handles the configuration of the page models. MyPortal enables
 * users to customize the page models of the portal they are visiting.
 * @author E.Santoboni
 */
public interface IPageUserConfigManager {

	public PageUserConfigBean getUserConfig(UserDetails user) throws ApsSystemException;

	@Deprecated
	public PageUserConfigBean getUserConfig(String username) throws ApsSystemException;

	public List<WidgetType> getCustomizableShowlets(UserDetails user) throws ApsSystemException;

	public CustomPageConfig getGuestPageConfig(IPage page, HttpServletRequest request) throws ApsSystemException;

	public Widget[] getShowletsToRender(IPage page, Widget[] customShowlets) throws ApsSystemException;

	public void updateUserPageConfig(String username, IPage page, ShowletUpdateInfoBean[] updateInfo) throws ApsSystemException;

	public void updateGuestPageConfig(IPage page, ShowletUpdateInfoBean[] updateInfos, HttpServletRequest request, HttpServletResponse response) throws ApsSystemException;

	public void removeUserPageConfig(String username, IPage page) throws ApsSystemException;

	public void removeGuestPageConfig(IPage page, HttpServletRequest request, HttpServletResponse response) throws ApsSystemException;

	public WidgetType getVoidShowlet();

	public static final int STATUS_OPEN = 0;//DEFAULT STATUS
	public static final int STATUS_CLOSE = 1;

}
