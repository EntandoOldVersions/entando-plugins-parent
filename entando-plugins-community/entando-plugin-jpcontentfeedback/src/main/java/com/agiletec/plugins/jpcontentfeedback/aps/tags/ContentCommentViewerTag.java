/*
*
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2012 Entando s.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpcontentfeedback.aps.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.agiletec.plugins.jpcontentfeedback.aps.system.JpcontentfeedbackSystemConstants;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.ICommentManager;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.IComment;

/**
 * Estrae dal sistema un commento, dato il suo identificativo specificato come parametro
 * @author D.Cherchi
 *
 */
public class ContentCommentViewerTag extends TagSupport{
	 @Override
		public int doStartTag() throws JspException {
			try {
				ICommentManager commentManager = (ICommentManager) ApsWebApplicationUtils.getBean(JpcontentfeedbackSystemConstants.COMMENTS_MANAGER, this.pageContext);
				IComment comment = commentManager.getComment(this.getCommentId());
				this.pageContext.setAttribute(this.getCommentName(), comment);
			} catch (Throwable e) {
				ApsSystemUtils.logThrowable(e, this, "doStartTag");
				throw new JspException("Errore inizializzazione tag", e);
			}
			return EVAL_PAGE;
		}

	public void setCommentId(int commentId) {
		this._commentId = commentId;
	}

	public int getCommentId() {
		return _commentId;
	}

	public void setCommentName(String commentName) {
		this._commentName = commentName;
	}

	public String getCommentName() {
		return _commentName;
	}

	private int _commentId;
	private String _commentName;

}
