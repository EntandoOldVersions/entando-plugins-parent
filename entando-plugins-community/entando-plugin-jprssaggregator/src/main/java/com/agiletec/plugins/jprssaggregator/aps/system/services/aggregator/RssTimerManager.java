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
package com.agiletec.plugins.jprssaggregator.aps.system.services.aggregator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.logging.Logger;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jprssaggregator.aps.system.services.aggregator.event.AggregatorItemsChangedEvent;
import com.agiletec.plugins.jprssaggregator.aps.system.services.aggregator.event.AggregatorItemsChangedObserver;

/**
 * This Service hendles the notifications for the AggregatorItemsChangedEvent.
 */
public class RssTimerManager extends AbstractService implements AggregatorItemsChangedObserver {

	@Override
	public void init() throws Exception {
		this.loadMap();
		this.startThreads();
		ApsSystemUtils.getLogger().config(this.getClass().getName() + ": initialized. Active tasks: " + this.getTimerTaskMap().size());
	}	

	/**
	 * Load the entries stored in the database and creates a new {@link RssTimerTask} for eache one
	 * @throws ApsSystemException
	 */
	private void loadMap() throws ApsSystemException {
		this.setTimerTaskMap(new HashMap<String, RssTimerTask>());
		List<ApsAggregatorItem> items = this.getAggregatorManager().getItems();
		Iterator<ApsAggregatorItem> it = items.iterator();
		while (it.hasNext()) {
			ApsAggregatorItem item = it.next();
			String key = new Integer(item.getCode()).toString();
			RssTimerTask timerTask = new RssTimerTask(item, this.getAggregatorManager());
			this.getTimerTaskMap().put(key, timerTask);
		}
	}

	@Override
	public void updateTasks(AggregatorItemsChangedEvent event) {
		Logger log = ApsSystemUtils.getLogger();
		try {	
			if (event.getOperationCode() == AggregatorItemsChangedEvent.INSERT_OPERATION_CODE) {
				int itemCode = event.getItemCode();
				ApsAggregatorItem item = this.getAggregatorManager().getItem(itemCode);
				RssTimerTask timerTask = new RssTimerTask(item, this.getAggregatorManager());
				this.getTimerTaskMap().put(new Integer(itemCode).toString(), timerTask);
				log.finest("jprssaggregator Created new thread: " + itemCode);
				this.startThread(new Integer(event.getItemCode()).toString());
			} else if (event.getOperationCode() == AggregatorItemsChangedEvent.REMOVE_OPERATION_CODE) {
				String taskKey = new Integer(event.getItemCode()).toString();
				RssTimerTask task = this.getTimerTaskMap().get(taskKey);
				task.cancel();
				this.getTimerTaskMap().remove(taskKey);
				log.finest("jprssaggregator Removed thread: " + taskKey);
			} else if (event.getOperationCode() == AggregatorItemsChangedEvent.UPDATE_OPERATION_CODE) {
				String taskKey = new Integer(event.getItemCode()).toString();
				RssTimerTask task = this.getTimerTaskMap().get(taskKey);
				task.cancel();
				this.getTimerTaskMap().remove(taskKey);
				ApsAggregatorItem item = this.getAggregatorManager().getItem(event.getItemCode());
				RssTimerTask timerTask = new RssTimerTask(item, this.getAggregatorManager());
				this.getTimerTaskMap().put(new Integer(item.getCode()).toString(), timerTask);
				log.finest("jprssaggregator Updating thread: " + taskKey);
				this.startThread(taskKey);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "updateTasks");
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		try {
			this.getTimer().cancel();
			this.getTimer().purge();
			this.setTimer(null);
			Iterator<RssTimerTask> it = this.getTimerTaskMap().values().iterator();
			while (it.hasNext()) {
				RssTimerTask task = it.next();
				task.cancel();
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "destroy");
		}
	}
	
	/**
	 * Starts all the tasks
	 */
	public void startThreads()  {
		try {
			this.resetTimer();
			Iterator<String> it = this.getTimerTaskMap().keySet().iterator();
			while (it.hasNext()) {
				String taskId = it.next();
				this.startThread(taskId);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "startThreads");
			throw new RuntimeException(t);
		}
	}

	/**
	 * Starts a task by the code 
	 * @param taskId the code of the task
	 */
	private void startThread(String taskId)  {
		Logger log = ApsSystemUtils.getLogger();
		log.finest("jprssaggregator Starting thread: " + taskId);
		try {
			RssTimerTask task =	(RssTimerTask) this.getTimerTaskMap().get(taskId);
			ApsAggregatorItem item = task.getItem();
			long delay = new Long(item.getDelay()).longValue();
			this.getTimer().schedule(task, 0, delay * 1000);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "startThread " + taskId);
			throw new RuntimeException(t);
		}
	}

	/**
	 * Resets all the tasks
	 */
	private void resetTimer() {
		if (null != getTimer()) {
			this.getTimer().cancel();
			this.getTimer().purge();
			this.setTimer(null);
		}
		this.setTimer(new Timer());
	}

	public void setAggregatorManager(IAggregatorManager aggregatorManager) {
		this._aggregatorManager = aggregatorManager;
	}
	protected IAggregatorManager getAggregatorManager() {
		return _aggregatorManager;
	}

	public void setTimer(Timer timer) {
		this._timer = timer;
	}
	public Timer getTimer() {
		return _timer;
	}

	public void setTimerTaskMap(Map<String, RssTimerTask> timerTaskMap) {
		this._timerTaskMap = timerTaskMap;
	}
	public Map<String, RssTimerTask> getTimerTaskMap() {
		return _timerTaskMap;
	}

	private Map<String, RssTimerTask> _timerTaskMap;
	private IAggregatorManager _aggregatorManager;
	private Timer _timer;

}
