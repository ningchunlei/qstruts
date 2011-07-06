/**
 * ClassName:Dispatcher.java
 * Authoer:ningcl
 * Date:2011-1-10 
 */
package org.xz.qstruts.dispatcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsConstants;
import org.apache.struts2.StrutsException;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.config.DefaultPropertiesProvider;
import org.apache.struts2.config.LegacyPropertiesConfigurationProvider;
import org.apache.struts2.config.StrutsXmlConfigurationProvider;
import org.apache.struts2.dispatcher.StrutsRequestWrapper;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.dispatcher.multipart.MultiPartRequest;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.apache.struts2.util.ClassLoaderUtils;
import org.apache.struts2.util.ObjectFactoryDestroyable;
import org.apache.struts2.views.freemarker.FreemarkerManager;
import org.xz.qstruts.QStrutsConstant;
import org.xz.qstruts.config.BeanSelectionProvider;
import org.xz.qstruts.config.QStrutsXmlConfigurationProvider;
import org.xz.qstruts.config.RuntimeProvider;
import org.xz.qxork2.ActionContext;
import org.xz.qxork2.ActionContextProvider;

import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionProxyFactory;
import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.ConfigurationProvider;
import com.opensymphony.xwork2.config.entities.InterceptorMapping;
import com.opensymphony.xwork2.config.entities.InterceptorStackConfig;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.config.providers.XmlConfigurationProvider;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.ContainerBuilder;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.opensymphony.xwork2.util.FileManager;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.opensymphony.xwork2.util.location.LocatableProperties;
import com.opensymphony.xwork2.util.location.Location;
import com.opensymphony.xwork2.util.location.LocationUtils;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.opensymphony.xwork2.util.profiling.UtilTimerStack;

import freemarker.template.Template;

/**
 * @author ningcl
 * @version 1.0 
 */
public class Dispatcher {
	/**
     * Provide a logging instance.
     */
    private static final Logger LOG = LoggerFactory.getLogger(Dispatcher.class);

    /**
     * Store ConfigurationManager instance, set on init.
     */
    private ConfigurationManager configurationManager;

    /**
     * Store state of  StrutsConstants.STRUTS_DEVMODE setting.
     */
    private boolean devMode;
    
    private boolean moduleDev;
    
    private String defaultSpringId;
    
    /**
     * Store state of StrutsConstants.STRUTS_I18N_ENCODING setting.
     */
    private String defaultEncoding;

    /**
     * Store state of StrutsConstants.STRUTS_LOCALE setting.
     */
    private String defaultLocale;

    /**
     * Store state of StrutsConstants.STRUTS_MULTIPART_SAVEDIR setting.
     */
    private String multipartSaveDir;

    /**
     * Stores the value of StrutsConstants.STRUTS_MULTIPART_HANDLER setting
     */
    private String multipartHandlerName;

    /**
     * Provide list of default configuration files.
     */
    private static final String DEFAULT_CONFIGURATION_PATHS = "qstruts-default.xml,struts-plugin.xml,qstruts.xml";

    /**
     * Store state of STRUTS_DISPATCHER_PARAMETERSWORKAROUND.
     * <p/>
     * The workaround is for WebLogic.
     * We try to autodect WebLogic on Dispatcher init.
     * The workaround can also be enabled manually.
     */
    private boolean paramsWorkaroundEnabled = false;
    
    private static Dispatcher _instance = null;
    
    /**
     * Provide the dispatcher instance for the current thread.
     *
     * @return The dispatcher instance
     */
    public static Dispatcher getInstance() {
        return _instance;
    }

    /**
     * Store the dispatcher instance for this thread.
     *
     * @param instance The instance
     */
    public static void setInstance(Dispatcher instance) {
    	_instance = instance;
    }

    private ServletContext servletContext;
    private Map<String, String> initParams;

    /**
     * Create the Dispatcher instance for a given ServletContext and set of initialization parameters.
     *
     * @param servletContext Our servlet context
     * @param initParams The set of initialization parameters
     */
    public Dispatcher(ServletContext servletContext, Map<String, String> initParams) {
        this.servletContext = servletContext;
        this.initParams = initParams;
    }
    
    
    @Inject(QStrutsConstant.QSTRUTS_MODUELDEV)
    public void setModuleAlone(String moduledev) {
		this.moduleDev = "true".equals(moduledev);
	}
    
	public boolean isModuleDev() {
		return moduleDev;
	}

