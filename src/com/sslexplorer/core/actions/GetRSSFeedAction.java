package com.sslexplorer.core.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sslexplorer.core.actions.AuthenticatedAction;
import com.sslexplorer.core.forms.GetRSSFeedForm;
import com.sslexplorer.security.SessionInfo;


/**
 * Implementation of {@link com.sslexplorer.core.actions.AuthenticatedAction}
 * that retrieves an RSS feed and displays the panel for it.  
 */
public class GetRSSFeedAction extends AuthenticatedAction {

    /* (non-Javadoc)
     * @see com.sslexplorer.core.actions.CoreAction#getNavigationContext(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public int getNavigationContext(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return SessionInfo.MANAGEMENT_CONSOLE_CONTEXT;
    }
    
    /* (non-Javadoc)
     * @see com.sslexplorer.core.actions.AuthenticatedAction#onExecute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ActionForward onExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
    	((GetRSSFeedForm)form).init(request.getParameter("feed"));
        return mapping.findForward("display");
    }

}
