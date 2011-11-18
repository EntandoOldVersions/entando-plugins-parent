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

import com.agiletec.plugins.jprssaggregator.aps.system.services.aggregator.ApsAggregatorItem;

public class TestAggregatorManagerHelper {
	
	public static ApsAggregatorItem createItem(int delay, String descr, String link) {
		ApsAggregatorItem item = new ApsAggregatorItem();
		item.setDelay(delay);
		item.setDescr(descr);
		item.setLink(link);
		item.setContentType("RSS");
		return item;
	}
	
}