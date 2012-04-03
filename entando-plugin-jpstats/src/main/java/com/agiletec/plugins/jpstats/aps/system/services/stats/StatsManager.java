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
package com.agiletec.plugins.jpstats.aps.system.services.stats;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jfree.data.time.TimeSeries;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpstats.aps.system.services.stats.model.VisitsStat;

/**
 * Manager that handles the statistic service
 * @author M.Lisci - E.Santoboni
 */
public class StatsManager extends AbstractService implements IStatsManager {
	
	@Override
	public void init() throws Exception {
		this._firstCalendarDay = this.getStatsDao().getFirstCalendarDay();
		ApsSystemUtils.getLogger().config(this.getClass().getName() + ": initialized");
	}
	
	@Override
	public List<StatsRecord> loadStatsRecord(Date from, Date to) throws ApsSystemException {
		List<StatsRecord> records = new ArrayList<StatsRecord>();
		try {
			records = this.getStatsDao().loadStatsRecord(from, to);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "loadStatsRecord");
			throw new ApsSystemException("An error occurred extracting stats records", t);
		}
		return records;
	}
	
	@Override
	public void addStatsRecord(StatsRecord statsRecord) throws ApsSystemException {
		try {
			StatsRecorderThread thread = new StatsRecorderThread(this, statsRecord);
			thread.setName(RECORDER_THREAD_PREFIX + System.currentTimeMillis());
			thread.start();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addStatsRecord");
			throw new ApsSystemException("An error occurred adding a stats record", t);
		}
	}
	
	@Override
	public void deleteStatsRecord(Date from, Date to) throws ApsSystemException {
		try {
			this.getStatsDao().deleteStatsRecord(from, to);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteStatsRecord");
			throw new ApsSystemException("An error occurred removing stats records", t);
		}
	}
	
	@Override
	public String getAverageTimeSite(Date from, Date to) throws ApsSystemException {
		try {
			return this.getStatsDao().getAverageTimeSite(from, to);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteStatsRecord");
			throw new ApsSystemException("An error occurred removing stats records", t);
		}
	}
	
	@Override
	public List<VisitsStat> searchVisitsForDate(Date from, Date to) throws ApsSystemException {
		try {
			return this.getStatsDao().searchVisitsForDate(from, to);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "searchVisitsForDate");
			throw new ApsSystemException("An error occurred searching visits for date", t);
		}
	}
	
	@Override
	public List<VisitsStat> searchVisitsForPages(Date from, Date to) throws ApsSystemException {
		try {
			List<VisitsStat> visitsStat = this.getStatsDao().searchVisitsForPages(from, to);
			return visitsStat;
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "searchVisitsForPages");
			throw new ApsSystemException("An error occurred searching visits for pages", t);
		}
	}
	
	@Override
	public List<VisitsStat> searchVisitsForContents(Date from, Date to) throws ApsSystemException {
		try {
			return this.getStatsDao().searchVisitsForContents(from, to);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "searchVisitsForContents");
			throw new ApsSystemException("An error occurred searching visits for contents", t);
		}
	}
	
	@Override
	public TimeSeries getHitsByInterval(Calendar start, Calendar end) throws ApsSystemException {
		TimeSeries hitsPage = null;
		try {
			hitsPage = this.getStatsDao().getHitsByInterval(start,end);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getHitsByInterval");
			throw new ApsSystemException("An error occurred getting HitsByInterval", t);
		}
		return hitsPage;
	}

	@Override
	public String getAverageTimeSite(Calendar start, Calendar end) throws ApsSystemException {
		String timeSite = null;
		try {
			timeSite = this.getStatsDao().getAverageTimeSite(start,end);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getAverageTimeSite");
			throw new ApsSystemException("An error occurred getting the AverageTimeSite", t);
		}
		return timeSite;
	}

	@Override
	public String getAverageTimePage(Calendar start, Calendar end) throws ApsSystemException {
		String timePage = null;
		try {
			timePage = this.getStatsDao().getAverageTimePage(start, end);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getAverageTimePage");
			throw new ApsSystemException("An error occurred getting the AverageTimePage", t);
		}
		return timePage;
	}
	
	@Override
	public int getNumPageSession(Calendar start, Calendar end) throws ApsSystemException {
		int numPage = 0;
		try {
			numPage = this.getStatsDao().getNumPageSession(start, end);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getNumPageSession");
			throw new ApsSystemException("An error occurred getting the NumPageSession", t);
		}
		return numPage;
	}
	
	/**
	 * Gets the top ten pages by hits
	 * @param start Calendar object
	 * @param end Calendar object
	 * @return a Map (pagecode;hits) used to render the chart
	 * @throws ApsSystemException
	 */
	@Override
	public Map<String, Integer> getTopPages(Calendar start, Calendar end) throws ApsSystemException {
		Map<String, Integer> hitsPage = new HashMap<String, Integer>();
		try {
			hitsPage = this.getStatsDao().getTopPages(start, end);			
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getTopPages");
			throw new ApsSystemException("An error occurred getting the TopPages", t);
		}
		return hitsPage;
	}
	
	/**
	 * Gets the top ten Contents by hits
	 * @param start Calendar object
	 * @param end Calendar object
	 * @return a Map (content description;hits) used to render the chart
	 * @throws ApsSystemException
	 */
	@Override
	public Map<String, Integer> getTopContents(Calendar start, Calendar end) throws ApsSystemException {
		Map<String, Integer> topContents = new HashMap<String, Integer>();
		try {
			topContents = this.getStatsDao().getTopContents(start, end);			
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getTopContents");
			throw new ApsSystemException("An error occurred getting the TopPages", t);
		}
		return topContents;
	}
	
	/**
	 * Gets the count of different Ip addresses that visited the site
	 * @param start Calendar object
	 * @param end Calendar object
	 * @return integer value of the count of different Ip addresses that visited the site
	 * @throws ApsSystemException
	 */
	@Override
	public int getIPByDateInterval(Calendar start,Calendar end) throws ApsSystemException {
		int hitsCount = 0;
		try {
			hitsCount = this.getStatsDao().getIPByDateInterval(start, end).size();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getIPByDateInterval");
			throw new ApsSystemException("An error occurred getting the Ip addresses ", t);
		}
		return hitsCount;
	}
	
	protected void addStatsRecordFromThread(StatsRecord statsRecord) throws ApsSystemException {
		try {
			this.getStatsDao().addStatsRecord(statsRecord);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addStatsRecordFromThread");
			throw new ApsSystemException("An error occurred adding a stats record", t);
		}
	}
	
	/**
	 * Gets the oldest date stored in the statistic table
	 * This is the default value for the queries
	 * @return Calendar object that represents the oldest date stored in the statistic table
	 */
	@Override
	public Calendar getFirstCalendarDay()  {
		return _firstCalendarDay;
	}
	
	protected IStatsDAO getStatsDao() {
		return _statsDao;
	}
	public void setStatsDao(IStatsDAO statsDao) {
		this._statsDao = statsDao;
	}
	
	private IStatsDAO _statsDao;
	private Calendar _firstCalendarDay;
	
	public static final String RECORDER_THREAD_PREFIX = "recorderThreadPrefix_";
	
}