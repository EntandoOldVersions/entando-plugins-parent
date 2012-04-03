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
package com.agiletec.plugins.jpuserreg.aps.internalservlet.registration;

import java.util.Collection;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Showlet;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.entity.AbstractApsEntityAction;
import com.agiletec.plugins.jpuserprofile.aps.system.services.ProfileSystemConstants;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.IUserProfileManager;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.model.IUserProfile;
import com.agiletec.plugins.jpuserreg.aps.JpUserRegSystemConstants;
import com.agiletec.plugins.jpuserreg.aps.system.services.userreg.IUserRegManager;

/**
 * Action to manage User Account Registration Requests
 * 
 * @author S.Puddu
 * @author E.Mezzano
 * @author G.Cocco
 * */
public class UserRegistrationAction extends AbstractApsEntityAction implements IUserRegistrationAction {
	
	@Override
	public void validate() {
		try {
			if (this.getUserProfile()!=null) {
				super.validate();
				this.getUserRegManager().clearOldAccountRequests();// FIXME Verificare se è il caso di richiamarlo qui
				if (this.existsUser(this.getUsername())) {
					this.addFieldError("username", this.getText("jpuserreg.error.duplicateUser"));
				}
				if (!this.isPrivacyPolicyAgreement()) {
					this.addFieldError("privacyPolicyAgreement", this.getText("jpuserreg.error.privacyPolicyAgreement.required"));
				}
				this.checkEmailAddress();
			}
		} catch (Throwable t) {
			throw new RuntimeException("Error validation of request for account activation" + this.getUsername(), t);
		}
	}
	
