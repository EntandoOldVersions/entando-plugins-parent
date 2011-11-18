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
package com.agiletec.plugins.jpaddressbook.apsadmin.addressbook;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.apsadmin.system.entity.AbstractApsEntityAction;
import com.agiletec.plugins.jpaddressbook.aps.system.JpaddressbookSystemConstants;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.IAddressBookManager;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model.Contact;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model.IContact;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.IUserProfileManager;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.model.IUserProfile;

/**
 * @author E.Santoboni
 */
public class ContactAction extends AbstractApsEntityAction implements IContactAction {
	
	@Override
	public void validate() {
		if (this.getContact() != null) {
			super.validate();
		}
	}
	
	@Override
	public IApsEntity getApsEntity() {
		return this.getContact().getContactInfo();
	}
	
	public IContact getContact() {
		return (IContact) this.getRequest().getSession().getAttribute(SESSION_PARAM_NAME_CURRENT_CONTACT);
	}
	
	@Override
	public String view() {
		return this.edit();
	}
	
	@Override
	public String createNew() {
		try {
			IUserProfile defaultProfile = this.getUserProfileManager().getDefaultProfileType();
			defaultProfile.disableAttributes(JpaddressbookSystemConstants.ATTRIBUTE_DISABLING_CODE_ON_MANAGE_CONTACT);
			Contact contact = new Contact(defaultProfile);
			contact.setOwner(this.getCurrentUser().getUsername());
			this.getRequest().getSession().setAttribute(SESSION_PARAM_NAME_CURRENT_CONTACT, contact);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "createNew");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String edit() {
		try {
			IContact contact = this.getAddressBookManager().getContact(this.getEntityId());
			if (null == contact) {
				this.addFieldError("contactKey", this.getText("*CONTATTO NULLO*"));//TODO LABEL DA MODIFICARE
				return INPUT;
			}
			if (!contact.isPublicContact() && !this.getCurrentUser().getUsername().equals(contact.getOwner())) {
				this.addFieldError("contactKey", this.getText("*CONTATTO NON PUBBLICO NON AUTORIZZATO*"));//TODO LABEL DA MODIFICARE
				return INPUT;
			}
			contact.getContactInfo().disableAttributes(JpaddressbookSystemConstants.ATTRIBUTE_DISABLING_CODE_ON_MANAGE_CONTACT);
			this.getRequest().getSession().setAttribute(SESSION_PARAM_NAME_CURRENT_CONTACT, contact);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String save() {
		try {
			IContact contact = this.getContact();
			if (contact == null) {
				return FAILURE;
			}
			if (null == contact.getId()) {
				this.getAddressBookManager().addContact(contact);
			} else {
				this.getAddressBookManager().updateContact(this.getCurrentUser().getUsername(), contact);
			}
			this.getRequest().getSession().removeAttribute(SESSION_PARAM_NAME_CURRENT_CONTACT);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected IAddressBookManager getAddressBookManager() {
		return _addressBookManager;
	}
	public void setAddressBookManager(IAddressBookManager addressBookManager) {
		this._addressBookManager = addressBookManager;
	}
	
	protected IUserProfileManager getUserProfileManager() {
		return _userProfileManager;
	}
	public void setUserProfileManager(IUserProfileManager userProfileManager) {
		this._userProfileManager = userProfileManager;
	}
	
	private IAddressBookManager _addressBookManager;
	private IUserProfileManager _userProfileManager;
	
}