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
package com.agiletec.plugins.jpnewsletter.apsadmin.subscriber;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.INewsletterManager;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.Subscriber;

/**
 * Classe per la ricerca dei sottoscritti al servizio generico di newsletter in
 * Back-End.
 * 
 * @author A.Turrini
 */
public class SubscriberFinderAction extends BaseAction implements ISubscriberFinderAction {
	
	public List<Subscriber> getSubscribers() {
		List<Subscriber> subscribers = new ArrayList<Subscriber>();
		try {
			Integer active = this.getInsertedActive();
			Boolean boolActive = active == null ? null : active.intValue()==1;
			subscribers = this.getNewsletterManager().searchSubscribers(this.getInsertedMailAddress(), boolActive);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getSubscribers");
		}
		return subscribers;
	}
	
	public String getInsertedMailAddress() {
		return _insertedMailAddress;
	}
	public void setInsertedMailAddress(String insertedMailAddress) {
		_insertedMailAddress = insertedMailAddress;
	}
	
	public Integer getInsertedActive() {
		return _insertedActive;
	}
	public void setInsertedActive(Integer insertedActive) {
		_insertedActive = insertedActive;
	}
	
	protected INewsletterManager getNewsletterManager() {
		return _newsletterManager;
	}
	public void setNewsletterManager(INewsletterManager newsletterManager) {
		_newsletterManager = newsletterManager;
	}
	
	private String _insertedMailAddress;
	private Integer _insertedActive;
	
	private INewsletterManager _newsletterManager;
	
}