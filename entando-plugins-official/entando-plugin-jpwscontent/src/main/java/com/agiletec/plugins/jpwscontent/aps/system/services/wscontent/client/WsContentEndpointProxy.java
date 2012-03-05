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
package com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.client;

public class WsContentEndpointProxy implements com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.client.WsContentEndpoint {
  private String _endpoint = null;
  private com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.client.WsContentEndpoint wsContentEndpoint = null;
  
  public WsContentEndpointProxy() {
    _initWsContentEndpointProxy();
  }
  
  public WsContentEndpointProxy(String endpoint) {
    _endpoint = endpoint;
    _initWsContentEndpointProxy();
  }
  
  private void _initWsContentEndpointProxy() {
    try {
      wsContentEndpoint = (new com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.client.WsContentEndpointServiceLocator()).getWsContentEndpoint();
      if (wsContentEndpoint != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)wsContentEndpoint)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)wsContentEndpoint)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (wsContentEndpoint != null)
      ((javax.xml.rpc.Stub)wsContentEndpoint)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.client.WsContentEndpoint getWsContentEndpoint() {
    if (wsContentEndpoint == null)
      _initWsContentEndpointProxy();
    return wsContentEndpoint;
  }
  
  public int addContent(com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.client.WsContentEnvelope envelope) throws java.rmi.RemoteException{
    if (wsContentEndpoint == null)
      _initWsContentEndpointProxy();
    return wsContentEndpoint.addContent(envelope);
  }
  
  public com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.client.WsContentEnvelope getContent(java.lang.String contentId) throws java.rmi.RemoteException{
    if (wsContentEndpoint == null)
      _initWsContentEndpointProxy();
    return wsContentEndpoint.getContent(contentId);
  }
  
  
}