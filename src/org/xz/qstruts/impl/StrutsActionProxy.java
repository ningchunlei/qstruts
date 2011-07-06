/*
 * $Id: StrutsActionProxy.java,v 1.1 2011/01/20 10:24:53 cvs_ningcl Exp $
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

// Copyright 2006 Google Inc. All Rights Reserved.

package org.xz.qstruts.impl;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionEventListener;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.UnknownHandlerManager;
import com.opensymphony.xwork2.XWorkMessages;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.opensymphony.xwork2.util.profiling.UtilTimerStack;


public class StrutsActionProxy implements ActionProxy {

    private static final long serialVersionUID = 3293074152487468527L;

	private static final Logger LOG = LoggerFactory.getLogger(StrutsActionProxy.class);

    protected Configuration configuration;
    protected ActionConfig config;
    protected ActionInvocation invocation;
    protected UnknownHandlerManager unknownHandlerManager;
    protected String actionName;
    protected String namespace;
    protected String method;
    protected boolean executeResult;
    protected boolean cleanupContext;

    protected ObjectFactory objectFactory;

    protected ActionEventListener actionEventListener;
    
   
    /**
     * This constructor is private so the builder methods (create*) should be used to create an DefaultActionProxy.
     * <p/>
     * The reason for the builder methods is so that you can use a subclass to create your own DefaultActionProxy instance
     * (like a RMIActionProxy).
     */
    protected StrutsActionProxy(ActionInvocation inv, String namespace, String actionName, String methodName, boolean executeResult, boolean cleanupContext) {
        
        this.invocation = inv;
		this.cleanupContext = cleanupContext;
		if (LOG.isDebugEnabled()) {
			LOG.debug("Creating an DefaultActionProxy for namespace " + namespace + " and action name " + actionName);
		}

		this.actionName = actionName;
		this.namespace = namespace;
		this.executeResult = executeResult;
        this.method = methodName;
    }
    
    @Inject
    public void setObjectFactory(ObjectFactory factory) {
        this.objectFactory = factory;
    }
    
    @Inject
    public void setConfiguration(Configuration config) {
        this.configuration = config;
    }
    
    @Inject
    public void setUnknownHandler(UnknownHandlerManager unknownHandlerManager) {
        this.unknownHandlerManager = unknownHandlerManager;
    }
    
    @Inject(required=false) 
    public void setActionEventListener(ActionEventListener listener) {
        this.actionEventListener = listener;
    }

    public Object getAction() {
        return invocation.getAction();
    }

    public String getActionName() {
        return actionName;
    }

    public ActionConfig getConfig() {
        return config;
    }
    
    public void setExecuteResult(boolean executeResult) {
        this.executeResult = executeResult;
    }

    public boolean getExecuteResult() {
        return executeResult;
    }

    public ActionInvocation getInvocation() {
        return invocation;
    }

    public String getNamespace() {
        return namespace;
    }

    public String execute() throws Exception {
        String retCode = null;
        try {
            retCode = invocation.invoke();
        } finally {
           
        }
        return retCode;
    }


    public String getMethod() {
        return method;
    }

    private void resolveMethod() {
        // if the method is set to null, use the one from the configuration
        // if the one from the configuration is also null, use "execute"
        if (StringUtils.isEmpty(this.method)) {
            this.method = config.getMethodName();
            if (StringUtils.isEmpty(this.method)) {
                this.method = "execute";
            }
        }
    }

    protected void prepare()  {
        String profileKey = "create DefaultActionProxy: ";
        try {
            UtilTimerStack.push(profileKey);
            config = configuration.getRuntimeConfiguration().getActionConfig(namespace, actionName);
    
            if (config == null && unknownHandlerManager.hasUnknownHandlers()) {
                config = unknownHandlerManager.handleUnknownAction(namespace, actionName);
            }
            if (config == null) {
                String message;
    
                if ((namespace != null) && (namespace.trim().length() > 0)) {
                    message = LocalizedTextUtil.findDefaultText(XWorkMessages.MISSING_PACKAGE_ACTION_EXCEPTION, Locale.getDefault(), new String[]{
                        namespace, actionName
                    });
                } else {
                    message = LocalizedTextUtil.findDefaultText(XWorkMessages.MISSING_ACTION_EXCEPTION, Locale.getDefault(), new String[]{
                        actionName
                    });
                }
                throw new ConfigurationException(message);
            }

            resolveMethod();
            
            if (!config.isAllowedMethod(method)) {
                throw new ConfigurationException("Invalid method: "+method+" for action "+actionName);
            }

            invocation.init(this);

        } finally {
            UtilTimerStack.pop(profileKey);
        }
    }
}
