
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
			
package com.sslexplorer.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sslexplorer.policyframework.NoPermissionException;
import com.sslexplorer.policyframework.Permission;
import com.sslexplorer.policyframework.PolicyUtil;
import com.sslexplorer.policyframework.Principal;
import com.sslexplorer.policyframework.Resource;
import com.sslexplorer.policyframework.ResourceType;
import com.sslexplorer.security.AccountLock;
import com.sslexplorer.security.AccountLockedException;
import com.sslexplorer.security.InvalidLoginCredentialsException;
import com.sslexplorer.security.SessionInfo;
import com.sslexplorer.security.User;

/**
 */
public class PolicyServiceImpl implements PolicyService {
    private static final PolicyServiceImpl POLICY_SERVICE = new PolicyServiceImpl();
    
    /**
     * @return PolicyService
     */
    public static PolicyService getInstance() {
        return POLICY_SERVICE;
    }

    public boolean canLogin(Principal principal) throws Exception {
        return PolicyUtil.canLogin(principal);
    }

    public void checkLogin(User user) throws InvalidLoginCredentialsException, AccountLockedException {
        PolicyUtil.checkLogin(user);
    }

    public void checkPermission(ResourceType<? extends Resource> resourceType, Permission permission, HttpServletRequest request)
                    throws NoPermissionException {
        PolicyUtil.checkPermission(resourceType, permission, request);
    }

    public void checkPermission(ResourceType<? extends Resource> resourceType, Permission permission, HttpSession session)
                    throws NoPermissionException {
        PolicyUtil.checkPermission(resourceType, permission, session);
    }

    public void checkPermission(ResourceType<? extends Resource> resourceType, Permission permission, SessionInfo sessionInfo)
                    throws NoPermissionException {
        PolicyUtil.checkPermission(resourceType, permission, sessionInfo);
    }

    public void checkPermission(ResourceType<? extends Resource> resourceType, Permission permission, User user)
                    throws NoPermissionException {
        PolicyUtil.checkPermission(resourceType, permission, user);
    }

    public void checkPermissions(ResourceType<?> resourceType, Permission[] permissions, HttpServletRequest request)
                    throws NoPermissionException {
        PolicyUtil.checkPermissions(resourceType, permissions, request);
    }

    public boolean isEnabled(User user) throws Exception {
        return PolicyUtil.isEnabled(user);
    }

    public void setEnabled(User user, boolean enabled, AccountLock lock, SessionInfo session) throws Exception {
        PolicyUtil.setEnabled(user, enabled, lock, session);
    }
}