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
package com.agiletec.plugins.jpwebmail.aps.system.services.webmail;

/**
 * @author E.Santoboni
 */
public class WebMailConfig implements Cloneable {
	
	@Override
	public WebMailConfig clone() {
		WebMailConfig config = new WebMailConfig();
		
		config.setLocalhost(this.getLocalhost());
		
		config.setDomainName(this.getDomainName());
		
		config.setCertificateEnable(this.isCertificateEnable());
		config.setCertificateLazyCheck(this.isCertificateLazyCheck());
		config.setCertificatePath(this.getCertificatePath());
		config.setCertificateDebugOnConsole(this.isCertificateDebugOnConsole());
		
		config.setImapHost(this.getImapHost());
		config.setImapPort(this.getImapPort());
		config.setImapProtocol(this.getImapProtocol());
		
		config.setDebug(this.isDebug());
		config.setSmtpJapsUserAuth(this.isSmtpJapsUserAuth());
		config.setSmtpHost(this.getSmtpHost());
		config.setSmtpPort(this.getSmtpPort());
		config.setSmtpUserName(this.getSmtpUserName());
		config.setSmtpPassword(this.getSmtpPassword());
		
		config.setTrashFolderName(this.getTrashFolderName());
		config.setSentFolderName(this.getSentFolderName());
		
		return config;
	}
	
	public String getSmtpHost() {
		return _smtpHost;
	}
	public void setSmtpHost(String smtpHost) {
		this._smtpHost = smtpHost;
	}
	
	public String getSmtpPassword() {
		return _smtpPassword;
	}
	public void setSmtpPassword(String smtpPassword) {
		this._smtpPassword = smtpPassword;
	}
	
	public String getSmtpUserName() {
		return _smtpUserName;
	}
	public void setSmtpUserName(String smtpUserName) {
		this._smtpUserName = smtpUserName;
	}
	
	public Integer getSmtpPort() {
		return _smtpPort;
	}
	public void setSmtpPort(Integer smtpPort) {
		this._smtpPort = smtpPort;
	}
	
	public boolean isSmtpJapsUserAuth() {
		return _smtpJapsUserAuth;
	}
	public void setSmtpJapsUserAuth(boolean smtpJapsUserAuth) {
		this._smtpJapsUserAuth = smtpJapsUserAuth;
	}
	
	public boolean isDebug() {
		return _debug;
	}
	public void setDebug(boolean debug) {
		this._debug = debug;
	}
	
	public String getLocalhost() {
		return _localhost;
	}
	public void setLocalhost(String localhost) {
		this._localhost = localhost;
	}
	
	public String getDomainName() {
		return _domainName;
	}
	public void setDomainName(String domainName) {
		this._domainName = domainName;
	}
	
	public String getImapHost() {
		return _imapHost;
	}
	public void setImapHost(String imapHost) {
		this._imapHost = imapHost;
	}
	
	public Integer getImapPort() {
		return _imapPort;
	}
	public void setImapPort(Integer imapPort) {
		this._imapPort = imapPort;
	}
	
	public String getImapProtocol() {
		return _imapProtocol;
	}
	public void setImapProtocol(String imapProtocol) {
		this._imapProtocol = imapProtocol;
	}
	
	public String getTrashFolderName() {
		return _trashFolderName;
	}
	public void setTrashFolderName(String trashFolderName) {
		this._trashFolderName = trashFolderName;
	}
	
	public String getSentFolderName() {
		return _sentFolderName;
	}
	public void setSentFolderName(String sentFolderName) {
		this._sentFolderName = sentFolderName;
	}
	
	public boolean isCertificateEnable() {
		return _certificateEnable;
	}
	public void setCertificateEnable(boolean certificateEnable) {
		this._certificateEnable = certificateEnable;
	}
	
	public boolean isCertificateLazyCheck() {
		return _certificateLazyCheck;
	}
	public void setCertificateLazyCheck(boolean certificateLazyCheck) {
		this._certificateLazyCheck = certificateLazyCheck;
	}
	
	public String getCertificatePath() {
		return _certificatePath;
	}
	public void setCertificatePath(String certificatePath) {
		this._certificatePath = certificatePath;
	}
	
	public boolean isCertificateDebugOnConsole() {
		return _certificateDebugOnConsole;
	}
	public void setCertificateDebugOnConsole(boolean certificateDebugOnConsole) {
		this._certificateDebugOnConsole = certificateDebugOnConsole;
	}
	
	private boolean _certificateEnable;
	private boolean _certificateLazyCheck;
	private String	_certificatePath;
	private boolean _certificateDebugOnConsole;
	
	private String _localhost;
	
	private String _domainName;
	
	private String _imapHost;
	private Integer _imapPort;
	private String _imapProtocol;
	
	private String _smtpHost;
	private String _smtpUserName;
	private String _smtpPassword;
	private Integer _smtpPort;
	private boolean _debug;
	private boolean _smtpJapsUserAuth;
	
	private String _trashFolderName;
	private String _sentFolderName;
	
}