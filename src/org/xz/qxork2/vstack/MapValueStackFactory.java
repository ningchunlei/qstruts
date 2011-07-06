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

import java.util.Map;
import java.util.Set;

import org.xz.qxork2.ActionContext;

import com.opensymphony.xwork2.inject.Container;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.ValueStackFactory;

/**
 * Creates an Ognl value stack
 */
public class MapValueStackFactory implements ValueStackFactory {
	
    private Container container;
	
    public ValueStack createValueStack() {
    	MapValueStack stack = new MapValueStack();
        stack.getContext().put(ActionContext.CONTAINER, container);
        return stack;
    }

    public ValueStack createValueStack(ValueStack stack) {
        ValueStack result = new MapValueStack(stack.getContext());
        stack.getContext().put(ActionContext.CONTAINER, container);
        return result;
    }
    
    @Inject
    public void setContainer(Container container) throws ClassNotFoundException {
        this.container = container;
    }
}
