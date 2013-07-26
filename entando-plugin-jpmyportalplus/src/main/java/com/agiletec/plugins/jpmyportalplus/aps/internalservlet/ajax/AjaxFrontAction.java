/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando Enterprise Edition software.
* You can redistribute it and/or modify it
* under the terms of the Entando's EULA
*
* See the file License for the specific language governing permissions
* and limitations under the License
*
*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpmyportalplus.aps.internalservlet.ajax;

import java.util.ArrayList;
import java.util.List;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.plugins.jpmyportalplus.aps.internalservlet.AbstractFrontAction;
import com.agiletec.plugins.jpmyportalplus.aps.internalservlet.util.FrameSelectItem;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.pagemodel.Frame;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.pagemodel.MyPortalPageModel;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.CustomPageConfig;

import org.entando.entando.aps.system.services.page.IPage;
import org.entando.entando.aps.system.services.page.Widget;

/**
 * @author E.Santoboni
 */
public class AjaxFrontAction extends AbstractFrontAction {

	@Override
	public String removeFrame() {
		boolean result = false;
		try {
			result = this.executeResetFrame();
			this.updateSessionParams();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeFrame", "Error on removeFrame");
		}
		this.setRemoveResult(String.valueOf(result));
		return SUCCESS;
	}

	@Override
	public String addWidgets() {
		throw new RuntimeException("ACTION NOT SUPPORTED - addWidgets");
	}

	@Override
	public String closeFrame() {
		//System.out.println("Frame to resize " + this.getFrameToResize());
		boolean result = false;
		try {
			result = this.executeCloseFrame();
			this.updateSessionParams();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "closeFrame", "Error on closeFrame");
		}
		this.setResizeResult(String.valueOf(result));
		return SUCCESS;
	}

	@Override
	public String openFrame() {
		//System.out.println("Frame to resize " + this.getFrameToResize());
		boolean result = false;
		try {
			result = this.executeOpenFrame();
			this.updateSessionParams();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "openFrame", "Error on openFrame");
		}
		this.setResizeResult(String.valueOf(result));
		return SUCCESS;
	}

	@Override
	public String resetFrames() {
		throw new RuntimeException("ACTION NOT SUPPORTED - resetFrames");
	}

	@Override
	public String openSwapSection() {
		//System.out.println("Frame where opens section " + this.getFrameWhereOpenSection());
		List<FrameSelectItem> selectItems = new ArrayList<FrameSelectItem>();
		this.setSelectItems(selectItems);
		try {
			IPage currentPage = this.getCurrentPage();
			MyPortalPageModel pageModel = (MyPortalPageModel) currentPage.getModel();
			Integer currentColumnId = pageModel.getFrameConfigs()[this.getFrameWhereOpenSection()].getColumn();
			if (null == currentColumnId) {
				return SUCCESS;
			}
			CustomPageConfig config = this.getCustomPageConfig();
			org.entando.entando.aps.system.services.page.Widget[] customShowlets = (null == config || config.getConfig() == null) ? null : config.getConfig();
			org.entando.entando.aps.system.services.page.Widget[] showletsToRender = this.getPageUserConfigManager().getShowletsToRender(currentPage, customShowlets);
			Lang currentLang = this.getCurrentLang();
			String voidShowletCode = this.getPageUserConfigManager().getVoidShowlet().getCode();
			for (int i = 0; i < showletsToRender.length; i++) {
				Frame frame = pageModel.getFrameConfigs()[i];
				Integer columnId = frame.getColumn();
				if (frame.isLocked() || null == columnId || i == this.getFrameWhereOpenSection().intValue()) continue;
				org.entando.entando.aps.system.services.page.Widget showlet = showletsToRender[i];
				if (columnId.equals(currentColumnId)) {
					if (showlet != null && !showlet.getType().getCode().equals(voidShowletCode)) {
						FrameSelectItem item = new FrameSelectItem(currentColumnId, columnId, showlet, i, currentLang);
						selectItems.add(item);
					}
				} else {
					if (showlet == null || showlet.getType().getCode().equals(voidShowletCode)) {
						boolean check = this.check(columnId);
						if (!check) {
							FrameSelectItem item = new FrameSelectItem(currentColumnId, columnId, null, i, currentLang);
							selectItems.add(item);
						}
					}
				}
				if (i == this.getFrameWhereOpenSection() && null != showlet) {
					this.setShowletCodeOnOpeningSection(showlet.getType().getCode());
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "openSwapSection", "Error on opening Swap Section");
		}
		return SUCCESS;
	}

	private boolean check(Integer columnId) {
		for (int i=0; i<this.getSelectItems().size(); i++) {
			FrameSelectItem frameSelectItem = this.getSelectItems().get(i);
			if (columnId.equals(frameSelectItem.getColumnIdDest())) {
				return true;
			}
		}
		return false;
	}

	public String getRemoveResult() {
		return _removeResult;
	}
	protected void setRemoveResult(String removeResult) {
		this._removeResult = removeResult;
	}

	public String getResizeResult() {
		return _resizeResult;
	}
	protected void setResizeResult(String resizeResult) {
		this._resizeResult = resizeResult;
	}

	public Integer getFrameWhereOpenSection() {
		return _frameWhereOpenSection;
	}
	public void setFrameWhereOpenSection(Integer frameWhereOpenSection) {
		this._frameWhereOpenSection = frameWhereOpenSection;
	}

	public List<FrameSelectItem> getSelectItems() {
		return _selectItems;
	}
	protected void setSelectItems(List<FrameSelectItem> selectItems) {
		this._selectItems = selectItems;
	}

	public String getShowletCodeOnOpeningSection() {
		return _showletCodeOnOpeningSection;
	}
	protected void setShowletCodeOnOpeningSection(String showletCodeOnOpeningSection) {
		this._showletCodeOnOpeningSection = showletCodeOnOpeningSection;
	}

	private String _removeResult;

	private String _resizeResult;

	private Integer _frameWhereOpenSection;
	private List<FrameSelectItem> _selectItems;
	private String _showletCodeOnOpeningSection;

}
