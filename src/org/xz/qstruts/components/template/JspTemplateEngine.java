/*
 * $Id: JspTemplateEngine.java,v 1.3 2011/02/15 09:05:06 cvs_ningcl Exp $
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

package org.xz.qstruts.components.template;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.struts2.components.Include;
import org.xz.qstruts.components.UIBean;
import org.xz.qstruts.dispatcher.Dispatcher;
import org.xz.qstruts.views.jsp.TagUtils;
import org.xz.qxork2.ActionContext;

import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;

/**
 * JSP based template engine.
 */
public class JspTemplateEngine extends BaseTemplateEngine {
    private static final Logger LOG = LoggerFactory.getLogger(JspTemplateEngine.class);
    
    
    
    public void renderTemplate(TemplateRenderingContext templateContext) throws Exception {
        Template template = templateContext.getTemplate();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Trying to render template " + template + ", repeating through parents until we succeed");
        }
        UIBean tag = templateContext.getTag();
        ValueStack stack = templateContext.getStack();
        PageContext pageContext = (PageContext) stack.getContext().get(ActionContext.PAGE_CONTEXT);
        pageContext.getRequest().setAttribute(tag.getComponentName(), tag);
        List<Template> templates = template.getPossibleTemplates(this);
        Exception exception = null;
        boolean success = false;
        String encoding = Dispatcher.getInstance().getEncoding();
        for (Template t : templates) {
            try {
                TagUtils.include(getFinalTemplateName(t), pageContext.getOut(),
                        pageContext.getRequest(), (HttpServletResponse) pageContext.getResponse(),encoding);
                success = true;
                break;
            } catch (Exception e) {
                if (exception == null) {
                    exception = e;
                }
            }
        }

        if (!success) {
            LOG.error("Could not render JSP template " + templateContext.getTemplate());

            if (exception != null) {
                throw exception;
            } else {
                return;
            }
        }

        stack.pop();
    }

    protected String getSuffix() {
        return "jsp";
    }
}
