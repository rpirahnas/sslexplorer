
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
			
package com.sslexplorer.vfs.store.site;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.sslexplorer.boot.ContextHolder;
import com.sslexplorer.properties.Pair;
import com.sslexplorer.properties.PairListDataSource;

public class SiteIconsListDataSource implements PairListDataSource {

	public List getValues(HttpServletRequest request) {
		File dir = new File(new File(ContextHolder.getContext().getConfDirectory(), "site"), "icons");
		List l = new ArrayList();
		l.add(new Pair("default", "Default"));
		if(dir.exists() && dir.canRead()) {
			String[] names = dir.list();
			for(int i = 0 ; i < names.length; i++) {
				l.add(new Pair(names[i], names[i]));
			}
		}
		return l;
	}
}
