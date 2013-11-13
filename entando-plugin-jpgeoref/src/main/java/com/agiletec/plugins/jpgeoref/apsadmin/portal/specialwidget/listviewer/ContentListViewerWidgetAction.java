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
package com.agiletec.plugins.jpgeoref.apsadmin.portal.specialwidget.listviewer;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.IContentListWidgetHelper;
import com.agiletec.plugins.jacms.aps.system.services.content.widget.UserFilterOptionBean;
import com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.listviewer.IContentListFilterAction;
import com.agiletec.plugins.jpgeoref.aps.system.GeoRefSystemConstants;

/**
 * Action per la gestione della configurazione della showlet erogatore avanzato lista contenuti.
 * @author E.Santoboni
 */
public class ContentListViewerWidgetAction extends com.agiletec.plugins.jacms.apsadmin.portal.specialwidget.listviewer.ContentListViewerWidgetAction {
	
	@Override
	public void validate() {
		super.validate();
		try {
			if (null == this.getFieldErrors().get("contentType")) {
				if (null == this.getContentManager().createContentType(this.getContentType())) {
					this.addFieldError("contentType", this.getText("error.widget.listViewer.invalidContentType", new String[]{this.getContentType()}));
				}
			}
			if (this.getActionErrors().size()>0 || this.getFieldErrors().size()>0) {
				this.setShowlet(super.createNewShowlet());
				return;
			}
			this.createValuedShowlet();
			this.validateTitle();
			this.validateLink();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "validate", "Error validating list viewer");
		}
	}
	
	/**
	 * Restituisce la lista di contenuti (in forma small) definiti nel sistema che contengono un'attributo coords.
	 * Il metodo è a servizio delle jsp che richiedono questo dato per fornire
	 * una corretta visualizzazione della pagina.
	 * @return La lista di tipi di contenuto (in forma small) definiti nel sistema.
	 */
	@Override
	public List<SmallContentType> getContentTypes() {
		List<SmallContentType> list = new ArrayList<SmallContentType>();
		try {
			List<SmallContentType> smallContentTypes = super.getContentTypes();
			if (null != smallContentTypes && !smallContentTypes.isEmpty()) {
				for (int i = 0; i < smallContentTypes.size(); i++) {
					SmallContentType smallContentType = smallContentTypes.get(i);
					Content prototype = (Content) this.getContentManager().getEntityPrototype(smallContentType.getCode());
					if (null != prototype.getAttributeByRole(GeoRefSystemConstants.ATTRIBUTE_ROLE_COORD)) {
						list.add(smallContentType);
					}
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentTypes", "Error extracting content types");
			throw new RuntimeException("Error extracting content types", t);
		}
		return list;
	}
        
        public static final String ATTRIBUTE_TYPE_COORD = "Coords";        
        
        @Override
	public List<SelectItem> getAllowedUserFilterTypes() throws ApsSystemException {
		List<SelectItem> types = new ArrayList<SelectItem>();
		try {
			types.add(new SelectItem(UserFilterOptionBean.KEY_FULLTEXT, this.getText("label.fulltext")));
			types.add(new SelectItem(UserFilterOptionBean.KEY_CATEGORY, this.getText("label.category")));
			String contentType = this.getWidget().getConfig().getProperty(IContentListWidgetHelper.WIDGET_PARAM_CONTENT_TYPE);
			Content prototype = this.getContentManager().createContentType(contentType);
			List<AttributeInterface> contentAttributes = prototype.getAttributeList();
			for (int i=0; i<contentAttributes.size(); i++) {
				AttributeInterface attribute = contentAttributes.get(i);
				if (attribute.isSearcheable()) {
                                    // Escludo filtro per tipo Coordinate
                                    if(!ContentListViewerWidgetAction.ATTRIBUTE_TYPE_COORD.equals(attribute.getType()))
					types.add(new SelectItem(UserFilterOptionBean.TYPE_ATTRIBUTE + "_" + attribute.getName(), this.getText("label.attribute", new String[]{attribute.getName()})));
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getAllowedUserFilterTypes");
			throw new ApsSystemException("Error extracting allowed user filter types", t);
		}
		return types;
	}
        
        @Override
	public List<SelectItem> getAllowedFilterTypes() throws ApsSystemException {
		List<SelectItem> types = new ArrayList<SelectItem>();
		try {
			types.add(new SelectItem(IContentListFilterAction.METADATA_KEY_PREFIX + IContentManager.CONTENT_CREATION_DATE_FILTER_KEY, this.getText("label.creationDate")));
			types.add(new SelectItem(IContentListFilterAction.METADATA_KEY_PREFIX + IContentManager.CONTENT_MODIFY_DATE_FILTER_KEY, this.getText("label.lastModifyDate")));
			String contentType = this.getWidget().getConfig().getProperty(IContentListWidgetHelper.WIDGET_PARAM_CONTENT_TYPE);
			Content prototype = this.getContentManager().createContentType(contentType);
			List<AttributeInterface> contentAttributes = prototype.getAttributeList();
			for (int i=0; i<contentAttributes.size(); i++) {
				AttributeInterface attribute = contentAttributes.get(i);
				if (attribute.isSearcheable()) {
                                    // Escludo filtro per tipo Coordinate
                                    if(!ContentListViewerWidgetAction.ATTRIBUTE_TYPE_COORD.equals(attribute.getType()))
					types.add(new SelectItem(attribute.getName(), this.getText("label.attribute", new String[]{attribute.getName()})));
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getAllowedFilterTypes");
			throw new ApsSystemException("Error extracting allowed filter types", t);
		}
		return types;
	}        
	
}