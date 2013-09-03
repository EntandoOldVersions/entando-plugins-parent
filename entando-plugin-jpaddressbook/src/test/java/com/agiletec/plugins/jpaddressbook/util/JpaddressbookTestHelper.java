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
package com.agiletec.plugins.jpaddressbook.util;

import java.sql.Connection;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.AddressBookDAO;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model.Contact;

import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.aps.system.services.userprofile.model.UserProfile;

public class JpaddressbookTestHelper extends AbstractDAO {
	
	public JpaddressbookTestHelper(ApplicationContext applicationContext) {
		DataSource dataSource = (DataSource) applicationContext.getBean("servDataSource");
		this.setDataSource(dataSource);
		
		this._addressBookDAO = new AddressBookDAO();
		this._addressBookDAO.setDataSource(dataSource);
		ILangManager langManager = (ILangManager) applicationContext.getBean(SystemConstants.LANGUAGE_MANAGER);
		this._addressBookDAO.setLangManager(langManager);
		
		this._profileManager = (IUserProfileManager) applicationContext.getBean(SystemConstants.USER_PROFILE_MANAGER);
	}
	
	public void cleanAddressBook() {
		Connection conn = null;
		try {
			conn = this.getConnection();
			this.executeQuery(DELETE_CONTACT_ATTRIBUTE_ROLE_RECORDS, conn);
			this.executeQuery(DELETE_CONTACT_SEARCH_RECORDS, conn);
			this.executeQuery(DELETE_CONTACTS, conn);
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			closeConnection(conn);
		}
	}
	
	private void executeQuery(String query, Connection conn) throws Throwable {
		Statement stat = null;
		try {
			stat = conn.createStatement();
			stat.executeUpdate(query);
		} catch (Throwable t) {
			throw t;
		} finally {
			closeDaoResources(null, stat);
		}
	}
	
	public void addContact(String id, String owner, 
			boolean publicContact, String fullname, String email, Date birthdate, String lang) {
		IUserProfile profile = this.createUserProfile(id, fullname, email, birthdate, lang);
		Contact contact = this.createContact(id, owner, publicContact, profile);
		this._addressBookDAO.addContact(contact);
	}
	
	public Contact createContact(String id, String owner, boolean publicContact, IUserProfile profile) {
		Contact contact = new Contact(profile);
		contact.setId(id);
		contact.setOwner(owner);
		contact.setPublicContact(publicContact);
		return contact;
	}
	
	public IUserProfile createUserProfile(String id, String fullname, String email, Date birthdate, String lang) {
		UserProfile profile = (UserProfile) this._profileManager.getDefaultProfileType();
		profile.setId(id);
		
		MonoTextAttribute fullnameAttr = (MonoTextAttribute) profile.getAttribute("fullname");
		fullnameAttr.setSearcheable(true);
		fullnameAttr.setText(fullname);
		
		MonoTextAttribute emailAttr = (MonoTextAttribute) profile.getAttribute("email");
		emailAttr.setText(email);
		
		DateAttribute birthdateAttr = (DateAttribute) profile.getAttribute("birthdate");
		birthdateAttr.setDate(birthdate);
		
		MonoTextAttribute langAttr = (MonoTextAttribute) profile.getAttribute("language");
		langAttr.setText(lang);
		
		return profile;
	}
	
	public Date setBirthdate(int year, int month, int day){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DAY_OF_MONTH, day);
		Date birthdate = new Date(calendar.getTimeInMillis());
		return birthdate;
	}
	
	private static final String DELETE_CONTACTS = 
		"DELETE FROM jpaddressbook_contacts";
	
	private static final String DELETE_CONTACT_SEARCH_RECORDS =
		"DELETE FROM jpaddressbook_contactsearch";
	
	private static final String DELETE_CONTACT_ATTRIBUTE_ROLE_RECORDS =
		"DELETE FROM jpaddressbook_attroles";
	
	private AddressBookDAO _addressBookDAO;
	protected IUserProfileManager _profileManager;
	
}