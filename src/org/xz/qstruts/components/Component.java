/*
 * $Id: Component.java,v 1.3 2011/02/14 04:01:31 cvs_ningcl Exp $
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

package org.xz.qstruts.components;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Stack;

import org.apache.struts2.StrutsException;
import org.apache.struts2.dispatcher.mapper.ActionMapper;
import org.apache.struts2.util.FastByteArrayOutputStream;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * Base class to extend for UI components.
 * <p/>
 * This class is a good extension point when building reuseable UI components.
 *
 */
public class Component {
    public static final String COMPONENT_STACK = "__component_stack";

    protected ValueStack stack;

    /**
     * Constructor.
     *
     * @param stack  OGNL value stack.
     */
    public Component(ValueStack stack) {
        this.stack = stack;
        getComponentStack().push(this);
    }

    /**
     * Gets the name of this component.
     * @return the name of this component.
     */
    private String getComponentName() {
        Class c = getClass();
        String name = c.getName();
        int dot = name.lastIndexOf('.');

        return name.substring(dot + 1).toLowerCase();
    }
    
    protected String getComponentModuleName(){
    	Class c = getClass();
        String name = c.getPackage().getName().split("\\.")[2];
        return name.toLowerCase();

    }
    
    /**
     * Gets the OGNL value stack assoicated with this component.
     * @return the OGNL value stack assoicated with this component.
     */
    public ValueStack getStack() {
        return stack;
    }

    /**
     * Callback for the start tag of this component.
     * Should the body be evaluated?
     *
     * @param writer  the output writer.
     * @return true if the body should be evaluated
     */
    public boolean start(Writer writer) {
        return true;
    }

    /**
     * Callback for the end tag of this component.
     * Should the body be evaluated again?
     * <p/>
     * <b>NOTE:</b> will pop component stack.
     * @param writer  the output writer.
     * @param body    the rendered body.
     * @return true if the body should be evaluated again
     */
    public boolean end(Writer writer, String body) {
        return end(writer, body, true);
    }

    /**
     * Callback for the start tag of this component.
     * Should the body be evaluated again?
     * <p/>
     * <b>NOTE:</b> has a parameter to determine to pop the component stack.
     * @param writer  the output writer.
     * @param body    the rendered body.
     * @param popComponentStack  should the component stack be popped?
     * @return true if the body should be evaluated again
     */
    protected boolean end(Writer writer, String body, boolean popComponentStack) {
        assert(body != null);

        try {
            writer.write(body);
        } catch (IOException e) {
            throw new StrutsException("IOError while writing the body: " + e.getMessage(), e);
        }finally{
        	if (popComponentStack) {
                popComponentStack();
            }
        }
        
        return false;
    }


    /**
     * Constructs a string representation of the given exception.
     * @param t  the exception
     * @return the exception as a string.
     */
    protected String toString(Throwable t) {
        FastByteArrayOutputStream bout = new FastByteArrayOutputStream();
        PrintWriter wrt = new PrintWriter(bout);
        t.printStackTrace(wrt);
        wrt.close();

        return bout.toString();
    }

    public Stack getComponentStack() {
        Stack componentStack = (Stack) stack.getContext().get(COMPONENT_STACK);
        if (componentStack == null) {
            componentStack = new Stack();
            stack.getContext().put(COMPONENT_STACK, componentStack);
        }
        return componentStack;
    }
    
    /**
     * Pops the component stack.
     */
    protected void popComponentStack() {
        getComponentStack().pop();
    }
    
    /**
     * Finds the nearest ancestor of this component stack.
     * @param clazz the class to look for, or if assignable from.
     * @return  the component if found, <tt>null</tt> if not.
     */
    protected Component findAncestor(Class clazz) {
        Stack componentStack = getComponentStack();
        int currPosition = componentStack.search(this);
        if (currPosition >= 0) {
            int start = componentStack.size() - currPosition - 1;

            //for (int i = componentStack.size() - 2; i >= 0; i--) {
            for (int i = start; i >=0; i--) {
                Component component = (Component) componentStack.get(i);
                if (clazz.isAssignableFrom(component.getClass()) && component != this) {
                    return component;
                }
            }
        }

        return null;
    }
    
    /**
     * Pushes this component's parameter Map as well as the component itself on to the stack
     * and then copies the supplied parameters over. Because the component's parameter Map is
     * pushed before the component itself, any key-value pair that can't be assigned to componet
     * will be set in the parameters Map.
     *
     * @param params  the parameters to copy.
     */
    public void copyParams(Map params) {
        
    }

    /**
     * Overwrite to set if body shold be used.
     * @return always false for this component.
     */
    public boolean usesBody() {
        return false;
    }
}
