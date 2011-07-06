/**
 * ClassName:ServletDispatcherResult.java
 * Authoer:ningcl
 * Date:2011-1-11 
 */
package org.xz.qstruts.dispatcher;

import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.lang.xwork.ObjectUtils;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.dispatcher.StrutsResultSupport;
import org.apache.struts2.views.util.UrlHelper;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

/**
 * @author ningcl
 * @version 1.0 
 */
public class ServletDispatcherResult extends StrutsResultSupport{
	
	 private static final long serialVersionUID = -1970659272360685627L;

	    private static final Logger LOG = LoggerFactory.getLogger(ServletDispatcherResult.class);

	    public ServletDispatcherResult() {
	        super();
	    }

	    public ServletDispatcherResult(String location) {
	        super(location,false,false);
	    }

	    /**
	     * Dispatches to the given location. Does its forward via a RequestDispatcher. If the
	     * dispatch fails a 404 error will be sent back in the http response.
	     *
	     * @param finalLocation the location to dispatch to.
	     * @param invocation    the execution state of the action
	     * @throws Exception if an error occurs. If the dispatch fails the error will go back via the
	     *                   HTTP request.
	     */
	    public void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {
	        if (LOG.isDebugEnabled()) {
	            LOG.debug("Forwarding to location " + finalLocation);
	        }

	        PageContext pageContext = (PageContext)invocation.getInvocationContext().get(StrutsStatics.PAGE_CONTEXT);

	        if (pageContext != null) {
	            pageContext.include(finalLocation);
	        } else {
	            HttpServletRequest request = (HttpServletRequest)invocation.getInvocationContext().get(StrutsStatics.HTTP_REQUEST);
	            HttpServletResponse response =(HttpServletResponse)invocation.getInvocationContext().get(StrutsStatics.HTTP_RESPONSE);
	            RequestDispatcher dispatcher = request.getRequestDispatcher(finalLocation);

	            //add parameters passed on the location to #parameters
	            // see WW-2120
	            if (invocation != null && finalLocation != null && finalLocation.length() > 0
	                    && finalLocation.indexOf("?") > 0) {
	                String queryString = finalLocation.substring(finalLocation.indexOf("?") + 1);
	                Map parameters = (Map) invocation.getInvocationContext().getContextMap().get("parameters");
	                Map queryParams = UrlHelper.parseQueryString(queryString, true);
	                if (queryParams != null && !queryParams.isEmpty())
	                    parameters.putAll(queryParams);
	            }

	            // if the view doesn't exist, let's do a 404
	            if (dispatcher == null) {
	                response.sendError(404, "result '" + finalLocation + "' not found");

	                return;
	            }

	            //if we are inside an action tag, we always need to do an include
	            Boolean insideActionTag = (Boolean) ObjectUtils.defaultIfNull(request.getAttribute(StrutsStatics.STRUTS_ACTION_TAG_INVOCATION), Boolean.FALSE);

	            // If we're included, then include the view
	            // Otherwise do forward
	            // This allow the page to, for example, set content type
	            if (!insideActionTag && !response.isCommitted() && (request.getAttribute("javax.servlet.include.servlet_path") == null)) {
	                request.setAttribute("struts.view_uri", finalLocation);
	                request.setAttribute("struts.request_uri", request.getRequestURI());

	                dispatcher.forward(request, response);
	            } else {
	                dispatcher.include(request, response);
	            }
	        }
	    }
	
}