	@Override
	public String createNew() {
		String profileTypeCode = this.getProfileTypeCode();
		try {
			boolean allowed = false;
			if (profileTypeCode!=null) {
				IUserProfile userProfile = (IUserProfile) this.getUserProfileManager().getProfileType(profileTypeCode);
				if (userProfile!=null) {
					if (userProfile.getAttributeByRole(ProfileSystemConstants.ATTRIBUTE_ROLE_MAIL)==null) {// Verifica che contenga l'attributo della mail
						ApsSystemUtils.getLogger().warning("Registration attempt with profile " + profileTypeCode + " missing email address");
					} else {
						userProfile.disableAttributes(JpUserRegSystemConstants.ATTRIBUTE_DISABLING_CODE_ON_REGISTRATION);
						this.setUserProfile(userProfile);
						allowed = true;
					}
				}
			}
			if (!allowed) {
				this.addFieldError("profileTypeCode", this.getText("jpuserreg.error.profileType.required"));
				return INPUT;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "createNew");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String edit() {
		this.addActionError(this.getText("jpuserreg.error.operation.unsupported"));
		return FAILURE;
	}
	
	@Override
	public String view() {
		this.addActionError(this.getText("jpuserreg.error.operation.unsupported"));
		return FAILURE;
	}
	
	/**
	 * It Adds user account and profile in to the system, 
	 * keeping disabled status until the end of registration process
	 * */
	@Override
	public String save() {
		try {
			IUserProfile userProfile = this.getUserProfile();
			if (userProfile!=null) {
				userProfile.setId(this.getUsername().trim());
				this._userRegManager.regAccount(userProfile);
				this.setUserProfile(null);
			} else {
				return "expired";
			}
		} catch (Throwable t) {
			this.addActionError(this.getText("jpuserreg.error.genericError"));
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Extract the typeCode from the current showlet.
	 * @return The type code extracted from the showlet.
	 */
	protected String extractTypeCode() {
		String typeCode = null;
		RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
		if (reqCtx != null) {
			Showlet showlet = (Showlet) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_SHOWLET);
			if (showlet != null) {
				ApsProperties config = showlet.getConfig();
				if (null != config) {
					String showletTypeCode = config.getProperty(JpUserRegSystemConstants.TYPECODE_SHOWLET_PARAM);
					if (showletTypeCode!=null && showletTypeCode.trim().length()>0) {
						typeCode = showletTypeCode.trim();
					}
				}
			}
		}
		return typeCode;
	}
	
	/**
	 * check if user exist
	 * @param username
	 * @return true if exist a user with this username, false if user not exist.
	 * @throws Throwable In error case.
	 */
	protected boolean existsUser(String username) throws Throwable {
		boolean exists = (username!=null && username.trim().length()>=0 && this.getUserManager().getUser(username.trim())!=null);
		return exists;
	}
	
	private void checkEmailAddress() throws ApsSystemException {
		String emailAttrName = this.getEmailAttrName();
		String email = (String) this.getUserProfile().getValue(emailAttrName);
		if (null!=email) {
			if (!email.equals(this.getEmailConfirm())) {
				this.addFieldError(emailAttrName, this.getText("jpuserreg.error.email.wrongConfirm"));
			} else if(this.verifyEmailAlreadyInUse(email)){
				this.addFieldError(emailAttrName, this.getText("jpuserreg.error.email.alreadyInUse"));
			}
		}
	}
	
	/**
	 * Verify if email already in use
	 * @param email
	 * @return
	 * @throws ApsSystemException
	 */
	private boolean verifyEmailAlreadyInUse(String email) throws ApsSystemException {
		try {
			Collection<String> usernames = this.getUserRegManager().getUsernamesByEmail(email);
			if (usernames.size()>0) {
				return true;
			}
		} catch (ApsSystemException e) {
			ApsSystemUtils.logThrowable(e, this, "verifyEmailAlreadyInUse");
			throw e;
		}
		return false;
	}
	
	@Override
	public IApsEntity getApsEntity() {
		return (IApsEntity) this.getRequest().getSession().getAttribute(SESSION_PARAM_NAME_REQ_PROFILE);
	}
	public IUserProfile getUserProfile() {
		return (IUserProfile) this.getRequest().getSession().getAttribute(UserRegistrationAction.SESSION_PARAM_NAME_REQ_PROFILE);
	}
	protected void setUserProfile(IUserProfile userProfile) {
		if (userProfile!=null) {
			this.getRequest().getSession().setAttribute(UserRegistrationAction.SESSION_PARAM_NAME_REQ_PROFILE, userProfile);
		} else {
			this.getRequest().getSession().removeAttribute(UserRegistrationAction.SESSION_PARAM_NAME_REQ_PROFILE);
		}
	}
	
	public String getEmailAttrName() {
		if (this._emailAttrName==null) {
			AttributeInterface attribute = this.getUserProfile().getAttributeByRole(ProfileSystemConstants.ATTRIBUTE_ROLE_MAIL);
			if (attribute!=null) {
				this._emailAttrName = attribute.getName();
			}
		}
		return this._emailAttrName;
	}
	
	public String getProfileTypeCode() {
		if (null==this._profileTypeCode) {
			this._profileTypeCode = this.extractTypeCode();
			if (this._profileTypeCode==null) {
				this._profileTypeCode = ProfileSystemConstants.DEFAULT_PROFILE_TYPE_CODE;
			}
		}
		return _profileTypeCode;
	}
	public void setProfileTypeCode(String profileTypeCode) {
		String showletTypeCode = this.extractTypeCode();
		this._profileTypeCode = (null==showletTypeCode) ? profileTypeCode : showletTypeCode;
	}
	
	public void setUsername(String username) {
		this._username = username;
	}
	public String getUsername() {
		return _username;
	}
	
	public void setEmailConfirm(String emailConfirm) {
		this._emailConfirm = emailConfirm;
	}
	public String getEmailConfirm() {
		return _emailConfirm;
	}
	
	public void setPrivacyPolicyAgreement(boolean privacyPolicyAgreement) {
		this._privacyPolicyAgreement = privacyPolicyAgreement;
	}
	public boolean isPrivacyPolicyAgreement() {
		return _privacyPolicyAgreement;
	}
	
	protected IUserRegManager getUserRegManager() {
		return _userRegManager;
	}
	public void setUserRegManager(IUserRegManager userRegManager) {
		this._userRegManager = userRegManager;
	}
	
	protected IUserProfileManager getUserProfileManager() {
		return _userProfileManager;
	}
	public void setUserProfileManager(IUserProfileManager userProfileManager) {
		this._userProfileManager = userProfileManager;
	}
	
	protected IUserManager getUserManager() {
		return _userManager;
	}
	public void setUserManager(IUserManager userManager) {
		this._userManager = userManager;
	}
	
	private String _emailAttrName;
	private String _profileTypeCode;
	private String _username;
	private String _emailConfirm;
	private boolean _privacyPolicyAgreement = false;
	
	private IUserRegManager _userRegManager;
	private IUserManager _userManager;
	private IUserProfileManager _userProfileManager;
	
}