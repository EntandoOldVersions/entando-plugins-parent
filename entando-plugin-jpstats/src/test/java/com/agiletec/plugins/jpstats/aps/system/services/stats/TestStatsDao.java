/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
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
package com.agiletec.plugins.jpstats.aps.system.services.stats;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.jfree.data.time.TimeSeries;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jpstats.aps.ApsPluginBaseTestCase;
import com.agiletec.plugins.jpstats.aps.util.CalendarConverter;
import com.agiletec.plugins.jpstats.util.TestStatsUtils;

public class TestStatsDao extends ApsPluginBaseTestCase {
	
	public void testAddStatsRecord() throws Throwable{
		String today = DateConverter.getFormattedDate(new Date(), "dd/MM/yyyy");
		Calendar start = CalendarConverter.getCalendarDay(today, 0, 0, 0, 0);
		Calendar end = CalendarConverter.getCalendarDay(today, 23, 59, 59, 999);
		try {
			this._statsDAO.addStatsRecord(this.createRecordForTest());
			List<StatsRecord> records = _statsDAO.loadStatsRecord(start.getTime(), end.getTime());
			assertEquals(1, records.size());
		} catch (Throwable t) {
			throw t;
		} finally {
			this._statsDAO.deleteStatsRecord(start.getTime(), end.getTime());
			List<StatsRecord> records = _statsDAO.loadStatsRecord(start.getTime(), end.getTime());
			assertEquals(0, records.size());
		}
	}
	
	private StatsRecord createRecordForTest() {
		StatsRecord statsRecord = new StatsRecord(Calendar.getInstance());
		statsRecord.setIp("127.0.0.1");
		statsRecord.setReferer("http://www.japsportal.org/japsportal/it/chi_siamo.wp");
		statsRecord.setSessionId("A3EFA0BC2E5FB24436609B5BE786EDCA");
		statsRecord.setRole("");
		statsRecord.setPageCode("chi_siamo");
		statsRecord.setLangcode("it");
		statsRecord.setUseragent("Mozilla/5.0 (X11; U; Linux i686; it; rv:1.9.0.7) Gecko/2009030422 Ubuntu/8.10 (intrepid) Firefox/3.0.7");
		statsRecord.setBrowserLang("it-it,it;q=0.8,en-us;q=0.5,en;q=0.3");
		statsRecord.setContentId("ART124");
		return statsRecord;
	}
	
	public void testGetHitsByInterval() throws ApsSystemException{
		TimeSeries result = null;
		CalendarConverter converter = new CalendarConverter();
		Calendar initialDate = converter.getCalendarDay("06/03/2007", 0, 0, 0, 0);
		Calendar finalDate = converter.getCalendarDay("10/03/2007", 23, 59, 59, 999);
		try{
			result = _statsDAO.getHitsByInterval(initialDate,finalDate);
		} catch (Throwable t) {
			throw new ApsSystemException("error in get hits by interval", t);
		}
		assertNotNull(result);
	}
	
	public void testGetAverageTimeSite() throws ApsSystemException{
		String result = null;
		CalendarConverter converter = new CalendarConverter();
		Calendar initialDate = converter.getCalendarDay("06/03/2007", 0, 0, 0, 0);
		Calendar finalDate = converter.getCalendarDay("10/03/2007", 23, 59, 59, 999);
		try{
			result = _statsDAO.getAverageTimeSite(initialDate,finalDate);
		} catch (Throwable t) {
			throw new ApsSystemException("error in get average time site", t);
		}
		assertNotNull(result);
	}

	public void testGetAverageTimePage() throws ApsSystemException{
		String result = null;
		CalendarConverter converter = new CalendarConverter();
		Calendar initialDate = converter.getCalendarDay("06/03/2007", 0, 0, 0, 0);
		Calendar finalDate = converter.getCalendarDay("10/03/2007", 23, 59, 59, 999);
		try {
			result = _statsDAO.getAverageTimePage(initialDate,finalDate);
		} catch (Throwable t) {
			throw new ApsSystemException("error in get average time site", t);
		}
		assertNotNull(result);
	}

	public void testGetNumPageSession() throws ApsSystemException{
		int result;
		CalendarConverter converter = new CalendarConverter();
		Calendar initialDate = converter.getCalendarDay("06/03/2007", 0, 0, 0, 0);
		Calendar finalDate = converter.getCalendarDay("10/03/2007", 23, 59, 59, 999);
		try{
			result = _statsDAO.getNumPageSession(initialDate,finalDate);
		} catch (Throwable t) {
			throw new ApsSystemException("error in get average time site", t);
		}
		assertTrue(result >= 0);
	}
	
	public void testGetPageVisitedDesc() throws ApsSystemException{
		Map result = null;
		CalendarConverter converter = new CalendarConverter();
		Calendar initialDate = converter.getCalendarDay("06/03/2007", 0, 0, 0, 0);
		Calendar finalDate = converter.getCalendarDay("10/03/2007", 23, 59, 59, 999);
		try{
			result = _statsDAO.getTopPages(initialDate,finalDate);
		} catch (Throwable t) {
			throw new ApsSystemException("error in get average time site", t);
		}
		assertNotNull(result);
	}

	public void testGetTopContents() throws ApsSystemException{
		Map result = null;
		CalendarConverter converter = new CalendarConverter();
		Calendar initialDate = converter.getCalendarDay("06/03/2007", 0, 0, 0, 0);
		Calendar finalDate = converter.getCalendarDay("10/03/2007", 23, 59, 59, 999);
		try {
			result = _statsDAO.getTopContents(initialDate, finalDate);
		} catch (Throwable t) {
			throw new ApsSystemException("error in get average time site", t);
		}
		assertNotNull(result);
	}

	public void testGetFirstCalendarDay() throws ApsSystemException{
		Calendar firstDay = null;
		try {
			firstDay = _statsDAO.getFirstCalendarDay();
		} catch (Throwable t) {
			throw new ApsSystemException("error in get first day");
		}
		assertNotNull(firstDay);
	}
	
	public void testGetIPByDateInterval() throws ApsSystemException{
		Map result = null;
		CalendarConverter converter = new CalendarConverter();
		Calendar initialDate = converter.getCalendarDay("06/03/2007", 0, 0, 0, 0);
		Calendar finalDate = converter.getCalendarDay("10/03/2007", 23, 59, 59, 999);
		try{
			result = _statsDAO.getIPByDateInterval(initialDate,finalDate);
		}catch (Throwable t) {
			throw new ApsSystemException("error in get average time site", t);
		}
		assertNotNull(result);
	}
	
	protected void tearDown() throws Exception {
		try {
			DataSource dataSource = (DataSource) this.getApplicationContext().getBean("portDataSource");
			TestStatsUtils.cleanDB(ip, dataSource);
		} catch (Throwable e) {
			throw new Exception(e);
		}
		super.tearDown();
	}
	
	@Override
	protected void init() throws Exception {
		super.init();
		_statsDAO = (StatsDAO) this.getApplicationContext().getBean("jpstatsStatsDAO");
	}
	
	private StatsDAO _statsDAO;
	private String ip = "0.0.0.0";

}
