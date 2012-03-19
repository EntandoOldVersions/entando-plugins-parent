/*
*
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.agiletec.aps.system.ApsSystemUtils;

/**
 * Classe Scheduler
 * @author M.Casari - E.Santoboni
 */
public class Scheduler extends TimerTask {
	
	public Scheduler(INewsletterManager newsletterManager, Date start, long horsDelay) {
		this._newsletterManager = newsletterManager;
		this._timer = new Timer();
		this._timer.schedule(this, start, horsDelay*60*60*1000);
	}
	
	@Override
	public boolean cancel() {
		this._timer.cancel();
		return super.cancel();
	}
	
	@Override
	public void run() {
		try {
			this._newsletterManager.sendNewsletter();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "run");
			throw new RuntimeException("Error on executing TimerTask", t);
		}
	}
	
	private Timer _timer;
	private INewsletterManager _newsletterManager;
	
}