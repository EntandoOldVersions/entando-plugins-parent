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
package org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import java.sql.PreparedStatement;

/**
 * @author E.Santoboni
 */
public class PageModelDAO extends AbstractDAO implements IPageModelDAO {
	
	private static final Logger _logger =  LoggerFactory.getLogger(PageModelDAO.class);
	
	@Override
	public Map<String, Map<Integer, MyPortalFrameConfig>> loadModelConfigs() {
		Connection conn = null;
		Statement stat = null;
		ResultSet res = null;
		Map<String, Map<Integer, MyPortalFrameConfig>> models = new HashMap<String, Map<Integer, MyPortalFrameConfig>>();
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(ALL_PAGEMODEL_CONFIG);
			while (res.next()) {
				String code = res.getString(1);
				Map<Integer, MyPortalFrameConfig> modelConfig = this.getPageModelConfig(code, res.getString(2));
				models.put(code, modelConfig);
			}
		} catch (Throwable t) {
			_logger.error("Error loading the page models",  t);
			throw new RuntimeException("Error loading the page models", t);
		} finally{
			closeDaoResources(res, stat, conn);
		}
		return models;
	}
	
	protected Map<Integer, MyPortalFrameConfig> getPageModelConfig(String code, String xmlFrames) throws ApsSystemException {
		Map<Integer, MyPortalFrameConfig> config = null;
		try {
			if (null != xmlFrames && xmlFrames.length() > 0) {
				MyPortalPageModelDOM pageModelDOM = new MyPortalPageModelDOM(xmlFrames);
				config = pageModelDOM.getModelConfig();
			}
		} catch (Throwable t) {
			_logger.error("Error building the page model code '{}'", code, t);
			throw new RuntimeException("Error building the page model code '" + code + "'", t);
		}
		return config;
	}
	
	@Override
	public void deleteModelConfiguration(String code) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_PAGEMODEL_CONFIGURATION);
			stat.setString(1, code);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			_logger.error("Error while deleting a model configuration",  t);
			throw new RuntimeException("Error while deleting a model configuration", t);
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	private final String ALL_PAGEMODEL_CONFIG = 
		"SELECT code, config FROM jpmyportalplus_modelconfig";
	
	private static final String DELETE_PAGEMODEL_CONFIGURATION =
			"DELETE FROM jpmyportalplus_modelconfig WHERE code = ?";
	
}
