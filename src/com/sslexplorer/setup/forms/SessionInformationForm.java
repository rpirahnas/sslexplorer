package com.sslexplorer.setup.forms;

import com.sslexplorer.core.forms.CoreForm;
import com.sslexplorer.security.SessionInfo;

/**
 */
public class SessionInformationForm extends CoreForm {
    private SessionInfo session;
    
    /**
     * @param session
     */
    public void initialise(SessionInfo session) {
        this.session = session;
    }
    
    /**
     * @return SessionInfo
     */ 
    public SessionInfo getSession() {
        return session;
    }
}
