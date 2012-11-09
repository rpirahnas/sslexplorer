package com.sslexplorer.policyframework.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;

import com.sslexplorer.boot.PropertyList;
import com.sslexplorer.input.MultiSelectSelectionModel;
import com.sslexplorer.policyframework.AccessRights;
import com.sslexplorer.policyframework.PolicyDatabaseFactory;
import com.sslexplorer.policyframework.Resource;
import com.sslexplorer.security.SessionInfo;
import com.sslexplorer.tabs.TabModel;

/**
 * Form for editing a {@link AccessRights}.
 */
public class AccessRightsForm extends AbstractResourceForm<AccessRights> implements TabModel {

    final static Log log = LogFactory.getLog(PoliciesForm.class);

    private MultiSelectSelectionModel accessRightsModel;
    private PropertyList selectedAccessRights;

    private String selectedTab = "details";

    /**
     * Constructor
     */
    public AccessRightsForm() {
        super();
        selectedAccessRights = new PropertyList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.tabs.TabModel#getTabCount()
     */
    public int getTabCount() {
        return 3;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.tabs.TabModel#getTabName(int)
     */
    public String getTabName(int idx) {
        switch (idx) {
            case 0:
                return "details";
            case 1:
                return "permissions";
            default:
                return "policies";
        }
    }

    public String getTabTitle(int idx) {
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.tabs.TabModel#getSelectedTab()
     */
    public String getSelectedTab() {
        return selectedTab;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.tabs.TabModel#setSelectedTab(java.lang.String)
     */
    public void setSelectedTab(String selectedTab) {
        this.selectedTab = selectedTab;
    }

    public void reset(ActionMapping mapping, HttpServletRequest request) {
        super.reset(mapping, request);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.tabs.TabModel#getTabBundle(int)
     */
    public String getTabBundle(int idx) {
        return null;
    }

    @Override
    public void applyToResource() throws Exception {
    }

    /**
     * Get the Access Rights selection model
     * 
     * @return MultiSelectSelectionModel
     */
    public MultiSelectSelectionModel getAccessRightsModel() {
        return accessRightsModel;
    }

    /**
     * @param accessRightsModel
     */
    public void setAccessRightsModel(MultiSelectSelectionModel accessRightsModel) {
        this.accessRightsModel = accessRightsModel;
    }

    /**
     * @return String
     */
    public String getSelectedAccessRights() {
        return selectedAccessRights.getAsTextFieldText();
    }

    /**
     * @return PropertyList
     */
    public PropertyList getSelectedAccessRightsList() {
        return selectedAccessRights;
    }

    /**
     * @param selectedAccessRights
     */
    public void setSelectedAccessRights(String selectedAccessRights) {
        this.selectedAccessRights.setAsTextFieldText(selectedAccessRights);
    }

    /**
     * @param selectedAccessRights
     */
    public void setSelectedAccessRights(PropertyList selectedAccessRights) {
        this.selectedAccessRights = selectedAccessRights;
    }

    @Override
    public Resource getResourceByName(String name, SessionInfo session) throws Exception {
        return PolicyDatabaseFactory.getInstance().getAccessRightsByName(name, session.getUser().getRealm().getRealmID());
    }
}
