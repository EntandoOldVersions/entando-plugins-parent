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
package com.agiletec.plugins.jpwebmail.apsadmin.webmail.message;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Date;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderNotFoundException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpwebmail.aps.system.JpwebmailSystemConstants;
import com.agiletec.plugins.jpwebmail.aps.system.services.addressbook.IAddressBookManager;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.IUserMailHelper;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.IWebMailManager;
import com.agiletec.plugins.jpwebmail.apsadmin.util.AttachmentInfo;
import com.agiletec.plugins.jpwebmail.apsadmin.webmail.message.helper.FileHelper;
import com.agiletec.plugins.jpwebmail.apsadmin.webmail.message.helper.INewMessageActionHelper;

/**
 * @version 1.0
 * @author E.Santoboni
 */
public class NewMessageAction extends AbstractMessageAction implements INewMessageAction {
	
	@Override
	public void validate() {
		super.validate();
		try {
			MimeMessage message = this.getNewMessageHelper().updateMessageOnSession(this.getRequest());
			if ((null == message.getRecipients(RecipientType.TO) || message.getRecipients(RecipientType.TO).length == 0) && 
					(null == message.getRecipients(RecipientType.CC) || message.getRecipients(RecipientType.CC).length == 0) && 
					(null == message.getRecipients(RecipientType.BCC) || message.getRecipients(RecipientType.BCC).length == 0)) {
				this.addFieldError("recipientsTo", this.getText("WebMail.recipients.empty"));
			}
			if (null == message.getSubject() || message.getSubject().trim().length() == 0) {
				this.addFieldError("subject", this.getText("WebMail.subject.empty"));
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "validate");
			throw new RuntimeException("Errore in validazione", t);
		}
	}
	
