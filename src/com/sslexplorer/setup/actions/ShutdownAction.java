/*
 */
package com.sslexplorer.setup.actions;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.sslexplorer.boot.ContextHolder;
import com.sslexplorer.core.BundleActionMessage;
import com.sslexplorer.core.CoreServlet;
import com.sslexplorer.core.CoreUtil;
import com.sslexplorer.core.GlobalWarning;
import com.sslexplorer.core.GlobalWarningManager;
import com.sslexplorer.core.GlobalWarning.DismissType;
import com.sslexplorer.core.actions.AuthenticatedDispatchAction;
import com.sslexplorer.policyframework.Permission;
import com.sslexplorer.policyframework.PolicyConstants;
import com.sslexplorer.policyframework.PolicyUtil;
import com.sslexplorer.security.Constants;
import com.sslexplorer.security.SessionInfo;
import com.sslexplorer.setup.forms.ShutdownForm;
import com.sslexplorer.tasks.shutdown.ShutdownTimerTask;
import com.sslexplorer.tasks.timer.StoppableTimer;

/**
 * Action to shut the server down.
 */
public class ShutdownAction extends AuthenticatedDispatchAction {
    final static Log log = LogFactory.getLog(ShutdownAction.class);

    /**
     * Constructor
     */
    public ShutdownAction() {
        super(PolicyConstants.SERVICE_CONTROL_RESOURCE_TYPE, new Permission[] { PolicyConstants.PERM_SHUTDOWN,
            PolicyConstants.PERM_RESTART });
    }

    public ActionForward sendMessage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
        return CoreUtil.addParameterToForward(mapping.findForward("message"), "users", "*");
    }

    public ActionForward installShutdown(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                         HttpServletResponse response) throws Exception {
        ShutdownForm shutdownForm = (ShutdownForm) form;
        performShutdown(request, shutdownForm);
        return mapping.findForward("installShutdown");
    }

    public ActionForward confirmed(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
                    throws Exception {
        ShutdownForm shutdownForm = (ShutdownForm) form;
        performShutdown(request, shutdownForm);
        // return to the home as there is a delay before the shutdown.
        return mapping.findForward("refresh");
    }

    public ActionForward shutdown(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
                    throws Exception {
        ShutdownForm shutdownForm = (ShutdownForm) form;
        PolicyUtil.checkPermission(PolicyConstants.SERVICE_CONTROL_RESOURCE_TYPE, shutdownForm.getShutdownOperation()
            .equals(ShutdownForm.SHUTDOWN) ? PolicyConstants.PERM_SHUTDOWN : PolicyConstants.PERM_RESTART, request);
        if (shutdownForm.getAlreadyPerforming()) {
            shutdownForm.setReferer(CoreUtil.getReferer(request));
            StoppableTimer timer = (StoppableTimer) CoreServlet.getServlet().getServletContext().getAttribute(StoppableTimer.NAME);
            timer.cancelTimerTask(ShutdownTimerTask.NAME);
            GlobalWarningManager.getInstance().removeGlobalWarning((HttpSession) null, "shutdown.global.warning.message");
            shutdownForm.setAlreadyPerforming(false);
            return mapping.findForward("refresh");
        } else {
            if (shutdownForm.isImmediate()) {
                // for an imediate shut down.
                return mapping.findForward("confirmImmediate");
            } else {
                ActionForward fwd = mapping.findForward("confirmTimed");
                return new ActionForward(CoreUtil.addParameterToPath(fwd.getPath(),
                    "arg0",
                    new SimpleDateFormat("HH:mm").format(new Date(System.currentTimeMillis()
                        + (Integer.parseInt(shutdownForm.getShutdownDelay()) * 60 * 1000)))), fwd.getRedirect());
            }
        }
    }

    private void performShutdown(HttpServletRequest request, ShutdownForm shutdownForm) throws Exception {
        final boolean restart = "restart".equals(shutdownForm.getShutdownOperation());
        if (restart) {
            if (!ContextHolder.getContext().isRestartAvailableMode()) {
                ActionMessages msgs = new ActionMessages();
                msgs.add(Globals.ERROR_KEY, new ActionMessage("server.not.service"));
                saveErrors(request, msgs);
            } else {
                doShutdown(request, shutdownForm, restart);
            }
        } else {
            doShutdown(request, shutdownForm, restart);
        }
    }

    private void doShutdown(HttpServletRequest request, ShutdownForm shutdownForm, final boolean restart) {
        StoppableTimer timer = (StoppableTimer) CoreServlet.getServlet().getServletContext().getAttribute(StoppableTimer.NAME);
        ShutdownTimerTask stt = new ShutdownTimerTask(restart, Integer.parseInt(shutdownForm.getShutdownDelay()));
        timer.schedule(ShutdownTimerTask.NAME, stt, stt.getDelay());
        GlobalWarningManager.getInstance().addMultipleGlobalWarning(new GlobalWarning(GlobalWarning.ALL_USERS, new BundleActionMessage("setup",
            "shutdown.global.warning.message",
            stt.getShutDownTimeString()), DismissType.NO_DISMISS));
        request.getSession().setAttribute(Constants.RESTARTING, Boolean.valueOf(restart));
    }

    public int getNavigationContext(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return SessionInfo.MANAGEMENT_CONSOLE_CONTEXT | SessionInfo.SETUP_CONSOLE_CONTEXT;
    }
}
