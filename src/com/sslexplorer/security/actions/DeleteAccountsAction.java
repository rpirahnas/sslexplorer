
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
			
package com.sslexplorer.security.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.sslexplorer.core.CoreAttributeConstants;
import com.sslexplorer.core.CoreEvent;
import com.sslexplorer.core.CoreEventConstants;
import com.sslexplorer.core.CoreServlet;
import com.sslexplorer.core.UserDatabaseManager;
import com.sslexplorer.core.actions.AuthenticatedDispatchAction;
import com.sslexplorer.policyframework.PolicyConstants;
import com.sslexplorer.policyframework.PolicyUtil;
import com.sslexplorer.security.LogonControllerFactory;
import com.sslexplorer.security.SessionInfo;
import com.sslexplorer.security.User;
import com.sslexplorer.security.UserDatabase;

/**
 * Action to delete a users account.
 */
public class DeleteAccountsAction extends AuthenticatedDispatchAction {

    /**
     * 
     */
    public DeleteAccountsAction() {
        super();
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws Exception
     */
    public ActionForward onExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
                    throws Exception {
    	PolicyUtil.checkPermission(PolicyConstants.ACCOUNTS_AND_GROUPS_RESOURCE_TYPE, PolicyConstants.PERM_DELETE, request);

        User currentUser = isSetupMode() ? null : LogonControllerFactory.getInstance().getUser(request);

        String[] accounts = request.getParameterValues("username");
        boolean found = false;
        for (int i = 0; i < accounts.length; i++) {
            if (currentUser != null && accounts[i].equals(currentUser.getPrincipalName())) {
                found = true;
            }
        }
        if (!found) {
            UserDatabase udb = UserDatabaseManager.getInstance().getUserDatabase(currentUser.getRealm());
            for (int i = 0; accounts != null && i < accounts.length; i++) {
                User user = udb.getAccount(accounts[i]);
                if(udb.supportsAccountCreation()) {
                	try {
                        udb.deleteAccount(user);
                        CoreServlet.getServlet().fireCoreEvent(new CoreEvent(this, CoreEventConstants.USER_REMOVED, null, null, CoreEvent.STATE_SUCCESSFUL)
                        		.addAttribute(CoreAttributeConstants.EVENT_ATTR_PRINCIPAL_ID, user.getPrincipalName())
                        		.addAttribute(CoreAttributeConstants.EVENT_ATTR_FULL_NAME, user.getFullname()));
                	} catch (Exception e) {
        	            CoreServlet.getServlet().fireCoreEvent(new CoreEvent(this, CoreEventConstants.USER_REMOVED, null, null, e)
                        		.addAttribute(CoreAttributeConstants.EVENT_ATTR_PRINCIPAL_ID, user.getPrincipalName())
                        		.addAttribute(CoreAttributeConstants.EVENT_ATTR_FULL_NAME, user.getFullname()));
                		throw e;
                	}
                }
            }
        } else {
            ActionMessages mesgs = new ActionMessages();
            mesgs.add(Globals.ERROR_KEY, new ActionMessage("availableAccounts.cannotDeleteOwnAccount"));
            saveErrors(request, mesgs);
        }

        return mapping.findForward("refresh");
    }

    /* (non-Javadoc)
     * @see com.sslexplorer.core.actions.CoreAction#getNavigationContext(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public int getNavigationContext(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return SessionInfo.MANAGEMENT_CONSOLE_CONTEXT;
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     * @throws Exception
     */
    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception {
    	return mapping.findForward("success");
    }
}