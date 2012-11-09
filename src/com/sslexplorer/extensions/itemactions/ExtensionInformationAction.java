/**
 * 
 */
package com.sslexplorer.extensions.itemactions;

import com.sslexplorer.extensions.ExtensionBundleItem;
import com.sslexplorer.policyframework.Permission;
import com.sslexplorer.policyframework.PolicyConstants;
import com.sslexplorer.security.SessionInfo;
import com.sslexplorer.table.AvailableTableItemAction;
import com.sslexplorer.table.TableItemAction;

public final class ExtensionInformationAction extends TableItemAction {

    public ExtensionInformationAction() {
        super("extensionInformation", "extensions", 400, "", true, SessionInfo.MANAGEMENT_CONSOLE_CONTEXT,
                        PolicyConstants.EXTENSIONS_RESOURCE_TYPE, new Permission[] { PolicyConstants.PERM_CHANGE });
    }

    public boolean isEnabled(AvailableTableItemAction availableItem) {
        ExtensionBundleItem item = (ExtensionBundleItem)availableItem.getRowItem();
        return item.getBundle().getInstructionsURL()!=null && !item.getBundle().getInstructionsURL().equals("") && !item.getSubFormName().equals("updateableExtensionsForm");
    }

    public String getOnClick(AvailableTableItemAction availableItem) {
        ExtensionBundleItem item = (ExtensionBundleItem)availableItem.getRowItem();
        return "window.open('" + item.getBundle().getInstructionsURL() + "')";
    }
}