package com.sslexplorer.properties.forms;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.sslexplorer.core.CoreUtil;
import com.sslexplorer.policyframework.OwnedResource;
import com.sslexplorer.policyframework.ResourceItem;
import com.sslexplorer.properties.PropertyProfile;

public class PropertyProfileItem extends ResourceItem<PropertyProfile> {

	public PropertyProfileItem(PropertyProfile resource, List policies) {
		super(resource, policies);
	}

    public String getSmallIconPath(HttpServletRequest request) {
        if(((OwnedResource)getResource()).getOwnerUsername() != null) {
            return CoreUtil.getThemePath(request.getSession()) + "/images/personal.gif";            
        }
        else {
            return CoreUtil.getThemePath(request.getSession()) + "/images/global.gif";          
        }
    }

    public String getLargeIconAdditionalIcon(HttpServletRequest request) {
		if(((OwnedResource)getResource()).getOwnerUsername() != null) {
	        return CoreUtil.getThemePath(request.getSession()) + "/images/personal.gif";			
		}
		else {
	        return CoreUtil.getThemePath(request.getSession()) + "/images/global.gif";			
		}
	}

	public String getLargeIconPath(HttpServletRequest request) {
        return CoreUtil.getThemePath(request.getSession()) + "/images/actions/profileLarge.gif";
	}

}