	@Override
	public String newMessage() {
		try {
			MimeMessage message = this.getWebMailManager().createNewEmptyMessage();
			String from = this.getUserMailHelper().getEmailAddress(this.getCurrentUser());
			message.setFrom(new InternetAddress(from));
			Multipart multiPart = new MimeMultipart();
			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setContent("", "TEXT/PLAIN; charset=UTF-8");
			multiPart.addBodyPart(textBodyPart);
			message.setContent(multiPart, "UTF-8");
			this.getRequest().getSession().setAttribute(JpwebmailSystemConstants.CURRENT_MESSAGE_ON_EDIT, message);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "reply");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String reply() {
		return this.reply(false, false);
	}
	
	@Override
	public String replyAll() {
		return this.reply(true, false);
	}
	
	@Override
	public String forward() {
		return this.reply(false, true);
	}
	
	protected String reply(boolean all, boolean insertAttach) {
		try {
			Message message = this.getSelectedMessage();
			if (null == message) return "intro";
			// Create a reply message
	        MimeMessage reply = (MimeMessage) message.reply(all);
	        // Set the from field
	        String from = this.getUserMailHelper().getEmailAddress(this.getCurrentUser());
	        reply.setFrom(new InternetAddress(from));
	        
	        if (!all && insertAttach) {//is Forward
	        	String subject = "[Fwd: " + message.getSubject() + "]";
	        	reply.setSubject(subject, "UTF-8");
	        }
	        
	        Multipart multiPart = new MimeMultipart();
	        
	        // Create the reply content, copying over the original if text
			MimeMessage orig = (MimeMessage) message;
			StringBuffer buffer = this.getContentForReply(orig);
			
			MimeBodyPart textBodyPart = new MimeBodyPart();
			textBodyPart.setContent(buffer.toString(), "TEXT/PLAIN; charset=UTF-8");
			multiPart.addBodyPart(textBodyPart);
			
			if (insertAttach) {
	        	this.replyAttachments(message, multiPart);
	        }
			reply.setContent(multiPart, "UTF-8");
			this.getRequest().getSession().setAttribute(JpwebmailSystemConstants.CURRENT_MESSAGE_ON_EDIT, reply);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "reply");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	private StringBuffer getContentForReply(MimeMessage orig) throws MessagingException, IOException {
		StringBuffer buffer = new StringBuffer();
		String content = null;
		if (orig.isMimeType("text/plain")) {
			content = (String) orig.getContent();
		} else {
			content = this.getContent(orig);
		}
		StringReader contentReader = new StringReader(content);
		BufferedReader br = new BufferedReader(contentReader);
		String contentLine;
		while ((contentLine = br.readLine()) != null) {
			buffer.append("> ");
			buffer.append(contentLine);
			buffer.append("\r\n");
		}
		buffer.append("\n\n");
		return buffer;
	}
	
	public MimeMessage getMessage() {
		return (MimeMessage) this.getRequest().getSession().getAttribute(JpwebmailSystemConstants.CURRENT_MESSAGE_ON_EDIT);
	}
	
	@Override
	public String send() {
		String sentFolderName = this.getWebMailManager().getSentFolderName();
		try {
			MimeMessage message = this.getMessage();
			UserDetails currentUser = this.getCurrentUser();
			this.getWebMailManager().sendMail(message, currentUser.getUsername(), currentUser.getPassword());
			Folder dfolder = this.getStore().getFolder(sentFolderName);
			if (null == dfolder) {
				this.addActionMessage(this.getText("SENT_FOLDER_NULL"));
				return SUCCESS;
			}
			dfolder.open(Folder.READ_WRITE);
			message.setSentDate(new Date());
			message.setFlag(Flags.Flag.SEEN, true);
			Message[] msgs = new Message[1];
			msgs[0] = message;
			if (msgs.length != 0) {
				dfolder.appendMessages(msgs);
			}
			this.setOpenedFolder(dfolder);
		} catch (FolderNotFoundException t) {
			ApsSystemUtils.logThrowable(t, this, "sent", "Sent Folder '" + sentFolderName + "' not Found");
			this.addActionError(this.getText("SENT_FOLDER_NOT_FOUND", new String[]{sentFolderName}));
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "send");
			return FAILURE;
		} finally {
			this.getNewMessageHelper().deleteUserWebMailTempDirectory(this.getCurrentUser());
		}
		return SUCCESS;
	}
	
	private void replyAttachments(Message message, Multipart multiPart) throws ApsSystemException {
		List<AttachmentInfo> attachInfos = this.getAttachmentInfos(message);
		for (int i=0; i<attachInfos.size(); i++) {
			AttachmentInfo info = attachInfos.get(i);
			BodyPart bodyPart = info.getBodyPart();
			this.createTempBodyPart(i, bodyPart, multiPart);
		}
	}
	
	private void createTempBodyPart(int number, BodyPart bodyPart, Multipart multiPart) throws ApsSystemException {
		try {
			String bodyPartFileName = bodyPart.getFileName();
			String bodypartFolder = this.getNewMessageHelper().getUserWebMailDiskRootFolder(this.getCurrentUser()) + File.separator + number;
			File dir = new File(bodypartFolder);
			if (!dir.exists() || !dir.isDirectory()) {
				dir.mkdirs();
			}
			String fileName = bodypartFolder + File.separator + bodyPartFileName;
			InputStream inputStream = bodyPart.getDataHandler().getInputStream();
			FileHelper.saveFile(fileName, inputStream);
			
		    BodyPart messageBodyPart = new MimeBodyPart();
    		DataSource source = new FileDataSource(fileName);
    		messageBodyPart.setDataHandler(new DataHandler(source));
    		messageBodyPart.setFileName(bodyPartFileName);
    		multiPart.addBodyPart(messageBodyPart);
	    } catch (Throwable t) {
	    	throw new ApsSystemException("Errore in preparazione attachments in reply", t);
	    }
	}
	
	public Boolean existAddressBook() {
		return (null != this.getAddressBookManager());
	}
	
	protected IWebMailManager getWebMailManager() {
		return _webMailManager;
	}
	public void setWebMailManager(IWebMailManager webMailManager) {
		this._webMailManager = webMailManager;
	}
	
	protected INewMessageActionHelper getNewMessageHelper() {
		return _newMessageHelper;
	}
	public void setNewMessageHelper(INewMessageActionHelper newMessageHelper) {
		this._newMessageHelper = newMessageHelper;
	}
	
	protected IUserMailHelper getUserMailHelper() {
		return _userMailHelper;
	}
	public void setUserMailHelper(IUserMailHelper userMailHelper) {
		this._userMailHelper = userMailHelper;
	}
	
	public IAddressBookManager getAddressBookManager() {
		return _addressBookManager;
	}
	public void setAddressBookManager(IAddressBookManager addressBookManager) {
		this._addressBookManager = addressBookManager;
	}
	
	private IWebMailManager _webMailManager;
	private INewMessageActionHelper _newMessageHelper;
	private IUserMailHelper _userMailHelper;
	private IAddressBookManager _addressBookManager;
	
}