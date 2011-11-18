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
/**
 * WsContentEndpoint.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.client;

public interface WsContentEndpoint extends java.rmi.Remote {
    public int addContent(com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.client.WsContentEnvelope envelope) throws java.rmi.RemoteException;
    public com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.client.WsContentEnvelope getContent(java.lang.String contentId) throws java.rmi.RemoteException;
}
