/**
 * ClassName:ModuleLoader.java
 * Authoer:ningcl
 * Date:2011-1-26 
 */
package org.xz.qstruts.components;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.StrutsException;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import org.xz.qstruts.dispatcher.Dispatcher;
import org.xz.qxork2.ActionContext;
import org.xz.qxork2.ModuleRequest;
import org.xz.qxork2.ModuleResponse;
import org.xz.qxork2.validator.Utils;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author ningcl
 * @version 1.0 
 */
@StrutsTag(name="moduleloader", tldTagClass="org.xz.qstruts.views.jsp.ModuleLoaderTag", description="ModuelLoader tag",tldBodyContent="empty")
public class ModuleLoader extends Component{
	
	private String moduleName="";
	private String parameters = "";
	/**
	 * @param stack
	 */
	public ModuleLoader(ValueStack stack) {
		super(stack);
		// TODO Auto-generated constructor stub
	}
	
	public boolean end(Writer writer, String body) {
		String[] splits = null;
		String mod = null;
		if(moduleName !=null && (mod=(String)ActionContext.getHttpRequest(stack.getContext()).getAttribute(moduleName))!=null){
			body = mod;
		}else if(moduleName!=null && (splits=moduleName.split("\\.")).length>=3){
			ModuleRequest request = new ModuleRequest(ActionContext.getHttpRequest(stack.getContext()));
			HttpServletResponse parent = ActionContext.getHttpResponse(stack.getContext());
			ModuleResponse response = new ModuleResponse(parent);
			try {
				Map map = makeEatraParametes(request,parameters);
				Dispatcher.getInstance().serviceAction(request, response,
						ActionContext.getServletContext(stack.getContext()), Utils.makeActionMapping(splits, map));
				body = (String) ActionContext.getResponseContent(response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new StrutsException(e);
			} 
		}
		return super.end(writer, body);
	}
	
	public Map makeEatraParametes(ModuleRequest req,String parameters){
		if(StringUtils.isEmpty(parameters)){
			return null;
		}
		parameters = parameters.trim();
		if(!parameters.startsWith("{") || !parameters.endsWith("}")){
			return null;
		}
		String name="";
		String value="";
		int start=0;
		HashMap map = new HashMap();
		for(int i=1;i<parameters.length();i++){
			char ch = parameters.charAt(i);
			if(ch==':'){
				name = parameters.substring(start+1,i);
				start = i;
			}else if(ch==',' || ch=='}'){
				value = parameters.substring(start+1,i);
				if(value.startsWith("$")){
					map.put(name, req.getParameter(value));
				}else if(value.startsWith("#")){
					map.put(name, req.getAttribute(value));
				}else{
					map.put(name, value);
				}
				start = i;
			}
		}
		return map;
	}
	
	public void copyParams(Map params) {
		setModuleName((String)params.get("moduleName"));
		setParameters((String)params.get("parameters"));
    }
	
	@StrutsTagAttribute(description = "service module", defaultValue = "",required=true)
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	
	@StrutsTagAttribute(description = "extra parameters", defaultValue = "",required=false)
	public void setParameters(String parameters){
		this.parameters = parameters;
	}
}
