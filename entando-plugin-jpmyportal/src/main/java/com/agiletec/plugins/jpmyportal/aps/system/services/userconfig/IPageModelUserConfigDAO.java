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

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Showlet;
import com.agiletec.aps.system.services.showlettype.ShowletType;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.model.PageModelUserConfigBean;

public interface IPageModelUserConfigDAO {

	/**
	 * Return a bean containing the customized configuration of the user. If the user did not 
	 * customize any frame it returns null.
	 * @param username
	 * @return bean The bean containing the myportal customization of the current user, null otherwise.
	 */
	public PageModelUserConfigBean getUserConfig(String username);

	/**
	 * Salva nel DB una nuova voce relativa alla personalizzazione del sito di un dato utente, a partire dallo username di quest'ultimo, dal
	 * modello di pagina coinvolto e dal frame interessato e infine dalla showlet.
	 * 
	 * @param username l'utente al quale si aggiunge una personalizzazione 
	 * @param pageModelCode il modello di pagina coinvolto
	 * @param framepos il frame interessato
	 * @param showlet la showlet personalizzata da caricare
	 * @throws ApsSystemException se si cerca di intervenire in un frame che non esiste nel modello di pagina
	 */
	public void saveUserConfig(String username, String pageModelCode, int frame, Showlet showlet);

	/**
	 * Remove from the DB a single recored located through the user, the page model and the frame position.
	 * @param username the user that owning the configuration record 
	 * @param pageModelCode the involved page model
	 * @param framepos the frame related
	 */
	public void removeUserConfig(String username, String pageModelCode, int frame);

//	/**
//	 * To release a frame means to assign the default showlet 'voidShowlet' as passed by the manager and NOT 
//	 * to delete the relative record from the customization.<br />
//	 * NOTE: the void showlet is defined in the 'baseManagersConfig.xml' of the plugin
//	 * @param username the username of the configuration owner
//	 * @param pageModelCode the page model interested in the custmozation
//	 * @param framepos the frame of the page model
//	 * @param voidShowletCode the void showlet as passed by the manager.
//	 * @throws ApsSystemException if the customization takes places in an unknown page model or in an
//	 * invalid frame.
//	 * @throws Throwable for all other errors.
//	 */
//	public void releaseFrame(String username, String pageModelCode, int frame, String voidShowletCode);
	/**
	 * Assign to the current frame the default showlet as passed by the manager.<br />
	 * NOTE: the void showlet is defined in the 'baseManagersConfig.xml' of the plugin
	 * @param customization customization bean currently in use.
	 * @param pageModelCode current page model
	 * @param targetFrame frame to empty
	 * @param defaultShowletCode code of the default showlet 
	 * @return PageModelUserConfigBean the customization bean modified.
	 */
	public PageModelUserConfigBean voidFrame(PageModelUserConfigBean customization, String pageModelCode, int targetFrame,
			String defaultShowletCode);
	
	/**
	 * Swap two showlets minimizing the number of the connections to the database. The customization bean is
	 * consequently modified.<br />
	 * NOTE: even if the configuration bean may contain the showlet(s) to swap only those taken from the array
	 * provided by the caller are currently used. We may say that the configuration bean is used only to check
	 * if a frame has been previously customized.
	 * @param customization the bean containing the personalization
	 * @param pageModelCode page model involved in the showlets swapping
	 * @param firstFrame frame containig the first showlet
	 * @param secondFrame frame containig the second showlet
	 * @param Showlets array containing all the showlets for the given page models
	 * @return PageModelUserConfigBean the configuration bean modified.
	 * @throws ApsSystemException in case of error
	 */
	public PageModelUserConfigBean swapShowlets(PageModelUserConfigBean customization, String pageModelCode, int firstFrame, int secondFrame, 
			Showlet[] showlets);
	
	/**
	 * Associate the given showlet to the current frame.
	 * @param customization the customization bean currently in use.
	 * @param username the current user
	 * @param pageModelCode the current page model
	 * @param targetFrame the target frame
	 * @param showlet the showlet to assign
	 * @return PageModelUserConfigBean the modified customization bean
	 */
	public PageModelUserConfigBean assignShowletToFrame(PageModelUserConfigBean customization, String pageModelCode, int targetFrame, Showlet showlet);
	
	/**
	 * Update a record located by username, page model and frame position containing the name and
	 * the configuration of the user showlet.
	 * @param username The username owning the configuration to update
	 * @param pageModelCode The customized page model
	 * @param framepos The position of the frame
	 * @param showlet The showlet contained in the given frame position.
	 * @throws ApsSystemException if the customization takes places in an unknown page model or in an
	 * invalid frame.
	 * @throws Throwable for all other errors.
	 */
	public void updateUserConfig(String username, String pageModelCode, int framepos, Showlet showlet);
	
	/**
	 * This method removes from the user customization all those showlets that are no longer available to myportal itself or those
	 * that simply do not exist anymore in the catalog.
	 * @param configurableShowlets elenco delle showlet configurabili
	 * @param voidShowletCode unique code of the 'void showlet'
	 */
	public void syncCustomization(List<ShowletType> configurableShowlets, String voidShowletCode);

}
