/*
 * $Id: StrutsUtil.java,v 1.5 2011/06/23 06:13:43 cvs_ningcl Exp $
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

package org.xz.qstruts.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;


import org.apache.struts2.util.ClassLoaderUtils;
import org.apache.struts2.views.util.UrlHelper;
import org.xz.qxork2.ActionContext;

import com.opensymphony.xwork2.ObjectFactory;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;


/**
 * Struts base utility class, for use in Velocity and Freemarker templates
 *
 */
public class StrutsUtil {

    protected static final Logger LOG = LoggerFactory.getLogger(StrutsUtil.class);


    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected Map classes = new Hashtable();
    protected ActionContext ac;

    public StrutsUtil(ActionContext ac ,HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        this.ac = ac;
    }


    public Object bean(Object aName) throws Exception {
        String name = aName.toString();
        Class c = (Class) classes.get(name);

        if (c == null) {
            c = ClassLoaderUtils.loadClass(name, StrutsUtil.class);
            classes.put(name, c);
        }

        return ObjectFactory.getObjectFactory().buildBean(c, ac.getContextMap());
    }

    public String include(Object aName) throws Exception {
        return include(aName, request, response);
    }

    /**
     * @deprecated the request and response are stored in this util class, please use include(string)
     */
    public String include(Object aName, HttpServletRequest aRequest, HttpServletResponse aResponse) throws Exception {
        try {
            RequestDispatcher dispatcher = aRequest.getRequestDispatcher(aName.toString());

            if (dispatcher == null) {
                throw new IllegalArgumentException("Cannot find included file " + aName);
            }

            ResponseWrapper responseWrapper = new ResponseWrapper(aResponse);

            dispatcher.include(aRequest, responseWrapper);

            return responseWrapper.getData();
        }
        catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }

    public String buildUrl(String url) {
        return UrlHelper.buildUrl(url, request, response, null);
    }

    /*
     * @return the url ContextPath. An empty string if one does not exist.
     */
    public String getContext() {
        return (request == null)? "" : request.getContextPath();
    }


    public int toInt(long aLong) {
        return (int) aLong;
    }

    public long toLong(int anInt) {
        return (long) anInt;
    }

    public long toLong(String aLong) {
        if (aLong == null) {
            return 0;
        }

        return Long.parseLong(aLong);
    }

    public String toString(long aLong) {
        return Long.toString(aLong);
    }

    public String toString(int anInt) {
        return Integer.toString(anInt);
    }

    public String format(String format,Date date){
    	try{
    		return new SimpleDateFormat(format).format(date);
    	}catch(Exception e){
    		return "";
    	}
    }

    public String format(Date date){
    	return format("yyyy-MM-dd",date);
    }

    public String format(String format,double f){
    	return new DecimalFormat(format).format(f);
    }

    public String format(String format,double f,double t,String def){
    	if(f==t){
    		return def;
    	}
    	return new DecimalFormat(format).format(f);
    }

    static class ResponseWrapper extends HttpServletResponseWrapper {
        StringWriter strout;
        PrintWriter writer;
        ServletOutputStream sout;

        ResponseWrapper(HttpServletResponse aResponse) {
            super(aResponse);
            strout = new StringWriter();
            sout = new ServletOutputStreamWrapper(strout);
            writer = new PrintWriter(strout);
        }

        public String getData() {
            writer.flush();

            return strout.toString();
        }

        public ServletOutputStream getOutputStream() {
            return sout;
        }

        public PrintWriter getWriter() throws IOException {
            return writer;
        }
    }

    static class ServletOutputStreamWrapper extends ServletOutputStream {
        StringWriter writer;

        ServletOutputStreamWrapper(StringWriter aWriter) {
            writer = aWriter;
        }

        public void write(int aByte) {
            writer.write(aByte);
        }
    }

}
