/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando Enterprise Edition software.
* You can redistribute it and/or modify it
* under the terms of the Entando's EULA
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwebmail.apsadmin.webmail.message.helper;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpwebmail.aps.system.JpwebmailSystemConstants;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.IWebMailManager;

/**
 * @version 1.0
 * @author E.Santoboni
 */
public class NewMessageActionHelper implements INewMessageActionHelper {
	
	@Override
	public MimeMessage updateMessageOnSession(HttpServletRequest request) throws ApsSystemException {
		MimeMessage message = (MimeMessage) request.getSession().getAttribute(JpwebmailSystemConstants.CURRENT_MESSAGE_ON_EDIT);
		try {
            if (null != message) {
            	this.setRecipients("recipientsTo", RecipientType.TO, request, message);
            	this.setRecipients("recipientsCc", RecipientType.CC, request, message);
            	this.setRecipients("recipientsBcc", RecipientType.BCC, request, message);
            	
            	String subject = request.getParameter("subject");
            	if (subject != null) {
            		message.setSubject(subject, "UTF-8");
            	}
            	
            	String textContent = request.getParameter("content");
            	
            	MimeMultipart multipart = (MimeMultipart) message.getContent();
            	BodyPart textBodyPart = multipart.getBodyPart(0);
            	textBodyPart.setContent(textContent, "TEXT/PLAIN; charset=UTF-8");
            }
        } catch (Throwable t) {
        	ApsSystemUtils.getLogger().throwing("NewMessageActionHelper", "updateMessageOnSession", t);
        	throw new RuntimeException("Errore in updateMessageOnSession", t);
        }
		return message;
	}
	
	protected void setRecipients(String paramName, RecipientType type, HttpServletRequest request, MimeMessage message) throws Throwable {
		try {
			//TODO MOLTO DA AFFINARE!!!
			String recipientsString = request.getParameter(paramName);
			if (null == recipientsString) return;
			String[] recipientsArray = recipientsString.split(",");
			String recPattern = "(?:(.*?)<(" + EMAIL_REGEXP +")>)|(" + EMAIL_REGEXP +")";
			if (null != recipientsArray && recipientsArray.length > 0) {
				this.setRecipients(type, message, recipientsArray, recPattern);
			}
		} catch (Throwable t) {
			ApsSystemUtils.getLogger().throwing("ContentActionHelper", "setRecipients", t);
			throw new ApsSystemException("Errore in estrazione recipients " + paramName, t);
		}
	}
	
	private void setRecipients(RecipientType type, MimeMessage message, String[] recipientsArray, String recPattern) 	throws AddressException, MessagingException {
		Pattern pattern = Pattern.compile(recPattern);
		Matcher matcher = pattern.matcher("");
		Address[] addresses = new Address[0];
		for (int i = 0; i < recipientsArray.length; i++) {
			String rec = recipientsArray[i].trim();
			matcher.reset(rec);
			if (matcher.find()) {
				String fullString = matcher.group(0);
				String nome = matcher.group(1);
				String realAddress = matcher.group(2);
				Address recipient = null;
				if (null != nome && null != realAddress) {
					recipient = new InternetAddress(fullString);
				} else if (null != fullString && fullString.trim().length() > 0) {
					recipient = new InternetAddress(fullString);
				}
				if (null != recipient) {
					addresses = this.addAddress(addresses, recipient);
				}
			}
		}
		message.setRecipients(type, addresses);
	}
	
	public Address[] addAddress(Address[] addresses, Address recipient) {
		int len = addresses.length;
		Address[] newChildren = new Address[len + 1];
		for(int i=0; i < len; i++){
			newChildren[i] = addresses[i];
		}
		newChildren[len] = recipient;
		return newChildren;
	}
	
	public void deleteUserWebMailTempDirectory(UserDetails currentUser) {
		String dirPath = this.getUserWebMailDiskRootFolder(currentUser);
		File dir = new File(dirPath);
		FileHelper.deleteDir(dir, dirPath);
	}
	
	public String getUserWebMailDiskRootFolder(UserDetails currentUser) {
		try {
			String root = this.getTempRootFolder();
			String userFolder = root+File.separator+currentUser.getUsername();
			File dir = new File(userFolder);
			if (!dir.exists() || !dir.isDirectory()) {
				dir.mkdirs();
			}
			return userFolder;
		} catch (Throwable t) {
			throw new RuntimeException("Error creating temp folder ", t);
		}
	}
	
	protected String getTempRootFolder() throws Throwable {
		return this.getWebMailManager().loadConfig().getTempDiskRootFolder();
	}
	
	protected IWebMailManager getWebMailManager() {
		return _webMailManager;
	}
	public void setWebMailManager(IWebMailManager webMailManager) {
		this._webMailManager = webMailManager;
	}
	
	private IWebMailManager _webMailManager;
	
	private static final String EMAIL_REGEXP = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)" + "*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")"  + "@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
	
}