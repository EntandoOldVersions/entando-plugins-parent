/*
 *
 * Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
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
 * Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
 *
 */
package com.agiletec.plugins.jpmyportal.aps.system.services.userconfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Showlet;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.aps.system.services.showlettype.IShowletTypeManager;
import com.agiletec.aps.system.services.showlettype.ShowletType;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpmyportal.aps.system.JpmyportalSystemConstants;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.model.PageModelUserConfigBean;

/**
 * This DAO tracks in the database the customization of the page models (and NOT the pages!) of the current
 * user. When an user customizes a page model by swapping two showlets or in any allowed manner, a new record
 * is created in the database. All the record of a single user are often referred as customization bean
 * @author M. Minnai
 */
public class PageModelUserConfigDAO extends AbstractDAO implements IPageModelUserConfigDAO {
	
	@Override
	public void syncCustomization(List<ShowletType> allowedShowlets, String voidShowletCode) {
		int configuredShowletSize = 0;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(GET_CONFIGURED_SHOWLET_CODE);
			res = stat.executeQuery();
			while (res.next()) {
				String currentCode = res.getString(1);
				ShowletType currentConfiguredShowlet = this.getShowletTypeManager().getShowletType(currentCode);
				if (null == currentConfiguredShowlet) {
					ApsSystemUtils.getLogger().info(JpmyportalSystemConstants.MYPORTAL_CONFIG_ITEM + ": deleting unknown showlet '"+currentCode+"' from the configuration bean");
					purgeConfigurationFromInvalidShowlets(conn, currentCode);
				} else {
					if (currentConfiguredShowlet != null && 
							!allowedShowlets.contains(currentConfiguredShowlet) && !currentCode.equals(voidShowletCode)) {
						ApsSystemUtils.getLogger().info(JpmyportalSystemConstants.MYPORTAL_CONFIG_ITEM + ": removing the no longer configurable showlet '"+currentCode+"' from the configuration bean");
						purgeConfigurationFromInvalidShowlets(conn, currentCode);
					} else {
						configuredShowletSize++;
					}
				}
			}
			ApsSystemUtils.getLogger().info(JpmyportalSystemConstants.MYPORTAL_CONFIG_ITEM + ": " + configuredShowletSize + " customizable showlet(s) found");
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error while cleaning the configuration database", "syncCustomization");
		} finally {
			this.closeDaoResources(res, stat, conn);
		}
	}
	
	/**
	 * Delete from customization database those record containing the code of the given showlet. 
	 * @param conn datasource connection
	 * @param code the code of the showlet to delete
	 * @throws ApsSystemException in case of error
	 */
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
	public PageModelUserConfigBean swapShowlets(PageModelUserConfigBean customization, String pageModelCode, int firstFrame, int secondFrame, Showlet[] showlets) {
		Connection conn = null;
		try {
			String username = customization.getUsername();
			Showlet[] customShowlets = null;
			Showlet tmp = null;
			conn = this.getConnection();
			conn.setAutoCommit(false);
			if (!customization.getConfig().containsKey(pageModelCode)) {
				throw new ApsSystemException("ERROR: the showlets to swap are containd in the unknown page model \""+pageModelCode+"\"");
			}
			customShowlets = customization.getConfig().get(pageModelCode);
			if (null != customShowlets[firstFrame]) {
				// AGGIORNA la voce nel db per la PRIMA showlet, già invertita
				this.transactUpdateUserConfig(conn, username, pageModelCode, firstFrame, showlets[secondFrame]);
			} else {
				//  CREA la voce nel db per la PRIMA showlet, già invertita
				this.transactSaveUserConfig(conn, username, pageModelCode, firstFrame, showlets[secondFrame]);
			}
			if (null != customShowlets[secondFrame]) {
				// AGGIORNA la voce nel db per la SECONDA showlet, già invertita
				this.transactUpdateUserConfig(conn, username, pageModelCode, secondFrame, showlets[firstFrame]);			
			} else {
				// CREA la voce nel db per la SECONDA showlet, già invertita
				this.transactSaveUserConfig(conn, username, pageModelCode, secondFrame, showlets[firstFrame]);
			}
			// scambiamo gli oggetti del bean di personalizzazione
			tmp = showlets[firstFrame];
			customShowlets[firstFrame] = showlets[secondFrame];
			customShowlets[secondFrame] = tmp;
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error detected while swapping showlets", "swapShowlets");
			customization = null;
		} finally {
			this.closeConnection(conn);
		}
		return customization;
	}
	
	@Override
	public PageModelUserConfigBean voidFrame(PageModelUserConfigBean customization, String pageModelCode, int targetFrame, String defaultShowletCode) {
		Connection conn = null;
		try {
			Showlet[] showlets = null;
			Showlet voidShowlet = this.createShowletFromRecord(defaultShowletCode, null);
			conn = this.getConnection();
			conn.setAutoCommit(false);
			if (!customization.getConfig().containsKey(pageModelCode)) {
				throw new ApsSystemException("ERROR: the frame to void belongs to the unknown page model \""+pageModelCode+"\"");
			}
			showlets = customization.getConfig().get(pageModelCode);
			if (null != showlets[targetFrame]) {
				this.transactUpdateUserConfig(conn, customization.getUsername(), pageModelCode, targetFrame, voidShowlet);
			} else {
				this.transactSaveUserConfig(conn, customization.getUsername(), pageModelCode, targetFrame, voidShowlet);
			}
			conn.commit();
			showlets[targetFrame] = voidShowlet;
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error while voiding a frame", "voidFrame");
			customization = null;
		} finally {
			this.closeConnection(conn);
		}
		return customization;
	}
	
	@Override
	public PageModelUserConfigBean assignShowletToFrame(PageModelUserConfigBean customization, String pageModelCode, int targetFrame, Showlet showlet) {
		Connection conn = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			if (null == customization.getConfig().get(pageModelCode)[targetFrame]) {
				this.transactSaveUserConfig(conn, customization.getUsername(), pageModelCode, targetFrame, showlet);
			} else {
				this.transactUpdateUserConfig(conn, customization.getUsername(), pageModelCode, targetFrame, showlet);
			}
			// modifica il bean in sessione
			customization.getConfig().get(pageModelCode)[targetFrame] = showlet;
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error while associating the showlet to the frame", "assignShowletToFrame");
			customization = null;
		} finally {
			this.closeConnection(conn);
		}
		return customization;
	}
	
	@Override
	public PageModelUserConfigBean getUserConfig(String username) {
		Connection conn = null;
		PageModelUserConfigBean result = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_USER_CONFIG);
			stat.setString(1, username);
			res = stat.executeQuery();
			result = this.createPageModelUserConfigBeanFromResultSet(username, res);
		} catch (Throwable t) {
			processDaoException(t, "Error while saving user customization", "getUserConfig");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return result;
	}
	
	/**
	 * Get the number of frames of the given page model.
	 * @param code The page model
	 * @return the number of frames of the page model
	 * @throws ApsSystemException in case of error
	 */
	private int getPageModelframe(String code) throws ApsSystemException {
		PageModel current = this.getPageModelManager().getPageModel(code);
		try {
			return current.getFrames().length;
		} catch (Throwable t) {
			throw new ApsSystemException("ERROR: unknown page model \""+code+"\"");
		}
	}	
	
	/**
	 * Create a showlet with a given code and optional configuration. 
	 * @param showletcode the code of the showlet
	 * @param config the configuration for the current showlet
	 * @return a new object with the given code and configuration
	 * @throws ApsSystemException if the given code is unknown or faulting XML configuration
	 */
	private Showlet createShowletFromRecord(String showletcode, String config) throws ApsSystemException {
		Showlet newShowlet = new Showlet();
		try {
			ShowletType inheritedType = this.getShowletTypeManager().getShowletType(showletcode);
			newShowlet.setType(inheritedType);
			ApsProperties properties = new ApsProperties();
			if (null != config && config.trim().length() > 0) {
				try {
					properties.loadFromXml(config);
				} catch (Throwable t) {
					String msg = "Error setting showlet properties for type '" + showletcode + "'";
					ApsSystemUtils.logThrowable(t, this, "createShowletFromRecord", msg);
					throw new ApsSystemException(msg, t);
				}
			} else {
				properties = inheritedType.getConfig();
			}
			newShowlet.setConfig(properties);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "createShowletFromRecord");
			throw new ApsSystemException("Error create Showlet From Record for type '" + showletcode + "'", t);
		}
		return newShowlet;
	}
	
	/**
	 * Create a 'PageModelUserConfigBean' object containing the customized configuration of a user from a 
	 * result set.
	 * @param username the associated username
	 * @param res the result set as returned by the database
	 * @return the object containing the customized configuration of the page models associated to the user,
	 * null otherwise
	 */
	private PageModelUserConfigBean createPageModelUserConfigBeanFromResultSet(String username, ResultSet res) {
		PageModelUserConfigBean pageModelUserBean = null;
		try {
			// SE abbiamo una risposta dal DB allora settiamo l'username nel bean.
			if (null != res) {
				Map<String, Showlet[]> userAssociatedMap = new HashMap<String, Showlet[]>();
				// assegnamo il nome utente al bean e la relativa mappa vuota
				while (res.next()) {
					if (pageModelUserBean == null) {
						pageModelUserBean = new PageModelUserConfigBean();
						pageModelUserBean.setUsername(username);
						pageModelUserBean.setConfig(userAssociatedMap);
					}
					// leggiamo il record
					String currentPageModel = res.getString(1);
					int currentFramepos = res.getInt(2);
					String currentShowletCode = res.getString(3);
					String currentConfig = res.getString(4);
					if (!userAssociatedMap.containsKey(currentPageModel)) {
						/*
						 * crea l'array di showlets della giusta dimensione;
						 * inserisci la showlet nell'array nella giusta posizione data dal numero di frame;
						 * aggiungi la voce in mappa con chiave 'pagemodel';
						 */
						int showletArraySize = getPageModelframe(currentPageModel);
						Showlet[] currentShowletArray = new Showlet[showletArraySize];
						currentShowletArray[currentFramepos] = createShowletFromRecord(currentShowletCode,currentConfig);					
						userAssociatedMap.put(currentPageModel, currentShowletArray);
					} else {
						/*
						 * ottieni l'array del pagmodel corrente;
						 * inserisci la showlet nell'array nella giusta posizione data dal numero di frame;
						 */
						Showlet[] currentShowletArray = userAssociatedMap.get(currentPageModel);
						currentShowletArray[currentFramepos] = createShowletFromRecord(currentShowletCode,currentConfig);
					}
				}
			}
		} catch (Throwable t) {
			processDaoException(t, "Error while parsing the result set", "createPageModelUserConfigBeanFromResultSet");
		}
		return pageModelUserBean;
	}
	
	/**
	 * Save in the database a new customization for the given user
	 * @param username the user owning the new customization
	 * @param pageModelCode The page model modified in its showlets
	 * @param framepos the frame modified
	 * @param showlet the showlet associated
	 * @throws ApsSystemException invoked if the frame is not valid or the page model is unknown.
	 */
	@Override
	public void saveUserConfig(String username, String pageModelCode, int framepos, Showlet showlet) {
		Connection conn = null;
		String showletCode = null;
		String showletConfig = null;
		PreparedStatement stat = null;
		try {
			int maxAllowedFrame = getPageModelframe(pageModelCode);
			conn = this.getConnection();
			conn.setAutoCommit(false);
			if (null != showlet.getConfig()) {
				showletConfig = showlet.getConfig().toXml();
			}
			showletCode = showlet.getType().getCode();
			stat = conn.prepareStatement(ADD_USER_CONFIG);
			stat.setString(1, username);
			stat.setString(2, pageModelCode);
			if (framepos < maxAllowedFrame) {
				stat.setInt(3, framepos);
			} else {
				throw new ApsSystemException("ERROR: invalid frame position for the given page model");
			}
			stat.setString(4, showletCode);
			if (null != showletConfig) {
				stat.setString(5, showletConfig);
			} else {
				stat.setNull(5, java.sql.Types.VARCHAR);
			}
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error while saving the new configuration", "saveUserConfig");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	private void transactSaveUserConfig(Connection conn, String username, String pageModelCode, int framepos, Showlet showlet) {
		String showletCode = null;
		String showletConfig = null;
		PreparedStatement stat = null;
		try {
			int maxAllowedFrame = getPageModelframe(pageModelCode);
			if (null != showlet.getConfig()) {
				showletConfig = showlet.getConfig().toXml();
			}
			showletCode = showlet.getType().getCode();
			stat = conn.prepareStatement(ADD_USER_CONFIG);
			stat.setString(1, username);
			stat.setString(2, pageModelCode);
			if (framepos < maxAllowedFrame) {
				stat.setInt(3, framepos);
			} else {
				throw new ApsSystemException("ERROR: invalid frame position in the current page model");
			}
			stat.setString(4, showletCode);
			if (null != showletConfig) {
				stat.setString(5, showletConfig);
			} else {
				stat.setNull(5, java.sql.Types.VARCHAR);
			}
			stat.executeUpdate();
		} catch (Throwable t) {
			processDaoException(t, "Error while saving user configuration", "transactSaveUserConfig");
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	@Override
	public void removeUserConfig(String username, String pageModelCode, int framepos) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_USER_CONFIG);
			stat.setString(1, username);
			stat.setString(2, pageModelCode);
			stat.setInt(3, framepos);
			stat.execute();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error while removing a record of the customization", "removeUserConfig");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void updateUserConfig(String username, String pageModelCode, int framepos, Showlet showlet) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			int maxAllowedFrame = getPageModelframe(pageModelCode);
			ApsProperties currentConfig = showlet.getConfig();
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_USER_CONFIG);
			stat.setString(1, showlet.getType().getCode());
			if (null != currentConfig) {
				stat.setString(2, showlet.getConfig().toXml());
			} else {
				stat.setNull(2, java.sql.Types.VARCHAR);
			}
			stat.setString(3, username);
			stat.setString(4, pageModelCode);
			if (framepos < maxAllowedFrame) {
				stat.setInt(5, framepos);
			} else {
				throw new ApsSystemException("ERROR: invalid position for the given page model");
			}
			stat.execute();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Error while updating a customization record", "updateUserConfig");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	private void transactUpdateUserConfig(Connection conn, String username, String pageModelCode, int framepos, Showlet showlet) {
		PreparedStatement stat = null;
		try {
			int maxAllowedFrame = getPageModelframe(pageModelCode);
			ApsProperties currentConfig = showlet.getConfig();
			stat = conn.prepareStatement(UPDATE_USER_CONFIG);
			stat.setString(1, showlet.getType().getCode());
			if (null != currentConfig) {
				stat.setString(2, showlet.getConfig().toXml());
			} else {
				stat.setNull(2, java.sql.Types.VARCHAR);
			}
			stat.setString(3, username);
			stat.setString(4, pageModelCode);
			if (framepos < maxAllowedFrame) {
				stat.setInt(5, framepos);
			} else {
				throw new ApsSystemException("ERROR: invalid position for the given page model");
			}
			stat.execute();
		} catch (Throwable t) {
			processDaoException(t, "Error while updating a customization record", "transactUpdateUserConfig");
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	protected IPageModelManager getPageModelManager() {
		return _pageModelManager;
	}
	public void setPageModelManager(IPageModelManager pageModelManager) {
		this._pageModelManager = pageModelManager;
	}
	
	protected IShowletTypeManager getShowletTypeManager() {
		return _showletTypeManager;
	}
	public void setShowletTypeManager(IShowletTypeManager showletTypeManager) {
		this._showletTypeManager = showletTypeManager;
	}
	
	private IPageModelManager _pageModelManager;
	private IShowletTypeManager _showletTypeManager;
	
	private final String ADD_USER_CONFIG = 
		"INSERT INTO jpmyportal_userpagemodelconfig (username, pagemodelcode, framepos, showletcode, config) VALUES ( ? , ? , ? , ? , ? )";
	
	private final String DELETE_USER_CONFIG = 
		"DELETE FROM jpmyportal_userpagemodelconfig WHERE username = ? AND pageModelCode = ? AND framepos = ?";
	
	private final String GET_USER_CONFIG = 
		"SELECT pagemodelcode, framepos, showletcode, config FROM jpmyportal_userpagemodelconfig WHERE username = ? ORDER BY pagemodelcode";
	
	private final String UPDATE_USER_CONFIG = 
		"UPDATE jpmyportal_userpagemodelconfig SET showletcode = ? , config = ? WHERE username = ? AND pagemodelcode = ? AND framepos = ?";
	
	private final String GET_CONFIGURED_SHOWLET_CODE = 
		"SELECT showletcode FROM jpmyportal_userpagemodelconfig GROUP BY showletcode";
	
	private final String DELETE_USER_CONFIG_BY_SHOWLET_CODE = 
		"DELETE FROM jpmyportal_userpagemodelconfig WHERE showletcode = ? ";
	
}