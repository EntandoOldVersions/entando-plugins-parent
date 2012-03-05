/*
 *
 * Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
 * Copyright 2012 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 */
package com.agiletec.plugins.jpphotogallery.aps.tags;

import java.util.Collection;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.tags.PagerTag;
import com.agiletec.aps.tags.util.IPagerVO;
import com.agiletec.aps.tags.util.PagerTagHelper;
import com.agiletec.plugins.jpphotogallery.aps.tags.util.PhotogalleryPagerTagHelper;

public class PhotogalleryPagerTag extends PagerTag {
	
	@Override
	public int doStartTag() throws JspException {
		ServletRequest request =  this.pageContext.getRequest();
		try {
			Collection object = (Collection) this.pageContext.getAttribute(this.getListName());
			if (object == null) {
				ApsSystemUtils.getLogger().severe("Non c'è nessuna lista nella request");
			} else {
				PagerTagHelper helper = new PhotogalleryPagerTagHelper();
				IPagerVO pagerVo = helper.getPagerVO(object, this.getPagerId(), 
						this.isPagerIdFromFrame(), this.getMax(),  this.isAdvanced(), this.getOffset(), request);
				this.pageContext.setAttribute(this.getObjectName(), pagerVo);
			}
		} catch (Throwable e) {
			ApsSystemUtils.logThrowable(e, this, "doStartTag");
			throw new JspException("Errore inizializzazione tag", e);
		}
		return EVAL_BODY_INCLUDE;
	}
	
}