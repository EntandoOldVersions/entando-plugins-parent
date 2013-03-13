/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software; 
* you can redistribute it and/or modify it
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
package com.agiletec.plugins.jpmyportalplus.aps.tags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.beanutils.BeanComparator;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.RequestContext;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Showlet;
import com.agiletec.aps.system.services.showlettype.ShowletType;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.pagemodel.Frame;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.pagemodel.MyPortalPageModel;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.IPageUserConfigManager;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.CustomPageConfig;
import com.agiletec.plugins.jpmyportalplus.aps.tags.util.WidgetCheckInfo;

/**
 * Returns the list of widget (in form of {@link WidgetCheckInfo}) to use into the function of page configuration.
 * @author E.Santoboni
 */
public class CustomizableShowletsTag extends TagSupport {
    
    public int doStartTag() throws JspException {
        RequestContext reqCtx = (RequestContext) this.pageContext.getRequest().getAttribute(RequestContext.REQCTX);
        List<WidgetCheckInfo> checkInfos = new ArrayList<WidgetCheckInfo>();
        IPageUserConfigManager pageUserConfigManager = (IPageUserConfigManager) ApsWebApplicationUtils.getBean(JpmyportalplusSystemConstants.PAGE_USER_CONFIG_MANAGER, pageContext);
        try {
            Lang currentLang = (Lang) this.pageContext.getSession().getAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_LANG);
            IPage currentPage = (IPage) reqCtx.getExtraParam(SystemConstants.EXTRAPAR_CURRENT_PAGE);
            Showlet[] customShowletConfig = this.getCustomShowletConfig(currentPage);
            Showlet[] showletsToRender = pageUserConfigManager.getShowletsToRender(currentPage, customShowletConfig);
            List<String> allowedShowlets = new ArrayList<String>();
            Map<String, ShowletType> customizableShowlets = this.getCustomizableShowlets(pageUserConfigManager);
            allowedShowlets.addAll(customizableShowlets.keySet());
            Frame[] frames = ((MyPortalPageModel) currentPage.getModel()).getFrameConfigs();
            for (int i = 0; i < frames.length; i++) {
                Frame frame = frames[i];
                if (!frame.isLocked()) {
                    Showlet showlet = showletsToRender[i];
                    if (null != showlet && allowedShowlets.contains(showlet.getType().getCode())) {
                        WidgetCheckInfo info = new WidgetCheckInfo(showlet.getType(), true, currentLang);
                        allowedShowlets.remove(showlet.getType().getCode());
                        checkInfos.add(info);
                    }
                }
            }
            for (int i = 0; i < allowedShowlets.size(); i++) {
                String code = allowedShowlets.get(i);
                ShowletType type = customizableShowlets.get(code);
                WidgetCheckInfo info = new WidgetCheckInfo(type, false, currentLang);
                checkInfos.add(info);
            }
            BeanComparator comparator = new BeanComparator("title");
            Collections.sort(checkInfos, comparator);
            this.pageContext.setAttribute(this.getVar(), checkInfos);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "doStartTag");
            throw new JspException("Error on doStartTag", t);
        }
        return super.doStartTag();
    }

    protected Showlet[] getCustomShowletConfig(IPage currentPage) throws Throwable {
        Showlet[] customShowlets = null;
        try {
            CustomPageConfig customPageConfig =
                    (CustomPageConfig) this.pageContext.getSession().getAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_PAGE_CONFIG);
            if (customPageConfig != null && !customPageConfig.getPageCode().equals(currentPage.getCode())) {
                throw new RuntimeException("Current page '" + currentPage.getCode()
                        + "' not equals then pageCode of custom config param '" + customPageConfig.getPageCode() + "'");
            }
            if (null != customPageConfig) {
                customShowlets = customPageConfig.getConfig();
            }
        } catch (Throwable t) {
            String message = "Error extracting custom showlets";
            ApsSystemUtils.logThrowable(t, this, "getCustomShowletConfig", message);
            throw new ApsSystemException(message, t);
        }
        return customShowlets;
    }

    private Map<String, ShowletType> getCustomizableShowlets(IPageUserConfigManager pageUserConfigManager) throws ApsSystemException {
        Map<String, ShowletType> map = new HashMap<String, ShowletType>();
        UserDetails currentUser = (UserDetails) this.pageContext.getSession().getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER);
        try {
            List<ShowletType> list = pageUserConfigManager.getCustomizableShowlets(currentUser);
            for (int i = 0; i < list.size(); i++) {
                ShowletType type = list.get(i);
                map.put(type.getCode(), type);
            }
        } catch (Throwable t) {
            String message = "Error extracting customizable Showlets by user '" + currentUser.getUsername() + "'";
            ApsSystemUtils.logThrowable(t, this, "getCustomizableShowlets", message);
            throw new ApsSystemException(message, t);
        }
        return map;
    }
    
    public String getVar() {
        return var;
    }
    public void setVar(String var) {
        this.var = var;
    }
    
    private String var;
    
}