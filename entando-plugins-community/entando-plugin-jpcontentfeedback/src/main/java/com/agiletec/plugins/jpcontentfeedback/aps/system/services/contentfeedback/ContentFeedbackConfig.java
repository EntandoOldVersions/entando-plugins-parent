package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "contentFeedbackConfig")
public class ContentFeedbackConfig implements IContentFeedbackConfig {

	public ContentFeedbackConfig() {
		super();
	}

	public ContentFeedbackConfig(String xml) throws Throwable {
		JAXBContext context = JAXBContext.newInstance(ContentFeedbackConfig.class);
		ContentFeedbackConfig config = (ContentFeedbackConfig) context.createUnmarshaller().unmarshal(new StringReader(xml));
		this.setAnonymousComment(config.getAnonymousComment());
		this.setComment(config.getComment());
		this.setRateComment(config.getRateComment());
		this.setRateContent(config.getRateContent());
	}

	public String toXML() throws Throwable {
		JAXBContext context = JAXBContext.newInstance(ContentFeedbackConfig.class);
		StringWriter sw = new StringWriter();
		context.createMarshaller().marshal(this, sw);
		return sw.toString();
	}

	public String getRateContent() {
		return _rateContent;
	}
	public void setRateContent(String rateContent) {
		this._rateContent = rateContent;
	}

	public String getComment() {
		return _comment;
	}
	public void setComment(String comment) {
		this._comment = comment;
	}

	public String getRateComment() {
		return _rateComment;
	}
	public void setRateComment(String rateComment) {
		this._rateComment = rateComment;
	}


	public String getAnonymousComment() {
		return _anonymousComment;
	}
	public void setAnonymousComment(String anonymousComment) {
		this._anonymousComment = anonymousComment;
	}

	private String _rateContent;
	private String _comment;
	private String _rateComment;
	private String _anonymousComment;
}
