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
package com.agiletec.plugins.jpsurvey.aps.system.services.collect;

import java.util.Date;
import java.util.List;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpsurvey.aps.system.services.collect.model.Voter;

public interface IVoterManager {
	
	public Voter getVoter(String username, String ipAddress, int surveyId) throws ApsSystemException;
	
	/**
	 * Get a voter given its ID
	 * @param id the unique identifier of the voter
	 * @return the obeject which describes the voter
	 */
	public Voter getVoterById(int id) throws ApsSystemException;

	/**
	 * Save the given voter to the database.
	 * @param voter the object to save
	 */
	public void saveVoter(Voter voter) throws ApsSystemException;

	/**
	 * Delete a voter identified by the given ID
	 * @param id
	 */
	public void deleteVoterById(int id) throws ApsSystemException;
	
	/**
	 * Delete all the voters which answered the given survey 
	 * @param surveyId
	 */
	public void deleteVoterBySurveyId(int surveyId) throws ApsSystemException;
	
	/**
	 * Search the users matching the given criteria
	 * 
	 * @param id
	 * @param age
	 * @param country
	 * @param sex
	 * @param date
	 * @param surveyId
	 * @param ipAddress
	 * @return a list containing the ID of the users found
	 */
	public List<Integer> searchVotersByIds(Integer id, Integer age, String country, Character sex, 
			Date date, Integer surveyId, String ipAddress) throws ApsSystemException;

}