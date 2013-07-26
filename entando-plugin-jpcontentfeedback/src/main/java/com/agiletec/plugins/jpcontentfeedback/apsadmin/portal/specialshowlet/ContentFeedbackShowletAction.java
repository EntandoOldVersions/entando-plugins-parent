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
package com.agiletec.plugins.jpcontentfeedback.apsadmin.portal.specialshowlet;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.apsadmin.portal.specialshowlet.viewer.ContentViewerShowletAction;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.IContentFeedbackConfig;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.IContentFeedbackManager;

import org.entando.entando.aps.system.services.page.IPage;
import org.entando.entando.aps.system.services.page.Widget;


/**
 * Action per la gestione della configurazione della showlet erogatore contenuto singolo,
 * e del blocco relativo agli elementi del contentFeedback: commenti e rating
 * @author D.Cherchi
 *
 */
public class ContentFeedbackShowletAction  extends ContentViewerShowletAction implements IContentFeedbackShowletAction {

	@Override
	public String init() {
		this.getRequest().getSession().removeAttribute(SESSION_PARAM_STORE_CONFIG);
		return super.init();
	}

	protected String extractInitConfig() {
		if (null != this.getShowlet()) return SUCCESS;
		org.entando.entando.aps.system.services.page.Widget showlet = this.getCurrentPage().getShowlets()[this.getFrame()];
		Logger log = ApsSystemUtils.getLogger();
		if (null == showlet) {
			try {
				showlet = this.createNewShowlet();
				//for ContentFeedbackShowletAction
				IContentFeedbackConfig systemConfig = this.getContentFeedbackManager().getConfig();
				String value = systemConfig.getComment();
				if (null != value && value.equalsIgnoreCase("true")) showlet.getConfig().setProperty(SHOWLET_PARAM_COMMENT_ACTIVE, value);

				value = systemConfig.getAnonymousComment();
				if (null != value && value.equalsIgnoreCase("true")) showlet.getConfig().setProperty(SHOWLET_PARAM_COMMENT_ANONYMOUS, value);

				value = systemConfig.getModeratedComment();
				if (null != value && value.equalsIgnoreCase("true")) showlet.getConfig().setProperty(SHOWLET_PARAM_COMMENT_MODERATED, value);

				value = systemConfig.getRateContent();
				if (null != value && value.equalsIgnoreCase("true")) showlet.getConfig().setProperty(SHOWLET_PARAM_RATE_CONTENT, value);

				value = systemConfig.getRateComment();
				if (null != value && value.equalsIgnoreCase("true")) showlet.getConfig().setProperty(SHOWLET_PARAM_RATE_COMMENT, value);
				//---

			} catch (Exception e) {
				log.severe(e.getMessage());
				//TODO METTI MESSAGGIO DI ERRORE NON PREVISO... Vai in pageTree con messaggio di errore Azione non prevista o cosa del genere
				this.addActionError(this.getText("Message.userNotAllowed"));
				return "pageTree";
			}
			log.info("Configurating new org.entando.entando.aps.system.services.page.Widget " + this.getShowletTypeCode() + " - Page " + this.getPageCode() + " - Frame " + this.getFrame());
		} else {
			log.info("Edit org.entando.entando.aps.system.services.page.Widget config " + showlet.getType().getCode() + " - Page " + this.getPageCode() + " - Frame " + this.getFrame());
			showlet = this.createCloneFrom(showlet);
		}
		this.setShowlet(showlet);
		return SUCCESS;
	}

	public String storeSessionParams() {
		Map<String, String> sessionParams = new HashMap<String, String>();
		sessionParams.put(SHOWLET_PARAM_COMMENT_ACTIVE, this.getUsedComment());
		sessionParams.put(SHOWLET_PARAM_COMMENT_MODERATED, this.getCommentValidation());
		sessionParams.put(SHOWLET_PARAM_RATE_CONTENT, this.getUsedContentRating());
		sessionParams.put(SHOWLET_PARAM_RATE_COMMENT, this.getUsedCommentWithRating());
		sessionParams.put(SHOWLET_PARAM_COMMENT_ANONYMOUS, this.getAnonymousComment());

		this.getRequest().getSession().setAttribute(SESSION_PARAM_STORE_CONFIG, sessionParams);
		return SUCCESS;
	}

	public void restoreSessionParams() {
		Map<String, String> sessionParams = (Map<String, String>) this.getRequest().getSession().getAttribute(SESSION_PARAM_STORE_CONFIG);
		if (null != sessionParams) {
			Iterator<String> it = sessionParams.keySet().iterator();
			while (it.hasNext()) {
				String key = it.next();
				if (null != sessionParams.get(key) && sessionParams.get(key).equalsIgnoreCase("true")) {
					this.getShowlet().getConfig().setProperty(key, "true");
				}
			}
		}
		this.getRequest().getSession().removeAttribute(SESSION_PARAM_STORE_CONFIG);
	}


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
			this.restoreSessionParams();
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

	public String getUsedComment() {
		return _usedComment;
	}
	public void setUsedComment(String usedComment) {
		this._usedComment = usedComment;
	}

	public String getAnonymousComment() {
		return _anonymousComment;
	}
	public void setAnonymousComment(String anonymousComment) {
		this._anonymousComment = anonymousComment;
	}

	public String getCommentValidation() {
		return _commentValidation;
	}
	public void setCommentValidation(String commentValidation) {
		this._commentValidation = commentValidation;
	}

	public String getUsedContentRating() {
		return _usedContentRating;
	}
	public void setUsedContentRating(String usedContentRating) {
		this._usedContentRating = usedContentRating;
	}

	public String getUsedCommentWithRating() {
		return _usedCommentWithRating;
	}
	public void setUsedCommentWithRating(String usedCommentWithRating) {
		this._usedCommentWithRating = usedCommentWithRating;
	}

	protected IContentFeedbackManager getContentFeedbackManager() {
		return _contentFeedbackManager;
	}
	public void setContentFeedbackManager(IContentFeedbackManager contentFeedbackManager) {
		this._contentFeedbackManager = contentFeedbackManager;
	}

	private IContentFeedbackManager _contentFeedbackManager;

	private String _usedComment;
	private String _anonymousComment;
	private String _commentValidation;
	private String _usedContentRating;
	private String _usedCommentWithRating;

	public static final String SHOWLET_PARAM_COMMENT_ACTIVE = "usedComment";
	public static final String SHOWLET_PARAM_COMMENT_MODERATED = "commentValidation";
	public static final String SHOWLET_PARAM_RATE_CONTENT = "usedContentRating";
	public static final String SHOWLET_PARAM_RATE_COMMENT = "usedCommentWithRating";

	public static final String SHOWLET_PARAM_COMMENT_ANONYMOUS = "anonymousComment";

	public static final String SESSION_PARAM_STORE_CONFIG = "ContentFeedbackShowletAction_params_store";


}
