/*
 *
 * Copyright 2005 AgileTec s.r.l. (http://www.agiletec.it) All rights reserved.
 *
 * This file is part of jAPS software.
 * jAPS is a free software; 
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
package org.entando.entando.plugins.jpuserprofile.aps.system.services.api.response;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.entando.entando.aps.system.services.api.model.AbstractApiResponse;
import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;
import org.entando.entando.aps.system.services.api.model.ListResponse;
import org.entando.entando.plugins.jpuserprofile.aps.system.services.api.model.JAXBUserProfile;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "response")
public class UserProfileResponse extends AbstractApiResponse {
    
    public void setResult(Object result, String html) {
        UserProfileResponseResult responseResult = new UserProfileResponseResult();
        responseResult.setMainResult(result);
        responseResult.setHtml(html);
        this._result = responseResult;
    }
    
    @XmlElement(name = "result", required = true)
    private UserProfileResponseResult _result;
    
    public static class UserProfileResponseResult extends AbstractApiResponseResult {
        
        @XmlElement(name = "userProfile", required = false)
        public JAXBUserProfile getResult() {
            if (this.getMainResult() instanceof JAXBUserProfile) {
                return (JAXBUserProfile) this.getMainResult();
            }
            return null;
        }
        
        @XmlElement(name = "userProfile", required = false)
        public ListResponse getResults() {
            if (this.getMainResult() instanceof Collection) {
                List<JAXBUserProfile> contentTypes = new ArrayList<JAXBUserProfile>();
                contentTypes.addAll((Collection<JAXBUserProfile>) this.getMainResult());
                ListResponse listResponse = new ListResponse(contentTypes) {};
                return listResponse;
            }
            return null;
        }
    }
    
}