/**
 * 
 */
package com.sslexplorer.navigation.itemactions;

import java.text.MessageFormat;

import org.apache.struts.action.ActionMapping;

import com.sslexplorer.navigation.AbstractFavoriteItem;
import com.sslexplorer.navigation.WrappedFavoriteItem;
import com.sslexplorer.policyframework.itemactions.AbstractPathAction;
import com.sslexplorer.security.Constants;
import com.sslexplorer.table.AvailableTableItemAction;

public final class RemoveFavoriteAction extends AbstractPathAction {
	public RemoveFavoriteAction() {
		super("remove", "navigation", 100, true, "{2}.do?actionTarget=confirmRemove&selectedItem={0}");
	}

	public boolean isEnabled(AvailableTableItemAction availableItem) {
		WrappedFavoriteItem item = (WrappedFavoriteItem) availableItem.getRowItem();
		return !item.getFavoriteType().equals(AbstractFavoriteItem.GLOBAL_FAVORITE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sslexplorer.table.TableItemAction#getPath(com.sslexplorer.table.AvailableTableItemAction)
	 */
	public String getPath(AvailableTableItemAction availableItem) {
		WrappedFavoriteItem item = (WrappedFavoriteItem) availableItem.getRowItem();
		return MessageFormat.format(requiredPath, new Object[] { item.getFavoriteItem()
						.getResource()
						.getResourceType()
						.getResourceTypeId() + "_"
			+ item.getFavoriteItem().getResource().getResourceId(),
			((ActionMapping) availableItem.getRequest().getAttribute(Constants.REQ_ATTR_ACTION_MAPPING)).getName(),
			((ActionMapping) availableItem.getRequest().getAttribute(Constants.REQ_ATTR_ACTION_MAPPING)).getPath() });
	}
}