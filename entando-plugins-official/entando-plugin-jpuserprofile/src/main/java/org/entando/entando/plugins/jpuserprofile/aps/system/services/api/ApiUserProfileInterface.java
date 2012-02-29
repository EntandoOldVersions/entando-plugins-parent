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

import java.util.Properties;

import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.api.model.BaseApiResponse;
import org.entando.entando.aps.system.services.api.server.IResponseBuilder;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.IUserProfileManager;
import org.entando.entando.plugins.jpuserprofile.aps.system.services.api.model.JAXBUserProfile;

/**
 * @author E.Santoboni
 */
public class ApiUserProfileInterface {

    public JAXBUserProfile getUserProfile(Properties properties) throws ApiException, Throwable {
        JAXBUserProfile jaxbUserProfile = null;
        try {
            //throw new ApiException("xxx", "yyy");
            /*
            String typeCode = properties.getProperty("profileTypeCode");
            IApsEntity masterProfileType = this.getUserProfileManager().getEntityPrototype(typeCode);
            if (null == masterProfileType) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "User Profile type with code '" + typeCode + "' does not exist");
            }
            jaxbProfileType = new JAXBUserProfileType(masterProfileType);
             */
            //} catch (ApiException ae) {
            //    throw ae;
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "getUserProfile");
            throw new ApsSystemException("Error extracting user profile", t);
        }
        return jaxbUserProfile;
    }

    public BaseApiResponse addUserProfileType(JAXBUserProfile jaxbUserProfile) throws Throwable {
        BaseApiResponse response = new BaseApiResponse();
        try {
            /*
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
             */
            response.setResult(IResponseBuilder.SUCCESS, null);
            //} catch (ApiException ae) {
            //    response.addErrors(ae.getErrors());
            //    response.setResult(IResponseBuilder.FAILURE, null);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "addUserProfile");
            throw new ApsSystemException("Error adding user profile", t);
        }
        return response;
    }

    public BaseApiResponse updateUserProfile(JAXBUserProfile jaxbUserProfile) throws Throwable {
        BaseApiResponse response = new BaseApiResponse();
        try {
            /*
            String typeCode = jaxbProfileType.getTypeCode();
            IApsEntity masterProfileType = this.getUserProfileManager().getEntityPrototype(typeCode);
            if (null == masterProfileType) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "User Profile type with code '" + typeCode + "' doesn't exist");
            }
            Map<String, AttributeInterface> attributes = this.getUserProfileManager().getEntityAttributePrototypes();
            IApsEntity profileType = jaxbProfileType.buildEntityType(this.getUserProfileManager().getEntityClass(), attributes);
            ((IEntityTypesConfigurer) this.getUserProfileManager()).updateEntityPrototype(profileType);
             */
            response.setResult(IResponseBuilder.SUCCESS, null);
            //} catch (ApiException ae) {
            //    response.addErrors(ae.getErrors());
            //    response.setResult(IResponseBuilder.FAILURE, null);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "updateUserProfile");
            throw new ApsSystemException("Error updating user profile", t);
        }
        return response;
    }

    public void deleteUserProfile(Properties properties) throws ApiException, Throwable {
        try {
            /*
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
             */
            //} catch (ApiException ae) {
            //    throw ae;
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "deleteUserProfile");
            throw new ApsSystemException("Error deleting user Profile", t);
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