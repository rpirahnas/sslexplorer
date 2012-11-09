
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
			
package com.sslexplorer.properties.attributes;

import java.io.IOException;

import org.jdom.Element;
import org.jdom.JDOMException;

import com.sslexplorer.boot.AbstractXMLDefinedPropertyClass;
import com.sslexplorer.boot.PropertyDefinition;

public abstract class AbstractXMLDefinedAttributesPropertyClass extends AbstractXMLDefinedPropertyClass implements AttributesPropertyClass {
    
    private String messageResourcesKey;
    private boolean supportsVisibility;    
    
    public AbstractXMLDefinedAttributesPropertyClass(String name, boolean supportsReplacementsVariablesInValues, String messageResourcesKey, boolean supportsVisibility) throws IOException, JDOMException {
		super(name, supportsReplacementsVariablesInValues);
        this.messageResourcesKey = messageResourcesKey;
        this.supportsVisibility = supportsVisibility;
	}
    
    protected PropertyDefinition createDefinition(Element element) throws JDOMException {
    	return new XMLAttributeDefinition(element);
    }

    public String getMessageResourcesKey() {
        return messageResourcesKey;
    }

    public boolean isSupportsVisibility() {
        return supportsVisibility;
    }

}
