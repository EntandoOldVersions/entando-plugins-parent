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
package com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model;

import com.agiletec.aps.system.services.page.Showlet;

/**
 * @author E.Santoboni
 */
public class ShowletUpdateInfoBean {
	
	public ShowletUpdateInfoBean(int framePos, Showlet showlet, int status) {
		this.setFramePos(framePos);
		this.setShowlet(showlet);
		this.setStatus(status);
	}
	
	public int getFramePos() {
		return _framePos;
	}
	protected void setFramePos(int framePos) {
		this._framePos = framePos;
	}
	
	public Showlet getShowlet() {
		return _showlet;
	}
	protected void setShowlet(Showlet showlet) {
		this._showlet = showlet;
	}
	
	public int getStatus() {
		return _status;
	}
	protected void setStatus(int status) {
		this._status = status;
	}
	
	private int _framePos;
	private Showlet _showlet;
	private int _status;
	
}