	/**
     * Modify state of StrutsConstants.STRUTS_DEVMODE setting.
     * @param mode New setting
     */
    @Inject(StrutsConstants.STRUTS_DEVMODE)
    public void setDevMode(String mode) {
        devMode = "true".equals(mode);
    }
    
    
   
    /**
     * Modify state of StrutsConstants.STRUTS_MULTIPART_SAVEDIR setting.
     * @param val New setting
     */
    @Inject(StrutsConstants.STRUTS_MULTIPART_SAVEDIR)
    public void setMultipartSaveDir(String val) {
        multipartSaveDir = val;
    }

    @Inject(StrutsConstants.STRUTS_MULTIPART_HANDLER)
    public void setMultipartHandler(String val) {
        multipartHandlerName = val;
    }
    
    @Inject(StrutsConstants.STRUTS_I18N_ENCODING)
    public void setDefaultEncoding(String encoding){
    	defaultEncoding = encoding;
    }
    
    public String getDefaultSpringId() {
		return defaultSpringId;
	}
    
    @Inject(QStrutsConstant.QSTRUTS_DEFAULTACTIONSPRINGID)
	public void setDefaultSpringId(String defaultSpringId) {
		this.defaultSpringId = defaultSpringId;
	}

	/**
     * Releases all instances bound to this dispatcher instance.
     */
    public void cleanup() {

    	// clean up ObjectFactory
        ObjectFactory objectFactory = getContainer().getInstance(ObjectFactory.class);
        if (objectFactory == null) {
            LOG.warn("Object Factory is null, something is seriously wrong, no clean up will be performed");
        }
        if (objectFactory instanceof ObjectFactoryDestroyable) {
            try {
                ((ObjectFactoryDestroyable)objectFactory).destroy();
            }
            catch(Exception e) {
                // catch any exception that may occured during destroy() and log it
                LOG.error("exception occurred while destroying ObjectFactory ["+objectFactory+"]", e);
            }
        }

        // clean up all interceptors by calling their destroy() method
        Set<Interceptor> interceptors = new HashSet<Interceptor>();
        Collection<PackageConfig> packageConfigs = configurationManager.getConfiguration().getPackageConfigs().values();
        for (PackageConfig packageConfig : packageConfigs) {
            for (Object config : packageConfig.getAllInterceptorConfigs().values()) {
                if (config instanceof InterceptorStackConfig) {
                    for (InterceptorMapping interceptorMapping : ((InterceptorStackConfig) config).getInterceptors()) {
                	    interceptors.add(interceptorMapping.getInterceptor());
                    }
                }
            }
        }
        for (Interceptor interceptor : interceptors) {
        	interceptor.destroy();
        }

        // clean up configuration
    	configurationManager.destroyConfiguration();
    	configurationManager = null;
    }

    private void init_DefaultProperties() {
        configurationManager.addConfigurationProvider(new DefaultPropertiesProvider());
    }
    
    private void init_LegacyStrutsProperties() {
        configurationManager.addConfigurationProvider(new LegacyPropertiesConfigurationProvider());
    }

    private void init_TraditionalXmlConfigurations() {
        String configPaths = initParams.get("config");
        if (configPaths == null) {
            configPaths = DEFAULT_CONFIGURATION_PATHS;
        }
        String[] files = configPaths.split("\\s*[,]\\s*");
        for (String file : files) {
            if (file.endsWith(".xml")) {
                if ("xwork.xml".equals(file)) {
                    configurationManager.addConfigurationProvider(new XmlConfigurationProvider(file, false));
                } else {
                    configurationManager.addConfigurationProvider(new StrutsXmlConfigurationProvider(file, false, servletContext));
                }
            } else {
                throw new IllegalArgumentException("Invalid configuration file name");
            }
        }
    }

    private void init_CustomConfigurationProviders() {
        String configProvs = initParams.get("configProviders");
        if (configProvs != null) {
            String[] classes = configProvs.split("\\s*[,]\\s*");
            for (String cname : classes) {
                try {
                    Class cls = ClassLoaderUtils.loadClass(cname, this.getClass());
                    ConfigurationProvider prov = (ConfigurationProvider)cls.newInstance();
                    configurationManager.addConfigurationProvider(prov);
                } catch (InstantiationException e) {
                    throw new ConfigurationException("Unable to instantiate provider: "+cname, e);
                } catch (IllegalAccessException e) {
                    throw new ConfigurationException("Unable to access provider: "+cname, e);
                } catch (ClassNotFoundException e) {
                    throw new ConfigurationException("Unable to locate provider class: "+cname, e);
                }
            }
        }
    }

