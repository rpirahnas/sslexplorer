
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
			
package com.sslexplorer.policyframework.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.sslexplorer.input.MultiSelectDataSource;
import com.sslexplorer.policyframework.DelegatedPoliciesDataSource;
import com.sslexplorer.policyframework.DelegatedPoliciesExcludePesronalDataSource;
import com.sslexplorer.policyframework.ResourceType;
import com.sslexplorer.policyframework.wizards.forms.AccessRightsDetailsForm;
import com.sslexplorer.security.LogonControllerFactory;
import com.sslexplorer.security.User;

/**
 * Abstract implementation of a {@link com.sslexplorer.policyframework.forms.AbstractWizardPolicySelectionForm}
 * that should be used for wizards that create delegatable resources. 
 */
public class AbstractWizardResourcePolicySelectionForm extends AbstractWizardPolicySelectionForm {

    private ResourceType resourceType;

    /**
     * Constructor.
     *
     * @param nextAvailable next button available
     * @param previousAvailable previous button available
     * @param page page 
     * @param pageName page name
     * @param resourceBundle resource bundle
     * @param resourcePrefix resource prefix
     * @param stepIndex step
     * @param resourceType resource type
     */
    public AbstractWizardResourcePolicySelectionForm(boolean nextAvailable, boolean previousAvailable, String page, String pageName, String resourceBundle, String resourcePrefix, int stepIndex, ResourceType resourceType) {
        super(nextAvailable, previousAvailable, page, "sourceValues_selectedPolicies", false, false, pageName, resourceBundle,
                        resourcePrefix, stepIndex);
        this.resourceType = resourceType;
    }
    
    /* (non-Javadoc)
     * @see com.sslexplorer.policyframework.forms.AbstractWizardPolicySelectionForm#createDatasource(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public MultiSelectDataSource createDatasource(ActionMapping mapping, HttpServletRequest request) {
        try {
            User u = LogonControllerFactory.getInstance().getUser(request);
            return new DelegatedPoliciesDataSource(null, resourceType, (String)getWizardSequence(request).getAttribute(AccessRightsDetailsForm.ATTR_RESOURCE_PERMISSION_CLASS, null),
                u);
        }
        catch(Exception e) {
            return null;
        }
    }
    public MultiSelectDataSource createDatasourceExcludePersonal(ActionMapping mapping, HttpServletRequest request) {
        try {
            return new DelegatedPoliciesExcludePesronalDataSource(null, resourceType, (String)getWizardSequence(request).getAttribute(AccessRightsDetailsForm.ATTR_RESOURCE_PERMISSION_CLASS, null),
                LogonControllerFactory.getInstance().getSessionInfo(request).getRealm());
        }
        catch(Exception e) {
            return null;
        }
    }

}
