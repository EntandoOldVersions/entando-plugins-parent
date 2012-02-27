package com.agiletec.plugins.jpuserprofile.aps.tags;

import javax.servlet.jsp.JspException;

import org.apache.taglibs.standard.tag.common.core.OutSupport;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpuserprofile.aps.system.services.ProfileSystemConstants;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.IUserProfileManager;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.model.IUserProfile;

public class ProfileAttributeTag extends OutSupport {

	@Override
	public int doStartTag() throws JspException {
		try {
			IUserProfileManager userProfileManager = (IUserProfileManager) ApsWebApplicationUtils.getBean(ProfileSystemConstants.USER_PROFILE_MANAGER, this.pageContext);

			IUserProfile profile = userProfileManager.getProfile(this.getUsername());
			if (null == profile) return super.doStartTag();
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

	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
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
	private String _username;

}