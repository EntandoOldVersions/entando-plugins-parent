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
package com.agiletec.plugins.jpuserprofile.aps.tags;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.apache.taglibs.standard.tag.common.core.OutSupport;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpuserprofile.aps.system.services.ProfileSystemConstants;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.model.IUserProfile;

/**
 * Current User Profile tag.
 * Return a attribute value of the current user profile.
 * @author E.Santoboni
 */
public class CurrentProfileAttributeTag extends OutSupport {
	
	@Override
	public int doStartTag() throws JspException {
		HttpSession session = this.pageContext.getSession();
		try {
			UserDetails currentUser = (UserDetails) session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			if (currentUser == null || currentUser.getUsername().equals(SystemConstants.GUEST_USER_NAME) || null == currentUser.getProfile()) {
				ApsSystemUtils.getLogger().severe("User '" + currentUser + "' : Null user, or guest user or user without profile");
				return super.doStartTag();
			}
			IUserProfile profile = (IUserProfile) currentUser.getProfile();
			Object value = null;
			if (this.getKey().equals(ProfileSystemConstants.ATTRIBUTE_ROLE_FIRST_NAME) 
					|| this.getKey().equals(ProfileSystemConstants.ATTRIBUTE_ROLE_SURNAME) 
					|| this.getKey().equals(ProfileSystemConstants.ATTRIBUTE_ROLE_MAIL)) {
				value = profile.getAttributeByRole(this.getKey());
			} else {
				value = profile.getValue(this.getKey());
			}
			if (null == value) return super.doStartTag();
			if (this.getVar() != null) {
				this.pageContext.setAttribute(this.getVar(), value);
			} else {
				if (this.getEscapeXml()) {
					out(this.pageContext, this.getEscapeXml(), value);
				} else {
					this.pageContext.getOut().print(value);
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "doStartTag");
			throw new JspException("Error during tag initialization", t);
		}
		return super.doStartTag();
	}
	
	@Override
	public void release() {
		super.release();
		this.setVar(null);
		super.escapeXml = true;
		this.setKey(null);
	}
	
	/**
	 * Imposta l'attributo che definisce il nome della variabile di output.
	 * @param var Il nome della variabile.
	 */
	public void setVar(String var) {
		this._var = var;
	}

	/**
	 * Restituisce l'attributo che definisce il nome della variabile di output.
	 * @return Il nome della variabile.
	 */
	public String getVar() {
		return _var;
	}
	
	public String getKey() {
		return _key;
	}
	public void setKey(String key) {
		this._key = key;
	}
	
	/**
	 * Determina se effettuare l'escape dei caratteri speciali nella label ricavata.
	 * @return True nel caso si debba effettuare l'escape, false in caso contrario.
	 */
	public boolean getEscapeXml() {
		return super.escapeXml;
	}
	
	/**
	 * Setta se effettuare l'escape dei caratteri speciali nella label ricavata.
	 * @param escapeXml True nel caso si debba effettuare l'escape, false in caso contrario.
	 */
	public void setEscapeXml(boolean escapeXml) {
		super.escapeXml = escapeXml;
	}
	
	private String _var;
	private String _key;
	
}