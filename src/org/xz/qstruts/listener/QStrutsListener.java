/**
 * ClassName:QStrutsListener.java
 * Authoer:ningcl
 * Date:2010-12-21 
 */
package org.xz.qstruts.listener;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.web.context.support.StaticWebApplicationContext;
import org.xz.qstruts.annotation.Namespace;
import org.xz.qstruts.classloader.QStrutsClassLoader;
import org.xz.qstruts.config.RuntimeProvider;



/**
 * @author ningcl
 * @version 1.0 
 */
public class QStrutsListener implements ServletContextListener {
   
	HashMap<String,List> map = new HashMap();
	
    public void contextInitialized(ServletContextEvent sce) {
    	StaticWebApplicationContext context = new StaticWebApplicationContext();
    	context.setServletContext(sce.getServletContext());
    	ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(context, true);
    	scanner.addIncludeFilter(new AnnotationTypeFilter(Namespace.class));
    	Set<BeanDefinition> sets = scanner.findCandidateComponents("mod");
    	QStrutsClassLoader loader = new QStrutsClassLoader(getClass().getClassLoader());
    	for(Iterator<BeanDefinition> it=sets.iterator();it.hasNext();){
    		BeanDefinition bean = it.next();
    		try {
				loader.loadClass(bean.getBeanClassName());
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	ServletContext sc = sce.getServletContext();
    	File dir = new File(sc.getRealPath("modules"));
    	Stack stack = new Stack();
    	registervm(dir,stack);
    	Set set = map.keySet();
    	for(Iterator<String> it=set.iterator();it.hasNext();){
    		String key = it.next();
    		String[] list = key.split("-");
    		RuntimeProvider.addPackageConfig(list[0], list[1], "$v",list[3], map.get(key));
    	}
    }
    
    private void registervm(File dir,Stack stack){
    	if(dir.isDirectory()){
    		File[] files = dir.listFiles(); 
        	for(int i=0;i<files.length;i++){
        		stack.push(files[i].getName());
        		if(files[i].isFile() && (files[i].getName().endsWith(".mvm") || files[i].getName().endsWith(".mjsp"))){
        			registervm(stack);
        		}else if(files[i].isDirectory()){
        			registervm(files[i], stack);
        		}
        		stack.pop();
        	}
    	}
    }
    
    private void registervm(Stack<String> stack){
    	StringBuilder sb = new StringBuilder(100);
    	String groupid = (String)stack.get(0);
    	for(int i=1;i<stack.size()-2;i++){
    		sb.append("/").append(stack.get(i));
    	}
    	String appid=sb.substring(1);
    	String clazz = stack.get(stack.size()-2);
    	String method = stack.peek();
    	method = method.substring(0,method.indexOf("."));
    	
    	String key = groupid+"-"+appid+"-$v-"+clazz;
    	List<String[]> list = map.get(key); 
    	if(list==null){
    		list = new ArrayList<String[]>();
    		map.put(key, list);
    	}
    	list.add(new String[]{method,method});
    	//RuntimeProvider.addPackageConfig(groupid, appid, "$v",clazz, list);
    }
    
    public void contextDestroyed(ServletContextEvent sce) {
        	
    }
}