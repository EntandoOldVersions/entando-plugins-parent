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
package com.agiletec.plugins.jpaddressbook.aps.system.services.vcard.util;

import info.ineighborhood.cardme.EmailAddress;
import info.ineighborhood.cardme.MailingAddress;
import info.ineighborhood.cardme.Note;
import info.ineighborhood.cardme.PhoneNumber;
import info.ineighborhood.cardme.impl.EmailAddressImpl;
import info.ineighborhood.cardme.impl.MailingAddressImpl;
import info.ineighborhood.cardme.impl.NoteImpl;
import info.ineighborhood.cardme.impl.PhoneNumberImpl;
import info.ineighborhood.cardme.impl.VCardImpl;
import info.ineighborhood.cardme.types.EmailAddressType;
import info.ineighborhood.cardme.types.EncodingType;
import info.ineighborhood.cardme.types.MailingAddressType;
import info.ineighborhood.cardme.types.PhoneNumberType;
import info.ineighborhood.cardme.types.Type;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.EnumeratorAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.HypertextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.MonoTextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.NumberAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.TextAttribute;
import com.agiletec.plugins.jpaddressbook.aps.system.JpaddressbookSystemConstants;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.model.IContact;
import com.agiletec.plugins.jpaddressbook.aps.system.services.vcard.model.VCardContactField;
import com.agiletec.plugins.jpaddressbook.aps.system.services.vcard.parse.VCardAddress;

/**
 * @author A.Cocco
 */
public class VCardCreator {
	
