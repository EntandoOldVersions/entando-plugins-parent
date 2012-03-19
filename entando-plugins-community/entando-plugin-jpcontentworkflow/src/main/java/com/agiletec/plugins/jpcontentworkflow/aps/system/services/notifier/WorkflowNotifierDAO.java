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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.model.ContentStatusChangedEventInfo;

/**
 * @author E.Santoboni
 */
public class WorkflowNotifierDAO extends AbstractDAO implements IWorkflowNotifierDAO {
	
	@Override
	public void saveContentEvent(ContentStatusChangedEventInfo contentEvent) throws ApsSystemException {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(SAVE_EVENT);
			contentEvent.setId(this.newId(conn));
			stat.setInt(1, contentEvent.getId());
			stat.setTimestamp(2, new Timestamp(contentEvent.getDate().getTime()));
			stat.setString(3, contentEvent.getContentId());
			stat.setString(4, contentEvent.getContentTypeCode());
			stat.setString(5, contentEvent.getContentDescr());
			stat.setString(6, contentEvent.getMainGroup());
			stat.setString(7, contentEvent.getStatus());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Errore in salvataggio evento cambiamento stato contenuto " + 
					contentEvent.getContentId(), "saveContentStatusChangedInfo");
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public Map<String, List<ContentStatusChangedEventInfo>> getEventsToNotify() throws ApsSystemException {
		Map<String, List<ContentStatusChangedEventInfo>> eventsToNotify = new HashMap<String, List<ContentStatusChangedEventInfo>>();
		Connection conn = null;
		Statement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			res = stat.executeQuery(LOAD_ALL_EVENTS_TO_BE_NOTIFIED);
			while (res.next()) {
				ContentStatusChangedEventInfo event = new ContentStatusChangedEventInfo();
				event.setId(res.getInt(1));
				event.setDate(res.getTimestamp(2));
				event.setContentId(res.getString(3));
				String contentType = res.getString(4);
				event.setContentTypeCode(contentType);
				event.setContentDescr(res.getString(5));
				event.setMainGroup(res.getString(6));
				event.setStatus(res.getString(7));
				List<ContentStatusChangedEventInfo> contentTypeEvents = (List<ContentStatusChangedEventInfo>) eventsToNotify.get(contentType);
				if (contentTypeEvents == null) {
					contentTypeEvents = new ArrayList<ContentStatusChangedEventInfo>();
					eventsToNotify.put(contentType, contentTypeEvents);
				}
				contentTypeEvents.add(event);
			}
		} catch (Throwable t) {
			this.processDaoException(t, "Errore in caricamento contenuti da notificare", "getContentsToNotify");
		} finally {
			this.closeDaoResources(res, stat, conn);
		}
		return eventsToNotify;
	}
	
	@Override
	public void signNotifiedEvents(List<ContentStatusChangedEventInfo> notifiedEvents) throws ApsSystemException {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_EVENT);
			for (ContentStatusChangedEventInfo event : notifiedEvents) {
				stat.setInt(1, event.getId());
				stat.addBatch();
				stat.clearParameters();
			}
			stat.executeBatch();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			this.processDaoException(t, "Errore in aggiunta record tabella", "addContentSearchRecord");
		} finally {
			this.closeDaoResources(null, stat, conn);
		}
	}
	
	private int newId(Connection conn) throws ApsSystemException {
		int id = 0;
		Statement stat = null;
		ResultSet res = null;
		try {
			stat = conn.createStatement();
			res = stat.executeQuery(GET_LAST_ID);
			if (res.next()) {
				id = res.getInt(1);
			}
			id = id + 1; // N.B.: funziona anche per il primo record
		} catch (Throwable e) {
			this.processDaoException(e, "Errore in estrazione ultimo id", "newId");
		} finally {
			this.closeDaoResources(res, stat);
		}
		return id;
	}
	
	private final String SAVE_EVENT = 
		"INSERT INTO jpcontentworkflow_events " +
		"(id, data, contentid, contenttype, descr, maingroup, status, notified) " +
		"VALUES ( ? , ? , ? , ? , ? , ? , ? , 0 ) ";
	
	private final String LOAD_ALL_EVENTS_TO_BE_NOTIFIED = 
		"SELECT id, data, contentid, contenttype, descr, maingroup, status " +
		"FROM jpcontentworkflow_events WHERE notified = 0 ORDER BY contenttype, status ";
	
	private final String UPDATE_EVENT = 
		"UPDATE jpcontentworkflow_events SET notified = 1 WHERE id = ? ";
	
	private final String GET_LAST_ID = 
		"SELECT MAX(id) FROM jpcontentworkflow_events ";
	
}