package com.sslexplorer.realms;

import com.sslexplorer.policyframework.DefaultResourceType;
import com.sslexplorer.policyframework.PolicyConstants;

/**
 * Implementation of a {@link com.sslexplorer.policyframework.ResourceType} for
 * <i>Realms</i> resources.
 */
public class DefaultRealmResourceType extends DefaultResourceType {

    /**
     * Constructor
     */
    public DefaultRealmResourceType() {
        super(PolicyConstants.REALM_RESOURCE_TYPE_ID, "realms", PolicyConstants.DELEGATION_CLASS);
    }

}
