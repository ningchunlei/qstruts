/**
 * ClassName:ModuleImport.java
 * Authoer:ningcl
 * Date:2011-2-17 
 */
package org.xz.qstruts.components;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import org.xz.qxork2.ActionContext;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author ningcl
 * @version 1.0 
 */
@StrutsTag(name="moduleImport", tldTagClass="org.xz.qstruts.views.jsp.ModuleImportTag", description="ModuleImport tag",tldBodyContent="empty")
public class ModuleImport extends Component{
	
	public static final String IMPORT=ActionContext.APPOBJECT+"__IMPORT";
	public static final String HEAD = "head";
	public static final String TAIL = "tail";
	
	private String type="";
	private String src="";
	private String module="";
	private String position="";
	
	/**
	 * @param stack
	 */
	public ModuleImport(ValueStack stack) {
		super(stack);
		// TODO Auto-generated constructor stub
	}
	
	public boolean end(Writer writer, String body) {
		List list = (List)((HttpServletRequest) stack.getContext().get(ActionContext.HTTP_REQUEST)).getAttribute(IMPORT);
		if(list==null){
			synchronized(ModuleImport.class){
				if((list=(List)((HttpServletRequest) stack.getContext().get(ActionContext.HTTP_REQUEST)).getAttribute(IMPORT))==null){
					list = new ArrayList();
					((HttpServletRequest) stack.getContext().get(ActionContext.HTTP_REQUEST)).setAttribute(IMPORT, list);
				}
			}
		}
		list.add(this);
		return true;
	}
	
	public void copyParams(Map params) {
		setModule((String)params.get("module"));
		setSrc((String)params.get("src"));
		setType((String)params.get("type"));
		setPosition((String)params.get("position"));
    }
	
	public String getType() {
		return type;
	}
	
	@StrutsTagAttribute(description = "type:css or js", defaultValue = "",required=true)
	public void setType(String type) {
		this.type = type;
	}
	
	public String getSrc() {
		return src;
	}
	
	@StrutsTagAttribute(description = "file name", defaultValue = "",required=true)
	public void setSrc(String src) {
		this.src = src;
	}

	public String getModule() {
		return module;
	}
	
	@StrutsTagAttribute(description = "module name", defaultValue = "",required=true)
	public void setModule(String module) {
		this.module = module;
	}

	public String getPosition() {
		return position;
	}

	@StrutsTagAttribute(description = "where it is inserted", defaultValue = "")
	public void setPosition(String position) {
		if(StringUtils.isEmpty(position)){
			position = HEAD;
		}
		this.position = position;
	}
	
}
