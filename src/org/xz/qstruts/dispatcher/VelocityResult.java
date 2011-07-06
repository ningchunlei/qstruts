/*
 * $Id: VelocityResult.java,v 1.5 2011/07/06 02:45:39 cvs_ningcl Exp $
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

package org.xz.qstruts.dispatcher;

import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;

import org.apache.struts2.StrutsConstants;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.dispatcher.StrutsResultSupport;
import org.apache.struts2.views.JspSupportServlet;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.xz.qstruts.views.velocity.VelocityManager;
import org.xz.qxork2.ActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;


/**
 * <!-- START SNIPPET: description -->
 *
 * Using the Servlet container's {@link JspFactory}, this result mocks a JSP
 * execution environment and then displays a Velocity template that will be
 * streamed directly to the servlet output.
 *
 * <!-- END SNIPPET: description -->
 * <p/>
 * <b>This result type takes the following parameters:</b>
 *
 * <!-- START SNIPPET: params -->
 *
 * <ul>
 *
 * <li><b>location (default)</b> - the location of the template to process.</li>
 *
 * <li><b>parse</b> - true by default. If set to false, the location param will
 * not be parsed for Ognl expressions.</li>
 *
 * </ul>
 * <p>
 * This result follows the same rules from {@link StrutsResultSupport}.
 * </p>
 *
 * <!-- END SNIPPET: params -->
 *
 * <b>Example:</b>
 *
 * <pre><!-- START SNIPPET: example -->
 * &lt;result name="success" type="velocity"&gt;
 *   &lt;param name="location"&gt;foo.vm&lt;/param&gt;
 * &lt;/result&gt;
 * <!-- END SNIPPET: example --></pre>
 *
 */
public class VelocityResult extends StrutsResultSupport {

    private static final long serialVersionUID = 7268830767762559424L;

    private static final Logger LOG = LoggerFactory.getLogger(VelocityResult.class);

    private String defaultEncoding;
    private VelocityManager velocityManager;

    public VelocityResult() {
        super();
    }

    public VelocityResult(String location) {
        super(location,false,false);
    }

    @Inject(StrutsConstants.STRUTS_I18N_ENCODING)
    public void setDefaultEncoding(String val) {
        defaultEncoding = val;
    }

    @Inject
    public void setVelocityManager(VelocityManager mgr) {
        this.velocityManager = mgr;
    }

    /**
     * Creates a Velocity context from the action, loads a Velocity template and executes the
     * template. Output is written to the servlet output stream.
     *
     * @param finalLocation the location of the Velocity template
     * @param invocation    an encapsulation of the action execution state.
     * @throws Exception if an error occurs when creating the Velocity context, loading or executing
     *                   the template or writing output to the servlet response stream.
     */
    public void doExecute(String finalLocation, ActionInvocation invocation) throws Exception {

        HttpServletRequest request = (HttpServletRequest)invocation.getInvocationContext().get(StrutsStatics.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse)invocation.getInvocationContext().get(StrutsStatics.HTTP_RESPONSE);
        JspFactory jspFactory = null;
        ServletContext servletContext = (ServletContext)invocation.getInvocationContext().get(StrutsStatics.SERVLET_CONTEXT);
        Servlet servlet = JspSupportServlet.jspSupportServlet;

        velocityManager.init(servletContext);

        try {
            String encoding = getEncoding(finalLocation);
            String contentType = getContentType(finalLocation);

            if (encoding != null) {
                contentType = contentType + ";charset=" + encoding;
            }

            Template t = getTemplate(velocityManager.getVelocityEngine(), invocation, finalLocation, encoding);

            Context context = createContext(new ActionContext(invocation.getInvocationContext().getContextMap()), request, response);
            Writer writer = new OutputStreamWriter(response.getOutputStream(), encoding);

            response.setContentType(contentType);
            //writer.append("<!--BEGIN-").append(finalLocation).append("-->");
            t.merge(context, writer);
            //writer.append("<!--END-").append(finalLocation).append("-->");
            // always flush the writer (we used to only flush it if this was a jspWriter, but someone asked
            // to do it all the time (WW-829). Since Velocity support is being deprecated, we'll oblige :)
            writer.flush();
        } catch (Exception e) {
            LOG.error("Unable to render Velocity Template, '" + finalLocation + "'", e);
            throw e;
        }

        return;
    }

    /**
     * Retrieve the content type for this template.
     * <p/>
     * People can override this method if they want to provide specific content types for specific templates (eg text/xml).
     *
     * @return The content type associated with this template (default "text/html")
     */
    protected String getContentType(String templateLocation) {
        return "text/html";
    }

    /**
     * Retrieve the encoding for this template.
     * <p/>
     * People can override this method if they want to provide specific encodings for specific templates.
     *
     * @return The encoding associated with this template (defaults to the value of 'struts.i18n.encoding' property)
     */
    protected String getEncoding(String templateLocation) {
        String encoding = defaultEncoding;
        if (encoding == null) {
            encoding = System.getProperty("file.encoding");
        }
        if (encoding == null) {
            encoding = "UTF-8";
        }
        return encoding;
    }

    /**
     * Given a value stack, a Velocity engine, and an action invocation, this method returns the appropriate
     * Velocity template to render.
     *
     * @param stack      the value stack to resolve the location again (when parse equals true)
     * @param velocity   the velocity engine to process the request against
     * @param invocation an encapsulation of the action execution state.
     * @param location   the location of the template
     * @param encoding   the charset encoding of the template
     * @return the template to render
     * @throws Exception when the requested template could not be found
     */
    protected Template getTemplate(VelocityEngine velocity, ActionInvocation invocation, String location, String encoding) throws Exception {
        if (!location.startsWith("/") && !location.startsWith("path:")) {
            location = invocation.getProxy().getNamespace() + "/" + location;
        }

        Template template = velocity.getTemplate(location, encoding);

        return template;
    }

    /**
     * Creates the VelocityContext that we'll use to render this page.
     *
     * @param velocityManager a reference to the velocityManager to use
     * @param stack           the value stack to resolve the location against (when parse equals true)
     * @param location        the name of the template that is being used
     * @return the a minted Velocity context.
     */
    protected Context createContext(ActionContext ac, HttpServletRequest request, HttpServletResponse response) {
        return velocityManager.createContext(ac, request, response);
    }
}
