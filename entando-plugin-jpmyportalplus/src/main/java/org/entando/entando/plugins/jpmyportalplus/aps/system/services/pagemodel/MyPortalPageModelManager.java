/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpmyportalplus.aps.system.services.pagemodel;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * Servizio di gestione dei modelli di pagina.
 * @author M.Diana - E.Santoboni
 */
@Aspect
public class MyPortalPageModelManager extends AbstractService implements IMyPortalPageModelManager {

	private static final Logger _logger = LoggerFactory.getLogger(MyPortalPageModelManager.class);
	
	@Override
	public void init() throws Exception {
		this.loadPageModelConfigurations();
		_logger.debug("{} ready. initialized {} page model configurations", this.getClass().getName() ,this._configuration.size());
	}
	
	@Override
	public Map<Integer, MyPortalFrameConfig> getPageModelConfig(String code) {
		return this._configuration.get(code);
	}
	
	/*
	@AfterReturning(pointcut = "execution(* com.agiletec.aps.system.services.user.IUserManager.getUser(..))", returning = "user")
    public void injectProfile(Object pageModel) {
        if (user != null) {
            AbstractUser userDetails = (AbstractUser) user;
            if (null == userDetails.getProfile()) {
                try {
                    IUserProfile profile = this.getProfile(userDetails.getUsername());
                    userDetails.setProfile(profile);
                } catch (Throwable t) {
                	_logger.error("Error injecting profile on user {}", userDetails.getUsername(), t);
                    //ApsSystemUtils.logThrowable(t, this, "injectProfile", "Error injecting profile on user " + userDetails.getUsername());
                }
            }
        }
    }
	*/
	
	@Before("execution(* com.agiletec.aps.system.services.pagemodel.IPageModelManager.deletePageModel(..)) && args(code)")
    public void deleteProfile(String code) {
        try {
			this.getPageModelDAO().deleteModelConfiguration(code);
			this._configuration.remove(code);
		} catch (Throwable t) {
			_logger.error("Error deleting page model configuration", t);
			throw new RuntimeException("Error deleting page model configuration", t);
		}
    }
	
	private void loadPageModelConfigurations() throws ApsSystemException {
		try {
			this._configuration = this.getPageModelDAO().loadModelConfigs();
		} catch (Throwable t) {
			_logger.error("Error loading page model configurations", t);
			throw new ApsSystemException("Error loading page model configurations", t);
		}
	}
	
	protected IPageModelDAO getPageModelDAO() {
		return _pageModelDao;
	}
	public void setPageModelDAO(IPageModelDAO pageModelDAO) {
		this._pageModelDao = pageModelDAO;
	}
	
	/**
	 * Map dei modelli di pagina configurati nel sistema
	 */
	private Map<String, Map<Integer, MyPortalFrameConfig>> _configuration;
	
	private IPageModelDAO _pageModelDao;
	
}
