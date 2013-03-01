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
package org.entando.entando.plugins.jpfrontshortcut.aps.internalservlet.content;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.notify.NotifyManager;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;

import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.agiletec.plugins.jacms.apsadmin.content.ContentAction;

import java.util.ArrayList;
import java.util.List;

import org.entando.entando.plugins.jpfrontshortcut.aps.system.JpFrontShortcutSystemConstants;

/**
 * @author E.Santoboni
 */
public class FrontContentAction extends ContentAction {
	
	public String editView() {
		try {
			String modelIdString = this.getRequest().getParameter("modelId");
			ContentModel model = this.getContentModel(modelIdString);
			//System.out.println(modelIdString);
			if (null == model) {
				modelIdString = this.getContentManager().getDefaultModel(this.getContentId());
				model = this.getContentModel(modelIdString);
			}
			//System.out.println(model);
			if (null == model) {
				return this.edit();
			}
			this.setContentModel(model);
			//System.out.println(model);
			if (!this.getContentId().startsWith(model.getContentType())) {
				throw new ApsSystemException("Invalid model id " + model.getId() + 
						" of type " + model.getContentType() + " for content " + this.getContentId());
			}
			//System.out.println(model.getId());
			this.extractAttributesToEdit(model.getContentShape());
			//System.out.println(this.getAttributeName());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "editView");
			return FAILURE;
		}
		return this.edit();
	}
    
	public ContentModel getContentModel(String modelIdString) {
		ContentModel model = null;
		try {
			int modelId = Integer.parseInt(modelIdString);
			model = this.getContentModelManager().getContentModel(modelId);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentModel", "Error extracting modelId");
		}
		return model;
	}
	
	private void extractAttributesToEdit(String text) {
		int postfixLen = ".".length();
		int end = 0;
		int start = text.indexOf("$content.");
		while (start >= 0) {
			end = text.indexOf(".", start + "$content.".length());
			if (end >= 0) {
				end = end + postfixLen;
				String attributeName = text.substring(start + "$content.".length(), end - ".".length());
				if (null == this.getAttributeName()) {
					this.setAttributeName(new ArrayList<String>());
				}
				//System.out.println(attributeName);
				if (!this.getAttributeName().contains(attributeName)) {
					this.getAttributeName().add(attributeName);
				}
				start = text.indexOf("$content.", end);
			} else {
				start = -1; //uscita
			}
		}
	}
    
    @Override
    public String edit() {
        String result = super.edit();
		if (!result.equals(SUCCESS)) {
			return result;
		}
		try {
			ContentModel model = this.getContentModel();
			if (null != this.getAttributeName() && null != model) {
				Content content = this.getContent();
				if (!content.getTypeCode().equals(model.getContentType())) {
					ApsSystemUtils.getLogger().severe("Invalid model id " + model.getId() + 
						" of type " + model.getContentType() + " for content " + this.getContentId());
					return SUCCESS;
				}
				List<AttributeInterface> attributes = content.getAttributeList();
				for (int i = 0; i < attributes.size(); i++) {
					AttributeInterface attribute = attributes.get(i);
					//jpfrontshortcut_${typeCodeKey}_${attributeNameI18nKey}
					String attributeLabelKey = "jpfrontshortcut_" + content.getTypeCode() + "_" + attribute.getName();
					if (null == this.getI18nManager().getLabelGroup(attributeLabelKey)) {
						this.addLabelGroups(attributeLabelKey, attribute.getName());
					}
					attribute.setDisablingCodes(this.createNewCodes(attribute.getDisablingCodes()));
					if (!this.getAttributeName().contains(attribute.getName())) {
						attribute.disable(JpFrontShortcutSystemConstants.WIDGET_DISABLING_CODE);
					}
				}
				Lang currentLang = super.getCurrentLang();
				this.getRequest().getSession()
						.setAttribute(JpFrontShortcutSystemConstants.CONTENT_LANG_SESSION_PARAM, currentLang);
			}
		} catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "edit");
            return FAILURE;
        }
        return result;
    }
    
	protected void addLabelGroups(String key, String defaultValue) throws ApsSystemException {
		try {
			ApsProperties properties = new ApsProperties();
			Lang defaultLang = super.getLangManager().getDefaultLang();
			properties.put(defaultLang.getCode(), defaultValue);
			this.getI18nManager().addLabelGroup(key, properties);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addLabelGroups");
			throw new RuntimeException("Error adding label groups - key '" + key + "'", t);
		}
	}
	
	private String[] createNewCodes(String[] oldDisablingCodes) {
		if (null == oldDisablingCodes) {
			String[] newCodes = new String[1];
			newCodes[0] = JpFrontShortcutSystemConstants.WIDGET_DISABLING_CODE;
			return newCodes;
		}
		int len = oldDisablingCodes.length;
		String[] newCodes = new String[len + 1];
		for (int i=0; i < len; i++){
			newCodes[i] = oldDisablingCodes[i];
		}
		newCodes[len] = JpFrontShortcutSystemConstants.WIDGET_DISABLING_CODE;
		return newCodes;
	}
	
    @Override
	public String saveAndApprove() {
		if (null != super.getContent()) {
			this.setContentId(super.getContent().getId());
		}
		String result = super.saveAndApprove();
		try {
			synchronized (this) {
				this.wait(1000);
			}
			this.waitNotifyingThread();
			this.getRequest().getSession()
					.removeAttribute(JpFrontShortcutSystemConstants.CONTENT_LANG_SESSION_PARAM);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveAndApprove");
			return FAILURE;
		}
		return result;
	}
    
    protected void waitNotifyingThread() throws InterruptedException {
		Thread[] threads = new Thread[20];
        Thread.enumerate(threads);
        for (int i=0; i<threads.length; i++) {
            Thread currentThread = threads[i];
            if (currentThread != null && 
                            currentThread.getName().startsWith(NotifyManager.NOTIFYING_THREAD_NAME)) {
                    currentThread.join();
            }
        }
    }
	
	public String getRedirectUrl() {
		return "";
	}
    
    public String executeSubmit() {
        return SUCCESS;
    }

    public List<String> getAttributeName() {
        return _attributeName;
    }
    public void setAttributeName(List<String> attributeName) {
        this._attributeName = attributeName;
    }
    
    public String getLangCode() {
        return _langCode;
    }
    public void setLangCode(String langCode) {
        this._langCode = langCode;
    }
	
	protected ContentModel getContentModel() {
		return _contentModel;
	}
	protected void setContentModel(ContentModel contentModel) {
		this._contentModel = contentModel;
	}
	
	protected IContentModelManager getContentModelManager() {
		return _contentModelManager;
	}
	public void setContentModelManager(IContentModelManager contentModelManager) {
		this._contentModelManager = contentModelManager;
	}
    
	protected II18nManager getI18nManager() {
		return _i18nManager;
	}
	public void setI18nManager(II18nManager i18nManager) {
		this._i18nManager = i18nManager;
	}
	
    private List<String> _attributeName;
    private String _langCode;
	
	private ContentModel _contentModel;
	private IContentModelManager _contentModelManager;
	private II18nManager _i18nManager;
	
}