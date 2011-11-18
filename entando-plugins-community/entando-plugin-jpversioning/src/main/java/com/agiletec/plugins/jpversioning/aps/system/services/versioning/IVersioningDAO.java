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
package com.agiletec.plugins.jpversioning.aps.system.services.versioning;

import java.util.List;

/**
 * @version 1.0
 * @author E.Santoboni
 */
public interface IVersioningDAO {
	
	public List<Long> getVersions(String contentId);
	
	public List<Long> getLastVersions(String contentType, String descr);

	public ContentVersion getVersion(long id);
	
	public ContentVersion getLastVersion(String contentId);
	
	public void addContentVersion(ContentVersion versionRecord);
	
	public void deleteVersion(long versionId);
	
	public void deleteWorkVersions(String contentId, int onlineVersion);
	
}