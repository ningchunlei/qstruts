/**
 * ClassName:ActionContext.java
 * Authoer:ningcl
 * Date:2011-1-10 
 */
package org.xz.qxork2;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.StrutsStatics;
import org.xz.qstruts.dispatcher.Dispatcher;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.XWorkException;
import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author ningcl
 * @version 1.0 
 */
public class ActionContext implements StrutsStatics{
	
	/**
     * Constant that indicates the action is running under a "development mode".
     * This mode provides more feedback that is useful for developers but probably
     * too verbose/error prone for production.
     */
    //public static final String DEV_MODE = "__devMode";

    /**
     * Constant for the name of the action being executed.
     */
    public static final String ACTION_NAME = "com.opensymphony.xwork2.ActionContext.name";

    /**
     * Constant for the {@link com.opensymphony.xwork2.util.ValueStack OGNL value stack}.
     */
    public static final String VALUE_STACK = ValueStack.VALUE_STACK;

    /**
     * Constant for the action's session.
     */
    public static final String SESSION = "com.opensymphony.xwork2.ActionContext.session";

    /**
     * Constant for the action's application context.
     */
    public static final String APPLICATION = "com.opensymphony.xwork2.ActionContext.application";

    /**
     * Constant for the action's parameters.
     */
    public static final String PARAMETERS = "com.opensymphony.xwork2.ActionContext.parameters";

    /**
     * Constant for the action's locale.
     */
    public static final String LOCALE = "com.opensymphony.xwork2.ActionContext.locale";

    /**
     * Constant for the action's type converter.
     */
    public static final String TYPE_CONVERTER = "com.opensymphony.xwork2.ActionContext.typeConverter";

    /**
     * Constant for the action's {@link com.opensymphony.xwork2.ActionInvocation invocation} context.
     */
    public static final String ACTION_INVOCATION = "com.opensymphony.xwork2.ActionContext.actionInvocation";

    /**
     * Constant for the map of type conversion errors.
     */
    public static final String CONVERSION_ERRORS = "com.opensymphony.xwork2.ActionContext.conversionErrors";

    /**
     * Constant for the container
     */
    public static final String CONTAINER = "com.opensymphony.xwork2.ActionContext.container";
    
    
    public static final String MODULENAME = "org.xz.qstruts.module";
    
    public static final String ACTIONCONTEXT = "org.xz.qstruts.actioncontext";
    
    public static final String REQUESTPARAMETERCONTEXT = "org.xz.qstruts.request.parameters";
    
    public static final String APPOBJECT = "org.application.object.";
    
    public static final String MODULEDEV = "org.xz.qstrtus.moduledev";
    
    
    Map<String, Object> context;


    /**
     * Creates a new ActionContext initialized with another context.
     *
     * @param context a context map.
     */
    public ActionContext(Map<String, Object> context) {
        this.context = context;
    }


    /**
     * Sets the action invocation (the execution state).
     *
     * @param actionInvocation the action execution state.
     */
    public void setActionInvocation(ActionInvocation actionInvocation) {
        put(ACTION_INVOCATION, actionInvocation);
    }

    /**
     * Gets the action invocation (the execution state).
     *
     * @return the action invocation (the execution state).
     */
    public ActionInvocation getActionInvocation() {
        return (ActionInvocation) get(ACTION_INVOCATION);
    }

    /**
     * Sets the action's application context.
     *
     * @param application the action's application context.
     */
    public void setApplication(Map<String, Object> application) {
        put(APPLICATION, application);
    }

    /**
     * Returns a Map of the ServletContext when in a servlet environment or a generic application level Map otherwise.
     *
     * @return a Map of ServletContext or generic application level Map
     */
    public Map<String, Object> getApplication() {
        return (Map<String, Object>) get(APPLICATION);
    }


    /**
     * Sets the action's context map.
     *
     * @param contextMap the context map.
     */
    public void setContextMap(Map<String, Object> contextMap) {
        this.context = contextMap;
    }

    /**
     * Gets the context map.
     *
     * @return the context map.
     */
    public Map<String, Object> getContextMap() {
        return context;
    }

    /**
     * Sets conversion errors which occurred when executing the action.
     *
     * @param conversionErrors a Map of errors which occurred when executing the action.
     */
    public void setConversionErrors(Map<String, Object> conversionErrors) {
        put(CONVERSION_ERRORS, conversionErrors);
    }

    /**
     * Gets the map of conversion errors which occurred when executing the action.
     *
     * @return the map of conversion errors which occurred when executing the action or an empty map if
     *         there were no errors.
     */
    public Map<String, Object> getConversionErrors() {
        Map<String, Object> errors = (Map) get(CONVERSION_ERRORS);

        if (errors == null) {
            errors = new HashMap<String, Object>();
            setConversionErrors(errors);
        }

        return errors;
    }

