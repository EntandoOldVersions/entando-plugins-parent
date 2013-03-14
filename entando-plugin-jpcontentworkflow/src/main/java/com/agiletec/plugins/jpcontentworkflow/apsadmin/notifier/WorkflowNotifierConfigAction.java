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
package com.agiletec.plugins.jpcontentworkflow.apsadmin.notifier;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.IWorkflowNotifierManager;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.model.NotifierConfig;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;

/**
 * @author E.Santoboni
 */
public class WorkflowNotifierConfigAction extends BaseAction implements IWorkflowNotifierConfigAction {
	
	@Override
	public String config() {
		try {
			NotifierConfig notifierConfig = this.getNotifierManager().getNotifierConfig();
			this.valueFormForEdit(notifierConfig);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "config");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String save() {
		try {
			NotifierConfig notifierConfig = this.getUpdatedConfig();
			this.getNotifierManager().saveNotifierConfig(notifierConfig);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected NotifierConfig getUpdatedConfig() throws ApsSystemException, ParseException {
		NotifierConfig notifierConfig = this.getConfig();
		
		Date startDate = DateConverter.parseDate(this.getStartDate(), DATE_FORMAT);
		long time = startDate.getTime() + this.getHour()*3600000l + this.getMinute()*60000l;
		startDate.setTime(time);
		notifierConfig.setStartScheduler(startDate);
		
		return notifierConfig;
	}
	
	protected void valueFormForEdit(NotifierConfig notifierConfig) {
		this.setConfig(notifierConfig);
		
		Calendar start = Calendar.getInstance();
		start.setTime(notifierConfig.getStartScheduler());
		this.setStartDate(DateConverter.getFormattedDate(notifierConfig.getStartScheduler(), DATE_FORMAT));
		this.setHour(start.get(Calendar.HOUR_OF_DAY));
		this.setMinute(start.get(Calendar.MINUTE));
	}
	
	public int[] getCounterArray(int startValue, int length) {
		int[] counter = {};
		if (length>0) {
			counter = new int[length];
			for (int i=0; i<length; i++) {
				counter[i] = startValue++;
			}
		}
		return counter;
	}
	
	public Map<String, String> getSenderCodes() {
		try {
			return this.getMailManager().getMailConfig().getSenders();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getSenderCodes");
			throw new RuntimeException("Error loading mail sender codes", t);
		}
	}
	
	public NotifierConfig getConfig() {
		return _config;
	}
	public void setConfig(NotifierConfig config) {
		this._config = config;
	}
	
	public String getStartDate() {
		return _startDate;
	}
	public void setStartDate(String startDate) {
		this._startDate = startDate;
	}
	
	public int getHour() {
		return _hour;
	}
	public void setHour(int hour) {
		this._hour = hour;
	}
	
	public int getMinute() {
		return _minute;
	}
	public void setMinute(int minute) {
		this._minute = minute;
	}
	
	protected IWorkflowNotifierManager getNotifierManager() {
		return _notifierManager;
	}
	public void setNotifierManager(IWorkflowNotifierManager notifierManager) {
		this._notifierManager = notifierManager;
	}
	
	protected IMailManager getMailManager() {
		return _mailManager;
	}
	public void setMailManager(IMailManager mailManager) {
		this._mailManager = mailManager;
	}
	
	private NotifierConfig _config = new NotifierConfig();
	
	private String _startDate;
	private int _hour;
	private int _minute;
	
	private IWorkflowNotifierManager _notifierManager;
	private IMailManager _mailManager;
	private static final String DATE_FORMAT = "dd/MM/yyyy";
	
}