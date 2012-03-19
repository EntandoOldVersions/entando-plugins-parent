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
package com.agiletec.plugins.jpmyportal.aps.internalservlet.myportal;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletResponseAware;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Showlet;
import com.agiletec.aps.system.services.showlettype.IShowletTypeManager;
import com.agiletec.aps.system.services.showlettype.ShowletType;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpmyportal.aps.system.JpmyportalSystemConstants;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.IPageModelUserConfigManager;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.model.PageModelUserConfigBean;

/**
 * Azioni di MyPortalIntra.
 * NOTA: le azioni fanno un redirect sulla pagina corrente al momento al ritorno...
 */
public class MyPortalAction extends BaseAction implements IMyPortalAction, ServletResponseAware {
	
	/**
	 * Scambia il contenuto delle showlet contenute in due frame contenuti nella configurazione personalizzata dell'utente. Se l'utente
	 * effettua uno scambio con la showlet di un frame che non è nella configurazione personalizzata dell'utente allora la showlet viene
	 * recuperata dalla configurazione di default.
	 * @return il risultato dell'operazione.
	 */
	@Override
	public String swapFrames() {
		HttpServletRequest req = this.getRequest();
		PageModelUserConfigBean userConfiguration = (PageModelUserConfigBean) req.getSession().getAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG);
		IPage page =  this.getPageManager().getPage(this.getCurrentPage());
		String currentPageCode = page.getModel().getCode();
		Showlet[] defaultShowlets = page.getShowlets();
		try {
			int firstFrame = this.getFrameSource();
			int frameDest = this.getFrameDest();
			Showlet[] meshUp = null;
			Showlet[] customShowlets = null;
			UserDetails currentUser = (UserDetails) req.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			if (null != userConfiguration) {
				meshUp = new Showlet[defaultShowlets.length];
				customShowlets = userConfiguration.getConfig().get(currentPageCode);
				for (int scan = 0; scan < defaultShowlets.length; scan++) {
					if (null == customShowlets[scan]) {
						meshUp[scan]=defaultShowlets[scan];
					} else {
						meshUp[scan]=customShowlets[scan];
					}
				}
			} else {
				meshUp = defaultShowlets;
			}
			userConfiguration = this.getPageModelUserConfigManager().swapShowlets(
					userConfiguration, currentUser.getUsername(), currentPageCode, firstFrame, frameDest, meshUp);
			req.getSession().setAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG, userConfiguration);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "swapFrames", "Errore nell'azione \"swapFrames\"");
			return FAILURE;
		}
		return SUCCESS; 
	}
	
	/**
	 * Assegna la showlet vuota al frame corrente.<br />
	 * NOTA: in realtà il manager assegna il codice della showlet specificata nella configurazione, nell'implementazione corrente 'void',
	 * 		 ma potrebbe essere una qualunque
	 * @return il risultato dell'operazione.
	 */
	public String emptyCustomizableShowlet() {
		HttpServletRequest req = this.getRequest();
		PageModelUserConfigBean userConfiguration = (PageModelUserConfigBean) req.getSession().getAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG);		
		UserDetails currentUser = (UserDetails) req.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
		try {
			String currentPage = this.getCurrentPage();
			int currentFrame = this.getFrameSource();
			IPage page =  this.getPageManager().getPage(currentPage);
			String currentPageModel = page.getModel().getCode();
			userConfiguration = this.getPageModelUserConfigManager().voidFrame(userConfiguration, currentUser.getUsername(), currentPageModel, currentFrame);
			req.getSession().setAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG, userConfiguration);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "emptyCustomizableShowlet", "Errore nell'azione \"emptyCustomizableShowlet\"");
			return FAILURE;
		}
		return SUCCESS; 
	}	
	
	/**
	 * Assegna la showlet richiesta al frame in oggetto
	 * @return il risultato dell'operazione.
	 */
	@Override
	public String assignShowletToFrame() {
		HttpServletRequest req = this.getRequest();
		PageModelUserConfigBean userConfiguration = 
			(PageModelUserConfigBean) req.getSession().getAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG);
		try {
			String currentPage = this.getCurrentPage();
			int currentFrame = this.getFrameSource();
			String choosenShowlet = this.getChoosenShowlet();
			IPage page = this.getPageManager().getPage(currentPage);
			UserDetails currentUser = (UserDetails) req.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			String currentPageModelCode = page.getModel().getCode();
			Showlet targetShowlet = new Showlet();
			ShowletType inheritedType = this.getShowletTypeManager().getShowletType(choosenShowlet);
			if (null != inheritedType) {
				targetShowlet.setType(inheritedType);
				targetShowlet.setPublishedContent(null);
				targetShowlet.setConfig(new ApsProperties());
				if (inheritedType.isLogic()) {
					targetShowlet.setConfig(inheritedType.getConfig());
				}
			} else { 
				ApsSystemUtils.getLogger().info("ERRORE: \"assignShowletToFrame\" cerca di assegnare un tipo null");
			}
			userConfiguration = this.getPageModelUserConfigManager().assignShowletToFrame(
					userConfiguration, currentUser.getUsername(), currentPageModelCode, currentFrame, targetShowlet);
			req.getSession().setAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG, userConfiguration);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "assignShowletToFrame", "Errore nell'azione \"assignShowletToFrame\"");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Carica una lista di tutte le showlet che possono essere configurate
	 * @return il risultato dell'operazione.
	 */
	public String configureCustomizableShowlet() {
		List<ShowletType> customizableShowlets = this.getPageModelUserConfigManager().getCustomizableShowlets();
		this.setCustomizableShowlets(customizableShowlets);
		return SUCCESS;
	}
	
	@Override
	public String resetFrame() {
		HttpServletRequest req = this.getRequest();
		PageModelUserConfigBean userConfiguration = 
			(PageModelUserConfigBean) req.getSession().getAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG);
		try {
			String currentPage = this.getCurrentPage();
			int currentFrame = this.getFrameSource();
			IPage page = this.getPageManager().getPage(currentPage);
			UserDetails currentUser = (UserDetails) req.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
			String currentPageModelCode = page.getModel().getCode();
			this.getPageModelUserConfigManager().removeUserConfig(currentUser.getUsername(), currentPageModelCode, currentFrame);
			Showlet[] config = userConfiguration.getConfig().get(currentPageModelCode);
			config[currentFrame] = null;
			req.getSession().setAttribute(JpmyportalSystemConstants.SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG, userConfiguration);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "resetFrame", "Errore nell'azione \"resetFrame\"");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Costruisce il percorso del redirect diretto
	 * @return il percorso del redirect
	 */
	public String getDestForwardPath() {
		String applicationBaseUrl = this.getConfigManager().getParam(SystemConstants.PAR_APPL_BASE_URL);
		StringBuffer buffer = new StringBuffer(applicationBaseUrl);
		String currentLanguage = this.getCurrentLang().getCode();
		if (null == currentLanguage) {
			currentLanguage = this.getLangManager().getDefaultLang().getCode();
		}
		String currentPage = this.getCurrentPage();
		if (this.getPageManager().getPage(currentPage) == null) {
			currentPage = this.getPageManager().getRoot().getCode();
		}
		buffer.append(currentLanguage + "/" + currentPage);
		buffer.append(".wp");
		return buffer.toString();
	}
	
	/**
	 * Return the current system language used in the front-end interface. If this language does not
	 * belong to those known by the system the default language is returned. A log line will 
	 * report the problem.
	 * @return The current language.
	 */
	@Override
	public Lang getCurrentLang() {
		Lang currentLang = null;
		if (this.getCurrentLangCode()!=null) {
			currentLang = this.getLangManager().getLang(this.getCurrentLangCode());
		}
		if (currentLang==null) {
			RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
			if (reqCtx!=null) {
				currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
			}
			if (currentLang==null) {
				currentLang = super.getCurrentLang();
			}
		}
		return currentLang;
	}
	public String getCurrentLangCode() {
		return this._currentLangCode;
	}
	public void setCurrentLangCode(String currentLangCode) {
		this._currentLangCode = currentLangCode;
	}
	
	public Showlet getDefaultShowlet(String pageCode, String pos) {
		IPage page = this.getPageManager().getPage(pageCode);
		int posInteger = Integer.parseInt(pos);
		return page.getShowlets()[posInteger];
	}
	
	protected HttpServletResponse getResponse() {
		return _response;
	}
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this._response = response;
	}
	
	public ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	public IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}
	
	public IPageModelUserConfigManager getPageModelUserConfigManager() {
		return _pageModelUserConfigManager;
	}
	public void setPageModelUserConfigManager(IPageModelUserConfigManager pageModelUserConfigManager) {
		this._pageModelUserConfigManager = pageModelUserConfigManager;
	}
	
	public List<ShowletType> getCustomizableShowlets() {
		return _customizableShowlets;
	}
	public void setCustomizableShowlets(List<ShowletType> customizableShowlets) {
		this._customizableShowlets = customizableShowlets;
	}
	
	public IShowletTypeManager getShowletTypeManager() {
		return _showletTypeManager;
	}
	public void setShowletTypeManager(IShowletTypeManager showletTypeManager) {
		this._showletTypeManager = showletTypeManager;
	}
	
	public String getCurrentPage() {
		return _currentPage;
	}
	public void setCurrentPage(String currentPage) {
		this._currentPage = currentPage;
	}
	
	public Integer getFrameSource() {
		return _frameSource;
	}
	public void setFrameSource(Integer frameSource) {
		this._frameSource = frameSource;
	}
	
	public Integer getFrameDest() {
		return _frameDest;
	}
	public void setFrameDest(Integer frameDest) {
		this._frameDest = frameDest;
	}
	
	public String getChoosenShowlet() {
		return _choosenShowlet;
	}
	public void setChoosenShowlet(String choosenShowlet) {
		this._choosenShowlet = choosenShowlet;
	}
	
	/**
	 * Lista delle showlet configurabili
	 */
	private List<ShowletType>  _customizableShowlets;
	
	private String _currentPage;
	private String _currentLangCode;
	private Integer _frameSource;
	private Integer _frameDest;
	private String _choosenShowlet;
	
	private ConfigInterface _configManager;
	private HttpServletResponse _response;
	private IPageManager _pageManager;
	private IPageModelUserConfigManager _pageModelUserConfigManager;
	private IShowletTypeManager _showletTypeManager;
	
}