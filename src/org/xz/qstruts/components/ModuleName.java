/**
 * ClassName:ModuleName.java
 * Authoer:ningcl
 * Date:2011-1-19 
 */
package org.xz.qstruts.components;

import java.io.Writer;
import java.util.Map;

import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import org.xz.qxork2.ActionContext;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author ningcl
 * @version 1.0 
 */
@StrutsTag(name="modulename", tldTagClass="org.xz.qstruts.views.jsp.ModuleNameTag", description="ModuelName tag",tldBodyContent="empty")
public class ModuleName extends Component{
	
	private String var = "";
	
	/**
	 * @param stack
	 */
	public ModuleName(ValueStack stack) {
		super(stack);
		// TODO Auto-generated constructor stub
	}
	
	 public boolean end(Writer writer, String body) {
		 ActionContext.getHttpRequest(stack.getContext()).setAttribute(var, ActionContext.getModuleName(stack.getContext()));
		 return super.end(writer, body);
	 }
	 
	 public void copyParams(Map params) {
		 setVar((String)params.get("var"));
	 }

	 
    @StrutsTagAttribute(description = "set httpattribute using varname", defaultValue = "",required=true) 
	public void setVar(String var) {
		this.var = var;
	}
	 
	 
	 
}
