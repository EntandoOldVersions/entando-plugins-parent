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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.entando.entando.aps.system.services.widgettype.IWidgetTypeManager;
import org.entando.entando.aps.system.services.widgettype.WidgetType;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.config.model.MyPortalConfig;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.CustomPageConfig;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.PageUserConfigBean;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.ShowletUpdateInfoBean;

import org.entando.entando.aps.system.services.page.IPage;
import org.entando.entando.aps.system.services.page.IPageManager;
import org.entando.entando.aps.system.services.page.Widget;

/**
 * @author E.Santoboni
 */
public class PageUserConfigDAO extends AbstractDAO implements IPageUserConfigDAO {

	@Override
	public void syncCustomization(List<WidgetType> allowedShowlets, String voidShowletCode) {
		Set<String> allowedShowletCodes = this.getAllowedShowletCodes(allowedShowlets);
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_CONFIGURED_SHOWLET_CODE);
			res = stat.executeQuery();
			while (res.next()) {
				String currentCode = res.getString(1);
				WidgetType currentConfiguredShowlet = this.getWidgetTypeManager().getShowletType(currentCode);
				if (null == currentConfiguredShowlet) {
					ApsSystemUtils.getLogger().info(JpmyportalplusSystemConstants.MYPORTALPLUS_CONFIG_ITEM + ": deleting unknown showlet '"+currentCode+"' from the configuration bean");
					this.purgeConfigurationFromInvalidShowlets(conn, currentCode);
				} else {
					if (!allowedShowletCodes.contains(currentCode) && !currentCode.equals(voidShowletCode)) {
						ApsSystemUtils.getLogger().info(JpmyportalplusSystemConstants.MYPORTALPLUS_CONFIG_ITEM + ": removing the no longer configurable showlet '"+currentCode+"' from the configuration bean");
						this.purgeConfigurationFromInvalidShowlets(conn, currentCode);
					}
				}
			}
		} catch (Throwable t) {
			processDaoException(t, "Error while cleaning the configuration database", "syncCustomization");
		} finally {
			this.closeDaoResources(res, stat, conn);
		}
	}

	private Set<String> getAllowedShowletCodes(List<WidgetType> allowedShowlets) {
		Set<String> codes = new HashSet<String>();
		if (null == allowedShowlets) {
			return codes;
		}
		for (int i = 0; i < allowedShowlets.size(); i++) {
			WidgetType type = allowedShowlets.get(i);
			if (null != type) {
				codes.add(type.getCode());
			}
		}
		return codes;
	}

	private void purgeConfigurationFromInvalidShowlets(Connection conn, String code) throws ApsSystemException {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_USER_CONFIG_BY_SHOWLET_CODE);
			stat.setString(1, code);
			stat.execute();
		} catch (Throwable t) {
			throw new ApsSystemException("ERROR: could not remove invalid showlets from the customization database",t);
		} finally {
			closeDaoResources(null, stat);
		}
	}

	@Override
	public List<WidgetType> buildCustomizableShowletsList(MyPortalConfig config) {
		List<WidgetType> result = new ArrayList<WidgetType>();
		List<WidgetType> allShowlets = null;
		if (null == config) {
			return result;
		}
		allShowlets = this.getWidgetTypeManager().getShowletTypes();
		if (null != allShowlets) {
			Set<String> allowedShowletsCode = config.getAllowedShowlets();
			if (allowedShowletsCode != null && allowedShowletsCode.size() > 0) {
				for (WidgetType currentType : allShowlets) {
					if (allowedShowletsCode.contains(currentType.getCode())) {
						result.add(currentType);
					}
				}
			}
		}
		return result;
	}

	@Override
	public void updateUserPageConfig(String username, IPage page, ShowletUpdateInfoBean[] updateInfos) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			this.deleteUserConfig(username, page, updateInfos, conn);
			this.addUserConfig(username, page, updateInfos, conn);
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error while updating user config", "updateUserPageConfig");
		} finally {
			this.closeConnection(conn);
		}
	}

	protected void deleteUserConfig(String username, IPage page, ShowletUpdateInfoBean[] updateInfos, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(DELETE_PAGE_USER_CONFIG);
			for (int i = 0; i < updateInfos.length; i++) {
				ShowletUpdateInfoBean infoBean = updateInfos[i];
				stat.setString(1, username);
				stat.setString(2, page.getCode());
				stat.setInt(3, infoBean.getFramePos());
				stat.addBatch();
				stat.clearParameters();
			}
			stat.executeBatch();
		} catch (Throwable t) {
			processDaoException(t, "Error while deleting a user config record", "deleteUserConfig");
		} finally {
			closeDaoResources(null, stat);
		}
	}

	protected void addUserConfig(String username, IPage page, ShowletUpdateInfoBean[] updateInfos, Connection conn) {
		PreparedStatement stat = null;
		try {
			stat = conn.prepareStatement(ADD_USER_CONFIG);
			for (int i = 0; i < updateInfos.length; i++) {
				ShowletUpdateInfoBean infoBean = updateInfos[i];
				stat.setString(1, username);
				stat.setString(2, page.getCode());
				stat.setInt(3, infoBean.getFramePos());
				stat.setString(4, infoBean.getShowlet().getType().getCode());
				stat.setNull(5, java.sql.Types.CHAR);
				stat.setInt(6, infoBean.getStatus());
				stat.addBatch();
				stat.clearParameters();
			}
			stat.executeBatch();
		} catch (Throwable t) {
			processDaoException(t, "Error while adding a user config record", "addUserConfig");
		} finally {
			closeDaoResources(null, stat);
		}
	}

	@Override
	public PageUserConfigBean getUserConfig(String username) {
		Connection conn = null;
		PageUserConfigBean result = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_USER_CONFIG);
			stat.setString(1, username);
			res = stat.executeQuery();
			result = this.createPageUserConfigBeanFromResultSet(username, res);
		} catch (Throwable t) {
			processDaoException(t, "Error while saving user customization", "getUserConfig");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return result;
	}

	private int getPageModelframe(String pageCode) throws ApsSystemException {
		PageModel current = this.getPageManager().getPage(pageCode).getModel();
		try {
			return current.getFrames().length;
		} catch (Throwable t) {
			throw new ApsSystemException("ERROR: unknown page code '" + pageCode + "'");
		}
	}

	/**
	 * Create a showlet with a given code and optional configuration.
	 * @param widgetcode the code of the showlet
	 * @param config the configuration for the current showlet
	 * @return a new object with the given code and configuration
	 * @throws ApsSystemException if the given code is unknown or faulting XML configuration
	 */
	private Widget createShowletFromRecord(String widgetcode, String config) throws ApsSystemException {
		Widget newShowlet = new Widget();
		WidgetType inheritedType = this.getWidgetTypeManager().getShowletType(widgetcode);
		newShowlet.setType(inheritedType);
		ApsProperties properties = null;
		newShowlet.setConfig(properties);
		return newShowlet;
	}

	private PageUserConfigBean createPageUserConfigBeanFromResultSet(String username, ResultSet res) {
		PageUserConfigBean pageUserBean = new PageUserConfigBean(username);
		try {
			while (res.next()) {
				String currentPageCode = res.getString(1);
				CustomPageConfig pageConfig = pageUserBean.getConfig().get(currentPageCode);
				if (null == pageConfig) {
					int frames = this.getPageModelframe(currentPageCode);
					pageConfig = new CustomPageConfig(currentPageCode, frames);
					pageUserBean.getConfig().put(currentPageCode, pageConfig);
				}
				int currentFramePos = res.getInt(2);
				if (currentFramePos >= pageConfig.getConfig().length) {
					ApsSystemUtils.getLogger().severe("Posizione " + currentFramePos + " incompatibile con pagina '" + currentPageCode + "' " +
							"- utente " + username);
					continue;
				}
				String currentShowletCode = res.getString(3);
				String currentConfig = res.getString(4);
				Widget showlet = this.createShowletFromRecord(currentShowletCode, currentConfig);
				pageConfig.getConfig()[currentFramePos] = showlet;
				int status = res.getInt(5);
				pageConfig.getStatus()[currentFramePos] = status;
			}
		} catch (Throwable t) {
			this.processDaoException(t, "Error while parsing the result set", "createPageUserConfigBeanFromResultSet");
		}
		return pageUserBean;
	}

	@Override
	public void removeUserPageConfig(String username, String pageCode, Integer framePosition) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			String query = (null == username) ? DELETE_PAGE_CONFIGS : DELETE_PAGE_USER_CONFIGS;
			if (null != framePosition) {
				query += " AND framepos = ?";
			}
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(query);
			int index = 0;
			if (null != username) {
				stat.setString(++index, username);
			}
			stat.setString(++index, pageCode);
			if (null != framePosition) {
				stat.setInt(++index, framePosition);
			}
			stat.execute();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error while updating user configs", "removeUserPageConfig");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	@Override
	public void removeUnauthorizedShowlet(String username, String showletCode) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_UNAUTHORIZATED_SHOWLETS);
			stat.setString(1, username);
			stat.setString(2, showletCode);
			stat.execute();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error while removing unauthorized showlet", "removeUnauthorizedShowlet");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}

	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	protected IPageModelManager getPageModelManager() {
		return _pageModelManager;
	}
	public void setPageModelManager(IPageModelManager pageModelManager) {
		this._pageModelManager = pageModelManager;
	}

	public IWidgetTypeManager getWidgetTypeManager() {
		return _widgetTypeManager;
	}

	public void setWidgetTypeManager(IWidgetTypeManager widgetTypeManager) {
		this._widgetTypeManager = widgetTypeManager;
	}

	private IPageManager _pageManager;
	private IPageModelManager _pageModelManager;

	private IWidgetTypeManager _widgetTypeManager;

	private final String ADD_USER_CONFIG =
		"INSERT INTO jpmyportalplus_userpageconfig (username, pagecode, framepos, widgetcode, config, closed) VALUES (?, ?, ?, ?, ?, ?)";

	private final String DELETE_PAGE_USER_CONFIG =
		"DELETE FROM jpmyportalplus_userpageconfig WHERE username = ? AND pagecode = ? AND framepos = ?";

	private final String DELETE_PAGE_CONFIGS =
		"DELETE FROM jpmyportalplus_userpageconfig WHERE pagecode = ? ";

	private final String DELETE_PAGE_USER_CONFIGS =
		"DELETE FROM jpmyportalplus_userpageconfig WHERE username = ? AND pagecode = ? ";

	private final String GET_USER_CONFIG =
		"SELECT pagecode, framepos, widgetcode, config, closed FROM jpmyportalplus_userpageconfig WHERE username = ? ORDER BY pagecode";

	private final String GET_CONFIGURED_SHOWLET_CODE =
		"SELECT widgetcode FROM jpmyportalplus_userpageconfig GROUP BY widgetcode";

	private final String DELETE_USER_CONFIG_BY_SHOWLET_CODE =
		"DELETE FROM jpmyportalplus_userpageconfig WHERE widgetcode = ? ";

	private final String DELETE_UNAUTHORIZATED_SHOWLETS =
			"DELETE FROM jpmyportalplus_userpageconfig WHERE username = ? AND widgetcode = ? ";

}
