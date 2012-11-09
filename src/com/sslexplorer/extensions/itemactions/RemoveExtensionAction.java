/**
 * 
 */
package com.sslexplorer.extensions.itemactions;

import com.sslexplorer.extensions.ExtensionBundle;
import com.sslexplorer.extensions.ExtensionBundleItem;
import com.sslexplorer.policyframework.Permission;
import com.sslexplorer.policyframework.PolicyConstants;
import com.sslexplorer.security.SessionInfo;
import com.sslexplorer.table.AvailableTableItemAction;
import com.sslexplorer.table.TableItemAction;

public final class RemoveExtensionAction extends TableItemAction {
    public RemoveExtensionAction() {
        super("removeExtension", "extensions", 200, "", true, SessionInfo.MANAGEMENT_CONSOLE_CONTEXT,
                        PolicyConstants.EXTENSIONS_RESOURCE_TYPE, new Permission[] { PolicyConstants.PERM_CHANGE });
    }

    public boolean isEnabled(AvailableTableItemAction availableItem) {
        ExtensionBundleItem item = (ExtensionBundleItem)availableItem.getRowItem();
        return !item.getBundle().isDevExtension() && ( item.getBundle().getType() == ExtensionBundle.TYPE_INSTALLED || item.getBundle().getType() == ExtensionBundle.TYPE_UPDATEABLE ) && !item.getSubFormName().equals("updateableExtensionsForm");
    }

    public String getPath(AvailableTableItemAction availableItem) {
        ExtensionBundleItem item = (ExtensionBundleItem)availableItem.getRowItem();
        return "/removeExtension.do?id=" + item.getBundle().getId() + "&subForm=" + item.getSubFormName();
    }
}