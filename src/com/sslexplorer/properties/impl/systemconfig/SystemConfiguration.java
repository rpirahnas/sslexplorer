
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
			
package com.sslexplorer.properties.impl.systemconfig;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.JDOMException;

import com.sslexplorer.boot.AbstractPropertyKey;
import com.sslexplorer.boot.AbstractXMLDefinedPropertyClass;
import com.sslexplorer.boot.ContextHolder;
import com.sslexplorer.boot.PropertyClass;
import com.sslexplorer.boot.PropertyDefinition;
import com.sslexplorer.core.CoreServlet;
import com.sslexplorer.properties.ProfilesFactory;

/**
 * {@link PropertyClass} implementation suitable for system configuration
 * properties
 */
public class SystemConfiguration extends AbstractXMLDefinedPropertyClass {

    final static Log log = LogFactory.getLog(SystemConfiguration.class);

    /**
     * Constant for name
     */
    public final static String NAME = "systemConfig";

    /**
     * Constructor.
     * 
     * @throws IOException
     * @throws JDOMException
     */
    public SystemConfiguration() throws IOException, JDOMException {
        super(NAME, false);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.properties.PropertyType#retrieve(com.sslexplorer.properties.PropertyKey)
     */
    public String retrievePropertyImpl(AbstractPropertyKey key) throws IllegalArgumentException {
        PropertyDefinition def = getDefinition(key.getName());
        try {
            String val = ProfilesFactory.getInstance().retrieveGenericProperty(
                key.getName(), "", "0", "0", "");
            if (val == null) {
                val = def.getDefaultValue();
            } else {
                if (def.getType() == PropertyDefinition.TYPE_PASSWORD) {
                    try {
                        val = ContextHolder.getContext().deobfuscatePassword(val);
                    } catch (Throwable t) {
                        log.warn(
                            "Password property " + def.getName() + " could not be decoded. It has been result to the default.", t);
                    }
                }
            }
            return val;
        } catch (Exception e) {
            log.error("Failed to retrieve property.", e);
        }
        return null;
    }

    public String storePropertyImpl(AbstractPropertyKey key, String value) throws IllegalArgumentException {
        PropertyDefinition def = getDefinition(key.getName());

        String oldValue = retrieveProperty(key);

        if (def.getType() == PropertyDefinition.TYPE_PASSWORD) {
            try {
                value = ContextHolder.getContext().obfuscatePassword(value);
            } catch (Throwable t) {
                log.warn("Password property " + def.getName() + " could not be encoded.", t);
            }
        }
        try {
            ProfilesFactory.getInstance().storeGenericProperty(
                key.getName(), "", "0", "0", "", value);
        } catch (Exception e) {
            log.error("Could not store properties in database.");
        }
        return oldValue;
    }
}
