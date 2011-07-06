/*
 * Copyright 2002-2006,2009 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.xz.qstruts.impl;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionEventListener;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.XWorkException;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.config.entities.ActionConfig;
import com.opensymphony.xwork2.config.entities.InterceptorMapping;
import com.opensymphony.xwork2.config.entities.ResultConfig;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.interceptor.PreResultListener;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import com.opensymphony.xwork2.util.profiling.UtilTimerStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.xz.qstruts.dispatcher.CacheResult;
import org.xz.qstruts.dispatcher.Dispatcher;
import org.xz.qxork2.ActionContext;
import org.xz.qxork2.ActionContextProvider;
import org.xz.qxork2.ModuleResponse;
import org.xz.qxork2.QActionCache;
import org.xz.qxork2.UnknownHandlerManager;
import org.xz.qxork2.vstack.MapValueStack;


/**
 * The Default ActionInvocation implementation
 *
 * @author Rainer Hermanns
 * @author tmjee
 * @version $Date: 2011/03/25 10:23:57 $ $Id: QStrutsActionInvocation.java,v 1.6 2011/03/25 10:23:57 cvs_ningcl Exp $
 * @see com.opensymphony.xwork2.DefaultActionProxy
 */
public class QStrutsActionInvocation  implements ActionInvocation{
	
		private static final long serialVersionUID = -585293628862447329L;

		private static final Logger LOG = LoggerFactory.getLogger(QStrutsActionInvocation.class);

		private static final Class[] EMPTY_CLASS_ARRAY = new Class[0];
		
		private static ConcurrentHashMap<String, Method> methods = new ConcurrentHashMap();

	    protected Object action;
	    protected ActionProxy proxy;
	    protected List<PreResultListener> preResultListeners;
	    protected Map<String, Object> extraContext;
	    protected ActionContext invocationContext;
	    protected Iterator<InterceptorMapping> interceptors;
	    protected Result result;
	    protected Result explicitResult;
	    protected String resultCode;
	    protected boolean executed = false;
	    protected boolean pushAction = true;
	    protected ObjectFactory objectFactory;
	    protected Container container;
	    private Configuration configuration;
	    protected UnknownHandlerManager unknownHandlerManager;
	    private ValueStack valueStack;

	    public QStrutsActionInvocation(final Map<String, Object> extraContext, final boolean pushAction) {
	    	QStrutsActionInvocation.this.extraContext = extraContext;
	    	QStrutsActionInvocation.this.pushAction = pushAction;
	    }

	    @Inject
	    public void setUnknownHandlerManager(UnknownHandlerManager unknownHandlerManager) {
	        this.unknownHandlerManager = unknownHandlerManager;
	    }

	  
	    @Inject
	    public void setConfiguration(Configuration configuration) {
	        this.configuration = configuration;
	    }

	    @Inject
	    public void setObjectFactory(ObjectFactory fac) {
	        this.objectFactory = fac;
	    }

	    @Inject
	    public void setContainer(Container cont) {
	        this.container = cont;
	    }

	   
	    public Object getAction() {
	        return action;
	    }

	    public boolean isExecuted() {
	        return executed;
	    }

	    public ActionProxy getProxy() {
	        return proxy;
	    }

	    /**
	     * If the DefaultActionInvocation has been executed before and the Result is an instance of ActionChainResult, this method
	     * will walk down the chain of ActionChainResults until it finds a non-chain result, which will be returned. If the
	     * DefaultActionInvocation's result has not been executed before, the Result instance will be created and populated with
	     * the result params.
	     *
	     * @return a Result instance
	     * @throws Exception
	     */
	    public Result getResult() throws Exception {
	        Result returnResult = result;

	        return returnResult;
	    }

	    public String getResultCode() {
	        return resultCode;
	    }

	    public void setResultCode(String resultCode) {
	        if (isExecuted())
	            throw new IllegalStateException("Result has already been executed.");

	        this.resultCode = resultCode;
	    }

