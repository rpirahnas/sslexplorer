package com.sslexplorer.ajax;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.helpers.AjaxXmlBuilder;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.sslexplorer.boot.ContextHolder;
import com.sslexplorer.boot.Util;
import com.sslexplorer.core.CoreUtil;
import com.sslexplorer.core.GlobalWarningManager;
import com.sslexplorer.core.UserDatabaseManager;
import com.sslexplorer.install.forms.SelectUserDatabaseForm;
import com.sslexplorer.security.Constants;
import com.sslexplorer.security.LogonControllerFactory;
import com.sslexplorer.security.SessionInfo;
import com.sslexplorer.security.User;
import com.sslexplorer.security.UserDatabase;
import com.sslexplorer.wizard.AbstractWizardSequence;

/**
 * Implementation of {@link com.sslexplorer.ajax.AbstractAjaxXMLAction} that
 * returns an XML document containing a list of user accounts.
 * <p>
 * Two request parameters are supported. First, the optional <b>account</b> 
 * which may contain a search string and secondly <b>maxRows</b> which is an
 * integer, defaults to 10 and determines the maximum number of results to
 * return. If <b>account</b> is not supplied, all accounts (up to the specified
 * maximum rows) will be returned.
 */

public class DismissGlobalWarningAction extends AbstractAjaxXMLAction {

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.ajax.AbstractAjaxAction#onAjaxRequest(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm,
     *      javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse,
     *      org.ajaxtags.helpers.AjaxXmlBuilder)
     */
    protected void onAjaxRequest(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response,
                    AjaxXmlBuilder builder) throws Exception {    	
    	String messageKey = request.getParameter("messageKey");
    	if(messageKey == null) {
    		throw new Exception("No messageKey parameter supplied.");
    	}
    	GlobalWarningManager.getInstance().dismissGlobalWarning(request.getSession(), messageKey);        
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
        return SessionInfo.MANAGEMENT_CONSOLE_CONTEXT | SessionInfo.USER_CONSOLE_CONTEXT | SessionInfo.SETUP_CONSOLE_CONTEXT;
    }

}
