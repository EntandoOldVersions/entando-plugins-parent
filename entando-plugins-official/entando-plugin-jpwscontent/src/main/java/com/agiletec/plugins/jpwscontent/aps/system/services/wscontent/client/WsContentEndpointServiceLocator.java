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
 * WsContentEndpointServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.client;

public class WsContentEndpointServiceLocator extends org.apache.axis.client.Service implements com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.client.WsContentEndpointService {

    public WsContentEndpointServiceLocator() {
    }


    public WsContentEndpointServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WsContentEndpointServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WsContentEndpoint
    private java.lang.String WsContentEndpoint_address = "http://localhost:8080/PortalExample206/services/WsContentEndpoint";

    public java.lang.String getWsContentEndpointAddress() {
        return WsContentEndpoint_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WsContentEndpointWSDDServiceName = "WsContentEndpoint";

    public java.lang.String getWsContentEndpointWSDDServiceName() {
        return WsContentEndpointWSDDServiceName;
    }

    public void setWsContentEndpointWSDDServiceName(java.lang.String name) {
        WsContentEndpointWSDDServiceName = name;
    }

    public com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.client.WsContentEndpoint getWsContentEndpoint() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WsContentEndpoint_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWsContentEndpoint(endpoint);
    }

    public com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.client.WsContentEndpoint getWsContentEndpoint(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.client.WsContentEndpointSoapBindingStub _stub = new com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.client.WsContentEndpointSoapBindingStub(portAddress, this);
            _stub.setPortName(getWsContentEndpointWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWsContentEndpointEndpointAddress(java.lang.String address) {
        WsContentEndpoint_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.client.WsContentEndpoint.class.isAssignableFrom(serviceEndpointInterface)) {
                com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.client.WsContentEndpointSoapBindingStub _stub = new com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.client.WsContentEndpointSoapBindingStub(new java.net.URL(WsContentEndpoint_address), this);
                _stub.setPortName(getWsContentEndpointWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("WsContentEndpoint".equals(inputPortName)) {
            return getWsContentEndpoint();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://wscontent.services.system.aps.jpwscontent.plugins.agiletec.com", "WsContentEndpointService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://wscontent.services.system.aps.jpwscontent.plugins.agiletec.com", "WsContentEndpoint"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WsContentEndpoint".equals(portName)) {
            setWsContentEndpointEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
