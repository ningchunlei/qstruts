/**
 * ClassName:ModuleExportDirective.java
 * Authoer:ningcl
 * Date:2011-2-17 
 */
package org.xz.qstruts.views.velocity.components;

import org.xz.qstruts.components.Component;
import org.xz.qstruts.components.ModuleExport;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author ningcl
 * @version 1.0 
 */
public class ModuleExportDirective extends AbstractDirective{
	
	public int getType() {
        return BLOCK;
    }
	
	/* (non-Javadoc)
	 * @see org.xz.qstruts.views.velocity.components.AbstractDirective#getBean(com.opensymphony.xwork2.util.ValueStack)
	 */
	@Override
	protected Component getBean(ValueStack stack) {
		// TODO Auto-generated method stub
		return new ModuleExport(stack);
	}

	/* (non-Javadoc)
	 * @see org.xz.qstruts.views.velocity.components.AbstractDirective#getBeanName()
	 */
	@Override
	public String getBeanName() {
		// TODO Auto-generated method stub
		return "ModuleExport";
	}
	
}
