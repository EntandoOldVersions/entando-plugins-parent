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
package com.agiletec.plugins.jpwebdynamicform.aps.internalservlet.message;

import java.util.Date;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Showlet;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.apsadmin.system.entity.AbstractApsEntityAction;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.JpwebdynamicformSystemConstants;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.IMessageManager;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.Message;
import com.agiletec.plugins.jpwebdynamicform.aps.system.services.message.model.SmallMessageType;
import com.agiletec.plugins.jpwebdynamicform.apsadmin.message.common.INewMessageAction;

/**
 * Implementation for action managing Message entity editing operations.
 * @author E.Mezzano
 */
public class UserNewMessageAction extends AbstractApsEntityAction implements INewMessageAction {

	@Override
	public void validate() {
		if (this.getMessage() != null) {
			super.validate();
		}
	}

	@Override
	public IApsEntity getApsEntity() {
		return this.getMessage();
	}
	
	@Override
	public String view() {
		//Operation not allowed
		return null;
	}
	
	@Override
	public String createNew() {
		try {
			String typeCode = this.getTypeCode();
			Message message = null;
			if (typeCode != null && typeCode.length()>0) {
				message = this.getMessageManager().createMessageType(typeCode);
			}
			if (message == null) {
				return "voidTypeCode";
			}
			this.checkTypeLables(message);
			this.setMessageOnSession(message);
		} catch(Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "createNew");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected void checkTypeLables(Message messageType) {
		if (null == messageType) {
			return;
		}
		try {
			String typeLabelKey = "jpwebdynamicform_TITLE_" + messageType.getTypeCode();
			if (null == this.getI18nManager().getLabelGroup(typeLabelKey)) {
				this.addLabelGroups(typeLabelKey, messageType.getTypeDescr());
			}
			List<AttributeInterface> attributes = messageType.getAttributeList();
			for (int i = 0; i < attributes.size(); i++) {
				AttributeInterface attribute = attributes.get(i);
				//jpwebdynamicform_${typeCodeKey}_${attributeNameI18nKey}
				String attributeLabelKey = "jpwebdynamicform_" + messageType.getTypeCode() + "_" + attribute.getName();
				if (null == this.getI18nManager().getLabelGroup(attributeLabelKey)) {
					this.addLabelGroups(attributeLabelKey, attribute.getName());
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "checkTypeLables");
			throw new RuntimeException("Error checking label types", t);
		}
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
	
	@Override
	public String edit() {
		// Operation Not Allowed
		return null;
	}

	@Override
	public String entryMessage() {
		try {
			Message message = this.getMessage();
			if (message == null) {
				return "expiredMessage";
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "entryMessage");
			return FAILURE;
		}
		return SUCCESS;
	}

	@Override
	public String save() {
		try {
			Message message = this.getMessage();
			if (message == null) {
				return "expiredMessage";
			}
			String username = this.getCurrentUser().getUsername();
			message.setUsername(username);
			message.setCreationDate(new Date());
			message.setLangCode(this.getCurrentLang().getCode());
			try {
				this.getMessageManager().sendMessage(message);
			} catch (Exception e) {
				this.addActionError(this.getText("Errors.webdynamicform.sendingError"));
				return INPUT;
			}
			this.setMessageOnSession(null);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}

	/**
	 * Returns the current session Message.
	 * @return The current session Message.
	 */
	public Message getMessage() {
		if (this._message==null) {
			try {
				String sessionParamName = this.getSessionParamName();
				this._message = (Message) this.getRequest().getSession().getAttribute(sessionParamName);
			} catch(Throwable t) {
				ApsSystemUtils.logThrowable(t, this, "getMessage");
				throw new RuntimeException("Error finding message", t);
			}
		}
		return _message;
	}
	/**
	 * Sets the Message into the session.
	 * @param message The Message to set into the session.
	 */
	protected void setMessageOnSession(Message message) {
		String sessionParamName = this.getSessionParamName();
		if (message==null) {
			this.getRequest().getSession().removeAttribute(sessionParamName);
		} else {
			this.getRequest().getSession().setAttribute(sessionParamName, message);
		}
		this._message = message;
	}

	/**
	 * Returns the name of the session parameter containing the current Message.
	 * @return The name of the session parameter containing the current Message.
	 */
	protected String getSessionParamName() {
		String typeCode = this.getTypeCode();
		String sessionParamName = SESSION_PARAM_NAME_CURRENT_MESSAGE + typeCode;
		return sessionParamName;
	}

	/**
	 * Returns the current language from front-end context.
	 * @return The current language.
	 */
	@Override
	public Lang getCurrentLang() {
		super.getCurrentLang();
		if (null == this._currentLang) {
			RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
			if (null != reqCtx) {
				this._currentLang = (Lang) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG);
			}
			if (null == this._currentLang){
				this._currentLang = this.getLangManager().getDefaultLang();
			}
		}
		return this._currentLang;
	}

	/**
	 * Extract the typeCode from the current showlet.
	 * @return The type code extracted from the showlet.
	 */
	protected String extractTypeCode() {
		String typeCode = null;
		RequestContext reqCtx = (RequestContext) this.getRequest().getAttribute(RequestContext.REQCTX);
		if (reqCtx != null) {
			Showlet showlet = (Showlet) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_SHOWLET);
			if (showlet != null) {
				ApsProperties config = showlet.getConfig();
				if (null != config) {
					String showletTypeCode = config.getProperty(JpwebdynamicformSystemConstants.TYPECODE_SHOWLET_PARAM);
					if (showletTypeCode!=null && showletTypeCode.trim().length()>0) {
						typeCode = showletTypeCode.trim();
					}
				}
			}
		}
		return typeCode;
	}

	/**
	 * Returns the message type search filter.
	 * @return The message type search filter.
	 */
	@Override
	public String getTypeCode() {
		if (null==this._typeCode) {
			this._typeCode = this.extractTypeCode();
		}
		return _typeCode;
	}

	/**
	 * Sets the message type search filter.
	 * @param typeCode The message type search filter.
	 */
	public void setTypeCode(String typeCode) {
		String showletTypeCode = this.extractTypeCode();
		this._typeCode = (null==showletTypeCode) ? typeCode : showletTypeCode;
	}

	/**
	 * Returns the list of system languages.
	 * @return The list of system languages.
	 */
	public List<Lang> getLangs() {
		return this.getLangManager().getLangs();
	}
	
	protected II18nManager getI18nManager() {
		return _i18nManager;
	}
	public void setI18nManager(II18nManager i18nManager) {
		this._i18nManager = i18nManager;
	}
	
	protected IMessageManager getMessageManager() {
		return _messageManager;
	}
	public void setMessageManager(IMessageManager messageManager) {
		this._messageManager = messageManager;
	}

	private String _typeCode;
	private Lang _currentLang;
	private Message _message;
	
	private II18nManager _i18nManager;
	private IMessageManager _messageManager;

}