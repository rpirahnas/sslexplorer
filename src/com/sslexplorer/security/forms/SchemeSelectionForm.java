
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
			
package com.sslexplorer.security.forms;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;

import com.sslexplorer.core.forms.CoreForm;

public class SchemeSelectionForm extends CoreForm {
    
    private List authenticationSchemes;
    private String selectedAuthenticationScheme;
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        selectedAuthenticationScheme = null;
        authenticationSchemes = null;
    }

    public List getAuthenticationSchemes() {
      return authenticationSchemes;    
    }

    public void setAuthenticationSchemes(List authenticationSchemes) {
      this.authenticationSchemes = authenticationSchemes;    
    }
    
    public void setSelectedAuthenticationScheme(String selectedAuthenticationScheme) {
      this.selectedAuthenticationScheme = selectedAuthenticationScheme;
    }
    
    public String getSelectedAuthenticationScheme() {
      return selectedAuthenticationScheme;
    }
}
