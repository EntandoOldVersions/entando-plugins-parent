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
package com.agiletec.plugins.jpmyportal.aps.system;

public interface JpmyportalSystemConstants {
	
	public static final String MYPORTAL_CONFIG_ITEM = "jpmyportal_config";
	
	/**
	 * Nome del servizio che gestisce i modelli di pagine personalizzati di MyPortal.
	 */
	public static final String PAGE_MODEL_USER_CONFIG_MANAGER = "jpmyportalPageModelUserConfigManager";
	
	/**
	 * Nome parametro di sessione: configurazione personalizzata dell'utente corrente
	 */
	public static final String SESSIONPARAM_CURRENT_USER_PAGE_MODEL_CONFIG = "jpmyportal_currentUserPageModelConfig";
	
	/**
	 * Nome parametro extra per requestContext: pagina corrente dal quale estrarre la descrizione
	 */
	public static final String EXTRAPAR_CURRENT_PAGE = "extraParamCurrentPage";
	
	/**
	 * Nome parametro extra per requestContext: lista dei frames liberamente configurabili
	 */
	public static final String EXTRAPAR_CURRENT_PAGE_MODEL_SWAPPABLE_FRAMES = "pageModelSwappableFrames";
	
	/**
	 * Nome del parametro di configurazione XML con il quale sono indicate le showlet liberamente configurabili
	 */
	public static final String XML_PARAM_CUSTOMIZABLE_SHOWLETS = "allowed";
	

	
}