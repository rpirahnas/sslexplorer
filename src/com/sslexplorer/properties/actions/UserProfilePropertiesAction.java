
				/*
 *  SSL-Explorer
 *
 *  Copyright (C) 2003-2006 3SP LTD. All Rights Reserved
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2 of
 *  the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
			
package com.sslexplorer.properties.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.sslexplorer.boot.AbstractPropertyKey;
import com.sslexplorer.boot.PropertyDefinition;
import com.sslexplorer.core.CoreServlet;
import com.sslexplorer.policyframework.Permission;
import com.sslexplorer.policyframework.PolicyConstants;
import com.sslexplorer.policyframework.ResourceUtil;
import com.sslexplorer.properties.ProfilesFactory;
import com.sslexplorer.properties.PropertyProfile;
import com.sslexplorer.properties.forms.AbstractPropertiesForm;
import com.sslexplorer.properties.forms.ProfilePropertiesForm;
import com.sslexplorer.properties.impl.profile.ProfilePropertyKey;
import com.sslexplorer.security.Constants;
import com.sslexplorer.security.SessionInfo;

public class UserProfilePropertiesAction extends AbstractProfilePropertiesAction {
    static Log log = LogFactory.getLog(UserProfilePropertiesAction.class);

    public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                    HttpServletResponse response) throws Exception {
        ((ProfilePropertiesForm)form).setProfileScope(Constants.SCOPE_PERSONAL);
        return super.unspecified(mapping, form, request, response);
    }  

    @Override
    public ActionForward commit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ProfilePropertiesForm f = (ProfilePropertiesForm) form;     
        PropertyProfile profile = ProfilesFactory.getInstance().getPropertyProfile(f.getSelectedPropertyProfile());
        ResourceUtil.checkResourceManagementRights(profile, getSessionInfo(request), new Permission[] {PolicyConstants.PERM_CHANGE});
        return super.commit(mapping, form, request, response);
    }

    public int getNavigationContext(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        return SessionInfo.USER_CONSOLE_CONTEXT;
    }

    public AbstractPropertyKey createKey(PropertyDefinition definition, AbstractPropertiesForm form, SessionInfo sessionInfo) {
        ProfilePropertiesForm f = (ProfilePropertiesForm)form;
        return new ProfilePropertyKey(f.getSelectedPropertyProfile(), sessionInfo.getUser().getPrincipalName(), definition.getName(), sessionInfo.getUser().getRealm().getResourceId());
    }
}