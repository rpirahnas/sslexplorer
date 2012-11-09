
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
			
package com.sslexplorer.policyframework;

import com.sslexplorer.table.AbstractTableItemTableModel;

/**
 * @param <T>
 */
public class ResourceItemModel<T extends ResourceItem> extends AbstractTableItemTableModel<T> {
    
    private String id;
    
    /**
     * @param id
     */
    public ResourceItemModel(String id) {
        this.id = id;
    }

    public int getColumnCount() {
        return 1;
    }

    public String getColumnName(int col) {
        return "name";
    }

    public Class getColumnClass(int col) {
        return String.class;
    }

    public int getColumnWidth(int col) {
        return 0;
    }

    public String getId() {
        return id;
    }

}