	    /**
	     * Register a com.opensymphony.xwork2.interceptor.PreResultListener to be notified after the Action is executed and before the
	     * Result is executed. The ActionInvocation implementation must guarantee that listeners will be called in the order
	     * in which they are registered. Listener registration and execution does not need to be thread-safe.
	     *
	     * @param listener
	     */
	    public void addPreResultListener(PreResultListener listener) {
	        if (preResultListeners == null) {
	            preResultListeners = new ArrayList<PreResultListener>(1);
	        }

	        preResultListeners.add(listener);
	    }

	    public Result createResult() throws Exception {

	        if (explicitResult != null) {
	            Result ret = explicitResult;
	            explicitResult = null;

	            return ret;
	        }
	        ActionConfig config = proxy.getConfig();
	        Map<String, ResultConfig> results = config.getResults();

	        ResultConfig resultConfig = null;

	        try {
	            resultConfig = results.get(resultCode);
	        } catch (NullPointerException e) {
	            // swallow
	        }
	        
	        if (resultConfig == null) {
	            // If no result is found for the given resultCode, try to get a wildcard '*' match.
	            resultConfig = results.get("*");
	        }

	        if (resultConfig != null) {
	            try {
	                return objectFactory.buildResult(resultConfig, invocationContext.getContextMap());
	            } catch (Exception e) {
	                LOG.error("There was an exception while instantiating the result of type " + resultConfig.getClassName(), e);
	                throw new XWorkException(e, resultConfig);
	            }
	        } else if (resultCode != null && !Action.NONE.equals(resultCode) && unknownHandlerManager.hasUnknownHandlers()) {
	            return unknownHandlerManager.handleUnknownResult(invocationContext, proxy.getActionName(), proxy.getConfig(), resultCode);
	        }
	        return null;
	    }
	    
	    
	    /**
	     * @throws ConfigurationException If no result can be found with the returned code
	     */
	    public String invoke() throws Exception {
	        String profileKey = "invoke: ";
	        try {
	            UtilTimerStack.push(profileKey);

	            if (executed) {
	                throw new IllegalStateException("Action has already executed");
	            }

	            if (interceptors.hasNext()) {
	                final InterceptorMapping interceptor = (InterceptorMapping) interceptors.next();
	                String interceptorMsg = "interceptor: " + interceptor.getName();
	                UtilTimerStack.push(interceptorMsg);
	                try {
	                                resultCode = interceptor.getInterceptor().intercept(this);
	                            }
	                finally {
	                    UtilTimerStack.pop(interceptorMsg);
	                }
	            } else {
	                resultCode = invokeActionOnly();
	            }

	            // this is needed because the result will be executed, then control will return to the Interceptor, which will
	            // return above and flow through again
	            if (!executed) {
	                if (preResultListeners != null) {
	                    for (Object preResultListener : preResultListeners) {
	                        PreResultListener listener = (PreResultListener) preResultListener;

	                        String _profileKey = "preResultListener: ";
	                        try {
	                            UtilTimerStack.push(_profileKey);
	                            listener.beforeResult(this, resultCode);
	                        }
	                        finally {
	                            UtilTimerStack.pop(_profileKey);
	                        }
	                    }
	                }

	                // now execute the result, if we're supposed to
	                if (proxy.getExecuteResult()) {
	                    executeResult();
	                }

	                executed = true;
	            }

	            return resultCode;
	        }
	        finally {
	            UtilTimerStack.pop(profileKey);
	        }
	    }

	    public String invokeActionOnly() throws Exception {
	        return invokeAction(getAction(), proxy.getConfig());
	    }

