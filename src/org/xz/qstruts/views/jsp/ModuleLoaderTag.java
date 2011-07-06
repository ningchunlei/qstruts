/**
 * ClassName:ModuleLoaderTag.java
 * Authoer:ningcl
 * Date:2011-1-27 
 */
package org.xz.qstruts.views.jsp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xz.qstruts.components.Component;
import org.xz.qstruts.components.ModuleLoader;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author ningcl
 * @version 1.0 
 */
public class ModuleLoaderTag  extends ComponentTagSupport {
	
	private String moduleName = "";
	
	/* (non-Javadoc)
	 * @see org.xz.qstruts.views.jsp.ComponentTagSupport#getBean(com.opensymphony.xwork2.util.ValueStack, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req,
			HttpServletResponse res) {
		// TODO Auto-generated method stub
		return new ModuleLoader(stack);
	}
	
	protected void populateParams() {
		// TODO Auto-generated method stub
		super.populateParams();
		((ModuleLoader)component).setModuleName(moduleName);
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
}
