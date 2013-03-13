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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.common.AbstractDAO;

/**
 * Data Access Object for Channel Object. 
 */
public class RssDAO extends AbstractDAO implements IRssDAO {
	
	@Override
	public void addChannel(Channel channel) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(ADD_CHANNEL);
			stat.setInt(1, channel.getId());
			stat.setString(2, channel.getTitle());
			stat.setString(3, channel.getDescription());
			stat.setString(4, new Boolean(channel.isActive()).toString());
			stat.setString(5, channel.getContentType());
			stat.setString(6, channel.getCategory());
			if (null != channel.getFilters() && channel.getFilters().length() > 0) {
				stat.setString(7, channel.getFilters());
			} else {
				stat.setNull(7, Types.VARCHAR);
			}
			stat.setString(8, channel.getFeedType());
			if (channel.getMaxContentsSize() > 0) {
				stat.setInt(9, channel.getMaxContentsSize());
			} else {
				stat.setNull(9, Types.INTEGER);
			}
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			processDaoException(t, "Error adding a channel", "addChannel");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void updateChannel(Channel channel) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(UPDATE_CHANNEL);
			//id, title, description, active, contentType, fileters, feedtype
			stat.setString(1, channel.getTitle());
			stat.setString(2, channel.getDescription());
			stat.setString(3, new Boolean(channel.isActive()).toString());
			stat.setString(4, channel.getContentType());
			if (null != channel.getCategory() && channel.getCategory().length() > 0 ) {
				stat.setString(5, channel.getCategory());
			} else {
				stat.setNull(5, Types.VARCHAR);
			}
			stat.setString(6, channel.getFilters());
			stat.setString(7, channel.getFeedType());
			if (channel.getMaxContentsSize() > 0) {
				stat.setInt(8, channel.getMaxContentsSize());
			} else {
				stat.setNull(8, Types.INTEGER);
			}
			stat.setInt(9, channel.getId());
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			processDaoException(t, "Error updating a channel", "updateChannel");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public void deleteChannel(int id) {
		Connection conn = null;
		PreparedStatement stat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			stat = conn.prepareStatement(DELETE_CHANNEL);
			stat.setInt(1, id);
			stat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			processDaoException(t, "Error deleting a channel", "deleteChannel");
		} finally {
			closeDaoResources(null, stat, conn);
		}
	}
	
	@Override
	public List<Channel> getChannels(int status) throws Throwable {
		List<Channel> channels = new ArrayList<Channel>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			if (status == Channel.STATUS_ALL) {
				stat = conn.prepareStatement(LOAD_CHANNELS);
			} else {
				stat = conn.prepareStatement(LOAD_CHANNELS_BY_STATUS);
				stat.setString(1, new Boolean(status == Channel.STATUS_ACTIVE).toString());
			}
			res = stat.executeQuery();
			Channel channel;
			while (res.next()) {
				channel = this.getChannelFromResultSet(res);
				channels.add(channel);
			}
		} catch (Throwable t) {
			processDaoException(t, "Error loading the channels list with status", "getChannels");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return channels;
	}
	
	@Override
	public Channel getChannel(int id) {
		Channel channel = null;
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_CHANNEL_BY_ID);
			stat.setInt(1, id);
			res = stat.executeQuery();
			while (res.next()) {
				channel = this.getChannelFromResultSet(res);
			}
		} catch (Throwable t) {
			processDaoException(t, "Error loading a channel", "getChannel");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return channel;
	}
	
	private Channel getChannelFromResultSet(ResultSet res) throws Throwable {
		Channel channel = new Channel();
		try {
			channel.setActive(res.getString("active").equalsIgnoreCase("true"));
			channel.setContentType(res.getString("contenttype"));
			channel.setCategory(res.getString("category"));
			channel.setDescription(res.getString("description"));
			channel.setFeedType(res.getString("feedtype"));
			channel.setFilters(res.getString("filters"));
			channel.setId(res.getInt("id"));
			channel.setTitle(res.getString("title"));
			
			int maxContentSize = res.getInt("maxcontentsize");
			if (maxContentSize > 0) {
				channel.setMaxContentsSize(maxContentSize);
			}
		} catch (Throwable t) {
			throw new Throwable("Error creating a channel from resultset", t);
		}
		return channel;
	}

	private static final String ADD_CHANNEL = 
		"INSERT INTO jprss_channel (id, title, description, active, contentType, category, filters, feedtype, maxcontentsize) " +
		"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	private static final String DELETE_CHANNEL = 
		"DELETE FROM jprss_channel WHERE id = ?";
	
	private static final String UPDATE_CHANNEL = 
		"UPDATE jprss_channel SET title =?, description=?, active=?, contentType=?, category=?, filters=?, feedtype=?, maxcontentsize=? WHERE id=?";
	
	private static final String LOAD_CHANNELS_BASE_BLOCK = 
		"SELECT id, title, description, active, contentType, category, filters, feedtype, maxcontentsize from jprss_channel ";
	
	private static final String LOAD_CHANNELS_ORDER_BLOCK = "ORDER BY description ";
	
	private static final String LOAD_CHANNELS = 
		LOAD_CHANNELS_BASE_BLOCK + LOAD_CHANNELS_ORDER_BLOCK;
	
	private static final String LOAD_CHANNELS_BY_STATUS = 
		LOAD_CHANNELS_BASE_BLOCK + " where active = ? " + LOAD_CHANNELS_ORDER_BLOCK;
	
	private static final String LOAD_CHANNEL_BY_ID = 
		LOAD_CHANNELS_BASE_BLOCK + " where id = ? " + LOAD_CHANNELS_ORDER_BLOCK;
	
}
