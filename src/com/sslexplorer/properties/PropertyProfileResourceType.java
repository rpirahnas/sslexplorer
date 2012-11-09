
				/*
 *  SSL-Explorer
 *
 *  Copyright (C) 2003-2006 3SP LTD. All Rights Reserved
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2 of
 *  the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
			
package com.sslexplorer.properties;

import java.util.Collection;

import com.sslexplorer.core.CoreEvent;
import com.sslexplorer.core.CoreEventConstants;
import com.sslexplorer.core.CoreServlet;
import com.sslexplorer.policyframework.DefaultResourceType;
import com.sslexplorer.policyframework.ResourceChangeEvent;
import com.sslexplorer.security.SessionInfo;

/**
 * Implementation of a {@link com.sslexplorer.policyframework.ResourceType} for
 * <i>Profile Profile</i> and <i>Personal Profile</i> resources.
 */
public class PropertyProfileResourceType extends DefaultResourceType<PropertyProfile> {

    /**
     * Constructor
     * 
     * @param id id
     * @param permissionClass permission class
     */
    public PropertyProfileResourceType(int id, String permissionClass) {
        super(id, "policyframework", permissionClass);
    }

    @Override
    public PropertyProfile createResource(PropertyProfile resource, SessionInfo session) throws Exception {
        return ProfilesFactory.getInstance().createPropertyProfile(resource.getOwnerUsername(), resource.getResourceName(), resource.getResourceDescription(), 0, session.getRealmId());
    }
    
    @Override
    public Collection<PropertyProfile> getResources(SessionInfo session) throws Exception {
        String username = session.getUser().getPrincipalName();
        return ProfilesFactory.getInstance().getPropertyProfiles(username, true, session.getRealmId());
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.navigation.FavoriteResourceType#getResourceById(int)
     */
    public PropertyProfile getResourceById(int resourceId) throws Exception {
        return ProfilesFactory.getInstance().getPropertyProfile(resourceId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.policyframework.DefaultResourceType#getResourceByName(java.lang.String,
     *      com.sslexplorer.security.SessionInfo)
     */
    public PropertyProfile getResourceByName(String resourceName, SessionInfo session) throws Exception {
        return ProfilesFactory.getInstance().getPropertyProfile(null, resourceName, session.getRealmId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.boot.policyframework.ResourceType#removeResource(int,
     *      com.sslexplorer.security.SessionInfo)
     */
    public PropertyProfile removeResource(int resourceId, SessionInfo session) throws Exception {
        try {
            PropertyProfile resource = ProfilesFactory.getInstance().deletePropertyProfile(resourceId);
            CoreServlet.getServlet().fireCoreEvent(
                new ResourceChangeEvent(this, CoreEventConstants.DELETE_PROPERTY_PROFILE, resource, session,
                                CoreEvent.STATE_SUCCESSFUL));
            return resource;
        } catch (Exception e) {
            CoreServlet.getServlet().fireCoreEvent(
                new ResourceChangeEvent(this, CoreEventConstants.DELETE_PROPERTY_PROFILE, null, session,
                                CoreEvent.STATE_UNSUCCESSFUL));
            throw e;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.boot.policyframework.ResourceType#updateResource(com.sslexplorer.boot.policyframework.Resource,
     *      com.sslexplorer.security.SessionInfo)
     */
    public void updateResource(PropertyProfile resource, SessionInfo session) throws Exception {
        try {
            PropertyProfile profile = (PropertyProfile) resource;
            ProfilesFactory.getInstance().updatePropertyProfile(profile.getResourceId(), profile.getResourceName(),
                profile.getResourceDescription());
            CoreServlet.getServlet().fireCoreEvent(
                new ResourceChangeEvent(this, CoreEventConstants.UPDATE_PROPERTY_PROFILE, resource, session,
                                CoreEvent.STATE_SUCCESSFUL));
        } catch (Exception e) {
            CoreServlet.getServlet().fireCoreEvent(
                new ResourceChangeEvent(this, CoreEventConstants.UPDATE_PROPERTY_PROFILE, session, e));
            throw e;
        }
    }
}
