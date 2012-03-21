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
package com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.event.ReloadingEntitiesReferencesEvent;
import com.agiletec.aps.system.common.entity.event.ReloadingEntitiesReferencesObserver;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.parse.EntityHandler;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model.Contact;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model.ContactRecord;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model.IContact;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.IUserProfileManager;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.event.ProfileChangedEvent;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.event.ProfileChangedObserver;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.model.IUserProfile;

/**
 * @author E.Santoboni
 */
public class AddressBookManager extends AbstractService 
		implements IAddressBookManager, ReloadingEntitiesReferencesObserver, ProfileChangedObserver {
	
	@Override
	public void init() throws Exception {
		ApsSystemUtils.getLogger().config(this.getName() + ": initialized ");
	}
	
	@Override
	public void updateFromProfileChanged(ProfileChangedEvent event) {
		switch (event.getOperationCode()) {
		case ProfileChangedEvent.INSERT_OPERATION_CODE:
			this.saveProfile(event.getProfile());
			break;
		case ProfileChangedEvent.REMOVE_OPERATION_CODE:
			this.getAddressBookDAO().deleteContact(event.getProfile().getId());
			break;
		case ProfileChangedEvent.UPDATE_OPERATION_CODE:
			this.saveProfile(event.getProfile());
			break;
		}
	}
	
	protected void saveProfile(IUserProfile userProfile) {
		try {
			if (userProfile != null) {
				this.getAddressBookDAO().deleteContact(userProfile.getId());
				if (!userProfile.isPublicProfile()) return;
				Contact contact = new Contact(userProfile);
				contact.setOwner(userProfile.getUsername());
				contact.setPublicContact(userProfile.isPublicProfile());
				contact.setId(userProfile.getId());
				this.getAddressBookDAO().addContact(contact);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveProfile");
		}
	}
	
	@Override
	public void addContact(IContact contact) throws ApsSystemException {
		try {
			if (null == contact.getId()) {
				int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
				((Contact) contact).setId(String.valueOf(key));
			}
			this.getAddressBookDAO().addContact(contact);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addContact");
			throw new ApsSystemException("Error on adding Contact", t);
		}
	}
	
	@Override
	public void deleteContact(String owner, String contactKey) throws ApsSystemException {
		try {
			if (!checkOwner(owner, contactKey)) {
				throw new ApsSystemException("Deleting Contact " + contactKey 
						+ " - not present or wrong owner " + owner);
			}
			this.getAddressBookDAO().deleteContact(contactKey);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteContact");
			throw new ApsSystemException("Error on removing Contact", t);
		}
	}
	
	@Override
	public void updateContact(String owner, IContact contact) throws ApsSystemException {
		try {
			if (!checkOwner(owner, contact.getId())) {
				throw new ApsSystemException("Updating Contact " + contact.getId() 
						+ " - not present or wrong owner " + owner);
			}
			this.getAddressBookDAO().updateContact(contact);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "updateContact");
			throw new ApsSystemException("Error on updating Contact", t);
		}
	}
	
	private boolean checkOwner(String owner, String contactKey) throws ApsSystemException {
		EntitySearchFilter filter = new EntitySearchFilter(CONTACT_OWNER_FILTER_KEY, false, owner, false);
		EntitySearchFilter[] filters = {filter};
		List<String> contactsKeys = this.getContacts(filters);
		return contactsKeys.contains(contactKey);
	}
	
	@Override
	public IContact getContact(String contactKey) throws ApsSystemException {
		Contact contact = null;
		try {
			ContactRecord contactVo = (ContactRecord) this.getAddressBookDAO().loadEntityRecord(contactKey);
			if (contactVo != null) {
				IUserProfile profile = (IUserProfile) this.createEntityFromXml(contactVo.getTypeCode(), contactVo.getXml());
				contact = new Contact(profile);
				contact.setId(contactVo.getId());
				contact.setOwner(contactVo.getOwner());
				contact.setPublicContact(contactVo.isPublicContact());
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContact");
			throw new ApsSystemException("Errore recupero getContact", t);
		}
		return contact;
	}
	
	protected IApsEntity createEntityFromXml(String entityTypeCode, String xml) throws ApsSystemException {
		try {
			IApsEntity entityPrototype = this.getUserProfileManager().getProfileType(entityTypeCode);
			SAXParserFactory parseFactory = SAXParserFactory.newInstance();			
			SAXParser parser = parseFactory.newSAXParser();
			InputSource is = new InputSource(new StringReader(xml));
			EntityHandler handler = this.getEntityHandler();
			handler.initHandler(entityPrototype, "profile", null);
			parser.parse(is, handler);
			return entityPrototype;
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "createEntityFromXml");
			throw new ApsSystemException("Errore caricamento entità", t);
		}
	}
	
	protected List<String> getContacts(EntitySearchFilter[] filters) throws ApsSystemException {
		List<String> idList = null;
		filters = this.addOrderFilter(filters);
		try {
			idList = this.getAddressBookSearcherDAO().searchId(filters);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContacts");
			throw new ApsSystemException("Error on search contacts", t);
		}
		return idList;
	}
	
	@Override
	public List<String> getAllowedContacts(String owner, EntitySearchFilter[] filters) throws ApsSystemException {
		List<String> idList = null;
		filters = this.addOrderFilter(filters);
		try {
			idList = this.getAddressBookSearcherDAO().searchAllowedContacts(owner, filters);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getAllowedContacts");
			throw new ApsSystemException("Error on search contacts", t);
		}
		return idList;
	}
	
	protected EntitySearchFilter[] addOrderFilter(EntitySearchFilter[] filters) {
		IUserProfile prototype = this.getUserProfileManager().getDefaultProfileType();
		if (prototype.getSurnameAttributeName() != null) {
			EntitySearchFilter filterToAdd = new EntitySearchFilter(prototype.getSurnameAttributeName(), true);
			filterToAdd.setOrder(EntitySearchFilter.ASC_ORDER);
			int len = 0;
			if (filters != null) {
				len = filters.length;
			}
			EntitySearchFilter[] newFilters = new EntitySearchFilter[len + 1];
			for(int i=0; i < len; i++){
				newFilters[i] = filters[i];
			}
			newFilters[len] = filterToAdd;
			return newFilters;
		} else {
			return filters;
		}
	}
	
	@Override
	public void reloadEntitiesReferences(ReloadingEntitiesReferencesEvent event) {
		List<String> entitiesId = this.getAllEntityId();
		for (int i=0; i<entitiesId.size(); i++) {
			String entityId = (String) entitiesId.get(i);
			try {
				IContact entity = this.getContact(entityId);
				if (entity != null) {
					this.getAddressBookDAO().reloadEntitySearchRecords(entityId, entity.getContactInfo());
				}
				ApsSystemUtils.getLogger().info("Reloaded search references for entity " + entityId);
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "reloadEntitySearchReferences", "Error on reloading search references entity : " + entityId);
			}
		}
	}
	
	protected List<String> getAllEntityId() {
		List<String> entitiesId = new ArrayList<String>();
		try {
			entitiesId = this.getAddressBookDAO().getAllEntityId();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getAllEntityId");
			throw new RuntimeException("Errore in Caricamento lista id entità", t);
		}
		return entitiesId;
	}
	
	
	
	protected IAddressBookDAO getAddressBookDAO() {
		return _addressBookDAO;
	}
	public void setAddressBookDAO(IAddressBookDAO addressBookDAO) {
		this._addressBookDAO = addressBookDAO;
	}
	
	protected IAddressBookSearcherDAO getAddressBookSearcherDAO() {
		return _addressBookSearcherDAO;
	}
	public void setAddressBookSearcherDAO(IAddressBookSearcherDAO addressBookSearcherDAO) {
		this._addressBookSearcherDAO = addressBookSearcherDAO;
	}
	
	protected EntityHandler getEntityHandler() {
		return this._entityHandler.getHandlerPrototype();
	}
	public void setEntityHandler(EntityHandler entityHandler) {
		this._entityHandler = entityHandler;
	}
	
	protected IUserProfileManager getUserProfileManager() {
		return _userProfileManager;
	}
	public void setUserProfileManager(IUserProfileManager userProfileManager) {
		this._userProfileManager = userProfileManager;
	}
	
	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}
	
	private IAddressBookDAO _addressBookDAO;
	private IAddressBookSearcherDAO _addressBookSearcherDAO;
	
	private EntityHandler _entityHandler;
	
	private IUserProfileManager _userProfileManager;
	private IKeyGeneratorManager _keyGeneratorManager;
	
}