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
package com.agiletec.plugins.jpmyportalplus.aps.internalservlet.standard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Showlet;
import com.agiletec.aps.system.services.showlettype.ShowletType;
import com.agiletec.aps.system.services.url.IURLManager;
import com.agiletec.aps.system.services.user.UserDetails;
import com.agiletec.plugins.jpmyportalplus.aps.internalservlet.AbstractFrontAction;
import com.agiletec.plugins.jpmyportalplus.aps.internalservlet.IFrontAction;
import com.agiletec.plugins.jpmyportalplus.aps.system.JpmyportalplusSystemConstants;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.pagemodel.Frame;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.pagemodel.MyPortalPageModel;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.IPageUserConfigManager;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.ShowletUpdateInfoBean;

/**
 * @author E.Santoboni
 */
public class StandardFrontAction extends AbstractFrontAction implements IFrontAction {
	
	@Override
	public String swapFrames() {
		//System.out.println("Partenza " + this.getStartFramePos() + 
		//		" - ARRIVO  " + this.getTargetFramePos());
		try {
			Showlet[] customShowlets = super.getCustomShowletConfig();
			IPage currentPage = this.getCurrentPage();
			Showlet[] showletsToRender = this.getPageUserConfigManager().getShowletsToRender(currentPage, customShowlets);
			
			Showlet movedShowlet = showletsToRender[this.getStartFramePos()];
			Integer movedShowletStatusInteger = super.getCustomShowletStatus() != null ? super.getCustomShowletStatus()[this.getStartFramePos()] : null;
			int movedShowletStatus = (movedShowletStatusInteger == null) ? 0 : movedShowletStatusInteger;
			ShowletUpdateInfoBean movedShowletUpdateInfo = new ShowletUpdateInfoBean(this.getTargetFramePos(), movedShowlet, movedShowletStatus);
			this.addUpdateInfoBean(movedShowletUpdateInfo);
			
			Showlet showletToMove = showletsToRender[this.getTargetFramePos()];
			Integer showletToMoveStatusInteger = super.getCustomShowletStatus() != null ? super.getCustomShowletStatus()[this.getTargetFramePos()] : null;
			int showletToMoveStatus = (showletToMoveStatusInteger == null) ? 0 : showletToMoveStatusInteger;
			ShowletUpdateInfoBean showletToMoveUpdateInfo = new ShowletUpdateInfoBean(this.getStartFramePos(), showletToMove, showletToMoveStatus);
			this.addUpdateInfoBean(showletToMoveUpdateInfo);
			
			super.executeUpdateUserConfig(currentPage);
			this.updateSessionParams();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "swapFrames", "Error on swapFrame");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String removeFrame() {
		try {
			this.executeResetFrame();
			this.updateSessionParams();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeFrame", "Error on removeFrame");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String addWidgets() {
		try {
			Showlet[] customShowlets = super.getCustomShowletConfig();
			IPage currentPage = this.getCurrentPage();
			Showlet[] showletsToRender = this.getPageUserConfigManager().getShowletsToRender(currentPage, customShowlets);
			
			//for (int i = 0; i < this.getShowletToShow().size(); i++) {
			//	System.out.println("DA MOSTRARE " + this.getShowletToShow().get(i));
			//}
			List<Integer> framesToFlow = this.getFramesToFlow(showletsToRender, currentPage);
			//for (int i = 0; i < framesToFlow.size(); i++) {
			//	System.out.println("DA ELIMINARE - " + framesToFlow.get(i));
			//}
			List<String> showletsToAdd = this.getShowletsToAdd(showletsToRender, currentPage);
			//for (int i = 0; i < showletsToAdd.size(); i++) {
			//	System.out.println("DA AGGIUNGERE + " + showletsToAdd.get(i));
			//}
			
			String voidShowletCode = this.getPageUserConfigManager().getVoidShowlet().getCode();
			Frame[] frames = ((MyPortalPageModel) currentPage.getModel()).getFrameConfigs();
			for (int i = 0; i < frames.length; i++) {
				Frame frame = frames[i];
				if (!frame.isLocked()) {
					boolean isFrameToFlow = framesToFlow.contains(i);
					if (isFrameToFlow) {
						if (showletsToAdd.size()>0) {
							this.addNewWidgetUpdateInfo(showletsToAdd, i, isFrameToFlow);
						} else {
							Showlet showletToInsert = this.getShowletVoid();
							ShowletUpdateInfoBean infoBean = new ShowletUpdateInfoBean(i, showletToInsert, IPageUserConfigManager.STATUS_OPEN);
							this.addUpdateInfoBean(infoBean);
						}
					} else {
						Showlet showlet = showletsToRender[i];
						if ((null == showlet || (null != showlet && showlet.getType().getCode().equals(voidShowletCode))) && showletsToAdd.size()>0) {
							this.addNewWidgetUpdateInfo(showletsToAdd, i, isFrameToFlow);
						}
					}
				}
			}
			this.executeUpdateUserConfig(currentPage);
			this.updateSessionParams();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "addWidgets", "Error on addWidgets");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	protected List<Integer> getFramesToFlow(Showlet[] showletsToRender, IPage currentPage) throws Throwable {
		List<Integer> framesToFlow = new ArrayList<Integer>();
		try {
			String voidShowletCode = this.getPageUserConfigManager().getVoidShowlet().getCode();
			Frame[] frames = ((MyPortalPageModel) currentPage.getModel()).getFrameConfigs();
			for (int i = 0; i < frames.length; i++) {
				Frame frame = frames[i];
				if (!frame.isLocked()) {
					Showlet showlet = showletsToRender[i];
					if (null != showlet && 
							!showlet.getType().getCode().equals(voidShowletCode) &&
							(null == this.getShowletToShow() || !this.getShowletToShow().contains(showlet.getType().getCode()))) {
						framesToFlow.add(i);
					}
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getShowletsToHide", "Error on extracting showlet to hide");
			throw new ApsSystemException("Error on extracting showlet to hide", t);
		}
		return framesToFlow;
	}
	
	protected List<String> getShowletsToAdd(Showlet[] showletsToRender, IPage currentPage) throws Throwable {
		Set<String> showletsToAdd = new HashSet<String>();
		try {
			if (null != this.getShowletToShow()) {
				showletsToAdd.addAll(this.getShowletToShow());
			}
			Frame[] frames = ((MyPortalPageModel) currentPage.getModel()).getFrameConfigs();
			for (int i = 0; i < frames.length; i++) {
				Frame frame = frames[i];
				if (!frame.isLocked()) {
					Showlet showlet = showletsToRender[i];
					if (null != showlet) {
						showletsToAdd.remove(showlet.getType().getCode());
					}
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getShowletsToAdd", "Error on extracting showlet to add");
			throw new ApsSystemException("Error on extracting showlet to add", t);
		}
		List<String> codes = new ArrayList<String>(showletsToAdd);
		Collections.sort(codes);
		return codes;
	}
	
	protected void addNewWidgetUpdateInfo(List<String> showletsToAdd, int framePos, boolean frameToFlow) {
		ShowletUpdateInfoBean infoBean = null;
		Showlet showletToInsert = null;
		String typeCode = showletsToAdd.get(0);
		ShowletType type = this.getShowletTypeManager().getShowletType(typeCode);
		if (null != type) {
			showletsToAdd.remove(typeCode);
			showletToInsert = new Showlet();
			showletToInsert.setType(type);
		}
		if (null != showletToInsert) {
			infoBean = new ShowletUpdateInfoBean(framePos, showletToInsert, IPageUserConfigManager.STATUS_OPEN);
			this.addUpdateInfoBean(infoBean);
		} else if (frameToFlow) {
			infoBean = new ShowletUpdateInfoBean(framePos, this.getShowletVoid(), IPageUserConfigManager.STATUS_OPEN);
			this.addUpdateInfoBean(infoBean);
		}
	}
	
	@Override
	public String resetFrames() {
		try {
			IPage currentPage = this.getCurrentPage();
			UserDetails currentUser = super.getCurrentUser();
			if (currentUser.getUsername().equals(SystemConstants.GUEST_USER_NAME)) {
				this.getPageUserConfigManager().removeGuestPageConfig(currentPage, this.getRequest(), this.getResponse());
			} else {
				this.getPageUserConfigManager().removeUserPageConfig(currentUser.getUsername(), currentPage);
			}
			this.getRequest().getSession().removeAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_USER_PAGE_CONFIG);
			this.getRequest().getSession().removeAttribute(JpmyportalplusSystemConstants.SESSIONPARAM_CURRENT_CUSTOM_PAGE_CONFIG);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "resetFrames", "Error on resetFrames");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String closeFrame() {
		//System.out.println("Frame da Chiudere " + this.getFrameToResize());
		try {
			this.executeCloseFrame();
			this.updateSessionParams();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "closeFrame", "Error on closeFrame");
		}
		return SUCCESS;
	}
	
	@Override
	public String openFrame() {
		//System.out.println("Frame da Aprire " + this.getFrameToResize());
		try {
			this.executeOpenFrame();
			this.updateSessionParams();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "openFrame", "Error on openFrame");
		}
		return SUCCESS;
	}
	
	public String getDestForwardPath() {
		String pathDest = null;
		try {
			Lang currentLanguage = this.getCurrentSessionLang();
			if (null == currentLanguage) {
				currentLanguage = this.getLangManager().getDefaultLang();
			}
			IPage currentPage = this.getCurrentPage();
			String pathDestFirst = this.getUrlManager().createUrl(currentPage, currentLanguage, null);
			pathDest = this.getResponse().encodeURL(pathDestFirst);  
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getDestForwardPath", "Error on extracting destination forward Path");
			throw new RuntimeException("Error on extracting destination forward Path", t);
		}
		return pathDest;
	}
	
	@Override
	public String openSwapSection() {
		throw new RuntimeException("ACTION NOT SUPPORTED - openSwapSection");
	}
	
	public List<String> getShowletToShow() {
		return _showletToShow;
	}
	public void setShowletToShow(List<String> showletToShow) {
		this._showletToShow = showletToShow;
	}
	
	protected IURLManager getUrlManager() {
		return _urlManager;
	}
	public void setUrlManager(IURLManager urlManager) {
		this._urlManager = urlManager;
	}
	
	private List<String> _showletToShow;
	
	private IURLManager _urlManager;
	
}
