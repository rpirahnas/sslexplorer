package com.sslexplorer.policyframework.forms;

import com.sslexplorer.core.forms.CoreForm;
import com.sslexplorer.policyframework.Principal;

/**
 */
public class PrincipalInformationForm extends CoreForm {
    private Principal principal;
    
    /**
     * @param principal
     */
    public void initialise(Principal principal) {
        this.principal = principal;
    }
    
    /**
     * @return Principal
     */ 
    public Principal getPrincipal() {
        return principal;
    }
}