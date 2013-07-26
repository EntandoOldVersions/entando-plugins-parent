/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpmyportal.aps.system.services.userconfig;

import java.util.Iterator;
import java.util.List;

import com.agiletec.plugins.jpmyportal.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpmyportal.util.JpmyportalTestHelper;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.services.page.org.entando.entando.aps.system.services.page.Widget;
import com.agiletec.aps.system.services.showlettype.IShowletTypeManager;
import com.agiletec.aps.system.services.showlettype.ShowletType;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpmyportal.aps.system.JpmyportalSystemConstants;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.IPageModelUserConfigManager;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.model.PageModelUserConfigBean;

public class TestPageModelUserConfigManager extends ApsPluginBaseTestCase {
	
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
		this._showletTypeManager = (IShowletTypeManager) this.getService(SystemConstants.SHOWLET_TYPE_MANAGER);
		this._helper = new JpmyportalTestHelper(this.getApplicationContext());
	}
	
	/**
	 * Creiamo una configurazione fittizia per l'utente che poi cancelliamo
	 * @throws Throwable
	 */
	public void testSaveUserConfigAndDelete() throws Throwable {
		PageModelUserConfigBean userConfig = null;
		try {
			assertNull(this._pageModelUserConfigManager.getUserConfig("wiz"));
			this._pageModelUserConfigManager.saveUserConfig("wiz", "home", 0, getFakeShowlet("login_form"));
			this._pageModelUserConfigManager.saveUserConfig("wiz", "home", 1, getFakeShowletWithNoConfig("content_viewer"));
			this._pageModelUserConfigManager.saveUserConfig("wiz", "home", 2, getFakeShowlet("content_viewer"));			
			this._pageModelUserConfigManager.saveUserConfig("wiz", "service", 3, getFakeShowlet("content_viewer_list"));
			this._pageModelUserConfigManager.saveUserConfig("wiz", "service", 2, getFakeShowletWithNoConfig("content_viewer"));
			this._pageModelUserConfigManager.saveUserConfig("wiz", "service", 1, getFakeShowlet("content_viewer"));
			this._pageModelUserConfigManager.saveUserConfig("wiz", "service", 0, getFakeShowlet("content_viewer_list"));	
		} catch (Throwable t) {
			throw t;
		} finally {
			this._pageModelUserConfigManager.removeUserConfig("wiz", "home", 2);
			this._pageModelUserConfigManager.removeUserConfig("wiz", "home", 1);
			this._pageModelUserConfigManager.removeUserConfig("wiz", "home", 0);
			this._pageModelUserConfigManager.removeUserConfig("wiz", "service", 0);
			this._pageModelUserConfigManager.removeUserConfig("wiz", "service", 1);
			this._pageModelUserConfigManager.removeUserConfig("wiz", "service", 2);
			this._pageModelUserConfigManager.removeUserConfig("wiz", "service", 3);
			userConfig = this._pageModelUserConfigManager.getUserConfig("wiz");
			assertNull(userConfig);
		}
	}
	
	/**
	 * Carichiamo una configurazione relativa ad un utente già esistente nel db quindi verifichiamo le dimensioni dela lista (una 
	 * chiave per ogni pagemodel) quindi nell'array corrispondente che esistano le showlet (alcune senza configurazione) in maniera
	 * consistente con quanto registrato nel database
	 * @throws Throwable
	 */
	public void testGetUserConfig() throws Throwable {
		PageModelUserConfigBean userCfg = null;
		org.entando.entando.aps.system.services.page.Widget[] showletArray = null;
		int nullShowlet=0;
		int nullConfig=0;
		try {
			// carica il profilo di un utente insesistente
			userCfg = this._pageModelUserConfigManager.getUserConfig("nottolo");
			assertNull(userCfg);
			// carichiamo il profilo associato a un utente esistente
			userCfg = this._pageModelUserConfigManager.getUserConfig("mainEditor");
			assertNotNull(userCfg);
			assertEquals("mainEditor", userCfg.getUsername());
			assertNotNull(userCfg.getConfig());
			// verifichiamo la grandezza della mappa e i nomi dei pagemodels
			assertEquals(2, userCfg.getConfig().size());
			assertTrue(userCfg.getConfig().containsKey("home"));
			assertTrue(userCfg.getConfig().containsKey("service"));
			// verifichiamo la grandezza degli array
			showletArray = userCfg.getConfig().get("home");
			assertEquals(6, showletArray.length);
			for (int scan=0; scan<showletArray.length; scan++) {
				if (null == showletArray[scan]) {
					nullShowlet++;
				} else {
					ApsProperties config = showletArray[scan].getConfig();
					if (config==null || config.isEmpty()) {
						nullConfig++;
					}
				}
			}
			assertEquals(4, nullShowlet);
			assertEquals(2, nullConfig);
			// contiamo le showlet nulle e nelle altre contiamo le configurazioni nulle; pagemodel == 'service'
			nullShowlet = 0;
			nullConfig = 0;
			
			showletArray = userCfg.getConfig().get("service");
			assertEquals(4, showletArray.length);
			// contiamo le showlet nulle e nelle altre contiamo le configurazioni nulle; pagemodel == 'home'
			showletArray = userCfg.getConfig().get("home");
			showletArray = userCfg.getConfig().get("service");
			for (int scan=0;scan<4;scan++) {
				if (null == showletArray[scan]) {
					nullShowlet++;
				} else {
					ApsProperties config = showletArray[scan].getConfig();
					if (config==null || config.isEmpty()) {
						nullConfig++;
					}
				}
			}
			assertEquals(3, nullShowlet);
			assertEquals(1, nullConfig);
		} catch (Throwable t) {
			throw t;
		}
	}
	
	/**
	 * Associamo la showlet void ad un frame individuato per utente, modello di pagina e frame number. La showlet void viene caricata 
	 * assieme al manager in fase d'avvio
	 * @throws Throwable
	 */
	public void testReleaseFrame() throws Throwable {
		PageModelUserConfigBean userCfg;
		PageModelUserConfigBean sessionUserCfg= null;
		try {
			// Se l'utente NON ha un record lo crea, altrimenti lo aggiorna. Il bean di personalizzazione deve essere allineato
			sessionUserCfg = this._pageModelUserConfigManager.voidFrame(sessionUserCfg, "pageManagerCoach", "home", 3);
			userCfg = this._pageModelUserConfigManager.getUserConfig("pageManagerCoach");
			assertNotNull(userCfg);
			assertTrue(userCfg.getConfig().containsKey("home"));
			assertNotNull(sessionUserCfg);
			assertEquals("jpmyportal_void", sessionUserCfg.getConfig().get("home")[3].getType().getCode());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._pageModelUserConfigManager.removeUserConfig("pageManagerCoach", "home", 3);
		}
	}
	
	public void testTransactionalSwapFrames() throws Throwable {
		org.entando.entando.aps.system.services.page.Widget zero = this.getFakeShowlet("login_form");
		org.entando.entando.aps.system.services.page.Widget uno = this.getFakeShowlet("content_viewer");
		org.entando.entando.aps.system.services.page.Widget due = this.getFakeShowlet("jpmyportal_void");
		org.entando.entando.aps.system.services.page.Widget tre = this.getFakeShowlet("content_viewer_list");
		org.entando.entando.aps.system.services.page.Widget showlets[] = {zero,uno,due,tre};
		PageModelUserConfigBean sessionUserCfg= null;
		PageModelUserConfigBean userCfg = null;
		try {
			// se l'utente NON ha personalizzazioni lo scambio creerà due record nel DB. Controlla la coerenza della personalizzazione aggiornata
			sessionUserCfg = this._pageModelUserConfigManager.swapShowlets(sessionUserCfg, "pageManagerCoach", "home", 0, 1, showlets); //0-uno, 1-zero
			userCfg = this._pageModelUserConfigManager.getUserConfig("pageManagerCoach");
			assertNotNull(userCfg);
			assertEquals("pageManagerCoach", userCfg.getUsername());
			assertNotNull(userCfg.getConfig());
			assertNotNull(userCfg.getConfig().get("home"));
			assertEquals(uno.getType().getCode(), userCfg.getConfig().get("home")[0].getType().getCode());
			assertEquals(zero.getType().getCode(), userCfg.getConfig().get("home")[1].getType().getCode());
			assertNotNull(sessionUserCfg);
			assertEquals(uno.getType().getCode(), sessionUserCfg.getConfig().get("home")[0].getType().getCode());
			assertEquals(zero.getType().getCode(), sessionUserCfg.getConfig().get("home")[1].getType().getCode());
			// la prima showlet è già in cfg personalizzata, la seconda no
			showlets[0] = uno;
			showlets[1] = zero;
			this._pageModelUserConfigManager.swapShowlets(sessionUserCfg, "pageManagerCoach", "home", 1, 2, showlets); //0-uno, 1-due, 2-zero
			userCfg = this._pageModelUserConfigManager.getUserConfig("pageManagerCoach");
			assertNotNull(userCfg);
			assertEquals(uno.getType().getCode(), userCfg.getConfig().get("home")[0].getType().getCode());
			assertEquals(due.getType().getCode(), userCfg.getConfig().get("home")[1].getType().getCode());
			assertEquals(zero.getType().getCode(), userCfg.getConfig().get("home")[2].getType().getCode());
			assertNotNull(sessionUserCfg);
			assertEquals(uno.getType().getCode(), sessionUserCfg.getConfig().get("home")[0].getType().getCode());
			assertEquals(due.getType().getCode(), sessionUserCfg.getConfig().get("home")[1].getType().getCode());
			assertEquals(zero.getType().getCode(), sessionUserCfg.getConfig().get("home")[2].getType().getCode());
			// la prima showlet non è in configurazione personalizzata, la seconda sì
			showlets[0] = uno;
			showlets[1] = due;
			showlets[2] = zero;
			this._pageModelUserConfigManager.swapShowlets(sessionUserCfg, "pageManagerCoach", "home", 3, 0, showlets); //0-tre, 1-due, 2-zero, 3-uno
			userCfg = this._pageModelUserConfigManager.getUserConfig("pageManagerCoach");
			assertNotNull(userCfg);
			assertEquals(tre.getType().getCode(), userCfg.getConfig().get("home")[0].getType().getCode());
			assertEquals(due.getType().getCode(), userCfg.getConfig().get("home")[1].getType().getCode());
			assertEquals(zero.getType().getCode(), userCfg.getConfig().get("home")[2].getType().getCode());
			assertEquals(uno.getType().getCode(), userCfg.getConfig().get("home")[3].getType().getCode());
			assertNotNull(sessionUserCfg);
			assertEquals(tre.getType().getCode(), sessionUserCfg.getConfig().get("home")[0].getType().getCode());
			assertEquals(due.getType().getCode(), sessionUserCfg.getConfig().get("home")[1].getType().getCode());
			assertEquals(zero.getType().getCode(), sessionUserCfg.getConfig().get("home")[2].getType().getCode());
			assertEquals(uno.getType().getCode(), sessionUserCfg.getConfig().get("home")[3].getType().getCode());
			// entrambe le showlet sono contenute nella configurazione personalizzata
			showlets[0] = tre;
			showlets[1] = due;
			showlets[2] = zero;
			showlets[3] = uno;
			this._pageModelUserConfigManager.swapShowlets(sessionUserCfg, "pageManagerCoach", "home", 3, 2, showlets);
			userCfg = this._pageModelUserConfigManager.getUserConfig("pageManagerCoach");
			assertNotNull(userCfg);
			assertEquals(tre.getType().getCode(), userCfg.getConfig().get("home")[0].getType().getCode());
			assertEquals(due.getType().getCode(), userCfg.getConfig().get("home")[1].getType().getCode());
			assertEquals(uno.getType().getCode(), userCfg.getConfig().get("home")[2].getType().getCode());
			assertEquals(zero.getType().getCode(), userCfg.getConfig().get("home")[3].getType().getCode());
			assertNotNull(sessionUserCfg);
			assertEquals(tre.getType().getCode(), sessionUserCfg.getConfig().get("home")[0].getType().getCode());
			assertEquals(due.getType().getCode(), sessionUserCfg.getConfig().get("home")[1].getType().getCode());
			assertEquals(uno.getType().getCode(), sessionUserCfg.getConfig().get("home")[2].getType().getCode());
			assertEquals(zero.getType().getCode(), sessionUserCfg.getConfig().get("home")[3].getType().getCode());			
		} catch (Throwable t) {
			throw t;
		} finally {
			this._pageModelUserConfigManager.removeUserConfig("pageManagerCoach", "home", 0);
			this._pageModelUserConfigManager.removeUserConfig("pageManagerCoach", "home", 1);
			this._pageModelUserConfigManager.removeUserConfig("pageManagerCoach", "home", 2);
			this._pageModelUserConfigManager.removeUserConfig("pageManagerCoach", "home", 3);
		}
	}
	
	public void testTransactionalAssignFrame() throws Throwable {
		org.entando.entando.aps.system.services.page.Widget firstShowlet = this.getFakeShowlet("login_form");
		org.entando.entando.aps.system.services.page.Widget secondShowlet = this.getFakeShowlet("content_viewer");
		PageModelUserConfigBean sessionUserCfg= null;
		PageModelUserConfigBean userCfg;
		try {
			// il frame da configurare NON è presente in configurazione personalizzata
			sessionUserCfg = this._pageModelUserConfigManager.assignShowletToFrame(sessionUserCfg, "pageManagerCoach", "home", 0, firstShowlet);
			userCfg = this._pageModelUserConfigManager.getUserConfig("pageManagerCoach");
			assertNotNull(userCfg);
			assertEquals(firstShowlet.getType().getCode(), userCfg.getConfig().get("home")[0].getType().getCode());
			assertNotNull(sessionUserCfg);
			assertTrue(sessionUserCfg.getConfig().containsKey("home"));
			assertEquals(firstShowlet.getType().getCode(), sessionUserCfg.getConfig().get("home")[0].getType().getCode());
			// il frame da configurare è presente in configurazione personalizzata
			this._pageModelUserConfigManager.assignShowletToFrame(sessionUserCfg, "pageManagerCoach", "home", 0, secondShowlet);
			userCfg = this._pageModelUserConfigManager.getUserConfig("pageManagerCoach");
			assertNotNull(userCfg);
			assertEquals(secondShowlet.getType().getCode(), userCfg.getConfig().get("home")[0].getType().getCode());
			assertNotNull(sessionUserCfg);
			assertTrue(sessionUserCfg.getConfig().containsKey("home"));
			assertEquals(secondShowlet.getType().getCode(), sessionUserCfg.getConfig().get("home")[0].getType().getCode());
			// configuriamo un altro frame 
			sessionUserCfg =  this._pageModelUserConfigManager.assignShowletToFrame(sessionUserCfg, "pageManagerCoach", "service", 1, secondShowlet);
			userCfg = this._pageModelUserConfigManager.getUserConfig("pageManagerCoach");
			assertEquals(secondShowlet.getType().getCode(), userCfg.getConfig().get("service")[1].getType().getCode());
			assertNotNull(sessionUserCfg);
			assertTrue(sessionUserCfg.getConfig().containsKey("service"));
			assertTrue(sessionUserCfg.getConfig().containsKey("home"));
			assertEquals(secondShowlet.getType().getCode(), sessionUserCfg.getConfig().get("service")[1].getType().getCode());
			assertEquals(secondShowlet.getType().getCode(), userCfg.getConfig().get("home")[0].getType().getCode());
			assertEquals(secondShowlet.getType().getCode(), sessionUserCfg.getConfig().get("home")[0].getType().getCode());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._pageModelUserConfigManager.removeUserConfig("pageManagerCoach", "home", 0);
			this._pageModelUserConfigManager.removeUserConfig("pageManagerCoach", "service", 1);
		}
	}
	
	public void testTransactionalRemoveFrame() throws Throwable {
		org.entando.entando.aps.system.services.page.Widget showlet = this.getFakeShowlet("jpmyportal_void");
		PageModelUserConfigBean sessionUserCfg= null;
		PageModelUserConfigBean userCfg;
		try {
			sessionUserCfg = this._pageModelUserConfigManager.assignShowletToFrame(sessionUserCfg, "pageManagerCoach", "service", 1, showlet);
			this._pageModelUserConfigManager.assignShowletToFrame(sessionUserCfg, "pageManagerCoach", "home", 0, showlet);
			userCfg = this._pageModelUserConfigManager.getUserConfig("pageManagerCoach");
			assertNotNull(userCfg);
			assertNotNull(userCfg.getConfig());
			assertTrue(userCfg.getConfig().containsKey("service"));
			assertTrue(userCfg.getConfig().containsKey("home"));
			assertNotNull(sessionUserCfg);
			assertNotNull(sessionUserCfg.getConfig());
			assertTrue(sessionUserCfg.getConfig().containsKey("service"));
			assertTrue(sessionUserCfg.getConfig().containsKey("home"));
			sessionUserCfg = this._pageModelUserConfigManager.voidFrame(sessionUserCfg, "pageManagerCoach", "service", 1);
			userCfg = this._pageModelUserConfigManager.getUserConfig("pageManagerCoach");
			assertNotNull(userCfg);
			assertNotNull(userCfg.getConfig());
			assertTrue(userCfg.getConfig().containsKey("service"));
			assertTrue(userCfg.getConfig().containsKey("home"));
			assertEquals(showlet.getType().getCode(), userCfg.getConfig().get("home")[0].getType().getCode());
			assertEquals("jpmyportal_void", userCfg.getConfig().get("service")[1].getType().getCode());
			assertNotNull(sessionUserCfg);
			assertNotNull(sessionUserCfg.getConfig());
			assertTrue(sessionUserCfg.getConfig().containsKey("service"));
			assertTrue(sessionUserCfg.getConfig().containsKey("home"));
			assertEquals(showlet.getType().getCode(), sessionUserCfg.getConfig().get("home")[0].getType().getCode());
			assertEquals("jpmyportal_void", sessionUserCfg.getConfig().get("service")[1].getType().getCode());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._pageModelUserConfigManager.removeUserConfig("pageManagerCoach", "service", 1);
			this._pageModelUserConfigManager.removeUserConfig("pageManagerCoach", "home", 0);
		}
	}
	
	/**
	 * Testiamo le routine di aggiornamento di un record. Come caso speciale proviamo ad aggiornare un record inesistente nel
	 * database: la showlet associata deve continuare ad essere nulla
	 * @throws Throwable
	 */
	public void testUpdateUserConfiguration() throws Throwable {
		PageModelUserConfigBean userCfg;
		try {
			userCfg = this._pageModelUserConfigManager.getUserConfig("pageManagerCoach");
			assertNull(userCfg);
			this._pageModelUserConfigManager.saveUserConfig("pageManagerCoach", "service", 3, getFakeShowletWithNoConfig("login_form"));
			this._pageModelUserConfigManager.updateUserConfig("pageManagerCoach", "service", 3, getFakeShowlet("content_viewer"));
			userCfg = this._pageModelUserConfigManager.getUserConfig("pageManagerCoach");
			assertNotNull(userCfg);
			assertTrue(userCfg.getConfig().containsKey("service"));
			assertTrue(!userCfg.getConfig().get("service")[3].getConfig().isEmpty());
			assertEquals("content_viewer", userCfg.getConfig().get("service")[3].getType().getCode());
			// aggiorniamo un record inesistente
			this._pageModelUserConfigManager.updateUserConfig("pageManagerCoach", "service", 1, getFakeShowlet("content_viewer"));
			userCfg = this._pageModelUserConfigManager.getUserConfig("pageManagerCoach");
			assertNotNull(userCfg);
			assertTrue(userCfg.getConfig().containsKey("service"));
			assertNull(userCfg.getConfig().get("service")[1]);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._pageModelUserConfigManager.removeUserConfig("pageManagerCoach", "service", 3);
		}
	}
	
	/**
	 * Controlla che modificare la configurazione per un modello di pagina lasci i rimanenti inalterati
	 * @throws Throwable
	 */
	public void testConfigurationBeanSanity() throws Throwable {
		PageModelUserConfigBean userCfg;
		PageModelUserConfigBean sessionUserCfg= null;
		org.entando.entando.aps.system.services.page.Widget showlet = this.getFakeShowlet("void");
		try {
			assertNull(this._pageModelUserConfigManager.getUserConfig("pageManagerCoach"));
			this._pageModelUserConfigManager.saveUserConfig("pageManagerCoach", "home", 0, getFakeShowlet("login_form"));
			this._pageModelUserConfigManager.saveUserConfig("pageManagerCoach", "home", 1, getFakeShowletWithNoConfig("content_viewer"));
			this._pageModelUserConfigManager.saveUserConfig("pageManagerCoach", "home", 2, getFakeShowlet("content_viewer"));			
			userCfg = this._pageModelUserConfigManager.getUserConfig("pageManagerCoach");
			assertNotNull(userCfg);
			sessionUserCfg = this._pageModelUserConfigManager.assignShowletToFrame(userCfg, userCfg.getUsername(), "service", 3, showlet);
			assertNotNull(sessionUserCfg);
			assertEquals("pageManagerCoach", sessionUserCfg.getUsername());
			assertNotNull(sessionUserCfg.getConfig().get("service"));
			assertNotNull(sessionUserCfg.getConfig().get("service")[3]);
			assertNotNull(sessionUserCfg.getConfig().get("service")[3].getType());
			assertNotNull(sessionUserCfg.getConfig().get("service")[3].getConfig());
			assertNull(sessionUserCfg.getConfig().get("service")[3].getPublishedContent());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._pageModelUserConfigManager.removeUserConfig("pageManagerCoach", "home", 0);
			this._pageModelUserConfigManager.removeUserConfig("pageManagerCoach", "home", 1);
			this._pageModelUserConfigManager.removeUserConfig("pageManagerCoach", "home", 2);
			this._pageModelUserConfigManager.removeUserConfig("pageManagerCoach", "service", 3);
		}
	}
	
	/**
	 * Assicura che l'insieme delle showlet configurabili comprenda solo quelle realmente esistenti nello
	 * showlet catalog
	 * @throws Throwable
	 */
	public void testConfigurationParams() throws Throwable {
		try {
			List<ShowletType> allowedShowlets = this._pageModelUserConfigManager.getCustomizableShowlets();
			assertNotNull(allowedShowlets);
			assertEquals(3, allowedShowlets.size());
			// verifica la consistenza dei dati
			Iterator<ShowletType> itr = allowedShowlets.iterator();
			assertNotNull(itr);
			while (itr.hasNext()) {
				ShowletType currentType = itr.next();
				assertNotNull(this._showletTypeManager.getShowletType(currentType.getCode()));
			}
		} catch (Throwable t) {
			throw t;
		}
	}
	
	/**
	 * Verifica che la cancellazione dal database di configurazione di tutte quelle showlet non più esistenti o non più configurabili.
	 * @throws Throwable
	 */
	public void testPurgeConfiguration() throws Throwable {
		PageModelUserConfigBean userCfg;
		try {
			assertNull(this._pageModelUserConfigManager.getUserConfig("pageManagerCoach"));
			// aggiungiamo un record con una showlet inesistente		
			this._pageModelUserConfigManager.saveUserConfig("pageManagerCoach", "home", 0, getFakeShowlet("nonesiste"));
			// aggiungiamo un record con una showlet non configurabile			
			this._pageModelUserConfigManager.saveUserConfig("pageManagerCoach", "home", 4, getFakeShowlet("formAction"));
			((AbstractService) this._pageModelUserConfigManager).refresh();
			userCfg = this._pageModelUserConfigManager.getUserConfig("pageManagerCoach");
			assertNull(userCfg);
		} catch (Throwable t) {
			throw t;
		} finally {
			this._pageModelUserConfigManager.removeUserConfig("pageManagerCoach", "home", 0);
			this._pageModelUserConfigManager.removeUserConfig("pageManagerCoach", "home", 4);
		}
	}
	
	/**
	 * Crea una showlet con valori 'custom' sia nel codice che nella configurazione
	 * @param custom codice, replicato poi nella chiave e nel valore
	 * @return
	 */
	private org.entando.entando.aps.system.services.page.Widget getFakeShowlet(String custom) {
		org.entando.entando.aps.system.services.page.Widget fakeShowlet = new org.entando.entando.aps.system.services.page.Widget();
		ApsProperties fakeProperties = new ApsProperties();
		ShowletType fakeType = new ShowletType();		
		fakeProperties.setProperty("Key-"+custom, "Value-"+custom);
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
	private org.entando.entando.aps.system.services.page.Widget getFakeShowletWithNoConfig(String custom) {
		org.entando.entando.aps.system.services.page.Widget fakeShowlet = new org.entando.entando.aps.system.services.page.Widget();
		ApsProperties fakeProperties = new ApsProperties();
		ShowletType fakeType = new ShowletType();
		fakeType.setCode(custom);
		fakeShowlet.setConfig(fakeProperties);
		fakeShowlet.setType(fakeType);
		return fakeShowlet;
	}
	
	private IShowletTypeManager _showletTypeManager;
	private IPageModelUserConfigManager _pageModelUserConfigManager;
	private JpmyportalTestHelper _helper;
	
}