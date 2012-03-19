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
package com.agiletec.plugins.jpmyportalplus.aps.internalservlet.util;

import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Showlet;

/**
 * @author E.Santoboni
 */
public class FrameSelectItem {
	
	public FrameSelectItem(Integer columnId, Integer columnIdDest, 
			Showlet showlet, Integer frameId, Lang lang) {
		super();
		this._currentColumnId = columnId;
		this._columnIdDest = columnIdDest;
		this._showlet = showlet;
		this._frameId = frameId;
		this._lang = lang;
	}
	
	public Boolean getSameColumn() {
		return (null != this.getCurrentColumnId() 
				&& null != this.getColumnIdDest() 
				&& this.getCurrentColumnId().equals(this.getColumnIdDest()));
	}
	
	public Integer getCurrentColumnId() {
		return _currentColumnId;
	}
	public Integer getColumnIdDest() {
		return _columnIdDest;
	}
	public Showlet getShowlet() {
		return _showlet;
	}
	public Integer getFrameId() {
		return _frameId;
	}
	
	public String getCode() {
		if (this._showlet == null) return null;
		return this._showlet.getType().getCode();
	}
	
	public String getTitle() {
		if (this._showlet == null) return null;
		String title = this._showlet.getType().getTitles().getProperty(this._lang.getCode());
		if (null != title) {
			return title;
		}
		return this.getCode();
	}
	
	private Integer _currentColumnId;
	private Showlet _showlet;
	private Integer _frameId;
	private Lang _lang;
	
	private Integer _columnIdDest;
	
}
