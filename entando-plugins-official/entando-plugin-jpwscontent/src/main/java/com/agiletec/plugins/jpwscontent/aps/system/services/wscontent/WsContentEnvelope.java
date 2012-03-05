/*
*
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpwscontent.aps.system.services.wscontent;

import com.agiletec.plugins.jpwscontent.aps.system.services.resource.WsResource;

/**
 * Base object used for Content exchange
 * It consists in a string tha rapresent the xml format of the content and eventually
 * an array of resources. 
 */
public class WsContentEnvelope implements java.io.Serializable {
	
	public WsContentEnvelope() { }

	public WsContentEnvelope(String content, WsResource[] resources) {
		this.setContent(content);
		this.setResources(resources);
	}

	public void setContent(String content) {
		this._content = content;
	}
	public String getContent() {
		return _content;
	}

	public void setResources(WsResource[] resources) {
		this._resources = resources;
	}
	public WsResource[] getResources() {
		return _resources;
	}
	
	private String _content;
	private WsResource[] _resources;

}