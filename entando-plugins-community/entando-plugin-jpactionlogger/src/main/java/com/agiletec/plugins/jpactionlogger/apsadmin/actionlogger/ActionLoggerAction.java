/*
*
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* jAPS is a free software; 
* you can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpactionlogger.apsadmin.actionlogger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpactionlogger.aps.system.services.actionlogger.IActionLoggerManager;
import com.agiletec.plugins.jpactionlogger.aps.system.services.actionlogger.model.ActionRecord;
import com.agiletec.plugins.jpactionlogger.aps.system.services.actionlogger.model.ActionRecordSearchBean;
import com.agiletec.plugins.jpactionlogger.aps.system.services.actionlogger.model.IActionRecordSearchBean;
import com.opensymphony.xwork2.Action;

public class ActionLoggerAction extends BaseAction implements IActionLoggerAction {
	
	@Override
	public List<Integer> getActionRecords() {
		List<Integer> actionRecords = new ArrayList<Integer>();
		try {
			IActionRecordSearchBean searchBean = this.prepareSearchBean();
			actionRecords = this.getActionLoggerManager().getActionRecords(searchBean);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getActionRecords");
		}
		return actionRecords;
	}
	
	@Override
	public String delete() {
		try {
			this.getActionLoggerManager().deleteActionRecord(this.getId());
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "delete");
			return FAILURE;
		}
		return Action.SUCCESS;
	}
	
	public ActionRecord getActionRecord(int id) {
		ActionRecord actionRecord = null;
		try {
			actionRecord = this.getActionLoggerManager().getActionRecord(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getActionRecord");
		}
		return actionRecord;
	}
	
	protected IActionRecordSearchBean prepareSearchBean() {
		ActionRecordSearchBean searchBean = new ActionRecordSearchBean();
		searchBean.setUsername(this.getUsername());
		searchBean.setActionName(this.getActionName());
		searchBean.setNamespace(this.getNamespace());
		searchBean.setParams(this.getParams());
		searchBean.setStart(this.getStart());
		Date end = this.getEnd();
		if (end != null) {
			searchBean.setEnd(new Date(end.getTime() + 86400000)); // un giorno dopo
		}
		return searchBean;
	}
	
	public void setId(int id) {
		this._id = id;
	}
	public int getId() {
		return _id;
	}
	
	public void setUsername(String username) {
		this._username = username;
	}
	public String getUsername() {
		return _username;
	}
	
	public void setNamespace(String namespace) {
		this._namespace = namespace;
	}
	public String getNamespace() {
		return _namespace;
	}
	
	public void setActionName(String actionName) {
		this._actionName = actionName;
	}
	public String getActionName() {
		return _actionName;
	}
	
	public void setParams(String params) {
		this._params = params;
	}
	public String getParams() {
		return _params;
	}
	
	public void setStart(Date start) {
		this._start = start;
	}
	public Date getStart() {
		return _start;
	}
	
	public void setEnd(Date end) {
		this._end = end;
	}
	public Date getEnd() {
		return _end;
	}
	
	public void setActionLoggerManager(IActionLoggerManager actionLoggerManager) {
		this._actionLoggerManager = actionLoggerManager;
	}
	protected IActionLoggerManager getActionLoggerManager() {
		return _actionLoggerManager;
	}
	
	private int _id;
	private String _username;
	private String _namespace;
	private String _actionName;
	private String _params;
	private Date _start;
	private Date _end;
	
	private IActionLoggerManager _actionLoggerManager;
	
}