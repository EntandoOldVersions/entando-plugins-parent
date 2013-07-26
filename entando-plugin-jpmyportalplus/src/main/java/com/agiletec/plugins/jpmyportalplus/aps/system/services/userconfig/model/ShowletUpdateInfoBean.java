/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando Enterprise Edition software.
* You can redistribute it and/or modify it
* under the terms of the Entando's EULA
*
* See the file License for the specific language governing permissions
* and limitations under the License
*
*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model;

import org.entando.entando.aps.system.services.page.Widget;



/**
 * @author E.Santoboni
 */
public class ShowletUpdateInfoBean {

	public ShowletUpdateInfoBean(int framePos, org.entando.entando.aps.system.services.page.Widget showlet, int status) {
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

	public org.entando.entando.aps.system.services.page.Widget getShowlet() {
		return _showlet;
	}
	protected void setShowlet(org.entando.entando.aps.system.services.page.Widget showlet) {
		this._showlet = showlet;
	}

	public int getStatus() {
		return _status;
	}
	protected void setStatus(int status) {
		this._status = status;
	}

	private int _framePos;
	private org.entando.entando.aps.system.services.page.Widget _showlet;
	private int _status;

}
