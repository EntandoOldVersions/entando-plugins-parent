/*
 *
 * Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
 * Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 */

package com.agiletec.plugins.jpavatar.aps.system.services.avatar;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpavatar.aps.system.JpAvatarSystemConstants;
import com.agiletec.plugins.jpavatar.aps.system.services.avatar.parse.AvatarConfigDOM;
import com.agiletec.plugins.jpavatar.aps.system.utils.MD5Util;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.IUserProfileManager;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.model.UserProfile;

/**
 * @author S.Puddu
 */
@Aspect
public class AvatarManager extends AbstractService implements IAvatarManager {

	@Override
	public void init() throws Exception {
		this.loadConfig();
		ApsSystemUtils.getLogger().info(this.getClass().getName() + " : inizialized");
	}

	/**
	 * Load the XML configuration containing service configuration.
	 * @throws ApsSystemException
	 */
	private void loadConfig() throws ApsSystemException {
		try {
			ConfigInterface configManager = this.getConfigManager();
			String xml = configManager.getConfigItem(JpAvatarSystemConstants.CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Configuration item not present: " + JpAvatarSystemConstants.CONFIG_ITEM);
			}
			AvatarConfigDOM configDOM = new AvatarConfigDOM();
			this.setConfig(configDOM.extractConfig(xml));
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "loadConfigs");
			throw new ApsSystemException("Error on AvatarManager startup", t);
		}
	}

	@Override
	public void updateConfig(AvatarConfig config) throws ApsSystemException {
		try {
			String xml = new AvatarConfigDOM().createConfigXml(config);
			this.getConfigManager().updateConfigItem(JpAvatarSystemConstants.CONFIG_ITEM, xml);
			this.setConfig(config);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "updateConfig");
			throw new ApsSystemException("Error updating jpavatar config", t);
		}
	}

	@Override
	public String getAvatar(String username) throws ApsSystemException {
		String url = null;
		try {
			if (this.getConfig().getStyle().equalsIgnoreCase(AvatarConfig.STYLE_LOCAL)) {
				String avatarFileName = null;

				StringBuffer urlBuffer = new StringBuffer();
				String sep = System.getProperty("file.separator");
				urlBuffer.append(this.getConfigManager().getParam(SystemConstants.PAR_RESOURCES_ROOT_URL));
				if (!urlBuffer.toString().endsWith(sep)) {
					urlBuffer.append(sep);
				}
				urlBuffer.append("plugins").append(sep).append("jpavatar").append(sep);

				File avatarResource = this.getAvatarResource(username);					
				if (null != avatarResource) avatarFileName = avatarResource.getName();
				
				if (StringUtils.isNotBlank(avatarFileName)) {
					url = urlBuffer.toString() + "avatar" + sep + avatarFileName;
				} else {
					url = urlBuffer.toString() + JpAvatarSystemConstants.DEFAULT_AVATAR_NAME;
				}

			} else if (this.getConfig().getStyle().equalsIgnoreCase(AvatarConfig.STYLE_GRAVATAR)) {
				url = this.getGravatarUrl() + this.getGravatarHash(username);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getAvatar");
			throw new ApsSystemException("Error getting avatar for user " + username, t);
		}
		return url;
	}

	@Override
	public File getAvatarResource(String username) {
		File avatarFileName = null;
		if (StringUtils.isNotBlank(username)) {
			String basePath = this.getAvatarDiskFolder() + AVATAR_SUBFOLDER;
			File dir = new File(basePath);

			String[] files = dir.list(new PrefixFileFilter(username.toLowerCase() + "."));
			if (null != files && files.length > 0) {
				File resFile = new File (basePath + System.getProperty("file.separator") + files[0]);
				if (resFile.exists()) {
					avatarFileName = new File (basePath + System.getProperty("file.separator") + files[0]);
				}
			}			
		}
		return avatarFileName;
	}

	public String getGravatarHash(String username) throws ApsSystemException {
		String hash = null;
		try {
			if (null == username) return null;
			UserProfile profile = (UserProfile) this.getUserProfileManager().getProfile(username);
			if (null != profile) {
				String emailAttr = profile.getMailAttributeName();
				if (null == emailAttr) return null;
				String email = (String) profile.getValue(emailAttr);
				if (null != email) {
					hash = MD5Util.md5Hex(email);
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getGravatarHash");
			throw new ApsSystemException("Error getting gravatar hash for user " + username, t);
		}
		return hash;
	}

	@Override
	public void saveAvatar(String username, File file, String filename) throws ApsSystemException {
		try {
			String path = this.createFullDiskPath(username.toLowerCase(), filename);
			if (null == path) {
				ApsSystemUtils.getLogger().warning("Impossible to save avatar for user " + username + " . Wrong filename: " + file.getName());
				return;
			}
			ApsSystemUtils.getLogger().finest("Saving avatar to position: " + path);
			File destFile = new File(path);
			FileUtils.copyFile(file, destFile);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveAvatar");
			throw new ApsSystemException("Error saving avatar for user " + username, t);
		}
	}

	private String createFullDiskPath(String username, String filename) {
		StringBuffer diskFolder = new StringBuffer(this.getAvatarDiskFolder()).append(AVATAR_SUBFOLDER).append(System.getProperty("file.separator"));
		int point = filename.lastIndexOf(".");
		if (point < 1) {
			return null;
		}
		diskFolder.append(username).append(filename.substring(point));
		String path = diskFolder.toString();
		return path;
	}

	@AfterReturning(
			pointcut="execution(* com.agiletec.aps.system.services.user.IUserManager.removeUser(..)) && args(key)")
	public void removeAvatar(Object key) throws ApsSystemException {
		String username = null;
		try {
			if (key instanceof String) {
				username = key.toString();
			} else if (key instanceof UserDetails) {
				UserDetails userDetails = (UserDetails) key;
				username = userDetails.getUsername();
			}
			username = username.toLowerCase();
			
			File fileToDelete = this.getAvatarResource(username);
			if (null != fileToDelete) {
				FileUtils.forceDelete(fileToDelete);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeAvatar");
			throw new ApsSystemException("Error deleting avatar for user " + username, t);
		}
	}

	public void setAvatarDiskFolder(String avatarDiskFolder) {
		this._avatarDiskFolder = avatarDiskFolder;
	}

	public String getAvatarDiskFolder() {
		try {
			if (null == this._avatarDiskFolder) {
				this._avatarDiskFolder = this.createAvatarDiskFolderPath(SystemConstants.PAR_RESOURCES_DISK_ROOT, File.separator);
				File dir = new File(this._avatarDiskFolder);
				if (!dir.exists()) {
					FileUtils.forceMkdir(dir);
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getAvatarDiskFolder");
			throw new RuntimeException("Error creating Avatar Disk Folder", t);
		}
		return _avatarDiskFolder;
	}

	private String createAvatarDiskFolderPath(String baseParamName, String separator) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.getConfigManager().getParam(baseParamName));
		if (!buffer.toString().endsWith(separator)) {
			buffer.append(separator);
		}
		buffer.append("plugins").append(separator).append("jpavatar").append(separator);
		return buffer.toString();
	}

	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	protected IUserProfileManager getUserProfileManager() {
		return _userProfileManager;
	}
	public void setUserProfileManager(IUserProfileManager userProfileManager) {
		this._userProfileManager = userProfileManager;
	}

	public String getGravatarUrl() {
		return _gravatarUrl;
	}
	public void setGravatarUrl(String gravatarUrl) {
		this._gravatarUrl = gravatarUrl;
	}

	public AvatarConfig getConfig() {
		return _config;
	}
	public void setConfig(AvatarConfig config) {
		this._config = config;
	}

	private String _avatarDiskFolder;
	private ConfigInterface _configManager;
	private IUserProfileManager _userProfileManager;
	private String _gravatarUrl;
	private AvatarConfig _config;
	public static final String AVATAR_SUBFOLDER = "avatar";

}
