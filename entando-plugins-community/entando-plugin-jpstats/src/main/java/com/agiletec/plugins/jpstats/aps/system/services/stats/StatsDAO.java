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
package com.agiletec.plugins.jpstats.aps.system.services.stats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentRecordVO;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jpstats.aps.system.services.stats.model.ContentStatistic;
import com.agiletec.plugins.jpstats.aps.system.services.stats.model.DateStatistic;
import com.agiletec.plugins.jpstats.aps.system.services.stats.model.PageStatistic;
import com.agiletec.plugins.jpstats.aps.system.services.stats.model.VisitsStat;

/**
 * Data Access Object for the Statistics Manager
 * @version 1.2
 * @author M.Lisci - E.Santoboni
 */
public class StatsDAO extends AbstractDAO implements IStatsDAO {
	
	@Override
	public List<StatsRecord> loadStatsRecord(Date from, Date to) {
		List<StatsRecord> records = new ArrayList<StatsRecord>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		String startString = new Timestamp(from.getTime()).toString();
		String endString = new Timestamp(to.getTime()).toString();
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(LOAD_RECORDS);
			stat.setString(1, startString);
			stat.setString(2, endString);
			res = stat.executeQuery();
			while (res.next()) {
				StatsRecord record = this.createStatsRecord(res);
				records.add(record);
			}
		} catch (Throwable t) {			
			processDaoException(t, "Error getting Ip address ", "loadStatsRecord");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return records;
	}
	
