
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
			
package com.sslexplorer.boot;

public class AbstractPropertyKey {
    
    protected String name;
    protected String propertyClassName;
    
    public AbstractPropertyKey(String name, String propertyClassName) {
        this.name = name;
        this.propertyClassName = propertyClassName;
    }
    
    public String getPropertyClassName() {
        return propertyClassName;
    }

    public int hashCode() {
        return getName().hashCode();
    }

    public String getName() {
	    return name;
    }
    
    public String toString() {
        return "[" + getName() + "]";
    }
    
    public boolean equals(Object o) {
        AbstractPropertyKey k = (AbstractPropertyKey)o;
        return getName().equals(k.getName()) && getPropertyClassName().equals(k.getPropertyClassName());
    }

}
