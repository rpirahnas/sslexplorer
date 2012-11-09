package com.sslexplorer.policyframework.forms;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sslexplorer.core.forms.CoreForm;
import com.sslexplorer.policyframework.Policy;
import com.sslexplorer.policyframework.Resource;

public class ResourceInformationForm extends CoreForm {
    final static Log log = LogFactory.getLog(ResourceInformationForm.class);
    
    private Resource resource;
    private Collection<Policy> policies;
    
    public void initialise(Resource resource, Collection<Policy> policies) {
    	this.resource = resource;
        this.policies = policies;
    }
    
    public Resource getResource() {
    	return resource;
    }
    
    public int getPolicyCount() {
        return policies == null ? 0 : policies.size();
    }
    
    public Collection<Policy> getPolicies() {
        return policies;
    }
}
