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
package com.agiletec.plugins.jpnewsletter.aps.system.services.url;

import javax.servlet.http.HttpServletResponse;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.url.PageURL;
import com.agiletec.aps.system.services.url.URLManager;

/**
 * Servizio di gestione degli url; crea un URL completo ad una pagina del portale 
 * a partire da informazioni essenziali.
 * @author 
 */
public class JpnewsletterURLManager extends URLManager {
	
	@Override
	public void init() throws Exception {
		ApsSystemUtils.getLogger().config(this.getClass().getName() + ": inizializzato");
	}
	
	/**
	 * Crea un URL completo ad una pagina del portale a partire dalle informazioni
	 * essenziali contenute nell'oggetto pageUrl passato come parametro.<br>
	 * In questa implementazione, l'URL è costruito come concatenazione dei seguenti elementi:
	 * <ul>
	 * <li> parametro di configurazione PAR_APPL_BASE_URL, che rappresenta l'URL
	 * base della web application così come viene visto dall'esterno; deve comprendere
	 * la stringa "http://" e deve terminare con "/";
	 * <li> codice della lingua impostata nell'oggetto pageUrl, oppure la lingua corrente, 
	 * oppure la lingua di default;
	 * <li> codice della pagina impostata nell'oggetto pageUrl, oppure la pagina corrente, 
	 * oppure la root page;
	 * <li> eventuale query string se sull'oggetto pageUrl sono stati impostati parametri.
	 * </ul>
	 * @param pageUrl L'oggetto contenente le informazioni da tradurre in URL.
	 * @see com.agiletec.aps.system.services.url.AbstractURLManager#getURLString(com.agiletec.aps.system.services.url.PageURL, com.agiletec.aps.system.RequestContext)
	 */
	@Override
	public String getURLString(PageURL pageUrl, RequestContext reqCtx){
		String langCode = pageUrl.getLangCode();
		ILangManager langManager = this.getLangManager();
		Lang lang = langManager.getLang(langCode);
		if(lang == null) {
			if (reqCtx != null) {
				lang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
			}
			if (lang == null) {
				lang = this.getLangManager().getDefaultLang();
			}
		}
		IPageManager pageManager = this.getPageManager();
		String pageCode = pageUrl.getPageCode();
		IPage page = pageManager.getPage(pageCode);
		if (page == null && reqCtx != null) {
			page = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
		}
		if(page == null) {
			page = pageManager.getRoot();
		}
		String url = this.createUrl(page, lang, pageUrl.getParams());
		if (reqCtx != null) {
			HttpServletResponse resp = reqCtx.getResponse();
			url = resp.encodeURL(url.toString());  
		}
		return url;
	}
	
}