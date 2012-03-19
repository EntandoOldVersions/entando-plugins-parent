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
package com.agiletec.plugins.jpmyportal.aps.system.services.userconfig;

import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.page.Showlet;
import com.agiletec.aps.system.services.showlettype.ShowletType;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.model.MyPortalConfig;
import com.agiletec.plugins.jpmyportal.aps.system.services.userconfig.model.PageModelUserConfigBean;

/**
 * Interface for the service of MyPortal that handles the configuration of the page models. MyPortal enables
 * users to customize the page models of the portal they are visiting.
 * @author E.Santoboni
 */
public interface IPageModelUserConfigManager {
	
	public MyPortalConfig getConfig();
	
	public void saveConfig(MyPortalConfig config) throws ApsSystemException;
	
	/**
	 * Return the page model configuration belonging to the given user.
	 * The returned map is index by the code of the page model, and each element will contain the configuration
	 * for each frame of the model.
	 * @param username the owner of the customization
	 * @return The map of the page models customized by the user
	 * @throws ApsSystemException In case of error
	 */
	public PageModelUserConfigBean getUserConfig(String username) throws ApsSystemException;
	
	/**
	 * Save in the database a new customization item for the current user.
	 * @param username The user owning the new customization item.
	 * @param pageModelCode The page model personalized by the user
	 * @param framepos The frame customized.
	 * @param showlet The new showlet associated to the frame.
	 * @throws ApsSystemException when an attempt to configure an invalid frame is made
	 */
	public void saveUserConfig(String username, String pageModelCode, int frame, Showlet showlet) throws ApsSystemException;
	
	/**
	 * Remove from the database a single record located by username, page model and frame position. They
	 * all must exist at invocation time.
	 * @param username The user owning the record to delete from the configuration
	 * @param pageModelCode The page model 
	 * @param framepos The frame position within the page model
	 * @throws <Throwable> in case of error.
	 */
	public void removeUserConfig(String username, String pageModelCode, int frame) throws ApsSystemException;
	
	/**
	 * Update a configuration record located by username, page model and frame position with the name and
	 * the configuration of the given showlet.
	 * @param username
	 * @param pageModelCode
	 * @param framepos
	 * @param showlet
	 */
	public void updateUserConfig(String username, String pageModelCode, int framepos, Showlet showlet) throws ApsSystemException;
	
	/**
	 * Swaps the given showlets. If necessary a new customization bean is created to host the new 
	 * personalization in the page models for the current user.
	 * @param customization the current customization bean
	 * @param pageModelCode The code of the page model where the swap takes place
	 * @param firstFrame The frame where to start the swap process from
	 * @param secondFrame The second frame involved in the process
	 * @param showlets The array containing the showlets to swap, a mesh up of default showlet for that
	 * page model and the customized ones.
	 * @return PageModelUserConfigBean the updated customization bean.
	 * @throws ApsSystemException In case of error
	 */
	public PageModelUserConfigBean swapShowlets(PageModelUserConfigBean customization, String username, String pageModelCode, 
			int firstFrame, int secondFrame, Showlet[] showlets) throws ApsSystemException;
	
	/**
	 * Assign to the current frame the default showlet as passed by the manager.<br />
	 * NOTE: the void showlet is defined in the 'baseManagersConfig.xml' of the plugin
	 * @param customization The current customization bean
	 * @param username The username owning that bean
	 * @param pageModelCode The code of the page model
	 * @param targetFrame The frame to void
	 * @param voidShowletCode The code of the void showlet as passed by the manager
	 * @throws ApsSystemException In case of error
	 */
	public PageModelUserConfigBean voidFrame(PageModelUserConfigBean customization, 
			String username, String pageModelCode, int targetFrame) throws ApsSystemException;
	
	/**
	 * Put the given frame with a showlet
	 * @param customization The current customization bean
	 * @param pageModelCode The code of the page model
	 * @param targetFrame The frame where to put the given showlet
	 * @param showlet The showlet
	 * @throws ApsSystemException In case of error
	 */
	public PageModelUserConfigBean assignShowletToFrame(PageModelUserConfigBean customization, 
			String username, String pageModelCode, int targetFrame, Showlet showlet) throws ApsSystemException;
	
	/**
	 * Get the list of the showlet that cannot be handled by the jpmyportal plugin
	 */
	public List<ShowletType> getCustomizableShowlets();
	
	/**
	 * Return the code of the 'void showlet'
	 * @return The code of the 'void showlet'
	 */
	public String getVoidShowletCode();
	
}