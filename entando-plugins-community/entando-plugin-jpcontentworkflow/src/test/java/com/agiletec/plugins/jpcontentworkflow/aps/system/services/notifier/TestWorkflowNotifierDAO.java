/*
*
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* jAPS is a free software; 
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.agiletec.plugins.jpcontentworkflow.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpcontentworkflow.util.WorkflowNotifierTestHelper;

import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.IWorkflowNotifierDAO;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.WorkflowNotifierDAO;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.model.ContentStatusChangedEventInfo;

/**
 * @author E.Santoboni
 */
public class TestWorkflowNotifierDAO extends ApsPluginBaseTestCase {
	
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
		this._helper.deleteContentEvents(); // Eventi rimossi nel caso qualche altro Test esterno li abbia generati
    }
	
	@Override
	protected void tearDown() throws Exception {
		this._helper.deleteContentEvents();
		super.tearDown();
	}
	
	public void testSaveContentEvent() throws Throwable {
		ContentStatusChangedEventInfo statusChangedInfo = this.prepareContentEventInfo();
		try {
			this._notifierDao.saveContentEvent(statusChangedInfo);
			Map<String, List<ContentStatusChangedEventInfo>> statusChangedInfos = this._notifierDao.getEventsToNotify();
			assertEquals(1, statusChangedInfos.size());
			List<ContentStatusChangedEventInfo> artStatusChangedInfos = statusChangedInfos.get("ART");
			assertEquals(1, artStatusChangedInfos.size());
			ContentStatusChangedEventInfo addedStatusChangedInfo = artStatusChangedInfos.get(0);
			this.compareContentEvents(statusChangedInfo, addedStatusChangedInfo);
		} catch (Throwable t) {
			throw t;
		}
	}
	
	public void testSignNotifiedEvents() throws Throwable {
		ContentStatusChangedEventInfo event = this.prepareContentEventInfo();
		try {
			assertEquals(0, this._notifierDao.getEventsToNotify().size());
			this._notifierDao.saveContentEvent(event);
			assertEquals(1, this._notifierDao.getEventsToNotify().size());
			
			List<ContentStatusChangedEventInfo> contentEvents = new ArrayList<ContentStatusChangedEventInfo>();
			contentEvents.add(event);
			this._notifierDao.signNotifiedEvents(contentEvents);
			assertEquals(0, this._helper.getEventsToNotify().size());
			assertEquals(0, this._notifierDao.getEventsToNotify().size());
			assertEquals(1, this._helper.getNotifiedEvents().size());
		} catch (Throwable t) {
			throw t;
		}
	}
	
	private void compareContentEvents(ContentStatusChangedEventInfo info1, ContentStatusChangedEventInfo info2) {
		assertEquals(info1.getContentId(), info2.getContentId());
		assertEquals(info1.getContentTypeCode(), info2.getContentTypeCode());
		assertEquals(info1.getContentDescr(), info2.getContentDescr());
		assertEquals(DateConverter.getFormattedDate(info1.getDate(), "dd/MM/yyyy HH:mm"), 
				DateConverter.getFormattedDate(info2.getDate(), "dd/MM/yyyy HH:mm"));
		assertEquals(info1.getMainGroup(), info2.getMainGroup());
		assertEquals(info1.getStatus(), info2.getStatus());
	}
	
	private ContentStatusChangedEventInfo prepareContentEventInfo() {
		ContentStatusChangedEventInfo statusChangedInfo = new ContentStatusChangedEventInfo();
		statusChangedInfo.setContentId("ART1");
		statusChangedInfo.setContentTypeCode("ART");
		statusChangedInfo.setContentDescr("contentDescr");
		statusChangedInfo.setDate(new Date());
		statusChangedInfo.setMainGroup("free");
		statusChangedInfo.setStatus(Content.STATUS_DRAFT);
		return statusChangedInfo;
	}
	
	private void init() {
		DataSource dataSource = (DataSource) this.getApplicationContext().getBean("servDataSource");
		WorkflowNotifierDAO notifierDao = new WorkflowNotifierDAO();
		notifierDao.setDataSource(dataSource);
		this._notifierDao = notifierDao;
		
		this._helper = new WorkflowNotifierTestHelper();
		this._helper.setDataSource(dataSource);
	}
	
	private IWorkflowNotifierDAO _notifierDao;
	private WorkflowNotifierTestHelper _helper;
	
}