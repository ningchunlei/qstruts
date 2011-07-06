/**
 * ClassName:AutoConfig.java
 * Authoer:ningcl
 * Date:2011-1-19 
 */
package org.xz.qstruts.annotation.tld;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;


import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import org.apache.tools.ant.Task;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.xz.qstruts.utils.StringHelp;


/**
 * @author ningcl
 * @version 1.0 
 */
public class AutoConfig extends Task{
	private String tldFile = "";
	private String base = "";
	private String shortName = "";
	private String uri = "";
	private String version = "";
	
	public void setTldFile(String file){
		tldFile = file;
	}
	
	public void setBase(String base){
		this.base = base;
	}
	
	public void setShortName(String shortName){
		this.shortName = shortName;
	}
	
	public void setUri(String uri){
		this.uri = uri;
	}
	
	public void setVersion(String version){
		this.version = version;
	}
	
	public void execute(){
		try{
			
			GenericApplicationContext  context = new GenericApplicationContext();
			PathMatchingResourcePatternResolver pp = new PathMatchingResourcePatternResolver(getClass().getClassLoader());
			context.setResourceLoader(pp);
	    	ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(context, true);
	    	scanner.addIncludeFilter(new AnnotationTypeFilter(StrutsTag.class));
	    	Set<BeanDefinition> sets = scanner.findCandidateComponents("");
	    	
	    
	    	
	    	ArrayList<Tag> taglist = new ArrayList<Tag>();
	    	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tldFile),"UTF-8"));
	    	for(Iterator<BeanDefinition> it=sets.iterator();it.hasNext();){
	    		BeanDefinition bean = it.next();
	    		try {
	    			Class tagClazz = Class.forName(bean.getBeanClassName());
	    			StrutsTag strutsTag = (StrutsTag)tagClazz.getAnnotation(StrutsTag.class);
	    			Tag tag = new Tag();
	    			tag.setDescription(strutsTag.description());
	    			tag.setName(strutsTag.name());
	    			tag.setTagclazz(strutsTag.tldTagClass());
	    			tag.setBody(strutsTag.tldBodyContent());
	    			tag.setDyAttribute(strutsTag.allowDynamicAttributes());
	    			
	    			Method[] methods = tagClazz.getMethods();
	    			for(int i=0;i<methods.length;i++){
	    				StrutsTagAttribute sta = (StrutsTagAttribute)methods[i].getAnnotation(StrutsTagAttribute.class);
	    				if(sta!=null){
	    					TagAttribute tagattr = new TagAttribute();
	    					tagattr.setDescription(sta.description());
	    					String mth = methods[i].getName();
	    					mth = mth.replaceFirst("set", "");
	    					mth = StringHelp.firstLetterToLowerCase(mth);
	    					tagattr.setName(mth);
	    					tagattr.setRequired(sta.required());
	    					tagattr.setRtexprvalue(sta.rtexprvalue());
	    					tag.getAttributes().add(tagattr);
	    				}
	    			}
	    			taglist.add(tag);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
	    	Properties p = new Properties();
	        applyDefaultConfiguration(p);
	        VelocityEngine velocityEngine = new VelocityEngine();
	        velocityEngine.init(p);
	        VelocityContext vc = new VelocityContext();
	        vc.put("taglist", taglist);
	        vc.put("version", version);
	        vc.put("shortname", shortName);
	        vc.put("uri", uri);
			
	        Template t = velocityEngine.getTemplate("org/xz/qstruts/annotation/tld/autoconfig-tld.vm");
	        t.merge(vc, writer);
	        writer.close();
		}catch(Exception e){
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		
	}
	
	private void applyDefaultConfiguration(Properties p) {
        // ensure that caching isn't overly aggressive

        /**
         * Load a default resource loader definition if there isn't one present.
         * Ben Hall (22/08/2003)
         */
        
        p.setProperty(Velocity.RESOURCE_LOADER, "strutsclass");
        

                /**
         * Refactored the Velocity templates for the Struts taglib into the classpath from the web path.  This will
         * enable Struts projects to have access to the templates by simply including the Struts jar file.
         * Unfortunately, there does not appear to be a macro for the class loader keywords
         * Matt Ho - Mon Mar 17 00:21:46 PST 2003
         */
        p.setProperty("strutsclass.resource.loader.description", "Velocity Classpath Resource Loader");
        p.setProperty("strutsclass.resource.loader.class", "org.apache.struts2.views.velocity.StrutsResourceLoader");
        p.setProperty("strutsclass.resource.loader.modificationCheckInterval", "2");
        p.setProperty("strutsclass.resource.loader.cache", "true");

    }
	
	public static final class Tag{
		
		public String description = "";
		public String name = "";
		public String tagclazz = "";
		public String body = "";
		public boolean dyAttribute = false;
		public ArrayList attributes = new ArrayList();
		
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getTagclazz() {
			return tagclazz;
		}
		public void setTagclazz(String tagclazz) {
			this.tagclazz = tagclazz;
		}
		public String getBody() {
			return body;
		}
		public void setBody(String body) {
			this.body = body;
		}
		public boolean isDyAttribute() {
			return dyAttribute;
		}
		public void setDyAttribute(boolean dyAttribute) {
			this.dyAttribute = dyAttribute;
		}
		public ArrayList getAttributes() {
			return attributes;
		}
		public void setAttributes(ArrayList attributes) {
			this.attributes = attributes;
		}
		
		
	}
	
	public static final class TagAttribute{
		public String description = "";
		public String name = "";
		public boolean required = false;
		public boolean rtexprvalue = false;
		
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public boolean isRequired() {
			return required;
		}
		public void setRequired(boolean required) {
			this.required = required;
		}
		public boolean isRtexprvalue() {
			return rtexprvalue;
		}
		public void setRtexprvalue(boolean rtexprvalue) {
			this.rtexprvalue = rtexprvalue;
		}
		
	}
	
	public static void main(String[] args){
		AutoConfig config = new AutoConfig();
		config.setBase("");
		config.setShortName("s");
		config.setUri("/tag");
		config.setVersion("2.2");
		config.setTldFile("c:/a.tld");
		config.execute();
	}
	
}
