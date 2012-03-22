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
package com.agiletec.plugins.jpmyportalplus.aps.system.services.pagemodel;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.pagemodel.PageModel;
import com.agiletec.plugins.jpmyportalplus.aps.ApsPluginBaseTestCase;

/**
 * @author E.Santoboni
 */
public class TestPageModelManager extends ApsPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.init();
	}

	public void testGetPageModel_1() throws Throwable {
		PageModel pageModel = this._pageModelManager.getPageModel("home");
		assertNotNull(pageModel);
		assertTrue(pageModel instanceof MyPortalPageModel);
		Frame[] frames = ((MyPortalPageModel) pageModel).getFrameConfigs();
		assertEquals(6, frames.length);
		for (int i = 0; i < frames.length; i++) {
			Frame frame = frames[i];
			assertTrue(frame.isLocked());
		}
	}
	
	public void testGetPageModel_2() throws Throwable {
		PageModel pageModel = this._pageModelManager.getPageModel("jpmyportalplus_pagemodel");
		assertNotNull(pageModel);
		assertTrue(pageModel instanceof MyPortalPageModel);
		Frame[] frames = ((MyPortalPageModel) pageModel).getFrameConfigs();
		assertEquals(8, frames.length);
		for (int i = 0; i < frames.length; i++) {
			Frame frame = frames[i];
			if (i == 0 || i == 7) {
				assertTrue(frame.isLocked());
			} else {
				assertFalse(frame.isLocked());
			}
			if (i == 1 || i == 2) {
				assertEquals(1, frame.getColumn().intValue());
			} else if (i == 3 || i == 4) {
				assertEquals(2, frame.getColumn().intValue());
			} else if (i == 5 || i == 6) {
				assertEquals(3, frame.getColumn().intValue());
			} else assertNull(frame.getColumn());
		}
	}
	
	private void init() throws Exception {
		try {
			this._pageModelManager = (IPageModelManager) this.getService(SystemConstants.PAGE_MODEL_MANAGER);
		} catch (Throwable e) {
			throw new Exception(e);
		}
	}
	
	protected IPageModelManager _pageModelManager;
	
}
