/*
*
* Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
* This file is part of jAPS software.
* jAPS is a free software;
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
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment;

import java.util.HashMap;
import java.util.List;

import org.apache.struts2.util.TokenHelper;

import com.agiletec.plugins.jpcontentfeedback.apsadmin.JpContentFeedbackApsAdminBaseTestCase;

import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.cache.ICacheManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.Showlet;
import com.agiletec.aps.system.services.showlettype.IShowletTypeManager;
import com.agiletec.aps.system.services.showlettype.ShowletType;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.ContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.dispenser.ContentAuthorizationInfo;
import com.agiletec.plugins.jpcontentfeedback.aps.internalservlet.feedback.ContentFeedbackAction;
import com.agiletec.plugins.jpcontentfeedback.aps.system.JpcontentfeedbackSystemConstants;
import com.agiletec.plugins.jpcontentfeedback.aps.system.TokenInterceptor;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.ICommentManager;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.CommentSearchBean;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.IRatingManager;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.RatingDAO;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.RatingManager;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model.IRating;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;

public class TestCommentFrontEndAction extends JpContentFeedbackApsAdminBaseTestCase{

	protected void setUp() throws Exception {
        super.setUp();
        this.init();
    }

	public void testAddDeleteCommentByShowletConfig()throws Throwable{
		try{
			Content content = this._contentManager.loadContent("ART1", true);
			ICacheManager cacheManager = (ICacheManager) ApsWebApplicationUtils.getBean(SystemConstants.CACHE_MANAGER, this.getRequest());
			ContentAuthorizationInfo authInfo = new ContentAuthorizationInfo(content);
			cacheManager.putInCache(ContentManager.getContentAuthInfoCacheKey("ART1"), authInfo);
			this.setUserOnSession("admin");
			Showlet showlet = new Showlet();
            IShowletTypeManager showletTypeMan =
            	(IShowletTypeManager) this.getService(SystemConstants.SHOWLET_TYPE_MANAGER);
            ShowletType showletType = showletTypeMan.getShowletType("content_feedback_viewer");
            showlet.setType(showletType);
            ApsProperties prop = new ApsProperties();
            prop.put("contentId", "ART1");
            showlet.setConfig(prop);
            showlet.setPublishedContent("ART1");

            List<String> listaIds = this._commentManager.searchCommentIds(null);
			assertEquals(0, listaIds.size());

            RequestContext e = new RequestContext();
            e.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_SHOWLET, showlet);
            Lang lang = new Lang();
    		lang.setCode("en");
            e.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG, lang);
            this.getRequest().setAttribute(RequestContext.REQCTX, e);

			this.initAction("/do/jpcontentfeedback/FrontEnd/contentfeedback", "insert");
			this.addParameter("contentId", "ART1");
			this.addParameter("commentText", "testComment");

			this.setToken();

			String result1 = this.executeAction();
			assertEquals(Action.SUCCESS, result1);

			this.initAction("/do/jpcontentfeedback/FrontEnd/contentfeedback", "insert");
			assertEquals(TokenInterceptor.INVALID_TOKEN_CODE, this.executeAction());

			CommentSearchBean searchBean = new CommentSearchBean();
			searchBean.setComment("Testo ");
			listaIds = this._commentManager.searchCommentIds(null);
			assertEquals(1, listaIds.size());

			this.initAction("/do/jpcontentfeedback/FrontEnd/contentfeedback", "delete");
			this.addParameter("contentId", listaIds.get(0));

		} catch (Throwable t) {
			t.printStackTrace();
			throw t;
		} finally{
			List<String> listaIds = this._commentManager.searchCommentIds(null);
			for (int i=0; i< listaIds.size(); i++){
				this._commentManager.deleteComment(Integer.parseInt(listaIds.get(i)));
			}
		}
	}

	private void setToken() {
		String token = TokenHelper.generateGUID();
		ActionContext.getContext().setSession(new HashMap<String, Object>());
		ActionContext.getContext().getSession().put(TokenHelper.DEFAULT_TOKEN_NAME, token);
		ActionContext.getContext().getSession().put(TokenHelper.TOKEN_NAME_FIELD,
				TokenHelper.DEFAULT_TOKEN_NAME);
		this.addParameter(TokenHelper.DEFAULT_TOKEN_NAME, new String[]{token});
		this.addParameter(TokenHelper.TOKEN_NAME_FIELD, new String[]{
		        TokenHelper.DEFAULT_TOKEN_NAME});
	}


	public void testAddContentRatingByShowletConfig()throws Throwable{
		try{
			Content content = this._contentManager.loadContent("ART1", true);
			ICacheManager cacheManager = (ICacheManager) ApsWebApplicationUtils.getBean(SystemConstants.CACHE_MANAGER, this.getRequest());
			ContentAuthorizationInfo authInfo = new ContentAuthorizationInfo(content);
			cacheManager.putInCache(ContentManager.getContentAuthInfoCacheKey("ART1"), authInfo);
			this.setUserOnSession("admin");
			Showlet showlet = new Showlet();
            IShowletTypeManager showletTypeMan =
            	(IShowletTypeManager) this.getService(SystemConstants.SHOWLET_TYPE_MANAGER);
            ShowletType showletType = showletTypeMan.getShowletType("content_feedback_viewer");
            showlet.setType(showletType);
            ApsProperties prop = new ApsProperties();
            prop.put("contentId", "ART1");
            showlet.setConfig(prop);
            showlet.setPublishedContent("ART1");

            RequestContext e = new RequestContext();
            e.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_SHOWLET, showlet);
            Lang lang = new Lang();
    		lang.setCode("en");
            e.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG, lang);
            this.getRequest().setAttribute(RequestContext.REQCTX, e);

            List<String> listaIds = this._commentManager.searchCommentIds(null);
			assertEquals(0, listaIds.size());

			this.initAction("/do/jpcontentfeedback/FrontEnd/contentfeedback", "insert");
			this.setToken();
			this.addParameter("contentId", "ART1");
			this.addParameter("title", "testTitle");
			this.addParameter("commentText", "testComment");
			String result1 = this.executeAction();
			assertEquals(Action.SUCCESS, result1);

			this.initAction("/do/jpcontentfeedback/FrontEnd/contentfeedback", "insert");
			this.setToken();
			this.addParameter("contentId", "ART1");
			this.addParameter("title", "testTitle");
			this.addParameter("commentText", "testComment");
			String result2 = this.executeAction();
			assertEquals(Action.SUCCESS, result2);

			CommentSearchBean searchBean = new CommentSearchBean();
			searchBean.setComment("Testo ");
			listaIds = this._commentManager.searchCommentIds(null);
			assertEquals(2, listaIds.size());

		// Inserimento votazione su commento 1
			this.initAction("/do/jpcontentfeedback/FrontEnd/contentfeedback", "insertVote");

			this.addParameter("contentId", "ART1");
			this.addParameter("selectedComment", listaIds.get(0));
			this.addParameter("vote", 2);
			result2 = this.executeAction();
			assertEquals(Action.SUCCESS, result2);

			ContentFeedbackAction action = (ContentFeedbackAction)this.getAction();
			IRating ratingConten_0 = action.getCommentRating(Integer.parseInt(listaIds.get(0)));
			assertNotNull(ratingConten_0);
			assertEquals(1, ratingConten_0.getVoters());
			assertEquals(2, ratingConten_0.getSumvote());

			IRating ratingConten_1 = action.getCommentRating(Integer.parseInt(listaIds.get(1)));
			assertNull(ratingConten_1);

			IRating ratingConten = action.getContentRating();
			assertNull(ratingConten);

		// Inserimento votazione su contenuto
			this.initAction("/do/jpcontentfeedback/FrontEnd/contentfeedback", "insertVote");
			this.addParameter("contentId", "ART1");
			this.addParameter("vote", 4);
			result2 = this.executeAction();
			assertEquals(Action.SUCCESS, result2);

			action = (ContentFeedbackAction)this.getAction();
			ratingConten_0 = action.getCommentRating(Integer.parseInt(listaIds.get(0)));
			assertNotNull(ratingConten_0);
			assertEquals(1, ratingConten_0.getVoters());
			assertEquals(2, ratingConten_0.getSumvote());

			ratingConten_1 = action.getCommentRating(Integer.parseInt(listaIds.get(1)));
			assertNull(ratingConten_1);

			ratingConten = action.getContentRating();
			assertEquals(1, ratingConten.getVoters());
			assertEquals(4, ratingConten.getSumvote());

		} catch (Throwable t) {
			throw t;
		} finally{
			List<String> listaIds = this._commentManager.searchCommentIds(null);
			RatingDAO ratingDao = (RatingDAO) ((RatingManager)this._ratingManager).getRatingDAO();
			for (int i=0; i< listaIds.size(); i++){
				IRating rating = this._ratingManager.getCommentRating(Integer.parseInt(listaIds.get(i)));
				if (rating!=null){
					ratingDao.removeRating(rating.getCommentId());
				}
				this._commentManager.deleteComment(Integer.parseInt(listaIds.get(i)));
			}
			IRating rating = this._ratingManager.getContentRating("ART1");
			if (rating!=null){
				((RatingDAO)ratingDao).removeContentRating(rating.getContentId());
			}
		}
	}


	public void testViewContentAndAddCommentByRequest()throws Throwable{
		try{
			Content content = this._contentManager.loadContent("ART1", true);
			ICacheManager cacheManager = (ICacheManager) ApsWebApplicationUtils.getBean(SystemConstants.CACHE_MANAGER, this.getRequest());
			ContentAuthorizationInfo authInfo = new ContentAuthorizationInfo(content);
			cacheManager.putInCache(ContentManager.getContentAuthInfoCacheKey("ART1"), authInfo);
			this.setUserOnSession("admin");
			this._contentManager.loadContent("ART1", true);
			Showlet showlet = new Showlet();
            IShowletTypeManager showletTypeMan =
            	(IShowletTypeManager) this.getService(SystemConstants.SHOWLET_TYPE_MANAGER);
            ShowletType showletType = showletTypeMan.getShowletType("content_feedback_viewer");
            showlet.setType(showletType);

            RequestContext e = new RequestContext();
            e.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_SHOWLET, showlet);
            Lang lang = new Lang();
    		lang.setCode("en");
            e.addExtraParam(SystemConstants.EXTRAPAR_CURRENT_LANG, lang);
            this.getRequest().setAttribute(RequestContext.REQCTX, e);

			this.setUserOnSession("admin");
			this.initAction("/do/jpcontentfeedback/FrontEnd/contentfeedback", "intro");
			this.addParameter("contentId", "ART1");
			String result = this.executeAction();
			assertEquals(Action.SUCCESS, result);

			ContentFeedbackAction action = (ContentFeedbackAction)this.getAction();
			List<String> commentIds = action.getContentCommentIds();
			assertEquals(0, commentIds.size());

			this.initAction("/do/jpcontentfeedback/FrontEnd/contentfeedback", "insert");
			this.addParameter("contentId", "ART1");
			this.addParameter("title", "testTitle");
			this.addParameter("commentText", "testComment");
			this.setToken();
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);

			this.setUserOnSession("admin");
			this.initAction("/do/jpcontentfeedback/FrontEnd/contentfeedback", "intro");
			this.addParameter("contentId", "ART1");
			result = this.executeAction();
			assertEquals(Action.SUCCESS, result);

			action = (ContentFeedbackAction)this.getAction();
			commentIds = action.getContentCommentIds();
			assertEquals(1, commentIds.size());

		} catch (Throwable t) {
			t.printStackTrace();
			throw t;
		} finally{
			List<String> listaIds = this._commentManager.searchCommentIds(null);
			for (int i=0; i< listaIds.size(); i++){
				this._commentManager.deleteComment(Integer.parseInt(listaIds.get(i)));
			}
		}
	}

	private void init() throws Exception {
    	try {
    		this._commentManager = (ICommentManager) this.getService(JpcontentfeedbackSystemConstants.COMMENTS_MANAGER);
    		this._ratingManager = (IRatingManager) this.getService(JpcontentfeedbackSystemConstants.RATING_MANAGER);
    		this._contentManager = (IContentManager) this.getService(JacmsSystemConstants.CONTENT_MANAGER);
    		this._authorizationManager = (IAuthorizationManager) this.getService(SystemConstants.AUTHORIZATION_SERVICE);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
    }

	private ICommentManager _commentManager = null;
	private IRatingManager _ratingManager = null;
	private IContentManager _contentManager;
	private IAuthorizationManager _authorizationManager;
}
