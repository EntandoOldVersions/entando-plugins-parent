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
package com.agiletec.plugins.jpstats.aps;

import com.agiletec.ConfigTestUtils;
import com.agiletec.aps.BaseTestCase;
import com.agiletec.plugins.jpstats.PluginConfigTestUtils;
import com.agiletec.plugins.jpstats.aps.system.services.JpStatsSystemConstants;
import com.agiletec.plugins.jpstats.aps.system.services.stats.StatsManager;
import com.agiletec.plugins.jpstats.util.TestStatsUtils;

public class ApsPluginBaseTestCase extends BaseTestCase {
	
	@Override
	protected ConfigTestUtils getConfigUtils() {
		return new PluginConfigTestUtils();
	}
	
	
	@Override
	protected void setUp() throws Exception {
        super.setUp();
        this.init();
        TestStatsUtils.cleanTestEnvironment(this._statsManager);
	}
	
	protected void init() throws Exception {
    	try {
    		this._statsManager = (StatsManager) this.getService(JpStatsSystemConstants.STATS_MANAGER);
    	} catch (Throwable t) {
            throw new Exception(t);
        }
	}
	
	protected StatsManager _statsManager = null;
	
}
