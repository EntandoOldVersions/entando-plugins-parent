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
package com.agiletec.plugins.jpwscontent.aps.system.services.wscontent;
/**
 * Base interface for the services that must handle remote resources
 */
public interface IWsContentManager {

	/**
	 * Add a new content from a remote call
	 * @param envelope The {@link ApsWsContentEnvelope} containing the xml rapresentation of the content
	 * @throws Throwable if an error occurs
	 */
	public int addContent(WsContentEnvelope envelope) throws Throwable;

	/**
	 * Exports a content.
	 * @param contentId the id of the content to export
	 * @return A new {@link ApsWsContentEnvelope} containing the xml rapresentation of the content an the relate resources
	 * @throws Throwable if an error occurs
	 */
	public WsContentEnvelope getContent(String contentId) throws Throwable;
	
	public static final int RECIVING_OK = 1;
	public static final int RECIVING_NONE = 0;
	public static final int RECIVING_FAILURE = -1;
}
