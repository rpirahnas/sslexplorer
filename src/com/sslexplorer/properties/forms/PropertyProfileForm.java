
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
			
package com.sslexplorer.properties.forms;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.sslexplorer.boot.PropertyList;
import com.sslexplorer.input.MultiSelectSelectionModel;
import com.sslexplorer.policyframework.Resource;
import com.sslexplorer.policyframework.ResourceUtil;
import com.sslexplorer.policyframework.forms.AbstractResourceForm;
import com.sslexplorer.properties.ProfilesFactory;
import com.sslexplorer.properties.PropertyProfile;
import com.sslexplorer.security.Constants;
import com.sslexplorer.security.SessionInfo;
import com.sslexplorer.security.User;

/**
 * Form for editing property profiles.
 */
public class PropertyProfileForm extends AbstractResourceForm<PropertyProfile> {
    private static Log log = LogFactory.getLog(PropertyProfileForm.class);
    private List propertyProfiles;
    private int selectedPropertyProfile;
    private String scope;
    private String selectedTab = "details";

    /**
     * Get the action needed to perform the update.
     * 
     * @return String
     */
    public String getUpdateAction() {
        return getEditing() ? (Constants.SCOPE_GLOBAL.equals(getPropertyScope()) ? "/editGlobalPropertyProfile.do"
                        : "/editPropertyProfile.do")
                        : (Constants.SCOPE_GLOBAL.equals(getPropertyScope()) ? "/createGlobalPropertyProfile.do"
                                        : "/createPropertyProfile.do");
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.policyframework.forms.AbstractResourceForm#validate(org.apache.struts.action.ActionMapping,
     *      javax.servlet.http.HttpServletRequest)
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errs = super.validate(mapping, request);
        if (isCommiting()) {
            if (getResourceName().equalsIgnoreCase("default")
                            && (!getEditing() || (getEditing() && !getResource().getResourceName().equalsIgnoreCase("default")))) {
                errs.add(Globals.ERROR_KEY, new ActionMessage("error.createProfile.cantUseNameDefault"));
                setResourceName("");
            }
        }
        return errs;
    }

    /**
     * @return List of property profiles
     */
    public List getPropertyProfiles() {
        return propertyProfiles;
    }

    /**
     * @param selectedPropertyProfile
     */
    public void setSelectedPropertyProfile(int selectedPropertyProfile) {
        this.selectedPropertyProfile = selectedPropertyProfile;
    }

    /**
     * @return int property profile id
     */
    public int getSelectedPropertyProfile() {
        return selectedPropertyProfile;
    }

    /**
     * @return the property scope
     */
    public String getPropertyScope() {
        return scope;
    }

    /**
     * @param scope
     */
    public void setPropertyScope(String scope) {
        this.scope = scope;
    }

    /**
     * @param propertyProfiles
     */
    public void setPropertyProfiles(List propertyProfiles) {
        this.propertyProfiles = propertyProfiles;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.policyframework.forms.AbstractResourceForm#getResourceByName(java.lang.String,
     *      com.sslexplorer.security.SessionInfo)
     */
    public Resource getResourceByName(String name, SessionInfo session) throws Exception {
        String username = Constants.SCOPE_GLOBAL.equals(getPropertyScope()) ? null : getUser().getPrincipalName();
        return ProfilesFactory.getInstance().getPropertyProfile(username, name, getUser().getRealm().getResourceId());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.tabs.TabModel#getTabCount()
     */
    public int getTabCount() {
        return getPropertyScope().equals(Constants.SCOPE_PERSONAL) ? 1 : 2;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.tabs.TabModel#getTabName(int)
     */
    public String getTabName(int idx) {
        switch (idx) {
            case 0:
                return "details";
            default:
                return "policies";
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.tabs.TabModel#getTabTitle(int)
     */
    public String getTabTitle(int idx) {
        // Get from resources
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.policyframework.forms.AbstractResourceForm#initialise(javax.servlet.http.HttpServletRequest,
     *      com.sslexplorer.policyframework.Resource, boolean,
     *      com.sslexplorer.input.MultiSelectSelectionModel,
     *      com.sslexplorer.boot.PropertyList, com.sslexplorer.security.User,
     *      boolean)
     */
    public void initialise(HttpServletRequest request, PropertyProfile resource, boolean editing, MultiSelectSelectionModel policyModel,
                           PropertyList selectedPolicies, User owner, boolean assignOnly) throws Exception {
        super.initialise(request, resource, editing, policyModel, selectedPolicies, owner, assignOnly);
        propertyProfiles = ProfilesFactory.getInstance().getPropertyProfiles(owner == null ? null : owner.getPrincipalName(), true,
            ((PropertyProfile) resource).getRealmID());
        if (owner != null) {
            propertyProfiles = ResourceUtil.filterResources(owner, propertyProfiles, owner == null ? true : false);
        }
        if (resource != null) {
            selectedPropertyProfile = ((PropertyProfile) resource).getResourceId();
        } else {
            selectedPropertyProfile = 0;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.tabs.TabModel#getSelectedTab()
     */
    public String getSelectedTab() {
        return selectedTab;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.tabs.TabModel#setSelectedTab(java.lang.String)
     */
    public void setSelectedTab(String selectedTab) {
        this.selectedTab = selectedTab;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.policyframework.forms.AbstractResourceForm#applyToResource()
     */
    public void applyToResource() throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.tabs.TabModel#getTabBundle(int)
     */
    public String getTabBundle(int idx) {
        return null;
    }
}