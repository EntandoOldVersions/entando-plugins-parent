/*
*
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpavatar.aps.system.services.avatar;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.PrefixFileFilter;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpavatar.aps.system.JpAvatarSystemConstants;
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
		ApsSystemUtils.getLogger().info(this.getClass().getName() + " : inizialized");
	}

	public String getGravatarHash(String username) throws ApsSystemException {
		String hash = null;
		try {
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

	public String getAvatarURL(String username) throws ApsSystemException {
		String url = null;
		try {
			if (this.isGravatarActive()) {
				url = this.getGravatarUrl() + this.getGravatarHash(username);
			} else {
				StringBuffer buffer = new StringBuffer();
				String sep = System.getProperty("file.separator");
				buffer.append(this.getConfigManager().getParam(SystemConstants.PAR_RESOURCES_ROOT_URL));
				if (!buffer.toString().endsWith(sep)) {
					buffer.append(sep);
				}
				buffer.append("plugins").append(sep).append("jpavatar").append(sep);
				
				String avatarFileName = this.getAvatar(username);
				if (null != avatarFileName) {
					url = buffer.toString()+ "avatar" + sep + avatarFileName;
				} else {
					url = buffer.toString() + JpAvatarSystemConstants.DEFAULT_AVATAR_NAME;
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getAvatarURL");
			throw new ApsSystemException("Error getting gravatar hash for user " + username, t);
		}
		return url;
	}

	@Override
	public String getAvatar(String username) throws ApsSystemException {
		String path = null;
		try {
			File dir = new File(this.getAvatarDiskFolder() + AVATAR_SUBFOLDER);
			String[] files = dir.list(new PrefixFileFilter(username.toLowerCase() + "."));
			if (null != files && files.length > 0) {
				path = files[0];
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getAvatar");
			throw new ApsSystemException("Error getting avatar for user " + username, t);
		}
		return path;
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
			String filename = this.getAvatar(username);
			if (null != filename) {
				File fileToDelete = new File(this.createFullDiskPath(username, filename));
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

	@Override
	public String getAvatarDiskFolder() {
		try {
			if (null == this._avatarDiskFolder) {
				this._avatarDiskFolder = 	this.getAvatarProperty(SystemConstants.PAR_RESOURCES_DISK_ROOT, File.separator);
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

	public void setAvatarURL(String avatarURL) {
		this._avatarURL = avatarURL;
	}

	@Override
	public String getAvatarURL() {
		if (null == this._avatarURL) {
			this._avatarURL = this.getAvatarProperty(SystemConstants.PAR_RESOURCES_ROOT_URL, "/");
		}
		return _avatarURL;
	}


	private String getAvatarProperty(String baseParamName, String separator) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(this.getConfigManager().getParam(baseParamName));
		if (!buffer.toString().endsWith(separator)) {
			buffer.append(separator);
		}
		buffer.append("plugins").append(separator).append("jpavatar").append(separator);
		return buffer.toString();
	}

	@Override
	public boolean isGravatarActive() {
		return null != this.getUseGravatar() && this.getUseGravatar().equalsIgnoreCase("true");
	}
	
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	public String getUseGravatar() {
		return _useGravatar;
	}
	public void setUseGravatar(String useGravatar) {
		this._useGravatar = useGravatar;
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

	private String _avatarDiskFolder;
	private String _avatarURL;
	private ConfigInterface _configManager;
	private IUserProfileManager _userProfileManager;
	public static final String AVATAR_SUBFOLDER = "avatar";
	private String _useGravatar;
	private String _gravatarUrl;
	
}