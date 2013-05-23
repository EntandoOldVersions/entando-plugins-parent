package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback;

import com.agiletec.aps.system.exception.ApsSystemException;

public interface IContentFeedbackManager {

	public IContentFeedbackConfig getConfig();

	/**
	 * When true, contents can be commented
	 */
	public abstract boolean isCommentActive();

	/**
	 * If contents can be commented, when true, also guest users can add comment
	 * @return
	 */
	public abstract boolean allowAnonymousComment();

	/**
	 * when <b>true</b> comments should pass through a back office validation before get online
	 * @return
	 */
	public abstract boolean isCommentModerationActive();
	
	
	/**
	 * when <b>true</b> contents can be rated
	 * @return
	 */
	public abstract boolean isRateContentActive();


	/**
	 * when <b>true</b> comments on contents can be rated
	 * @return
	 */
	public abstract boolean isRateCommentActive();


	public void updateConfig(IContentFeedbackConfig config) throws ApsSystemException;

}
