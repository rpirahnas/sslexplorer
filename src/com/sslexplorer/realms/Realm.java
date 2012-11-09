package com.sslexplorer.realms;

import com.sslexplorer.policyframework.Resource;

/**
 * Interface for realms.
 */
public interface Realm extends Resource {
    
    /**
     * @return String
     */
    public String getType();
    
    /**
     * @param type
     */
    public void setType(String type);
    
//    /**
//     * @return String
//     */
//    public UserDatabase getUserDatabase();
//    
//    /**
//     * @param userDatabase
//     */
//    public void setUserDatabase(UserDatabase userDatabase);

}
