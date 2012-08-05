/*
*
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpgeoref.apsadmin.portal.specialshowlet.listviewer;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jpgeoref.aps.system.GeoRefSystemConstants;

/**
 * Action per la gestione della configurazione della showlet erogatore avanzato lista contenuti.
 * @author E.Santoboni
 */
public class ContentListViewerShowletAction extends com.agiletec.plugins.jacms.apsadmin.portal.specialshowlet.listviewer.ContentListViewerShowletAction {
	
	@Override
	public void validate() {
		super.validate();
		try {
			if (null == this.getFieldErrors().get("contentType")) {
				if (null == this.getContentManager().createContentType(this.getContentType())) {
					this.addFieldError("contentType", this.getText("error.showlet.listViewer.invalidContentType", new String[]{this.getContentType()}));
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
	
}