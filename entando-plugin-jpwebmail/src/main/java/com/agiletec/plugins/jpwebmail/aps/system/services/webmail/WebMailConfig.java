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
		config.setUseEntandoUserPassword(this.isUseEntandoUserPassword());
		
		config.setCertificateEnable(this.isCertificateEnable());
		config.setCertificateLazyCheck(this.isCertificateLazyCheck());
		config.setCertificatePath(this.getCertificatePath());
		config.setCertificateDebugOnConsole(this.isCertificateDebugOnConsole());
		
		config.setImapHost(this.getImapHost());
		config.setImapPort(this.getImapPort());
		config.setImapProtocol(this.getImapProtocol());
		
		config.setDebug(this.isDebug());
		config.setSmtpEntandoUserAuth(this.isSmtpEntandoUserAuth());
		config.setSmtpHost(this.getSmtpHost());
		config.setSmtpPort(this.getSmtpPort());
		config.setSmtpUserName(this.getSmtpUserName());
		config.setSmtpPassword(this.getSmtpPassword());
		config.setSmtpProtocol(this.getSmtpProtocol());
		
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
	
	public void setSmtpProtocol(Integer smtpProtocol) {
		this._smtpProtocol = smtpProtocol;
	}
	public Integer getSmtpProtocol() {
		return _smtpProtocol;
	}
	
	public boolean isSmtpEntandoUserAuth() {
		return _smtpEntandoUserAuth;
	}
	public void setSmtpEntandoUserAuth(boolean smtpEntandoUserAuth) {
		this._smtpEntandoUserAuth = smtpEntandoUserAuth;
	}
	
	@Deprecated
	public boolean isSmtpJapsUserAuth() {
		return this.isSmtpEntandoUserAuth();
	}
	@Deprecated
	public void setSmtpJapsUserAuth(boolean smtpJapsUserAuth) {
		this.setSmtpEntandoUserAuth(smtpJapsUserAuth);
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
	
	public boolean isUseEntandoUserPassword() {
		return _useEntandoUserPassword;
	}
	public void setUseEntandoUserPassword(boolean useEntandoUserPassword) {
		this._useEntandoUserPassword = useEntandoUserPassword;
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
	
	public String getTempDiskRootFolder() {
		return _tempDiskRootFolder;
	}
	public void setTempDiskRootFolder(String tempDiskRootFolder) {
		this._tempDiskRootFolder = tempDiskRootFolder;
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
	
	/**
	 * Return true if mail configuration expects an anonymous authentication.<br/>
	 * NOTE: an anonymous authentication occurs whenever the username and the
	 * associated password are <b>not</b> provided <b>and</b> no security
	 * encapsulation protocol is specified.
	 * @return True if the username and the password are not provided
	 */
	public boolean hasAnonimousSmtpAuth() {
		return (((null == this._smtpUserName || this._smtpUserName.length() == 0)
				&& (null == this._smtpPassword || this._smtpPassword.length() == 0)) && 
				!this.hasSecureSmtp());
	}
	
	/**
	 * Return true if the mail transport uses a secure connection.
	 * @return true if the selected encapsulation protocol is SSL or TLS
	 */
	public boolean hasSecureSmtp() {
		if (null == this._smtpProtocol || 
				this._smtpProtocol == PROTO_STD) return false;
		return true;
	}
	
	private boolean _certificateEnable;
	private boolean _certificateLazyCheck;
	private String	_certificatePath;
	private boolean _certificateDebugOnConsole;
	
	private String _localhost;
	private String _domainName;
	private boolean _useEntandoUserPassword; //Use Entando password
	
	private String _imapHost;
	private Integer _imapPort;
	private String _imapProtocol;
	
	private String _smtpHost;
	private String _smtpUserName;
	private String _smtpPassword;
	private Integer _smtpPort;
	private Integer _smtpProtocol;
	private boolean _debug;
	private boolean _smtpEntandoUserAuth;
	
	private String _tempDiskRootFolder;
	
	private String _trashFolderName;
	private String _sentFolderName;
	
	public static final int PROTO_STD = 0;
	public static final int PROTO_SSL = 1;
	public static final int PROTO_TLS = 2;
	
}