	    protected void createAction(Map<String, Object> contextMap) {
	        // load action
	        String timerKey = "actionCreate: " + proxy.getActionName();
	        try {
	            UtilTimerStack.push(timerKey);
	            if(proxy.getConfig().getClassName().equals("$v")){
	            	action = objectFactory.buildBean(Dispatcher.getInstance().getDefaultSpringId(), contextMap);
	            }else{
	            	action = objectFactory.buildAction(proxy.getActionName(), proxy.getNamespace(), proxy.getConfig(), contextMap);
	            }
	        } catch (InstantiationException e) {
	            throw new XWorkException("Unable to intantiate Action!", e, proxy.getConfig());
	        } catch (IllegalAccessException e) {
	            throw new XWorkException("Illegal access to constructor, is it public?", e, proxy.getConfig());
	        } catch (Exception e) {
	            String gripe = "";

	            if (proxy == null) {
	                gripe = "Whoa!  No ActionProxy instance found in current ActionInvocation.  This is bad ... very bad";
	            } else if (proxy.getConfig() == null) {
	                gripe = "Sheesh.  Where'd that ActionProxy get to?  I can't find it in the current ActionInvocation!?";
	            } else if (proxy.getConfig().getClassName() == null) {
	                gripe = "No Action defined for '" + proxy.getActionName() + "' in namespace '" + proxy.getNamespace() + "'";
	            } else {
	                gripe = "Unable to instantiate Action, " + proxy.getConfig().getClassName() + ",  defined for '" + proxy.getActionName() + "' in namespace '" + proxy.getNamespace() + "'";
	            }

	            gripe += (((" -- " + e.getMessage()) != null) ? e.getMessage() : " [no message in exception]");
	            throw new XWorkException(gripe, e, proxy.getConfig());
	        } finally {
	            UtilTimerStack.pop(timerKey);
	        }
	    }

	    protected Map<String, Object> createContextMap() {
	        //put this DefaultActionInvocation into the context map
	    	extraContext.put(ActionContext.ACTION_INVOCATION, this);
	    	extraContext.put(ActionContext.CONTAINER, container);

	        return extraContext;
	    }

	    /**
	     * Uses getResult to get the final Result and executes it
	     *
	     * @throws ConfigurationException If not result can be found with the returned code
	     */
	    private void executeResult() throws Exception {
	        result = createResult();

	        String timerKey = "executeResult: " + getResultCode();
	        try {
	            UtilTimerStack.push(timerKey);
	            if (result != null) {
	                result.execute(this);
                	/*if(action instanceof QActionCache){
	            		((QActionCache)action).setCache(this,ActionContext.getResponseContent(
	            				(ModuleResponse)(invocationContext.getHttpResponse(extraContext))));
                	}*/
	            } else if (resultCode != null && !Action.NONE.equals(resultCode)) {
	                throw new ConfigurationException("No result defined for action " + getAction().getClass().getName()
	                        + " and result " + getResultCode(), proxy.getConfig());
	            } else {
	                if (LOG.isDebugEnabled()) {
	                    LOG.debug("No result returned for action " + getAction().getClass().getName() + " at " + proxy.getConfig().getLocation());
	                }
	            }
	        } finally {
	            UtilTimerStack.pop(timerKey);
	        }
	    }

	    public void init(ActionProxy proxy) {
	        this.proxy = proxy;
	        Map<String, Object> contextMap = createContextMap();

	        createAction(contextMap);

	        if (pushAction) {
	            contextMap.put("action", action);
	        }

	        invocationContext = new ActionContext(contextMap);
	        invocationContext.setName(proxy.getActionName());
	        String mod = proxy.getNamespace();
	        if(mod.startsWith("/")){
	        	mod = mod.substring(1);	
	        }
	        /*int index = -1;
	        if((index=mod.indexOf("/"))!=-1){
	        	mod = mod.substring(0,index);
	        }*/
	        invocationContext.setModuleName(mod);
	        valueStack = new MapValueStack(contextMap);
	        invocationContext.setValueStack(valueStack);
	        
	        contextMap.put(ActionContext.ACTIONCONTEXT, invocationContext);
	        
	        // get a new List so we don't get problems with the iterator if someone changes the list
	        List<InterceptorMapping> interceptorList = new ArrayList<InterceptorMapping>(proxy.getConfig().getInterceptors());
	        interceptors = interceptorList.iterator();
	        
	        if(action instanceof ActionContextProvider){
	        	((ActionContextProvider) action).setActionContext(invocationContext);
	        }
	    }

