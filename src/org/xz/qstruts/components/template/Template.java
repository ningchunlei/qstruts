/*
 * $Id: Template.java,v 1.3 2011/04/18 03:50:28 cvs_ningcl Exp $
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

import java.util.ArrayList;
import java.util.List;

/**
 * A template.
 * <p/>
 * A template is used as a model for rendering output.
 * This object contains basic common template information
 */
public class Template implements Cloneable {
    String dir;
    String name;
    String prefix="/modules/components";
    
    public Template(String prefix,String dir,String name){
    	this(dir,name);
    	this.prefix = prefix;
    }
    
    /**
     * Constructor.
     *
     * @param dir  base folder where the template is stored.
     * @param theme  the theme of the template
     * @param name   the name of the template.
     */
    public Template(String dir, String name) {
        this.dir = dir;
        this.name = name;
    }

    public String getDir() {
        return dir;
    }

    public String getName() {
        return name;
    }

    public List<Template> getPossibleTemplates(TemplateEngine engine) {
        List<Template> list = new ArrayList<Template>(3);
        Template template = this;
        list.add(template);
        return list;
    }

    /**
     * Constructs a string in the format <code>/dir/theme/name</code>.
     * @return a string in the format <code>/dir/theme/name</code>.
     */
    public String toString() {
        return prefix+"/" + dir + "/" + name;
    }

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
