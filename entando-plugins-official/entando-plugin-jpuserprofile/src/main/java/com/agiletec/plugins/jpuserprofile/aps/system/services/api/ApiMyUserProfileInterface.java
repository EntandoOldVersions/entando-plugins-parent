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
package com.agiletec.plugins.jpuserprofile.aps.system.services.api;

import java.util.Properties;

import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.user.UserDetails;

/**
 * @author E.Santoboni
 */
public class ApiMyUserProfileInterface {
    
    public JAXBUserDetails getMyUserProfile(Properties properties) throws ApiException, Throwable {
        try {
            UserDetails userDetail = (UserDetails) properties.get(SystemConstants.API_USER_PARAMETER);
            if (null == userDetail) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Null user");
            }
            return new JAXBUserDetails(userDetail);
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "getMyUserProfile");
            throw new ApsSystemException("Error extracting userprofile", t);
        }
    }
    
}
