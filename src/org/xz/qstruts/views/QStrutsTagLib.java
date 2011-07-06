/**
 * ClassName:QStrutsTagLib.java
 * Authoer:ningcl
 * Date:2011-1-27 
 */
package org.xz.qstruts.views;

import java.util.Arrays;
import java.util.List;

import org.apache.struts2.views.DefaultTagLibrary;
import org.xz.qstruts.components.ModuleExport;
import org.xz.qstruts.components.ModuleImport;
import org.xz.qstruts.views.velocity.components.ModuleDevDirective;
import org.xz.qstruts.views.velocity.components.ModuleExportDirective;
import org.xz.qstruts.views.velocity.components.ModuleImportDirective;
import org.xz.qstruts.views.velocity.components.ModuleLoaderDirective;
import org.xz.qstruts.views.velocity.components.ModuleNameDirective;
import org.xz.qstruts.views.velocity.components.ModuleUrlRewriteDirective;

/**
 * @author ningcl
 * @version 1.0 
 */
public class QStrutsTagLib extends DefaultTagLibrary{

	/* (non-Javadoc)
	 * @see org.apache.struts2.views.TagLibrary#getVelocityDirectiveClasses()
	 */
	public List<Class> getVelocityDirectiveClasses() {
		// TODO Auto-generated method stub
		 Class[] directives = new Class[] {
				 ModuleLoaderDirective.class,
				 ModuleDevDirective.class,
				 ModuleNameDirective.class,
				 ModuleImportDirective.class,
				 ModuleExportDirective.class,
				 ModuleUrlRewriteDirective.class
	      };
		    return Arrays.asList(directives);
	}

}
