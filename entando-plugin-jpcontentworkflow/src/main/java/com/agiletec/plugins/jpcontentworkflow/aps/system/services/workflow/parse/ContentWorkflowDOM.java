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
package com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.parse;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.Step;
import com.agiletec.plugins.jpcontentworkflow.aps.system.services.workflow.model.Workflow;

/**
 * @author E.Santoboni
 */
public class ContentWorkflowDOM {
	
	public Map<String, Workflow> extractConfig(String xml) throws ApsSystemException {
		Map<String,Workflow> config = new HashMap<String, Workflow>();
		Element root = this.getRootElement(xml);
		Iterator<Element> contentTypesIter = root.getChildren(CONTENT_CHILD).iterator();
		while (contentTypesIter.hasNext()) {
			Element contentTypeElem = contentTypesIter.next();
			Workflow workflow = this.extractContentWorkflow(contentTypeElem);
			config.put(workflow.getTypeCode(), workflow);
		}
		return config;
	}
	
	public String createConfigXml(Map<String, Workflow> config) throws ApsSystemException {
		Element root = this.createConfigElement(config);
		Document doc = new Document(root);
		XMLOutputter out = new XMLOutputter();
		Format format = Format.getPrettyFormat();
		format.setIndent("\t");
		out.setFormat(format);
		return out.outputString(doc);
	}
	
	protected Workflow extractContentWorkflow(Element contentTypeElem) {
		Workflow workflow = new Workflow();
		String typeCode = contentTypeElem.getAttributeValue(CONTENT_TYPECODE_ATTR);
		workflow.setTypeCode(typeCode);
		String role = contentTypeElem.getAttributeValue(CONTENT_ROLE_ATTR);
		if (null != role && role.trim().length()>0) {
			workflow.setRole(role.trim());
		}
		Iterator<Element> stepIter = contentTypeElem.getChildren(STEP_CHILD).iterator();
		while (stepIter.hasNext()) {
			Element stepElem = stepIter.next();
			Step step = this.extractStep(stepElem);
			workflow.addStep(step);
		}
		return workflow;
	}
	
	protected Step extractStep(Element stepElem) {
		Step step = new Step();
		String code = stepElem.getAttributeValue(STEP_CODE_ATTR);
		step.setCode(code);
		String descr = stepElem.getAttributeValue(STEP_DESCR_ATTR);
		step.setDescr(descr);
		String role = stepElem.getAttributeValue(STEP_ROLE_ATTR);
		step.setRole(role);
		return step;
	}
	
	protected Element createConfigElement(Map<String,Workflow> config) {
		Element configElem = new Element(ROOT);
		Iterator<Workflow> workflowIter = config.values().iterator();
		while (workflowIter.hasNext()) {
			Workflow workflow = workflowIter.next();
			Element workflowElem = this.createWorkflowElement(workflow);
			configElem.addContent(workflowElem);
		}
		return configElem;
	}
	
	protected Element createWorkflowElement(Workflow workflow) {
		Element workflowElem = new Element(CONTENT_CHILD);
		workflowElem.setAttribute(CONTENT_TYPECODE_ATTR, workflow.getTypeCode());
		String role = workflow.getRole();
		if (role != null && role.trim().length() > 0) {
			workflowElem.setAttribute(CONTENT_ROLE_ATTR, role.trim());
		}
		Iterator<Step> stepsIter = workflow.getSteps().iterator();
		while (stepsIter.hasNext()) {
			Step step = stepsIter.next();
			Element stepElem = this.createStepElement(step);
			workflowElem.addContent(stepElem);
		}
		return workflowElem;
	}
	
	protected Element createStepElement(Step step) {
		Element stepElem = new Element(STEP_CHILD);
		stepElem.setAttribute(STEP_CODE_ATTR, step.getCode());
		stepElem.setAttribute(STEP_DESCR_ATTR, step.getDescr());
		String role = step.getRole();
		if (role!=null) {
			stepElem.setAttribute(STEP_ROLE_ATTR, step.getRole());
		}
		return stepElem;
	}
	
	protected Element getRootElement(String xmlText) throws ApsSystemException {
		SAXBuilder builder = new SAXBuilder();
		builder.setValidation(false);
		StringReader reader = new StringReader(xmlText);
		Element root = null;
		try {
			Document doc = builder.build(reader);
			root = doc.getRootElement();
		} catch (Throwable t) {
			ApsSystemUtils.getLogger().error("Errore nel parsing: " + t.getMessage());
			throw new ApsSystemException("Errore nel parsing della configurazione Dimensioni di resize", t);
		}
		return root;
	}
	
	private static final String ROOT = "contenttypes";
	
	private static final String CONTENT_CHILD = "contenttype";
	private static final String CONTENT_TYPECODE_ATTR = "typecode";
	private static final String CONTENT_ROLE_ATTR = "role";
	
	private static final String STEP_CHILD = "step";
	private static final String STEP_CODE_ATTR = "code";
	private static final String STEP_DESCR_ATTR = "descr";
	private static final String STEP_ROLE_ATTR = "role";
	
}