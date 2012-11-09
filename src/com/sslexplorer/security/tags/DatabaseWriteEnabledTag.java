package com.sslexplorer.security.tags;

import javax.servlet.jsp.tagext.TagSupport;

import com.sslexplorer.core.UserDatabaseManager;
import com.sslexplorer.security.LogonControllerFactory;
import com.sslexplorer.security.User;
import com.sslexplorer.security.UserDatabase;

public class DatabaseWriteEnabledTag extends TagSupport {

	boolean databaseWriteEnabled = false;
	
	public DatabaseWriteEnabledTag() {
	}
	
	public int doStartTag() {
        UserDatabase udb;
        try {
            User user = LogonControllerFactory.getInstance().getUser(pageContext.getSession(), null);
            udb = UserDatabaseManager.getInstance().getUserDatabase(user.getRealm().getResourceId());
        } catch (Exception e1) {
            return SKIP_BODY;
        } 

	    if(!udb.supportsAccountCreation()) {
	      return databaseWriteEnabled ? SKIP_BODY : EVAL_BODY_INCLUDE;
	    }
	    
		return databaseWriteEnabled ? EVAL_BODY_INCLUDE : SKIP_BODY;
	}
	
	public void setDatabaseWriteEnabled(boolean databaseWriteEnabled) {
	    this.databaseWriteEnabled = databaseWriteEnabled;
	}
}
