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
package com.agiletec.plugins.jpsurvey.aps.system.services.survey;

import com.agiletec.plugins.jpsurvey.aps.system.services.survey.model.Choice;

public interface IChoiceDAO {

	/**
	 * Load the choice of a survey
	 * @param id the id of the question to load
	 * @return the choice requested, null otherwise
	 */
	public Choice loadChoice(int id);
	
	/**
	 * Save the given choice in the database
	 * @param choice
	 */
	public void saveChoice(Choice choice);
	
	/**
	 * Save the choice in the database selecting automatically the position (thus ignoring the one defined
	 * for the given choice)
	 * @param choice
	 */
	public void saveChoiceInSortedPosition(Choice choice);
	
	/**
	 * Delete a choice
	 * @param id The id of the survey to delete
	 */
	public void deleteChoice(int id);
	
	/**
	 * Updates a single choice in the database. The choice to update is located through its ID
	 * @param choice
	 */
	public void updateChoice(Choice choice);
	
	/**
	 * Delete multiple choice from the database given the ID of the question they belong to 
	 * @param id The id of the question of the choices to delete
	 */
	public void deleteChoiceByQuestionId(int id);
	
	/**
	 * Swaps the position of the given choice with the one closer by position. We can choose to swap the
	 * given choice with the following or preceding one
	 * @param choiceId the ID of the choice to move
	 * @param isUp when true the position of the given choice will be swapped with the one preceding in the
	 *  list, with the following otherwise
	 */
	public void swapChoicePosition(int id, boolean isUp);
	
}
