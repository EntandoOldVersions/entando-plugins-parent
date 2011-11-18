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
package com.agiletec.plugins.jpphotogallery.apsadmin.page.specialshowlet.photogallery;

public class NumberAttributeFilterAction extends com.agiletec.plugins.jacms.apsadmin.portal.specialshowlet.listviewer.NumberAttributeFilterAction {
	
	public String getModelIdMaster() {
		return _modelIdMaster;
	}
	public void setModelIdMaster(String modelIdMaster) {
		this._modelIdMaster = modelIdMaster;
	}
	
	public String getModelIdPreview() {
		return _modelIdPreview;
	}
	public void setModelIdPreview(String modelIdPreview) {
		this._modelIdPreview = modelIdPreview;
	}
	
	// Parametri showlet jpphotogallery
	private String _modelIdMaster;
	private String _modelIdPreview;
	
}