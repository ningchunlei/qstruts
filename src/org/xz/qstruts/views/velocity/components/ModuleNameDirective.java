/**
 * ClassName:ModuleNameDirective.java
 * Authoer:ningcl
 * Date:2011-1-27 
 */
package org.xz.qstruts.views.velocity.components;

import org.xz.qstruts.components.Component;
import org.xz.qstruts.components.ModuleName;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author ningcl
 * @version 1.0 
 */
public class ModuleNameDirective extends AbstractDirective{

	protected Component getBean(ValueStack stack) {
		// TODO Auto-generated method stub
		return new ModuleName(stack);
	}

	public String getBeanName() {
		// TODO Auto-generated method stub
		return "ModuleName";
	}
	
}
