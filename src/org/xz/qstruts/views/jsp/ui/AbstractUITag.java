/*
 * $Id: AbstractUITag.java,v 1.2 2011/02/15 09:05:05 cvs_ningcl Exp $
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

package org.xz.qstruts.views.jsp.ui;

import org.xz.qstruts.components.UIBean;
import org.xz.qstruts.views.jsp.ComponentTagSupport;


/**
 * Abstract base class for all UI tags.
 *
 */
public abstract class AbstractUITag extends ComponentTagSupport {
    
	protected String cssClass; 
	protected String titleClass;
	protected String template;
	
    protected void populateParams() {
        super.populateParams();
        UIBean uiBean = (UIBean) component;
        uiBean.setCssClass(cssClass);
        uiBean.setTitleClass(titleClass);
    }

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public String getTitleClass() {
		return titleClass;
	}

	public void setTitleClass(String titleClass) {
		this.titleClass = titleClass;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

}
