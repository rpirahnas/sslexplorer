
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
			
package com.sslexplorer.policyframework;

import java.util.Collection;

import com.sslexplorer.core.CoreEvent;
import com.sslexplorer.core.CoreEventConstants;
import com.sslexplorer.core.CoreServlet;
import com.sslexplorer.security.SessionInfo;

/**
 * Implementation of a {@link com.sslexplorer.policyframework.ResourceType} for
 * <i>Policy</i> resources.
 */
public class PolicyResourceType extends DefaultResourceType<Policy> {

    /**
     * Constructor
     */
    public PolicyResourceType() {
        super(PolicyConstants.POLICY_RESOURCE_TYPE_ID, "policyframework", PolicyConstants.SYSTEM_CLASS);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.navigation.FavoriteResourceType#getResourceById(int)
     */
    public Policy getResourceById(int resourceId) throws Exception {
        return PolicyDatabaseFactory.getInstance().getPolicy(resourceId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.policyframework.DefaultResourceType#getResourceByName(java.lang.String,
     *      com.sslexplorer.security.SessionInfo)
     */
    public Policy getResourceByName(String resourceName, SessionInfo session) throws Exception {
        return PolicyDatabaseFactory.getInstance().getPolicyByName(resourceName, session.getUser().getRealm().getResourceId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.boot.policyframework.ResourceType#removeResource(int,
     *      com.sslexplorer.security.SessionInfo)
     */
    public Policy removeResource(int resourceId, SessionInfo session) throws Exception {
        try {
            if (resourceId == PolicyDatabaseFactory.getInstance().getEveryonePolicyIDForRealm(session.getUser().getRealm())) {
                throw new Exception("Cannot remove 'Everyone' policy.");
            }
            Policy resource = PolicyDatabaseFactory.getInstance().deletePolicy(resourceId);
            CoreServlet.getServlet().fireCoreEvent(
                addPolicyAttributes(new ResourceDeleteEvent(this, CoreEventConstants.DELETE_POLICY, resource, session,
                                CoreEvent.STATE_SUCCESSFUL), ((Policy) resource)));
            return resource;
        } catch (Exception e) {
            CoreServlet.getServlet().fireCoreEvent(new ResourceDeleteEvent(this, CoreEventConstants.DELETE_POLICY, session, e));
            throw e;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.boot.policyframework.ResourceType#updateResource(com.sslexplorer.boot.policyframework.Resource,
     *      com.sslexplorer.security.SessionInfo)
     */
    public void updateResource(Policy resource, SessionInfo session) throws Exception {
        try {
            PolicyDatabaseFactory.getInstance().updatePolicy((Policy) resource);
            CoreEvent coreEvent = addPolicyAttributes(new ResourceChangeEvent(this, CoreEventConstants.UPDATE_POLICY, resource,
                            session, CoreEvent.STATE_SUCCESSFUL), ((Policy) resource));

            CoreServlet.getServlet().fireCoreEvent(coreEvent);

        } catch (Exception e) {
            CoreServlet.getServlet().fireCoreEvent(new ResourceChangeEvent(this, CoreEventConstants.UPDATE_POLICY, session, e));
            throw e;
        }
    }

    CoreEvent addPolicyAttributes(CoreEvent evt, Policy shortcut) {
        return evt;
    }

    @Override
    public Policy createResource(Policy resource, SessionInfo session) throws Exception {
        Policy policy = (Policy) resource;
        return PolicyDatabaseFactory.getInstance().createPolicy(policy.getResourceName(), policy.getResourceDescription(),
            policy.getType(), policy.getRealmID());
    }

    @Override
    public Collection<Policy> getResources(SessionInfo session) throws Exception {
        return PolicyDatabaseFactory.getInstance().getPolicies(session.getRealm());
    }
}
