package org.xz.qstruts.views.velocity.components;

import org.xz.qstruts.components.Component;
import org.xz.qstruts.components.ModuleUrlRewrite;

import com.opensymphony.xwork2.util.ValueStack;

public class ModuleUrlRewriteDirective extends AbstractDirective{

	/* (non-Javadoc)
	 * @see org.xz.qstruts.views.velocity.components.AbstractDirective#getBean(com.opensymphony.xwork2.util.ValueStack)
	 */
	@Override
	protected Component getBean(ValueStack stack) {
		// TODO Auto-generated method stub
		return new ModuleUrlRewrite(stack);
	}

	/* (non-Javadoc)
	 * @see org.xz.qstruts.views.velocity.components.AbstractDirective#getBeanName()
	 */
	@Override
	public String getBeanName() {
		// TODO Auto-generated method stub
		return "ModuleUrl";
	}
	
}
