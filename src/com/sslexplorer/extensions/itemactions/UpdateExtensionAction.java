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
import com.sslexplorer.tasks.TaskUtil;

public final class UpdateExtensionAction extends TableItemAction {
    
    public UpdateExtensionAction() {
        super("updateExtension", "extensions", 300, "", true, SessionInfo.MANAGEMENT_CONSOLE_CONTEXT, PolicyConstants.EXTENSIONS_RESOURCE_TYPE, new Permission[]{ PolicyConstants.PERM_CHANGE});
    }

    public boolean isEnabled(AvailableTableItemAction availableItem) {
        ExtensionBundleItem item = (ExtensionBundleItem)availableItem.getRowItem();
        return !item.getBundle().isDevExtension() && item.getBundle().isUpdateable() && item.getSubFormName().equals("updateableExtensionsForm");
    }

    @Override
    public String getOnClick(AvailableTableItemAction availableItem) {
        return TaskUtil.getTaskPathOnClick(getPath(availableItem), "extensions", "updateExtension", availableItem.getRequest().getSession(), 440, 100);
    }

    public String getPath(AvailableTableItemAction availableItem) {
        ExtensionBundleItem item = (ExtensionBundleItem)availableItem.getRowItem();
        return "/showExtensionStore.do?actionTarget=update&id=" + item.getBundle().getId()+"&version="+ item.getVersion().toString() + "&subForm=" + item.getSubFormName();
    }
}