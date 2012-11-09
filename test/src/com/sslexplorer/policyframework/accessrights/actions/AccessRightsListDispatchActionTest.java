
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
			
package com.sslexplorer.policyframework.accessrights.actions;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import com.sslexplorer.policyframework.AccessRight;
import com.sslexplorer.policyframework.AccessRights;
import com.sslexplorer.policyframework.AccessRightsItem;
import com.sslexplorer.policyframework.DefaultAccessRights;
import com.sslexplorer.policyframework.Permission;
import com.sslexplorer.policyframework.PolicyConstants;
import com.sslexplorer.policyframework.ResourceType;
import com.sslexplorer.policyframework.actions.AbstractResourcesDispatchActionTest;
import com.sslexplorer.policyframework.forms.AccessRightsListForm;
import com.sslexplorer.services.ResourceServiceAdapter;

/**
 */
public class AccessRightsListDispatchActionTest extends AbstractResourcesDispatchActionTest<AccessRights, AccessRightsItem> {
    private final ResourceType<AccessRights> resourceType = PolicyConstants.ACCESS_RIGHTS_RESOURCE_TYPE;

    /**
     * @throws Exception
     */
    public AccessRightsListDispatchActionTest() throws Exception {
        super("", "");
    }

    @Override
    protected void onSetUp() throws Exception {
        super.onSetUp();
        setRequestPath("/accessRightsList");
        setForwardPath(".site.AccessRightsList");
        setEditPath("/editAccessRights");
        setConfirmDeletePath("/confirmRemoveAccessRights");
        setRemovedMessage("access.rights.deleted.message");
        setActionFormClass(AccessRightsListForm.class);
        
        setResourceService(new ResourceServiceAdapter<AccessRights>(resourceType));
    }

    @Override
    protected AccessRights getDefaultResource(int selectedRealmId) {
        AccessRight accessRight = new AccessRight(resourceType, new Permission(1, "policyFramework"));
        List<AccessRight> accessRightsList = Collections.singletonList(accessRight);
        return new DefaultAccessRights(selectedRealmId, -1, "MyNewAccessRight", "A test access right.", accessRightsList,
                        PolicyConstants.PERSONAL_CLASS, Calendar.getInstance(), Calendar.getInstance());
    }

    @Override
    protected int getInitialResourceCount() {
        return 1;
    }

    @Override
    public String getViewPath() {
        return "/viewAccessRights";
    }
}