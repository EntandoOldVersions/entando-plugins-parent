package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback;

public interface IContentFeedbackConfig {

    /**
     * when <b>true</b> contents can be commented
     */
	public abstract String getComment();

    /**
     * when <b>true</b> contents can be commented
     */
	public abstract void setComment(String comment);

    /**
     * when <b>true</b>, and only when the param <i>comment</i> is <i>true</i>, contents contents can be commented by guest users
     */
	public abstract String getAnonymousComment();
	
    /**
     * when <b>true</b>, and only when the param <i>comment</i> is <i>true</i>, contents contents can be commented by guest users
     */
	public abstract void setAnonymousComment(String anonymousComment);
	

    /**
     * when <b>true</b> comments should pass through a back office validation before get online
     */
    public String getModeratedComment();
	
    /**
     * when <b>true</b> comments should pass through a back office validation before get online
     */
    public void setModeratedComment(String moderatedComment);	
	
	/**
	 * when <b>true</b> contents can be rated
	 */
	public abstract String getRateContent();

	/**
	 * when <b>true</b> contents can be rated
	 */
	public abstract void setRateContent(String rateContent);
	
	/**
	 * when <b>true</b> comments can be rated
	 */
	public abstract String getRateComment();
	
	/**
	 * when <b>true</b> comments can be rated
	 */
	public abstract void setRateComment(String rateComment);


	public String toXML() throws Throwable;

}