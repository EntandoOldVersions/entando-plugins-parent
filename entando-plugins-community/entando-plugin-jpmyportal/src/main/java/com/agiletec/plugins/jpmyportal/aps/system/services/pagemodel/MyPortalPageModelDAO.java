/*
*
* Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
* This file is part of JAPS software.
* JAPS and its  source-code is  licensed under the  terms of the
* GNU General Public License  as published by  the Free Software
* Foundation (http://www.fsf.org/licensing/licenses/gpl.txt).
* 
* You may copy, adapt, and redistribute this file for commercial
* or non-commercial use.
* When copying,  adapting,  or redistributing  this document you
* are required to provide proper attribution  to AgileTec, using
* the following attribution line:
* Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
*/
package com.agiletec.plugins.jpmyportal.aps.system.services.pagemodel;

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
				MyPortalPageModelDOM pageModelDOM = new MyPortalPageModelDOM(xmlFrames, this.getShowletTypeManager());
				pageModel.setFrames(pageModelDOM.getFrames());
				pageModel.setMainFrame(pageModelDOM.getMainFrame());
				pageModel.setFrameConfigs(pageModelDOM.getFrameConfigs());
				pageModel.setDefaultShowlet(pageModelDOM.getDefaultShowlet());
			}
			pageModel.setPluginCode(res.getString(4));
		} catch (Throwable t) {
			processDaoException(t, "Error detected building the page model", "getPageModelFromResultSet");
		}
		return pageModel;
	}
	
}
