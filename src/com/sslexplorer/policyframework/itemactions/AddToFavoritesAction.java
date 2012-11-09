/**
 * 
 */
package com.sslexplorer.policyframework.itemactions;

import com.sslexplorer.navigation.AbstractFavoriteItem;
import com.sslexplorer.security.SessionInfo;
import com.sslexplorer.table.AvailableTableItemAction;

public final class AddToFavoritesAction extends AbstractPathAction {

    public static final String TABLE_ITEM_ACTION_ID = "addToFavorites";

    public AddToFavoritesAction(String messageResourcesKey) {
        super(TABLE_ITEM_ACTION_ID, messageResourcesKey, 100, false, SessionInfo.USER_CONSOLE_CONTEXT,
                        "{2}.do?actionTarget=favorite&selectedResource={0}");
    }

    public boolean isEnabled(AvailableTableItemAction availableItem) {
        AbstractFavoriteItem item = (AbstractFavoriteItem) availableItem.getRowItem();
        return item.getFavoriteType().equals(AbstractFavoriteItem.NO_FAVORITE);
    }
}