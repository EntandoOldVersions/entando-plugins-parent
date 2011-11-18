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
 * WsContentEnvelope.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.agiletec.plugins.jpwscontent.aps.system.services.wscontent.client;

public class WsContentEnvelope  implements java.io.Serializable {
    private java.lang.String content;

    private com.agiletec.plugins.jpwscontent.aps.system.services.resource.WsResource[] resources;

    public WsContentEnvelope() {
    }

    public WsContentEnvelope(
           java.lang.String content,
           com.agiletec.plugins.jpwscontent.aps.system.services.resource.WsResource[] resources) {
           this.content = content;
           this.resources = resources;
    }


    /**
     * Gets the content value for this WsContentEnvelope.
     * 
     * @return content
     */
    public java.lang.String getContent() {
        return content;
    }


    /**
     * Sets the content value for this WsContentEnvelope.
     * 
     * @param content
     */
    public void setContent(java.lang.String content) {
        this.content = content;
    }


    /**
     * Gets the resources value for this WsContentEnvelope.
     * 
     * @return resources
     */
    public com.agiletec.plugins.jpwscontent.aps.system.services.resource.WsResource[] getResources() {
        return resources;
    }


    /**
     * Sets the resources value for this WsContentEnvelope.
     * 
     * @param resources
     */
    public void setResources(com.agiletec.plugins.jpwscontent.aps.system.services.resource.WsResource[] resources) {
        this.resources = resources;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof WsContentEnvelope)) return false;
        WsContentEnvelope other = (WsContentEnvelope) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.content==null && other.getContent()==null) || 
             (this.content!=null &&
              this.content.equals(other.getContent()))) &&
            ((this.resources==null && other.getResources()==null) || 
             (this.resources!=null &&
              java.util.Arrays.equals(this.resources, other.getResources())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getContent() != null) {
            _hashCode += getContent().hashCode();
        }
        if (getResources() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getResources());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getResources(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(WsContentEnvelope.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://wscontent.services.system.aps.jpwscontent.plugins.agiletec.com", "WsContentEnvelope"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("content");
        elemField.setXmlName(new javax.xml.namespace.QName("http://wscontent.services.system.aps.jpwscontent.plugins.agiletec.com", "content"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("resources");
        elemField.setXmlName(new javax.xml.namespace.QName("http://wscontent.services.system.aps.jpwscontent.plugins.agiletec.com", "resources"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://resource.services.system.aps.jpwscontent.plugins.agiletec.com", "WsResource"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("http://wscontent.services.system.aps.jpwscontent.plugins.agiletec.com", "item"));
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
