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
package com.agiletec.plugins.jpmyportalplus.aps.tags.util;

import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.showlettype.ShowletType;

/**
 * @author E.Santoboni
 */
public class WidgetCheckInfo {
	
	public WidgetCheckInfo(ShowletType showletType, Boolean checked, Lang lang) {
		this._showletType = showletType;
		this._checked = checked;
		this._lang = lang;
	}
	
	public ShowletType getShowletType() {
		return _showletType;
	}
	
	public Boolean getChecked() {
		return _checked;
	}
	
	public Lang getLang() {
		return _lang;
	}
	
	public String getCode() {
		return this._showletType.getCode();
	}
	
	public String getTitle() {
		String title = this._showletType.getTitles().getProperty(this._lang.getCode());
		if (null != title) {
			return title;
		}
		return this.getCode();
	}
	
	private ShowletType _showletType;
	private Boolean _checked;
	private Lang _lang;
	
}
