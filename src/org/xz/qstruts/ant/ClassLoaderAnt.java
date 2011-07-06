/**
 * ClassName:ClassLoaderAnt.java
 * Authoer:ningcl
 * Date:2011-2-23 
 */
package org.xz.qstruts.ant;

import java.util.Iterator;
import java.util.Set;

import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.tools.ant.Task;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.xz.qstruts.annotation.Namespace;
import org.xz.qstruts.classloader.QStrutsClassLoader;

/**
 * @author ningcl
 * @version 1.0 
 */
public class ClassLoaderAnt extends Task{
	
	public void execute(){
		GenericApplicationContext  context = new GenericApplicationContext();
		PathMatchingResourcePatternResolver pp = new PathMatchingResourcePatternResolver(getClass().getClassLoader());
		context.setResourceLoader(pp);
    	ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(context, true);
    	scanner.addIncludeFilter(new AnnotationTypeFilter(Namespace.class));
    	Set<BeanDefinition> sets = scanner.findCandidateComponents("");    	
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
	}
	
}
