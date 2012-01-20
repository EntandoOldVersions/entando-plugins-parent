/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/index.html
 */
package com.agiletec.plugins.jpcasclient.aps.filter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.jasig.cas.client.session.HashMapBackedSessionMappingStorage;
import org.jasig.cas.client.session.SessionMappingStorage;
import org.jasig.cas.client.util.CommonUtils;
import org.jasig.cas.client.util.XmlUtils;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpcasclient.CasClientPluginSystemCostants;
import com.agiletec.plugins.jpcasclient.aps.system.services.config.ICasClientConfigManager;

/**
 * Implements the Single Sign Out protocol. It handles registering the session and destroying the session.
 * Zuanni's change setting encodnig to UTF8 and minor refactorings like managing plugin disactivation config
 * parameter and jAPs style logs.
 * @author Scott Battaglia
 * @author zuanni
 * @version $Revision$ $Date$
 * @since 3.1
 */
public class CasSingleSignOutFilter implements Filter {

	public void init(final FilterConfig filterConfig) throws ServletException {
		_log = ApsSystemUtils.getLogger();
	}

	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse, final FilterChain filterChain) throws IOException, ServletException {
		final HttpServletRequest request = (HttpServletRequest) servletRequest;
		request.setCharacterEncoding("UTF-8");
		ICasClientConfigManager configManager = (ICasClientConfigManager) ApsWebApplicationUtils.getBean(CasClientPluginSystemCostants.JPCASCLIENT_CONFIG_MANAGER, request);
		boolean isActive = configManager.getClientConfig().isActive();
		if (isActive) {
			if ("POST".equals(request.getMethod())) {
				final String logoutRequest = request.getParameter("logoutRequest");

				if (CommonUtils.isNotBlank(logoutRequest)) {

					if(_log.isLoggable(Level.FINE)){
						_log.fine("Logout request=[" + logoutRequest + "]");
					}

					final String sessionIdentifier = XmlUtils.getTextForElement(logoutRequest, "SessionIndex");

					if (CommonUtils.isNotBlank(sessionIdentifier)) {
						final HttpSession session = SESSION_MAPPING_STORAGE.removeSessionByMappingId(sessionIdentifier);

						if (session != null) {
							String sessionID = session.getId();

							if (_log.isLoggable(Level.FINE)) {
								_log.fine("Invalidating session [" + sessionID + "] for ST [" + sessionIdentifier + "]");
							}

							try {
								session.invalidate();
							} catch (final IllegalStateException e) {
								ApsSystemUtils.logThrowable(e, this, "doFilter");
							}
						}
						return;
					}
				}
			} else {
				final String artifact = request.getParameter(this._artifactParameterName);
				final HttpSession session = request.getSession();

				if (_log.isLoggable(Level.FINE) && session != null) {
					_log.fine("Storing session identifier for " + session.getId());
				}
				if (CommonUtils.isNotBlank(artifact)) {
					try {
						SESSION_MAPPING_STORAGE.removeBySessionById(session.getId());
					} catch (final Exception e) {
						// ignore if the session is already marked as invalid.  Nothing we can do!
					}
					SESSION_MAPPING_STORAGE.addSessionById(artifact, session);
				}
			}
		}
		filterChain.doFilter(servletRequest, servletResponse);
	}

	public void setSessionMappingStorage(final SessionMappingStorage storage) {
		SESSION_MAPPING_STORAGE = storage;
	}

	public static SessionMappingStorage getSessionMappingStorage() {
		return SESSION_MAPPING_STORAGE;
	}

	public void destroy() {
		// nothing to do
	}

	/**
	 * The name of the artifact parameter.  This is used to capture the session identifier.
	 */
	private final static String _artifactParameterName = "ticket";

	private static SessionMappingStorage SESSION_MAPPING_STORAGE = new HashMapBackedSessionMappingStorage();

	private static Logger _log;

}