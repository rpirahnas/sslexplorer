
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
			
package com.sslexplorer.keystore.wizards.types;

import java.io.File;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import com.sslexplorer.boot.ContextKey;
import com.sslexplorer.boot.KeyStoreManager;
import com.sslexplorer.core.CoreAttributeConstants;
import com.sslexplorer.core.CoreEvent;
import com.sslexplorer.core.CoreEventConstants;
import com.sslexplorer.core.CoreServlet;
import com.sslexplorer.keystore.wizards.AbstractKeyStoreImportType;
import com.sslexplorer.properties.Property;
import com.sslexplorer.security.SessionInfo;
import com.sslexplorer.wizard.AbstractWizardSequence;


/**
 * Implementation of a {@link com.sslexplorer.keystore.wizards.AbstractKeyStoreImportType}
 * that imports a root server certifcate.
 */
public class RootServerCertificateImportType extends AbstractKeyStoreImportType {
    /**
     * Constant for importing a root certificate for the server certificate 
     */
    public final static String ROOT_SERVER_CERTIFICATE = "rootServerCertificate";

    /**
     * Constructor.
     */
    public RootServerCertificateImportType() {
        super(ROOT_SERVER_CERTIFICATE, "keystore", false, true, 20);
    }

    /* (non-Javadoc)
     * @see com.sslexplorer.keystore.wizards.AbstractKeyStoreImportType#doInstall(java.io.File, java.lang.String, java.lang.String, com.sslexplorer.wizard.AbstractWizardSequence)
     */
    public void doInstall(File file, String alias, String passphrase, AbstractWizardSequence seq, SessionInfo sessionInfo) throws Exception {
        KeyStoreManager mgr = KeyStoreManager.getInstance(KeyStoreManager.DEFAULT_KEY_STORE);
        String pw = Property.getProperty(new ContextKey("webServer.keystore.sslCertificate.password"));
        mgr.importCert(alias, file, pw);
        Certificate certif = mgr.getCertificate(alias);

        CoreEvent coreEvent = new CoreEvent(this, CoreEventConstants.KEYSTORE_ROOT_CERTIFICATE_IMPORTED, KeyStoreManager.DEFAULT_KEY_STORE, seq.getSession())
                        .addAttribute(CoreAttributeConstants.EVENT_ATTR_CERTIFICATE_ALIAS, alias)
                        .addAttribute(CoreAttributeConstants.EVENT_ATTR_CERTIFICATE_TYPE, certif.getType())
                        .addAttribute(CoreAttributeConstants.EVENT_ATTR_CERTIFICATE_HOSTNAME, KeyStoreManager.getX509CertificateEntity((X509Certificate)certif, "cn"))
                        .addAttribute(CoreAttributeConstants.EVENT_ATTR_CERTIFICATE_ORGANISATIONAL_UNIT, KeyStoreManager.getX509CertificateEntity((X509Certificate)certif, "ou"))
                        .addAttribute(CoreAttributeConstants.EVENT_ATTR_CERTIFICATE_COMPANY, KeyStoreManager.getX509CertificateEntity((X509Certificate)certif, "o"))
                        .addAttribute(CoreAttributeConstants.EVENT_ATTR_CERTIFICATE_STATE, KeyStoreManager.getX509CertificateEntity((X509Certificate)certif, "st"))
                        .addAttribute(CoreAttributeConstants.EVENT_ATTR_CERTIFICATE_LOCATION, KeyStoreManager.getX509CertificateEntity((X509Certificate)certif, "l"))
                        .addAttribute(CoreAttributeConstants.EVENT_ATTR_CERTIFICATE_COUNTRY_CODE, KeyStoreManager.getX509CertificateEntity((X509Certificate)certif, "c"));
        CoreServlet.getServlet().fireCoreEvent(coreEvent);
    }

}