    private void init_FilterInitParameters() {
        configurationManager.addConfigurationProvider(new ConfigurationProvider() {
            public void destroy() {}
            public void init(Configuration configuration) throws ConfigurationException {}
            public void loadPackages() throws ConfigurationException {}
            public boolean needsReload() { return false; }

            public void register(ContainerBuilder builder, LocatableProperties props) throws ConfigurationException {
                props.putAll(initParams);
            }
        });
    }

    private void init_AliasStandardObjects() {
        configurationManager.addConfigurationProvider(new BeanSelectionProvider());
    }

    private Container init_PreloadConfiguration() {
        Configuration config = configurationManager.getConfiguration();
        Container container = config.getContainer();

        boolean reloadi18n = Boolean.valueOf(container.getInstance(String.class, StrutsConstants.STRUTS_I18N_RELOAD));
        LocalizedTextUtil.setReloadBundles(reloadi18n);

        return container;
    }

    private void init_CheckConfigurationReloading(Container container) {
        FileManager.setReloadingConfigs("true".equals(container.getInstance(String.class,
                StrutsConstants.STRUTS_CONFIGURATION_XML_RELOAD)));
    }

    private void init_CheckWebLogicWorkaround(Container container) {
        // test whether param-access workaround needs to be enabled
        if (servletContext != null && servletContext.getServerInfo() != null
                && servletContext.getServerInfo().indexOf("WebLogic") >= 0) {
            LOG.info("WebLogic server detected. Enabling Struts parameter access work-around.");
            paramsWorkaroundEnabled = true;
        } else {
            paramsWorkaroundEnabled = "true".equals(container.getInstance(String.class,
                    StrutsConstants.STRUTS_DISPATCHER_PARAMETERSWORKAROUND));
        }
    }

    /**
     * Load configurations, including both XML and zero-configuration strategies,
     * and update optional settings, including whether to reload configurations and resource files.
     */
    public void init() {

    	if (configurationManager == null) {
    		configurationManager = new ConfigurationManager(BeanSelectionProvider.DEFAULT_BEAN_NAME);
    	}

        try {
            init_DefaultProperties(); // [1]
            init_TraditionalXmlConfigurations(); // [2]
            configurationManager.addConfigurationProvider(new QStrutsXmlConfigurationProvider("qstruts-mod.xml", false, servletContext));
            configurationManager.addConfigurationProvider(new RuntimeProvider());
            init_LegacyStrutsProperties(); // [3]
            init_CustomConfigurationProviders(); // [5]
            init_FilterInitParameters() ; // [6]
            init_AliasStandardObjects() ; // [7]

            Container container = init_PreloadConfiguration();
            container.inject(this);
            init_CheckConfigurationReloading(container);
            init_CheckWebLogicWorkaround(container);

        } catch (Exception ex) {
            if (LOG.isErrorEnabled())
                LOG.error("Dispatcher initialization failed", ex);
            throw new StrutsException(ex);
        }
    }

    /**
     * Load Action class for mapping and invoke the appropriate Action method, or go directly to the Result.
     * <p/>
     * This method first creates the action context from the given parameters,
     * and then loads an <tt>ActionProxy</tt> from the given action name and namespace.
     * After that, the Action method is executed and output channels through the response object.
     * Actions not found are sent back to the user via the {@link Dispatcher#sendError} method,
     * using the 404 return code.
     * All other errors are reported by throwing a ServletException.
     *
     * @param request  the HttpServletRequest object
     * @param response the HttpServletResponse object
     * @param mapping  the action mapping object
     * @throws ServletException when an unknown error occurs (not a 404, but typically something that
     *                          would end up as a 5xx by the servlet container)
     * @param context Our ServletContext object
     */
    public void serviceAction(HttpServletRequest request, HttpServletResponse response, ServletContext context,
                              ActionMapping mapping) throws ServletException {

        Map<String, Object> extraContext = createContextMap(request, response, mapping, context);
        ActionProxy proxy = null;
        String timerKey = "Handling request from Dispatcher";
        try {
            UtilTimerStack.push(timerKey);
            String namespace = mapping.getNamespace();
            String name = mapping.getName();
            String method = mapping.getMethod();
            
            Configuration config = configurationManager.getConfiguration();
            proxy = config.getContainer().getInstance(ActionProxyFactory.class).createActionProxy(
                    namespace, name, method, extraContext, true, false);
            
            request.setAttribute(ActionContext.REQUESTPARAMETERCONTEXT, mapping.getParams());
            request.setAttribute(ActionContext.ACTIONCONTEXT, proxy.getInvocation().getInvocationContext());
            request.setAttribute(ActionContext.VALUE_STACK, proxy.getInvocation().getStack());
            // if the ActionMapping says to go straight to a result, do it!
            if (mapping.getResult() != null) {
                Result result = mapping.getResult();
                result.execute(proxy.getInvocation());
            } else {
                proxy.execute();
            }

        } catch (ConfigurationException e) {
        	// WW-2874 Only log error if in devMode
        	if(devMode) {
                String reqStr = request.getRequestURI();
                if (request.getQueryString() != null) {
                    reqStr = reqStr + "?" + request.getQueryString();
                }
                LOG.error("Could not find action or result\n" + reqStr, e);
            }
        	else {
        		LOG.warn("Could not find action or result", e);
        	}
            sendError(request, response, context, HttpServletResponse.SC_NOT_FOUND, e);
        } catch (Exception e) {
            sendError(request, response, context, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e);
        } finally {
        	if(proxy!=null && proxy.getAction()!=null){
              	 if(proxy.getAction() instanceof ActionContextProvider){
       	        	((ActionContextProvider) proxy.getAction()).setActionContext(null);
       	        }
              }
        	UtilTimerStack.pop(timerKey);
        }
    }

