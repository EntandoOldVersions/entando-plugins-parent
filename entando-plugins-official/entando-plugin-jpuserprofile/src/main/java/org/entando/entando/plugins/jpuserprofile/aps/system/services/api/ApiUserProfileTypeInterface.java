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
package org.entando.entando.plugins.jpuserprofile.aps.system.services.api;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.api.model.BaseApiResponse;
import org.entando.entando.aps.system.services.api.server.IResponseBuilder;

import org.entando.entando.plugins.jpuserprofile.aps.system.services.api.model.JAXBUserProfileType;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.aps.system.common.entity.IEntityTypesConfigurer;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.IUserProfileManager;

/**
 * @author E.Santoboni
 */
public class ApiUserProfileTypeInterface {
    
    public JAXBUserProfileType getUserProfileType(Properties properties) throws ApiException, Throwable {
        JAXBUserProfileType jaxbProfileType = null;
        try {
            String typeCode = properties.getProperty("profileTypeCode");
            IApsEntity masterProfileType = this.getUserProfileManager().getEntityPrototype(typeCode);
            if (null == masterProfileType) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "User Profile type with code '" + typeCode + "' does not exist");
            }
            jaxbProfileType = new JAXBUserProfileType(masterProfileType);
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "getProfileType");
            throw new ApsSystemException("Error extracting user profile type", t);
        }
        return jaxbProfileType;
    }
    
    public BaseApiResponse addUserProfileType(JAXBUserProfileType jaxbProfileType) throws Throwable {
        BaseApiResponse response = new BaseApiResponse();
        try {
            String typeCode = jaxbProfileType.getTypeCode();
            IApsEntity masterProfileType = this.getUserProfileManager().getEntityPrototype(typeCode);
            if (null != masterProfileType) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "User Profile type with code '" + typeCode + "' already exists");
            }
            if (typeCode == null || typeCode.length() != 3) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "Invalid type code - '" + typeCode + "'");
            }
            Map<String, AttributeInterface> attributes = this.getUserProfileManager().getEntityAttributePrototypes();
            IApsEntity profileType = jaxbProfileType.buildEntityType(this.getUserProfileManager().getEntityClass(), attributes);
            ((IEntityTypesConfigurer) this.getUserProfileManager()).addEntityPrototype(profileType);
            response.setResult(IResponseBuilder.SUCCESS, null);
        } catch (ApiException ae) {
            response.addErrors(ae.getErrors());
            response.setResult(IResponseBuilder.FAILURE, null);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "addProfileType");
            throw new ApsSystemException("Error adding user profile type", t);
        }
        return response;
    }
    
    public BaseApiResponse updateUserProfileType(JAXBUserProfileType jaxbProfileType) throws Throwable {
        BaseApiResponse response = new BaseApiResponse();
        try {
            String typeCode = jaxbProfileType.getTypeCode();
            IApsEntity masterProfileType = this.getUserProfileManager().getEntityPrototype(typeCode);
            if (null == masterProfileType) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "User Profile type with code '" + typeCode + "' doesn't exist");
            }
            Map<String, AttributeInterface> attributes = this.getUserProfileManager().getEntityAttributePrototypes();
            IApsEntity profileType = jaxbProfileType.buildEntityType(this.getUserProfileManager().getEntityClass(), attributes);
            ((IEntityTypesConfigurer) this.getUserProfileManager()).updateEntityPrototype(profileType);
            response.setResult(IResponseBuilder.SUCCESS, null);
        } catch (ApiException ae) {
            response.addErrors(ae.getErrors());
            response.setResult(IResponseBuilder.FAILURE, null);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "updateProfileType");
            throw new ApsSystemException("Error updating user profile type", t);
        }
        return response;
    }
    
    public void deleteUserProfileType(Properties properties) throws ApiException, Throwable {
        try {
            String typeCode = properties.getProperty("profileTypeCode");
            IApsEntity masterProfileType = this.getUserProfileManager().getEntityPrototype(typeCode);
            if (null == masterProfileType) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "User Profile type with code '" + typeCode + "' doesn't exist");
            }
            EntitySearchFilter filter = new EntitySearchFilter(IEntityManager.ENTITY_TYPE_CODE_FILTER_KEY, false, typeCode, false);
            List<String> profileIds = this.getUserProfileManager().searchId(new EntitySearchFilter[]{filter});
            if (null != profileIds && !profileIds.isEmpty()) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "User profile type '" + typeCode + "' are used into " + profileIds.size() + " profiles");
            }
            ((IEntityTypesConfigurer) this.getUserProfileManager()).removeEntityPrototype(typeCode);
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "deleteProfileType");
            throw new ApsSystemException("Error deleting user Profile type", t);
        }
    }
    
    protected IUserProfileManager getUserProfileManager() {
        return _userProfileManager;
    }
    public void setUserProfileManager(IUserProfileManager userProfileManager) {
        this._userProfileManager = userProfileManager;
    }
    
    private IUserProfileManager _userProfileManager;
    
}
