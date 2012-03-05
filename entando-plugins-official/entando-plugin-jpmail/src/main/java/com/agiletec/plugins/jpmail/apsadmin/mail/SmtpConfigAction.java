/*
 *
 * Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
 * Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 */
package com.agiletec.plugins.jpmail.apsadmin.mail;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpmail.aps.services.mail.MailConfig;

public class SmtpConfigAction extends BaseAction implements ISmtpConfigAction {
	
	@Override
	public String edit() {
		try {
			MailConfig config = this.getMailManager().getMailConfig();
			this.populateForm(config);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "edit");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String save() {
		try {
			MailConfig config = this.prepareConfig();
			this.getMailManager().updateMailConfig(config);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Populate the action with the content of the given MailConfig.
	 * @param config The configuration used to populate the action.
	 */
	protected void populateForm(MailConfig config) {
		if (config!=null) {
			this.setDebug(config.isDebug());
			this.setSmtpHost(config.getSmtpHost());
			this.setSmtpPort(config.getSmtpPort());
			this.setSmtpTimeout(config.getSmtpTimeout());
			this.setSmtpUserName(config.getSmtpUserName());
			this.setSmtpPassword(config.getSmtpPassword());
			this.setSmtpProtocol(config.getSmtpProtocol());
		} else {
			config = new MailConfig();
		}
	}
	
	/**
	 * Prepares a MailConfig starting from the action form fields.
	 * @return a MailConfig starting from the action form fields.
	 * @throws ApsSystemException In case of errors.
	 */
	protected MailConfig prepareConfig() throws ApsSystemException {
		MailConfig config = this.getMailManager().getMailConfig();
		config.setDebug(this.isDebug());
		config.setSmtpHost(this.getSmtpHost());
		config.setSmtpPort(this.getSmtpPort());
		config.setSmtpTimeout(this.getSmtpTimeout());
		config.setSmtpUserName(this.getSmtpUserName());
		config.setSmtpPassword(this.getSmtpPassword());
		config.setSmtpProtocol(this.getSmtpProtocol());
		return config;
	}
	
	/**
	 * Returns the smtp host name.
	 * @return The smtp host name.
	 */
	public String getSmtpHost() {
		return _smtpHost;
	}
	/**
	 * Sets the smtp host name.
	 * @param smtpHost The smtp host name.
	 */
	public void setSmtpHost(String smtpHost) {
		this._smtpHost = smtpHost;
	}
	
	/**
	 * Return the smtp port.
	 * @return The smtp port.
	 */
	public Integer getSmtpPort() {
		return _smtpPort;
	}
	/**
	 * Sets the smtp port.
	 * @param port The smtp port.
	 */
	public void setSmtpPort(Integer smtpPort) {
		this._smtpPort = smtpPort;
	}
	
	/**
	 * Return the smtp timeout.
	 * @return The smtp timeout.
	 */
	public Integer getSmtpTimeout() {
		return _smtpTimeout;
	}
	/**
	 * Sets the smtp timeout. If 0 or null uses default.
	 * @param port The smtp timeout.
	 */
	public void setSmtpTimeout(Integer smtpTimeout) {
		this._smtpTimeout = smtpTimeout;
	}
	
	/**
	 * Returns the smtp username.
	 * @return The smtp username.
	 */
	public String getSmtpUserName() {
		return _smtpUserName;
	}
	/**
	 * Sets the smtp username.
	 * @param smtpUserName The smtp username.
	 */
	public void setSmtpUserName(String smtpUserName) {
		this._smtpUserName = smtpUserName;
	}
	
	/**
	 * Returns the smtp password.
	 * @return The smtp password.
	 */
	public String getSmtpPassword() {
		return _smtpPassword;
	}
	/**
	 * Sets the smtp password.
	 * @param smtpPassword The smtp password.
	 */
	public void setSmtpPassword(String smtpPassword) {
		this._smtpPassword = smtpPassword;
	}
	
	/**
	 * Returns the debug flag, used to trace debug informations.
	 * @return The debug flag.
	 */
	public boolean isDebug() {
		return _debug;
	}
	/**
	 * Sets the debug flag, used to trace debug informations.
	 * @param debug The debug flag.
	 */
	public void setDebug(boolean debug) {
		this._debug = debug;
	}
	
	/**
	 * Returns the IMailManager service.
	 * @return The IMailManager service.
	 */
	public IMailManager getMailManager() {
		return _mailManager;
	}
	/**
	 * Set method for Spring bean injection.<br /> Sets the IMailManager service.
	 * @param mailManager The IMailManager service.
	 */
	public void setMailManager(IMailManager mailManager) {
		this._mailManager = mailManager;
	}
	
	/**
	 * Set the transport security layer protocol
	 * @param protocol
	 */
	public void setSmtpProtocol(Integer smtpProtocol) {
		this._smtpProtocol = smtpProtocol;
	}

	/**
	 * Get the transport security layer protocol
	 * @param protocol
	 */
	public Integer getSmtpProtocol() {
		return _smtpProtocol;
	}
	
	private String _smtpHost;
	private Integer _smtpPort;
	private Integer _smtpTimeout;
	private String _smtpUserName;
	private String _smtpPassword;
	private Integer _smtpProtocol;
	private boolean _debug;
	
	private IMailManager _mailManager;
	
}