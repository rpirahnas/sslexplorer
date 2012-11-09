
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
			
package com.sslexplorer.properties.impl.realms;

import com.sslexplorer.boot.AbstractPropertyKey;
import com.sslexplorer.realms.Realm;

/**
 */
public class RealmKey extends AbstractPropertyKey {
    
    int realmID;
    
    /**
     * Constructor
     * 
     * @param name name
     * @param realmID realm ID
     */
    public RealmKey(String name, int realmID) {
        super(name, RealmProperties.NAME);
        this.realmID = realmID;
    }
    
    /**
     * Constructor
     * 
     * @param name name
     * @param realm realm
     */
    public RealmKey(String name, Realm realm) {
    	this(name, realm.getResourceId());
    }

    /**
     * the id of the realm
     * 
     * @return realmID 
     */
    public int getRealmID() {
        return realmID;
    }

}
