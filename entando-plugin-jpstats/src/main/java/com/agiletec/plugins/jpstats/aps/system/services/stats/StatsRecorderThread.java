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
package com.agiletec.plugins.jpstats.aps.system.services.stats;

import com.agiletec.aps.system.ApsSystemUtils;

/**
 * @author E.Santoboni
 */
public class StatsRecorderThread extends Thread {
	
	public StatsRecorderThread(StatsManager statsManager, StatsRecord statsRecord) {
		this.statsManager = statsManager;
		this.statsRecord = statsRecord;
	}
	
	public void run() {
		try {
			this.statsManager.addStatsRecordFromThread(this.statsRecord);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "run");
		}
	}
	
	private StatsManager statsManager;
	private StatsRecord statsRecord;
	
}
