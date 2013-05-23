/*
 *
 * Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
 * Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 */
package com.agiletec.plugins.jpsurvey.aps.system.services.collect;

import java.util.Date;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.Voter;

public class VoterManager extends AbstractService implements IVoterManager {
	
	@Override
	public void init() throws Exception {
		ApsSystemUtils.getLogger().config(this.getClass().getName() + ": initiated ");		
	}
	
	@Override
	public Voter getVoter(String username, String ipAddress, int surveyId) throws ApsSystemException {
		Voter voter = null;
		try {
			voter = this.getVoterDAO().getVoter(username, ipAddress, surveyId);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getVoter");
            throw new ApsSystemException("Errore getting the voter " + username + " by IP "+ ipAddress + " and idSurvey " + surveyId, t);
		}
		return voter;
	}
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.aps.system.services.collect.IVoterManager#getVoterById(int)
	 */
	public Voter getVoterById(int id) throws ApsSystemException {
		Voter voter=null;
		try {
			voter=this.getVoterDAO().getVoterById(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getVoterById");
            throw new ApsSystemException("Error finding the voter of ID "+id, t);
		}
		return voter;
	}
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.aps.system.services.collect.IVoterManager#saveVoter(com.agiletec.plugins.jpsurvey.aps.system.services.collect.Voter)
	 */
	public void saveVoter(Voter voter) throws ApsSystemException {
		try {
			this.getVoterDAO().saveVoter(voter);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "saveVoter");
            throw new ApsSystemException("Error registering new voter", t);
		} 
	}
	
	/* (non-Javadoc)
	 * @see com.agiletec.plugins.jpsurvey.aps.system.services.collect.IVoterManager#deleteVoterById(int)
	 */
	public void deleteVoterById(int id) throws ApsSystemException {
		try {
			this.getVoterDAO().deleteVoterById(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteVoterById");
            throw new ApsSystemException("Error deleting voter ID "+id, t);
		} 
	}
	
	public void deleteVoterBySurveyId(int id) throws ApsSystemException {
		try {
			this.getVoterDAO().deleteVoterBySurveyId(id);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "deleteVoterBySurveyId");
            throw new ApsSystemException("Errore deleting voters associated to survey ID "+id, t);
		} 
	}
	
	public List<Integer> searchVotersByIds(Integer id, Integer age, String country, Character sex, 
			Date date, Integer surveyId, String ipAddress) throws ApsSystemException {
		List<Integer> list=null;
		try {
			list = this.getVoterDAO().searchVotersByIds(id, age, country, sex, date, surveyId, ipAddress);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getVotersByIds");
            throw new ApsSystemException("Error searching voters", t);
		}
		return list;
	}
	
	@Override
	public List<Integer> searchVoters(FieldSearchFilter[] filters) throws ApsSystemException {
		List<Integer> list=null;
		try {
			list = this.getVoterDAO().searchVotersId(filters);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "searchVoters");
            throw new ApsSystemException("Error searching voters", t);
		}
		return list;
	}
	
	public void setVoterDAO(IVoterDAO voterDAO) {
		this._voterDAO = voterDAO;
	}
	protected IVoterDAO getVoterDAO() {
		return _voterDAO;
	}

	private IVoterDAO _voterDAO;
}
