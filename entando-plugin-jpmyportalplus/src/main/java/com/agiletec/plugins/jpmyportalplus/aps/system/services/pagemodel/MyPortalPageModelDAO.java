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
package com.agiletec.plugins.jpmyportalplus.aps.system.services.pagemodel;

import java.sql.ResultSet;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.pagemodel.PageModelDAO;

/**
 * Data Access Object for the page models
 * @author E. Santoboni
 */
public class MyPortalPageModelDAO extends PageModelDAO {
	
	/**
	 * Build and return a page model from a result set.
	 * @param res The result set to parse
	 * @return The page model corresponding to the result set
	 * @throws ApsSystemException In case of error
	 */
	@Override
	protected PageModel getPageModelFromResultSet(ResultSet res) throws ApsSystemException {
		MyPortalPageModel pageModel = new MyPortalPageModel();
		try {
			pageModel.setCode(res.getString(1));
			pageModel.setDescr(res.getString(2));
			String xmlFrames = res.getString(3);
			if (null != xmlFrames && xmlFrames.length() > 0) {
				MyPortalPageModelDOM pageModelDOM = new MyPortalPageModelDOM(xmlFrames, this.getWidgetTypeManager());
				pageModel.setFrames(pageModelDOM.getFrames());
				pageModel.setMainFrame(pageModelDOM.getMainFrame());
				pageModel.setFrameConfigs(pageModelDOM.getFrameConfigs());
				pageModel.setDefaultWidget(pageModelDOM.getDefaultShowlet());
			}
			pageModel.setPluginCode(res.getString(4));
		} catch (Throwable t) {
			processDaoException(t, "Error detected building the page model", "getPageModelFromResultSet");
		}
		return pageModel;
	}
	
}
