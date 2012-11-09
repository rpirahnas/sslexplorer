
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
			
package com.sslexplorer.core;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;

import javax.security.auth.login.Configuration;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.RequestProcessor;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.MessageResourcesFactory;

import com.maverick.ssl.SSLTransportFactory;
import com.maverick.ssl.SSLTransportImpl;
import com.sslexplorer.agent.AgentRequestHandler;
import com.sslexplorer.boot.BootProgressMonitor;
import com.sslexplorer.boot.ContextHolder;
import com.sslexplorer.boot.ContextListener;
import com.sslexplorer.boot.PropertyClassManager;
import com.sslexplorer.boot.PropertyDefinitionCategory;
import com.sslexplorer.boot.SystemProperties;
import com.sslexplorer.extensions.ApplicationParameters;
import com.sslexplorer.extensions.ExtensionException;
import com.sslexplorer.extensions.forms.ConfigureExtensionsForm;
import com.sslexplorer.extensions.store.ExtensionStore;
import com.sslexplorer.jdbc.JDBCUserDatabase;
import com.sslexplorer.jdbc.hsqldb.EmbeddedHSQLDBServer;
import com.sslexplorer.keystore.wizards.KeyStoreImportTypeManager;
import com.sslexplorer.keystore.wizards.types.ReplyFromCAImportType;
import com.sslexplorer.keystore.wizards.types.RootServerCertificateImportType;
import com.sslexplorer.keystore.wizards.types.ServerAuthenticationKeyImportType;
import com.sslexplorer.keystore.wizards.types.TrustedServerCertificateImportType;
import com.sslexplorer.keystore.wizards.types._3SPPurchaseImportType;
import com.sslexplorer.language.Language;
import com.sslexplorer.language.LanguagePackDefinition;
import com.sslexplorer.language.LanguagePackManager;
import com.sslexplorer.navigation.NavigationBar;
import com.sslexplorer.navigation.NavigationManager;
import com.sslexplorer.notification.Notifier;
import com.sslexplorer.notification.agent.AgentMessageSink;
import com.sslexplorer.notification.smtp.SMTPMessageSink;
import com.sslexplorer.policyframework.PolicyDatabaseFactory;
import com.sslexplorer.policyframework.ResourceStack;
import com.sslexplorer.properties.ProfilesFactory;
import com.sslexplorer.properties.Property;
import com.sslexplorer.properties.impl.policyattributes.PolicyAttributes;
import com.sslexplorer.properties.impl.profile.ProfileProperties;
import com.sslexplorer.properties.impl.realms.RealmProperties;
import com.sslexplorer.properties.impl.resource.ResourceAttributes;
import com.sslexplorer.properties.impl.systemconfig.SystemConfigKey;
import com.sslexplorer.properties.impl.systemconfig.SystemConfiguration;
import com.sslexplorer.properties.impl.userattributes.UserAttributes;
import com.sslexplorer.requesthandler.test.TestRequestHandler;
import com.sslexplorer.rss.FeedManager;
import com.sslexplorer.security.AuthenticationModuleManager;
import com.sslexplorer.security.Constants;
import com.sslexplorer.security.EmbeddedClientAuthenticationModule;
import com.sslexplorer.security.HTTPAuthenticationModule;
import com.sslexplorer.security.LogonControllerFactory;
import com.sslexplorer.security.PasswordAuthenticationModule;
import com.sslexplorer.security.PersonalQuestionsAuthenticationModule;
import com.sslexplorer.security.SystemDatabaseFactory;
import com.sslexplorer.security.UserDatabase;
import com.sslexplorer.security.UserDatabaseDefinition;
import com.sslexplorer.security.WebDAVAuthenticationModule;
import com.sslexplorer.services.CoreEventService;
import com.sslexplorer.services.CoreEventServiceImpl;
import com.sslexplorer.services.MessageResourceService;
import com.sslexplorer.table.TableItemActionMenuTree;
import com.sslexplorer.tasks.timer.StoppableTimer;

/**
 * Main controlling servlet. This extends struts own
 * {@link org.apache.struts.action.ActionServlet}. Pretty much all SSL-Explorer
 * actions will come through here.
 * <p>
 * Many features in SSL-Explorer require access to this servlet as it manages
 * pages scripts, the logon controller, the policy database, the plugin manager,
 * the system database and the events system.
 * <p>
 * Only a single instance of this servlet will ever exist, a reference to which
 * is available through the static {@link #getServlet()} method.
 */
public class CoreServlet extends ActionServlet implements ContextListener, MessageResourceService {

    /**
     * 
     */
    private static final long serialVersionUID = 2175322073390766579L;

    final static Log log = LogFactory.getLog(CoreServlet.class);

    private static CoreServlet instance;

    private CoreMessageResources applicationStoreResources;

    private CoreEventService coreEventService;

    private StringBuffer tileConfigFiles = new StringBuffer();

    private List<CoreScript> pageScripts;

