package com.sslexplorer.vfs.store.site;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.vfs.FileObject;

import com.sslexplorer.boot.ContextHolder;
import com.sslexplorer.extensions.ExtensionBundle;
import com.sslexplorer.policyframework.LaunchSession;
import com.sslexplorer.security.PasswordCredentials;
import com.sslexplorer.vfs.AbstractStore;
import com.sslexplorer.vfs.AbstractVFSMount;
import com.sslexplorer.vfs.FileObjectVFSResource;
import com.sslexplorer.vfs.VFSMount;
import com.sslexplorer.vfs.VFSResource;
import com.sslexplorer.vfs.utils.URI;
import com.sslexplorer.vfs.utils.URI.MalformedURIException;
import com.sslexplorer.vfs.webdav.DAVAuthenticationRequiredException;
import com.sslexplorer.vfs.webdav.DAVUtilities;

/**
 * Store for site specific resources such as custom icons.
 */
public class SiteStore extends AbstractStore {
	
	private File siteDir;

	/**
	 * Constructor.
	 */
	public SiteStore() {
		super("site", "UTF-8");
		siteDir = new File(ContextHolder.getContext().getConfDirectory(), "site");
		if(!siteDir.exists()) {
			siteDir.mkdirs();
		} 
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sslexplorer.vfs.webdav.AbstractStore#getMountFromString(java.lang.String,
	 *      com.sslexplorer.security.SessionInfo)
	 */
	public VFSMount getMountFromString(String mountName, LaunchSession launchSession) throws DAVAuthenticationRequiredException {
		if(mountName.equals("icons")) {
			return new SiteIconsMount();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.sslexplorer.vfs.VFSStore#getMountNames()
	 */
	public Collection<String> getMountNames() throws Exception {
		return Arrays.asList(new String[] { "icons" });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sslexplorer.vfs.webdav.DAVStore#validateUserEnteredPath(java.lang.String)
	 */
	public String createURIFromPath(String path) throws IllegalArgumentException {
		throw new IllegalArgumentException();
	}

	class SiteIconsMount extends AbstractVFSMount {

		private ExtensionBundle bundle;

		SiteIconsMount()  {
			super(null, SiteStore.this, "icons", false);
		}

		public VFSResource getResource(String path, PasswordCredentials requestCredentials)
						throws IOException {
			VFSResource parent = null;
			if (path.equals("")) {
				parent = getStore().getStoreResource();
			}
			return new FileObjectVFSResource(getLaunchSession(), this, 
							parent,
							path,
							getStore().getRepository(),
							requestCredentials);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.sslexplorer.vfs.AbstractVFSMount#getRootVFSURI(java.lang.String)
		 */
		public URI getRootVFSURI(String charset) throws MalformedURIException {
			File baseDir = new File(siteDir, "icons");
			if(!baseDir.exists()) {
				baseDir.mkdirs();
			}
			return new URI(baseDir.toURI().toString());
		}

		protected FileObject createVFSFileObject(String path, PasswordCredentials credentials) throws IOException {
			URI uri = getRootVFSURI();
			uri.setPath(DAVUtilities.concatenatePaths(uri.getPath(), path));
			FileObject root = getStore().getRepository().getFileSystemManager().resolveFile(uri.toString());
			return root;
		}
	}
}
