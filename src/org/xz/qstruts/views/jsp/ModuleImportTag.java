/**
 * ClassName:ModuleImport.java
 * Authoer:ningcl
 * Date:2011-2-17 
 */
package org.xz.qstruts.views.jsp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.xz.qstruts.components.Component;
import org.xz.qstruts.components.ModuleImport;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author ningcl
 * @version 1.0 
 */
public class ModuleImportTag  extends ComponentTagSupport{
	
	private String type="";
	private String src="";	
	private String module="";
	
	/* (non-Javadoc)
	 * @see org.xz.qstruts.views.jsp.ComponentTagSupport#getBean(com.opensymphony.xwork2.util.ValueStack, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public Component getBean(ValueStack stack, HttpServletRequest req,
			HttpServletResponse res) {
		// TODO Auto-generated method stub
		return new ModuleImport(stack);
	}
	
	protected void populateParams() {
		// TODO Auto-generated method stub
		super.populateParams();
		((ModuleImport)component).setModule(module);
		((ModuleImport)component).setSrc(src);
		((ModuleImport)component).setType(type);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}
	
}
