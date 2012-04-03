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
package com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier;

import java.util.Date;

import javax.sql.DataSource;

import com.agiletec.plugins.jpcontentworkflow.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpcontentworkflow.util.WorkflowNotifierTestHelper;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpcontentworkflow.aps.system.JpcontentworkflowSystemConstants;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.IWorkflowNotifierManager;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.model.NotifierConfig;
import com.agiletec.plugins.jpmail.aps.services.JpmailSystemConstants;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpmail.aps.services.mail.MailManager;

/**
 * @author E.Santoboni
 */
public class TestWorkflowNotifierManager extends ApsPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
		this._helper.deleteContentEvents();
		this.activeMailManager(false);
    }
	
	@Override
	protected void tearDown() throws Exception {
		this._helper.deleteContentEvents();
		this.activeMailManager(true);
		super.tearDown();
	}
	
	public void testGetNotifierConfig() throws Throwable {
		NotifierConfig notifierConfig = this._notifierManager.getNotifierConfig();
		assertFalse(notifierConfig.isActive());
		assertEquals(24, notifierConfig.getHoursDelay());
		assertEquals("CODE1", notifierConfig.getSenderCode());
		assertEquals("email", notifierConfig.getMailAttrName());
		assertFalse(notifierConfig.isHtml());
		assertEquals("[Portale]: Notifica stato contenuti", notifierConfig.getSubject());
		assertEquals("Elenco contenuti: Ciao {user},<br />di seguito l'elenco dei contenuti per cui è richiesto il tuo intervento<br /><br />", notifierConfig.getHeader());
		assertEquals("<br />Contenuto {type} - {descr} - Stato {status}<br />", notifierConfig.getTemplate());
		assertEquals("<br />Fine Mail (footer)", notifierConfig.getFooter());
	}
	
	public void testSetNotifierConfig() throws Throwable {
		String config = this._configManager.getConfigItem(JpcontentworkflowSystemConstants.WORKFLOW_NOTIFIER_CONFIG_ITEM);
		NotifierConfig originaryNotifierConfig = this._notifierManager.getNotifierConfig();
		NotifierConfig newNotifierConfig = this.prepareNotifierConfig();
		try {
			this._notifierManager.saveNotifierConfig(newNotifierConfig);
			NotifierConfig updatedNotifierConfig = this._notifierManager.getNotifierConfig();
			this.compareNotifiers(newNotifierConfig, updatedNotifierConfig);
			
			this._notifierManager.saveNotifierConfig(originaryNotifierConfig);
			updatedNotifierConfig = this._notifierManager.getNotifierConfig();
			this.compareNotifiers(originaryNotifierConfig, updatedNotifierConfig);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._configManager.updateConfigItem(JpcontentworkflowSystemConstants.WORKFLOW_NOTIFIER_CONFIG_ITEM, config);
		}
	}
	
	public void testSendEMails() throws Throwable {
		String config = this._configManager.getConfigItem(JpcontentworkflowSystemConstants.WORKFLOW_NOTIFIER_CONFIG_ITEM);
		NotifierConfig notifierConfig = this._notifierManager.getNotifierConfig();
		try {
			this.addContentStatusChangedEvent("ART1", Content.STATUS_READY);
			this.addContentStatusChangedEvent("ART180", Content.STATUS_DRAFT);
			this.addContentStatusChangedEvent("EVN41", Content.STATUS_READY);
			assertEquals(0, this._helper.getEventsToNotify().size());
			assertEquals(0, this._helper.getNotifiedEvents().size());
			
			notifierConfig.setActive(true);
			this._notifierManager.saveNotifierConfig(notifierConfig);
			
			this.addContentStatusChangedEvent("ART1", Content.STATUS_DRAFT);
			this.addContentStatusChangedEvent("ART180", Content.STATUS_READY);
			this.addContentStatusChangedEvent("EVN41", Content.STATUS_DRAFT);
			assertEquals(2, this._helper.getEventsToNotify().size());
			assertEquals(0, this._helper.getNotifiedEvents().size());
			
			this._notifierManager.sendMails();
			assertEquals(0, this._helper.getEventsToNotify().size());
			assertEquals(2, this._helper.getNotifiedEvents().size());
		} catch (Throwable t) {
			this.addContentStatusChangedEvent("ART1", Content.STATUS_DRAFT);
			this.addContentStatusChangedEvent("ART180", Content.STATUS_READY);
			this.addContentStatusChangedEvent("EVN41", Content.STATUS_DRAFT);
			throw t;
		} finally {
			this._configManager.updateConfigItem(JpcontentworkflowSystemConstants.WORKFLOW_NOTIFIER_CONFIG_ITEM, config);
		}
	}
	
	private NotifierConfig prepareNotifierConfig() {
		NotifierConfig notifierConfig = new NotifierConfig();
		
		notifierConfig.setActive(true);
		notifierConfig.setHoursDelay(23);
		notifierConfig.setStartScheduler(new Date());
		
		notifierConfig.setSenderCode("CODE2");
		notifierConfig.setMailAttrName("eMail");
		notifierConfig.setHtml(true);
		notifierConfig.setSubject("Oggetto");
		notifierConfig.setHeader("header");
		notifierConfig.setTemplate("template");
		notifierConfig.setFooter("footer");
		
		return notifierConfig;
	}
	
	private void compareNotifiers(NotifierConfig nc1, NotifierConfig nc2) {
		assertEquals(nc1.isActive(), nc2.isActive());
		assertEquals(nc1.getHoursDelay(), nc2.getHoursDelay());
		long time1 = nc1.getStartScheduler().getTime()/60000;
		long time2 = nc2.getStartScheduler().getTime()/60000;
		assertEquals(time1, time2);
		assertEquals(nc1.getSenderCode(), nc2.getSenderCode());
		assertEquals(nc1.getMailAttrName(), nc2.getMailAttrName());
		assertEquals(nc1.isHtml(), nc2.isHtml());
		assertEquals(nc1.getSubject(), nc2.getSubject());
		assertEquals(nc1.getHeader(), nc2.getHeader());
		assertEquals(nc1.getTemplate(), nc2.getTemplate());
		assertEquals(nc1.getFooter(), nc2.getFooter());
	}
	
	private String addContentStatusChangedEvent(String contentId, String status) throws ApsSystemException {
		Content content = this._contentManager.loadContent(contentId, false);
		String currentStatus = content.getStatus();
		content.setStatus(status);
		this._contentManager.saveContent(content);
		return currentStatus;
	}
	
	private void init() {
		this._configManager = (ConfigInterface) this.getService(SystemConstants.BASE_CONFIG_MANAGER);
		this._notifierManager = (IWorkflowNotifierManager) this.getService(JpcontentworkflowSystemConstants.WORKFLOW_NOTIFIER_MANAGER);
		this._contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
		this._helper = new WorkflowNotifierTestHelper();
		this._helper.setDataSource(dataSource);
	}
	
	private void activeMailManager(boolean active) {
		IMailManager mailManager = (IMailManager) this.getService(JpmailSystemConstants.MAIL_MANAGER);
		if (mailManager instanceof MailManager) {
			((MailManager) mailManager).setActive(active);
		}
	}
	
	private ConfigInterface _configManager;
	private IWorkflowNotifierManager _notifierManager;
	private IContentManager _contentManager;
	private WorkflowNotifierTestHelper _helper;
	
}