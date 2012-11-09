
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
			
package com.sslexplorer.policyframework.itemactions;

import com.sslexplorer.policyframework.NoPermissionException;
import com.sslexplorer.policyframework.Permission;
import com.sslexplorer.policyframework.PolicyConstants;
import com.sslexplorer.policyframework.Resource;
import com.sslexplorer.policyframework.ResourceItem;
import com.sslexplorer.policyframework.ResourceUtil;
import com.sslexplorer.table.AvailableTableItemAction;

/**
 */
public class EnableResourceAction extends AbstractPathAction {
    /**
     */
    public static final String TABLE_ITEM_ACTION_ID = "enable";

    /**
     * Constructor
     * @param navigationContext
     * @param messageResourcesKey
     */
    public EnableResourceAction(int navigationContext, String messageResourcesKey) {
        this(navigationContext, messageResourcesKey, "{2}.do?actionTarget=confirmRemove&selectedResource={0}");
    }

    /**
     * @param navigationContext
     * @param messageResourcesKey
     * @param requiredPath
     */
    public EnableResourceAction(int navigationContext, String messageResourcesKey, String requiredPath) {
        super(TABLE_ITEM_ACTION_ID, messageResourcesKey, 150, false, navigationContext, requiredPath);
    }

    /*
     * (non-Javadoc)
     * @see com.sslexplorer.table.TableItemAction#isEnabled(com.sslexplorer.table.AvailableTableItemAction)
     */
    public boolean isEnabled(AvailableTableItemAction availableItem) {
        try {
            ResourceItem item = (ResourceItem) availableItem.getRowItem();
            Permission[] permissions = new Permission[] { PolicyConstants.PERM_CREATE_EDIT_AND_ASSIGN, PolicyConstants.PERM_EDIT_AND_ASSIGN, PolicyConstants.PERM_ASSIGN };
            ResourceUtil.checkResourceManagementRights(item.getResource(), availableItem.getSessionInfo(), permissions);
            return isEnabled(item.getResource());
        } catch (NoPermissionException e) {
            return false;
        }
    }
    
    /**
     * @param resource
     * @return boolean
     */
    public boolean isEnabled(Resource resource) {
        return true;
    }
}