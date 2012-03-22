package com.agiletec.plugins.jpcontentfeedback.apsadmin.config;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.ContentFeedbackConfig;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.IContentFeedbackConfig;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.IContentFeedbackManager;

public class ContentFeedbackConfigAction extends BaseAction {
    
    public String edit() {
        try {
            IContentFeedbackConfig config = this.getContentFeedbackManager().getConfig();
            if (null == config) {
                config = new ContentFeedbackConfig();
            }
            this.setConfig((ContentFeedbackConfig) config);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "edit");
            return FAILURE;
        }
        return SUCCESS;
    }
    
    public String update() {
        try {
            this.getContentFeedbackManager().updateConfig(this.getConfig());
            this.addActionMessage(this.getText("jpcontentfeedback.message.config.updated"));
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "update");
            return FAILURE;
        }
        return SUCCESS;
    }
    
    protected IContentFeedbackManager getContentFeedbackManager() {
        return _contentFeedbackManager;
    }
    public void setContentFeedbackManager(IContentFeedbackManager contentFeedbackManager) {
        this._contentFeedbackManager = contentFeedbackManager;
    }
    
    public ContentFeedbackConfig getConfig() {
        return _config;
    }
    public void setConfig(ContentFeedbackConfig config) {
        this._config = config;
    }
    
    private ContentFeedbackConfig _config;
    private IContentFeedbackManager _contentFeedbackManager;
    
}
