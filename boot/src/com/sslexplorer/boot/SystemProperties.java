package com.sslexplorer.boot;

/**
 * Facade for retrieving system properties. If SSL-Explorer has been
 * rebranded, it may be a requirement that all system properties
 * are not prefixed with <b>sslexplorer</b>. 
 * <p>
 * An alternative system property prefix may be set using the
 * boot.propertyPrefix system property.
 * <p>
 * When this prefix is set, any system property names that begin
 * with <b>sslexplorer.[propertyName]</b> will be changed to use 
 * <b>[prefix].[propertyName]</b>.
 */
public class SystemProperties {
    
    /**
     * The system property prefix. 
     */
    private static String systemPropertyPrefix = null;
    
    /**
     * Set the system property prefix
     * 
     * @param systemPropertyPrefix
     */
    public static void setPrefix(String systemPropertyPrefix) {
        SystemProperties.systemPropertyPrefix = systemPropertyPrefix;
    }
    
    /**
     * Get the value of a system property, returning <code>null</code> if 
     * no such property exists.
     * 
     * @param propertyName property name
     * @return property value or <code>null</code> if no such property exists
     */
    public static String get(String propertyName) {
        return get(propertyName, null);
    } 
    
    /**
     * Get the value of a system property, returning the supplied default
     * if no such property exists
     * 
     * @param propertyName property name
     * @param defaultValue default property value
     * @return property value or <code>defaultValue</code> if no such property exists
     */
    public static String get(String propertyName, String defaultValue) {
        if(propertyName.startsWith("sslexplorer.") && systemPropertyPrefix != null) {
            return System.getProperty(systemPropertyPrefix + "." + propertyName.substring(12), defaultValue);
        }
        return System.getProperty(propertyName, defaultValue);
    }
}
