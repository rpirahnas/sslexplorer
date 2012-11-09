
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
			
package com.sslexplorer.core;


/**
 * SSL-Explorer stores most of its configuration and resources in one of
 * the <i>Database</i> implementations.
 * <p>
 * Current databases include {@link com.sslexplorer.properties.PropertyDatabase},
 * {@link com.sslexplorer.security.SystemDatabase} and 
 * {@link com.sslexplorer.policyframework.PolicyDatabase}.
 * Plugins may define futher Databases.
 */
public interface Database {
  
    /**
     * Clean up the database. This is an admnistrator initiated action
     * that should be used to tidy up any old data that is no longer
     * relevant.
     * <p>
     * For example, SSL-Explorer allows the administrator to select the
     * source of <i>User Accounts</i>. The administrator chooses to use
     * the <i>Active Directory User Database</i>. Because SSL-Explorer does
     * not know when a user account is deleted from the Active Directory,
     * other Databases that store resources keyed by username, may end up
     * with items that are no longer valid. Every now and again the administrator
     * should run the <i>Clean Up</i> function which will scan these other
     * databases looking for non-existant users and delete the items. 
     * 
     * @throws Exception on any error
     */
    public void cleanup() throws Exception;
    
    /**
     * Open and initialise the database.
     * 
     * @param controllingServlet controlling servlet
     * @throws Exception on any error
     */
    public void open(CoreServlet controllingServlet) throws Exception;
    
    /**
     * Close the database. Normally called during shutdown.
     * 
     * @throws Exception on any error
     */
    public void close() throws Exception;


}