	/**
	 * Create the VCARD
	 * @param contact
	 * @param vcards
	 * @return the VCard
	 */
	public String createVCard(IContact contact, List<VCardContactField> vcards) {
		String vcardMapping = "";
		String attributeValue = "";
		info.ineighborhood.cardme.VCard vcard = new VCardImpl();
		VCardAddress vcardAddress = new VCardAddress();
		try {
			vcard.setVersion(info.ineighborhood.cardme.VCard.VERSION_3_0);
			vcard.setRevisionDate(Calendar.getInstance());
			vcard.setTimeZone(Calendar.getInstance().getTimeZone());
			for (int i = 0; i < vcards.size(); i++) {
				attributeValue = "";
				VCardContactField card = vcards.get(i);
				String profileAttribute = card.getProfileAttribute().trim();
				if(card.isEnabled()) {
					attributeValue = this.getVcardFieldValue(contact, profileAttribute);
				}
				String code = card.getCode().trim();
				this.setVcardFields(attributeValue, code, vcard, vcardAddress);
			}
			vcard.addAddress(this.createAddress(vcardAddress, MailingAddressType.HOME_MAILING_ADDRESS));
			vcardMapping = vcard.toString();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "createVCard");
		}
		return vcardMapping;
	}
	
	private void setVcardFields(String attributeValue, String code, info.ineighborhood.cardme.VCard vcard, VCardAddress vcardAddress) {
		if(JpaddressbookSystemConstants.VCARD_FIELD_NAME.equals(code)) vcard.setFirstName(attributeValue);
		if(JpaddressbookSystemConstants.VCARD_FIELD_SURNAME.equals(code)) vcard.setLastName(attributeValue);
		if(JpaddressbookSystemConstants.VCARD_FIELD_TITLE.equals(code)) vcard.setJobTitle(attributeValue);
		if(JpaddressbookSystemConstants.VCARD_FIELD_ROLE.equals(code)) vcard.setRole(attributeValue);
		if(JpaddressbookSystemConstants.VCARD_FIELD_ORG.equals(code)) vcard.setOrganization(attributeValue);
		if(JpaddressbookSystemConstants.VCARD_FIELD_BDAY.equals(code) && validateDate(attributeValue)) vcard.setBirthday(createBirthday(attributeValue));
		if(JpaddressbookSystemConstants.VCARD_FIELD_ADDR_HOME_COUNTRY.equals(code)) vcardAddress.setCountry(attributeValue);
		if(JpaddressbookSystemConstants.VCARD_FIELD_ADDR_HOME_REGION.equals(code)) vcardAddress.setRegion(attributeValue);
		if(JpaddressbookSystemConstants.VCARD_FIELD_ADDR_HOME_STREET.equals(code)) vcardAddress.setStreet(attributeValue);
		if(JpaddressbookSystemConstants.VCARD_FIELD_ADDR_HOME_CITY.equals(code)) vcardAddress.setCity(attributeValue);
		if(JpaddressbookSystemConstants.VCARD_FIELD_ADDR_HOME_CAP.equals(code)) vcardAddress.setCap(attributeValue);
		//MailingAddressType.HOME_MAILING_ADDRESS
		//MailingAddressType.WORK_MAILING_ADDRESS
		if(JpaddressbookSystemConstants.VCARD_FIELD_TEL_CELL.equals(code)) vcard.addPhoneNumber(createPhoneNumber(attributeValue, PhoneNumberType.CELL_PHONE));
		if(JpaddressbookSystemConstants.VCARD_FIELD_TEL_HOME.equals(code)) vcard.addPhoneNumber(createPhoneNumber(attributeValue, PhoneNumberType.HOME_PHONE));
		if(JpaddressbookSystemConstants.VCARD_FIELD_TEL_WORK.equals(code)) vcard.addPhoneNumber(createPhoneNumber(attributeValue, PhoneNumberType.WORK_PHONE));
		if(JpaddressbookSystemConstants.VCARD_FIELD_TEL_MODEM.equals(code)) vcard.addPhoneNumber(createPhoneNumber(attributeValue, PhoneNumberType.MODEM_NUMBER));
		if(JpaddressbookSystemConstants.VCARD_FIELD_TEL_FAX.equals(code)) vcard.addPhoneNumber(createPhoneNumber(attributeValue, PhoneNumberType.FAX_NUMBER));
		if(JpaddressbookSystemConstants.VCARD_FIELD_EMAIL_FIRST.equals(code)) vcard.addEmailAddress(createEmailAddress(attributeValue));
		if(JpaddressbookSystemConstants.VCARD_FIELD_EMAIL_SECOND.equals(code)) vcard.addEmailAddress(createEmailAddress(attributeValue));
		if(JpaddressbookSystemConstants.VCARD_FIELD_EMAIL_THIRD.equals(code)) vcard.addEmailAddress(createEmailAddress(attributeValue));
		if(JpaddressbookSystemConstants.VCARD_FIELD_NOTE.equals(code)) vcard.addNote(createNote(attributeValue));
		if(JpaddressbookSystemConstants.VCARD_FIELD_URL.equals(code)) vcard.setUrl(attributeValue);
	}
	
	/**
	 * Returns Vcard field value
	 * @param contact
	 * @param profileAttribute
	 */
	private String getVcardFieldValue(IContact contact, String profileAttribute) {
		String attributeValue = "";
		if(contact != null){
			List<AttributeInterface> attributes = contact.getAttributes();
			for (int j = 0; j < attributes.size(); j++) {
				AttributeInterface attribute = attributes.get(j);
				String name = attribute.getName();
				if(name != null && profileAttribute.equals(name.trim())){
					String attributeType = attribute.getType();
					attributeValue = this.getAttributeValue(attribute, attributeType);
				}
			}
		}
		return attributeValue;
	}

	/**
	 * Returns the attribute value
	 * @param attribute
	 * @param attributeType
	 */
	private String getAttributeValue(AttributeInterface attribute, String attributeType) {
		String attributeValue = null;
		if(attributeType != null){
			attributeType = attributeType.trim();
			if(JpaddressbookSystemConstants.ATTRIBUTE_TYPE_DATE.equals(attributeType)){
				DateAttribute dateAttribute = (DateAttribute) attribute;
				attributeValue = dateAttribute.getFormattedDate("dd/MM/yyyy");
			}
			if(JpaddressbookSystemConstants.ATTRIBUTE_TYPE_ENUMERATOR.equals(attributeType)){
				EnumeratorAttribute textAttribute = (EnumeratorAttribute) attribute;
				attributeValue = textAttribute.getText();
			}
			if(JpaddressbookSystemConstants.ATTRIBUTE_TYPE_HYPERTEXT.equals(attributeType)){
				HypertextAttribute hyperTextAttribute = (HypertextAttribute)attribute;
				attributeValue = hyperTextAttribute.getText();
			}
			if(JpaddressbookSystemConstants.ATTRIBUTE_TYPE_LONGTEXT.equals(attributeType)){
				TextAttribute textAttribute = (TextAttribute) attribute;
				attributeValue = textAttribute.getText();
			}
			if(JpaddressbookSystemConstants.ATTRIBUTE_TYPE_MONOTEXT.equals(attributeType)){
				MonoTextAttribute mono = (MonoTextAttribute) attribute;
				attributeValue = mono.getText(); 
			}
			if(JpaddressbookSystemConstants.ATTRIBUTE_TYPE_NUMBER.equals(attributeType)){
				NumberAttribute numberattribute = (NumberAttribute) attribute;
				attributeValue = numberattribute.getNumber();
			}
			if(JpaddressbookSystemConstants.ATTRIBUTE_TYPE_TEXT.equals(attributeType)){
				TextAttribute textAttribute = (TextAttribute) attribute;
				attributeValue = textAttribute.getText();
			}
		}
		return attributeValue;
	}

	/**
	 * Create Email Address
	 * @param emailAddress
	 * @return email address
	 */
	private static EmailAddress createEmailAddress(String emailAddress){
		EmailAddress email = new EmailAddressImpl();
		email.addType(EmailAddressType.INTERNET_EMAIL);
		email.setEmailAddress(emailAddress);
		return email;
	}

	/**
	 * Create phone number
	 * @param phoneNumber
	 * @param phoneLocation
	 * @return phone number
	 */
	private static PhoneNumber createPhoneNumber(String phoneNumber, Type phoneLocation){
		PhoneNumber phone = new PhoneNumberImpl();
		phone.addType(phoneLocation);
		phone.setLocalNumber(phoneNumber);
		return phone;

	}

	/**
	 * Create the address
	 * @param streetAddress
	 * @param addressLocation
	 * @return the address
	 */
	private static MailingAddress createAddress(VCardAddress vcardAddress, Type addressLocation){
		MailingAddress address = new MailingAddressImpl();
		address.addType(addressLocation);
		address.setCountry(vcardAddress.getCountry() != null ? vcardAddress.getCountry() : "");
		address.setRegion(vcardAddress.getRegion() != null ? vcardAddress.getRegion() : "");
		address.setLocality(vcardAddress.getCity() != null ? vcardAddress.getCity() : "");
		address.setStreetAddress(vcardAddress.getStreet() != null ? vcardAddress.getStreet() : "");
		address.setPostalCode(vcardAddress.getCap() != null ? vcardAddress.getCap() : "");
		return address;
	}

	/**
	 * Create the birthday
	 * @param year
	 * @param month
	 * @param dayOfMonth
	 * @return the birthday
	 */
	private static Calendar createBirthday(String date){
		Calendar birthday = Calendar.getInstance();
		birthday.clear();
		String year = date.substring(6);
		String month = date.substring(3, 5);
		String day = date.substring(0, 2);
		birthday.set(Calendar.YEAR, Integer.valueOf(year));
		birthday.set(Calendar.MONTH, Integer.valueOf(month)-1);
		birthday.set(Calendar.DAY_OF_MONTH, Integer.valueOf(day));
		return birthday;
	}

	/**
	 * Create note
	 * @param noteContent
	 * @return note
	 */
	private static Note createNote(String noteContent){
		Note note = new NoteImpl();
		note.setEncodingType(EncodingType.QUOTED_PRINTABLE);
		note.setNote(noteContent);
		return note;
	}
	
	private static boolean validateDate(String dateString) {
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date date = null;
		try {
			df.setLenient(false);
			date = df.parse(dateString);
		} catch (Throwable t) {
			ApsSystemUtils.getLogger().log(Level.SEVERE, "The date is not valid." + t.getMessage());
			return false;
		}
		return true;
	}
	
}
