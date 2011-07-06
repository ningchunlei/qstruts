/*
 * $Id: TagUtils.java,v 1.1 2011/01/20 10:24:53 cvs_ningcl Exp $
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

package org.xz.qstruts.views.jsp;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.jsp.PageContext;

import org.apache.struts2.RequestUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.ApplicationMap;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.dispatcher.RequestMap;
import org.apache.struts2.dispatcher.SessionMap;
import org.apache.struts2.dispatcher.mapper.ActionMapper;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.util.AttributeMap;
import org.apache.struts2.util.FastByteArrayOutputStream;
import org.xz.qxork2.ActionContext;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.ValueStackFactory;


/**
 */
public class TagUtils {

    public static ValueStack getStack(PageContext pageContext) {
        HttpServletRequest req = (HttpServletRequest) pageContext.getRequest();
        ValueStack stack = (ValueStack) req.getAttribute(ActionContext.VALUE_STACK);
        stack.set(ActionContext.PAGE_CONTEXT, pageContext);
        return stack;
    }

    public static String buildNamespace(ActionMapper mapper, ValueStack stack, HttpServletRequest request) {
        ActionContext context = new ActionContext(stack.getContext());
        ActionInvocation invocation = context.getActionInvocation();

        if (invocation == null) {
            ActionMapping mapping = mapper.getMapping(request,
                    Dispatcher.getInstance().getConfigurationManager());

            if (mapping != null) {
                return mapping.getNamespace();
            } else {
                // well, if the ActionMapper can't tell us, and there is no existing action invocation,
                // let's just go with a default guess that the namespace is the last the path minus the
                // last part (/foo/bar/baz.xyz -> /foo/bar)

                String path = RequestUtils.getServletPath(request);
                return path.substring(0, path.lastIndexOf("/"));
            }
        } else {
            return invocation.getProxy().getNamespace();
        }
    }
    
    public static void include(String aResult, Writer writer, ServletRequest request, HttpServletResponse response,String encoding) throws ServletException, IOException {
        String resourcePath = aResult;
        RequestDispatcher rd = request.getRequestDispatcher(resourcePath);

        if (rd == null) {
            throw new ServletException("Not a valid resource path:" + resourcePath);
        }

        PageResponse pageResponse = new PageResponse(response);

        // Include the resource
        rd.include((HttpServletRequest) request, pageResponse);

        if (encoding != null) {
            //use the encoding specified in the property file
            pageResponse.getContent().writeTo(writer, encoding);
        } else {
            //use the platform specific encoding
            pageResponse.getContent().writeTo(writer, null);
        }
    }
    
   
    
    
    /**
     * Implementation of ServletOutputStream that stores all data written
     * to it in a temporary buffer accessible from {@link #getBuffer()} .
     *
     * @author <a href="joe@truemesh.com">Joe Walnes</a>
     * @author <a href="mailto:scott@atlassian.com">Scott Farquhar</a>
     */
    static final class PageOutputStream extends ServletOutputStream {

        private FastByteArrayOutputStream buffer;


        public PageOutputStream() {
            buffer = new FastByteArrayOutputStream();
        }


        /**
         * Return all data that has been written to this OutputStream.
         */
        public FastByteArrayOutputStream getBuffer() throws IOException {
            flush();

            return buffer;
        }

        public void close() throws IOException {
            buffer.close();
        }

        public void flush() throws IOException {
            buffer.flush();
        }

        public void write(byte[] b, int o, int l) throws IOException {
            buffer.write(b, o, l);
        }

        public void write(int i) throws IOException {
            buffer.write(i);
        }

        public void write(byte[] b) throws IOException {
            buffer.write(b);
        }
    }


    /**
     * Simple wrapper to HTTPServletResponse that will allow getWriter()
     * and getResponse() to be called as many times as needed without
     * causing conflicts.
     * <p/>
     * The underlying outputStream is a wrapper around
     * {@link PageOutputStream} which will store
     * the written content to a buffer.
     * <p/>
     * This buffer can later be retrieved by calling {@link #getContent}.
     *
     * @author <a href="mailto:joe@truemesh.com">Joe Walnes</a>
     * @author <a href="mailto:scott@atlassian.com">Scott Farquhar</a>
     */
    static final class PageResponse extends HttpServletResponseWrapper {

        protected PrintWriter pagePrintWriter;
        protected ServletOutputStream outputStream;
        private PageOutputStream pageOutputStream = null;


        /**
         * Create PageResponse wrapped around an existing HttpServletResponse.
         */
        public PageResponse(HttpServletResponse response) {
            super(response);
        }


        /**
         * Return the content buffered inside the {@link PageOutputStream}.
         *
         * @return
         * @throws IOException
         */
        public FastByteArrayOutputStream getContent() throws IOException {
            //if we are using a writer, we need to flush the
            //data to the underlying outputstream.
            //most containers do this - but it seems Jetty 4.0.5 doesn't
            if (pagePrintWriter != null) {
                pagePrintWriter.flush();
            }

            return ((PageOutputStream) getOutputStream()).getBuffer();
        }

        /**
         * Return instance of {@link PageOutputStream}
         * allowing all data written to stream to be stored in temporary buffer.
         */
        public ServletOutputStream getOutputStream() throws IOException {
            if (pageOutputStream == null) {
                pageOutputStream = new PageOutputStream();
            }

            return pageOutputStream;
        }

        /**
         * Return PrintWriter wrapper around PageOutputStream.
         */
        public PrintWriter getWriter() throws IOException {
            if (pagePrintWriter == null) {
                pagePrintWriter = new PrintWriter(new OutputStreamWriter(getOutputStream(), getCharacterEncoding()));
            }

            return pagePrintWriter;
        }
    }
    
}
