
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
			
package com.sslexplorer.services;

import java.util.EventListener;

import javax.swing.event.EventListenerList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sslexplorer.core.CoreEvent;
import com.sslexplorer.core.CoreListener;

/**
 */
public class CoreEventServiceImpl implements CoreEventService {
    private static final Log log = LogFactory.getLog(CoreEventServiceImpl.class);
    private final EventListenerList coreListeners;

    /**
     */
    public CoreEventServiceImpl() {
        coreListeners = new EventListenerList();
    }

    public void addCoreListener(CoreListener listener) {
        coreListeners.add(CoreListener.class, listener);
    }

    public void removeCoreListener(CoreListener listener) {
        coreListeners.remove(CoreListener.class, listener);
    }

    public void fireCoreEvent(CoreEvent evt) {
        if (evt == null) {
            return;
        }

        EventListener[] listeners = coreListeners.getListeners(CoreListener.class);
        for (int index = listeners.length - 1; index >= 0; index--) {
            // We don't want badly behaved listeners to throw uncaught
            // exceptions and upset other listeners
            try {
                CoreListener coreListener = (CoreListener) listeners[index];
                coreListener.coreEvent(evt);
            } catch (Throwable thr) {
                log.error("Event failed.", thr);
            }
        }
    }
}