    private Notifier notifier;

    private EmbeddedHSQLDBServer dbServer;

    private boolean shuttingDown;

    private boolean devConfig;

    private Digester digester;

    private ModuleConfig moduleConfig;

    private boolean pastInitialisation;

    private BootProgressMonitor bootProgressMonitor;

    /**
     * Constructor
     */
    public CoreServlet() {
        super();
        instance = this;
        pageScripts = new ArrayList<CoreScript>();
        coreEventService = new CoreEventServiceImpl();
        NavigationManager.addMenuTree(new CoreMenuTree());
        NavigationManager.addMenuTree(new PageTaskMenuTree());
        NavigationManager.addMenuTree(new ToolBarMenuTree());
        NavigationManager.addMenuTree(new NavigationBar());
        NavigationManager.addMenuTree(new TableItemActionMenuTree());
        Configuration.setConfiguration(new CoreJAASConfiguration());
        addTileConfigurationFile("/WEB-INF/tiles-defs.xml");

        // Make sure that Http redirects are followed in places where we use URL
        // to connect to our website
        HttpURLConnection.setFollowRedirects(true);

    }

    /**
     * Add a database
     * 
     * @param databaseName database
     * @param file
     * @throws Exception
     */
    public void addDatabase(String databaseName, File file) throws Exception {
        if (log.isInfoEnabled())
            log.info("Adding database " + databaseName + " to folder " + file.getPath());
        dbServer.addDatabase(databaseName, file);
    }

    /**
     * Add a {@link CoreScript} that will be made available on every page. Note,
     * for the scripts to appear the theme must include the
     * <strong>explorer:pageScripts</strong> tag.
     * 
     * @param script script to add to all pages
     */
    public void addPageScript(CoreScript script) {
        pageScripts.add(script);
    }

    /**
     * Remove a {@link CoreScript} from those made available on every page.
     * 
     * @param script script to remove from all pages
     */
    public void removePageScript(CoreScript script) {
        pageScripts.remove(script);
    }

    /**
     * Return a list of all the currently registered page scripts.
     * 
     * @return list of page scripts
     */
    public List getPageScripts() {
        return pageScripts;
    }

    /**
     * Return a command separated list of tiles configuration files that are
     * currently in use.
     * 
     * @return list of tiles configuration files
     */
    public String getTilesConfigurationFiles() {
        return tileConfigFiles.toString();
    }

