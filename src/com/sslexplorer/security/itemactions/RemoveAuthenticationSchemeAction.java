
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
			
package com.sslexplorer.security.itemactions;

import com.sslexplorer.policyframework.NoPermissionException;
import com.sslexplorer.policyframework.Permission;
import com.sslexplorer.policyframework.PolicyConstants;
import com.sslexplorer.policyframework.ResourceItem;
import com.sslexplorer.policyframework.ResourceUtil;
import com.sslexplorer.policyframework.itemactions.RemoveResourceAction;
import com.sslexplorer.security.AuthenticationScheme;
import com.sslexplorer.table.AvailableTableItemAction;

public class RemoveAuthenticationSchemeAction extends RemoveResourceAction {

    /**
     * @param navigationContext
     * @param messageResourcesKey
     */
    public RemoveAuthenticationSchemeAction(int navigationContext, String messageResourcesKey) {
        super(navigationContext, messageResourcesKey);
    }

    public boolean isEnabled(AvailableTableItemAction availableItem) {
        try {
            ResourceItem item = (ResourceItem) availableItem.getRowItem();
            try {
                if (((AuthenticationScheme) item.getResource()).isSystemScheme()){
                    // it is System Authentication Scheme so cannot be deleted.
                    return false;
                }
            } catch (Exception e) {
                // there has been an error so do not display
                return false;
            }
            
            Permission[] permissions = new Permission[] { PolicyConstants.PERM_DELETE };
            ResourceUtil.checkResourceManagementRights(item.getResource(), availableItem.getSessionInfo(), permissions);
            return true;
        } catch (NoPermissionException e) {
            return false;
        }
    }

}
