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
package com.agiletec.plugins.jpmyportal.aps.system.services.pagemodel;

import com.agiletec.aps.system.services.pagemodel.PageModel;

/**
 * This class represents a page model. This object contains only the description and
 * the definition of the available frames. The true definition of the model is contained
 * in a JSP file. It is assumed that the JSP shares the same name with the related
 * page model. The frames are the part of the page able to host a showlet. 
 * @author E. Santoboni
 */
public class MyPortalPageModel extends PageModel {
	
	/**
	 * Return the sorted frames of the model.
	 * @return An array containing the frames
	 */
	public Frame[] getFrameConfigs() {
		return _frames;
	}
	
	/**
	 * Set the frames of the page model 
	 * @param frames The array containing the frames
	 */
	protected void setFrameConfigs(Frame[] frames) {
		this._frames = frames;
	}
	
	/**
	 * Array containing the description of the frames
	 */
	private Frame[] _frames = new Frame[0];
	
}
