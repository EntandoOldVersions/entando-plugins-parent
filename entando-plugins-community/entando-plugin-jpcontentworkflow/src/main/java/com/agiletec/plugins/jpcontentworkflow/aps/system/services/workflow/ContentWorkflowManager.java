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
package com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.role.Permission;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jpcontentworkflow.aps.system.JpcontentworkflowSystemConstants;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.Step;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.Workflow;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.WorkflowSearchFilter;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.parse.ContentWorkflowDOM;

/**
 * @author E.Santoboni
 */
public class ContentWorkflowManager extends AbstractService implements IContentWorkflowManager {
	
	@Override
	public void init() throws Exception {
		this.loadConfig();
		ApsSystemUtils.getLogger().config(this.getName() + ": initialized");
	}
	
	protected void loadConfig() {
		try {
			ConfigInterface configManager = this.getConfigManager();
			String xml = configManager.getConfigItem(JpcontentworkflowSystemConstants.CONTENT_WORKFLOW_CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Configuration item not present: " + JpcontentworkflowSystemConstants.CONTENT_WORKFLOW_CONFIG_ITEM);
			}
			ContentWorkflowDOM configDOM = new ContentWorkflowDOM();
			this.setWorkflowConfig(configDOM.extractConfig(xml));
		} catch (Exception e) {
			ApsSystemUtils.logThrowable(e, this, "loadConfig");
		}
	}
	
	@Override
	public String getRole(String typeCode) {
		Workflow workflow = this.getWorkflow(typeCode);
		return workflow.getRole();
	}
	
	@Override
	public void updateRole(String typeCode, String role) throws ApsSystemException {
		Workflow workflow = this.getWorkflow(typeCode);
		workflow.setRole(role);
		this.updateWorkflow(workflow);
	}
	
	@Override
	public List<Step> getSteps(String typeCode) {
		Workflow workflow = this.getWorkflow(typeCode);
		return workflow.getSteps();
	}
	
	@Override
	public void updateSteps(String typeCode, List<Step> steps) throws ApsSystemException {
		Workflow workflow = this.getWorkflow(typeCode);
		workflow.setSteps(steps);
		this.updateWorkflow(workflow);
	}
	
	@Override
	public Workflow getWorkflow(String typeCode) {
		Workflow workflow = this.getWorkflowConfig().get(typeCode);
		if (workflow == null) {
			workflow = new Workflow();
			workflow.setTypeCode(typeCode);
		}
		return workflow;
	}
	
	protected void updateWorkflow(Workflow workflow) throws ApsSystemException {
		Map<String, Workflow> config = this.getWorkflowConfig();
		config.put(workflow.getTypeCode(), workflow);
		try {
			String xml = new ContentWorkflowDOM().createConfigXml(config);
			this.getConfigManager().updateConfigItem(JpcontentworkflowSystemConstants.CONTENT_WORKFLOW_CONFIG_ITEM, xml);
			this.setWorkflowConfig(config);
		} catch (Exception e) {
			this.loadConfig();
			ApsSystemUtils.logThrowable(e, this, "updateWorkflow");
			throw new ApsSystemException("Error updating workflow for content " + workflow.getTypeCode(), e);
		}
	}
	
	@Override
	public List<String> searchUsedSteps(String typeCode) {
		return this.getWorkflowDAO().searchUsedSteps(typeCode);
	}
	
	@Override
	public List<WorkflowSearchFilter> getWorkflowSearchFilters(UserDetails user) throws ApsSystemException {
		List<WorkflowSearchFilter> filters = new ArrayList<WorkflowSearchFilter>();
		try {
			List<SmallContentType> contentTypes = this.getManagingContentTypes(user);
			boolean isSupervisor = this.getAuthorizationManager().isAuthOnPermission(user, Permission.SUPERVISOR);
			for (int i = 0; i < contentTypes.size(); i++) {
				SmallContentType type = contentTypes.get(i);
				Workflow workflow = this.getWorkflow(type.getCode());
				WorkflowSearchFilter filter = new WorkflowSearchFilter();
				filter.setTypeCode(type.getCode());
				List<String> allowedSteps = this.getAllowedStatus(user, isSupervisor, workflow);
				filter.setAllowedSteps(allowedSteps);
				filters.add(filter);
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getWorkflowSearchFilters");
			throw new ApsSystemException("Error extracting workflow search filters by user " + user, t);
		}
		return filters;
	}
	
	protected List<String> getAllowedStatus(UserDetails user, boolean isSupervisor, Workflow workflow) {
		List<String> allowedSteps = new ArrayList<String>();
		allowedSteps.add(Content.STATUS_NEW);
		allowedSteps.add(Content.STATUS_DRAFT);
		Iterator<Step> stepsIter = workflow.getSteps().iterator();
		while (stepsIter.hasNext()) {
			Step step = stepsIter.next();
			String stepRole = step.getRole();
			if (null == stepRole || stepRole.trim().length() == 0 
					|| this.getAuthorizationManager().isAuthOnRole(user, step.getRole())) {
				allowedSteps.add(step.getCode());
			}
		}
		if (isSupervisor) {
			allowedSteps.add(Content.STATUS_READY);
			allowedSteps.add(Content.STATUS_PUBLIC);
		}
		return allowedSteps;
	}
	
	@Override
	public List<SmallContentType> getManagingContentTypes(UserDetails user) throws ApsSystemException {
		List<SmallContentType> types = new ArrayList<SmallContentType>();
		try {
			List<SmallContentType> contentTypes = this.getContentManager().getSmallContentTypes();
			for (int i = 0; i < contentTypes.size(); i++) {
				SmallContentType contentType = contentTypes.get(i);
				Workflow workflow = this.getWorkflow(contentType.getCode());
				if (null != workflow && null != workflow.getRole()) {
					String roleName = workflow.getRole();
					if (null == roleName || roleName.trim().length() == 0 
							|| this.getAuthorizationManager().isAuthOnRole(user, roleName)) {
						types.add(contentType);
					}
				} else {
					types.add(contentType);
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getManagingContentTypes");
			throw new ApsSystemException("Error extracting managing types by user " + user, t);
		}
		return types;
	}
	
	protected Map<String, Workflow> getWorkflowConfig() {
		return _workflowConfig;
	}
	protected void setWorkflowConfig(Map<String, Workflow> workflowConfig) {
		this._workflowConfig = workflowConfig;
	}
	
	protected IContentWorkflowDAO getWorkflowDAO() {
		return _workflowDAO;
	}
	public void setWorkflowDAO(IContentWorkflowDAO workflowDAO) {
		this._workflowDAO = workflowDAO;
	}
	
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	protected IAuthorizationManager getAuthorizationManager() {
		return _authorizationManager;
	}
	public void setAuthorizationManager(IAuthorizationManager authorizationManager) {
		this._authorizationManager = authorizationManager;
	}
	
	private Map<String, Workflow> _workflowConfig;
	
	private IContentWorkflowDAO _workflowDAO;
	private ConfigInterface _configManager;
	
	private IContentManager _contentManager;
	private IAuthorizationManager _authorizationManager;
	
}