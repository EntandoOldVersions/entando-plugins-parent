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
package com.agiletec.plugins.jprss.aps.system.services.rss;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.sun.syndication.feed.synd.SyndFeed;

/**
 * This class manages all the CRUD operations on the {@link Channel} object and
 * generates the xml for the rss.
 */
public interface IRssManager {
	
	/**
	 * Add a new Channel object
	 * @param channel the channel to insert.
	 * @throws ApsSystemException if an error occurs
	 */
	public void addChannel(Channel channel) throws ApsSystemException;

	/**
	 * Delete a channel.
	 * @param id the code of the channel do delete.
	 */
	public void deleteChannel(int id) throws ApsSystemException;

	/**
	 * Update a channel object
	 * @param channel the object to update
	 * @throws ApsSystemException
	 */
	public void updateChannel(Channel channel) throws ApsSystemException;

	/**
	 * Load the entire list o channels.
	 * @param status value used to filter the list.
	 * The values for the status are: 
	 * 1:active
	 * 2:not active
	 * 3:both 
	 * @return the list of the channel filtered by the status.
	 */
	public List<Channel> getChannels(int status) throws ApsSystemException;
	
	/**
	 * Returns a Map [code, descr] of all the contentTypes configured to by exported in rss format
	 * @return a Map [code, descr] of all the contentTypes
	 */
	public Map<String, String> getAvailableContentTypes();
	
	/**
	 * Returns a map with all the feedTypes that con be used.
	 * these values are stored in the spring definition of this service
	 * @return Returns a map with all the feedTypes that con be used
	 */
	public Map<String, String> getAvailableFeedTypes();
	
	public RssContentMapping getContentMapping(String typeCode);
	
	/**
	 * Returns a channel.
	 * @param id the code of the channel
	 * @return the channel object identified by the provided id.
	 */
	public Channel getChannel(int id) throws ApsSystemException;

	/**
	 * Build {@link SyndFeed} according to the params provided.
	 * This object is the one the the rssServlet uses to print data in response.
	 * @param channel The target channel object.
	 * @param lang the code of the lang that will be used retrieve the contents
	 * @param feedLink il link completo, generato dalla sevlet dell'oggetto syndFeed.
	 * @param req the request
	 * @param resp the response
	 * @return a SyndFeed according to the params provided
	 * @throws ApsSystemException if an error occurs
	 */
	public SyndFeed getSyndFeed(Channel channel, String lang, String feedLink, HttpServletRequest req,  HttpServletResponse resp) throws ApsSystemException;
	
}
