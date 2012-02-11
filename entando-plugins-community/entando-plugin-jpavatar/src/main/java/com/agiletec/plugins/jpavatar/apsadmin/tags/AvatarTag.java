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
package com.agiletec.plugins.jpavatar.apsadmin.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpavatar.aps.system.JpAvatarSystemConstants;
import com.agiletec.plugins.jpavatar.aps.system.services.avatar.IAvatarManager;

public class AvatarTag extends TagSupport {

	public int doEndTag() throws JspException {
		try {
			IAvatarManager avatarManager = (IAvatarManager) ApsWebApplicationUtils.getBean(JpAvatarSystemConstants.AVATAR_MANAGER, pageContext);
			IUserManager  userManager = (IUserManager) ApsWebApplicationUtils.getBean(SystemConstants.USER_MANAGER, pageContext);
			HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
			UserDetails currentUser = null;
			if (null != this.getUsername() && this.getUsername().trim().length() > 0) {
				currentUser = (UserDetails) userManager.getUser(this.getUsername());
			} else {
				currentUser = (UserDetails) request.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			}
			if (null == currentUser|| currentUser.getUsername().trim().length() == 0 || currentUser.getUsername().equalsIgnoreCase(SystemConstants.GUEST_USER_NAME)) {
				this.doOut(this.getNullAvatar(avatarManager));
			} else {

				//String avatarName = avatarManager.getAvatar(currentUser.getUsername());
				String avatarName = avatarManager.getAvatarURL(currentUser.getUsername());
				if (null != avatarName) {
					this.doOut(avatarName);
				} else {
					this.doOut(this.getNullAvatar(avatarManager));
				}

			}
		} catch (Throwable e) {
			ApsSystemUtils.logThrowable(e, this, "doEndTag");
			throw new JspException("Error in AvatarTag", e);
		}
		return EVAL_PAGE;
	}

	/**
	 * String returned when no avatar is found
	 * @param avatarManager
	 * @return
	 */
	private String getNullAvatar(IAvatarManager avatarManager) {
		String nullAvatar = null;
		if (null != this.getReturnDefaultAvatar() && this.getReturnDefaultAvatar().equalsIgnoreCase("true")) {
			nullAvatar = avatarManager.getAvatarURL() + JpAvatarSystemConstants.DEFAULT_AVATAR_NAME;
		} 
		return nullAvatar;
	}

	private void doOut(String value) throws Throwable {
		if (null != this.getVar() && this.getVar().trim().length() > 0) {
			this.pageContext.getRequest().setAttribute(this.getVar(), value);
		} else {
			pageContext.getOut().print(value);
		}
	}

	public void setReturnDefaultAvatar(String returnDefaultAvatar) {
		this._returnDefaultAvatar = returnDefaultAvatar;
	}
	public String getReturnDefaultAvatar() {
		return _returnDefaultAvatar;
	}

	public void setVar(String var) {
		this._var = var;
	}
	public String getVar() {
		return _var;
	}

	public void setUsername(String username) {
		this._username = username;
	}
	public String getUsername() {
		return _username;
	}

	private String _returnDefaultAvatar;
	private String _var;
	private String _username;
}