	    protected String invokeAction(Object action, ActionConfig actionConfig) throws Exception {
	        String methodName = proxy.getMethod();
	        
	        boolean isDefaultAction = false;
	        if(actionConfig.getClassName().equals("$v")){
	        	methodName = "show";
	        	isDefaultAction = true;
	        }
	        
	        if (LOG.isDebugEnabled()) {
	            LOG.debug("Executing action method = " + actionConfig.getMethodName());
	        }

	        String timerKey = "invokeAction: " + proxy.getActionName();
	        try {
	            UtilTimerStack.push(timerKey);

	            boolean methodCalled = false;
	            Object methodResult = null;
	            Method method = null;
	            try {
	            	if(isDefaultAction){
	            		String key = getAction().getClass().getName()+methodName;
		            	method = methods.get(key);
		            	if(method==null){
		            		method = getAction().getClass().getMethod(methodName, EMPTY_CLASS_ARRAY);
		            		methods.put(key, method);
		            	}
	            	}else{
	            		String key = getAction().getClass().getName()+"_"+methodName;
		            	method = methods.get(key);
		            	if(method==null){
		            		method = getAction().getClass().getMethod("_"+methodName, EMPTY_CLASS_ARRAY);
		            		methods.put(key, method);
		            	}
	            	}
	            } catch (NoSuchMethodException e) {
	            	 if (unknownHandlerManager.hasUnknownHandlers()) {
	                     try {
	                         methodResult = unknownHandlerManager.handleUnknownMethod(action, methodName);
	                         methodCalled = true;
	                     } catch (NoSuchMethodException e2) {
	                         // throw the original one
	                         throw e;
	                     }
	                 } else {
	                     throw e;
	                 }
	            }

	            if (!methodCalled) {
	            	if(action instanceof QActionCache){
	            		Result r = ((QActionCache)action).getCache(this);
	            		if(r!=null){
	            			 methodResult = r; 
	            		}else{
	            			 methodResult = method.invoke(action, new Object[0]);
	            		}
	            	}else{
	            		methodResult = method.invoke(action, new Object[0]);
	            	}
	               
	            }

	            if (methodResult instanceof Result) {
	                this.explicitResult = (Result) methodResult;
	                // Wire the result automatically
	                container.inject(explicitResult);
	                return null;
	            } else {
	                return (String) methodResult;
	            }
	        } catch (NoSuchMethodException e) {
	            throw new IllegalArgumentException("The " + methodName + "() is not defined in action " + getAction().getClass() + "");
	        } catch (InvocationTargetException e) {
	            // We try to return the source exception.
	            e.printStackTrace();
	        	Throwable t = e.getTargetException();
	            if (t instanceof Exception) {
	                throw (Exception) t;
	            } else {
	                throw e;
	            }
	        } finally {
	            UtilTimerStack.pop(timerKey);
	        }
	    }
	    
	 
		/* (non-Javadoc)
		 * @see com.opensymphony.xwork2.ActionInvocation#getStack()
		 */
		public ValueStack getStack() {
			// TODO Auto-generated method stub
			return invocationContext.getValueStack();
		}

		/* (non-Javadoc)
		 * @see com.opensymphony.xwork2.ActionInvocation#setActionEventListener(com.opensymphony.xwork2.ActionEventListener)
		 */
		public void setActionEventListener(
				ActionEventListener actioneventlistener) {
			// TODO Auto-generated method stub
			
		}

		/* (non-Javadoc)
		 * @see com.opensymphony.xwork2.ActionInvocation#getInvocationContext()
		 */
		public com.opensymphony.xwork2.ActionContext getInvocationContext() {
			// TODO Auto-generated method stub
			return new com.opensymphony.xwork2.ActionContext(invocationContext.getContextMap());
		}

	
}
