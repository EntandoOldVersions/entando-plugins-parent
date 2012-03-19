/*
*
* Copyright 2009 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
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
* Copyright 2009 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
*/
package com.agiletec.plugins.jpavatar.apsadmin.avatar;

import java.io.File;
import java.io.FileInputStream;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpavatar.aps.system.services.avatar.IAvatarManager;
import com.agiletec.plugins.jpavatar.aps.system.utils.ImageInfo;

public class AvatarAction extends BaseAction implements IAvatarAction {

	@Override
	public void validate() {
		super.validate();
		this.checkAvatar();
	}

	private void checkAvatar() {
		File avatar = this.getAvatar();
		
		if (null != avatar && this.getAvatarFileName().trim().length() > 0) {
			String fileName = this.getAvatarFileName();
			String docType = fileName.substring(fileName.lastIndexOf('.')+1).trim();
			String[] types = this.getImageTypes().split(",");
			if (!isValidType(docType, types)) {
				String[] args = new String[1];
				args[0] = this.getImageTypes();
				this.addFieldError("Avatar", this.getText("jpavatar.avatar.invalidType",  args));
			}
			if (avatar.length() > this.getImageMaxSize() * 1024) {
				String[] args = new String [2];
				args[0] = new Long(avatar.length()).toString();
				args[1] = new Long(this.getImageMaxSize() * 1024).toString();
				this.addFieldError("Avatar", this.getText("jpavatar.avatar.sizeTooBig", args));
			}
			ImageInfo imageInfo = new ImageInfo();
			try {
				FileInputStream fis = new FileInputStream(this.getAvatar());
				imageInfo.setInput(fis);
				if (imageInfo.check()) {
					int width = imageInfo.getWidth();
					int heigth = imageInfo.getHeight();
					if (width > this.getImageMaxWidth()|| heigth > this.getImageMaxHeight()) {
						String[] args = new String [4];
						args[0] = new Integer(this.getImageMaxWidth()).toString();
						args[1] = new Integer(this.getImageMaxHeight()).toString();
						args[2] = new Integer(width).toString();
						args[3] = new Integer(heigth).toString();
						this.addFieldError("Avatar", this.getText("jpavatar.avatar.worngSize", args));
					}
				}
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "checkAvatar");
				throw new RuntimeException("error in avatar validation for user " + this.getCurrentUser().getUsername(), t);
			}
		}
	}
	
	private boolean isValidType(String docType, String[] rightTypes) {
		boolean isValid = false;
		if (rightTypes.length > 0) {
			for (int i = 0; i < rightTypes.length; i++) {
				if (docType.toLowerCase().equals(rightTypes[i].toLowerCase())) {
					isValid = true;
					break;
				}
			}
		} else {
			isValid = true;
		}
		return isValid;
	}

	public String getAvatarName() {
		String avatarName = null;
		try {
			avatarName = this.getAvatarManager().getAvatar(this.getCurrentUser().getUsername());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getAvatarName");
			throw new RuntimeException("error in getAvatar for " + this.getCurrentUser().getUsername(), t);
		}
		return avatarName;
	}
	
	public String edit() {
		return SUCCESS;
	}

	public String save() {
		try {
			if (null != this.getAvatar()) {
				this.getAvatarManager().saveAvatar(this.getCurrentUser().getUsername(), this.getAvatar(), this.getAvatarFileName());
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}

	public String bin() {
		try {
			this.addActionMessage(this.getText("jpavatar.message.confirmDelete"));
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "bin");
			return FAILURE;
		}
		return SUCCESS;
	}

	public String delete() {
		try {
			this.getAvatarManager().removeAvatar(this.getCurrentUser().getUsername());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "delete");
			return FAILURE;
		}
		return SUCCESS;
	}

	public void setImageTypes(String imageTypes) {
		this._imageTypes = imageTypes.toLowerCase();
	}
	public String getImageTypes() {
		return _imageTypes;
	}

	public void setImageMaxSize(int imageMaxSize) {
		this._imageMaxSize = imageMaxSize;
	}
	public int getImageMaxSize() {
		return _imageMaxSize;
	}

	public void setImageMaxWidth(int imageMaxWidth) {
		this._imageMaxWidth = imageMaxWidth;
	}
	public int getImageMaxWidth() {
		return _imageMaxWidth;
	}

	public void setImageMaxHeight(int imageMaxHeight) {
		this._imageMaxHeight = imageMaxHeight;
	}
	public int getImageMaxHeight() {
		return _imageMaxHeight;
	}

	public void setAvatar(File avatar) {
		this._avatar = avatar;
	}
	public File getAvatar() {
		return _avatar;
	}

	public void setAvatarContentType(String avatarContentType) {
		this._avatarContentType = avatarContentType;
	}
	public String getAvatarContentType() {
		return _avatarContentType;
	}

	public void setAvatarFileName(String avatarFileName) {
		this._avatarFileName = avatarFileName;
	}
	public String getAvatarFileName() {
		return _avatarFileName;
	}

	public void setAvatarManager(IAvatarManager avatarManager) {
		this._avatarManager = avatarManager;
	}
	protected IAvatarManager getAvatarManager() {
		return _avatarManager;
	}

	private File _avatar;
	private String _avatarContentType;
	private String _avatarFileName;
	private String _imageTypes;
	private int _imageMaxSize;
	private int _imageMaxWidth;
	private int _imageMaxHeight;
	private IAvatarManager _avatarManager;
}
