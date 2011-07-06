/**
 * ClassName:ModuleDevDirective.java
 * Authoer:ningcl
 * Date:2011-1-27 
 */
package org.xz.qstruts.views.velocity.components;

import org.xz.qstruts.components.Component;
import org.xz.qstruts.components.ModuleDev;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author ningcl
 * @version 1.0 
 */
public class ModuleDevDirective extends AbstractDirective{

	/* (non-Javadoc)
	 * @see org.xz.qstruts.views.velocity.components.AbstractDirective#getBean(com.opensymphony.xwork2.util.ValueStack)
	 */
	@Override
	protected Component getBean(ValueStack stack) {
		// TODO Auto-generated method stub
		return new ModuleDev(stack);
	}

	public String getBeanName() {
		// TODO Auto-generated method stub
		return "ModuleDev";
	}
	
	
	
}
