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
import java.util.List;

import org.entando.entando.aps.system.services.api.model.ApiException;
import org.entando.entando.aps.system.services.api.model.BaseApiResponse;
import org.entando.entando.aps.system.services.api.server.IResponseBuilder;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;

import org.entando.entando.plugins.jpuserprofile.aps.system.services.api.model.JAXBUserProfile;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.common.entity.helper.BaseFilterUtils;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.IUserProfileManager;
import com.agiletec.plugins.jpuserprofile.aps.system.services.profile.model.IUserProfile;

/**
 * @author E.Santoboni
 */
public class ApiUserProfileInterface {
    
    public List<String> getUserProfiles(Properties properties) throws Throwable {
        List<String> usernames = null;
        try {
            String userProfileType = properties.getProperty("userProfileType");
            IUserProfile prototype = (IUserProfile) this.getUserProfileManager().getEntityPrototype(userProfileType);
            if (null == prototype) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Profile Type '" + userProfileType + "' does not exist");
            }
            String langCode = properties.getProperty(SystemConstants.API_LANG_CODE_PARAMETER);
            String filtersParam = properties.getProperty("filters");
            BaseFilterUtils filterUtils = new BaseFilterUtils();
            EntitySearchFilter[] filters = filterUtils.getFilters(prototype, filtersParam, langCode);
            usernames = this.getUserProfileManager().searchId(userProfileType, filters);
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "getUserProfiles");
            throw new ApsSystemException("Error searching usernames", t);
        }
        return usernames;
    }
    
    public JAXBUserProfile getUserProfile(Properties properties) throws ApiException, Throwable {
        JAXBUserProfile jaxbUserProfile = null;
        try {
            String username = properties.getProperty("username");
            IUserProfile userProfile = this.getUserProfileManager().getProfile(username);
            if (null == userProfile) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Profile of user '" + username + "' does not exist");
            }
            jaxbUserProfile = new JAXBUserProfile(userProfile, null);
        } catch (ApiException ae) {
            throw ae;
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "getUserProfile");
            throw new ApsSystemException("Error extracting user profile", t);
        }
        return jaxbUserProfile;
    }
    
    public BaseApiResponse addUserProfile(JAXBUserProfile jaxbUserProfile) throws Throwable {
        BaseApiResponse response = new BaseApiResponse();
        try {
            String username = jaxbUserProfile.getId();
            if (null != this.getUserProfileManager().getProfile(username)) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Profile of user '" + username + "' already exist");
            }
            IApsEntity profilePrototype = this.getUserProfileManager().getEntityPrototype(jaxbUserProfile.getTypeCode());
            if (null == profilePrototype) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "User Profile type with code '" + jaxbUserProfile.getTypeCode() + "' does not exist");
            }
            IUserProfile userProfile = (IUserProfile) jaxbUserProfile.buildEntity(profilePrototype, null);
            //TODO VALIDATE
            this.getUserProfileManager().addProfile(username, userProfile);
            response.setResult(IResponseBuilder.SUCCESS, null);
        } catch (ApiException ae) {
            response.addErrors(ae.getErrors());
            response.setResult(IResponseBuilder.FAILURE, null);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "addUserProfile");
            throw new ApsSystemException("Error adding user profile", t);
        }
        return response;
    }

    public BaseApiResponse updateUserProfile(JAXBUserProfile jaxbUserProfile) throws Throwable {
        BaseApiResponse response = new BaseApiResponse();
        try {
            String username = jaxbUserProfile.getId();
            if (null == this.getUserProfileManager().getProfile(username)) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Profile of user '" + username + "' does not exist");
            }
            IApsEntity profilePrototype = this.getUserProfileManager().getEntityPrototype(jaxbUserProfile.getTypeCode());
            if (null == profilePrototype) {
                throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "User Profile type with code '" + jaxbUserProfile.getTypeCode() + "' does not exist");
            }
            IUserProfile userProfile = (IUserProfile) jaxbUserProfile.buildEntity(profilePrototype, null);
            //TODO VALIDATE
            this.getUserProfileManager().updateProfile(username, userProfile);
            response.setResult(IResponseBuilder.SUCCESS, null);
        } catch (ApiException ae) {
            response.addErrors(ae.getErrors());
            response.setResult(IResponseBuilder.FAILURE, null);
        } catch (Throwable t) {
            ApsSystemUtils.logThrowable(t, this, "updateUserProfile");
            throw new ApsSystemException("Error updating user profile", t);
        }
        return response;
    }
    
    public void deleteUserProfile(Properties properties) throws ApiException, Throwable {
        BaseApiResponse response = new BaseApiResponse();
        try {
            String username = properties.getProperty("username");
            IUserProfile userProfile = this.getUserProfileManager().getProfile(username);
            if (null == userProfile) {
                throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Profile of user '" + username + "' does not exist");
            }
            this.getUserProfileManager().deleteProfile(username);
            response.setResult(IResponseBuilder.SUCCESS, null);
        } catch (ApiException ae) {
            response.addErrors(ae.getErrors());
            response.setResult(IResponseBuilder.FAILURE, null);
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