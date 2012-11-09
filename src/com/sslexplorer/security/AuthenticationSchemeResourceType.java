package com.sslexplorer.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.sslexplorer.core.CoreAttributeConstants;
import com.sslexplorer.core.CoreEvent;
import com.sslexplorer.core.CoreEventConstants;
import com.sslexplorer.core.CoreServlet;
import com.sslexplorer.policyframework.DefaultResourceType;
import com.sslexplorer.policyframework.PolicyConstants;
import com.sslexplorer.policyframework.PolicyDatabaseFactory;
import com.sslexplorer.policyframework.ResourceChangeEvent;
import com.sslexplorer.policyframework.ResourceDeleteEvent;
import com.sslexplorer.policyframework.ResourceType;

/**
 * Implementation of a {@link com.sslexplorer.policyframework.ResourceType} that
 * is used for configured <i>Authentication Schemes</i>.
 */
public class AuthenticationSchemeResourceType extends DefaultResourceType<AuthenticationScheme> {

    /**
     * Constructor
     */
    public AuthenticationSchemeResourceType() {
        super(PolicyConstants.AUTHENTICATION_SCHEMES_RESOURCE_TYPE_ID, "policyframework", PolicyConstants.SYSTEM_CLASS);
    }

    @Override
    public Collection<AuthenticationScheme> getResources(SessionInfo session) throws Exception {
        return SystemDatabaseFactory.getInstance().getAuthenticationSchemeSequences(session.getRealmId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.navigation.FavoriteResourceType#getResourceById(int)
     */
    public AuthenticationScheme getResourceById(int resourceId) throws Exception {
        return SystemDatabaseFactory.getInstance().getAuthenticationSchemeSequence(resourceId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.policyframework.DefaultResourceType#getResourceByName(java.lang.String,
     *      com.sslexplorer.security.SessionInfo)
     */
    public AuthenticationScheme getResourceByName(String resourceName, SessionInfo session) throws Exception {
        return SystemDatabaseFactory.getInstance().getAuthenticationSchemeSequence(resourceName,
            session.getUser().getRealm().getRealmID());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.boot.policyframework.ResourceType#removeResource(int,
     *      com.sslexplorer.security.SessionInfo)
     */
    public AuthenticationScheme removeResource(int resourceId, SessionInfo session) throws Exception {
        try {
            AuthenticationScheme resource = SystemDatabaseFactory.getInstance().getAuthenticationSchemeSequence(resourceId);
            SystemDatabaseFactory.getInstance().deleteAuthenticationSchemeSequence(resourceId);
            CoreServlet.getServlet().fireCoreEvent(
                new ResourceDeleteEvent(this, CoreEventConstants.DELETE_AUTHENTICATION_SCHEME, resource, session,
                                CoreEvent.STATE_SUCCESSFUL));
            return resource;
        } catch (Exception e) {
            CoreServlet.getServlet().fireCoreEvent(
                new ResourceDeleteEvent(this, CoreEventConstants.DELETE_AUTHENTICATION_SCHEME, null, session,
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
    public void updateResource(AuthenticationScheme resource, SessionInfo session) throws Exception {
        try {
            SystemDatabaseFactory.getInstance().updateAuthenticationSchemeSequence((AuthenticationScheme) resource);
            CoreEvent evt = new ResourceChangeEvent(this, CoreEventConstants.UPDATE_AUTHENTICATION_SCHEME, resource, session,
                            CoreEvent.STATE_SUCCESSFUL);
            int authCounter = 1;
            for (Iterator i = ((AuthenticationScheme) resource).modules(); i.hasNext();) {
                String s = (String) i.next();
                AuthenticationSchemeResourceType.addAuthenticationModule(evt, s, authCounter);
                authCounter++;
            }
            CoreServlet.getServlet().fireCoreEvent(evt);
        } catch (Exception e) {
            CoreServlet.getServlet().fireCoreEvent(
                new ResourceChangeEvent(this, CoreEventConstants.UPDATE_AUTHENTICATION_SCHEME, session, e));
            throw e;
        }
    }

    /**
     * @param evt
     * @param authenticationModule
     * @param position
     */
    public static void addAuthenticationModule(CoreEvent evt, String authenticationModule, int position) {
        evt.addAttribute(CoreAttributeConstants.EVENT_ATTR_AUTHENTICATION_MODULE + position, authenticationModule);
    }

    @Override
    public AuthenticationScheme createResource(AuthenticationScheme resource, SessionInfo session) throws Exception {
        AuthenticationScheme authScheme = (AuthenticationScheme) resource;
        // get the new priority
        int priority = AuthenticationSchemeResourceType.getAuthenticationSchemePriority(session);
        return SystemDatabaseFactory.getInstance().createAuthenticationSchemeSequence(authScheme.getRealmID(),
            authScheme.getResourceName(), authScheme.getResourceDescription(), authScheme.getModules(), authScheme.getEnabled(),
            priority);
    }

    /**
     * Static method to retrieve the next available priority.
     * 
     * @param session
     * @return int the unique priority
     * @throws Exception
     */
    public static int getAuthenticationSchemePriority(SessionInfo session) throws Exception {
        User user = session.getUser();
        ResourceType resourceType = PolicyConstants.AUTHENTICATION_SCHEMES_RESOURCE_TYPE;
        List<Integer> granted = PolicyDatabaseFactory.getInstance().getGrantedResourcesOfType(user, resourceType);

        List<AuthenticationScheme> schemes = new ArrayList<AuthenticationScheme>();
        for (Integer resourceId : granted) {
            AuthenticationScheme scheme = (AuthenticationScheme) resourceType.getResourceById(resourceId.intValue());
            schemes.add(scheme);
        }
        Collections.sort(schemes, new Comparator<AuthenticationScheme>() {
            public int compare(AuthenticationScheme o1, AuthenticationScheme o2) {
                return Math.abs(o1.getPriorityInt()) - Math.abs(o2.getPriorityInt());
            }
        });
        AuthenticationScheme authenticationScheme = schemes.get(schemes.size() - 1);
        return authenticationScheme.getPriorityInt() + 1;
    }
}
