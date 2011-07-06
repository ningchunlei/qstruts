/*
 * $Id: QStrutsXmlConfigurationProvider.java,v 1.3 2011/01/24 06:35:20 cvs_ningcl Exp $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.xz.qstruts.config;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Element;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.ExceptionMappingConfig;
import com.opensymphony.xwork2.config.entities.InterceptorMapping;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import com.opensymphony.xwork2.config.entities.ResultConfig;
import com.opensymphony.xwork2.config.providers.XmlConfigurationProvider;
import com.opensymphony.xwork2.config.providers.XmlHelper;
import com.opensymphony.xwork2.inject.ContainerBuilder;
import com.opensymphony.xwork2.inject.Context;
import com.opensymphony.xwork2.inject.Factory;
import com.opensymphony.xwork2.util.DomHelper;
import com.opensymphony.xwork2.util.location.LocatableProperties;
import com.opensymphony.xwork2.util.location.Location;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

/**
 * Override Xwork class so we can use an arbitrary config file
 */
public class QStrutsXmlConfigurationProvider extends XmlConfigurationProvider {

    private static final Logger LOG = LoggerFactory.getLogger(QStrutsXmlConfigurationProvider.class);
    private File baseDir = null;
    private String filename;
    private String reloadKey;
    private ServletContext servletContext;

    /**
     * Constructs the configuration provider
     *
     * @param errorIfMissing If we should throw an exception if the file can't be found
     */
    public QStrutsXmlConfigurationProvider(boolean errorIfMissing) {
        this("qstruts.xml", errorIfMissing, null);
    }

    /**
     * Constructs the configuration provider
     *
     * @param filename The filename to look for
     * @param errorIfMissing If we should throw an exception if the file can't be found
     * @param ctx Our ServletContext
     */
    public QStrutsXmlConfigurationProvider(String filename, boolean errorIfMissing, ServletContext ctx) {
        super(filename, errorIfMissing);
        this.servletContext = ctx;
        this.filename = filename;
        reloadKey = "configurationReload-"+filename;
        Map<String,String> dtdMappings = new HashMap<String,String>(getDtdMappings());
        dtdMappings.put("-//Apache Software Foundation//DTD Struts Configuration 2.0//EN", "struts-2.0.dtd");
        dtdMappings.put("-//Apache Software Foundation//DTD Struts Configuration 2.1//EN", "struts-2.1.dtd");
        dtdMappings.put("-//Apache Software Foundation//DTD Struts Configuration 2.1.7//EN", "struts-2.1.7.dtd");
        setDtdMappings(dtdMappings);
  
        this.baseDir = new File((ctx.getRealPath("/WEB-INF/modules/")));
        if(!baseDir.exists()){
        	baseDir = null;
        }
		
    }
    
    /* (non-Javadoc)
     * @see com.opensymphony.xwork2.config.providers.XmlConfigurationProvider#register(com.opensymphony.xwork2.inject.ContainerBuilder, java.util.Properties)
     */
    @Override
    public void register(ContainerBuilder containerBuilder, LocatableProperties props) throws ConfigurationException {
        if (servletContext != null && !containerBuilder.contains(ServletContext.class)) {
            containerBuilder.factory(ServletContext.class, new Factory<ServletContext>() {
                public ServletContext create(Context context) throws Exception {
                    return servletContext;
                }
            });
        }
        super.register(containerBuilder, props);
    }

    /* (non-Javadoc)
     * @see com.opensymphony.xwork2.config.providers.XmlConfigurationProvider#init(com.opensymphony.xwork2.config.Configuration)
     */
    @Override
    public void loadPackages() {
        ActionContext ctx = ActionContext.getContext();
        ctx.put(reloadKey, Boolean.TRUE);
        super.loadPackages();
    }

    /**
     * Look for the configuration file on the classpath and in the file system
     *
     * @param fileName The file name to retrieve
     * @see com.opensymphony.xwork2.config.providers.XmlConfigurationProvider#getConfigurationUrls
     */
    @Override
    protected Iterator<URL> getConfigurationUrls(String fileName) throws IOException {
    	List<URL> list = new ArrayList<URL>();
    	if(baseDir!=null){
        	File[] files = baseDir.listFiles();
        	for(int i=0;i<files.length;i++){
        		File module = new File(files[i],filename);
        		if(module.exists()){
        			list.add(module.toURL());
        		}
        	}
        }
    	return list.iterator();
    }

    /**
     * Overrides needs reload to ensure it is only checked once per request
     */
    @Override
    public boolean needsReload() {
        ActionContext ctx = ActionContext.getContext();
        if (ctx != null) {
            return ctx.get(reloadKey) == null && super.needsReload();
        } else {
            return super.needsReload();
        }

    }
    
    protected void addAction(Element actionElement, PackageConfig.Builder packageContext) throws ConfigurationException {
        String name = actionElement.getAttribute("name");
        String className = actionElement.getAttribute("class");
        String methodName = actionElement.getAttribute("method");
        Location location = DomHelper.getLocationObject(actionElement);
        if (location == null) {
            LOG.warn("location null for " + className);
        }
        //methodName should be null if it's not set
        methodName = (methodName.trim().length() > 0) ? methodName.trim() : null;
        List<InterceptorMapping> interceptorList = buildInterceptorList(actionElement, packageContext);
        List<ExceptionMappingConfig> exceptionMappings = buildExceptionMappings(actionElement, packageContext);
        Map<String,String> parameters = XmlHelper.getParams(actionElement);
        
        String include = parameters.get("include");
        String[] ins = include.split(",");
        
        ActionConfig.Builder builder = new ActionConfig.Builder(packageContext.getName(), name, className)
        .methodName(methodName)
        .addInterceptors(interceptorList)
        .addExceptionMappings(exceptionMappings)
        .location(location);
        
        for(int i=0;i<ins.length;i++){
        	builder.addAllowedMethod(ins[i]);
        }
        
        ActionConfig actionConfig = builder.build();
        packageContext.addActionConfig(name, actionConfig);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Loaded " + (StringUtils.isNotEmpty(packageContext.getNamespace()) ? (packageContext.getNamespace() + "/") : "") + name + " in '" + packageContext.getName() + "' package:" + actionConfig);
        }
    }
    
    public String toString() {
        return ("Struts XML configuration provider ("+filename+")");
    }
}
