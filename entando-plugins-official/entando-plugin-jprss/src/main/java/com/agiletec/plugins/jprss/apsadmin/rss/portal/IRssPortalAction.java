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
package com.agiletec.plugins.jprss.apsadmin.rss.portal;

/**
 * Interface for the action that renders the feed
 * @author spuddu
 */
public interface IRssPortalAction {

	/**
	 * Renders the feed according to the id and the language (lang) provided as parameter.
	 * @return
	 */
	public String show();

}