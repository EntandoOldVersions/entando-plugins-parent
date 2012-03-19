/*
*
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpcontentworkflow.apsadmin.content;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.SelectItem;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.content.IContentSearcherManager;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.WorkflowSearchFilter;
import com.agiletec.plugins.jpcontentworkflow.apsadmin.content.helper.IContentWorkFlowActionHelper;

/**
 * @author E.Santoboni
 */
public class ContentFinderAction extends com.agiletec.plugins.jacms.apsadmin.content.ContentFinderAction {
	
	@Override
	public List<String> getContents() {
		List<String> result = null;
		try {
			if (this.hasCurrentUserPermission(Permission.SUPERUSER)) {
				return super.getContents();
			}
			UserDetails user = this.getCurrentUser();
			List<WorkflowSearchFilter> workflowFilters = ((IContentWorkFlowActionHelper) this.getContentActionHelper()).getWorkflowSearchFilters(user);
			if (workflowFilters.size() > 0) {
				List<String> allowedGroups = this.getContentGroupCodes();
				EntitySearchFilter[] filters = this.createFilters();
				String[] categories = null;
				Category category = this.getCategoryManager().getCategory(this.getCategoryCode());
				if (null != category && !category.isRoot()) {
					categories = new String[]{this.getCategoryCode().trim()};
				}
				result = this.getContentSearcherManager().loadContentsId(workflowFilters, categories, filters, allowedGroups);
			} else {
				result = new ArrayList<String>();
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContents");
			throw new RuntimeException("Errore in ricerca contenuti", t);
		}
		return result;
	}
	
	@Override
	public String insertOnLine() {
		try {
			if (null == this.getContentIds()) return SUCCESS;
			Iterator<String> iter = this.getContentIds().iterator();
			List<Content> publishedContents = new ArrayList<Content>();
			IContentManager contentManager = (IContentManager) this.getContentManager();
			while (iter.hasNext()) {
				String contentId = (String) iter.next();
				Content contentToPublish = contentManager.loadContent(contentId, false);
				String[] msgArg = new String[1];
				if (null == contentToPublish) {
					msgArg[0] = contentId;
					this.addActionError(this.getText("error.content.contentToPublishNull", msgArg));
					continue;
				}
				msgArg[0] = contentToPublish.getDescr();
				if (!Content.STATUS_READY.equals(contentToPublish.getStatus())) {
					String[] args = {contentToPublish.getId(), contentToPublish.getDescr()};
					this.addActionError(this.getText("error.content.publish.notReadyStatus", args));
					continue;
				}
				if (!this.isUserAllowed(contentToPublish)) {
					this.addActionError(this.getText("error.content.userNotAllowedToPublishContent", msgArg));
					continue;
				}
				this.getContentActionHelper().scanEntity(contentToPublish, this);
				if (this.getFieldErrors().size()>0) {
					this.addActionError(this.getText("error.content.publishingContentWithErrors", msgArg));
					continue;
				}
				contentManager.insertOnLineContent(contentToPublish);
				ApsSystemUtils.getLogger().info("Pubblicato contenuto " + contentToPublish.getId() 
						+ " da Utente " + this.getCurrentUser().getUsername());
				publishedContents.add(contentToPublish);
			}
			//RIVISITARE LOGICA DI COSTRUZIONE LABEL
			this.addConfirmMessage("message.content.publishedContents", publishedContents);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "insertOnLine");
			throw new RuntimeException("Errore in inserimento contenuti online", t);
		}
		return SUCCESS;
	}
	
	// Portare a protected in Action padre di jacms
	protected void addConfirmMessage(String key, List<Content> deletedContents) {
		if (deletedContents.size()>0) {
			String confirm = this.getText(key);
			for (int i=0; i<deletedContents.size(); i++) {
				Content content = deletedContents.get(i);
				if (i>0) confirm += " - ";
				confirm += " '" + content.getDescr() + "'";
			}
			this.addActionMessage(confirm);
		}
	}
	
	@Override
	public List<SmallContentType> getContentTypes() {
		return ((IContentWorkFlowActionHelper) this.getContentActionHelper()).getAllowedContentTypes(this.getCurrentUser());
	}
	
	@Override
	public List<SelectItem> getAvalaibleStatus() {
		if (null != this.getContentType() && this.getContentType().trim().length() > 0) {
			return ((IContentWorkFlowActionHelper) this.getContentActionHelper()).getAvalaibleStatus(this.getCurrentUser(), this.getContentType());
		}
		List<SelectItem> items = new ArrayList<SelectItem>();
		items.add(new SelectItem(Content.STATUS_DRAFT, "name.contentStatus." + Content.STATUS_DRAFT));
		if (super.hasCurrentUserPermission(Permission.SUPERVISOR)) {
			items.add(new SelectItem(Content.STATUS_READY, "name.contentStatus." + Content.STATUS_READY));
		}
		return items;
	}

	protected IContentSearcherManager getContentSearcherManager() {
		return _contentSearcherManager;
	}
	public void setContentSearcherManager(IContentSearcherManager contentSearcherManager) {
		this._contentSearcherManager = contentSearcherManager;
	}
	
	private IContentSearcherManager _contentSearcherManager;
	
}