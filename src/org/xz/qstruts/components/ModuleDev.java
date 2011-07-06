/**
 * ClassName:ModuleDev.java
 * Authoer:ningcl
 * Date:2011-1-19 
 */
package org.xz.qstruts.components;

import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import org.xz.qstruts.dispatcher.Dispatcher;
import org.xz.qxork2.ActionContext;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author ningcl
 * @version 1.0 
 */
@StrutsTag(name="moduledev", tldTagClass="org.xz.qstruts.views.jsp.ModuleDevTag", description="ModuelDev tag")
public class ModuleDev extends Component{
		
	/**
	 * @param stack
	 */
	public ModuleDev(ValueStack stack) {
		super(stack);
		// TODO Auto-generated constructor stub
	}
	
	public boolean start(Writer writer) {
        return Dispatcher.getInstance().isModuleDev();
    }

    public boolean end(Writer writer, String body) {
        return super.end(writer, body);
    }
    
}
