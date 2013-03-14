/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpfastcontentedit.apsadmin.portal.specialshowlet.fastcontentedit;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.services.page.Showlet;
import com.agiletec.apsadmin.portal.specialshowlet.SimpleShowletConfigAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;

public class FastContentEditShowletConfigAction extends SimpleShowletConfigAction {
	
	@Override
	public void validate() {
		super.validate();
		if (null == this.getFieldErrors().get("contentType")) {
			if (null == this.getContentManager().createContentType(this.getContentType())) {
				//TODO DA RIVEDERE LABEL
				this.addFieldError("contentType", this.getText("INVALID_CONTENT_TYPE", new String[]{this.getContentType()}));
			}
		}
		if (this.getActionErrors().size()>0 || this.getFieldErrors().size()>0) {
			try {
				Showlet showlet = super.createNewShowlet();
				this.setShowlet(showlet);
			} catch (Throwable t) {
				throw new RuntimeException(t);
			}
		}
	}
	
	@Override
	public String save() {
		Logger log = ApsSystemUtils.getLogger();
		try {
			this.checkBaseParams();
			this.createValuedShowlet();
			this.getPageManager().joinShowlet(this.getPageCode(), this.getShowlet(), this.getFrame());
			log.finest("Salvataggio showlet - code = " + this.getShowlet().getType().getCode() + 
					", pageCode = " + this.getPageCode() + ", frame = " + this.getFrame());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return "configure";
	}
	
	/**
	 * Restituisce la lista di contenuti (in forma small) definiti nel sistema.
	 * Il metodo è a servizio delle jsp che richiedono questo dato per fornire 
	 * una corretta visualizzazione della pagina.
	 * @return La lista di tipi di contenuto (in forma small) definiti nel sistema.
	 */
	public List<SmallContentType> getContentTypes() {
		return this.getContentManager().getSmallContentTypes();
	}
	
	public List<AttributeInterface> getSelectableAttributes() {
		String typeCode = this.getShowlet().getConfig().getProperty("typeCode");
		List<AttributeInterface> attributesForSelect = new ArrayList<AttributeInterface>();
		if (null != typeCode && typeCode.length() > 0) {
			IApsEntity  contentType =  getContentManager().getEntityPrototype(typeCode);
			List<AttributeInterface> attributes =  contentType.getAttributeList();
			for (AttributeInterface attribute : attributes) {
				if (attribute.isTextAttribute()) {
					attributesForSelect.add(attribute);
				}
			}
		}
		return attributesForSelect;
	}
	
	public String configContentType() {
		try {
			Showlet showlet = super.createNewShowlet();
			showlet.getConfig().setProperty("typeCode", this.getContentType());
			this.setShowlet(showlet);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "init");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String changeContentType() {
		try {
			Showlet showlet = super.createNewShowlet();
			this.setShowlet(showlet);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "changeContentType");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public String getContentType() {
		return _contentType;
	}
	public void setContentType(String contentType) {
		this._contentType = contentType;
	}
	
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	public IContentManager getContentManager() {
		return _contentManager;
	}
	
	public void setAuthorAttribute(String authorAttribute) {
		this._authorAttribute = authorAttribute;
	}
	public String getAuthorAttribute() {
		return _authorAttribute;
	}
	
	private String _contentType;
	private IContentManager _contentManager;
	private String _authorAttribute;

}
