package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback;

public interface IContentFeedbackConfig {

	public abstract String getRateContent();

	public abstract void setRateContent(String rateContent);

	public abstract String getComment();

	public abstract void setComment(String comment);

	public abstract String getRateComment();

	public abstract void setRateComment(String rateComment);

	public abstract String getAnonymousComment();

	public abstract void setAnonymousComment(String anonymousComment);

	public String toXML() throws Throwable;

}