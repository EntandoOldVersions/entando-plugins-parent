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
package com.agiletec.plugins.jpmyportalplus.aps.tags.util;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.tags.util.BaseFrameDecoratorContainer;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.IMyPortalConfigManager;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.pagemodel.Frame;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.pagemodel.MyPortalPageModel;

import java.util.Set;

/**
 * @author E.Santoboni
 */
public class MyPortalWidgetDecoratorContainer extends BaseFrameDecoratorContainer {

	@Override
	public boolean needsDecoration(Widget showlet, RequestContext reqCtx) {
		try {
			IPage page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
			Integer currentFrame = (Integer) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_FRAME);
			IMyPortalConfigManager myportalConfigManager =
					(IMyPortalConfigManager) ApsWebApplicationUtils.getBean(JpmyportalplusSystemConstants.MYPORTAL_CONFIG_MANAGER, reqCtx.getRequest());
			Set<String> allowedShowlet = myportalConfigManager.getConfig().getAllowedShowlets();
			MyPortalPageModel model = (MyPortalPageModel) page.getModel();
			Frame currentFrameObject = model.getFrameConfigs()[currentFrame];
			return (!currentFrameObject.isLocked() && allowedShowlet.contains(showlet.getType().getCode()));
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "needsDecoration", "Error checking widget decorators");
			throw new RuntimeException("Error checking widget decorators", t);
		}
	}

	@Override
	public boolean isShowletDecorator() {
		return true;
	}

}