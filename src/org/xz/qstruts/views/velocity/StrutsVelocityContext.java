/*
 * $Id: StrutsVelocityContext.java,v 1.1 2011/01/20 10:24:53 cvs_ningcl Exp $
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

package org.xz.qstruts.views.velocity;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.VelocityContext;
import org.xz.qxork2.ActionContext;

import com.opensymphony.xwork2.util.ValueStack;


/**
 */
public class StrutsVelocityContext extends VelocityContext {

    private static final long serialVersionUID = 8497212428904436963L;
    HttpServletRequest stack;


    public StrutsVelocityContext(HttpServletRequest stack) {
        this.stack = stack;
    }

   
    public boolean internalContainsKey(Object key) {
        boolean contains = super.internalContainsKey(key);

        // first let's check to see if we contain the requested key
        if (contains) {
            return true;
        }

        // if not, let's search for the key in the ognl value stack
        if (stack != null) {
            Object o = stack.getAttribute(key.toString());
            if (o != null) {
                return true;
            }

        }
        // nope, i guess it's really not here
        return false;
    }

    public Object internalGet(String key) {
        // first, let's check to see if have the requested value
        if (super.internalContainsKey(key)) {
            return super.internalGet(key);
        }

        // still no luck?  let's look against the value stack
        if (stack != null) {
        	 Object object = stack.getAttribute(key.toString());

            if (object != null) {
                return object;
            }

        }

        // nope, i guess it's really not here
        return null;
    }
}
