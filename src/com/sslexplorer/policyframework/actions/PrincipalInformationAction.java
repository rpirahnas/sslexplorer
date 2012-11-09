
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sslexplorer.core.UserDatabaseManager;
import com.sslexplorer.core.actions.AuthenticatedDispatchAction;
import com.sslexplorer.policyframework.Principal;
import com.sslexplorer.policyframework.forms.PrincipalInformationForm;
import com.sslexplorer.security.Constants;
import com.sslexplorer.security.LogonControllerFactory;
import com.sslexplorer.security.SessionInfo;

/**
 * Action that display details about a {@link Principal}.
 */
public class PrincipalInformationAction extends AuthenticatedDispatchAction {

    final static Log log = LogFactory.getLog(PrincipalInformationAction.class);

    /* (non-Javadoc)
     * @see org.apache.struts.actions.DispatchAction#unspecified(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
            try {
                String principalType = request.getParameter("principalType");
                String principalName = request.getParameter("principalName");
                Principal principal;
                if("account".equals(principalType)) {
                    principal = UserDatabaseManager.getInstance().getUserDatabase(LogonControllerFactory.getInstance().getUser(request).getRealm()).getAccount(principalName);
                } else if("role".equals(principalType)) {
                    principal = UserDatabaseManager.getInstance().getUserDatabase(LogonControllerFactory.getInstance().getUser(request).getRealm()).getRole(principalName);
                } else {
                    throw new Exception();
                }
                request.setAttribute(Constants.REQ_ATTR_INFO_RESOURCE, principal);
                return principalInformation(mapping, form, request, response);
            } catch (Exception e) {
              log.error("Failed to get principal information. ", e);
              response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());                
            }
            return null;
        }
        
    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws Exception
     */
    public ActionForward principalInformation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrincipalInformationForm informationForm = (PrincipalInformationForm) form;
        Principal principal = (Principal) request.getAttribute(Constants.REQ_ATTR_INFO_RESOURCE);
        informationForm.initialise(principal);
        return mapping.findForward("display");
    }
    
    /* (non-Javadoc)
     * @see com.sslexplorer.core.actions.CoreAction#getNavigationContext(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public int getNavigationContext(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return SessionInfo.MANAGEMENT_CONSOLE_CONTEXT;
    }
}