/*
*
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpversioning.apsadmin.content;

import java.util.List;

import com.agiletec.plugins.jpversioning.aps.system.services.versioning.ContentVersion;

/**
 * @author E.Santoboni
 */
public interface IContentVersionsAction {
	
	public List<Long> getContentVersions();
	
	public ContentVersion getContentVersion(long id);
	
}