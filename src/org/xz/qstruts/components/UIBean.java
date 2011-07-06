/*
 * $Id: UIBean.java,v 1.3 2011/02/15 09:05:05 cvs_ningcl Exp $
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

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.struts2.StrutsException;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import org.xz.qstruts.components.template.Template;
import org.xz.qstruts.components.template.TemplateEngine;
import org.xz.qstruts.components.template.TemplateEngineManager;
import org.xz.qstruts.components.template.TemplateRenderingContext;

import com.opensymphony.xwork2.config.ConfigurationException;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;


public abstract class UIBean extends Component {
    private static final Logger LOG = LoggerFactory.getLogger(UIBean.class);
    
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    
    protected String template;
    
    protected String cssClass;
    protected String titleClass;
    

    public UIBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack);
        this.request = request;
        this.response = response;
    }

    // templateDir and theme attributes
    protected TemplateEngineManager templateEngineManager;

    @Inject
    public void setTemplateEngineManager(TemplateEngineManager mgr) {
        this.templateEngineManager = mgr;
    }

    public boolean end(Writer writer, String body) {
        try {
            super.end(writer, body, false);
            mergeTemplate(writer, buildTemplateName(getDefaultTemplate()));
        } catch (Exception e) {
            throw new StrutsException(e);
        }
        return false;
    }

    /**
     * A contract that requires each concrete UI Tag to specify which template should be used as a default.  For
     * example, the CheckboxTab might return "checkbox.vm" while the RadioTag might return "radio.vm".  This value
     * <strong>not</strong> begin with a '/' unless you intend to make the path absolute rather than relative to the
     * current theme.
     *
     * @return The name of the template to be used as the default.
     */
    protected abstract String getDefaultTemplate();
    
    public abstract String getComponentName();

    protected Template buildTemplateName(String myDefaultTemplate) {
        String template = myDefaultTemplate;
        String templateDir = getTemplateDir();
        return new Template(templateDir, template);
    }

    protected void mergeTemplate(Writer writer, Template template) throws Exception {
    	int index = -1;
        final TemplateEngine engine = templateEngineManager.getTemplateEngine(template);
        if (engine == null) {
            throw new ConfigurationException("Unable to find a TemplateEngine for template " + template);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Rendering template " + template);
        }

        final TemplateRenderingContext context = new TemplateRenderingContext(template, writer, getStack(),null, this);
        engine.renderTemplate(context);
    }

    public String getTemplateDir() {
        return getComponentModuleName();
    }
    
    @StrutsTagAttribute(description="The css class to use for element")
    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }
    
    @StrutsTagAttribute(description = "The css class to use for title")
    public void setTitleClass(String titleClass){
    	this.titleClass = titleClass;
    }

	public String getCssClass() {
		return cssClass;
	}

	public String getTitleClass() {
		return titleClass;
	}
    
	public String getTemplate() {
        return template;
    }

    @StrutsTagAttribute(description="The template (other than default) to use for rendering the element")
    public void setTemplate(String template) {
        this.template = template;
    }
    
}
