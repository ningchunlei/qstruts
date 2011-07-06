/**
 * ClassName:ModuleImportDirective.java
 * Authoer:ningcl
 * Date:2011-2-17 
 */
package org.xz.qstruts.views.velocity.components;

import org.xz.qstruts.components.Component;
import org.xz.qstruts.components.ModuleImport;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author ningcl
 * @version 1.0 
 */
public class ModuleImportDirective extends AbstractDirective{
	
	/* (non-Javadoc)
	 * @see org.xz.qstruts.views.velocity.components.AbstractDirective#getBean(com.opensymphony.xwork2.util.ValueStack)
	 */
	@Override
	protected Component getBean(ValueStack stack) {
		// TODO Auto-generated method stub
		return new ModuleImport(stack);
	}

	/* (non-Javadoc)
	 * @see org.xz.qstruts.views.velocity.components.AbstractDirective#getBeanName()
	 */
	@Override
	public String getBeanName() {
		// TODO Auto-generated method stub
		return "ModuleImport";
	}
	
}
