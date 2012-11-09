
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
			
package com.sslexplorer.policyframework.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sslexplorer.boot.PropertyList;
import com.sslexplorer.core.CoreUtil;
import com.sslexplorer.input.MultiSelectDataSource;
import com.sslexplorer.input.MultiSelectSelectionModel;
import com.sslexplorer.policyframework.AccessRight;
import com.sslexplorer.policyframework.AccessRights;
import com.sslexplorer.policyframework.AccessRightsMultiSelectDataSource;
import com.sslexplorer.policyframework.DefaultAccessRights;
import com.sslexplorer.policyframework.DelegatedPoliciesDataSource;
import com.sslexplorer.policyframework.PolicyConstants;
import com.sslexplorer.policyframework.Resource;
import com.sslexplorer.policyframework.ResourceStack;
import com.sslexplorer.policyframework.forms.AccessRightsForm;
import com.sslexplorer.security.LogonControllerFactory;
import com.sslexplorer.security.SessionInfo;

/**
 * Implementation of a
 * {@link com.sslexplorer.policyframework.actions.AbstractResourceDispatchAction}
 * that allows viewing and editing of a single <i>Resource Permission</i>.
 */
public class AccessRightsDispatchAction extends AbstractResourceDispatchAction {

    final static Log log = LogFactory.getLog(AccessRightsDispatchAction.class);

    /**
     * Constructor
     */
    public AccessRightsDispatchAction() {
        super(PolicyConstants.ACCESS_RIGHTS_RESOURCE_TYPE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.policyframework.actions.AbstractResourceDispatchAction#createResource(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public Resource createResource(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
                    throws Exception {
        SessionInfo sessionInfo = getSessionInfo(request);
        int selectedRealmId = sessionInfo.getRealmId();
        return new DefaultAccessRights(selectedRealmId, "");
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
                    throws Exception {
        ActionForward actionForward = super.edit(mapping, form, request, response);
        AccessRightsForm arf = (AccessRightsForm) form;
        AccessRights accessRights = (AccessRights) ResourceStack.peekEditingStack(request.getSession());
        if (actionForward.getName().equals("home")){
            // super returned a home, so we must go home.
            return actionForward;
        }
        List<AccessRight> accessRights2 = accessRights.getAccessRights();
        PropertyList selectedAccessRights = new PropertyList();
        for (AccessRight right : accessRights2) {
            String permissionString = CoreUtil.getMessageResources(getSessionInfo(request).getHttpSession(),
                right.getPermission().getBundle()).getMessage("permission." + right.getPermission().getId() + ".title").trim();
            String resourceTypeString = CoreUtil.getMessageResources(getSessionInfo(request).getHttpSession(),
                right.getResourceType().getBundle()).getMessage(
                "resourceType." + right.getResourceType().getResourceTypeId() + ".title").trim();
            String lableString = resourceTypeString + " " + permissionString;
            selectedAccessRights.add(lableString);
        }
        AccessRightsMultiSelectDataSource accessRightsMultiSelectDataSource = new AccessRightsMultiSelectDataSource(accessRights
                        .getAccessRightsClass());
        MultiSelectSelectionModel accessRightsModel = new MultiSelectSelectionModel(getSessionInfo(request),
                        accessRightsMultiSelectDataSource, selectedAccessRights);
        arf.setAccessRightsModel(accessRightsModel);
        arf.setSelectedAccessRights(selectedAccessRights);
        return actionForward;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.policyframework.actions.AbstractResourceDispatchAction#commit(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward commit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
                    throws Exception {
        AccessRightsForm arf = (AccessRightsForm) form;
        AccessRights resource = (AccessRights) arf.getResource();
        PropertyList selectedAccessRightsList = arf.getSelectedAccessRightsList();
        resource.getAccessRights().clear();
        resource.setAllAccessRights(getSessionInfo(request).getHttpSession(), selectedAccessRightsList);
        saveMessage(request, "editAccessRights.message.saved", resource);
        super.commit(mapping, form, request, response);
        return getRedirectWithMessages(mapping, request);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.core.actions.CoreAction#getNavigationContext(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public int getNavigationContext(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return SessionInfo.MANAGEMENT_CONSOLE_CONTEXT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.policyframework.actions.AbstractResourceDispatchAction#createAvailablePoliciesDataSource(com.sslexplorer.policyframework.Resource,
     *      org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    protected MultiSelectDataSource createAvailablePoliciesDataSource(Resource resource, ActionMapping mapping, ActionForm form,
                                                                      HttpServletRequest request, HttpServletResponse response)
                    throws Exception {
        return new DelegatedPoliciesDataSource(null, null, ((AccessRights) resource).getAccessRightsClass(), LogonControllerFactory
                        .getInstance().getUser(request));
    }

    @Override
    protected void doUpdate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
                    throws Exception {
        super.doUpdate(mapping, form, request, response);
        // we now need to rebuild any menus, as more or less could be visable.
        LogonControllerFactory.getInstance().applyMenuItemChanges(request);
    }

}