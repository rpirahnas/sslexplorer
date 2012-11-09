/**
 * 
 */
package com.sslexplorer.properties.itemactions;

import com.sslexplorer.policyframework.ResourceItem;
import com.sslexplorer.policyframework.itemactions.RemoveResourceAction;
import com.sslexplorer.security.SessionInfo;
import com.sslexplorer.table.AvailableTableItemAction;

public final class RemoveProfileAction extends RemoveResourceAction {
	public RemoveProfileAction() {
		super(SessionInfo.ALL_CONTEXTS, "properties");
	}

	public boolean isEnabled(AvailableTableItemAction availableItem) {
		return super.isEnabled(availableItem)
				&& ((ResourceItem) availableItem.getRowItem()).getResource()
						.getResourceId() != 0;
	}
}