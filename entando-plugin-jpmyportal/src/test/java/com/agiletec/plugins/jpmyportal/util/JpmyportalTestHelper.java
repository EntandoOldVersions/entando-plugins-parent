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
package com.agiletec.plugins.jpmyportal.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.showlettype.IShowletTypeManager;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.IPageModelUserConfigDAO;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.PageModelUserConfigDAO;

public class JpmyportalTestHelper extends AbstractDAO {
	
	public JpmyportalTestHelper(ApplicationContext applicationContext) {
		DataSource dataSource = (DataSource) applicationContext.getBean("portDataSource");
		this.setDataSource(dataSource);
		
		PageModelUserConfigDAO configDAO = new PageModelUserConfigDAO();
		
		configDAO.setDataSource(dataSource);
		configDAO.setPageModelManager((IPageModelManager) applicationContext.getBean(SystemConstants.PAGE_MODEL_MANAGER));
		configDAO.setShowletTypeManager((IShowletTypeManager) applicationContext.getBean(SystemConstants.SHOWLET_TYPE_MANAGER));
		
		this._configDAO = configDAO;
	}
	
	public void initUserPageConfig() {
		this.addUserConfig("editorCoach", "service", 2, "content_viewer", null);
		this.addUserConfig("mainEditor", "service", 2, "jpmyportal_void", null);
		this.addUserConfig("mainEditor", "home", 3, "jpmyportal_void", null);
		this.addUserConfig("mainEditor", "home", 4, "content_viewer_list", null);
	}
	
	public void cleanUserPageConfig() {
		Connection conn = null;
		Statement stat = null;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			stat.executeUpdate(CLEAN_USERS_CONFIG);
		} catch (Throwable t) {
			processDaoException(t, "Errore nel salvataggio della configurazione utente", "saveUserConfig");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	public void addUserConfig(String username, String pageModelCode, int frame, String showletCode, String showletConfig) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(ADD_USER_CONFIG);
			stat.setString(1, username);		
			stat.setString(2, pageModelCode);
			stat.setInt(3, frame);
			stat.setString(4, showletCode);
			if (null != showletConfig) {
				stat.setString(5, showletConfig);
			} else {
				stat.setNull(5, java.sql.Types.VARCHAR);
			}
			stat.executeUpdate();
		} catch (Throwable t) {
			processDaoException(t, "Errore nel salvataggio della configurazione utente", "saveUserConfig");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	private static final String ADD_USER_CONFIG = 
		"INSERT INTO jpmyportal_userpagemodelconfig (username, pagemodelcode, framepos, showletcode, config) VALUES (?, ?, ?, ?, ?) ";
	
	private static final String CLEAN_USERS_CONFIG = 
		"TRUNCATE jpmyportal_userpagemodelconfig ";
	
	IPageModelUserConfigDAO _configDAO;
	
}