package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback;

import com.agiletec.aps.system.exception.ApsSystemException;

public interface IContentFeedbackManager {

	public IContentFeedbackConfig getConfig();

	public abstract boolean isRateContentActive();

	public abstract boolean isCommentActive();

	public abstract boolean isRateCommentActive();

	public abstract boolean allowAnonymousComment();

	public void updateConfig(IContentFeedbackConfig config) throws ApsSystemException;

}
