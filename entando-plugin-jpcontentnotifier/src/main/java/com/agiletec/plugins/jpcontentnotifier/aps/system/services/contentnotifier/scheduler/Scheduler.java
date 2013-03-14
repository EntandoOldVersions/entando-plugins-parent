/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* JAPS and its  source-code is  licensed under the  terms of the
* GNU General Public License  as published by  the Free Software
* Foundation (http://www.fsf.org/licensing/licenses/gpl.txt).
* 
* You may copy, adapt, and redistribute this file for commercial
* or non-commercial use.
* When copying,  adapting,  or redistributing  this document you
* are required to provide proper attribution  to AgileTec, using
* the following attribution line:
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpcontentnotifier.aps.system.services.contentnotifier.scheduler;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.util.DateConverter;

/**
 * Classe Scheduler
 * @version 1.0
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
		Timer timer = new Timer();
		timer.schedule(this, start, delay);
		this._task = task;
		ApsSystemUtils.getLogger().log(Level.FINEST, "jpcontentnotifier: Scheduler - StartTime: " + DateConverter.getFormattedDate(start, "dd/MM/yyyy HH:mm:ss") + " - Delay: " + delay);
	}
	
	public boolean cancel() {
		this._task = null;
		return super.cancel();
	}
	
	public void run() {
		if (null != _task) _task.execute();
	}
	
	private Task _task;
	
}