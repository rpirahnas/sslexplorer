
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
			
package com.sslexplorer.extensions.itemactions;

import javax.servlet.http.HttpServletRequest;

import com.sslexplorer.extensions.ExtensionBundleItem;
import com.sslexplorer.policyframework.Permission;
import com.sslexplorer.policyframework.PolicyConstants;
import com.sslexplorer.security.SessionInfo;
import com.sslexplorer.table.AvailableTableItemAction;
import com.sslexplorer.table.TableItemAction;

/**
 */
public class DisableExtensionAction extends TableItemAction {

    /**
     * Default constructor
     */
    public DisableExtensionAction() {
        super("disable", "extensions", 400, "", false, SessionInfo.MANAGEMENT_CONSOLE_CONTEXT,
                        PolicyConstants.EXTENSIONS_RESOURCE_TYPE, new Permission[] { PolicyConstants.PERM_CHANGE });
    }
    
    @Override
    public String getPath(AvailableTableItemAction availableItem) {
        ExtensionBundleItem item = (ExtensionBundleItem)availableItem.getRowItem();
        return "/showExtensionStore.do?actionTarget=disable&id=" + item.getBundle().getId() + "&subForm=" + item.getSubFormName();
    }

    @Override
    public boolean isEnabled(AvailableTableItemAction availableItem) {
        ExtensionBundleItem item = (ExtensionBundleItem) availableItem.getRowItem();
        return item.getBundle().canDisable() && !item.getSubFormName().equals("updateableExtensionsForm");
    }
}