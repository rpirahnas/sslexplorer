package com.sslexplorer.install.forms;

import java.util.Arrays;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;

import com.sslexplorer.boot.PropertyClass;
import com.sslexplorer.boot.PropertyClassManager;
import com.sslexplorer.core.CoreServlet;
import com.sslexplorer.core.UserDatabaseManager;
import com.sslexplorer.jdbc.JDBCUserDatabase;
import com.sslexplorer.properties.Property;
import com.sslexplorer.properties.PropertyItem;
import com.sslexplorer.properties.impl.realms.RealmKey;
import com.sslexplorer.properties.impl.realms.RealmProperties;
import com.sslexplorer.realms.DefaultRealm;
import com.sslexplorer.realms.Realm;
import com.sslexplorer.security.UserDatabase;
import com.sslexplorer.security.UserDatabaseDefinition;
import com.sslexplorer.wizard.AbstractWizardSequence;
import com.sslexplorer.wizard.forms.AbstractWizardPropertiesForm;


/**
 * Implementation of {@link AbstractWizardPropertiesForm} that allows the
 * installer to configure the selected user database.
 */
public class ConfigureUserDatabaseForm extends AbstractWizardPropertiesForm {
    
    
    final static Log log = LogFactory.getLog(ConfigureUserDatabaseForm.class);
    
    private UserDatabaseDefinition userDatabaseDefinition;

    /**
     * Constructor.
     *
     */
    public ConfigureUserDatabaseForm() {
        super(Arrays.asList(new PropertyClass[] { PropertyClassManager.getInstance().getPropertyClass(RealmProperties.NAME) } ));
    }

    /* (non-Javadoc)
     * @see com.sslexplorer.wizard.forms.AbstractWizardPropertiesForm#getParentCategory()
     */
    public int getParentCategory() {
        return userDatabaseDefinition.getInstallationCategory();
    }

    /* (non-Javadoc)
     * @see com.sslexplorer.wizard.forms.AbstractWizardPropertiesForm#getNextAvailable()
     */
    public boolean getNextAvailable() {
        return true;
    }

    /* (non-Javadoc)
     * @see com.sslexplorer.wizard.forms.AbstractWizardPropertiesForm#getPreviousAvailable()
     */
    public boolean getPreviousAvailable() {
        return true;
    }

    /* (non-Javadoc)
     * @see com.sslexplorer.wizard.forms.AbstractWizardPropertiesForm#getFinishAvailable()
     */
    public boolean getFinishAvailable() {
        return false;
    }

    /* (non-Javadoc)
     * @see com.sslexplorer.wizard.forms.AbstractWizardPropertiesForm#getPage()
     */
    public String getPage() {
        return "/WEB-INF/jsp/content/install/configureUserDatabase.jspf";
    }

    /* (non-Javadoc)
     * @see com.sslexplorer.wizard.forms.AbstractWizardPropertiesForm#getResourcePrefix()
     */
    public String getResourcePrefix() {
        return "installation.configureUserDatabase";
    }

    /* (non-Javadoc)
     * @see com.sslexplorer.wizard.forms.AbstractWizardPropertiesForm#getResourceBundle()
     */
    public String getResourceBundle() {
        return "install";
    }

    /* (non-Javadoc)
     * @see com.sslexplorer.wizard.forms.AbstractWizardPropertiesForm#getPageName()
     */
    public String getPageName() {
        return "configureUserDatabase";
    }

    /* (non-Javadoc)
     * @see com.sslexplorer.wizard.forms.AbstractWizardPropertiesForm#getFocussedField()
     */
    public String getFocussedField() {
        return "";
    }

    /* (non-Javadoc)
     * @see com.sslexplorer.wizard.forms.AbstractWizardPropertiesForm#reset(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
        
        /* Determine the currently selected user database so it can be used to determine the
         * category to display
         */
        AbstractWizardSequence seq = getWizardSequence(request);
        String udbName = (String)seq.getAttribute(SelectUserDatabaseForm.ATTR_USER_DATABASE, JDBCUserDatabase.DATABASE_TYPE);
        userDatabaseDefinition = UserDatabaseManager.getInstance().getUserDatabaseDefinition(udbName);
    }

    public boolean getAutocomplete() {
        return false;
    }

    /* (non-Javadoc)
     * @see com.sslexplorer.wizard.forms.AbstractWizardPropertiesForm#init(com.sslexplorer.wizard.AbstractWizardSequence)
     */
    public void init(AbstractWizardSequence sequence, HttpServletRequest request) throws Exception {
        super.init(sequence, request);
    }

    /* (non-Javadoc)
     * @see com.sslexplorer.wizard.forms.AbstractWizardPropertiesForm#apply(com.sslexplorer.wizard.AbstractWizardSequence)
     */
    public void apply(AbstractWizardSequence sequence) throws Exception {
        // First shutdown the user databae
        super.apply(sequence);
        
        // Get the chosen user database
        UserDatabase currentUdb = (UserDatabase)sequence.getAttribute(SelectUserDatabaseForm.ATTR_USER_DATABASE_INSTANCE, null);
        
        // The realm is only available in the user database once it has been opened
        Realm realm = currentUdb.getRealm();
        if(realm == null) {
        	Calendar now = Calendar.getInstance();
            realm = new DefaultRealm((String)sequence.getAttribute(SelectUserDatabaseForm.ATTR_USER_DATABASE, null), 1, UserDatabaseManager.DEFAULT_REALM_NAME, UserDatabaseManager.DEFAULT_REALM_DESCRIPTION, now, now);        	
        }

        /* The properties have to be persisted now so the
         * user database uses the next settings. 
         */ 
        PropertyItem[] items = getPropertyItems();
        for(int i = 0 ; i < items.length; i++) {
        	Property.setProperty(new RealmKey(items[i].getDefinition().getName(), realm), items[i].getPropertyValue().toString(), null);            
        }
        
        // Now try and open the database to make sure the configuration is correct
        if(currentUdb.isOpen()) {
        	currentUdb.close();
        }
    	currentUdb.open(CoreServlet.getServlet(), realm);
    }

    public int getStepIndex() {
        return 2;
    }
}
