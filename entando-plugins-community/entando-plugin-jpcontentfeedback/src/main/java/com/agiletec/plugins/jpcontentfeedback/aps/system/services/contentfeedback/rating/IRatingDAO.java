/*
*
* Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
* This file is part of jAPS software.
* Entando is a free software;
* you can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
*
* See the file License for the specific language governing permissions
* and limitations under the License
*
*
*
* Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
*
*/
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating;

import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model.IRating;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model.IRatingSearchBean;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model.Rating;

public interface IRatingDAO {

	public void addRating(IRating rating);

	public void updateRating(IRating rating);

	public Rating getRating(IRatingSearchBean searchBean);

	public void removeRating(int commentId);

	public void removeContentRating(String contentId);


}