    /**
     * Sets the Locale for the current action.
     *
     * @param locale the Locale for the current action.
     */
    public void setLocale(Locale locale) {
        put(LOCALE, locale);
    }

    /**
     * Gets the Locale of the current action. If no locale was ever specified the platform's
     * {@link java.util.Locale#getDefault() default locale} is used.
     *
     * @return the Locale of the current action.
     */
    public Locale getLocale() {
        Locale locale = (Locale) get(LOCALE);

        if (locale == null) {
            locale = Locale.getDefault();
            setLocale(locale);
        }

        return locale;
    }
    
    public String getModuleName(){
    	return (String)get(MODULENAME);
    }
    
    public void setModuleName(String name){
    	put(MODULENAME,name);
    }
    
    /**
     * Sets the name of the current Action in the ActionContext.
     *
     * @param name the name of the current action.
     */
    public void setName(String name) {
        put(ACTION_NAME, name);
    }

    /**
     * Gets the name of the current Action.
     *
     * @return the name of the current action.
     */
    public String getName() {
        return (String) get(ACTION_NAME);
    }

    /**
     * Sets the action parameters.
     *
     * @param parameters the parameters for the current action.
     */
    public void setParameters(Map<String, Object> parameters) {
        put(PARAMETERS, parameters);
    }

    /**
     * Returns a Map of the HttpServletRequest parameters when in a servlet environment or a generic Map of
     * parameters otherwise.
     *
     * @return a Map of HttpServletRequest parameters or a multipart map when in a servlet environment, or a
     *         generic Map of parameters otherwise.
     */
    public Map<String, Object> getParameters() {
        return (Map<String, Object>) get(PARAMETERS);
    }

    /**
     * Sets a map of action session values.
     *
     * @param session  the session values.
     */
    public void setSession(Map<String, Object> session) {
        put(SESSION, session);
    }

    /**
     * Gets the Map of HttpSession values when in a servlet environment or a generic session map otherwise.
     *
     * @return the Map of HttpSession values when in a servlet environment or a generic session map otherwise.
     */
    public Map<String, Object> getSession() {
        return (Map<String, Object>) get(SESSION);
    }

    /**
     * Sets the OGNL value stack.
     *
     * @param stack the OGNL value stack.
     */
    public void setValueStack(ValueStack stack) {
        put(VALUE_STACK, stack);
    }

    /**
     * Gets the OGNL value stack.
     *
     * @return the OGNL value stack.
     */
    public ValueStack getValueStack() {
        return (ValueStack) get(VALUE_STACK);
    }
    
    /**
     * Gets the container for this request
     * 
     * @param cont The container
     */
    public void setContainer(Container cont) {
        put(CONTAINER, cont);
    }
    
    /**
     * Sets the container for this request
     * 
     * @return The container
     */
    public Container getContainer() {
        return (Container) get(CONTAINER);
    }
    
    public <T> T getInstance(Class<T> type) {
        Container cont = getContainer();
        if (cont != null) {
            return cont.getInstance(type);
        } else {
            throw new XWorkException("Cannot find an initialized container for this request.");
        }
    }

    /**
     * Returns a value that is stored in the current ActionContext by doing a lookup using the value's key.
     *
     * @param key the key used to find the value.
     * @return the value that was found using the key or <tt>null</tt> if the key was not found.
     */
    public Object get(String key) {
        return context.get(key);
    }

    /**
     * Stores a value in the current ActionContext. The value can be looked up using the key.
     *
     * @param key   the key of the value.
     * @param value the value to be stored.
     */
    public void put(String key, Object value) {
        context.put(key, value);
    }
    
    public static HttpServletRequest getHttpRequest(Map context)
	{
		return (HttpServletRequest)context.get(ActionContext.HTTP_REQUEST);
	}
	 
	public static HttpServletResponse getHttpResponse(Map context)
	{
		return (HttpServletResponse)context.get(ActionContext.HTTP_RESPONSE);
	}
	
	public static ServletContext getServletContext(Map context){
		return (ServletContext)context.get(ActionContext.SERVLET_CONTEXT);
	}
	
	public static String getModuleName(Map context){
		return (String)context.get(ActionContext.MODULENAME);
	}
	
	public static String getResponseContent(ModuleResponse response) throws UnsupportedEncodingException, IOException{
		String encoding = response.getCharacterEncoding();
		if(encoding==null){
			encoding = Dispatcher.getInstance().getEncoding();
		}
		return new String(response.getContent().toByteArray(),encoding);
	}
	
}
