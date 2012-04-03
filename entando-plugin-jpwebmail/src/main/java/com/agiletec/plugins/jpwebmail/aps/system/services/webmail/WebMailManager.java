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

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jpwebmail.aps.system.JpwebmailSystemConstants;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.parse.WebMailConfigDOM;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.utils.CertificateHandler;

/**
 * Servizio gestore della WebMail.
 * @author E.Santoboni
 */
public class WebMailManager extends AbstractService implements IWebMailManager {
	
	@Override
	public void init() throws Exception {
		this.loadConfigs();
		ApsSystemUtils.getLogger().config(this.getClass().getName() + ": inizialized");
	}
	
	private void loadConfigs() throws ApsSystemException {
		try {
			String xml = this.getConfigManager().getConfigItem(JpwebmailSystemConstants.WEBMAIL_CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Item configurazione assente: " + JpwebmailSystemConstants.WEBMAIL_CONFIG_ITEM);
			}
			WebMailConfigDOM contactConfigDom = new WebMailConfigDOM();
			this.setConfig(contactConfigDom.extractConfig(xml));
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "loadConfigs");
			throw new ApsSystemException("Errore in fase di inizializzazione", t);
		}
	}
	
	@Override
	public WebMailConfig loadConfig() throws ApsSystemException {
		return this.getConfig().clone();
	}
	
	@Override
	public void updateConfig(WebMailConfig config) throws ApsSystemException {
		try {
			String xml = new WebMailConfigDOM().createConfigXml(config);
			this.getConfigManager().updateConfigItem(JpwebmailSystemConstants.WEBMAIL_CONFIG_ITEM, xml);
			this.setConfig(config);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "updateConfig", "Error updating Web Mail Service configuration");
			throw new ApsSystemException("Error updating Web Mail Service configuration", t);
		}
	}
	
	@Override
	public Store initInboxConnection(String username, String password) throws ApsSystemException {
		Logger log = ApsSystemUtils.getLogger();
		Store store = null;
		try {
			// Get session
			Session session = this.createSession();
			// Get the store
			store = session.getStore(this.getConfig().getImapProtocol());
			// Connect to store
			if (log.isLoggable(Level.INFO)) {
				log.info("Connessione utente " + username);
			}
//			 System.out.print("** tentivo di connessione con protocollo"+this.getConfig().getImapProtocol()+"" +
//			 		" a "+this.getConfig().getImapHost()+" ["+this.getConfig().getImapPort()+"]\n");
			store.connect(this.getConfig().getImapHost(), username, password);
		} catch (NoSuchProviderException e) {
			ApsSystemUtils.logThrowable(e, this, "initInboxConnection", "Provider " + this.getConfig().getImapHost() + " non raggiungibile");
			throw new ApsSystemException("Errore in apertura connessione Provider", e);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "initInboxConnection",
					"Errore generico in apertura connessione");
			throw new ApsSystemException(
					"Errore generico in apertura connessione", t);
		}
		return store;
	}
	
	@Override
	public MimeMessage createNewEmptyMessage() throws ApsSystemException {
		try {
			Session session = this.createSession();
			return new JpMimeMessage(session);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "createNewEmptyMessage", "Errore in creazione messaggio vuoto");
			throw new ApsSystemException("Errore in creazione messaggio vuoto", t);
		}
	}
	
	@Override
	public void sendMail(MimeMessage msg) throws ApsSystemException {
		WebMailConfig config = this.getConfig();
		String username = config.getSmtpUserName();
		String password = config.getSmtpPassword();
		this.sendMail(msg, username, password);
	}
	
	@Override
	public void sendMail(MimeMessage msg, String username, String password) throws ApsSystemException {
		Session session = (msg instanceof JpMimeMessage) ? ((JpMimeMessage) msg).getSession() : this.createSession();
		WebMailConfig config = this.getConfig();
		String smtpUsername = (config.isSmtpJapsUserAuth()) ? username : config.getSmtpUserName();
		String smtpPassword = (config.isSmtpJapsUserAuth()) ? password : config.getSmtpPassword();
		Transport bus = null;
		try {
			bus = session.getTransport("smtp");
			Integer port = config.getSmtpPort();
			if ((smtpUsername != null && smtpUsername.trim().length()>0) && 
					(smtpPassword != null && smtpPassword.trim().length()>0)) {
				if (port != null && port.intValue() > 0) {
					bus.connect(config.getSmtpHost(), port.intValue(), smtpUsername, smtpPassword);
				} else {
					bus.connect(config.getSmtpHost(), smtpUsername, smtpPassword);
				}
			} else {
				bus.connect();
			}
			msg.saveChanges();
			Transport.send(msg);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "sendMail", "Error on sending mail");
			throw new ApsSystemException("Errore in spedizione mail", t);
		} finally {
			closeTransport(bus);
		}
	}
	
	/**
	 * Prepara la Session per l'invio della mail, inoltre effettua l'handshake SSL con l'host di destinazione quando richiesto. 
	 * @return La Session pronta per l'uso.
	 */
	protected Session createSession() throws ApsSystemException {
		Properties properties = System.getProperties();
		WebMailConfig config = this.getConfig();
		String imapProtocol = config.getImapProtocol();
		String host = config.getImapHost();
		Integer port = config.getImapPort();
		
		if (null != imapProtocol && imapProtocol.trim().length()>0) {
			properties.setProperty("mail.store.protocol", imapProtocol);
		} else {
			throw new ApsSystemException("Protocollo (IMAP) non specificato");
		}
		if (null != host && host.trim().length()>0) {
			properties.setProperty("mail.imap.host", host);
		} else {
			throw new ApsSystemException("Host (IMAP) non specificato");
		}
		if (null != port && port.intValue()>0) {
			properties.setProperty("mail.imap.port", port.toString());
		} else {
			throw new ApsSystemException("Porta dell'host (IMAP) non specificata");
		}
		// analizza il certificato dell'host. Se l'handshake è già stato effettuato ritorna immediatamente
		this.getCertificateHandler().aquireCertificate(host, port.intValue(), imapProtocol, config);
		// verifica se si possa procedere in sicurezza con la connessione all'host
		if (!this.getCertificateHandler().proceedWithConnection()) {
			ApsSystemUtils.getLogger().info("La connessione verso '"+host+"' non risulta fidata");
		} else {
			ApsSystemUtils.getLogger().info("La connessione verso '"+host+"' è fidata");
		}
		properties.setProperty("mail.imap.timeout", "5000");
		if (imapProtocol.equalsIgnoreCase("imaps")) {			
			properties.setProperty("mail.imap.ssl.protocols", "SSL");
			properties.setProperty("mail.imap.starttls.enable", "true");
		}
		properties.put("mail.smtp.host", config.getSmtpHost());
		Integer smtpPort = config.getSmtpPort();
		if (smtpPort != null && smtpPort.intValue()>0) {
			properties.put("mail.smtp.port", smtpPort.toString());
		}
		if (config.getLocalhost() != null && config.getLocalhost().trim().length() > 0) {
			properties.put("mail.smtp.localhost", config.getLocalhost());
		}
		ApsSystemUtils.getLogger().info("");
		return Session.getInstance(properties, null);
	}
	
	/**
	 * Effettua la chiusura controllata del transport.
	 * @param transport Il Transport da chiudere.
	 * @throws ApsSystemException In caso di errore in chiusura.
	 */
	protected void closeTransport(Transport transport) throws ApsSystemException {
		if (transport != null) {
			try {
				transport.close();
			} catch (MessagingException e) {
				throw new ApsSystemException("Errore in chiusura connessione", e);
			}
		}
	}
	
	@Override
	public void closeConnection(Store store) {
		try {
			if (null != store) store.close();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "closeConnection", "Errore in chiusura connessione");
		}
	}
	
	@Override
	public String getSentFolderName() {
		return this.getConfig().getSentFolderName();
	}
	
	@Override
	public String getTrashFolderName() {
		return this.getConfig().getTrashFolderName();
	}
	
	@Override
	public String getDomainName() {
		return this.getConfig().getDomainName();
	}
	
	protected WebMailConfig getConfig() {
		return _config;
	}
	protected void setConfig(WebMailConfig config) {
		this._config = config;
	}
	
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	protected CertificateHandler getCertificateHandler() {
		return _certificateHandler;
	}
	public void setCertificateHandler(CertificateHandler certificateHandler) {
		this._certificateHandler = certificateHandler;
	}
	
	private WebMailConfig _config;
	private CertificateHandler  _certificateHandler;
	
	private ConfigInterface _configManager;
	
}