    /**
     * @return CoreEventService
     */
    public CoreEventService getCoreEventService() {
        return coreEventService;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.core.CoreEventManager#addCoreListener(com.sslexplorer.core.CoreListener)
     */
    public void addCoreListener(CoreListener listener) {
        coreEventService.addCoreListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.core.CoreEventManager#removeCoreListener(com.sslexplorer.core.CoreListener)
     */
    public void removeCoreListener(CoreListener listener) {
        coreEventService.removeCoreListener(listener);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.sslexplorer.core.CoreEventManager#fireCoreEvent(com.sslexplorer.core.CoreEvent)
     */
    public void fireCoreEvent(CoreEvent evt) {
        coreEventService.fireCoreEvent(evt);
    }

    public MessageResources getMessageResources(String key) {
        return CoreUtil.getMessageResources(getServletContext(), key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.ActionServlet#initOther()
     */
    protected void initOther() throws ServletException {
        super.initOther();

        // Some sanity checks
        checkDevEnvironment();

        // Intiailise notifier
        initNotifier();

        // Listen for core events
        addCoreListener(new DefaultCoreListener());

        // Initialise plugin manager
        if (log.isInfoEnabled())
            log.info("Creating extension manager.");

        /*
         * Load the application store resources - these may be changed by the
         * application store when new descriptors are loaded
         */

        MessageResourcesFactory messageResourcesFactory = new CoreMessageResourcesFactory(getClass().getClassLoader());
        applicationStoreResources = (CoreMessageResources) messageResourcesFactory
                        .createResources("com.sslexplorer.applications.store.ApplicationResources");
        applicationStoreResources.setReturnNull(false);

        // finally setup the scheduler
        this.getServletContext().setAttribute(StoppableTimer.NAME, new StoppableTimer());
    }

    /**
     * Get the {@link CoreMessageResources} that contains all installed
     * extension message resource.
     * 
     * @return extension store message resources
     */
    public CoreMessageResources getExtensionStoreResources() {
        return applicationStoreResources;
    }

    /**
     * @param path
     */
    public void addTileConfigurationFile(String path) {
        if (log.isInfoEnabled())
            log.info("Adding tile configuration file " + path);
        if (tileConfigFiles.length() > 0)
            tileConfigFiles.append(',');
        tileConfigFiles.append(path);
    }

    /**
     * @param path
     */
    public void removeTileConfigurationFile(String path) {
        if (log.isInfoEnabled())
            log.info("Removing tile configuration file " + path);
        // TODO Why on earth are we using a string to store the list of tile
        // moduleConfig files?
        StringBuffer buf = new StringBuffer();
        StringTokenizer t = new StringTokenizer(tileConfigFiles.toString(), ",");
        while (t.hasMoreTokens()) {
            String p = t.nextToken();
            if (!p.equals(path)) {
                if (buf.length() > 0) {
                    buf.append(",");
                }
                buf.append(p);
            }
        }
        tileConfigFiles = buf;
        if (log.isInfoEnabled())
            log.info("New tile configuration path is " + tileConfigFiles.toString());
    }

    protected void activateCore() throws ServletException {

        // Add the default language definition
        LanguagePackDefinition pdef = new LanguagePackDefinition(null, "core");
        pdef.addLanguage(new Language(null, "en", "English"));
        LanguagePackManager.getInstance().addLanguagePackDefinition(pdef);

        // now the databases are registered we need a userdatabase setup so the
        // process can proceed.
        bootProgressMonitor.updateMessage("Initialising user database");
        bootProgressMonitor.updateProgress(50);
        UserDatabaseManager.getInstance().initialize(ContextHolder.getContext().isSetupMode());

        // Use the default system database if no other has been registered
        try {
            bootProgressMonitor.updateMessage("Initialising system database");
            bootProgressMonitor.updateProgress(55);
            SystemDatabaseFactory.getInstance().open(this);
        } catch (Exception e) {
            log.error("Failed to initialise system database.", e);
            throw new ServletException("Failed to initialise system database.", e);
        }

        // Policy database
        try {
            bootProgressMonitor.updateMessage("Initialising policy database");
            bootProgressMonitor.updateProgress(60);
            PolicyDatabaseFactory.getInstance().open(this);
            PolicyDatabaseFactory.getInstance().initAccessRights();
        } catch (Exception e) {
            log.error("Failed to initialise policy database.", e);
            throw new ServletException("Failed to initialise policy database.", e);
        }

        // Start the plugins
        bootProgressMonitor.updateMessage("Activating extensions");
        bootProgressMonitor.updateProgress(65);
        if ("true".equalsIgnoreCase(SystemProperties.get("sslexplorer.disableExtensions"))) {
            log.warn("Extension manager disabled, extension store wont be activated.");
        } else {
            try {
                ExtensionStore.getInstance().activate();
            } catch (ExtensionException ee) {
                throw new ServletException("Failed to activate extension store.", ee);
            }
        }

        // Register CONNECT handler
        bootProgressMonitor.updateMessage("Registering request handlers");
        bootProgressMonitor.updateProgress(75);
        if (!ContextHolder.getContext().isSetupMode()) {
            if (SystemProperties.get("sslexplorer.testing", "false").equals("true")) {
                ContextHolder.getContext().registerRequestHandler(new TestRequestHandler());
            }
            ContextHolder.getContext().registerRequestHandler(new AgentRequestHandler());
        }

        /*
         * If running in setup mode, we don't want to change any properties
         * until the wizard has finished
         */
        if (ContextHolder.getContext().isSetupMode())
            PropertyClassManager.getInstance().setAutoCommit(false);

        /*
         * Disable any property categories for user databases configuration if
         * not in setup mode
         */
        if (!ContextHolder.getContext().isSetupMode()) {
            bootProgressMonitor.updateMessage("Removing hidden categories");
            bootProgressMonitor.updateProgress(80);
            UserDatabase defaultUserDatabase = UserDatabaseManager.getInstance().getDefaultUserDatabase();
            if (defaultUserDatabase == null) {
                // throw new ServletException("There is no default userdatabase,
                // check log for details.");
            } else {
                log.info("The default user database is " + defaultUserDatabase.getDatabaseDescription());
                for (UserDatabaseDefinition def : UserDatabaseManager.getInstance().getUserDatabaseDefinitions()) {
                    if (def.getInstallationCategory() > 0) {
                        PropertyDefinitionCategory cat = PropertyClassManager.getInstance().getPropertyClass(RealmProperties.NAME)
                                        .getPropertyDefinitionCategory(def.getInstallationCategory());
                        log.info("Disabling user database configuration category " + def.getInstallationCategory() + " ("
                                        + cat.hashCode() + ")");
                        if (cat == null) {
                            log.error("No such category " + def.getInstallationCategory());
                        } else {
                            cat.setEnabled(false);
                        }
                    }
                }
            }
        }

        // Start the notifier
        try {
            notifier.start();
        } catch (Exception e) {
            log.error("Failed to start notifier.", e);
        }

        // Starting Updating RSS feeds

        if (!Property.getPropertyBoolean(new SystemConfigKey("ui.rssFeeds")) 
        		|| SystemProperties.get("sslexplorer.disable.rssFeeds", "false").equals("true")) {
            if (log.isInfoEnabled())
                log.info("RSS feeds disabled, not checking");
        } else {

            FeedManager.getInstance().startUpdating();
        }
    }

    protected void initNotifier() throws ServletException {

        File queueDir = new File(ContextHolder.getContext().getConfDirectory(), "queue");
        if (!queueDir.exists()) {
            if (!queueDir.mkdirs()) {
                throw new ServletException("Could not create message queue directory " + queueDir.getAbsolutePath());
            }
        }
        if (!queueDir.isDirectory()) {
            throw new ServletException("Message queue directory appears to not be a directory.");
        }
        if (!queueDir.canWrite() || !queueDir.canRead()) {
            throw new ServletException("Message queue directory " + queueDir.getAbsolutePath() + " has incorret permissions.");
        }
        try {
            notifier = new Notifier(queueDir);
        } catch (IOException e2) {
            log.error("Notifier failed to initialise.", e2);
            throw new ServletException(e2);
        }
        try {
            notifier.addSink(new SMTPMessageSink(), Property.getPropertyBoolean(new SystemConfigKey("smtp.startOnStartup")));
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Failed to add SMTP message sink.", e);
        }
        notifier.addSink(new AgentMessageSink(), true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.ActionServlet#initModuleMessageResources(org.apache.struts.config.ModuleConfig)
     */
    protected void initModuleMessageResources(final ModuleConfig config) throws ServletException {
        super.initModuleMessageResources(config);
        if (log.isInfoEnabled())
            log.info("Initialising extension message resources");
        getServletContext().setAttribute("applicationStore" + config.getPrefix(), applicationStoreResources);
    }

    /**
     * There will only ever been one instance of the servlet, this method allows
     * static access to it.
     * 
     * @return the core servlet instance
     */
    public static CoreServlet getServlet() {
        return instance;
    }

    /**
     * Get the notifier instance that may be used to send notification messages
     * to users, adminstrators etc.
     * 
     * @return notifier
     */
    public Notifier getNotifier() {
        return notifier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.GenericServlet#init()
     */
    public void init() throws ServletException {
        pastInitialisation = false;

        try {

            ContextHolder.getContext().addContextListener(this);

            if (SystemProperties.get("sslexplorer.disableNewSSLEngine", "false").equals("true"))
                SSLTransportFactory.setTransportImpl(SSLTransportImpl.class);

            // Init bouncy castle
            Class c = Class.forName("org.bouncycastle.jce.provider.BouncyCastleProvider");
            Security.insertProviderAt((Provider) c.newInstance(), 0);

            // Start the database
            // TODO this should be configurable
            dbServer = new EmbeddedHSQLDBServer("true".equalsIgnoreCase(SystemProperties.get("sslexplorer.hsqldb.tcpipServer",
                "false")));

            // Load the property classes
            PropertyClassManager.getInstance().registerPropertyClass(new ProfileProperties());
            PropertyClassManager.getInstance().registerPropertyClass(new SystemConfiguration());
            PropertyClassManager.getInstance().registerPropertyClass(new UserAttributes());
            PropertyClassManager.getInstance().registerPropertyClass(new PolicyAttributes());
            PropertyClassManager.getInstance().registerPropertyClass(new RealmProperties());
            PropertyClassManager.getInstance().registerPropertyClass(new ApplicationParameters());
            PropertyClassManager.getInstance().registerPropertyClass(new ResourceAttributes());

            // Load the property database and categories
            // Use the default system database if no other has been registered
            try {
                ProfilesFactory.getInstance().open(this);
            } catch (Exception e) {
                log.error("Failed to initialise property database.", e);
                throw new ServletException("Failed to initialise system database.", e);
            }

            bootProgressMonitor = ContextHolder.getContext().getBootProgressMonitor();

            // Initialise extensions
            bootProgressMonitor.updateMessage("Initialising extensions");
            bootProgressMonitor.updateProgress(10);
            initExtensionStore();

            // Initialise core
            bootProgressMonitor.updateMessage("Initialising core");
            bootProgressMonitor.updateProgress(20);
            initInternal();
            initOther();
            initServlet();

            getServletContext().setAttribute(Globals.ACTION_SERVLET_KEY, this);
            initModuleConfigFactory();
            // Initialize modules as needed
            ModuleConfig moduleConfig = initModuleConfig("", config);
            initCore();

            // Start extensions
            bootProgressMonitor.updateMessage("Starting extensions");
            bootProgressMonitor.updateProgress(30);
            startExtensions();

            // Update module configuration
            bootProgressMonitor.updateMessage("Updating configuration");
            bootProgressMonitor.updateProgress(40);
            initModuleMessageResources(moduleConfig);
            initModuleDataSources(moduleConfig);
            initModulePlugIns(moduleConfig);

            // Start extensions
            bootProgressMonitor.updateMessage("Activating core");
            bootProgressMonitor.updateProgress(45);
            activateCore();

            // do not freeze, we want to be able to dynamically change struts
            // configuration
            // moduleConfig.freeze();

            bootProgressMonitor.updateMessage("Finalising configuration");
            bootProgressMonitor.updateProgress(85);
            Enumeration names = getServletConfig().getInitParameterNames();
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                if (!name.startsWith("moduleConfig/")) {
                    continue;
                }
                String prefix = name.substring(6);
                moduleConfig = initModuleConfig(prefix, getServletConfig().getInitParameter(name));
                initModuleMessageResources(moduleConfig);
                initModuleDataSources(moduleConfig);
                initModulePlugIns(moduleConfig);
                // do not freeze, we want to be able to dynamically change
                // struts configuration
                // moduleConfig.freeze();
            }

            this.initModulePrefixes(this.getServletContext());
            this.destroyConfigDigester();

            fireCoreEvent(new CoreEvent(this, CoreEventConstants.SERVER_STARTED, null, null));

        } catch (UnavailableException ex) {
            throw ex;
        } catch (Throwable t) {

            // The follow error message is not retrieved from internal message
            // resources as they may not have been able to have been
            // initialized
            log.error("Failed to initialise core.", t);
            throw new UnavailableException(t.getMessage());
        } finally {
            pastInitialisation = true;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.ActionServlet#initModuleConfig(java.lang.String,
     *      java.lang.String)
     */
    protected ModuleConfig initModuleConfig(String prefix, String paths) throws ServletException {
        moduleConfig = super.initModuleConfig(prefix, paths);
        digester = initConfigDigester();
        moduleConfig.getControllerConfig().setProcessorClass("com.sslexplorer.core.CoreRequestProcessor");
        return moduleConfig;
    }

    protected void initCore() throws ServletException {

        // // Process each specified resource path
        // while (paths.length() > 0) {
        // digester.push(moduleConfig);
        // String path = null;
        // int comma = paths.indexOf(',');
        // if (comma >= 0) {
        // path = paths.substring(0, comma).trim();
        // paths = paths.substring(comma + 1);
        // } else {
        // path = paths.trim();
        // paths = "";
        // }
        //
        // if (path.length() < 1) {
        // break;
        // }
        //
        // this.parseModuleConfigFile(digester, path);
        // }

        // Add the additional default struts configs

        digester.push(moduleConfig);
        this.parseModuleConfigFile(digester, "/WEB-INF/wizard-struts-config.xml");
        digester.push(moduleConfig);
        this.parseModuleConfigFile(digester, "/WEB-INF/ajax-struts-config.xml");

        /**
         * Install Maverick SSL support
         */
        if (!("false".equals(SystemProperties.get("sslexplorer.useMaverickSSL", "true")))) {
            try {
                if (log.isInfoEnabled())
                    log.info("Installing Maverick SSL");
                com.maverick.ssl.https.HttpsURLStreamHandlerFactory.addHTTPSSupport();
            } catch (IOException ex1) {
                throw new ServletException("Failed to install Maverick SSL support");
            }
            boolean strictHostVerification = false;
            System.setProperty("com.maverick.ssl.allowUntrustedCertificates", String.valueOf(!strictHostVerification));
            System.setProperty("com.maverick.ssl.allowInvalidCertificates", String.valueOf(!strictHostVerification));
        }

        if (log.isInfoEnabled())
            log.info("Adding default authentication modules.");
        // Add the default authentication modules
        AuthenticationModuleManager.getInstance().registerModule("Password", PasswordAuthenticationModule.class, "properties",
            true, true, false);
        AuthenticationModuleManager.getInstance().registerModule("HTTP", HTTPAuthenticationModule.class, "properties", true, true,
            false);
        AuthenticationModuleManager.getInstance().registerModule("PersonalQuestions", PersonalQuestionsAuthenticationModule.class,
            "properties", false, true, false);
        AuthenticationModuleManager.getInstance().registerModule("WebDAV", WebDAVAuthenticationModule.class, "properties", true,
            false, true);
        AuthenticationModuleManager.getInstance().registerModule("EmbeddedClient", EmbeddedClientAuthenticationModule.class,
            "properties", true, false, true);

        // Add the default page scripts
        addPageScript(new CoreScript(CoreScript.PAGE_HEADER, "JavaScript", "/js/cookieDetect.jsp", null, "text/javascript"));
        addPageScript(new CoreScript(CoreScript.PAGE_HEADER, "JavaScript", "/js/prototype.js", null, "text/javascript"));
        addPageScript(new CoreScript(CoreScript.PAGE_HEADER, "JavaScript", "/js/extensions.js", null, "text/javascript"));
        addPageScript(new CoreScript(CoreScript.PAGE_HEADER, "JavaScript", "/js/scriptaculous.js", null, "text/javascript"));
        addPageScript(new CoreScript(CoreScript.PAGE_HEADER, "JavaScript", "/js/overlibmws.js", null, "text/javascript"));
        addPageScript(new CoreScript(CoreScript.PAGE_HEADER, "JavaScript", "/js/ajaxtags.js", null, "text/javascript"));
        addPageScript(new CoreScript(CoreScript.PAGE_HEADER, "JavaScript", "/js/cookies.js", null, "text/javascript"));
        addPageScript(new CoreScript(CoreScript.PAGE_HEADER, "JavaScript", "/js/table.js", null, "text/javascript"));
        addPageScript(new CoreScript(CoreScript.PAGE_HEADER, "JavaScript", "/js/datetimepicker.js", null, "text/javascript"));
        addPageScript(new CoreScript(CoreScript.PAGE_HEADER, "JavaScript", "/js/modalbox.js", null, "text/javascript"));
        addPageScript(new CoreScript(CoreScript.AFTER_BODY_START, "JavaScript", "/js/set.js", null, "text/javascript"));
        addPageScript(new CoreScript(CoreScript.AFTER_BODY_START, "JavaScript", "/js/resources.js", null, "text/javascript"));
        addPageScript(new CoreScript(CoreScript.AFTER_BODY_START, "JavaScript", "/js/items.js", null, "text/javascript"));
        addPageScript(new CoreScript(CoreScript.AFTER_BODY_START, "JavaScript", "/js/input.js", null, "text/javascript"));
        addPageScript(new CoreScript(CoreScript.AFTER_BODY_START, "JavaScript", "/js/windowManager.js", null, "text/javascript"));
        addPageScript(new CoreScript(CoreScript.BEFORE_BODY_END, "JavaScript", "/js/wz_tooltip.js", null, "text/javascript"));

        // Add the default panels
        PanelManager.getInstance().addPanel(
            new DefaultPanel("menu", Panel.SIDEBAR, 50, null, "menu", "navigation", false, true, false, false));
        PanelManager.getInstance().addPanel(
            new DefaultPanel("editingResourceInfo", Panel.SIDEBAR, 100, "/WEB-INF/jsp/tiles/editingResourceInfo.jspf", null,
                            "navigation", false, true, false, false) {
                public boolean isAvailable(HttpServletRequest request, HttpServletResponse response, String layout) {
                    return super.isAvailable(request, response, layout) && !ResourceStack.isEmpty(request.getSession())
                                    && request.getSession().getAttribute(Constants.WIZARD_SEQUENCE) == null;
                }

            });
        PanelManager.getInstance().addPanel(
            new DefaultPanel("wizardSequenceInfo", Panel.SIDEBAR, 100, "/WEB-INF/jsp/tiles/wizardSequenceInfo.jspf", null,
                            "navigation", false, true, false, false) {
                public boolean isAvailable(HttpServletRequest request, HttpServletResponse response, String layout) {
                    return super.isAvailable(request, response, layout) && ResourceStack.isEmpty(request.getSession())
                                    && request.getSession().getAttribute(Constants.WIZARD_SEQUENCE) != null;
                }
            });
        // PanelManager.getInstance().addPanel(new DefaultPanel("about",
        // Panel.SIDEBAR,
        // 1000,
        // "/WEB-INF/jsp/tiles/about.jspf",
        // null,
        // "navigation",
        // false,
        // false,
        // false,
        // false) {
        //
        // public boolean isAvailable(HttpServletRequest request,
        // HttpServletResponse response, String layout) {
        // return LogonControllerFactory.getInstance().getSessionInfo(request)
        // != null && layout.equals(MAIN_LAYOUT) &&
        // !ContextHolder.getContext().isSetupMode()
        // && super.isAvailable(request, response, layout);
        // }
        //
        // });
        PanelManager.getInstance().addPanel(
            new DefaultPanel("pageInfo", Panel.CONTENT, 25, "/WEB-INF/jsp/tiles/pageInfo.jspf", null, "navigation") {

                public boolean isAvailable(HttpServletRequest request, HttpServletResponse response, String layout) {
                    return (LogonControllerFactory.getInstance().getSessionInfo(request) != null || ContextHolder.getContext()
                                    .isSetupMode())
                                    && request.getSession().getAttribute(Constants.SESSION_LOCKED) == null;
                }

            });
        PanelManager.getInstance().addPanel(new DefaultPanel("content", Panel.CONTENT, 50, null, "content", "navigation", false));
        PanelManager.getInstance().addPanel(
            new DefaultPanel("panelOptions", Panel.MESSAGES, 10, "/WEB-INF/jsp/tiles/panelOptions.jspf", null, "navigation", true,
                            true, true, true) {

                public String getDefaultFrameState() {
                    return FRAME_CLOSED;
                }

            });
        PanelManager.getInstance().addPanel(
            new DefaultPanel("pageTasks", Panel.MESSAGES, 50, "/WEB-INF/jsp/tiles/pageTasks.jspf", "pageTasks", "navigation",
                            false, false, true, true));
        PanelManager.getInstance().addPanel(
            new DefaultPanel("toolBar", Panel.CONTENT, 35, "/WEB-INF/jsp/tiles/toolBar.jspf", null, "navigation") {
                public boolean isAvailable(HttpServletRequest request, HttpServletResponse response, String layout) {
                    return (LogonControllerFactory.getInstance().getSessionInfo(request) != null || ContextHolder.getContext()
                                    .isSetupMode())
                                    && request.getSession().getAttribute(Constants.TOOL_BAR_ITEMS) != null;
                }
            });
        PanelManager.getInstance().addPanel(
            new DefaultPanel("errorMessages", Panel.MESSAGES, 60, "/WEB-INF/jsp/tiles/errorMessages.jspf", null, "navigation",
                            true, false, true, true));
        PanelManager.getInstance().addPanel(
            new DefaultPanel("warnings", Panel.MESSAGES, 70, "/WEB-INF/jsp/tiles/warnings.jspf", null, "navigation", true, true,
                            true, true));
        PanelManager.getInstance().addPanel(
            new DefaultPanel("infoMessages", Panel.MESSAGES, 80, "/WEB-INF/jsp/tiles/infoMessages.jspf", null, "navigation", true,
                            true, true, true));
        PanelManager.getInstance().addPanel(new ClipboardPanel());
        PanelManager.getInstance().addPanel(
            new DefaultPanel("rssFeeds", Panel.MESSAGES, 120, "/WEB-INF/jsp/tiles/rssFeeds.jspf", null, "navigation", true, true,
                            true, true) {
            });
        PanelManager.getInstance().addPanel(
            new DefaultPanel("userSessions", Panel.STATUS_TAB, 10, "/WEB-INF/jsp/content/setup/userSessions.jspf", null, "setup",
                            false));
        PanelManager.getInstance()
                        .addPanel(
                            new DefaultPanel("systemInfo", Panel.STATUS_TAB, 20, "/WEB-INF/jsp/content/setup/systemInfo.jspf",
                                            null, "setup", false));

        // Add the default key store import types
        KeyStoreImportTypeManager.getInstance().registerType(new _3SPPurchaseImportType());
        KeyStoreImportTypeManager.getInstance().registerType(new RootServerCertificateImportType());
        KeyStoreImportTypeManager.getInstance().registerType(new ReplyFromCAImportType());
        KeyStoreImportTypeManager.getInstance().registerType(new ServerAuthenticationKeyImportType());
        KeyStoreImportTypeManager.getInstance().registerType(new TrustedServerCertificateImportType());

        // Add the default user databases
        UserDatabaseManager.getInstance().registerDatabase(
            new UserDatabaseDefinition(JDBCUserDatabase.class, "builtIn", "properties", -1));

        /*
         * Add the 'site' VFS directory as a resource base. This must be done
         * because we need access to the resources without authentication
         */
        File siteDir = new File(ContextHolder.getContext().getConfDirectory(), "site");
        try {
            if (!siteDir.exists()) {
                siteDir.mkdirs();
            }
            ContextHolder.getContext().addResourceBase(siteDir.toURL());
        } catch (Exception e) {
            log.error("Failed to add " + siteDir.getPath()
                            + " as a resource base. Site specific resources such as icons may not work.");
        }

        // Add the extension store panels.
        // categories are Installed, Updateable, Beta, Remote Access,
        // AccessControl, Resources, UserInterface, Misc, Articles
        PanelManager.getInstance().addPanel(
            new DefaultPanel("installed", ConfigureExtensionsForm.EXTENSIONS_TAB_ID, 10,
                            "/WEB-INF/jsp/content/extensions/installedExtensionStoreContent.jspf", null, "extensions", false));
        PanelManager.getInstance().addPanel(
            new DefaultPanel("updateable", ConfigureExtensionsForm.EXTENSIONS_TAB_ID, 20,
                            "/WEB-INF/jsp/content/extensions/updateableExtensionStoreContent.jspf", null, "extensions", false));
        PanelManager.getInstance().addPanel(
            new DefaultPanel("beta", ConfigureExtensionsForm.EXTENSIONS_TAB_ID, 30,
                            "/WEB-INF/jsp/content/extensions/betaExtensionStoreContent.jspf", null, "extensions", false));
        PanelManager.getInstance().addPanel(
            new DefaultPanel("remoteAccess", ConfigureExtensionsForm.EXTENSIONS_TAB_ID, 40,
                            "/WEB-INF/jsp/content/extensions/remoteAccessExtensionStoreContent.jspf", null, "extensions", false));
        PanelManager.getInstance().addPanel(
            new DefaultPanel("accessControl", ConfigureExtensionsForm.EXTENSIONS_TAB_ID, 50,
                            "/WEB-INF/jsp/content/extensions/accessControlExtensionStoreContent.jspf", null, "extensions", false));
        PanelManager.getInstance().addPanel(
            new DefaultPanel("resources", ConfigureExtensionsForm.EXTENSIONS_TAB_ID, 60,
                            "/WEB-INF/jsp/content/extensions/resourcesExtensionStoreContent.jspf", null, "extensions", false));
        PanelManager.getInstance().addPanel(
            new DefaultPanel("userInterface", ConfigureExtensionsForm.EXTENSIONS_TAB_ID, 70,
                            "/WEB-INF/jsp/content/extensions/userInterfaceExtensionStoreContent.jspf", null, "extensions", false));
        PanelManager.getInstance().addPanel(
            new DefaultPanel("misc", ConfigureExtensionsForm.EXTENSIONS_TAB_ID, 80,
                            "/WEB-INF/jsp/content/extensions/miscExtensionStoreContent.jspf", null, "extensions", false));
        PanelManager.getInstance().addPanel(
            new DefaultPanel("articles", ConfigureExtensionsForm.EXTENSIONS_TAB_ID, 90,
                            "/WEB-INF/jsp/content/extensions/articlesExtensionStoreContent.jspf", null, "extensions", false));

    }

    /**
     * @param path
     * @throws ServletException
     */
    public void addStrutsConfig(String path) throws ServletException {
        digester.push(moduleConfig);
        if (log.isInfoEnabled())
            log
                            .info("Processing plugin struts configuration file " + path + " to moulde config '"
                                            + moduleConfig.getPrefix() + "'");
        try {
            /*
             * If we have already past initialisation stage, then this call is a
             * plugin being started at runtime
             */
            if (pastInitialisation) {
                initModuleConfig("", path);
                initModuleMessageResources(moduleConfig);
                initModuleDataSources(moduleConfig);
                initModulePlugIns(moduleConfig);
            } else {
                this.parseModuleConfigFile(digester, path);
            }
        } catch (UnavailableException ue) {
            if (log.isInfoEnabled()) {
                log.error("Failed to add struts config.", ue);
            }
        }
    }

    /**
     * @throws ServletException
     */
    public void initExtensionStore() throws ServletException {

        // Initialise extension store
        ExtensionStore store = ExtensionStore.getInstance();

        // Initialise store
        try {
            store.init(ContextHolder.getContext().getApplicationDirectory());
        } catch (Exception e) {
            log.error("Failed to initialise extension store.", e);
            throw new ServletException(e);
        }
    }

    /**
     * @throws ServletException
     */
    public void startExtensions() throws ServletException {

        // Intialise plugins
        if ("true".equalsIgnoreCase(SystemProperties.get("sslexplorer.disableExtensions"))) {
            log.warn("Extension manager disabled, extensions wont be started");
        } else {
            try {
                ExtensionStore.getInstance().start();
            } catch (ExtensionException ee) {
                log.error("Failed to start extension store.", ee);
                throw new ServletException("Failed to start extension store.", ee);
            }
        }
    }

    /**
     * 
     */
    public void stopped() {
        if (shuttingDown) {
            throw new RuntimeException("Already shutting down.");
        }
        shuttingDown = true;
        fireCoreEvent(new CoreEvent(this, CoreEventConstants.SERVER_STOPPING, null, null));
        if (notifier != null && notifier.isStarted()) {
            notifier.stop();
        }

        // Stop updating rss feeds
        if(FeedManager.getInstance().isUpdating())
            FeedManager.getInstance().stopUpdating();

        // Close down all user databases
        UserDatabaseManager.getInstance().closeAll();

        // Stop all extensions
        try {
            ExtensionStore.getInstance().stop();
        } catch (ExtensionException e) {
            log.error("Failed to stop extensions.", e);
        }

        // Stop the database server
        if (dbServer != null) {
            dbServer.stop();
        }

        // Fire our last event. Listeners should do as little as possible at
        // this stage
        fireCoreEvent(new CoreEvent(this, CoreEventConstants.SERVER_STOPPED, null, null));

        shuttingDown = false;
    }

    /**
     * Get if the system is currently shutting down. This will become true as
     * soon as {@link #stopped()} has been invoked.
     * 
     * @return shutting down
     */
    public boolean isShuttingDown() {
        return shuttingDown;
    }

    void checkDevEnvironment() throws ServletException {
        devConfig = "true".equalsIgnoreCase(SystemProperties.get("sslexplorer.useDevConfig", "false"));
        File defaultDevConfDir = new File(SystemProperties.get("user.dir"), "conf");
        try {
            if (devConfig
                            && ContextHolder.getContext().getConfDirectory().getCanonicalFile().equals(
                                defaultDevConfDir.getCanonicalFile())) {
                throw new ServletException("When running in developmenet mode, you may NOT use "
                                + defaultDevConfDir.getAbsolutePath() + " as your 'conf' directory. Please specifiy "
                                + "a different directory using the --conf=<dir> argument when starting the server.");
            }
        } catch (IOException ioe) {
            throw new ServletException("Failed to determine if incorrect conf directory is being used", ioe);
        }
    }

    public RequestProcessor getRequestProcessor(HttpServletRequest request) throws ServletException {
        return getRequestProcessor(getModuleConfig(request));

    }
}