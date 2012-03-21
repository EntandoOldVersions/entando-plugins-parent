/*
*
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpcontentrefs.apsadmin.contentrelations;

import java.util.List;

import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;

/**
 * @author E.Santoboni
 */
public abstract class AbstractContentRelationAction extends BaseAction implements IContentRelationAction {
	
	@Override
	public String edit() {
		if (!this.isValidContentType()) {
			return "intro";
		}
		return SUCCESS;
	}
	
	protected boolean isValidContentType() {
		String contTypeCode = this.getContentTypeCode();
		return (contTypeCode != null && this.getContentManager().getSmallContentTypesMap().get(contTypeCode) != null);
	}
	
	public SmallContentType getContentType(String contentTypeCode) {
		return (SmallContentType) this.getContentManager().getSmallContentTypesMap().get(contentTypeCode);
	}
	
	public List<SmallContentType> getContentTypes() {
		return this.getContentManager().getSmallContentTypes();
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	public String getContentTypeCode() {
		return _contentTypeCode;
	}
	public void setContentTypeCode(String contentTypeCode) {
		this._contentTypeCode = contentTypeCode;
	}
	
	public IContentManager _contentManager;
	private String _contentTypeCode;
	
}
