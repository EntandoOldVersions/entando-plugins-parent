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
package com.agiletec.plugins.jpcalendar.aps.system.services.calendar;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.common.AbstractDAO;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpcalendar.aps.system.services.calendar.util.DateEventInfo;

/**
 * Data Access Object for the service managing
 * data for calendar.
 * @author E.Santoboni
 */
public class CalendarDAO extends AbstractDAO implements ICalendarDAO {
	
	@Override
	public int[] loadCalendar(Calendar requiredMonth, String groupName, String contentType, 
			String attributeNameStart, String attributeNameEnd) throws ApsSystemException {
		int[] eventsForMonth = new int[31];
		List<DateEventInfo> eventsInfo = this.getEventsInfo(requiredMonth, groupName, 
				contentType, attributeNameStart, attributeNameEnd);
		for (int i=0; i<eventsInfo.size(); i++) {
			DateEventInfo info = (DateEventInfo) eventsInfo.get(i);
			if (info.isValidEvent()) {
				for (int j=(info.getStartDay()-1); j<=(info.getEndDay()-1); j++) {
					int eventsOfDay = eventsForMonth[j];
					eventsForMonth[j] = (++eventsOfDay);
				}
			}
		}
		return eventsForMonth;
	}
	
	@Override
	public int getFirstYear(String contentType, String attributeName) throws ApsSystemException {
		Calendar today = Calendar.getInstance();
		int year = today.get(Calendar.YEAR);
		PreparedStatement stat = null;
    	ResultSet res = null;
    	Connection conn = null;
		try {
			conn = this.getConnection();
    		stat = conn.prepareStatement(SEARCH_FIRST_YEAR);
    		stat.setString(1, contentType);
    		stat.setString(2, attributeName);
    		res = stat.executeQuery();
    		if (res.next()) {
    			java.sql.Date date = res.getDate(1);
    			Calendar cal = Calendar.getInstance();
				if (date != null) cal.setTime(date);
				year = cal.get(Calendar.YEAR);
    		}
    	} catch (Throwable t) {
			processDaoException(t,"Error in search for first year", "getFirstYear");
    	} finally {
    		closeDaoResources(res, stat, conn);
    	}
		return year;
	}
	
	private List<DateEventInfo> getEventsInfo(Calendar requiredMonth, String groupName, String contentType, 
			String attributeNameStart, String attributeNameEnd) throws ApsSystemException {
		Map<String, DateEventInfo> eventsInfoMap = new HashMap<String, DateEventInfo>();
		PreparedStatement stat = null;
		ResultSet res = null;
		Date first = this.getFirstDay(requiredMonth);
		Date last = this.getLastDay(requiredMonth);
		Connection conn = null;
		try {
			conn = this.getConnection();
			stat = conn.prepareStatement(SEARCH_DATES);
			stat.setString(1, contentType);
			stat.setString(2, attributeNameStart);
			stat.setString(3, attributeNameEnd);
			stat.setString(4, groupName);
			res = stat.executeQuery();
			while (res.next()) {
				String contentId = res.getString(1);
				DateEventInfo info = (DateEventInfo) eventsInfoMap.get(contentId);
				if (info == null) {
					info = new DateEventInfo(first, last);
					eventsInfoMap.put(contentId, info);
				}
				String attributeName = res.getString(2);
				if (attributeName.equalsIgnoreCase(attributeNameStart)) {
					info.setStart(res.getDate(3));
				} else if (attributeName.equalsIgnoreCase(attributeNameEnd)) {
					info.setEnd(res.getDate(3));
				}
			}
		} catch (Throwable t) {
			processDaoException(t, "Error loading calendar", "loadCalendar");
		} finally {
			closeDaoResources(res, stat, conn);
		}
		List<DateEventInfo> eventsInfo = new ArrayList<DateEventInfo>();
		eventsInfo.addAll(eventsInfoMap.values());
		return eventsInfo;
	}
	
	private java.sql.Date getFirstDay(Calendar requiredMonth) {
		Calendar first = (Calendar) requiredMonth.clone();
		first.set(Calendar.DAY_OF_MONTH, requiredMonth.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date date = new Date(first.getTime().getTime());
		return date;
	}
	
	private java.sql.Date getLastDay(Calendar requiredMonth) {
		Calendar last = (Calendar) requiredMonth.clone();
		last.set(Calendar.DAY_OF_MONTH, requiredMonth.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date date = new Date(last.getTime().getTime());
		return date;
	}
	
	private final String SEARCH_DATES = 
		"SELECT contents.contentid, contentsearch.attrname, contentsearch.datevalue " +
		"FROM (contents INNER JOIN contentrelations ON contents.contentid = contentrelations.contentid) " +
		"INNER JOIN contentsearch ON contents.contentid = contentsearch.contentid " +
		"WHERE contents.onlinexml IS NOT NULL " +
		"AND contents.contenttype = ? " + // 1 tipo contenuto
		"AND (contentsearch.attrname = ? " + // 2 nome attributo inizio
		"OR contentsearch.attrname = ?) " + // 3 nome attributo fine
		"AND contentrelations.refgroup = ? " + // 4 gruppo
		"ORDER BY contents.contentid";
	
	private final String SEARCH_FIRST_YEAR = 
		"SELECT MIN(contentsearch.datevalue) " +
		"FROM contents INNER JOIN contentsearch ON contents.contentid = contentsearch.contentid " +
		"WHERE contents.onlinexml IS NOT NULL " +
		"AND contents.contenttype = ? " +
		"AND contentsearch.attrname = ? ";
		
}
