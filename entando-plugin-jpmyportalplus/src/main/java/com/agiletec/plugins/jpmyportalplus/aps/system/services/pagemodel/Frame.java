/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
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
package com.agiletec.plugins.jpmyportalplus.aps.system.services.pagemodel;

import org.entando.entando.aps.system.services.page.Widget;


/**
 * Representation of a frame of page model
 * @author E.Santoboni
 */
public class Frame {

	public int getPos() {
		return _pos;
	}
	public void setPos(int pos) {
		this._pos = pos;
	}

	public String getDescr() {
		return _descr;
	}
	public void setDescr(String descr) {
		this._descr = descr;
	}

	public boolean isMainFrame() {
		return _mainFrame;
	}
	public void setMainFrame(boolean mainFrame) {
		this._mainFrame = mainFrame;
	}

	public Integer getColumn() {
		return _column;
	}
	public void setColumn(Integer column) {
		this._column = column;
	}

	public Widget getDefaultShowlet() {
		return _defaultShowlet;
	}
	public void setDefaultShowlet(Widget defaultShowlet) {
		this._defaultShowlet = defaultShowlet;
	}

	public boolean isLocked() {
		return _locked;
	}
	public void setLocked(boolean locked) {
		this._locked = locked;
	}

	private int _pos;
	private String _descr;
	private boolean _mainFrame;
	private boolean _locked;
	private Integer _column;
	private Widget _defaultShowlet;

}
