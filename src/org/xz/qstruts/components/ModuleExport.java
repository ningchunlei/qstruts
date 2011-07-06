/**
 * ClassName:ModuleExport.java
 * Authoer:ningcl
 * Date:2011-2-17 
 */
package org.xz.qstruts.components;

import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import org.xz.qxork2.ActionContext;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * @author ningcl
 * @version 1.0 
 */
public class ModuleExport extends Component{
	
	private String position="";
	
	/**
	 * @param stack
	 */
	public ModuleExport(ValueStack stack) {
		super(stack);
	}
	
	public boolean end(Writer writer, String body) {
		List list = (List)((HttpServletRequest) stack.getContext().get(ActionContext.HTTP_REQUEST)).getAttribute(ModuleImport.IMPORT);
		if(list!=null){
			StringBuilder sb = new StringBuilder("<script>BH.load([");
			HashMap map = new HashMap();
			for(int i=0;i<list.size();i++){
				ModuleImport bean = (ModuleImport)list.get(i);
				if(!position.equals(bean.getPosition())){
					continue;
				}
				String key = bean.getModule()+","+bean.getType()+","+bean.getSrc();
				if(map.get(key)!=null){
					continue;
				}
				map.put(key, "");
				sb.append("{appid:\"").append(bean.getModule()).append("\",")
				.append("src:\"").append(bean.getSrc()).append("\"},");
				
			}
			if(sb.charAt(sb.length()-1)==','){
				sb.deleteCharAt(sb.length()-1);
			}
			sb.append("]);</script>");
			sb.append(body);
			super.end(writer, sb.toString());
		}else{
			super.end(writer, body);
		}
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.xz.qstruts.components.Component#copyParams(java.util.Map)
	 */
	@Override
	public void copyParams(Map params) {
		// TODO Auto-generated method stub
		setPosition((String)params.get("position"));
	}
	
	/* (non-Javadoc)
	 * @see org.xz.qstruts.components.Component#usesBody()
	 */
	@Override
	public boolean usesBody() {
		// TODO Auto-generated method stub
		return true;
	}

	public String getPosition() {
		return position;
	}
	
	@StrutsTagAttribute(description = "where it is inserted", defaultValue = "")
	public void setPosition(String position) {
		if(StringUtils.isEmpty(position)){
			position = ModuleImport.HEAD;
		}
		this.position = position;
	}

}