    /**
     * Create a context map containing all the wrapped request objects
     *
     * @param request The servlet request
     * @param response The servlet response
     * @param mapping The action mapping
     * @param context The servlet context
     * @return A map of context objects
     */
    public Map<String,Object> createContextMap(HttpServletRequest request, HttpServletResponse response,
            ActionMapping mapping, ServletContext context) {
    	
    	 HashMap<String,Object> extraContext = new HashMap<String,Object>();

         Locale locale;
         if (defaultLocale != null) {
             locale = LocalizedTextUtil.localeFromString(defaultLocale, request.getLocale());
         } else {
             locale = request.getLocale();
         }

         //extraContext.put(ActionContext.LOCALE, locale);

         extraContext.put(StrutsStatics.HTTP_REQUEST, request);
         extraContext.put(StrutsStatics.HTTP_RESPONSE, response);
         extraContext.put(StrutsStatics.SERVLET_CONTEXT, servletContext);

        if (mapping != null) {
            extraContext.put(ServletActionContext.ACTION_MAPPING, mapping);
        }
        return extraContext;
    }

    

    /**
     * Return the path to save uploaded files to (this is configurable).
     *
     * @return the path to save uploaded files to
     * @param servletContext Our ServletContext
     */
    private String getSaveDir(ServletContext servletContext) {
        String saveDir = multipartSaveDir.trim();

        if (saveDir.equals("")) {
            File tempdir = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
            LOG.info("Unable to find 'struts.multipart.saveDir' property setting. Defaulting to javax.servlet.context.tempdir");

            if (tempdir != null) {
                saveDir = tempdir.toString();
                setMultipartSaveDir(saveDir);
            }
        } else {
            File multipartSaveDir = new File(saveDir);

            if (!multipartSaveDir.exists()) {
                if (multipartSaveDir.mkdir() == false) {
                    String logMessage;
        	    try {
                        logMessage = "Could not find create multipart save directory '"+multipartSaveDir.getCanonicalPath()+"'.";
        	    } catch (IOException e) {
                        logMessage = "Could not find create multipart save directory '"+multipartSaveDir.toString()+"'.";
        	    }
        	    if(devMode) {
                        LOG.error(logMessage);
        	    }
        	    else {
                        LOG.warn(logMessage);
        	    }
                }
            }
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("saveDir=" + saveDir);
        }

        return saveDir;
    }

    /**
     * Prepare a request, including setting the encoding and locale.
     *
     * @param request The request
     * @param response The response
     */
    public void prepare(HttpServletRequest request, HttpServletResponse response) {
        String encoding = null;
        if (defaultEncoding != null) {
            encoding = defaultEncoding;
        }

        Locale locale = null;
        if (defaultLocale != null) {
            locale = LocalizedTextUtil.localeFromString(defaultLocale, request.getLocale());
        }

        if (encoding != null) {
            try {
                request.setCharacterEncoding(encoding);
            } catch (Exception e) {
                LOG.error("Error setting character encoding to '" + encoding + "' - ignoring.", e);
            }
        }

        if (locale != null) {
            response.setLocale(locale);
        }

        if (paramsWorkaroundEnabled) {
            request.getParameter("foo"); // simply read any parameter (existing or not) to "prime" the request
        }
    }

