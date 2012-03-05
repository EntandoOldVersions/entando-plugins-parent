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
package org.entando.entando.plugins.jpuserprofile.aps.system.services.api.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.entando.entando.aps.system.common.entity.api.JAXBEntityType;
import org.entando.entando.aps.system.services.api.model.AbstractApiResponse;
import org.entando.entando.aps.system.services.api.model.AbstractApiResponseResult;
import org.entando.entando.aps.system.services.api.model.ListResponse;
import org.entando.entando.plugins.jpuserprofile.aps.system.services.api.model.JAXBUserProfileType;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "response")
public class UserProfileTypeResponse extends AbstractApiResponse {
    
    public void setResult(Object result, String html) {
        UserProfileTypeResponseResult responseResult = new UserProfileTypeResponseResult();
        responseResult.setMainResult(result);
        responseResult.setHtml(html);
        this._result = responseResult;
    }
    
    @XmlElement(name = "result", required = true)
    private UserProfileTypeResponseResult _result;
    
    public static class UserProfileTypeResponseResult extends AbstractApiResponseResult {
        
        @XmlElement(name = "userProfileType", required = false)
        public JAXBUserProfileType getResult() {
            if (this.getMainResult() instanceof JAXBEntityType) {
                return (JAXBUserProfileType) this.getMainResult();
            }
            return null;
        }
        /*
        @XmlElement(name = "userProfileTypes", required = false)
        public ListResponse getResults() {
        if (this.getMainResult() instanceof Collection) {
        List<JAXBUserProfileType> contentTypes = new ArrayList<JAXBUserProfileType>();
        contentTypes.addAll((Collection<JAXBUserProfileType>) this.getMainResult());
        ListResponse listResponse = new ListResponse(contentTypes){};
        return listResponse;
        }
        return null;
        }
         */
        
        @XmlElement(name = "userProfileTypes", required = false)
        public ListResponse getResults() {
            return null;
        }
        
    }
    
}