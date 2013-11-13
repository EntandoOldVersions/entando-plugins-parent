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
package com.agiletec.plugins.jpmyportalplus.aps.internalservlet.ajax;

import java.util.HashMap;
import java.util.Map;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.plugins.jpmyportalplus.aps.internalservlet.AbstractFrontAction;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.pagemodel.Frame;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.pagemodel.MyPortalPageModel;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.IPageUserConfigManager;
import com.agiletec.plugins.jpmyportalplus.aps.system.services.userconfig.model.WidgetUpdateInfoBean;

/**
 * @author E.Santoboni
 */
public class SpecialFrontFrameSwapperAction extends AbstractFrontAction {

	/**
	 * Una volta individuato il frame di atterraggio, sono quattro i casi riconosciuti:
	 * 1) Atterraggio in posizione vuota; caso più semplice, sostituzione con quella trascinata.
	 * 2) atterraggio in posizione in testa alla colonna in frame occupato;
	 * si trova il primo frame libero e si spostano tutte le showlet dalla prima posizione al primo frame libero trovato.
	 * 3) atterraggio in posizione intermedia occupato:
	 * Se esiste un frame successivo libero si fa una cosa simile al precedente caso
	 * (si trova il primo frame libero e si spostano, incrementando di una posizione, tutte le showlet dalla posizione di destinazione al primo frame libero trovato)
	 * altrimenti si trova il primo frame libero precedente a quello di destinazione e si spostano,
	 * decrementando di una posizione, tutte le showlet dalla posizione di destinazione al primo frame libero trovato).
	 * 4) atterraggio in posizione finale occupata:
	 * si trova il primo frame libero precedente a quello di destinazione e si spostano,
	 * decrementando di una posizione, tutte le showlet dalla posizione di destinazione al primo frame libero trovato.
	 * NOTA : nel caso in cui il frame di partenza sia nella stessa colonna del frame di arrivo, va considerato il farme di partenza cone vuoto
	 */
	@Override
	public String swapFrames() {
		//System.out.println("Partenza " + this.getStartFramePos() +
		//		" - POSIZIONE PREC  " + this.getTargetPrevFramePos() +
		//		" - POSIZIONE SUCC " + this.getTargetNextFramePos());
		if (this.getTargetFramePos() != null) {
			return super.swapFrames();
		}
		try {
			IPage currentPage = this.getCurrentPage();
			if (null == this.getTargetPrevFramePos() && null == this.getTargetNextFramePos()) {
				ApsSystemUtils.getLogger().debug("Page '" + currentPage.getCode() + "' - No swap to do");
				return SUCCESS;
			}
			Widget[] customShowlets = super.getCustomShowletConfig();
			Widget[] showletsToRender = this.getPageUserConfigManager().getShowletsToRender(currentPage, customShowlets);
			MyPortalPageModel pageModel = (MyPortalPageModel) currentPage.getModel();
			Integer[] columnFrames = this.calculateColumnFrames(pageModel);
			boolean isFrameDestBusy = this.calculateFrameDestOnSwapAjax(showletsToRender, columnFrames);
			if (this.getStartFramePos().equals(this.getTargetFramePos())) {
				//System.out.println("SPOSTAMENTO NELLA STESSA POSIZIONE - pos " + this.getStartFramePos());
				return SUCCESS;
			}
			//System.out.println("Frame di destinazione " + this.getTargetFramePos() + " - occupato " + isFrameDestBusy);
			if (!isFrameDestBusy) {
				//frame destinazione non occupato
				WidgetUpdateInfoBean frameStartUpdate =
					new WidgetUpdateInfoBean(this.getStartFramePos(), this.getShowletVoid(), IPageUserConfigManager.STATUS_OPEN);
				this.addUpdateInfoBean(frameStartUpdate);
				WidgetUpdateInfoBean frameTargetUpdate = this.buildShowletToMoveUpdateInfo(showletsToRender);
				this.addUpdateInfoBean(frameTargetUpdate);
			} else {
				//frame destinazione occupato
				this.setShiftingElements(new HashMap<Integer, Integer>());
				Integer nextVoidFrame = this.calculateVoidFramePos(showletsToRender, columnFrames, true);
				Integer prevVoidFrame = null;
				if (null != nextVoidFrame) {
					//si cerca un frame vuoto tra quelli dopo il frame di destinazione
					//System.out.println("PROSSIMO FRAME VUOTO (SU CUI CALCOLARE SWITCH) " + nextVoidFrame);
					this.buildUpdateInfosForSwitch(showletsToRender, columnFrames, true, nextVoidFrame);
				} else {
					//si cerca un frame vuoto tra quelli prima del frame di destinazione
					//ma prima si fa lo switch del frame di destinazione nella posizione precedente (se la destinazione non è l'ultima posizione della colonna)
					if (!columnFrames[columnFrames.length-1].equals(this.getTargetFramePos())) {
						String result = this.setNewTargetFramePosOnSwitchOnTop(columnFrames);
						if (null != result) return result;
					}
					prevVoidFrame = this.calculateVoidFramePos(showletsToRender, columnFrames, false);
					if (null == prevVoidFrame) {
						//System.out.println("SPOSTAMENTO NON POSSIBILE... TUTTI I FRAME DI COLONNA OCCUPATI ");
						return SUCCESS;
					}
					//System.out.println("PRECEDENTE FRAME VUOTO (SU CUI CALCOLARE SWITCH) " + prevVoidFrame);
					this.buildUpdateInfosForSwitch(showletsToRender, columnFrames, false, prevVoidFrame);
				}
			}
			super.executeUpdateUserConfig(currentPage);
			this.updateSessionParams();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "swapFrames", "Error on swapFrame");
			return SUCCESS;
		}
		return SUCCESS;
	}

	private Integer[] calculateColumnFrames(MyPortalPageModel pageModel) throws Throwable {
		//System.out.println("Partenza " + this.getStartFramePos() +
		//		" - POSIZIONE PREC  " + this.getTargetPrevFramePos() +
		//		" - POSIZIONE SUCC " + this.getTargetNextFramePos());
		Integer[] frames = new Integer[0];
		try {
			Integer columnDest = null;
			if (null != this.getTargetPrevFramePos()) {
				columnDest = pageModel.getFrameConfigs()[this.getTargetPrevFramePos()].getColumn();
				if (null != this.getTargetNextFramePos()) {
					Integer columnDestCheck = pageModel.getFrameConfigs()[this.getTargetNextFramePos()].getColumn();
					if (null == columnDestCheck || null == columnDest) {
						throw new RuntimeException("QUALCHE MARCATORE COLONNA NULLO - Colonna '" + columnDest + "' di POSIZIONE PREC  " + this.getTargetPrevFramePos() +
								" - colonna '" + columnDestCheck + "' di POSIZIONE SUCC " + this.getTargetNextFramePos());
					} else if (!columnDest.equals(columnDestCheck)) {
						throw new RuntimeException("Colonna '" + columnDest + "' di POSIZIONE PREC  " + this.getTargetPrevFramePos() +
								" incompatibile con colonna '" + columnDestCheck + "' di POSIZIONE SUCC " + this.getTargetNextFramePos());
					}
				}
			} else {
				columnDest = pageModel.getFrameConfigs()[this.getTargetNextFramePos()].getColumn();
			}
			if (null == columnDest) {
				throw new RuntimeException("MARCATORE COLONNA NULLO di POSIZIONE PREC  " + this.getTargetPrevFramePos() +
						" o di POSIZIONE SUCC " + this.getTargetNextFramePos());
			}
			for (int i = 0; i < pageModel.getFrameConfigs().length; i++) {
				Frame frame = pageModel.getFrameConfigs()[i];
				if (null != frame.getColumn() && frame.getColumn().equals(columnDest)) {
					frames = this.addFrame(frames, i);
					//System.out.println("COLONNA " + columnDest + " - FRAME " + i);
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "calculateColumnFrames", "Error on calculateColumnFrames");
			//throw t;
			return null;
		}
		return frames;
	}

	private Integer[] addFrame(Integer[] frames, Integer frameToAdd) {
		int len = frames.length;
		Integer[] newFrames = new Integer[len + 1];
		for(int i=0; i < len; i++){
			newFrames[i] = frames[i];
		}
		newFrames[len] = frameToAdd;
		return newFrames;
	}

	private boolean calculateFrameDestOnSwapAjax(Widget[] showletsToRender, Integer[] columnFrames) {
		//System.out.println("Partenza " + this.getStartFramePos() +
		//		" - POSIZIONE PREC  " + this.getTargetPrevFramePos() +
		//		" - POSIZIONE SUCC " + this.getTargetNextFramePos());
		Widget showlet = null;
		String voidShowletCode = this.getPageUserConfigManager().getVoidShowlet().getCode();
		try {
			Integer targetFramePos = null;
			if (this.getTargetPrevFramePos() == null) {
				//SPOSTAMENTO IN TESTA ALLA COLONNA
				//primo frame di colonna
				targetFramePos = columnFrames[0];
			} else if (this.getTargetNextFramePos() == null) {
				//SPOSTAMENTO IN CODA ALLA COLONNA
				//primo tra gli ultimi frame di colonna liberi
				Integer prevFrame = null;
				for (int i = 0; i < columnFrames.length; i++) {
					Integer frame = columnFrames[columnFrames.length - i - 1];
					Widget showletToRender = showletsToRender[frame];
					if (i==0 && showletToRender != null && !showletToRender.getType().getCode().equals(voidShowletCode)) {
						//L'ULTIMO FRAME DI COLONNA NON E' LIBERO... è quello il target
						targetFramePos = frame;
						//System.out.println("aaaa " + targetFramePos);
						break;
					}
					if (i==columnFrames.length-1 && (showletToRender == null || showletToRender.getType().getCode().equals(voidShowletCode))) {
						//SI E' ARIVATO IN TESTA SENZA TROVARE UN FRAME OCCUPATO... si mette in testa
						targetFramePos = columnFrames[0];
						//System.out.println("bbb " + targetFramePos);
						break;
					}
					if (frame.equals(this.getStartFramePos())) {
						//SI E' TROVATO IL FRAME DI PARTENZA
						targetFramePos = frame;
						//System.out.println("cccc " + targetFramePos);
						break;
					}
					if (showletToRender != null && !showletToRender.getType().getCode().equals(voidShowletCode)) {
						//SI E' TROVATO UN FRAME OCCUPATO... SI METTE IN QUELLO PRECEDENTE!
						targetFramePos = prevFrame;
						//System.out.println("ddd " + targetFramePos);
						break;
					}
					prevFrame = frame;
				}
			} else {
				//SPOSTAMENTO NEL MEZZO DELLA COLONNA
				if (this.getTargetNextFramePos()-this.getTargetPrevFramePos() == 1) {
					targetFramePos = this.getTargetNextFramePos();
				} else {
					targetFramePos = this.getTargetPrevFramePos() + 1;
				}
			}
			if (null == targetFramePos) {
				throw new RuntimeException("No target frame pos extracted - Start " + this.getStartFramePos() +
					" - Prev pos  " + this.getTargetPrevFramePos() +
					" - Next pos " + this.getTargetNextFramePos());
			}
			this.setTargetFramePos(targetFramePos);
			showlet = showletsToRender[this.getTargetFramePos()];
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "calculateFrameDestOnSwapAjax", "Error on calculateFrameDestOnSwapAjax");
			//throw t;
		}
		if (showlet == null || showlet.getType().getCode().equals(voidShowletCode)) {
			return false;
		} else {
			return true;
		}
	}

	private WidgetUpdateInfoBean buildShowletToMoveUpdateInfo(Widget[] showletsToRender) throws Throwable {
		Widget showletToMove = showletsToRender[this.getStartFramePos()];
		Integer statusShowletToMoveInteger = super.getCustomShowletStatus() != null ? super.getCustomShowletStatus()[this.getStartFramePos()] : null;
		int statusShowletToMove = (statusShowletToMoveInteger == null) ? 0 : statusShowletToMoveInteger;
		WidgetUpdateInfoBean frameTargetUpdate =
			new WidgetUpdateInfoBean(this.getTargetFramePos(), showletToMove, statusShowletToMove);
		//System.out.println("MESSA SHOWLET da spostare in POSIZIONE TARGET " + this.getTargetFramePos());
		return frameTargetUpdate;
	}

	private Integer calculateVoidFramePos(Widget[] showletsToRender, Integer[] columnFrames, boolean next) {
		boolean check = false;
		String voidShowletCode = this.getPageUserConfigManager().getVoidShowlet().getCode();
		for (int i = 0; i < columnFrames.length; i++) {
			Integer framePos = null;
			if (next) {
				framePos = columnFrames[i];
			} else {
				framePos = columnFrames[columnFrames.length - i - 1];
			}
			if (check) {
				Widget showlet = showletsToRender[framePos];
				if (framePos.equals(this.getStartFramePos())
						|| (null == showlet || showlet.getType().getCode().equals(voidShowletCode))) {
					return framePos;
				}
			}
			if (framePos.equals(this.getTargetFramePos())) {
				check = true;
			}
		}
		return null;
	}

	private String setNewTargetFramePosOnSwitchOnTop(Integer[] columnFrames) {
		for (int i = 0; i < columnFrames.length; i++) {
			Integer framePos = columnFrames[i];
			if (i == 0 && framePos.equals(this.getTargetFramePos())) {
				//System.out.println("SPOSTAMENTO NON POSSIBILE IN TESTATA... TUTTI I FRAME DI COLONNA OCCUPATI");
				return SUCCESS;
			} else if (framePos.equals(this.getTargetFramePos())) {
				Integer newTargetFramePos = columnFrames[i-1];
				this.setTargetFramePos(newTargetFramePos);
				//System.out.println("DEFINITO NUOVO TARGET in posizione " + newTargetFramePos);
				break;
			}
		}
		return null;
	}

	private void buildUpdateInfosForSwitch(Widget[] showletsToRender, Integer[] columnFrames, boolean switchAfter, Integer endFramePos) throws Throwable {
		Integer prevFrames = null;
		boolean check = false;
		for (int i = 0; i < columnFrames.length; i++) {
			Integer framePos = null;
			if (switchAfter) {
				framePos = columnFrames[i];
			} else {
				framePos = columnFrames[columnFrames.length - i - 1];
			}
			if (framePos.equals(this.getTargetFramePos())) {
				WidgetUpdateInfoBean frameTargetUpdate = this.buildShowletToMoveUpdateInfo(showletsToRender);
				this.addUpdateInfoBean(frameTargetUpdate);
				check = true;
				prevFrames = framePos;
				continue;
			}
			if (check) {
				Widget showletToSwitch = showletsToRender[prevFrames];
				Integer statusShowletToSwitchInteger = super.getCustomShowletStatus() != null ? super.getCustomShowletStatus()[prevFrames] : null;
				int statusShowletToSwitch = (statusShowletToSwitchInteger == null) ? 0 : statusShowletToSwitchInteger;
				WidgetUpdateInfoBean showletToSwitchUpdateInfo = new WidgetUpdateInfoBean(framePos, showletToSwitch, statusShowletToSwitch);
				//System.out.println("MESSA SHOWLET da switchare da posizione " + prevFrames + " a POSIZIONE " + framePos);
				this.getShiftingElements().put(prevFrames, framePos);
				this.addUpdateInfoBean(showletToSwitchUpdateInfo);
				if (framePos.equals(endFramePos)) {
					//System.out.println("TERMINE SWITCH");
					check = false;
				}
			}
			prevFrames = framePos;
		}
		if (!endFramePos.equals(this.getStartFramePos())) {
			//System.out.println("SVUOTAMENTO FRAME PARTENZA");
			WidgetUpdateInfoBean frameStartUpdate =
				new WidgetUpdateInfoBean(this.getStartFramePos(), this.getShowletVoid(), IPageUserConfigManager.STATUS_OPEN);
			this.addUpdateInfoBean(frameStartUpdate);
		} else {
			//System.out.println("CASO IN CUI IL FRAME DI PARTENZA E' IL DESTINATARIO DI UN FRAME SWITCHATO");
		}
	}

	@Override
	public String removeFrame() {
		throw new RuntimeException("ACTION NOT SUPPORTED - removeFrame");
	}

	@Override
	public String addWidgets() {
		throw new RuntimeException("ACTION NOT SUPPORTED - addWidgets");
	}

	@Override
	public String closeFrame() {
		throw new RuntimeException("ACTION NOT SUPPORTED - closeFrame");
	}

	@Override
	public String openFrame() {
		throw new RuntimeException("ACTION NOT SUPPORTED - openFrame");
	}

	@Override
	public String resetFrames() {
		throw new RuntimeException("ACTION NOT SUPPORTED - resetFrames");
	}

	@Override
	public String openSwapSection() {
		throw new RuntimeException("ACTION NOT SUPPORTED - openSwapSection");
	}

	public Integer getTargetPrevFramePos() {
		return _targetPrevFramePos;
	}
	public void setTargetPrevFramePos(Integer targetPrevFramePos) {
		this._targetPrevFramePos = targetPrevFramePos;
	}

	public Integer getTargetNextFramePos() {
		return _targetNextFramePos;
	}
	public void setTargetNextFramePos(Integer targetNextFramePos) {
		this._targetNextFramePos = targetNextFramePos;
	}

	public Map<Integer, Integer> getShiftingElements() {
		return _shiftingElements;
	}
	public void setShiftingElements(Map<Integer, Integer> shiftingElements) {
		this._shiftingElements = shiftingElements;
	}

	private Integer _targetPrevFramePos;
	private Integer _targetNextFramePos;

	private Map<Integer, Integer> _shiftingElements;

}