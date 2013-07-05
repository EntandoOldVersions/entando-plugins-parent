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
package com.agiletec.plugins.jprss.aps.system.services.rss;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.event.EntityTypesChangingEvent;
import com.agiletec.aps.system.common.entity.event.EntityTypesChangingObserver;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.plugins.jacms.aps.system.JacmsSystemConstants;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.showlet.util.EntitySearchFilterDOM;
import com.agiletec.plugins.jacms.aps.system.services.linkresolver.ILinkResolverManager;
import com.agiletec.plugins.jprss.aps.system.services.JpRssSystemConstants;
import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;

/**
 * Manager that handles the Channels
 */
public class RssManager extends AbstractService implements IRssManager, EntityTypesChangingObserver {
	
	@Override
	public void init() throws Exception {
		this.loadMappingConfig();
		ApsSystemUtils.getLogger().config(this.getClass().getName() + ": initialized");
	}
	
	@Override
	public void updateFromEntityTypesChanging(EntityTypesChangingEvent event) {
		if (!event.getEntityManagerName().equals(JacmsSystemConstants.CONTENT_MANAGER)) {
			return;
		}
		try {
			this.loadMappingConfig();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addChannel", "error loading Rss Content Config");
		}
	}
	
	private void loadMappingConfig() throws ApsSystemException {
		Map<String, RssContentMapping> mappings = new HashMap<String, RssContentMapping>();
		try {
			Map<String, IApsEntity> contentTypes = this.getContentManager().getEntityPrototypes();
			Iterator<IApsEntity> contentTypeIter = contentTypes.values().iterator();
			while (contentTypeIter.hasNext()) {
				IApsEntity contentType = contentTypeIter.next();
				AttributeInterface attributeTitle = contentType.getAttributeByRole(JpRssSystemConstants.ATTRIBUTE_ROLE_RSS_CONTENT_TITLE);
				if (null != attributeTitle) {
					RssContentMapping mapping = new RssContentMapping();
					mapping.setContentType(contentType.getTypeCode());
					mapping.setTitleAttributeName(attributeTitle.getName());
					AttributeInterface attributeDescr = contentType.getAttributeByRole(JpRssSystemConstants.ATTRIBUTE_ROLE_RSS_CONTENT_DESCRIPTION);
					if (null != attributeDescr) {
						mapping.setDescriptionAttributeName(attributeDescr.getName());
					}
					mappings.put(contentType.getTypeCode(), mapping);
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "loadMappingConfig");
			throw new ApsSystemException("Error loading rss content mapping", t);
		}
		this.setContentMapping(mappings);
	}

	@Override
	public void addChannel(Channel channel) throws ApsSystemException {
		try {
			int key = getKeyGeneratorManager().getUniqueKeyCurrentValue();
			channel.setId(key);
			this.getRssDAO().addChannel(channel);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addChannel");
			throw new ApsSystemException("Error adding a new channel", t);
		}
	}

	@Override
	public void deleteChannel(int id) throws ApsSystemException {
		try {
			this.getRssDAO().deleteChannel(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteChannel");
			throw new ApsSystemException("Error deleting the channel with code: " + id, t);
		}
	}
	
	@Override
	public void updateChannel(Channel channel) throws ApsSystemException {
		try {
			this.getRssDAO().updateChannel(channel);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "updateChannel");
			throw new ApsSystemException("Error updating a channel", t);
		}
	}
	
	@Override
	public List<Channel> getChannels(int status) throws ApsSystemException {
		List<Channel> channels = null;
		try {
			channels = this.getRssDAO().getChannels(status);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getChannels");
			throw new ApsSystemException("Error getting the list of the channels", t);
		}
		return channels;
	}
	
	@Override
	public Channel getChannel(int id) throws ApsSystemException {
		Channel channel = null;
		try {
			channel = this.getRssDAO().getChannel(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getChannel");
			throw new ApsSystemException("Error loading channel with id" + id,t);
		}
		return channel;
	}
	
	private EntitySearchFilter[] getEntitySearchFilter(Channel channel, String langCode) {
		String contentType = channel.getContentType();
		String showletParam = channel.getFilters();
		EntitySearchFilter[] entitySearchFilters = null;
		if (null!=showletParam && showletParam.trim().length()>0) {
			EntitySearchFilterDOM dom = new EntitySearchFilterDOM();
			entitySearchFilters = dom.getFilters(contentType, showletParam,this.getContentManager(), langCode); 
		} else {
			entitySearchFilters = new EntitySearchFilter[0];
		}
		return entitySearchFilters;
	}
	
	@Override
	public SyndFeed getSyndFeed(Channel channel, String lang, String feedLink, HttpServletRequest req,  HttpServletResponse resp) throws ApsSystemException {
		SyndFeed feed = null;
		if (null == feed) {
			feed = new SyndFeedImpl();
			feed.setFeedType(channel.getFeedType());
			feed.setTitle(channel.getTitle());
			feed.setLink(feedLink);
			feed.setDescription(channel.getDescription());
			List<String> contentsId = this.getContentsId(channel, lang);
			feed.setEntries(this.getEntries(contentsId, lang, feedLink, req, resp));	
		}
		return feed;
	}

	private List<SyndEntry> getEntries(List<String> contentsId, String lang, String feedLink, HttpServletRequest req,  HttpServletResponse resp) throws ApsSystemException {
		List<SyndEntry> entries = new ArrayList<SyndEntry>();
		Iterator<String> idIterator = contentsId.iterator();
		while (idIterator.hasNext()) {
			String id = (String) idIterator.next();
			ContentRecordVO currentContent = this.getContentManager().loadContentVO(id);
			RssContentMapping mapping = (RssContentMapping) this.getContentMapping().get(currentContent.getTypeCode());
			if (null == mapping) {
				ApsSystemUtils.getLogger().severe("Null content mapping by existed channel for content type " + currentContent.getTypeCode());
				continue;
			}
			entries.add(this.createEntry(currentContent, lang, feedLink, req, resp));
		}
		return entries;
	}
	
	private SyndEntry createEntry(ContentRecordVO contentVO, String langCode, String feedLink, HttpServletRequest req, HttpServletResponse resp) throws ApsSystemException {
		SyndEntry entry = new SyndEntryImpl();
		RssContentMapping mapping = this.getContentMapping().get(contentVO.getTypeCode());
		try {
			Content content = (Content) this.getContentManager().loadContent(contentVO.getId(), true);
			ITextAttribute titleAttr = (ITextAttribute) content.getAttribute(mapping.getTitleAttributeName());
			String title = (titleAttr.getTextForLang(langCode));
			if (null == title || title.trim().length() == 0) {
				title = titleAttr.getText();
			}
			entry.setTitle(title);
			String link = this.createLink(content, feedLink);
			entry.setLink(link);
			entry.setPublishedDate(contentVO.getModify());
			ITextAttribute descrAttr = (ITextAttribute) content.getAttribute(mapping.getDescriptionAttributeName());
			if (null != descrAttr) {
				SyndContent description = new SyndContentImpl();
				description.setType(JpRssSystemConstants.SYNDCONTENT_TYPE_TEXTHTML);
				String inLang = descrAttr.getTextForLang(langCode);
				//TODO Ottimizzare!
				RequestContext requestContext = new RequestContext();
				requestContext.setRequest(req);
				requestContext.setResponse(resp);
				if (null != inLang && inLang.length() >0) {
					String textValue = this.getLinkResolver().resolveLinks(inLang, requestContext);
					if (null != textValue && textValue.trim().length()>0) {
						description.setValue(textValue);	
					} else {
						description.setValue(descrAttr.getText());	
					}
				} else {
					String textValue =  this.getLinkResolver().resolveLinks(descrAttr.getText(), requestContext);
					description.setValue(textValue);
				}
				entry.setDescription(description);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "createEntry");
			throw new ApsSystemException("Error in createEntry", t);
		}
		return entry;
	}
	
	private String createLink(Content content, String feedLink) {
		SymbolicLink symbolicLink = new SymbolicLink();
		StringBuilder destination = new StringBuilder(feedLink);
		String viewPage = content.getViewPage();
		if (null == viewPage || null == this.getPageManager().getPage(viewPage)) {
			viewPage = this.getPageManager().getRoot().getCode();
		}
		destination.append(viewPage).append(".page").append("?contentId=").append(content.getId());
		symbolicLink.setDestinationToUrl(destination.toString());
		return symbolicLink.getUrlDest();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> getAvailableContentTypes() {
		Map<String, String> availableContentTypes = new HashMap<String, String>();
		Iterator it = this.getContentMapping().entrySet().iterator();
		Map<String, SmallContentType> contentTypes = this.getContentManager().getSmallContentTypesMap();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			String typeCode = (String) pairs.getKey();
			SmallContentType smallContentType = (SmallContentType) contentTypes.get(typeCode);
			if (null != smallContentType) {
				availableContentTypes.put(typeCode, smallContentType.getDescr());
			}
		}
		return availableContentTypes;
	}
	
	@Override
	public RssContentMapping getContentMapping(String typeCode) {
		RssContentMapping mapping = this.getContentMapping().get(typeCode);
		if (null != mapping) {
			return mapping.clone();
		}
		return null;
	}
	
	protected EntitySearchFilter[] addFilter(EntitySearchFilter[] filters, EntitySearchFilter filterToAdd){
		int len = filters.length;
		EntitySearchFilter[] newFilters = new EntitySearchFilter[len + 1];
		for(int i=0; i < len; i++){
			newFilters[i] = filters[i];
		}
		newFilters[len] = filterToAdd;
		return newFilters;
	}

	private List<String> getContentsId(Channel channel, String langCode) throws ApsSystemException {
		RssContentMapping mapping = (RssContentMapping) this.getContentMapping().get(channel.getContentType());
		if (null == mapping) {
			ApsSystemUtils.getLogger().severe("Null content mapping by existed channel for content type " + channel.getContentType());
			return new ArrayList<String>();
		}
		try {
			EntitySearchFilter[] searchFilters = this.getEntitySearchFilter(channel, langCode);//this.getEntitySearchFilterDOM().getFilters(channel.getContentType(), channel.getFilters());
			EntitySearchFilter filterToAdd = new EntitySearchFilter(IContentManager.ENTITY_TYPE_CODE_FILTER_KEY, false, channel.getContentType(), false);
			EntitySearchFilter[] entitySearchFilters = addFilter(searchFilters, filterToAdd);

			String[] categories = null;
			if (null != channel.getCategory() && channel.getCategory().trim().length() > 0) {
				categories = new String[] {channel.getCategory()};
			}
			Collection<String> userGroupCodes = new ArrayList<String>();
			userGroupCodes.add(Group.FREE_GROUP_NAME);
			List<String> contentsId = this.getContentManager().loadPublicContentsId(channel.getContentType(), categories, entitySearchFilters, userGroupCodes);
			
			if (channel.getMaxContentsSize() > 0 && contentsId.size() > channel.getMaxContentsSize()) {
				return contentsId.subList(0, channel.getMaxContentsSize());
			} else {
				return contentsId;
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getContentsId");
			throw new ApsSystemException("Error in rss contents", t);
		}
	}	
	
	@Override
	public Map<String, String> getAvailableFeedTypes() {
		return _availableFeedTypes;
	}
	public void setAvailableFeedTypes(Map<String, String> availableFeedTypes) {
		this._availableFeedTypes = availableFeedTypes;
	}
	
	protected Map<String, RssContentMapping> getContentMapping() {
		return _contentMapping;
	}
	protected void setContentMapping(Map<String, RssContentMapping> contentMapping) {
		this._contentMapping = contentMapping;
	}
	
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	protected ILinkResolverManager getLinkResolver() {
		return _linkResolver;
	}
	public void setLinkResolver(ILinkResolverManager linkResolver) {
		this._linkResolver = linkResolver;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	protected IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}
	
	protected IRssDAO getRssDAO() {
		return _rssDAO;
	}
	public void setRssDAO(IRssDAO rssDAO) {
		this._rssDAO = rssDAO;
	}
	
	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}
	
	private Map<String, RssContentMapping> _contentMapping;
	private IContentManager _contentManager;
	private IPageManager _pageManager;
	private ConfigInterface _configManager;
	private IKeyGeneratorManager _keyGeneratorManager;
	private Map<String, String> _availableFeedTypes;
	private ILinkResolverManager _linkResolver;
	private IRssDAO _rssDAO;

}
