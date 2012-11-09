
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
			
package com.sslexplorer.properties.wizards.forms;

import com.sslexplorer.policyframework.PolicyConstants;
import com.sslexplorer.policyframework.forms.AbstractWizardResourcePolicySelectionForm;


/**
 * Implementation of a {@link com.sslexplorer.policyframework.forms.AbstractWizardResourcePolicySelectionForm}
 * that allows an adminstrator to attach policies to <i>Profiles</i>.
 */
public class ProfilePolicySelectionForm extends AbstractWizardResourcePolicySelectionForm {
    
    /**
     * Constructor.
     */
    public ProfilePolicySelectionForm() {
        super(true, true, "/WEB-INF/jsp/content/properties/profileWizard/profilePolicySelection.jspf", 
            "profilePolicySelection", "properties", "profileWizard.profilePolicySelection", 2,
            PolicyConstants.PROFILE_RESOURCE_TYPE);
    }
}
