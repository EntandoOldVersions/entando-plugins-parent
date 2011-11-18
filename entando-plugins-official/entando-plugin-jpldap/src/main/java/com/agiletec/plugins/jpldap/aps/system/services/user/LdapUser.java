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
package com.agiletec.plugins.jpldap.aps.system.services.user;

import javax.naming.directory.Attributes;

import com.agiletec.aps.system.services.user.AbstractUser;

/**
 * A UserDetails implementation which is used internally by the Ldap services. 
 * It contains a set of attributes that have been retrieved from the Ldap server.
 * @author E.Santoboni
 */
public class LdapUser extends AbstractUser {
	
	public LdapUser(String userToStringConfig) {
		this._userToStringConfig = userToStringConfig;
	}

	@Override
	public boolean isJapsUser() {
		return false;
	}
	
	@Override
	public String toString() {
		
		
		String[] attrsConf = _userToStringConfig.split(",");
		
		Attributes attrs = this.getAttributes();
		StringBuffer definition = null;
		if (attrs != null && null != attrsConf) {
			definition = new StringBuffer();
			try {
				
				for (int i = 0; i < attrsConf.length; i++) {

					if (i>0) {
						definition.append(" ");
					}
					definition.append(attrs.get(attrsConf[i]).get(0).toString());
					
				}
			} catch (Throwable t) {
				throw new RuntimeException("Errore", t);
			}
		}
		if (null != definition && definition.length() > 0) {
			return definition.toString();		
		}
		
		return super.toString();
	}
    
    /**
     * Crea una copia dell'oggetto user e lo restituisce.
     * @return Oggetto di tipo User clonato.
     */
    public Object clone() {
        LdapUser cl = new LdapUser(this._userToStringConfig);
        cl.setUsername(this.getUsername());
        cl.setPassword("");
        cl.setAuthorities(this.getAuthorities());
        cl.setAttributes(this.getAttributes());
        return cl;
    }
	
    /**
     * Return the attributes that have been retrieved from the Ldap server.
     * @return The ldap Attributes.
     */
	public Attributes getAttributes() {
		return _attributes;
	}
	
	/**
	 * Set the attributes that have been retrieved from the Ldap server.
	 * @param attributes The ldap Attributes.
	 */
	protected void setAttributes(Attributes attributes) {
		this._attributes = attributes;
	}
	
	private Attributes _attributes;
	private String _userToStringConfig;
	
}