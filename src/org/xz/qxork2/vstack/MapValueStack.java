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
package org.xz.qxork2.vstack;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.opensymphony.xwork2.util.ClearableValueStack;
import com.opensymphony.xwork2.util.CompoundRoot;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * Ognl implementation of a value stack that allows for dynamic Ognl expressions to be evaluated against it. When evaluating an expression,
 * the stack will be searched down the stack, from the latest objects pushed in to the earliest, looking for a bean with a getter or setter
 * for the given property or a method of the given name (depending on the expression being evaluated).
 *
 * @author Patrick Lightbody
 * @author tm_jee
 * @version $Date: 2011/01/20 10:24:53 $ $Id: MapValueStack.java,v 1.1 2011/01/20 10:24:53 cvs_ningcl Exp $
 */
public class MapValueStack implements Serializable, ValueStack, ClearableValueStack{

    public static final String THROW_EXCEPTION_ON_FAILURE = MapValueStack.class.getName() + ".throwExceptionOnFailure";
    private Map _context;
    
    public MapValueStack(){
    	_context = new HashMap();
    }
    
    public MapValueStack(Map context){
    	_context = context;
    }
    
	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.util.ValueStack#findString(java.lang.String)
	 */
	public String findString(String expr) {
		// TODO Auto-generated method stub
		return (String)_context.get(expr);
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.util.ValueStack#findString(java.lang.String, boolean)
	 */
	public String findString(String expr, boolean throwExceptionOnFailure) {
		// TODO Auto-generated method stub
		return findString(expr);
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.util.ValueStack#findValue(java.lang.String)
	 */
	public Object findValue(String expr) {
		// TODO Auto-generated method stub
		return _context.get(expr);
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.util.ValueStack#findValue(java.lang.String, boolean)
	 */
	public Object findValue(String expr, boolean throwExceptionOnFailure) {
		// TODO Auto-generated method stub
		return findValue(expr);
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.util.ValueStack#findValue(java.lang.String, java.lang.Class)
	 */
	public Object findValue(String expr, Class asType) {
		// TODO Auto-generated method stub
		return findValue(expr);
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.util.ValueStack#findValue(java.lang.String, java.lang.Class, boolean)
	 */
	public Object findValue(String expr, Class asType,
			boolean throwExceptionOnFailure) {
		// TODO Auto-generated method stub
		return findValue(expr);
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.util.ValueStack#getContext()
	 */
	public Map<String, Object> getContext() {
		// TODO Auto-generated method stub
		return _context;
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.util.ValueStack#getExprOverrides()
	 */
	public Map<Object, Object> getExprOverrides() {
		// TODO Auto-generated method stub
		return _context;
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.util.ValueStack#getRoot()
	 */
	public CompoundRoot getRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.util.ValueStack#peek()
	 */
	public Object peek() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.util.ValueStack#pop()
	 */
	public Object pop() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.util.ValueStack#push(java.lang.Object)
	 */
	public void push(Object o) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.util.ValueStack#set(java.lang.String, java.lang.Object)
	 */
	public void set(String key, Object o) {
		// TODO Auto-generated method stub
		_context.put(key, o);
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.util.ValueStack#setDefaultType(java.lang.Class)
	 */
	public void setDefaultType(Class defaultType) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.util.ValueStack#setExprOverrides(java.util.Map)
	 */
	public void setExprOverrides(Map<Object, Object> overrides) {
		// TODO Auto-generated method stub
		_context.putAll(overrides);
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.util.ValueStack#setValue(java.lang.String, java.lang.Object)
	 */
	public void setValue(String expr, Object value) {
		// TODO Auto-generated method stub
		set(expr,value);
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.util.ValueStack#setValue(java.lang.String, java.lang.Object, boolean)
	 */
	public void setValue(String expr, Object value,
			boolean throwExceptionOnFailure) {
		// TODO Auto-generated method stub
		set(expr,value);
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.util.ValueStack#size()
	 */
	public int size() {
		// TODO Auto-generated method stub
		return _context.size();
	}

	/* (non-Javadoc)
	 * @see com.opensymphony.xwork2.util.ClearableValueStack#clearContextValues()
	 */
	public void clearContextValues() {
		// TODO Auto-generated method stub
		_context.clear();
	}
    

}
