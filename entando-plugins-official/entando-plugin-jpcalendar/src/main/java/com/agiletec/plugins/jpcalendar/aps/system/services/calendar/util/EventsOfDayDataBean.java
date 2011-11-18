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
package com.agiletec.plugins.jpcalendar.aps.system.services.calendar.util;

import java.util.Date;
import java.util.Set;

/**
 * @author E.Santoboni
 */
public interface EventsOfDayDataBean {
	
	public String getAttributeNameEnd();
	
	public String getAttributeNameStart();
	
	public String getContentType();
	
	public Date getRequiredDay();
	
	public Set<String> getAllowedGroups();
	
	public void setAllowedGroups(Set<String> allowedGroups);
	
}
