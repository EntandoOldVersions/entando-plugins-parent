/*
 *
 * Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
 *
 * This file is part of jAPS software.
 * jAPS is a free software; 
 * you can redistribute it and/or modify it
 * under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
 * 
 * See the file License for the specific language governing permissions   
 * and limitations under the License
 * 
 * 
 * 
 * Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
 *
 */
package com.agiletec.plugins.jpmyportal.aps.internalservlet.myportal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.agiletec.plugins.jpmyportal.apsadmin.ApsAdminPluginBaseTestCase;
import com.agiletec.plugins.jpmyportal.util.JpmyportalTestHelper;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.page.Showlet;
import com.agiletec.aps.system.services.pagemodel.IPageModelManager;
import com.agiletec.aps.system.services.showlettype.ShowletType;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpmyportal.aps.system.JpmyportalSystemConstants;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.IPageModelUserConfigManager;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.model.PageModelUserConfigBean;
import com.opensymphony.xwork2.Action;

public class TestMyPortalAction extends ApsAdminPluginBaseTestCase {
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
        this._helper.initUserPageConfig();
    }
	
	@Override
	protected void tearDown() throws Exception {
		this._helper.cleanUserPageConfig();
		super.tearDown();
	}
	
	private void init() {
		this._pageModelUserConfigManager = (IPageModelUserConfigManager) this.getService(JpmyportalSystemConstants.PAGE_MODEL_USER_CONFIG_MANAGER);
		this._pageModelManager = (IPageModelManager) this.getService(SystemConstants.PAGE_MODEL_MANAGER);
		this._helper = new JpmyportalTestHelper(this.getApplicationContext());
	}
	
	public void testSwapShowletActionWithNoBeanInSession() throws Throwable {
		UserDetails user = this.getFakeUser("editorCoach");
		PageModelUserConfigBean cfg = null; // this.getFakePersonalization("editorCoach", "home", "service");
		this.getRequest().getSession().setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, user);
		try {
			// esegue lo swap quando l'utente NON ha un bean di configurazione personalizzata in sessione
			this.initAction("/do/Front/jpmyportal/MyPortal", "swapFrames");
			this.addParameter("currentPage", "homepage");
			this.addParameter("frameSource", "0");
			this.addParameter("frameDest", "2");
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			cfg = (PageModelUserConfigBean) this.getRequest().getSession().getAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG);
			assertNotNull(cfg);
			assertNotNull(cfg.getUsername());
			assertEquals(user.getUsername() ,cfg.getUsername());
			assertNotNull(cfg.getConfig());
			assertTrue(cfg.getConfig().containsKey("home"));
			assertEquals("content_viewer", cfg.getConfig().get("home")[0].getType().getCode());
			assertEquals("content_viewer_list", cfg.getConfig().get("home")[2].getType().getCode());
			cfg = this._pageModelUserConfigManager.getUserConfig(user.getUsername());
			assertNotNull(cfg);
			assertNotNull(cfg.getUsername());
			assertEquals(user.getUsername() ,cfg.getUsername());
			assertNotNull(cfg.getConfig());
			assertTrue(cfg.getConfig().containsKey("home"));
			assertEquals("content_viewer", cfg.getConfig().get("home")[0].getType().getCode());
			assertEquals("content_viewer_list", cfg.getConfig().get("home")[2].getType().getCode());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._pageModelUserConfigManager.removeUserConfig("editorCoach", "home", 0);
			this._pageModelUserConfigManager.removeUserConfig("editorCoach", "home", 2);
		}
	}
	
	public void testSwapShowletWithBeanInSession() throws Throwable {
		UserDetails user = this.getFakeUser("mainEditor");
		PageModelUserConfigBean cfg = this._pageModelUserConfigManager.getUserConfig("mainEditor");
		this.getRequest().getSession().setAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG, cfg);
		this.getRequest().getSession().setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, user);
		try {
			this.initAction("/do/Front/jpmyportal/MyPortal", "swapFrames");
			this.addParameter("currentPage", "homepage");
			this.addParameter("frameSource", "3");
			this.addParameter("frameDest", "4");
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);
			cfg = (PageModelUserConfigBean) this.getRequest().getSession().getAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG);
			assertNotNull(cfg);
			assertNotNull(cfg.getUsername());
			assertEquals(user.getUsername() ,cfg.getUsername());
			assertNotNull(cfg.getConfig());
			assertTrue(cfg.getConfig().containsKey("home"));
			assertEquals("content_viewer_list", cfg.getConfig().get("home")[3].getType().getCode());
			assertEquals("jpmyportal_void", cfg.getConfig().get("home")[4].getType().getCode());
			cfg = this._pageModelUserConfigManager.getUserConfig(user.getUsername());
			assertNotNull(cfg);
			assertNotNull(cfg.getUsername());
			assertEquals(user.getUsername() ,cfg.getUsername());
			assertNotNull(cfg.getConfig());
			assertTrue(cfg.getConfig().containsKey("home"));
			assertEquals("content_viewer_list", cfg.getConfig().get("home")[3].getType().getCode());
			assertEquals("jpmyportal_void", cfg.getConfig().get("home")[4].getType().getCode());
		} catch (Throwable t) {
			throw t;
		} finally {
			Showlet restoreVoid = this.getFakeShowlet("jpmyportal_void");
			Showlet restoreViewer = this.getFakeShowlet("content_viewer_list");
			this._pageModelUserConfigManager.removeUserConfig(user.getUsername(), "home", 3);
			this._pageModelUserConfigManager.removeUserConfig(user.getUsername(), "home", 4);
			this._pageModelUserConfigManager.saveUserConfig(user.getUsername(), "home", 3, restoreVoid);
			this._pageModelUserConfigManager.saveUserConfig(user.getUsername(), "home", 4, restoreViewer);
		}
	}
	
	/**
	 * Corrisponde ad assegnare la showlet 'void' al frame selezionato
	 * @throws Throwable 
	 */
	public void testResetFrameWithNoBeanInSession() throws Throwable {
		UserDetails user = this.getFakeUser("Adso"); 
		PageModelUserConfigBean cfg = null;
		this.getRequest().getSession().setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, user);
		try {
			// la mancanza di un bean in sessione comporterà la creazione di un nuovo record
			this.initAction("/do/Front/jpmyportal/MyPortal", "emptyFrame");
			this.addParameter("currentPage", "service");
			this.addParameter("frameSource", "2");
			String result = this.executeAction();			
			assertEquals(Action.SUCCESS, result);
			cfg = (PageModelUserConfigBean) this.getRequest().getSession().getAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG);
			assertNotNull(cfg);
			assertEquals(user.getUsername(), cfg.getUsername());
			assertNotNull(cfg.getConfig());
			assertEquals("jpmyportal_void", cfg.getConfig().get("service")[2].getType().getCode());
			cfg = this._pageModelUserConfigManager.getUserConfig(user.getUsername());
			assertNotNull(cfg);
			assertEquals(user.getUsername(), cfg.getUsername());
			assertNotNull(cfg.getConfig());
			assertEquals("jpmyportal_void", cfg.getConfig().get("service")[2].getType().getCode());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._pageModelUserConfigManager.removeUserConfig(user.getUsername(), "service", 2);
		}
	}
	
	/**
	 * Corrisponde ad assegnare la showlet 'void' al frame selezionato
	 * @throws Throwable 
	 */
	public void testResetFrameWithBeanInSession() throws Throwable {
		UserDetails user = this.getFakeUser("mainEditor"); 
		PageModelUserConfigBean cfg = this._pageModelUserConfigManager.getUserConfig("mainEditor");
		this.getRequest().getSession().setAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG, cfg);
		this.getRequest().getSession().setAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER, user);
		try {
			// la presenza di un bean in sessione comporterà l'aggiornamento del record
			this.initAction("/do/Front/jpmyportal/MyPortal", "emptyFrame");
			this.addParameter("currentPage", "homepage");
			this.addParameter("frameSource", "5");
			String result = this.executeAction();			
			assertEquals(Action.SUCCESS, result);
			cfg = (PageModelUserConfigBean) this.getRequest().getSession().getAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG);
			assertNotNull(cfg);
			assertEquals(user.getUsername(), cfg.getUsername());
			assertNotNull(cfg.getConfig());
			assertEquals("jpmyportal_void", cfg.getConfig().get("home")[5].getType().getCode());
			cfg = this._pageModelUserConfigManager.getUserConfig("mainEditor");
			assertNotNull(cfg);
			assertEquals(user.getUsername(), cfg.getUsername());
			assertNotNull(cfg.getConfig());
			assertEquals("jpmyportal_void", cfg.getConfig().get("home")[5].getType().getCode());
			// controlla che l'altro modello di pagina non abbia subito modifiche
			assertTrue(cfg.getConfig().containsKey("service"));
			assertEquals(4, cfg.getConfig().get("service").length);
			assertEquals(null, cfg.getConfig().get("service")[0]);
			assertEquals(null, cfg.getConfig().get("service")[1]);
			assertEquals("jpmyportal_void", cfg.getConfig().get("service")[2].getType().getCode());
			assertEquals(null, cfg.getConfig().get("service")[3]);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._pageModelUserConfigManager.removeUserConfig(user.getUsername(), "home", 5);
		}
	}
	
	private UserDetails getFakeUser(String username) {
		User user = new User();
		user.setPassword(username);
		user.setUsername(username);
		user.setProfile(null);
		return user;
	}
	
	/**
	 * Crea una configurazione personalizzata da mettere in sessione. È possibile creare configurazioni con uno, due o senza modelli
	 * di pagina, il nome utente va sempre specificato
	 * @param name nome dell'utente associato
	 * @param firstModel crea o aggiungi alla mappa il modello di pagina indicato  
	 * @param secondModel crea o aggiungi alla mappa il modello di pagina indicato  
	 * @return un oggetto di configurazione di MyPortal 
	 */
	private PageModelUserConfigBean getFakePersonalization(String name, String firstModel, String secondModel) {
		PageModelUserConfigBean c = new PageModelUserConfigBean();
		Map<String, Showlet[]> map = null;
		if (null != firstModel  || null != secondModel ) {			
			map = new HashMap<String, Showlet[]>();
			if (firstModel != null) {
				int size = this._pageModelManager.getPageModel(firstModel).getFrames().length;
				Showlet[] showlet = new Showlet[size];
				if (size > 2) {
					showlet[0] = this.getFakeShowletWithNoConfig("content_viewer");
					showlet[2] = this.getFakeShowlet("login_form");
				} else {
					showlet[0] = this.getFakeShowlet("content_viewer_list");
				}				
				map.put(firstModel, showlet);
			}			
			if (secondModel != null) {
				int size = this._pageModelManager.getPageModel(secondModel).getFrames().length;
				Showlet[] showlet = new Showlet[size];
				if (size > 2) {
					showlet[0] = this.getFakeShowlet("content_viewer_list");
					showlet[2] = this.getFakeShowletWithNoConfig("settoreSelect");
				} else {
					showlet[0] = this.getFakeShowletWithNoConfig("content_viewer");
				}
				map.put(secondModel, showlet);
			}
		}
		c.setConfig(map);
		c.setUsername(name);
		return c;
	}
	
	/**
	 * Crea una showlet con valori 'custom' sia nel codice che nella configurazione
	 * @param custom codice, replicato poi nella chiave e nel valore
	 * @return
	 */
	private Showlet getFakeShowlet(String custom) {
		Showlet fakeShowlet = new Showlet();
		ApsProperties fakeProperties = null;
//		ApsProperties fakeProperties = new ApsProperties();
//		fakeProperties.setProperty("Key-"+custom, "Value-"+custom);
		ShowletType fakeType = new ShowletType();
		fakeType.setCode(custom);
		fakeShowlet.setConfig(fakeProperties);
		fakeShowlet.setType(fakeType);
		return fakeShowlet;
	}
	
	/**
	 * crea una showlet con il solo codice identificativo.
	 * @param custom
	 * @return
	 */
	private Showlet getFakeShowletWithNoConfig(String custom) {
		Showlet fakeShowlet = new Showlet();
		ApsProperties fakeProperties = new ApsProperties();
		ShowletType fakeType = new ShowletType();
		fakeType.setCode(custom);
		fakeShowlet.setConfig(fakeProperties);
		fakeShowlet.setType(fakeType);
		return fakeShowlet;
	}
	
	/**
	 * Dump della configurazione usato solo ai fini di debug 
	 * @param c
	 */
	private void dumpConfiguration(PageModelUserConfigBean customization) {
		Iterator<String> itr = customization.getConfig().keySet().iterator();
		while (itr.hasNext()) {
			String key = itr.next();
			Showlet[] currentmap = customization.getConfig().get(key);
			System.out.println("KEY "+key);
			if (null == currentmap)
				continue;
			for (int scan=0;scan<currentmap.length;scan++) {
				if (null != currentmap[scan]) {
					System.out.println("  "+scan+" "+currentmap[scan].getType().getCode());
				} else {
					System.out.println("  "+scan+" NULL");
				}
			}
		}
	}
	
	private IPageModelUserConfigManager  _pageModelUserConfigManager;
	private IPageModelManager _pageModelManager;
	private JpmyportalTestHelper _helper;
	
}