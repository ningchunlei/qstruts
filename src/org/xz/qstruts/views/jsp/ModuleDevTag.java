/**
 * ClassName:ModuleDevTag.java
 * Authoer:ningcl
 * Date:2011-1-19 
 */
package org.xz.qstruts.views.jsp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xz.qstruts.components.Component;
import org.xz.qstruts.components.ModuleDev;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author ningcl
 * @version 1.0 
 */
public class ModuleDevTag extends ComponentTagSupport{

	/* (non-Javadoc)
	 * @see org.xz.qstruts.views.jsp.ComponentTagSupport#getBean(com.opensymphony.xwork2.util.ValueStack, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req,
			HttpServletResponse res) {
		// TODO Auto-generated method stub
		return new ModuleDev(stack);
	}
	
	
}