	/**
	 * Adds a record to the statistic table
	 * @param statsRecord 
	 */
	@Override
	public void addStatsRecord(StatsRecord statsRecord) {
		Connection conn = null;
		PreparedStatement prepStat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			prepStat = conn.prepareStatement(ADD_RECORD);
			prepStat.setString(1, statsRecord.getIp());
			prepStat.setString(2, statsRecord.getReferer());
			prepStat.setString(3, statsRecord.getSessionId());
			prepStat.setString(4, statsRecord.getRole());
			prepStat.setString(5, statsRecord.getTimestamp());
			prepStat.setString(6, statsRecord.getYear());
			prepStat.setString(7, statsRecord.getMonth());
			prepStat.setString(8, statsRecord.getDay());
			prepStat.setString(9, statsRecord.getHour());
			prepStat.setString(10, statsRecord.getPageCode());
			prepStat.setString(11, statsRecord.getLangcode());
			prepStat.setString(12, statsRecord.getUseragent());
			prepStat.setString(13, statsRecord.getBrowserLang());
			prepStat.setString(14, statsRecord.getContentId());
			prepStat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			processDaoException(t, "Error adding a statistic record",	"addStatsRecord");
		} finally {
			closeDaoResources(null, prepStat, conn);
		}
	}
	
	@Override
	public void deleteStatsRecord(Date from, Date to) {
		Connection conn = null;
		PreparedStatement prepStat = null;
		try {
			conn = this.getConnection();
			conn.setAutoCommit(false);
			prepStat = conn.prepareStatement(REMOVE_RECORDS);
			prepStat.setString(1, (new java.sql.Timestamp(from.getTime())).toString());
			prepStat.setString(2, (new java.sql.Timestamp(to.getTime())).toString());
			prepStat.executeUpdate();
			conn.commit();
		} catch (Throwable t) {
			processDaoException(t, "Error removing statistic records", "deleteStatsRecord");
		} finally {
			closeDaoResources(null, prepStat, conn);
		}
	}
	
	/**
	 * Gets the average time spent on the site by session
	 * @param start Calendar 
	 * @param end Calendar
	 * @return a string whith the format hh:mm:ss
	 */
	@Override
	public String getAverageTimeSite(Date start, Date end) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		String mediaSessioni = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(SEARCH_AVERAGE_TIME_SITE);
			stat.setDate(1, new java.sql.Date(start.getTime()));
			stat.setDate(2, new java.sql.Date(end.getTime()));
			res = stat.executeQuery();
			String media = null;
			while (res.next()) {
				media = res.getString(1);   
			}
			mediaSessioni = media;
		} catch (Throwable t) {
			processDaoException(t, "Error getting  average time site", "getAverageTimeSite");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return this.roundInterval(mediaSessioni);
	}
	
	@Override
	public List<VisitsStat> searchVisitsForDate(Date from, Date to) {
		List<VisitsStat> visitsStats = new ArrayList<VisitsStat>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(SEARCH_DAILY_VISITS);
			stat.setString(1, DateConverter.getFormattedDate(from, "yyyy-MM-dd 00:00:00.000"));
			stat.setString(2, DateConverter.getFormattedDate(to, "yyyy-MM-dd 23:59:59.999"));
			res = stat.executeQuery();
			Calendar calendar = Calendar.getInstance();
			while (res.next()) {
				DateStatistic statistic = new DateStatistic();
				int hit = res.getInt(1);
				calendar.set(res.getInt(2), res.getInt(3)-1, res.getInt(4), 0, 0, 0);
				Date day = calendar.getTime();
				statistic.setDate(day);
				statistic.setVisits(new Integer(hit));
				visitsStats.add(statistic);
			}
		} catch (Throwable t) {
			processDaoException(t, "Error searching visits for date", "searchVisitsForDate");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return visitsStats;
	}
	
	@Override
	public List<VisitsStat> searchVisitsForPages(Date from, Date to) {
		List<VisitsStat> visitsStats = new ArrayList<VisitsStat>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(SEARCH_PAGE_VISITS);
			stat.setString(1, DateConverter.getFormattedDate(from, "yyyy-MM-dd 00:00:00.000"));
			stat.setString(2, DateConverter.getFormattedDate(to, "yyyy-MM-dd 23:59:59.999"));
			res = stat.executeQuery();
			IPageManager pageManager = this.getPageManager();
			String langCode = this.getLangManager().getDefaultLang().getCode();
			while (res.next()) {
				PageStatistic statistic = new PageStatistic();
				String code = res.getString(1);
				statistic.setCode(code);
				IPage page = pageManager.getPage(code);
				String descr = (page!=null) ? page.getTitle(langCode) : code;
				statistic.setDescr(descr);
				statistic.setVisits(new Integer(res.getInt(2)));
				visitsStats.add(statistic);
			}
		} catch (Throwable t) {
			processDaoException(t, "Error searching visits for pages", "searchVisitsForPages");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return visitsStats;
	}
	
	@Override
	public List<VisitsStat> searchVisitsForContents(Date from, Date to) {
		List<VisitsStat> visitsStats = new ArrayList<VisitsStat>();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(SEARCH_CONTENT_VISITS);
			stat.setString(1, DateConverter.getFormattedDate(from, "yyyy-MM-dd 00:00:00.000"));
			stat.setString(2, DateConverter.getFormattedDate(to, "yyyy-MM-dd 23:59:59.999"));
			res = stat.executeQuery();
			Map<String, SmallContentType> contentTypes = this.getContentManager().getSmallContentTypesMap();
			while (res.next()) {
				ContentStatistic statistic = new ContentStatistic();
				String id = res.getString(1);
				statistic.setId(id);
				ContentRecordVO content = this.getContentManager().loadContentVO(id);
				if (content == null) {
					statistic.setDescr(id);
				} else {
					SmallContentType contentType = contentTypes.get(content.getTypeCode());
					statistic.setDescr(content.getDescr());
					statistic.setType(contentType.getDescr());
				}
				statistic.setVisits(new Integer(res.getInt(2)));
				visitsStats.add(statistic);
			}
		} catch (Throwable t) {
			processDaoException(t, "Error searching visits for contents", "searchVisitsForContents");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return visitsStats;
	}
	
	/**
	 * Gets the hits between two dates
	 * @param start Calendar 
	 * @param end Calendar
	 * @return a TimeSeries object, used to render the chart
	 */
	@Override
	public TimeSeries getHitsByInterval(Calendar start, Calendar end) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		TimeSeries hitsPage = new TimeSeries("Japs_Chart_v0.0", Day.class);
		String startString = new Timestamp(start.getTimeInMillis()).toString();
		String endString = new Timestamp(end.getTimeInMillis()).toString();
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(HITS_BY_INTERVAL);
			stat.setString(1, startString);
			stat.setString(2, endString);
			res = stat.executeQuery();
			Day initDay = new Day(start.getTime());
			Day endDay = new Day(end.getTime());
			while (res.next()) {
				Day day = new Day(res.getInt("day"),res.getInt("month"),res.getInt("year"));
				hitsPage.add(day,res.getInt("hits"));
			}
			try {
				hitsPage.add(initDay,0);
			} catch (Throwable t) {}
			try {
				hitsPage.add(endDay,0);
			} catch (Throwable t) {}
		} catch (Throwable t) {
			processDaoException(t, "Error getting hits by interval ", "getHitsByInterval");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return hitsPage;
	}
	
	/**
	 * Gets the average time spent on the site by session
	 * @param start Calendar 
	 * @param end Calendar
	 * @return a string whith the format hh:mm:ss
	 */
	@Override
	public String getAverageTimeSite(Calendar start, Calendar end) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		String mediaSessioni = null;
		String startString = new Timestamp(start.getTimeInMillis()).toString();
		String endString = new Timestamp(end.getTimeInMillis()).toString();
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(AVERAGE_TIME_SITE);  
			stat.setString(1, startString);
			stat.setString(2, endString);
			res = stat.executeQuery();
			String media = null;
			while (res.next()) {
				media = res.getString(1);   
			}
			mediaSessioni = media;
		} catch (Throwable t) {
			processDaoException(t, "Error getting  average time site", "getAverageTimeSite");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return this.roundInterval(mediaSessioni);
	}
	
	/**
	 * Gets the average time spent on a page by pagecode and by session
	 * @param start Calendar 
	 * @param end Calendar
	 * @return a string whith the format hh:mm:ss
	 */
	@Override
	public String getAverageTimePage(Calendar start, Calendar end) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		String mediaTimePage = new String();
		String startString = new Timestamp(start.getTimeInMillis()).toString();
		String endString = new Timestamp(end.getTimeInMillis()).toString();
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(AVERAGE_TIME_PAGE);  
			stat.setString(1, startString);
			stat.setString(2, endString);
			res = stat.executeQuery();
			while (res.next()) {
				mediaTimePage = res.getString("media");		
			}
		} catch (Throwable t) {
			processDaoException(t, "Error getting average time page", "getAverageTimePage");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return roundInterval(mediaTimePage);
	}
	
	/**
	 * Gets the average amount of pages visited in each session
	 * @param start Calendar 
	 * @param end Calendar
	 * @return int the average amount of pages visited in each session
	 */
	@Override
	public int getNumPageSession(Calendar start, Calendar end) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		int mediaPage = 0;
		String startString = new Timestamp(start.getTimeInMillis()).toString();
		String endString = new Timestamp(end.getTimeInMillis()).toString();
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(AVERAGE_PAGE);  
			stat.setString(1, startString);
			stat.setString(2, endString);
			res = stat.executeQuery(); 				
			while (res.next()) {
				mediaPage = res.getInt(1);			
			}
		} catch (Throwable t) {
			processDaoException(t, "Error getting average num page session ", "getNumPageSession");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return mediaPage;
	}
	
	/**
	 * Gets the ten most visited pages 
	 * @param start Calendar 
	 * @param end Calendar
	 * @return a map (pagecode:hits) used to render the chart
	 */
	@Override
	public Map<String, Integer> getTopPages(Calendar start, Calendar end) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		Map<String, Integer> hitsPage = new TreeMap<String, Integer>();
		String startString = new Timestamp(start.getTimeInMillis()).toString();
		String endString = new Timestamp(end.getTimeInMillis()).toString();
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_TOP_PAGES);
			stat.setString(1, startString);
			stat.setString(2, endString);
			res = stat.executeQuery();
			while (res.next()) {
				int count = res.getInt(2);
				hitsPage.put(res.getString(1), new Integer(count));
			}
		} catch (Throwable t) {
			processDaoException(t, "Error getting the most visited pages ", "getPageVisitedDesc");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return hitsPage;
	}
	
	/**
	 * Gets the ten most visited contents 
	 * If the content does not exists anymore the function
	 * prints [DELETED] instead of the description
	 * @param start Calendar 
	 * @param end Calendar
	 * @param contentManager 
	 * @return a map (content_descr:hits) used to render the chart
	 */
	@Override
	public Map<String, Integer> getTopContents(Calendar start, Calendar end) {
		IContentManager contentManager = this.getContentManager();
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		Map<String, Integer> topContents = new TreeMap<String, Integer>();
		String startString = new Timestamp(start.getTimeInMillis()).toString();
		String endString = new Timestamp(end.getTimeInMillis()).toString();
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_TOP_CONTENTS);
			stat.setString(1,  startString);
			stat.setString(2,  endString);
			res = stat.executeQuery();
			while (res.next()) {
				String contentId = res.getString(1);
				String contentDescr = null;
				ContentRecordVO content = contentManager.loadContentVO(contentId);
				if (null == content ) {
					contentDescr = "[DELETED]";
				} else {
					contentDescr = content.getDescr();
				}
				int count = res.getInt(2);
				topContents.put(contentDescr, new Integer(count));
			}
		} catch (Throwable t) {
			processDaoException(t, "Error getting the most visited contents ", "getTopContents");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return topContents;
	}

	/**
	 * 
	 * @return Calendar the first date stored in the statistic table
	 * If the table is empty returns the current date
	 */
	@Override
	public Calendar getFirstCalendarDay() {
		Connection conn = null;
		Calendar firstDay = Calendar.getInstance();
		PreparedStatement stat = null;
		ResultSet res = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_FIRST_DATE);
			res = stat.executeQuery();
			while(res.next()) {
				int year = Integer.parseInt(res.getString(1));
				int month = Integer.parseInt(res.getString(2));
				int day = Integer.parseInt(res.getString(3));
				firstDay.set(year,month-1, day,0,0,0);
				firstDay.set(Calendar.MILLISECOND, 0);
			}
		} catch (Throwable t) {
			processDaoException(t, "Error getting the first day ", "getFirstCalendarDay");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return firstDay;
	}
	
	/**
	 * Gets a map of Ip Address (ip,hits) 
	 * @param start Calendar
	 * @param end Calendar
	 * @return a map of Ip (ip,hits) 
	 */
	@Override
	public Map<String, Integer> getIPByDateInterval(Calendar start, Calendar end) {
		Connection conn = null;
		PreparedStatement stat = null;
		ResultSet res = null;
		Map<String, Integer> statsRecord = new TreeMap<String, Integer>();
		String startString = new Timestamp(start.getTimeInMillis()).toString();
		String endString = new Timestamp(end.getTimeInMillis()).toString();
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(GET_IP);
			stat.setString(1, startString);
			stat.setString(2, endString);
			res = stat.executeQuery();
			while (res.next()) {
				String ip = res.getString(1);
				int count = res.getInt(2);
				statsRecord.put(ip, new Integer(count));
			}
		} catch (Throwable t) {			
			processDaoException(t, "Error getting Ip address ", "getIPByDateInterval");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		return statsRecord;
	}
	
	/**
	 * Rounds a string cutting the milliseconds
	 * Queries the gets average time can return null values
	 * This function transform null values in 00:00:00
	 * @param interval String 
	 * @return 
	 */
	private String roundInterval(String interval) {
		if(interval==null) interval = "00:00:00";
		int length = interval.length();
		if (interval.indexOf(".")!=-1) {
			length=interval.indexOf(".");
		}
		return interval.substring(0,length);
	}
	
	private StatsRecord createStatsRecord(ResultSet res) throws Throwable {
		//ip, referer, session_id, role, timestamp, year, month, day, hour, pagecode, langcode, useragent, browserlang, content
		Calendar calendar = this.extractRecordDate(res);
		StatsRecord record = new StatsRecord(calendar);
		record.setIp(res.getString("ip"));
		record.setReferer(res.getString("referer"));
		record.setSessionId(res.getString("session_id"));
		record.setRole(res.getString("role"));
		record.setPageCode(res.getString("pagecode"));
		record.setLangcode(res.getString("langcode"));
		record.setUseragent(res.getString("useragent"));
		record.setBrowserLang(res.getString("browserlang"));
		record.setContentId(res.getString("content"));
		return record;
	}
	
	private Calendar extractRecordDate(ResultSet res) throws SQLException {
		Calendar calendar = Calendar.getInstance();
		String year = res.getString("year");
		String month = res.getString("month");
		String day = res.getString("day");
		String hour = res.getString("hour");
		calendar.set(Calendar.YEAR, Integer.parseInt(year));
		calendar.set(Calendar.MONTH, Integer.parseInt(month)-1);
		calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day));
		String[] array = hour.split(":");
		if (array.length == 3) {
			calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(array[0].trim()));
			calendar.set(Calendar.MINUTE, Integer.parseInt(array[1].trim()));
			calendar.set(Calendar.SECOND, Integer.parseInt(array[2].trim()));
		}
		return calendar;
	}
	
	public IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	public IPageManager getPageManager() {
		return _pageManager;
	}
	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}
	
	public ILangManager getLangManager() {
		return _langManager;
	}
	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}
	
	private IContentManager _contentManager;
	private IPageManager _pageManager;
	private ILangManager _langManager;
	
	private final String ADD_RECORD = 
		"INSERT INTO jpstats_statistics (ip, referer, session_id, role, timestamp, year, month, day, hour, pagecode, langcode, useragent, browserlang, content) "
		+ "VALUES ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";
	
	private final String REMOVE_RECORDS = 
		"DELETE FROM jpstats_statistics WHERE timestamp >= ? AND timestamp <= ? ";
	
	private final String LOAD_RECORDS = 
		"SELECT * FROM jpstats_statistics WHERE timestamp >= ? AND timestamp <= ? ORDER BY timestamp DESC";
	
	private final String SEARCH_DAILY_VISITS =
		"SELECT count(*) as hits, year, month, day FROM jpstats_statistics " +
		"WHERE (timestamp BETWEEN ? AND ? ) GROUP BY year, month, day ORDER BY hits DESC";
	
	private final String SEARCH_PAGE_VISITS = 
		"SELECT pagecode, COUNT(*) AS hits FROM jpstats_statistics WHERE (timestamp BETWEEN ? AND ?) " +
		"GROUP BY pagecode ORDER BY hits DESC";
	
	private final String SEARCH_CONTENT_VISITS = 
		"SELECT content, COUNT(*) AS hits FROM jpstats_statistics WHERE (timestamp BETWEEN ? AND ?) " +
		"AND content IS NOT NULL GROUP BY content ORDER BY hits DESC";
	
	private final String SEARCH_AVERAGE_TIME_SITE = 
		"SELECT avg(x) AS media FROM( SELECT session_id as s, (MAX(timestamp)::TIMESTAMP - MIN(timestamp)::TIMESTAMP) AS x " +
		" FROM jpstats_statistics WHERE (timestamp BETWEEN ? and ?) GROUP BY s HAVING count(session_id)>1 )AS SUBQUERY";
	
	private final String HITS_BY_INTERVAL =
		"SELECT count(*) as hits, day, month, year FROM jpstats_statistics  " +
		" WHERE (timestamp BETWEEN ? and ? )" +
		" GROUP BY year, month, day ORDER BY year, month, day ASC";
	
	private final String AVERAGE_TIME_SITE = 
		"SELECT avg(x) AS media " +
		" FROM( SELECT session_id,(MAX(timestamp)::TIMESTAMP - MIN(timestamp)::TIMESTAMP) AS x " +
		" FROM jpstats_statistics  " +
		" WHERE (timestamp BETWEEN ? and ?)" +
		" GROUP BY session_id " +
		" HAVING count(session_id)>1 )AS SUBQUERY";
	
	private final String AVERAGE_TIME_PAGE=
		"SELECT AVG(x) AS media" +
		" FROM( SELECT session_id as s, pagecode as p, (MAX(timestamp)::TIMESTAMP - MIN(timestamp)::TIMESTAMP) AS x " +
		" FROM jpstats_statistics  " +
		" WHERE (timestamp BETWEEN ? and ?)" +
		" GROUP BY p, s )AS SUBQUERY ";	
	
	private final String AVERAGE_PAGE=
		"SELECT AVG(x)::INT AS media " +
		" FROM(SELECT session_id, COUNT(pagecode) AS x " +
		" FROM jpstats_statistics  " +
		" WHERE (timestamp BETWEEN ? and ?)" +
		" GROUP BY session_id )AS SUBQUERY";
	
	private final String GET_TOP_PAGES = 
		"SELECT pagecode,COUNT(*) AS hits FROM jpstats_statistics  " +
		" WHERE (timestamp BETWEEN ? and ?)" +
		" GROUP BY pagecode " +
		" ORDER BY hits DESC" +
		" LIMIT 10;";
	
	private final String GET_TOP_CONTENTS = 
		"SELECT content, COUNT(content) AS hits FROM jpstats_statistics " +
		"WHERE (timestamp BETWEEN ? and ?) and content IS NOT NULL " +
		"GROUP BY content " +
		"ORDER BY hits " +
		"DESC LIMIT 10";

	private final String GET_FIRST_DATE=
		"SELECT  year, month, day FROM jpstats_statistics ORDER BY timestamp ASC LIMIT 1";

	private final String GET_IP = 
		"SELECT DISTINCT ip, count(*) as count " +
		"FROM jpstats_statistics WHERE (timestamp BETWEEN ? and ?) GROUP BY ip";
	
}