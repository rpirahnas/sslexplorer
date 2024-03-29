/* ========================================================================== *
 * Copyright (C) 2004-2005 Pier Fumagalli <http://www.betaversion.org/~pier/> *
 *                            All rights reserved.                            *
 * ========================================================================== *
 *                                                                            *
 * Licensed under the  Apache License, Version 2.0  (the "License").  You may *
 * not use this file except in compliance with the License.  You may obtain a *
 * copy of the License at <http://www.apache.org/licenses/LICENSE-2.0>.       *
 *                                                                            *
 * Unless  required  by applicable  law or  agreed  to  in writing,  software *
 * distributed under the License is distributed on an  "AS IS" BASIS, WITHOUT *
 * WARRANTIES OR  CONDITIONS OF ANY KIND, either express or implied.  See the *
 * License for the  specific language  governing permissions  and limitations *
 * under the License.                                                         *
 *                                                                            *
 * ========================================================================== */
package com.sslexplorer.vfs.webdav;

import com.sslexplorer.vfs.VFSRepository;
import com.sslexplorer.vfs.VFSResource;

/**
 * <p>A simple interface identifying a {@link VFSRepository} event listener.</p> 
 *
 * @author <a href="http://www.betaversion.org/~pier/">Pier Fumagalli</a>
 */
public interface DAVListener {

    /** <p>An event representing the creation of a collection.</p> */
    public static final int COLLECTION_CREATED = 1;
    /** <p>An event representing the deletion of a collection.</p> */
    public static final int COLLECTION_REMOVED = 2;
    /** <p>An event representing the creation of a resource.</p> */
    public static final int RESOURCE_CREATED = 3;
    /** <p>An event representing the deletion of a resource.</p> */
    public static final int RESOURCE_REMOVED = 4;
    /** <p>An event representing the modification of a resource.</p> */
    public static final int RESOURCE_MODIFIED = 5;
    
    /**
     * <p>Notify this {@link DAVListener} of an action occurred on a
     * specified {@link VFSResource}.</p>
     * 
     * @param resource the {@link VFSResource} associated with the notification.
     * @param event a number identifying the type of the notification.
     */
    public void notify(VFSResource resource, int event);

}
