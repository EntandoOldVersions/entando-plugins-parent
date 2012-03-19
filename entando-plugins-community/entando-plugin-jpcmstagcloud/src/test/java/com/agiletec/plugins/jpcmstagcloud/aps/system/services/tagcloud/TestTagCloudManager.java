/*
 *
 * Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
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
 * Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
 *
 */
package com.agiletec.plugins.jpcmstagcloud.aps.system.services.tagcloud;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import com.agiletec.plugins.jpcmstagcloud.aps.JpcmstagcloudBaseTestCase;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.user.IUserManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentDAO;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentDAO;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpcmstagcloud.aps.system.JpcmstagcloudSystemConstants;
import com.agiletec.plugins.jpcmstagcloud.aps.system.services.tagcloud.ITagCloudManager;
import com.agiletec.plugins.jpcmstagcloud.aps.system.services.tagcloud.TagCloudManager;

/**
 * @author E.Santoboni
 */
public class TestTagCloudManager extends JpcmstagcloudBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
		this.addFakeContents();
	}
	
	@Override
	protected void tearDown() throws Exception {
		this.removeFakeContents();
		super.tearDown();
	}
	
	public void testLoadPublicTaggedContentsId() throws Throwable {
		UserDetails user = this.getUser("admin");
		List<String> contentIds = this._tagCloudManager.loadPublicTaggedContentsId("cat1", user);
		compareIds(new String[] { "ART180" }, contentIds);
		
		contentIds = this._tagCloudManager.loadPublicTaggedContentsId("evento", user);
		compareIds(new String[] { "EVN192", "EVN193", "RAH101b", "ART120b" }, contentIds);
		
		user = this.getUser("editorCoach");
		contentIds = this._tagCloudManager.loadPublicTaggedContentsId("evento", user);
		compareIds(new String[] { "EVN192", "EVN193", "RAH101b" }, contentIds);
	}
	
	public void testGetCloudInfos() throws Throwable {
		UserDetails user = this.getUser("admin");
		Map<String, Integer> expected = new HashMap<String, Integer>();
		expected.put("cat1", new Integer(1));
		expected.put("evento", new Integer(4));
		expected.put("general", new Integer(1));
		
		Map<ITreeNode, Integer> cloudInfos = this._tagCloudManager.getCloudInfos(user);
		this.compareCloudInfos(expected, cloudInfos);
		
		user = this.getUser("editorCoach");
		expected.put("cat1", new Integer(1));
		expected.put("evento", new Integer(3));
		cloudInfos = this._tagCloudManager.getCloudInfos(user);
		this.compareCloudInfos(expected, cloudInfos);
		
		user = this._userManager.getGuestUser();
		expected.put("cat1", new Integer(1));
		expected.put("evento", new Integer(2));
		expected.remove("general");
		cloudInfos = this._tagCloudManager.getCloudInfos(user);
		this.compareCloudInfos(expected, cloudInfos);
	}
	
	private void compareIds(String[] expected, List<String> received) {
		assertEquals(expected.length, received.size());
		for (String id : expected) {
			if (!received.contains(id)) {
				fail("Expected " + id + " - not found in " + received.toString());
			}
		}
	}
	
	private void compareCloudInfos(Map<String, Integer> expected, Map<ITreeNode, Integer> received) {
		//assertEquals(expected.size(), received.size());
		for (Entry<ITreeNode, Integer> current : received.entrySet()) {
			String key = current.getKey().getCode();
			Integer expectedSize = expected.get(key);
			if (expectedSize != null) {
				assertEquals(expectedSize.intValue(), current.getValue().intValue());
			}
		}
	}
	
	private void addFakeContents() throws ApsSystemException {
		((TagCloudManager) this._tagCloudManager).setDelayDays(20000);
    	Content masterContent = this._contentManager.loadContent("EVN193", true);
    	
    	Content content1 = this._contentManager.loadContent("RAH101", true);
    	content1.setId("RAH101b");
    	for (Category category : masterContent.getCategories()) {
    		content1.addCategory(category);
    	}
    	this._contentDao.addEntity(content1);
    	this._contentDao.insertOnLineContent(content1);
    	
    	Content content2 = this._contentManager.loadContent("ART120", true);
    	content2.setId("ART120b");
    	for (Category category : masterContent.getCategories()) {
    		content2.addCategory(category);
    	}
    	this._contentDao.addEntity(content2);
    	this._contentDao.insertOnLineContent(content2);
	}
	
	private void removeFakeContents() {
		this._contentDao.deleteEntity("RAH101b");
		this._contentDao.deleteEntity("ART120b");
	}
	
	private void init() throws Exception {
		try {
			this._tagCloudManager = (ITagCloudManager) this.getService(JpcmstagcloudSystemConstants.TAG_CLOUD_MANAGER);
			((TagCloudManager) this._tagCloudManager).setTagCloudCategoryRoot("home");
			((TagCloudManager) this._tagCloudManager).refresh();
			this._userManager = (IUserManager) this.getService(SystemConstants.USER_MANAGER);
			this._contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
			ContentDAO contentDao = new ContentDAO();
			DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
			contentDao.setDataSource(dataSource);
			ILangManager langManager = (ILangManager) this.getService(SystemConstants.LANGUAGE_MANAGER);
			contentDao.setLangManager(langManager);
			this._contentDao = contentDao;
		} catch (Throwable t) {
			throw new Exception(t);
		}
	}
	
	private ITagCloudManager _tagCloudManager;
    private IContentDAO _contentDao;
    private IContentManager _contentManager;
	private IUserManager _userManager;
	
}