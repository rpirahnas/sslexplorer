
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
			
package com.sslexplorer.input.validators;

import com.sslexplorer.boot.PropertyValidator;


/**
 * Convenience {@link PropertyValidator} implementation that only allows non-blank
 * strings.
 */
public class NonBlankStringValidator extends StringValidator {
    
    /**
     * Constructor.
     */
    public NonBlankStringValidator() {
        super(1, Integer.MAX_VALUE, null, null, true);
    }
}
