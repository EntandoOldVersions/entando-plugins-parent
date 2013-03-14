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
package com.agiletec.plugins.jpcontentworkflow.aps.system.services.notifier.scheduler;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.util.DateConverter;

/**
 * Classe Scheduler
 * @author M.Casari - E.Santoboni
 */
public class Scheduler extends TimerTask {
	
	/**
	 * Costruttore dello scheduler
	 * @param task Task da eseguire.
	 * @param start Data di partenza dello scheduler
	 * @param delay Intervallo di schedulazione in millisecondi
	 */
	public Scheduler(Task task, Date start, long delay) {
		this._timer = new Timer();
		this._task = task;
		this._timer.schedule(this, start, delay);
		ApsSystemUtils.getLogger().log(Level.FINEST, "jpcontentworkflow: Scheduler - StartTime: " + DateConverter.getFormattedDate(start, "dd/MM/yyyy HH:mm:ss") + " - Delay: " + delay);
	}
	
	@Override
	public boolean cancel() {
		this._task = null;
		this._timer.cancel();
		this._timer.purge();
		return super.cancel();
	}
	
	@Override
	public void run() {
		if (null != _task) _task.execute();
	}
	
	private Task _task;
	private Timer _timer;
}