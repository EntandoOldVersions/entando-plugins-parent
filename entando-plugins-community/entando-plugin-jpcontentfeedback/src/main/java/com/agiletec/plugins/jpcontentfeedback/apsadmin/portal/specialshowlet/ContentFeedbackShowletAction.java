/*
*
* Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
* This file is part of jAPS software.
* Entando is a free software;
* you can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
*
* See the file License for the specific language governing permissions
* and limitations under the License
*
*
*
* Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
*/
package com.agiletec.plugins.jpcontentfeedback.apsadmin.portal.specialshowlet;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.apsadmin.portal.specialshowlet.SimpleShowletConfigAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;

/**
 * Action per la gestione della configurazione della showlet erogatore contenuto singolo,
 * e del blocco relativo agli elementi del contentFeedback: commenti e rating
 * @author D.Cherchi
 *
 */
public class ContentFeedbackShowletAction  extends SimpleShowletConfigAction implements IContentFeedbackShowletAction{

	@Override
	public void validate() {
		super.validate();
		if (this.getFieldErrors().size()==0) {
			try {
				if (this.getContentId()!=null){
					Content publishingContent = this.getContentManager().loadContent(this.getContentId(), true);
					if (null == publishingContent) {
						this.addFieldError("contentId", this.getText("Page.specialShowlet.viewer.nullContent"));
					} else {
						IPage currentPage = this.getCurrentPage();
						String mainGroup = currentPage.getGroup();
						if (!publishingContent.getMainGroup().equals(Group.FREE_GROUP_NAME) && !publishingContent.getGroups().contains(Group.FREE_GROUP_NAME) &&
								!publishingContent.getMainGroup().equals(mainGroup) && !publishingContent.getGroups().contains(mainGroup) &&
								!Group.ADMINS_GROUP_NAME.equals(mainGroup)) {
							this.addFieldError("contentId", this.getText("Page.specialShowlet.viewer.invalidContent"));
						}
					}
				}
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "validate", "Errore in validazione contenuto con id " + this.getContentId());
				throw new RuntimeException("Errore in validazione contenuto con id " + this.getContentId(), t);
			}
		}
		if (this.getFieldErrors().size()>0) {
			try {
				this.createValuedShowlet();
			} catch (Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "validate", "Errore in creazione showlet valorizzata");
				throw new RuntimeException("Errore in creazione showlet valorizzata", t);
			}
		}
	}

	@Override
	public String joinContent() {
		try {
			this.createValuedShowlet();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "joinContent");
			throw new RuntimeException("Errore in associazione contenuto", t);
		}
		return SUCCESS;
	}

	/**
	 * Restituisce il contenuto vo in base all'identificativo.
	 * @param contentId L'identificativo del contenuto.
	 * @return Il contenuto vo cercato.
	 */
	public ContentRecordVO getContentVo(String contentId) {
		ContentRecordVO contentVo = null;
		try {
			contentVo = this.getContentManager().loadContentVO(contentId);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentVo");
			throw new RuntimeException("Errore in caricamento contenuto vo", t);
		}
		return contentVo;
	}

	/**
	 * Restituisce la lista di Modelli di Contenuto compatibili con il contenuto specificato.
	 * @param contentId Il contenuto cui restituire i modelli compatibili.
	 * @return La lista di Modelli di Contenuto compatibili con il contenuto specificato.
	 */
	public List<ContentModel> getModelsForContent(String contentId) {
		if (null == contentId) return new ArrayList<ContentModel>();
		String typeCode = contentId.substring(0, 3);
		return this.getContentModelManager().getModelsForContentType(typeCode);
	}
	
	protected IContentModelManager getContentModelManager() {
		return _contentModelManager;
	}
	public void setContentModelManager(IContentModelManager contentModelManager) {
		this._contentModelManager = contentModelManager;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	public String getContentId() {
		return _contentId;
	}
	public void setContentId(String contentId) {
		this._contentId = contentId;
	}
	
	public String getModelId() {
		return _modelId;
	}
	public void setModelId(String modelId) {
		this._modelId = modelId;
	}
	
	private IContentModelManager _contentModelManager;
	private IContentManager _contentManager;
	private String _contentId;
	private String _modelId;
	
}