    /**
     * Wrap and return the given request or return the original request object.
     * </p>
     * This method transparently handles multipart data as a wrapped class around the given request.
     * Override this method to handle multipart requests in a special way or to handle other types of requests.
     * Note, {@link org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper} is
     * flexible - look first to that object before overriding this method to handle multipart data.
     *
     * @param request the HttpServletRequest object.
     * @param servletContext Our ServletContext object
     * @return a wrapped request or original request.
     * @see org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper
     * @throws java.io.IOException on any error.
     */
    public HttpServletRequest wrapRequest(HttpServletRequest request, ServletContext servletContext) throws IOException {
        // don't wrap more than once
        if (request instanceof StrutsRequestWrapper) {
            return request;
        }

        String content_type = request.getContentType();
        if (content_type != null && content_type.indexOf("multipart/form-data") != -1) {
            MultiPartRequest mpr = null;
            //check for alternate implementations of MultiPartRequest
            Set<String> multiNames = getContainer().getInstanceNames(MultiPartRequest.class);
            if (multiNames != null) {
                for (String multiName : multiNames) {
                    if (multiName.equals(multipartHandlerName)) {
                        mpr = getContainer().getInstance(MultiPartRequest.class, multiName);
                    }
                }
            }
            if (mpr == null ) {
                mpr = getContainer().getInstance(MultiPartRequest.class);
            }
            request = new MultiPartRequestWrapper(mpr, request, getSaveDir(servletContext));
        } else {
            request = new StrutsRequestWrapper(request);
        }

        return request;
    }

    /**
     * Send an HTTP error response code.
     *
     * @param request  the HttpServletRequest object.
     * @param response the HttpServletResponse object.
     * @param code     the HttpServletResponse error code (see {@link javax.servlet.http.HttpServletResponse} for possible error codes).
     * @param e        the Exception that is reported.
     * @param ctx      the ServletContext object.
     */
    public void sendError(HttpServletRequest request, HttpServletResponse response,
            ServletContext ctx, int code, Exception e) {
        if (devMode) {
            response.setContentType("text/html");

            try {
                FreemarkerManager mgr = getContainer().getInstance(FreemarkerManager.class);

                freemarker.template.Configuration config = mgr.getConfiguration(ctx);
                Template template = config.getTemplate("/org/apache/struts2/dispatcher/error.ftl");

                List<Throwable> chain = new ArrayList<Throwable>();
                Throwable cur = e;
                chain.add(cur);
                while ((cur = cur.getCause()) != null) {
                    chain.add(cur);
                }

                HashMap<String,Object> data = new HashMap<String,Object>();
                data.put("exception", e);
                data.put("unknown", Location.UNKNOWN);
                data.put("chain", chain);
                data.put("locator", new Locator());
                template.process(data, response.getWriter());
                response.getWriter().close();
            } catch (Exception exp) {
                try {
                    response.sendError(code, "Unable to show problem report: " + exp);
                } catch (IOException ex) {
                    // we're already sending an error, not much else we can do if more stuff breaks
                }
            }
        } else {
            try {
                // WW-1977: Only put errors in the request when code is a 500 error
                if (code == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
                    // send a http error response to use the servlet defined error handler
                    // make the exception availible to the web.xml defined error page
                    request.setAttribute("javax.servlet.error.exception", e);

                    // for compatibility
                    request.setAttribute("javax.servlet.jsp.jspException", e);
                }

                // send the error response
                response.sendError(code, e.getMessage());
            } catch (IOException e1) {
                // we're already sending an error, not much else we can do if more stuff breaks
            }
        }
    }

    

    /**
     * Provide an accessor class for static XWork utility.
     */
    public static class Locator {
        public Location getLocation(Object obj) {
            Location loc = LocationUtils.getLocation(obj);
            if (loc == null) {
                return Location.UNKNOWN;
            }
            return loc;
        }
    }
    
    public String getEncoding(){
    	return defaultEncoding;
    }
    
    /**
     * Expose the ConfigurationManager instance.
     *
     * @return The instance
     */
    public ConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    /**
     * Modify the ConfigurationManager instance
     *
     * @param mgr The configuration manager
     */
    public void setConfigurationManager(ConfigurationManager mgr) {
        this.configurationManager = mgr;
    }

    /**
     * Expose the dependency injection container.
     * @return Our dependency injection container
     */
    public Container getContainer() {
        ConfigurationManager mgr = getConfigurationManager();
        if (mgr == null) {
            throw new IllegalStateException("The configuration manager shouldn't be null");
        } else {
            Configuration config = mgr.getConfiguration();
            if (config == null) {
                throw new IllegalStateException("Unable to load configuration");
            } else {
                return config.getContainer();
            }
        }
    }
}
