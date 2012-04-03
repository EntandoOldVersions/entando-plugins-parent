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
package com.agiletec.plugins.jpversioning.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.resource.IResourceManager;
import com.agiletec.plugins.jacms.aps.system.services.resource.model.ResourceInterface;
import com.agiletec.plugins.jpversioning.aps.system.services.versioning.ContentVersion;

public class JpversioningTestHelper extends AbstractDAO {
	
	public JpversioningTestHelper(DataSource dataSource, ApplicationContext context) {
		this.setDataSource(dataSource);
		this._contentManager = (IContentManager) context.getBean(JacmsSystemConstants.CONTENT_MANAGER);
		this._resourceManager = (IResourceManager) context.getBean(JacmsSystemConstants.RESOURCE_MANAGER);
	}
	
	public void initContentVersions() throws ApsSystemException {
		this.cleanContentVersions();
		ContentRecordVO record = this._contentManager.loadContentVO("ART1");
		ContentVersion version1 = this.createContentVersion(1, record.getId(), record.getTypeCode(), 
				"Articolo", Content.STATUS_DRAFT, record.getXmlWork(), DateConverter.parseDate("2005-02-13", "yyyy-MM-dd"), 
				"0.0", 0, true, "admin");
		this.addContentVersion(version1);
		ContentVersion version2 = this.createContentVersion(2, record.getId(), record.getTypeCode(), 
				"Articolo 2", Content.STATUS_DRAFT, record.getXmlWork(), DateConverter.parseDate("2005-02-14", "yyyy-MM-dd"), 
				"0.1", 0, false, "mainEditor");
		this.addContentVersion(version2);
		ContentVersion version3 = this.createContentVersion(3, record.getId(), record.getTypeCode(), 
				"Articolo 3", Content.STATUS_READY, record.getXmlWork(), DateConverter.parseDate("2005-02-15", "yyyy-MM-dd"), 
				"1.0", 1, true, "mainEditor");
		this.addContentVersion(version3);
	}
	
	public void cleanContentVersions() throws ApsSystemException {
		Connection conn = null;
		Statement stat = null;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			stat.executeUpdate(DELETE_VERSIONS);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new ApsSystemException("Error cleaning content versions", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	public void cleanTrashedResources() throws ApsSystemException {
		Connection conn = null;
		Statement stat = null;
		try {
			conn = this.getConnection();
			stat = conn.createStatement();
			stat.executeUpdate(DELETE_TRASHED_RESOURCES);
		} catch (Throwable t) {
			t.printStackTrace();
			throw new ApsSystemException("Error cleaning trashed resources", t);
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	public void addContentVersion(ContentVersion contentVersion) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_VERSION_RECORD);
			stat.setLong(1, contentVersion.getId());
			stat.setString(2, contentVersion.getContentId());
			stat.setString(3, contentVersion.getContentType());
			stat.setString(4, contentVersion.getDescr());
			stat.setString(5, contentVersion.getStatus());
			stat.setString(6, contentVersion.getXml());
			stat.setTimestamp(7, new Timestamp(contentVersion.getVersionDate().getTime()));
			stat.setString(8, contentVersion.getVersion());
			stat.setInt(9, contentVersion.getOnlineVersion());
			int approved = contentVersion.isApproved() ? 1 : 0;
			stat.setInt(10, approved);
			stat.setString(11, contentVersion.getUsername());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			this.executeRollback(conn);
			processDaoException(t, "Errore in aggiunta VersionRecord", "addVersionRecord");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	public void addTrashedResource(String id, String type, String descr, String mainGroup, String xml, ResourceInterface resource) {
		Connection conn = null;
		PreparedStatement stat = null;
        try {
        	conn = this.getConnection();
        	stat = conn.prepareStatement(ADD_RESOURCE);
			stat.setString(1, resource.getId());
			stat.setString(2, resource.getType());
			stat.setString(3, resource.getDescr());
			stat.setString(4, resource.getMainGroup());
			stat.setString(5, resource.getXML());
			stat.executeUpdate();
        } catch (Throwable t) {
            processDaoException(t, "Error adding record for trashed resource", "addTrashedResource");
        } finally {
            closeDaoResources(null, stat, conn);
        }
	}
	
	public ContentVersion createContentVersion(long id, String contentId, String contentType, 
			String descr, String status, String xml, Date versionDate, String version, 
			int onlineVersion, boolean approved, String username) {
		ContentVersion contentVersion = new ContentVersion();
		contentVersion.setId(id);
		contentVersion.setContentId(contentId);
		contentVersion.setContentType(contentType);
		contentVersion.setDescr(descr);
		contentVersion.setStatus(status);
		contentVersion.setXml(xml);
		contentVersion.setVersionDate(versionDate);
		contentVersion.setVersion(version);
		contentVersion.setOnlineVersion(onlineVersion);
		contentVersion.setApproved(approved);
		contentVersion.setUsername(username);
		return contentVersion;
	}

	private IContentManager _contentManager;
	private IResourceManager _resourceManager;
	
	private static final String DELETE_VERSIONS = 
		"TRUNCATE jpversioning_versionedcontents";
	
	private static final String DELETE_TRASHED_RESOURCES = 
		"TRUNCATE jpversioning_trashedresources";
	
	private final String ADD_VERSION_RECORD = 
		"INSERT INTO jpversioning_versionedcontents ( id, contentid, contenttype, descr, " +
		"status, xml, versiondate, version, onlineversion, approved, username ) " +
		" VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ?, ?, ? )";
	
	private final String ADD_RESOURCE = 
		"INSERT INTO jpversioning_trashedresources ( resid, restype, descr, maingroup, resxml ) VALUES ( ? , ? , ? , ? , ? )";
	
}