/*
*
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpcasclient.aps.system.services.controller.control;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.AssertionImpl;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;

/**
 * Utility for CAS ticket validation
 * 
 * @author G.Cocco
 * */
public class CasClientTicketValidationUtil {

	public CasClientTicketValidationUtil(String urlCasValidate) {
		this._urlCasValidate = urlCasValidate;
	}
	
	/**
	 * ticket validation 
	 * */
	public Assertion validateTicket(String service, String ticket_key) throws ApsSystemException {
		Assertion assertion = null;
		MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
		_client = new HttpClient(connectionManager);
		GetMethod authget = new GetMethod();
		Map<String, String> params = new HashMap<String, String>();
		params.put("service", service);
		params.put("ticket", ticket_key);				
		authget.getParams().setCookiePolicy(CookiePolicy.DEFAULT);
		String responseBodyValidation = null;
		String responseAssertion = null;
		String responseUser = null;
		responseBodyValidation = this.CASgetMethod(authget, _client, this._urlCasValidate, params);				
		try {
			// 	controllo della risposta sulla richiesta validazione ticket
			if ( null != responseBodyValidation && responseBodyValidation.length() > 0 ) {
				InputStreamReader reader;
				reader = new InputStreamReader(authget.getResponseBodyAsStream());
				BufferedReader bufferedReader = new BufferedReader(reader);
				responseAssertion = bufferedReader.readLine();
				if (responseAssertion.equals(_positiveResponse)) {
					responseUser = bufferedReader.readLine();
				}
				_log.info("CasClientTicketValidationUtil - Assertion: " + responseAssertion + " user: " + responseUser);
			}
		} catch (Throwable e) {
			this._log.severe("Error in CasClientTicketValidationUtil - validateTicket");
			throw new ApsSystemException("Errore in CasClientTicketValidationUtil - validateTicket", e);
		}
		if (null != responseAssertion && null != responseUser && responseAssertion.equalsIgnoreCase(_positiveResponse) && responseUser.length() > 0 ) {
			assertion = new AssertionImpl(responseUser);
		}
		return assertion;
	}
	
	/**
	 * Service method for execution of get request. 
	 * */
	public String CASgetMethod(GetMethod authget, HttpClient client, String url, Map<String, String> params) {
		String body = null;
		authget.setPath(url);
		int i = 0;
		if ( null != params && params.size() > 0 ) {
			NameValuePair[] services = new NameValuePair[params.size()];
			for (Iterator iter = params.entrySet().iterator(); iter.hasNext();)	{
				Map.Entry entry = (Map.Entry)iter.next();
				String key = (String)entry.getKey();
				String value = (String)entry.getValue();
				services[i++] = new NameValuePair(key, value);
			}
			authget.setQueryString(services);
		}
		try {
			client.executeMethod(authget);
			body = authget.getResponseBodyAsString();
		} catch (Throwable e) {
			this._log.severe("Error in CasClientTicketValidationUtil - CASgetMethod");
			throw new RuntimeException("Error in CasClientTicketValidationUtil - CASgetMethod", e);
		} finally {
			authget.releaseConnection();
		}
		return body;
	}
	
	private final static String _positiveResponse = "yes";
	private HttpClient _client;
	private String _urlCasValidate;
	private Logger _log = ApsSystemUtils.getLogger();
	
}
