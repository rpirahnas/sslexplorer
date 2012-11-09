
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sslexplorer.boot.PropertyList;
import com.sslexplorer.policyframework.PolicyDatabaseFactory;
import com.sslexplorer.properties.wizards.actions.ProfileDetailsAction;
import com.sslexplorer.security.Constants;
import com.sslexplorer.security.User;
import com.sslexplorer.wizard.AbstractWizardSequence;
import com.sslexplorer.wizard.forms.DefaultWizardForm;

public class ProfileSummaryForm extends DefaultWizardForm {
    
    final static Log log = LogFactory.getLog(ProfileSummaryForm.class);

    // Private statics for sequence attributes
    
    // Private instance variables
    private String scope;
    private User user;
    private String resourceName;
    private List selectedPolicies;

    public ProfileSummaryForm() {
        super(false, true, "/WEB-INF/jsp/content/properties/profileWizard/profileSummary.jspf", 
            "", true, true, "profileSummary", "properties", "profileWizard.profileSummary", 3);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.wizard.forms.AbstractWizardForm#init(com.sslexplorer.wizard.AbstractWizardSequence)
     */
    public void init(AbstractWizardSequence sequence, HttpServletRequest request)  throws Exception {
        resourceName = (String)sequence.getAttribute(ProfileDetailsForm.ATTR_RESOURCE_NAME, Constants.SCOPE_PERSONAL);
        PropertyList l = (PropertyList)sequence.getAttribute(ProfilePolicySelectionForm.ATTR_SELECTED_POLICIES, null);
        selectedPolicies = new ArrayList();
        for(Iterator i = l.iterator(); i.hasNext(); ) {
            selectedPolicies.add(PolicyDatabaseFactory.getInstance().getPolicy(Integer.parseInt(i.next().toString())).getResourceName());
        }
        scope = (String)sequence.getAttribute(ProfileDetailsAction.ATTR_PROFILE_SCOPE, Constants.SCOPE_PERSONAL);
        user = sequence.getSession().getUser();
    }

    /**
     * @return Returns the name.
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * @return Returns the selectedPolicies.
     */
    public List getSelectedPolicies() {
        return selectedPolicies;
    }

    /**
     * @return Returns the scope.
     */
    public String getScope() {
        return scope;
    }

    /**
     * @return Returns the user.
     */
    public User getUser() {
        return user;
    }
}
