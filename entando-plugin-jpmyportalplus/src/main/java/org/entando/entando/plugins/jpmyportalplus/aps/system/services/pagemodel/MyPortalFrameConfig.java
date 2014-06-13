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
package org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel;

/**
 * Representation of a frame of page model
 * @author E.Santoboni
 */
public class MyPortalFrameConfig {
	
	public Integer getColumn() {
		return _column;
	}
	public void setColumn(Integer column) {
		this._column = column;
	}
	
	public boolean isLocked() {
		return _locked;
	}
	public void setLocked(boolean locked) {
		this._locked = locked;
	}
	
	private boolean _locked;
	private Integer _column;
	
}